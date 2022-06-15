package fr.dila.solonepg.ui.services.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.api.service.SolonEpgUserManager;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.th.impl.SolonAlertManager;
import fr.dila.st.ui.th.model.SpecificContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.userworkspace.api.UserWorkspaceService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ STServiceLocator.class, SolonEpgServiceLocator.class })
@PowerMockIgnore("javax.management.*")
public class EpgUserManagerUIServiceImplTest {
    private static final String USER_ID = "pgirard";
    private static final String USER_ID_2 = "adminsgg";

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private EpgUserManagerUIServiceImpl service;

    @Mock
    private SpecificContext context;

    @Mock
    private SolonEpgUserManager epgUserManager;

    @Mock
    private CoreSession session;

    @Mock
    private DocumentModel userDoc;

    @Mock
    private UserWorkspaceService userWorkspaceService;

    @Mock
    private DocumentModel userProfilDoc;

    @Mock
    private SolonAlertManager alert;

    @Before
    public void setUp() {
        service = new EpgUserManagerUIServiceImpl();

        PowerMockito.mockStatic(STServiceLocator.class);
        when(STServiceLocator.getUserManager()).thenReturn(epgUserManager);
        when(STServiceLocator.getUserWorkspaceService()).thenReturn(userWorkspaceService);

        PowerMockito.mockStatic(SolonEpgServiceLocator.class);

        when(context.getSession()).thenReturn(session);
        when(context.getCurrentDocument()).thenReturn(userDoc);
        when(context.getMessageQueue()).thenReturn(alert);

        when(userWorkspaceService.getCurrentUserPersonalWorkspace(session)).thenReturn(userProfilDoc);
        when(epgUserManager.canDeleteUser(session, USER_ID)).thenReturn(true);
        when(epgUserManager.canDeleteUser(session, USER_ID_2)).thenReturn(false);
    }

    @Test
    public void deleteUser() {
        when(epgUserManager.getUserId(userDoc)).thenReturn(USER_ID);

        service.deleteUser(context);

        verify(alert).addSuccessToQueue("admin.user.delete.success");
        verify(epgUserManager).deleteUser(userDoc);
    }

    @Test
    public void deleteUserWithErrorMessage() {
        when(epgUserManager.getUserId(userDoc)).thenReturn(USER_ID_2);

        service.deleteUser(context);

        verify(alert).addErrorToQueue(ResourceHelper.getString("admin.user.delete.not.allow.tab.ref"));
        verify(epgUserManager, Mockito.never()).deleteUser(userDoc);
    }
}
