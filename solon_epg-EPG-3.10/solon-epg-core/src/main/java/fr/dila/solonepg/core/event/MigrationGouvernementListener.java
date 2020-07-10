package fr.dila.solonepg.core.event;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.PostCommitEventListener;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.administration.changementgouvernement.MigrationInfo;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.migration.MigrationDetailModel;
import fr.dila.solonepg.api.migration.MigrationLoggerModel;
import fr.dila.solonepg.api.service.ChangementGouvernementService;
import fr.dila.solonepg.api.service.DossierDistributionService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;

/**
 * Gestion de la migration d'un gouvernement.
 * 
 * @author asatre
 */
public class MigrationGouvernementListener implements PostCommitEventListener {

	private static final Log	log	= LogFactory.getLog(MigrationGouvernementListener.class);

	@Override
	public void handleEvent(final EventBundle events) throws ClientException {
		if (events.containsEventName(SolonEpgEventConstant.MIGRATION_GVT_EVENT)) {
			for (final Event event : events) {
				handleEvent(event);
			}
		}
	}

	private void handleEvent(final Event event) throws ClientException {

		if (!event.getName().equals(SolonEpgEventConstant.MIGRATION_GVT_EVENT)) {
			return;
		}

		final CoreSession coreSession = event.getContext().getCoreSession();
		final Map<String, Serializable> properties = event.getContext().getProperties();
		log.info("Début migration");
		@SuppressWarnings("unchecked")
		List<MigrationInfo> migrationList = (List<MigrationInfo>) properties
				.get(SolonEpgEventConstant.MIGRATION_GVT_DATA);

		for (MigrationInfo migrationInfo : migrationList) {
			this.lancerMigration(migrationInfo, coreSession);
			TransactionHelper.commitOrRollbackTransaction();
			TransactionHelper.startTransaction();
			// Envoi par mail de l'export des détails de la migration
			List<MigrationDetailModel> detailDocs = SolonEpgServiceLocator.getChangementGouvernementService()
					.getMigrationDetailModelList(migrationInfo.getLoggerId());
			MigrationLoggerModel migrationLogger = SolonEpgServiceLocator.getChangementGouvernementService()
					.findMigrationById(migrationInfo.getLoggerId());
			ConfigService configService = STServiceLocator.getConfigService();
			String recipient = configService.getValue("mail.migration.details");
			if (!StringUtil.isBlank(recipient)) {
				// Post commit event
				EventProducer eventProducer = STServiceLocator.getEventProducer();
				Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
				eventProperties.put(SolonEpgEventConstant.SEND_MIGRATION_DETAILS_DETAILS_PROPERTY,
						(Serializable) detailDocs);
				eventProperties.put(SolonEpgEventConstant.SEND_MIGRATION_DETAILS_RECIPIENT_PROPERTY, recipient);
				eventProperties.put(SolonEpgEventConstant.SEND_MIGRATION_DETAILS_LOGGER_PROPERTY, migrationLogger);

				InlineEventContext eventContext = new InlineEventContext(coreSession, coreSession.getPrincipal(),
						eventProperties);
				eventProducer.fireEvent(eventContext.newEvent(SolonEpgEventConstant.SEND_MIGRATION_DETAILS_EVENT));

				log.info(
						"La génération de l'export a commencé. Il sera transmis par courrier électronique dès qu'il sera disponible.");
			}
		}
		coreSession.save();
		log.info("Fin de la migration");

	}

