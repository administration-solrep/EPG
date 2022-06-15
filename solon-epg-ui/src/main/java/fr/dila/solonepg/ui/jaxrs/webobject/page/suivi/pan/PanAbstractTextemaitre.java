package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan;

import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.ui.bean.pan.ConsultTexteMaitreDTO;
import fr.dila.solonepg.ui.bean.pan.PanActionsDTO;
import fr.dila.solonepg.ui.enums.pan.PanActionCategory;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Collections;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

public abstract class PanAbstractTextemaitre extends PanAbstractSousOngletContextuel {
    protected static final String ID_PREMIER_TABLEAU = "idPremierTableau";

    public PanAbstractTextemaitre() {
        super();
    }

    @Override
    public void loadSubtabContent() {
        // récupérer le sortPaginateForm depuis la session)
        // si different de null on le récupère et on le passe dans le context data, sinon new sortPaginateForm
        LoisSuiviesForm sortPaginateForm = UserSessionHelper.getUserSessionParameter(
            context,
            PanUserSessionKey.SECOND_TABLE
        );
        if (sortPaginateForm == null) {
            sortPaginateForm = new LoisSuiviesForm();
        }

        template.getData().put("firstTableForm", sortPaginateForm);

        loadSpecificTexteMaitreContent();

        TexteMaitre texteMaitre = context.getCurrentDocument().getAdapter(TexteMaitre.class);
        template.getData().put("firstTableActions", context.getActions(PanActionCategory.FIRST_TABLE_ACTIONS));
        template.getData().put("firstTableLineActions", context.getActions(PanActionCategory.FIRST_TABLE_LINE_ACTIONS));
        String typePremierTableau = context.getFromContextData(PanContextDataKey.FIRST_TABLE_TYPE);
        template.getData().put("firstTableType", typePremierTableau + "s");
        template
            .getData()
            .put(
                "firstTableCaption",
                ResourceHelper.getString(
                    "pan.application." +
                    context.getFromContextData(PanContextDataKey.CURRENT_SECTION) +
                    "." +
                    typePremierTableau +
                    "s.title",
                    texteMaitre.getNumero()
                )
            );
        template.getData().put("firstTableLineTemplate", "ligne-" + typePremierTableau);

        if (StringUtils.isNotBlank(texteMaitre.getMinisterePilote())) {
            Map<String, String> mapMinisterePilote = Collections.singletonMap(
                texteMaitre.getMinisterePilote(),
                StringUtils.join(
                    texteMaitre.getMinisterePiloteLabel(),
                    " - ",
                    STServiceLocator
                        .getOrganigrammeService()
                        .getOrganigrammeNodeById(texteMaitre.getMinisterePilote(), OrganigrammeType.MINISTERE)
                        .getLabel()
                )
            );
            template.getData().put("mapMinisterePilote", mapMinisterePilote);
        }
        template.getData().put("isFirstTable", true);
        template
            .getData()
            .put(
                PanTemplateConstants.PROFIL_ONLY_MINISTERIEL,
                (
                    (PanActionsDTO) context.getFromContextData(PanContextDataKey.PAN_ACTION_DTO)
                ).getIsOnlyUtilisateurMinistereLoiOrOrdonnance()
            );
    }

    public abstract void loadSpecificTexteMaitreContent();

    @POST
    public ThTemplate sortPaginateTable(@SwBeanParam LoisSuiviesForm sortPaginateForm) {
        context.putInContextData(PanContextDataKey.PAN_FORM, sortPaginateForm);
        String idPremierTableau = UserSessionHelper.getUserSessionParameter(
            context,
            PanTemplateConstants.ID_FIRST_TABLE
        );
        template.getData().put(PanTemplateConstants.ID_FIRST_TABLE, idPremierTableau);
        // call service and build template
        UserSessionHelper.putUserSessionParameter(context, PanUserSessionKey.SECOND_TABLE, sortPaginateForm);
        template.setName("fragments/pan/texte-maitre-premier-tableau");

        // renvoyer juste le fragment avec le tabxleau à jour
        return getSubtabContent();
    }

    @Path("verrouiller")
    public Object verrouilleTexteMaitre(@FormParam(ID_PREMIER_TABLEAU) String idPremierTableau) {
        verifyMinisterOrUpdaterAccess(
            context.getSession(),
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION_ENUM)
        );
        PanUIServiceLocator.getPanUIService().lockCurrentDocument(context);
        return chargerBlocContextuel(idPremierTableau);
    }

    @Path("deverrouiller")
    public Object deverrouilleTexteMaitre(@FormParam(ID_PREMIER_TABLEAU) String idPremierTableau) {
        PanUIServiceLocator.getPanUIService().unlockCurrentDocument(context);
        return chargerBlocContextuel(idPremierTableau);
    }

    protected Object chargerBlocContextuel(String idPremierTableau) {
        template.setName(CONTEXTUAL_BLOCK_TEMPLATE);
        if (
            StringUtils.isNotBlank(idPremierTableau) &&
            (
                PanUIServiceLocator.getPanUIService().isInApplicationDesLoisOrOrdonnances(context) ||
                PanUIServiceLocator.getPanUIService().isInOrdonnances38C(context)
            )
        ) {
            return getDetail(idPremierTableau);
        } else {
            template = getSubtabContent();
            return newObject("PanAjax", context, template);
        }
    }

    protected Response sauvegarderTexteMaitre() {
        AbstractMapDTO texteMaitreDTO = getTexteMaitreDto(context);
        context.putInContextData(PanContextDataKey.TEXTE_MAITRE_DTO, texteMaitreDTO);
        saveTexteMaitre(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    protected abstract AbstractMapDTO getTexteMaitreDto(SpecificContext context);

    protected abstract void saveTexteMaitre(SpecificContext context);

    @Path("{idPremierTableau}")
    public Object getDetail(@PathParam(ID_PREMIER_TABLEAU) String idPremierTableau) {
        context.removeNavigationContextTitle();
        template.setContext(context);
        if (
            !(template instanceof AjaxLayoutThTemplate) ||
            StringUtils.equals(template.getName(), CONTEXTUAL_BLOCK_TEMPLATE)
        ) {
            template = getSubtabContent();
        } else {
            template.setName("fragments/pan/texte-maitre-second-tableau");
        }

        String url = buildUrl() + "/" + idPremierTableau;
        template.getData().put(PanTemplateConstants.SECOND_TABLE_DATA_URL, url);
        template.getData().put(PanTemplateConstants.SECOND_TABLE_DATA_AJAX_URL, "/ajax" + url);
        template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        context.putInContextData(PanContextDataKey.FIRST_TABLE_ID, idPremierTableau);
        template.getData().put(PanTemplateConstants.ID_FIRST_TABLE, idPremierTableau);
        UserSessionHelper.putUserSessionParameter(context, PanTemplateConstants.ID_FIRST_TABLE, idPremierTableau);

        ConsultTexteMaitreDTO texteMaitreDTO = PanUIServiceLocator.getPanUIService().getConsultTexteMaitreDTO(context);
        template.getData().put(PanTemplateConstants.CONSULT_TEXTE_MAITRE_DTO, texteMaitreDTO);
        context.putInContextData(PanContextDataKey.CONSULT_TEXTEM_DTO, texteMaitreDTO);

        return newObject(getSecondTableauAjaxWebObject(), context, template);
    }

    public abstract String getSecondTableauAjaxWebObject();

    @Override
    public String getMyTemplateName() {
        return "fragments/pan/texte-maitre";
    }
}
