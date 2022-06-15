package fr.dila.solonepg.core.administration;

import fr.dila.solonepg.api.administration.BulletinOfficiel;
import fr.dila.solonepg.api.constant.SolonEpgBulletinOfficielConstants;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import java.util.List;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Tests unitaires sur les bulletins officiels (metadonnées).
 *
 * @author asatre
 */
public class TestBulletinOfficiel extends AbstractEPGTest {
    @Inject
    private BulletinOfficielService bulletinOfficielService;

    @Test
    public void testCreate() throws Exception {
        DocumentModel docModel = createDocument(
            SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_DOCUMENT_TYPE,
            "newBO"
        );
        BulletinOfficiel bulletinOfficiel = docModel.getAdapter(BulletinOfficiel.class);
        bulletinOfficiel.setIntitule("toto");
        bulletinOfficiel.setNor("NOR");
        bulletinOfficiel.setEtat("running");
        session.saveDocument(bulletinOfficiel.getDocument());
        Assert.assertEquals("assert bulletinOfficiel.getIntitule() == toto", bulletinOfficiel.getIntitule(), "toto");
        Assert.assertEquals("assert bulletinOfficiel.getNor() == NOR", bulletinOfficiel.getNor(), "NOR");
    }

    @Test
    public void testFindAll() throws Exception {
        DocumentModel docModel = createDocument(
            SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_DOCUMENT_TYPE,
            "newBO"
        );
        BulletinOfficiel bulletinOfficiel = docModel.getAdapter(BulletinOfficiel.class);
        bulletinOfficiel.setIntitule("toto");
        bulletinOfficiel.setNor("NOR");
        bulletinOfficiel.setEtat("running");
        session.saveDocument(bulletinOfficiel.getDocument());

        DocumentModel docModel1 = createDocument(
            SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_DOCUMENT_TYPE,
            "newBO1"
        );
        BulletinOfficiel bulletinOfficiel1 = docModel1.getAdapter(BulletinOfficiel.class);
        bulletinOfficiel1.setIntitule("toto");
        bulletinOfficiel1.setNor("NOR");
        bulletinOfficiel1.setEtat("running");
        session.saveDocument(bulletinOfficiel1.getDocument());
        session.save();

        List<DocumentModel> list = bulletinOfficielService.getAllActiveBulletinOfficielOrdered(session);
        Assert.assertNotNull("assert getAllActiveBulletinOfficielOrdered not null", list);
        Assert.assertEquals("assert getAllActiveBulletinOfficielOrdered.size == 2", list.size(), 2);
    }
}
