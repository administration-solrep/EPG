package fr.dila.solonmgpp.web.suivi;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;

import com.sun.faces.util.MessageFactory;

import fr.dila.ss.web.birt.BirtReportActionsBean;
/**
 * 
 * @author aRammal
 */

@Name("suiviActions")
@Scope(ScopeType.CONVERSATION)
public class SuiviActionsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final int IN_CALENDRIER_PARLEMENTAIRE = 0;
    private static final int IN_ECHEANCIER_DE_PROMULGATION = 1;
    private static final int IN_SUIVI_DES_ECHEANCES = 3;

    private static final String BIRT_CALENDRIER_PATH = "birtReports/calendrier/";
    private static final String BIRT_SUIVI_DES_ECHEANCES_FILE = "liste_suivi_echeances.rptdesign";
    private static final String BIRT_SUIVI_DES_ECHEANCES_NAME = "liste_suivi_echeances";
    private static final String BIRT_ECHEANCIER_DE_PROMULGATION_FILE = "liste_echeancier_promulgation.rptdesign";
    private static final String BIRT_ECHEANCIER_DE_PROMULGATION_NAME = "liste_echeancier_promulgation";
    
    private int tabIndex = 0;

    @In(create = true, required = true)
    protected transient CalendrierParlementaireActionsBean calendrierParlementaireActions;
    
    @In(create = true, required = true)
    protected transient BirtReportActionsBean birtReportingActions;

    public void goToCalendrierParlementaire()
    {
        calendrierParlementaireActions.setListeDesLois(null);
        tabIndex = IN_CALENDRIER_PARLEMENTAIRE;
        calendrierParlementaireActions.setDateDeDebutFiltre(new Date());
        calendrierParlementaireActions.setDateDeFinFiltre(null);
    }
    
    public void goToEcheancierDePromulgation()
    {
        tabIndex = IN_ECHEANCIER_DE_PROMULGATION;
    }
    
    public void goToSuiviDesEcheances()
    {
        tabIndex = IN_SUIVI_DES_ECHEANCES;
    }
    
    public boolean isInCalendrierParlementaire()
    {
        return (tabIndex == IN_CALENDRIER_PARLEMENTAIRE);
    }
    
    public boolean isInCalendrierParlementaireRefreshData() throws ClientException
    {
        if(tabIndex == IN_CALENDRIER_PARLEMENTAIRE) {
            calendrierParlementaireActions.refreshData();
            return true;
        }
        return false;
    }
    
    
    public boolean isInEcheancierDePromulgation()
    {
        return (tabIndex == IN_ECHEANCIER_DE_PROMULGATION);
    }
    
    public boolean isInSuiviDesEcheances()
    {
        return (tabIndex == IN_SUIVI_DES_ECHEANCES);
    }
    
    public Converter getStringListConverter() {
      return new Converter() {

          @Override
          public String getAsString(FacesContext context, UIComponent component, Object value) {

              if (value instanceof List) {
                  @SuppressWarnings("unchecked")
                  List<String> destList = (List<String>) value;
                  if(!destList.isEmpty()){
                    StringBuilder outputText = new StringBuilder();
                    for(String item : destList){
                        if(outputText.length() != 0){
                            outputText.append(", ") ;      
                        }
                        outputText.append(MessageFactory.getMessage(context, item).getDetail()) ;                      
                    }
                    return outputText.toString() ;
                  }
              }
              return "";
          }

          @Override
          public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
              return arg2;
          }
      };
  }
    
  public void exportPdf() throws Exception{
    String fileName = getFileForExport();
    if(fileName!=null) {
      HashMap<String, String> inputValues = new HashMap<String, String>();
      birtReportingActions.generatePdf(fileName, inputValues, isInEcheancierDePromulgation()?BIRT_ECHEANCIER_DE_PROMULGATION_NAME:BIRT_SUIVI_DES_ECHEANCES_NAME);
    }
  }
  public void exportXls() throws Exception{
    String fileName = getFileForExport();
    if(fileName!=null) {
      HashMap<String, String> inputValues = new HashMap<String, String>();
      birtReportingActions.generateXls(fileName, inputValues, isInEcheancierDePromulgation()?BIRT_ECHEANCIER_DE_PROMULGATION_NAME:BIRT_SUIVI_DES_ECHEANCES_NAME);
    }
  }
  private String getFileForExport() {
    if(isInEcheancierDePromulgation()) {
      return BIRT_CALENDRIER_PATH + BIRT_ECHEANCIER_DE_PROMULGATION_FILE;
    }
    else if(isInSuiviDesEcheances()) {
      return BIRT_CALENDRIER_PATH + BIRT_SUIVI_DES_ECHEANCES_FILE;
    }
    else{
        return null;  
    } 
  }
}
