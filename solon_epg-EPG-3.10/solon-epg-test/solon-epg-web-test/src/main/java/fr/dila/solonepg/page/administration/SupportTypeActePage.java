package fr.dila.solonepg.page.administration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.EPGWebPage;

public abstract class SupportTypeActePage extends EPGWebPage {

    public static final String TYPE_ACTE_TEMPLATE = "@RP1@_model_properties:nxl_@RP1@_modele:nxw_type_acte_@RP2@_model_motsCleLabel";

    public static final String PAGE_SOURCE_TEMPLATE = "//*[contains(@id,'@RP1@_model_properties:nxl_@RP1@_modele:nxw_type_acte_@RP2@_model_index_suggestBox:suggest')]/tbody/tr[1]/td";

    public static final String SUGGEST_SOURCE_ID_TEMPLATE = "@RP1@_model_properties:nxl_@RP1@_modele:nxw_type_acte_@RP2@_model_index_suggestBox:suggest";

    public static final String SUGGEST_ITEM_PATH_TEMPLATE = "//*[@id='@RP1@_model_properties:nxl_@RP1@_modele:nxw_type_acte_@RP2@_model_index_suggestBox:suggest']/tbody/tr[1]/td";

    public static final String EDIT_BUTTON = "@RP1@_model_properties:nxl_@RP1@_modele:editerModelefondDossierWidget";

    public void addtypeActe(final String typeActe, final String typeActeValue) {
        String typeActeRep = this.replaceTemplate(TYPE_ACTE_TEMPLATE);
        WebElement elem = getDriver().findElement(By.id(typeActeRep));
        fillField(" Type d'acte", elem, "");

        elem = getDriver().findElement(By.id(typeActeRep));
        fillField(" Type d'acte", elem, typeActe);
        waitForPageSourcePart(By.xpath(this.replaceTemplate(PAGE_SOURCE_TEMPLATE)), TIMEOUT_IN_SECONDS);

        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.id(this.replaceTemplate(SUGGEST_SOURCE_ID_TEMPLATE))));

        final WebElement suggest = getDriver().findElement(By.xpath(this.replaceTemplate(SUGGEST_ITEM_PATH_TEMPLATE)));
        suggest.click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.textToBePresentInElementValue(By.id(typeActeRep), typeActeValue));

    }

    public void editTypeActe(final String typeActeValue) {
        final WebElement elem = getDriver().findElement(By.id(this.replaceTemplate(EDIT_BUTTON)));
        elem.click();
        waitForPageSourcePart(By.xpath(String.format("//*[@class='treeModelTitle']//*[contains(string(), '%s')]", typeActeValue)), TIMEOUT_IN_SECONDS);
        waitForPageSourcePart("folder_explore.png", TIMEOUT_IN_SECONDS);
    }

    private String replaceTemplate(String template) {
        String[] replacement = this.getTypeActeReplacement();

        template = template.replace("@RP1@", replacement[0]);
        template = template.replace("@RP2@", replacement[1]);

        return template;

    }

    protected abstract String[] getTypeActeReplacement();
}
