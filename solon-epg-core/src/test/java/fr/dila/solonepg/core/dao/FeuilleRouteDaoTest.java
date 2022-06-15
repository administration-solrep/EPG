package fr.dila.solonepg.core.dao;

import fr.dila.solonepg.api.criteria.EpgFeuilleRouteCriteria;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class FeuilleRouteDaoTest extends AbstractEPGTest {
    public static String EXPECTED_QUERY_PART_CRITERIA_INTITULE = "AND r.dc:title ILIKE ?";
    public static String EXPECTED_QUERY_PART_CRITERIA_NUMERO = "AND r.fdr:numero ILIKE ?";

    @Test
    public void testBuildCriteriaIntitule() {
        EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        criteria.setIntitule("Nom fdr");
        EpgFeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);

        String query = feuilleRouteDao.getQueryString();
        List<Object> paramList = feuilleRouteDao.getParamList();

        Assert.assertTrue(query.contains(EXPECTED_QUERY_PART_CRITERIA_INTITULE));
        Assert.assertTrue(paramList.contains("Nom fdr"));
    }

    @Test
    public void testBuildCriteriaStarBeforeIntitule() {
        EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        criteria.setIntitule("*Nom fdr");
        EpgFeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);

        String query = feuilleRouteDao.getQueryString();
        List<Object> paramList = feuilleRouteDao.getParamList();

        Assert.assertTrue(query.contains(EXPECTED_QUERY_PART_CRITERIA_INTITULE));
        Assert.assertTrue(paramList.contains("%Nom fdr"));
    }

    @Test
    public void testBuildCriteriaStarAfterIntitule() {
        EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        criteria.setIntitule("Nom fdr*");
        EpgFeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);

        String query = feuilleRouteDao.getQueryString();
        List<Object> paramList = feuilleRouteDao.getParamList();

        Assert.assertTrue(query.contains(EXPECTED_QUERY_PART_CRITERIA_INTITULE));
        Assert.assertTrue(paramList.contains("Nom fdr%"));
    }

    @Test
    public void testBuildCriteriaStarInIntitule() {
        EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        criteria.setIntitule("Nom*fdr");
        EpgFeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);

        String query = feuilleRouteDao.getQueryString();
        List<Object> paramList = feuilleRouteDao.getParamList();

        Assert.assertTrue(query.contains(EXPECTED_QUERY_PART_CRITERIA_INTITULE));
        Assert.assertTrue(paramList.contains("Nom%fdr"));
    }

    @Test
    public void testBuildCriteriaWhithoutIntitule() {
        EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        criteria.setNumero("");
        EpgFeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);

        String query = feuilleRouteDao.getQueryString();

        Assert.assertFalse(query.contains(EXPECTED_QUERY_PART_CRITERIA_INTITULE));
    }

    @Test
    public void testBuildCriteriaNumero() {
        EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        criteria.setNumero("15");
        EpgFeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);

        String query = feuilleRouteDao.getQueryString();
        List<Object> paramList = feuilleRouteDao.getParamList();

        Assert.assertTrue(query.contains(EXPECTED_QUERY_PART_CRITERIA_NUMERO));
        Assert.assertTrue(paramList.contains("15"));
    }

    @Test
    public void testBuildCriteriaStarBeforeNumero() {
        EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        criteria.setNumero("*15");
        EpgFeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);

        String query = feuilleRouteDao.getQueryString();
        List<Object> paramList = feuilleRouteDao.getParamList();

        Assert.assertTrue(query.contains(EXPECTED_QUERY_PART_CRITERIA_NUMERO));
        Assert.assertTrue(paramList.contains("%15"));
    }

    @Test
    public void testBuildCriteriaStarAfterNumero() {
        EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        criteria.setNumero("15*");
        EpgFeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);

        String query = feuilleRouteDao.getQueryString();
        List<Object> paramList = feuilleRouteDao.getParamList();

        Assert.assertTrue(query.contains(EXPECTED_QUERY_PART_CRITERIA_NUMERO));
        Assert.assertTrue(paramList.contains("15%"));
    }

    @Test
    public void testBuildCriteriaStarInNumero() {
        EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        criteria.setNumero("1*5");
        EpgFeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);

        String query = feuilleRouteDao.getQueryString();
        List<Object> paramList = feuilleRouteDao.getParamList();

        Assert.assertTrue(query.contains(EXPECTED_QUERY_PART_CRITERIA_NUMERO));
        Assert.assertTrue(paramList.contains("1%5"));
    }

    @Test
    public void testBuildCriteriaWhithoutNumero() {
        EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        criteria.setNumero("");
        EpgFeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);

        String query = feuilleRouteDao.getQueryString();

        Assert.assertFalse(query.contains(EXPECTED_QUERY_PART_CRITERIA_NUMERO));
    }

    @Test
    public void testBuildCriteriaNumeroAndIntitule() {
        EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        criteria.setNumero("15");
        criteria.setIntitule("Nom fdr");
        EpgFeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(session, criteria);

        String query = feuilleRouteDao.getQueryString();
        List<Object> paramList = feuilleRouteDao.getParamList();

        Assert.assertTrue(query.contains(EXPECTED_QUERY_PART_CRITERIA_INTITULE));
        Assert.assertTrue(query.contains(EXPECTED_QUERY_PART_CRITERIA_NUMERO));
        Assert.assertTrue(paramList.size() == 3);
        Assert.assertTrue(paramList.get(1) == "Nom fdr");
        Assert.assertTrue(paramList.get(2) == "15");
    }
}
