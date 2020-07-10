/**
 * 
 */
package fr.dila.solonepg.api.administration;

import java.io.Serializable;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.st.api.domain.STDomainObject;

/**
 * Indexation Mots clés Interface for solon epg.
 * 
 * @author asatre
 *
 */
public interface IndexationMotCle extends STDomainObject,Serializable {
    
    String getIntitule();
    void setIntitule(String intitule);
    
    List<String> getMinistereIds();
    void setMinistereIds(List<String> ministereIds);
    
    List<String> getMotsCles();
    void setMotsCles(List<String> motsCles);  
    
    String getAuthor() throws ClientException;

    Boolean isAuthor(CoreSession session) throws ClientException;
}
