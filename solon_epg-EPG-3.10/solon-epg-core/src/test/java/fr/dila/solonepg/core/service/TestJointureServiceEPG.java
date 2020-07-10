package fr.dila.solonepg.core.service;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.solonepg.core.util.RequeteurUtils;
import fr.dila.st.api.recherche.QueryAssembler;
import fr.dila.st.api.service.JointureService;
import fr.dila.st.core.service.STServiceLocator;

public class TestJointureServiceEPG extends SolonEpgRepositoryTestCase {

    private JointureService jointureService;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        openSession();
        jointureService = STServiceLocator.getJointureService();
    }
    
    @Override
    public void tearDown() throws Exception {
    	closeSession();
    	super.tearDown();
    }

    public void testDefaultQueryAssembler(){
    	QueryAssembler defaultAssembler = jointureService.getDefaultQueryAssembler();
    	assertNotNull(defaultAssembler);
    	assertNotNull(RequeteurUtils.getDossierAssembler());
    	assertEquals(defaultAssembler.getClass(), RequeteurUtils.getDossierAssembler().getClass());
    }

    public void testBuildEmptyRequete() {
        String whereClause = "";
      	QueryAssembler assembler = RequeteurUtils.getDossierAssembler();
        assertNotNull(assembler);
        assembler.setWhereClause(whereClause);
        assertEquals("ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d WHERE testAcl(d.ecm:uuid) = 1", assembler.getFullQuery());
    }

    /**
     * Le type principal que l'on requête est le dossier.
     * @throws ClientException
     */
    public void testBuildRequeteDossierOnly() throws ClientException {
        String whereClause = "d.dos:numeroNor = '3'";
    	QueryAssembler assembler = RequeteurUtils.getDossierAssembler();
        assertNotNull(assembler);
        assembler.setWhereClause(whereClause);
        assertEquals("ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d " + "WHERE ((d.dos:numeroNor = '3')) AND testAcl(d.ecm:uuid) = 1", assembler.getFullQuery());
    }

    /**
     * Pour les recherches textuelles à l'intérieur des documents, on a besoin de lier les recherches sur ces fichiers aux recherches faîtes 
     * sur le dossier. Ce test contrôle que cette jointure est effectuée de manière correcte.
     * @throws ClientException
     */
    public void testBuildRequeteDossierAndFile() throws ClientException {
        String whereClause = "d.dos:numeroNor = '3' AND f.dc:nature = 'extrait' AND f.ecm_fulltext = 'HALLO'";
    	QueryAssembler assembler = RequeteurUtils.getDossierAssembler();
        assertNotNull(assembler);
        assembler.setWhereClause(whereClause);
        assertEquals("ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d, FileSolonEpg AS f WHERE ((d.dos:numeroNor = '3' AND f.dc:nature = 'extrait' AND f.ecm_fulltext = 'HALLO') AND f.filepg:relatedDocument = d.ecm:uuid) AND testAcl(d.ecm:uuid) = 1", assembler.getFullQuery());
    }
 
    /**
     * Recherche sur les étapes et sur le dossier
     * @throws ClientException
     */
    public void testConstructionRequeteDossierAndRouteStep() throws ClientException {
        String whereClause = "d.dos:numeroNor = '3' AND e.rtsk:idAuteur = 'JKROWING'";
    	QueryAssembler assembler = RequeteurUtils.getDossierAssembler();
        assertNotNull(assembler);
        assembler.setWhereClause(whereClause);
        assertEquals("ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d,RouteStep AS e WHERE ((d.dos:numeroNor = '3' AND e.rtsk:idAuteur = 'JKROWING')  AND e.rtsk:documentRouteId = d.dos:lastDocumentRoute) AND testAcl(d.ecm:uuid) = 1", assembler.getFullQuery());
    }
    
    /**
     * Recherche sur les étapes
     * @throws ClientException
     */
    public void testConstructionRouteStepOnly() throws ClientException {
        String whereClause = "e.rtsk:idAuteur = 'JKROWING'";
    	QueryAssembler assembler = RequeteurUtils.getDossierAssembler();
        assertNotNull(assembler);
        assembler.setWhereClause(whereClause);
        assertEquals("ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d,RouteStep AS e WHERE ((e.rtsk:idAuteur = 'JKROWING')  AND e.rtsk:documentRouteId = d.dos:lastDocumentRoute) AND testAcl(d.ecm:uuid) = 1", assembler.getFullQuery());
    }
    
}
