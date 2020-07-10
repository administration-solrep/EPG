package fr.dila.solonepg.core.service;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.core.ActiviteNormativeRechercheFixture;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.solonepg.core.util.RequeteurUtils;
import fr.dila.st.api.service.RequeteurService;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.query.Requeteur;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;

/**
 * Test la jointure de la recherche d'activité normative, en déployant également les jointures EPG pour tester d'éventuels problèmes.
 * 
 * @author jgomez
 *
 */
public class TestRechercheActiviteNormative extends SolonEpgRepositoryTestCase {

    private static final Log LOGGER = LogFactory.getLog(TestRechercheActiviteNormative.class);

    private ActiviteNormativeRechercheFixture fixture;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        openSession();
        setUpFixture();
    }

    @Override
    public void tearDown() throws Exception {
        closeSession();
        super.tearDown();
    }

    @Override
    protected void deployRepositoryContrib() throws Exception {
        super.deployRepositoryContrib();

        //deploy RechercheActiviteNormativeContrib
        deployContrib("fr.dila.solonepg.core.tests", "OSGI-INF/test-recherche-an-content-template-contrib.xml");
    }

    private void setUpFixture() throws ClientException {
        fixture = new ActiviteNormativeRechercheFixture(session);
        fixture.initRepo();
    }

    private void checkQuery(final String query, final Integer expectedResultsSize) throws ClientException {
        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, null, query, null);
        assertNotNull(docs);
        assertEquals(expectedResultsSize.intValue(), docs.size());
    }

    public void testSetUp() throws ClientException {
        // Vérification configuration générale
        assertEquals("1", fixture.getLoi().getApplicationLoi());
        assertNotNull(session.getDocument(fixture.getRootRef()));
        assertEquals(6, fixture.getActiviteNormativeCount());

        // Vérification configuration mesure
        final DocumentModelList mesures = session.query("SELECT * FROM ActiviteNormative WHERE texm:champLibre = 'tata'");
        assertEquals(2, mesures.size());
        final MesureApplicative m1 = mesures.get(0).getAdapter(MesureApplicative.class);
        final List<String> mesureIds = fixture.getLoi().getDocument().getAdapter(TexteMaitre.class).getMesuresIds();
        assertEquals(4, mesureIds.size());
        assertNotNull(m1.getId());
        assertTrue(mesureIds.contains(m1.getId()));
        final DocumentModelList lois2 = session.query("SELECT * FROM ActiviteNormative WHERE texm:mesureIds = '" + m1.getId() + "'");
        assertEquals(1, lois2.size());
    }

    /**
     * Test de différentes requêtes - Le premier paramètre de checkout sert pour les messages de debug
     * @throws ClientException
     */
    public void testQueries() throws ClientException {
        checkOutput("Loi", "d.texm:champLibre = 'argh'", 1);
        checkOutput("Mesure 1", "m.texm:champLibre = 'tata'", 1);
        checkOutput("Mesure 2", "m.texm:baseLegale LIKE \"%code%\"", 1);
        checkOutput("Decret", "m.texm:champLibre = 'toto' AND de.texm:champLibre='tete'", 1);
    }

    private void checkOutput(final String debugName, final String whereClause, final Integer expectedValue) throws ClientException {
        final String query = RequeteurUtils.queryApplicationLoi(session, whereClause);
        LOGGER.info("Requête : " + query);
        checkQuery(query, expectedValue);
    }

    /***
     * 
     * Test avec des dates dynamiques. 
     * @throws ClientException 
     * 
     */

    public void testQueriesWithDynamicDates() throws ClientException {
        // Vérifie que la date correspond bien aux conditions (entre -3 et -1 jour))
        final Calendar expectedCal = fixture.getLoi().getDocument().getAdapter(TexteMaitre.class).getDateReunionProgrammation();
        final DateTime joda1 = new DateTime().minusDays(1);
        final DateTime joda2 = new DateTime().minusDays(4);
        final DateTime expectedDate = new DateTime(expectedCal.getTime());
        assertNotNull(expectedCal);
        LOGGER.info("La date d.texm:dateReunionProgrammation est : " + DateUtil.convert(expectedDate));
        assertTrue(expectedDate.isBefore(joda1));
        assertTrue(expectedDate.isAfter(joda2));

        // Vérifie que la requête ramène bien le résultat espéré
        checkOutput("Loi", "d.texm:dateReunionProgrammation BETWEEN ufnxql_date:(NOW-4J) AND ufnxql_date:(NOW-1J)", 1);
        checkOutput("Loi", "d.texm:dateReunionProgrammation BETWEEN ufnxql_date:(NOW-1J) AND ufnxql_date:(NOW+1J)", 0);
        checkOutput("Loi", "d.texm:dateReunionProgrammation BETWEEN ufnxql_date:(NOW-1M) AND ufnxql_date:(NOW+1J)", 1);
        checkOutput("Loi", "d.texm:dateReunionProgrammation BETWEEN ufnxql_date:(NOW-2Y) AND ufnxql_date:(NOW-1Y)", 0);
        checkOutput("Loi", "d.texm:dateReunionProgrammation BETWEEN ufnxql_date:(NOW-2Y) AND ufnxql_date:(NOW+1Y)", 1);
    }

    public void testRequeteurAjoutDateDynamique() {
        final String maRequete = "SELECT * FROM Dossier AS d WHERE d.dos:date1 BETWEEN ufnxql_date:(NOW-2J) AND ufnxql_date:(NOW-1J)";
        final RequeteurService service = STServiceLocator.getRequeteurService();
        final String query = service.resolveKeywords(session, maRequete);
        final Requeteur requeteur = new Requeteur();
        requeteur.setQuery(query);
        requeteur.updateTranslation();
        assertEquals(1, requeteur.getStatements().size());
    }

}
