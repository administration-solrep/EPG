package fr.dila.solonepg.core.cases;

import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test unitaire de pattern de recherche/remplacement dans un texte.
 *
 * @author asatre
 */
public class TestPatternInTexte extends AbstractEPGTest {

    public void testReplaceAll() {
        String result = "CC*SS*".replaceAll("\\*", "%");
        Assert.assertEquals("CC%SS%", result);
    }

    @Test
    public void testPattern() {
        Calendar date = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .extractDateFromTitre(
                "Loi n°2011-940 du 10 août 2011 modifiant certaines dispositions de la loi n° 2009-879 du 21 juillet 2009 portant réforme de l'hôpital et relative aux patients, à la santé et aux territoires"
            );
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM yyyy", Locale.FRENCH);
        String result = sdf.format(date.getTime());
        Assert.assertEquals("10 août 2011", result);
    }

    @Test
    public void testReplaceFirst() throws Exception {
        String cleanTypeActe = "Projet de loi constitutionnel test";
        cleanTypeActe = cleanTypeActe.replaceFirst("(?iu)Projet de loi constitutionnel", "");
        Assert.assertEquals(cleanTypeActe, " test");

        cleanTypeActe = "projet de loi organique test2";
        cleanTypeActe = cleanTypeActe.replaceFirst("(?iu)Projet de loi organique", "");
        Assert.assertEquals(cleanTypeActe, " test2");

        cleanTypeActe = "projet de LOI test3";
        cleanTypeActe = cleanTypeActe.replaceFirst("(?iu)Projet de loi", "");
        Assert.assertEquals(cleanTypeActe, " test3");
    }
}
