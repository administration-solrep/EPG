package fr.dila.solonepg.ui.event;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaExp;
import fr.dila.solonepg.elastic.services.RequeteurService;
import fr.dila.solonepg.ui.helper.EpgRequeteHelper;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.ss.ui.bean.RequeteExperteDTO;
import fr.dila.st.api.constant.STRechercheExportEventConstants;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;

public class ExportDossierRechercheExperteListener extends AbstractExportDossierRechercheListener {

    public ExportDossierRechercheExperteListener() {
        super(STRechercheExportEventConstants.EVENT_NAME);
    }

    @Override
    protected long countItemsToExport(CoreSession session, Map<String, Serializable> eventProperties) throws Exception {
        SearchCriteriaExp searchCriteriaExp = getSearchCriteria(eventProperties, 0);

        RequeteurService requeteurService = EpgUIServiceLocator.getRequeteurService();
        return requeteurService.getCountResults(searchCriteriaExp, session);
    }

    @Override
    protected Collection<ElasticDossier> getSearchResults(
        CoreSession session,
        Map<String, Serializable> eventProperties
    )
        throws Exception {
        SearchCriteriaExp searchCriteriaExp = getSearchCriteria(eventProperties, getMaxItemsToExport());

        RequeteurService requeteurService = EpgUIServiceLocator.getRequeteurService();
        return requeteurService.getResults(searchCriteriaExp, session, true).getResults().values();
    }

    private static SearchCriteriaExp getSearchCriteria(Map<String, Serializable> eventProperties, int pageSize) {
        RequeteExperteDTO requestDto = (RequeteExperteDTO) eventProperties.get(SolonEpgEventConstant.REQUEST_DTO);

        SearchCriteriaExp searchCriteriaExp = new SearchCriteriaExp();
        searchCriteriaExp.setClausesOu(EpgRequeteHelper.toListClausesOu(requestDto.getRequetes()));
        searchCriteriaExp.setPageSize(pageSize);
        return searchCriteriaExp;
    }
}
