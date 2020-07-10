package fr.dila.solonmgpp.webtest.webdriver010.dossier010;

import fr.dila.solonmgpp.page.epg.espaceEpg.AdministrationEpgPageForMGPP;
import fr.dila.solonmgpp.page.epg.espaceEpg.TraitementPageForMGGP;
import fr.dila.solonmgpp.page.epp.AdministrationEppPage;
import fr.dila.solonepp.page.CorbeillePage;
import fr.dila.solonmgpp.webtest.common.AbstractMgppWebTest;
import fr.dila.st.annotations.TestDocumentation;
import fr.sword.naiad.commons.webtest.annotation.WebTest;

public class TestConnexion extends AbstractMgppWebTest {

	/**
	 * test des parametrages EPP
	 * 
	 */
	@WebTest(description = "Parametrage EPP")
	@TestDocumentation(categories = { "Administration EPP", "Paramétrage EPP", "Organigramme EPP" })
	public void connexionEPP() {

		CorbeillePage corbeillePage = loginAsAdminEpp();

		AdministrationEppPage adminEppPage = corbeillePage.navigateToAnotherTab("Administration",
				AdministrationEppPage.class);
		adminEppPage.openCorbeille("Gestion de l'organigramme");
		adminEppPage.createEppParametres();

		logoutEpp();
	}

	/**
	 * test des parametrages MGPP
	 */
	@WebTest(description = "Parametrage MGPP")
	@TestDocumentation(categories = { "Administration EPG", "Paramétrage MGPP" })
	public void connexionEPG() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		AdministrationEpgPageForMGPP adminPage = traitementpage.goToAdministration();
		adminPage.openCorbeille("Paramètres MGPP");
		adminPage.createMgppParametres();
		logoutEpg();
	}

}