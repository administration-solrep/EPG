package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.core.recherche.query.ActiviteNormativeQueryAssembler;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import fr.dila.solonepg.core.util.RequeteurUtils;
import fr.dila.st.core.util.SolonDateConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test la jointure de la recherche d'activité normative, en déployant également les jointures EPG pour tester d'éventuels problèmes.
 * @author jgomez
 *
 */
public class TestJointureServiceActiviteNormative extends AbstractEPGTest {
    private static final String UFNXQL_AN_QUERY = "ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM ActiviteNormative AS d";

    private static final Log LOGGER = LogFactory.getLog(TestRechercheActiviteNormative.class);

    @Test
    public void testDefaultQueryAssembler() {
        Assert.assertNotNull(RequeteurUtils.getRechercheActiviteNormativeAssembler());
    }

    @Test
    public void testBuildEmptyRequete() {
        ActiviteNormativeQueryAssembler assembler = (ActiviteNormativeQueryAssembler) RequeteurUtils.getRechercheActiviteNormativeAssembler(
            null,
            StringUtils.EMPTY
        );
        Assert.assertNotNull(assembler);
        Assert.assertEquals(UFNXQL_AN_QUERY, assembler.getFullQuery());
    }

    @Test
    public void testNavigationActiviteNormative() {
        for (ActiviteNormativeEnum anEnum : ActiviteNormativeEnum.values()) {
            checkEmptyQuery(anEnum, " WHERE d.norma:" + anEnum.getAttributSchema() + " = '1'");
        }
    }

    private void checkEmptyQuery(ActiviteNormativeEnum anEnum, String constraint) {
        ActiviteNormativeQueryAssembler assembler = (ActiviteNormativeQueryAssembler) RequeteurUtils.getRechercheActiviteNormativeAssembler(
            anEnum,
            StringUtils.EMPTY
        );
        Assert.assertEquals(UFNXQL_AN_QUERY + constraint, assembler.getFullQuery());
    }

    @Test
    public void testApplicationLoi() {
        checkOutput(
            " requête sur des lois ",
            "d.textm:numeroNor = A",
            UFNXQL_AN_QUERY + " WHERE ((d.textm:numeroNor = A)) AND d.norma:applicationLoi = '1'"
        );
        checkOutput(
            " requête sur des mesures ",
            "d.textm:numeroNor = A AND m.textm:codeModifie = M",
            UFNXQL_AN_QUERY +
            ", ActiviteNormative AS m WHERE ((d.textm:numeroNor = A AND m.textm:codeModifie = M) AND d.texm:mesureIds = m.ecm:uuid ) AND d.norma:applicationLoi = '1'"
        );
        checkOutput(
            " requête sur des décrets ",
            "d.textm:numeroNor = A AND m.textm:codeModifie = M AND de.textm:champLibre",
            UFNXQL_AN_QUERY +
            ", ActiviteNormative AS m, ActiviteNormative AS de  WHERE ((d.textm:numeroNor = A AND m.textm:codeModifie = M AND de.textm:champLibre) AND d.texm:mesureIds = m.ecm:uuid AND m.texm:decretIds = de.ecm:uuid) AND d.norma:applicationLoi = '1'"
        );
        checkOutput(
            " requête sur des mesures seuls ",
            "m.textm:champLibre",
            UFNXQL_AN_QUERY +
            ", ActiviteNormative AS m WHERE ((m.textm:champLibre) AND d.texm:mesureIds = m.ecm:uuid ) AND d.norma:applicationLoi = '1'"
        );
        checkOutput(
            " requête sur des décrets seuls ",
            "de.textm:champLibre",
            UFNXQL_AN_QUERY +
            ", ActiviteNormative AS m, ActiviteNormative AS de  WHERE ((de.textm:champLibre) AND d.texm:mesureIds = m.ecm:uuid AND m.texm:decretIds = de.ecm:uuid) AND d.norma:applicationLoi = '1'"
        );
    }

    @Test
    public void testDateDynamique() {
        DateTime yesterday = new DateTime().minusDays(1);
        String yesterdayStr = SolonDateConverter.DATE_DASH_REVERSE.format(yesterday.toDate());

        DateTime threeDaysBefore = new DateTime().minusDays(3);
        String threeDaysBeforeStr = SolonDateConverter.DATE_DASH_REVERSE.format(threeDaysBefore.toDate());

        checkOutputRequeteur(
            " requête avec une date dynamique ",
            "d.texm:datePromulgation = ufnxql_date:(NOW-1J)",
            UFNXQL_AN_QUERY +
            " WHERE ((d.texm:datePromulgation = DATE '" +
            yesterdayStr +
            "')) AND d.norma:applicationLoi = '1'"
        );
        checkOutputRequeteur(
            " requête avec deux dates dynamiques ",
            "d.texm:datePromulgation BETWEEN ufnxql_date:(NOW-3J) AND ufnxql_date:(NOW-1J)",
            UFNXQL_AN_QUERY +
            " WHERE ((d.texm:datePromulgation BETWEEN DATE '" +
            threeDaysBeforeStr +
            "' AND DATE '" +
            yesterdayStr +
            "')) AND d.norma:applicationLoi = '1'"
        );
    }

    private void checkOutput(String message, String where, String output) {
        checkOutputJointureOnly(message + " - jointure", where, output);
        checkOutputRequeteur(message + " - requeteur", where, output);
    }

    /**
     * Vérification des requêtes en utilisant le service de jointure uniquement, sans la résolution des mots clés
     * @param message le message de débug
     * @param where la clause where
     * @param output la sortie attendue
     */
    private void checkOutputJointureOnly(String message, String where, String output) {
        LOGGER.info("Début de la" + message);
        ActiviteNormativeQueryAssembler assembler = RequeteurUtils.getApplicationLoiAssembler(where);
        Assert.assertEquals(output, assembler.getFullQuery());
        LOGGER.info("[ where = " + where + " ; " + assembler.getFullQuery() + "]");
        LOGGER.info("Fin de la " + message);
    }

    /**
     * Vérification des requêtes en utilisant le service de requêteur uniquement, avec la résolution des mots clés
     * @param message le message de débug
     * @param where la clause where
     * @param output la sortie attendue
     */
    private void checkOutputRequeteur(String message, String where, String output) {
        LOGGER.info("Début de la - " + message);
        Assert.assertEquals(output, RequeteurUtils.queryApplicationLoi(session, where));
        LOGGER.info("Fin de la " + message);
    }
}