package fr.dila.solonepg.core.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.convert.api.ConversionException;

import com.google.common.collect.Lists;

import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.service.StatsGenerationResultatService;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.event.batch.BatchLoggerModel;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;

public class StatsGenerationResultatServiceImpl implements StatsGenerationResultatService {

	/**
     * 
     */
	private static final long		serialVersionUID		= -3445703103388008293L;

	private static final STLogger	LOGGER					= STLogFactory
																	.getLog(StatsGenerationResultatServiceImpl.class);

	private BatchLoggerModel		batchLoggerModel;
	private List<String>			lstRapportsPubliables	= Lists.newArrayList(
																	ANReportEnum.TAB_AN_DELAI_MIN_TOUS.getId(),
																	ANReportEnum.TAB_AN_STAT_DEBUT_LEGISLATURE.getId(),
																	ANReportEnum.TAB_AN_INDIC_APP_ORDO_DEBUT_LEGISLATURE.getId(),
																	ANReportEnum.TAB_AN_STAT_MISE_APPLICATION.getId(),
																	ANReportEnum.TAB_AN_DEBUT_LEGISLATURE_MIN.getId(),
																	ANReportEnum.TAB_AN_DEBUT_LEGISLATURE_MIN_APP_ORDO
																			.getId(),
																	ANReportEnum.TAB_AN_TABLEAU_BORD_ACTIVE.getId(),
																	ANReportEnum.TAB_AN_TB_APP_ORDO_ACTIVE.getId(),
																	ANReportEnum.TAB_AN_TABLEAU_BORD_DIFFEREE.getId(),
																	ANReportEnum.TAB_AN_TB_APP_ORDO_DIFFEREE.getId(),
																	ANReportEnum.TAB_AN_FIL_EAU_GLOBAL.getId(),
																	ANReportEnum.TAB_AN_FIL_EAU_MIN_TOUS.getId(),
																	ANReportEnum.TAB_AN_FIL_EAU_GLOBAL_APP_ORDONNANCE
																			.getId(),
																	ANReportEnum.TAB_AN_FIL_EAU_MIN_TOUS_APP_ORDONNANCE
																			.getId(),
																	ANReportEnum.TAB_AN_MESURES_APPLICATION_LOI.getId(),
																	ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES.getId(),
																	ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN.getId(),
																	ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN_ALL
																			.getId(),
																	ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN_APP_ORDO
																			.getId(),
																	ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN_APP_ORDO_ALL
																			.getId(),
																	ANReportEnum.TAB_AN_MESURES_APPLICATION_LOI_ALL
																			.getId(),
																	ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES_ALL
																			.getId(),
																	ANReportEnum.TAB_AN_TAUX_APPLICATION_LOI_ALL
																			.getId(),
																	ANReportEnum.TAB_AN_TAUX_APPLICATION_SUB_APP_ORDONNANCES_ALL
																			.getId(),
																	ANReportEnum.TAB_AN_DELAI_LOI_TOUS.getId(),
																	ANReportEnum.TAB_AN_DELAI_ORDO_TOUS_APP_ORDO
																			.getId(),
																	ANReportEnum.TAB_AN_TABLEAU_BORD_HABILITATION_SS_FILTRE
																			.getId(),
																	ANReportEnum.TAB_AN_HABILITATION_SS_FLTR_MIN_ALL
																			.getId(),
																	ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_PAS_DEPOSE
																			.getId(),
																	ANReportEnum.TAB_AN_RATIFICATION_38C_MIN.getId(),
																	ANReportEnum.TAB_AN_RATIFICATION_741_MIN.getId(),
																	ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_38C
																			.getId(),
																	ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_741
																			.getId(),
																	ANReportEnum.TAB_AN_TABLEAU_BORD_HABILITATION_SS_FILTRE
																			.getId(),
																	ANReportEnum.TAB_AN_INDIC_APP_ORDO_MISE_APPLI.getId(),
																	ANReportEnum.TAB_AN_DELAI_MIN_TOUS_APP_ORDO.getId());

