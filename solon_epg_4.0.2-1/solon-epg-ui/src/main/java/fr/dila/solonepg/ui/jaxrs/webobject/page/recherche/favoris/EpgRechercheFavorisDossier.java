package fr.dila.solonepg.ui.jaxrs.webobject.page.recherche.favoris;

import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.model.EpgRechercheTemplate;
import fr.dila.st.core.util.ObjectHelper;
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

@WebObject(type = "AppliRechercheFavorisDossier")
public class EpgRechercheFavorisDossier extends SolonWebObject {
    private static final String DOSSIER_URL = "/recherche/favoris/dossiers";

    @GET
    public ThTemplate getFavorisDossiers(@SwBeanParam DossierListForm dossierlistForm) {
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);

        context.setNavigationContextTitle(
            new Breadcrumb(
                "Favoris de consultation - Dossiers",
                DOSSIER_URL,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
        template.setContext(context);
        template.setName("pages/recherche/favoris-dossier.html");
        List<String> idDossiers = EpgUIServiceLocator
            .getEpgFavorisConsultationUIService()
            .getFavorisConsultationDossiersId(context);
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idDossiers);
        EpgDossierList lstResults = EpgUIServiceLocator.getSolonEpgDossierListUIService().getDossiersFavoris(context);

        dossierlistForm = ObjectHelper.requireNonNullElseGet(dossierlistForm, DossierListForm::newForm);
        dossierlistForm.setColumnVisibility(lstResults.getListeColonnes());

        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());
        map.put(STTemplateConstants.RESULT_FORM, dossierlistForm);
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.DATA_URL, DOSSIER_URL);
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/recherche/favoris/dossiers");
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        map.put(
            EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS,
            context.getActions(EpgActionCategory.SELECTION_TOOL_DOSSIERS_ACTIONS)
        );
        map.put(
            EpgTemplateConstants.FAVORIS_DOSSIER_ACTIONS,
            context.getActions(EpgActionCategory.FAVORIS_DOSSIERS_ACTIONS)
        );
        template.setData(map);
        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgRechercheTemplate();
    }
}
