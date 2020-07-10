package fr.dila.solonepg.page.parametre;

import org.openqa.selenium.WebElement;

public class ModifierParametre extends ParametreDetailTab {

	public static final String VALEUR_PATH = "//*[contains(@id, 'nxl_parametreLayout:nxw_paramValue')]";

    public void setValeur(String valeur) {
        WebElement el = this.getElementByXpath(VALEUR_PATH);
        this.fillField("Valeur", el, valeur);
    }
}
