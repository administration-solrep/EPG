package fr.dila.solonepg.ui.jaxrs.webobject.ajax.archivage;

import fr.dila.ss.ui.bean.parametres.ParametreList;
import fr.dila.ss.ui.services.SSParametreUIService;
import fr.dila.ss.ui.services.SSUIServiceLocator;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
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

@WebObject(type = "ArchivageAjax")
public class EpgArchivageAjax extends SolonWebObject {
    public static final String TITLE = "Paramètres des règles d'archivage";
    private static final String DATA_AJAX_URL = "/ajax/archivage";
    private static final String DATA_URL = "/admin/archivage/parametre";

    @GET
    public ThTemplate getTableParamReglesArchivage(@SwBeanParam PaginationForm listForm) {
        Map<String, Object> mapData = new HashMap<>();

        template.setContext(context);

        context.setNavigationContextTitle(
            new Breadcrumb(TITLE, DATA_URL, Breadcrumb.TITLE_ORDER, context.getWebcontext().getRequest())
        );

        if (listForm == null) {
            listForm = new PaginationForm();
        }

        context.putInContextData("listForm", listForm);

        SSParametreUIService paramService = SSUIServiceLocator.getParametreUIService();

        ParametreList listResult = paramService.getParametresArchive(context);

        listResult.buildColonnes();

        mapData.put(STTemplateConstants.PAGE, listForm.getPage());
        mapData.put(STTemplateConstants.TITLE, TITLE);
        mapData.put(SSTemplateConstants.PARAM_CONTEXT, "technique");
        mapData.put(STTemplateConstants.DATA_AJAX_URL, DATA_AJAX_URL);
        mapData.put(STTemplateConstants.DATA_URL, DATA_URL);
        mapData.put(STTemplateConstants.RESULT_FORM, listForm);
        mapData.put(STTemplateConstants.RESULT_LIST, listResult);

        template.setContext(context);
        template.setData(mapData);

        return template;
    }

    @Path("adamant")
    public Object doAdamant() {
        return newObject("AdamantAjax");
    }

    @Path("intermediaire")
    public Object doArchivageIntermediaire() {
        return newObject("ArchivageIntermediaireAjax");
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/table/tableParametres", context);
    }
}
