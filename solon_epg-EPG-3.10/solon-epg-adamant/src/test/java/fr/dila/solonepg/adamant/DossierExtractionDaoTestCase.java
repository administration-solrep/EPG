package fr.dila.solonepg.adamant;

import org.nuxeo.runtime.api.Framework;

import fr.dila.solonepg.adamant.dao.DossierExtractionDao;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.st.core.sql.PersistenceTestCase;

public class DossierExtractionDaoTestCase extends PersistenceTestCase {

	
	public void testAddLogEntry() throws Exception {
		DossierExtractionLot entry = doCreateLot();
		assertNotNull(entry);
	}
	
	private DossierExtractionLot doCreateLot() throws Exception {
		DossierExtractionDao dossierExtractionDao = Framework.getService(DossierExtractionDao.class);
		return dossierExtractionDao.createLot();
	}
}
