package fr.dila.solonepg.core.administration;

import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.administration.BulletinOfficiel;
import fr.dila.solonepg.api.constant.SolonEpgBulletinOfficielConstants;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

/**
 * Tests unitaires sur les bulletins oficiels (metadonnées).
 * 
 * @author asatre
 */
public class TestBulletinOfficiel extends SolonEpgRepositoryTestCase {

    @Override
    public void setUp() throws Exception {

        super.setUp();
        
        openSession();
    }
    
    @Override
    public void tearDown() throws Exception{
    	closeSession();
    	super.tearDown();
    }
    

    public void testCreate() throws Exception {
    	DocumentModel docModel = createDocument(SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_DOCUMENT_TYPE, "newBO");
        BulletinOfficiel bulletinOfficiel = docModel.getAdapter(BulletinOfficiel.class);
        bulletinOfficiel.setIntitule("toto");
        bulletinOfficiel.setNor("NOR");
        bulletinOfficiel.setEtat("running");
        session.saveDocument(bulletinOfficiel.getDocument());
        assertEquals("assert bulletinOfficiel.getIntitule() == toto", bulletinOfficiel.getIntitule(), "toto");
        assertEquals("assert bulletinOfficiel.getNor() == NOR", bulletinOfficiel.getNor(), "NOR");
    }
    
    public void testFindAll() throws Exception {
    	
    	DocumentModel docModel = createDocument(SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_DOCUMENT_TYPE, "newBO");
    	BulletinOfficiel bulletinOfficiel = docModel.getAdapter(BulletinOfficiel.class);
        bulletinOfficiel.setIntitule("toto");
        bulletinOfficiel.setNor("NOR");
        bulletinOfficiel.setEtat("running");
        session.saveDocument(bulletinOfficiel.getDocument());
        
    	DocumentModel docModel1 = createDocument(SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_DOCUMENT_TYPE, "newBO1");
    	BulletinOfficiel bulletinOfficiel1 = docModel1.getAdapter(BulletinOfficiel.class);
        bulletinOfficiel1.setIntitule("toto");
        bulletinOfficiel1.setNor("NOR");
        bulletinOfficiel1.setEtat("running");
        session.saveDocument(bulletinOfficiel1.getDocument());
        session.save();
        
        BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
        List<DocumentModel> list =  bulletinOfficielService.getAllActiveBulletinOfficielOrdered(session);
        assertNotNull("assert getAllActiveBulletinOfficielOrdered not null", list);
        assertEquals("assert getAllActiveBulletinOfficielOrdered.size == 2", list.size(), 2);
        
    }
    
}
