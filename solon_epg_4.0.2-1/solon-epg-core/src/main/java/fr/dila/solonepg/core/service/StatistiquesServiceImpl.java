package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.service.StatistiquesService;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.naiad.nuxeo.ufnxql.core.query.FlexibleQueryMaker;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.query.sql.model.OrderByExprs;
import org.nuxeo.ecm.core.query.sql.model.Predicates;
import org.nuxeo.ecm.core.query.sql.model.QueryBuilder;
import org.nuxeo.ecm.platform.usermanager.UserManager;

public class StatistiquesServiceImpl implements StatistiquesService {
    private static final String QUERY_UFNXQL_NB_TYPE_ACTE =
        "SELECT d.dos:typeActe, count() AS count FROM Dossier AS d " +
        " WHERE d.dos:statutArchivage IN ('1', '2') " +
        " AND testDossierAcl(d.ecm:uuid) = 1 " +
        " AND d.dc:created BETWEEN ? AND ? " +
        " GROUP BY d.dos:typeActe";

    /**
     * get Users By status(active or not active)
     *
     * @param active
     *            the user status
     * @return list of users
     */
    @Override
    public DocumentModelList getUsers(boolean active) {
        UserManager userManager = STServiceLocator.getUserManager();
        QueryBuilder builder;
        Date today = Calendar.getInstance().getTime();

        if (active) {
            builder =
                new QueryBuilder()
                    .predicate((Predicates.or(Predicates.gte("dateFin", today), Predicates.isnull("dateFin"))))
                    .and(Predicates.eq("deleted", "FALSE"));
        } else {
            builder =
                new QueryBuilder().predicate(Predicates.lte("dateFin", today)).and(Predicates.eq("deleted", "TRUE"));
        }

        builder = builder.order(OrderByExprs.asc(STSchemaConstant.USER_LAST_NAME));

        return userManager.searchUsers(builder);
    }

    /**
     * get Count des typed d'acte par type dans l'interval de date
     *
     * @param dateDebut
     *            Date de début de l'interval
     * @param dateFin
     *            Date de fin de l'interval
     * @return liste des users
     */
    @Override
    public Map<String, String> getNbTypeActeParType(CoreSession session, Calendar dateDebut, Calendar dateFin) {
        Map<String, String> map = new HashMap<>();
        try (
            IterableQueryResult res = QueryUtils.doUFNXQLQuery(
                session,
                QUERY_UFNXQL_NB_TYPE_ACTE,
                new Object[] { dateDebut, dateFin }
            )
        ) {
            final Iterator<Map<String, Serializable>> itr = res.iterator();

            while (itr.hasNext()) {
                Map<String, Serializable> row = itr.next();
                map.put(row.get("d.dos:typeActe").toString(), row.get(FlexibleQueryMaker.COL_COUNT).toString());
            }
        }

        return map;
    }
}
