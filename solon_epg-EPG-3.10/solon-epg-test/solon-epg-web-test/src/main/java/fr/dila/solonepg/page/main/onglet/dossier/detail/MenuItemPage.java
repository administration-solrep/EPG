package fr.dila.solonepg.page.main.onglet.dossier.detail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.dila.solonepg.page.commun.MultiElementFinder;

public class MenuItemPage extends EPGWebPage {

    public static final String VERROUILLER_IMAGE_PATH = "//img[@alt='Verrouiller']";
    public static final String DEVERROUILLER_IMAGE_PATH = "//img[@alt='Déverrouiller']";
    public static final String VERSER_DOSSIER__IMAGE_PATH = "//img[@alt='Verser le dossier']";
    public static final String VALIDER_PATH = "//input[@title='Valider']";

    /**
     * Verrouiller
     */
    public void verrouiller() {
        getFlog().action("Verrouillage du dossier");
        WebElement boutonVerrouiller =
        		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.xpath(VERROUILLER_IMAGE_PATH)));
        boutonVerrouiller.click();
        this.waitElementVisibilityByXpath(DEVERROUILLER_IMAGE_PATH);
    }

    public void verserDossier() {
        this.getElementByXpath(VERSER_DOSSIER__IMAGE_PATH).click();
    }

    public void assertVerrouilleAbsent() {
        this.assertElementNotPresent(By_enum.XPATH, VERROUILLER_IMAGE_PATH);
    }

    public void assertVerrouille() {
        this.assertElementPresent(By_enum.XPATH, VERROUILLER_IMAGE_PATH);
    }

    /**
     * @param assertText to be assertet when popup is opened Popup confirmation
     */
    protected void confirmer(String assertText) {

        // Wait for popup to Open
        this.waitElementVisibilityById("popup_container");

        this.assertTrue(assertText);
        // Confirmer
        this.getElementById("popup_ok").click();

    }

    /**
     * Valider
     */
    public boolean valider() {
        String assertText = "Êtes-vous sûr de vouloir valider l'étape de la feuille de route ?";
        WebElement validerBtn = this.getElementByXpath(VALIDER_PATH);
        // Click the button
        validerBtn.click();
        // Confimer la validation
        confirmer(assertText);

        boolean success = true;
        MultiElementFinder finder = new MultiElementFinder(getDriver());
        // On met l'info en premier, car il est arrivé que 2 messages soient présents: un d'échec, et un de succès
        // Dans ce cas, ce qui nous intéresse, c'est le succès
        int categorie = finder.byCssClass("infoFeedback", "warningFeedback");
        
        if (categorie==2) {
        	String messageErreur = finder.getContent();
        	getFlog().action("La validation d'étape a échoué pour la raison suivante: " + messageErreur);
        	success = false;
        } else if (categorie == 1) {
        	// On vérifie que le dossier est bien repassé à l'état "verrouillé" suite à la validation avec succès
        	getFlog().action("La validation d'étape est effectuée avec le message de succès: "+ finder.getContent());
        }
        return success;
    }

    /**
     * Assert Valider
     */
    public void assertValider() {
        this.assertElementPresent(By_enum.XPATH, VALIDER_PATH);
    }

}
