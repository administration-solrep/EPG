package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.habilitations;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.ui.bean.pan.TableauProgrammationHabilitationDTO;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanAbstractSousOngletContextuel;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanHabilitationsTableausuivi")
public class PanHabilitationsTableausuivi extends PanAbstractSousOngletContextuel {
    private static final ImmutableMap<String, String> AFFICHER_APPLIQUEES_OPTIONS = ImmutableMap.of(
        "true",
        "choix.oui",
        "false",
        "choix.non"
    );

    public PanHabilitationsTableausuivi() {
        super();
    }

    @Override
    public void loadSubtabContent() {
        TableauProgrammationHabilitationDTO tableauDeSuiviLoiDTO = getTableauProgrammationOuSuiviDTO(false);
        boolean afficherAppliquees = !(boolean) context.getFromContextData(PanContextDataKey.MASQUER_APPLIQUE);
        template.getData().put("afficherMesuresAppliquees", AFFICHER_APPLIQUEES_OPTIONS);
        template.getData().put(PanTemplateConstants.AFFICHER_APPLIQUEES, String.valueOf(afficherAppliquees));
        template.getData().put("tableSuiviDTO", tableauDeSuiviLoiDTO);
    }

    @POST
    public ThTemplate sortPaginateTable(@SwBeanParam LoisSuiviesForm sortPaginateForm) {
        context.putInContextData("form", sortPaginateForm);
        // call service and build template
        UserSessionHelper.putUserSessionParameter(context, PanUserSessionKey.TAB_SORT_PAGINATE_FORM, sortPaginateForm);
        // renvoyer juste le fragment avec le tableau à jour
        return template;
    }

    @Path("publier")
    @GET
    public ThTemplate publierTableauSuivi() {
        template.setName(CONTEXTUAL_BLOCK_TEMPLATE);
        TableauProgrammationHabilitationDTO tableauDeSuiviLoiDTO = getTableauProgrammationOuSuiviDTO(false);
        context.putInContextData(PanContextDataKey.TABLEAU_PROGRAMMATION_DTO, tableauDeSuiviLoiDTO);
        PanUIServiceLocator.getTableauProgrammation38CUIService().publierTableauSuiviHabilitation(context);
        getSubtabContent();
        return template;
    }

    private TableauProgrammationHabilitationDTO getTableauProgrammationOuSuiviDTO(boolean isTableauProgrammation) {
        ActiviteNormativeProgrammation activiteNormativeProgrammation = context
            .getCurrentDocument()
            .getAdapter(ActiviteNormativeProgrammation.class);
        TableauProgrammationHabilitationDTO tableauDeSuiviLoiDTO = new TableauProgrammationHabilitationDTO(
            activiteNormativeProgrammation,
            context.getSession(),
            isTableauProgrammation,
            context.getFromContextData(PanContextDataKey.MASQUER_APPLIQUE)
        );
        return tableauDeSuiviLoiDTO;
    }

    @Override
    public String getMyTemplateName() {
        return "fragments/pan/tableau-suivi";
    }
}
