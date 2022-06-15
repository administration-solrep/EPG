package fr.dila.solonmgpp.core.query;

import fr.dila.st.core.jointure.CorrespondenceDescriptor;
import fr.dila.st.core.query.ufnxql.UFNXQLQueryAssembler;
import java.util.List;

public class MGPPUFNXQLQueryAssembler extends UFNXQLQueryAssembler {

    /**
     * Initialise les correpondances sur le projet Réponses
     *
     */
    public MGPPUFNXQLQueryAssembler() {
        super();
    }

    public List<CorrespondenceDescriptor> getCorrespondences() {
        return correspondences;
    }

    /**
     * Retourne la requête complête de l'assembleur du service EPG.
     *
     */
    @Override
    public String getFullQuery() {
        String resultQuery = new String(getWhereClause());
        List<CorrespondenceDescriptor> useful_correspondences = get_useful_correspondences(getWhereClause());
        resultQuery = transformQuery(resultQuery, useful_correspondences);
        return resultQuery;
    }

    @Override
    public String getAllResultsQuery() {
        return null;
    }
}
