package fr.dila.solonepg.rest.api;

import fr.sword.wsdl.solon.eurlex.SearchRequest;
import fr.sword.wsdl.solon.eurlex.SearchResults;

/**
 * Interface permettant de gerer toutes les operations sur Eurlex
 */
public interface EurlexDelegate {

	/**
	 * service EURLexWebService :
	 * 
	 * Permet de rechercher existence d'une transposition d'une directive
	 * 
	 * @param request
	 * @return Response
	 * @throws Exception
	 */
	SearchResults rechercherExistenceTranspositionDirective(SearchRequest request) throws Exception;

}
