package fr.dila.solonepg.core.cases;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

/**
 * Test unitaire de pattern de recherche/remplacement dans un texte.
 * 
 * @author asatre
 */
public class TestPatternInTexte extends SolonEpgRepositoryTestCase {


    public void testReplaceAll() {
        String result = "CC*SS*".replaceAll("\\*", "%");
        assertEquals("CC%SS%", result);

    }

    public void testPattern() {

        Calendar date = SolonEpgServiceLocator
                .getActiviteNormativeService()
                .extractDateFromTitre(
                        "Loi n°2011-940 du 10 août 2011 modifiant certaines dispositions de la loi n° 2009-879 du 21 juillet 2009 portant réforme de l'hôpital et relative aux patients, à la santé et aux territoires");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM yyyy", Locale.FRENCH);
        String result = sdf.format(date.getTime());
        assertEquals("10 août 2011", result);

    }

    public void INTE_testNor() throws Exception {
        Calendar date = Calendar.getInstance();
        final String annee = Integer.toString(date.get(Calendar.YEAR)).substring(2, 4);

        List<String> listNumeroNor = new ArrayList<String>();
        listNumeroNor.add("CCOZ1100001Y");
        listNumeroNor.add("CCOZ1200008Y");
        listNumeroNor.add("CCOZ1300106Y");
        listNumeroNor.add("CCOZ1200016Y");
        listNumeroNor.add("CCOZ1200004Y");
        listNumeroNor.add("CCOZ1210006Y");
        listNumeroNor.add("CCOZ1200005Y");
        listNumeroNor.add("CCOZ1210001Y");

        int max = 0;
        String numeroDossier = null;
        String anneeDossier = null;
        for (String numeroNor : listNumeroNor) {
            if (StringUtils.isNotBlank(numeroNor) && numeroNor.length() == 12) {
                anneeDossier = numeroNor.substring(4, 6);
                if (annee.equals(anneeDossier)) {
                    numeroDossier = numeroNor.substring(6, numeroNor.length() - 1);
                    int numeroDossierInt = Integer.parseInt(numeroDossier);
                    if (max < numeroDossierInt) {
                        max = numeroDossierInt;
                    }
                }
            }
        }

        assertEquals(max, 10006);

    }

    public void testReplaceFirst() throws Exception {

        String cleanTypeActe = "Projet de loi constitutionnel test";
        cleanTypeActe = cleanTypeActe.replaceFirst("(?iu)Projet de loi constitutionnel", "");
        assertEquals(cleanTypeActe, " test");

        cleanTypeActe = "projet de loi organique test2";
        cleanTypeActe = cleanTypeActe.replaceFirst("(?iu)Projet de loi organique", "");
        assertEquals(cleanTypeActe, " test2");

        cleanTypeActe = "projet de LOI test3";
        cleanTypeActe = cleanTypeActe.replaceFirst("(?iu)Projet de loi", "");
        assertEquals(cleanTypeActe, " test3");

    }

}