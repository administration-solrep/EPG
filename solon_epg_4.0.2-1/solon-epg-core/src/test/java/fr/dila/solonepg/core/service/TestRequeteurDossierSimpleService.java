package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.service.RequeteurDossierSimpleService;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import fr.dila.st.api.recherche.Recherche;
import java.util.List;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Test;

public class TestRequeteurDossierSimpleService extends AbstractEPGTest {
    private static final String CRITERE_ETAPES = "critereEtapes";
    private static final String CRITERE_SECONDAIRES = "critereSecondaires";
    private static final String CRITERE_PRINCIPAUX = "criterePrincipaux";

    @Inject
    private RequeteurDossierSimpleService rs;

    @Test
    public void testGetRecherches() {
        List<Recherche> recherches = rs.getRecherches();
        Assert.assertEquals(3, recherches.size());
    }

    @Test
    public void testInitialisationEtatPanneau() {
        Assert.assertEquals(true, rs.getRecherche(CRITERE_PRINCIPAUX).getIsFolded());
        Assert.assertEquals(false, rs.getRecherche(CRITERE_SECONDAIRES).getIsFolded());
        Assert.assertEquals(false, rs.getRecherche(CRITERE_ETAPES).getIsFolded());
    }
}
