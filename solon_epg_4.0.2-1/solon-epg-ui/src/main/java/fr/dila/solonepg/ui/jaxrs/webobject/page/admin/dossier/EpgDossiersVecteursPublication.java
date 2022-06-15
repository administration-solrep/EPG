package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.dossier;

import fr.dila.solonepg.ui.bean.dossier.vecteurpublication.AdminPublicationMinisterielleList;
import fr.dila.solonepg.ui.bean.dossier.vecteurpublication.AdminVecteurPublicationList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.EpgVecteurPublicationUIService;
import fr.dila.solonepg.ui.th.bean.AdminPublicationMinisterielleForm;
import fr.dila.solonepg.ui.th.bean.AdminVecteurPublicationForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Map;
import javax.ws.rs.GET;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AdminDossiersVecteursPublication")
public class EpgDossiersVecteursPublication extends SolonWebObject {

    @GET
    public ThTemplate getHome(
        @SwBeanParam AdminVecteurPublicationForm adminVecteurPublicationForm,
        @SwBeanParam AdminPublicationMinisterielleForm adminBulletinOfficielForm
    ) {
        verifyAction(EpgActionEnum.ADMIN_MENU_DOSSIER_VECTEUR, EpgURLConstants.URL_VECTEUR_PUBLICATION);
        context.putInContextData(EpgContextDataKey.ADMIN_VECTEUR_PUBLICATION_FORM, adminVecteurPublicationForm);
        context.putInContextData(EpgContextDataKey.ADMIN_PUBLICATION_MINISTERIELLE_FORM, adminBulletinOfficielForm);
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("menu.admin.dossier.vecteur.title"),
                EpgURLConstants.URL_VECTEUR_PUBLICATION,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
        template.setName("pages/admin/dossier/vecteurs-publication");
        Map<String, Object> map = template.getData();
        final EpgVecteurPublicationUIService epgVecteurPublicationUIService = EpgUIServiceLocator.getEpgVecteurPublicationUIService();

        // VECTEUR DE PUBLICATION
        AdminVecteurPublicationList adminVecteurPublicationList = new AdminVecteurPublicationList();
        adminVecteurPublicationList.setListe(epgVecteurPublicationUIService.getVecteurPublications(context));
        adminVecteurPublicationForm =
            ObjectHelper.requireNonNullElseGet(adminVecteurPublicationForm, AdminVecteurPublicationForm::newForm);
        map.put(
            STTemplateConstants.LST_COLONNES,
            adminVecteurPublicationList.getListeColonnes(adminVecteurPublicationForm)
        );
        map.put(STTemplateConstants.RESULT_LIST, adminVecteurPublicationList);
        map.put(EpgTemplateConstants.NB_VECTEURS_PUBLICATION, adminVecteurPublicationList.getNbTotal());
        map.put(STTemplateConstants.DATA_URL, EpgURLConstants.URL_VECTEUR_PUBLICATION);
        map.put(
            EpgTemplateConstants.DATA_AJAX_URL_VECTEUR_PUBLICATION,
            EpgURLConstants.URL_AJAX_VECTEUR_PUBLICATION_TABLE
        );
        map.put(
            EpgTemplateConstants.EDIT_ACTION_VECTEUR_PUBLICATION,
            context.getActions(EpgActionCategory.VECTEUR_PUBLICATION_TABLE_ACTIONS)
        );

        // PUBLICATIONS MINISTERIELLES
        AdminPublicationMinisterielleList adminPublicationMinisterielleList = new AdminPublicationMinisterielleList();
        adminPublicationMinisterielleList.setListe(epgVecteurPublicationUIService.getBulletinsOfficiels(context));
        adminBulletinOfficielForm =
            ObjectHelper.requireNonNullElseGet(adminBulletinOfficielForm, AdminPublicationMinisterielleForm::newForm);
        map.put(
            EpgTemplateConstants.LST_COLONNES_PUBLICATIONS,
            adminPublicationMinisterielleList.getListeColonnes(adminBulletinOfficielForm)
        );
        map.put(EpgTemplateConstants.RESULT_LIST_PUBLICATIONS, adminPublicationMinisterielleList);
        map.put(EpgTemplateConstants.NB_PUBLICATIONS_MINISTERIELLES, adminPublicationMinisterielleList.getNbTotal());
        map.put(STTemplateConstants.DATA_URL, EpgURLConstants.URL_VECTEUR_PUBLICATION);
        map.put(
            EpgTemplateConstants.DATA_AJAX_URL_PUBLICATION_MINISTERIELLE,
            EpgURLConstants.URL_AJAX_PUBLICATION_MINISTERIELLE_TABLE
        );
        map.put(
            EpgTemplateConstants.DELETE_ACTION_PUBLICATION_MINISTERIELLE,
            context.getActions(EpgActionCategory.PUBLICATION_MINISTERIELLE_TABLE_ACTIONS)
        );

        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgAdminTemplate();
    }
}
