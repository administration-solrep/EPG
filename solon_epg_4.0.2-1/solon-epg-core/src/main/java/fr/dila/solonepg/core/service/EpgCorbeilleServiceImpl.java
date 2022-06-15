package fr.dila.solonepg.core.service;

import static java.util.Objects.requireNonNull;

import fr.dila.cm.caselink.CaseLinkConstants;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STDossierLinkConstant;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.service.STCorbeilleServiceImpl;
import fr.dila.st.core.util.StringHelper;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.schema.PrefetchInfo;

/**
 * Implémentation du service Corbeille de l'application SOLON EPG.
 *
 * @author jtremeaux
 */
public class EpgCorbeilleServiceImpl extends STCorbeilleServiceImpl implements EpgCorbeilleService {
    /**
     * UID.
     */
    private static final long serialVersionUID = -2392698015083550568L;

    private static final STLogger LOGGER = STLogFactory.getLog(EpgCorbeilleServiceImpl.class);

    /**
     * query : SELECT id FROM DossierLink WHERE caseDocumentId = ? AND deleted = 0 AND caseLifeCycleState = 'running' AND routingTaskType = ?
     */
    private static final String DOSSIER_LINK_CE_RUNNING_QUERY =
        "SELECT dl.ecm:uuid as id FROM " +
        STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE +
        " as dl WHERE dl.acslk:caseDocumentId = ? AND dl." +
        DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DELETED +
        " = 0 AND dl." +
        DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_LINK_CASE_LIFE_CYCLE_STATE_PROPERTY +
        " = 'running' AND dl." +
        DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX +
        ":" +
        STDossierLinkConstant.DOSSIER_LINK_ROUTING_TASK_TYPE_PROPERTY +
        " = ?";

    @Override
    public List<DossierLink> findDossierLinks(final CoreSession session, final List<String> dossierIds) {
        final StringBuilder query = new StringBuilder("SELECT dl.ecm:uuid as id FROM ");
        query.append(STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE);
        query.append(" as dl WHERE dl.acslk:caseDocumentId IN (");
        query.append(StringHelper.getQuestionMark(dossierIds.size()));
        query.append(") AND dl.");
        query.append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX);
        query.append(":");
        query.append(DossierSolonEpgConstants.DELETED);
        query.append(" = 0 ");
        query.append("AND testDossierAcl(dl.ecm:uuid) = 1");

        final List<String> params = new ArrayList<>();
        params.addAll(dossierIds);

