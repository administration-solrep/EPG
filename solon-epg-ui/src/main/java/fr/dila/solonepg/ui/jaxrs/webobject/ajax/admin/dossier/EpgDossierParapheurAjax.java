package fr.dila.solonepg.ui.jaxrs.webobject.ajax.admin.dossier;

import avro.shaded.com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.ListParametreParapheurForm;
import fr.dila.solonepg.ui.th.bean.ParametreParapheurFormConsultation;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
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
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossiersParapheurAjax")
public class EpgDossierParapheurAjax extends SolonWebObject {
    private static final ImmutableMap<Boolean, String> ETAT_OPTIONS = ImmutableMap.of(
        Boolean.TRUE,
        "admin.parapheurs.etat.non.vide",
        Boolean.FALSE,
        "admin.parapheurs.etat.vide"
    );

    @GET
    public ThTemplate getFolderParapheur(@QueryParam("typeActe") String typeActe) {
        verifyAction(EpgActionEnum.ADMIN_MENU_DOSSIER_PARAPHEUR, "/ajax/admin/dossier/parapheur");
        context.putInContextData(EpgContextDataKey.TYPE_ACTE, typeActe);
        UserSessionHelper.putUserSessionParameter(context, EpgContextDataKey.TYPE_ACTE.getName(), typeActe);
        List<ParametreParapheurFormConsultation> lstResults = EpgUIServiceLocator
            .getEpgParametrageParapheurUIService()
            .getAllParametreParapheurDocument(context);
        ThTemplate template = new AjaxLayoutThTemplate(
            "fragments/dossier/parapheurs/parapheurs-content",
            getMyContext()
        );

        Map<String, Object> map = new HashMap<>();
        map.put("repertoires", lstResults);
        map.put("etatOptions", ETAT_OPTIONS);
        map.put("typeActe", typeActe);
        template.setData(map);
        return template;
    }

    @Path("enregistrer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@SwBeanParam ListParametreParapheurForm parapheursForm) {
        verifyAction(EpgActionEnum.RETIRER_DOSSIERS, "/ajax/admin/dossier/parapheur/enregistrer");
        context.putInContextData(
            EpgContextDataKey.PARAMETRE_PARAPHEUR_FORM_LIST,
            parapheursForm.getParametreParapheurFormCreations()
        );
        context.putInContextData(EpgContextDataKey.TYPE_ACTE, parapheursForm.getTypeActe());
        EpgUIServiceLocator.getEpgParametrageParapheurUIService().save(context);

        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            context
                .getMessageQueue()
                .addToastSuccess(
                    ResourceHelper.getString("admin.parapheurs.enregistrer.success", parapheursForm.getTypeActe())
                );
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }
}
