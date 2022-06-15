package fr.dila.solonepg.adamant;

import fr.dila.solonepg.adamant.dao.DossierExtractionDao;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.st.core.sql.PersistenceTestCase;
import org.junit.Ignore;
import org.nuxeo.runtime.api.Framework;

@Ignore
public class DossierExtractionDaoTestCase extends PersistenceTestCase {

    @Ignore("Ne pas exécuter sans avoir lancé nuxéo")
    public void testAddLogEntry() throws Exception {
        DossierExtractionLot entry = doCreateLot();
        assertNotNull(entry);
    }

    private DossierExtractionLot doCreateLot() throws Exception {
        DossierExtractionDao dossierExtractionDao = Framework.getService(DossierExtractionDao.class);
        return dossierExtractionDao.createLot();
    }
}
