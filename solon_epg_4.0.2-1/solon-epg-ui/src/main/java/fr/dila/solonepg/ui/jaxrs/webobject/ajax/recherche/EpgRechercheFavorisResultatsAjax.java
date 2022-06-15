package fr.dila.solonepg.ui.jaxrs.webobject.ajax.recherche;

import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getFavorisRechercheUIService;
import static fr.dila.st.ui.enums.STContextDataKey.USERS_LIST_FORM;
import static fr.dila.st.ui.th.constants.STTemplateConstants.DATA_AJAX_URL;
import static fr.dila.st.ui.th.constants.STTemplateConstants.DATA_URL;
import static fr.dila.st.ui.th.constants.STTemplateConstants.LST_COLONNES;
import static fr.dila.st.ui.th.constants.STTemplateConstants.NB_RESULTS;
import static fr.dila.st.ui.th.constants.STTemplateConstants.OTHER_PARAMETER;
import static fr.dila.st.ui.th.constants.STTemplateConstants.RESULTAT_LIST;
import static fr.dila.st.ui.th.constants.STTemplateConstants.RESULT_FORM;
import static fr.dila.st.ui.th.constants.STTemplateConstants.RESULT_LIST;
import static org.apache.commons.lang3.StringUtils.isBlank;

import fr.dila.solonepg.api.recherche.FavorisRecherche;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.favoris.recherche.EpgFavorisRechercheDTO;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgColumnEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.helper.EpgDossierListHelper;
import fr.dila.solonepg.ui.services.FavorisRechercheUIService;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.ss.core.enumeration.StatutModeleFDR;
import fr.dila.ss.ui.bean.SSUsersList;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.enums.SSActionCategory;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.th.bean.ModeleFDRListForm;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.bean.UsersListForm;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "RechercheFavorisResultatsAjax")
public class EpgRechercheFavorisResultatsAjax extends SolonWebObject {
    private static final String ID_FAVORI = "idFavori";

    @GET
    @Path("{idFavori}")
    public ThTemplate getHomeResults(
        @PathParam(ID_FAVORI) @SwId String idFavori,
        @SwBeanParam DossierListForm dossierForm,
        @SwBeanParam UsersListForm userForm,
        @SwBeanParam ModeleFDRListForm fdrForm
    ) {
        return getResultats(idFavori, dossierForm, userForm, fdrForm);
    }

