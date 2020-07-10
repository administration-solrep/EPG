package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm10Page;

public class MgppDetailComm10Page extends AbstractMgppDetailComm{

    private static final String FICHE_LOI_ACCELEREE = "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[3]/td/div/div/table/tbody/tr[2]/td[3]/img[@src='/solon-epg/icons/task.png']";
    
    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return MgppCreateComm10Page.class;
    }
    
    public void verifierFicheLoiAcceleree(){
        checkElementPresent(By_enum.XPATH, FICHE_LOI_ACCELEREE, Mode.READ);
    }
}
