package fr.dila.solonepg.page.administration;

import org.openqa.selenium.By;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class ViewModelePage extends EPGWebPage {

	public GestionModelesPage retourListe() {
		getDriver().findElement(By.xpath("//input[@value='Retour à la liste']")).click();
		return (GestionModelesPage) getPage(GestionModelesPage.class);
	}

	public GestionModelesPage validerFDR() {
		getDriver().findElement(By.xpath("//input[@value='Valider le modèle']")).click();
		return (GestionModelesPage) getPage(GestionModelesPage.class);

	}

	public ViewModelePage libererVerrou() {

		getDriver().findElement(By.xpath("//input[@value='Libérer le verrou']")).click();
		return (ViewModelePage) getPage(ViewModelePage.class);
	}
}
