package fr.dila.solonmgpp.page.epg.mgpp.procedureLegislative;

import org.openqa.selenium.By;

import fr.dila.solonmgpp.page.commun.CommonWebPage;

public class HistoriqueEppOnglet extends CommonWebPage {

	private final String	lex01_link					= "//*[@id='historique_epp']/div/div/div/div/table/tbody/tr/td[3]/a/span";
	private final String	lex03_link					= "//*[@id='historique_epp']/div/div/div/div/div/table/tbody/tr/td[3]/a/span";
	private final String	lex14_link					= "//*[@id='historique_epp']/div/div/div/div/div/div/table/tbody/tr/td[3]/a/span";
	private final String	lex16_link					= "//*[@id='historique_epp']/div/div/div/div/div/div/div/table/tbody/tr/td[3]/a/span";
	private final String	lex17_link					= "//*[@id='historique_epp']/div/div/div/div/div/div/div/div/table/tbody/tr/td[3]/a/span";
	private final String	lex18_link					= "//*[@id='historique_epp']/div/div/div/div/div/div/div/div/div/table/tbody/tr/td[3]/a/span";
	private final String	lex16_link_2				= "//*[@id='historique_epp']/div/div/div/div/div/div/div/div/div/div/table/tbody/tr/td[3]/a/span";
	private final String	lex17_link_2				= "//*[@id='historique_epp']/div/div/div/div/div/div/div/div/div/div/div/table/tbody/tr/td[3]/a/span";
	private final String	identifiantCommunication	= "//*[@id='meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_identifiant']";

	public void checkAllValues(final String dossier_id) {
		assertTrue("LEX-01 : Pjl - Dépôt");
		getDriver().findElement(By.xpath(lex01_link)).click();
		waitForPageLoaded(getDriver());
		assertTrue(dossier_id + "_00000");
		assertTrue("LEX-03 : Pjl - Enregistrement du dépôt");

		navigateToAnotherTab("Historique EPP", HistoriqueEppOnglet.class);
		getDriver().findElement(By.xpath(lex03_link)).click();
		waitForPageLoaded(getDriver());

		checkValue(By_enum.XPATH, identifiantCommunication, dossier_id + "_00001", Boolean.FALSE);

		assertTrue(dossier_id + "_00001");
		assertTrue("LEX-14 : Engagement de la procédure accélérée");

		navigateToAnotherTab("Historique EPP", HistoriqueEppOnglet.class);
		getDriver().findElement(By.xpath(lex14_link)).click();
		waitForPageLoaded(getDriver());

		assertTrue(dossier_id + "_00002");
		assertTrue("LEX-16 : Pjl - Transmission ou notification du rejet");

		navigateToAnotherTab("Historique EPP", HistoriqueEppOnglet.class);
		getDriver().findElement(By.xpath(lex16_link)).click();
		waitForPageLoaded(getDriver());

		assertTrue(dossier_id + "_00003");
		assertTrue("LEX-17 : Pjl - Navettes diverses");

		navigateToAnotherTab("Historique EPP", HistoriqueEppOnglet.class);
		getDriver().findElement(By.xpath(lex17_link)).click();
		waitForPageLoaded(getDriver());

		assertTrue(dossier_id + "_00004");
		assertTrue("LEX-18 : Pjl - Enregistrement du dépôt en navette");

		navigateToAnotherTab("Historique EPP", HistoriqueEppOnglet.class);
		getDriver().findElement(By.xpath(lex18_link)).click();
		waitForPageLoaded(getDriver());

		assertTrue(dossier_id + "_00005");
		assertTrue("LEX-16 : Pjl - Transmission ou notification du rejet");

		navigateToAnotherTab("Historique EPP", HistoriqueEppOnglet.class);
		getDriver().findElement(By.xpath(lex16_link_2)).click();
		waitForPageLoaded(getDriver());

		assertTrue(dossier_id + "_00006");
		assertTrue("LEX-17 : Pjl - Navettes diverses");

		navigateToAnotherTab("Historique EPP", HistoriqueEppOnglet.class);
		getDriver().findElement(By.xpath(lex17_link_2)).click();
		waitForPageLoaded(getDriver());

		assertTrue(dossier_id + "_00007");
	}
}
