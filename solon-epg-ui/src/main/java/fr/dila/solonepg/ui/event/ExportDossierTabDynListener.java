package fr.dila.solonepg.ui.event;

import com.google.gson.Gson;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.enums.FavorisRechercheType;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.recherche.TableauDynamique;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.search.AbstractCriteria;
import fr.dila.solonepg.elastic.models.search.RechercheLibre;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaExp;
import fr.dila.solonepg.elastic.services.RequeteurService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

public class ExportDossierTabDynListener extends AbstractExportDossierRechercheListener {
    private static final String UNKNOWN_SEARCH_TYPE_ERROR_MESSAGE = "Le type de recherche [%s] n'existe pas";

    public ExportDossierTabDynListener() {
        super(SolonEpgEventConstant.DOSSIERS_TAB_DYN_EXPORT_EVENT);
    }

    @Override
    protected long countItemsToExport(CoreSession session, Map<String, Serializable> eventProperties) throws Exception {
        TableauDynamique tabDyn = getTableauDynamique(session, eventProperties);

        FavorisRechercheType type = tabDyn.getType();

        if (type == FavorisRechercheType.ES_LIBRE) {
            RechercheLibre rechercheLibre = getSearchCriteria(tabDyn, 0);

            RequeteurService requeteurService = EpgUIServiceLocator.getRequeteurService();
            return requeteurService.getCountResults(rechercheLibre, session);
        } else if (type == FavorisRechercheType.ES_EXPERTE) {
            SearchCriteriaExp searchCriteriaExp = getSearchCriteria(tabDyn, 0);

            RequeteurService requeteurService = EpgUIServiceLocator.getRequeteurService();
            return requeteurService.getCountResults(searchCriteriaExp, session);
        } else {
            throw new EPGException(String.format(UNKNOWN_SEARCH_TYPE_ERROR_MESSAGE, type));
        }
    }

    @Override
    protected Collection<ElasticDossier> getSearchResults(
        CoreSession session,
        Map<String, Serializable> eventProperties
    )
        throws Exception {
        TableauDynamique tabDyn = getTableauDynamique(session, eventProperties);

        FavorisRechercheType type = tabDyn.getType();

        if (type == FavorisRechercheType.ES_LIBRE) {
            RechercheLibre rechercheLibre = getSearchCriteria(tabDyn, getMaxItemsToExport());

            RequeteurService requeteurService = EpgUIServiceLocator.getRequeteurService();
            return requeteurService.getResults(rechercheLibre, session).getResults();
        } else if (type == FavorisRechercheType.ES_EXPERTE) {
            SearchCriteriaExp searchCriteriaExp = getSearchCriteria(tabDyn, getMaxItemsToExport());

            RequeteurService requeteurService = EpgUIServiceLocator.getRequeteurService();
            return requeteurService.getResults(searchCriteriaExp, session, true).getResults().values();
        } else {
            throw new EPGException(String.format(UNKNOWN_SEARCH_TYPE_ERROR_MESSAGE, type));
        }
    }

    private static TableauDynamique getTableauDynamique(
        CoreSession session,
        Map<String, Serializable> eventProperties
    ) {
        String idTabDyn = (String) eventProperties.get(SolonEpgEventConstant.ID_TAB_DYN);
        DocumentModel tabDynDoc = session.getDocument(new IdRef(idTabDyn));

        return tabDynDoc.getAdapter(TableauDynamique.class);
    }

    private static <T extends AbstractCriteria> T getSearchCriteria(TableauDynamique tabDyn, int pageSize) {
        Gson gson = new Gson();
        FavorisRechercheType type = tabDyn.getType();
        String jsonQuery = tabDyn.getQuery();

        if (type == FavorisRechercheType.ES_LIBRE) {
            RechercheLibre rechercheLibre = gson.fromJson(jsonQuery, RechercheLibre.class);
            rechercheLibre.setPageSize(pageSize);
            return (T) rechercheLibre;
        } else if (type == FavorisRechercheType.ES_EXPERTE) {
            SearchCriteriaExp searchCriteriaExp = gson.fromJson(jsonQuery, SearchCriteriaExp.class);
            searchCriteriaExp.setPageSize(pageSize);
            return (T) searchCriteriaExp;
        } else {
            throw new EPGException(String.format(UNKNOWN_SEARCH_TYPE_ERROR_MESSAGE, type));
        }
    }
}
