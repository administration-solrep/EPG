package fr.dila.solonepg.page.administration;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.EPGWebPage;

/**
 * 
 * @author admin
 */
public class GestionDesMigrationsPage extends EPGWebPage {

    public static final String CREATE_DOSSIER = "formCreationDossier:buttonCreationDossier";
    public static final String TYPE_ACTE_LIST_ID = "creation_dossier_100:nxl_creation_dossier_layout_100_a:nxw_type_acte_field_select_one_menu";
    public static final String BOUTON_SUIVANT_ID = "creation_dossier_100:buttonSuivant";
    public static final String FIND_ANCIEN_POSTE = "a4jChangementGouvernement:nxl_changement_gouvernement_poste_old_element:nxw_old_poste_field_findButton";
    public static final String FIND_NEW_POSTE = "a4jChangementGouvernement:nxl_changement_gouvernement_poste_new_element:nxw_new_poste_field_findButton";
    public static final String DIRECTION_CONCERNE = "creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_findButton";
    public static final String BOUTON_TERMINER_ID = "creation_dossier_100:buttonTerminer";
    public static final String VALIDER_CHANGEMENT_GOUVERNEMENT_BUTTON_ID = "a4jChangementGouvernement:validerChangementGouvernement";
    public static final String MINISTERE_CHECKBOX_ID = "a4jChangementGouvernement:nxl_changement_gouvernement_type_element:nxw_typeMigration_select:0";
    public static final String FIND_ANCIEN_MINISTERE = "a4jChangementGouvernement:nxl_changement_gouvernement_ministere_old_element:nxw_old_ministere_field_findButton";
    public static final String FIND_NEW_MINISTERE = "a4jChangementGouvernement:nxl_changement_gouvernement_ministere_new_element:nxw_new_ministere_field_findButton";

  
    public void setOldPoste(final List<String> ar) {
        selectDestinataireFromOrganigramme(ar, FIND_ANCIEN_POSTE);
    }

    public void setNewPoste(final List<String> ar) {
        selectDestinataireFromOrganigramme(ar, FIND_NEW_POSTE);
    }

    //EFI - Min. Economie, finances et industrie
    public void setOldMinistere(final String ministere) {
        selectInOrganigramme(ministere, FIND_ANCIEN_MINISTERE);
    }
    
    //CCO - CCO
    public void setNewMinistere(final String ministere) {
        selectInOrganigramme(ministere, FIND_NEW_MINISTERE);
    }
    
    public void validerChangementGouvernement() {
        getDriver().findElement(By.id(VALIDER_CHANGEMENT_GOUVERNEMENT_BUTTON_ID)).click();

        // attente fin de la migration
        waitForPageSourcePart("Migration en cours", TIMEOUT_IN_SECONDS);
        new WebDriverWait(getDriver(), 120).until(ExpectedConditions.invisibilityOfElementLocated((By
            .xpath("//img[@alt='En cours']"))));
    }

    public void setNor(final String nor) {
        final WebElement elem = getDriver().findElement(By.id(NOR_INPUT_ID));
        fillField("NOR", elem, nor);
    }

    // Ancien Ministère

    public void checkMinistere() {
        final WebElement elem = getDriver().findElement(By.id(MINISTERE_CHECKBOX_ID));
        elem.click();
        waitForPageSourcePart("Ancien Ministère", TIMEOUT_IN_SECONDS);
    }

}
