package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.habilitations;

import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.ui.bean.pan.TableauProgrammationHabilitationDTO;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanAbstractSousOngletContextuel;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanHabilitationsTableauprogrammation")
public class PanHabilitationsTableauprogrammation extends PanAbstractSousOngletContextuel {

    public PanHabilitationsTableauprogrammation() {
        super();
    }

    @Override
    public void loadSubtabContent() {
        TableauProgrammationHabilitationDTO tableauDeProgrammationDTO = getTableauProgrammationDTO();
        template.getData().put("tableProgDTO", tableauDeProgrammationDTO);
    }

    private TableauProgrammationHabilitationDTO getTableauProgrammationDTO() {
        ActiviteNormativeProgrammation activiteNormativeProgrammation = context
            .getCurrentDocument()
            .getAdapter(ActiviteNormativeProgrammation.class);
        TableauProgrammationHabilitationDTO tableauDeProgrammationDTO = new TableauProgrammationHabilitationDTO(
            activiteNormativeProgrammation,
            context.getSession(),
            true,
            context.getFromContextData(PanContextDataKey.MASQUER_APPLIQUE)
        );
        return tableauDeProgrammationDTO;
    }

    @Path("figer")
    @GET
    public ThTemplate figerTableauProgrammation() {
        template.setName(CONTEXTUAL_BLOCK_TEMPLATE);
        TableauProgrammationHabilitationDTO tableauDeProgrammationDTO = getTableauProgrammationDTO();
        context.putInContextData(PanContextDataKey.TABLEAU_PROGRAMMATION_DTO, tableauDeProgrammationDTO);
        PanUIServiceLocator.getTableauProgrammation38CUIService().lockCurrentProgrammationHabilitation(context);
        getSubtabContent();
        return template;
    }

    @Path("defiger")
    @GET
    public ThTemplate defigerTableauProgrammation() {
        template.setName(CONTEXTUAL_BLOCK_TEMPLATE);
        TableauProgrammationHabilitationDTO tableauDeProgrammationDTO = getTableauProgrammationDTO();
        context.putInContextData(PanContextDataKey.TABLEAU_PROGRAMMATION_DTO, tableauDeProgrammationDTO);
        PanUIServiceLocator.getTableauProgrammation38CUIService().unlockCurrentProgrammationHabilitation(context);
        getSubtabContent();
        return template;
    }

    @Override
    public String getMyTemplateName() {
        return "fragments/pan/tableau-programmation";
    }
}
