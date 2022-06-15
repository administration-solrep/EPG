package fr.dila.solonepg.core.service;

import static java.util.Optional.ofNullable;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.criteria.EpgFeuilleRouteCriteria;
import fr.dila.solonepg.api.enums.FavorisRechercheType;
import fr.dila.solonepg.api.recherche.FavorisConsultation;
import fr.dila.solonepg.api.recherche.FavorisRecherche;
import fr.dila.solonepg.api.recherche.ResultatConsulte;
import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringHelper;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteElement.ElementLifeCycleState;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.FlexibleQueryMaker;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.platform.userworkspace.api.UserWorkspaceService;

/**
 * Implementation service pour les resultats consultes
 *
 * @author asatre
 */
public class EspaceRechercheServiceImpl implements EspaceRechercheService {
    private static final Log LOG = LogFactory.getLog(EspaceRechercheServiceImpl.class);
    private static final long serialVersionUID = 4028504311494664171L;

    /**
     * Contenu du paramètre d'activation de la page de recherche libre.
     */
    private static String activationRechercheLibre;

    /**
     * PARAM : creator
     */
    private static final String UFNXQL_QUERY_RESULTAT_CONSULTE =
        "SELECT rc.ecm:uuid as id FROM ResultatConsulte as rc" + " WHERE rc.dc:creator = ? ";

    @Override
    public DocumentModel createBareFavorisRecherche(final CoreSession session, final FavorisRechercheType type) {
        final DocumentModel favoriRechercheDoc = session.createDocumentModel(
            SolonEpgConstant.FAVORIS_RECHERCHE_DOCUMENT_TYPE
        );
        final FavorisRecherche favorisRecherche = favoriRechercheDoc.getAdapter(FavorisRecherche.class);

        favorisRecherche.setType(type);

        return favoriRechercheDoc;
    }

    @Override
    public void addDocumentModelToResultatsConsultes(
        final CoreSession session,
        final String userworkspacePath,
        final String documentIdToAdd,
        final DocKind kind
    ) {
        if (kind == null || documentIdToAdd == null) {
            return;
        }

        final ResultatConsulte resultatConsulte = getResultatConsulte(session, userworkspacePath);
        final int nbDerniersResultatsConsultes = getNbDerniersResultatsConsultes(session);

        switch (kind) {
            case DOSSIER:
                resultatConsulte.addNewDossier(documentIdToAdd, nbDerniersResultatsConsultes);
                break;
            case FEUILLE_ROUTE:
                resultatConsulte.addNewFdr(documentIdToAdd, nbDerniersResultatsConsultes);
                break;
            case USER:
                resultatConsulte.addNewUser(documentIdToAdd, nbDerniersResultatsConsultes);
                break;
            default:
                LOG.warn("kind not supported : " + kind);
                break;
        }

        resultatConsulte.save(session);
        session.save();
    }

    /**
     * Recherche le document résultat consultés de l'utilisateur. Le crée si besoin
     *
     * @param session           Session
     * @param userworkspacePath Workspace de l'utilisateur
     * @return Document résultat consultés de l'utilisateur
     */
    @Override
    public ResultatConsulte getResultatConsulte(final CoreSession session, final String userworkspacePath) {
        final List<DocumentModel> resultatConsulteDocList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            "ResultatConsulte",
            UFNXQL_QUERY_RESULTAT_CONSULTE,
            new Object[] { session.getPrincipal().getName() }
        );

