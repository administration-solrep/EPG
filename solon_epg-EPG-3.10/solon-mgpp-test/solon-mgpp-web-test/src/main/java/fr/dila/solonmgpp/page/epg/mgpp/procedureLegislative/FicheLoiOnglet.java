package fr.dila.solonmgpp.page.epg.mgpp.procedureLegislative;

import org.openqa.selenium.By;

import fr.dila.solonmgpp.page.commun.CommonWebPage;

public class FicheLoiOnglet extends CommonWebPage{

    private static final String SAVE_BOUTON = "metadonnee_dossier:save_fiche";
    private static final String OBSERVATION = "metadonnee_dossier:nxl_fiche_loi:nxw_fiche_loi_observation";
    private static final String GENERER_COURRIER_BOUTON = "//*[@value='Générer courrier']";

    public void setObservation(String observation){
        getDriver().findElement(By.id(OBSERVATION)).sendKeys(observation);
    }

    public void save(){
        getDriver().findElement(By.id(SAVE_BOUTON)).click();
    }

    public void setObservationAndSave(final String observation){
        setObservation(observation);
        save();
        waitForPageLoaded(getDriver());

        checkElementPresent(By_enum.XPATH, GENERER_COURRIER_BOUTON, Mode.EDIT);
    }

}
