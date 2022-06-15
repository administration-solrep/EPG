package fr.dila.solonepg.ui.jaxrs.webobject.page.recherche;

import static com.tngtech.jgiven.impl.util.AssertionUtil.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.services.EpgRechercheUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.ss.ui.enums.SSUserSessionKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.session.UserSession;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ EpgUIServiceLocator.class, ServiceUtil.class, UserSessionHelper.class, WebEngine.class })
@PowerMockIgnore("javax.management.*")
public class EpgRechercheRapideResultsTest {

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Mock
    private SpecificContext context;

    @Mock
    private WebContext webContext;

    @Mock
    private UserSession userSession;

    @Mock
    private CoreSession coreSession;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ProfilUtilisateurService profilUtilisateurService;

    @Mock
    private ProfilUtilisateur profilUtilisateur;

    @Mock
    private EpgRechercheUIService epgRechercheUIService;

    private EpgRechercheRapideResults page;
    private ThTemplate thTemplate = new ThTemplate();

    @Before
    public void setup() {
        page = new EpgRechercheRapideResults();
        PowerMockito.mockStatic(ServiceUtil.class);
        PowerMockito.mockStatic(UserSessionHelper.class);
        PowerMockito.mockStatic(EpgUIServiceLocator.class);
        PowerMockito.mockStatic(WebEngine.class);

        Whitebox.setInternalState(page, "context", context);
        Whitebox.setInternalState(page, "template", thTemplate);
        when(context.getSession()).thenReturn(coreSession);
        when(context.getWebcontext()).thenReturn(webContext);
        when(webContext.getUserSession()).thenReturn(userSession);
        when(webContext.getRequest()).thenReturn(request);

        when(WebEngine.getActiveContext()).thenReturn(webContext);
        when(webContext.getCoreSession()).thenReturn(coreSession);

        when(SolonEpgServiceLocator.getProfilUtilisateurService()).thenReturn(profilUtilisateurService);
        when(profilUtilisateurService.getProfilUtilisateurForCurrentUser(coreSession)).thenReturn(profilUtilisateur);
        when(profilUtilisateur.getNbDossiersVisiblesMax()).thenReturn(10);

        when(EpgUIServiceLocator.getEpgRechercheUIService()).thenReturn(epgRechercheUIService);
    }

    @Test
    public void testRechercheRapideEmptyNor() {
        when(UserSessionHelper.getUserSessionParameter(context, SSUserSessionKey.NOR)).thenReturn("");

        ThTemplate template = (ThTemplate) page.doRecherche(false);

        Map<String, Object> map = template.getData();
        assertNotNull(map);

        DossierListForm resultForm = (DossierListForm) map.get(STTemplateConstants.RESULT_FORM);
        assertNotNull(resultForm);

        assertNotNull(map.get(STTemplateConstants.RESULT_LIST));
        assertEquals("/recherche/rapide", map.get(STTemplateConstants.DATA_URL));
        assertEquals("/ajax/recherche/rapide", map.get(STTemplateConstants.DATA_AJAX_URL));
    }

    @Test
    public void testRechercheRapideManyResults() {
        when(UserSessionHelper.getUserSessionParameter(context, SSUserSessionKey.NOR)).thenReturn("nor-test");

        EpgDossierList list = new EpgDossierList();
        when(epgRechercheUIService.doRechercheRapide(context)).thenReturn(list);

        ThTemplate template = (ThTemplate) page.doRecherche(false);

        Map<String, Object> map = template.getData();
        assertNotNull(map);

        DossierListForm resultForm = (DossierListForm) map.get(STTemplateConstants.RESULT_FORM);
        assertNotNull(resultForm);

        assertNotNull(map.get(STTemplateConstants.RESULT_LIST));
        assertEquals("/recherche/rapide", map.get(STTemplateConstants.DATA_URL));
        assertEquals("/ajax/recherche/rapide", map.get(STTemplateConstants.DATA_AJAX_URL));
    }
}
