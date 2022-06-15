package fr.dila.solonepg.ui.jaxrs.webobject.page.recherche;

import com.google.common.collect.Lists;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgColumnEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgRechercheUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.ui.enums.SSUserSessionKey;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliEpgRapideResultats")
public class EpgRechercheRapideResults extends SolonWebObject {
    private static final ArrayList<String> DEFAULT_COLONNES = Lists.newArrayList(
        EpgColumnEnum.TEXTE_ENTREPRISE.getNxPropName(),
        EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName()
    );
    private static final String SEARCH_RESULT_KEYS = "Résultat(s) de recherche";

    public EpgRechercheRapideResults() {
        super();
    }

    @POST
    public Object getPostResults() {
        return doRecherche(true);
    }

    @GET
    public Object getGetResults() {
        return doRecherche(false);
    }

    protected Object doRecherche(boolean isReload) {
        template.setContext(context);

        String nor = UserSessionHelper.getUserSessionParameter(context, SSUserSessionKey.NOR);
        DossierListForm dossierlistForm = null;
        if (isReload) {
            dossierlistForm = UserSessionHelper.getUserSessionParameter(context, SSUserSessionKey.SEARCH_RESULT_FORM);
        }

        EpgDossierList lstResults = new EpgDossierList();
        lstResults.buildColonnes(dossierlistForm, DEFAULT_COLONNES, true, true);

        dossierlistForm = ObjectHelper.requireNonNullElseGet(dossierlistForm, DossierListForm::newForm);
        dossierlistForm.setRechercheES(true);

        if (StringUtils.isNotEmpty(nor)) {
            context.putInContextData(EpgContextDataKey.DOSSIER_NOR, nor);
            context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
            EpgRechercheUIService epgRechercheUIService = EpgUIServiceLocator.getEpgRechercheUIService();
            lstResults = epgRechercheUIService.doRechercheRapide(context);

            if (lstResults.getNbTotal() == 1 && CollectionUtils.isNotEmpty(lstResults.getListe())) {
                String dossierId = lstResults.getListe().get(0).getDossierId();
                EpgCorbeilleService epgCorbeilleService = SolonEpgServiceLocator.getCorbeilleService();
                List<DocumentModel> dossiersLinks = epgCorbeilleService.findDossierLink(
                    context.getSession(),
                    dossierId
                );
                // On redirige vers le dossier trouvé
                if (dossiersLinks.isEmpty()) {
                    // Pas de dossierLink actif : recherche sans action attendue
                    return redirect("dossier/" + dossierId + "/document");
                } else if (dossiersLinks.size() == 1) {
                    UserSessionHelper.putUserSessionParameter(context, SpecificContext.LAST_TEMPLATE, ThTemplate.class);
                    // Un seul dossierLink possible -> on le sélectionne
                    String dossierLinkId = dossiersLinks.get(0).getId();
                    return redirect("dossier/" + dossierId + "/document?dossierLinkId=" + dossierLinkId);
                }
            }
        }

        dossierlistForm.setColumnVisibility(lstResults.getListeColonnes());
        lstResults.setTitre(SEARCH_RESULT_KEYS);
        lstResults.setSousTitre(String.format("%d résultat(s) trouvé(s)", lstResults.getNbTotal()));

        // Je renvoie mon formulaire en sortie
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.RESULT_FORM, dossierlistForm);

        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.TITRE, lstResults.getTitre());
        map.put(STTemplateConstants.SOUS_TITRE, lstResults.getSousTitre());
        map.put(STTemplateConstants.DISPLAY_TABLE, !lstResults.getListe().isEmpty());
        map.put(STTemplateConstants.GENERALE_ACTIONS, null);
        map.put(
            EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS,
            context.getActions(EpgActionCategory.SELECTION_TOOL_DOSSIERS_ACTIONS)
        );
        map.put(
            EpgTemplateConstants.ADD_DOSSIERS_TO_FAVORIS_ACTIONS,
            context.getActions(EpgActionCategory.ADD_DOSSIERS_TO_FAVORIS_ACTIONS)
        );
        map.put(STTemplateConstants.DATA_URL, "/recherche/rapide");
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/recherche/rapide");

        map.put(STTemplateConstants.EXPORT_ACTION, context.getAction(EpgActionEnum.RECHERCHE_RAPIDE_DOSSIER_EXPORT));

        template.setData(map);

        return template;
    }
}
