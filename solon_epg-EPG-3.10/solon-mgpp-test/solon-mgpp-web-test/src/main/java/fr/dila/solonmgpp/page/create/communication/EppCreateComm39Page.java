package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailComm39Page;

public class EppCreateComm39Page extends AbstractEppCreateComm {

    public static final String TYPE_COMM = "PPR-01 : PPR 34-1C - Information du Gouvernement du dépôt";
    public static final String DATE_DEPOT_INPUT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateDepotTexteInputDate";
    public static final String NUM_DEPOT_TXT_INPUT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_numeroDepotTexte";
    
    public EppDetailComm39Page createComm39(String idDossier,String objet, String auteur, String intitule, String dateDepot, String numDepot) {
        checkValue(COMMUNICATION, TYPE_COMM);
        setIdentifiantDossier(idDossier);
        setObjet(objet);
        addAuteur(auteur);
        checkValueContain(INTITULE, intitule);
        setDateDepotTexte(dateDepot);
        setNumeroDepotTexte(numDepot);
        return publier();
    }
    
    public void setDateDepotTexte(String date) {
        final WebElement elem = getDriver().findElement(By.id(DATE_DEPOT_INPUT));
        fillField("Date dépôt texte", elem, date);
      }
    
    public void setNumeroDepotTexte(String numero) {
        final WebElement elem = getDriver().findElement(By.id(NUM_DEPOT_TXT_INPUT));
        fillField("Numero dépôt texte", elem, numero);
      }
    
    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailComm39Page.class;
    }


}
