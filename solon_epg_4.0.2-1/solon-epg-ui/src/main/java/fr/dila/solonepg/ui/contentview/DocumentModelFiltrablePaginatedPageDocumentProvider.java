package fr.dila.solonepg.ui.contentview;

import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.DateUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageProviderDefinition;
import org.nuxeo.ecm.platform.query.api.PageSelection;
import org.nuxeo.ecm.platform.query.api.PageSelections;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;

public class DocumentModelFiltrablePaginatedPageDocumentProvider extends AbstractFiltrableProvider {
    public static final String MAX = "Max";

    private static final STLogger LOG = STLogFactory.getLog(DocumentModelFiltrablePaginatedPageDocumentProvider.class);

    private static final long serialVersionUID = 1L;

    protected transient List<Object> filtrableParameters;

    private int filtrableCount;

    public DocumentModelFiltrablePaginatedPageDocumentProvider() {
        super();
        filtrableCount = 0;
        filtrableParameters = new ArrayList<>();
    }

    @Override
    public List<DocumentModel> getCurrentPage() {
        checkQueryCache();
        if (currentPageDocuments == null) {
            error = null;
            errorMessage = null;

            final CoreSession coreSession = getCoreSession();
            if (query == null) {
                buildQuery(coreSession);
            }
            if (query == null) {
                throw new NuxeoException(String.format("Cannot perform null query: check provider '%s'", getName()));
            }

            currentPageDocuments = new ArrayList<>();

            try {
                LOG.debug(
                    STLogEnumImpl.FAIL_GET_QUERYASSEMBLER_TEC,
                    String.format(
                        "Perform query for provider '%s': '%s' with pageSize=%s, offset=%s",
                        getName(),
                        query,
                        Long.valueOf(getPageSize()),
                        Long.valueOf(offset)
                    )
                );

                final List<Object> params = new ArrayList<>();
                params.addAll(filtrableParameters);
                params.addAll(getOtherParams());

                final Object[] paramsObject = params.toArray();

                resultsCount = QueryUtils.doCountQuery(coreSession, query, paramsObject);

                Long pagesize = getPageSize() < 1 ? maxPageSize : getPageSize();

                if (resultsCount > 0) {
                    String documentType = properties.get("documentType") == null
                        ? "Document"
                        : properties.get("documentType").toString();
                    currentPageDocuments =
                        QueryUtils.doUFNXQLQueryAndFetchForDocuments(
                            coreSession,
                            documentType,
                            query,
                            paramsObject,
                            pagesize,
                            offset
                        );
                }

                LOG.debug(
                    STLogEnumImpl.FAIL_GET_QUERYASSEMBLER_TEC,
                    String.format(
                        "Performed query for provider '%s': got %s hits",
                        getName(),
                        Long.valueOf(resultsCount)
                    )
                );
            } catch (final Exception e) {
                error = e;
                errorMessage = e.getMessage();
                LOG.warn(STLogEnumImpl.FAIL_GET_QUERYASSEMBLER_TEC, e);
            }
        }
        return currentPageDocuments;
    }

    protected Collection<? extends Object> getOtherParams() {
        return new ArrayList<>();
    }

    private void addSingleFilter(
        StringBuilder query,
        String key,
        Entry<String, Serializable> entry,
        String prefix,
        Map<String, Serializable> map
    ) {
        final Serializable value = entry.getValue();
        if (value instanceof String) {
            String valeur = (String) value;
            addParameterString(query, valeur, key, prefix, entry);
        } else if (value instanceof Date) {
            addParameterDate(query, key, entry.getKey(), (Date) value, prefix, map);
        } else if (value instanceof Boolean) {
            addParameter(query, key, entry.getKey(), BooleanUtils.isTrue((Boolean) value) ? 1 : 0, prefix);
        }
    }

    protected void buildDynamicFilters(StringBuilder query, PageSelection<Map<String, Serializable>> pageSelection) {
        final Set<String> filtrableProps = getFiltrableProperty();
        String prefix = filtrableProps
            .stream()
            .map(prop -> StringUtils.isNotBlank(getPrefixFromProperty(prop)) ? getPrefixFromProperty(prop) + "." : "")
            .filter(StringUtils::isNotEmpty)
            .findFirst()
            .orElse("");

        if (CollectionUtils.isNotEmpty(filtrableProps)) {
            for (final String key : pageSelection.getData().keySet()) {
                final Object obj = pageSelection.getData().get(key);
                if (obj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    final Map<String, Serializable> map = (Map<String, Serializable>) obj;

                    for (final Entry<String, Serializable> entry : map.entrySet()) {
                        addSingleFilter(query, key, entry, prefix, map);
                    }
                }
            }
        }
    }

