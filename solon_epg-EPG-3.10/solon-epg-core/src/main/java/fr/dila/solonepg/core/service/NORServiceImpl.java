package fr.dila.solonepg.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunVoid;
import org.nuxeo.ecm.platform.uidgen.UIDSequencer;
import org.nuxeo.ecm.platform.uidgen.service.ServiceHelper;
import org.nuxeo.ecm.platform.uidgen.service.UIDGeneratorService;
import org.nuxeo.ecm.platform.uidgen.service.UIDSequencerImpl;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.util.SessionUtil;

public class NORServiceImpl extends DefaultComponent implements NORService {

	/**
	 * Serial version UID
	 */
	private static final long	serialVersionUID			= -427109452126538120L;

	private static final String	SOLON_EPG_NOR_SEQUENCER		= "SOLON_EPG_NOR_SEQUENCER_";

	private static final Log	log							= LogFactory.getLog(NORServiceImpl.class);

	protected String			FIRST_LETTER_RECTIFICATIF	= "Z";

	protected String			SECOND_LETTER_RECTIFICATIF	= "F";

	protected String			THIRD_LETTER_RECTIFICATIF	= "H";

	@Override
	public void deactivate(final ComponentContext context) throws Exception {

	}

	@Override
	public String createNOR(final String norActe, final String norMinistere, final String norDirection)
			throws ClientException {
		// Vérification des arguments
		if (StringUtils.isBlank(norActe) || norActe.length() != 1) {
			throw new IllegalArgumentException("Lettres de NOR acte invalides");
		}
		checkNorMinistere(norMinistere);
		checkNorDirection(norDirection);

		// Année
		final Calendar date = Calendar.getInstance();
		final String annee = Integer.toString(date.get(Calendar.YEAR)).substring(2, 4);

		// Compteur
		String compteur;
		String nuxeoTemplates = Framework.getProperty("nuxeo.templates", "");
		// Si le template oracle n'est pas activé, on utilise le sequenceur nuxeo (utile pour H2)
		if (nuxeoTemplates.contains("oracle")) {
			compteur = String.format("%05d", getNumeroChronoNor(annee));
		} else {
			final UIDGeneratorService uidGeneratorService = ServiceHelper.getUIDGeneratorService();
			final UIDSequencer sequencer = uidGeneratorService.getSequencer();
			compteur = String.format("%05d", sequencer.getNext(SOLON_EPG_NOR_SEQUENCER + annee) - 1);
		}

		final String nor = norMinistere + norDirection + annee + compteur + norActe;

		return nor;
	}

	/**
	 * On va rechercher le numero chrono dans le sequenceur oracle si le séquenceur n'existe pas (nouvelle année par
	 * exemple), on appelle la procédure stockée de création d'un nouveau séquenceur
	 * 
	 * @param annee
	 * @return un numero chrono unique
	 * @throws ClientException
	 */
	private int getNumeroChronoNor(final String annee) throws ClientException {
		final int[] nextVal = new int[1];
		nextVal[0] = -1;
		UIDSequencerImpl.getOrCreatePersistenceProvider().run(true, new RunVoid() {
			public void runWith(EntityManager entityManager) throws ClientException {
				// génération du nom de séquence à aller chercher en fonction de l'année
				String sequenceName = SOLON_EPG_NOR_SEQUENCER + annee;

				// On vérifie que la séquence existe
				final StringBuilder checkSequence = new StringBuilder(
						"SELECT count(1) FROM user_sequences WHERE sequence_name = '").append(sequenceName).append("'");
				final Query checkSequenceQuery = entityManager.createNativeQuery(checkSequence.toString());
				int count = ((Number) checkSequenceQuery.getSingleResult()).intValue();
				// Si elle n'existe pas, on la créée
				if (count == 0) {
					// Si la sequence n'existe pas, on génère une nouvelle séquence
					CoreSession session = null;
					try {
						session = SessionUtil.getCoreSession();
						// Appel de la procédure stockée CREATE_NEW_SEQUENCE qui prend en paramètre le nom de la
						// nouvelle séquence
						StringBuilder createSequence = new StringBuilder("CREATE_NEW_SEQUENCE('").append(sequenceName)
								.append("')");
						QueryUtils.execSqlFunction(session, createSequence.toString(), null);
					} finally {
						if (session != null) {
							SessionUtil.close(session);
						}
					}
				}
				final StringBuilder selectNextVal = new StringBuilder("SELECT ").append(sequenceName).append(
						".nextval from dual");
				// récupération de la prochaine valeur
				final Query nextValQuery = entityManager.createNativeQuery(selectNextVal.toString());
				final List<Number> resultList = nextValQuery.getResultList();
				if (resultList != null && !resultList.isEmpty()) {
					nextVal[0] = ((Number) resultList.get(0)).intValue();
				}
			}
		});
		if (nextVal[0] == -1) {
			throw new EPGException("Impossible de générer le numéro chrono du NOR");
		}
		return nextVal[0];
	}

