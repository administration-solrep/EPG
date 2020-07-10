package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailCommSD01Page;

public class EppCreateCommSD01Page extends AbstractEppCreateComm {

    public static final String DATE_DEAMNDE = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateDemandeInputDate";
    public static final String GROUPE_PARLEMETAIRE_INPUT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_groupeParlementaire_suggest";
    public static final String GROUPE_PARLEMETAIRE_SUGGEST = "//table[@id='evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_groupeParlementaire_suggestionBox:suggest']/tbody/tr/td[2]";
    public static final String GROUPE_PARLEMETAIRE_DELETE = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_groupeParlementaire_list:0:nxw_metadonnees_version_groupeParlementaire_delete";

    public EppDetailCommSD01Page createCommSD01(String idDossier, String object, String dateDemande, String groupe) {
//        setIdentifiantDossier("ID1_SD-01");
//        setObjet("objet SD-01");
//        addGroupeParlementaire("groupeAN");
        setIdentifiantDossier(idDossier);
        setObjet(object);
        addGroupeParlementaire(groupe);
        setDateDemande(dateDemande);
        return publier();
    }

    // groupeAN
    public void addGroupeParlementaire(final String groupe) {
        final WebElement elem = getDriver().findElement(By.id(GROUPE_PARLEMETAIRE_INPUT));
        fillField("Auteur", elem, groupe);
        waitForPageSourcePart(By.xpath(GROUPE_PARLEMETAIRE_SUGGEST), TIMEOUT_IN_SECONDS);
        final WebElement suggest = getDriver().findElement(By.xpath(GROUPE_PARLEMETAIRE_SUGGEST));
        suggest.click();
        waitForPageSourcePart(By.id(GROUPE_PARLEMETAIRE_DELETE), TIMEOUT_IN_SECONDS);
    }

    public void setDateDemande(final String dateDemande) {
        final WebElement elem = getDriver().findElement(By.id(DATE_DEAMNDE));
        fillField("Date de la demande", elem, dateDemande);
    }

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailCommSD01Page.class;
    }

}
