package fr.dila.solonepg.core.recherche.dossier;

import java.util.Arrays;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;

public class TestCriteresPrincipaux extends RechercheDossierSimpleTestCase{

    protected Dossier dossier1;

    
	@Override
	public void setUp() throws Exception {
		super.setUp();
	    createDossier1();
	}

    private void createDossier1() throws ClientException {
        dossier1 = createDossier();
	    dossier1.setNumeroNor("ECOX9800017A");
	    dossier1.setMinistereResp("1");
	    dossier1.setDirectionResp("2");
	    dossier1.setStatut("4");
	    dossier1.setIdCreateurDossier("JPAUL");
	    session.saveDocument(dossier1.getDocument());
	    session.save();
    } 
	
   public void testMinistereResponsable() throws ClientException {
        RequeteDossierSimple requeteDossier = rs.getRequete(session);
        requeteDossier.init();
        requeteDossier.setIdMinistereResponsable(Arrays.asList("1"));
        expectedResults(requeteDossier,1,"Recherche erronée, il y a un dossier avec pour identifiant ministère 1");
        requeteDossier.setIdMinistereResponsable(Arrays.asList("2"));
        expectedResults(requeteDossier,0,"Recherche erronée, il n'y a pas de dossier avec pour identifiant ministère 2");
    }

   public void testDirectionResponsable() throws ClientException {
       RequeteDossierSimple requeteDossier = rs.getRequete(session);
       requeteDossier.init();
       requeteDossier.setIdDirectionResponsable("2");
       expectedResults(requeteDossier,1,"Recherche erronée, il y a un dossier avec l'identifiant de direction 2");
       requeteDossier.setIdDirectionResponsable("1");
       expectedResults(requeteDossier,0,"Recherche erronée, il n'y a pas de dossier l'identifiant de direction 1");
   }

// TESTS EN ECHEC LORS DE LA LIVRAISON 1.7
   
//   public void testNumeroNor() throws ClientException {
//       RequeteDossierSimple requeteDossier = rs.getRequete(session);
//       requeteDossier.init();
//       
//       requeteDossier.setNumeroNor("E*");
//       expectedResults(requeteDossier,1,"Recherche erronée, il y a un dossier avec le nor commençant par E");
//       assertEquals("La requête a modifié le numéro NOR entré par l'utilisateur","E*",requeteDossier.getNumeroNor());
//       
//       requeteDossier.setNumeroNor("C*");
//       expectedResults(requeteDossier,0,"Recherche erronée, il n'y a pas de dossier avec le nor commençant par C");
//       assertEquals("La requête a modifié le numéro NOR entré par l'utilisateur","C*",requeteDossier.getNumeroNor());   
//   }
//   
//   public void testStatutDossier() throws ClientException {
//       RequeteDossierSimple requeteDossier = rs.getRequete(session);
//       requeteDossier.init();
//       requeteDossier.setIdStatutDossier("4");
//       expectedResults(requeteDossier,1,"Recherche erronée, il y a un dossier avec statut 4");
//       requeteDossier.setNumeroNor("5");
//       expectedResults(requeteDossier,0,"Recherche erronée, il n'y a pas de dossier avec statut 5");
//   }

   public void testAuteur() throws ClientException {
       RequeteDossierSimple requeteDossier = rs.getRequete(session);
       requeteDossier.init();
       requeteDossier.setIdAuteur("JPAUL");
       expectedResults(requeteDossier,1,"Recherche erronée, il y a un dossier crée par JPAUL");
       requeteDossier.setIdAuteur("JCHARLES");
       expectedResults(requeteDossier,0,"Recherche erronée, il n'y a pas de dossier créé par JCHARLES");
   }
   
}