    @GET
    @Path("resultats")
    public ThTemplate getResultats(
        @QueryParam(ID_FAVORI) @SwId String idFavori,
        @SwBeanParam DossierListForm dossierForm,
        @SwBeanParam UsersListForm userForm,
        @SwBeanParam ModeleFDRListForm fdrForm
    ) {
        context.setCurrentDocument(idFavori);
        DocumentModel favoriDoc = context.getCurrentDocument();
        EpgFavorisRechercheDTO favori = MapDoc2Bean.docToBean(favoriDoc, EpgFavorisRechercheDTO.class);

        context.setNavigationContextTitle(
            new Breadcrumb(
                String.format("Résultats requête favori %s", favori.getIntitule()),
                "/recherche/favoris/executer/" + favori.getIdFavori(),
                Breadcrumb.TITLE_ORDER + 1
            )
        );

        if (isBlank(template.getName())) {
            template = new AjaxLayoutThTemplate("fragments/components/favoriInconnu", context);
        }

        FavorisRecherche favRech = favoriDoc.getAdapter(FavorisRecherche.class);
        FavorisRechercheUIService favorisRechercheUIService = getFavorisRechercheUIService();
        if (favRech.isTypeDossier() || favRech.isTypeDossierSimple()) {
            dossierForm = ObjectHelper.requireNonNullElseGet(dossierForm, DossierListForm::newForm);
            dossierForm.setDossier(true);
            context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierForm);

            EpgDossierList lstDossier = favorisRechercheUIService.getDossiers(context);
            EpgDossierListHelper.setUserColumns(
                context.getSession(),
                dossierForm,
                lstDossier,
                Arrays.asList(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName())
            );

            setFragmentAjax(EpgRechercheDerniersResultatsAjax.TABLE_DOSSIER);
            dossierForm.setColumnVisibility(lstDossier.getListeColonnes());
            template.getData().put(EpgTemplateConstants.EST_DOSSIER, true);
            template.getData().put(LST_COLONNES, lstDossier.getListeColonnes());
            template.getData().put(RESULT_FORM, dossierForm);
            template.getData().put(RESULT_LIST, lstDossier);
            template.getData().put(NB_RESULTS, lstDossier.getNbTotal());
            template.getData().put(STTemplateConstants.LST_SORTED_COLONNES, lstDossier.getListeSortedColonnes());
            template
                .getData()
                .put(STTemplateConstants.LST_SORTABLE_COLONNES, lstDossier.getListeSortableAndVisibleColonnes());
            template
                .getData()
                .put(
                    EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS,
                    context.getActions(EpgActionCategory.SELECTION_TOOL_DOSSIERS_ACTIONS)
                );
            template
                .getData()
                .put(
                    EpgTemplateConstants.ADD_DOSSIERS_TO_FAVORIS_ACTIONS,
                    context.getActions(EpgActionCategory.ADD_DOSSIERS_TO_FAVORIS_ACTIONS)
                );
        } else if (favRech.isTypeUser()) {
            userForm = ObjectHelper.requireNonNullElseGet(userForm, UsersListForm::new);
            setFragmentAjax(EpgRechercheDerniersResultatsAjax.TABLE_UTILISATEURS);
            template.getData().put(EpgTemplateConstants.EST_UTILISATEUR, true);
            context.putInContextData(USERS_LIST_FORM, userForm);

            SSUsersList listUser = favorisRechercheUIService.getUsersList(context);
            template.getData().put(RESULTAT_LIST, listUser.getListe());
            template.getData().put(STTemplateConstants.HAS_PAGINATION, true);
            template.getData().put(RESULT_FORM, userForm);
            template.getData().put(RESULT_LIST, listUser);
            template.getData().put(LST_COLONNES, listUser.getListeColonnes(userForm));
            template.getData().put(NB_RESULTS, listUser.getNbTotal());
        } else if (favRech.isTypeModeleFeuilleRoute()) {
            fdrForm = ObjectHelper.requireNonNullElseGet(fdrForm, ModeleFDRListForm::new);
            context.putInContextData(SSContextDataKey.LIST_MODELE_FDR, fdrForm);

            ModeleFDRList listMfdr = favorisRechercheUIService.getModelesFeuilleRoute(context);

            setFragmentAjax(EpgRechercheDerniersResultatsAjax.TABLE_MFDR);
            template.getData().put("statutModeleFDR", StatutModeleFDR.values());
            template.getData().put(RESULT_LIST, listMfdr);
            template.getData().put(LST_COLONNES, listMfdr.getListeColonnes());
            template.getData().put(NB_RESULTS, listMfdr.getNbTotal());
            template.getData().put(RESULT_FORM, fdrForm);
            template.getData().put(EpgTemplateConstants.EST_MFDR, true);
            template.getData().put(STTemplateConstants.BASE_URL, EpgURLConstants.BASE_URL_FDR);
            template
                .getData()
                .put(
                    SSTemplateConstants.FAVORIS_RECHERCHE_FDR_ACTIONS,
                    context.getActions(SSActionCategory.FAVORIS_RECHERCHE_FDR_ACTIONS)
                );
        }
        Map<String, Object> otherParameter = new HashMap<>();
        otherParameter.put(ID_FAVORI, idFavori);
        template.getData().put(OTHER_PARAMETER, otherParameter);
        template.getData().put(EpgTemplateConstants.FAVORI_RECHERCHE, favori);
        template.getData().put(DATA_URL, String.format(EpgURLConstants.URL_PAGE_FAVORI_RECHERCHE, idFavori));
        template.getData().put(DATA_AJAX_URL, EpgURLConstants.URL_AJAX_FAVORI_RECHERCHE);
        return template;
    }

    private void setFragmentAjax(String name) {
        if (template instanceof AjaxLayoutThTemplate) {
            template = new AjaxLayoutThTemplate(name, context);
        }
    }
}
