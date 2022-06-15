package fr.dila.solonepg.ui.jaxrs.webobject;

import fr.dila.solonepg.ui.th.model.EpgLayoutThTemplate;
import fr.dila.ss.ui.jaxrs.webobject.SsRoot;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.nuxeo.ecm.webengine.model.WebObject;

@Path("app-ui")
@Produces("text/html;charset=UTF-8")
@WebObject(type = "solonepg-ui")
public class EpgRoot extends SsRoot {

    public EpgRoot() {
        super();
    }

    @Path("mgpp")
    public Object mgpp() {
        return newObject("MgppUI");
    }

    @POST
    @Path("/@@login")
    @Override
    public Object execLogin() {
        return redirect("/travail");
    }

    @GET
    @Override
    public Object doHome() {
        return redirect("/travail");
    }

    @Override
    protected ThTemplate getMyOwnTemplate(String page, SpecificContext context) {
        return new EpgLayoutThTemplate(page, context);
    }

    @Path("suiviLibre")
    public Object doSuiviLibre() {
        SpecificContext context = new SpecificContext();
        context.setNavigationContextTitle(
            new Breadcrumb(ResourceHelper.getString("suiviLibre.solon"), "/", Breadcrumb.TITLE_ORDER)
        );
        return newObject("SuiviLibre", context);
    }

    @Path("selection")
    public Object doSelectionTool() {
        return newObject("SelectionTool");
    }
}