	private void lancerMigration(MigrationInfo migrationInfo, final CoreSession coreSession) throws ClientException {
		final ChangementGouvernementService chgtGouvService = SolonEpgServiceLocator.getChangementGouvernementService();
		MigrationLoggerModel model = null;
		try {
			model = chgtGouvService.findMigrationById(migrationInfo.getLoggerId());

			final String typeMigration = migrationInfo.getTypeMigration();
			final String oldElementOrganigramme = migrationInfo.getOldElementOrganigramme();
			final String newElementOrganigramme = migrationInfo.getNewElementOrganigramme();
			final Boolean deleteOldElementOrganigramme = migrationInfo.getDeleteOldElementOrganigramme();
			Serializable oldMinistere = migrationInfo.getOldMinistereElementOrganigramme();
			final String oldMinistereElementOrganigramme = oldMinistere != null ? (String) oldMinistere : null;
			Serializable newMinistere = migrationInfo.getNewMinistereElementOrganigramme();
			final String newMinistereElementOrganigramme = newMinistere != null ? (String) newMinistere : null;
			final Boolean migrationWithDossierClos = migrationInfo.getMigrationWithDossierClos();
			final Boolean migrationModeleFdr = migrationInfo.getMigrationModeleFdr();

			final MigrationLoggerModel migrationLoggerModel = model;

			new UnrestrictedSessionRunner(coreSession) {

				@Override
				public void run() throws ClientException {

					try {

						migrationLoggerModel.setStatus(SolonEpgConstant.EN_COURS_STATUS);
						chgtGouvService.flushMigrationLogger(migrationLoggerModel);

						if (typeMigration.equals(VocabularyConstants.MIN_TYPE)) {
							migrationMinistere(session, oldElementOrganigramme, newElementOrganigramme,
									deleteOldElementOrganigramme, migrationLoggerModel, migrationWithDossierClos, migrationModeleFdr);

						} else if (typeMigration.equals(VocabularyConstants.DIR_TYPE)) {
							migrationDirection(session, oldMinistereElementOrganigramme,
									newMinistereElementOrganigramme, oldElementOrganigramme, newElementOrganigramme,
									deleteOldElementOrganigramme, migrationLoggerModel, migrationWithDossierClos, migrationModeleFdr);

						} else if (typeMigration.equals(VocabularyConstants.UST_TYPE)) {
							migrationUniteStructurelle(session, oldElementOrganigramme, newElementOrganigramme,
									deleteOldElementOrganigramme, migrationLoggerModel);

						} else if (typeMigration.equals(VocabularyConstants.POSTE_TYPE)) {
							migrationPoste(coreSession, oldElementOrganigramme, newElementOrganigramme,
									deleteOldElementOrganigramme, migrationLoggerModel);

						}

						migrationLoggerModel.setEndDate(Calendar.getInstance().getTime());
						migrationLoggerModel.setStatus(SolonEpgConstant.TERMINEE_STATUS);
						chgtGouvService.flushMigrationLogger(migrationLoggerModel);

					} catch (Exception e) {
						log.error(e.getMessage(), e);
						migrationLoggerModel.setStatus(SolonEpgConstant.FAILED_STATUS);
					}
				}

			}.runUnrestricted();

			log.info("Fin de la migration " + oldElementOrganigramme + " vers " + newElementOrganigramme);
		} finally {
			coreSession.save();
			if (model != null) {
				model.setEndDate(Calendar.getInstance().getTime());
				chgtGouvService.flushMigrationLogger(model);
			}
		}

	}

