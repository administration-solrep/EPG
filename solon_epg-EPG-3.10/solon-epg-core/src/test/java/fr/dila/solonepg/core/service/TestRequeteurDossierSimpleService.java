package fr.dila.solonepg.core.service;

import java.util.List;

import org.junit.Test;

import fr.dila.solonepg.api.service.RequeteurDossierSimpleService;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.st.api.recherche.Recherche;

public class TestRequeteurDossierSimpleService extends SolonEpgRepositoryTestCase { 

    //TODO : creér un enum pour les Recherches
    private static final String CRITERE_ETAPES = "critereEtapes";
    private static final String CRITERE_SECONDAIRES = "critereSecondaires";
    private static final String CRITERE_PRINCIPAUX = "criterePrincipaux";

    private RequeteurDossierSimpleService rs;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		rs = SolonEpgServiceLocator.getRequeteurDossierSimpleService();
		assertNotNull(rs);
	} 
	


    public void testGetRecherches() {
        List<Recherche> recherches = rs.getRecherches();
        assertEquals(3, recherches.size());
    }
    
    @Test
    public void testInitialisationEtatPanneau() {
        assertEquals(true,rs.getRecherche(CRITERE_PRINCIPAUX).getIsFolded());
        assertEquals(false,rs.getRecherche(CRITERE_SECONDAIRES).getIsFolded());
        assertEquals(false,rs.getRecherche(CRITERE_ETAPES).getIsFolded());
    }
    
}
