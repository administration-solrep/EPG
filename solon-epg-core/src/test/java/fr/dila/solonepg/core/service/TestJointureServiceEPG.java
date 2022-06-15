package fr.dila.solonepg.core.service;

import fr.dila.solonepg.core.test.AbstractEPGTest;
import fr.dila.solonepg.core.util.RequeteurUtils;
import fr.dila.st.api.recherche.QueryAssembler;
import fr.dila.st.api.service.JointureService;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Test;

public class TestJointureServiceEPG extends AbstractEPGTest {
    @Inject
    private JointureService jointureService;

    @Test
    public void testDefaultQueryAssembler() {
        QueryAssembler defaultAssembler = jointureService.getDefaultQueryAssembler();
        Assert.assertNotNull(defaultAssembler);
        Assert.assertNotNull(RequeteurUtils.getDossierAssembler());
        Assert.assertEquals(defaultAssembler.getClass(), RequeteurUtils.getDossierAssembler().getClass());
    }

    @Test
    public void testBuildEmptyRequete() {
        String whereClause = "";
        QueryAssembler assembler = RequeteurUtils.getDossierAssembler();
        Assert.assertNotNull(assembler);
        assembler.setWhereClause(whereClause);
        Assert.assertEquals(
            "ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d WHERE testDossierAcl(d.ecm:uuid) = 1",
            assembler.getFullQuery()
        );
    }

    /**
     * Le type principal que l'on requête est le dossier.
     */
    @Test
    public void testBuildRequeteDossierOnly() {
        String whereClause = "d.dos:numeroNor = '3'";
        QueryAssembler assembler = RequeteurUtils.getDossierAssembler();
        Assert.assertNotNull(assembler);
        assembler.setWhereClause(whereClause);
        Assert.assertEquals(
            "ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d " +
            "WHERE ((d.dos:numeroNor = '3')) AND testDossierAcl(d.ecm:uuid) = 1",
            assembler.getFullQuery()
        );
    }

    /**
     * Pour les recherches textuelles à l'intérieur des documents, on a besoin de lier les recherches sur ces fichiers aux recherches faîtes
     * sur le dossier. Ce test contrôle que cette jointure est effectuée de manière correcte.
     */
    @Test
    public void testBuildRequeteDossierAndFile() {
        String whereClause = "d.dos:numeroNor = '3' AND f.dc:nature = 'extrait' AND f.ecm_fulltext = 'HALLO'";
        QueryAssembler assembler = RequeteurUtils.getDossierAssembler();
        Assert.assertNotNull(assembler);
        assembler.setWhereClause(whereClause);
        Assert.assertEquals(
            "ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d, FileSolonEpg AS f WHERE ((d.dos:numeroNor = '3' AND f.dc:nature = 'extrait' AND f.ecm_fulltext = 'HALLO') AND f.filepg:relatedDocument = d.ecm:uuid) AND testDossierAcl(d.ecm:uuid) = 1",
            assembler.getFullQuery()
        );
    }

    /**
     * Recherche sur les étapes et sur le dossier
     */
    @Test
    public void testConstructionRequeteDossierAndRouteStep() {
        String whereClause = "d.dos:numeroNor = '3' AND e.rtsk:idAuteur = 'JKROWING'";
        QueryAssembler assembler = RequeteurUtils.getDossierAssembler();
        Assert.assertNotNull(assembler);
        assembler.setWhereClause(whereClause);
        Assert.assertEquals(
            "ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d,RouteStep AS e WHERE ((d.dos:numeroNor = '3' AND e.rtsk:idAuteur = 'JKROWING')  AND e.rtsk:documentRouteId = d.dos:lastDocumentRoute) AND testDossierAcl(d.ecm:uuid) = 1",
            assembler.getFullQuery()
        );
    }

    /**
     * Recherche sur les étapes
     */
    @Test
    public void testConstructionRouteStepOnly() {
        String whereClause = "e.rtsk:idAuteur = 'JKROWING'";
        QueryAssembler assembler = RequeteurUtils.getDossierAssembler();
        Assert.assertNotNull(assembler);
        assembler.setWhereClause(whereClause);
        Assert.assertEquals(
            "ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d,RouteStep AS e WHERE ((e.rtsk:idAuteur = 'JKROWING')  AND e.rtsk:documentRouteId = d.dos:lastDocumentRoute) AND testDossierAcl(d.ecm:uuid) = 1",
            assembler.getFullQuery()
        );
    }
}
