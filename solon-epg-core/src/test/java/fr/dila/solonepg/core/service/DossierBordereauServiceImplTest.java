package fr.dila.solonepg.core.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.constant.STWebserviceConstant;
import fr.dila.st.api.service.JetonService;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.xsd.solon.epg.TypeModification;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ STServiceLocator.class, SolonEpgServiceLocator.class })
@PowerMockIgnore("javax.management.*")
public class DossierBordereauServiceImplTest {

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Mock
    private CoreSession session;

    @Mock
    private DocumentModel mockDocumentModel;

    @Mock
    private JournalService mockJournalService;

    @Mock
    private SolonEpgVocabularyService mockVocabularyService;

    @Mock
    private EpgCorbeilleService mockCorbeilleService;

    @Mock
    private JetonService mockJetonService;

    @Mock
    private Dossier mockDossier;

    private static final String NUMERO_NOR = "numeroNor";
    private static final String DOSSIER_ID = "dossierId";

    private DossierBordereauServiceImpl service;

    @Before
    public void before() {
        mockStatic(STServiceLocator.class);
        when(STServiceLocator.getJournalService()).thenReturn(mockJournalService);
        when(STServiceLocator.getJetonService()).thenReturn(mockJetonService);

        mockStatic(SolonEpgServiceLocator.class);
        when(SolonEpgServiceLocator.getSolonEpgVocabularyService()).thenReturn(mockVocabularyService);
        when(SolonEpgServiceLocator.getCorbeilleService()).thenReturn(mockCorbeilleService);

        when(mockDocumentModel.getId()).thenReturn(DOSSIER_ID);
        when(mockDocumentModel.getAdapter(Dossier.class)).thenReturn(mockDossier);

        when(mockCorbeilleService.existsPourAvisCEStep(session, DOSSIER_ID)).thenReturn(true);
        when(mockDossier.getNumeroNor()).thenReturn(NUMERO_NOR);

        service = new DossierBordereauServiceImpl();
    }

    @Test
    public void fireEventWithCategorieActePropertyShouldNotCreateJeton() {
        String categorieActeLabel = "Catégorie d'acte";
        String oldCategorieActeId = "1";
        String oldCategorieActeValue = "Réglementaire";
        String newCategorieActeId = "2";
        String newCategorieActeValue = "Non réglementaire";

        prepareMockVocabularyService(
            VocabularyConstants.NATURE,
            DossierSolonEpgConstants.DOSSIER_CATEGORIE_ACTE_PROPERTY,
            categorieActeLabel,
            oldCategorieActeId,
            oldCategorieActeValue,
            newCategorieActeId,
            newCategorieActeValue
        );

        Map.Entry<String, Object> entry = new AbstractMap.SimpleEntry<>(
            DossierSolonEpgConstants.DOSSIER_CATEGORIE_ACTE_XPATH,
            newCategorieActeId
        );
        service.fireEvent(session, mockDocumentModel, entry, newCategorieActeId, oldCategorieActeId);

        assertThatActionIsJournalisee(categorieActeLabel, oldCategorieActeValue, newCategorieActeValue);
        assertNoJetonIsCreated(newCategorieActeId);
    }

    @Test
    public void fireEventWithPrioriteShouldCreateJetonWithPrioriteId() {
        String label = "Priorite";
        String oldId = "1";
        String oldValue = "Normal";
        String newId = "3";
        String newValue = "Très urgent";

        prepareMockVocabularyService(
            VocabularyConstants.VOC_PRIORITE,
            ConseilEtatConstants.CONSEIL_ETAT_PRIORITE_PROPERTY,
            label,
            oldId,
            oldValue,
            newId,
            newValue
        );

        Map.Entry<String, Object> entry = new AbstractMap.SimpleEntry<>(
            ConseilEtatConstants.CONSEIL_ETAT_PRIORITE_XPATH,
            newId
        );
        service.fireEvent(session, mockDocumentModel, entry, newId, oldId);

        assertThatActionIsJournalisee(label, oldValue, newValue);
        assertJetonIsCreated(newId);
    }

    private void assertNoJetonIsCreated(String newId) {
        verify(mockJetonService, Mockito.never())
            .addDocumentInBasket(
                Mockito.any(CoreSession.class),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(DocumentModel.class),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(List.class)
            );
    }

    private void assertThatActionIsJournalisee(
        String categorieActeLabel,
        String oldCategorieActeValue,
        String newCategorieActeValue
    ) {
        verify(mockJournalService)
            .journaliserActionBordereau(
                Mockito.eq(session),
                Mockito.eq(mockDocumentModel),
                Mockito.eq(STEventConstant.BORDEREAU_UPDATE),
                Mockito.eq(
                    categorieActeLabel + " : '" + newCategorieActeValue + "' remplace '" + oldCategorieActeValue + "'"
                )
            );
    }

    private void prepareMockVocabularyService(
        String vocabularyDirName,
        String propertyName,
        String vocLabel,
        String oldId,
        String oldValue,
        String newId,
        String newValue
    ) {
        when(mockVocabularyService.getLabelFromId(vocabularyDirName, newId, STVocabularyConstants.COLUMN_LABEL))
            .thenReturn(newValue);
        when(mockVocabularyService.getLabelFromId(vocabularyDirName, oldId, STVocabularyConstants.COLUMN_LABEL))
            .thenReturn(oldValue);

        when(
                mockVocabularyService.getLabelFromId(
                    STVocabularyConstants.BORDEREAU_LABEL,
                    propertyName,
                    STVocabularyConstants.COLUMN_LABEL
                )
            )
            .thenReturn(vocLabel);
    }

    private void assertJetonIsCreated(String newId) {
        verify(mockJetonService)
            .addDocumentInBasket(
                Mockito.eq(session),
                Mockito.eq(STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER),
                Mockito.eq(TableReference.MINISTERE_CE),
                Mockito.eq(mockDocumentModel),
                Mockito.eq(NUMERO_NOR),
                Mockito.eq(TypeModification.PRIORISATION.name()),
                Mockito.eq(Collections.singletonList(newId))
            );
    }
}
