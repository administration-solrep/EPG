package fr.dila.solonepg.webengine.wsutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;

import javax.xml.bind.JAXB;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.xml.sax.SAXException;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.spe.WsUtil;
import fr.dila.solonepg.webengine.SolonEpgEngineTestCase;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epg.ChercherDossierResponse;
import fr.sword.xsd.solon.epg.DossierEpgWithFile;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePERequest;
import fr.sword.xsd.solon.spe.PEDemandeType;

public class TestWsUtil extends SolonEpgEngineTestCase {

    protected FondDeDossierService fondDossierService;
    
    protected ParapheurService parapheurService;
    
    protected STUserService stUserService;
    
    protected UserManager userManager;
    
    private static final Log LOGGER = LogFactory.getLog(TestWsUtil.class);

    public TestWsUtil(){
    	
    }
    
	@Override
	public void setUp() throws Exception {
		super.setUp();
        deployContrib("fr.dila.solonepg.rest.tests","OSGI-INF/test-mockorga-framework.xml");
        deployContrib("fr.dila.solonepg.rest.tests","OSGI-INF/test-mockuser-framework.xml");
        deployContrib("fr.dila.solonepg.rest.tests","OSGI-INF/test-mockministeres-framework.xml");
        deployContrib("fr.dila.solonepg.rest.tests","OSGI-INF/test-mockusanddirection-framework.xml");
		openSession();
		
        // import FondDossier model Service
        fondDossierService = SolonEpgServiceLocator.getFondDeDossierService();
        assertNotNull(fondDossierService);
        
        //import parapheur model Service 
        parapheurService = SolonEpgServiceLocator.getParapheurService();
        assertNotNull(parapheurService);
        
        stUserService = STServiceLocator.getSTUserService();
        assertNotNull(stUserService);
        assertEquals("ADMIN-SOLON ADMIN-SOLON",stUserService.getUserFullName("hg"));
        
//        deployBundle("org.nuxeo.ecm.platform.usermanager");
//        userManager = STServiceLocator.getUserManager();
//        assertNotNull("Manager non présent",userManager);
	}
	
	@Override
	public void tearDown() throws Exception {
		closeSession();
		super.tearDown();
	}

	public Dossier createDossierTest() throws Exception{
	    DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
	    Dossier dossier = docModel.getAdapter(Dossier.class);
	    dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_AVENANT);
	    dossier.setStatut(VocabularyConstants.STATUT_INITIE);
	    dossier.setNumeroNor("BCFF0758784D");
	    dossier.setCategorieActe(VocabularyConstants.NATURE_REGLEMENTAIRE);	    
        DublincoreSchemaUtils.setCreator(dossier.getDocument(), "ADMIN-SOLON");
	    dossier.setVecteurPublication(Collections.singletonList("Journal Officiel"));
        dossier.setPublicationIntegraleOuExtrait(DossierSolonEpgConstants.DOSSIER_IN_EXTENSO_PROPERTY_VALUE);
	    
	    // creation du parapheur avec l'arborescence associée au type d'acte
	    fondDossierService.createAndFillFondDossier(dossier, session);
        
        //creation du parapheur avec l'arborescence associée au type d'acte
        parapheurService.createAndFillParapheur(dossier, session);
        
	    dossier.save(session);
	    return dossier;
	}
	
	
	public void testMockContrib(){
    	OrganigrammeService service = STServiceLocator.getOrganigrammeService();
    	assertNotNull(service);
    	assertEquals(MockOrganigrammeService.class, service.getClass());
	}
	
    public void testEnvoyerPremiereDemandePERequest() throws Exception {
    	Dossier dossier = createDossierTest();

    	DossierEpgWithFile dossierEpgWithFile = WsUtil.getDossierEpgWithFileFromDossier(session, dossier, false);
    	assertNotNull(dossierEpgWithFile.getDossierEpg());
    	
        EnvoyerPremiereDemandePERequest request = new EnvoyerPremiereDemandePERequest();
        request.setTypeDemande(PEDemandeType.EPREUVAGE);
        // ajoute le dossier dans la requete
        request.getDossier().add(dossierEpgWithFile);
        String file = "xml-output/request-publication-avenant.xml";
		compareRequestAgainstFile(request, file);
		assertTrue(validateOutput(file));
    }

    public void testChercherDossier() throws Exception {
    	Dossier dossier = createDossierTest();

     	DossierEpgWithFile dossierEpgWithFile = WsUtil.getDossierEpgWithFileFromDossier(session, dossier, false);
    	assertNotNull(dossierEpgWithFile.getDossierEpg());
      	
    	ChercherDossierResponse chercherDossierResponse = new ChercherDossierResponse();
    	chercherDossierResponse.setDernierEnvoi(true);
    	chercherDossierResponse.setJeton("31");
    	chercherDossierResponse.setStatut(TraitementStatut.OK);
    	chercherDossierResponse.getDossier().add(dossierEpgWithFile);
        String file = "xml-output/response-chercherDossier-avenant.xml";
    	compareRequestAgainstFile(chercherDossierResponse, file);
		assertTrue(validateOutput(file));
    }

    
    
	private void compareRequestAgainstFile(Serializable request, String filePath) throws IOException {
		logRequest(request);
		StringWriter xmlOutPut = new StringWriter();
		JAXB.marshal(request, xmlOutPut);
		String xmlOuputStr = xmlOutPut.toString();
		File file = getFile(filePath);
		String expectedOutput = FileUtils.readFileToString(file);
		assertEquals(expectedOutput, xmlOuputStr);
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
	
	public Boolean validateOutput(String filePath) throws IOException, SAXException{
		String rootPath = "xsd/solon/epg/";
		URL wsspeXsdFile = ClassLoader.getSystemResource(rootPath + "WSspe.xsd");
		URL wsepgXsdFile = ClassLoader.getSystemResource(rootPath + "WSepg.xsd");
		URL acteXsdFile = ClassLoader.getSystemResource(rootPath + "actes.xsd");
		URL speCommonsFile = ClassLoader.getSystemResource(rootPath + "spe-commons.xsd");
		URL arFile = ClassLoader.getSystemResource(rootPath + "ar.xsd");
		URL solonCommonsFile = ClassLoader.getSystemResource(rootPath + "solon-commons.xsd");
		assertNotNull(wsspeXsdFile);
		Source xmlFile = new StreamSource(getFile(filePath));
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

	    ResourceResolver resolver = new ResourceResolver();
	    schemaFactory.setResourceResolver(resolver);
	    Schema schema = schemaFactory.newSchema(new Source[] {
	    		new StreamSource(new FileInputStream(speCommonsFile.getFile())),
	    		new StreamSource(new FileInputStream(acteXsdFile.getFile())),
	    		new StreamSource(new FileInputStream(solonCommonsFile.getFile())),
	    		new StreamSource(new FileInputStream(arFile.getFile())),
	    		new StreamSource(new FileInputStream(wsspeXsdFile.getFile())),
	    		new StreamSource(new FileInputStream(wsepgXsdFile.getFile()))

	    		
	    });
		Validator validator = schema.newValidator();
		
		Boolean validated = false;
		try {
		  validator.validate(xmlFile);
		  validated = true;
		  System.out.println(xmlFile.getSystemId() + " is valid");
		}
		catch (SAXException e) {
		  System.out.println(xmlFile.getSystemId() + " is NOT valid");
		  System.out.println("Reason: " + e.getLocalizedMessage());
		}
		return validated;
	}

}
