package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.ClientRuntimeException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageProviderDefinition;
import org.nuxeo.ecm.platform.query.api.PageSelection;
import org.nuxeo.ecm.platform.query.api.PageSelections;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;

import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.web.contentview.CorePageProviderUtil;
import fr.dila.st.web.contentview.PaginatedPageDocumentProvider;

/**
 * Provider avec filtre {@link PaginatedPageDocumentProvider}
 * 
 * @author admin
 * 
 */
public class DocumentModelFiltrablePaginatedPageDocumentProvider extends AbstractFiltrableProvider {

    private static final String MAX = "Max";

    private static final Log log = LogFactory.getLog(DocumentModelFiltrablePaginatedPageDocumentProvider.class);

    private static final long serialVersionUID = 1L;

    private List<Object> filtrableParameters;

    private int filtrableCount;

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
                throw new ClientRuntimeException(String.format("Cannot perform null query: check provider '%s'", getName()));
            }

            currentPageDocuments = new ArrayList<DocumentModel>();

            try {

                if (log.isDebugEnabled()) {
                    log.debug(String.format("Perform query for provider '%s': '%s' with pageSize=%s, offset=%s", getName(), query,
                            Long.valueOf(getPageSize()), Long.valueOf(offset)));
                }

                final List<Object> params = new ArrayList<Object>();
                params.addAll(filtrableParameters);
                params.addAll(getOtherParams());

                final Object[] paramsObject = params.toArray();

                resultsCount = QueryUtils.doCountQuery(coreSession, query, paramsObject);

                Long pagesize = getPageSize();
                if (pagesize < 1) {
                    pagesize = maxPageSize;
                }
                if (resultsCount > 0) {
                    currentPageDocuments = QueryUtils.doUFNXQLQueryAndFetchForDocuments(coreSession, null, query, paramsObject, pagesize, offset);
                }

                if (log.isDebugEnabled()) {
                    log.debug(String.format("Performed query for provider '%s': got %s hits", getName(), Long.valueOf(resultsCount)));
                }

            } catch (final Exception e) {
                error = e;
                errorMessage = e.getMessage();
                log.warn(e.getMessage(), e);
            }
        }
        return currentPageDocuments;
    }

    protected Collection<? extends Object> getOtherParams() {
        return new ArrayList<Object>();
    }

    @Override
    protected void buildQuery(final CoreSession coreSession) {
        try {
            SortInfo[] sortArray = getSortInfosWithAdditionalSort();
            String newQuery;
            final PageProviderDefinition def = getDefinition();
            StringBuilder query = new StringBuilder(def.getPattern());

            query = getQuery(query);

            filtrableParameters = new ArrayList<Object>();
            filtrableCount = 0;

            String prefix = "";
            final PageSelections<Map<String, Serializable>> pageSelections = getCurrentFiltrableMap();
            for (final PageSelection<Map<String, Serializable>> pageSelection : pageSelections.getEntries()) {
                final Set<String> filtrableProps = new HashSet<String>();
                if (getFiltrableProperty() != null) {
                    for (final String property : getFiltrableProperty()) {
                        final String[] props = property.split("\\.");
                        if (props.length > 1) {
                            filtrableProps.add(props[1]);
                            prefix = props[0] + ".";
                        } else {
                            filtrableProps.add(props[0]);
                        }
                    }
                }
                if (!filtrableProps.isEmpty()) {
                    for (final String key : pageSelection.getData().keySet()) {
                        final Object obj = pageSelection.getData().get(key);
                        if (obj instanceof Map) {
                            @SuppressWarnings("unchecked")
                            final Map<String, Serializable> map = (Map<String, Serializable>) obj;
                            for (final Entry<String, Serializable> entry : map.entrySet()) {
                                final Serializable value = entry.getValue();
                                if (value instanceof String) {
                                    String valeur = (String) value;
                                    if (!StringUtils.isEmpty(valeur)) {
                                        valeur = valeur.trim().replace('*', '%');
                                        if (Boolean.TRUE.toString().equals(valeur)) {
                                            addParameter(query, key, entry.getKey(), 1, prefix);
                                        } else if (Boolean.FALSE.toString().equals(valeur)) {
                                            addParameter(query, key, entry.getKey(), 0, prefix);
                                        } else {
                                            addParameter(query, key, entry.getKey(), valeur, prefix);
                                        }
                                    }
                                } else if (value instanceof Date) {
                                    addParameterDate(query, key, entry.getKey(), (Date) value, prefix, map);
                                } else if (value instanceof Boolean) {
                                    addParameter(query, key, entry.getKey(), (Boolean) value ? 1 : 0, prefix);
                                }
                            }
                        }
                    }
                }
                // il y a un seul document pour le filtre
                break;
            }

            query = completeQueryIfNeeded(query);

            // on force les quotes est les escape des paramètres
            newQuery = NXQLQueryBuilder.getQuery(query.toString(), null, Boolean.FALSE, Boolean.FALSE, sortArray);

            if (newQuery != null && !newQuery.equals(this.query)) {
                // query has changed => refresh
                refresh();
                this.query = newQuery;
            }
        } catch (final ClientException e) {
            throw new ClientRuntimeException(e);
        }
    }

    protected StringBuilder completeQueryIfNeeded(final StringBuilder query) {
        return query;
    }

    protected StringBuilder getQuery(final StringBuilder query) throws ClientException {
        return new StringBuilder(NXQLQueryBuilder.getQuery(query.toString(), getParameters(), Boolean.FALSE, Boolean.FALSE));
    }

    private void addParameterDate(final StringBuilder query, final String key, final String mapKey, final Date value, final String prefix,
            final Map<String, Serializable> map) {

        if (StringUtils.isNotBlank(mapKey) && mapKey.endsWith(MAX)) {
            // filte dateMax des widgets filtrable_date
            return;
        }

        final Calendar calDebut = Calendar.getInstance();
        calDebut.setTime(value);
        calDebut.set(Calendar.HOUR_OF_DAY, 0);
        calDebut.set(Calendar.MINUTE, 0);
        calDebut.set(Calendar.SECOND, 0);
        calDebut.set(Calendar.MILLISECOND, 0);

        final Calendar calFin = Calendar.getInstance();

        final Date maxValue = (Date) map.get(mapKey + MAX);

        if (maxValue == null) {
            calFin.setTime(new Date());
        } else {
            calFin.setTime(maxValue);
        }

        calFin.set(Calendar.HOUR_OF_DAY, 23);
        calFin.set(Calendar.MINUTE, 59);
        calFin.set(Calendar.SECOND, 59);
        calFin.set(Calendar.MILLISECOND, 999);

        if (query.toString().toUpperCase().contains("WHERE")) {
            query.append(" AND ");
        } else {
            query.append(" WHERE ");
        }

        query.append(prefix);
        query.append(key);
        query.append(":");
        query.append(mapKey);
        query.append(" >= ");
        query.append(" ? ");

        query.append(" AND ");
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

    private void addParameter(final StringBuilder query, final String key, final String mapKey, final Serializable value, final String prefix) {
        if (query.toString().toUpperCase().contains("WHERE")) {
            query.append(" AND ");
        } else {
            query.append(" WHERE ");
        }

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
    public Boolean isFiltreActif() {
        return filtrableParameters != null && !filtrableParameters.isEmpty();
    }

    @Override
    public Integer getFiltreActif() {
        if (filtrableParameters != null) {
            return filtrableCount;
        }
        return 0;
    }

    @Override
    public void setSearchDocumentModel(final DocumentModel searchDocumentModel) {
        if (this.searchDocumentModel != searchDocumentModel) {
            this.searchDocumentModel = searchDocumentModel;
            refresh();
        }
    }

}
