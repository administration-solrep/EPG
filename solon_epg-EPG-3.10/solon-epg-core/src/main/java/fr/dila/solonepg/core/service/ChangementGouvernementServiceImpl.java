package fr.dila.solonepg.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.persistence.PersistenceProvider;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunCallback;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunVoid;
import org.nuxeo.ecm.core.persistence.PersistenceProviderFactory;
import org.nuxeo.ecm.core.schema.PrefetchInfo;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.cm.mailbox.Mailbox;
import fr.dila.cm.mailbox.MailboxConstants;
import fr.dila.cm.security.CaseManagementSecurityConstants;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgTableReferenceConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.migration.MigrationDetailModel;
import fr.dila.solonepg.api.migration.MigrationDiscriminatorConstants;
import fr.dila.solonepg.api.migration.MigrationDiscriminatorConstants.MigrationDiscirminatorEnum;
import fr.dila.solonepg.api.migration.MigrationLoggerModel;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.ChangementGouvernementService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.api.service.IndexationEpgService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.migration.BulletinDetailModelImpl;
import fr.dila.solonepg.core.migration.ClosDetailModelImpl;
import fr.dila.solonepg.core.migration.CreatorDetailModelImpl;
import fr.dila.solonepg.core.migration.LancerDetailModelImpl;
import fr.dila.solonepg.core.migration.MailBoxDetailModelImpl;
import fr.dila.solonepg.core.migration.MigrationLoggerModelImpl;
import fr.dila.solonepg.core.migration.ModeleFDRDetailModelImpl;
import fr.dila.solonepg.core.migration.MotsClesDetailModelImpl;
import fr.dila.solonepg.core.migration.StepDetailModelImpl;
import fr.dila.solonepg.core.migration.TableRefDetailModelImpl;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.feuilleroute.STRouteStep;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.service.SecurityService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;

public class ChangementGouvernementServiceImpl extends DefaultComponent implements ChangementGouvernementService {

	private static final STLogger LOGGER = STLogFactory.getLog(ChangementGouvernementServiceImpl.class);

	private static final String BATCH_SIZE_PROP = "solonepg.chgt.gouv.batch.size";
	private static final int BATCH_SIZE_DEFAULT = 100;

	/**
	 * Récupère dossiers clos (publié, abandonné, clos, terminé sans publication,
	 * nor attribué)
	 */
	private static final String QUERY_FROM_DOSS_WHERE_CLOS_SQL = " FROM dossier_solon_epg WHERE (statut IN ('"
			+ VocabularyConstants.STATUT_PUBLIE + "', '" + VocabularyConstants.STATUT_ABANDONNE + "', '"
			+ VocabularyConstants.STATUT_CLOS + "', '" + VocabularyConstants.STATUT_NOR_ATTRIBUE + "', '"
			+ VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION + "') OR isAfterDemandePublication = '1') ";

	private static volatile PersistenceProvider persistenceProvider;

	@Override
	public void migrerElementsFils(final CoreSession session, final OrganigrammeNode oldNode,
			final OrganigrammeNode newNode, final MigrationLoggerModel migrationLoggerModel) throws ClientException {
		migrationLoggerModel.setElementsFils(0);
		flushMigrationLogger(migrationLoggerModel);
		final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
				MigrationDiscriminatorConstants.STEP, "Migration éléments fils");

		final EpgOrganigrammeService organigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
		organigrammeService.migrateNode(session, oldNode, newNode);

		migrationLoggerModel.setElementsFils(1);
		flushMigrationLogger(migrationLoggerModel);

		migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
		flushMigrationDetail(migrationDetailModel);

	}

	@Override
	public void migrerModeleStepFdr(final CoreSession session, final OrganigrammeNode oldNode,
			final OrganigrammeNode newNode, final MigrationLoggerModel migrationLoggerModel) throws ClientException {

		migrationLoggerModel.setFdrStep(0);
		flushMigrationLogger(migrationLoggerModel);

		// récupération des identifiants de mailbox
		final String oldPostMailboxId = SSConstant.MAILBOX_POSTE_ID_PREFIX + oldNode.getId();
		final String newPostMailboxId = SSConstant.MAILBOX_POSTE_ID_PREFIX + newNode.getId();

		int nbStepFdrToUpdate = 0;

		final List<String> params = new ArrayList<String>();
		params.add(oldPostMailboxId);

		// on modifie les étapes et les dossierLink
		final StringBuilder query = new StringBuilder();
		query.append("SELECT r.ecm:uuid as id FROM ");
		query.append(STConstant.ROUTE_STEP_DOCUMENT_TYPE);
		query.append(" as r WHERE r.");
		query.append(CaseConstants.STEP_DISTRIBUTION_MAILBOX_ID_PROPERTY_NAME);
		query.append(" = ? AND r.");
		query.append(STSchemaConstant.ECM_CURRENT_LIFE_CYCLE_STATE_XPATH);
		query.append(" IN ('running', 'ready') ");

		final List<DocumentModel> stepFdrToUpdate = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				STConstant.ROUTE_STEP_DOCUMENT_TYPE, query.toString(), params.toArray());

		if (stepFdrToUpdate == null || stepFdrToUpdate.isEmpty()) {
			migrationLoggerModel.setFdrStepCount(0);
			migrationLoggerModel.setFdrStepCurrent(0);
			flushMigrationLogger(migrationLoggerModel);

			final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
					MigrationDiscriminatorConstants.STEP, "Migration étape : aucune étape à migrer");
			migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
			SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationDetail(migrationDetailModel);
		} else {
			nbStepFdrToUpdate = stepFdrToUpdate.size();

			migrationLoggerModel.setFdrStepCount(nbStepFdrToUpdate);

			int index = 0;
			migrationLoggerModel.setFdrStepCurrent(index);
			flushMigrationLogger(migrationLoggerModel);

			LOGGER.info(session, SSLogEnumImpl.MIGRATE_MOD_FDR_TEC,
					nbStepFdrToUpdate + " étapes de feuille de route à mettre à jour.");
			// liste des feuilles de route à mettre à jour pour les droits de lecture
			for (final DocumentModel documentModel : stepFdrToUpdate) {
				final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
						MigrationDiscriminatorConstants.STEP, "Migration étape : " + documentModel.getId());
				// on verrouille le document pour s'assurer que personne ne le modifie
				lockDocument(session, documentModel);

				final STRouteStep sTRouteStep = documentModel.getAdapter(STRouteStep.class);
				sTRouteStep.setDistributionMailboxId(newPostMailboxId);

				// modification du dc:title (sauf si c'est la première étape )
				final String oldTitle = documentModel.getTitle();
				final int lastIndex = oldTitle.lastIndexOf(SSConstant.MAILBOX_POSTE_ID_PREFIX);
				if (lastIndex != -1) {
					final String newTitle = oldTitle.substring(0, lastIndex) + newPostMailboxId;
					DublincoreSchemaUtils.setTitle(sTRouteStep.getDocument(), newTitle);
				}

				migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
				SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationDetail(migrationDetailModel);
				sTRouteStep.save(session);
				// on enlève le verrou
				session.removeLock(documentModel.getRef());

				migrationLoggerModel.setFdrStepCurrent(++index);
				flushMigrationLogger(migrationLoggerModel);

				createMigrationDetailFor(migrationLoggerModel, MigrationDiscriminatorConstants.STEP,
						"Migration étape : " + documentModel.getId() + " : Fin");
			}
		}

		migrationLoggerModel.setFdrStep(1);
		flushMigrationLogger(migrationLoggerModel);
	}

	@Override
	public void updateDossierCreatorPoste(final CoreSession session, final OrganigrammeNode oldNode,
			final OrganigrammeNode newNode, final MigrationLoggerModel migrationLoggerModel) throws ClientException {

		migrationLoggerModel.setCreatorPoste(0);
		flushMigrationLogger(migrationLoggerModel);

		// récupération des identifiants de mailbox
		final String oldPostMailboxId = SSConstant.MAILBOX_POSTE_ID_PREFIX + oldNode.getId();
		final String newPostMailboxId = SSConstant.MAILBOX_POSTE_ID_PREFIX + newNode.getId();

		int nbDossierToUpdate = 0;

		// on change le posteCreator du Dossier qui est utilisé dans l'espace de suivi >
		// infocentre

		final List<String> params = new ArrayList<String>();
		params.add(oldPostMailboxId);

		// on récupère les dossiers concernés
		final StringBuilder query = new StringBuilder();
		query.append("SELECT d.ecm:uuid as id FROM ");
		query.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
		query.append(" as d WHERE d.dos:");
		query.append(DossierSolonEpgConstants.DOSSIER_POSTE_CREATOR_PROPERTY);
		query.append(" = ? ");

		final List<DocumentModel> dossierToUpdate = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, query.toString(), params.toArray());
		if (dossierToUpdate == null || dossierToUpdate.isEmpty()) {
			migrationLoggerModel.setCreatorPosteCount(0);
			migrationLoggerModel.setCreatorPosteCurrent(0);
			flushMigrationLogger(migrationLoggerModel);
			final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
					MigrationDiscriminatorConstants.CREATOR, "Migration poste createur : aucun dossier à migrer");
			migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
			flushMigrationDetail(migrationDetailModel);
		} else {
			nbDossierToUpdate = dossierToUpdate.size();
			LOGGER.info(session, STLogEnumImpl.MIGRATE_DOSSIER_TEC, nbDossierToUpdate + " dossiers à mettre à jour.");

			migrationLoggerModel.setCreatorPosteCount(nbDossierToUpdate);

			int index = 0;
			migrationLoggerModel.setCreatorPosteCurrent(index);
			flushMigrationLogger(migrationLoggerModel);

			final SecurityService securityService = STServiceLocator.getSecurityService();
			for (final DocumentModel documentModel : dossierToUpdate) {

				// on verrouille le document pour s'assurer que personne ne le modifie
				lockDocument(session, documentModel);

				final Dossier dossier = documentModel.getAdapter(Dossier.class);

				final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
						MigrationDiscriminatorConstants.CREATOR,
						"Migration poste createur dossier : " + dossier.getNumeroNor());

				dossier.setCreatorPoste(newPostMailboxId);

				// note : on modifie le droit mailbox_poste-xxx du dossier
				final ACP acp = documentModel.getACP();
				// enleve l'acl lié au nouveau poste
				securityService.removeAceToAcp(acp, CaseManagementSecurityConstants.ACL_MAILBOX_PREFIX,
						CaseManagementSecurityConstants.MAILBOX_PREFIX + oldPostMailboxId, SecurityConstants.READ_WRITE,
						true);
				// ajoute l'acl lié au nouveau poste
				securityService.addAceToAcp(acp, CaseManagementSecurityConstants.ACL_MAILBOX_PREFIX,
						CaseManagementSecurityConstants.MAILBOX_PREFIX + newPostMailboxId,
						SecurityConstants.READ_WRITE);

				session.setACP(documentModel.getRef(), acp, true);
				dossier.save(session);

				// on enlève le verrou
				session.removeLock(documentModel.getRef());

				migrationLoggerModel.setCreatorPosteCurrent(++index);
				flushMigrationLogger(migrationLoggerModel);

				migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
				flushMigrationDetail(migrationDetailModel);
			}
		}
		migrationLoggerModel.setCreatorPoste(1);
		flushMigrationLogger(migrationLoggerModel);
	}

	@Override
	public void updateMailBox(final CoreSession session, final OrganigrammeNode oldNode, final OrganigrammeNode newNode,
			final MigrationLoggerModel migrationLoggerModel) throws ClientException {

		migrationLoggerModel.setMailboxPoste(0);
		flushMigrationLogger(migrationLoggerModel);

		// récupération des identifiants de mailbox
		final String oldPostMailboxId = SSConstant.MAILBOX_POSTE_ID_PREFIX + oldNode.getId();
		final String newNodeId = newNode.getId();
		final String newPostMailboxId = SSConstant.MAILBOX_POSTE_ID_PREFIX + newNodeId;
		final String oldPostLabel = oldNode.getLabel();
		final String newPostLabel = newNode.getLabel();

		// on récupère la mailbox contenant les DossierLink de l'ancien poste.
		final StringBuilder query = new StringBuilder();
		query.append("SELECT m.ecm:uuid as id FROM ");
		query.append(SolonEpgConstant.SOLON_EPG_MAILBOX_TYPE);
		query.append(" as m WHERE m.");
		query.append(MailboxConstants.GROUPS_FIELD);
		query.append(" = ? ");

		final List<String> params = new ArrayList<String>();
		params.add(oldPostMailboxId);

		final List<DocumentModel> oldMlbxPosteList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SolonEpgConstant.SOLON_EPG_MAILBOX_TYPE, query.toString(), params.toArray());

		params.clear();
		params.add(newPostMailboxId);

		final List<DocumentModel> newMlbxPosteList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SolonEpgConstant.SOLON_EPG_MAILBOX_TYPE, query.toString(), params.toArray());

		if (oldMlbxPosteList == null || oldMlbxPosteList.isEmpty()) {
			migrationLoggerModel.setMailboxPosteCount(0);
			migrationLoggerModel.setMailboxPosteCurrent(0);
			flushMigrationLogger(migrationLoggerModel);

			final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
					MigrationDiscriminatorConstants.MAILBOX, "Aucune mailbox trouvée");
			migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
			flushMigrationDetail(migrationDetailModel);
		} else if (oldMlbxPosteList.size() > 1) {
			migrationLoggerModel.setMailboxPosteCount(0);
			migrationLoggerModel.setMailboxPosteCurrent(0);
			flushMigrationLogger(migrationLoggerModel);
			final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
					MigrationDiscriminatorConstants.MAILBOX, "Plus d'une mailbox trouvée pour " + oldPostLabel);
			migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
			flushMigrationDetail(migrationDetailModel);
			throw new ClientException("Plus d'un mailbox pour le poste " + oldPostLabel + "ont été trouvées !");
		} else {
			DocumentModel newMlbxPoste = null;

			if (newMlbxPosteList == null || newMlbxPosteList.isEmpty()) {
				final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
						MigrationDiscriminatorConstants.MAILBOX, "Migration mailbox : céation de  " + newPostLabel);
				migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
				flushMigrationDetail(migrationDetailModel);
				// création de la mailbox poste
				final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
				final Mailbox newMailBox = mailboxPosteService.createPosteMailbox(session, newNodeId, newPostLabel);
				newMlbxPoste = newMailBox.getDocument();
			} else if (newMlbxPosteList.size() > 1) {
				final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
						MigrationDiscriminatorConstants.MAILBOX, "Plus d'une mailbox trouvée pour " + newPostLabel);
				migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
				flushMigrationDetail(migrationDetailModel);
				throw new ClientException("Plus d'un mailbox pour le poste " + newPostLabel + "ont été trouvées !");
			}

			final DocumentModel oldMlbxPoste = oldMlbxPosteList.get(0);
			if (newMlbxPoste == null && newMlbxPosteList != null && !newMlbxPosteList.isEmpty()) {
				newMlbxPoste = newMlbxPosteList.get(0);
			}

			if (newMlbxPoste != null) {
				final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
						MigrationDiscriminatorConstants.MAILBOX,
						"Migration mailbox : " + oldMlbxPoste.getTitle() + " vers " + newMlbxPoste.getTitle());

				// déplacement des anciennes mailbox dans la nouvelle
				final List<DocumentRef> documentRefList = session.getChildrenRefs(oldMlbxPoste.getRef(), null);
				session.move(documentRefList, newMlbxPoste.getRef());

				migrationLoggerModel.setMailboxPosteCount(1);
				migrationLoggerModel.setMailboxPosteCurrent(1);

				migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
				flushMigrationDetail(migrationDetailModel);
			}
		}

		migrationLoggerModel.setMailboxPoste(1);
		flushMigrationLogger(migrationLoggerModel);
	}

	@Override
	public void migrerModeleFdrDirection(final CoreSession session, final EntiteNode oldMinistereNode,
			final UniteStructurelleNode oldDirectionNode, final EntiteNode newMinistereNode,
			final UniteStructurelleNode newDirectionNode, final MigrationLoggerModel migrationLoggerModel, 
			Boolean desactivateModelFdr) throws ClientException {
		final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
		feuilleRouteModelService.migrerModeleFdrDirection(session, oldMinistereNode, oldDirectionNode, newMinistereNode,
				newDirectionNode, migrationLoggerModel, desactivateModelFdr);
	}

	@Override
	public void migrerModeleFdrMinistere(final CoreSession session, final EntiteNode oldNode,
			final EntiteNode newNode, final MigrationLoggerModel migrationLoggerModel, Boolean desactivateModelFdr) throws ClientException {
		final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
		feuilleRouteModelService.migrerModeleFdrMinistere(session, oldNode, newNode, migrationLoggerModel, desactivateModelFdr);
	}

	@Override
	public void reattribuerNorDossierDirection(final CoreSession session, final OrganigrammeNode oldMinistereNode,
			final OrganigrammeNode oldDirectionNode, final OrganigrammeNode newMinistereNode,
			final OrganigrammeNode newDirectionNode, final MigrationLoggerModel migrationLoggerModel,
			Map<String, String> norReattributionsPubConj)
			throws ClientException {

		final String batchSizeStr = Framework.getProperty(BATCH_SIZE_PROP, Integer.toString(BATCH_SIZE_DEFAULT));
		int batchSize;
		try {
			batchSize = Integer.parseInt(batchSizeStr);
		} catch (NumberFormatException e) {
			LOGGER.error(session, EpgLogEnumImpl.MIGRATE_NOR_TEC,
					String.format("failed to parse property [%s] : %s", batchSizeStr, e.getMessage()), e);
			batchSize = BATCH_SIZE_DEFAULT;
		}
		LOGGER.info(session, EpgLogEnumImpl.MIGRATE_NOR_TEC, "batch size : " + batchSize);

		migrationLoggerModel.setNorDossierLanceInite(0);
		flushMigrationLogger(migrationLoggerModel);

		final boolean hasDirection = !(oldDirectionNode == null);

		// récupère les informations du ministère
		final String oldMinistereNodeId = oldMinistereNode.getId();
		final String newMinistereNodeId = newMinistereNode.getId();
		final EntiteNode ministereNode = (EntiteNode) newMinistereNode;
		final String newNorMinistere = ministereNode.getNorMinistere();

		String oldDirectionTempNodeId = null;
		String newDirectionTempNodeId = null;
		String norNewTempDirection = null;

		boolean reattributionNor = true;

		if (hasDirection) {
			oldDirectionTempNodeId = oldDirectionNode.getId();
			newDirectionTempNodeId = newDirectionNode.getId();
			final UniteStructurelleNode directionNode = (UniteStructurelleNode) newDirectionNode;
			norNewTempDirection = directionNode.getNorDirection(newMinistereNodeId);

			// Mantis152681: si les 2 directions ont la même lettre, le numéro NOR ne
			// changera pas, donc pas besoin de
			// le réattribuer
			if (oldMinistereNodeId.equals(newMinistereNodeId)) {
				UniteStructurelleNode oldDirection = (UniteStructurelleNode) oldDirectionNode;
				String oldNorDirection = oldDirection.getNorDirection(oldMinistereNodeId);
				if (oldNorDirection != null && oldNorDirection.equals(norNewTempDirection)) {
					reattributionNor = false;
				}
			}

		}
		// Mantis 155911
		if ("".equals(norNewTempDirection)
				&& ((UniteStructurelleNode) newDirectionNode).getUniteStructurelleParentList().size() == 1
				&& ((UniteStructurelleNode) newDirectionNode).getUniteStructurelleParentList().get(0).getType()
						.equals(OrganigrammeType.DIRECTION)) {
			// On a une direction sous une direction et on n'a pas réussi à trouver le NOR
			// On prend donc le NOR de la direction parente
			norNewTempDirection = ((UniteStructurelleNode) newDirectionNode).getUniteStructurelleParentList().get(0)
					.getNorDirection(newMinistereNodeId);
		}
		final String newNorDirection = norNewTempDirection;
		final String oldDirectionNodeId = oldDirectionTempNodeId;
		final String newDirectionNodeId = newDirectionTempNodeId;

		final List<String> dossierIdsToReattribuer = retrieveDossierToReattribuer(session, hasDirection,
				oldMinistereNodeId, oldDirectionNodeId);

		if (dossierIdsToReattribuer == null || dossierIdsToReattribuer.isEmpty()) {
			final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
					MigrationDiscriminatorConstants.LANCE, "Aucune réattribution de NOR à effectuer");
			migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
			flushMigrationDetail(migrationDetailModel);

			migrationLoggerModel.setNorDossierLanceIniteCount(0);
			migrationLoggerModel.setNorDossierLanceIniteCurrent(0);
			flushMigrationLogger(migrationLoggerModel);
		} else {
			final int nbDossierToUpdate = dossierIdsToReattribuer.size();
			LOGGER.info(session, EpgLogEnumImpl.MIGRATE_NOR_TEC,
					nbDossierToUpdate + " réattribution de NOR à effectuer.");

			migrationLoggerModel.setNorDossierLanceIniteCount(nbDossierToUpdate);
			migrationLoggerModel.setNorDossierLanceIniteCurrent(0);
			flushMigrationLogger(migrationLoggerModel);
			final NORService norService = SolonEpgServiceLocator.getNORService();

			final Set<String> existingNors = norService.retrieveAvailableNorList(session);

			int fromIndex = 0;
			while (fromIndex < dossierIdsToReattribuer.size()) {
				int toIndex = fromIndex + batchSize;
				if (toIndex > dossierIdsToReattribuer.size()) {
					toIndex = dossierIdsToReattribuer.size();
				}
				List<String> sublist = dossierIdsToReattribuer.subList(fromIndex, toIndex);
				reattribuerNorProcessDossierBucket(sublist, session, hasDirection, newNorMinistere, newNorDirection,
						existingNors, migrationLoggerModel, newMinistereNodeId, newDirectionNodeId, reattributionNor,
						norService, fromIndex, norReattributionsPubConj);
				fromIndex = toIndex;
			}

		}

		migrationLoggerModel.setNorDossierLanceInite(1);
		flushMigrationLogger(migrationLoggerModel);
	}

	private List<String> retrieveDossierToReattribuer(CoreSession session, boolean hasDirection,
			String oldMinistereNodeId, String oldDirectionNodeId) throws ClientException {

		final List<String> params = new ArrayList<String>();
		params.add(oldMinistereNodeId);

		// récupère les dossiers à réattribuer
		final StringBuilder query = new StringBuilder();
		query.append("SELECT d.ecm:uuid as id FROM ");
		query.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
		query.append(" as d WHERE d.dos:statut IN('");
		query.append(VocabularyConstants.STATUT_INITIE);
		query.append("','");
		query.append(VocabularyConstants.STATUT_LANCE);
		query.append("') AND d.dos:isAfterDemandePublication = 0 and d.dos:");
		query.append(DossierSolonEpgConstants.DOSSIER_MINISTERE_ATTACHE_PROPERTY);
		query.append(" = ? ");

		if (hasDirection) {
			query.append(" and d.dos:");
			query.append(DossierSolonEpgConstants.DOSSIER_DIRECTION_ATTACHE_PROPERTY);
			query.append(" = ? ");

			params.add(oldDirectionNodeId);
		}

		return QueryUtils.doUFNXQLQueryForIdsList(session, query.toString(), params.toArray(), 0, 0);
	}

	/**
	 * @param norReattributionsPubConj
	 */
	private void reattribuerNorProcessDossierBucket(List<String> dossierIdsToReattribuer, CoreSession session,
			boolean hasDirection, String newNorMinistere, String newNorDirection, Set<String> existingNors,
			final MigrationLoggerModel migrationLoggerModel, String newMinistereNodeId, String newDirectionNodeId,
			boolean reattributionNor, NORService norService, int inputIndex,
			Map<String, String> norReattributionsPubConj)
			throws ClientException {

		final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
		List<DocumentModel> dossierToReattribuer = session.getDocuments(dossierIdsToReattribuer, new PrefetchInfo(
				"dublincore,common,dossier_solon_epg,conseil_etat,note,retour_dila,traitement_papier,distribution,case"));

		List<String> numeroNors = new ArrayList<String>();
		for (final DocumentModel documentModel : dossierToReattribuer) {
			final Dossier dossier = documentModel.getAdapter(Dossier.class);
			numeroNors.add(dossier.getNumeroNor());
		}

		// retrieve activiteNormative
		Map<String, ActiviteNormative> activiteNormativeByNor = SolonEpgServiceLocator.getActiviteNormativeService()
				.findActiviteNormativeByNors(numeroNors, session);

		int index = inputIndex;
		for (final DocumentModel documentModel : dossierToReattribuer) {

			reattribuerNorProcessOneDossier(session, documentModel, hasDirection, newNorMinistere, newNorDirection,
					existingNors, migrationLoggerModel, dossierService, newMinistereNodeId, newDirectionNodeId,
					reattributionNor, norService, activiteNormativeByNor, norReattributionsPubConj);

			migrationLoggerModel.setNorDossierLanceIniteCurrent(++index);
			flushMigrationLogger(migrationLoggerModel);
		}

		dossierService.updateDossierWhenReattributionJetonCE(session, dossierToReattribuer);

		session.save();
	}

	/**
	 * @param norReattributionsPubConj
	 *            permet de maintenir une liste des réattributions de NOR sur
	 *            tout le process.
	 */
	private void reattribuerNorProcessOneDossier(CoreSession session, DocumentModel documentModel, boolean hasDirection,
			String newNorMinistere, String newNorDirection, Set<String> existingNors,
			final MigrationLoggerModel migrationLoggerModel, final DossierService dossierService,
			String newMinistereNodeId, String newDirectionNodeId, boolean reattributionNor, NORService norService,
			Map<String, ActiviteNormative> activiteNormativeByNor, Map<String, String> norReattributionsPubConj)
			throws ClientException {

		// on verrouille le document pour s'assurer que personne ne le modifie
		lockDocument(session, documentModel);

		final Dossier dossier = documentModel.getAdapter(Dossier.class);
		final String nor = dossier.getNumeroNor();
		MigrationDetailModel migrationDetailModel;
		// Calculer le nouveau numéro NOR pour regarder s'il existe
		String nouveauNumeroNOR = nor;
		if (!hasDirection) {
			nouveauNumeroNOR = newNorMinistere + nouveauNumeroNOR.substring(3);
		} else {
			nouveauNumeroNOR = newNorMinistere + newNorDirection + nouveauNumeroNOR.substring(4);
		}
		// On vérifie que le nouveau numéro NOR n'existe pas
		boolean norDestinationExist = existingNors.contains(nouveauNumeroNOR);
		if (norDestinationExist) {
			// Si le numéro NOR final existe déjà, la migration du dossier n'est pas
			// effectuée
			// On logge l'erreur et on envoie un mail aux administrateurs fonctionnels
			migrationDetailModel = createMigrationDetailFor(migrationLoggerModel, MigrationDiscriminatorConstants.LANCE,
					"Le texte " + nor + " n'a pas pu faire l'objet d'une migration vers le nor " + nouveauNumeroNOR
							+ ". Le NOR cible est déjà utilisé.");
			String objet = "Migration du dossier " + nor;
			String corpsTemplate = "Le texte " + nor + " n'a pas pu faire l'objet d'une migration vers le nor "
					+ nouveauNumeroNOR + ". Le NOR cible est déjà utilisé.";
			// Recherche des utilisateurs qui sont administrateur fonctionnel
			UserManager userManager = STServiceLocator.getUserManager();
			List<String> listeAdministrateurs = userManager
					.getGroup(STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME).getMemberUsers();
			// Envoi d'un mail à chaque utilisateur trouvé pour lui signifier que le dossier
			// n'a pas été migré
			for (String user : listeAdministrateurs) {
				String userMail = STServiceLocator.getSTUserService().getUserInfo(user, "m");
				if (userMail != null && !userMail.isEmpty()) {
					sendMail(session, userMail, objet, corpsTemplate);
				}
			}

		} else {
			migrationDetailModel = createMigrationDetailFor(migrationLoggerModel, MigrationDiscriminatorConstants.LANCE,
					"Réattribution de NOR du dossier " + nor);
			final ActiviteNormative activiteNormative = activiteNormativeByNor.get(dossier.getNumeroNor());
			boolean hasPublicationsConjointes = dossierService.updateDossierWhenReattribution(newMinistereNodeId,
					newDirectionNodeId, !hasDirection, reattributionNor, newNorMinistere, newNorDirection, norService,
					dossier, session, false, activiteNormative);

			String newNor = dossier.getNumeroNor();
			if (!nor.equals(newNor) && hasPublicationsConjointes) {
				// Le NOR du dossier a été modifié ET il a des publications conjointes
				norReattributionsPubConj.put(nor, newNor);
			}
		}
		// on enlève le verrou
		session.removeLock(documentModel.getRef());

		migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
		flushMigrationDetail(migrationDetailModel);
	}

	@Override
	public void reattribuerNorDossierMinistere(final CoreSession session, final OrganigrammeNode oldNode,
			final OrganigrammeNode newNode, final MigrationLoggerModel migrationLoggerModel,
			Map<String, String> norReattributionsPubConj) throws ClientException {
		reattribuerNorDossierDirection(session, oldNode, null, newNode, null, migrationLoggerModel,
				norReattributionsPubConj);
	}

	@Override
	public void updateDossierDirectionRattachement(final CoreSession session, final OrganigrammeNode oldMinistereNode,
			final OrganigrammeNode oldDirectionNode, final OrganigrammeNode newMinistereNode,
			final OrganigrammeNode newDirectionNode, final MigrationLoggerModel migrationLoggerModel)
			throws ClientException {

		migrationLoggerModel.setNorDossierClos(0);
		flushMigrationLogger(migrationLoggerModel);

		final boolean hasDirection = !(oldDirectionNode == null);

		// récupère les ids des noeuds ministères
		final String oldMinistereNodeId = oldMinistereNode.getId();
		final String newMinistereNodeId = newMinistereNode.getId();
		String oldDirectionNodeId = null;
		String newDirectionNodeIdTemp = null;
		if (hasDirection) {
			oldDirectionNodeId = oldDirectionNode.getId();
			newDirectionNodeIdTemp = newDirectionNode.getId();
		}
		final String newDirectionNodeId = newDirectionNodeIdTemp;

		// récupération requete id dossier clos
		final StringBuilder endQuery = new StringBuilder(QUERY_FROM_DOSS_WHERE_CLOS_SQL);
		// ajout filtre sur ministereattache
		endQuery.append(" AND ").append(DossierSolonEpgConstants.DOSSIER_MINISTERE_ATTACHE_PROPERTY).append(" = '")
				.append(oldMinistereNodeId).append("' ");
		// ajout filtre sur directionattache si nécessaire
		if (hasDirection) {
			endQuery.append(" AND ").append(DossierSolonEpgConstants.DOSSIER_DIRECTION_ATTACHE_PROPERTY).append(" = '")
					.append(oldDirectionNodeId).append("' ");
		}

		final StringBuilder querySelectIds = new StringBuilder("SELECT id ").append(endQuery);
		final StringBuilder querySelectNor = new StringBuilder("SELECT id, numeroNor ").append(endQuery);

		// récupération du nombre de dossier à mettre à jour
		final List<String> norDossiersList = new ArrayList<String>();
		final List<String> idsDossiersList = new ArrayList<String>();
		IterableQueryResult res = null;
		try {
			res = QueryUtils.doSqlQuery(session, new String[] { FlexibleQueryMaker.COL_ID, "dos:numeroNor" },
					querySelectNor.toString(), null);

			Iterator<Map<String, Serializable>> iterator = res.iterator();
			while (iterator.hasNext()) {
				Map<String, Serializable> row = iterator.next();
				String idDossier = (String) row.get(FlexibleQueryMaker.COL_ID);
				String norDossier = (String) row.get("dos:numeroNor");
				norDossiersList.add(norDossier);
				idsDossiersList.add(idDossier);
			}
		} finally {
			if (res != null) {
				res.close();
			}
		}

		final int nbDossiersToUpdate = norDossiersList.size();

		if (nbDossiersToUpdate == 0) {
			migrationLoggerModel.setNorDossierClosCount(0);
			migrationLoggerModel.setNorDossierClosCurrent(0);
			flushMigrationLogger(migrationLoggerModel);
			final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
					MigrationDiscriminatorConstants.CLOS, "Aucun changement de ministère de rattachement à effectuer");
			migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
			flushMigrationDetail(migrationDetailModel);
		} else {
			// maj migration logger
			LOGGER.info(session, STLogEnumImpl.MIGRATE_MINISTERE_TEC,
					nbDossiersToUpdate + " ministère de rattachement à mettre à jour.");
			migrationLoggerModel.setNorDossierClosCount(nbDossiersToUpdate);
			migrationLoggerModel.setNorDossierClosCurrent(0);
			flushMigrationLogger(migrationLoggerModel);

			final List<MigrationDetailModel> migrationsDetails = new ArrayList<MigrationDetailModel>();
			final String migrationDetailInit = "Migration du ministère de rattachement des dossiers : ";
			final StringBuilder migrationDetail = new StringBuilder(migrationDetailInit);
			for (int indexNor = 0; indexNor < nbDossiersToUpdate; indexNor++) {
				if (indexNor % 14 == 0 || indexNor == nbDossiersToUpdate - 1) {
					migrationDetail.append(norDossiersList.get(indexNor)).append(".");
					// On créé le logger pour la liste
					final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
							MigrationDiscriminatorConstants.CLOS, migrationDetail.toString());
					migrationsDetails.add(migrationDetailModel);
					// On vide la liste pour en démarrer une nouvelle
					migrationDetail.delete(migrationDetailInit.length(), migrationDetail.length());
				} else {
					// On ajoute le nor à la liste existante
					migrationDetail.append(norDossiersList.get(indexNor)).append(", ");
				}
			}

			// update set ministereattache = new valeur[, directionattache = new valeur (si
			// nécessaire)] where id in
			// list
			final List<Integer> resultUpdate = new ArrayList<Integer>();
			getOrCreatePersistenceProvider().run(true, new RunVoid() {
				@Override
				public void runWith(EntityManager entityManager) throws ClientException {
					final StringBuilder query = new StringBuilder("UPDATE dossier_solon_epg SET ");
					query.append(DossierSolonEpgConstants.DOSSIER_MINISTERE_ATTACHE_PROPERTY).append(" = :ministere ");
					if (hasDirection) {
						query.append(",").append(DossierSolonEpgConstants.DOSSIER_DIRECTION_ATTACHE_PROPERTY)
								.append(" = :direction ");
					}
					query.append("WHERE id IN (").append(querySelectIds).append(")");
					Query queryType = entityManager.createNativeQuery(query.toString());
					queryType.setParameter("ministere", newMinistereNodeId);
					if (hasDirection) {
						queryType.setParameter("direction", newDirectionNodeId);
					}
					int updateExecuted = queryType.executeUpdate();
					resultUpdate.add(new Integer(updateExecuted));
				}
			});
			migrationLoggerModel.setNorDossierClosCurrent(resultUpdate.get(0));
			for (MigrationDetailModel migrationDetailModel : migrationsDetails) {
				migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
				flushMigrationDetail(migrationDetailModel);
			}

			flushMigrationLogger(migrationLoggerModel);
		}
	}

	@Override
	public void updateDossierMinistereRattachement(final CoreSession session, final OrganigrammeNode oldNode,
			final OrganigrammeNode newNode, final MigrationLoggerModel migrationLoggerModel) throws ClientException {
		updateDossierDirectionRattachement(session, oldNode, null, newNode, null, migrationLoggerModel);
	}

	@Override
	public void migrerBulletinOfficiel(final CoreSession session, final OrganigrammeNode oldNode,
			final OrganigrammeNode newNode, final MigrationLoggerModel migrationLoggerModel) throws ClientException {

		// Récupération des lettres de NOR de l'ancien ministère
		final String norAncienMinistere = ((EntiteNode) oldNode).getNorMinistere();

		// Récupération des lettres de NOR du nouveau ministère
		final String norNouveauMinistere = ((EntiteNode) newNode).getNorMinistere();

		// migration des bulletins officiels
		final BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
		bulletinOfficielService.migrerBulletinOfficiel(session, norAncienMinistere, norNouveauMinistere,
				migrationLoggerModel);
	}

	@Override
	public void migrerGestionIndexation(final CoreSession session, final OrganigrammeNode oldNode,
			final OrganigrammeNode newNode, final MigrationLoggerModel migrationLoggerModel) throws ClientException {
		final String ancienMinistereId = oldNode.getId();
		final String nouveauMinistereId = newNode.getId();

		final IndexationEpgService indexationEpgService = SolonEpgServiceLocator.getIndexationEpgService();
		indexationEpgService.migrerGestionIndexation(session, ancienMinistereId, nouveauMinistereId,
				migrationLoggerModel);
	}

	@Override
	public void migrerTableReferenceMinistere(final CoreSession session, final OrganigrammeNode oldNode,
			final OrganigrammeNode newNode, final MigrationLoggerModel migrationLoggerModel) throws ClientException {

		migrationLoggerModel.setTableRef(0);
		flushMigrationLogger(migrationLoggerModel);

		final String ancienMinistereId = oldNode.getId();
		final String nouveauMinistereId = newNode.getId();

		int nbMinistereToUpdate = 0;

		final List<String> params = new ArrayList<String>();

		// récupère les ministères à migrer
		final StringBuilder query = new StringBuilder();
		query.append("SELECT t.ecm:uuid as id FROM ");
		query.append(SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE);
		query.append(" as t WHERE t.");
		query.append(SolonEpgTableReferenceConstants.TABLE_REFERENCE_PREFIX);
		query.append(":");
		query.append(SolonEpgTableReferenceConstants.TABLE_REFERENCE_MINISTERE_CE);
		query.append(" = ? ");

		params.add(ancienMinistereId);

		query.append(" OR t.");
		query.append(SolonEpgTableReferenceConstants.TABLE_REFERENCE_PREFIX);
		query.append(":");
		query.append(SolonEpgTableReferenceConstants.TABLE_REFERENCE_MINISTERE_PM);
		query.append(" = ? ");

		params.add(ancienMinistereId);

		final List<DocumentModel> updateMinTabRef = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE, query.toString(), params.toArray());

		if (updateMinTabRef == null || updateMinTabRef.isEmpty()) {
			migrationLoggerModel.setTableRefCount(0);
			migrationLoggerModel.setTableRefCurrent(0);
			flushMigrationLogger(migrationLoggerModel);
			final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
					MigrationDiscriminatorConstants.TABLEREF,
					"Migration ministères Table de référence : aucun élement à migrer");
			migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
			flushMigrationDetail(migrationDetailModel);
		} else {
			nbMinistereToUpdate = updateMinTabRef.size();
			LOGGER.info(session, STLogEnumImpl.UPDATE_TABLE_REFERENCE_TEC,
					nbMinistereToUpdate + " table de référence mise à jour.");

			int index = 0;
			migrationLoggerModel.setTableRefCount(nbMinistereToUpdate);
			migrationLoggerModel.setTableRefCurrent(index);
			flushMigrationLogger(migrationLoggerModel);

			for (final DocumentModel documentModel : updateMinTabRef) {

				// on verrouille le document pour s'assurer que personne ne le modifie
				lockDocument(session, documentModel);

				final TableReference tableReference = documentModel.getAdapter(TableReference.class);

				final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
						MigrationDiscriminatorConstants.TABLEREF, "Migration ministères Table de référence");
				MigrationDetailModel migrationDetailModelCe = null;
				MigrationDetailModel migrationDetailModelPm = null;

				if (ancienMinistereId.equals(tableReference.getMinistereCE())) {
					migrationDetailModelCe = createMigrationDetailFor(migrationLoggerModel,
							MigrationDiscriminatorConstants.TABLEREF,
							"Migration ministères Table de référence : Conseil d'Etat");
					tableReference.setMinistereCE(nouveauMinistereId);
					migrationDetailModelCe.setEndDate(Calendar.getInstance().getTime());
					flushMigrationDetail(migrationDetailModelCe);
				}
				if (ancienMinistereId.equals(tableReference.getMinisterePM())) {
					migrationDetailModelPm = createMigrationDetailFor(migrationLoggerModel,
							MigrationDiscriminatorConstants.TABLEREF,
							"Migration ministères Table de référence : Ministère PM");
					tableReference.setMinisterePM(nouveauMinistereId);
					tableReference.setDirectionsPM(null);
					migrationDetailModelPm.setEndDate(Calendar.getInstance().getTime());
					flushMigrationDetail(migrationDetailModelPm);
				}

				tableReference.save(session);

				// on enlève le verrou
				session.removeLock(documentModel.getRef());

				if (migrationDetailModelCe != null) {
					flushMigrationDetail(migrationDetailModelCe);
				}
				if (migrationDetailModelPm != null) {
					flushMigrationDetail(migrationDetailModelPm);
				}
				migrationLoggerModel.setTableRefCurrent(++index);
				flushMigrationLogger(migrationLoggerModel);
				migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
				flushMigrationDetail(migrationDetailModel);
			}
		}

		migrationLoggerModel.setTableRef(1);
		flushMigrationLogger(migrationLoggerModel);
	}

	@Override
	public void migrerTableReferenceDirection(final CoreSession session, final OrganigrammeNode oldMinistereNode,
			final OrganigrammeNode oldDirectionNode, final OrganigrammeNode newMinistereNode,
			final OrganigrammeNode newDirectionNode, final MigrationLoggerModel migrationLoggerModel)
			throws ClientException {

		migrationLoggerModel.setTableRef(0);
		flushMigrationLogger(migrationLoggerModel);

		final String ancienMinistereId = oldMinistereNode.getId();
		final String ancienneDirectionId = oldDirectionNode.getId();
		final String nouveauMinistereId = newMinistereNode.getId();
		final String nouvelleDirectionId = newDirectionNode.getId();

		int nbDirectionToUpdate = 0;

		final List<String> params = new ArrayList<String>();

		// récupère les ministères à migrer
		final StringBuilder query = new StringBuilder();
		query.append("SELECT t.ecm:uuid as id FROM ");
		query.append(SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE);
		query.append(" as t WHERE t.");
		query.append(SolonEpgTableReferenceConstants.TABLE_REFERENCE_PREFIX);
		query.append(":");
		query.append(SolonEpgTableReferenceConstants.TABLE_REFERENCE_MINISTERE_PM);
		query.append(" = ? ");
		params.add(ancienMinistereId);

		final List<DocumentModel> updateDirTabRef = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE, query.toString(), params.toArray());

		if (updateDirTabRef == null || updateDirTabRef.isEmpty()) {
			migrationLoggerModel.setTableRefCount(0);
			migrationLoggerModel.setTableRefCurrent(0);
			flushMigrationLogger(migrationLoggerModel);
			final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
					MigrationDiscriminatorConstants.TABLEREF,
					"Migration direction Table de référence : aucun élement à migrer");
			migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
			flushMigrationDetail(migrationDetailModel);

		} else {
			nbDirectionToUpdate = updateDirTabRef.size();

			migrationLoggerModel.setTableRefCount(nbDirectionToUpdate);
			int index = 0;
			migrationLoggerModel.setTableRefCurrent(index);
			flushMigrationLogger(migrationLoggerModel);

			LOGGER.info(session, STLogEnumImpl.UPDATE_TABLE_REFERENCE_TEC,
					nbDirectionToUpdate + " table de référence mise à jour.");
			for (final DocumentModel documentModel : updateDirTabRef) {
				// on verrouille le document pour s'assurer que personne ne le modifie
				lockDocument(session, documentModel);

				final TableReference tableReference = documentModel.getAdapter(TableReference.class);
				final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
						MigrationDiscriminatorConstants.TABLEREF, "Migration direction Table de référence");
				final List<String> directionPm = tableReference.getDirectionsPM();
				if (directionPm == null || !directionPm.contains(ancienneDirectionId)) {
					migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
					flushMigrationDetail(migrationDetailModel);

					// on enlève le verrou
					session.removeLock(documentModel.getRef());

					migrationLoggerModel.setTableRefCurrent(++index);
					flushMigrationLogger(migrationLoggerModel);
					break;
				}

				if (ancienMinistereId.equals(nouveauMinistereId)) {
					// si le nouveau ministère est aussi le ministère Pm, la nouvelle direction
					// remplace l'ancienne
					// direction proposé dans les directions PRM.
					directionPm.add(nouvelleDirectionId);
				}
				directionPm.remove(ancienneDirectionId);
				tableReference.setDirectionsPM(directionPm);
				tableReference.save(session);

				// on enlève le verrou
				session.removeLock(documentModel.getRef());

				migrationLoggerModel.setTableRefCurrent(++index);
				flushMigrationLogger(migrationLoggerModel);
				migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
				flushMigrationDetail(migrationDetailModel);
			}
		}

		migrationLoggerModel.setTableRef(1);
		flushMigrationLogger(migrationLoggerModel);
	}

	@Override
	public void migrerTableReferencePoste(CoreSession session, OrganigrammeNode oldPosteNode,
			OrganigrammeNode newPosteNode, MigrationLoggerModel migrationLoggerModel) throws ClientException {

		migrationLoggerModel.setTableRef(0);
		flushMigrationLogger(migrationLoggerModel);

		final String ancienPosteId = oldPosteNode.getId();
		final String nouveauPosteId = newPosteNode.getId();

		final DocumentModel tableReferenceDoc = SolonEpgServiceLocator.getTableReferenceService()
				.getTableReference(session);
		final TableReference tableReference = tableReferenceDoc.getAdapter(TableReference.class);
		final List<String> postesRetourDan = tableReference.getRetourDAN();

		if (postesRetourDan == null || postesRetourDan.isEmpty() || !postesRetourDan.contains(ancienPosteId)) {
			migrationLoggerModel.setTableRefCount(0);
			migrationLoggerModel.setTableRefCurrent(0);
			flushMigrationLogger(migrationLoggerModel);
			final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
					MigrationDiscriminatorConstants.TABLEREF,
					"Migration poste Table de référence : aucun élement à migrer");
			migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
			flushMigrationDetail(migrationDetailModel);

		} else {
			migrationLoggerModel.setTableRefCount(1);

			// on verrouille le document pour s'assurer que personne ne le modifie
			lockDocument(session, tableReferenceDoc);

			final MigrationDetailModel migrationDetailModel = createMigrationDetailFor(migrationLoggerModel,
					MigrationDiscriminatorConstants.TABLEREF,
					"Migration poste Table de référence : Corbeille de retour au DAN");

			postesRetourDan.add(nouveauPosteId);
			postesRetourDan.remove(ancienPosteId);
			tableReference.setRetourDAN(postesRetourDan);
			tableReference.save(session);

			// on enlève le verrou
			session.removeLock(tableReferenceDoc.getRef());

			migrationLoggerModel.setTableRefCurrent(1);
			flushMigrationLogger(migrationLoggerModel);
			migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
			flushMigrationDetail(migrationDetailModel);
		}

		migrationLoggerModel.setTableRef(1);
		flushMigrationLogger(migrationLoggerModel);
	}

	/**
	 * Lock le document même si le document en question était locké par un autre
	 * utilisateur.
	 * 
	 * @throws ClientException
	 */
	private void lockDocument(final CoreSession session, final DocumentModel docModel) throws ClientException {
		final DocumentRef docRef = docModel.getRef();
		// si le document est verrouillé, on le déverrouille
		if (docModel.isLocked()) {
			session.removeLock(docRef);
		}
		// on verrouille le document pour s'assurer que personne ne le modifie
		session.setLock(docRef);
	}

	@Override
	public MigrationLoggerModel createMigrationLogger(final String ssPrincipal) throws ClientException {
		return getOrCreatePersistenceProvider().run(true, new RunCallback<MigrationLoggerModel>() {

			@Override
			public MigrationLoggerModel runWith(final EntityManager entityManager) {

				final MigrationLoggerModel migrationLoggerModel = new MigrationLoggerModelImpl();
				migrationLoggerModel.setStartDate(Calendar.getInstance().getTime());
				migrationLoggerModel.setPrincipalName(ssPrincipal);

				entityManager.persist(migrationLoggerModel);

				return migrationLoggerModel;
			}
		});
	}

	/**
	 * Recuperation du persistence provider
	 * 
	 * @return
	 */
	private static PersistenceProvider getOrCreatePersistenceProvider() {
		if (persistenceProvider == null) {
			synchronized (ChangementGouvernementServiceImpl.class) {
				if (persistenceProvider == null) {
					activatePersistenceProvider();
				}
			}
		}
		return persistenceProvider;
	}

	private static void activatePersistenceProvider() {
		final Thread thread = Thread.currentThread();
		final ClassLoader last = thread.getContextClassLoader();
		try {
			thread.setContextClassLoader(PersistenceProvider.class.getClassLoader());
			final PersistenceProviderFactory persistenceProviderFactory = Framework
					.getLocalService(PersistenceProviderFactory.class);
			persistenceProvider = persistenceProviderFactory.newProvider("SWMIGRATIONLOGGER");
			persistenceProvider.openPersistenceUnit();
		} finally {
			thread.setContextClassLoader(last);
		}
	}

	@Override
	public void deactivate(final ComponentContext context) throws Exception {
		deactivatePersistenceProvider();
	}

	private static void deactivatePersistenceProvider() {
		if (persistenceProvider != null) {
			synchronized (ChangementGouvernementServiceImpl.class) {
				if (persistenceProvider != null) {
					persistenceProvider.closePersistenceUnit();
					persistenceProvider = null;
				}
			}
		}
	}

	@Override
	public MigrationLoggerModel findMigrationById(final Long idLogger) throws ClientException {

		return getOrCreatePersistenceProvider().run(true, new RunCallback<MigrationLoggerModel>() {

			@Override
			public MigrationLoggerModel runWith(final EntityManager entityManager) {
				try {
					return (MigrationLoggerModel) entityManager
							.createQuery("Select log from MigrationLogger log where log.id = :idLogger")
							.setParameter("idLogger", idLogger).getSingleResult();
				} catch (final NoResultException e) {
					return null;
				}
			}
		});

	}

	@Override
	public void flushMigrationLogger(final MigrationLoggerModel migrationLoggerModel) throws ClientException {
		getOrCreatePersistenceProvider().run(true, new RunCallback<String>() {

			@Override
			public String runWith(final EntityManager entityManager) {
				final MigrationLoggerModel model = entityManager.merge(migrationLoggerModel);
				entityManager.persist(model);
				return null;
			}
		});
	}

	@Override
	public MigrationDetailModel createMigrationDetailFor(final MigrationLoggerModel migrationLoggerModel,
			final String type, final String detail) throws ClientException {
		return createMigrationDetailFor(migrationLoggerModel, type, detail, "OK");
	}

	@Override
	public MigrationDetailModel createMigrationDetailFor(final MigrationLoggerModel migrationLoggerModel,
			final String type, final String detail, final String statut) throws ClientException {
		return getOrCreatePersistenceProvider().run(true, new RunCallback<MigrationDetailModel>() {

			@Override
			public MigrationDetailModel runWith(final EntityManager entityManager) {
				final MigrationLoggerModel model = entityManager.merge(migrationLoggerModel);
				entityManager.persist(model);

				MigrationDetailModel migrationDetail = null;
				switch (MigrationDiscirminatorEnum.find(type)) {
				case FDR:
					migrationDetail = new ModeleFDRDetailModelImpl();
					break;
				case STEP:
					migrationDetail = new StepDetailModelImpl();
					break;
				case LANCE:
					migrationDetail = new LancerDetailModelImpl();
					break;
				case CLOS:
					migrationDetail = new ClosDetailModelImpl();
					break;
				case BO:
					migrationDetail = new BulletinDetailModelImpl();
					break;
				case MOTSCLES:
					migrationDetail = new MotsClesDetailModelImpl();
					break;
				case CREATOR:
					migrationDetail = new CreatorDetailModelImpl();
					break;
				case MAILBOX:
					migrationDetail = new MailBoxDetailModelImpl();
					break;
				case TABLEREF:
					migrationDetail = new TableRefDetailModelImpl();
					break;
				default:
					break;
				}

				if (migrationDetail != null) {
					migrationDetail.setMigration(migrationLoggerModel);
					migrationDetail.setDetail(detail);
					migrationDetail.setStartDate(Calendar.getInstance().getTime());
					migrationDetail.setStatut(statut);
					entityManager.persist(migrationDetail);
				}
				LOGGER.info(STLogEnumImpl.LOG_INFO_TEC, detail);

				return migrationDetail;
			}
		});
	}

	@Override
	public void flushMigrationDetail(final MigrationDetailModel migrationDetailModel) throws ClientException {
		getOrCreatePersistenceProvider().run(true, new RunCallback<String>() {

			@Override
			public String runWith(final EntityManager entityManager) {
				final MigrationDetailModel model = entityManager.merge(migrationDetailModel);
				entityManager.persist(model);
				return null;
			}
		});
	}

	@Override
	public void reattribuerMinistereActiviteNormative(final CoreSession session,
			final OrganigrammeNode oldMinistereNode, final OrganigrammeNode newMinistereNode) throws ClientException {

		final List<DocumentModel> listTexteMaitre = SolonEpgServiceLocator.getActiviteNormativeService()
				.findTexteMaitreByMinisterePilote(oldMinistereNode.getId(), session);
		for (final DocumentModel documentModel : listTexteMaitre) {
			final TexteMaitre texteMaitre = documentModel.getAdapter(TexteMaitre.class);
			texteMaitre.setMinisterePilote(newMinistereNode.getId());
			session.saveDocument(texteMaitre.getDocument());
		}

	}

	@Override
	public List<MigrationLoggerModel> getMigrationWithoutEndDate() throws ClientException {
		return getOrCreatePersistenceProvider().run(true, new RunCallback<List<MigrationLoggerModel>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<MigrationLoggerModel> runWith(final EntityManager entityManager) {
				try {
					return entityManager.createQuery("Select log from MigrationLogger log where log.endDate is null")
							.getResultList();
				} catch (final NoResultException e) {
					return null;
				}
			}
		});
	}

	@Override
	public List<MigrationLoggerModel> getMigrationLoggerModelList() throws ClientException {
		return getOrCreatePersistenceProvider().run(true, new RunCallback<List<MigrationLoggerModel>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<MigrationLoggerModel> runWith(final EntityManager entityManager) {
				try {
					return entityManager.createQuery("Select log from MigrationLogger log order by log.startDate")
							.getResultList();
				} catch (final NoResultException e) {
					return null;
				}
			}
		});

	}

	@Override
	public List<MigrationDetailModel> getMigrationDetailModelList(final Long loggerId) throws ClientException {
		return getOrCreatePersistenceProvider().run(true, new RunCallback<List<MigrationDetailModel>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<MigrationDetailModel> runWith(final EntityManager entityManager) {
				try {
					final Query query = entityManager.createQuery(
							"Select log from MigrationDetail log where log.migration.id = :migration order by log.startDate");
					query.setParameter("migration", loggerId);
					return query.getResultList();
				} catch (final NoResultException e) {
					return null;
				}
			}
		});

	}

	private void sendMail(CoreSession session, String adresse, String objet, String corps) {
		final STMailService mailService = STServiceLocator.getSTMailService();
		try {
			if (adresse != null) {
				mailService.sendTemplateMail(adresse, objet, corps, null);
			} else {
				LOGGER.warn(session, STLogEnumImpl.FAIL_GET_MAIL_TEC);
			}
		} catch (final ClientException exc) {
			LOGGER.error(session, STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
		}
	}

	@Override
	public String getLogMessage(MigrationLoggerModel migrationLoggerModel) throws ClientException {
		String typeMigration = migrationLoggerModel.getTypeMigration();
		StringBuilder message = new StringBuilder();
		if (VocabularyConstants.MIN_TYPE.equals(typeMigration)) {
			final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
			OrganigrammeNode oldMinistereNode = ministeresService.getEntiteNode(migrationLoggerModel.getOldElement());
			OrganigrammeNode newMinistereNode = ministeresService.getEntiteNode(migrationLoggerModel.getNewElement());
			message.append("Migration du ");
			if (oldMinistereNode == null) {
				appendPost(message, "** Ministère inconnu **", "Ministère");
			} else {
				appendPost(message, oldMinistereNode.getLabel(), "Ministère");

			}
			message.append(" vers ");
			if (newMinistereNode == null) {
				appendPost(message, "** Ministère inconnu **", "Ministère");
			} else {
				appendPost(message, newMinistereNode.getLabel(), "Ministère");
			}

		} else if (VocabularyConstants.DIR_TYPE.equals(typeMigration)) {
			final STUsAndDirectionService usService = STServiceLocator.getSTUsAndDirectionService();
			OrganigrammeNode oldDirectionNode = usService
					.getUniteStructurelleNode(migrationLoggerModel.getOldElement());
			OrganigrammeNode newDirectionNode = usService
					.getUniteStructurelleNode(migrationLoggerModel.getNewElement());

			message.append("Migration du ");
			appendPost(message, oldDirectionNode.getLabel(), "Direction");
			message.append(" vers ");
			appendPost(message, newDirectionNode.getLabel(), "Direction");

		} else if (VocabularyConstants.UST_TYPE.equals(typeMigration)) {
			final STUsAndDirectionService usService = STServiceLocator.getSTUsAndDirectionService();
			OrganigrammeNode oldUniteStruct = usService.getUniteStructurelleNode(migrationLoggerModel.getOldElement());
			OrganigrammeNode newUniteStruct = usService.getUniteStructurelleNode(migrationLoggerModel.getNewElement());

			message.append("Migration de ");
			appendPost(message, oldUniteStruct.getLabel(), "l' Unité structurelle");
			message.append(" vers ");
			appendPost(message, newUniteStruct.getLabel(), "l' Unité structurelle");

		} else if (VocabularyConstants.POSTE_TYPE.equals(typeMigration)) {
			final STPostesService postesService = STServiceLocator.getSTPostesService();
			OrganigrammeNode oldPosteNode = postesService.getPoste(migrationLoggerModel.getOldElement());
			OrganigrammeNode newPosteNode = postesService.getPoste(migrationLoggerModel.getNewElement());

			message.append("Migration du ");
			appendPost(message, oldPosteNode.getLabel(), "Poste");
			message.append(" vers ");
			appendPost(message, newPosteNode.getLabel(), "Poste");
		}

		return message.toString();
	}

	private void appendPost(StringBuilder builder, String label, String post) {
		if (!label.contains(post)) {
			builder.append(" ");
			builder.append(post);
			builder.append(" ");
		}
		builder.append(label);
	}

}
