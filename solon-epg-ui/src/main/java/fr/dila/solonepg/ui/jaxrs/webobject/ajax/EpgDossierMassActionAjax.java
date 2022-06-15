package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import fr.dila.solonepg.ui.bean.SelectionSummaryDTO;
import fr.dila.solonepg.ui.bean.SelectionSummaryList;
import fr.dila.solonepg.ui.bean.SubstitutionMassSummaryList;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgSelectionSummaryUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossierMassActionAjax")
public class EpgDossierMassActionAjax extends AbstractEpgMassActionAjax {
    private static final String DELETABLE_DOSSIERS_ID = "deletableDossiersId";

    private static final String AUTH_TEMPLATE_URL = "fragments/components/modal-suppression-dossiers-auth-content";
    private static final String CONFIRM_AND_AUTH_TEMPLATE_URL =
        "fragments/components/modal-suppression-dossiers-summary-content";
    private static final String NO_DELETABLE_DOS_TEMPLATE_URL =
        "fragments/components/modal-suppression-dossiers-no-deletable-dos-content";

    public EpgDossierMassActionAjax() {
        super();
    }

    @GET
    @Path("suppression")
    public ThTemplate showContentModalDeleteFolders(
        @SwRequired @QueryParam("idDossiers[]") List<String> idDossiers,
        @SwRequired @QueryParam("deleteUrlAjax") String deleteUrlAjax
    ) {
        ThTemplate template = new AjaxLayoutThTemplate();

        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idDossiers);
        context.putInContextData(EpgContextDataKey.IS_FROM_ADMINISTRATION, false);
        template.setContext(context);

        EpgSelectionSummaryUIService epgSelectionSummaryUIService = EpgUIServiceLocator.getEpgSelectionSummaryUIService();
        SelectionSummaryList selectionSummaryList = epgSelectionSummaryUIService.getSelectionSummaryList(context);

        template.setName(
            CollectionUtils.isNotEmpty(selectionSummaryList.getDeletableDossiers())
                ? (
                    CollectionUtils.isEmpty(selectionSummaryList.getIgnoredDossiers())
                        ? AUTH_TEMPLATE_URL
                        : CONFIRM_AND_AUTH_TEMPLATE_URL
                )
                : NO_DELETABLE_DOS_TEMPLATE_URL
        );

        Map<String, Object> map = new HashMap<>();
        map.put(
            EpgTemplateConstants.SHOW_SUMMARY,
            CollectionUtils.isNotEmpty(selectionSummaryList.getIgnoredDossiers())
        );
        map.put(EpgTemplateConstants.IGNORED_DOSSIERS, selectionSummaryList.getIgnoredDossiers());
        map.put(EpgTemplateConstants.DELETABLE_DOSSIERS, selectionSummaryList.getDeletableDossiers());
        map.put(EpgTemplateConstants.URL_DELETE_AJAX, deleteUrlAjax);
        UserSessionHelper.putUserSessionParameter(
            context,
            DELETABLE_DOSSIERS_ID,
            selectionSummaryList
                .getDeletableDossiers()
                .stream()
                .map(SelectionSummaryDTO::getId)
                .collect(Collectors.toList())
        );

        template.setData(map);

        return template;
    }

    @GET
    @Path("substitutionMass/modal")
    public ThTemplate showContentModalSubstitutionMass(
        @SwRequired @QueryParam("idDossiers[]") List<String> idDossiers
    ) {
        template = new AjaxLayoutThTemplate("fragments/components/modal-substitution-mass-content", context);
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idDossiers);

        SubstitutionMassSummaryList substitutionSummaryList = EpgUIServiceLocator
            .getEpgSelectionSummaryUIService()
            .getDossierSubstitutionSummaryList(context);

        Map<String, Object> map = new HashMap<>();
        map.put(EpgTemplateConstants.IGNORED_DOSSIERS, substitutionSummaryList.getIgnoredDossiers());
        map.put(EpgTemplateConstants.SUBSTITUABLE_DOSSIERS, substitutionSummaryList.getSubstituableDossiers());
        template.setData(map);
        return template;
    }

    @POST
    @Path("supprimer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDossiers(
        @SwRequired @FormParam("username") String username,
        @SwRequired @FormParam("data") String data
    ) {
        return doDeleteDossiers(username, data);
    }
}
