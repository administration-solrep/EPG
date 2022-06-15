package fr.dila.solonepg.ui.contentview;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TranspositionDirective;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.core.dto.activitenormative.OrdonnanceDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreLoiDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreTraiteDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.TranspositionDirectiveDTOImpl;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.domain.STDomainObject;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.util.ObjectHelper;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class PanMainTableauPageProvider extends AbstractPanPageProvider<STDomainObject, AbstractMapDTO> {
    private static final long serialVersionUID = 1L;

    private static final String FIXED_SQL_TO_REPLACE = "AND 0";

    private String attributSchema = null;
    private boolean excludeRatifie = false;
    private String legislature = null;

    private List<String> extraSchemas = new ArrayList<>();
    private List<String> extraSchemasConditions = new ArrayList<>();
    private String extraWhere = null;
    private boolean forceNonPaginatedResults = false;
    private Class<? extends STDomainObject> adapterClass = ActiviteNormative.class;
    private Class<? extends AbstractMapDTO> dtoClass = null;

    protected void populateFromIds(CoreSession session, List<String> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            List<DocumentModel> documents = QueryHelper.retrieveDocuments(
                session,
                ids,
                QueryHelper.toPrefetchInfo(
                    STSchemaConstant.DUBLINCORE_SCHEMA,
                    SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA
                )
            );
            currentItems.addAll(mapDocumentToDto(session, documents));
        }
    }

    protected Collection<? extends Map<String, Serializable>> mapDocumentToDto(
        CoreSession session,
        List<DocumentModel> documents
    ) {
        return documents
            .stream()
            .map(this::getAdapter)
            .map(adapterDocToDto -> this.mapAdapterDocToDto(session, adapterDocToDto))
            .collect(Collectors.toList());
    }

    @Override
    protected void fillCurrentPageMapList(CoreSession coreSession) {
        refreshQuery();
        currentItems = new ArrayList<>();

        List<Object> params = getQueryParams();

        Object[] qParams = params.toArray();
        resultsCount = QueryUtils.doCountQuery(coreSession, query, qParams);
        if (resultsCount > 0) {
            List<String> ids;
            if (!forceNonPaginatedResults) {
                ids = QueryUtils.doQueryForIds(coreSession, query, getPageSize(), offset, qParams);
            } else {
                ids = QueryUtils.doQueryForIds(coreSession, query, 0, 0, qParams);
            }
            populateFromIds(coreSession, ids);
        }
    }

    @Override
    protected STDomainObject getAdapter(DocumentModel documentModel) {
        return documentModel.getAdapter(adapterClass);
    }

    @Override
    protected AbstractMapDTO mapAdapterDocToDto(CoreSession session, STDomainObject adapterDoc) {
        if (TexteMaitreLoiDTOImpl.class.equals(dtoClass)) {
            return new TexteMaitreLoiDTOImpl((ActiviteNormative) adapterDoc);
        } else if (TranspositionDirectiveDTOImpl.class.equals(dtoClass)) {
            return new TranspositionDirectiveDTOImpl((TranspositionDirective) adapterDoc);
        } else if (OrdonnanceDTOImpl.class.equals(dtoClass)) {
            return new OrdonnanceDTOImpl((ActiviteNormative) adapterDoc, session);
        } else if (TexteMaitreTraiteDTOImpl.class.equals(dtoClass)) {
            return new TexteMaitreTraiteDTOImpl((ActiviteNormative) adapterDoc, session);
        }
        return null;
    }

    public void setAttributSchema(String attributSchema) {
        this.attributSchema = ObjectHelper.requireNonNullElse(attributSchema, "");
    }

    public void setExcludeRatifie(boolean excludeRatifie) {
        this.excludeRatifie = excludeRatifie;
    }

    public void setLegislature(String legislature) {
        this.legislature = legislature;
    }

    private void refreshQuery() {
        StringBuilder newFrom = new StringBuilder();
        extraSchemas.forEach(s -> newFrom.append(", ").append(s));

        StringBuilder newCondition = new StringBuilder();
        extraSchemasConditions.forEach(c -> newCondition.append(" AND ").append(c));

        newCondition.append(" AND d.norma:").append(attributSchema).append(" = '1'");

        if (StringUtils.isNotBlank(legislature)) {
            newCondition.append(
                " AND d." +
                TexteMaitreConstants.TEXTE_MAITRE_PREFIX +
                ":" +
                TexteMaitreConstants.LEGISLATURE_PUBLICATION +
                " = " +
                "'" +
                legislature +
                "'"
            );
        }

        if (excludeRatifie) {
            newCondition.append(
                " AND d." + TexteMaitreConstants.TEXTE_MAITRE_PREFIX + ":" + TexteMaitreConstants.RATIFIE + " = 0 "
            );
        }
        if (StringUtils.isNotBlank(extraWhere)) {
            newCondition.append(" AND ").append(this.extraWhere);
        }

        this.query =
            this.query.replaceAll("WHERE ", newFrom + " WHERE ")
                .replaceAll(FIXED_SQL_TO_REPLACE, newCondition.toString());
    }

    public void overrideAdapterClass(Class<? extends STDomainObject> adapterClass) {
        this.adapterClass = adapterClass;
    }

    public void setDtoClass(Class<? extends AbstractMapDTO> dtoClass) {
        this.dtoClass = dtoClass;
    }

    public void addSchema(String shema, String condition) {
        extraSchemas.add(shema);
        extraSchemasConditions.add(condition);
    }

    public void setExtraWhere(String extraWhere) {
        this.extraWhere = extraWhere;
    }

    public void setForceNonPaginatedResults(Boolean forceNonPaginatedResults) {
        this.forceNonPaginatedResults = forceNonPaginatedResults;
    }
}
