package fr.dila.solonepg.page.creation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CreationDossier101Page extends AbstractCreationDossierPage {
     
    public static final String TRANSPOSITION_DIRECTIVE_CHECKBOX = "creation_dossier_101:nxl_creation_dossier_layout_101:nxw_transposition_directive_field:nxw_transposition_directive_field_checkbox:0";
    public static final String APPLICATION_LOI_CHECKBOX = "creation_dossier_101:nxl_creation_dossier_layout_101:nxw_application_loi_field:nxw_application_loi_field_checkbox:0";
    public static final String TRANSPOSITION_ORDONNANCE_CHECKBOX = "creation_dossier_101:nxl_creation_dossier_layout_101:nxw_transposition_ordonnance_field:nxw_transposition_ordonnance_field_checkbox:0";
    public static final String BOUTON_SUIVANT_ID = "creation_dossier_101:buttonSuivant";
    public static final String BOUTON_TERMINER_ID = "creation_dossier_101:buttonTerminer";

    public void checkTranspositionDirective(){
        final WebElement checkBox = getDriver().findElement(By.id(TRANSPOSITION_DIRECTIVE_CHECKBOX));
        checkBox.click();
    }
    
    public void checkapplicationLoi(){
        final WebElement checkBox = getDriver().findElement(By.id(APPLICATION_LOI_CHECKBOX));
        checkBox.click();
    }
    
    public void checkTranspositionOrdonnance(){
        final WebElement checkBox = getDriver().findElement(By.id(TRANSPOSITION_ORDONNANCE_CHECKBOX));
        checkBox.click();
    }
    
    @Override
    protected String getButtonSuivantId() {
        return BOUTON_SUIVANT_ID;
    }

    @Override
    protected String getButtonTerminerId() {
        return BOUTON_TERMINER_ID;
    }
    
    
}