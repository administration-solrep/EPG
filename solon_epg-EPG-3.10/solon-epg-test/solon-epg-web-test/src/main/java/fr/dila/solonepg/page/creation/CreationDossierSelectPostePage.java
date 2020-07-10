package fr.dila.solonepg.page.creation;

import java.util.List;


public class CreationDossierSelectPostePage extends AbstractCreationDossierPage {
     
    public static final String BOUTON_TERMINER_ID = "creation_dossier_select_poste:buttonTerminer";
    public static final String POSTE = "creation_dossier_select_poste:nxl_creation_dossier_layout_select_poste:nxw_dossier_creation_choix_poste_poste_findButton";
  

    
    public void setPoste(List<String> ar) {
        selectMultipleInOrganigramme(ar, POSTE);

      }
    @Override
    protected String getButtonSuivantId() {
        return null;
    }
    
    @Override
    protected String getButtonTerminerId() {
        return BOUTON_TERMINER_ID;
    }
    
    
    
}