package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suivi.pan;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.ui.bean.pan.AbstractPanSortedList;
import fr.dila.solonepg.ui.enums.pan.PanActionEnum;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanRechercheExperte;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.pan.PanRequeteUIService;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.ss.ui.bean.RequeteExperteDTO;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.jaxrs.webobject.ajax.SSRequeteExperteAjax;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AjaxRequeteExpertePan")
public class PanRechercheExperteAjax extends SSRequeteExperteAjax {

    public PanRechercheExperteAjax() {
        super();
    }

    @Override
    public String getSuffixForSessionKeys(SpecificContext context) {
        return "_pan_" + context.getFromContextData(PanContextDataKey.CURRENT_SECTION);
    }

    @Path("supprimer/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response supprimerTexteMaitre(@PathParam("id") String idTexteMaitre) {
        context.setCurrentDocument(idTexteMaitre);
        PanUIServiceLocator.getPanUIService().removeFromActiviteNormative(context);
        UserSessionHelper.putUserSessionParameter(context, getDtoSessionKey(context) + "_resetSearch", true);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Path("resultats")
    public ThTemplate getResultsFromRequete(@SwBeanParam LoisSuiviesForm form) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/pan/recherche-result-list", context);

        Map<String, Object> map = new HashMap<>();

        form = ObjectHelper.requireNonNullElseGet(form, LoisSuiviesForm::new);

        initRecherche(context, getDtoSessionKey(context), map, form);

        template.setData(map);

        UserSessionHelper.putUserSessionParameter(context, getResultsSessionKey(context), map);
        UserSessionHelper.putUserSessionParameter(context, getDtoSessionKey(context) + "_rechercheForm", form);

        return template;
    }

    public static void initRecherche(
        SpecificContext context,
        String sessionKey,
        Map<String, Object> map,
        LoisSuiviesForm form
    ) {
        context.putInContextData(PanContextDataKey.PAN_FORM, form);
        context.putInContextData(
            SSContextDataKey.REQUETE_EXPERTE_DTO,
            UserSessionHelper.getUserSessionParameter(context, sessionKey, RequeteExperteDTO.class)
        );

        context.putInContextData(PanContextDataKey.CURRENT_TAB, ActiviteNormativeConstants.TAB_AN_RECHERCHE);
        PanRequeteUIService epgRechercheUIService = EpgUIServiceLocator.getPanRequeteUIService();
        AbstractPanSortedList<AbstractMapDTO> lstResults = epgRechercheUIService.doRechercheExperte(context);

        String titre = StringUtils.defaultIfBlank(
            lstResults.getTitre(),
            ResourceHelper.getString("requete.experte.result.titre")
        );

        map.put(PanTemplateConstants.CURRENT_SECTION, context.getFromContextData(PanContextDataKey.CURRENT_SECTION));
        map.put(PanTemplateConstants.CURRENT_PAN_TAB, context.getFromContextData(PanContextDataKey.CURRENT_TAB));
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.RESULT_FORM, form);
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());
        map.put(STTemplateConstants.TITRE, titre);
        map.put(
            PanTemplateConstants.TEXTE_MAITRE_DELETE_ACTION,
            context.getAction(PanActionEnum.TEXTE_MAITRE_DELETE_RECHERCHE_AN)
        );
        map.put(
            STTemplateConstants.SOUS_TITRE,
            ResourceHelper.getString("requete.experte.result.nbResults", lstResults.getNbTotal())
        );
        map.put(STTemplateConstants.DISPLAY_TABLE, lstResults.getNbTotal() > 0);
        map.put(
            STTemplateConstants.DATA_AJAX_URL,
            "/ajax/suivi/pan/" +
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION) +
            "/recherche/experte/resultats"
        );
    }

    @Override
    protected String getContribName() {
        return PanRechercheExperte.getChampsforSection(context);
    }
}
