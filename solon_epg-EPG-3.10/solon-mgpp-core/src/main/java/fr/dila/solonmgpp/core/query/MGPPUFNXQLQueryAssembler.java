package fr.dila.solonmgpp.core.query;

import java.util.List;

import fr.dila.st.core.jointure.CorrespondenceDescriptor;
import fr.dila.st.core.query.ufnxql.UFNXQLQueryAssembler;


public class MGPPUFNXQLQueryAssembler extends UFNXQLQueryAssembler{


    /**
     * Initialise les correpondances sur le projet Réponses
     * 
     */
    public MGPPUFNXQLQueryAssembler(){
        super();
    }
   
    public List<CorrespondenceDescriptor> getCorrespondences(){
        return correspondences;
    }
    
   /**
    * Retourne la requête complête de l'assembleur du service EPG.
    * 
    */
   //TODO JGZ: Ne mettre que ce qui est variable dans cette classe.
   @Override
   public String getFullQuery(){
       String resultQuery = new String(getWhereClause());
       List<CorrespondenceDescriptor> useful_correspondences = get_useful_correspondences(getWhereClause());
       resultQuery = transformQuery(resultQuery,useful_correspondences);
       return resultQuery;
    }

    @Override
    public String getAllResultsQuery() {
        return null;
    }
}
