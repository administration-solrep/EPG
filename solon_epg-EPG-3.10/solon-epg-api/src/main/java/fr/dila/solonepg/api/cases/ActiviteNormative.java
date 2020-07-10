package fr.dila.solonepg.api.cases;

import fr.dila.st.api.domain.STDomainObject;

/**
 * Activite Normative.
 * 
 * @author asatre
 *
 */
public interface ActiviteNormative extends STDomainObject {

    String getApplicationLoi();
    void setApplicationLoi(String applicationLoi);
    
    String getOrdonnance();
    void setOrdonnance(String ordonnance);
    
    String getTraite();
    void setTraite(String traite);
    
    String getTransposition();
    void setTransposition(String transposition);

    //Suivi des habilitations
    String getOrdonnance38C();
    void setOrdonnance38C(String ordonnance38C);
    
    <T> T getAdapter(Class<T> itf);
    
    String getApplicationOrdonnance();
    void setApplicationOrdonnance(String applicationOrdonnance);
    
}
