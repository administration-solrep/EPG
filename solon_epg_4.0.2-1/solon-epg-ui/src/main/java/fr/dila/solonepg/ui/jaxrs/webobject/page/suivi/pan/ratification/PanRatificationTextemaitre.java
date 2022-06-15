package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.ratification;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.core.dto.activitenormative.OrdonnanceDTOImpl;
import fr.dila.solonepg.ui.bean.pan.LoisDeRatificationPanUnsortedList;
import fr.dila.solonepg.ui.enums.pan.PanActionEnum;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanAbstractTextemaitre;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.th.model.bean.pan.OrdonnanceForm;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanRatificationTextemaitre")
public class PanRatificationTextemaitre extends PanAbstractTextemaitre {
    private static final ImmutableMap<String, String> FONDEMENT_CONSTITUTIONNEL_VALUES = ImmutableMap.of(
        "true",
        "Article 38 C",
        "false",
        "Article 74-1"
    );

    public PanRatificationTextemaitre() {
        super();
    }

    @Override
    public void loadSpecificTexteMaitreContent() {
        context.putInContextData(PanContextDataKey.FIRST_TABLE_TYPE, "loiRatification");

        LoisDeRatificationPanUnsortedList loisRatificationList = PanUIServiceLocator
            .getTexteMaitreOrdonnanceUIService()
            .getLoiDeRatificationList(context);
        template.getData().put(PanTemplateConstants.FIRST_TABLE_LIST, loisRatificationList);

        ActiviteNormative activiteNormative = context.getCurrentDocument().getAdapter(ActiviteNormative.class);
        OrdonnanceDTOImpl texteMaitreDTO = new OrdonnanceDTOImpl(activiteNormative, context.getSession());
        template.getData().put(PanTemplateConstants.TEXTE_MAITRE, texteMaitreDTO);

        template.getData().put(PanTemplateConstants.NOR_TYPE, "loi");
        template
            .getData()
            .put(
                PanTemplateConstants.AJOUTER_PREMIER_TABLE_NOR_ACTION,
                context.getAction(PanActionEnum.ADD_ELT_PREMIER_TABLEAU_NOR)
            );

        template.getData().put("fondementConstitutionnelValues", FONDEMENT_CONSTITUTIONNEL_VALUES);
    }

    @Override
    @POST
    public ThTemplate sortPaginateTable(@SwBeanParam LoisSuiviesForm sortPaginateForm) {
        context.putInContextData("form", sortPaginateForm);
        // call service and build template
        UserSessionHelper.putUserSessionParameter(context, PanUserSessionKey.SECOND_TABLE, sortPaginateForm);
        // renvoyer juste le fragment avec le tableau à jour
        return template;
    }

    @Override
    public String getSecondTableauAjaxWebObject() {
        return "PanDecretApplicationAjax";
    }

    @Path("sauvegarder")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response sauvegarderTexteMaitre(@SwBeanParam OrdonnanceForm texteMaitreFormDTO) {
        context.putInContextData(PanContextDataKey.TEXTE_MAITRE_FORM, texteMaitreFormDTO);
        return sauvegarderTexteMaitre();
    }

    @Override
    protected void saveTexteMaitre(SpecificContext context) {
        PanUIServiceLocator.getTexteMaitreOrdonnanceUIService().saveOrdonnance(context);
    }

    @Override
    protected AbstractMapDTO getTexteMaitreDto(SpecificContext context) {
        ActiviteNormative activiteNormative = context.getCurrentDocument().getAdapter(ActiviteNormative.class);
        return new OrdonnanceDTOImpl(activiteNormative, context.getSession());
    }
}