	@Override
	public String createRectificatifNOR(final Dossier dossierRectifie) throws ClientException {
		final String ancienNor = dossierRectifie.getNumeroNor();
		final StringBuilder nouveauNor = new StringBuilder(ancienNor.substring(0, 11));
		final Long nbDossierRectifie = dossierRectifie.getNbDossierRectifie();
		// on ne peut pas créér plus de 3 rectificatif !
		if (nbDossierRectifie > 2L) {
			throw new IllegalArgumentException(
					"Plus de 3 rectifications sur le même dossier ! Nouvelle rectification interdite !");
		}
		// on ajoute 1 au nb de dossier rectifie pour prendre en compte le rectificatif que l'on est en train de creer
		nouveauNor.append(getLettreFromNbRectificatif(nbDossierRectifie + 1));
		return nouveauNor.toString();
	}

	@Override
	public String getLettreFromNbRectificatif(final Long nbRectificatif) {
		if (nbRectificatif == 1L) {
			return FIRST_LETTER_RECTIFICATIF;
		} else if (nbRectificatif == 2L) {
			return SECOND_LETTER_RECTIFICATIF;
		} else if (nbRectificatif == 3L) {
			return THIRD_LETTER_RECTIFICATIF;
		}
		return null;
	}

	@Override
	public DocumentModel getDossierFromNOR(final CoreSession session, final String nor) throws ClientException {
		if (StringUtils.isBlank(nor)) {
			return null;
		}
		if (session == null) {
			throw new IllegalArgumentException("La session ne peut pas etre nulle");
		}

		final StringBuilder queryBuider = new StringBuilder(" SELECT d.ecm:uuid as id FROM ");
		queryBuider.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
		queryBuider.append(" as d WHERE d.");
		queryBuider.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
		queryBuider.append(":");
		queryBuider.append(DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY);
		queryBuider.append(" = ? ");

		final List<DocumentModel> listDoc = QueryUtils.doUnrestrictedUFNXQLQueryAndFetchForDocuments(session,
				DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, queryBuider.toString(), new Object[] { nor }, 1, 0);
		if (!listDoc.isEmpty()) {
			return listDoc.get(0);
		}

		return null;

	}
	
