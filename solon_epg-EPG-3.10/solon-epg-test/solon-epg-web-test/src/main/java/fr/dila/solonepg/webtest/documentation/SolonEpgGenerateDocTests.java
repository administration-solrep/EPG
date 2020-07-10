package fr.dila.solonepg.webtest.documentation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import fr.dila.solonepg.webtest.webdriver000.TestCritique;
import fr.dila.solonepg.webtest.webdriver010.TestSuite;
import fr.dila.solonepg.webtest.webdriver010.TestSuiteDependant;
import fr.dila.solonepg.webtest.webdriver020.TestSuitePAN;
import fr.dila.st.annotations.AbstractGenerateDocTests;
import fr.sword.naiad.commons.webtest.helper.AbstractWebTest;

public class SolonEpgGenerateDocTests extends AbstractGenerateDocTests {

	public static void main(String[] args) {
		categoriesAndTestsMap = new TreeMap<String, List<String>>();
		
		for (Class<? extends AbstractWebTest> clazz : getAllTestsClasses()) {
			updateMapWithTestClass(clazz);
		}

		StringBuilder html = generateHtml("SOLON EPG");
		
		File htmlFile = new File ("DocumentationTestsSolonEpg.html");
		
		try {
			FileUtils.writeStringToFile(htmlFile, html.toString());
		} catch (IOException e) {
			System.out.println(e);
		}
		System.out.println("Génération terminée");

	}
	
	private static List<Class<? extends AbstractWebTest>> getAllTestsClasses() {
		List<Class<? extends AbstractWebTest>> classes = new ArrayList<Class<? extends AbstractWebTest>>();
		classes.add(TestCritique.class);
		classes.add(TestSuite.class);
		classes.add(TestSuiteDependant.class);
		classes.add(TestSuitePAN.class);
		return classes;
	}

}
