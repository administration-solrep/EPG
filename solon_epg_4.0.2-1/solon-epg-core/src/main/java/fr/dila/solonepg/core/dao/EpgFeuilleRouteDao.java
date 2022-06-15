package fr.dila.solonepg.core.dao;

import fr.dila.cm.core.dao.FeuilleRouteDao;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.criteria.EpgFeuilleRouteCriteria;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * DAO des étapes de feuilles de routes.
 *
 * @author jtremeaux
 */
public class EpgFeuilleRouteDao extends FeuilleRouteDao {

    /**
     * Constructeur de FeuilleRouteDao.
     *
     */
    public EpgFeuilleRouteDao(CoreSession session, EpgFeuilleRouteCriteria criteria) {
        super(session, criteria);
    }

    /**
     * Construit la chaîne de la requete et la liste des paramètres.
     */
    @Override
    protected void buildWhere(final CoreSession session, StringBuilder sb) {
        super.buildWhere(session, sb);

        if (StringUtils.isNotBlank(criteria.getDirection())) {
            sb
                .append(" AND r.")
                .append(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA_PREFIX)
                .append(":")
                .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_DIRECTION_PROPERTY)
                .append(" = ? ");
            paramList.add(criteria.getDirection());
        } else if (criteria.getDirectionNull()) {
            sb
                .append(" AND ( r.")
                .append(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA_PREFIX)
                .append(":")
                .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_DIRECTION_PROPERTY)
                .append(" IS NULL");
            sb
                .append(" OR r.")
                .append(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA_PREFIX)
                .append(":")
                .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_DIRECTION_PROPERTY)
                .append(" ='' )");
        }

        if (StringUtils.isNotBlank(((EpgFeuilleRouteCriteria) criteria).getNumero())) {
            sb
                .append(" AND r.")
                .append(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA_PREFIX)
                .append(":")
                .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_NUMERO_PROPERTY)
                .append(" ILIKE ? ");
            paramList.add(((EpgFeuilleRouteCriteria) criteria).getNumero().replace("*", "%"));
        }

        if (StringUtils.isNotBlank(((EpgFeuilleRouteCriteria) criteria).getTypeActe())) {
            sb
                .append(" AND r.")
                .append(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA_PREFIX)
                .append(":")
                .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY)
                .append(" = ? ");
            paramList.add(((EpgFeuilleRouteCriteria) criteria).getTypeActe());
        } else if (((EpgFeuilleRouteCriteria) criteria).getTypeActeNull()) {
            sb
                .append(" AND ( r.")
                .append(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA_PREFIX)
                .append(":")
                .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY)
                .append(" IS NULL ");
            sb
                .append(" OR r.")
                .append(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA_PREFIX)
                .append(":")
                .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY)
                .append(" ='' )");
        }
    }

    @Override
    protected StringBuilder buildSelect() {
        return super
            .buildSelect()
            .append(", r.")
            .append(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA_PREFIX)
            .append(":")
            .append(SSFeuilleRouteConstant.FEUILLE_ROUTE_NUMERO_PROPERTY);
    }

    /**
     * Recherche de feuilles de route par critères de recherche.
     *
     * @param criteria Critères de recherche
     * @return Liste de feuilles de route
     */
    public List<DocumentModel> list(final CoreSession session) {
        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE,
            queryString,
            paramList.toArray()
        );
    }

    /**
     * Recherche d'étapes de feuille de route par critères de recherche.
     * Retourne un unique résultat.
     *
     * @param criteria Critères de recherche
     * @return Liste de feuilles de route
     */
    public DocumentModel getSingleResult(final CoreSession session) {
        final List<DocumentModel> list = list(session);

        if (list == null || list.isEmpty()) {
            return null;
        }

        if (list.size() > 1) {
            throw new NuxeoException("La requête doit retourner résultat unique: " + criteria);
        }

        return list.iterator().next();
    }
}
