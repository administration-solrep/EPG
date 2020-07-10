package fr.dila.solonmgpp.page.detail.communication;

import java.util.Calendar;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.utils.DateUtils;

public class MgppDetailComm13Page extends AbstractMgppDetailComm{

    private static final String PROCEDURE_ACCEEREE_PATH = "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[3]/td/div/div/table/tbody/tr[2]/td[3]/img[@src='/solon-epg/icons/task.png']";
    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return null;
    }
    
    public void assertProcedureAcceleree(){
        //assertElementPresent(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[3]/td/div/div/table/tbody/tr[2]/td[3]/img[@src='/solon-epg/icons/task.png']");
        assertElementPresent(By_enum.XPATH, PROCEDURE_ACCEEREE_PATH);
    }
    
    public void assertNavetteSenat2(){
        //@@FOUND
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[3]/tbody/tr[1]/th","2ème lecture Sénat", Boolean.FALSE); 
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[3]/tbody/tr[2]/td[1]", "Transmis le", Boolean.FALSE); 
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[3]/tbody/tr[2]/td[2]", "", Boolean.FALSE); 
        
    }
    
    public void assertNavetteAn1(){
        //@@FOUND
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[2]/tbody/tr[1]/th","1ère lecture Assemblée Nationale", Boolean.FALSE); 
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[2]/tbody/tr[2]/td[1]", "Transmis le", Boolean.FALSE); 
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[2]/tbody/tr[2]/td[2]", DateUtils.format(Calendar.getInstance()), Boolean.FALSE); 
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[2]/tbody/tr[4]/td[1]", "Texte", Boolean.FALSE); 
        //@@NOT_FOUND
        //checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[2]/tbody/tr[4]/td[2]", "2012-04", Boolean.FALSE); 
    }
    
    public void assertNavetteSenat1(){
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[1]/tbody/tr[1]/th","1ère lecture Sénat", Boolean.FALSE); 
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[1]/tbody/tr[2]/td[1]", "Transmis le", Boolean.FALSE); 
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[1]/tbody/tr[2]/td[2]", DateUtils.format(Calendar.getInstance()), Boolean.FALSE); 
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[1]/tbody/tr[3]/td[1]", "Adopté", Boolean.FALSE); 
        //@@NOT_FOUND
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[1]/tbody/tr[3]/td[2]", DateUtils.format(Calendar.getInstance()), Boolean.FALSE); 
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[1]/tbody/tr[4]/td[1]", "Texte", Boolean.FALSE); 
        //@@NOT_FOUND
        checkValue(By_enum.XPATH, "//*[@id='metadonnee_dossier:documentViewPanel']/table/tbody/tr[4]/td/div/div/table[1]/tbody/tr[4]/td[2]", "2012-04", Boolean.FALSE); 
        
    }

}
