package fr.dila.solonepg.ui.contentview;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import org.nuxeo.ecm.platform.query.api.PageSelections;

/**
 * Interface pour les provider filtrables
 *
 * @author asatre
 *
 */
public interface FiltrableProvider {
    Map<String, Serializable> getFiltrableMap();

    void setFiltrableMap(Map<String, Serializable> filters);

    String resetFilter();

    /**
     * Ajout d'une propriété filtrable basé sur sortPropertyName du layout
     *
     * @param property
     */
    void addFiltrableProperty(String property);

    void addFiltrableProperty(String property, Serializable value);

    Set<String> getFiltrableProperty();

    PageSelections<Map<String, Serializable>> getCurrentFiltrableMap();
}
