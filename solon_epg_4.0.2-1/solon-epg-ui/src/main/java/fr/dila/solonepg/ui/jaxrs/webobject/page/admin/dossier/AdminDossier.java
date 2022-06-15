package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.dossier;

import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AdminDossier")
public class AdminDossier extends SolonWebObject {

    @Path("suppression")
    public Object getDossiersSuppression() {
        return newObject("DossiersSuppression", context);
    }

    @Path("parapheur")
    public Object getAdmant() {
        return newObject("DossiersParapheur", context);
    }

    @Path("fdd")
    public Object getFdd() {
        return newObject("AdminDossiersFdd", context);
    }

    @Path("vecteurs")
    public Object getVecteursPublication() {
        return newObject("AdminDossiersVecteursPublication", context);
    }

    @Path("table/reference")
    public Object getTableReference() {
        return newObject("AdminDossiersTableReference", context);
    }

    @GET
    @Path("abandon")
    public ThTemplate getDossierAAbandonner(@SwBeanParam DossierListForm dossierlistForm) {
        verifyAction(EpgActionEnum.ADMIN_MENU_DOSSIER_ABANDON, EpgURLConstants.ADMIN_DOSSIERS_ABANDON);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        template.setContext(context);
        template.setName("pages/admin/dossier/abandon-dossier");

        context.setNavigationContextTitle(
            new Breadcrumb(
                "Dossier à abandonner",
                EpgURLConstants.ADMIN_DOSSIERS_ABANDON,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
        EpgDossierList lstResults = SolonEpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersAbandon(context);

        dossierlistForm = ObjectHelper.requireNonNullElseGet(dossierlistForm, DossierListForm::newForm);
        dossierlistForm.setColumnVisibility(lstResults.getListeColonnes());

        Map<String, Object> otherParams = FiltreUtils.initOtherParamMapWithProviderInfos(context);

        Map<String, Object> dataMap = template.getData();
        dataMap.put(STTemplateConstants.OTHER_PARAMETER, otherParams);
        dataMap.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        dataMap.put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        dataMap.put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());
        dataMap.put(STTemplateConstants.RESULT_FORM, dossierlistForm);
        dataMap.put(STTemplateConstants.RESULT_LIST, lstResults);
        dataMap.put(STTemplateConstants.NB_RESULTS, lstResults.getNbTotal());
        dataMap.put(STTemplateConstants.TITRE, lstResults.getTitre());
        dataMap.put(STTemplateConstants.SOUS_TITRE, lstResults.getSousTitre());
        dataMap.put(STTemplateConstants.DISPLAY_TABLE, !lstResults.getListe().isEmpty());
        dataMap.put(
            STTemplateConstants.EDIT_ACTIONS,
            context.getActions(EpgActionCategory.ADMIN_ABANDON_DOSSIER_ACTIONS)
        );
        dataMap.put(STTemplateConstants.DATA_URL, EpgURLConstants.ADMIN_DOSSIERS_ABANDON);
        dataMap.put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.AJAX_ADMIN_DOSSIERS_ABANDON);
        dataMap.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());

        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, lstResults, dataMap);

        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgAdminTemplate();
    }
}
