package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import fr.dila.solonepg.core.test.ActiviteNormativeRechercheFixture;
import fr.dila.solonepg.core.util.RequeteurUtils;
import fr.dila.st.api.service.RequeteurService;
import fr.dila.st.core.query.Requeteur;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.runtime.test.runner.Deploy;

/**
 * Test la jointure de la recherche d'activité normative, en déployant également les jointures EPG pour tester d'éventuels problèmes.
 *
 * @author jgomez
 *
 */
@Deploy("fr.dila.solonepg.core:OSGI-INF/test-recherche-an-content-template-contrib.xml")
public class TestRechercheActiviteNormative extends AbstractEPGTest {
    private static final Log LOGGER = LogFactory.getLog(TestRechercheActiviteNormative.class);

    private ActiviteNormativeRechercheFixture fixture;

    @Before
    public void setUp() {
        setUpFixture();
    }

    private void setUpFixture() {
        fixture = new ActiviteNormativeRechercheFixture(session);
        fixture.initRepo();
    }

    private void checkQuery(final String query, final Integer expectedResultsSize) {
        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, query, null);
        Assert.assertNotNull(docs);
        Assert.assertEquals(expectedResultsSize.intValue(), docs.size());
    }

    @Test
    public void testSetUp() {
        // Vérification configuration générale
        Assert.assertEquals("1", fixture.getLoi().getApplicationLoi());
        Assert.assertNotNull(session.getDocument(fixture.getRootRef()));
        Assert.assertEquals(6, fixture.getActiviteNormativeCount());

        // Vérification configuration mesure
        final DocumentModelList mesures = session.query(
            "SELECT * FROM ActiviteNormative WHERE texm:champLibre = 'tata'"
        );
        Assert.assertEquals(2, mesures.size());
        final MesureApplicative m1 = mesures.get(0).getAdapter(MesureApplicative.class);
        final List<String> mesureIds = fixture.getLoi().getDocument().getAdapter(TexteMaitre.class).getMesuresIds();
        Assert.assertEquals(4, mesureIds.size());
        Assert.assertNotNull(m1.getId());
        Assert.assertTrue(mesureIds.contains(m1.getId()));
        final DocumentModelList lois2 = session.query(
            "SELECT * FROM ActiviteNormative WHERE texm:mesureIds = '" + m1.getId() + "'"
        );
        Assert.assertEquals(1, lois2.size());
    }

    /**
     * Test de différentes requêtes - Le premier paramètre de checkout sert pour les messages de debug
     */
    @Test
    public void testQueries() {
        checkOutput("Loi", "d.texm:champLibre = 'argh'", 1);
        checkOutput("Mesure 1", "m.texm:champLibre = 'tata'", 1);
        checkOutput("Mesure 2", "m.texm:baseLegale LIKE \"%code%\"", 1);
        checkOutput("Decret", "m.texm:champLibre = 'toto' AND de.texm:champLibre='tete'", 1);
    }

    private void checkOutput(final String debugName, final String whereClause, final Integer expectedValue) {
        final String query = RequeteurUtils.queryApplicationLoi(session, whereClause);
        LOGGER.info("Requête : " + query);
        checkQuery(query, expectedValue);
    }

    /***
     *
     * Test avec des dates dynamiques.
     */
    @Test
    public void testQueriesWithDynamicDates() {
        // Vérifie que la date correspond bien aux conditions (entre -3 et -1 jour))
        final Calendar expectedCal = fixture
            .getLoi()
            .getDocument()
            .getAdapter(TexteMaitre.class)
            .getDateReunionProgrammation();
        final DateTime joda1 = new DateTime().minusDays(1);
        final DateTime joda2 = new DateTime().minusDays(4);
        final DateTime expectedDate = new DateTime(expectedCal.getTime());
        Assert.assertNotNull(expectedCal);
        LOGGER.info("La date d.texm:dateReunionProgrammation est : " + DateUtil.convert(expectedDate));
        Assert.assertTrue(expectedDate.isBefore(joda1));
        Assert.assertTrue(expectedDate.isAfter(joda2));

        // Vérifie que la requête ramène bien le résultat espéré
        checkOutput("Loi", "d.texm:dateReunionProgrammation BETWEEN ufnxql_date:(NOW-4J) AND ufnxql_date:(NOW-1J)", 1);
        checkOutput("Loi", "d.texm:dateReunionProgrammation BETWEEN ufnxql_date:(NOW-1J) AND ufnxql_date:(NOW+1J)", 0);
        checkOutput("Loi", "d.texm:dateReunionProgrammation BETWEEN ufnxql_date:(NOW-1M) AND ufnxql_date:(NOW+1J)", 1);
        checkOutput("Loi", "d.texm:dateReunionProgrammation BETWEEN ufnxql_date:(NOW-2Y) AND ufnxql_date:(NOW-1Y)", 0);
        checkOutput("Loi", "d.texm:dateReunionProgrammation BETWEEN ufnxql_date:(NOW-2Y) AND ufnxql_date:(NOW+1Y)", 1);
    }

    @Test
    public void testRequeteurAjoutDateDynamique() {
        final String maRequete =
            "SELECT * FROM Dossier AS d WHERE d.dos:date1 BETWEEN ufnxql_date:(NOW-2J) AND ufnxql_date:(NOW-1J)";
        final RequeteurService service = STServiceLocator.getRequeteurService();
        final String query = service.resolveKeywords(session, maRequete);
        final Requeteur requeteur = new Requeteur();
        requeteur.setQuery(query);
        requeteur.updateTranslation();
        Assert.assertEquals(1, requeteur.getStatements().size());
    }
}
