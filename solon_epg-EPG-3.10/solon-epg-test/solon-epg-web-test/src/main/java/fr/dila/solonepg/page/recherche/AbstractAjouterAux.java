package fr.dila.solonepg.page.recherche;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.sword.naiad.commons.webtest.WebPage;

public abstract class AbstractAjouterAux extends EPGWebPage {

    /**
     * Set Intitule
     */
    public void setIntitule(String value) {
        WebElement el = findElement(By.id(getIntituleId()));
        fillField("", el, value);
    }

    protected abstract String getIntituleId();

    /**
     * Set Destinataire
     */
    public void setDestinataire(String dest) {
        selectInOrganigramme(dest, getFindButtonId());
    }

    protected abstract String getFindButtonId();

    /**
     * Set Destinataire avec expand
     */
    public void setDestinataire(List<String> ar) {
        selectMultipleInOrganigramme(ar, getFindButtonId());
    }

    /**
     * Ajouter
     */
    public void ajouter() {
        clickToPage(findElement(By.xpath(getAjouterBouttonPath())), WebPage.class);
    }

    protected abstract String getAjouterBouttonPath();

}
