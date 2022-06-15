package fr.dila.solonmgpp.webengine.wsepp;

import fr.dila.solonepp.rest.api.WSEpp;
import fr.dila.st.rest.client.WSProxyFactory;
import fr.dila.st.rest.helper.JaxBHelper;
import fr.sword.xsd.solon.epp.ChercherCorbeilleRequest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestChercherCorbeilleEpp {
    // private static WSEpp wsEppGvt;

    protected static String endpoint = "http://localhost:8080";

    protected static final String BASEPATH = "solon-epp/site/solonepp/";

    protected static WSEpp getWSEpp(String login, String password) throws Exception {
        WSProxyFactory proxyFactory = new WSProxyFactory(endpoint, BASEPATH, login, null);
        Assert.assertNotNull(proxyFactory);

        WSEpp wsEpp = proxyFactory.getService(WSEpp.class, password);
        Assert.assertNotNull(wsEpp);

        return wsEpp;
    }

    @BeforeClass
    public static void setup() throws Exception {
        // wsEppGvt = getWSEpp("ws-gouvernement", "ws-gouvernement");
        // wsEppAn = getWSEpp("ws-an", "ws-an");
        // wsEppSenat = getWSEpp("ws-senat", "ws-senat");
    }

    /**
     * Recherche des corbeilles du gouvernement
     *
     * @throws Exception
     */
    @Test
    public void chercherCorbeilleGouvernement() throws Exception {
        // GVT recherche la liste de ses corbeilles
        String filename = "fr/dila/solonmgpp/webengine/wsepp/chercherCorbeille/0010 Chercher corbeille.xml";
        ChercherCorbeilleRequest chercherCorbeilleRequest = JaxBHelper.buildRequestFromFile(
            filename,
            ChercherCorbeilleRequest.class
        );
        Assert.assertNotNull(chercherCorbeilleRequest);
        // ChercherCorbeilleResponse chercherCorbeilleResponse = wsEppGvt.chercherCorbeille(chercherCorbeilleRequest);
        // Assert.assertNotNull(chercherCorbeilleResponse);
        // TraitementStatut traitementStatut = chercherCorbeilleResponse.getStatut();
        // Assert.assertNotNull(traitementStatut);
        // Assert.assertEquals(chercherCorbeilleResponse.getMessageErreur(), TraitementStatut.OK, traitementStatut);
        // List<Section> sectionList = chercherCorbeilleResponse.getSection();
        // Assert.assertFalse(sectionList.isEmpty());
        // List<Corbeille> corbeilleList = chercherCorbeilleResponse.getCorbeille();
        // Assert.assertNotNull(corbeilleList);
        // Corbeille corbeille = corbeilleList.get(0);
        // Assert.assertEquals("CORBEILLE_GOUVERNEMENT_RAPPORT", corbeille.getIdCorbeille());
        // Assert.assertEquals("Dépôt de rapport", corbeille.getNom());
        // Assert.assertTrue(corbeille.getDescription().contains("rapports au parlement"));
        // Assert.assertFalse(sectionList.isEmpty());
        // Section section = sectionList.get(0);
        // Assert.assertEquals("SECTION_GOUVERNEMENT_PROCEDURE_LEGISLATIVE", section.getIdSection());
        // Assert.assertEquals("Procédure législative", section.getNom());
        // Assert.assertTrue(section.getDescription().contains("concernant les procédures législatives"));
        // List<Corbeille> subCorbeilleList = section.getCorbeille();
        // Assert.assertFalse(subCorbeilleList.isEmpty());
        // Corbeille subCorbeille = subCorbeilleList.get(0);
        // Assert.assertEquals("CORBEILLE_GOUVERNEMET_PROC_LEG_RECEPTION", subCorbeille.getIdCorbeille());
        // Assert.assertEquals("Reçu", subCorbeille.getNom());
        // Assert.assertTrue(subCorbeille.getDescription().contains("procédure législative dont le gouvernement est destinataire"));
    }
}
