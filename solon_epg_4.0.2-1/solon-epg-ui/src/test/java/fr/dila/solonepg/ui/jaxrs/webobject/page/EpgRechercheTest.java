package fr.dila.solonepg.ui.jaxrs.webobject.page;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.ui.th.model.EpgRechercheTemplate;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.session.UserSession;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ WebEngine.class })
@PowerMockIgnore("javax.management.*")
public class EpgRechercheTest {

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Mock
    private WebContext webContext;

    @Mock
    private UserSession userSession;

    @Mock
    private HttpServletRequest request;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(WebEngine.class);

        when(WebEngine.getActiveContext()).thenReturn(webContext);
        when(webContext.getUserSession()).thenReturn(userSession);

        when(webContext.getRequest()).thenReturn(request);
        when(request.getQueryString()).thenReturn("");
    }

    @Test
    public void testRechercheRapide() {
        EpgRechercheTemplate template = spy(new EpgRechercheTemplate());
        EpgRecherche epgRecherche = spy(new EpgRecherche());

        doReturn(null).when(epgRecherche).newObject(any(), any(), any());
        Whitebox.setInternalState(epgRecherche, "template", template);

        epgRecherche.doRechercheRapide("nor-test");

        verify(epgRecherche).newObject(Mockito.eq("AppliEpgRapideResultats"), any(), Mockito.eq(template));
    }
}
