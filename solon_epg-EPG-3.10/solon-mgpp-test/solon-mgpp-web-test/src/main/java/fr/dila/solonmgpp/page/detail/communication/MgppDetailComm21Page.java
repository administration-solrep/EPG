package fr.dila.solonmgpp.page.detail.communication;

import java.util.Calendar;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.utils.DateUtils;

public class MgppDetailComm21Page extends AbstractMgppDetailComm{

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return null;
    }

    public void verifierNavette(){
        verifierNavette_cmp();
        //checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier']/span/table/tbody/tr[4]/td/div/div/table/tbody/tr[4]/td[2]", "Date réunion Assemblée nationale", Boolean.FALSE);
        //Assert.assertTrue(getDriver().findElements(By.xpath("//*[@id='metadonnee_dossier']/span/table/tbody/tr[4]/td/div/div/table/tbody/tr[4]/td[3][contains(text(), '" + DateUtils.format(Calendar.getInstance()) + "') and contains(text(), '01/01/2012')]")).size() > 0);
        checkValue(By_enum.XPATH, "//*[@id='meta_evt']/div/div/table/tbody/tr/td[1]/table[1]/tbody/tr[12]/td[1]/span", "Date réunion CMP", Boolean.TRUE);
        checkValue(By_enum.XPATH, "//*[@id='meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_dateCMP_list:0:nxw_metadonnees_evenement_dateCMP_listItem']/table/tbody/tr/td", DateUtils.format(Calendar.getInstance()), Boolean.TRUE);
//        checkValue(By_enum.XPATH, "//*[@id='meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_dateCMP_list:1:nxw_metadonnees_evenement_dateCMP_listItem']/table/tbody/tr/td", DateUtils.format(Calendar.getInstance()), Boolean.TRUE);
        
    }
    
    public void verifierNavette_cmp(){        
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table[@class='navetteTable CMP']/tbody/tr[1]/th", "Commission mixte paritaire", Boolean.FALSE);
    }
}
