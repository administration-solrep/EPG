package fr.dila.solonepg.ui.contentview;

import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.contentview.AbstractDTOPageProvider;
import fr.dila.st.ui.contentview.CorePageProviderUtil;
import fr.dila.st.ui.contentview.OrderByDistinctQueryCorrector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageProviderDefinition;
import org.nuxeo.ecm.platform.query.api.PageSelection;
import org.nuxeo.ecm.platform.query.api.PageSelections;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;

/**
 * Classe abstraite pour les provider filtrables
 *
 * @author admin
 */
public abstract class AbstractDTOFiltrableProvider extends AbstractDTOPageProvider implements FiltrableProvider {
    private static final long serialVersionUID = 6988934299045550657L;

    protected static final String WHERE = " WHERE ";

    protected static final String AND = " AND ";

    private static final String FILTRABLE_ID = "filtrableId";

    private Map<String, Serializable> filtrableMap = new HashMap<>();

    private Set<String> filtrableProperty = new TreeSet<>();

    private transient PageSelections<Map<String, Serializable>> currentFiltrableMap;

    private Map<String, String> prefixForSchemaMap = null;

    protected transient List<Serializable> filtrableParameters;
    protected int filtrableCount;

    private static final String PREFIX_FOR_SCHEMA_PROPERTY = "prefixForSchema";

    protected class MapInfo {
        private String prefix;
        private String property;

        public MapInfo() {}

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
    protected void buildQuery() {
        SortInfo[] sortArray = null;

        String newQuery;
        PageProviderDefinition def = getDefinition();

        String stringQuery = getQuery(def);

        StringBuilder query = new StringBuilder(stringQuery);

        if (sortInfos != null) {
            sortArray = CorePageProviderUtil.getSortinfoForQuery(sortInfos);
        }

        buildQueryFilters(query);

        // on force les quotes est les escape des paramètres
        boolean forceQuoteAndEscape = false;
        newQuery =
            NXQLQueryBuilder.getQuery(
                query.toString(),
                null,
                forceQuoteAndEscape,
                forceQuoteAndEscape,
                getSearchDocumentModel(),
                sortArray
            );
        newQuery = OrderByDistinctQueryCorrector.correct(newQuery, sortArray);

        if (newQuery != null && !newQuery.equals(this.query)) {
            // query has changed => refresh
            refresh();
            this.query = newQuery;
        }
    }

    protected String getQuery(PageProviderDefinition def) {
        return NXQLQueryBuilder.getQuery(
            def.getPattern(),
            getParameters(),
            def.getQuotePatternParameters(),
            def.getEscapePatternParameters(),
            getSearchDocumentModel()
        );
    }

