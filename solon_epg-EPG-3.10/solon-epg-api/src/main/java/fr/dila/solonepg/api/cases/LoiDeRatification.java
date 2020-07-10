package fr.dila.solonepg.api.cases;

import java.util.Calendar;

/**
 * Loi de ratification ({@link ActiviteNormative}).
 * 
 * @author asatre
 *
 */
public interface LoiDeRatification extends TexteMaitre {

    String getId();

    String getTermeDepot();
    void setTermeDepot(String termeDepot);

    Calendar getDateLimiteDepot();
    void setDateLimiteDepot(Calendar dateLimiteDepot);

    Calendar getDateLimitePublication();
    void setDateLimitePublication(Calendar dateLimitePublication);
    
    Calendar getDateSaisineCE();
    void setDateSaisineCE(Calendar dateSaisineCE);
    
    Calendar getDateExamenCE();
    void setDateExamenCE(Calendar dateExamenCE);
    
    String getSectionCE();
    void setSectionCE(String sectionCE);
    
    Calendar getDateExamenCM();
    void setDateExamenCM(Calendar dateExamenCM);
    
    String getChambreDepot();
    void setChambreDepot(String chambreDepot);
    
    Calendar getDateDepot();
    void setDateDepot(Calendar dateDepot);
    
    String getNumeroDepot();
    void setNumeroDepot(String numeroDepot);

    Boolean isDateSaisineCELocked();
    void setDateSaisineCELocked(Boolean dateSaisineCELocked);

    Boolean isDateExamenCELocked();
    void setDateExamenCELocked(Boolean dateExamenCELocked);

    Boolean isSectionCELocked();
    void setSectionCELocked(Boolean sectionCELocked);

    Boolean isChambreDepotLocked();
    void setChambreDepotLocked(Boolean chambreDepotLocked);

    Boolean isDateDepotLocked();
    void setDateDepotLocked(Boolean dateDepotLocked);

    Boolean isNumeroDepotLocked();
    void setNumeroDepotLocked(Boolean numeroDepotLocked);
    
}
