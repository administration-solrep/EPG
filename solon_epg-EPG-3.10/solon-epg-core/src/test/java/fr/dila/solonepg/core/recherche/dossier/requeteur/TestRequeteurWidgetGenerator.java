package fr.dila.solonepg.core.recherche.dossier.requeteur;

import java.util.Collection;

import fr.dila.solonepg.api.constant.SolonEpgRequeteurConstant;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.st.api.service.STRequeteurWidgetGeneratorService;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Teste la génération des widgets pour le requêteur EPG.
 * @author jgomez
 *
 */

public class TestRequeteurWidgetGenerator  extends SolonEpgRepositoryTestCase {
   
    /**
     * Teste la récupération de toutes les catégories utilisées dans la contribution
     */
    public void testGetCategories() throws Exception {
    	final STRequeteurWidgetGeneratorService generatorService = STServiceLocator.getSTRequeteurWidgetGeneratorService();
    	assertNotNull("Le service de génération des widgets n'existe pas", generatorService);
    	
        Collection<String>  categories =  generatorService.getRequeteurContribution(SolonEpgRequeteurConstant.REQUETEUR_EXPERT_DOSSIER_CONTRIBUTION).getCategories();
        assertNotNull(categories);
        assertEquals(true,categories.contains("dossier"));
        assertEquals(true,categories.contains("tous"));
        assertEquals(true,categories.contains("etape"));
    }
    
}
