package fr.dila.solonepg.ui.jaxrs.webobject.ajax.archivage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgParametrageAdamantUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.ArchivageAdamantForm;
import fr.dila.solonepg.ui.th.bean.ParametreAdamantForm;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.enums.AlertType;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AdamantAjax")
public class EpgAdamantAjax extends SolonWebObject {

    @POST
    @Path("sauvegarde")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveParamAdamant(@SwBeanParam ParametreAdamantForm parametreAdamantForm) {
        context.putInContextData(EpgContextDataKey.PARAMETRE_ADAMANT_FORM, parametreAdamantForm);

        EpgParametrageAdamantUIService parametrageAdamantService = EpgUIServiceLocator.getParametrageAdamantUIService();
        parametrageAdamantService.save(context);

        context
            .getMessageQueue()
            .addToastSuccess(ResourceHelper.getString("sauvegarder.parametrage.adamant.success.toast")); // Toast en cas de succès
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @GET
    @Path("reinitialiser")
    public ThTemplate reinitParamAdamant() {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/archivage/parametreAdamantForm", context);

        ParametreAdamantForm parametreAdamantForm = EpgUIServiceLocator
            .getParametrageAdamantUIService()
            .getParametreAdamantDocument(context);

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.RESULT_FORM, parametreAdamantForm);
        template.setData(map);

        context
            .getMessageQueue()
            .addMessageToQueue(ResourceHelper.getString("annuler.parametrage.adamant.toast"), AlertType.TOAST_WARNING);

        return template;
    }

    @GET
    @Path("demande")
    public ThTemplate demandeExtraction(@SwBeanParam ArchivageAdamantForm form) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/archivage/modalArchivageAdamantMsg", context);

        Integer nbDocument = EpgUIServiceLocator
            .getEpgArchivageAdamantUIService()
            .countDossierEligible(
                context.getSession(),
                form.getStatut(),
                form.getIntervalleEligibiliteDebut().getTime(),
                form.getIntervalleEligibiliteFin().getTime()
            );

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();
        map.put("nbDocument", nbDocument);
        template.setData(map);

        return template;
    }

    @GET
    @Path("extraire")
    @Produces(MediaType.APPLICATION_JSON)
    public Response extraireDocument(@SwBeanParam ArchivageAdamantForm form) {
        EpgUIServiceLocator
            .getEpgArchivageAdamantUIService()
            .creerLotArchivage(
                context.getSession(),
                form.getStatut(),
                form.getIntervalleEligibiliteDebut().getTime(),
                form.getIntervalleEligibiliteFin().getTime()
            );

        context
            .getMessageQueue()
            .addToastSuccess(ResourceHelper.getString("archivage.adamant.demander.extraction.success.toast")); // Toast en cas de succès

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @GET
    @Path("suggestions")
    public String getSuggestions(@QueryParam("typeSelection") String typeSelection, @QueryParam("input") String input)
        throws JsonProcessingException {
        SpecificContext context = new SpecificContext();

        if (StringUtils.isNotBlank(typeSelection)) {
            context.putInContextData(STContextDataKey.TYPE_SELECTION, typeSelection);
        }
        if (StringUtils.isNotBlank(input)) {
            context.putInContextData(STContextDataKey.INPUT, input);
        }

        List<String> list = EpgUIServiceLocator.getParametrageAdamantUIService().getSuggestion(context);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(list);
    }
}
