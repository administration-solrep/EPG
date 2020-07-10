package fr.dila.solonepg.core.recherche.dossier;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.solonepg.api.service.RequeteurDossierSimpleService;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

public class RechercheDossierSimpleTestCase extends SolonEpgRepositoryTestCase{

    protected RequeteurDossierSimpleService rs;
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        rs = SolonEpgServiceLocator.getRequeteurDossierSimpleService();
        openSession();
    }
    
    @Override
    public void tearDown() throws Exception {
    	closeSession();
    	super.tearDown();
    }
    
    /**
     * Renvoie la requête complête de la requête simple 
     * @param requeteDossier
     * @return
     * @throws ClientException
     */
    protected String outputQuery(RequeteDossierSimple requeteDossier) throws ClientException {
        return rs.getFullQuery(requeteDossier);
   }
    
    /**
     * Compare le nombre de résultats de la requeteDossier avec le paramêtre expected, et si celle-ci fait apparaître une différence
     * affiche l'helpMessage
     * @param requeteDossier
     * @param expected
     * @param helpMessage
     * @throws ClientException
     */
    protected void expectedResults(RequeteDossierSimple requeteDossier, int expected, String helpMessage) throws ClientException {
        List<DocumentModel> results = rs.query(session,requeteDossier);
        assertEquals(helpMessage,expected,results.size());
    }
    
    /**
     * Retourne un calendrier 
     * @param year
     * @param month
     * @param day
     * @return
     */
    protected Calendar getCalendar(int year,int month, int day) {
        Calendar dateSignature = GregorianCalendar.getInstance();
        dateSignature.set(year,month,day);
        return dateSignature;
    } 
}
