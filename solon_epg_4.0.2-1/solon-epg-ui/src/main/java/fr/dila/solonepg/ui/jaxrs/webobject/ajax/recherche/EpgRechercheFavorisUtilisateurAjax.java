package fr.dila.solonepg.ui.jaxrs.webobject.ajax.recherche;

import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.recherche.favoris.AbstractEpgRechercheFavorisUtilisateur;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.EpgUserListForm;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.bean.UserSearchForm;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "RechercheFavorisUtilisateurAjax")
public class EpgRechercheFavorisUtilisateurAjax extends AbstractEpgRechercheFavorisUtilisateur {

    public EpgRechercheFavorisUtilisateurAjax() {
        super();
    }

    @POST
    public ThTemplate sortFavorisUtilisateur(@SwBeanParam EpgUserListForm userListForm) {
        template = new AjaxLayoutThTemplate("fragments/table/tableUsers", context);
        template.setData(getTemplateData(userListForm));
        return template;
    }

    @POST
    @Path("supprimer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response supprimer(@SwRequired @FormParam("ids[]") List<String> ids) {
        context.putInContextData(EpgContextDataKey.LIST_FAVORIS, ids);

        EpgUIServiceLocator.getEpgFavorisConsultationUIService().removeFavorisConsultationUser(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ajouter")
    public Response ajouter(@FormParam("userIds[]") List<String> userIds) {
        context.putInContextData(EpgContextDataKey.LIST_FAVORIS, userIds);
        EpgUIServiceLocator.getEpgFavorisConsultationUIService().addUtilisateursToFavorisConsultation(context);
        UserSessionHelper.putUserSessionParameter(context, SpecificContext.MESSAGE_QUEUE, context.getMessageQueue());

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("enregistrer/criteres")
    public Response saveCritereRecherche(@SwBeanParam UserSearchForm userSearchform) {
        userSearchform = ObjectHelper.requireNonNullElseGet(userSearchform, UserSearchForm::new);
        UserSessionHelper.putUserSessionParameter(context, EpgUserSessionKey.USER_SEARCH_FORM, userSearchform);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }
}
