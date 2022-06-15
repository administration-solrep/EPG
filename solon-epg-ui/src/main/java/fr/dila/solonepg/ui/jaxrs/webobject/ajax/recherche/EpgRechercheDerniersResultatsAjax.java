package fr.dila.solonepg.ui.jaxrs.webobject.ajax.recherche;

import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.EpgUserList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgColumnEnum;
import fr.dila.solonepg.ui.helper.EpgDossierListHelper;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.ss.core.enumeration.StatutModeleFDR;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.enums.SSActionCategory;
import fr.dila.ss.ui.th.bean.ModeleFDRListForm;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.enums.STActionCategory;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.bean.PaginationForm;
import fr.dila.st.ui.th.bean.UsersListForm;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DerniersResultatsConsultesAjax")
public class EpgRechercheDerniersResultatsAjax extends SolonWebObject {
    private static final String MODELE_FDR = "mfdr";
    private static final String DOSSIER = "dossier";
    private static final String USER = "utilisateur";
    public static final String TABLE_MFDR = "fragments/table/tableModelesFDR";
    public static final String TABLE_DOSSIER = "fragments/table/tableDossiers";
    public static final String TABLE_UTILISATEURS = "fragments/table/tableUsers";

    public EpgRechercheDerniersResultatsAjax() {
        super();
    }

    @GET
    @Path("{type}")
    public ThTemplate getHomeResults(@PathParam("type") String type, @SwBeanParam PaginationForm paginationForm) {
        return getResultats(type, paginationForm);
    }

    @GET
    @Path("consultes")
    public ThTemplate getResultats(
        @QueryParam("type") @SwNotEmpty String type,
        @SwBeanParam PaginationForm paginationForm
    ) {
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("recherche.derniers.resultats.consultes.title", convertType(type)),
                String.format(EpgURLConstants.URL_PAGE_DERNIERS_RESULTATS, type),
                Breadcrumb.TITLE_ORDER + 1,
                context.getWebcontext().getRequest()
            )
        );
        template = getAjaxTemplate(type);
        paginationForm = ObjectHelper.requireNonNullElseGet(paginationForm, PaginationForm::new);
        context.putInContextData(STContextDataKey.PAGINATION_FORM, paginationForm);

        Map<String, Object> map = template.getData();
        if (DOSSIER.equals(type)) {
            EpgDossierList lstDossier = EpgUIServiceLocator
                .getSolonEpgDossierListUIService()
                .getDernierDossierConsultee(context);
            DossierListForm dossierForm = new DossierListForm();
            dossierForm.setColumnVisibility(lstDossier.getListeColonnes());
            dossierForm.setSize(paginationForm.getSize());
            dossierForm.setPage(paginationForm.getPage());
            EpgDossierListHelper.setUserColumns(
                context.getSession(),
                dossierForm,
                lstDossier,
                Collections.singletonList(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName()),
                false
            );
            map.put(STTemplateConstants.LST_COLONNES, lstDossier.getListeColonnes());
            map.put(STTemplateConstants.RESULT_FORM, dossierForm);
            map.put(STTemplateConstants.RESULT_LIST, lstDossier);
            map.put(STTemplateConstants.NB_RESULTS, lstDossier.getNbTotal());
            map.put(EpgTemplateConstants.EST_DOSSIER, true);
            map.put(
                EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS,
                context.getActions(EpgActionCategory.SELECTION_TOOL_DOSSIERS_ACTIONS)
            );
            map.put(
                EpgTemplateConstants.ADD_DOSSIERS_TO_FAVORIS_ACTIONS,
                context.getActions(EpgActionCategory.ADD_DOSSIERS_TO_FAVORIS_ACTIONS)
            );
        } else if (USER.equals(type)) {
            EpgUserList listUser = EpgUIServiceLocator.getEpgUserListUIService().getDernierUserConsulte(context);

            UsersListForm userForm = new UsersListForm();
            userForm.setSize(paginationForm.getSize());
            userForm.setPage(paginationForm.getPage());

            map.put(STTemplateConstants.RESULT_FORM, userForm);
            map.put(STTemplateConstants.RESULT_LIST, listUser);
            map.put(STTemplateConstants.RESULTAT_LIST, listUser.getListe());
            map.put(STTemplateConstants.LST_COLONNES, listUser.getListeColonnes());
            map.put(STTemplateConstants.NB_RESULTS, listUser.getNbTotal());
            map.put(STTemplateConstants.HAS_PAGINATION, true);
            map.put(EpgTemplateConstants.EST_UTILISATEUR, true);
            map.put(STTemplateConstants.GENERALE_ACTIONS, context.getActions(STActionCategory.USER_ACTION_LIST_LEFT));
        } else if (MODELE_FDR.equals(type)) {
            ModeleFDRList lstResults = EpgUIServiceLocator
                .getEpgModeleFdrListUIService()
                .getDernierModelFDRConsulte(context);
            lstResults.buildColonnesNonTriables();
            lstResults.setHasPagination(true);
            lstResults.setHasSelect(true);
            ModeleFDRListForm modeleForm = new ModeleFDRListForm();
            modeleForm.setSize(paginationForm.getSize());
            modeleForm.setPage(paginationForm.getPage());
            map.put(EpgTemplateConstants.STATUT_MODELE_FDR, StatutModeleFDR.values());
            map.put(STTemplateConstants.RESULT_LIST, lstResults);
            map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
            map.put(STTemplateConstants.NB_RESULTS, lstResults.getNbTotal());
            map.put(STTemplateConstants.RESULT_FORM, modeleForm);
            map.put(
                EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS,
                context.getActions(EpgActionCategory.SELECTION_TOOL_FDR_ACTIONS)
            );
            map.put(
                SSTemplateConstants.FAVORIS_RECHERCHE_FDR_ACTIONS,
                context.getActions(SSActionCategory.FAVORIS_RECHERCHE_FDR_ACTIONS)
            );
            map.put(EpgTemplateConstants.EST_MFDR, true);
            map.put(STTemplateConstants.BASE_URL, EpgURLConstants.BASE_URL_FDR);
        }
        Map<String, Object> otherParameter = new HashMap<>();
        otherParameter.put(EpgTemplateConstants.TYPE, type);
        map.put(STTemplateConstants.OTHER_PARAMETER, otherParameter);
        map.put(
            STTemplateConstants.TITLE,
            ResourceHelper.getString("recherche.derniers.resultats.consultes.title", convertType(type))
        );
        map.put(STTemplateConstants.DATA_URL, String.format(EpgURLConstants.URL_PAGE_DERNIERS_RESULTATS, type));
        map.put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DERNIERS_RESULTATS);
        return template;
    }

    private ThTemplate getAjaxTemplate(String type) {
        if (StringUtils.isBlank(template.getName())) {
            switch (type) {
                case DOSSIER:
                    template = new AjaxLayoutThTemplate(TABLE_DOSSIER, context);
                    break;
                case USER:
                    template = new AjaxLayoutThTemplate(TABLE_UTILISATEURS, context);
                    break;
                case MODELE_FDR:
                    template = new AjaxLayoutThTemplate(TABLE_MFDR, context);
                    break;
                default:
                    break;
            }
        }
        return template;
    }

    private String convertType(String type) {
        switch (type) {
            case DOSSIER:
                return "Dossiers";
            case USER:
                return "Utilisateurs";
            case MODELE_FDR:
                return "Modèles de feuille de route";
            default:
                return StringUtils.EMPTY;
        }
    }
}
