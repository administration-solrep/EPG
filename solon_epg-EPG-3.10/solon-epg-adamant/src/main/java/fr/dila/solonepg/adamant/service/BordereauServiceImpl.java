package fr.dila.solonepg.adamant.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharEncoding;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.core.persistence.PersistenceProvider;
import org.nuxeo.ecm.core.persistence.PersistenceProviderFactory;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunCallback;
import org.nuxeo.runtime.api.Framework;

import fr.dila.solonepg.adamant.SolonEpgAdamantConstant;
import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.adamant.model.BordereauPK;
import fr.dila.solonepg.adamant.model.DossierExtractionBordereau;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.core.dto.DossierArchivageStatDTO;
import fr.dila.solonepg.core.dto.DossierArchivageStatDTOImpl;
import fr.dila.solonepg.core.service.DossierServiceImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.SHA512Util;

public class BordereauServiceImpl implements BordereauService {
	
	private static volatile PersistenceProvider	persistenceProvider;
	
	@Override
	public DossierExtractionBordereau initBordereau(Dossier dossier, DossierExtractionLot lot) {
		BordereauPK bordereauPK = new BordereauPK(dossier.getDocument().getId(), lot.getId());
		DossierExtractionBordereau bordereau = new DossierExtractionBordereau();
		bordereau.setBordereauPK(bordereauPK);

		bordereau.setTypeActe(SolonEpgServiceLocator.getActeService().getActe(dossier.getTypeActe()).getLabel());
		bordereau.setStatut(SolonEpgServiceLocator.getVocabularyService()
				.getEntryLabel(VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME, dossier.getStatut()));
		bordereau.setVersion(SolonEpgServiceLocator.getDossierService().isDossierReprise(dossier) ? "V1" : "V2");
		
		bordereau.setExtractionDate(new Date());

		return bordereau;
	}

	@Override
	public void completeBordereau(DossierExtractionBordereau bordereau, File sipFile) throws IOException {
		bordereau.setPoids(sipFile.length());
		bordereau.setEmpreinte(SHA512Util.getSHA512Hash(sipFile));
	}

	@Override
	public void saveBordereau(DossierExtractionBordereau bordereau) throws ClientException {
		SolonEpgAdamantServiceLocator.getDossierExtractionDao().saveBordereau(bordereau);
	}

	/**
	 * Séparateur de cellule du fichier CSV
	 */
	private static final String CSV_CELL_SEP = ",";

	/**
	 * Séparateur de ligne du fichier CSV
	 */
	private static final String CSV_LINE_SEP = "\r\n";

	@Override
	public void generateBordereauFile(CoreSession session, DossierExtractionLot lot, File folder) throws ClientException, IOException {
		// Récupération des informations bordereau de chaque dossier du lot.
		Collection<DossierExtractionBordereau> bordereaux = SolonEpgAdamantServiceLocator.getDossierExtractionDao()
				.getBordereaux(lot);

		StringBuilder builder = new StringBuilder();
		generateBordereauFileHeaders(builder);

		for (DossierExtractionBordereau bordereau : bordereaux) {
			generateBordereauFileLine(builder, bordereau);
		}

		File bordereauFile = new File(
				folder.getAbsolutePath() + SolonEpgAdamantConstant.FILE_SEPARATOR + computeBordereauFilename(session, lot));
		FileUtils.writeStringToFile(bordereauFile, builder.toString(), CharEncoding.UTF_8);
	}

	/**
	 * Génère le nom du fichier bordereau à produire selon le format suivant :
	 * numéro d’entrée SOLON (fourni par les Archives nationales), suivi d’un
	 * underscore et de la date d’extraction au format AAAA-MM-JJ-hh-mm, suivi
	 * d’un underscore et du numéro du lot d’extraction Exemple :
	 * 20190082_2018-04-17-12-30_123.csv
	 * 
	 * @param lot
	 * @return
	 * @throws ClientException 
	 */
	private String computeBordereauFilename(CoreSession session, DossierExtractionLot lot) throws ClientException {
		final DossierExtractionService dossierExtractionService = SolonEpgAdamantServiceLocator.getDossierExtractionService();

		return dossierExtractionService.getNumeroSolon(session) + "_"
				+ DateUtil.simpleDateFormat(SolonEpgAdamantConstant.DATE_FORMAT_FOR_FILES_AND_FOLDERS)
						.format(new Date())
				+ "_" + lot.getName() + ".csv";
	}

