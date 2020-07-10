package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailComm34Page;

public class EppCreateComm34Page extends AbstractEppCreateComm {

    public static final String TYPE_COMM = "NOM-02 : Avis des assemblées sur une proposition de nomination";

    public static final String DATE_INPUT_DATE = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateInputDate";
    public static final String SENS_AVIS = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_sensAvis";

    public EppDetailComm34Page createComm34(String sensAvis, String dateInputDate) {
        setDateInputDate(dateInputDate);
        setSensAvis(sensAvis);
        return publier();
    }

    public void setDateInputDate(final String dateInputDate) {
        final WebElement elem = getDriver().findElement(By.id(DATE_INPUT_DATE));
        fillField("Date JO", elem, dateInputDate);
    }

    public void setSensAvis(final String sensAvis) {
        final WebElement sensAvisElem = getDriver().findElement(By.id(SENS_AVIS));
        getFlog().action("Selectionne \"" + sensAvis + "\" dans le select \"Sens Avis\"");
        final Select select = new Select(sensAvisElem);
        select.selectByValue(sensAvis);
        // FAVORABLE"
    }
    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailComm34Page.class;
    }

}
