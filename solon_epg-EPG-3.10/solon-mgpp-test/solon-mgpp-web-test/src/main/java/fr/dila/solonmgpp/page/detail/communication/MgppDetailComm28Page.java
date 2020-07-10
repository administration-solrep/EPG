package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm28Page;

public class MgppDetailComm28Page extends AbstractMgppDetailComm{

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return MgppCreateComm28Page.class;
    }
    
    public void checkValues(final String identifiant, final String titre, final String numeroLoi, final String dateReception ){
        checkValue_identifiant(identifiant);
        checkValue_titre(titre);
        //Vérification Loi votée (LOI N°)
        checkValue_numeroLoi(numeroLoi);
        checkValue_dateReception(dateReception);
    }
    
    public void checkValue_identifiant(final String identifiant){
        checkValue("meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_identifiant", identifiant, Boolean.FALSE);
    }
    
    public void checkValue_titre(final String titre){
        checkValue("meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_titre", titre, Boolean.FALSE);
    }
    
    public void checkValue_numeroLoi(final String numeroLoi){
        checkValue("meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_numeroLoi", numeroLoi, Boolean.FALSE);
    }
    
    public void checkValue_dateReception(final String dateReception){
        checkValue("metadonnee_dossier:nxl_fiche_loi_vote:nxw_fiche_loi_dateReception", dateReception, Boolean.FALSE);
    }

}
