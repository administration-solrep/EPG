package fr.dila.solonmgpp.page.epg.espaceEpg;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonmgpp.page.epg.commun.SolonEpgPage;

public class CreationPage extends SolonEpgPage {

    public static final String CREATE_DOSSIER = "formCreationDossier:buttonCreationDossier";
    public static final String TYPE_ACTE_LIST_ID = "creation_dossier_100:nxl_creation_dossier_layout_100_a:nxw_type_acte_field_select_one_menu";
    public static final String BOUTON_SUIVANT_ID = "creation_dossier_100:buttonSuivant";
    public static final String BOUTON_TERMINER_PATH = "//input[contains(@id, 'creation_dossier_') and contains(@id, 'buttonTerminer') ]";
    public static final String BOUTON_TERMINER_FORM_PATH = "//form[contains(@id, 'creation_dossier_')]";
    public static final String DOSSIER_ATTACH_ACTE_INTEGRAL = "src/main/attachments/doc2003.doc";
    public static final String DOSSIER_ATTACH_EXTRAIT = "src/main/attachments/doc2004.doc";
    public static final String TELECHARGEMENT_EFFECTUE_MESSAGE = "Téléchargement effectué";
    public static final String MINISTERE_RESPONSABLE = "creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_findButton";
    public static final String DIRECTION_CONCERNE = "creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_findButton";
    
    public void createDossier() {
        getDriver().findElement(By.id(CREATE_DOSSIER)).click();
    }

    protected String getAddFileInput() {
        return "//*[contains(@id, 'createFile') and contains(@id, 'Files:file') and not(contains(@id, 'Items'))]";
    }

    protected String getAddFile() {
        return "//*[contains(@id, 'createFile') and contains(@id, 'ButtonImage')]/img";
    }

    public void setTypeActe(String textValue) {
        final WebElement typeDacteSelect = getDriver().findElement(By.id(TYPE_ACTE_LIST_ID));
        getFlog().action("Selectionne \"" + textValue + "\" dans le select \"Type d'acte\"");
        final Select select = new Select(typeDacteSelect);
        select.selectByVisibleText(textValue);
    }
    
    public void setDirectionConcerne(ArrayList<String> ar) {
        selectDestinataireFromOrganigramme(ar, DIRECTION_CONCERNE);
    }

    public void appuyerBoutonSuivantEtTerminer() {
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.elementToBeClickable(By.id(BOUTON_SUIVANT_ID)));
        getDriver().findElement(By.id(BOUTON_SUIVANT_ID)).click();
        appuyerBoutonTerminer();
    }
    
    public static final String POSTE = "creation_dossier_select_poste:nxl_creation_dossier_layout_select_poste:nxw_dossier_creation_choix_poste_poste_findButton";

    public void setPoste(ArrayList<String> ar) {
        selectMultipleInOrganigramme(ar, POSTE);
    }

    public void appuyerBoutonTerminer() {
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.elementToBeClickable(By.xpath(BOUTON_TERMINER_PATH)));
        getDriver().findElement(By.xpath(BOUTON_TERMINER_PATH)).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(BOUTON_TERMINER_PATH)));
    }
    
    public void setMinistereResponsable(final ArrayList<String> pathToMinistere) {
        selectMultipleInOrganigramme(pathToMinistere, MINISTERE_RESPONSABLE);
    }

    public void ajouterDocuments() {
        getFlog().startAction("attachment d'un dossier sur l'Acte intégral");
        getDriver().findElement(By.xpath("//td[contains(text(),'Acte intégral')]")).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Ajouter document']")));
        getDriver().findElement(By.xpath("//span[text()='Ajouter document']")).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.id("createFileParapheurPanelCDiv")));
        addAttachment(DOSSIER_ATTACH_ACTE_INTEGRAL);
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By.id("createFileParapheurPanelCDiv")));
        getFlog().endAction();

        getFlog().startAction("attachment d'un dossier sur l'Extrait");
        getDriver().findElement(By.xpath("//td[contains(text(),'Extrait')]")).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Ajouter document']")));
        getDriver().findElement(By.xpath("//span[text()='Ajouter document']")).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.id("createFileParapheurPanelCDiv")));
        addAttachment(DOSSIER_ATTACH_EXTRAIT);
        getFlog().endAction();

    }

    public void ajouterDocument(String xpath, String fichier) {
        getFlog().startAction("attachment d'un fichier");
        getDriver().findElement(By.xpath(xpath)).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Ajouter document']")));
        getDriver().findElement(By.xpath("//span[text()='Ajouter document']")).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.id("createFileParapheurPanelCDiv")));
        addAttachment(fichier);
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By.id("createFileParapheurPanelCDiv")));
        getFlog().endAction();
    }

    public void lancerDossier() {
        // <td>clickAndWait</td> <td>//*[@title='Lancer le dossier']</td>
        getDriver().findElement(By.xpath("//*[@title='Lancer le dossier']")).click();
        waitForPageLoaded(getDriver());
    }

}
