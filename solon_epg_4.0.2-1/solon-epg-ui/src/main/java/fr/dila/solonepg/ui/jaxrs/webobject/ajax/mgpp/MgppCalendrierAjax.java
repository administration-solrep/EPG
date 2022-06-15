package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import static fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory.MGPP_ECH_PROMULGATION_ACTIONS;

import fr.dila.solonepg.ui.bean.MgppEcheancierPromulgationList;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.bean.PaginationForm;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "CalendrierAjax")
public class MgppCalendrierAjax extends SolonWebObject {

    public MgppCalendrierAjax() {
        super();
    }

    @GET
    public ThTemplate getHome() {
        return getEcheancierPromulgation(null);
    }

    @GET
    @Path("promulgation")
    public ThTemplate getEcheancierPromulgation(@SwBeanParam PaginationForm resultForm) {
        template.setContext(context);
        if (resultForm == null) {
            resultForm = new PaginationForm();
        }
        context.putInContextData(STContextDataKey.PAGINATION_FORM, resultForm);

        MgppEcheancierPromulgationList lstResults = MgppUIServiceLocator
            .getMgppCalendrierUIService()
            .getEcheancierPromulgation(context);

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.NB_RESULTS, lstResults.getNbTotal());
        map.put(STTemplateConstants.RESULT_FORM, resultForm);
        map.put(STTemplateConstants.DATA_URL, "/mgpp/calendrier/promulgation");
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/mgpp/calendrier/promulgation");
        map.put(SSTemplateConstants.PRINT_ACTIONS, context.getActions(MGPP_ECH_PROMULGATION_ACTIONS));
        template.setData(map);
        return template;
    }

    @Path("suivi")
    public Object getCalendrierSuivi() {
        return newObject("CalendrierSuiviEcheancesAjax", context);
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/table/tableEcheancierPromulgation", getMyContext());
    }
}
