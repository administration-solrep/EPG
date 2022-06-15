package fr.dila.solonepg.core.cases;

import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.parapheur.ParapheurModel;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Tests unitaires sur le modèle de parapheur.
 *
 * @author arolin
 */
public class TestParapheurModel extends AbstractEPGTest {
    private static final Log log = LogFactory.getLog(TestParapheurModel.class);

    protected DocumentModel documentModelParapheurModel;

    protected ParapheurModel parapheurModel;

    @Before
    public void setUp() throws Exception {
        DocumentModel docModel = createDocument(
            SolonEpgParapheurConstants.PARAPHEUR_MODEL_DOCUMENT_TYPE,
            "newParapheurModelTest"
        );
        documentModelParapheurModel = docModel;

        parapheurModel = docModel.getAdapter(ParapheurModel.class);
    }

    @Test
    public void testGetDocument() {
        // on verifie que le document est de type dossier, possède
        // possède le schéma "dossier"
        log.info("begin : test dossier type ");

        DocumentModel dossierModel = parapheurModel.getDocument();
        Assert.assertNotNull(dossierModel);
        Assert.assertEquals(SolonEpgParapheurConstants.PARAPHEUR_MODEL_DOCUMENT_TYPE, dossierModel.getType());
        String[] schemas = dossierModel.getSchemas();
        boolean foundFondDossierModelSchema = false;
        for (String schema : schemas) {
            if (schema.equals(SolonEpgParapheurConstants.PARAPHEUR_MODEL_DOCUMENT_SCHEMA)) {
                foundFondDossierModelSchema = true;
            }
        }
        Assert.assertTrue(foundFondDossierModelSchema);
    }

    @Test
    public void testParapheurModelProperties() {
        // init metadata variable
        String typeActe = TypeActeConstants.TYPE_ACTE_AMNISTIE;

        //
        // get the diverse info property
        parapheurModel.setTypeActe(typeActe);

        parapheurModel.save(session);
        session.save();

        //        closeSession();
        //        openSession();

        // check properties persistance

        Assert.assertEquals(typeActe, parapheurModel.getTypeActe());
    }
}
