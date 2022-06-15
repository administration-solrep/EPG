package fr.dila.solonepg.core.recherche.query;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.st.core.jointure.CorrespondenceDescriptor;
import fr.dila.st.core.query.ufnxql.UFNXQLQueryAssembler;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class ActiviteNormativeQueryAssembler extends UFNXQLQueryAssembler {
    public static final String PREFIX_DOC_ACTIVITE_NORMATIVE = "d.";
    private ActiviteNormativeEnum anEnum;

    /**
     * Initialise les correspondances sur le projet EPG
     *
     */
    public ActiviteNormativeQueryAssembler() {
        super();
    }

    public List<CorrespondenceDescriptor> getCorrespondences() {
        return correspondences;
    }

    /**
     * Retourne la requête complète de l'assembleur du service EPG.
     *
     */
    @Override
    public String getFullQuery() {
        String where = StringUtils.EMPTY;
        String extraConstraint = getTypeConstraint();
        String resultQuery = new String(getWhereClause());
        if (StringUtils.isBlank(getWhereClause()) && StringUtils.isNotBlank(extraConstraint)) {
            where = " WHERE ";
        }
        if (StringUtils.isBlank(getWhereClause()) && correspondences != null && correspondences.get(0) != null) {
            return correspondences.get(0).getQueryPart() + where + extraConstraint;
        }
        List<CorrespondenceDescriptor> usefulCorrespondences = get_useful_correspondences(getWhereClause());
        resultQuery = transformQuery(resultQuery, usefulCorrespondences);
        return resultQuery + " AND " + extraConstraint;
    }

    @Override
    public String getAllResultsQuery() {
        return null;
    }

    public void setActiviteNormativeEnum(ActiviteNormativeEnum anEnum) {
        this.anEnum = anEnum;
    }

    public String getTypeConstraint() {
        if (anEnum == null) {
            return StringUtils.EMPTY;
        } else {
            StringBuilder typeConstraintBuilder = new StringBuilder();
            typeConstraintBuilder
                .append(PREFIX_DOC_ACTIVITE_NORMATIVE)
                .append(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA_PREFIX)
                .append(":")
                .append(anEnum.getAttributSchema())
                .append(" = '1'");
            return typeConstraintBuilder.toString();
        }
    }
}
