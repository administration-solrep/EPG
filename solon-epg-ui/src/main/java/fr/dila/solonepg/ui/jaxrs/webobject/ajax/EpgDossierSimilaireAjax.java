package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getDossiersSimilairesUIService;
import static fr.dila.st.core.query.QueryHelper.getDocument;

import fr.dila.solonepg.ui.bean.dossier.bordereau.EpgBordereauDTO;
import fr.dila.solonepg.ui.bean.dossier.bordereau.EpgDossierSimilaireBordereauDTO;
import fr.dila.solonepg.ui.bean.dossier.similaire.DossierSimilaireDTO;
import fr.dila.solonepg.ui.bean.dossier.similaire.DossierSimilaireList;
import fr.dila.solonepg.ui.bean.dossier.similaire.DossierSimilaireListForm;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossiersimilaireAjax")
public class EpgDossierSimilaireAjax extends SolonWebObject {
    private static final String DOSSIER_ID = "dossierId";
    private static final String DOSSIER_SOURCE_ID = "dossierSourceId";
    private static final String CURRENT_INDEX = "indexCourant";
    private static final String MAP_KEY_BORDEREAU = "bordereauDto";

    public EpgDossierSimilaireAjax() {
        super();
    }

    @Path("chercher")
    @POST
    public ThTemplate searchSimilarDirectory(@SwBeanParam DossierSimilaireListForm resultForm) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/table/table-dossiers-similaires", context);

        context.putInContextData(EpgContextDataKey.DOSSIER_SIMILAIRE_FORM, resultForm);
        DossierSimilaireList dossierSimilaireList = EpgUIServiceLocator
            .getDossiersSimilairesUIService()
            .getDossiersSimilaires(context);

        dossierSimilaireList.buildColonnes(resultForm);

        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.RESULT_LIST, dossierSimilaireList);
        map.put(STTemplateConstants.LST_COLONNES, dossierSimilaireList.getListeColonnes());
        map.put(STTemplateConstants.LST_SORTED_COLONNES, dossierSimilaireList.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, dossierSimilaireList.getListeSortableAndVisibleColonnes());
        map.put(STTemplateConstants.RESULT_FORM, resultForm);
        map.put(
            STTemplateConstants.DATA_URL,
            String.format("/dossiersimilaire?dossierId=%s#main_content", resultForm.getDossierId())
        );
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/dossiersimilaire/chercher");
        template.setData(map);
        return template;
    }

    @Path("bordereau/suivant")
    @POST
    public ThTemplate nextBordereau(
        @SwId @FormParam(DOSSIER_ID) String dossierID,
        @SwRequired @FormParam(CURRENT_INDEX) Long indexCourant
    ) {
        context.putInContextData(EpgContextDataKey.CURRENT_INDEX, indexCourant);
        DossierSimilaireDTO dto = getDossiersSimilairesUIService().getNextEntry(context);
        String idDossier = dto.getIdDossier();
        context.setCurrentDocument(idDossier);
        EpgDossierSimilaireBordereauDTO bordereauDTO = EpgUIServiceLocator
            .getDossiersSimilairesUIService()
            .getCurrentBordereau(context);

        return mapBordereau(bordereauDTO, "" + dto.getIndexCourant(), idDossier);
    }

    @Path("bordereau/precedent")
    @POST
    public ThTemplate previousBordereau(
        @SwId @FormParam(DOSSIER_ID) String dossierID,
        @SwRequired @FormParam(CURRENT_INDEX) Long indexCourant
    ) {
        context.putInContextData(EpgContextDataKey.CURRENT_INDEX, indexCourant);

        DossierSimilaireDTO dto = getDossiersSimilairesUIService().getPreviousEntry(context);
        String idDossier = dto.getIdDossier();
        context.setCurrentDocument(idDossier);
        EpgDossierSimilaireBordereauDTO bordereauDTO = EpgUIServiceLocator
            .getDossiersSimilairesUIService()
            .getCurrentBordereau(context);

        return mapBordereau(bordereauDTO, "" + dto.getIndexCourant(), idDossier);
    }

    @Path("copier/{dossierSourceId}/{dossierId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response copyFields(
        @PathParam(DOSSIER_SOURCE_ID) String dossierSourceId,
        @PathParam(DOSSIER_ID) String dossierId,
        @FormParam("champs[]") List<String> champsACopier
    ) {
        context.putInContextData(DOSSIER_ID, dossierId);
        context.putInContextData(DOSSIER_SOURCE_ID, dossierSourceId);

        CoreSession session = context.getSession();
        DocumentModel dossierSource = getDocument(session, dossierSourceId);
        DocumentModel dossier = getDocument(session, dossierId);
        champsACopier.stream().forEach(xpath -> dossierSource.setPropertyValue(xpath, dossier.getPropertyValue(xpath)));
        session.saveDocument(dossierSource);

        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            context.getMessageQueue().addToastSuccess(ResourceHelper.getString("dossier.similaire.copie.success"));
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    private ThTemplate mapBordereau(EpgBordereauDTO bordereauDTO, String currentIndex, String idDossier) {
        ThTemplate template = new AjaxLayoutThTemplate();
        template.setName("fragments/dossier/onglets/bordereau");
        Map<String, Object> map = new HashMap<>();
        map.put(MAP_KEY_BORDEREAU, bordereauDTO);
        map.put(STTemplateConstants.EDIT_ACTIONS, context.getActions(EpgActionCategory.DOSSIER_SIMILAIRE_BORDEREAU));
        map.put(DOSSIER_ID, idDossier);
        map.put(CURRENT_INDEX, currentIndex);
        template.setData(map);
        return template;
    }
}
