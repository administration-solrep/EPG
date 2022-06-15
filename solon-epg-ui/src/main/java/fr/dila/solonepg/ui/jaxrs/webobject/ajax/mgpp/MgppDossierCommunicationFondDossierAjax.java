package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import static fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory.MGPP_FDD_FICHE_ACTIONS;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.COMMUNICATION_ID;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.DOSSIERS_PARLEMENTAIRES_SELECTED;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.FICHE_ID;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.LST_DOCUMENTS;
import static fr.dila.solonepg.ui.th.constants.MgppTemplateConstants.MGPP_FDD_ACTIONS;
import static java.util.Optional.ofNullable;

import fr.dila.solonepg.ui.bean.MgppMessageDTO;
import fr.dila.solonepg.ui.enums.mgpp.MgppUserSessionKey;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.st.ui.bean.DocumentDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossierCommunicationFondDossierAjax")
public class MgppDossierCommunicationFondDossierAjax extends SolonWebObject {

    public MgppDossierCommunicationFondDossierAjax() {
        super();
    }

    @GET
    public ThTemplate getFondDossier(@PathParam("id") String id) {
        Map<String, Object> map = new HashMap<>();
        context.putInContextData(FICHE_ID, getCurrentFicheId(id));
        List<DocumentDTO> lstDocuments = MgppUIServiceLocator
            .getMgppFondDossierUIService()
            .getFondDeDossierDocuments(context);
        map.put(LST_DOCUMENTS, lstDocuments);
        context.putInContextData(
            DOSSIERS_PARLEMENTAIRES_SELECTED,
            UserSessionHelper.getUserSessionParameter(context, MgppUserSessionKey.DOSSIER_PARLEMENTAIRE)
        );
        map.put(MGPP_FDD_ACTIONS, context.getActions(MGPP_FDD_FICHE_ACTIONS));
        template.getData().putAll(map);
        return template;
    }

    private String getCurrentFicheId(String id) {
        context.putInContextData(COMMUNICATION_ID, id);
        MgppMessageDTO message = MgppUIServiceLocator.getMgppDossierUIService().getCurrentMessageDTO(context);
        context.putInContextData(
            STContextDataKey.DOSSIER_ID,
            Optional.ofNullable(message).map(MgppMessageDTO::getIdDossier).orElse(id)
        );
        DocumentModel currentFicheDoc = MgppUIServiceLocator.getMgppFicheUIService().getFicheForDossier(context);
        return ofNullable(currentFicheDoc).map(DocumentModel::getId).orElse("");
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/mgpp/dossier/onglets/fondDossier", context);
    }
}
