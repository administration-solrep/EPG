package fr.dila.solonepg.page.administration;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class TablesDeReferencePage extends EPGWebPage {

    public static final String CABINET_DU_PM_ID = "tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_cabinet_pm_select_findButton";
    public static final String CHARGE_DU_MISSION_ID = "tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_charge_mission_select_findButton";
    public static final String SIGNATAIRE_ID = "tables_reference_properties:nxl_tables_reference_signataires:nxw_signataire_select_findButton";
    public static final String MINISTERE_ID = "tables_reference_properties:nxl_tables_reference_ministere_pm:nxw_ministerePM_select_findButton";
    public static final String DIRECTION_DU_PM_ID = "tables_reference_properties:nxl_tables_reference_ministere_pm:nxw_directionPM_select_findButton";
    public static final String CONSEILLE_DETAT_ID = "tables_reference_properties:nxl_tables_reference_ministere_ce:nxw_ministereCe_select_findButton";
    public static final String LISTE_DE_DIFFUSION = "tables_reference_properties:nxl_tables_reference_vecteur_publication_multiples:nxw_vecteur_publication_multiples_select_findButton";

    public static final String CABINET_DU_PM_ARCEPUSER1_ID = "tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_cabinet_pm_select_tree:node:0:node:0:node:6:node:0:node:0::nxw_cabinet_pm_select_addUsr";
    public static final String CABINET_DU_PM_ARCEPVIG1_ID = "tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_cabinet_pm_select_tree:node:0:node:0:node:6:node:0:node:1::nxw_cabinet_pm_select_addUsr";

    public static final String CHARGE_DU_MISSION_ARCEPUSER1_ID = "tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_charge_mission_select_tree:node:0:node:0:node:6:node:0:node:0::nxw_charge_mission_select_addUsr";
    public static final String CHARGE_DU_MISSION_ARCEPVIG1_ID = "tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_charge_mission_select_tree:node:0:node:0:node:6:node:0:node:1::nxw_charge_mission_select_addUsr";

    public static final String SIGNATAIRE_ARCEPVIG1_ID = "tables_reference_properties:nxl_tables_reference_signataires:nxw_signataire_select_tree:node:0:node:0:node:6:node:0:node:1::nxw_signataire_select_addUsr";
    public static final String SIGNATAIRE_ARCEPUSER1_ID = "tables_reference_properties:nxl_tables_reference_signataires:nxw_signataire_select_tree:node:0:node:0:node:6:node:0:node:0::nxw_signataire_select_addUsr";

    public static final String LISTE_DE_DIFFUSION_ARCEPUSER1_ID = "tables_reference_properties:nxl_tables_reference_vecteur_publication_multiples:nxw_vecteur_publication_multiples_select_tree:node:0:node:0:node:6:node:0:node:0::nxw_vecteur_publication_multiples_select_addUsr";

    public static final String PRM_PREMIER_MINISTERE_ID = "tables_reference_properties:nxl_tables_reference_ministere_pm:nxw_ministerePM_select_tree:node:0:node:62::nxw_ministerePM_select_addMin";

    public static final String CET_CONSEILLE_DETAT = "tables_reference_properties:nxl_tables_reference_ministere_ce:nxw_ministereCe_select_tree:node:0:node:9::nxw_ministereCe_select_addMin";

    public static final String ENREGISTRE_BOUTTON_ID = "tables_reference_properties:button_save_tref";

    @FindBy(id="mode_parution_properties:button_add_modes")
    public WebElement ajouteModeParution;
    
    @FindBy(id="mode_parution_properties:button_save_modes")
    public WebElement saveModeParution;
    
    private List<String> getPathToCabinetDuPm_Arcep() {

        List<String> ar = new ArrayList<String>();
        ar.add("tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_cabinet_pm_select_tree:node:0:node:0::min:handle:img:collapsed");
        ar.add("tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_cabinet_pm_select_tree:node:0:node:0:node:6::dir:handle:img:collapsed");
        ar.add("tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_cabinet_pm_select_tree:node:0:node:0:node:6:node:0::poste:handle:img:collapsed");

        return ar;
    }

    private List<String> getPathToChargeDuMission_Arcep() {
        List<String> ar = new ArrayList<String>();
        ar.add("tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_charge_mission_select_tree:node:0:node:0::min:handle:img:collapsed");
        ar.add("tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_charge_mission_select_tree:node:0:node:0:node:6::dir:handle:img:collapsed");
        ar.add("tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_charge_mission_select_tree:node:0:node:0:node:6:node:0::poste:handle:img:collapsed");
        return ar;
    }

    private List<String> getPathToSignataire_Arcep() {
        List<String> ar = new ArrayList<String>();
        ar.add("tables_reference_properties:nxl_tables_reference_signataires:nxw_signataire_select_tree:node:0:node:0::min:handle:img:collapsed");
        ar.add("tables_reference_properties:nxl_tables_reference_signataires:nxw_signataire_select_tree:node:0:node:0:node:6::dir:handle:img:collapsed");
        ar.add("tables_reference_properties:nxl_tables_reference_signataires:nxw_signataire_select_tree:node:0:node:0:node:6:node:0::poste:handle:img:collapsed");
        return ar;
    }

    private List<String> getPathToListeDeDiffusion_Arcep() {
        List<String> ar = new ArrayList<String>();
        ar.add("tables_reference_properties:nxl_tables_reference_vecteur_publication_multiples:nxw_vecteur_publication_multiples_select_tree:node:0:node:0::min:handle:img:collapsed");
        ar.add("tables_reference_properties:nxl_tables_reference_vecteur_publication_multiples:nxw_vecteur_publication_multiples_select_tree:node:0:node:0:node:6::dir:handle:img:collapsed");
        ar.add("tables_reference_properties:nxl_tables_reference_vecteur_publication_multiples:nxw_vecteur_publication_multiples_select_tree:node:0:node:0:node:6:node:0::poste:handle:img:collapsed");
        return ar;
    }

    private List<String> getDirectionDuPmPath() {
        List<String> ar = new ArrayList<String>();
        ar.add("tables_reference_properties:nxl_tables_reference_ministere_pm:nxw_directionPM_select_tree:node:0:node:0::min:handle:img:collapsed");
        ar.add("tables_reference_properties:nxl_tables_reference_ministere_pm:nxw_directionPM_select_tree:node:0:node:0:node:3::addBtnDir");
        return ar;

    }

    public void setDirectionDuPm() {
        this.selectMultipleInOrganigramme(this.getDirectionDuPmPath(), DIRECTION_DU_PM_ID);
    }

    public void setCabinetDuPm(String select) {
        List<String> pathToArcep = this.getPathToCabinetDuPm_Arcep();
        pathToArcep.add(select);
        this.selectMultipleInOrganigramme(pathToArcep, CABINET_DU_PM_ID);
    }

    public void setChargeDuMission(String select) {
        List<String> pathToArcep = this.getPathToChargeDuMission_Arcep();
        pathToArcep.add(select);
        this.selectMultipleInOrganigramme(pathToArcep, CHARGE_DU_MISSION_ID);
    }

    public void setSignataire(String select) {
        List<String> pathToArcep = this.getPathToSignataire_Arcep();
        pathToArcep.add(select);
        this.selectMultipleInOrganigramme(pathToArcep, SIGNATAIRE_ID);
    }

    public void setListDeDiffusion(String select) {
        List<String> pathToArcep = this.getPathToListeDeDiffusion_Arcep();
        pathToArcep.add(select);
        this.selectMultipleInOrganigramme(pathToArcep, LISTE_DE_DIFFUSION);
    }

    public void setMinistere(String ministere) {
        List<String> ar = new ArrayList<String>();
        ar.add(ministere);
        this.selectMultipleInOrganigramme(ar, MINISTERE_ID);
    }

    public void setConseilleDetat() {
        List<String> ar = new ArrayList<String>();
        ar.add(CET_CONSEILLE_DETAT);
        this.selectMultipleInOrganigramme(ar, CONSEILLE_DETAT_ID);
    }

    public void enregistrer() {
        this.getElementById(ENREGISTRE_BOUTTON_ID).click();
    }
    
   public void ajouteModeParution(String nom) {
       ajouteModeParution.click();
       
       waitForPageSourcePart("Ajout d'un nouveau mode de parution", TIMEOUT_IN_SECONDS);
       String inputIds = "mode_parution_properties:administration_mode_parution:nxl_mode_parution_listing";
       String inputIdsEnd = ":nxw_modeParution";
       String inputId = inputIds + inputIdsEnd;	// Id du champ de la première ligne
       waitForPageSourcePartDisplayed(By.id(inputId), TIMEOUT_IN_SECONDS);
       List<WebElement> inputs = getDriver().findElements(By.xpath("//input[contains(@id, '"+inputIds+"')][contains(@id, '"+inputIdsEnd+"')]"));
       WebElement input = inputs.get(inputs.size() -1);
       fillField("mode de parution", input, nom);
       
       saveModeParution.click();
       
       waitForPageSourcePart("Sauvegarde des modes de parution", TIMEOUT_IN_SECONDS);
   }
}