	/**
	 * Génération des headers du fichier.
	 * 
	 * @param builder
	 *            StringBuilder à compléter
	 */
	private void generateBordereauFileHeaders(StringBuilder builder) {
		builder.append("Nom").append(CSV_CELL_SEP);
		builder.append("Empreinte").append(CSV_CELL_SEP);
		builder.append("Poids").append(CSV_CELL_SEP);
		builder.append("Type Acte").append(CSV_CELL_SEP);
		builder.append("Statut").append(CSV_CELL_SEP);
		builder.append("Version");

		builder.append(CSV_LINE_SEP);
	}

	/**
	 * Génération d'une ligne du fichier bordereau correspondant à un lot.
	 * 
	 * @param builder
	 *            StringBuilder à compléter
	 * @param bordereau
	 *            un bordereau persisté
	 */
	private void generateBordereauFileLine(StringBuilder builder, DossierExtractionBordereau bordereau) {
		builder.append(bordereau.getNomSip()).append(CSV_CELL_SEP);
		builder.append(bordereau.getEmpreinte()).append(CSV_CELL_SEP);
		builder.append(bordereau.getPoids()).append(CSV_CELL_SEP);
		builder.append(bordereau.getTypeActe()).append(CSV_CELL_SEP);
		builder.append(bordereau.getStatut()).append(CSV_CELL_SEP);
		builder.append(bordereau.getVersion());

		builder.append(CSV_LINE_SEP);
	}
	

	@Override
	public List<DossierArchivageStatDTO> getDossierArchivageStatResultList(final String statutAfter, 
			final String dateDebut, final String dateFin, final Long offset, final Long nbDos, final String sortInfo) throws ClientException {
		return getOrCreatePersistenceProvider().run(true, new RunCallback<List<DossierArchivageStatDTO>>() {
			@Override
			@SuppressWarnings("unchecked")
			public List<DossierArchivageStatDTO> runWith(EntityManager entityManager) throws ClientException {
				final Long limit = offset + nbDos;
				final StringBuilder query = new StringBuilder(
						"SELECT outer.* FROM (SELECT ROWNUM rn, inner.* FROM (SELECT TO_CHAR(d.ID) as id, TO_CHAR(d.NUMERONOR) as nor, "
						+ "TO_CHAR(d.TITREACTE) as titreActe, TO_CHAR(b.STATUT_ARCHIVAGE_AFTER) as statutPeriode, "
						+ "TO_DATE(TO_CHAR(b.EXTRACTION_DATE,'YYYY-MM-DD HH:MI:SS'), 'YYYY-MM-DD HH:MI:SS') as dateExt, "
						+ "TO_CHAR(d.STATUTARCHIVAGE) as statut, TO_CHAR(b.MESSAGE) as erreur FROM DOSSIER_SOLON_EPG d INNER JOIN DOSSIER_EXTRACTION_BORDEREAU b "
						+ "ON d.id=b.ID_DOSSIER WHERE b.STATUT_ARCHIVAGE_AFTER = '" + statutAfter + "' AND b.EXTRACTION_DATE BETWEEN "
						+ "TO_DATE('" + dateDebut + "','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('" + dateFin + "','YYYY-MM-DD HH24:MI:SS') ORDER BY " + sortInfo + ") inner) outer "
						+ "WHERE outer.rn > '" + offset + "' AND outer.rn <= '" + limit + "'");
				
				ArrayList<DossierArchivageStatDTO> result = new ArrayList<DossierArchivageStatDTO>();
				for (Object[] dosObj : (List<Object[]>) entityManager.createNativeQuery(query.toString()).getResultList()) {
					DossierArchivageStatDTO dto = new DossierArchivageStatDTOImpl((String) dosObj[1], 
							(String) dosObj[2], (String) dosObj[3], (String) dosObj[4], 
							(Date) dosObj[5], (String) dosObj[6], (String) dosObj[7]);
					result.add(dto);
				}
				return result;
			}
		});
	}
	
