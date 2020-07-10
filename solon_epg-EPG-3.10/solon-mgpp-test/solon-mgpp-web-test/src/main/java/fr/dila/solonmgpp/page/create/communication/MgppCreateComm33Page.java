package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm33Page;

public class MgppCreateComm33Page extends AbstractMgppCreateComm {

    public static final String TYPE_COMM = "OEP-03 : Désignation au sein d'un organisme extraparlementaire";
    public static final String DATE_ACTE_INPUTDATE = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_dateActeInputDate";
    public static final String ECHEANCE = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_echeance";
    

    
    public void setDateActe(final String dateActe) {
        final WebElement elem = getDriver().findElement(By.id(DATE_ACTE_INPUTDATE));
        fillField("Date de la lettre du Premier ministre", elem, dateActe);
    }

    public void setEcheance(final String echeance) {
        final WebElement elem = getDriver().findElement(By.id(ECHEANCE));
        fillField("Échéance", elem, echeance);
    }
     
    
    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailComm33Page.class;
    }


}