	/**
	 * Migration d'un ministère.
	 * 
	 * @param migrationLoggerModel
	 * 
	 * @param changementGouvernementService
	 * @param epgOrganigrammeService
	 * @param migrationWithDossierClos
	 *            Si vrai, on migre les dossiers clos
	 * @throws ClientException
	 */
	private void migrationMinistere(CoreSession session, String oldElementOrganigramme, String newElementOrganigramme,
			Boolean deleteOldElementOrganigramme, MigrationLoggerModel migrationLoggerModel,
			Boolean migrationWithDossierClos, Boolean migrationModeleFdr) throws ClientException {

		final ChangementGouvernementService changementGouvernementService = SolonEpgServiceLocator
				.getChangementGouvernementService();
		final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
		// Si la migration des modèles de feuille de route n'est pas coché : on doit désactiver les modèles. 
		Boolean desactivateModeleFdr = !migrationModeleFdr;

		EntiteNode oldMinistereNode = ministeresService.getEntiteNode(oldElementOrganigramme);
		EntiteNode newMinistereNode = ministeresService.getEntiteNode(newElementOrganigramme);

		// migration des éléments fils.
		changementGouvernementService.migrerElementsFils(session, oldMinistereNode, newMinistereNode,
				migrationLoggerModel);

		// migration des modèles de feuilles de route.
		changementGouvernementService.migrerModeleFdrMinistere(session, oldMinistereNode, newMinistereNode,
				migrationLoggerModel, desactivateModeleFdr);
		session.save();

		// Réattribution du Nor.
		Map<String, String> norReattributionsPubConj = new HashMap<String, String>();
		changementGouvernementService.reattribuerNorDossierMinistere(session, oldMinistereNode, newMinistereNode,
				migrationLoggerModel, norReattributionsPubConj);

		// Mise à jour de l'activite normative
		changementGouvernementService
				.reattribuerMinistereActiviteNormative(session, oldMinistereNode, newMinistereNode);
		session.save();

		if (migrationWithDossierClos) {
			// Mise à jour du ministère de rattachement.
			changementGouvernementService.updateDossierMinistereRattachement(session, oldMinistereNode,
					newMinistereNode, migrationLoggerModel);

			session.save();
		}
		// note : pas besoin de Migrer les jetons du ministère CE car ils ne sont pas directement affectés à un
		// ministère.

		// Migration des ministères spécifique de la table de référence (Ministère CE, ministère du SGG)
		changementGouvernementService.migrerTableReferenceMinistere(session, oldMinistereNode, newMinistereNode,
				migrationLoggerModel);

		session.save();
		// Migration des bulletins officiels.
		changementGouvernementService.migrerBulletinOfficiel(session, oldMinistereNode, newMinistereNode,
				migrationLoggerModel);

		session.save();
		// Migration des mots cles de l'indexation
		changementGouvernementService.migrerGestionIndexation(session, oldMinistereNode, newMinistereNode,
				migrationLoggerModel);

		session.save();

		// Migration des dossiers link des ministères
		DossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
		dossierDistributionService.updateDossierLinksACLs(session, newElementOrganigramme);
		session.save();

		suppressionElementOrganigramme(session, deleteOldElementOrganigramme, oldMinistereNode, migrationLoggerModel);

		if (!norReattributionsPubConj.isEmpty()) {
			migrerPublicationsConjointes(session, norReattributionsPubConj);
			session.save();
		}
	}

	/**
	 * Pour chaque NOR de dossier effectivement migré (NOR modifié), on migre
	 * les publications conjointes en partant du NOR original.
	 * 
	 * @param norReattributions
	 *            une Map liant l'ancien NOR au nouveau NOR
	 */
	private void migrerPublicationsConjointes(final CoreSession session, Map<String, String> norReattributions)
			throws ClientException {
		final DossierService dossierService = SolonEpgServiceLocator.getDossierService();

		for (String oldNor : norReattributions.keySet()) {
			String newNor = norReattributions.get(oldNor);
			Dossier dossier = SolonEpgServiceLocator.getNORService().getDossierFromNOR(session, newNor)
					.getAdapter(Dossier.class);
			dossierService.updatePublicationsConjointes(dossier, session, oldNor, newNor, norReattributions);
		}
	}

