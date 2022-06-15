package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.organigramme;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.ss.api.client.InjectionGvtDTO;
import fr.dila.ss.ui.enums.SSActionEnum;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.pages.admin.STOrganigramme;
import fr.dila.st.ui.jaxrs.webobject.pages.admin.organigramme.STOrganigrammeGouvernement;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "OrganigrammeGouvernement")
public class EPGOrganigrammeGouvernement extends STOrganigrammeGouvernement {
    private static final String MAP_KEY_INJECTIONS = "injections";
    private static final String MAP_KEY_INJECTIONS_OLD = "injectionsOld";
    private static final String MAP_KEY_INJECTIONS_NEW = "injectionsNew";

    public EPGOrganigrammeGouvernement() {
        super();
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgAdminTemplate();
    }

    @Path("import/epg")
    @GET
    public Object importerGouvernementEpg() {
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("ss.organigramme.injection.import"),
                "/admin/organigramme/gouvernement/import",
                STOrganigramme.ORGANIGRAMME_ORDER + 1
            )
        );
        template.setName("pages/admin/organigramme/importGouvernementEPG");

        return mapTemplate(
            context.getAction(SSActionEnum.CONFIRM_IMPORT_GOUVERNEMENT),
            context.getAction(EpgActionEnum.INJECTION_GOUV_EXPORT)
        );
    }

    @Path("import/epp")
    @GET
    public Object importerGouvernementEpp() {
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("ss.organigramme.injection.import"),
                "/admin/organigramme/gouvernement/import",
                STOrganigramme.ORGANIGRAMME_ORDER + 1
            )
        );
        template.setName("pages/admin/organigramme/importGouvernementEPP");

        return mapTemplate(context.getAction(EpgActionEnum.CONFIRM_IMPORT_GOUVERNEMENT_EPP), null);
    }

    private Object mapTemplate(Action action, Action export) {
        List<InjectionGvtDTO> dtos = UserSessionHelper.getUserSessionParameter(context, EpgUserSessionKey.INJECTIONS);
        List<InjectionGvtDTO> oldGvt = UserSessionHelper.getUserSessionParameter(
            context,
            EpgUserSessionKey.MAP_KEY_INJECTIONS_OLD
        );
        List<InjectionGvtDTO> newGvt = UserSessionHelper.getUserSessionParameter(
            context,
            EpgUserSessionKey.MAP_KEY_INJECTIONS_NEW
        );

        if (CollectionUtils.isEmpty(dtos) || oldGvt == null || newGvt == null) {
            return handleImportError(ImmutableList.of(ResourceHelper.getString("gouvernement.import.data.empty")));
        }

        Map<String, Object> map = new HashMap<>();
        map.put(MAP_KEY_INJECTIONS, dtos);
        map.put(MAP_KEY_INJECTIONS_OLD, oldGvt);
        map.put(MAP_KEY_INJECTIONS_NEW, newGvt);
        List<String> injectionModifications = UserSessionHelper.getUserSessionParameter(
            context,
            EpgUserSessionKey.INJECTIONS_MODIFICATIONS
        );
        map.put(EpgTemplateConstants.MODIFICATIONS, injectionModifications);
        map.put(STTemplateConstants.ACTION, action);
        if (export != null) {
            map.put(STTemplateConstants.EXPORT_ACTION, context.getAction(EpgActionEnum.INJECTION_GOUV_EXPORT));
        }
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        template.setData(map);
        return template;
    }

    private Response handleImportError(List<String> messages) {
        messages.forEach(m -> context.getMessageQueue().addErrorToQueue(m));
        UserSessionHelper.putUserSessionParameter(context, SpecificContext.MESSAGE_QUEUE, context.getMessageQueue());
        return redirect(context.getUrlPreviousPage());
    }
}
