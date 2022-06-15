package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.lois;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreLoiDTOImpl;
import fr.dila.solonepg.core.exception.ActiviteNormativeException;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.MesuresApplicativesPanUnsortedList;
import fr.dila.solonepg.ui.enums.pan.PanActionEnum;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanAbstractTextemaitre;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.th.model.bean.pan.TexteMaitreLoiForm;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanLoisTextemaitre")
public class PanLoisTextemaitre extends PanAbstractTextemaitre {
    private static final STLogger LOGGER = STLogFactory.getLog(PanLoisTextemaitre.class);

    public PanLoisTextemaitre() {
        super();
    }

    @Override
    public void loadSpecificTexteMaitreContent() {
        context.putInContextData(PanContextDataKey.FIRST_TABLE_TYPE, "mesure");
        MesuresApplicativesPanUnsortedList mesuresList = PanUIServiceLocator.getPanUIService().getMesures(context);
        Map<String, Object> templateData = template.getData();
        templateData.put(PanTemplateConstants.FIRST_TABLE_LIST, mesuresList);

        ActiviteNormative activiteNormative = context.getCurrentDocument().getAdapter(ActiviteNormative.class);
        TexteMaitreLoiDTOImpl texteMaitreDTO = new TexteMaitreLoiDTOImpl(activiteNormative);

        // Override du label ministère court
        String ministerePilote = activiteNormative.getAdapter(TexteMaitre.class).getMinisterePilote();
        OrganigrammeNode organigrammeNodeById = SolonEpgServiceLocator
            .getEpgOrganigrammeService()
            .getOrganigrammeNodeById(ministerePilote, OrganigrammeType.MINISTERE);
        texteMaitreDTO.setMinisterePiloteLabel(
            texteMaitreDTO.getMinisterePiloteLabel() + " - " + organigrammeNodeById.getLabel()
        );

        templateData.put(PanTemplateConstants.TEXTE_MAITRE, texteMaitreDTO);

        templateData.put("natureTexteList", EpgUIServiceLocator.getEpgSelectValueUIService().getNatureTexte());
        templateData.put("procedureVoteList", EpgUIServiceLocator.getEpgSelectValueUIService().getProcedureVote());
        templateData.put(
            "legislaturePublicationList",
            PanUIServiceLocator.getPanUIService().getLegislaturesPublicationValues(context)
        );
        templateData.put(
            PanTemplateConstants.TYPE_MESURES_LIST,
            EpgUIServiceLocator.getEpgSelectValueUIService().getTypeMesures()
        );
        templateData.put(
            PanTemplateConstants.POLE_CHARGE_MISSION_LIST,
            EpgUIServiceLocator.getEpgSelectValueUIService().getPoleChargeMission()
        );
        templateData.put(
            PanTemplateConstants.RESPONSABLE_AMENDEMENT_LIST,
            EpgUIServiceLocator.getEpgSelectValueUIService().getResponsableAmendement()
        );

        templateData.put("masquerColonnesMesureAction", context.getAction(PanActionEnum.HIDE_COLUMNS_MESURES));
    }

    @Override
    public String getSecondTableauAjaxWebObject() {
        return "PanDecretAjax";
    }

    @Path("sauvegarder")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response sauvegarderTexteMaitre(@SwBeanParam TexteMaitreLoiForm texteMaitreFormDTO) {
        verifyMinisterOrUpdaterAccess(
            context.getSession(),
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION_ENUM)
        );
        context.putInContextData(PanContextDataKey.TEXTE_MAITRE_FORM, texteMaitreFormDTO);
        try {
            return sauvegarderTexteMaitre();
        } catch (ActiviteNormativeException e) {
            LOGGER.error(context.getSession(), EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC, e);
            context.getMessageQueue().addErrorToQueue(e.getMessage());
            return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
        }
    }

    @Override
    protected void saveTexteMaitre(SpecificContext context) {
        PanUIServiceLocator.getTexteMaitreUIService().saveTexteMaitre(context);
    }

    @Override
    protected AbstractMapDTO getTexteMaitreDto(SpecificContext context) {
        ActiviteNormative activiteNormative = context.getCurrentDocument().getAdapter(ActiviteNormative.class);
        return new TexteMaitreLoiDTOImpl(activiteNormative);
    }

    @Path("valider/decrets")
    public Object validerDecrets(@SwRequired @FormParam(ID_PREMIER_TABLEAU) String idPremierTableau) {
        SolonEpgServiceLocator
            .getActiviteNormativeService()
            .validerDecrets(
                idPremierTableau,
                context.getFromContextData(PanContextDataKey.TEXTE_MAITRE_ID),
                context.getSession()
            );
        template.setName(CONTEXTUAL_BLOCK_TEMPLATE);
        return getDetail(idPremierTableau);
    }
}
