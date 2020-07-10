package fr.dila.solonepg.core.cases;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.parapheur.ParapheurModel;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;

/**
 * Tests unitaires sur le modèle de parapheur.
 * 
 * @author arolin
 */
public class TestParapheurModel extends SolonEpgRepositoryTestCase {

    private static final Log log = LogFactory.getLog(TestParapheurModel.class);

    protected DocumentModel documentModelParapheurModel;

    protected ParapheurModel parapheurModel;

    @Override
    public void setUp() throws Exception {

        super.setUp();
        
        openSession();
        DocumentModel docModel = createDocument(SolonEpgParapheurConstants.PARAPHEUR_MODEL_DOCUMENT_TYPE, "newParapheurModelTest");
        documentModelParapheurModel = docModel;

        parapheurModel = docModel.getAdapter(ParapheurModel.class);
    }
    
    @Override
    public void tearDown() throws Exception {
    	closeSession();
    	super.tearDown();
    }

    public void testGetDocument() {

        // on verifie que le document est de type dossier, possède
        // possède le schéma "dossier"
        log.info("begin : test dossier type ");

        DocumentModel dossierModel = parapheurModel.getDocument();
        assertNotNull(dossierModel);
        assertEquals(SolonEpgParapheurConstants.PARAPHEUR_MODEL_DOCUMENT_TYPE, dossierModel.getType());
        String[] schemas = dossierModel.getSchemas();
        boolean foundFondDossierModelSchema = false;
        for (String schema : schemas) {
            if (schema.equals(SolonEpgParapheurConstants.PARAPHEUR_MODEL_DOCUMENT_SCHEMA)) {
                foundFondDossierModelSchema = true;
            }
        }
        assertTrue(foundFondDossierModelSchema);
    }

    public void testParapheurModelProperties() throws ClientException {

        // init metadata variable
        String typeActe = TypeActeConstants.TYPE_ACTE_AMNISTIE;
        
        //
        // get the diverse info property
        parapheurModel.setTypeActe(typeActe);

        parapheurModel.save(session);
        session.save();

        closeSession();
        openSession();

        // check properties persistance

        assertEquals(typeActe,parapheurModel.getTypeActe());

    }
    
}