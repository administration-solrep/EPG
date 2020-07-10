package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.query.api.PageSelection;
import org.nuxeo.ecm.platform.query.api.PageSelections;

import fr.dila.st.web.contentview.PaginatedPageDocumentProvider;

/**
 * Classe abstraite pour les provider filtrables
 * 
 * @author admin
 * 
 */
public abstract class AbstractFiltrableProvider extends PaginatedPageDocumentProvider implements FiltrableProvider {

    /**
     * 
     */
    private static final long serialVersionUID = 4347856008554103741L;

    private static final String PROVIDER_BEAN = "providerBean";

    private static final String FILTRABLE_ID = "filtrableId";

    private Map<String, Serializable> filtrableMap;

    private Set<String> filtrableProperty;

    private PageSelections<Map<String, Serializable>> currentFiltrableMap;

    @Override
    public ProviderBean getProviderBean() throws ClientException {
        Map<String, Serializable> props = getProperties();
        ProviderBean providerBean = (ProviderBean) props.get(PROVIDER_BEAN);
        if (providerBean == null) {
            throw new ClientException("cannot find providerBean");
        }
        return providerBean;
    }

    @Override
    public String getFiltrableId() throws ClientException {
        Map<String, Serializable> props = getProperties();
        String filtrableId = (String) props.get(FILTRABLE_ID);
        if (filtrableId == null) {
            throw new ClientException("cannot find filtrableId");
        }
        return filtrableId;
    }

    @Override
    public Map<String, Serializable> getFiltrableMap() throws ClientException {
        if (filtrableMap == null) {
            filtrableMap = getProviderBean().getFilter(getFiltrableId());
        }
        return filtrableMap;
    }

    @Override
    public String processFilter() throws ClientException {
        getProviderBean().processFilter(filtrableMap, getFiltrableId());
        refresh();
        // force navigation to first page
        setCurrentPage(0);
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String resetFilter() throws ClientException {
        filtrableMap = getProviderBean().resetFilter(getFiltrableId());
        for (String key : filtrableMap.keySet()) {
            Map<String, Serializable> map = (Map<String, Serializable>) filtrableMap.get(key);
            if (map != null) {
                for (String mapKey : map.keySet()) {
                    map.put(mapKey, null);
                }
            }
        }
        currentFiltrableMap = null;
        getCurrentFiltrableMap();
        refresh();
        // force navigation to first page
        setCurrentPage(0);
        return null;
    }

    @Override
    public void addFiltrableProperty(String property) {
        if (filtrableProperty == null) {
            filtrableProperty = new HashSet<String>();
        }
        filtrableProperty.add(property);
        processFiltrableProperty(property);

    }

    @SuppressWarnings("unchecked")
    private void processFiltrableProperty(String property) {
        String[] props = property.split("\\.");
        String filtrableProp;
        if (props.length > 1) {
            filtrableProp = props[1];
        } else {
            filtrableProp = props[0];
        }
        String[] filtrableProps = filtrableProp.split(":");

        Map<String, Serializable> map = (Map<String, Serializable>) filtrableMap.get(filtrableProps[0]);
        if (map == null) {
            map = new HashMap<String, Serializable>();
        }
        map.put(filtrableProps[1], map.get(filtrableProps[1]));
        filtrableMap.put(filtrableProps[0], (Serializable) map);
    }

    @Override
    public Set<String> getFiltrableProperty() {
        return filtrableProperty;
    }

    @Override
    public PageSelections<Map<String, Serializable>> getCurrentFiltrableMap() throws ClientException {
        if (currentFiltrableMap == null) {
            List<PageSelection<Map<String, Serializable>>> entries = new ArrayList<PageSelection<Map<String, Serializable>>>();
            entries.add(new PageSelection<Map<String, Serializable>>(getFiltrableMap(), Boolean.FALSE));
            currentFiltrableMap = new PageSelections<Map<String, Serializable>>();
            currentFiltrableMap.setName(getFiltrableId());
            currentFiltrableMap.setEntries(entries);
        }
        return currentFiltrableMap;
    }

    @Override
    public abstract Boolean isFiltreActif();

    @Override
    public abstract Integer getFiltreActif();

}
