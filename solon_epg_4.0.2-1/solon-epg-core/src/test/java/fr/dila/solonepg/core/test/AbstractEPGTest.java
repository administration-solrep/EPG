package fr.dila.solonepg.core.test;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.cm.service.CaseDistributionService;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.service.MailboxService;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.sword.naiad.nuxeo.commons.core.schema.DublincorePropertyUtil;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import javax.inject.Inject;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.mockito.MockitoFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features({ SolonEPGFeature.class, MockitoFeature.class })
public abstract class AbstractEPGTest {
    @Inject
    protected CoreFeature coreFeature;

    @Inject
    protected CoreSession session;

    @Inject
    protected EventService eventService;

    @Inject
    protected DossierService dossierService;

    @Inject
    protected MailboxService correspMailboxService;

    @Inject
    protected CaseDistributionService distributionService;

    @Inject
    protected EpgDossierDistributionService dossierDistributionService;

    @Inject
    protected EPGFeuilleRouteService epgFeuilleRouteService;

    protected static final String administrator = "Administrator";
    protected static final String user = "user";
    protected static final String user1 = "user1";
    protected static final String user2 = "user2";
    protected static final String user3 = "user3";
    protected static final String POSTE1 = "poste1";
    protected static final String POSTE2 = "poste2";
    protected static final String POSTE3 = "poste3";
    protected static final String DEFAULT_TYPE_ACTE_ID = "1";
    protected static final String DEFAULT_MINISTERE_ID = "1";
    protected static final String DEFAULT_DIRECTION_ID = "1";
    protected static final String DEFAULT_NOR = "ECOX9800017A";
    protected static final String DEFAULT_ROUTE_NAME = "FeuilleRouteTest";
    public static final String CASE_TITLE = "moncase";

    protected DocumentModel createDocument(String type, String id) throws Exception {
        DocumentModel document = session.createDocumentModel(type);
        document.setPathInfo("/", id);
        document = session.createDocument(document);
        return document;
    }

    protected DocumentModel createDocumentModel(CoreSession session, String name, String type, String path) {
        DocumentModel route1 = session.createDocumentModel(path, name, type);
        DublincorePropertyUtil.setTitle(route1, name);
        return session.createDocument(route1);
    }

    /**
     * Crée une dossier standard.
     *
     * @return Dossier
     */
    protected Dossier createDossier() {
        // Crée le dossier
        DocumentModel dossierDoc = session.createDocumentModel(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
        DublincoreSchemaUtils.setTitle(dossierDoc, DEFAULT_NOR);

        Mailbox userMailbox = getPersonalMailbox(user);
        Dossier dossier = distributionService.createEmptyCase(session, dossierDoc, userMailbox, Dossier.class);

        // Ajout des informations de créationt
        dossier.setTypeActe(DEFAULT_TYPE_ACTE_ID);
        dossier.setMinistereResp(DEFAULT_MINISTERE_ID);
        dossier.setDirectionResp(DEFAULT_DIRECTION_ID);
        dossier.setNumeroNor(DEFAULT_NOR);

        // Crée le dossier
        dossierService.createDossier(session, dossier, POSTE1, null);
        return dossier;
    }

    public Mailbox getPersonalMailbox(String name) {
        UserManager userManager = ServiceUtil.getRequiredService(UserManager.class);
        DocumentModel userModel = userManager.getUserModel(name);
        return correspMailboxService.createPersonalMailboxes(session, userModel);
    }

    protected Mailbox getPosteMailbox(String posteId) {
        final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
        Mailbox mailbox = mailboxPosteService.getMailboxPoste(session, posteId);
        if (mailbox == null) {
            return mailboxPosteService.createPosteMailbox(session, posteId, posteId);
        }
        return mailbox;
    }
}