	@Override
	public void generateAllReportResult(final CoreSession session, final String reportName, final String reportFile,
			final Map<String, String> inputValues, final String reportId, final BatchLoggerModel batchLoggerModel,
			boolean curLegis) throws ClientException {

		try {
			this.batchLoggerModel = batchLoggerModel;
			DocumentModel birtResultatFichier = getBirtResultatFichier(session, reportName);
			List<String> outPutFormats = new ArrayList<String>();
			outPutFormats.add(HTMLRenderOption.OUTPUT_FORMAT_HTML);
			outPutFormats.add(HTMLRenderOption.OUTPUT_FORMAT_PDF);
			outPutFormats.add("xls");
			// Pour les besoins débug génération rapport birt
			LOGGER.info(session, STLogEnumImpl.LOG_INFO_TEC, "****************NAME REPORT : " + reportName);
			Map<String, Blob> exportsContent = SSServiceLocator.getSSBirtService().generateReportResults(reportName,
					reportFile, inputValues, outPutFormats);
			final Blob blobHtml = exportsContent.get(HTMLRenderOption.OUTPUT_FORMAT_HTML);
			final Blob blobPdf = exportsContent.get(HTMLRenderOption.OUTPUT_FORMAT_PDF);
			final Blob blobXls = exportsContent.get("xls");

			if (birtResultatFichier == null) {
				birtResultatFichier = session.createDocumentModel(
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_PATH, reportName,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_TYPE);

				// set document file properties
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_FILE_NAME_PROPERTY, reportName);
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PROPERTY, blobHtml);
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_CSV_PROPERTY, blobXls);
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PDF_PROPERTY, blobPdf);
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_MODIFIED, Calendar.getInstance());

				// set entite
				// creation du document en session
				session.createDocument(birtResultatFichier);
			} else {
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PROPERTY, blobHtml);
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_CSV_PROPERTY, blobXls);
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PDF_PROPERTY, blobPdf);
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_MODIFIED, Calendar.getInstance());

				session.saveDocument(birtResultatFichier);
			}
			session.save();
			// publier les rapports
			if (isRapportPubliable(reportId)) {
				publierReportResulat(reportName, blobHtml, curLegis, session);
			}

		} catch (final ConversionException exc) {
			LOGGER.warn(session, SSLogEnumImpl.FAIL_CREATE_BIRT_TEC, exc);
		} catch (final Exception e) {
			throw new ClientException("error while generating stat report birt", e);
		}
	}

	@Override
	public void saveReportResulat(final CoreSession session, final String reportName, final Blob blobHtml,
			final Blob blobPdf, final Blob blobXls) throws ClientException {

		try {
			DocumentModel birtResultatFichier = getBirtResultatFichier(session, reportName);

			if (birtResultatFichier == null) {

				birtResultatFichier = session.createDocumentModel(
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_PATH, reportName,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_TYPE);

				// set document file properties
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_FILE_NAME_PROPERTY, reportName);
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PROPERTY, blobHtml);
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_CSV_PROPERTY, blobXls);
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PDF_PROPERTY, blobPdf);
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_MODIFIED, Calendar.getInstance());

				// set entite
				// creation du document en session
				session.createDocument(birtResultatFichier);
				// commit creation
				session.save();
			} else {
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PROPERTY, blobHtml);
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_CSV_PROPERTY, blobXls);
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PDF_PROPERTY, blobPdf);
				birtResultatFichier.setProperty(ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_MODIFIED, Calendar.getInstance());

				session.saveDocument(birtResultatFichier);
				session.save();
			}
		} catch (final ConversionException exc) {
			LOGGER.warn(session, SSLogEnumImpl.FAIL_CREATE_BIRT_TEC, exc);
		} catch (final Exception e) {
			throw new ClientException("Error while generating stat report birt", e);
		}
	}

	@Override
	public void publierReportResulat(final String reportName, final Blob blobHtml, boolean curLegis, CoreSession session)
			throws ClientException {
		final String generatedReportPath;
		if (curLegis) {
			generatedReportPath = SolonEpgServiceLocator.getActiviteNormativeService().getPathDirANStatistiquesPublie();
		} else {
			generatedReportPath = SolonEpgServiceLocator.getActiviteNormativeService()
					.getPathDirANStatistiquesLegisPrecPublie(session);
		}
		publierReportResulat(reportName, blobHtml, generatedReportPath);
		session.save();
	}

	@Override
	public void publierReportResulat(final String reportName, final Blob blobHtml, final String path)
			throws ClientException {
		publierReportResulat(reportName, blobHtml, path, "html");
	}

	@Override
	public void publierReportResulat(final String reportName, final Blob blob, final String path, final String extension)
			throws ClientException {
		final long startTime = Calendar.getInstance().getTimeInMillis();
		FileOutputStream outputStream = null;
		final String pathName = path + "/" + reportName + "." + extension;
		try {
			if (blob != null) {
				LOGGER.info(SSLogEnumImpl.CREATE_BIRT_TEC, pathName);
				final File report = new File(pathName);
				if (!report.exists()) {
					report.createNewFile();
				}
				outputStream = new FileOutputStream(report);
				final byte[] bytes = blob.getByteArray();
				outputStream.write(bytes);
				outputStream.close();
			}
		} catch (final Exception exc1) {
			LOGGER.error(SSLogEnumImpl.FAIL_PUBLISH_BIRT_TEC, exc1);
			final long endTime = Calendar.getInstance().getTimeInMillis();
			if (batchLoggerModel != null) {
				try {
					STServiceLocator.getSuiviBatchService().createBatchResultFor(batchLoggerModel,
							"Erreur lors de la génération d'un rapport " + pathName, endTime - startTime);
				} catch (Exception exc) {
					LOGGER.error(STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, exc);
				}
			}
			throw new ClientException("error while saving report birt to " + extension, exc1);
		} finally {
			if (outputStream != null) {
				IOUtils.closeQuietly(outputStream);
			}
		}

		if (batchLoggerModel != null) {
			final long endTimeOk = Calendar.getInstance().getTimeInMillis();
			try {
				STServiceLocator.getSuiviBatchService().createBatchResultFor(batchLoggerModel,
						"Génération d'un rapport" + pathName, endTimeOk - startTime);
				
			} catch (Exception exc) {
				LOGGER.error(STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, exc);
			}
		}
	}

	@Override
	public DocumentModel getBirtResultatFichier(final CoreSession session, final String reportName)
			throws ClientException {
		DocumentModel birtResultatFichier = null;
		final String query = "SELECT * FROM BirtResultatFichier WHERE ecm:name ='" + reportName
				+ "' and ecm:isProxy = 0";
		final DocumentModelList list = session.query(query);
		if (list != null && list.size() > 0) {
			birtResultatFichier = list.get(0);
		}
		return birtResultatFichier;
	}

	@Override
	public Blob generateReportResult(final String reportName, final String reportFile,
			final Map<String, String> inputValues, final String outPutFormat) throws ClientException {
		try {
			return SSServiceLocator.getSSBirtService().generateReportResults(reportName, reportFile, inputValues,
					outPutFormat);
		} catch (final Exception e) {
			throw new ClientException("error while generating stat report birt", e);
		}
	}

	@Override
	public String getMinisteresListBirtReportParam(CoreSession session) throws ClientException {
		StringBuilder ministereParm = new StringBuilder();
		String query = "SELECT distinct MINISTEREPILOTE from texte_maitre where MINISTEREPILOTE is not null";
		String returnType = TexteMaitreConstants.TEXTE_MAITRE_PREFIX + ":" + TexteMaitreConstants.MINISTERE_PILOTE;

		IterableQueryResult res = null;
		try {
			res = QueryUtils.doSqlQuery(session, new String[] { returnType }, query, new String[] {});

			Iterator<Map<String, Serializable>> iterator = res.iterator();
			// Collections.sort(ministereList, new ProtocolarOrderComparator());
			// Récupération de la liste des ministères

			while (iterator.hasNext()) {
				Map<String, Serializable> row = iterator.next();
				String minId = (String) row.get(returnType);

				final EntiteNode entiteNode = STServiceLocator.getSTMinisteresService().getEntiteNode(minId);
				if (entiteNode != null) {
					ministereParm.append("$id$=").append(entiteNode.getId()).append(";;$label$=")
							.append(entiteNode.getLabel()).append(";;$ordre$=").append(entiteNode.getOrdre())
							.append(";;$formule$=").append(entiteNode.getFormule()).append(";;&");
				}
			}
		} catch (ClientException e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_EXEC_SQL, e);
		} finally {
			if (res != null) {
				res.close();
			}
		}

		return ministereParm.toString();
	}

	@Override
	public String getDirectionsListBirtReportParam() throws ClientException {
		StringBuilder directionParm = new StringBuilder();
		final List<OrganigrammeNode> directionsList = SolonEpgServiceLocator.getEpgOrganigrammeService()
				.getAllDirectionList();
		// Récupération de la liste des directions
		for (final OrganigrammeNode node : directionsList) {
			final UniteStructurelleNode uniteStructurelleNode = (UniteStructurelleNode) node;
			directionParm.append("$id$=").append(uniteStructurelleNode.getId()).append(";;$label$=")
					.append(uniteStructurelleNode.getLabel()).append(";;&");

		}
		return directionParm.toString();
	}

	@Override
	public boolean isRapportPubliable(String anReportId) {

		return lstRapportsPubliables.contains(anReportId);
	}

	@Override
	public String getMinisteresCourantListBirtReportParam() throws ClientException {
		StringBuilder ministereParm = new StringBuilder();
		// Récupération de la liste des ministères
		final List<EntiteNode> ministereList = STServiceLocator.getSTMinisteresService().getCurrentMinisteres();

		for (final OrganigrammeNode node : ministereList) {
			final EntiteNode entiteNode = (EntiteNode) node;
			ministereParm.append("$id$=").append(entiteNode.getId()).append(";;$label$=").append(entiteNode.getLabel())
					.append(";;$ordre$=").append(entiteNode.getOrdre()).append(";;$formule$=")
					.append(entiteNode.getFormule()).append(";;&");
		}
		return ministereParm.toString();
	}
}