        final List<DocumentModel> listDocs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE,
            query.toString(),
            params.toArray()
        );

        final List<DossierLink> result = new ArrayList<>();

        for (final DocumentModel documentModel : listDocs) {
            result.add(documentModel.getAdapter(DossierLink.class));
        }

        return result;
    }

    @Override
    public List<DocumentModel> findDossierLink(final CoreSession session, final String dossierId) {
        final StringBuilder query = new StringBuilder("SELECT DISTINCT dl.ecm:uuid as id FROM ")
            .append(STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE)
            .append(" as dl WHERE dl.acslk:caseDocumentId = ? AND dl.")
            .append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX)
            .append(":")
            .append(DossierSolonEpgConstants.DELETED)
            .append(" = 0 ")
            .append("AND testDossierAcl(dl.ecm:uuid) = 1");

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE,
            query.toString(),
            new Object[] { dossierId }
        );
    }

    @Override
    public boolean existsPourAvisCEStep(final CoreSession session, final String dossierId) {
        DocumentModel dossierLinkDoc = getPourAvisCeDossierLinkDoc(session, dossierId);
        return dossierLinkDoc != null;
    }

    @Override
    public DocumentModel getPourAvisCeDossierLinkDoc(final CoreSession session, final String dossierId) {
        Object[] params = new Object[] { dossierId, VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE };
        List<DocumentModel> dossiersLinksDocs = QueryUtils.doUnrestrictedUFNXQLQueryAndFetchForDocuments(
            session,
            DOSSIER_LINK_CE_RUNNING_QUERY,
            params,
            1,
            0
        );
        if (dossiersLinksDocs != null && !dossiersLinksDocs.isEmpty()) {
            return dossiersLinksDocs.get(0);
        }
        return null;
    }

    @Override
    public Calendar getStartDateForRunningStep(
        final CoreSession session,
        final String dossierId,
        final String stepType
    ) {
        final Calendar dateDebutEtapePourAvisCE = Calendar.getInstance();
        try {
            new UnrestrictedSessionRunner(session) {

                @Override
                public void run() {
                    DocumentModel dossierLinkDoc = null;
                    try {
                        final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
                        dossierLinkDoc = corbeilleService.getPourAvisCeDossierLinkDoc(session, dossierId);
                    } catch (final NuxeoException e) {
                        LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC, dossierId, e);
                    }
                    if (dossierLinkDoc != null) {
                        final DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
                        dateDebutEtapePourAvisCE.setTime(dossierLink.getDateCreation().getTime());
                    }
                }
            }
            .runUnrestricted();
        } catch (NuxeoException ce) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC, dossierId, ce);
        }
        return dateDebutEtapePourAvisCE;
    }

    @Override
    public List<DocumentModel> findDossierLink(
        final CoreSession session,
        final Collection<String> dossierIds,
        PrefetchInfo prefetch
    ) {
        if (dossierIds == null || dossierIds.isEmpty()) {
            return Collections.emptyList();
        }
        // pas de test sur les droits, les documents non visible par l'utilisateur ne seront pas chargés
        final StringBuilder query = new StringBuilder(" SELECT dl.ecm:uuid as id FROM ");
        query.append(STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE);
        query.append(" as dl WHERE dl.acslk:caseDocumentId IN (");
        query.append(StringHelper.getQuestionMark(dossierIds.size()));
        query.append(") AND dl.");
        query.append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX);
        query.append(":");
        query.append(DossierSolonEpgConstants.DELETED);
        query.append(" = 0 ");

        return QueryHelper.doUFNXQLQueryAndFetchForDocuments(
            session,
            query.toString(),
            dossierIds.toArray(),
            0,
            0,
            prefetch
        );
    }

    @Override
    public String getInfocentreQuery(
        final CoreSession session,
        final STDossier.DossierState dossierState,
        final Set<String> mailboxIds
    ) {
        final StringBuilder nxQuery = new StringBuilder();

        switch (dossierState) {
            case init:
                nxQuery.append(" SELECT d.ecm:uuid as id FROM Dossier as d WHERE d.dos:");
                nxQuery.append(DossierSolonEpgConstants.DOSSIER_POSTE_CREATOR_PROPERTY);
                nxQuery.append(" IN ('");
                nxQuery.append(StringUtils.join(mailboxIds, "','"));
                nxQuery.append("') ");

                nxQuery.append(" AND testDossierAcl(d.ecm:uuid) = 1 ");

                lanceOuInitie(nxQuery);

                break;
            case done:
                nxQuery.append(" SELECT d.ecm:uuid as id FROM Dossier as d, RouteStep as rs ");
                nxQuery.append(" WHERE d.dos:lastDocumentRoute = rs.rtsk:documentRouteId");
                nxQuery.append(" AND rs.ecm:currentLifeCycleState = '");
                nxQuery.append(STDossier.DossierState.done.name());
                nxQuery.append("'");
                nxQuery.append(" AND rs.rtsk:distributionMailboxId IN ('");
                nxQuery.append(StringUtils.join(mailboxIds, "','"));
                nxQuery.append("')");

                nxQuery.append(" AND testDossierAcl(d.ecm:uuid) = 1 ");

                lanceOuInitie(nxQuery);

                break;
            case running:
                nxQuery.append(" SELECT d.ecm:uuid as id FROM RouteStep as rs, Dossier as d ");
                nxQuery.append(" WHERE d.dos:lastDocumentRoute = rs.rtsk:documentRouteId ");
                nxQuery.append(" AND rs.ecm:currentLifeCycleState = '");
                nxQuery.append(dossierState.name());
                nxQuery.append("' ");
                nxQuery.append(" AND rs.rtsk:distributionMailboxId IN ('");
                nxQuery.append(StringUtils.join(mailboxIds, "','"));
                nxQuery.append("')");

                nxQuery.append(" AND testDossierAcl(d.ecm:uuid) = 1 ");

                lanceOuInitie(nxQuery);

                break;
            default:
                break;
        }

        return nxQuery.toString();
    }

    private void lanceOuInitie(final StringBuilder nxQuery) {
        // récupérer uniquement les dossier initié et lancé
        nxQuery.append(" AND d." + DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
        nxQuery.append(":");
        nxQuery.append(DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY);
        nxQuery.append(" IN ('");
        nxQuery.append(VocabularyConstants.STATUT_INITIE);
        nxQuery.append("','");
        nxQuery.append(VocabularyConstants.STATUT_LANCE);
        nxQuery.append("')");
    }

    @Override
    public DocumentModel getCaseLinkPourInitialisation(final CoreSession session, final DocumentModel dossierDoc) {
        // FEV 509 - On affiche toujours le dossier qu'on vient de créer, même si l'utilisateur crée un dosiser pour une
        // autre entité
        final StringBuilder queryBuilder = new StringBuilder("SELECT dl.ecm:uuid as id FROM DossierLink as dl WHERE ");
        queryBuilder.append(" dl.acslk:caseDocumentId = ? ");
        queryBuilder.append(" AND dl.acslk:routingTaskType = ? ");
        queryBuilder.append(" AND dl.");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(DossierSolonEpgConstants.DELETED);
        queryBuilder.append(" = 0 ");

        final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            "DossierLink",
            queryBuilder.toString(),
            new Object[] { dossierDoc.getId(), VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION },
            1,
            0
        );

        if (list.size() != 1) {
            return null;
        }

        return list.get(0);
    }

    @Override
    protected List<DocumentModel> queryDocs(
        final CoreSession session,
        final String query,
        final Collection<String> paramList
    ) {
        PrefetchInfo prefetchInfo = new PrefetchInfo(
            String.join(
                ",",
                DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA,
                DossierSolonEpgConstants.DOSSIER_SCHEMA,
                CaseLinkConstants.CASE_LINK_SCHEMA
            )
        );
        return QueryHelper.doUnrestrictedUFNXQLQueryAndFetchForDocuments(
            session,
            query,
            paramList.toArray(new String[paramList.size()]),
            0,
            0,
            prefetchInfo
        );
    }

    @Override
    public List<DocumentModel> findDossierLinkFromPoste(final CoreSession session, final Collection<String> postesId) {
        requireNonNull(postesId);
        Collection<String> mailboxs = getMailboxPosteIds(session, postesId);

        final StringBuilder stringBuilder = getDossierLinksQuery(mailboxs);

        return queryDocs(session, stringBuilder.toString(), mailboxs);
    }

    protected Collection<String> getMailboxPosteIds(CoreSession session, final Collection<String> postesId) {
        MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
        return mailboxPosteService.getMailboxPosteDocIds(session, postesId);
    }

    private StringBuilder getDossierLinksQuery(Collection<String> mailboxs) {
        final StringBuilder stringBuilder = new StringBuilder("SELECT dl.ecm:uuid as id FROM ");
        stringBuilder.append(STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE);
        stringBuilder.append(" AS dl, ");
        stringBuilder.append(STConstant.DOSSIER_DOCUMENT_TYPE);
        stringBuilder.append(" AS d WHERE d.ecm:uuid = dl.acslk:caseDocumentId");
        stringBuilder.append(" AND dl.acslk:caseLifeCycleState IN ('init', 'running') ");
        stringBuilder.append(" AND dl.acslk:deleted = 0 AND dl.ecm:parentId IN (");
        stringBuilder.append(StringHelper.getQuestionMark(mailboxs.size())).append(") AND dl.");
        stringBuilder.append(LIFECYCLE_EQUAL_TODO);
        stringBuilder.append(" AND dl.acslk:deleted = 0");
        stringBuilder.append(" AND dl.acslk:archive = 0");

        stringBuilder.append(" AND testDossierAcl(dl.ecm:uuid) = 1 ");

        lanceOuInitie(stringBuilder);

        return stringBuilder;
    }
}
