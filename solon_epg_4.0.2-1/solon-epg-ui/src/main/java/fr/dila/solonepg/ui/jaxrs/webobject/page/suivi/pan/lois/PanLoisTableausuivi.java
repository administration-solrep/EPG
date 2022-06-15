package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.lois;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.api.exception.WsBdjException;
import fr.dila.solonepg.ui.bean.pan.TableauProgrammationOuSuiviDTO;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanAbstractSousOngletContextuel;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.ThTemplate;
import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanLoisTableausuivi")
public class PanLoisTableausuivi extends PanAbstractSousOngletContextuel {
    private static final ImmutableMap<String, String> AFFICHER_APPLIQUEES_OPTIONS = ImmutableMap.of(
        "true",
        "choix.oui",
        "false",
        "choix.non"
    );

    public PanLoisTableausuivi() {
        super();
    }

    @Override
    public void loadSubtabContent() {
        TableauProgrammationOuSuiviDTO tableauDeSuiviLoiDTO = getTableauProgrammationOuSuiviDTO(false);
        boolean afficherAppliquees = !(boolean) context.getFromContextData(PanContextDataKey.MASQUER_APPLIQUE);
        template.getData().put("afficherMesuresAppliquees", AFFICHER_APPLIQUEES_OPTIONS);
        template.getData().put(PanTemplateConstants.AFFICHER_APPLIQUEES, String.valueOf(afficherAppliquees));
        template.getData().put("tableSuiviDTO", tableauDeSuiviLoiDTO);
    }

    @POST
    public ThTemplate sortPaginateTable(@SwBeanParam LoisSuiviesForm sortPaginateForm) {
        context.putInContextData(PanContextDataKey.PAN_FORM, sortPaginateForm);
        // call service and build template
        UserSessionHelper.putUserSessionParameter(context, PanUserSessionKey.TAB_SORT_PAGINATE_FORM, sortPaginateForm);
        // renvoyer juste le fragment avec le tableau à jour
        return template;
    }

    @Path("sauvegarder")
    @GET
    public ThTemplate sauvegarderTableauSuivi() {
        template.setName(CONTEXTUAL_BLOCK_TEMPLATE);
        TableauProgrammationOuSuiviDTO tableauDeSuiviLoiDTO = getTableauProgrammationOuSuiviDTO(true);
        context.putInContextData(PanContextDataKey.TABLEAU_PROGRAMMATION_DTO, tableauDeSuiviLoiDTO);
        PanUIServiceLocator.getTableauProgrammationUIService().sauvegarderTableauSuivi(context);
        getSubtabContent();
        return template;
    }

    @Path("publier")
    @GET
    public ThTemplate publierTableauSuivi()
        throws WsBdjException, IOException, FactoryConfigurationError, XMLStreamException {
        template.setName(CONTEXTUAL_BLOCK_TEMPLATE);
        TableauProgrammationOuSuiviDTO tableauDeSuiviLoiDTO = getTableauProgrammationOuSuiviDTO(true);
        context.putInContextData(PanContextDataKey.TABLEAU_PROGRAMMATION_DTO, tableauDeSuiviLoiDTO);
        PanUIServiceLocator.getTableauProgrammationUIService().publierTableauSuivi(context);
        getSubtabContent();
        return template;
    }

    @Path("afficher")
    @GET
    public ThTemplate afficherTableauSuivi(@QueryParam("afficher") String afficher) {
        template.setName(CONTEXTUAL_BLOCK_TEMPLATE);
        context.putInContextData(PanContextDataKey.MASQUER_APPLIQUE, !Boolean.parseBoolean(afficher));
        getSubtabContent();
        return template;
    }

    private TableauProgrammationOuSuiviDTO getTableauProgrammationOuSuiviDTO(boolean isTableauProgrammation) {
        ActiviteNormativeProgrammation activiteNormativeProgrammation = context
            .getCurrentDocument()
            .getAdapter(ActiviteNormativeProgrammation.class);
        return new TableauProgrammationOuSuiviDTO(
            activiteNormativeProgrammation,
            context.getSession(),
            isTableauProgrammation,
            context.getFromContextData(PanContextDataKey.MASQUER_APPLIQUE),
            "Loi",
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION)
        );
    }

    @Override
    public String getMyTemplateName() {
        return "fragments/pan/tableau-suivi";
    }
}
