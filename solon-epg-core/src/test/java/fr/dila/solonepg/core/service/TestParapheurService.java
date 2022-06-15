package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Tests unitaires sur le service des modèles de parapheur.
 *
 * @author arolin
 */
public class TestParapheurService extends AbstractEPGTest {
    private static final Log log = LogFactory.getLog(TestParapheurService.class);

    @Inject
    private ParapheurService parapheurService;

    /*
     * test de création d'un parapheur dans un  dossier
     */
    @Test
    public void testCreationParapheur() throws Exception {
        log.info("testCreationParapheur");
        //création d'un dossier avec un type d'acte
        DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
        Dossier dossier = docModel.getAdapter(Dossier.class);
        dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_AMNISTIE);

        dossier.save(session);

        //creation du parapheur avec l'arborescence associée au type d'acte
        parapheurService.createAndFillParapheur(dossier, session);

        dossier.save(session);

        Assert.assertNotNull(dossier);
        //récupération parapheur
        DocumentModel parapheurDoc = dossier.getParapheur(session).getDocument();
        Assert.assertNotNull(parapheurDoc);
        List<DocumentModel> paraFolderList = session.getChildren(parapheurDoc.getRef());
        //verification creation arborescence parapheur
        Assert.assertNotNull(paraFolderList);
        Assert.assertEquals(3, paraFolderList.size());
    }

    /*
     * test de récupération des répertoires racines du parapheur sous forme de noeud
     */
    @Test
    public void testGetParapheurRootNode() throws Exception {
        log.info("testGetParapheurRootNode");
        //création d'un dossier avec un type d'acte
        DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
        Dossier dossier = docModel.getAdapter(Dossier.class);
        dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_AMNISTIE);

        //creation du parapheur avec l'arborescence associée au type d'acte
        parapheurService.createAndFillParapheur(dossier, session);

        dossier.save(session);

        Assert.assertNotNull(dossier);
        //récupération parapheur
        DocumentModel parapheurDoc = dossier.getParapheur(session).getDocument();

        List<ParapheurFolder> listParapheurNode = parapheurService.getParapheurRootNode(session, parapheurDoc);

        Assert.assertNotNull(listParapheurNode);
        Assert.assertEquals(3, listParapheurNode.size());
    }

    @Override
    protected DocumentModel createDocument(String type, String id) throws Exception {
        DocumentModel document = session.createDocumentModel(type);
        document.setPathInfo("/", id);
        document = session.createDocument(document);
        return document;
    }
}