    @Override
    public Map<String, Serializable> getFiltrableMap() {
        if (filtrableMap == null) {
            getCurrentFiltrableMap();
            Map<String, Serializable> props = getProperties();
            // Si la propriété existe (et quelle est à 'true')
            if (
                (String) props.get("isEmptyByDefault") != null &&
                "true".contains((String) props.get("isEmptyByDefault"))
            ) {
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
    public void setFiltrableMap(Map<String, Serializable> filters) {
        this.filtrableMap = filters;
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
    private void processFiltrableProperty(String property, Serializable value) {
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
            setFiltrableId();
            currentFiltrableMap.setEntries(entries);
        }
        return currentFiltrableMap;
    }

    private void setFiltrableId() {
        Map<String, Serializable> props = getProperties();
        String filtrableId = (String) props.get(FILTRABLE_ID);
        if (filtrableId != null) {
            currentFiltrableMap.setName(filtrableId);
        }
    }

    public Map<String, String> getPrefixForSchema() {
        if (prefixForSchemaMap == null) {
            prefixForSchemaMap = new HashMap<>();
            String propValue = (String) getProperties().get(PREFIX_FOR_SCHEMA_PROPERTY);
            String[] associations = propValue.split(";");
            prefixForSchemaMap =
                Stream
                    .of(associations)
                    .map(association -> association.split(":"))
                    .filter(array -> array.length == 2)
                    .collect(Collectors.toMap(elements -> elements[0], elements -> elements[1]));
        }
        return prefixForSchemaMap;
    }

    protected void addParameter(StringBuilder query, String key, String mapKey, Serializable value, String prefix) {
        appendWhereOrAnd(query);

        query.append(prefix);
        query.append(key);
        query.append(":");
        query.append(mapKey);
        if (value instanceof String && ((String) value).contains("%")) {
            query.append(" ILIKE ");
        } else {
            query.append(" = ");
        }
        query.append(" ? ");
        filtrableParameters.add(value);
        filtrableCount++;
    }

    protected void appendWhereOrAnd(StringBuilder query) {
        if (StringUtils.containsIgnoreCase(query.toString(), "WHERE")) {
            query.append(AND);
        } else {
            query.append(WHERE);
        }
    }

    private void addParameterDate(StringBuilder query, String key, String mapKey, Date value, String prefix) {
        Calendar calDebut = DateUtil.toCalendarFromNotNullDate(value);
        DateUtil.setDateToBeginingOfDay(calDebut);

        Calendar calFin = DateUtil.toCalendarFromNotNullDate(value);
        DateUtil.setDateToEndOfDay(calFin);

        appendWhereOrAnd(query);

        query.append(prefix);
        query.append(key);
        query.append(":");
        query.append(mapKey);
        query.append(" >= ");
        query.append(" ? ");

        query.append(AND);
        query.append(prefix);
        query.append(key);
        query.append(":");
        query.append(mapKey);
        query.append(" <= ");
        query.append(" ? ");

        filtrableParameters.add(calDebut);
        filtrableParameters.add(calFin);
        filtrableCount++;
    }

    private void addIntervalDate(StringBuilder query, String key, String mapKey, Date min, Date max, String prefix) {
        appendWhereOrAnd(query);

        if (min != null) {
            Calendar calDebut = DateUtil.toCalendarFromNotNullDate(min);
            DateUtil.setDateToBeginingOfDay(calDebut);

            query.append(prefix);
            query.append(key);
            query.append(":");
            query.append(mapKey);
            query.append(" >= ");
            query.append(" ? ");
            filtrableParameters.add(calDebut);
        }

        if (max != null) {
            Calendar calFin = DateUtil.toCalendarFromNotNullDate(max);
            DateUtil.setDateToEndOfDay(calFin);
            if (min != null) {
                query.append(AND);
            }
            query.append(prefix);
            query.append(key);
            query.append(":");
            query.append(mapKey);
            query.append(" <= ");
            query.append(" ? ");
            filtrableParameters.add(calFin);
        }

        filtrableCount++;
    }

    protected void buildQueryFilters(StringBuilder query) {
        filtrableParameters = new ArrayList<>();
        filtrableCount = 0;

        Map<String, String> prefix = new HashMap<>();
        PageSelections<Map<String, Serializable>> pageSelections = getCurrentFiltrableMap();
        for (PageSelection<Map<String, Serializable>> pageSelection : pageSelections.getEntries()) {
            buildQueryFiltrableProperty(query, prefix, pageSelection);
            // il y a un seul document pour le filtre
            break;
        }
    }

    protected void buildQueryFiltrableProperty(
        StringBuilder query,
        Map<String, String> prefix,
        PageSelection<Map<String, Serializable>> pageSelection
    ) {
        Set<String> filtrableProps = new HashSet<>();
        if (getFiltrableProperty() != null) {
            for (String property : getFiltrableProperty()) {
                String[] props = property.split("\\.");
                if (props.length > 1) {
                    filtrableProps.add(props[1]);
                    prefix.put(props[1], props[0] + ".");
                } else {
                    filtrableProps.add(props[0]);
                }
            }
        }
        if (!filtrableProps.isEmpty()) {
            for (String key : pageSelection.getData().keySet()) {
                Serializable value = pageSelection.getData().get(key);
                if (!(value instanceof Map)) {
                    MapInfo mapInfo = retrieveMapInfo(pageSelection.getData(), key);
                    if (value instanceof String) {
                        buildFiltrablePropertyString(query, prefix, value, mapInfo);
                    } else if (value instanceof Date) {
                        addParameterDate(
                            query,
                            mapInfo.getPrefix(),
                            mapInfo.getProperty(),
                            (Date) value,
                            getPrefix(prefix, mapInfo.getPrefix() + ":" + mapInfo.getProperty())
                        );
                    } else if (value instanceof Boolean) {
                        addParameter(
                            query,
                            mapInfo.getPrefix(),
                            mapInfo.getProperty(),
                            (Boolean) value ? 1 : 0,
                            getPrefix(prefix, mapInfo.getPrefix() + ":" + mapInfo.getProperty())
                        );
                    }
                } else {
                    buildQueryGenericFilters(query, prefix, pageSelection, filtrableProps, key, value);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private MapInfo retrieveMapInfo(Map<String, Serializable> data, String key) {
        for (Entry<String, Serializable> entry : data.entrySet()) {
            Object obj = entry.getValue();
            if (obj instanceof Map) {
                for (String objKey : ((Map<String, Serializable>) obj).keySet()) {
                    if (objKey.equals(key)) {
                        MapInfo mapInfo = new MapInfo();

                        String prefix = entry.getKey();

                        String alias = getPrefixForSchema().get(entry.getKey());
                        if (StringUtils.isNotEmpty(alias)) {
                            prefix = alias + "." + prefix;
                        }

                        mapInfo.setPrefix(prefix);
                        mapInfo.setProperty(objKey);
                        return mapInfo;
                    }
                }
            }
        }

        throw new NuxeoException("Impossible de filtrer sur " + key);
    }

    protected void buildQueryGenericFilters(
        StringBuilder query,
        Map<String, String> prefix,
        PageSelection<Map<String, Serializable>> pageSelection,
        Set<String> filtrableProps,
        String key,
        Serializable value
    ) {
        @SuppressWarnings("unchecked")
        Map<String, Serializable> valueMap = (Map<String, Serializable>) value;

        Set<String> keysToRemove = new HashSet<>();

        // 1- Gestion des filtres de type date (date de début, date de fin, intervalle)
        for (Entry<String, Serializable> entry : valueMap.entrySet()) {
            String propKey = entry.getKey();
            // La valeur a-t-elle déjà été traitée dans le cas d'un min/max  ?
            if (!keysToRemove.contains(propKey)) {
                String fullKey = key + ":" + propKey;

                if (filtrableProps.contains(fullKey)) {
                    Serializable subValue = entry.getValue();

                    if (subValue instanceof Date) {
                        buildQueryGenericFilterDate(
                            query,
                            prefix,
                            pageSelection,
                            valueMap,
                            keysToRemove,
                            propKey,
                            subValue
                        );
                    }
                }
            }
        }

        keysToRemove.forEach(valueMap::remove);

        // 2- Gestion des autres types de filtres
        for (Entry<String, Serializable> entry : valueMap.entrySet()) {
            String fullKey = key + ":" + entry.getKey();

            if (filtrableProps.contains(fullKey)) {
                MapInfo mapInfo = retrieveMapInfo(pageSelection.getData(), entry.getKey());
                Serializable subValue = entry.getValue();

                if (subValue instanceof String) {
                    buildFiltrablePropertyString(query, prefix, subValue, mapInfo);
                } else if (subValue instanceof Boolean) {
                    addParameter(
                        query,
                        mapInfo.getPrefix(),
                        mapInfo.getProperty(),
                        (Boolean) subValue ? 1 : 0,
                        getPrefix(prefix, mapInfo.getPrefix() + ":" + mapInfo.getProperty())
                    );
                }
            }
        }
    }

    protected void buildQueryGenericFilterDate(
        StringBuilder query,
        Map<String, String> prefix,
        PageSelection<Map<String, Serializable>> pageSelection,
        Map<String, Serializable> valueMap,
        Set<String> keysToRemove,
        String propKey,
        Serializable subValue
    ) {
        Date beginDate = null;
        Date endDate = null;
        if (propKey.endsWith("Max")) {
            // Fin d'intervalle, y a-t-il un début ?
            endDate = (Date) subValue;
            String propKeyMin = StringUtils.removeEnd(propKey, "Max");

            MapInfo mapInfo = retrieveMapInfo(pageSelection.getData(), propKey);
            String property = StringUtils.remove(mapInfo.getProperty(), "Max");

            beginDate = (Date) valueMap.get(propKeyMin);
            if (
                !buildIntervalFilter(
                    query,
                    prefix,
                    keysToRemove,
                    Pair.of(beginDate, endDate),
                    Pair.of(propKey, propKeyMin),
                    mapInfo,
                    property
                )
            ) {
                // Juste une date de fin
                addIntervalDate(
                    query,
                    mapInfo.getPrefix(),
                    property,
                    null,
                    endDate,
                    getPrefix(prefix, mapInfo.getPrefix() + ":" + property)
                );

                // Pour ne pas retomber sur ces valeurs, on les enlève de valueMap
                keysToRemove.add(propKey);
            }
        } else {
            // Début d'intervalle, y a-t-il une fin ?
            beginDate = (Date) subValue;
            String propKeyMax = propKey + "Max";

            endDate = (Date) valueMap.get(propKeyMax);
            MapInfo mapInfo = retrieveMapInfo(pageSelection.getData(), propKey);

            String property = mapInfo.getProperty();

            if (
                !buildIntervalFilter(
                    query,
                    prefix,
                    keysToRemove,
                    Pair.of(beginDate, endDate),
                    Pair.of(propKey, propKeyMax),
                    mapInfo,
                    property
                )
            ) {
                // Juste une date de début
                addIntervalDate(
                    query,
                    mapInfo.getPrefix(),
                    mapInfo.getProperty(),
                    beginDate,
                    null,
                    getPrefix(prefix, mapInfo.getPrefix() + ":" + mapInfo.getProperty())
                );

                // Pour ne pas retomber sur ces valeurs, on les enlève de valueMap
                keysToRemove.add(propKey);
            }
        }
    }

    protected boolean buildIntervalFilter(
        StringBuilder query,
        Map<String, String> prefix,
        Set<String> keysToRemove,
        Pair<Date, Date> dates,
        Pair<String, String> propKeys,
        MapInfo mapInfo,
        String property
    ) {
        Date beginDate = dates.getLeft();
        Date endDate = dates.getRight();
        if (beginDate != null && endDate != null && beginDate.before(endDate)) {
            // Il y a un début et une fin => gérer l'intervalle
            addIntervalDate(
                query,
                mapInfo.getPrefix(),
                property,
                beginDate,
                endDate,
                getPrefix(prefix, mapInfo.getPrefix() + ":" + property)
            );

            // Pour ne pas retomber sur ces valeurs, on les enlève de valueMap
            keysToRemove.add(propKeys.getLeft());
            keysToRemove.add(propKeys.getRight());

            return true;
        }
        return false;
    }

    protected void buildFiltrablePropertyString(
        StringBuilder query,
        Map<String, String> prefixMap,
        Serializable value,
        MapInfo mapInfo
    ) {
        String valeur = (String) value;
        if (StringUtils.isNotEmpty(valeur)) {
            valeur = valeur.trim().replace('*', '%');
            Serializable paramValue;
            if (Boolean.TRUE.toString().equals(valeur)) {
                paramValue = 1;
            } else if (Boolean.FALSE.toString().equals(valeur)) {
                paramValue = 0;
            } else {
                paramValue = valeur;
            }
            addParameter(
                query,
                mapInfo.getPrefix(),
                mapInfo.getProperty(),
                paramValue,
                getPrefix(prefixMap, mapInfo.getPrefix() + ":" + mapInfo.getProperty())
            );
        }
    }

    private String getPrefix(Map<String, String> prefixMap, String key) {
        return ObjectHelper.requireNonNullElse(prefixMap.get(key), "");
    }

    protected List<Object> getQueryParams() {
        return new ArrayList<>(filtrableParameters);
    }
}
