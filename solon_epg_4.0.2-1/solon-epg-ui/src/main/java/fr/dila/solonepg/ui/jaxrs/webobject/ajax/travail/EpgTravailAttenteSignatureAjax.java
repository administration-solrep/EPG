package fr.dila.solonepg.ui.jaxrs.webobject.ajax.travail;

import fr.dila.solonepg.ui.bean.AttenteSignatureDTO;
import fr.dila.solonepg.ui.bean.AttenteSignatureList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgOngletAttenteSignatureEnum;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.AttenteSignatureForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.bean.PaginationForm;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.ControllerUtils;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AttenteSignatureAjax")
public class EpgTravailAttenteSignatureAjax extends SolonWebObject {
    private static final String ATTENTE_SIGNATURE_TABLE_PAGINATE_FORM_KEY = "ATTENTE_SIGNATURE_TABLE_PAGINATE_FORM_KEY";
    private static final String IDS = "ids[]";

    public EpgTravailAttenteSignatureAjax() {
        super();
    }

    @GET
    public ThTemplate doHome(@PathParam("tab") String tab, @SwBeanParam PaginationForm form) {
        return getAttenteSignatureTab(tab, form);
    }

    @GET
    @Path("{tab}")
    public ThTemplate getAttenteSignatureTab(@PathParam("tab") String tab, @SwBeanParam PaginationForm form) {
        form = ObjectHelper.requireNonNullElseGet(form, PaginationForm::new);
        UserSessionHelper.putUserSessionParameter(context, ATTENTE_SIGNATURE_TABLE_PAGINATE_FORM_KEY, form);
        EpgOngletAttenteSignatureEnum onglet = EpgOngletAttenteSignatureEnum.valueOf(tab);

        context.putInContextData(STContextDataKey.PAGINATION_FORM, form);
        context.putInContextData(EpgContextDataKey.TYPE_ATTENTE_SIGNATURE, onglet.getId());
        AttenteSignatureList lstResults = EpgUIServiceLocator
            .getEpgAttenteSignatureUIService()
            .getTextesEnAttenteSignature(context);

        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes(onglet));
        map.put(STTemplateConstants.RESULT_FORM, form);
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.NB_RESULTS, lstResults.getNbTotal());
        map.put(
            STTemplateConstants.GENERALE_ACTIONS,
            context.getActions(EpgActionCategory.ATTENTE_SIGNATURE_GENERAL_ACTIONS)
        );
        map.put(
            SSTemplateConstants.PRINT_ACTIONS,
            context.getActions(EpgActionCategory.ATTENTE_SIGNATURE_PRINT_ACTIONS)
        );
        map.put(
            EpgTemplateConstants.LINE_ACTIONS,
            context.getActions(EpgActionCategory.ATTENTE_SIGNATURE_LINE_ACTIONS)
        );
        map.put(STTemplateConstants.DATA_URL, "/travail/attenteSignature/" + tab);
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/travail/attenteSignature/" + tab);
        map.put(EpgTemplateConstants.CURRENT_TAB, tab);
        template.getData().putAll(map);

        return template;
    }

    @GET
    @Path("charger/{texteId}")
    public ThTemplate chargerTexteAttenteSignature(
        @PathParam("texteId") String texteId,
        @SwRequired @QueryParam("currentTab") String currentTab
    ) {
        template.setName("fragments/travail/attenteSignatureEdit");
        List<SelectValueDTO> vecteursPublicationList = EpgUIServiceLocator
            .getEpgSelectValueUIService()
            .getVecteurPublicationTS();

        context.setCurrentDocument(texteId);
        context.putInContextData(
            EpgContextDataKey.TYPE_ATTENTE_SIGNATURE,
            EpgOngletAttenteSignatureEnum.valueOf(currentTab).getId()
        );
        AttenteSignatureDTO attenteSignatureDTO = EpgUIServiceLocator
            .getEpgAttenteSignatureUIService()
            .getAttenteSignatureDTOFromDossier(context);

        HashMap<String, Object> map = new HashMap<>();
        map.put("item", attenteSignatureDTO);
        map.put(EpgTemplateConstants.CURRENT_TAB, currentTab);
        map.put("vecteursPublicationList", vecteursPublicationList);
        template.setData(map);

        return template;
    }

    @POST
    @Path("{tab}/editer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editerTexteAttenteSignature(
        @PathParam("tab") String tab,
        @SwBeanParam AttenteSignatureForm attenteSignatureForm
    )
        throws IOException {
        context.putInContextData(EpgContextDataKey.ATTENTE_SIGNATURE_FORM, attenteSignatureForm);
        context.putInContextData(
            EpgContextDataKey.TYPE_ATTENTE_SIGNATURE,
            EpgOngletAttenteSignatureEnum.valueOf(tab).getId()
        );
        EpgUIServiceLocator.getEpgAttenteSignatureUIService().saveTexteAttenteSignature(context);

        template =
            getAttenteSignatureTab(
                tab,
                UserSessionHelper.getUserSessionParameter(context, ATTENTE_SIGNATURE_TABLE_PAGINATE_FORM_KEY)
            );
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put(EpgTemplateConstants.TEMPLATE, ControllerUtils.renderHtmlFromTemplate(template));

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue(), (Serializable) jsonData).build();
    }

    @POST
    @Path("{tab}/retirer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retirerDossiersAttenteSignature(
        @PathParam("tab") String tab,
        @SwRequired @FormParam(IDS) List<String> ids
    )
        throws IOException {
        context.putInContextData(STContextDataKey.IDS, ids);
        EpgUIServiceLocator.getEpgAttenteSignatureUIService().retirerTextesAttenteSignature(context);

        template =
            getAttenteSignatureTab(
                tab,
                UserSessionHelper.getUserSessionParameter(context, ATTENTE_SIGNATURE_TABLE_PAGINATE_FORM_KEY)
            );
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put(EpgTemplateConstants.TEMPLATE, ControllerUtils.renderHtmlFromTemplate(template));

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue(), (Serializable) jsonData).build();
    }

    @POST
    @Path("ajouter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response ajouterDossierAttenteSignature(
        @SwRequired @FormParam("dossierId") String dossierId,
        @FormParam("type") String type
    ) {
        context.setCurrentDocument(dossierId);
        if (StringUtils.isNotBlank(type)) {
            EpgOngletAttenteSignatureEnum onglet = EpgOngletAttenteSignatureEnum.valueOf(type);
            context.putInContextData(EpgContextDataKey.TYPE_ATTENTE_SIGNATURE, onglet.getId());
        }

        EpgUIServiceLocator.getEpgAttenteSignatureUIService().ajouterDossierDansAttenteSignature(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/table/table-attente-signature", getMyContext());
    }
}
