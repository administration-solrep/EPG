package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.habilitations;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreLoiDTOImpl;
import fr.dila.solonepg.ui.bean.pan.HabilitationsPanUnsortedList;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.lois.PanLoisTextemaitre;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.POST;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanHabilitationsTextemaitre")
public class PanHabilitationsTextemaitre extends PanLoisTextemaitre {

    public PanHabilitationsTextemaitre() {
        super();
    }

    @Override
    public void loadSpecificTexteMaitreContent() {
        context.putInContextData(PanContextDataKey.FIRST_TABLE_TYPE, "habilitation");
        HabilitationsPanUnsortedList habilitationsList = PanUIServiceLocator
            .getTexteMaitreHabilitationUIService()
            .getListHabilitation(context);
        template.getData().put(PanTemplateConstants.FIRST_TABLE_LIST, habilitationsList);

        ActiviteNormative activiteNormative = context.getCurrentDocument().getAdapter(ActiviteNormative.class);
        TexteMaitreLoiDTOImpl texteMaitreDTO = new TexteMaitreLoiDTOImpl(activiteNormative);
        template.getData().put(PanTemplateConstants.TEXTE_MAITRE, texteMaitreDTO);

        template
            .getData()
            .put(
                "legislaturePublicationList",
                PanUIServiceLocator.getPanUIService().getLegislaturesPublicationValues(context)
            );
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
        return "PanOrdonnanceHabilitationAjax";
    }
}
