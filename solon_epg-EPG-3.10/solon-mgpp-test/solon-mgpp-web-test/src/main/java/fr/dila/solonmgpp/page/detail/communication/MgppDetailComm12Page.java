package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;

public class MgppDetailComm12Page extends AbstractMgppDetailComm{

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return null;
    }
    
    public void verifierNavette(){
      checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[1]/tbody/tr[1]/th","1ère lecture Sénat", Boolean.FALSE);
      checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[3]/tbody/tr[1]/th","2ème lecture Sénat", Boolean.FALSE);
      checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[4]/tbody/tr[1]/th", "Commission mixte paritaire", Boolean.FALSE);
    }

}
