package fr.dila.solonepg.core.recherche.query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.search.api.client.querymodel.QueryModelService;
import org.nuxeo.ecm.core.search.api.client.querymodel.descriptor.QueryModelDescriptor;

import fr.dila.solonepg.api.service.RequeteurDossierSimpleService;

/**
 * 
 * TODO : Reprise de Réponses, cette classe doit être remplacée ...
 * Classe utilitaire pour donner la clause WHERE d'un model nuxeo.
 * 
 * @author jgomez
 *
 */
@SuppressWarnings("deprecation")
public class EPGQueryModel {
    
    /**
     * Retourne la clause d'un model Nuxeo.
     * @param qmService
     * @param model
     * @param requeteModelName
     * @return
     * @throws ClientException
     */
    public String getRequetePart(QueryModelService qmService, DocumentModel model,String requeteModelName) throws ClientException {
        QueryModelDescriptor qmDesc = qmService.getQueryModelDescriptor(requeteModelName);
        if (qmDesc == null){
            return StringUtils.EMPTY;
        }
        String query = qmDesc.getQuery(model);

		// Analyser la partie de la requête concernant le numéro NOR
		Pattern norPattern = Pattern.compile("d.dos:numeroNor LIKE '[^']*'");
		Matcher norMatcher = norPattern.matcher(query);
		if (norMatcher.find() && query.contains(";")) {
			String norQueryOld = query.substring(norMatcher.start(), norMatcher.end());
			String norPart = norQueryOld.split(" ", 3)[2];
			String[] nors = norPart.substring(1, norPart.length() - 1).split(";");
			String norQueryNew = "";
			for (String nor : nors) {
				if (!nor.trim().isEmpty())
					norQueryNew += "d.dos:numeroNor LIKE '" + nor.trim() + "' OR ";
			}
			if (!norQueryNew.isEmpty()) {
				norQueryNew = "(" + norQueryNew.substring(0, norQueryNew.length() - 4) + ")";
				query = query.replace(norQueryOld, norQueryNew);
			}
		}

        StringBuilder modifiedQuery = new StringBuilder(query.replace("SELECT * FROM Document" , StringUtils.EMPTY).replace(" WHERE ",StringUtils.EMPTY)) ;
        
        // Bug nuxeo sur les subclause.
        if (RequeteurDossierSimpleService.REQUETE_TEXTE_INTEGRAL.equals(requeteModelName) && modifiedQuery.length() != 0){
            modifiedQuery.insert(0, "(") ;
            modifiedQuery.append(")") ;            
        }
        return modifiedQuery.toString();
        
    }

    /**
     * Retourne les clauses de tous les models donnés en argument, liées par un AND (et avec des parenthèses).
     * @param qmService
     * @param model
     * @param modelNames
     * @return
     * @throws ClientException
     */
    public String getAndRequeteParts(QueryModelService qmService, DocumentModel model,String... modelNames) throws ClientException {
        List<String> clauses = new ArrayList<String>();
        for (String modelName : modelNames) {
            String requetePart = getRequetePart(qmService,model,modelName);
            if (!StringUtils.isBlank(requetePart)){
                clauses.add( "(" + requetePart + ")");
            }
        }
        return StringUtils.join(clauses, " AND ");
    }

}
