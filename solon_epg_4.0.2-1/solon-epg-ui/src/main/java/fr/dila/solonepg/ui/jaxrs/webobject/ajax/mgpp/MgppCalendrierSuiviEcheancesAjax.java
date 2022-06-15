package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import static fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory.MGPP_SUIVI_ECHEANCES_ACTIONS;

import fr.dila.solonepg.ui.bean.MgppSuiviEcheancesList;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.MgppSuiviEcheancesListForm;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "CalendrierSuiviEcheancesAjax")
public class MgppCalendrierSuiviEcheancesAjax extends SolonWebObject {

    public MgppCalendrierSuiviEcheancesAjax() {
        super();
    }

    @GET
    public ThTemplate getHome() {
        return getListeSuivi(null);
    }

    @POST
    @Path("liste")
    public ThTemplate getListeSuivi(@FormParam("search") String json) {
        template.setContext(context);
        MgppSuiviEcheancesListForm suiviForm = new MgppSuiviEcheancesListForm(json);

        context.putInContextData(MgppContextDataKey.SUIVI_ECHEANCES_LIST_FORM, suiviForm);

        MgppSuiviEcheancesList lstResults = MgppUIServiceLocator
            .getMgppCalendrierUIService()
            .getSuiviEcheances(context);

        template.getData().put(STTemplateConstants.RESULT_LIST, lstResults);
        template.getData().put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes(suiviForm));
        template.getData().put(STTemplateConstants.NB_RESULTS, lstResults.getNbTotal());
        template.getData().put(STTemplateConstants.RESULT_FORM, suiviForm);
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, "/ajax/mgpp/calendrier/suivi/liste");
        template.getData().put(SSTemplateConstants.PRINT_ACTIONS, context.getActions(MGPP_SUIVI_ECHEANCES_ACTIONS));
        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/table/tableSuiviEcheances", getMyContext());
    }
}