	/**
	 * Migration d'une direction.
	 * 
	 * @param migrationLoggerModel
	 * 
	 * @param changementGouvernementService
	 * @param epgOrganigrammeService
	 * @param migrationWithDossierClos
	 *            si vrai, on migre les dossiers clos
	 * @throws ClientException
	 */
	private void migrationDirection(CoreSession session, String oldMinistereElementOrganigramme,
			String newMinistereElementOrganigramme, String oldElementOrganigramme, String newElementOrganigramme,
			Boolean deleteOldElementOrganigramme, MigrationLoggerModel migrationLoggerModel,
			Boolean migrationWithDossierClos, Boolean migrationModeleFdr) throws ClientException {

		final ChangementGouvernementService changementGouvernementService = SolonEpgServiceLocator
				.getChangementGouvernementService();
		final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
		final STUsAndDirectionService usService = STServiceLocator.getSTUsAndDirectionService();
		// Si la migration des modèles de feuille de route n'est pas coché : on doit désactiver les modèles. 
		Boolean desactivateModeleFdr = !migrationModeleFdr;

		EntiteNode oldMinistereNode = ministeresService.getEntiteNode(oldMinistereElementOrganigramme);
		EntiteNode newMinistereNode = ministeresService.getEntiteNode(newMinistereElementOrganigramme);
		UniteStructurelleNode oldDirectionNode = usService.getUniteStructurelleNode(oldElementOrganigramme);
		UniteStructurelleNode newDirectionNode = usService.getUniteStructurelleNode(newElementOrganigramme);

		// migration des éléments fils.
		changementGouvernementService.migrerElementsFils(session, oldDirectionNode, newDirectionNode,
				migrationLoggerModel);

		// migration des modèles de feuilles de route.
		changementGouvernementService.migrerModeleFdrDirection(session, oldMinistereNode, oldDirectionNode,
				newMinistereNode, newDirectionNode, migrationLoggerModel, desactivateModeleFdr);
		session.save();

		// Réattribution du Nor.
		Map<String, String> norReattributions = new HashMap<String, String>();
		changementGouvernementService.reattribuerNorDossierDirection(session, oldMinistereNode, oldDirectionNode,
				newMinistereNode, newDirectionNode, migrationLoggerModel, norReattributions);
		session.save();

		// Migrer les directions de la table de référence ( direction PRM)
		changementGouvernementService.migrerTableReferenceDirection(session, oldMinistereNode, oldDirectionNode,
				newMinistereNode, newDirectionNode, migrationLoggerModel);
		session.save();

		if (migrationWithDossierClos) {
			// Mise à jour du ministère de rattachement.
			changementGouvernementService.updateDossierDirectionRattachement(session, oldMinistereNode,
					oldDirectionNode, newMinistereNode, newDirectionNode, migrationLoggerModel);
		}
		session.save();

		// suppression du noeud organigramme
		suppressionElementOrganigramme(session, deleteOldElementOrganigramme, oldDirectionNode, migrationLoggerModel,
				oldMinistereNode);

		if (!norReattributions.isEmpty()) {
			migrerPublicationsConjointes(session, norReattributions);
			session.save();
		}
	}

	/**
	 * Migration d'une unité structurelle.
	 * 
	 * @param migrationLoggerModel
	 * 
	 * @param changementGouvernementService
	 * @param epgOrganigrammeService
	 * @throws ClientException
	 */
	private void migrationUniteStructurelle(CoreSession session, String oldElementOrganigramme,
			String newElementOrganigramme, Boolean deleteOldElementOrganigramme,
			MigrationLoggerModel migrationLoggerModel) throws ClientException {

		final ChangementGouvernementService changementGouvernementService = SolonEpgServiceLocator
				.getChangementGouvernementService();
		final STUsAndDirectionService usService = STServiceLocator.getSTUsAndDirectionService();

		OrganigrammeNode oldDirectionNode = usService.getUniteStructurelleNode(oldElementOrganigramme);
		OrganigrammeNode newDirectionNode = usService.getUniteStructurelleNode(newElementOrganigramme);

		// migration des éléments fils.
		changementGouvernementService.migrerElementsFils(session, oldDirectionNode, newDirectionNode,
				migrationLoggerModel);

		session.save();

		// suppression du noeud organigramme
		suppressionElementOrganigramme(session, deleteOldElementOrganigramme, oldDirectionNode, migrationLoggerModel);
	}

