package fr.dila.solonepg.core.recherche.dossier;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.solonepg.api.service.RequeteurDossierSimpleService;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.inject.Inject;
import org.junit.Assert;
import org.nuxeo.ecm.core.api.DocumentModel;

public abstract class AbstractRechercheDossierSimpleTest extends AbstractEPGTest {
    @Inject
    protected RequeteurDossierSimpleService rs;

    /**
     * Renvoie la requête complête de la requête simple
     * @param requeteDossier
     * @return
     */

    protected String outputQuery(RequeteDossierSimple requeteDossier) {
        return rs.getFullQuery(requeteDossier);
    }

    /**
     * Compare le nombre de résultats de la requeteDossier avec le paramêtre expected, et si celle-ci fait apparaître une différence
     * affiche l'helpMessage
     * @param requeteDossier
     * @param expected
     * @param helpMessage
     */
    protected void expectedResults(RequeteDossierSimple requeteDossier, int expected, String helpMessage) {
        List<DocumentModel> results = rs.query(session, requeteDossier);
        Assert.assertEquals(helpMessage, expected, results.size());
    }

    /**
     * Retourne un calendrier
     * @param year
     * @param month
     * @param day
     * @return
     */
    protected Calendar getCalendar(int year, int month, int day) {
        Calendar dateSignature = GregorianCalendar.getInstance();
        dateSignature.set(year, month, day);
        return dateSignature;
    }
}
