package fr.dila.solonepg.page.administration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class GestionModelesPage extends EPGWebPage {

	@FindBy(how = How.XPATH, using = ".//*[@id='createRouteActionsForm']/input[1]")
	private WebElement	creationModele;

	/**
	 * Va vers l'administration
	 * 
	 * @return
	 */
	public CreationModelePage goToCreationModele() {
		return clickToPage(creationModele, CreationModelePage.class);
	}

	public CreationModelePage dupliquer(String nomFdr) {
		getFlog().startAction("Duplication du modèle de FDR : " + nomFdr);
		WebElement lienFDR = getElementByLinkText(nomFdr);
		CreationModelePage page = clickContextMenuToPage(lienFDR, "Dupliquer", CreationModelePage.class);
		getFlog().endAction();
		return page;
	}

	public ViewModelePage ouvrir(String nomFdr) {
		getFlog().startAction("Duplication du modèle de FDR : " + nomFdr);
		WebElement lienFDR = getElementByLinkText(nomFdr);
		ViewModelePage page = clickContextMenuToPage(lienFDR, "Ouvrir", ViewModelePage.class);
		getFlog().endAction();
		return page;
	}
}
