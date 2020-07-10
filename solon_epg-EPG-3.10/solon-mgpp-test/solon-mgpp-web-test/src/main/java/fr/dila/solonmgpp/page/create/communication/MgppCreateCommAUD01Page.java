package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommAUD01Page;

public class MgppCreateCommAUD01Page extends AbstractMgppCreateComm {

    private static final String DATE_LETTRE_PM_INPUT_DATE_ID = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_dateLettrePmInputDate";

    private static final String PERSONNE_INPUT_ID = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_personne";

    private static final String FONCTION_INPUT_ID = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_fonction";

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailCommAUD01Page.class;
    }

    public void setDateLettrePM(final String dateLettrePM) {
        final WebElement elem = getDriver().findElement(By.id(DATE_LETTRE_PM_INPUT_DATE_ID));
        fillField("Date de la lettre du Premier ministre", elem, dateLettrePM);
    }

    public void setPersonneAuditionne(final String personneAuditionne) {
        final WebElement elem = getDriver().findElement(By.id(PERSONNE_INPUT_ID));
        fillField("Personne à auditionne", elem, personneAuditionne);
    }

    public void setFonction(final String fonction) {
        final WebElement elem = getDriver().findElement(By.id(FONCTION_INPUT_ID));
        fillField("Fonction", elem, fonction);
    }

}