	@Override
	public List<DossierArchivageStatDTO> getDossierArchivageStatFullResultList(final String statutAfter, 
			final String dateDebut, final String dateFin, final String sortInfo) throws ClientException {
		return getOrCreatePersistenceProvider().run(true, new RunCallback<List<DossierArchivageStatDTO>>() {
			@Override
			@SuppressWarnings("unchecked")
			public List<DossierArchivageStatDTO> runWith(EntityManager entityManager) throws ClientException {
				final StringBuilder query = new StringBuilder(
						"SELECT TO_CHAR(d.ID) as id, TO_CHAR(d.NUMERONOR) as nor, "
						+ "TO_CHAR(d.TITREACTE) as titreActe, TO_CHAR(b.STATUT_ARCHIVAGE_AFTER) as statutPeriode, "
						+ "TO_DATE(TO_CHAR(b.EXTRACTION_DATE,'YYYY-MM-DD HH:MI:SS'), 'YYYY-MM-DD HH:MI:SS') as dateExt, "
						+ "TO_CHAR(d.STATUTARCHIVAGE) as statut, TO_CHAR(b.MESSAGE) as erreur FROM DOSSIER_SOLON_EPG d INNER JOIN DOSSIER_EXTRACTION_BORDEREAU b "
						+ "ON d.id=b.ID_DOSSIER WHERE b.STATUT_ARCHIVAGE_AFTER = '" + statutAfter + "' AND b.EXTRACTION_DATE BETWEEN "
						+ "TO_DATE('" + dateDebut + "','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('" + dateFin + "','YYYY-MM-DD HH24:MI:SS') ORDER BY " + sortInfo);
				
				ArrayList<DossierArchivageStatDTO> result = new ArrayList<DossierArchivageStatDTO>();
				for (Object[] dosObj : (List<Object[]>) entityManager.createNativeQuery(query.toString()).getResultList()) {
					DossierArchivageStatDTO dto = new DossierArchivageStatDTOImpl((String) dosObj[0], 
							(String) dosObj[1], (String) dosObj[2], (String) dosObj[3], 
							(Date) dosObj[4], (String) dosObj[5], (String) dosObj[6]);
					result.add(dto);
				}
				return result;
			}
		});
	}
	
	@Override
	public Integer getCountDossierArchivageStatResult(final String statutAfter, 
			final String dateDebut, final String dateFin) throws ClientException {
		return getOrCreatePersistenceProvider().run(true, new RunCallback<Integer>() {
			@Override
			public Integer runWith(EntityManager entityManager) throws ClientException {
				
				final StringBuilder query = new StringBuilder(
						"SELECT COUNT(*) FROM DOSSIER_SOLON_EPG d INNER JOIN DOSSIER_EXTRACTION_BORDEREAU b ON d.id=b.ID_DOSSIER "
								+ "WHERE b.STATUT_ARCHIVAGE_AFTER = '" + statutAfter + "' AND b.EXTRACTION_DATE BETWEEN "
								+ "TO_DATE('" + dateDebut + "','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('" + dateFin + "','YYYY-MM-DD HH24:MI:SS')");
				
				Object result = entityManager.createNativeQuery(query.toString()).getSingleResult();
				
				return ((BigDecimal) result).intValue();
			}
		});
	}

	public static PersistenceProvider getOrCreatePersistenceProvider() {
		if (persistenceProvider == null) {
			synchronized (BordereauServiceImpl.class) {
				if (persistenceProvider == null) {
					activatePersistenceProvider();
				}
			}
		}
		return persistenceProvider;
	}

	public static void activatePersistenceProvider() {
		final Thread thread = Thread.currentThread();
		final ClassLoader last = thread.getContextClassLoader();
		try {
			thread.setContextClassLoader(PersistenceProvider.class.getClassLoader());
			final PersistenceProviderFactory persistenceProviderFactory = Framework
					.getLocalService(PersistenceProviderFactory.class);
			persistenceProvider = persistenceProviderFactory.newProvider("adamant-provider");
			persistenceProvider.openPersistenceUnit();
		} finally {
			thread.setContextClassLoader(last);
		}
	}
}
