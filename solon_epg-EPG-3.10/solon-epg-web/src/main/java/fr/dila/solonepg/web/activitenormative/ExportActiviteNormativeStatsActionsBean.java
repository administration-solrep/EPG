package fr.dila.solonepg.web.activitenormative;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.webapp.action.WebActionsBean;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.birt.ExportPANStat;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.service.ActiviteNormativeStatsService;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.core.service.ActiviteNormativeReportsGenerator;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.client.PANExportParametreDTO;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean Seam de export pan stats
 * 
 * @author mchahine
 */
@Name("exportActiviteNormativeStatsActions")
@Scope(ScopeType.CONVERSATION)
public class ExportActiviteNormativeStatsActionsBean implements Serializable {

	private static final long							serialVersionUID			= 2776349205588506388L;
	private static final STLogger						LOGGER						= STLogFactory
																							.getLog(ActiviteNormativeActionsBean.class);

	@In(create = true, required = false)
	protected transient WebActionsBean					webActions;

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true, required = false)
	protected transient TexteMaitreActionsBean			texteMaitreActions;

	@In(create = true, required = false)
	protected transient ActiviteNormativeActionsBean	activiteNormativeActions;

	@In(create = true, required = false)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true, required = false)
	protected transient FacesMessages					facesMessages;

	@In(required = true, create = true)
	protected SSPrincipal								ssPrincipal;

	private ParametrageAN								parameterStatsDoc;

	private boolean										periodeParMois;

	private int											mois;

	private int											annee;

	private boolean										parameterDisplayed;

	protected PANExportParametreDTO						exportDefaultParam			= new PANExportParametreDTO();

	private List<String>								exportedLegislatures		= new ArrayList<String>();

	private boolean										publicationLegisPrecVisible	= false;

	public void exportPanStats(boolean forceExportLegisPrec) throws Exception {
		final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
		final String currentUser = ssPrincipal.getName();
		final String userWorkspacePath = STServiceLocator.getUserWorkspaceService()
				.getCurrentUserPersonalWorkspace(documentManager, null).getPathAsString();

		if (forceExportLegisPrec) {
			initExportParameters(false);
		}

		DocumentModel exportPanStatDoc = anStatsService.getExportPanStatDoc(documentManager, currentUser,
				exportedLegislatures);
		ExportPANStat exportLegislature = null;

		if (exportPanStatDoc != null) {
			exportLegislature = exportPanStatDoc.getAdapter(ExportPANStat.class);
		}

		if ((exportLegislature != null && !exportLegislature.isExporting()) || exportLegislature == null) {
			if (anStatsService.flagExportFor(documentManager, currentUser, userWorkspacePath, exportedLegislatures)) {
				// Post commit event
				EventProducer eventProducer = STServiceLocator.getEventProducer();
				Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
				eventProperties.put(SolonEpgANEventConstants.INPUT_VALUES_PROPERTY,
						(Serializable) this.assignParameters());
				eventProperties.put(SolonEpgANEventConstants.USER_PROPERTY, currentUser);
				eventProperties.put(SolonEpgANEventConstants.USER_WS_PATH_PROPERTY, userWorkspacePath);
				eventProperties
						.put(SolonEpgANEventConstants.EXPORTED_LEGIS, StringUtil.join(exportedLegislatures, ","));

				InlineEventContext eventContext = new InlineEventContext(documentManager, ssPrincipal, eventProperties);
				eventProducer.fireEvent(eventContext.newEvent(SolonEpgANEventConstants.EXPORT_STATS_EVENT));
			}
		} else {
			// On affiche à l'utilisateur qu'un rafraichissement a déjà été
			// demandé et est en cours
			String dateRequest = DateUtil.formatWithHour(exportLegislature.getDateRequest().getTime());
			String info = "La mise à jour a déjà été demandée le " + dateRequest;
			facesMessages.add(StatusMessage.Severity.INFO, info);
		}
	}

	/**
	 * export Pan Stats
	 * 
	 * @throws Exception
	 */
	public void exportPanStats() throws Exception {

		exportPanStats(false);
	}

	private String buildLegislatureParamLabel() {
		StringBuilder builder = new StringBuilder();
		if (!this.exportedLegislatures.isEmpty()) {

			if (exportedLegislatures.size() == 1) {
				String legislatureSelectionnee = exportedLegislatures.get(0);
				builder.append("la ");

				if (StringUtil.isBlank(legislatureSelectionnee) || legislatureSelectionnee.equals("NR")) {
					builder.append("législature non renseignée");
				} else {
					builder.append(legislatureSelectionnee + " législature");
				}
			} else {
				builder.append("les ");
				int i = 0;
				while (i < exportedLegislatures.size() - 1) {
					String legislature = exportedLegislatures.get(i);
					if (StringUtil.isBlank(legislature) || legislature.equals("NR")) {
						legislature = "non renseignée";
					}

					builder.append(legislature);
					builder.append(", ");
					i++;
				}
				String lastLegislature = exportedLegislatures.get(exportedLegislatures.size() - 1);
				if (StringUtil.isBlank(lastLegislature) || lastLegislature.equals("NR")) {
					lastLegislature = "non renseignée";
				}
				builder.append(lastLegislature);

				builder.append(" législatures");
			}
		}
		return builder.toString();
	}

	private Map<String, String> assignParameters() throws ClientException {
		Map<String, String> inputValues = new HashMap<String, String>();
		String ministeresParam = SolonEpgServiceLocator.getStatsGenerationResultatService()
				.getMinisteresListBirtReportParam(documentManager);
		String directionsParam = SolonEpgServiceLocator.getStatsGenerationResultatService()
				.getDirectionsListBirtReportParam();

		inputValues.put("MINISTERES_PARAM", ministeresParam);
		inputValues.put("DIRECTIONS_PARAM", directionsParam);
		inputValues.put("DEBUTLEGISLATURE_PARAM",
				DateUtil.formatDDMMYYYYSlash(exportDefaultParam.getDateDebutLegislature()));
		inputValues.put("PERIODE_PARAM", (isPeriodeParMois() ? "M" : "A"));
		inputValues.put("MOIS_PARAM", String.valueOf(getMois()));
		inputValues.put("ANNEE_PARAM", String.valueOf(getAnnee()));
		inputValues
				.put("LEGISLATURE_PARAM", DateUtil.formatDDMMYYYYSlash(exportDefaultParam.getDateDebutLegislature()));

		inputValues.put("LEGISLATURES", StringUtil.joinValueToBlank(exportedLegislatures, ", ", "'", "NR"));
		inputValues.put("LEGISLATURES_LABEL", buildLegislatureParamLabel());

		return inputValues;
	}

	public boolean isExporting(List<ExportPANStat> lstExports) throws ClientException {
		boolean isExporting = false;
		int i = 0;

		if (lstExports != null) {
			while (i < lstExports.size() && !isExporting) {
				if (lstExports.get(i).isExporting()) {
					isExporting = true;
				}
				i++;
			}

		}
		return isExporting;
	}

	public void initExportParameters(boolean legisEnCours) throws ClientException {
		if (documentManager != null) {

			if (legisEnCours) {
				// Taux d'exécution des lois (depuis le début de la législature)
				exportDefaultParam.setTauxPromulgationDebut(parameterStatsDoc.getLECTauxDebutLegisDatePromulBorneInf());
				exportDefaultParam.setTauxPromulgationFin(parameterStatsDoc.getLECTauxDebutLegisDatePromulBorneSup());

				exportDefaultParam.setTauxPublicationDebut(parameterStatsDoc.getLECTauxDebutLegisDatePubliBorneInf());
				exportDefaultParam.setTauxPublicationFin(parameterStatsDoc.getLECTauxDebutLegisDatePubliBorneSup());

				// Taux d'exécution des lois (au cours de la dernière session
				// parlementaire)
				exportDefaultParam.setLolfPromulgationDebut(parameterStatsDoc.getLECTauxSPDatePromulBorneInf());
				exportDefaultParam.setLolfPromulgationFin(parameterStatsDoc.getLECTauxSPDatePromulBorneSup());

				exportDefaultParam.setLolfPublicationDebut(parameterStatsDoc.getLECTauxSPDatePubliBorneInf());
				exportDefaultParam.setLolfPublicationFin(parameterStatsDoc.getLECTauxSPDatePubliBorneSup());

				// Bilan semestriel
				exportDefaultParam.setBilanPromulgationDebut(parameterStatsDoc.getLECBSDatePromulBorneInf());
				exportDefaultParam.setBilanPromulgationFin(parameterStatsDoc.getLECBSDatePromulBorneSup());

				exportDefaultParam.setBilanPublicationDebut(parameterStatsDoc.getLECBSDatePubliBorneInf());
				exportDefaultParam.setBilanPublicationFin(parameterStatsDoc.getLECBSDatePubliBorneSup());

				exportDefaultParam.setDateDebutLegislature(parameterStatsDoc.getLegislatureEnCoursDateDebut());

				// Cas des ordonnances
				// Bilan semestriel
				exportDefaultParam.setBilanPublicationDebutOrdo(parameterStatsDoc.getLECBSDatePubliOrdoBorneInf());
				exportDefaultParam.setBilanPublicationFinOrdo(parameterStatsDoc.getLECBSDatePubliOrdoBorneSup());

				exportDefaultParam.setBilanPublicationDebutOrdoDecret(parameterStatsDoc.getLECBSDatePubliDecretOrdoBorneInf());
				exportDefaultParam.setBilanPublicationFinOrdoDecret(parameterStatsDoc.getLECBSDatePubliDecretOrdoBorneSup());
				
				// Taux d'exécution des lois (depuis le début de la législature)
				exportDefaultParam.setTauxDLPublicationOrdosDebut(parameterStatsDoc.getLECTauxDebutLegisDatePubliOrdoBorneInf());
				exportDefaultParam.setTauxDLPublicationOrdosFin(parameterStatsDoc.getLECTauxDebutLegisDatePubliOrdoBorneSup());

				exportDefaultParam.setTauxDLPublicationDecretsOrdosDebut(parameterStatsDoc.getLECTauxDebutLegisDatePubliDecretOrdoBorneInf());
				exportDefaultParam.setTauxDLPublicationDecretsOrdosFin(parameterStatsDoc.getLECTauxDebutLegisDatePubliDecretOrdoBorneSup());

				// Taux d'exécution des lois (au cours de la dernière session
				// parlementaire)
				exportDefaultParam.setTauxSPPublicationOrdosDebut(parameterStatsDoc.getLECTauxSPDatePubliOrdoBorneInf());
				exportDefaultParam.setTauxSPPublicationOrdosFin(parameterStatsDoc.getLECTauxSPDatePubliOrdoBorneSup());

				exportDefaultParam.setTauxSPPublicationDecretsOrdosDebut(parameterStatsDoc.getLECTauxSPDatePubliDecretOrdoBorneInf());
				exportDefaultParam.setTauxSPPublicationDecretsOrdosFin(parameterStatsDoc.getLECTauxSPDatePubliDecretOrdoBorneSup());
				

				exportedLegislatures.clear();
				exportedLegislatures.add(parameterStatsDoc.getLegislatureEnCours());
			} else {
				// Taux d'exécution des lois (depuis le début de la législature)
				exportDefaultParam.setTauxPromulgationDebut(parameterStatsDoc.getLPTauxDebutLegisDatePromulBorneInf());
				exportDefaultParam.setTauxPromulgationFin(parameterStatsDoc.getLPTauxDebutLegisDatePromulBorneSup());

				exportDefaultParam.setTauxPublicationDebut(parameterStatsDoc.getLPTauxDebutLegisDatePubliBorneInf());
				exportDefaultParam.setTauxPublicationFin(parameterStatsDoc.getLPTauxDebutLegisDatePubliBorneSup());

				// Taux d'exécution des lois (au cours de la dernière session
				// parlementaire)
				exportDefaultParam.setLolfPromulgationDebut(parameterStatsDoc.getLPTauxSPDatePromulBorneInf());
				exportDefaultParam.setLolfPromulgationFin(parameterStatsDoc.getLPTauxSPDatePromulBorneSup());

				exportDefaultParam.setLolfPublicationDebut(parameterStatsDoc.getLPTauxSPDatePubliBorneInf());
				exportDefaultParam.setLolfPublicationFin(parameterStatsDoc.getLPTauxSPDatePubliBorneSup());

				// Bilan semestriel
				exportDefaultParam.setBilanPromulgationDebut(parameterStatsDoc.getLPBSDatePromulBorneInf());
				exportDefaultParam.setBilanPromulgationFin(parameterStatsDoc.getLPBSDatePromulBorneSup());

				exportDefaultParam.setBilanPublicationDebut(parameterStatsDoc.getLPBSDatePubliBorneInf());
				exportDefaultParam.setBilanPublicationFin(parameterStatsDoc.getLPBSDatePubliBorneSup());

				// Cas des Ordonnances
				// Bilan semestriel
				exportDefaultParam.setBilanPublicationDebutOrdo(parameterStatsDoc.getLPBSDatePubliOrdoBorneInf());
				exportDefaultParam.setBilanPublicationFinOrdo(parameterStatsDoc.getLPBSDatePubliOrdoBorneSup());

				exportDefaultParam.setBilanPublicationDebutOrdoDecret(parameterStatsDoc.getLPBSDatePubliDecretOrdoBorneInf());
				exportDefaultParam.setBilanPublicationFinOrdoDecret(parameterStatsDoc.getLPBSDatePubliDecretOrdoBorneSup());
				
				// Taux d'exécution des lois (depuis le début de la législature)
				exportDefaultParam.setTauxDLPublicationOrdosDebut(parameterStatsDoc.getLPTauxDebutLegisDatePubliOrdoBorneInf());
				exportDefaultParam.setTauxDLPublicationOrdosFin(parameterStatsDoc.getLPTauxDebutLegisDatePubliOrdoBorneSup());

				exportDefaultParam.setTauxDLPublicationDecretsOrdosDebut(parameterStatsDoc.getLPTauxDebutLegisDatePubliDecretOrdoBorneInf());
				exportDefaultParam.setTauxDLPublicationDecretsOrdosFin(parameterStatsDoc.getLPTauxDebutLegisDatePubliDecretOrdoBorneSup());

				// Taux d'exécution des lois (au cours de la dernière session
				// parlementaire)
				exportDefaultParam.setTauxSPPublicationOrdosDebut(parameterStatsDoc.getLPTauxSPDatePubliOrdoBorneInf());
				exportDefaultParam.setTauxSPPublicationOrdosFin(parameterStatsDoc.getLPTauxSPDatePubliOrdoBorneSup());

				exportDefaultParam.setTauxSPPublicationDecretsOrdosDebut(parameterStatsDoc.getLPTauxSPDatePubliDecretOrdoBorneInf());
				exportDefaultParam.setTauxSPPublicationDecretsOrdosFin(parameterStatsDoc.getLPTauxSPDatePubliDecretOrdoBorneSup());
				

				exportDefaultParam.setDateDebutLegislature(parameterStatsDoc.getLegislaturePrecedenteDateDebut());

				exportedLegislatures.clear();
				exportedLegislatures.add(getLegislaturePrecedenteLabel());
			}

			Calendar cal = Calendar.getInstance();
			setPeriodeParMois(true);
			setMois(cal.get(Calendar.MONTH) + 1);
			setAnnee(cal.get(Calendar.YEAR));

		}
	}

	public Boolean isAllowedToExportAN() {
		if (ssPrincipal != null) {
			return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_EXPORT_DATA_UPDATER);
		}
		return Boolean.FALSE;
	}

	public void downloadZip(List<String> legislatures) throws ClientException {

		ExportPANStat exportPanStatDoc = this.getExportPanStatDoc(legislatures);
		createZipFile(exportPanStatDoc);

	}

	private void createZipFile(ExportPANStat exportPanStatDoc) {
		String downloadFile = ActiviteNormativeReportsGenerator.getMainDirectory(exportPanStatDoc
				.isCurLegis(documentManager))
				+ ssPrincipal.getName()
				+ File.separator
				+ exportPanStatDoc.getName()
				+ ".zip";
		File file = new File(downloadFile);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.reset();

		response.setContentType("application/zip");
		response.addHeader("Content-Disposition", "attachment; filename=" + exportPanStatDoc.getName() + ".zip");
		response.setHeader("Set-Cookie", "fileDownload=true; path=/");

		OutputStream responseOutputStream;
		try {
			responseOutputStream = response.getOutputStream();
			InputStream zipInputStream = new FileInputStream(file);

			byte[] bytesBuffer = new byte[2048];
			int bytesRead;
			while ((bytesRead = zipInputStream.read(bytesBuffer)) > 0) {
				responseOutputStream.write(bytesBuffer, 0, bytesRead);
			}

			responseOutputStream.flush();

			zipInputStream.close();
			responseOutputStream.close();

		} catch (IOException e) {
			LOGGER.error(STLogEnumImpl.FAIL_CLOSE_STREAM_TEC, e);
		}

		facesContext.responseComplete();
	}

	private ExportPANStat findByLegislature(List<ExportPANStat> lstExports, String legislature, boolean exactMatch) {
		ExportPANStat export = null;
		boolean found = false;
		int i = 0;
		if (lstExports != null) {
			while (i < lstExports.size() && !found) {
				if (lstExports.get(i).getExportedLegislatures().contains(legislature)) {
					if (exactMatch && lstExports.get(i).getExportedLegislatures().size() == 1) {
						export = lstExports.get(i);
					} else if (!exactMatch) {
						export = lstExports.get(i);
					}

				}
				i++;
			}
		}

		return export;
	}

	public List<EspaceActiviteNormativeTreeNode> getExportTreeNodes() throws ClientException {
		List<EspaceActiviteNormativeTreeNode> nodes = new ArrayList<EspaceActiviteNormativeTreeNode>();

		List<ExportPANStat> lstAllExports = getAllExportPanStatDoc();
		StringBuilder builder = new StringBuilder("export-global-");
		String enCoursStatus = this.isExporting(lstAllExports) ? "disabled" : "enabled";
		ExportPANStat exportPrec = findByLegislature(lstAllExports, getLegislaturePrecedenteLabel(), true);

		builder.append(enCoursStatus);
		if (isAllowedToExportAN()) {
			EspaceActiviteNormativeTreeNode mainNode = new EspaceActiviteNormativeTreeNode("Export Global",
					builder.toString(), null);
			EspaceActiviteNormativeTreeNode precNode = new EspaceActiviteNormativeTreeNode(
					"Exporter la législature précédente", "export-prec-" + enCoursStatus, null);

			initChildrenForExport(mainNode, true, lstAllExports);
			mainNode.setOpened(true);
			nodes.add(mainNode);
			nodes.add(precNode);
		}

		if (exportPrec != null && !exportPrec.isExporting()) {
			EspaceActiviteNormativeTreeNode legisPrecNode = new EspaceActiviteNormativeTreeNode(
					"Publier la législature précédente", "publier-prec-legis", null);
			nodes.add(legisPrecNode);
			publicationLegisPrecVisible = true;
		}

		return nodes;
	}

	private void initChildrenForExport(EspaceActiviteNormativeTreeNode mainNode, boolean enCours,
			List<ExportPANStat> exportPanStatDoc) throws ClientException {
		String prefix = enCours ? "cur" : "prec";
		String detailTitle;
		String detailKey;

		if (exportPanStatDoc != null) {
			for (ExportPANStat stat : exportPanStatDoc) {
				if (stat.isExporting()) {
					detailTitle = "génération en cours...";
					detailKey = prefix + "export-detail-en-cours";
				} else {
					detailTitle = stat.getName();
					detailKey = prefix + "export-detail-terminee";
				}
				EspaceActiviteNormativeTreeNode childNode = new EspaceActiviteNormativeTreeNode(detailTitle, detailKey,
						null);
				if (stat.getExportedLegislatures() != null) {
					childNode.setLegislatures(stat.getExportedLegislatures());
				}
				childNode.setOpened(true);
				mainNode.getChildren().add(childNode);

			}
		}
	}

	public boolean adviseNodeOpened() {
		return true;
	}

	public void setParameterDisplayed(boolean parameterDisplayed) {
		this.parameterDisplayed = parameterDisplayed;
	}

	public boolean isParameterDisplayed() {
		return parameterDisplayed;
	}

	public void displayParameters() throws ClientException {

		loadParameterStatDoc();

		this.setParameterDisplayed(true);
		// Reset Parameters
		this.initExportParameters(true);
	}

	private void loadParameterStatDoc() {

		SolonEpgParametreService paramEPGservice = SolonEpgServiceLocator.getSolonEpgParametreService();

		try {
			this.parameterStatsDoc = paramEPGservice.getDocAnParametre(documentManager);

		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOCUMENT_FONC, e);
		}
	}

	private List<ExportPANStat> getAllExportPanStatDoc() throws ClientException {
		final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
		List<ExportPANStat> doc = anStatsService.getAllExportPanStatDocForUser(documentManager, ssPrincipal.getName());
		if (doc != null) {
			return doc;
		}
		return null;
	}

	private ExportPANStat getExportPanStatDoc(List<String> legislatures) throws ClientException {
		final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
		DocumentModel doc = anStatsService.getExportPanStatDoc(documentManager, ssPrincipal.getName(), legislatures);
		if (doc != null) {
			return doc.getAdapter(ExportPANStat.class);
		}
		return null;
	}

	public boolean isPeriodeParMois() {
		return periodeParMois;
	}

	public void setPeriodeParMois(boolean periodeParMois) {
		this.periodeParMois = periodeParMois;
	}

	public int getMois() {
		return mois;
	}

	public void setMois(int mois) {
		this.mois = mois;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public DocumentModel getParameterStatsDoc() {
		return parameterStatsDoc.getDocument();
	}

	public void setParameterStatsDoc(DocumentModel parameterStatsDoc) {
		if (parameterStatsDoc != null) {
			this.parameterStatsDoc = parameterStatsDoc.getAdapter(ParametrageAN.class);
		} else {
			this.parameterStatsDoc = null;
		}
	}

	public int getLegislatureCount() {

		if (this.parameterStatsDoc == null) {
			loadParameterStatDoc();
		}
		return this.parameterStatsDoc.getLegislatures().size();
	}

	public void publierLegisPrec() throws ClientException {
		EventProducer eventProducer = STServiceLocator.getEventProducer();
		Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
		eventProperties.put(SolonEpgANEventConstants.USER_PROPERTY, ssPrincipal.getName());
		eventProperties.put(SolonEpgANEventConstants.EXPORT_PAN_CURLEGISLATURE, Boolean.valueOf(false));

		InlineEventContext eventContext = new InlineEventContext(documentManager, ssPrincipal, eventProperties);
		eventProducer.fireEvent(eventContext.newEvent(SolonEpgANEventConstants.PUBLISH_STATS_EVENT));
		facesMessages.add(StatusMessage.Severity.INFO,
				resourcesAccessor.getMessages().get("activite.normative.publish.message"));
	}

	public PANExportParametreDTO getExportDefaultParam() {

		return exportDefaultParam;
	}

	public void setExportDefaultParam(PANExportParametreDTO exportDefaultParam) {
		this.exportDefaultParam = exportDefaultParam;
	}

	public List<String> getExportedLegislatures() {

		return exportedLegislatures;
	}

	public void setExportedLegislatures(List<String> exportedLegislatures) {
		this.exportedLegislatures = exportedLegislatures;
	}

	private String getLegislaturePrecedenteLabel() {
		if (parameterStatsDoc == null) {
			loadParameterStatDoc();
		}

		if (parameterStatsDoc.getLegislatures() != null) {
			if (parameterStatsDoc.getLegislatures().size() > 1
					&& parameterStatsDoc.getLegislatures().indexOf(parameterStatsDoc.getLegislatureEnCours()) > 0) {
				int lastIndex = parameterStatsDoc.getLegislatures().indexOf(parameterStatsDoc.getLegislatureEnCours()) - 1;
				String labelLegisPrec = parameterStatsDoc.getLegislatures().get(lastIndex);

				return labelLegisPrec;
			}
		}

		return "";
	}

	public Boolean isAllowedToEditParameters() {
		if (ssPrincipal != null) {
			return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_PARAM_LEGIS_UPDATER);
		}
		return Boolean.FALSE;
	}

	public Boolean isAllowedToPublishLegislature() {
		if (ssPrincipal != null) {
			return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_PUBLI_LEGIS_EXEC);
		}
		return Boolean.FALSE;
	}

	public Boolean isAllowedToViewJournalTechnique() {
		if (ssPrincipal != null) {
			return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_READER)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_READER)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_READER)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_READER)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_READER)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_READER)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_UPDATER)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_UPDATER)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_UPDATER)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_UPDATER)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_UPDATER)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_UPDATER)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_EXPORT_DATA_UPDATER)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_PARAM_LEGIS_UPDATER)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_PUBLI_LEGIS_EXEC)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE)
					|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_ORDONNANCE_MINISTERE);
		}
		return Boolean.FALSE;
	}

	public Boolean displayExportTreeNode() {

		return isAllowedToEditParameters() || (isAllowedToPublishLegislature() && publicationLegisPrecVisible)
				|| isAllowedToExportAN() || isAllowedToViewJournalTechnique();
	}

	public String exportLegisPrec() throws Exception {
		facesMessages.add(StatusMessage.Severity.INFO,
				resourcesAccessor.getMessages().get("activite.normative.exportLegisPrec.message"));
		exportPanStats(true);

		return null;
	}
}
