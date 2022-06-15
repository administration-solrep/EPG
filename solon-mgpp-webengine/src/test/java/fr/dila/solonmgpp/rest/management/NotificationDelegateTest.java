package fr.dila.solonmgpp.rest.management;

import static fr.sword.xsd.commons.TraitementStatut.KO;
import static fr.sword.xsd.commons.TraitementStatut.OK;

import fr.dila.solonepg.core.test.SolonEPGFeature;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.api.service.EvenementService;
import fr.dila.solonmgpp.api.service.NavetteService;
import fr.dila.solonmgpp.api.service.NotificationService;
import fr.dila.solonmgpp.rest.api.NotificationDelegate;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.sword.xsd.solon.epp.EvenementType;
import fr.sword.xsd.solon.epp.Institution;
import fr.sword.xsd.solon.epp.Message;
import fr.sword.xsd.solon.epp.NotificationEvenementType;
import fr.sword.xsd.solon.epp.NotifierEvenementRequest;
import fr.sword.xsd.solon.epp.NotifierEvenementRequest.Notifications;
import fr.sword.xsd.solon.epp.NotifierEvenementResponse;
import fr.sword.xsd.solon.epp.NotifierTableDeReferenceRequest;
import fr.sword.xsd.solon.epp.NotifierTableDeReferenceResponse;
import fr.sword.xsd.solon.epp.ObjetType;
import javax.inject.Inject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.nuxeo.ecm.core.api.CloseableCoreSession;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.mockito.MockitoFeature;
import org.nuxeo.runtime.mockito.RuntimeService;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features({ SolonEPGFeature.class, MockitoFeature.class })
public class NotificationDelegateTest {
    @Inject
    protected CoreFeature coreFeature;

    @Mock
    private SSPrincipal principal;

    @Mock
    @RuntimeService
    private DossierService dossierService;

    @Mock
    @RuntimeService
    private EvenementService evenementService;

    @Mock
    @RuntimeService
    private NavetteService navetteService;

    @Mock
    @RuntimeService
    private NotificationService notificationService;

    private NotificationDelegate delegate;
    private CloseableCoreSession session;

    @Before
    public void setUp() {
        session = coreFeature.openCoreSession(principal);
        delegate = new NotificationDelegateImpl(session);
    }

    @After
    public void tearDown() {
        session.close();
    }

    @Test
    public void testNotifierEvenement() throws Exception {
        NotifierEvenementRequest request = new NotifierEvenementRequest();
        NotifierEvenementResponse response;

        Notifications notif1 = new Notifications();
        notif1.setTypeNotification(NotificationEvenementType.ACCUSER_RECEPTION);
        notif1.setMessage(new Message());
        notif1.getMessage().setIdEvenement("EVT_00000");
        notif1.getMessage().setTypeEvenement(EvenementType.EVT_02);
        Notifications notif2 = new Notifications();
        notif2.setTypeNotification(NotificationEvenementType.PUBLIER_INITIALE);
        notif2.setMessage(new Message());
        notif2.getMessage().setIdEvenement("EVT_00001");
        notif2.getMessage().setTypeEvenement(EvenementType.EVT_02);
        notif2.getMessage().setDestinataireEvenement(Institution.GOUVERNEMENT);
        Notifications notif3 = new Notifications();
        notif3.setTypeNotification(NotificationEvenementType.PUBLIER_ANNULATION);
        notif3.setMessage(new Message());
        notif3.getMessage().setIdEvenement("EVT_00001");
        notif3.getMessage().setTypeEvenement(EvenementType.EVT_02);
        notif3.getMessage().setEmetteurEvenement(Institution.GOUVERNEMENT);

        response = delegate.notifierEvenement(request);
        Assert.assertEquals(KO, response.getStatut());

        request.getNotifications().add(notif1);
        request.getNotifications().add(notif2);
        request.getNotifications().add(notif3);
        response = delegate.notifierEvenement(request);
        Assert.assertEquals(OK, response.getStatut());
    }

    @Test
    public void testNotifierTableDeReference() {
        NotifierTableDeReferenceRequest request = new NotifierTableDeReferenceRequest();
        NotifierTableDeReferenceResponse response;

        request.setType(ObjetType.MANDAT);
        response = delegate.notifierTableDeReference(request);
        Assert.assertEquals(OK, response.getStatut());
    }
}
