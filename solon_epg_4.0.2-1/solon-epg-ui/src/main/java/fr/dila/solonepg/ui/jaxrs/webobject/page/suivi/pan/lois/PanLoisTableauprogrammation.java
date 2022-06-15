package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.lois;

import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.ui.bean.pan.TableauProgrammationOuSuiviDTO;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanAbstractSousOngletContextuel;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanLoisTableauprogrammation")
public class PanLoisTableauprogrammation extends PanAbstractSousOngletContextuel {
    private static final String TABLEAU_PROGRAMMATION_TEMPLATE = "fragments/pan/tableau-programmation";

    public PanLoisTableauprogrammation() {
        super();
    }

    @Override
    public void loadSubtabContent() {
        TableauProgrammationOuSuiviDTO tableauDeProgrammationDTO = getTableauProgrammationDTO();
        template.getData().put("tableProgDTO", tableauDeProgrammationDTO);
    }

    @POST
    public ThTemplate sortPaginateTable(@SwBeanParam LoisSuiviesForm sortPaginateForm) {
        context.putInContextData(PanContextDataKey.PAN_FORM, sortPaginateForm);
        // call service and build template
        UserSessionHelper.putUserSessionParameter(context, PanUserSessionKey.TAB_SORT_PAGINATE_FORM, sortPaginateForm);
        // renvoyer juste le fragment avec le tableau à jour
        return template;
    }

    @Path("figer")
    @GET
    public ThTemplate figerTableauProgrammation() {
        template.setName(CONTEXTUAL_BLOCK_TEMPLATE);
        TableauProgrammationOuSuiviDTO tableauDeProgrammationDTO = getTableauProgrammationDTO();
        context.putInContextData(PanContextDataKey.TABLEAU_PROGRAMMATION_DTO, tableauDeProgrammationDTO);
        PanUIServiceLocator.getTableauProgrammationUIService().lockCurrentProgrammationLoi(context);
        getSubtabContent();
        return template;
    }

    @Path("defiger")
    @GET
    public ThTemplate defigerTableauProgrammation() {
        template.setName(CONTEXTUAL_BLOCK_TEMPLATE);
        TableauProgrammationOuSuiviDTO tableauDeProgrammationDTO = getTableauProgrammationDTO();
        context.putInContextData(PanContextDataKey.TABLEAU_PROGRAMMATION_DTO, tableauDeProgrammationDTO);
        PanUIServiceLocator.getTableauProgrammationUIService().unlockCurrentProgrammationLoi(context);
        getSubtabContent();
        return template;
    }

    private TableauProgrammationOuSuiviDTO getTableauProgrammationDTO() {
        ActiviteNormativeProgrammation activiteNormativeProgrammation = context
            .getCurrentDocument()
            .getAdapter(ActiviteNormativeProgrammation.class);
        TableauProgrammationOuSuiviDTO tableauDeProgrammationDTO = new TableauProgrammationOuSuiviDTO(
            activiteNormativeProgrammation,
            context.getSession(),
            true,
            context.getFromContextData(PanContextDataKey.MASQUER_APPLIQUE),
            "Loi",
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION)
        );
        return tableauDeProgrammationDTO;
    }

    @Override
    public String getMyTemplateName() {
        return TABLEAU_PROGRAMMATION_TEMPLATE;
    }
}
