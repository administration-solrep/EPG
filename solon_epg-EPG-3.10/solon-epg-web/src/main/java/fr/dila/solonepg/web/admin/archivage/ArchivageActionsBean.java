package fr.dila.solonepg.web.admin.archivage;

import java.io.OutputStream;
import java.io.Serializable;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.platform.audit.web.listener.ejb.ContentHistoryActionsBean;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.api.Framework;

import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.adamant.service.DossierExtractionService;
import fr.dila.solonepg.adamant.service.ExtractionLotService;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ArchiveService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.admin.AdministrationActionsBean;
import fr.dila.solonepg.web.archivage.ArchiveActionsBean;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.feuilleroute.DocumentRoutingActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;

@Name("archivageActions")
@Scope(ScopeType.CONVERSATION)
public class ArchivageActionsBean implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -4501028632976178346L;

	private static final STLogger					LOGGER				= STLogFactory.getLog(ArchivageActionsBean.class);

	@In(create = true, required = true)
	protected transient CoreSession					documentManager;
	@In(create = true)
	protected transient DocumentsListsManager		documentsListsManager;
	@In(create = true)
	protected transient NuxeoPrincipal				currentUser;
	@In(create = true)
	protected transient AdministrationActionsBean	administrationActions;
	@In(create = true, required = false)
	protected transient ResourcesAccessor			resourcesAccessor;
	@In(create = true, required = false)
	protected transient FacesMessages				facesMessages;
	/**    
	 * 
	 */
	public String									userName;

	/**
	 * 
	 */
	public String									password;
	/**
	 * 
	 */
	public String									typeAction;
	/**
	 * 
	 */
	public String									authenticationSuccess;

	/**
	 * 
	 */
	public Integer									nbMois;

	@In(create = true, required = false)
	protected transient DocumentRoutingActionsBean	routingActions;

	@In(create = true, required = false)
	protected transient ArchiveActionsBean			archiveActionsBean;

	protected transient Date						startDate;

	protected transient Date						endDate;

	protected transient String						status;

	/**
	 * utilisé pour récupérer les informations du journal
	 */
	@In(create = true, required = false)
	protected transient ContentHistoryActionsBean	contentHistoryActions;

	public boolean isButtonToDisplay() {
		return !documentsListsManager.isWorkingListEmpty(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
	}

	/**
	 * Methode qui retire le(s) dossiers selectionnees de la liste des dossiers a archiver
	 * 
	 * @return
	 * @throws ClientException
	 */
	public String retirerSelection() throws ClientException {
		// TODO: handle exception
		String result = "";
		List<DocumentModel> docs = documentsListsManager
				.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
		for (DocumentModel dossierDoc : docs) {
			dossierService.retirerDossierFromListCandidatsArchivageIntermediaireUnrestricted(documentManager,
					dossierDoc, nbMois);
		}
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		result = administrationActions.navigateToArchivageIntermediaire();
		documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		return result;
	}

	/**
	 * Valider le login de l'utilisateur
	 * 
	 * @return
	 * @throws ClientException
	 */
	public String validerRetireSelection() throws ClientException {
		String result = "";
		authentifier();
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		result = administrationActions.navigateToArchivageIntermediaire();

		return result;
	}

	/**
	 * authentifier l'acces de l'utilisateur saisie par l'utilisateur
	 * 
	 * @return
	 */
	public boolean authentifier() {
		boolean isAuthentifiedOk = false;
		if (currentUser == null) {
			isAuthentifiedOk = false;
		} else {
			UserManager userManager = STServiceLocator.getUserManager();
			if (userName != null && password != null && userManager.authenticate(userName, password)) {
				isAuthentifiedOk = true;
			}
		}
		if (isAuthentifiedOk) {
			authenticationSuccess = "check";
		} else {
			authenticationSuccess = "uncheck";
		}
		return isAuthentifiedOk;
	}

	public String getUserName() {
		userName = currentUser.getName();
		return userName;
	}

	/**
	 * Action qui verse le(s) dossier(s) selectionner vers l'archivage
	 * 
	 * @return le vue de l'archivage intermediaire
	 * @throws ClientException
	 */
	public String verserSelection() throws ClientException {
		// TODO: handle exception
		String result = "";
		List<DocumentModel> docs = documentsListsManager
				.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
		for (DocumentModel dossierDoc : docs) {
			dossierService.verserDossierDansBaseArchivageIntermediaireUnrestricted(documentManager, dossierDoc);
		}
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		result = administrationActions.navigateToArchivageIntermediaire();
		documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		return result;
	}

	/**
	 * Genere un lot d'extraction pour l'ensemble des dossiers sélectionnés.
	 * 
	 * @return le vue de l'archivage definitive
	 * @throws ClientException 
	 * @throws Exception
	 */
	public String genererArchive() throws ClientException {

		String result = "";
		List<DocumentModel> docs = documentsListsManager
				.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		
		List<String> dossiersToExportIds = new ArrayList<String>();
		DossierExtractionService dossierExtractionService = SolonEpgAdamantServiceLocator.getDossierExtractionService();
		
		for (DocumentModel documentModel : docs) {
			try {
				dossierExtractionService.checkFile(documentModel, documentManager);
				dossiersToExportIds.add(documentModel.getId());
			} catch (Exception e) {
				facesMessages.add(StatusMessage.Severity.INFO,"Dossier n°" 
						+ documentModel.getAdapter(Dossier.class).getNumeroNor() + " "
						+ e.getMessage());
			}
		}
		
		SolonEpgAdamantServiceLocator.getExtractionLotService().createAndAssignLotToDocumentsByIds(dossiersToExportIds,
				documentManager);
		
		result = administrationActions.navigateToArchivageDefinitive();
		documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);

		facesMessages.add(StatusMessage.Severity.INFO,
				resourcesAccessor.getMessages().get("command.admin.archivage.generer.success"));
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		return result;
	}

	

	/**
	 * Retirer les dossiers selectionnes de la liste a archiver
	 * 
	 * @return le vue de l'archivage definitive
	 * @throws ClientException
	 */
	public String retirerArchivageDefinitive() throws Exception {
		String result = "";
		final ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();
		final List<DocumentModel> docs = documentsListsManager
				.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		archiveService.retirerArchivageDefinitive(docs, documentManager);
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		result = administrationActions.navigateToArchivageDefinitive();
		documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		return result;
	}

	/**
	 * getHttpServletResponse
	 * 
	 * @return
	 */
	private static HttpServletResponse getHttpServletResponse() {
		ServletResponse response = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			response = (ServletResponse) facesContext.getExternalContext().getResponse();
		}

		if (response != null && response instanceof HttpServletResponse) {
			return (HttpServletResponse) response;
		}
		return null;
	}

	/**
	 * Generer un bordereau d'elimination pour les dossiers selectionnes de la liste a archiver
	 * 
	 * @return le vue de l'archivage definitive
	 * @throws ClientException
	 */
	public void genererBordereau() throws Exception {
		// TODO: handle exception

		final ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();
		// Export PDF du bordereau d'archivage
		HttpServletResponse response = getHttpServletResponse();
		if (response == null) {
			return;
		}

		response.reset();
		OutputStream outputStream = response.getOutputStream();
		final List<DocumentModel> docs = documentsListsManager
				.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		String[] docIds = new String[docs.size()];
		int index = 0;
		for (DocumentModel dossierDoc : docs) {
			docIds[index++] = dossierDoc.getId();
		}

		archiveService.generateListeEliminationPdf(outputStream, docIds);

		response.setHeader("Content-Type", "application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=Bordereau_elimination.pdf");

		FacesContext.getCurrentInstance().responseComplete();
	}

	/**
	 * Suppression des dossiers selectionnes de la liste a archiver definitivement
	 * 
	 * @return le vue de l'archivage definitive
	 * @throws ClientException
	 */
	public String archivageDefinitiveSuppression() throws ClientException {
		String result = "";
		final ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();
		if (authentifier()) {
			final List<DocumentModel> docs = documentsListsManager
					.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
			final Principal principal = documentManager.getPrincipal();
			// on lance les suppressions des dossiers sans vérifier les droits
			new UnrestrictedSessionRunner(documentManager) {
				@Override
				public void run() throws ClientException {
					for (DocumentModel dossierDoc : docs) {
						try {
							archiveService.supprimerDossier(session, dossierDoc, principal);
						} catch (Exception e) {
							throw new ClientException(e);
						}
					}
				}
			}.runUnrestricted();

			documentManager.save();

			// Reset de la liste de selection
			// documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
			// contentViewActions.reset(SolonEpgContentView.SUPPRESSION_DOSSIER);
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
			result = administrationActions.navigateToArchivageDefinitive();
			documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		}
		return result;

	}

	/**
	 * Function utiliser afin d'identifier si le button de suppression dans l'ecran de l'archivage definitive est a
	 * afficher
	 * 
	 * @return valider si le(s) selection(s) sont archiver ou un bordereau d'elimination est gerer
	 */
	public boolean isDefinitiveArchived() {
		// TODO: handle exception
		return true;

	}

	/**
	 * Controle l'accès à l'archivage intermédiaire
	 * 
	 */
	public boolean isAccessIntermediaireAuthorized() {
		SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
		return (ssPrincipal.isAdministrator() || ssPrincipal
				.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_ARCHIVAGE_INTERMEDIAIRE_READER));
	}

	/**
	 * Controle l'accès à l'archivage définitif
	 * 
	 */
	public boolean isAccessDefinitifAuthorized() {
		SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
		return (ssPrincipal.isAdministrator() || ssPrincipal
				.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_ARCHIVAGE_DEFINITIF_READER));
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTypeAction() {
		return typeAction;
	}

	public void setTypeAction(String typeAction) {
		this.typeAction = typeAction;
	}

	public void setToDoAction(String typeAction) {
		this.typeAction = typeAction;
		this.authenticationSuccess = null;
	}

	public String getAuthenticationSuccess() {
		return authenticationSuccess;
	}

	public void setAuthenticationSuccess(String authenticationSuccess) {
		this.authenticationSuccess = authenticationSuccess;
	}

	public String resetAuthenticationSuccess() {
		authenticationSuccess = null;
		return "check";
	}

	public Integer getNbMois() {
		return nbMois;
	}

	public void setNbMois(Integer nbMois) {
		this.nbMois = nbMois;
	}

	/**
	 * Controle l'accès à l'archivage ADAMANT
	 * 
	 */
	public boolean isAccessAdamantAuthorized() {
		SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
		return (ssPrincipal.isAdministrator() || ssPrincipal
				.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_ARCHIVAGE_ADAMANT_READER));
	}

	public void creerLotArchivage() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int delaisEligibilite = SolonEpgAdamantServiceLocator.getDossierExtractionService().getDelaisEligibilite(documentManager);
		List<String> listIds = new ArrayList<String>();
		
		if (startDate != null && endDate != null) {
			String formatStartDate = sdf.format(removeMonthToDate(startDate, delaisEligibilite));
			String formatEndDate = sdf.format(removeMonthToDate(endDate, delaisEligibilite));
			if(!status.isEmpty()) {
				listIds = doQueryListIdDosEligible(status, formatStartDate, formatEndDate);
			} else {
				listIds.addAll(doQueryListIdDosEligible(VocabularyConstants.STATUT_PUBLIE, formatStartDate, formatEndDate));
				listIds.addAll(doQueryListIdDosEligible(VocabularyConstants.STATUT_CLOS, formatStartDate, formatEndDate));
				listIds.addAll(doQueryListIdDosEligible(VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION, formatStartDate, formatEndDate));
			}

			ExtractionLotService extractionLotService = Framework.getService(ExtractionLotService.class);
			extractionLotService.createAndAssignLotToDocumentsByIds(listIds, documentManager);
		}
		initialiserToutLesParametres();
	}
	
	public String creerLotFromArchivageDefinitif() throws Exception {
		
		DossierExtractionService dossierExtractionService = Framework.getService(DossierExtractionService.class);
		List<String> listIds = new ArrayList<String>();
		List<DocumentModel> docs = documentsListsManager.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		for (DocumentModel documentModel : docs) {
			Dossier dossier = documentModel.getAdapter(Dossier.class);
			if (dossierExtractionService.isDocumentEligible(dossier, documentManager)){
				listIds.add(documentModel.getId());
			} else {
				facesMessages.add(StatusMessage.Severity.INFO,
						"Le dossier n°" + dossier.getNumeroNor() + " n'a pas été archivé car non éligible");
			}
		}
		final ExtractionLotService extractionLotService = Framework.getService(ExtractionLotService.class);
		extractionLotService.createAndAssignLotToDocumentsByIds(listIds, documentManager);
		
		String result = administrationActions.navigateToArchivageDefinitive();
		documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		return result;
	}

	/**
	 * Compte le nombre de dossier Eligible à l'extraction dans l'intervalle choisi 
	 * @throws ClientException 
	 */
	public int compterDosPourExtraction() throws ClientException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int delaisEligibilite = SolonEpgAdamantServiceLocator.getDossierExtractionService().getDelaisEligibilite(documentManager);
		int countDossiers = 0;

		if (startDate != null && endDate != null) {
			String formatStartDate = sdf.format(removeMonthToDate(startDate, delaisEligibilite));
			String formatEndDate = sdf.format(removeMonthToDate(endDate, delaisEligibilite));
			
			if (!status.isEmpty()) {
				return doQueryCountDosEligible(status, formatStartDate, formatEndDate);
			} else {
				countDossiers += doQueryCountDosEligible(VocabularyConstants.STATUT_PUBLIE, formatStartDate, formatEndDate);
				countDossiers += doQueryCountDosEligible(VocabularyConstants.STATUT_CLOS, formatStartDate, formatEndDate);
				countDossiers += doQueryCountDosEligible(VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION, formatStartDate, formatEndDate);
				return countDossiers;
			}
		} else {
			return 0;
		}
	}
	
	// retour la query count sous forme de string en fonction du statut et des dates données 
	public Integer doQueryCountDosEligible(String statut, String startDate, String endDate) throws ClientException
	{
		String query = "";
		Integer count = 0;
		if(statut != null && statut.equals(VocabularyConstants.STATUT_PUBLIE)) {
			query = "SELECT count(distinct d.id) FROM dossier_solon_epg d "
					+ "INNER JOIN retour_dila r on d.id=r.id "
					+ "WHERE d.statutarchivage IN "
					+ "('" + VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE
					+ "','" + VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE
					+ "','" + VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE + "')"
					+ " AND d.id_extraction_lot IS NULL"
					+ " AND d.statut = " + VocabularyConstants.STATUT_PUBLIE 
					+ " AND r.dateparutionjorf BETWEEN DATE '" + startDate + "' AND DATE '" + endDate + "'";
		} else if (statut != null && statut.equals(VocabularyConstants.STATUT_CLOS)) {
			query = "SELECT count(distinct d.id) FROM dossier_solon_epg d "
					+ "LEFT OUTER JOIN (SELECT MAX(lc.LOG_EVENT_DATE) AS EVENTDATE,	lc.LOG_DOC_UUID " 
					+ "FROM NXP_LOGS lc GROUP BY lc.LOG_DOC_UUID) llc ON llc.LOG_DOC_UUID = d.ID "
					+ "WHERE d.STATUT = '" + VocabularyConstants.STATUT_CLOS + "' AND d.STATUTARCHIVAGE IN "
					+ "('" + VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE
					+ "','" + VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE
					+ "','" + VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE + "') "
					+ "AND d.ID_EXTRACTION_LOT IS NULL AND llc.EVENTDATE BETWEEN DATE '" + startDate + "' AND DATE '" + endDate + "'";
		} else if (statut != null && statut.equals(VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION)) {
			query = "SELECT count(distinct d.id) FROM dossier_solon_epg d "
					+ "LEFT OUTER JOIN dos_rubriques u ON u.id = d.id AND u.item = 'REPRISE' "
					+ "LEFT OUTER JOIN (SELECT MIN(lt.LOG_EVENT_DATE) AS EVENTDATETERMINE,	lt.LOG_DOC_UUID " 
					+ "FROM NXP_LOGS lt WHERE lt.LOG_EVENT_COMMENT = 'label.journal.comment.terminerDossierSansPublication' "
					+ "GROUP BY lt.LOG_DOC_UUID) llt ON llt.LOG_DOC_UUID = d.ID "
					+ "WHERE d.STATUT = '" + VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION + "' AND d.STATUTARCHIVAGE IN "
					+ "('" + VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE
					+ "','" + VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE
					+ "','" + VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE + "') "
					+ "AND d.ID_EXTRACTION_LOT IS NULL AND ((u.ITEM IS NOT NULL "
					+ "AND (llt.EVENTDATETERMINE IS NULL OR llt.EVENTDATETERMINE < d.CREATED) "
					+ "AND d.created BETWEEN DATE '" + startDate + "' AND DATE '" + endDate + "') "
					+ "OR (u.ITEM IS NULL AND llt.EVENTDATETERMINE BETWEEN DATE '" + startDate + "' AND DATE '" + endDate + "'))";
		} 
		
		IterableQueryResult resCount = QueryUtils.doSqlQuery(documentManager, new String[]{FlexibleQueryMaker.COL_COUNT}, query, new Object[] {});
		
		if (resCount != null) {
			Iterator<Map<String, Serializable>> iterator = resCount.iterator();
			if (iterator.hasNext()) {
				Map<String, Serializable> row = iterator.next();
				count = ((Long) row.get(FlexibleQueryMaker.COL_COUNT)).intValue();
			}
			resCount.close();
		}
		return count;
	}
	
	public List<String> doQueryListIdDosEligible(String statut, String startDate, String endDate) throws ClientException
	{
		String query = "";
		if(statut != null && statut.equals(VocabularyConstants.STATUT_PUBLIE)) {
			query = "SELECT distinct d.id FROM dossier_solon_epg d "
					+ "INNER JOIN retour_dila r on d.id=r.id "
					+ "WHERE d.statutarchivage IN "
					+ "('" + VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE
					+ "','" + VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE
					+ "','" + VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE + "')"
					+ " AND d.id_extraction_lot IS NULL"
					+ " AND d.statut = " + VocabularyConstants.STATUT_PUBLIE 
					+ " AND r.dateparutionjorf BETWEEN DATE '" + startDate + "' AND DATE '" + endDate + "'";
		} else if (statut != null && statut.equals(VocabularyConstants.STATUT_CLOS)) {
			query = "SELECT distinct d.id FROM dossier_solon_epg d "
					+ "LEFT OUTER JOIN (SELECT MAX(lc.LOG_EVENT_DATE) AS EVENTDATE,	lc.LOG_DOC_UUID " 
					+ "FROM NXP_LOGS lc GROUP BY lc.LOG_DOC_UUID) llc ON llc.LOG_DOC_UUID = d.ID "
					+ "WHERE d.STATUT = '" + VocabularyConstants.STATUT_CLOS + "' AND d.STATUTARCHIVAGE IN "
					+ "('" + VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE
					+ "','" + VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE
					+ "','" + VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE + "') "
					+ "AND d.ID_EXTRACTION_LOT IS NULL AND llc.EVENTDATE BETWEEN DATE '" + startDate + "' AND DATE '" + endDate + "'";
		} else if (statut != null && statut.equals(VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION)) {
			query = "SELECT distinct d.id FROM dossier_solon_epg d "
					+ "LEFT OUTER JOIN dos_rubriques u ON u.id = d.id AND u.item = 'REPRISE' "
					+ "LEFT OUTER JOIN (SELECT MIN(lt.LOG_EVENT_DATE) AS EVENTDATETERMINE,	lt.LOG_DOC_UUID " 
					+ "FROM NXP_LOGS lt WHERE lt.LOG_EVENT_COMMENT = 'label.journal.comment.terminerDossierSansPublication' "
					+ "GROUP BY lt.LOG_DOC_UUID) llt ON llt.LOG_DOC_UUID = d.ID "
					+ "WHERE d.STATUT = '" + VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION + "' AND d.STATUTARCHIVAGE IN "
					+ "('" + VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE
					+ "','" + VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE
					+ "','" + VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE + "') "
					+ "AND d.ID_EXTRACTION_LOT IS NULL AND ((u.ITEM IS NOT NULL "
					+ "AND (llt.EVENTDATETERMINE IS NULL OR llt.EVENTDATETERMINE < d.CREATED) "
					+ "AND d.created BETWEEN DATE '" + startDate + "' AND DATE '" + endDate + "') "
					+ "OR (u.ITEM IS NULL AND llt.EVENTDATETERMINE BETWEEN DATE '" + startDate + "' AND DATE '" + endDate + "'))";
		}
		
		IterableQueryResult res = QueryUtils.doSqlQuery(documentManager, new String[] { FlexibleQueryMaker.COL_ID }, query, new Object[] {});

		List<String> listIds = new ArrayList<String>();
		if (res != null) {
			Iterator<Map<String, Serializable>> iterator = res.iterator();
			if (iterator.hasNext()) {
				while (iterator.hasNext()) {
					Map<String, Serializable> row = iterator.next();
					String id = (String) row.get(FlexibleQueryMaker.COL_ID);
					listIds.add(id);
				}
			}
		}
		
		return listIds;
	}
	
	/**
	 * Ajouter un nombre de mois a une date
	 * @param date
	 * @param month
	 * @return
	 */
	public Date removeMonthToDate(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month*-1);
		return calendar.getTime();
	}

	/**
	 * Retourne la list des Dossiers éligible à l'extraction dans m'intervale de date
	 * @throws ClientException 
	 */
	public List<DocumentModel> findDossiersEligible(final CoreSession session)
			throws ClientException {

		return null;
	}

	/**
	 * Retourne le nombre de dossier en cours d'extraction
	 * @throws ClientException 
	 */
	public int getNbDosExtracting() throws ClientException {

		String query;
		int nbDossier = 0;

		query = "SELECT count(*) FROM dossier_solon_epg d "
				+"WHERE d.statutarchivage = " + VocabularyConstants.STATUT_ARCHIVAGE_EXTRACTION_EN_COURS;

		IterableQueryResult resCount = QueryUtils.doSqlQuery(documentManager, new String[]{FlexibleQueryMaker.COL_COUNT}, query, new Object[] {});

		if (resCount != null) {
			Iterator<Map<String, Serializable>> iterator = resCount.iterator();
			if (iterator.hasNext()) {
				Map<String, Serializable> row = iterator.next();
				nbDossier = ((Long) row.get(FlexibleQueryMaker.COL_COUNT)).intValue();
			}
			resCount.close();
		}
		return nbDossier;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void annulerExtraction() {
		initialiserToutLesParametres();
	}

	private void initialiserToutLesParametres() {
		startDate = null;
		endDate = null;
		status = null;
	}
}
