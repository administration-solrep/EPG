package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonmgpp.page.Organigramme;
import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailCommAlerte12Page;

public class EppCreateCommAlerteGeneriquePage extends AbstractEppCreateComm {

    
    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailCommAlerte12Page.class;
    }
    
    public void selectInEPPOrganigramme(final String destinataire, final String id) {
        getFlog().action("Sélection " + destinataire + " dans organigramme");
        final WebElement element = getDriver().findElement(By.id(id));
        final Organigramme organigramme = getPage(Organigramme.class);
        organigramme.openEPPOrganigramme(element);
        organigramme.selectElement(destinataire);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends AbstractDetailComm> T publier() {
        final WebElement elem = getDriver().findElement(By.id(PUBLIER_BTN_ID));
        getFlog().actionClickButton("Publier");
        elem.click();

        waitForPageSourcePart("La communication a été publiée", TIMEOUT_IN_SECONDS);
        return (T) getPage(getCreateResultPageClass());
    }

    
}