	/**
	 * Migration d'un poste.
	 * 
	 * @param migrationLoggerModel
	 * 
	 * @param changementGouvernementService
	 * @param epgOrganigrammeService
	 * @throws ClientException
	 */
	private void migrationPoste(CoreSession session, String oldElementOrganigramme, String newElementOrganigramme,
			Boolean deleteOldElementOrganigramme, MigrationLoggerModel migrationLoggerModel) throws ClientException {

		final ChangementGouvernementService changementGouvernementService = SolonEpgServiceLocator
				.getChangementGouvernementService();
		final STPostesService postesService = STServiceLocator.getSTPostesService();

		OrganigrammeNode oldPosteNode = postesService.getPoste(oldElementOrganigramme);
		OrganigrammeNode newPosteNode = postesService.getPoste(newElementOrganigramme);

		// migration des éléments fils
		changementGouvernementService.migrerElementsFils(session, oldPosteNode, newPosteNode, migrationLoggerModel);

		// migration des étapes de feuilles de route
		changementGouvernementService.migrerModeleStepFdr(session, oldPosteNode, newPosteNode, migrationLoggerModel);
		session.save();

		// Mise à jour du champ technique posteCreator utilisé pour la visibilité du dossier dans l'infoCentre de
		// l'espace de suivi.
		changementGouvernementService.updateDossierCreatorPoste(session, oldPosteNode, newPosteNode,
				migrationLoggerModel);
		session.save();

		// Migrer les postes de la table de référence ( corbeilles de retour au DAN )
		changementGouvernementService.migrerTableReferencePoste(session, oldPosteNode, newPosteNode,
				migrationLoggerModel);
		session.save();

		// Mise à jour des corbeilles du poste : change les droits et le nom.
		changementGouvernementService.updateMailBox(session, oldPosteNode, newPosteNode, migrationLoggerModel);
		session.save();

		// suppression du noeud organigramme
		suppressionElementOrganigramme(session, deleteOldElementOrganigramme, oldPosteNode, migrationLoggerModel);
	}

	/**
	 * Suppression logique de l'élément de l'organigramme.
	 * 
	 * @param epgOrganigrammeService
	 * @param oldElementOrganigrammeToDelete
	 * @throws ClientException
	 */
	private void suppressionElementOrganigramme(CoreSession session, Boolean deleteOldElementOrganigramme,
			OrganigrammeNode oldElementOrganigrammeToDelete, MigrationLoggerModel migrationLoggerModel)
			throws ClientException {

		if (deleteOldElementOrganigramme) {
			final ChangementGouvernementService chgtGouvService = SolonEpgServiceLocator
					.getChangementGouvernementService();
			migrationLoggerModel.setDeleteOld(0);
			chgtGouvService.flushMigrationLogger(migrationLoggerModel);
			// suppression de l'ancien element
			SolonEpgServiceLocator.getEpgOrganigrammeService().deleteFromDn(oldElementOrganigrammeToDelete, true);
			migrationLoggerModel.setDeleteOld(1);
			chgtGouvService.flushMigrationLogger(migrationLoggerModel);
		}
	}

	private void suppressionElementOrganigramme(CoreSession session, Boolean deleteOldElementOrganigramme,
			OrganigrammeNode oldDirectionNode, MigrationLoggerModel migrationLoggerModel,
			OrganigrammeNode oldMinistereNode) throws ClientException {
		if (deleteOldElementOrganigramme) {
			migrationLoggerModel.setDeleteOld(0);

			List<OrganigrammeNode> parents = SolonEpgServiceLocator.getEpgOrganigrammeService().getParentList(
					oldDirectionNode);
			if (parents != null && !parents.isEmpty()) {
				parents.remove(oldMinistereNode);
				if (parents.isEmpty()) {
					suppressionElementOrganigramme(session, deleteOldElementOrganigramme, oldDirectionNode,
							migrationLoggerModel);
				} else {
					oldDirectionNode.setParentList(parents);
					STServiceLocator.getSTUsAndDirectionService().updateUniteStructurelle(
							(UniteStructurelleNode) oldDirectionNode);
				}
			} else {
				suppressionElementOrganigramme(session, deleteOldElementOrganigramme, oldDirectionNode,
						migrationLoggerModel);
			}

			migrationLoggerModel.setDeleteOld(1);
			SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);

		}

	}

}
