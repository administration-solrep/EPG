package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.query.api.PageSelections;

/**
 * Interface pour les provider filtrables
 * 
 * @author asatre
 * 
 */
public interface FiltrableProvider {

    Map<String, Serializable> getFiltrableMap() throws ClientException;

    ProviderBean getProviderBean() throws ClientException;

    String getFiltrableId() throws ClientException;
    
    String processFilter() throws ClientException;
    
    String resetFilter() throws ClientException;
    
    /**
     * Ajout d'une propriété filtrable basé sur sortPropertyName du layout
     * 
     * @param property
     */
    void addFiltrableProperty(String property);

    Set<String> getFiltrableProperty();
    
    PageSelections<Map<String, Serializable>> getCurrentFiltrableMap() throws ClientException ;
    
    /**
     * 
     * @return true si un filtre est en cours
     */
    Boolean isFiltreActif();
    
    /**
     * 
     * @return le nombre de filtre actif en cours
     */
    Integer getFiltreActif();
    
}
