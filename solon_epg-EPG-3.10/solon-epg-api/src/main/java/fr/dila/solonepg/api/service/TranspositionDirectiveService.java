package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteTransposition;
import fr.dila.solonepg.api.cases.TranspositionDirective;
import fr.dila.solonepg.api.dto.TexteTranspositionDTO;

/**
 * Interface pour la gestion des transpositions de directive européenne
 * 
 * @author asatre
 * 
 */
public interface TranspositionDirectiveService extends Serializable {

	/**
	 * recherche une transposition de directive européenne, creation si inexistante
	 * 
	 * @param session
	 * @param numero
	 * @return
	 * @throws ClientException
	 */
	TranspositionDirective findOrCreateTranspositionDirective(CoreSession session, String numero)
			throws ClientException;

	/**
	 * mise a jour d'une transposition de directive européenne
	 * 
	 * @param session
	 * @param numero
	 * @return
	 * @throws ClientException
	 */
	TranspositionDirective updateTranspositionDirective(CoreSession session,
			TranspositionDirective transpositionDirective) throws ClientException;

	/**
	 * recherche des dossiers lié à la transposition de directive européenne
	 * 
	 * @param transpositionDirective
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	Set<String> findLinkedDossierNor(TranspositionDirective transpositionDirective, CoreSession session)
			throws ClientException;

	/**
	 * construction de la liste des numero de directive européennes
	 * 
	 * @param dossier
	 * @param session
	 * @throws ClientException
	 */
	void attachDirectiveEuropenneToDossier(Dossier dossier, CoreSession session) throws ClientException;

	/**
	 * Mise ajour d'une transposition de directive européenne avec verification que son numero est unique
	 * 
	 * @param session
	 * @param transpositionDirective
	 * @return
	 * @throws ClientException
	 */
	TranspositionDirective updateTranspositionDirectiveWithCheckUnique(CoreSession session,
			TranspositionDirective transpositionDirective) throws ClientException;

	/**
	 * retourne les {@link TexteTransposition} de la {@link TranspositionDirective}, creation des elements si non
	 * existant
	 * 
	 * @param transpositionDirective
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	List<TexteTransposition> fetchTexteTransposition(TranspositionDirective transpositionDirective, CoreSession session)
			throws ClientException;

	/**
	 * Sauvegarde de la liste de {@link TexteTransposition} d'une {@link TranspositionDirective}
	 * 
	 * @param transpositionDirective
	 * @param listTexteTranspositionDTO
	 * @param session
	 * @return
	 */
	TranspositionDirective saveTexteTranspositionDTO(TranspositionDirective transpositionDirective,
			List<TexteTranspositionDTO> listTexteTranspositionDTO, CoreSession session) throws ClientException;

	/**
	 * rechargement des {@link TexteTranspositionDTO} à partir des nor qu'ils contiennent
	 * 
	 * @param listTexteTranspositionDTO
	 * @param session
	 */
	void refreshTexteTransposition(List<TexteTranspositionDTO> listTexteTranspositionDTO, CoreSession session)
			throws ClientException;

	/**
	 * reacharge completement une transposition
	 * 
	 * @param adapter
	 * @param session
	 * @return
	 */
	TranspositionDirective reloadTransposition(TranspositionDirective adapter, CoreSession session)
			throws ClientException;;

	/**
	 * Recherche par webService EURLEX si une directive existe
	 * 
	 * @return
	 */
	String findDirectiveEurlexWS(String reference, String annee, String titre);

}
