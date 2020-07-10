package fr.dila.solonepg.web.admin.changementgouvernement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;

import fr.dila.solonepg.api.administration.changementgouvernement.MigrationInfo;
import fr.dila.solonepg.api.administration.changementgouvernement.MigrationLoggerInfos;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.migration.MigrationDetailModel;
import fr.dila.solonepg.api.migration.MigrationLoggerModel;
import fr.dila.solonepg.api.service.ChangementGouvernementService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.admin.AdministrationActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.admin.SSMigrationManagerActionsBean;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;

/**
 * Bean seam de gestion du changement de gouvernement.
 * 
 * @author arn
 */
@Name("migrationManagerActions")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.APPLICATION + 1)
public class MigrationGouvernementActionsBean extends SSMigrationManagerActionsBean {

	/**
	 * Serial Version UID
	 */
	private static final long						serialVersionUID	= 2258935244775633811L;

	@In(create = true)
	protected transient AdministrationActionsBean	administrationActions;

	@In(create = true, required = true)
	private ContentViewActions						contentViewActions;

	/**
	 * Champ utilisé pour afficher les erreurs de validation
	 */
	protected String								errorName			= null;

	/**
	 * Champ correspondant au type de noeud organigramme à migrer : par défaut
	 */

	protected MigrationLoggerModel					migration;
	protected List<MigrationInfo>					migrationList;
	protected Long									currentLogId;
	
	/**
	 * Boolean permettant de cacher les icons d'état de la mugration (réinitialisation page)
	 */
	protected boolean 				affichageEtat;

	public boolean isAffichageEtat() {
		return affichageEtat;
	}

	public void setAffichageEtat(boolean affichageEtat) {
		this.affichageEtat = affichageEtat;
	}

	/**
	 * Default constructor
	 */
	public MigrationGouvernementActionsBean() {
		// do nothing
	}

	@PostConstruct
	public void initialize() throws ClientException {
		// Create migration by default
		List<MigrationLoggerModel> migrationEnCoursList = getMigrationEnCoursList();
		if (migrationEnCoursList == null || migrationEnCoursList.size() == 0) {
			ajouterMigration();
		} else {
			for (MigrationLoggerModel migrationLoggerModel : migrationEnCoursList) {
				migrationLoggerModel.assignMigrationInfo(this.ajouterMigration());
			}
		}
	}

	/**
	 * Supprimer la migration courante
	 * 
	 * @param migrationInfo
	 * @throws ClientException
	 */
	public void supprimerMigration(MigrationInfo migrationInfo) throws ClientException {
		getMigrationList().remove(migrationInfo);
		adjustOrdres();
	}

	/**
	 * Ajouter une nouvelle migration
	 */
	public MigrationInfo ajouterMigration() {
		String randomId = RandomStringUtils.randomAlphabetic(MigrationInfo.MIGRATION_ID_LENGTH);
		MigrationInfo migrationInfo = new MigrationInfo(randomId);
		getMigrationList().add(migrationInfo);
		adjustOrdres();
		return migrationInfo;
	}

	/**
	 * Renvoie la liste des migrations
	 * 
	 * @return
	 */
	public List<MigrationInfo> getMigrationList() {
		if (migrationList == null) {
			migrationList = new ArrayList<MigrationInfo>();
		}
		return migrationList;
	}

	private void adjustOrdres() {
		int index = 1;
		for (MigrationInfo migrationInfo : migrationList) {
			migrationInfo.setOrdre(index++);
		}
	}

	public boolean aucuneMigrationEnCours() throws ClientException {
		List<MigrationLoggerModel> migrationEnCours = getMigrationEnCoursList();
		return migrationEnCours == null || migrationEnCours.isEmpty();
	}

