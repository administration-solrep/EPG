package fr.dila.solonepg.page.commun;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProfilUtilisateurPage extends EPGWebPage {

    public static final String NB_DOSSIER_PAGE_ID = "displayedProfilUtilisateurPopup:profilUtilisateurForm:nxl_profil_utilisateur_premieres_options:pageSizeSelector_nxw_nb_dossiers_pages";
    private static final String ANNULER_BTN_ID = "displayedProfilUtilisateurPopup:profilUtilisateurForm:annulerProfilUtilisateur";
    public static final String DOSSIER_URGENT_ID = "displayedProfilUtilisateurPopup:profilUtilisateurForm:nxl_profil_utilisateur_notification_layout_dossier:nxw_dossier_urgent";
    public static final String RETOUR_SGG_ID = "displayedProfilUtilisateurPopup:profilUtilisateurForm:nxl_profil_utilisateur_notification_layout_dossier:nxw_retour_sgg";
    public static final String NOTIFIER_PAR_MEL = "displayedProfilUtilisateurPopup:profilUtilisateurForm:nxl_profil_utilisateur_notification_layout_fdr:nxw_mail_si_franchissement_automatique";
    public static final String VALIDER_BTN_ID = "displayedProfilUtilisateurPopup:profilUtilisateurForm:validerProfilUtilisateur";
    public static final String CORBEILLE_AJOUTER_TOUT_BTN_PATH = "//a[@id='displayedProfilUtilisateurPopup:profilUtilisateurForm:metadonneesDisponibleEspaceTraitementcopyAlllink']";
    public static final String CORBEILLE_ENLEVER_TOUT_BTN_PATH = "//a[@id='displayedProfilUtilisateurPopup:profilUtilisateurForm:metadonneesDisponibleEspaceTraitementremovelink']";

    /**
     * Annuler Fermer le popup
     */
    public void annuler() {
    	waitElementVisibilityById(ANNULER_BTN_ID);
        getElementById(ANNULER_BTN_ID).click();
    }

    /**
     * @param checkBoxId
     */
    public void assertNotChecked(String checkBoxId) {
        Assert.assertFalse(isCheckBoxSelected(checkBoxId));
    }

    /**
     * @param checkBoxId
     */
    public void assertChecked(String checkBoxId) {
        Assert.assertTrue(isCheckBoxSelected(checkBoxId));
    }

    /**
     * select the checkBox
     * 
     * @param checkBoxId
     */
    public void selectCheckBox(String checkBoxId) {
    	waitElementVisibilityById(checkBoxId);
        getElementById(checkBoxId).click();
    }

    public void addTypeActe(final String typeActe, final String typeActeValue) {
        WebElement typeActeInput = getElementById("displayedProfilUtilisateurPopup:profilUtilisateurForm:nxl_profil_utilisateur_notification_layout_dossier:nxw_type_acte_notification_motsCleLabel");
        fillField("", typeActeInput, typeActe);
        waitForPageSourcePartDisplayed(By.id("displayedProfilUtilisateurPopup:profilUtilisateurForm:nxl_profil_utilisateur_notification_layout_dossier:nxw_type_acte_notification_index_suggestBox:suggest"), TIMEOUT_IN_SECONDS);
        WebElement suggestBox = getElementByXpath(".//*[@id='displayedProfilUtilisateurPopup:profilUtilisateurForm:nxl_profil_utilisateur_notification_layout_dossier:nxw_type_acte_notification_index_suggestBox:suggest']/tbody/tr[1]/td");
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.textToBePresentInElement(suggestBox, typeActeValue));

        WebElement eleToSelect = getElementByXpath(".//*[@id='displayedProfilUtilisateurPopup:profilUtilisateurForm:nxl_profil_utilisateur_notification_layout_dossier:nxw_type_acte_notification_index_suggestBox:suggest']/tbody/tr[1]/td");
        eleToSelect.click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.textToBePresentInElementValue(By.id("displayedProfilUtilisateurPopup:profilUtilisateurForm:nxl_profil_utilisateur_notification_layout_dossier:nxw_type_acte_notification_motsCleLabel"), typeActeValue));

        // Register selection
        getElementByXpath("//a[@id='displayedProfilUtilisateurPopup:profilUtilisateurForm:nxl_profil_utilisateur_notification_layout_dossier:addTypeActeProfilUtilisateurWidget']/img").click();
        
        waitElementVisibilityById("displayedProfilUtilisateurPopup:profilUtilisateurForm:nxl_profil_utilisateur_notification_layout_dossier:nxw_type_acte_notification_list:0:nxw_type_acte_notification_delete");
    }

    public void removeTypeActe() {
    	
        String removeBtn = "//a[@id='displayedProfilUtilisateurPopup:profilUtilisateurForm:nxl_profil_utilisateur_notification_layout_dossier:nxw_type_acte_notification_list:0:nxw_type_acte_notification_delete']/img";
        waitElementVisibilityByXpath(removeBtn);
        getElementByXpath(removeBtn).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(removeBtn)));
    }

    /**
     * Valide
     */
    public void valider() {
        getElementById(VALIDER_BTN_ID).click();
        waitForPageSourcePartHide(By.id("displayedProfilUtilisateurPopup:profilUtilisateurPanelCDiv"), TIMEOUT_IN_SECONDS);
    }

    public void ajouterTout() {
        WebElement el = getElementByXpath(CORBEILLE_AJOUTER_TOUT_BTN_PATH);
        String id = el.getAttribute("id");
        this.executeScript("document.getElementById('" + id + "').click();");
        sleep(2);
    }

    public void enleverTout() {
        WebElement el = getElementByXpath(CORBEILLE_ENLEVER_TOUT_BTN_PATH);
        String id = el.getAttribute("id");
        executeScript("document.getElementById('" + id + "').click();");
        sleep(2);
    }

    public void setList(String description, String listName, String value) {
        WebElement reqEl = getElementById(listName);
        getFlog().action("Selectionne \"" + value + "\" dans le select \"" + description + "\"");
        Select select = new Select(reqEl);
        select.selectByValue(value);
	}
}
