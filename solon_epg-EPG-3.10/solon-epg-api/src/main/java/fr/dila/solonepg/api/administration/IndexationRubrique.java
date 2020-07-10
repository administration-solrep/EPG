/**
 * 
 */
package fr.dila.solonepg.api.administration;

import java.io.Serializable;

import fr.dila.st.api.domain.STDomainObject;

/**
 * Indexation Rubrique Interface for solon epg.
 * 
 * @author asatre
 *
 */
public interface IndexationRubrique extends STDomainObject,Serializable {
    
    String getIntitule();
    void setIntitule(String intitule);
    
}
