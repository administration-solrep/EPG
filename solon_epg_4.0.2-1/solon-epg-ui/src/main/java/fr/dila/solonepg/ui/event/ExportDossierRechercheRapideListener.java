package fr.dila.solonepg.ui.event;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaRapide;
import fr.dila.solonepg.elastic.services.RequeteurService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.nuxeo.ecm.core.api.CoreSession;

public class ExportDossierRechercheRapideListener extends AbstractExportDossierRechercheListener {

    public ExportDossierRechercheRapideListener() {
        super(SolonEpgEventConstant.DOSSIERS_RECHERCHE_RAPIDE_EXPORT_EVENT);
    }

    @Override
    protected long countItemsToExport(CoreSession session, Map<String, Serializable> eventProperties) throws Exception {
        SearchCriteriaRapide rechercheRapide = getSearchCriteria(eventProperties, 0);

        RequeteurService requeteurService = EpgUIServiceLocator.getRequeteurService();
        return requeteurService.getCountResults(rechercheRapide, session);
    }

    @Override
    protected Collection<ElasticDossier> getSearchResults(
        CoreSession session,
        Map<String, Serializable> eventProperties
    )
        throws Exception {
        SearchCriteriaRapide rechercheRapide = getSearchCriteria(eventProperties, getMaxItemsToExport());

        RequeteurService requeteurService = EpgUIServiceLocator.getRequeteurService();
        return requeteurService.getResults(rechercheRapide, session, true).getResults().values();
    }

    private static SearchCriteriaRapide getSearchCriteria(Map<String, Serializable> eventProperties, int pageSize) {
        String nor = (String) eventProperties.get(SolonEpgEventConstant.NOR);

        SearchCriteriaRapide rechercheRapide = new SearchCriteriaRapide();
        rechercheRapide.setWildcardList(Stream.of(nor.split(";")).collect(Collectors.toList()));
        rechercheRapide.setPageSize(pageSize);
        return rechercheRapide;
    }
}
