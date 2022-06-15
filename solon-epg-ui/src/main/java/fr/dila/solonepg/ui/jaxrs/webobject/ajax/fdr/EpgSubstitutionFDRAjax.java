package fr.dila.solonepg.ui.jaxrs.webobject.ajax.fdr;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.ui.bean.dossier.bordereau.BordereauSaveForm;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgModeleFdrListUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.solonepg.ui.th.bean.SubstitutionModeleListForm;
import fr.dila.ss.api.criteria.SubstitutionCriteria;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.enums.SSUserSessionKey;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwLength;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "SubstitutionFDRAjax")
public class EpgSubstitutionFDRAjax extends SolonWebObject {

    @POST
    @Path("liste")
    public ThTemplate getListModeleSubstitution(@SwBeanParam SubstitutionModeleListForm form) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/table/tableModelesFDR", context);
        DocumentModel dossierDoc = context.getCurrentDocument();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        SubstitutionCriteria criteria = new SubstitutionCriteria();
        criteria.setTypeActe(form.getIsFiltreTypeActe() ? dossier.getTypeActe() : "");
        criteria.setListIdMinistereAttributaire(
            form.getIsFiltreMinistere()
                ? Collections.singletonList(dossier.getMinistereResp())
                : Collections.emptyList()
        );
        context.putInContextData(SSContextDataKey.SUBSTITUTION_CRITERIA, criteria);
        context.putInContextData(SSContextDataKey.LIST_MODELE_FDR, form);

        UserSessionHelper.putUserSessionParameter(context, SSUserSessionKey.MODELE_FDR_LIST_FORM, form);
        UserSessionHelper.putUserSessionParameter(context, SSUserSessionKey.SUBSTITUTION_CRITERIA, criteria);

        // Récupération de la liste des modèles disponnible pour la substitution
        EpgModeleFdrListUIService modeleFDRListUIService = EpgUIServiceLocator.getEpgModeleFdrListUIService();
        ModeleFDRList lstResults = modeleFDRListUIService.getModelesFDRSubstitution(context);

        // Je renvoie mon formulaire en sortie
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.RESULT_FORM, form);
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.ID_DOSSIER, dossierDoc.getId());
        map.put(STTemplateConstants.DATA_URL, "/dossier/" + dossierDoc.getId() + "/substitution/liste");
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/dossier/" + dossierDoc.getId() + "/substitution/liste");
        template.setData(map);
        return template;
    }

    @POST
    @Path("/valider")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveBordereauAndValidateSubstitution(
        @SwRequired @SwId @FormParam("dossierId") String id,
        @SwRequired @FormParam("idModele") String idModele,
        @SwBeanParam BordereauSaveForm form
    ) {
        context.setCurrentDocument(id);
        context.putInContextData(SSContextDataKey.ID_MODELE, idModele);
        context.putInContextData(EpgContextDataKey.BORDEREAU_SAVE_FORM, form);

        EpgUIServiceLocator.getBordereauUIService().saveBordereau(context);
        SolonEpgActionServiceLocator.getEpgDossierDistributionActionService().substituerRoute(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Path("/copie/fdr/nor")
    @Produces(MediaType.APPLICATION_JSON)
    public Response copieFdrFromNor(@SwRequired @SwLength(max = 12) @FormParam("norToCopie") String norToCopie) {
        context.putInContextData(EpgContextDataKey.NOR_TO_COPIE, norToCopie);

        SolonEpgActionServiceLocator.getEpgDossierDistributionActionService().copierFdrDossier(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Path("/reset/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetFilter(@SwRequired @SwId @FormParam("dossierId") String id) {
        context.setCurrentDocument(id);

        UserSessionHelper.putUserSessionParameter(context, SSUserSessionKey.MODELE_FDR_LIST_FORM, null);
        UserSessionHelper.putUserSessionParameter(context, SSUserSessionKey.SUBSTITUTION_CRITERIA, null);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }
}
