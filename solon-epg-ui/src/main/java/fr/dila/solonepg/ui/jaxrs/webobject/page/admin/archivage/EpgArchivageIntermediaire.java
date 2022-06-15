package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.archivage;

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
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliArchivageIntermediaire")
public class EpgArchivageIntermediaire extends SolonWebObject {

    @GET
    public ThTemplate getArchivageIntermediaire(@SwBeanParam DossierListForm dossierlistForm) {
        verifyAction(EpgActionEnum.ADMIN_MENU_ARCHIVAGE_INTERMEDIAIRE, EpgURLConstants.ADMIN_ARCHIVAGE_INTERMEDIAIRE);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);

        context.setNavigationContextTitle(
            new Breadcrumb(
                "Dossiers vers archivage intermédiaire",
                EpgURLConstants.ADMIN_ARCHIVAGE_INTERMEDIAIRE,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
        EpgDossierList lstResults = SolonEpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersCandidatToArchivageIntermediaire(context);

        dossierlistForm = ObjectHelper.requireNonNullElseGet(dossierlistForm, DossierListForm::newForm);
        dossierlistForm.setColumnVisibility(lstResults.getListeColonnes());

        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());
        map.put(STTemplateConstants.RESULT_FORM, dossierlistForm);
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(
            STTemplateConstants.EDIT_ACTIONS,
            context.getActions(EpgActionCategory.ADMIN_ARCHIVAGE_DOSSIER_ACTIONS)
        );
        map.put(STTemplateConstants.DATA_URL, EpgURLConstants.ADMIN_ARCHIVAGE_INTERMEDIAIRE);
        map.put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());

        Map<String, Object> otherParameter = FiltreUtils.initOtherParamMapWithProviderInfos(context);
        map.put(STTemplateConstants.OTHER_PARAMETER, otherParameter);

        template.setData(map);
        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgAdminTemplate("pages/admin/archivage/archivage-dossier-intermediaire", context);
    }
}
