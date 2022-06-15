/**
 *
 */
package fr.dila.solonepg.api.administration;

import fr.dila.st.api.domain.STDomainObject;
import java.io.Serializable;

/**
 * Indexation Rubrique Interface for solon epg.
 *
 * @author asatre
 *
 */
public interface IndexationRubrique extends STDomainObject, Serializable {
    String getIntitule();
    void setIntitule(String intitule);
}
