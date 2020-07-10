package fr.dila.solonepg.core.recherche.dossier;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossier;
import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;

/**
 * Test de la recherche sur le texte intégral d'EPG. On a pas vraiment de moyen de tester la recherche directement comme
 * on le fait pour les autres panneaux étant donné la forte dépendance à la base de donnée Oracle de l'analyseur
 * fulltext. On va donc comparer la requête directement
 * 
 * @author jgomez
 * 
 */

public class TestTexteIntegral extends RechercheDossierSimpleTestCase {

	/**
	 * Une recherche fulltext dans les actes et les fonds de dossier, base d'archivage intermédiaire.
	 * 
	 * @throws ClientException
	 */
	public void testRechercheFulltextBaseInter() throws ClientException {
		RequeteDossierSimple requeteDossierSimple = rs.getRequete(session);
		requeteDossierSimple.setTexteIntegral("coucou");
		requeteDossierSimple.setTIDansActe(true);
		requeteDossierSimple.setTIDansFondDossier(true);
		RequeteDossier requeteDossier = getRequeteDossier(requeteDossierSimple);
		requeteDossier.setDansBaseArchivageIntermediaire(true);
		requeteDossier.setDansBaseProduction(false);
		assertEquals(Boolean.TRUE, requeteDossier.getDansBaseArchivageIntermediaire());
		assertEquals(Boolean.FALSE, requeteDossier.getDansBaseProduction());

		assertNotNull("Le contenu du texte ne doit pas être nul", requeteDossierSimple.getTexteIntegral());
		assertEquals("coucou", requeteDossierSimple.getTexteIntegral());
		assertNull("La sous-clause du  texte intégral doit être nulle",
				requeteDossierSimple.getSousClauseTexteIntegral());
		String fullQuery = rs.getFullQuery(requeteDossierSimple);
		assertNotNull("La sous-clause du  texte intégral ne doit pas être nulle",
				requeteDossierSimple.getSousClauseTexteIntegral());
		assertEquals("(f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4))",
//		assertEquals("(f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4) AND f.ver:islatest = 1)",
				requeteDossierSimple.getSousClauseTexteIntegral());
		assertEquals(
				"La requête générée n'est pas correcte",
				"ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d, FileSolonEpg AS f WHERE (((d.dos:archive = 1) AND ((f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4)))) AND f.filepg:relatedDocument = d.ecm:uuid) AND testAcl(d.ecm:uuid) = 1",
//				"ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d, FileSolonEpg AS f WHERE (((d.dos:archive = 1) AND ((f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4) AND f.ver:islatest = 1))) AND f.filepg:relatedDocument = d.ecm:uuid) AND testAcl(d.ecm:uuid) = 1",
				fullQuery);
	}

	/**
	 * Une recherche fulltext dans les actes et les fonds de dossier, base d'archivage production.
	 * 
	 * @throws ClientException
	 */
	public void testRechercheFulltextBaseProd() throws ClientException {
		RequeteDossierSimple requeteDossierSimple = rs.getRequete(session);
		requeteDossierSimple.setTexteIntegral("coucou");
		requeteDossierSimple.setTIDansActe(true);
		requeteDossierSimple.setTIDansFondDossier(true);
		RequeteDossier requeteDossier = getRequeteDossier(requeteDossierSimple);
		requeteDossier.setDansBaseArchivageIntermediaire(false);
		requeteDossier.setDansBaseProduction(true);
		assertEquals(Boolean.FALSE, requeteDossier.getDansBaseArchivageIntermediaire());
		assertEquals(Boolean.TRUE, requeteDossier.getDansBaseProduction());

		assertNotNull("Le contenu du texte ne doit pas être nul", requeteDossierSimple.getTexteIntegral());
		assertEquals("coucou", requeteDossierSimple.getTexteIntegral());
		assertNull("La sous-clause du  texte intégral doit être nulle",
				requeteDossierSimple.getSousClauseTexteIntegral());
		String fullQuery = rs.getFullQuery(requeteDossierSimple);
		assertNotNull("La sous-clause du  texte intégral ne doit pas être nulle",
				requeteDossierSimple.getSousClauseTexteIntegral());
		assertEquals("(f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4))",
//		assertEquals("(f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4) AND f.ver:islatest = 1)",
				requeteDossierSimple.getSousClauseTexteIntegral());
		assertEquals(
				"La requête générée n'est pas correcte",
				"ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d, FileSolonEpg AS f WHERE (((d.dos:archive = 0) AND ((f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4)))) AND f.filepg:relatedDocument = d.ecm:uuid) AND testAcl(d.ecm:uuid) = 1",
//				"ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d, FileSolonEpg AS f WHERE (((d.dos:archive = 0) AND ((f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4) AND f.ver:islatest = 1))) AND f.filepg:relatedDocument = d.ecm:uuid) AND testAcl(d.ecm:uuid) = 1",
				fullQuery);
	}

	/**
	 * Une recherche fulltext dans les actes et les fonds de dossier, toutes les bases.
	 * 
	 * @throws ClientException
	 */
	public void testRechercheFulltextToutesBases() throws ClientException {
		RequeteDossierSimple requeteDossierSimple = rs.getRequete(session);
		requeteDossierSimple.setTexteIntegral("coucou");
		requeteDossierSimple.setTIDansActe(true);
		requeteDossierSimple.setTIDansFondDossier(true);
		RequeteDossier requeteDossier = getRequeteDossier(requeteDossierSimple);
		requeteDossier.setDansBaseArchivageIntermediaire(true);
		requeteDossier.setDansBaseProduction(true);
		assertEquals(Boolean.TRUE, requeteDossier.getDansBaseArchivageIntermediaire());
		assertEquals(Boolean.TRUE, requeteDossier.getDansBaseProduction());

		assertNotNull("Le contenu du texte ne doit pas être nul", requeteDossierSimple.getTexteIntegral());
		assertEquals("coucou", requeteDossierSimple.getTexteIntegral());
		assertNull("La sous-clause du  texte intégral doit être nulle",
				requeteDossierSimple.getSousClauseTexteIntegral());
		String fullQuery = rs.getFullQuery(requeteDossierSimple);
		assertNotNull("La sous-clause du  texte intégral ne doit pas être nulle",
				requeteDossierSimple.getSousClauseTexteIntegral());
		assertEquals("(f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4))",
//		assertEquals("(f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4) AND f.ver:islatest = 1)",
				requeteDossierSimple.getSousClauseTexteIntegral());
		assertEquals(
				"La requête générée n'est pas correcte",
				"ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d, FileSolonEpg AS f WHERE ((((f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4)))) AND f.filepg:relatedDocument = d.ecm:uuid) AND testAcl(d.ecm:uuid) = 1",
//				"ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d, FileSolonEpg AS f WHERE ((((f.ecm:fulltext = \"coucou\" AND f.filepg:filetypeId  IN (1,4) AND f.ver:islatest = 1))) AND f.filepg:relatedDocument = d.ecm:uuid) AND testAcl(d.ecm:uuid) = 1",
				fullQuery);
	}

	private RequeteDossier getRequeteDossier(RequeteDossierSimple requeteDossier) {
		return requeteDossier.getDocument().getAdapter(RequeteDossier.class);
	}

}
