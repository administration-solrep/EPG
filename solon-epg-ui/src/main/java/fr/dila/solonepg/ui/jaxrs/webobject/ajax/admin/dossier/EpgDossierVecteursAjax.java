package fr.dila.solonepg.ui.jaxrs.webobject.ajax.admin.dossier;

import fr.dila.solonepg.ui.bean.dossier.vecteurpublication.AdminPublicationMinisterielleDTO;
import fr.dila.solonepg.ui.bean.dossier.vecteurpublication.AdminPublicationMinisterielleList;
import fr.dila.solonepg.ui.bean.dossier.vecteurpublication.AdminVecteurPublicationDTO;
import fr.dila.solonepg.ui.bean.dossier.vecteurpublication.AdminVecteurPublicationList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.EpgVecteurPublicationUIService;
import fr.dila.solonepg.ui.th.bean.AdminPublicationMinisterielleForm;
import fr.dila.solonepg.ui.th.bean.AdminVecteurPublicationForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossiersVecteursAjax")
public class EpgDossierVecteursAjax extends SolonWebObject {
    private static final String VECTEUR_PUBLICATION_TABLE_FRAGMENT =
        "fragments/dossier/vecteurs-publication/vecteurs-publication-table";
    private static final String PUBLICATION_MINISTERIELLE_TABLE_FRAGMENT =
        "fragments/dossier/vecteurs-publication/publication-ministerielle-table";

    private static final EpgVecteurPublicationUIService epgVecteurPublicationUIService = EpgUIServiceLocator.getEpgVecteurPublicationUIService();

    @GET
    @Path("liste")
    public ThTemplate getListVecteurs(@SwBeanParam AdminVecteurPublicationForm adminVecteurPublicationForm) {
        context.putInContextData(EpgContextDataKey.ADMIN_VECTEUR_PUBLICATION_FORM, adminVecteurPublicationForm);
        ThTemplate template = new AjaxLayoutThTemplate(VECTEUR_PUBLICATION_TABLE_FRAGMENT, context);
        adminVecteurPublicationForm =
            ObjectHelper.requireNonNullElseGet(adminVecteurPublicationForm, AdminVecteurPublicationForm::newForm);
        Map<String, Object> map = template.getData();

        AdminVecteurPublicationList adminVecteurPublicationList = new AdminVecteurPublicationList();
        adminVecteurPublicationList.setListe(epgVecteurPublicationUIService.getVecteurPublications(context));
        adminVecteurPublicationForm =
            ObjectHelper.requireNonNullElseGet(adminVecteurPublicationForm, AdminVecteurPublicationForm::newForm);
        putDataVecteurPublication(adminVecteurPublicationForm, map, adminVecteurPublicationList);

        return template;
    }

    @POST
    @Path("sauvegarde")
    public ThTemplate saveOrUpdateVecteurs(@SwBeanParam AdminVecteurPublicationDTO adminVecteurPublicationDto) {
        context.putInContextData(EpgContextDataKey.ADMIN_VECTEUR_PUBLICATION, adminVecteurPublicationDto);
        ThTemplate template = new AjaxLayoutThTemplate(VECTEUR_PUBLICATION_TABLE_FRAGMENT, context);
        Map<String, Object> map = template.getData();

        String vecteurId = adminVecteurPublicationDto.getId();

        boolean isCreation = StringUtils.isBlank(vecteurId);
        if (isCreation) {
            epgVecteurPublicationUIService.addVecteur(context);
        } else {
            epgVecteurPublicationUIService.editVecteur(context);
        }

        AdminVecteurPublicationList adminVecteurPublicationList = new AdminVecteurPublicationList();
        adminVecteurPublicationList.setListe(epgVecteurPublicationUIService.getVecteurPublications(context));
        AdminVecteurPublicationForm adminVecteurPublicationForm = AdminVecteurPublicationForm.newForm();
        putDataVecteurPublication(adminVecteurPublicationForm, map, adminVecteurPublicationList);

        return template;
    }

