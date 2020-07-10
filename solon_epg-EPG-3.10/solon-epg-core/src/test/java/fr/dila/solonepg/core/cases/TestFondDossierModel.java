package fr.dila.solonepg.core.cases;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModel;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;

/**
 * Tests unitaires sur le modèle de fond de dossier.
 * 
 * @author arolin
 */
public class TestFondDossierModel extends SolonEpgRepositoryTestCase {

    private static final Log log = LogFactory.getLog(TestFondDossierModel.class);

    protected DocumentModel documentModelFondDossierModel;

    protected FondDeDossierModel fondDossierModel;

    @Override
    public void setUp() throws Exception {

        super.setUp();
        
        openSession();
        DocumentModel docModel = createDocument(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_TYPE, "newFondDossierModelTest");
        documentModelFondDossierModel = docModel;

        fondDossierModel = docModel.getAdapter(FondDeDossierModel.class);
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

        DocumentModel dossierModel = fondDossierModel.getDocument();
        assertNotNull(dossierModel);
        assertEquals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_TYPE, dossierModel.getType());
        String[] schemas = dossierModel.getSchemas();
        boolean foundFondDossierModelSchema = false;
        for (String schema : schemas) {
            if (schema.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_SCHEMA)) {
                foundFondDossierModelSchema = true;
            }
        }
        assertTrue(foundFondDossierModelSchema);
    }

    public void testFddModelProperties() throws ClientException {

        // init metadata variable
        String typeActe = TypeActeConstants.TYPE_ACTE_AMNISTIE;
        
        //
        // get the diverse info property
        fondDossierModel.setTypeActe(typeActe);

        fondDossierModel.save(session);
        session.save();

        closeSession();
        openSession();

        // check properties persistance

        assertEquals(typeActe,fondDossierModel.getTypeActe());
    }
}