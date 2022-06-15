package fr.dila.solonepg.ui.jaxrs.webobject.page.recherche.favoris;

import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.model.EpgRechercheTemplate;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.th.bean.ModeleFDRListForm;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliRechercheFavorisFdr")
public class EpgRechercheFavorisFdr extends SolonWebObject {
    private static final String FDR_URL = "/recherche/favoris/fdr";

    @GET
    public ThTemplate getFavorisDossiers(@SwBeanParam ModeleFDRListForm modeleFDRform) {
        context.putInContextData(SSContextDataKey.LIST_MODELE_FDR, modeleFDRform);

        context.setNavigationContextTitle(
            new Breadcrumb(
                "Favoris de consultation - Modèles de feuille de route",
                FDR_URL,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
        template.setContext(context);
        template.setName("pages/recherche/favoris-fdr.html");
        List<String> idFdr = EpgUIServiceLocator
            .getEpgFavorisConsultationUIService()
            .getFavorisConsultationFdrsId(context);
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idFdr);
        ModeleFDRList lstResults = EpgUIServiceLocator.getEpgModeleFdrListUIService().getModelesFDRByIds(context);

        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());
        map.put(STTemplateConstants.NB_RESULTS, lstResults.getNbTotal());
        map.put(STTemplateConstants.RESULT_FORM, modeleFDRform);
        map.put(STTemplateConstants.DATA_URL, FDR_URL);
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/recherche/favoris/fdr");
        lstResults.setHasSelect(true);
        lstResults.setHasPagination(true);
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        map.put(
            EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS,
            context.getActions(EpgActionCategory.SELECTION_TOOL_FDR_ACTIONS)
        );
        map.put(
            EpgTemplateConstants.FAVORIS_DOSSIER_ACTIONS,
            context.getActions(EpgActionCategory.FAVORIS_FDR_ACTIONS)
        );
        map.put(STTemplateConstants.BASE_URL, "/recherche/fdr");
        template.setData(map);
        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgRechercheTemplate();
    }
}