        DocumentModel resultatConsulte = null;
        if (resultatConsulteDocList == null || resultatConsulteDocList.isEmpty()) {
            resultatConsulte =
                session.createDocumentModel(SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_DOCUMENT_TYPE);
            resultatConsulte.setPathInfo(
                userworkspacePath + SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_PATH,
                SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_TITLE
            );
            resultatConsulte = session.createDocument(resultatConsulte);
        } else {
            resultatConsulte = resultatConsulteDocList.get(0);
        }
        return resultatConsulte.getAdapter(ResultatConsulte.class);
    }

    private int getNbDerniersResultatsConsultes(final CoreSession session) {
        final ParametrageApplication param = getParametrageApplication(session);
        final int nbDerniersResultatsConsultes = param != null && param.getNbDerniersResultatsConsultes() != null
            ? param.getNbDerniersResultatsConsultes().intValue()
            : 30;
        return nbDerniersResultatsConsultes;
    }

    @Override
    public int addToFavorisConsultation(
        final CoreSession session,
        final String userworkspacePath,
        final List<DocumentModel> documentToAddList
    ) {
        if (documentToAddList == null || documentToAddList.isEmpty()) {
            return 0;
        }

        final DocumentModel favorisConsultationDoc = getFavorisConsultation(userworkspacePath, session);
        final FavorisConsultation favorisConsultation = favorisConsultationDoc.getAdapter(FavorisConsultation.class);
        final DocumentModel documentModel = documentToAddList.get(0);
        List<String> currentDocumentIdList = null;
        List<String> newDocumentIdList = null;
        if (documentModel.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
            currentDocumentIdList = favorisConsultation.getDossiersId();
            newDocumentIdList =
                addDocumentToFavorisConsultation(session, currentDocumentIdList, documentToAddList, false);
            favorisConsultation.setDossiersId(newDocumentIdList);
        } else if (documentModel.hasSchema(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA)) {
            currentDocumentIdList = favorisConsultation.getFdrsId();
            newDocumentIdList =
                addDocumentToFavorisConsultation(session, currentDocumentIdList, documentToAddList, false);
            favorisConsultation.setFdrsId(newDocumentIdList);
        } else if (documentModel.hasSchema(STSchemaConstant.USER_SCHEMA)) {
            currentDocumentIdList = favorisConsultation.getUsers();
            newDocumentIdList =
                addDocumentToFavorisConsultation(session, currentDocumentIdList, documentToAddList, true);
            favorisConsultation.setUsers(newDocumentIdList);
        } else {
            throw new NuxeoException("Type de document inconnu : " + documentModel.getType());
        }
        favorisConsultation.save(session);
        session.saveDocument(favorisConsultation.getDocument());
        return newDocumentIdList.size() - currentDocumentIdList.size();
    }

    @Override
    public DocumentModel getFavorisConsultation(final String userworkspacePath, final CoreSession session) {
        final StringBuilder builder = new StringBuilder();
        builder.append(" SELECT fv.ecm:uuid as id FROM FavorisConsultation as fv");
        builder.append(" WHERE fv.dc:creator = ? ");

        final Object params[] = new Object[] { session.getPrincipal().getName() };

        final List<DocumentModel> docList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            builder.toString(),
            params
        );

        DocumentModel doc = null;
        if (docList == null || docList.isEmpty()) {
            doc = session.createDocumentModel(SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_DOCUMENT_TYPE);
            doc.setPathInfo(
                userworkspacePath + SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_PATH,
                SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_TITLE
            );
            doc = session.createDocument(doc);
        } else {
            doc = docList.get(0);
        }
        return doc;
    }

    /**
     * Ajoute un ensemble de documents aux favoris de consultation.
     *
     * @param session               Session
     * @param currentDocumentIdList Liste des UUID des favoris de consultation actuels
     * @param documentModelList     Liste des documents à ajouter aux favoris de consultation
     * @return Liste des UUID des favoris de consultation après modification
     */
    private List<String> addDocumentToFavorisConsultation(
        final CoreSession session,
        List<String> currentDocumentIdList,
        final List<DocumentModel> documentModelList,
        boolean checkUser
    ) {
        final ParametrageApplication param = getParametrageApplication(session);

        if (currentDocumentIdList == null) {
            currentDocumentIdList = new ArrayList<>();
        }

        final int nbFavorisConsultation = param != null && param.getNbFavorisConsultation() != null
            ? param.getNbFavorisConsultation().intValue()
            : 30;

        if (currentDocumentIdList.size() >= nbFavorisConsultation) {
            return currentDocumentIdList;
        }

        final Set<String> actualSet = new LinkedHashSet<>(currentDocumentIdList);
        for (final DocumentModel documentModel : documentModelList) {
            if (checkUser) {
                STUser user = documentModel.getAdapter(STUser.class);

                List<String> userPostes = user.getPostes();
                if (userPostes == null || userPostes.isEmpty()) {
                    continue;
                }
            }

            actualSet.add(documentModel.getId());
            if (actualSet.size() == nbFavorisConsultation) {
                break;
            }
        }
        return new ArrayList<>(actualSet);
    }

    private ParametrageApplication getParametrageApplication(final CoreSession session) {
        final ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
        return parametreApplicationService.getParametreApplicationDocument(session);
    }

    @Override
    public void removeFromFavorisConsultation(
        final CoreSession session,
        final String userworkspacePath,
        final List<DocumentModel> documentToRemoveList
    ) {
        if (documentToRemoveList == null || documentToRemoveList.isEmpty()) {
            return;
        }

        final DocumentModel favorisConsultationDoc = getFavorisConsultation(userworkspacePath, session);
        final FavorisConsultation favorisConsultation = favorisConsultationDoc.getAdapter(FavorisConsultation.class);
        final DocumentModel documenToRemove = documentToRemoveList.get(0);
        if (documenToRemove.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
            final List<String> currentDocumentIdList = favorisConsultation.getDossiersId();
            final List<String> newDocumentIdList = removeDocumentFromFavorisConsultation(
                currentDocumentIdList,
                documentToRemoveList
            );
            favorisConsultation.setDossiersId(newDocumentIdList);
        } else if (documenToRemove.hasSchema(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA)) {
            final List<String> currentDocumentIdList = favorisConsultation.getFdrsId();
            final List<String> newDocumentIdList = removeDocumentFromFavorisConsultation(
                currentDocumentIdList,
                documentToRemoveList
            );
            favorisConsultation.setFdrsId(newDocumentIdList);
        } else if (documenToRemove.hasSchema(STSchemaConstant.USER_SCHEMA)) {
            final List<String> currentDocumentIdList = favorisConsultation.getUsers();
            final List<String> newDocumentIdList = removeDocumentFromFavorisConsultation(
                currentDocumentIdList,
                documentToRemoveList
            );
            favorisConsultation.setUsers(newDocumentIdList);
        }
        favorisConsultation.save(session);
        session.saveDocument(favorisConsultationDoc);
        session.save();
    }

    /**
     * Retire une liste de documents des favoris de consultation.
     *
     * @param currentDocumentIdList Identifiant technique des documents présents dans le favori de consultation
     * @param documentToRemoveList  Documents à retirer de la liste
     * @return Liste des identifiants techniques des favoris de consultation après le traitement
     */
    private List<String> removeDocumentFromFavorisConsultation(
        List<String> currentDocumentIdList,
        final List<DocumentModel> documentToRemoveList
    ) {
        if (currentDocumentIdList == null) {
            currentDocumentIdList = new ArrayList<>();
        }

        final Set<String> set = new LinkedHashSet<>(currentDocumentIdList);
        for (final DocumentModel documentToRemove : documentToRemoveList) {
            // Tente d'enlever le document
            final String documentToRemoveId = documentToRemove.getId();
            set.remove(documentToRemoveId);
        }

        return new ArrayList<>(set);
    }

    @Override
    public List<DocumentModel> addToFavorisRecherche(
        final CoreSession session,
        final List<String> postes,
        final String intitule,
        final String query,
        final FavorisRechercheType type
    ) {
        final DocumentModel favorisRechercheDoc = session.createDocumentModel(
            SolonEpgConstant.FAVORIS_RECHERCHE_DOCUMENT_TYPE
        );
        final FavorisRecherche favorisRecherche = favorisRechercheDoc.getAdapter(FavorisRecherche.class);
        favorisRecherche.setTitle(intitule);
        favorisRecherche.setQueryPart(query);
        favorisRecherche.setType(type);

        return addToFavorisRecherche(session, postes, favorisRechercheDoc);
    }

    @Override
    public List<DocumentModel> addToFavorisRecherche(
        final CoreSession session,
        final List<String> postes,
        final DocumentModel favorisRechercheDoc
    ) {
        final List<DocumentModel> listUserError = new ArrayList<>();
        final Set<String> userDocSet = new HashSet<>();
        userDocSet.add(session.getPrincipal().getName());

        // en unrestricted pour avoir les droits de creation des documents dans les UserWorkspaces
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                final UserWorkspaceService userWorkspaceService = ServiceUtil.getRequiredService(
                    UserWorkspaceService.class
                );
                final UserManager userManager = STServiceLocator.getUserManager();

                if (postes != null && !postes.isEmpty()) {
                    final List<PosteNode> listNode = STServiceLocator.getSTPostesService().getPostesNodes(postes);

                    for (final PosteNode posteNode : listNode) {
                        userDocSet.addAll(posteNode.getMembers());
                    }
                }
                // on ajoute le user courant

                final ParametrageApplication param = getParametrageApplication(session);
                final Long nbFavorisRecherche = param != null ? param.getNbFavorisRecherche() : 30L;

                final StringBuilder domainQuery = new StringBuilder();
                domainQuery.append(" SELECT d.ecm:uuid as id FROM Domain as d ");
                domainQuery.append(" where d.dc:title = ? ");

                final Object params[] = new Object[] { "Case Management" };

                IterableQueryResult docList = null;
                DocumentModel domainModel = null;
                try {
                    docList = QueryUtils.doUFNXQLQuery(session, domainQuery.toString(), params);
                    final Iterator<Map<String, Serializable>> iterator = docList.iterator();
                    while (iterator.hasNext()) {
                        final Map<String, Serializable> row = iterator.next();
                        domainModel = session.getDocument(new IdRef((String) row.get("id")));
                        break;
                    }
                } finally {
                    if (docList != null) {
                        docList.close();
                    }
                }

                DocumentModel favoriRechercheUserDoc = null;
                final FavorisRecherche favorisRecherche = favorisRechercheDoc.getAdapter(FavorisRecherche.class);
                for (final String userId : userDocSet) {
                    final Long userNbFavorisRecherche = countFavorisRecherche(userId, session);

                    final DocumentModel user = userManager.getUserModel(userId);
                    if (userNbFavorisRecherche >= nbFavorisRecherche) {
                        listUserError.add(user);
                    } else {
                        final DocumentModel userWorkspace = userWorkspaceService.getCurrentUserPersonalWorkspace(
                            userId,
                            domainModel
                        );
                        final String favorisRecherchePath =
                            userWorkspace.getPathAsString() + SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_PATH;
                        if (favoriRechercheUserDoc == null) {
                            favorisRechercheDoc.setPathInfo(favorisRecherchePath, favorisRecherche.getTitle());
                            favoriRechercheUserDoc = session.createDocument(favorisRechercheDoc);
                        } else {
                            session.copy(
                                favoriRechercheUserDoc.getRef(),
                                new PathRef(favorisRecherchePath),
                                favorisRecherche.getTitle()
                            );
                        }
                    }
                }
            }
        }
        .runUnrestricted();

        return listUserError;
    }

    private Long countFavorisRecherche(final String idUser, final CoreSession session) {
        final StringBuilder nxQuery = new StringBuilder();
        nxQuery.append(" SELECT fr.ecm:uuid as id FROM FavorisRecherche as fr ");
        nxQuery.append(" WHERE isChildOf(fr.ecm:uuid, ");
        nxQuery.append(" select w.ecm:uuid from Workspace AS w where w.dc:title ='");
        nxQuery.append(idUser);
        nxQuery.append("' ) = 1 ");

        return QueryUtils.doCountQuery(session, FlexibleQueryMaker.KeyCode.UFXNQL.getKey() + nxQuery.toString());
    }

    @Override
    public void removeAllDossierFromFavorisConsultation(final CoreSession session, final String userworkspacePath) {
        final DocumentModel docRC = getFavorisConsultation(userworkspacePath, session);
        final FavorisConsultation favorisConsultation = docRC.getAdapter(FavorisConsultation.class);
        final List<String> dossiers = new LinkedList<>(favorisConsultation.getDossiersId());

        if (!dossiers.isEmpty()) {
            final StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT d.ecm:uuid as id FROM Dossier as d WHERE d.ecm:uuid IN (");
            queryBuilder.append(StringHelper.join(dossiers, ",", "'"));
            queryBuilder.append(") AND (testDossierAcl(d.ecm:uuid)=1) ");

            final List<String> list = QueryUtils.doQueryForIds(
                session,
                FlexibleQueryMaker.KeyCode.UFXNQL.getKey() + queryBuilder.toString()
            );

            favorisConsultation.setDossiersId(list);
            favorisConsultation.save(session);
        }
    }

    @Override
    public void removeAllDossierFromDerniersResultatsConsultes(
        final CoreSession session,
        final String userworkspacePath
    ) {
        final ResultatConsulte resultatConsulte = getResultatConsulte(session, userworkspacePath);
        final List<String> dossiers = new LinkedList<>(resultatConsulte.getDossiersId());

        if (!dossiers.isEmpty()) {
            final StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT d.ecm:uuid as id FROM Dossier as d WHERE d.ecm:uuid IN (");
            queryBuilder.append(StringHelper.join(dossiers, ",", "'"));
            queryBuilder.append(") AND (testDossierAcl(d.ecm:uuid)=1) ");

            final List<String> foundList = QueryUtils.doQueryForIds(
                session,
                FlexibleQueryMaker.KeyCode.UFXNQL.getKey() + queryBuilder.toString()
            );

            // pour garder l'ordre...
            final List<String> dossiersToRemove = new ArrayList<>();
            for (final String idDossier : dossiers) {
                if (!foundList.contains(idDossier)) {
                    dossiersToRemove.add(idDossier);
                }
            }
            resultatConsulte.removeDossiers(dossiersToRemove);
            resultatConsulte.save(session);
        }
    }

    @Override
    public void removeModeleFromDerniersResultatsConsultes(
        final CoreSession coreSession,
        final String userworkspacePath,
        final Set<String> modeleToRemove
    ) {
        final ResultatConsulte resultatConsulte = getResultatConsulte(coreSession, userworkspacePath);
        resultatConsulte.removeFdrs(modeleToRemove);
        resultatConsulte.save(coreSession);
    }

    @Override
    public void removeModeleFromFavorisConsultation(
        final CoreSession coreSession,
        final String userworkspacePath,
        final Set<String> modeleToRemove
    ) {
        removeFromFavorisConsultation(
            coreSession,
            userworkspacePath,
            modeleToRemove,
            FavorisConsultation::getFdrsId,
            FavorisConsultation::setFdrsId
        );
    }

    @Override
    public void removeDossierFromDerniersResultatsConsultes(
        final CoreSession coreSession,
        final String userworkspacePath,
        final Set<String> dossiersToRemove
    ) {
        final ResultatConsulte resultatConsulte = getResultatConsulte(coreSession, userworkspacePath);
        resultatConsulte.removeDossiers(dossiersToRemove);
        resultatConsulte.save(coreSession);
    }

    @Override
    public void removeDossierFromFavorisConsultation(
        final CoreSession coreSession,
        final String userworkspacePath,
        final Set<String> dossierToRemove
    ) {
        removeFromFavorisConsultation(
            coreSession,
            userworkspacePath,
            dossierToRemove,
            FavorisConsultation::getDossiersId,
            FavorisConsultation::setDossiersId
        );
    }

    @Override
    public void removeUserFromDerniersResultatsConsultes(
        final CoreSession coreSession,
        final String userworkspacePath,
        final Set<String> userToRemove
    ) {
        final ResultatConsulte resultatConsulte = getResultatConsulte(coreSession, userworkspacePath);
        resultatConsulte.removeUsers(userToRemove);
        resultatConsulte.save(coreSession);
    }

    @Override
    public void removeUserFromFavorisConsultations(
        final CoreSession coreSession,
        final String userworkspacePath,
        final Set<String> userToRemove
    ) {
        removeFromFavorisConsultation(
            coreSession,
            userworkspacePath,
            userToRemove,
            FavorisConsultation::getUsers,
            FavorisConsultation::setUsers
        );
    }

    private void removeFromFavorisConsultation(
        final CoreSession coreSession,
        final String userworkspacePath,
        final Set<String> idDocToRemove,
        Function<FavorisConsultation, List<String>> idGetter,
        BiConsumer<FavorisConsultation, List<String>> idSetter
    ) {
        final DocumentModel docRC = getFavorisConsultation(userworkspacePath, coreSession);
        final FavorisConsultation favorisConsultation = docRC.getAdapter(FavorisConsultation.class);
        final List<String> idDocFavoris = idGetter.apply(favorisConsultation);
        for (final String idDoc : idDocToRemove) {
            idDocFavoris.remove(idDoc);
        }
        idSetter.accept(favorisConsultation, idDocFavoris);
        favorisConsultation.save(coreSession);
    }

    @Override
    public EpgFeuilleRouteCriteria getFeuilleRouteCriteria(final DocumentModel favorisRechercheDoc) {
        final FavorisRecherche favorisRecherche = favorisRechercheDoc.getAdapter(FavorisRecherche.class);

        final EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        criteria.setCheckReadPermission(true);
        criteria.setIntitule(favorisRecherche.getFeuilleRouteTitle());
        criteria.setNumero(favorisRecherche.getFeuilleRouteNumero());
        criteria.setTypeActe(favorisRecherche.getFeuilleRouteTypeActe());
        criteria.setMinistere(favorisRecherche.getFeuilleRouteMinistere());
        criteria.setDirection(favorisRecherche.getFeuilleRouteDirection());
        criteria.setCreationUtilisateur(favorisRecherche.getFeuilleRouteCreationUtilisateur());
        ofNullable(favorisRecherche.getFeuilleRouteCreationDateMin())
            .map(Calendar::getTimeInMillis)
            .map(Date::new)
            .ifPresent(criteria::setCreationDateMin);
        ofNullable(favorisRecherche.getFeuilleRouteCreationDateMax())
            .map(Calendar::getTimeInMillis)
            .map(Date::new)
            .ifPresent(criteria::setCreationDateMax);
        if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteValidee())) {
            if (VocabularyConstants.BOOLEAN_TRUE.equals(favorisRecherche.getFeuilleRouteValidee())) {
                criteria.setCurrentLifeCycleState(ElementLifeCycleState.validated.name());
            } else {
                criteria.setCurrentLifeCycleState(ElementLifeCycleState.draft.name());
            }
        }
        criteria.setRoutingTaskType(favorisRecherche.getRouteStepRoutingTaskType());
        criteria.setDistributionMailboxId(favorisRecherche.getRouteStepDistributionMailboxId());
        criteria.setDeadline(favorisRecherche.getRouteStepEcheanceIndicative());
        if (StringUtils.isNotBlank(favorisRecherche.getRouteStepAutomaticValidation())) {
            if (VocabularyConstants.BOOLEAN_TRUE.equals(favorisRecherche.getRouteStepAutomaticValidation())) {
                criteria.setAutomaticValidation(true);
            } else {
                criteria.setAutomaticValidation(false);
            }
        }
        if (StringUtils.isNotBlank(favorisRecherche.getRouteStepObligatoireSgg())) {
            if (VocabularyConstants.BOOLEAN_TRUE.equals(favorisRecherche.getRouteStepObligatoireSgg())) {
                criteria.setObligatoireSGG(true);
            } else {
                criteria.setObligatoireSGG(false);
            }
        }
        if (StringUtils.isNotBlank(favorisRecherche.getRouteStepObligatoireMinistere())) {
            if (VocabularyConstants.BOOLEAN_TRUE.equals(favorisRecherche.getRouteStepObligatoireMinistere())) {
                criteria.setObligatoireMinistere(true);
            } else {
                criteria.setObligatoireMinistere(false);
            }
        }

        return criteria;
    }

    @Override
    public boolean canUseRechercheLibre(CoreSession session) {
        if (activationRechercheLibre == null) {
            synchronized (this) {
                ConfigService configService = STServiceLocator.getConfigService();
                activationRechercheLibre =
                    configService.getValue(SolonEpgConfigConstant.SOLONEPG_ELASTICSEARCH_ACTIVATION);
            }
        }

        if ("off".equals(activationRechercheLibre)) {
            return false;
        }

        if ("on".equals(activationRechercheLibre)) {
            return true;
        }

        // Le paramètre contient la liste des logins d'utilisateurs utilisés à
        // accéder à la recherche libre
        List<String> logins = Arrays.asList(activationRechercheLibre.split(","));
        String currentLogin = session.getPrincipal().getName();

        return StringHelper.containsIgnoreCase(logins, currentLogin);
    }
}
