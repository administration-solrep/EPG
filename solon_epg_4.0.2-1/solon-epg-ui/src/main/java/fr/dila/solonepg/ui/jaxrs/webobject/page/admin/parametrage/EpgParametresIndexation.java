package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.parametrage;

import static fr.dila.solonepg.ui.th.constants.EpgURLConstants.URL_INDEXATION;

import fr.dila.solonepg.ui.bean.EpgIndexationMotCleListDTO;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.services.actions.EpgGestionIndexationActionService;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.ui.jaxrs.webobject.page.admin.parametres.SsParametres;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "ParametresIndexation")
public class EpgParametresIndexation extends SsParametres {
    public static final String INDEXATION_TITLE = "param.indexation.title";

    public EpgParametresIndexation() {
        super();
    }

    @GET
    public ThTemplate getIndexation() {
        verifyAction(EpgActionEnum.ADMIN_MENU_PARAM_PARAM_INDEXATION, URL_INDEXATION);
        template.setName("pages/admin/parametres/param-indexation");
        template.setContext(context);

        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString(INDEXATION_TITLE),
                URL_INDEXATION,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );

        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.TITLE, ResourceHelper.getString(INDEXATION_TITLE));
        EpgGestionIndexationActionService indexationActionService = SolonEpgActionServiceLocator.getEpgGestionIndexationActionService();
        map.put(EpgTemplateConstants.RUBRIQUES_LIST, indexationActionService.getListIndexationRubrique(context));
        map.put(EpgTemplateConstants.MOTS_CLES_LIST, indexationActionService.getListIndexationMotCle(context));
        map.put(EpgTemplateConstants.ADD_ACTION, context.getAction(EpgActionEnum.ADD_RUBRIQUE));
        map.put(EpgTemplateConstants.CREATE_LIST_ACTION, context.getAction(EpgActionEnum.CREATE_LIST_MOTS_CLES));
        map.put(EpgTemplateConstants.IS_REATTRIBUTION, false);
        map.put(
            SSTemplateConstants.IS_ADMIN_FONCTIONNEL,
            context.getSession().getPrincipal().isMemberOf("Administrateur fonctionnel")
        );

        template.setData(map);

        return template;
    }

    @GET
    @Path("liste/{listeId}")
    public ThTemplate getIndexationListe(@PathParam("listeId") String listeId) {
        template.setName("pages/admin/parametres/param-indexation-liste");
        template.setContext(context);
        context.setCurrentDocument(listeId);

        EpgIndexationMotCleListDTO liste = SolonEpgActionServiceLocator
            .getEpgGestionIndexationActionService()
            .getMotCleList(context);

        context.setNavigationContextTitle(
            new Breadcrumb(
                liste.getLabel(),
                URL_INDEXATION,
                Breadcrumb.TITLE_ORDER + 1,
                context.getWebcontext().getRequest()
            )
        );

        Map<String, Object> map = new HashMap<>();
        map.put(EpgTemplateConstants.DTO, liste);
        map.put(EpgTemplateConstants.ADD_ACTION, context.getAction(EpgActionEnum.ADD_MOTS_CLES));
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        template.setData(map);

        return template;
    }
}
