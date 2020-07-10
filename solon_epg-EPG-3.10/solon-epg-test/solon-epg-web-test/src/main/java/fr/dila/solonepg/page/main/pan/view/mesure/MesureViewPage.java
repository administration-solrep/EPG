package fr.dila.solonepg.page.main.pan.view.mesure;

import junit.framework.Assert;

import org.openqa.selenium.WebElement;

import fr.dila.solonepg.page.main.onglet.dossier.detail.TextMaitrePage;

public class MesureViewPage extends TextMaitrePage {

    private static final String MESURE_NUMBER_SPAN_PATH = "//form[@id='texte_maitre_mesures']/table/tbody/tr/td/div/div/span";
    private static final String ADD_MESURE_BTN_ID = "texte_maitre_mesures:addMesure";

    private int getCurrentNumber() {
        WebElement el = getElementByXpath(MESURE_NUMBER_SPAN_PATH);
        String text = el.getText();
        if (text.contains("mesures")) {
            text = text.replace("mesures", "").trim();
        } else if (text.contains("mesure")) {
            text = text.replace("mesure", "").trim();
        }

        return new Integer(text);
    }

    public void ajouterUneMesure() {
        int newIndex = getCurrentNumber() + 1;
        String spanText = newIndex + " mesures";
        String newPath = MESURE_NUMBER_SPAN_PATH + "[contains(text(), '" + spanText + "')]";

        getElementById(ADD_MESURE_BTN_ID).click();

        waitElementVisibilityByXpath(newPath);
    }

    public void assertNombreDesMesures(int index) {
    	String spanText = index + " mesure";
    	if (index > 1) {
    		spanText += "s";
    	}
        
        String newPath = MESURE_NUMBER_SPAN_PATH;
        WebElement elementByXpath = getElementByXpath(newPath);
        String realText = elementByXpath.getText();
        Assert.assertEquals(spanText, realText.trim());
    }

}
