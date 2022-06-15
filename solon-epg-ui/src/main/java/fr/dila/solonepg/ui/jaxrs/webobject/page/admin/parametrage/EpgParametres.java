package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.parametrage;

import static fr.dila.solonepg.ui.th.constants.EpgURLConstants.URL_MOTEUR_CONSULT;
import static fr.dila.solonepg.ui.th.constants.EpgURLConstants.URL_MOTEUR_EDIT;
import static fr.dila.solonepg.ui.th.constants.EpgURLConstants.URL_MOTEUR_LIST;
import static fr.dila.solonepg.ui.th.constants.EpgURLConstants.URL_PARAM_PARAM;

import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.parametres.ParametreESDTO;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.ParametreApplicationForm;
import fr.dila.ss.ui.enums.SSActionCategory;
import fr.dila.ss.ui.jaxrs.webobject.page.admin.parametres.SsParametres;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "Parametres")
public class EpgParametres extends SsParametres {

    public EpgParametres() {
        super();
    }

    @Path("moteur")
    public Object listerParametresMoteur() {
        verifyAction(EpgActionEnum.ADMIN_MENU_PARAM_PARAM_MOTEUR, URL_MOTEUR_LIST);

        Map<String, Object> mapData = new HashMap<>();

        template.setContext(context);
        template.setData(mapData);
        template.setName(PARAMETRE_LIST);

        return newObject("EpgParametresMoteurAjax", context, template);
    }

    @Path("moteur/{name}")
    @GET
    public Object consulterParametreMoteur(@PathParam("name") String name) {
        verifyAction(EpgActionEnum.ADMIN_MENU_PARAM_PARAM_MOTEUR, String.format(URL_MOTEUR_CONSULT, name));

        Map<String, Object> mapData = new HashMap<>();

        ParametreESDTO param = MapDoc2Bean.docToBean(
            SolonEpgServiceLocator
                .getElasticParametrageService()
                .getParametre(context.getSession(), name)
                .getDocument(),
            ParametreESDTO.class
        );

        context.setNavigationContextTitle(
            new Breadcrumb(param.getTitre(), String.format(URL_MOTEUR_CONSULT, name), Breadcrumb.TITLE_ORDER + 2)
        );

        mapData.put(SSTemplateConstants.PARAMETRE, param);
        mapData.put(SSTemplateConstants.PARAM_CONTEXT, "moteur");
        mapData.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());

        mapData.put(STTemplateConstants.EDIT_ACTIONS, context.getActions(SSActionCategory.ADMIN_EDIT_PARAMETER));

        template.setContext(context);
        template.setData(mapData);
        template.setName(PARAMETRE_CONSULT);

        return template;
    }

    @Path("moteur/{name}/editer")
    @GET
    public Object editerParametreMoteur(@PathParam("name") String name) {
        verifyAction(EpgActionEnum.ADMIN_MENU_PARAM_PARAM_MOTEUR, String.format(URL_MOTEUR_EDIT, name));

        Map<String, Object> mapData = new HashMap<>();

        ParametreESDTO param = MapDoc2Bean.docToBean(
            SolonEpgServiceLocator
                .getElasticParametrageService()
                .getParametre(context.getSession(), name)
                .getDocument(),
            ParametreESDTO.class
        );

        context.setNavigationContextTitle(
            new Breadcrumb("Modifier", String.format(URL_MOTEUR_EDIT, name), Breadcrumb.TITLE_ORDER + 3)
        );

        mapData.put(SSTemplateConstants.PARAMETRE, param);
        mapData.put(SSTemplateConstants.EDIT_URL, "/ajax/admin/paramMoteur/modifier");
        mapData.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());

        template.setName(PARAMETRE_EDIT);
        template.setContext(context);
        template.setData(mapData);

        return template;
    }

    @GET
    @Path("param")
    public ThTemplate getParametresPage() {
        verifyAction(EpgActionEnum.ADMIN_MENU_PARAM_PARAM, URL_PARAM_PARAM);
        template.setContext(context);
        template.setName("pages/admin/parametres/param-form");

        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("menu.admin.param.param.title"),
                "admin/param/param#main_content",
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );

        ParametreApplicationForm parametresApplicationForm = EpgUIServiceLocator
            .getParametreApplicationUIService()
            .getParametreApplication(context);
        template.getData().put("paramAppliForm", parametresApplicationForm);
        return template;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("enregistrer")
    public Response enregistrerParametres(@SwBeanParam ParametreApplicationForm parametresApplicationForm) {
        verifyAction(EpgActionEnum.ADMIN_MENU_PARAM_PARAM, "/admin/param/enregistrer");
        context.putInContextData(EpgContextDataKey.PARAMETRE_APPLICATION_FORM, parametresApplicationForm);
        EpgUIServiceLocator.getParametreApplicationUIService().save(context);

        UserSessionHelper.putUserSessionParameter(context, SpecificContext.MESSAGE_QUEUE, context.getMessageQueue());
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("pan")
    public Object doPanParametrage() {
        return newObject("PanParametrageAjax", context, template);
    }

    @Path("indexation")
    public Object doParametresIndexation() {
        return newObject("ParametresIndexation", context, template);
    }
}