    private void putDataVecteurPublication(
        AdminVecteurPublicationForm adminVecteurPublicationForm,
        Map<String, Object> map,
        AdminVecteurPublicationList adminVecteurPublicationList
    ) {
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
    }

    @GET
    @Path("publication/liste")
    public ThTemplate getListPublications(
        @SwBeanParam AdminPublicationMinisterielleForm adminPublicationMinisterielleForm
    ) {
        context.putInContextData(
            EpgContextDataKey.ADMIN_PUBLICATION_MINISTERIELLE_FORM,
            adminPublicationMinisterielleForm
        );
        ThTemplate template = new AjaxLayoutThTemplate(PUBLICATION_MINISTERIELLE_TABLE_FRAGMENT, context);
        adminPublicationMinisterielleForm =
            ObjectHelper.requireNonNullElseGet(
                adminPublicationMinisterielleForm,
                AdminPublicationMinisterielleForm::newForm
            );

        AdminPublicationMinisterielleList adminPublicationMinisterielleList = new AdminPublicationMinisterielleList();
        adminPublicationMinisterielleList.setListe(epgVecteurPublicationUIService.getBulletinsOfficiels(context));
        putDataPublicationMinisterielle(template, adminPublicationMinisterielleList, adminPublicationMinisterielleForm);

        return template;
    }

    @POST
    @Path("publication/sauvegarde")
    public ThTemplate savePublication(@SwBeanParam AdminPublicationMinisterielleDTO adminPublicationMinisterielleDto) {
        context.putInContextData(EpgContextDataKey.ADMIN_PUBLICATION_MINISTERIELLE, adminPublicationMinisterielleDto);
        ThTemplate template = new AjaxLayoutThTemplate(PUBLICATION_MINISTERIELLE_TABLE_FRAGMENT, context);

        epgVecteurPublicationUIService.createPublicationMinisterielle(context);

        AdminPublicationMinisterielleList adminPublicationMinisterielleList = new AdminPublicationMinisterielleList();
        adminPublicationMinisterielleList.setListe(epgVecteurPublicationUIService.getBulletinsOfficiels(context));
        AdminPublicationMinisterielleForm adminPublicationMinisterielleForm = AdminPublicationMinisterielleForm.newForm();
        putDataPublicationMinisterielle(template, adminPublicationMinisterielleList, adminPublicationMinisterielleForm);

        return template;
    }

    @POST
    @Path("publication/supprimer")
    public ThTemplate deletePublication(@SwRequired @SwNotEmpty @FormParam("id") String idPublication) {
        context.putInContextData(STContextDataKey.ID, idPublication);
        ThTemplate template = new AjaxLayoutThTemplate(PUBLICATION_MINISTERIELLE_TABLE_FRAGMENT, context);

        epgVecteurPublicationUIService.deleteBulletinOfficiel(context);

        AdminPublicationMinisterielleList adminPublicationMinisterielleList = new AdminPublicationMinisterielleList();
        adminPublicationMinisterielleList.setListe(epgVecteurPublicationUIService.getBulletinsOfficiels(context));
        AdminPublicationMinisterielleForm adminPublicationMinisterielleForm = AdminPublicationMinisterielleForm.newForm();
        putDataPublicationMinisterielle(template, adminPublicationMinisterielleList, adminPublicationMinisterielleForm);

        return template;
    }

    private void putDataPublicationMinisterielle(
        ThTemplate template,
        AdminPublicationMinisterielleList adminPublicationMinisterielleList,
        AdminPublicationMinisterielleForm adminPublicationMinisterielleForm
    ) {
        Map<String, Object> map = template.getData();
        map.put(
            EpgTemplateConstants.LST_COLONNES_PUBLICATIONS,
            adminPublicationMinisterielleList.getListeColonnes(adminPublicationMinisterielleForm)
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
    }
}
