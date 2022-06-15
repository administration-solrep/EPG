package fr.dila.solonepg.core.cm.work;

import static fr.dila.solonepg.api.constant.VocabularyConstants.STATUT_ABANDONNE;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.wrap;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.StringHelper;
import fr.dila.st.core.work.SolonWork;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.apache.commons.collections4.ListUtils;
import org.nuxeo.ecm.core.work.api.Work;
import org.nuxeo.ecm.core.work.api.WorkManager;

public class TraitementDossiersFinScheduleWork extends SolonWork {
    private static final long serialVersionUID = -4820415116042140477L;

    /**
     *  
        Select count(1) 
        From 
            DOSSIER_SOLON_EPG dse
            LEFT JOIN ROUTING_TASK rt on rt.DOCUMENTROUTEID = dse.LASTDOCUMENTROUTE
            left join misc m on m.id = rt.id
        WHERE 
            dse.STATUT in ('4','5','6','7')
            and m.LIFECYCLESTATE = 'running';
     */
    private static final String QUERY_DOSSIERS_BY_STATUT =
        "Select d.ecm:uuid as id From Dossier as d, RouteStep as rs " +
        "WHERE d.dos:lastDocumentRoute = rs.rtsk:documentRouteId " +
        "AND d.dos:statut IN (%s) AND d.dos:deleted = 0 AND rs.ecm:currentLifeCycleState = 'running'";

    /**
     * 
        Select count(1) 
        From 
            DOSSIER_SOLON_EPG dse
            LEFT JOIN ROUTING_TASK rt on rt.DOCUMENTROUTEID = dse.LASTDOCUMENTROUTE
            left join misc m on m.id = rt.id
        WHERE 
            dse.STATUT in ('3')
            and m.LIFECYCLESTATE = 'running';
     */
    private static final String QUERY_LAST_DOC_ROUTE =
        "Select distinct d.dos:lastDocumentRoute as routeId From Dossier as d, RouteStep as rs " +
        "WHERE d.dos:lastDocumentRoute = rs.rtsk:documentRouteId " +
        "AND d.dos:statut IN (%s) AND d.dos:deleted = 0 AND rs.ecm:currentLifeCycleState = 'running'";

    private static final STLogger LOG = STLogFactory.getLog(TraitementDossiersFinScheduleWork.class);

    public static final String TRAITEMENT_DOSSIERS_FIN_CATEGORY = "TraitementDossiersFin";

    private static final int BUCKET_SIZE = 50;

    @Override
    public String getTitle() {
        return "TraitementDossiersFinWork";
    }

    @Override
    protected void doWork() {
        openSystemSession();

        List<String> statutsDossierFin = Arrays.asList(
            VocabularyConstants.STATUT_NOR_ATTRIBUE,
            VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION,
            VocabularyConstants.STATUT_CLOS,
            VocabularyConstants.STATUT_PUBLIE
        );
        String queryDossiersFin = String.format(
            QUERY_DOSSIERS_BY_STATUT,
            StringHelper.join(statutsDossierFin, ",", "'")
        );
        List<String> idsDossiersFin = QueryUtils.doUFNXQLQueryForIdsList(session, queryDossiersFin, null);
        if (!idsDossiersFin.isEmpty()) {
            LOG.info(STLogEnumImpl.DEFAULT, idsDossiersFin.size() + " dossiers au statut de fin à traiter");
            scheduleWorks(idsDossiersFin, TraitementDossiersTerminesWork::new);
        }

        List<String> idsFeuilleRoutes = getFeuilleRouteIdsForDossier(STATUT_ABANDONNE);
        if (!idsFeuilleRoutes.isEmpty()) {
            LOG.info(STLogEnumImpl.DEFAULT, idsFeuilleRoutes.size() + " dossiers au statut abandonné à traiter");
            scheduleWorks(idsFeuilleRoutes, TraitementDossiersAbandonnesWork::new);
        }
    }

    private List<String> getFeuilleRouteIdsForDossier(String dossierStatut) {
        String queryDossiersAbandonnes = format(QUERY_LAST_DOC_ROUTE, wrap(dossierStatut, "'"));
        return QueryUtils.doUFNXQLQueryAndMapping(
            session,
            queryDossiersAbandonnes,
            null,
            (Map<String, Serializable> rowData) -> (String) rowData.get("routeId")
        );
    }

    private void scheduleWorks(List<String> idsDossiersAbandonnes, Function<List<String>, ? extends Work> workMapper) {
        WorkManager workManager = ServiceUtil.getRequiredService(WorkManager.class);
        ListUtils.partition(idsDossiersAbandonnes, BUCKET_SIZE).stream().map(workMapper).forEach(workManager::schedule);
    }

    @Override
    public String getCategory() {
        return TRAITEMENT_DOSSIERS_FIN_CATEGORY;
    }
}
