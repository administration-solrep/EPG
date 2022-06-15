package fr.dila.solonepg.ui.jaxrs.webobject.ajax.admin.parametres;

import static fr.dila.solonepg.ui.th.constants.EpgURLConstants.URL_MOTEUR_AJAX;
import static fr.dila.solonepg.ui.th.constants.EpgURLConstants.URL_MOTEUR_LIST;

import fr.dila.solonepg.api.service.ElasticParametrageService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.parametres.ParametreESList;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.ss.ui.jaxrs.webobject.ajax.SsParametresAjax;
import fr.dila.ss.ui.th.bean.ParametreForm;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.bean.PaginationForm;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.enums.AlertType;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "EpgParametresMoteurAjax")
public class EpgParametresMoteurAjax extends SsParametresAjax {
    private static final String TITLE = "Paramétrage du moteur de recherche";

    public EpgParametresMoteurAjax() {
        super();
    }

    @GET
    @Override
    public ThTemplate doRecherche(@SwBeanParam PaginationForm listForm) {
        Map<String, Object> mapData = new HashMap<>();

        context.setNavigationContextTitle(
            new Breadcrumb(TITLE, URL_MOTEUR_LIST, Breadcrumb.TITLE_ORDER, context.getWebcontext().getRequest())
        );

        if (listForm == null) {
            listForm = new PaginationForm();
        }

        ElasticParametrageService paramMoteurRechercheService = SolonEpgServiceLocator.getElasticParametrageService();
        List<DocumentModel> documentModelList = paramMoteurRechercheService.getESParametres(context.getSession());
        context.putInContextData(EpgContextDataKey.PARAMETRES, documentModelList);
        ParametreESList listResult = EpgUIServiceLocator.getElasticParametrageUIService().handleListResult(context);

        listResult.buildColonnes();

        mapData.put(STTemplateConstants.PAGE, listForm.getPage());
        mapData.put(STTemplateConstants.TITLE, TITLE);
        mapData.put(SSTemplateConstants.PARAM_CONTEXT, "moteur");
        mapData.put(STTemplateConstants.DATA_AJAX_URL, URL_MOTEUR_AJAX);
        mapData.put(STTemplateConstants.DATA_URL, URL_MOTEUR_LIST);
        mapData.put(STTemplateConstants.RESULT_FORM, listForm);
        mapData.put(STTemplateConstants.RESULT_LIST, listResult);

        template.setContext(context);
        template.setData(mapData);

        return template;
    }

    @POST
    @Path("modifier")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifierParametre(@SwBeanParam ParametreForm parametreForm) {
        context.setCurrentDocument(parametreForm.getId());

        verifyAction(
            EpgActionEnum.ADMIN_MENU_PARAM_PARAM_MOTEUR,
            String.format("/admin/param/moteur/%s/editer", context.getCurrentDocument().getTitle())
        );

        SolonEpgServiceLocator
            .getElasticParametrageService()
            .updateESParametre(context.getSession(), context.getCurrentDocument(), parametreForm.getValeur());
        context
            .getMessageQueue()
            .addMessageToQueue(ResourceHelper.getString("parametres.modifier.message.succes"), AlertType.TOAST_SUCCESS);
        UserSessionHelper.putUserSessionParameter(context, SpecificContext.MESSAGE_QUEUE, context.getMessageQueue());

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }
}
