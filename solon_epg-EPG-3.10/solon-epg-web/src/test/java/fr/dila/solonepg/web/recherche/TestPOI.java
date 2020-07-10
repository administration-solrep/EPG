package fr.dila.solonepg.web.recherche;

import java.io.File;
import java.net.URL;

import org.apache.poi.extractor.ExtractorFactory;
import org.junit.Test;

import junit.framework.Assert;

/** voir Redmine 13749: cette classe de test permet de vérifier que les erreurs relevées en prod font partie du passé **/
public class TestPOI {

	@Test
	public void variousDocs() {
		// Malheureusement, ce fichier ne peut pas être lu avec POI
		Assert.assertFalse(checkFile("fichierErreurPOI3.5.doc"));
		// Avec la version 3.13 de POI, ce fichier DOCX peut être lu
		Assert.assertTrue(checkFile("fichierEnDocX.docx"));	// Fichier du dessus, sauvegardé en docx, avec Word
		Assert.assertTrue(checkFile("Lettredesaisine-contreseingEco_Fin.doc"));	// Fichier issu de la prod, qui ne marchait pas

	}
		
	private boolean checkFile(String fileName) {
		URL url = TestPOI.class.getClassLoader().getResource("doc/"+fileName);
		String filePath = url.getFile();
		File fichierTest = new File(filePath);
		Assert.assertTrue(fichierTest.exists());
		System.out.println(fichierTest.getAbsolutePath());
		Assert.assertTrue(fichierTest.length() > 0);
		try {
			ExtractorFactory.createExtractor(fichierTest);
			System.out.println("Ca marche !");
			return true;
		} catch (Exception e) {
			System.out.println("Echec: "+e.getMessage());
			return false;
		}
	}
}
