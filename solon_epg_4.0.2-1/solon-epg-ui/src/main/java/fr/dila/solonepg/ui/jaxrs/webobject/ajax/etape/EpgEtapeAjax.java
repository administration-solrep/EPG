package fr.dila.solonepg.ui.jaxrs.webobject.ajax.etape;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.ui.bean.EditionEtapeSqueletteDTO;
import fr.dila.solonepg.ui.bean.EpgFdrDTO;
import fr.dila.solonepg.ui.bean.squelette.EtapeSqueletteDTO;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.ui.bean.fdr.CreationEtapeDTO;
import fr.dila.ss.ui.bean.fdr.FdrTableDTO;
import fr.dila.ss.ui.jaxrs.webobject.ajax.etape.SSEtapeAjax;
import fr.dila.ss.ui.services.actions.SSActionsServiceLocator;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.th.model.AjaxJSONLayoutThTemplate;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.ControllerUtils;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AjaxEtape")
public class EpgEtapeAjax extends SSEtapeAjax {

    public EpgEtapeAjax() {
        super();
    }

    @Override
    public ThTemplate ajouterEtape(Long uniqueId, String fdrId) {
        context.putInContextData(STContextDataKey.ID, fdrId);
        return super.ajouterEtape(uniqueId, fdrId);
    }

    @Override
    @Path("saveEtape")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveEtape(
        @FormParam(DOSSIER_LINK_ID) String dossierLinkId,
        @SwBeanParam CreationEtapeDTO creationEtapeDTO
    ) {
        checkRightSaveEtape(dossierLinkId, creationEtapeDTO);

        EpgUIServiceLocator.getEpgFeuilleRouteUIService().addEtapes(context);

        handleErrors();

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Override
    @Path("charger")
    @POST
    public ThTemplate chargerEtape(@FormParam("stepId") String stepId) {
        ThTemplate template = super.chargerEtape(stepId);
        if (SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE.equals(context.getCurrentDocument().getType())) {
            template.getData().put(SSTemplateConstants.IS_SQUELETTE, true);
            template
                .getData()
                .put(
                    SSTemplateConstants.TYPE_DESTINATAIRE,
                    EpgUIServiceLocator.getEpgSelectValueUIService().getSqueletteTypeDestinataire()
                );
        }
        return template;
    }

    @GET
    @Path("ajouterEtapeSquelette")
    public ThTemplate ajouterEtapeSquelette(
        @QueryParam("uniqueId") String uniqueId,
        @QueryParam("fdrId") String fdrId
    ) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/fdr/etapeFdr", context);
        context.putInContextData(STContextDataKey.ID, fdrId);
        Map<String, Object> map = buildAjouterEtapeMap(uniqueId);
        map.put(
            SSTemplateConstants.TYPE_DESTINATAIRE,
            EpgUIServiceLocator.getEpgSelectValueUIService().getSqueletteTypeDestinataire()
        );
        map.put(SSTemplateConstants.IS_SQUELETTE, true);
        template.setData(map);

        return template;
    }

    @Path("editerSquelette")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response editerEtapeSquelette(
        @FormParam(DOSSIER_LINK_ID) String dossierLinkId,
        @SwBeanParam EditionEtapeSqueletteDTO editStep
    )
        throws IOException {
        ThTemplate template = new AjaxJSONLayoutThTemplate("fragments/fdr/colInfoEtapeFdr", context);

        context.putInContextData(EpgContextDataKey.EDIT_ETAPE_SQUELETTE_DTO, editStep);
        context.putInContextData(STContextDataKey.CURRENT_DOSSIER_LINK, dossierLinkId);

        SSActionsServiceLocator.getFeuilleRouteActionService().initRoutingActionDto(context, editStep.getStepId());
        verifyAction(EpgActionEnum.UPDATE_STEP_SQUELETTE, "editerSquelete");

        EtapeSqueletteDTO etapeDto = EpgUIServiceLocator.getEpgFeuilleRouteUIService().saveEtapeSquelette(context);

        etapeDto.setId(editStep.getStepId());
        FdrTableDTO fdrTableDto = new FdrTableDTO();
        EpgFdrDTO fdrDto = new EpgFdrDTO();
        fdrTableDto.setTotalNbLevel(editStep.getTotalNbLevel());
        fdrDto.setTable(fdrTableDto);
        fdrDto.buildColonnesFdrSquelette();
        template.getData().put(MAP_KEY_LINE, etapeDto);
        template.getData().put(DTO_KEY, fdrDto);
        template.getData().put(STATUS_INDEX_KEY, editStep.getLineIndex());
        template.getData().put(SSTemplateConstants.IS_SQUELETTE, true);

        return new JsonResponse(
            SolonStatus.OK,
            context.getMessageQueue(),
            ControllerUtils.renderHtmlFromTemplate(template)
        )
        .build();
    }

    @Override
    protected List<SelectValueDTO> getTypeEtapeAjout() {
        String idFdr = context.getFromContextData(STContextDataKey.ID);
        return EpgUIServiceLocator
            .getEpgSelectValueUIService()
            .getRoutingTaskTypesFilteredByIdFdr(context.getSession(), idFdr);
    }

    @GET
    @Path("ajouter/epreuve")
    public ThTemplate ajouterEpreuve(@SwId @SwRequired @QueryParam(FDR_ID) String fdrId) {
        context.setCurrentDocument(fdrId);
        context.putInContextData(STContextDataKey.ID, fdrId);
        ThTemplate template = new AjaxLayoutThTemplate("fragments/fdr/ajout-demande-epreuve", context);
        Map<String, Object> map = new HashMap<>();
        map.put(SSTemplateConstants.TYPE_ETAPE, getTypeEtapeAjout());
        map.put(SSTemplateConstants.BTN_DELETE_VISIBLE, false);
        map.put(EpgTemplateConstants.IS_DEMANDE_EPREUVE, true);
        map.put(SSTemplateConstants.PROFIL, context.getWebcontext().getPrincipal().getGroups());
        map.put(
            EpgTemplateConstants.ETAPES_DEMANDE_EPREUVE,
            EpgUIServiceLocator.getEpgFeuilleRouteUIService().getEtapesPourEpreuve(context)
        );
        template.setData(map);

        return template;
    }
}
