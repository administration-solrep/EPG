package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suivi.pan;

import fr.dila.solonepg.ui.bean.pan.PanStatistiquesParamDTO;
import fr.dila.solonepg.ui.enums.pan.PanActionEnum;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanStatistiques;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.model.bean.pan.PanStatistiquesParamForm;
import fr.dila.solonepg.ui.th.model.bean.pan.PanStatistiquesParamLegisEnCoursForm;
import fr.dila.solonepg.ui.th.model.bean.pan.PanStatistiquesParamLegisPrecForm;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanParametrageAjax")
public class PanParametrageAjax extends SolonWebObject {

    @GET
    public ThTemplate getParametrage() {
        verifyAction(PanActionEnum.PARAMETRAGE_STATISTIQUES, EpgURLConstants.URL_PAN_PARAM);
        template.setName("pages/admin/pan/panParametrageStatistiques");
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("menu.suivi.pan.parametrage.title"),
                EpgURLConstants.URL_PAN_PARAM,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );

        PanStatistiquesParamDTO dto = PanUIServiceLocator.getPanStatistiquesUIService().getStatParams(context);

        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        map.put(PanStatistiques.KEY_STAT_DTO, dto);
        template.setContext(context);
        template.setData(map);
        return template;
    }

    @POST
    @Path("editer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editerParametrage(
        @SwBeanParam PanStatistiquesParamForm legisForm,
        @SwBeanParam PanStatistiquesParamLegisEnCoursForm legisEnCoursForm,
        @SwBeanParam PanStatistiquesParamLegisPrecForm legisPrecForm
    ) {
        PanUIServiceLocator
            .getPanStatistiquesUIService()
            .updateStatParams(legisForm, legisEnCoursForm, legisPrecForm, context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }
}