	/**
	 * Lancer les migrations
	 * 
	 * @throws ClientException
	 */
	public String lancerMigration() throws ClientException {
		errorName = null;
		affichageEtat = true;
		// Valider les migration avant de lancer le traitement
		if (!isValid() || !aucuneMigrationEnCours()) {
			return null;
		}

		// Clear cache before migration start
		// redmin 6012
		if (contentViewActions != null) {
			contentViewActions.resetAll();
		}

		final ChangementGouvernementService chgtGvtServ = SolonEpgServiceLocator.getChangementGouvernementService();
		final String principalName = ssPrincipal.getName();
		// Create model logger for each migration
		for (MigrationInfo migrationInfo : getMigrationList()) {
			MigrationLoggerModel migrationLogger = chgtGvtServ.createMigrationLogger(principalName);
			migrationInfo.assignLoggerInfos(migrationLogger);
			chgtGvtServ.flushMigrationLogger(migrationLogger);
			// Assign the logger id
			migrationInfo.setLoggerId(migrationLogger.getId());
		}
		adjustOrdres();

		// Post commit event
		EventProducer eventProducer = STServiceLocator.getEventProducer();
		Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
		eventProperties.put(SolonEpgEventConstant.MIGRATION_GVT_DATA, (ArrayList<MigrationInfo>) getMigrationList());

		InlineEventContext eventContext = new InlineEventContext(documentManager, ssPrincipal, eventProperties);
		eventProducer.fireEvent(eventContext.newEvent(SolonEpgEventConstant.MIGRATION_GVT_EVENT));

		facesMessages.add(StatusMessage.Severity.INFO,
				resourcesAccessor.getMessages().get("info.organigrammeManager.migration.started"));
		return null;
	}