    @Override
    protected void buildQuery(final CoreSession coreSession) {
        SortInfo[] sortArray = getSortInfosWithAdditionalSort();

        final PageProviderDefinition def = getDefinition();
        StringBuilder query = new StringBuilder(def.getPattern());

        query = getQuery(query);

        filtrableParameters = new ArrayList<>();
        filtrableCount = 0;

        final PageSelections<Map<String, Serializable>> pageSelections = getCurrentFiltrableMap();
        if (pageSelections.getEntries() != null) {
            for (final PageSelection<Map<String, Serializable>> pageSelection : pageSelections.getEntries()) {
                buildDynamicFilters(query, pageSelection);
                // il y a un seul document pour le filtre
                break;
            }
        }

        query = completeQueryIfNeeded(query);

        // on force les quotes est les escape des paramètres
        String newQuery = NXQLQueryBuilder.getQuery(
            query.toString(),
            null,
            false,
            false,
            this.searchDocumentModel,
            sortArray
        );

        if (!newQuery.equals(this.query)) {
            // query has changed => refresh
            refresh();
            this.query = newQuery;
        }
    }

    protected StringBuilder completeQueryIfNeeded(final StringBuilder query) {
        return query;
    }

    protected StringBuilder getQuery(final StringBuilder query) {
        return new StringBuilder(
            NXQLQueryBuilder.getQuery(query.toString(), getParameters(), false, false, this.searchDocumentModel)
        );
    }

    private void addParameterString(
        StringBuilder query,
        String valeur,
        String key,
        String prefix,
        Entry<String, Serializable> entry
    ) {
        if (StringUtils.isNotEmpty(valeur)) {
            valeur = valeur.trim().replace('*', '%');
            if (Boolean.TRUE.toString().equals(valeur)) {
                addParameter(query, key, entry.getKey(), 1, prefix);
            } else if (Boolean.FALSE.toString().equals(valeur)) {
                addParameter(query, key, entry.getKey(), 0, prefix);
            } else {
                addParameter(query, key, entry.getKey(), valeur, prefix);
            }
        }
    }

    private void addParameterDate(
        final StringBuilder query,
        final String key,
        final String mapKey,
        final Date value,
        final String prefix,
        final Map<String, Serializable> map
    ) {
        if (StringUtils.isNotBlank(mapKey) && mapKey.endsWith(MAX)) {
            // filte dateMax des widgets filtrable_date
            return;
        }

        final Calendar calDebut = DateUtil.toCalendarFromNotNullDate(value);
        DateUtil.setDateToBeginingOfDay(calDebut);

        final Calendar calFin = Calendar.getInstance();

        final Date maxValue = (Date) map.get(mapKey + MAX);

        if (maxValue == null) {
            calFin.setTime(new Date());
        } else {
            calFin.setTime(maxValue);
        }

        DateUtil.setDateToEndOfDay(calFin);

        addCorrectTokenBeforeParam(query);

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

    private void addParameter(
        final StringBuilder query,
        final String key,
        final String mapKey,
        final Serializable value,
        final String prefix
    ) {
        addCorrectTokenBeforeParam(query);

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

    @Override
    public void setSearchDocumentModel(final DocumentModel searchDocumentModel) {
        if (this.searchDocumentModel != searchDocumentModel) {
            this.searchDocumentModel = searchDocumentModel;
            refresh();
        }
    }

    @Override
    protected void processFiltrableProperty(String property, Serializable value) {
        String filtrableProp = getPropertyName(property);

        String[] filtrableProps = filtrableProp.split(":");

        Map<String, Serializable> map = (Map<String, Serializable>) super.getFiltrableValue(filtrableProps[0]);
        if (map == null) {
            map = new HashMap<>();
        }

        Serializable valueToInsert = value == null ? map.get(filtrableProps[1]) : value;
        map.put(filtrableProps[1], valueToInsert);

        Serializable valueToInsertMax = super.getFiltrableValue(
            property + DocumentModelFiltrablePaginatedPageDocumentProvider.MAX
        );
        if (valueToInsertMax != null) {
            map.put(filtrableProps[1] + DocumentModelFiltrablePaginatedPageDocumentProvider.MAX, valueToInsertMax);
        }
        super.putFiltrableValue(filtrableProps[0], (Serializable) map);
    }
}
