package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.modele;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.ui.services.EpgModeleFdrFicheUIService;
import fr.dila.solonepg.ui.services.EpgSelectValueUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.ui.services.SSUIServiceLocator;
import fr.dila.ss.ui.services.actions.ModeleFeuilleRouteActionService;
import fr.dila.ss.ui.services.actions.SSActionsServiceLocator;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.exception.STAuthorizationException;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(
    { UserSessionHelper.class, SSActionsServiceLocator.class, EpgUIServiceLocator.class, SSUIServiceLocator.class }
)
@PowerMockIgnore("javax.management.*")
public class EpgModeleFeuilleRouteTest {
    private EpgModeleFeuilleRoute controlleur;

    @Mock
    private CoreSession session;

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Mock
    private SpecificContext context;

    @Mock
    private ModeleFeuilleRouteActionService modeleAction;

    @Mock
    private EpgModeleFdrFicheUIService modeleFDRFicheUIService;

    @Mock
    private EpgSelectValueUIService epgSelectService;

    @Mock
    private WebContext webcontext;

    @Mock
    private HttpServletRequest request;

    @Mock
    private NuxeoPrincipal princ;

    @Before
    public void before() throws Exception {
        controlleur = new EpgModeleFeuilleRoute();

        Whitebox.setInternalState(controlleur, "context", context);

        PowerMockito.mockStatic(UserSessionHelper.class);
        PowerMockito.mockStatic(SSActionsServiceLocator.class);
        PowerMockito.mockStatic(EpgUIServiceLocator.class);
        PowerMockito.mockStatic(SSUIServiceLocator.class);

        when(context.getSession()).thenReturn(session);
        when(session.getPrincipal()).thenReturn(princ);
        when(context.getWebcontext()).thenReturn(webcontext);
        when(webcontext.getPrincipal()).thenReturn(princ);
        when(webcontext.getRequest()).thenReturn(request);
        when(SSActionsServiceLocator.getModeleFeuilleRouteActionService()).thenReturn(modeleAction);
        when(EpgUIServiceLocator.getEpgSelectValueUIService()).thenReturn(epgSelectService);
        when(epgSelectService.getTypeActe()).thenReturn(new ArrayList<>());
        when(SSUIServiceLocator.getSSSelectValueUIService()).thenReturn(epgSelectService);
        when(epgSelectService.getRoutingTaskTypes()).thenReturn(new ArrayList<>());
        when(EpgUIServiceLocator.getEpgModeleFdrFicheUIService()).thenReturn(modeleFDRFicheUIService);
    }

    @Test
    public void testModeleCreationKO() {
        Throwable throwable = catchThrowable(() -> controlleur.getModeleFdrCreation());
        assertThat(throwable)
            .isInstanceOf(STAuthorizationException.class)
            .hasMessage("Accès à la ressource non autorisé : Vous n'avez pas les droits nécessaires pour cette action");
    }

    @Test
    public void testModeleCreation() {
        when(modeleAction.canUserCreateRoute(context)).thenReturn(true);
        ThTemplate template = controlleur.getModeleFdrCreation();
        assertNotNull(template);
        assertEquals("pages/admin/modele/createModeleFDR", template.getName());
        assertNotNull(template.getData());
        assertThat(template.getData())
            .containsOnlyKeys(
                SSTemplateConstants.TYPE_ETAPE,
                EpgTemplateConstants.TYPE_ACTE,
                STTemplateConstants.URL_PREVIOUS_PAGE,
                SSTemplateConstants.MODELE_FORM
            );
    }

    @Test
    public void testModeleModification() throws IllegalAccessException, InstantiationException {
        when(modeleAction.canUserReadRoute(context)).thenReturn(true);
        ThTemplate template = controlleur.getModeleFdrModification(StringUtils.EMPTY);
        assertNotNull(template);
        assertEquals("pages/admin/modele/consultModeleFDR", template.getName());
        assertNotNull(template.getData());
        assertEquals(9, template.getData().size());
        assertThat(template.getData())
            .containsKeys(
                SSTemplateConstants.TYPE_ETAPE,
                EpgTemplateConstants.TYPE_ACTE,
                STTemplateConstants.URL_PREVIOUS_PAGE,
                SSTemplateConstants.MODELE_FORM
            );
    }
}
