/**
 * 
 */
package fr.dila.solonepg.api.administration;

import java.io.Serializable;
import java.util.Calendar;

import fr.dila.st.api.domain.STDomainObject;

/**
 * Vecteurs de Publication Interface for solon epg.
 * 
 * @author ahullot
 *
 */
public interface VecteurPublication extends STDomainObject,Serializable {
        
    String getIntitule();
    void setIntitule(String intitule);
    
    Calendar getDateDebut();
    void setDateDebut(Calendar debut);

    Calendar getDateFin();
    void setDateFin(Calendar fin);
}
