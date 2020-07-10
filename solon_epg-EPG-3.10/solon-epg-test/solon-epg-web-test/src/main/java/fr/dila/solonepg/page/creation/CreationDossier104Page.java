package fr.dila.solonepg.page.creation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class CreationDossier104Page extends AbstractCreationDossierPage {
     
    public static final String REFERENCE = "creation_dossier_104:nxl_creation_dossier_layout_104:nxw_application_loi_list_indexLabel_0";
    public static final String TITRE = "creation_dossier_104:nxl_creation_dossier_layout_104:nxw_application_loi_list_indexLabel_1";
    public static final String NUMERO_ARTICLE = "creation_dossier_104:nxl_creation_dossier_layout_104:nxw_application_loi_list_indexLabel_2";
    public static final String NUMERO_ORDRE = "creation_dossier_104:nxl_creation_dossier_layout_104:nxw_application_loi_list_indexLabel_3";
    public static final String COMMENTAIRE = "creation_dossier_104:nxl_creation_dossier_layout_104:nxw_application_loi_list_indexLabel_4";

    public static final String ADD_TRANSPOSITION = "creation_dossier_104:nxl_creation_dossier_layout_104:addTranspositionImage";
    public static final String BOUTON_SUIVANT_ID = "creation_dossier_104:buttonSuivant";
    public static final String BOUTON_TERMINER_ID = "creation_dossier_104:buttonTerminer";

        public void setReference(final String reference) {
        final WebElement elem = getDriver().findElement(By.id(REFERENCE));
        fillField("Référence *", elem, reference);
    }
    
    public void setTitre(final String titre) {
        final WebElement elem = getDriver().findElement(By.id(TITRE));
        fillField("Titre", elem, titre);
    }

    public void setNumeroArticle(final String numeroArticle) {
        final WebElement elem = getDriver().findElement(By.id(NUMERO_ARTICLE));
        fillField("Numéro d'article", elem, numeroArticle);
    }   
    
    public void setNumeroOrdre(final String numeroOrdre) {
        final WebElement elem = getDriver().findElement(By.id(NUMERO_ORDRE));
        fillField("Numéro d'ordre", elem, numeroOrdre);
    }   
       
    public void setCommentaire(final String commentaire) {
        final WebElement elem = getDriver().findElement(By.id(COMMENTAIRE));
        fillField("Commentaire", elem, commentaire);
    }     

    
    public CreationDossier104Page ajouterTransposition(String text){
        getFlog().action("ajouter Transposition");
        getDriver().findElement(By.id(ADD_TRANSPOSITION)).click();
        waitForPageSourcePart(text, TIMEOUT_IN_SECONDS);
        return getPage(CreationDossier104Page.class);
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