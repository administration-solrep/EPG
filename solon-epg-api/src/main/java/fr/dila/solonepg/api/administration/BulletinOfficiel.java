/**
 *
 */
package fr.dila.solonepg.api.administration;

import fr.dila.st.api.domain.STDomainObject;
import java.io.Serializable;

/**
 * Bulletins Officiels Interface for solon epg.
 *
 * @author asatre
 *
 */
public interface BulletinOfficiel extends STDomainObject, Serializable {
    String getNor();
    void setNor(String nor);

    String getIntitule();
    void setIntitule(String intitule);

    String getEtat();
    void setEtat(String etat);
}
