package fr.dila.solonepg.webengine.wsutil;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.spe.WsUtil;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epg.ChercherDossierResponse;
import fr.sword.xsd.solon.epg.DossierEpgWithFile;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePERequest;
import fr.sword.xsd.solon.spe.PEDemandeType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import javax.inject.Inject;
import javax.xml.bind.JAXB;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.mockito.MockitoFeature;
import org.nuxeo.runtime.mockito.RuntimeService;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.xml.sax.SAXException;

@Deploy("fr.dila.solonepg.rest.tests:OSGI-INF/test-mockorga-framework.xml")
@Deploy("fr.dila.solonepg.rest.tests:OSGI-INF/test-mockuser-framework.xml")
@Features(MockitoFeature.class)
public class TestWsUtil extends AbstractEPGTest {
    private static final Log LOGGER = LogFactory.getLog(TestWsUtil.class);

    @Inject
    private FondDeDossierService fondDossierService;

    @Inject
    private ParapheurService parapheurService;

    @Mock
    @RuntimeService
    private STMinisteresService mockMinisteresService;

    @Mock
    @RuntimeService
    private STUsAndDirectionService mockUsAndDirectionService;

    public TestWsUtil() {}

    @Before
    public void setup() {
        EntiteNode entiteNode = Mockito.mock(EntiteNode.class);
        Mockito.when(entiteNode.getLabel()).thenReturn("entiteNode_label");
        Mockito.when(mockMinisteresService.getEntiteNode(Mockito.anyString())).thenReturn(entiteNode);

        UniteStructurelleNode usNode = Mockito.mock(UniteStructurelleNode.class);
        Mockito.when(usNode.getLabel()).thenReturn("uniteStructurelleNode_label");
        Mockito.when(mockUsAndDirectionService.getUniteStructurelleNode(Mockito.anyString())).thenReturn(usNode);
    }

    private Dossier createDossierTest() throws Exception {
        DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
        Dossier dossier = docModel.getAdapter(Dossier.class);
        dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_AVENANT);
        dossier.setStatut(VocabularyConstants.STATUT_INITIE);
        dossier.setNumeroNor("BCFF0758784D");
        dossier.setCategorieActe(VocabularyConstants.NATURE_REGLEMENTAIRE);
        DublincoreSchemaUtils.setCreator(dossier.getDocument(), "ADMIN-SOLON");
        dossier.setVecteurPublication(Collections.singletonList("Journal Officiel"));
        dossier.setPublicationIntegraleOuExtrait(DossierSolonEpgConstants.DOSSIER_INTEGRAL_PROPERTY_VALUE);

        // creation du parapheur avec l'arborescence associée au type d'acte
        fondDossierService.createAndFillFondDossier(dossier, session);

        //creation du parapheur avec l'arborescence associée au type d'acte
        parapheurService.createAndFillParapheur(dossier, session);

        dossier.save(session);
        return dossier;
    }

    @Test
    public void testMockContrib() {
        OrganigrammeService service = STServiceLocator.getOrganigrammeService();
        Assert.assertNotNull(service);
        Assert.assertEquals(MockOrganigrammeService.class, service.getClass());
    }

    @Test
    public void testEnvoyerPremiereDemandePERequest() throws Exception {
        Dossier dossier = createDossierTest();

        DossierEpgWithFile dossierEpgWithFile = WsUtil.getDossierEpgWithFileFromDossier(session, dossier, false);
        Assert.assertNotNull(dossierEpgWithFile.getDossierEpg());

        EnvoyerPremiereDemandePERequest request = new EnvoyerPremiereDemandePERequest();
        request.setTypeDemande(PEDemandeType.EPREUVAGE);
        // ajoute le dossier dans la requete
        request.getDossier().add(dossierEpgWithFile);
        String file = "xml-output/request-publication-avenant.xml";
        compareRequestAgainstFile(request, file);
        Assert.assertTrue(validateOutput(file));
    }

    @Test
    public void testChercherDossier() throws Exception {
        Dossier dossier = createDossierTest();

        DossierEpgWithFile dossierEpgWithFile = WsUtil.getDossierEpgWithFileFromDossier(session, dossier, false);
        Assert.assertNotNull(dossierEpgWithFile.getDossierEpg());

        ChercherDossierResponse chercherDossierResponse = new ChercherDossierResponse();
        chercherDossierResponse.setDernierEnvoi(true);
        chercherDossierResponse.setJeton("31");
        chercherDossierResponse.setStatut(TraitementStatut.OK);
        chercherDossierResponse.getDossier().add(dossierEpgWithFile);
        String file = "xml-output/response-chercherDossier-avenant.xml";
        compareRequestAgainstFile(chercherDossierResponse, file);
        Assert.assertTrue(validateOutput(file));
    }

    private void compareRequestAgainstFile(Serializable request, String filePath) throws IOException {
        logRequest(request);
        StringWriter xmlOutPut = new StringWriter();
        JAXB.marshal(request, xmlOutPut);
        String xmlOuputStr = xmlOutPut.toString();
        File file = getFile(filePath);
        String expectedOutput = FileUtils.readFileToString(file, Charset.defaultCharset());
        Assert.assertEquals(expectedOutput, xmlOuputStr);
    }

    private File getFile(String filePath) {
        URL urlFile = ClassLoader.getSystemResource(filePath);
        File file = new File(urlFile.getFile());
        return file;
    }

    private void logRequest(Serializable request) {
        StringWriter xmlOutPut = new StringWriter();
        JAXB.marshal(request, xmlOutPut);
        LOGGER.info(xmlOutPut);
    }

    public Boolean validateOutput(String filePath) throws IOException, SAXException {
        String rootPath = "xsd/solon/epg/";
        URL wsspeXsdFile = ClassLoader.getSystemResource(rootPath + "WSspe.xsd");
        URL wsepgXsdFile = ClassLoader.getSystemResource(rootPath + "WSepg.xsd");
        URL acteXsdFile = ClassLoader.getSystemResource(rootPath + "actes.xsd");
        URL speCommonsFile = ClassLoader.getSystemResource(rootPath + "spe-commons.xsd");
        URL arFile = ClassLoader.getSystemResource(rootPath + "ar.xsd");
        URL solonCommonsFile = ClassLoader.getSystemResource(rootPath + "solon-commons.xsd");
        Assert.assertNotNull(wsspeXsdFile);
        Source xmlFile = new StreamSource(getFile(filePath));
        SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

        ResourceResolver resolver = new ResourceResolver();
        schemaFactory.setResourceResolver(resolver);
        Schema schema = schemaFactory.newSchema(
            new Source[] {
                new StreamSource(new FileInputStream(speCommonsFile.getFile())),
                new StreamSource(new FileInputStream(acteXsdFile.getFile())),
                new StreamSource(new FileInputStream(solonCommonsFile.getFile())),
                new StreamSource(new FileInputStream(arFile.getFile())),
                new StreamSource(new FileInputStream(wsspeXsdFile.getFile())),
                new StreamSource(new FileInputStream(wsepgXsdFile.getFile()))
            }
        );
        Validator validator = schema.newValidator();

        Boolean validated = false;
        try {
            validator.validate(xmlFile);
            validated = true;
            System.out.println(xmlFile.getSystemId() + " is valid");
        } catch (SAXException e) {
            System.out.println(xmlFile.getSystemId() + " is NOT valid");
            System.out.println("Reason: " + e.getLocalizedMessage());
        }
        return validated;
    }
}
