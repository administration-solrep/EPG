package fr.dila.solonmgpp.core.service;

import static fr.dila.solonepg.core.operation.DataInjectionOperation.USER_ADMINSGG;
import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getDossierService;
import static fr.dila.ss.core.service.SSServiceLocator.getMailboxPosteService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.nuxeo.ecm.core.api.CoreInstance.openCoreSession;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonmgpp.api.service.CourrierProcessorService;
import fr.dila.solonmgpp.core.SolonMgppFeature;
import fr.dila.st.api.constant.STConstant;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CloseableCoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(SolonMgppFeature.class)
@Ignore
public class CourrierProcessorServiceIT extends AbstractCourrierIT {
    @Inject
    private CourrierProcessorService cpService;

    @Inject
    private UserManager um;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        createModele("modele");
    }

    @Test
    public void testGenerateCourrier() throws IOException {
        Dossier dos = createDossier();

        File courrier;
        try (
            CloseableCoreSession userSession = openCoreSession(
                session.getRepositoryName(),
                um.getPrincipal(USER_ADMINSGG)
            )
        ) {
            courrier =
                cpService.generateCourrierUnrestrictedFromDossierAndFiche(
                    userSession,
                    "modele",
                    dos.getDocument().getId(),
                    null
                );
        }

        assertThat(courrier).isNotNull();

        // Permets de consulter le résultat dans le répertoire tmp
        File temp = File.createTempFile("TempCourrier", ".docx");
        FileUtils.copyFile(courrier, temp);
    }

    private Dossier createDossier() {
        Mailbox posteMbx = getMailboxPosteService().createPosteMailbox(session, "50002249", "posteLabel");
        DocumentModel dossierDoc = session.createDocumentModel(STConstant.DOSSIER_DOCUMENT_TYPE);
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        dossier.setTitle("ECOX2000001A");
        dossier.setNumeroNor("ECOX2000001A");
        dossier.setTypeActe("1");
        dossier.setMinistereResp("50000507");
        dossier.setDirectionResp("50000655");
        return getDossierService().createDossier(session, dossierDoc, "50002249", posteMbx, "");
    }
}
