package fr.dila.solonepg.core.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.enums.BilanSemestrielType;
import fr.dila.solonepg.api.exception.WsBdjException;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.core.bdj.WsBdj;
import fr.dila.solonepg.core.cases.DecretImpl;
import fr.dila.solonepg.core.cases.MesureApplicativeImpl;
import fr.dila.solonepg.core.cases.TexteMaitreImpl;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.BilanSemestrielDTOImpl;
import fr.dila.solonepg.core.mock.MockDataContainer;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import fr.dila.st.core.util.SolonDateConverter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ActiviteNormativeServiceTest extends AbstractEPGTest {
    private static final String NUMERO_NOR_2 = "INTD1909999D";
    private static final String TITRE_OFFICIEL_1 =
        "Décret n° 2018-167 du 7 mars 2018 pris pour application de l'article 6 de la loi n° 55-385 du 3 avril 1955 relative à l'état d'urgence et de l'article L. 228-3 du code de la sécurité intérieure, et relatif au placement sous surveillance électronique mobile";
    private static final String NUMERO_NOR_1 = "INTD1801247D";

    private TexteMaitre texteMaitre;
    private TexteMaitre texteMaitreOlderLegislature;

    private ActiviteNormativeServiceImpl actNormService;
    private WsBdjServiceImpl wsBdjService;

    /**
     * Map de stockage des données des mocks
     */
    //    private Map<Serializable, Map<String, Object>> dataMap = new HashMap<>();
    private MockDataContainer mockDataContainer = new MockDataContainer();

    private Map<String, MesureApplicative> mapMesures;
    private Map<String, Decret> mapDecrets;

    private boolean texteMaitreSaved;

    @Mock
    private WsBdj wsBdj;

    @Before
    public void setUp() throws Exception {
        SolonEpgParametreService paramEPGservice = SolonEpgServiceLocator.getSolonEpgParametreService();
        ParametrageAN parametrageAN = paramEPGservice.getDocAnParametre(session);
        parametrageAN.setLegislatureEnCours("13");
        session.saveDocument(parametrageAN.getDocument());
        session.save();

        coreFeature.waitForAsyncCompletion();

        createMocks();
    }

    @Test
    public void testBuildNullEcheancier() throws IOException, FactoryConfigurationError, XMLStreamException {
        List<TexteMaitre> textesMaitres;

        textesMaitres = Collections.emptyList();
        String echeancierXml = wsBdjService.buildEcheancierXmlStr(session, textesMaitres, actNormService);
        Assert.assertNull(echeancierXml);

        textesMaitres = Collections.singletonList(texteMaitreOlderLegislature);
        echeancierXml = wsBdjService.buildEcheancierXmlStr(session, textesMaitres, actNormService);
        Assert.assertNull(echeancierXml);
    }

    @Test
    public void testBuildEcheancierXmlStr() throws IOException, FactoryConfigurationError, XMLStreamException {
        List<TexteMaitre> textesMaitres = Collections.singletonList(texteMaitre);

        String echeancierXml = wsBdjService.buildEcheancierXmlStr(session, textesMaitres, actNormService);
        Assert.assertNotNull(echeancierXml);

        String expectedXml = getFileContent("injectionbdj/echeancier.xml");
        Assert.assertEquals(expectedXml, echeancierXml);
    }

    @Test
    public void testBuildBilanSemestrielXmlStr() throws XMLStreamException, IOException {
        BilanSemestrielDTOImpl bilanSemestriel = new BilanSemestrielDTOImpl();

        SolonEpgParametreService paramEPGservice = SolonEpgServiceLocator.getSolonEpgParametreService();
        ParametrageAN parametrageAN = paramEPGservice.getDocAnParametre(session);
        parametrageAN.setLegislatureEnCours("15");
        session.saveDocument(parametrageAN.getDocument());
        session.save();

        bilanSemestriel.setType(BilanSemestrielType.LOI);
        bilanSemestriel.setDateBilan(SolonDateConverter.DATE_SLASH.parseToCalendar("10/08/2018").getTime());
        bilanSemestriel.setDateDebutIntervalleTextesPublies(
            SolonDateConverter.DATE_SLASH.parseToCalendar("01/07/2017").getTime()
        );
        bilanSemestriel.setDateFinIntervalleTextesPublies(
            SolonDateConverter.DATE_SLASH.parseToCalendar("31/12/2018").getTime()
        );
        bilanSemestriel.setDateDebutIntervalleMesures(
            SolonDateConverter.DATE_SLASH.parseToCalendar("01/07/2017").getTime()
        );
        bilanSemestriel.setDateFinIntervalleMesures(
            SolonDateConverter.DATE_SLASH.parseToCalendar("31/12/2018").getTime()
        );

        bilanSemestriel.addTexte("1", "Loi pour la confiance dans la vie politique", 10, 5, 5);
        bilanSemestriel.addTexte("2", "Loi de financement de la sécurité sociale", 20, 5, 15);

        final String bilanXml = wsBdjService.buildBilanXmlStr(bilanSemestriel, "15", session);

        final String expectedXml = getFileContent("injectionbdj/bilansemestriel.xml");

        Assert.assertNotNull(bilanXml);
        Assert.assertEquals(expectedXml, bilanXml);
    }

    @Test
    public void testPublierEchancierDone()
        throws WsBdjException, IOException, FactoryConfigurationError, XMLStreamException {
        texteMaitreSaved = false;

        List<TexteMaitre> textesMaitres = Collections.singletonList(texteMaitre);

        wsBdjService.publierEcheancierBDJ(textesMaitres, session);

        Assert.assertTrue(texteMaitreSaved);
    }

    @Test
    public void testPublierEchancierFailed() throws WsBdjException {
        Mockito.doThrow(WsBdjException.class).when(wsBdj).publierEcheancierBDJ(Matchers.any(), Matchers.any());

        texteMaitreSaved = false;

        List<TexteMaitre> textesMaitres = Collections.singletonList(texteMaitre);

        Assertions
            .assertThatExceptionOfType(WsBdjException.class)
            .isThrownBy(() -> wsBdjService.publierEcheancierBDJ(textesMaitres, session));
        // the date should not be updated
        Assert.assertFalse(texteMaitreSaved);
    }

    /**
     * Objectif de cette méthode : rendre la création de mock dynamique au niveau
     * des getters/setters.Il n'a pas été possible de simplifier autant la création
     * du setter, le when(T). ne pouvant pas être remplacé à première vue par une
     * référence de méthode (parce qu'il renvoie Void :( ).
     */
    private Decret createOneMockDecret(String numeroNor, String titreOfficiel) {
        Decret decret = Mockito.mock(DecretImpl.class);

        String id = mockDataContainer.generateId();

        String numeroNorKey = "numeroNor";
        String titreOffKey = "titreOfficiel";

        // Getter-setter Numero nor
        mockDataContainer.doAnswerGetter(decret::getNumeroNor, id, numeroNorKey);
        mockDataContainer.doAnswerSetter(decret, id, numeroNorKey).setNumeroNor(Matchers.anyString());

        // Getter-setter titre officiel
        mockDataContainer.doAnswerGetter(decret::getTitreOfficiel, id, titreOffKey);
        mockDataContainer.doAnswerSetter(decret, id, titreOffKey).setTitreOfficiel(Matchers.anyString());

        decret.setNumeroNor(numeroNor);
        decret.setTitreOfficiel(titreOfficiel);

        return decret;
    }

    private void createMocksMesuresApplicatives() {
        mapMesures = new HashMap<>();

        createOneMockMesureApplicative(
            "1",
            2,
            "Article 3, 2°",
            "Article L228-3, code de la sécurité intérieure",
            "Placement sous surveillance électronique mobile de toute personne à l’égard de laquelle il existe des raisons sérieuses de penser que son comportement constitue une menace d’une particulière gravité pour la sécurité et l’ordre publics",
            Arrays.asList(NUMERO_NOR_1, NUMERO_NOR_2)
        );

        createOneMockMesureApplicative(
            "2",
            2,
            "Article 11, I, 3°",
            "Article L114-1, code de la sécurité intérieure, I",
            "Modalités d'information des personnes intéressées de la consultation des traitements automatisés de données à caractère personnel relevant de l’article 26 de la loi n° 78-17 du 6 janvier 1978 relative à l’informatique, aux fichiers et aux libertés lors des enquêtes administratives destinées aux fins de vérifier que le comportement ces personnes n'est pas incompatible avec l'exercice des fonctions ou des missions envisagées",
            new ArrayList<>()
        );

        createOneMockMesureApplicative(
            "3",
            2,
            "Article 11, I, 3°",
            "Article L114-1, code de la sécurité intérieure, IV",
            "Composition et fonctionnement de l'organisme paritaire chargé de donner un avis préalable à la mutation ou à la radiation des cadres du fonctionnaire occupant un emploi participant à l’exercice de missions de souveraineté de l’État ou relevant du domaine de la sécurité ou de la défense et dont le comportement est devenu incompatible avec l’exercice de ses fonctions",
            new ArrayList<>()
        );

        createOneMockMesureApplicative(
            "4",
            3,
            "Article 11, II, 1°",
            "Article L4125-1, code de la défense",
            "Conditions d'exercice du recours administratif préalable aux recours contentieux formés par les militaires mentionnés à l’article L. 4111-2 à l’encontre d’actes relatifs à leur situation personnelle",
            new ArrayList<>()
        );

        createOneMockMesureApplicative(
            "5",
            3,
            "Article11, II, 2°, b) ",
            "Article L4139-15-1, code de la défense",
            "Composition et fonctionnement du conseil chargé de donner un avis préalable à la radiation des cadres ou à la résiliation du contrat du militaire dont le comportement est devenu incompatible avec l’exercice de ses fonctions eu égard à la menace grave qu’il fait peser sur la sécurité publique",
            new ArrayList<>()
        );

        createOneMockMesureApplicative(
            "6",
            3,
            "Article 11, II, 2°, b)",
            "Article L4139-15-1, code de la défense",
            "Radiation des cadres ou à la résiliation du contrat des militaires dont le comportement est devenu incompatible avec l’exercice de leurs fonctions eu égard à la menace grave qu’il font peser sur la sécurité publique",
            new ArrayList<>()
        );

        createOneMockMesureApplicative(
            "7",
            6,
            "Article 14, 1°",
            "Article L232-7-1, VI, code de la sécurité intérieure",
            "PNR maritime: traitement automatisé de données à caractère personnel mis en place pour les besoins de la prévention et de la constatation de certaines infractions, du rassemblement des preuves de ces infractions ainsi que de la recherche de leurs auteurs d'actes de terrorisme, d'atteintes aux intérêts fondamentaux de la Nation ainsi que les infractions mentionnées à l’article 694-32 du code de procédure pénale",
            new ArrayList<>()
        );

        createOneMockMesureApplicative(
            "8",
            3,
            "Article 15, I, 4°",
            "Article L852-2, code de la sécurité intérieure",
            "Liste des services intéressés pouvant être autorisés à réaliser des interceptions de correspondances échangées au sein d’un réseau de communications électroniques empruntant exclusivement la voie hertzienne et n’impliquant pas l’intervention d’un opérateur de communications électroniques, lorsque ce réseau est conçu pour une utilisation privative par une personne ou un groupe fermé d’utilisateurs, ainsi que la ou les finalités concernées, en application de l'article L. 811-4 du CSI",
            new ArrayList<>()
        );
    }

    @SuppressWarnings("unchecked")
    private void createOneMockMesureApplicative(
        String numero,
        int month,
        String article,
        String baseLegale,
        String objet,
        List<String> decretIds
    ) {
        Calendar cal = Calendar.getInstance();
        cal.set(2018, month, 1);

        MesureApplicative mesureApplicative = mock(MesureApplicativeImpl.class);

        String id = mockDataContainer.generateId();

        String numOrdreKey = "numeroOrdreKey";
        String articleKey = "articleKey";
        String baseLegaleKey = "baseLegaleKey";
        String objetRIMKey = "objetRIMKey";
        String objectifKey = "objectifKey";
        String decretsIdsKey = "decretsIdsKey";

        // Getter-setter numéro d'ordre
        mockDataContainer.doAnswerGetter(mesureApplicative::getNumeroOrdre, id, numOrdreKey);
        mockDataContainer.doAnswerSetter(mesureApplicative, id, numOrdreKey).setNumeroOrdre(Matchers.anyString());

        // Getter-setter article
        mockDataContainer.doAnswerGetter(mesureApplicative::getArticle, id, articleKey);
        mockDataContainer.doAnswerSetter(mesureApplicative, id, articleKey).setArticle(Matchers.anyString());

        // Getter-setter base légale
        mockDataContainer.doAnswerGetter(mesureApplicative::getBaseLegale, id, baseLegaleKey);
        mockDataContainer.doAnswerSetter(mesureApplicative, id, baseLegaleKey).setBaseLegale(Matchers.anyString());

        // Getter-setter objet RIM
        mockDataContainer.doAnswerGetter(mesureApplicative::getObjetRIM, id, objetRIMKey);
        mockDataContainer.doAnswerSetter(mesureApplicative, id, objetRIMKey).setObjetRIM(Matchers.anyString());

        // Getter-setter objectif
        mockDataContainer.doAnswerGetter(mesureApplicative::getDateObjectifPublication, id, objectifKey);
        mockDataContainer
            .doAnswerSetter(mesureApplicative, id, objectifKey)
            .setDateObjectifPublication(Matchers.any(Calendar.class));

        // Getter-setter decretsId
        mockDataContainer.doAnswerGetter(mesureApplicative::getDecretIds, id, decretsIdsKey);
        mockDataContainer.doAnswerSetter(mesureApplicative, id, decretsIdsKey).setDecretIds(Matchers.any(List.class));

        mesureApplicative.setNumeroOrdre(numero);
        mesureApplicative.setArticle(article);
        mesureApplicative.setBaseLegale(baseLegale);
        mesureApplicative.setObjetRIM(objet);
        mesureApplicative.setDateObjectifPublication(cal);
        mesureApplicative.setDecretIds(decretIds);

        mapMesures.put(numero, mesureApplicative);
    }

    private void createMocksTextesMaitres() {
        texteMaitre =
            createOneMockTexteMaitre(
                "2017-1510",
                "INTX1716370L",
                Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8"),
                "13"
            );

        texteMaitreOlderLegislature =
            createOneMockTexteMaitre("2017-0001", "INTX1716371L", Arrays.asList("1", "2"), "12");
    }

    @SuppressWarnings("unchecked")
    private TexteMaitre createOneMockTexteMaitre(
        String numero,
        String numeroNor,
        List<String> mesuresIds,
        String legislaturePublication
    ) {
        TexteMaitre mockTexteMaitre = mock(TexteMaitreImpl.class);

        String id = mockDataContainer.generateId();

        String numeroKey = "numeroKey";
        String numeroNorKey = "numeroNorKey";
        String mesuresIdsKey = "mesuresIdsKey";
        String legislaturePublicationKey = "legislaturePublicationKey";
        String dateInjectionKey = "dateInjectionKey";

        DocumentModel dm = mock(DocumentModel.class);

        // Getter-setter Numero
        mockDataContainer.doAnswerGetter(mockTexteMaitre::getNumero, id, numeroKey);
        mockDataContainer.doAnswerSetter(mockTexteMaitre, id, numeroKey).setNumero(Matchers.anyString());

        // Getter-setter numéro Nor
        mockDataContainer.doAnswerGetter(mockTexteMaitre::getNumeroNor, id, numeroNorKey);
        mockDataContainer.doAnswerSetter(mockTexteMaitre, id, numeroNorKey).setNumeroNor(Matchers.anyString());

        // Getter-setter mesures ids
        mockDataContainer.doAnswerGetter(mockTexteMaitre::getMesuresIds, id, mesuresIdsKey);
        mockDataContainer.doAnswerSetter(mockTexteMaitre, id, mesuresIdsKey).setMesuresIds(Matchers.any(List.class));

        // Getter-setter legislature publication
        mockDataContainer.doAnswerGetter(mockTexteMaitre::getLegislaturePublication, id, legislaturePublicationKey);
        mockDataContainer
            .doAnswerSetter(mockTexteMaitre, id, legislaturePublicationKey)
            .setLegislaturePublication(Matchers.anyString());

        // Getter-setter date injection
        mockDataContainer.doAnswerGetter(mockTexteMaitre::getDateInjection, id, dateInjectionKey);
        mockDataContainer
            .doAnswerSetter(mockTexteMaitre, id, dateInjectionKey)
            .setDateInjection(Matchers.any(Calendar.class));

        // save
        when(mockTexteMaitre.save(Matchers.any(CoreSession.class)))
            .thenAnswer(
                (Answer<DocumentModel>) invocation -> {
                    texteMaitreSaved = true;
                    return dm;
                }
            );

        mockTexteMaitre.setNumero(numero);
        mockTexteMaitre.setNumeroNor(numeroNor);
        mockTexteMaitre.setMesuresIds(mesuresIds);
        mockTexteMaitre.setLegislaturePublication(legislaturePublication);

        return mockTexteMaitre;
    }

    private void createMocks() {
        createMocksTextesMaitres();
        createMocksDecrets();
        createMocksMesuresApplicatives();

        createMockActNormService();
    }

    @SuppressWarnings("unchecked")
    private void createMockActNormService() {
        actNormService = Mockito.mock(ActiviteNormativeServiceImpl.class, Mockito.CALLS_REAL_METHODS);

        wsBdjService = Mockito.mock(WsBdjServiceImpl.class, Mockito.CALLS_REAL_METHODS);
        when(wsBdjService.getWsBdj()).thenReturn(wsBdj);

        Answer<List<MesureApplicative>> answer = invocation -> {
            List<String> listIds = (List<String>) invocation.getArguments()[0];

            return listIds.stream().map(mesureId -> mapMesures.get(mesureId)).collect(Collectors.toList());
        };
        when(actNormService.fetchMesure(Matchers.anyList(), Matchers.any(CoreSession.class))).thenAnswer(answer);

        Answer<List<Decret>> ans = invocation -> {
            List<String> listIds = (List<String>) invocation.getArguments()[0];

            return listIds.stream().map(decretId -> mapDecrets.get(decretId)).collect(Collectors.toList());
        };
        when(actNormService.fetchDecrets(Matchers.anyList(), Matchers.any(CoreSession.class))).thenAnswer(ans);
    }

    private void createMocksDecrets() {
        mapDecrets = new HashMap<>();

        Decret mockDecret1 = createOneMockDecret(NUMERO_NOR_1, TITRE_OFFICIEL_1);
        Decret mockDecret2 = createOneMockDecret(NUMERO_NOR_2, null);

        mapDecrets.put(NUMERO_NOR_1, mockDecret1);
        mapDecrets.put(NUMERO_NOR_2, mockDecret2);
    }

    private String getFileContent(String filePath) throws IOException {
        return IOUtils.toString(getClass().getClassLoader().getResourceAsStream(filePath), Charset.defaultCharset());
    }
}
