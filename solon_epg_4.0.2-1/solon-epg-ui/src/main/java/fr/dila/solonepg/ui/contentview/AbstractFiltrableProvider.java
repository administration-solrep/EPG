package fr.dila.solonepg.ui.contentview;

import fr.dila.st.ui.contentview.PaginatedPageDocumentProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.platform.query.api.PageSelection;
import org.nuxeo.ecm.platform.query.api.PageSelections;

/**
 * Classe abstraite pour les provider filtrables
 *
 * @author admin
 */
public abstract class AbstractFiltrableProvider extends PaginatedPageDocumentProvider implements FiltrableProvider {
    private static final long serialVersionUID = 4850848904009932814L;

    protected static final String WHERE = " WHERE ";

    protected static final String AND = " AND ";

    private Map<String, Serializable> filtrableMap = new HashMap<>();

    private Set<String> filtrableProperty = new TreeSet<>();

    private transient PageSelections<Map<String, Serializable>> currentFiltrableMap;

    @Override
    public Map<String, Serializable> getFiltrableMap() {
        return new HashMap<>(filtrableMap);
    }

    @Override
    public void setFiltrableMap(Map<String, Serializable> filters) {
        this.filtrableMap = new HashMap<>(filters);
    }

    @Override
    public String resetFilter() {
        filtrableMap = filtrableMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, null));
        currentFiltrableMap = null;
        getCurrentFiltrableMap();
        refresh();
        // force navigation to first page
        setCurrentPage(0);
        return null;
    }

    @Override
    public void addFiltrableProperty(String property) {
        addFiltrableProperty(property, null);
    }

    @Override
    public void addFiltrableProperty(String property, Serializable value) {
        if (filtrableProperty == null) {
            filtrableProperty = new HashSet<>();
        }
        filtrableProperty.add(property);
        processFiltrableProperty(property, value);
    }

    protected String getPrefixFromProperty(String property) {
        String[] props = property.split("\\.");
        return props.length > 1 ? props[0] : "";
    }

    protected String getPropertyName(String property) {
        String[] props = property.split("\\.");
        return props.length > 1 ? props[1] : props[0];
    }

    @SuppressWarnings("unchecked")
    protected void processFiltrableProperty(String property, Serializable value) {
        String filtrableProp = getPropertyName(property);

        String[] filtrableProps = filtrableProp.split(":");

        Map<String, Serializable> map = (Map<String, Serializable>) filtrableMap.get(filtrableProps[0]);
        if (map == null) {
            map = new HashMap<>();
        }
        Serializable valueToInsert = value == null ? map.get(filtrableProps[1]) : value;

        map.put(filtrableProps[1], valueToInsert);
        filtrableMap.put(filtrableProps[0], (Serializable) map);
    }

    @Override
    public Set<String> getFiltrableProperty() {
        return new TreeSet<>(filtrableProperty);
    }

    @Override
    public PageSelections<Map<String, Serializable>> getCurrentFiltrableMap() {
        if (currentFiltrableMap == null) {
            List<PageSelection<Map<String, Serializable>>> entries = new ArrayList<>();
            entries.add(new PageSelection<>(getFiltrableMap(), Boolean.FALSE));
            currentFiltrableMap = new PageSelections<>();
            currentFiltrableMap.setEntries(entries);
        }
        return currentFiltrableMap;
    }

    protected void addCorrectTokenBeforeParam(StringBuilder query) {
        if (StringUtils.containsIgnoreCase(query.toString(), "WHERE")) {
            query.append(AND);
        } else {
            query.append(WHERE);
        }
    }

    protected Serializable getFiltrableValue(String key) {
        return filtrableMap.get(key);
    }

    protected void putFiltrableValue(String key, Serializable value) {
        filtrableMap.put(key, value);
    }
}
