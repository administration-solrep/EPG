package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.ss.ui.enums.SSUserSessionKey;
import fr.dila.ss.ui.jaxrs.webobject.ajax.SSRechercheAjax;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(
    {
        EpgUIServiceLocator.class,
        ServiceUtil.class,
        UserSessionHelper.class,
        EpgRechercheAjaxTest.class,
        SSRechercheAjax.class,
        WebEngine.class
    }
)
@PowerMockIgnore("javax.management.*")
public class EpgRechercheAjaxTest {

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Mock
    private WebContext webContext;

    @Mock
    private HttpServletRequest request;

    @Mock
    private SpecificContext context;

    private EpgRechercheAjax controlleur;

    @Mock
    private CoreSession coreSession;

    @Mock
    private ProfilUtilisateurService profilUtilisateurService;

    @Mock
    private ProfilUtilisateur profilUtilisateur;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(ServiceUtil.class);
        PowerMockito.mockStatic(UserSessionHelper.class);
        PowerMockito.mockStatic(EpgUIServiceLocator.class);
        PowerMockito.mockStatic(WebEngine.class);

        controlleur = new EpgRechercheAjax();
        Whitebox.setInternalState(controlleur, "context", context);
        Whitebox.setInternalState(controlleur, "ctx", webContext);

        when(WebEngine.getActiveContext()).thenReturn(webContext);
        when(webContext.getCoreSession()).thenReturn(coreSession);

        when(SolonEpgServiceLocator.getProfilUtilisateurService()).thenReturn(profilUtilisateurService);
        when(profilUtilisateurService.getProfilUtilisateurForCurrentUser(coreSession)).thenReturn(profilUtilisateur);
        when(profilUtilisateur.getNbDossiersVisiblesMax()).thenReturn(10);

        when(context.getWebcontext()).thenReturn(webContext);
        when(webContext.getRequest()).thenReturn(request);
    }

    @Test
    public void testGetRapideResults() throws Exception {
        String nor = "nor-test";
        AjaxLayoutThTemplate template = new AjaxLayoutThTemplate();
        PowerMockito.whenNew(AjaxLayoutThTemplate.class).withNoArguments().thenReturn(template);

        when(UserSessionHelper.getUserSessionParameter(context, SSUserSessionKey.NOR)).thenReturn(nor);
        DossierListForm form = DossierListForm.newForm();
        controlleur.getRapideResults(form);

        Breadcrumb breadcrumb = new Breadcrumb(
            String.format("Consultation du dossier %s", nor),
            "/recherche/rapide",
            Breadcrumb.TITLE_ORDER,
            context.getWebcontext().getRequest()
        );
        verify(context).setNavigationContextTitle(breadcrumb);

        PowerMockito.verifyStatic();
        UserSessionHelper.putUserSessionParameter(context, SSUserSessionKey.SEARCH_RESULT_FORM, form);
        verify(webContext).newObject("AppliEpgRapideResultats", context, template);
    }
}
