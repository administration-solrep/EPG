package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.dossier;

import fr.dila.solonepg.ui.bean.dossier.tablesreference.AdminModeParutionDTO;
import fr.dila.solonepg.ui.bean.dossier.tablesreference.AdminTableReferenceDTO;
import fr.dila.solonepg.ui.bean.dossier.tablesreference.EpgIndexationSignataireDTO;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.constants.STURLConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AdminDossiersTableReference")
public class EpgDossierTableReference extends SolonWebObject {

    @GET
    public ThTemplate getHome() {
        verifyAction(
            EpgActionEnum.ADMIN_MENU_DOSSIER_TABLE_REFERENCE,
            EpgURLConstants.URL_ADMIN_DOSSIERS_TABLE_REFERENCE
        );

        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("menu.admin.dossier.table.reference.title"),
                EpgURLConstants.URL_ADMIN_DOSSIERS_TABLE_REFERENCE + STURLConstants.MAIN_CONTENT_ID,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
        template.setName("pages/admin/dossier/table-reference");
        Map<String, Object> map = template.getData();
        AdminTableReferenceDTO dto = EpgUIServiceLocator.getEpgDossierTablesReferenceUIService().getReferences(context);
        map.put(EpgTemplateConstants.DTO, dto);
        List<AdminModeParutionDTO> lModeParution = EpgUIServiceLocator
            .getEpgDossierTablesReferenceUIService()
            .getListModeParution(context);
        map.put(EpgTemplateConstants.LST_MODE_PARUTION, lModeParution);
        map.put(EpgTemplateConstants.NB_MODE_PARUTION, lModeParution.size());
        map.put(STTemplateConstants.TABLE_ACTIONS, context.getActions(EpgActionCategory.MODE_PARUTION_TABLE_ACTIONS));
        map.put(
            EpgTemplateConstants.ADD_ACTION_MODE_PARUTION,
            context.getActions(EpgActionCategory.MODE_PARUTION_ADD_ACTIONS)
        );
        // Signataire libre
        map.put(EpgTemplateConstants.ADD_ACTION, context.getAction(EpgActionEnum.ADD_SIGNATAIRE));

        List<EpgIndexationSignataireDTO> lSignataireLibre = dto.getSignatairesLibres() == null
            ? new ArrayList<>()
            : dto
                .getSignatairesLibres()
                .stream()
                .map(
                    nomSignataire ->
                        new EpgIndexationSignataireDTO(
                            nomSignataire,
                            nomSignataire,
                            context.getAction(EpgActionEnum.REMOVE_SIGNATAIRE)
                        )
                )
                .collect(Collectors.toList());
        map.put(STTemplateConstants.RESULT_LIST, lSignataireLibre);

        if (UserSessionHelper.getUserSessionParameter(context, EpgUserSessionKey.LIST_SIGNATAIRE_LIBRE) != null) {
            UserSessionHelper.clearUserSessionParameter(context, EpgUserSessionKey.LIST_SIGNATAIRE_LIBRE);
        }

        if (CollectionUtils.isNotEmpty(context.getMessageQueue().getSuccessQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgAdminTemplate();
    }
}
