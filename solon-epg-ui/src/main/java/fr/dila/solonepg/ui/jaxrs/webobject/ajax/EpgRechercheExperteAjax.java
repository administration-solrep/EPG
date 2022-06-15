package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.constants.EpgUIConstants;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.recherche.EpgRechercheExperte;
import fr.dila.solonepg.ui.services.EpgRechercheUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.ui.bean.RequeteExperteDTO;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.jaxrs.webobject.ajax.SSRequeteExperteAjax;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AjaxRequeteExperte")
public class EpgRechercheExperteAjax extends SSRequeteExperteAjax {

    public EpgRechercheExperteAjax() {
        super();
    }

    @POST
    @Path("resultats")
    public ThTemplate getResultsFromRequete(@SwBeanParam DossierListForm form) {
        // Je déclare mon template et j'instancie mon context
        ThTemplate template = new AjaxLayoutThTemplate();
        template.setName("fragments/recherche/result-list");
        template.setContext(context);

        // Je renvoie mon formulaire en sortie
        Map<String, Object> map = new HashMap<>();

        form = ObjectHelper.requireNonNullElseGet(form, DossierListForm::newForm);
        form.setRechercheES(true);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, form);
        context.putInContextData(
            SSContextDataKey.REQUETE_EXPERTE_DTO,
            UserSessionHelper.getUserSessionParameter(context, getDtoSessionKey(context), RequeteExperteDTO.class)
        );

        EpgRechercheUIService epgRechercheUIService = EpgUIServiceLocator.getEpgRechercheUIService();
        EpgDossierList lstResults = epgRechercheUIService.doRechercheExperte(context);

        String titre = StringUtils.defaultIfBlank(
            lstResults.getTitre(),
            ResourceHelper.getString("requete.experte.result.titre")
        );

        map.put(STTemplateConstants.RESULT_LIST, lstResults);

        form.setColumnVisibility(lstResults.getListeColonnes());
        map.put(STTemplateConstants.RESULT_FORM, form);
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());
        map.put(STTemplateConstants.TITRE, titre);
        map.put(
            STTemplateConstants.SOUS_TITRE,
            ResourceHelper.getString("requete.experte.result.nbResults", lstResults.getNbTotal())
        );
        map.put(
            EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS,
            context.getActions(EpgActionCategory.SELECTION_TOOL_DOSSIERS_ACTIONS)
        );
        map.put(
            EpgTemplateConstants.ADD_DOSSIERS_TO_FAVORIS_ACTIONS,
            context.getActions(EpgActionCategory.ADD_DOSSIERS_TO_FAVORIS_ACTIONS)
        );
        map.put(STTemplateConstants.DISPLAY_TABLE, lstResults.getNbTotal() > 0);
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/recherche/experte/resultats");
        map.put(STTemplateConstants.EXPORT_ACTION, context.getAction(EpgActionEnum.RECHERCHE_EXPERTE_DOSSIER_EXPORT));

        template.setData(map);
        UserSessionHelper.putUserSessionParameter(context, EpgUserSessionKey.DOSSIER_LIST_FORM, form);
        UserSessionHelper.putUserSessionParameter(context, getResultsSessionKey(context), map);

        return template;
    }

    @Path("exporter")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response exporter() {
        context.putInContextData(
            SSContextDataKey.REQUETE_EXPERTE_DTO,
            UserSessionHelper.getUserSessionParameter(context, getDtoSessionKey(context), RequeteExperteDTO.class)
        );

        SolonEpgUIServiceLocator.getSolonEpgDossierListUIService().exportDossiersRechercheExperte(context);
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Override
    protected String getContribName() {
        return EpgRechercheExperte.RECHERCHE_EXPERTE_CHAMP_CONTRIB_NAME;
    }

    @Override
    public String getSuffixForSessionKeys(SpecificContext context) {
        return EpgUIConstants.EPG_SUFFIX;
    }
}
