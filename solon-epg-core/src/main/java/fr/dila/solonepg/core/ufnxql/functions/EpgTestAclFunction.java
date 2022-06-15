package fr.dila.solonepg.core.ufnxql.functions;

import static fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_DENY_READER;

import fr.dila.st.core.ufnxql.functions.SolonTestAclFunction;
import fr.sword.naiad.nuxeo.ufnxql.core.query.ufnxql.QueryBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Fonction pour tester les acls
 *
 * testAcl(id) = 1,
 *   test l'acces en lecture sur le document avec l'id id
 *
 * testAcl(id, 'READ') = 1,
 *
 *
 */
public class EpgTestAclFunction extends SolonTestAclFunction {

    public EpgTestAclFunction() {
        super();
    }

    @Override
    protected String getReadAclsCheckSqlForOracle(String idColumnName, String[] principals) {
        String questionMarks = String.join(",", Collections.nCopies(principals.length, "?"));
        String queryAclsFmt =
            "EXISTS (select /*+ NL_SJ */ a.acl_id FROM %s a WHERE a.acl_id = %s AND a.USERGROUP IN (%s))";
        String queryPositiveAcls = String.format(queryAclsFmt, "SW_ACLR_USER_ACLID", idColumnName, questionMarks);
        if (checkNegativeAcls(Arrays.asList(principals))) {
            String queryNegativeAcls = String.format(queryAclsFmt, "SW_ACLR_DUSER_ACLID", idColumnName, "?");
            return queryPositiveAcls + " AND NOT " + queryNegativeAcls;
        } else {
            return queryPositiveAcls;
        }
    }

    protected boolean checkNegativeAcls(List<String> principals) { // NOSONAR parameter used in override
        return false;
    }

    @Override
    protected void addWhereParams(final QueryBuilder queryBuilder, List<String> listPrincipals) {
        queryBuilder.getWhereParams().addAll(listPrincipals);
        if (checkNegativeAcls(listPrincipals)) {
            queryBuilder.getWhereParams().add(DOSSIER_MESURE_NOMINATIVE_DENY_READER);
        }
    }
}