	/**
	 * Controle l'accès à la vue correspondante
	 * 
	 */
	public boolean isAccessAuthorized() {
		SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
		return (ssPrincipal.isAdministrator() || ssPrincipal
				.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_CHANGEMENT_ORGANIGRAMME));
	}

	public String annuler() throws ClientException {
		return administrationActions.navigateToEspaceAdministration();
	}

	public MigrationLoggerInfos getMigrationLoggerInfos(String widgetName, Long loggerId) throws ClientException {
		MigrationLoggerInfos result = null;
		MigrationLoggerModel loggerModel = null;

		loggerModel = SolonEpgServiceLocator.getChangementGouvernementService().findMigrationById(loggerId);

		if (widgetName != null && loggerModel != null) {

			String status = loggerModel.getStatus();
			if ("deplacer_element_fils".equals(widgetName)) {
				result = new MigrationLoggerInfos(loggerModel.getElementsFils(), loggerModel.getElementsFils(),
						loggerModel.getElementsFils(), status);
			} else if ("migrer_fdr_etape".equals(widgetName)) {
				result = new MigrationLoggerInfos(loggerModel.getFdrStep(), loggerModel.getFdrStepCurrent(),
						loggerModel.getFdrStepCount(), status);
			} else if ("update_creator_poste".equals(widgetName)) {
				result = new MigrationLoggerInfos(loggerModel.getCreatorPoste(), loggerModel.getCreatorPosteCurrent(),
						loggerModel.getCreatorPosteCount(), status);
			} else if ("update_mailboxes".equals(widgetName)) {
				result = new MigrationLoggerInfos(loggerModel.getMailboxPoste(), loggerModel.getMailboxPosteCurrent(),
						loggerModel.getMailboxPosteCount(), status);
			} else if ("migre_fdr_modele".equals(widgetName)) {
				result = new MigrationLoggerInfos(loggerModel.getModeleFdr(), loggerModel.getModeleFdrCurrent(),
						loggerModel.getModeleFdrCount(), status);
			} else if ("reattribution_nor_ministere".equals(widgetName)) {
				result = new MigrationLoggerInfos(loggerModel.getNorDossierLanceInite(),
						loggerModel.getNorDossierLanceIniteCurrent(), loggerModel.getNorDossierLanceIniteCount(),
						status);
			} else if ("rattachement_ministere".equals(widgetName)) {
				result = new MigrationLoggerInfos(loggerModel.getNorDossierClos(),
						loggerModel.getNorDossierClosCurrent(), loggerModel.getNorDossierClosCount(), status);
			} else if ("migrer_bulletin_officiel".equals(widgetName)) {
				result = new MigrationLoggerInfos(loggerModel.getBulletinOfficiel(),
						loggerModel.getBulletinOfficielCurrent(), loggerModel.getBulletinOfficielCount(), status);
			} else if ("migrer_mots_cles".equals(widgetName)) {
				result = new MigrationLoggerInfos(loggerModel.getMotsCles(), loggerModel.getMotsClesCurrent(),
						loggerModel.getMotsClesCount(), status);
			} else if ("reattribution_nor_direction".equals(widgetName)) {
				result = new MigrationLoggerInfos(loggerModel.getNorDossierLanceInite(),
						loggerModel.getNorDossierLanceIniteCurrent(), loggerModel.getNorDossierLanceIniteCount(),
						status);
			} else if ("rattachement_direction".equals(widgetName)) {
				result = new MigrationLoggerInfos(loggerModel.getNorDossierClos(),
						loggerModel.getNorDossierClosCurrent(), loggerModel.getNorDossierClosCount(), status);
			} else if ("delete_node_direction".equals(widgetName)
					|| "delete_node_unite_structurelle".equals(widgetName) || "delete_node_poste".equals(widgetName)
					|| "delete_node_ministere".equals(widgetName)) {
				result = new MigrationLoggerInfos(loggerModel.getDeleteOld(), loggerModel.getDeleteOld(),
						loggerModel.getDeleteOld(), status);
			}
		}

		return result;
	}

	public boolean getDeleteOldElementOrganigramme(String migrationId) {
		MigrationInfo migrationInfo = getMigration(migrationId);
		return migrationInfo != null && migrationInfo.getDeleteOldElementOrganigramme();
	}

	public boolean getMigrationWithDossierClos(String migrationId) {
		MigrationInfo migrationInfo = getMigration(migrationId);
		return migrationInfo != null && migrationInfo.getMigrationWithDossierClos();
	}
	
	public boolean getMigrationModeleFdr(String migrationId) {
		MigrationInfo migrationInfo = getMigration(migrationId);
		return migrationInfo != null && migrationInfo.getMigrationModeleFdr();
	}

	private MigrationInfo getMigrationFromEvent(ValueChangeEvent event) throws Exception {
		HtmlSelectBooleanCheckbox checkbox = (HtmlSelectBooleanCheckbox) event.getSource();
		String checkBoxId = checkbox.getId();
		String migrationId = checkBoxId.substring(0, MigrationInfo.MIGRATION_ID_LENGTH);
		return getMigration(migrationId);
	}

	/**
	 * Listener appelé lors d'un changement de valeur dans le menu prm
	 * 
	 * @param event
	 */
	public void deleteOldElementOrgChangeListener(ValueChangeEvent event) throws Exception {
		if (event != null && event.getNewValue() != null && event.getNewValue() instanceof Boolean) {
			getMigrationFromEvent(event).setDeleteOldElementOrganigramme((Boolean) event.getNewValue());
		} else {
			facesMessages.add(StatusMessage.Severity.ERROR,
					"la demande de suppression de l'élément organigramme n'a pas été prise en compte !");
		}
	}

	/**
	 * Listener appelé lors d'un changement de valeur dans le menu prm
	 * 
	 * @param event
	 */
	public void selectMigrationWithDossierClosChangeListener(ValueChangeEvent event) throws Exception {
		if (event != null && event.getNewValue() != null && event.getNewValue() instanceof Boolean) {
			boolean migrationWithDossierClos = (Boolean) event.getNewValue();
			MigrationInfo migrationInfo = getMigrationFromEvent(event);
			migrationInfo.setMigrationWithDossierClos(migrationWithDossierClos);
			if (!migrationWithDossierClos) {
				migrationInfo.setDeleteOldElementOrganigramme(false);
			}
		} else {
			facesMessages.add(StatusMessage.Severity.ERROR,
					"la demande de migrer les dossiers clos n'a pas été prise en compte !");
		}
	}
	
	/**
	 * Listener appelé lors d'un changement de valeur dans le menu prm
	 * 
	 * @param event
	 */
	public void selectMigrationModeleFdrChangeListener(ValueChangeEvent event) throws Exception {
		if (event != null && event.getNewValue() != null && event.getNewValue() instanceof Boolean) {
			boolean migrationModeleFdr = (Boolean) event.getNewValue();
			MigrationInfo migrationInfo = getMigrationFromEvent(event);
			migrationInfo.setMigrationModeleFdr(migrationModeleFdr);
			if (!migrationModeleFdr) {
				migrationInfo.setDeleteOldElementOrganigramme(false);
			}
		} else {
			facesMessages.add(StatusMessage.Severity.ERROR,
					"la demande de migrer les dossiers clos n'a pas été prise en compte !");
		}
	}

	private boolean isValid() {
		for (MigrationInfo migrationInfo : getMigrationList()) {
			String errorMessage = migrationInfo.validateMigration();
			if (errorMessage != null) {
				String message = resourcesAccessor.getMessages().get(errorMessage);
				facesMessages.add(StatusMessage.Severity.WARN, message);
				errorName = message;
				return false;
			}
		}
		return true;
	}

	private MigrationInfo getMigration(String migrationId) {
		// Pas besoin de faire toute la boucle si on sait que l'identifiant est null ou vide
		if (StringUtils.isNotBlank(migrationId)) {
			for (MigrationInfo migrationInfo : getMigrationList()) {
				if (migrationInfo.getId().equals(migrationId)) {
					return migrationInfo;
				}
			}
		}
		return null;
	}

	public void resetElementOrganigramme(String migrationId) {
		MigrationInfo migrationInfo = getMigration(migrationId);
		if (migrationInfo != null) {
			migrationInfo.resetElementOrganigramme();
		}
	}

	public String getErrorName() {
		return errorName;
	}

	public List<MigrationLoggerModel> getMigrationEnCoursList() throws ClientException {
		return SolonEpgServiceLocator.getChangementGouvernementService().getMigrationWithoutEndDate();
	}

	public boolean candisplaySucceed(Long loggerId) throws ClientException {
		MigrationLoggerModel miggrationLooger = SolonEpgServiceLocator.getChangementGouvernementService()
				.findMigrationById(loggerId);
		return miggrationLooger != null && miggrationLooger.terminee();
	}

	public boolean candisplayFailure(Long loggerId) throws ClientException {
		MigrationLoggerModel miggrationLooger = SolonEpgServiceLocator.getChangementGouvernementService()
				.findMigrationById(loggerId);
		return miggrationLooger != null && miggrationLooger.failed();
	}

	public boolean candisplayWaiter(Long loggerId) throws ClientException {
		MigrationLoggerModel miggrationLooger = SolonEpgServiceLocator.getChangementGouvernementService()
				.findMigrationById(loggerId);
		return miggrationLooger != null && miggrationLooger.enCours();
	}

	public List<MigrationLoggerModel> getMigrationLoggerModelList() throws ClientException {
		return SolonEpgServiceLocator.getChangementGouvernementService().getMigrationLoggerModelList();
	}

	public List<MigrationDetailModel> getMigrationDetailModelList() throws ClientException {
		return SolonEpgServiceLocator.getChangementGouvernementService().getMigrationDetailModelList(currentLogId);
	}

	public Long getCurrentLogId() {
		return currentLogId;
	}

	public void setCurrentLogId(Long currentLogId) {
		this.currentLogId = currentLogId;
	}

	public String getLogMessage(MigrationLoggerModel migrationLoggerModel) throws ClientException {
		return SolonEpgServiceLocator.getChangementGouvernementService().getLogMessage(migrationLoggerModel);
	}
	
	// Reset l'affichage de la page / vide les champs à saisir
	public void resetData() {
		affichageEtat = false;
		for (MigrationInfo migrationInfo : migrationList) {
			migrationInfo.resetElementOrganigramme();
		}
	}

	public void sendMailMigrationDetails() {
		try {
			List<MigrationDetailModel> detailDocs = getMigrationDetailModelList();
			MigrationLoggerModel migrationLogger = SolonEpgServiceLocator.getChangementGouvernementService()
					.findMigrationById(currentLogId);
			ConfigService configService = STServiceLocator.getConfigService();
			String recipient = configService.getValue("mail.migration.details");
			if (StringUtil.isBlank(recipient)) {
				facesMessages.add(StatusMessage.Severity.WARN,
						resourcesAccessor.getMessages().get("feedback.solonepg.no.mail"));
			} else {
				// Post commit event
				EventProducer eventProducer = STServiceLocator.getEventProducer();
				Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
				eventProperties.put(SolonEpgEventConstant.SEND_MIGRATION_DETAILS_DETAILS_PROPERTY,
						(Serializable) detailDocs);
				eventProperties.put(SolonEpgEventConstant.SEND_MIGRATION_DETAILS_RECIPIENT_PROPERTY, recipient);
				eventProperties.put(SolonEpgEventConstant.SEND_MIGRATION_DETAILS_LOGGER_PROPERTY,
						(Serializable) migrationLogger);

				InlineEventContext eventContext = new InlineEventContext(documentManager, ssPrincipal, eventProperties);
				eventProducer.fireEvent(eventContext.newEvent(SolonEpgEventConstant.SEND_MIGRATION_DETAILS_EVENT));

				facesMessages.add(StatusMessage.Severity.INFO,
						resourcesAccessor.getMessages().get("feedback.solonepg.export"));
			}
		} catch (ClientException e) {
			facesMessages.add(StatusMessage.Severity.ERROR,
					resourcesAccessor.getMessages().get("Erreur dans la génération de l'export"));
		}
	}
}
