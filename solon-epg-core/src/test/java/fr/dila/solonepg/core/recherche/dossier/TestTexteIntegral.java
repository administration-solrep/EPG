package fr.dila.solonepg.core.recherche.dossier;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossier;
import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test de la recherche sur le texte intégral d'EPG. On a pas vraiment de moyen de tester la recherche directement comme
 * on le fait pour les autres panneaux étant donné la forte dépendance à la base de donnée Oracle de l'analyseur
 * fulltext. On va donc comparer la requête directement
 *
 * @author jgomez
 *
 */

public class TestTexteIntegral extends AbstractRechercheDossierSimpleTest {

    /**
     * Une recherche fulltext dans les actes et les fonds de dossier, base d'archivage intermédiaire.
     */
    @Test
    public void testRechercheFulltextBaseInter() {
        RequeteDossierSimple requeteDossierSimple = rs.getRequete(session);
        requeteDossierSimple.setTexteIntegral("coucou");
        requeteDossierSimple.setTIDansActe(true);
        requeteDossierSimple.setTIDansFondDossier(true);
        RequeteDossier requeteDossier = getRequeteDossier(requeteDossierSimple);
        requeteDossier.setDansBaseArchivageIntermediaire(true);
        requeteDossier.setDansBaseProduction(false);
        Assert.assertEquals(Boolean.TRUE, requeteDossier.getDansBaseArchivageIntermediaire());
        Assert.assertEquals(Boolean.FALSE, requeteDossier.getDansBaseProduction());

        Assert.assertNotNull("Le contenu du texte ne doit pas être nul", requeteDossierSimple.getTexteIntegral());
        Assert.assertEquals("coucou", requeteDossierSimple.getTexteIntegral());
        Assert.assertNull(
            "La sous-clause du  texte intégral doit être nulle",
            requeteDossierSimple.getSousClauseTexteIntegral()
        );
        String fullQuery = rs.getFullQuery(requeteDossierSimple);
        Assert.assertNotNull(
            "La sous-clause du  texte intégral ne doit pas être nulle",
            requeteDossierSimple.getSousClauseTexteIntegral()
        );
        Assert.assertEquals(
            "(f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4))",
            //		Assert.assertEquals("(f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4) AND f.ver:islatest = 1)",
            requeteDossierSimple.getSousClauseTexteIntegral()
        );
        Assert.assertEquals(
            "La requête générée n'est pas correcte",
            "ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d, FileSolonEpg AS f WHERE (((d.dos:archive = 1) AND ((f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4)))) AND f.filepg:relatedDocument = d.ecm:uuid) AND testDossierAcl(d.ecm:uuid) = 1",
            //				"ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d, FileSolonEpg AS f WHERE (((d.dos:archive = 1) AND ((f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4) AND f.ver:islatest = 1))) AND f.filepg:relatedDocument = d.ecm:uuid) AND testDossierAcl(d.ecm:uuid) = 1",
            fullQuery
        );
    }

    /**
     * Une recherche fulltext dans les actes et les fonds de dossier, base d'archivage production.
     */
    @Test
    public void testRechercheFulltextBaseProd() {
        RequeteDossierSimple requeteDossierSimple = rs.getRequete(session);
        requeteDossierSimple.setTexteIntegral("coucou");
        requeteDossierSimple.setTIDansActe(true);
        requeteDossierSimple.setTIDansFondDossier(true);
        RequeteDossier requeteDossier = getRequeteDossier(requeteDossierSimple);
        requeteDossier.setDansBaseArchivageIntermediaire(false);
        requeteDossier.setDansBaseProduction(true);
        Assert.assertEquals(Boolean.FALSE, requeteDossier.getDansBaseArchivageIntermediaire());
        Assert.assertEquals(Boolean.TRUE, requeteDossier.getDansBaseProduction());

        Assert.assertNotNull("Le contenu du texte ne doit pas être nul", requeteDossierSimple.getTexteIntegral());
        Assert.assertEquals("coucou", requeteDossierSimple.getTexteIntegral());
        Assert.assertNull(
            "La sous-clause du  texte intégral doit être nulle",
            requeteDossierSimple.getSousClauseTexteIntegral()
        );
        String fullQuery = rs.getFullQuery(requeteDossierSimple);
        Assert.assertNotNull(
            "La sous-clause du  texte intégral ne doit pas être nulle",
            requeteDossierSimple.getSousClauseTexteIntegral()
        );
        Assert.assertEquals(
            "(f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4))",
            //		Assert.assertEquals("(f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4) AND f.ver:islatest = 1)",
            requeteDossierSimple.getSousClauseTexteIntegral()
        );
        Assert.assertEquals(
            "La requête générée n'est pas correcte",
            "ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d, FileSolonEpg AS f WHERE (((d.dos:archive = 0) AND ((f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4)))) AND f.filepg:relatedDocument = d.ecm:uuid) AND testDossierAcl(d.ecm:uuid) = 1",
            //				"ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d, FileSolonEpg AS f WHERE (((d.dos:archive = 0) AND ((f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4) AND f.ver:islatest = 1))) AND f.filepg:relatedDocument = d.ecm:uuid) AND testDossierAcl(d.ecm:uuid) = 1",
            fullQuery
        );
    }

    /**
     * Une recherche fulltext dans les actes et les fonds de dossier, toutes les bases.
     */
    @Test
    public void testRechercheFulltextToutesBases() {
        RequeteDossierSimple requeteDossierSimple = rs.getRequete(session);
        requeteDossierSimple.setTexteIntegral("coucou");
        requeteDossierSimple.setTIDansActe(true);
        requeteDossierSimple.setTIDansFondDossier(true);
        RequeteDossier requeteDossier = getRequeteDossier(requeteDossierSimple);
        requeteDossier.setDansBaseArchivageIntermediaire(true);
        requeteDossier.setDansBaseProduction(true);
        Assert.assertEquals(Boolean.TRUE, requeteDossier.getDansBaseArchivageIntermediaire());
        Assert.assertEquals(Boolean.TRUE, requeteDossier.getDansBaseProduction());

        Assert.assertNotNull("Le contenu du texte ne doit pas être nul", requeteDossierSimple.getTexteIntegral());
        Assert.assertEquals("coucou", requeteDossierSimple.getTexteIntegral());
        Assert.assertNull(
            "La sous-clause du  texte intégral doit être nulle",
            requeteDossierSimple.getSousClauseTexteIntegral()
        );
        String fullQuery = rs.getFullQuery(requeteDossierSimple);
        Assert.assertNotNull(
            "La sous-clause du  texte intégral ne doit pas être nulle",
            requeteDossierSimple.getSousClauseTexteIntegral()
        );
        Assert.assertEquals(
            "(f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4))",
            //		Assert.assertEquals("(f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4) AND f.ver:islatest = 1)",
            requeteDossierSimple.getSousClauseTexteIntegral()
        );
        Assert.assertEquals(
            "La requête générée n'est pas correcte",
            "ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d, FileSolonEpg AS f WHERE ((((f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4)))) AND f.filepg:relatedDocument = d.ecm:uuid) AND testDossierAcl(d.ecm:uuid) = 1",
            //				"ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d, FileSolonEpg AS f WHERE ((((f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4) AND f.ver:islatest = 1))) AND f.filepg:relatedDocument = d.ecm:uuid) AND testDossierAcl(d.ecm:uuid) = 1",
            fullQuery
        );
    }

    private RequeteDossier getRequeteDossier(RequeteDossierSimple requeteDossier) {
        return requeteDossier.getDocument().getAdapter(RequeteDossier.class);
    }
}