	@Override
	public DocumentModel getDossierFromNORWithACL(final CoreSession session, final String nor) throws ClientException {
		if (StringUtils.isBlank(nor)) {
			return null;
		}
		if (session == null) {
			throw new IllegalArgumentException("La session ne peut pas etre nulle");
		}
		final StringBuilder queryBuider = new StringBuilder(" SELECT d.ecm:uuid as id FROM ");
		queryBuider.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
		queryBuider.append(" as d WHERE d.");
		queryBuider.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
		queryBuider.append(":");
		queryBuider.append(DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY);
		queryBuider.append(" = ? ");

		final List<DocumentModel> listDoc = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, queryBuider.toString(), new Object[] { nor }, 1, 0);
		if (!listDoc.isEmpty()) {
			return listDoc.get(0);
		}
		return null;
	}

	@Override
	public Dossier getRectificatifFromNORAndNumRect(final CoreSession session, final String nor,
			final Long nbRectificatif) throws ClientException {
		if (nor == null || nor.trim().length() == 0) {
			return null;
		}

		if (nbRectificatif > 3L) {
			throw new IllegalArgumentException("Un dossier ne peut pas avoir plus de 3 rectificatifs");
		}

		// récupération du nor du rectificatif à partir du nor du dossier
		final StringBuilder norRectificatif = new StringBuilder(nor.substring(0, 11));
		norRectificatif.append(getLettreFromNbRectificatif(nbRectificatif));

		// récupération du rectificatif
		return findDossierFromNOR(session, norRectificatif.toString());
	}

	@Override
	public Dossier findDossierFromNOR(final CoreSession session, final String nor) throws ClientException {
		final DocumentModel doc = getDossierFromNOR(session, nor);
		if (doc != null) {
			return doc.getAdapter(Dossier.class);
		}
		return null;
	}

	protected void checkNorMinistere(final String norMinistere) throws ClientException {
		if (norMinistere == null || norMinistere.length() != 3) {
			throw new ClientException("Lettres de NOR ministère invalides : " + norMinistere);
		}
	}

	protected void checkNorDirection(final String norDirection) throws ClientException {
		if (norDirection == null || norDirection.length() != 1) {
			throw new ClientException("Lettres de NOR direction invalides : " + norDirection);
		}
	}

	@Override
	public String reattributionMinistereNOR(final Dossier dossierReattribue, final String norMinistere)
			throws ClientException {
		checkNorMinistere(norMinistere);
		String nouveauNor = dossierReattribue.getNumeroNor();
		nouveauNor = norMinistere + nouveauNor.substring(3);
		return nouveauNor;
	}

	@Override
	public String reattributionDirectionNOR(final Dossier dossierReattribue, final String norMinistere,
			final String norDirection) throws ClientException {
		checkNorMinistere(norMinistere);
		checkNorDirection(norDirection);
		String nouveauNor = dossierReattribue.getNumeroNor();
		nouveauNor = norMinistere + norDirection + nouveauNor.substring(4);
		return nouveauNor;
	}

	@Override
	public List<String> findDossierIdsFromNOR(final CoreSession coreSession, final String nor) throws ClientException {

		final StringBuilder queryBuider = new StringBuilder(" SELECT d.ecm:uuid as id FROM ");
		queryBuider.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
		queryBuider.append(" as d WHERE d.");
		queryBuider.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
		queryBuider.append(":");
		queryBuider.append(DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY);
		queryBuider.append(" LIKE ? ");
		queryBuider.append(" AND d.");
		queryBuider.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
		queryBuider.append(":");
		queryBuider.append(DossierSolonEpgConstants.ARCHIVE_DOSSIER_PROPERTY);
		queryBuider.append(" = ? AND testAcl(d.ecm:uuid) = 1 ");

		IterableQueryResult res = null;
		final List<String> result = new ArrayList<String>();
		try {
			res = QueryUtils.doUFNXQLQuery(coreSession, queryBuider.toString(), new Object[] { nor, 0 });

			final Iterator<Map<String, Serializable>> iterator = res.iterator();
			while (iterator.hasNext()) {
				final Map<String, Serializable> map = iterator.next();
				result.add((String) map.get("id"));
			}

		} finally {
			if (res != null) {
				res.close();
			}
		}

		return result;
	}

	@Override
	public Set<String> retrieveAvailableNorList(final CoreSession session) throws ClientException {

		final Set<String> availableNor = new HashSet<String>();

		final String query = "select dos.numeroNor from dossier_solon_epg dos";
		IterableQueryResult res = null;

		try {
			res = QueryUtils.doSqlQuery(session, new String[] { "dos:numeroNor" }, query, new Object[] {});

			final Iterator<Map<String, Serializable>> iterator = res.iterator();
			while (iterator.hasNext()) {
				final Map<String, Serializable> row = iterator.next();
				final String numeroNor = (String) row.get("dos:numeroNor");
				availableNor.add(numeroNor);
			}
		} catch (final ClientException e) {
			log.error("Erreur lors de la récupération des numéros des nors", e);
		} finally {
			if (res != null) {
				res.close();
			}
		}
		return availableNor;
	}
	
	@Override
	public boolean checkNorUnicity(final CoreSession session, String nor){
		boolean exist = false;

		final String query = "select dos.id from dossier_solon_epg dos where dos.numeroNor = ?";
		IterableQueryResult res = null;
		try {
			res = QueryUtils.doSqlQuery(session, new String[] { FlexibleQueryMaker.COL_ID }, query, new Object[] {nor});
			final Iterator<Map<String, Serializable>> iterator = res.iterator();
			while (iterator.hasNext()) {
				final Map<String, Serializable> row = iterator.next();
				final String docid = (String) row.get(FlexibleQueryMaker.COL_ID);
				log.warn(String.format("NOR[%s] used by Doc[%s]", nor, docid));
				exist = true;
			}
		} catch (final ClientException e) {
			log.error("Erreur lors de la récupération des numéros des nors", e);
		} finally {
			if (res != null) {
				res.close();
			}
		}
		return exist;
	}

	/**
	 * Récupération du dossier via son numéro ISA
	 */
	@Override
	public DocumentModel getDossierFromISA(final CoreSession session, final String nor) throws ClientException {
		if (StringUtils.isBlank(nor)) {
			return null;
		}
		if (session == null) {
			throw new IllegalArgumentException("La session ne peut pas etre nulle");
		}

		final StringBuilder queryBuider = new StringBuilder(" SELECT d.ecm:uuid as id FROM ");
		queryBuider.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
		queryBuider.append(" as d WHERE d.");
		queryBuider.append(DossierSolonEpgConstants.CONSEIL_ETAT_SCHEMA_PREFIX);
		queryBuider.append(":");
		queryBuider.append(DossierSolonEpgConstants.DOSSIER_NUMERO_ISA_PROPERTY);
		queryBuider.append(" = ? ");

		final List<DocumentModel> listDoc = QueryUtils.doUnrestrictedUFNXQLQueryAndFetchForDocuments(session,
				DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, queryBuider.toString(), new Object[] { nor });
		if (listDoc.size() > 1) {
			throw new ClientException("Plusieurs dossiers correspondent à ce numéro isa");
		}
		if (!listDoc.isEmpty() && listDoc.size() < 2) {
			return listDoc.get(0);
		}

		return null;

	}
	
	@Override
	public boolean isStructNorValide(String nor) {
				
		String regex = "^[a-zA-Z]{4}[0-9]{7}[a-zA-Z]{1}$";
		if(nor.matches(regex)) {
			return true;
		}
		return false;
	}
}
