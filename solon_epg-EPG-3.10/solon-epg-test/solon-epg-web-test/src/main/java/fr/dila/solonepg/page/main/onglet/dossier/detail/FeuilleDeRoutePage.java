package fr.dila.solonepg.page.main.onglet.dossier.detail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonepg.page.administration.CreationEtapePage;
import fr.dila.solonepg.page.commun.AjouterAlaFeuilleDeRoute;
import fr.sword.naiad.commons.webtest.WebPage;

public class FeuilleDeRoutePage extends MenuItemPage {

    private static final String LAST_STEP_EDIT_PATH = "(//a[starts-with(@id, 'dm_route_elements:nxl_feuille_route_instance_listing') and contains(@id, ':titleref')])[last()]";
	public static final String LANCER_DOSSIER_PATH = "//img[@title='Lancer le dossier']";
    public static final String MENU_PATH = "//table[@class='dataOutput']/tbody/tr[last()]//a[contains(@id, 'dm_route_elements:nxl_feuille_route_instance')]";

    public static final String AVIS_FAVORABLE = "//input[@title='Avis favorable']";
    private static final String AVIS_FAVORABLE_AVEC_CORRECTIONS_PATH = "//input[@title='Avis favorable avec corrections']";
    private static final String CORRECTION_EFFECTUER_PATH = "//input[@title='Correction effectuée']";

    @FindBy(how = How.XPATH, xpath = LAST_STEP_EDIT_PATH)
    private WebElement lastStepEditPath;

    /**
     * Lancer le dossier
     */
    public void lancerDossier() {
        clickToPage(findElement(By.xpath(LANCER_DOSSIER_PATH)), WebPage.class);
    }

    /**
     * Avis Favorable avec corrections
     */
    public void avisFavorableAvecCorrections() {
        this.getElementByXpath(AVIS_FAVORABLE_AVEC_CORRECTIONS_PATH).click();
        this.confirmer("Êtes-vous sûr de vouloir valider avec correction l'étape de la feuille de route ?");
    }

    /**
     * Avis Favorable
     */
    public void avisFavorable() {
        this.getElementByXpath(AVIS_FAVORABLE).click();
        this.confirmer("Êtes-vous sûr de vouloir valider l'étape de la feuille de route ?");
    }

    /**
     * Correction effectuée
     */
    public void correctionEffectuer() {
        this.getElementByXpath(CORRECTION_EFFECTUER_PATH).click();
        this.confirmer("Êtes-vous sûr de vouloir valider l'étape de la feuille de route ?");
    }

    public CreationEtapePage ajouterApres() {
        AjouterAlaFeuilleDeRoute ajouterAlaFeuilleDeRoute = clickContextMenu(lastStepEditPath, "Ajouter après", AjouterAlaFeuilleDeRoute.class);
        return ajouterAlaFeuilleDeRoute.ajouterUneEtape();
    }

}
