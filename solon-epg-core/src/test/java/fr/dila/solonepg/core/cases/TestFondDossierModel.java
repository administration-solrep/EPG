package fr.dila.solonepg.core.cases;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModel;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Tests unitaires sur le modèle de fond de dossier.
 *
 * @author arolin
 */
public class TestFondDossierModel extends AbstractEPGTest {
    private static final Log log = LogFactory.getLog(TestFondDossierModel.class);

    protected DocumentModel documentModelFondDossierModel;

    protected FondDeDossierModel fondDossierModel;

    @Before
    public void setUp() throws Exception {
        DocumentModel docModel = createDocument(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_TYPE,
            "newFondDossierModelTest"
        );
        documentModelFondDossierModel = docModel;

        fondDossierModel = docModel.getAdapter(FondDeDossierModel.class);
    }

    @Test
    public void testGetDocument() {
        // on verifie que le document est de type dossier, possède
        // possède le schéma "dossier"
        log.info("begin : test dossier type ");

        DocumentModel dossierModel = fondDossierModel.getDocument();
        Assert.assertNotNull(dossierModel);
        Assert.assertEquals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_TYPE, dossierModel.getType());
        String[] schemas = dossierModel.getSchemas();
        boolean foundFondDossierModelSchema = false;
        for (String schema : schemas) {
            if (schema.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_SCHEMA)) {
                foundFondDossierModelSchema = true;
            }
        }
        Assert.assertTrue(foundFondDossierModelSchema);
    }

    @Test
    public void testFddModelProperties() {
        // init metadata variable
        String typeActe = TypeActeConstants.TYPE_ACTE_AMNISTIE;

        //
        // get the diverse info property
        fondDossierModel.setTypeActe(typeActe);

        fondDossierModel.save(session);
        session.save();

        //        closeSession();
        //        openSession();

        // check properties persistance

        Assert.assertEquals(typeActe, fondDossierModel.getTypeActe());
    }
}
