package fr.dila.solonepg.core.recherche.query;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.dila.st.core.jointure.CorrespondenceDescriptor;
import fr.dila.st.core.query.ufnxql.UFNXQLQueryAssembler;


public class EPGUFNXQLQueryAssembler extends UFNXQLQueryAssembler{

    private static final String TEST_ACL = "testAcl(d.ecm:uuid) = 1";

    /**
     * Initialise les correpondances sur le projet Réponses
     * 
     */
    public EPGUFNXQLQueryAssembler(){
        super();
    }
   
    public List<CorrespondenceDescriptor> getCorrespondences(){
        return correspondences;
    }
    
   /**
    * Retourne la requête complête de l'assembleur du service EPG.
    * 
    */
   @Override
   public String getFullQuery(){
       String where = " ";
       if (StringUtils.isBlank(getWhereClause())){
           where = " WHERE ";
       }
       String resultQuery = new String(getWhereClause());
       if (StringUtils.isBlank(getWhereClause()) && correspondences != null && correspondences.get(0) != null){
           return correspondences.get(0).getQueryPart() + where + TEST_ACL;
       }
       List<CorrespondenceDescriptor> usefulCorrespondences = get_useful_correspondences(getWhereClause());
       resultQuery = transformQuery(resultQuery,usefulCorrespondences);
       return resultQuery + " AND " + TEST_ACL;
    }

    @Override
    public String getAllResultsQuery() {
        return null;
    }

}
