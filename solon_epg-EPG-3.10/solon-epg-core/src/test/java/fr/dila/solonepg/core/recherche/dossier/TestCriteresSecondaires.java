package fr.dila.solonepg.core.recherche.dossier;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.core.domain.ComplexeTypeImpl;


//TODO: Directives, application et loi pour plus tard
//TODO: Indexation pour plus tard
public class TestCriteresSecondaires extends RechercheDossierSimpleTestCase{

    private Dossier dossier1;

    
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
	    dossier1.setCategorieActe("1");
        Calendar dateSignature = getCalendar(2005,5,14);
	    dossier1.setDateSignature(dateSignature);
        Calendar datePublication = getCalendar(2009,5,14);
        dossier1.setDatePublication(datePublication);
        List<ComplexeType> directives = dossier1.getTranspositionDirective();
        ComplexeType directive = new ComplexeTypeImpl();
        Map<String,Serializable> map = new HashMap<String, Serializable>();
        map.put(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_COMMENTAIRE_PROPERTY,"Hello world");
        map.put(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY,"a36b");
        map.put(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_COMMENTAIRE_PROPERTY,"test");
        directive.setSerializableMap(map);
        directives.add(directive);
        
        RetourDila retourDila = dossier1.getDocument().getAdapter(RetourDila.class);
        retourDila.setNumeroTexteParutionJorf("203-213");
        
	    session.saveDocument(dossier1.getDocument());
	    session.save();
    }

   public void testCategorieActe() throws ClientException {
        RequeteDossierSimple requeteDossier = rs.getRequete(session);
        requeteDossier.setIdCategorieActe("1");
        expectedResults(requeteDossier,1,"Recherche erronée, il y a un dossier avec un identifiant avec une catégorie d'acte égal à 1");
        requeteDossier.setIdCategorieActe("2");
        expectedResults(requeteDossier,0,"Recherche erronée, il n'y a pas de dossier avec avec un identifiant de catégorie d'acte égal à 2");
   }
   
   public void testDateSignature() throws ClientException {
       RequeteDossierSimple requeteDossier = rs.getRequete(session);
       requeteDossier.setDateSignature_1(getCalendar(2001,5,14));
       requeteDossier.setDateSignature_2(getCalendar(2008,5,14));
       expectedResults(requeteDossier,1,"Recherche erronée, il y a un dossier signé entre 2001 et 2008");
       requeteDossier.setDateSignature_1(getCalendar(2008,5,14));
       requeteDossier.setDateSignature_2(getCalendar(2009,5,14));
       expectedResults(requeteDossier,0,"Recherche erronée, il n'y a pas de dossier signé entre 2008 et 2009");
   }
   
   public void testDatePublication() throws ClientException {
       RequeteDossierSimple requeteDossier = rs.getRequete(session);
       requeteDossier.setDatePublication_1(getCalendar(2008,5,14));
       requeteDossier.setDatePublication_2(getCalendar(2010,5,14));
       expectedResults(requeteDossier,1,"Recherche erronée, il y a un dossier publié entre 2001 et 2008");
       requeteDossier.setDatePublication_1(getCalendar(2006,5,14));
       requeteDossier.setDatePublication_2(getCalendar(2007,5,14));
       expectedResults(requeteDossier,0,"Recherche erronée, il n'y a pas de dossier publié entre 2006 et 2007");
   }
   
   public void testNumeroTexte() throws ClientException {
       RequeteDossierSimple requeteDossier = rs.getRequete(session);
       requeteDossier.setNumeroTexte("203-213");
       expectedResults(requeteDossier,1,"Recherche erronée, il y a un dossier de numéro 203-213");
       requeteDossier.setNumeroTexte("104");
       expectedResults(requeteDossier,0,"Recherche erronée, il n'y a pas de dossier de numéro 104");
   }

}
