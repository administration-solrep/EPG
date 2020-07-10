package fr.dila.solonepg.page.parametre;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.sword.naiad.commons.webtest.WebPage;

public class ParametreDetailTab extends EPGWebPage {

	public static final String ENREGISTRER_BTN_XPATH = "//input[contains(@value, 'Enregistrer') and contains(@class, 'button')]";

    public void enregistrer() {
        clickToPage(getElementByXpath(ENREGISTRER_BTN_XPATH), WebPage.class);
    }
}
