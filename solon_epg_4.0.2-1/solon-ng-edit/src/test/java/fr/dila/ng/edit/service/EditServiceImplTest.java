package fr.dila.ng.edit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.documentmodel.FileSolonEpg;
import fr.dila.ss.api.service.DocumentRoutingService;
import fr.dila.ss.api.tree.SSTreeFile;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.core.feature.SolonMockitoFeature;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import fr.dila.st.ui.services.actions.STLockActionService;
import java.util.Optional;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.runtime.test.runner.Features;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ STActionsServiceLocator.class, SSServiceLocator.class })
@PowerMockIgnore("javax.management.*")
@Features(SolonMockitoFeature.class)
public class EditServiceImplTest {

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private EditServiceImpl editService = new EditServiceImpl();

    @Mock
    private CoreSession session;

    @Mock
    private NuxeoPrincipal principal;

    @Mock
    private DocumentModel fichierDoc;

    @Mock
    private FileSolonEpg fichier;

    @Mock
    private DocumentModel dossierDoc;

    @Mock
    private Dossier dossier;

    @Mock
    private STLockActionService stLockActionService;

    @Mock
    private DocumentRoutingService documentRoutingService;

    @Mock
    private SSTreeFile ssTreeFile;

    @Before
    public void setup() {
        PowerMockito.mockStatic(STActionsServiceLocator.class);
        when(STActionsServiceLocator.getSTLockActionService()).thenReturn(stLockActionService);

        when(session.getPrincipal()).thenReturn(principal);

        PowerMockito.mockStatic(SSServiceLocator.class);
        when(SSServiceLocator.getDocumentRoutingService()).thenReturn(documentRoutingService);
    }

    @Test
    public void checkFichier() {
        // Given
        String id = "id";
        IdRef idRef = new IdRef(id);
        when(session.exists(idRef)).thenReturn(true);
        when(session.getDocument(idRef)).thenReturn(fichierDoc);
        when(fichierDoc.getAdapter(FileSolonEpg.class)).thenReturn(fichier);

        String idDossier = "id-dossier";
        IdRef idDossierRef = new IdRef(idDossier);
        when(fichier.getRelatedDocument()).thenReturn(idDossier);
        when(session.getDocument(idDossierRef)).thenReturn(dossierDoc);
        when(dossierDoc.getAdapter(Dossier.class)).thenReturn(dossier);

        when(stLockActionService.getLockOwnerName(dossierDoc, session)).thenReturn("user");
        when(principal.getName()).thenReturn("user");

        // When
        Optional<Response> response = editService.checkIdFichier(session, id, null);

        // Then
        assertThat(response).isNotPresent();
    }

    @Test
    public void checkFichierDevraitVerifierVersionFichier() {
        // Given
        String id = "id";
        IdRef idRef = new IdRef(id);
        when(session.exists(idRef)).thenReturn(true);
        when(session.getDocument(idRef)).thenReturn(fichierDoc);
        when(fichierDoc.getAdapter(FileSolonEpg.class)).thenReturn(fichier);

        when(fichierDoc.getAdapter(SSTreeFile.class)).thenReturn(ssTreeFile);
        when(ssTreeFile.getMajorVersion()).thenReturn(2L);

        String idDossier = "id-dossier";
        IdRef idDossierRef = new IdRef(idDossier);
        when(fichier.getRelatedDocument()).thenReturn(idDossier);
        when(session.getDocument(idDossierRef)).thenReturn(dossierDoc);
        when(dossierDoc.getAdapter(Dossier.class)).thenReturn(dossier);

        when(stLockActionService.getLockOwnerName(dossierDoc, session)).thenReturn("leet");
        when(principal.getName()).thenReturn("user");

        // When
        Optional<Response> response = editService.checkIdFichier(session, id, 1L);

        // Then
        assertThat(response).isPresent();
        assertThat((String) response.get().getEntity()).contains("Une nouvelle version du fichier existe");
    }

    @Test
    public void checkFichierDevraitPasVerifierVersionFichierSiNull() {
        // Given
        String id = "id";
        IdRef idRef = new IdRef(id);
        when(session.exists(idRef)).thenReturn(true);
        when(session.getDocument(idRef)).thenReturn(fichierDoc);
        when(fichierDoc.getAdapter(FileSolonEpg.class)).thenReturn(fichier);

        when(fichierDoc.getAdapter(SSTreeFile.class)).thenReturn(ssTreeFile);
        when(ssTreeFile.getMajorVersion()).thenReturn(2L);

        String idDossier = "id-dossier";
        IdRef idDossierRef = new IdRef(idDossier);
        when(fichier.getRelatedDocument()).thenReturn(idDossier);
        when(session.getDocument(idDossierRef)).thenReturn(dossierDoc);
        when(dossierDoc.getAdapter(Dossier.class)).thenReturn(dossier);

        when(stLockActionService.getLockOwnerName(dossierDoc, session)).thenReturn("leet");
        when(principal.getName()).thenReturn("user");

        // When
        Optional<Response> response = editService.checkIdFichier(session, id, null);

        // Then
        assertThat(response).isPresent();
        assertThat((String) response.get().getEntity()).doesNotContain("Une nouvelle version du fichier existe");
    }
}
