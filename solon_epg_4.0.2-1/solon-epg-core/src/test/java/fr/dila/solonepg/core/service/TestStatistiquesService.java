package fr.dila.solonepg.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.service.StatistiquesService;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import java.util.Calendar;
import java.util.Map;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.CloseableCoreSession;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

public class TestStatistiquesService extends AbstractEPGTest {
    @Inject
    private StatistiquesService statsService;

    @Test
    public void testGetUsers() throws Exception {
        DocumentModelList lstUsersActive = statsService.getUsers(true);
        assertNotNull(lstUsersActive);
        assertThat(lstUsersActive).hasSize(6);

        DocumentModelList lstUsersDeleted = statsService.getUsers(false);
        assertNotNull(lstUsersDeleted);
        assertThat(lstUsersDeleted).hasSize(1);
    }

    @Test
    public void testGetNbTypeActePartType() {
        try (CloseableCoreSession session = coreFeature.openCoreSession()) {
            createDoc(session, "d1", 1, "t1");
            createDoc(session, "d2", 2, "t1");
            createDoc(session, "d3", 1, "t2");
            createDoc(session, "d4", 2, "t3");
            createDoc(session, "d5", 3, "t4");
            session.save();
        }

        try (CloseableCoreSession session = coreFeature.openCoreSession()) {
            Calendar dateDebutCal = Calendar.getInstance();
            dateDebutCal.add(Calendar.YEAR, -1);
            Calendar dateFinCal = Calendar.getInstance();
            dateFinCal.add(Calendar.YEAR, 1);
            Map<String, String> types = statsService.getNbTypeActeParType(session, dateDebutCal, dateFinCal);
            Assert.assertFalse(types.isEmpty());
            Assert.assertFalse(types.containsKey("t4"));
            Assert.assertEquals("2", types.get("t1"));
            Assert.assertEquals("1", types.get("t2"));
            Assert.assertEquals("1", types.get("t3"));
        }
    }

    private void createDoc(CoreSession session, String id, int status, String typeActe) {
        DocumentModel doc = createDocumentModel(session, id, DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "/");
        doc.setProperty(
            DossierSolonEpgConstants.DOSSIER_SCHEMA,
            DossierSolonEpgConstants.DOSSIER_STATUT_ARCHIVAGE,
            Integer.toString(status)
        );
        doc.setProperty(
            DossierSolonEpgConstants.DOSSIER_SCHEMA,
            DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_PROPERTY,
            typeActe
        );
        session.saveDocument(doc);
    }
}
