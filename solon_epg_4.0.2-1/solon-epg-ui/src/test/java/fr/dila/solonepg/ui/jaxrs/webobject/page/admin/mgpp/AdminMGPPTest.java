package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.mgpp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.ui.enums.mgpp.MgppActionEnum;
import fr.dila.solonepg.ui.services.mgpp.MgppAdminUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.model.bean.MgppParamForm;
import fr.dila.st.core.exception.STAuthorizationException;
import fr.dila.st.ui.th.impl.SolonAlertManager;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.URLUtils;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.session.UserSession;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ MgppUIServiceLocator.class, URLUtils.class })
@PowerMockIgnore("javax.management.*")
public class AdminMGPPTest {

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    private static final String BASE_URL = "http://test.fr";

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private AdminMGPP controlleur;

    @Mock
    private SpecificContext context;

    @Mock
    private MgppAdminUIService service;

    @Mock
    private WebContext webContext;

    @Mock
    private UserSession userSession;

    @Mock
    private HttpServletRequest request;

    @Before
    public void setUp() {
        controlleur = new AdminMGPP();
        PowerMockito.mockStatic(MgppUIServiceLocator.class);

        Whitebox.setInternalState(controlleur, "context", context);
        when(context.getWebcontext()).thenReturn(webContext);
        when(webContext.getUserSession()).thenReturn(userSession);

        when(webContext.getRequest()).thenReturn(request);
        when(request.getQueryString()).thenReturn("");
        when(MgppUIServiceLocator.getMgppAdminUIService()).thenReturn(service);
        when(service.getMgppParameters(context)).thenReturn(new MgppParamForm());

        PowerMockito.mockStatic(URLUtils.class);
        PowerMockito.doAnswer(invocation -> BASE_URL + invocation.getArgumentAt(0, String.class)).when(URLUtils.class);
        URLUtils.generateRedirectPath(anyString(), any(HttpServletRequest.class));
    }

    @Test
    public void testDisplayParameter() {
        when(context.getAction(MgppActionEnum.ADMIN_MENU_PARAM_PARAM_MGPP)).thenReturn(new Action());
        ThTemplate template = controlleur.displayParameter();
        assertThat(template.getData().keySet()).containsExactly("form");
    }

    @Test
    public void testDisplayParameterForbidden() {
        Throwable th = catchThrowable(() -> controlleur.displayParameter());
        assertThat(th)
            .isInstanceOf(STAuthorizationException.class)
            .hasMessage("Accès à la ressource non autorisé : Vous n'avez pas les droits pour accéder à cette page");
    }

    @Test
    public void testSaveParameter() {
        when(context.getAction(MgppActionEnum.ADMIN_MENU_PARAM_PARAM_MGPP)).thenReturn(new Action());
        when(context.getMessageQueue()).thenReturn(new SolonAlertManager());
        Response response = controlleur.saveParameter(new MgppParamForm());

        assertThat(response.getStatus()).isEqualTo(303);
        assertThat(getResponseUrl(response)).isEqualTo(BASE_URL + "/admin/mgpp/parametres");
    }

    @Test
    public void testSaveParameterForbidden() {
        Throwable th = catchThrowable(() -> controlleur.saveParameter(new MgppParamForm()));
        assertThat(th)
            .isInstanceOf(STAuthorizationException.class)
            .hasMessage("Accès à la ressource non autorisé : Vous n'avez pas les droits pour accéder à cette page");
    }

    private String getResponseUrl(Response response) {
        return response.getMetadata().get("Location").get(0).toString();
    }
}
