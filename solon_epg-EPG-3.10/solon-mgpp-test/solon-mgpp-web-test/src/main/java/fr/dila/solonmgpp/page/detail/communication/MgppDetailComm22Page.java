package fr.dila.solonmgpp.page.detail.communication;

import java.util.Calendar;
import fr.dila.solonmgpp.utils.DateUtils;
import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;

public class MgppDetailComm22Page extends AbstractMgppDetailComm{

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return null;
    }
    
    public void verifierNavette_cmp(){
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[4]/tbody/tr[1]/th", "Commission mixte paritaire", Boolean.FALSE);
    }
    
  //XXX: replaced code
    public void verifierNavette(){
        verifierNavette_cmp();
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[4]/tbody/tr[3]/td[1]", "Date lecture des conclusions de la CMP Assemblée nationale"/*"Date réunion Assemblée nationale"*/, Boolean.FALSE);
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[4]/tbody/tr[3]/td[2]", DateUtils.format(Calendar.getInstance()), Boolean.FALSE);
        //checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier']/span/table/tbody/tr[4]/td/div/div/table/tbody/tr[4]/td[3]", "Accord", Boolean.FALSE);
        //checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[4]/tbody/tr[3]/td[4]", "Adopté", Boolean.FALSE);//XXX: replaced code
        //checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[4]/tbody/tr[4]/td[1]", "Date lecture des conclusions de la CMP Sénat"/*"Date réunion Sénat"*/, Boolean.FALSE);
        //checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[4]/tbody/tr[4]/td[2]", DateUtils.format(Calendar.getInstance()), Boolean.FALSE);
        //checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier']/span/table/tbody/tr[4]/td/div/div/table/tbody/tr[5]/td[3]", "Accord", Boolean.FALSE);
        //checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[4]/tbody/tr[4]/td[4]", "Adopté", Boolean.FALSE);
    }

}
