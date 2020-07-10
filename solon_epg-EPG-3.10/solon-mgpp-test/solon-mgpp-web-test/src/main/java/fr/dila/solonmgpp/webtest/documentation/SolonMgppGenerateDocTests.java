package fr.dila.solonmgpp.webtest.documentation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import fr.dila.solonmgpp.webtest.webdriver010.dossier010.TestConnexion;
import fr.dila.solonmgpp.webtest.webdriver010.dossier030.TestSuite;
import fr.dila.st.annotations.AbstractGenerateDocTests;

public class SolonMgppGenerateDocTests extends AbstractGenerateDocTests {

	public static void main(String[] args) {
		categoriesAndTestsMap = new TreeMap<String, List<String>>();
		
		for (Class clazz : getAllTestsClasses()) {
			updateMapWithTestClass(clazz);
		}

		StringBuilder html = generateHtml("SOLON MGPP");
		
		File htmlFile = new File ("DocumentationTestsSolonMgpp.html");
		
		try {
			FileUtils.writeStringToFile(htmlFile, html.toString());
		} catch (IOException e) {
			System.out.println(e);
		}
		System.out.println("Génération terminée");

	}
	
	private static List<Class> getAllTestsClasses() {
		List<Class> classes = new ArrayList<Class>();
		classes.add(TestConnexion.class);
		classes.add(TestSuite.class);
		classes.add(fr.dila.solonmgpp.webtest.webdriver010.dossier040.TestSuite.class);
		return classes;
	}

}
