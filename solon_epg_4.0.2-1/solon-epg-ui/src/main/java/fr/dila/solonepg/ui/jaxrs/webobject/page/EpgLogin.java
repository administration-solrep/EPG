package fr.dila.solonepg.ui.jaxrs.webobject.page;

import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.LOGIN_WAIT;

import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.ui.jaxrs.webobject.page.SSLogin;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.nuxeo.ecm.webengine.model.WebObject;

@Produces("text/html;charset=UTF-8")
@WebObject(type = "AppliLogin")
public class EpgLogin extends SSLogin {

    @Override
    @GET
    public Object getLogin(
        @QueryParam("failed") Boolean failed,
        @QueryParam(LOGIN_WAIT) Boolean wait,
        @QueryParam("resetpwd") Boolean resetpwd
    ) {
        Object response = super.getLogin(failed, wait, resetpwd);
        if (response instanceof ThTemplate) {
            ThTemplate template = (ThTemplate) response;

            Map<String, Object> data = template.getData();

            String urlSLQD = STServiceLocator
                .getSTParametreService()
                .getParametreWithoutSession(STParametreConstant.ADRESSE_URL_DIDACTICIEL);

            data.put(EpgTemplateConstants.URL_SLQD, urlSLQD);
        }

        return response;
    }
}
