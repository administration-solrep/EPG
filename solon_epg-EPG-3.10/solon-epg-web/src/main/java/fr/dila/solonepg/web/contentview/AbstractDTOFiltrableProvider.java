package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageSelection;
import org.nuxeo.ecm.platform.query.api.PageSelections;

import fr.dila.st.web.contentview.AbstractDTOPageProvider;
import fr.dila.st.web.contentview.CorePageProviderUtil;

/**
 * Classe abstraite pour les provider filtrables
 * 
 * @author admin
 * 
 */
public abstract class AbstractDTOFiltrableProvider extends AbstractDTOPageProvider implements FiltrableProvider {

    private static final long serialVersionUID = 4347856008554103741L;

    private static final String PROVIDER_BEAN = "providerBean";

    private static final String FILTRABLE_ID = "filtrableId";

	private static final String ADDITIONAL_SORT = "additionalSort";

    private Map<String, Serializable> filtrableMap;

    private Set<String> filtrableProperty;

    private PageSelections<Map<String, Serializable>> currentFiltrableMap;

    protected class MapInfo {

        private String prefix;
        private String property;

        public MapInfo() {

        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

    }

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
            Map<String, Serializable> props = getProperties();
            // Si la propriété existe (et quelle est à 'true') 
            if ((String) props.get("isEmptyByDefault")!=null && "true".contains((String) props.get("isEmptyByDefault"))) {
            	String defaultFiltrableProperty = (String) props.get("defaultEmptyFiltrableProperty");
            	String defaultFiltrableField = (String) props.get("defaultEmptyField");
            	String defaultFiltrableType = (String) props.get("defaultEmptyType");
            	if ("date".contains(defaultFiltrableType)) {
            		// On paramètre le filtre pour avoir la date du lendemain en création (pour n'avoir aucun résultats)
            		Calendar cal = Calendar.getInstance();
            		cal.add(Calendar.DATE, 1);
            		filtrableMap.put(defaultFiltrableField, cal.getTime());
            		addFiltrableProperty(defaultFiltrableProperty);
            	}
            }
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
        if (filtrableProps.length > 1) {
            map.put(filtrableProps[1], map.get(filtrableProps[1]));
            filtrableMap.put(filtrableProps[0], (Serializable) map);
        } else {
            filtrableMap.put(filtrableProps[0], map.get(filtrableProps[0]));
        }
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

	protected String getAdditionalSort() {
		return (String) getProperties().get(ADDITIONAL_SORT);
	}

	protected SortInfo[] getSortInfosWithAdditionalSort() {
		SortInfo[] sortArray = null;
		String additionalSort = getAdditionalSort();
		sortArray = CorePageProviderUtil.getSortinfoForQuery(sortInfos, additionalSort);
		return sortArray;
	}
}
