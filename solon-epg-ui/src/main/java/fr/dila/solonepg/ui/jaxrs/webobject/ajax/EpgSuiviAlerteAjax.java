package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import fr.dila.solonepg.ui.bean.recherchelibre.CritereRechercheForm;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.ss.ui.bean.AlerteForm;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.jaxrs.webobject.ajax.SSSuiviAlerteAjax;
import fr.dila.st.ui.annot.SwBeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AjaxAlertes")
public class EpgSuiviAlerteAjax extends SSSuiviAlerteAjax {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("sauvegarder")
    public Response sauvegarderAlerte(
        @SwBeanParam AlerteForm form,
        @SwBeanParam CritereRechercheForm critereForm,
        @SwBeanParam DossierListForm dossierListForm,
        @FormParam("sessionDtoKey") String sessionDtoKey
    ) {
        context.setCurrentDocument(form.getId());

        context.putInContextData(SSContextDataKey.ALERTE_FORM, form);
        context.putInContextData(EpgContextDataKey.CRITERE_RECHERCHE_FORM, critereForm);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierListForm);
        context.putInContextData(EpgContextDataKey.RECHERCHE_EXPERTE_KEY, sessionDtoKey);

        EpgUIServiceLocator.getEpgAlerteUIService().saveAlert(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }
}
