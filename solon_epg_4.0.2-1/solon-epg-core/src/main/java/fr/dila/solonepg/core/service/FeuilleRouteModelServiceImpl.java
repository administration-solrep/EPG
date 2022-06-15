package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.criteria.EpgFeuilleRouteCriteria;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.service.EpgDocumentRoutingService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.core.dao.EpgFeuilleRouteDao;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.ss.api.migration.MigrationDetailModel;
import fr.dila.ss.api.migration.MigrationDiscriminatorConstants;
import fr.dila.ss.api.migration.MigrationLoggerModel;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.api.vocabulary.VocabularyEntry;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteElement.ElementLifeCycleState;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * Implémentation du service de feuille de route de l'application SOLON EPG.
 *
 * @author jtremeaux
 */
public class FeuilleRouteModelServiceImpl
    extends fr.dila.ss.core.service.FeuilleRouteModelServiceImpl
    implements FeuilleRouteModelService {
    /**
     * UID.
     */
    private static final long serialVersionUID = -2392698015083550568L;

    /**
     * Logger.
     */
    private static final STLogger LOG = STLogFactory.getLog(FeuilleRouteModelServiceImpl.class);

    @Override
    public SSFeuilleRoute getDefaultRoute(
        final CoreSession session,
        final String typeActeId,
        final String ministereId,
        final String directionId
    ) {
        // Vérification des paramètres
        if (StringUtils.isEmpty(typeActeId)) {
            throw new NuxeoException("Type d'acte non défini");
        }
        if (StringUtils.isEmpty(ministereId)) {
            throw new NuxeoException("Ministère non défini");
        }
        if (StringUtils.isEmpty(directionId)) {
            throw new NuxeoException("Direction non définie");
        }

        // Recherche de la feuille de route par défaut pour un type d'acte, un ministère
        // et une direction
        EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        criteria.setFeuilleRouteDefaut(true);
        criteria.setCurrentLifeCycleState(ElementLifeCycleState.validated.name());
        criteria.setTypeActe(typeActeId);
        criteria.setMinistere(ministereId);
        criteria.setDirection(directionId);
        EpgFeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);
        List<DocumentModel> docList = feuilleRouteDao.list(session);
        if (docList != null && !docList.isEmpty()) {
            if (docList.size() > 1) {
                LOG.warn(
                    session,
                    SSLogEnumImpl.GET_MOD_FDR_TEC,
                    "Il y a plus d'un modèle de feuille de route par défaut définie pour la combinaison typeActe=<" +
                    typeActeId +
                    "> ministere=<" +
                    ministereId +
                    "> direction=<" +
                    directionId +
                    ">"
                );
            }

            final DocumentModel doc = docList.get(0);
            return doc.getAdapter(SSFeuilleRoute.class);
        }

        // Recherche de la feuille de route par défaut pour un type d'acte et un
        // ministère
        criteria = new EpgFeuilleRouteCriteria();
        criteria.setFeuilleRouteDefaut(true);
        criteria.setCurrentLifeCycleState(ElementLifeCycleState.validated.name());
        criteria.setTypeActe(typeActeId);
        criteria.setMinistere(ministereId);
        criteria.setDirectionNull(true);
        feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);
        docList = feuilleRouteDao.list(session);
        if (docList != null && !docList.isEmpty()) {
            if (docList.size() > 1) {
                LOG.warn(
                    session,
                    SSLogEnumImpl.GET_MOD_FDR_TEC,
                    "Il y a plus d'un modèle de feuille de route par défaut définie pour la combinaison typeActe=<" +
                    typeActeId +
                    ">, ministere=<" +
                    ministereId +
                    ">, direction non définie"
                );
            }

            final DocumentModel doc = docList.get(0);
            return doc.getAdapter(SSFeuilleRoute.class);
        }

        // Recherche de la feuille de route par défaut pour un ministère
        criteria = new EpgFeuilleRouteCriteria();
        criteria.setFeuilleRouteDefaut(true);
        criteria.setCurrentLifeCycleState(ElementLifeCycleState.validated.name());
        criteria.setTypeActeNull(true);
        criteria.setMinistere(ministereId);
        criteria.setDirectionNull(true);
        feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);
        docList = feuilleRouteDao.list(session);
        if (docList != null && !docList.isEmpty()) {
            if (docList.size() > 1) {
                LOG.warn(
                    session,
                    SSLogEnumImpl.GET_MOD_FDR_TEC,
                    "Il y a plus d'un modèle de feuille de route par défaut définie pour la combinaison ministere=<" +
                    ministereId +
                    ">, direction et type d'acte non définis"
                );
            }

            final DocumentModel doc = docList.get(0);
            return doc.getAdapter(SSFeuilleRoute.class);
        }

        // Recherche de la feuille de route par défaut globale
        criteria = new EpgFeuilleRouteCriteria();
        criteria.setFeuilleRouteDefaut(true);
        criteria.setCurrentLifeCycleState(ElementLifeCycleState.validated.name());
        criteria.setTypeActeNull(true);
        criteria.setMinistereNull(true);
        criteria.setDirectionNull(true);
        feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);
        docList = feuilleRouteDao.list(session);
        if (docList != null && !docList.isEmpty()) {
            if (docList.size() > 1) {
                LOG.warn(
                    session,
                    SSLogEnumImpl.GET_MOD_FDR_TEC,
                    "Il y a plus d'un modèle de feuille de route par défaut global"
                );
            }

            final DocumentModel doc = docList.get(0);
            return doc.getAdapter(SSFeuilleRoute.class);
        }
        return null;
    }

    @Override
    public FeuilleRoute getDefaultRouteGlobal(CoreSession session) {
        // Recherche de la feuille de route par défaut globale
        EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        criteria.setFeuilleRouteDefaut(true);
        criteria.setCurrentLifeCycleState(ElementLifeCycleState.validated.name());
        criteria.setTypeActeNull(true);
        criteria.setMinistereNull(true);
        criteria.setDirectionNull(true);
        EpgFeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);
        List<DocumentModel> docList = feuilleRouteDao.list(session);
        if (docList != null && !docList.isEmpty()) {
            if (docList.size() > 1) {
                LOG.warn(
                    session,
                    SSLogEnumImpl.GET_MOD_FDR_TEC,
                    "Il y a plus d'un modèle de feuille de route par défaut global"
                );
            }

            final DocumentModel doc = docList.get(0);
            return doc.getAdapter(FeuilleRoute.class);
        }
        return null;
    }

    @Override
    public void migrerModeleFdrDirection(
        final CoreSession session,
        final EntiteNode oldMinistereNode,
        final UniteStructurelleNode oldDirectionNode,
        final EntiteNode newMinistereNode,
        final UniteStructurelleNode newDirectionNode,
        final MigrationLoggerModel migrationLoggerModel,
        Boolean desactivateModelFdr
    ) {
        final boolean hasDirection = oldDirectionNode != null;

        // récupère les ids des noeuds ministères
        final String oldMinistereNodeId = oldMinistereNode.getId();
        final String newMinistereNodeId = newMinistereNode.getId();
        String oldDirectionNodeId = null;
        String newDirectionNodeId = null;

        if (hasDirection) {
            oldDirectionNodeId = oldDirectionNode.getId();
            newDirectionNodeId = newDirectionNode.getId();
        }
        final List<DocumentModel> fdrModelToUpdate = getFdrModelFromMinistereAndDirection(
            session,
            oldMinistereNodeId,
            oldDirectionNodeId,
            hasDirection
        );

        migrationLoggerModel.setModeleFdr(0);
        SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);

        if (fdrModelToUpdate == null || fdrModelToUpdate.isEmpty()) {
            migrationLoggerModel.setModeleFdr(1);
            migrationLoggerModel.setModeleFdrCount(0);
            migrationLoggerModel.setModeleFdrCurrent(0);
            SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);
            final MigrationDetailModel migrationDetailModel = SolonEpgServiceLocator
                .getChangementGouvernementService()
                .createMigrationDetailFor(
                    migrationLoggerModel,
                    MigrationDiscriminatorConstants.FDR,
                    "Aucun modèle de feuille de route",
                    "OK"
                );
            migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
            SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationDetail(migrationDetailModel);
        } else {
            final int size = fdrModelToUpdate.size();
            LOG.info(
                session,
                SSLogEnumImpl.MIGRATE_MOD_FDR_TEC,
                size + " modèles de feuille de route à mettre à jour."
            );
            migrationLoggerModel.setModeleFdrCount(size);

            int index = 0;
            migrationLoggerModel.setModeleFdrCurrent(index);
            SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);
            final EpgDocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();

            for (final DocumentModel documentModel : fdrModelToUpdate) {
                if (desactivateModelFdr) {
                    final MigrationDetailModel migrationDetailModel = SolonEpgServiceLocator
                        .getChangementGouvernementService()
                        .createMigrationDetailFor(
                            migrationLoggerModel,
                            MigrationDiscriminatorConstants.FDR,
                            "Désactivation du modèle de feuille de route '" + documentModel.getTitle() + "' : Début",
                            "KO"
                        );
                    SSFeuilleRoute routeModel = documentModel.getAdapter(SSFeuilleRoute.class);
                    if (routeModel.isValidated()) {
                        SSServiceLocator.getDocumentRoutingService().lockDocumentRoute(routeModel, session);
                        documentRoutingService.invalidateRouteModel(routeModel, session);
                        SSServiceLocator.getDocumentRoutingService().unlockDocumentRoute(routeModel, session);
                    }
                    migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
                    migrationDetailModel.setStatut("OK");
                    SolonEpgServiceLocator
                        .getChangementGouvernementService()
                        .flushMigrationDetail(migrationDetailModel);
                }

                final MigrationDetailModel migrationDetailModel = SolonEpgServiceLocator
                    .getChangementGouvernementService()
                    .createMigrationDetailFor(
                        migrationLoggerModel,
                        MigrationDiscriminatorConstants.FDR,
                        "Migration modèle de feuille de route '" + documentModel.getTitle() + "' : Début",
                        "KO"
                    );
                final MigrationDetailModel migrationDetailModel2 = SolonEpgServiceLocator
                    .getChangementGouvernementService()
                    .createMigrationDetailFor(
                        migrationLoggerModel,
                        MigrationDiscriminatorConstants.FDR,
                        "Renommage du modèle de feuille de route '" + documentModel.getTitle() + "' : Début",
                        "KO"
                    );

                // on verrouille le document pour s'assurer que personne ne le modifie
                final DocumentRef docRef = documentModel.getRef();
                // si le document est verrouillé, on le déverrouille
                if (documentModel.isLocked()) {
                    session.removeLock(docRef);
                }
                // on verrouille le document pour s'assurer que personne ne le modifie
                session.setLock(docRef);

                final SolonEpgFeuilleRoute solonEpgFeuilleRoute = documentModel.getAdapter(SolonEpgFeuilleRoute.class);
                solonEpgFeuilleRoute.setMinistere(newMinistereNodeId);
                if (hasDirection) {
                    solonEpgFeuilleRoute.setDirection(newDirectionNodeId);
                }

                // on renomme le modèle de feuille de route si l'ancien intitulé est au bon
                // format
                String oldIntitule = documentModel.getTitle();
                if (isOldIntituleCorrect(oldIntitule, oldMinistereNode, oldDirectionNode, hasDirection)) {
                    final String newIntitule = newIntitule(
                        documentModel.getTitle(),
                        oldDirectionNode,
                        newMinistereNode,
                        newDirectionNode,
                        hasDirection
                    );
                    solonEpgFeuilleRoute.setTitle(newIntitule);
                    migrationDetailModel2.setDetail(
                        "Renommage du modèle de feuille de route '" +
                        oldIntitule +
                        "' vers '" +
                        "'" +
                        newIntitule +
                        "' : Début"
                    );
                    migrationDetailModel2.setEndDate(Calendar.getInstance().getTime());
                    migrationDetailModel2.setStatut("OK");
                    SolonEpgServiceLocator
                        .getChangementGouvernementService()
                        .flushMigrationDetail(migrationDetailModel2);
                }
                solonEpgFeuilleRoute.save(session);
                // on enlève le verrou
                session.removeLock(docRef);

                migrationLoggerModel.setModeleFdrCurrent(++index);
                SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);
                migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
                migrationDetailModel.setStatut("OK");
                SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationDetail(migrationDetailModel);
            }
        }
        migrationLoggerModel.setModeleFdr(1);
        SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);
    }

    /**
     * FEV525 : Modification de l'intitule lors d'un changement de ministere.
     * '*trigramme_ministère* - *lettre_direction* - *type_acte* - intitulé libre'
     * Seules le trigramme ministere et la lettre de direction peuvent changer.
     *
     */
    private String newIntitule(
        final String oldIntitule,
        final UniteStructurelleNode oldDirectionNode,
        final EntiteNode newMinistereNode,
        final UniteStructurelleNode newDirectionNode,
        final boolean hasDirection
    ) {
        /*
         * 1 : on retire la partie variable de l'ancien intitulé. 2 : on crée le nouvel
         * intitule
         */
        String keep = null; // partie du libelle que l'on conserve
        if (hasDirection && oldDirectionNode != null) {
            // Il y avait anciennement une direction
            // la chaine de début est donc du style "XXXX - X - "
            keep = oldIntitule.substring(10);
        } else {
            // l'ancien modele ne disposait que d'un ministère
            // la chaine de début est "XXXX - "
            keep = oldIntitule.substring(6);
        }

        StringBuilder newTitle = new StringBuilder(); // chaine pour remplacer le début
        newTitle.append(newMinistereNode.getNorMinistere());
        if (newDirectionNode != null) {
            newTitle.append(" - ");
            newTitle.append(newDirectionNode.getNorDirectionForMinistereId(newMinistereNode.getId()));
        }

        newTitle.append(" - ");
        newTitle.append(keep);

        return newTitle.toString();
    }

    /**
     * FEV576 : Vérification du format de l'intitulé initial
     */
    private boolean isOldIntituleCorrect(
        final String oldIntitule,
        final EntiteNode oldMinistereNode,
        final UniteStructurelleNode oldDirectionNode,
        final boolean hasDirection
    ) {
        // Génération de l'intitulé fixe du modèle de feuille de route
        String fixe = oldMinistereNode.getNorMinistere() + " - ";
        if (hasDirection && oldDirectionNode != null) {
            fixe += oldDirectionNode.getNorDirectionForMinistereId(oldMinistereNode.getId()) + " - ";
        }

        // Vérifier si l'intitulé initial commence par cet intitulé fixe
        return oldIntitule.startsWith(fixe);
    }

    @Override
    public void migrerModeleFdrMinistere(
        final CoreSession session,
        final EntiteNode oldNode,
        final EntiteNode newNode,
        final MigrationLoggerModel migrationLoggerModel,
        Boolean desactivateModelFdr
    ) {
        migrerModeleFdrDirection(session, oldNode, null, newNode, null, migrationLoggerModel, desactivateModelFdr);
    }

    @Override
    public List<DocumentModel> getFdrModelFromMinistereAndDirection(
        final CoreSession session,
        final String ministereId,
        final String directionId,
        final boolean hasDirection
    ) {
        // récupère l'id du répertoire des feuilles de route
        final String modelFolderId = getFeuilleRouteModelFolderId(session);

        // migre les modeles de feuille de route pour les ministères
        final StringBuilder query = new StringBuilder();
        query.append("SELECT f.ecm:uuid as id FROM ");
        query.append(SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE);
        query.append(" as f WHERE f.ecm:parentId = ? ");

        final List<String> params = new ArrayList<>();
        params.add(modelFolderId);

        if (StringUtils.isNotEmpty(ministereId)) {
            query.append(" and f.");
            query.append(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA_PREFIX);
            query.append(":");
            query.append(SSFeuilleRouteConstant.FEUILLE_ROUTE_MINISTERE_PROPERTY);
            query.append(" = ? ");

            params.add(ministereId);
        }
        if (hasDirection) {
            query.append(" and f.");
            query.append(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA_PREFIX);
            query.append(":");
            query.append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_DIRECTION_PROPERTY);
            query.append(" = ? ");

            params.add(directionId);
        }

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE,
            query.toString(),
            params.toArray()
        );
    }

    @Override
    public List<DocumentModel> getNonDeletedFdrModelFromMinistereAndDirection(
        final CoreSession session,
        final String ministereId,
        final String directionId,
        final boolean hasDirection
    ) {
        // récupère l'id du répertoire des feuilles de route
        final String modelFolderId = getFeuilleRouteModelFolderId(session);

        // migre les modeles de feuille de route pour les ministères
        final StringBuilder query = new StringBuilder();
        query.append("SELECT f.ecm:uuid as id FROM ");
        query.append(SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE);
        query.append(" as f WHERE f.ecm:parentId = ? AND f.ecm:currentLifeCycleState != 'deleted' ");

        final List<String> params = new ArrayList<>();
        params.add(modelFolderId);

        if (StringUtils.isNotEmpty(ministereId)) {
            query.append(" and f.");
            query.append(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA_PREFIX);
            query.append(":");
            query.append(SSFeuilleRouteConstant.FEUILLE_ROUTE_MINISTERE_PROPERTY);
            query.append(" = ? ");

            params.add(ministereId);
        }
        if (hasDirection) {
            query.append(" and f.");
            query.append(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA_PREFIX);
            query.append(":");
            query.append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_DIRECTION_PROPERTY);
            query.append(" = ? ");

            params.add(directionId);
        }

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE,
            query.toString(),
            params.toArray()
        );
    }

    private static final String FEUILLE_ROUTE_SQUELETTE_FOLDER_QUERY =
        "SELECT * FROM " + SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_FOLDER_DOCUMENT_TYPE + " WHERE ecm:isProxy = 0";

    /**
     * Conserve l'id du document Racine des modèles de feuilles de route Evite une
     * requete SQL systematique à la base pour récupérer l'id qui ne change pas
     */
    private String feuilleRouteSqueletteFolderDocId;

    @Override
    public DocumentModel getFeuilleRouteSqueletteFolder(final CoreSession session) {
        if (feuilleRouteSqueletteFolderDocId == null) {
            final DocumentModelList list = session.query(FEUILLE_ROUTE_SQUELETTE_FOLDER_QUERY);
            if (list == null || list.isEmpty()) {
                throw new NuxeoException("Racine des squelettes de feuilles de route non trouvée");
            } else if (list.size() > 1) {
                throw new NuxeoException("Plusieurs racines des squelettes de feuilles de route trouvées");
            }

            final DocumentModel doc = list.get(0);
            feuilleRouteSqueletteFolderDocId = doc.getId();
            return doc;
        } else {
            return session.getDocument(new IdRef(feuilleRouteSqueletteFolderDocId));
        }
    }

    @Override
    public Integer creeModelesViaSquelette(
        CoreSession session,
        String ministereId,
        String bdcId,
        String cdmId,
        String scdmId,
        String cpmId
    ) {
        LOG.info(
            session,
            SSLogEnumImpl.CREATE_FDR_TEC,
            "Début de la génération des modeles de feuille de route pour le ministère : " +
            ministereId +
            ", bureau des cabinets : " +
            bdcId +
            ", charge de mission : " +
            cdmId +
            ", secrétariat du charge de mission : " +
            scdmId +
            ", conseiller PM : " +
            cpmId +
            "."
        );

        // récupération de la liste des squelettes
        List<DocumentModel> squelList = getAllValidatedSquelette(session);
        if (CollectionUtils.isNotEmpty(squelList)) {
            LOG.info(session, SSLogEnumImpl.CREATE_FDR_TEC, squelList.size() + " squelettes actifs.");

            // récupère le répertoire des feuilles de route
            final DocumentModel racineModel = getFeuilleRouteModelFolder(session);

            EpgDocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
            Iterator<DocumentModel> itSquel = squelList.iterator();
            DocumentModel curSquel = null;

            while (itSquel.hasNext()) {
                curSquel = itSquel.next();
                final SolonEpgFeuilleRoute model = curSquel.getAdapter(SolonEpgFeuilleRoute.class);
                documentRoutingService.createNewModelInstanceFromSquelette(
                    model,
                    racineModel,
                    ministereId,
                    bdcId,
                    cdmId,
                    scdmId,
                    cpmId,
                    session
                );
            }
        } else {
            LOG.info(session, SSLogEnumImpl.CREATE_FDR_TEC, "Aucun squelettes actifs.");
        }
        LOG.info(session, SSLogEnumImpl.CREATE_FDR_TEC, "Fin de la génération des modeles de feuille de route");
        return squelList.size();
    }

    private static final String FEUILLE_ROUTE_SQUELETTE_BY_TYPE_ACTE_QUERY =
        "SELECT DISTINCT r.ecm:uuid AS id FROM " +
        SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE +
        " AS r  WHERE r.ecm:parentId = ?  AND r.ecm:currentLifeCycleState != 'deleted'  AND r.ecm:currentLifeCycleState = ?  AND r.fdr:typeActe = ? ";

    @Override
    public boolean existsSqueletteWithTypeActe(CoreSession session, String typeActeId) {
        String[] paramList = new String[] {
            getFeuilleRouteSqueletteFolder(session).getId(),
            ElementLifeCycleState.validated.name(),
            typeActeId
        };
        List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE,
            FEUILLE_ROUTE_SQUELETTE_BY_TYPE_ACTE_QUERY,
            paramList
        );

        return !list.isEmpty();
    }

    private static final String FEUILLE_ROUTE_SQUELETTE_QUERY =
        "SELECT DISTINCT r.ecm:uuid AS id FROM " +
        SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE +
        " AS r  WHERE r.ecm:parentId = ?  AND r.ecm:currentLifeCycleState != 'deleted'  AND r.ecm:currentLifeCycleState = ?";

    @Override
    public List<DocumentModel> getAllValidatedSquelette(CoreSession session) {
        String[] paramList = new String[] {
            getFeuilleRouteSqueletteFolder(session).getId(),
            ElementLifeCycleState.validated.name()
        };
        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE,
            FEUILLE_ROUTE_SQUELETTE_QUERY,
            paramList
        );
    }

    @Override
    public String creeTitleModeleFDR(String idMinistere, String idDirection, String idTypeActe, String titreLibre) {
        final OrganigrammeService organigrammeService = STServiceLocator.getOrganigrammeService();

        final StringBuilder titleBuilder = new StringBuilder();

        if (idMinistere != null) {
            EntiteNode ministereNode = (EntiteNode) organigrammeService.getOrganigrammeNodeById(
                idMinistere,
                OrganigrammeType.MINISTERE
            );
            final String NORMinistere = ministereNode.getNorMinistere();

            titleBuilder.append(NORMinistere);
            titleBuilder.append(" - ");

            if (idDirection != null) {
                UniteStructurelleNode directionNode = (UniteStructurelleNode) organigrammeService.getOrganigrammeNodeById(
                    idDirection,
                    OrganigrammeType.DIRECTION
                );
                final String NORDirection = directionNode.getNorDirectionForMinistereId(idMinistere);

                titleBuilder.append(NORDirection);
                titleBuilder.append(" - ");
            }
        }

        if (idTypeActe != null && !idTypeActe.isEmpty()) {
            final SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
            final List<String> listId = new ArrayList<>();
            listId.add(idTypeActe);

            final List<VocabularyEntry> voc = vocService.getVocabularyEntryListFromDirectory(
                VocabularyConstants.TYPE_ACTE_VOCABULARY,
                VocabularyConstants.VOCABULARY_TYPE_ACTE_SCHEMA,
                listId
            );

            String typeActeLabel = null;

            final Iterator<VocabularyEntry> itActes = voc.iterator();

            while (itActes.hasNext() && typeActeLabel == null) {
                VocabularyEntry acte = itActes.next();
                if (idTypeActe.equals(acte.getId())) {
                    typeActeLabel = acte.getLabel();
                }
            }

            if (typeActeLabel != null) {
                titleBuilder.append(typeActeLabel);
                titleBuilder.append(" - ");
            } else {
                LOG.warn(
                    SSLogEnumImpl.CREATE_FDR_TEC,
                    "Non récupération du label de l'acte " +
                    idTypeActe +
                    "Lors de la construction du titre du modèle de feuille de route."
                );
            }
        }
        titleBuilder.append(titreLibre);

        return titleBuilder.toString();
    }

    @Override
    public boolean isModeleDefautUnique(CoreSession session, SolonEpgFeuilleRoute route) {
        if (route.isFeuilleRouteDefaut()) {
            EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
            criteria.setFeuilleRouteDefaut(true);
            if (StringUtils.isNotBlank(route.getMinistere())) {
                criteria.setMinistere(route.getMinistere());
            } else {
                criteria.setMinistereNull(true);
            }
            if (StringUtils.isNotBlank(route.getDirection())) {
                criteria.setDirection(route.getDirection());
            } else {
                criteria.setDirectionNull(true);
            }
            if (StringUtils.isNotBlank(route.getTypeActe())) {
                criteria.setTypeActe(route.getTypeActe());
            } else {
                criteria.setTypeActeNull(true);
            }
            EpgFeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);
            List<DocumentModel> feuilleRouteList = feuilleRouteDao.list(session);

            return (
                feuilleRouteList.isEmpty() ||
                (feuilleRouteList.size() == 1 && feuilleRouteList.get(0).getId().equals(route.getDocument().getId()))
            );
        }
        return true;
    }
}
