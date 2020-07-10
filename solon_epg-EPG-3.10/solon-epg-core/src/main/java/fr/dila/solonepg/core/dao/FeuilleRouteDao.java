package fr.dila.solonepg.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.criteria.FeuilleRouteCriteria;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.query.QueryUtils;

/**
 * DAO des étapes de feuilles de routes.
 * 
 * @author jtremeaux
 */
public class FeuilleRouteDao {

    /**
     * Critères de recherche.
     */
    private final FeuilleRouteCriteria criteria;

    /**
     * Chaîne de la requête après construction après construction de la requête.
     */
    private String queryString;

    /**
     * Paramètres après construction de la requête.
     */
    private List<Object> paramList;

    /**
     * Constructeur de FeuilleRouteDao.
     * 
     * @param session Session
     * @param criteria Critères de recherche
     * @throws ClientException
     */
    public FeuilleRouteDao(final CoreSession session, final FeuilleRouteCriteria criteria) throws ClientException {
        this.criteria = criteria;
        build(session);
    }

    /**
     * Construit la chaîne de la requete et la liste des paramètres.
     * 
     * @throws ClientException
     */
    protected void build(final CoreSession session) throws ClientException {
        final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
        final DocumentModel feuilleRouteModelFolder = feuilleRouteModelService.getFeuilleRouteModelFolder(session);

        paramList = new ArrayList<Object>();
        final StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT r.").append(STSchemaConstant.ECM_SCHEMA_PREFIX).append(":")
                .append(STSchemaConstant.ECM_UUID_PROPERTY).append(" AS id, r.").append(STSchemaConstant.ECM_SCHEMA_PREFIX).append(":")
                .append(STSchemaConstant.ECM_CURRENT_LIFE_CYCLE_STATE).append(", r.").append(STSchemaConstant.DUBLINCORE_SCHEMA_PREFIX).append(":")
                .append(STSchemaConstant.DUBLINCORE_TITLE_PROPERTY).append(", r.").append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":")
                .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_NUMERO_PROPERTY).append(", r.")
                .append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":")
                .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_DEMANDE_VALIDATION_PROPERTY).append(", r.")
                .append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":").append(STSchemaConstant.FEUILLE_ROUTE_MINISTERE_PROPERTY)
                .append(", r.").append(STSchemaConstant.DUBLINCORE_SCHEMA_PREFIX).append(":").append(STSchemaConstant.DUBLINCORE_CREATOR_PROPERTY)
                .append(", r.").append(STSchemaConstant.DUBLINCORE_SCHEMA_PREFIX).append(":").append(STSchemaConstant.DUBLINCORE_MODIFIED_PROPERTY)
                .append(" FROM ").append(STConstant.FEUILLE_ROUTE_DOCUMENT_TYPE).append(" AS r ");

        final boolean joinRouteStep = StringUtils.isNotBlank(criteria.getRoutingTaskType())
                || StringUtils.isNotBlank(criteria.getDistributionMailboxId()) || criteria.getDeadline() != null
                || criteria.getAutomaticValidation() != null || criteria.getObligatoireSGG() != null || criteria.getObligatoireMinistere() != null;

        if (joinRouteStep) {
            queryBuilder.append(", ").append(STConstant.ROUTE_STEP_DOCUMENT_TYPE).append(" AS s ");
        }

        queryBuilder.append(" WHERE r.").append(STSchemaConstant.ECM_SCHEMA_PREFIX).append(":").append(STSchemaConstant.ECM_PARENT_ID_PROPERTY)
                .append(" = ? ");
        paramList.add(feuilleRouteModelFolder.getId());

        queryBuilder.append(" AND r.").append(STSchemaConstant.ECM_SCHEMA_PREFIX).append(":").append(STSchemaConstant.ECM_CURRENT_LIFE_CYCLE_STATE)
                .append(" != 'deleted' ");

        if (criteria.isCheckReadPermission()) {
            final StringBuilder sb2 = new StringBuilder("r.").append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":")
                    .append(STSchemaConstant.FEUILLE_ROUTE_MINISTERE_PROPERTY);

            final SSPrincipal principal = (SSPrincipal) session.getPrincipal();
            queryBuilder.append(feuilleRouteModelService.getMinistereCriteria(principal, sb2.toString()));
        }

        if (StringUtils.isNotBlank(criteria.getCurrentLifeCycleState())) {
            queryBuilder.append(" AND r.").append(STSchemaConstant.ECM_SCHEMA_PREFIX).append(":")
                    .append(STSchemaConstant.ECM_CURRENT_LIFE_CYCLE_STATE).append(" = ? ");
            paramList.add(criteria.getCurrentLifeCycleState());
        }

        if (StringUtils.isNotBlank(criteria.getCreationUtilisateur())) {
            queryBuilder.append(" AND r.").append(STSchemaConstant.DUBLINCORE_SCHEMA_PREFIX).append(":")
                    .append(STSchemaConstant.DUBLINCORE_CREATOR_PROPERTY).append(" = ? ");
            paramList.add(criteria.getCreationUtilisateur());
        }

        if (criteria.getCreationDateMin() != null) {
            queryBuilder.append(" AND r.").append(STSchemaConstant.DUBLINCORE_SCHEMA_PREFIX).append(":")
                    .append(STSchemaConstant.DUBLINCORE_CREATED_PROPERTY).append(" >= ? ");
            paramList.add(criteria.getCreationDateMin());
        }

        if (criteria.getCreationDateMax() != null) {
            queryBuilder.append(" AND r.").append(STSchemaConstant.DUBLINCORE_SCHEMA_PREFIX).append(":")
                    .append(STSchemaConstant.DUBLINCORE_CREATED_PROPERTY).append(" <= ? ");
            paramList.add(criteria.getCreationDateMax());
        }

        if (criteria.isFeuilleRouteDefaut()) {
            queryBuilder.append(" AND r.").append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":")
                    .append(STSchemaConstant.FEUILLE_ROUTE_DEFAUT_PROPERTY).append(" = 1 ");
        }

        if (StringUtils.isNotBlank(criteria.getMinistere())) {
            queryBuilder.append(" AND r.").append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":")
                    .append(STSchemaConstant.FEUILLE_ROUTE_MINISTERE_PROPERTY).append(" = ? ");
            paramList.add(criteria.getMinistere());
        } else if (criteria.getMinistereNull()) {
            queryBuilder.append(" AND ( r.").append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":")
                    .append(STSchemaConstant.FEUILLE_ROUTE_MINISTERE_PROPERTY).append(" IS NULL");
            //TODO à enlever lorsque l'injection de fdr aura été corrigé
            queryBuilder.append(" OR r.").append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":")
                    .append(STSchemaConstant.FEUILLE_ROUTE_MINISTERE_PROPERTY).append(" ='' )");
        }

        if (StringUtils.isNotBlank(criteria.getDirection())) {
            queryBuilder.append(" AND r.").append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":")
                    .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_DIRECTION_PROPERTY).append(" = ? ");
            paramList.add(criteria.getDirection());
        } else if (criteria.getDirectionNull()) {
            queryBuilder.append(" AND ( r.").append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":")
                    .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_DIRECTION_PROPERTY).append(" IS NULL");
            //TODO à enlever lorsque l'injection de fdr aura été corrigé
            queryBuilder.append(" OR r.").append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":")
                    .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_DIRECTION_PROPERTY).append(" ='' )");
        }

        if (StringUtils.isNotBlank(criteria.getIntitule())) {
            queryBuilder.append(" AND r.").append(STSchemaConstant.DUBLINCORE_SCHEMA_PREFIX).append(":")
                    .append(STSchemaConstant.DUBLINCORE_TITLE_PROPERTY).append(" LIKE ? ");
            paramList.add(criteria.getIntitule().replace("*", "%"));
        } else if (StringUtils.isNotBlank(criteria.getIntituleLike())) {
            queryBuilder.append(" AND r.").append(STSchemaConstant.DUBLINCORE_SCHEMA_PREFIX).append(":")
                    .append(STSchemaConstant.DUBLINCORE_TITLE_PROPERTY).append(" ILIKE ? ");
            paramList.add("%" + criteria.getIntituleLike() + "%");
        } else if (criteria.getIntituleNull()) {
            queryBuilder.append(" AND ( r.").append(STSchemaConstant.DUBLINCORE_SCHEMA_PREFIX).append(":")
                    .append(STSchemaConstant.DUBLINCORE_TITLE_PROPERTY).append(" IS NULL ");
            //TODO à enlever lorsque l'injection de fdr aura été corrigé
            queryBuilder.append(" OR r.").append(STSchemaConstant.DUBLINCORE_SCHEMA_PREFIX).append(":")
                    .append(STSchemaConstant.DUBLINCORE_TITLE_PROPERTY).append(" ='' )");
        }

        if (StringUtils.isNotBlank(criteria.getNumero())) {
            queryBuilder.append(" AND r.").append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":")
                    .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_NUMERO_PROPERTY).append(" = ? ");
            paramList.add(criteria.getNumero());
        }

        if (StringUtils.isNotBlank(criteria.getTypeActe())) {
            queryBuilder.append(" AND r.").append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":")
                    .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY).append(" = ? ");
            paramList.add(criteria.getTypeActe());
        } else if (criteria.getTypeActeNull()) {
            queryBuilder.append(" AND ( r.").append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":")
                    .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY).append(" IS NULL ");
            //TODO à enlever lorsque l'injection de fdr aura été corrigé
            queryBuilder.append(" OR r.").append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX).append(":")
                    .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY).append(" ='' )");
        }

        if (joinRouteStep) {
            queryBuilder.append(" AND r.").append(STSchemaConstant.ECM_SCHEMA_PREFIX).append(":").append(STSchemaConstant.ECM_UUID_PROPERTY)
                    .append(" = s.").append(STSchemaConstant.ROUTING_TASK_SCHEMA_PREFIX).append(":")
                    .append(STSchemaConstant.ROUTING_TASK_DOCUMENT_ROUTE_ID_PROPERTY);

            if (StringUtils.isNotBlank(criteria.getRoutingTaskType())) {
                queryBuilder.append(" AND s.").append(STSchemaConstant.ROUTING_TASK_SCHEMA_PREFIX).append(":")
                        .append(STSchemaConstant.ROUTING_TASK_TYPE_PROPERTY).append(" = ? ");
                paramList.add(criteria.getRoutingTaskType());
            }

            if (StringUtils.isNotBlank(criteria.getDistributionMailboxId())) {
                queryBuilder.append(" AND s.").append(STSchemaConstant.ROUTING_TASK_MAILBOX_ID_XPATH).append(" = ? ");
                paramList.add(criteria.getDistributionMailboxId());
            }

            if (criteria.getDeadline() != null) {
                queryBuilder.append(" AND s.").append(STSchemaConstant.ROUTING_TASK_SCHEMA_PREFIX).append(":")
                        .append(STSchemaConstant.ROUTING_TASK_DEADLINE_PROPERTY).append(" = ? ");
                paramList.add(criteria.getDeadline());
            }

            if (criteria.getAutomaticValidation() != null) {
                queryBuilder.append(" AND s.").append(STSchemaConstant.ROUTING_TASK_SCHEMA_PREFIX).append(":")
                        .append(STSchemaConstant.ROUTING_TASK_AUTOMATIC_VALIDATION_PROPERTY).append(" = ")
                        .append(criteria.getAutomaticValidation() ? "1" : "0");
            }

            if (criteria.getObligatoireSGG() != null) {
                queryBuilder.append(" AND s.").append(STSchemaConstant.ROUTING_TASK_SCHEMA_PREFIX).append(":")
                        .append(STSchemaConstant.ROUTING_TASK_OBLIGATOIRE_SGG_PROPERTY).append(" = ")
                        .append(criteria.getObligatoireSGG() ? "1" : "0");
            }

            if (criteria.getObligatoireMinistere() != null) {
                queryBuilder.append(" AND s.").append(STSchemaConstant.ROUTING_TASK_SCHEMA_PREFIX).append(":")
                        .append(STSchemaConstant.ROUTING_TASK_OBLIGATOIRE_MINISTERE_PROPERTY).append(" = ")
                        .append(criteria.getObligatoireMinistere() ? "1" : "0");
            }
        }

        queryString = queryBuilder.toString();
    }

    /**
     * Recherche de feuilles de route par critères de recherche.
     * 
     * @param criteria Critères de recherche
     * @return Liste de feuilles de route
     * @throws ClientException
     */
    public List<DocumentModel> list(final CoreSession session) throws ClientException {
        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, STConstant.FEUILLE_ROUTE_DOCUMENT_TYPE, queryString, paramList.toArray());
    }

    /**
     * Recherche d'étapes de feuille de route par critères de recherche.
     * Retourne un unique résultat.
     * 
     * @param criteria Critères de recherche
     * @return Liste de feuilles de route
     * @throws ClientException
     */
    public DocumentModel getSingleResult(final CoreSession session) throws ClientException {
        final List<DocumentModel> list = list(session);

        if (list == null || list.isEmpty()) {
            return null;
        }

        if (list.size() > 1) {
            throw new ClientException("La requête doit retourner résultat unique: " + criteria);
        }

        return list.iterator().next();
    }

    /**
     * Retourne la chaîne de la requête après construction.
     *
     * @return queryString
     */
    public String getQueryString() {
        return queryString;
    }

    /**
     * Retourne la liste des paramètres de la requête après construction.
     *
     * @return paramList
     */
    public List<Object> getParamList() {
        return paramList;
    }
}
