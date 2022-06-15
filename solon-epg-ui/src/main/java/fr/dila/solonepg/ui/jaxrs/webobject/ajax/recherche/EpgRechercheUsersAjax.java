package fr.dila.solonepg.ui.jaxrs.webobject.ajax.recherche;

import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.jaxrs.webobject.pages.users.TransverseUsers;
import fr.dila.st.ui.th.bean.UsersListForm;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "EpgRechercheUsersAjax")
public class EpgRechercheUsersAjax extends SolonWebObject {

    public EpgRechercheUsersAjax() {}

    @GET
    @Path("liste")
    public ThTemplate getUsers(@SwBeanParam UsersListForm usersform) {
        template = new AjaxLayoutThTemplate("fragments/table/tableUsers", context);
        return TransverseUsers.generateListUsersTemplate(
            context,
            template,
            EpgURLConstants.URL_ANNUAIRE,
            ResourceHelper.getString("recherche.menu.consulter.annuaire"),
            usersform
        );
    }
}
