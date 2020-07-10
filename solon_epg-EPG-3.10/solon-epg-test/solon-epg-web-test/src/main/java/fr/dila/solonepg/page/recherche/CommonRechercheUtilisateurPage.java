package fr.dila.solonepg.page.recherche;

import org.openqa.selenium.By;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class CommonRechercheUtilisateurPage extends EPGWebPage {

	public ViewUtilisateurDetailsPage goToUserDetails(String user) {
		getFlog().action("select user " + user);
		findElement(By.partialLinkText(user)).click();
		waitForPageSourcePart("Voir", TIMEOUT_IN_SECONDS);
		return (ViewUtilisateurDetailsPage) getPage(ViewUtilisateurDetailsPage.class);
	}
}
