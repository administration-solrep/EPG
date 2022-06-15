package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier;

import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getDossiersSimilairesUIService;

import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.dossier.bordereau.EpgDossierSimilaireBordereauDTO;
import fr.dila.solonepg.ui.bean.dossier.similaire.DossierSimilaireList;
import fr.dila.solonepg.ui.bean.dossier.similaire.DossierSimilaireListForm;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.model.EpgTravailTemplate;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliDossiersimilaire")
public class EpgDossierSimilaire extends SolonWebObject {
    private static final String DOSSIER_ID = "dossierId";
    private static final String DOSSIER_SOURCE_ID = "dossierSourceId";
    private static final String CURRENT_INDEX = "indexCourant";
    private static final String MAP_KEY_BORDEREAU = "bordereauDto";

    public EpgDossierSimilaire() {
        super();
    }

    @GET
    public ThTemplate searchSimilarDirectory(@SwBeanParam DossierSimilaireListForm resultForm) {
        String url = String.format("/dossiersimilaire?dossierId=%s#main_content", resultForm.getDossierId());
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("dossier.similaire.titre"),
                url,
                Breadcrumb.SUBTITLE_ORDER + 1,
                context.getWebcontext().getRequest()
            )
        );
        template.setName("pages/dossier/dossier-similaires");

        ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();

        Long pageSizeUtilisateur = profilUtilisateurService.getUtilisateurPageSize(context.getSession());
        if (pageSizeUtilisateur != null) {
            resultForm.setSize(pageSizeUtilisateur.intValue());
        }

        context.putInContextData(EpgContextDataKey.DOSSIER_SIMILAIRE_FORM, resultForm);
        DossierSimilaireList dossierSimilaireList = getDossiersSimilairesUIService().getDossiersSimilaires(context);

        dossierSimilaireList.buildColonnes(resultForm);

        Map<String, Object> map = new HashMap<>();
        map.put(
            STTemplateConstants.URL_PREVIOUS_PAGE,
            String.format("/dossier/%s/bordereau#main_content", resultForm.getDossierId())
        );
        map.put(STTemplateConstants.RESULT_LIST, dossierSimilaireList);
        map.put(STTemplateConstants.LST_COLONNES, dossierSimilaireList.getListeColonnes());
        map.put(STTemplateConstants.LST_SORTED_COLONNES, dossierSimilaireList.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, dossierSimilaireList.getListeSortableAndVisibleColonnes());
        map.put(STTemplateConstants.RESULT_FORM, resultForm);
        map.put(STTemplateConstants.DATA_URL, url);
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/dossiersimilaire/chercher");
        template.setData(map);
        return template;
    }

    @Path("{dossierSourceId}/bordereau/{indexCourant}/{dossierId}")
    @GET
    public ThTemplate similarDirectoryBordereau(
        @PathParam(DOSSIER_SOURCE_ID) String dossierSourceId,
        @PathParam(CURRENT_INDEX) Long indexCourant,
        @PathParam(DOSSIER_ID) String dossierId
    ) {
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("dossier.similaire.bordereau.titre"),
                String.format(
                    "/%s/dossiersimilaire/bordereau/%s/%s#main_content",
                    dossierSourceId,
                    indexCourant,
                    dossierId
                ),
                Breadcrumb.SUBTITLE_ORDER + 2,
                context.getWebcontext().getRequest()
            )
        );
        template.setName("pages/dossier/dossier-similaires-bordereau");

        context.setCurrentDocument(dossierId);
        context.putInContextData(EpgContextDataKey.CURRENT_INDEX, indexCourant);
        EpgDossierSimilaireBordereauDTO bordereauDTO = EpgUIServiceLocator
            .getDossiersSimilairesUIService()
            .getCurrentBordereau(context);

        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        map.put(MAP_KEY_BORDEREAU, bordereauDTO);
        map.put(STTemplateConstants.EDIT_ACTIONS, context.getActions(EpgActionCategory.DOSSIER_SIMILAIRE_BORDEREAU));
        map.put("idDisabled", true);
        map.put(DOSSIER_SOURCE_ID, dossierSourceId);
        map.put(DOSSIER_ID, dossierId);
        map.put(CURRENT_INDEX, indexCourant);
        template.setData(map);
        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgTravailTemplate();
    }
}
