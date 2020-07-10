package fr.dila.solonmgpp.api.service;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.sword.xsd.solon.epp.RechercherEvenementResponse;

/**
 * Interface du service de gestion de la recherche dans SOLON MGPP
 * 
 * @author asatre
 * 
 */
public interface RechercheService {

	/**
	 * Initialise les restrictions par rapport au menu courant
	 * 
	 * @param critereRechercheDTO
	 * @return
	 */
	CritereRechercheDTO initCritereRechercheDTO(CritereRechercheDTO critereRechercheDTO);

	/**
	 * Recherche de message dans EPP par critere de recherche
	 * 
	 * @param critereRechercheDTO
	 * @return
	 * @throws ClientException
	 */
	List<MessageDTO> findMessage(CritereRechercheDTO critereRechercheDTO, CoreSession session) throws ClientException;

	/**
	 * Recherche de {@link Dossier}
	 * 
	 * @param coreSession
	 * @param critereRechercheDTO
	 * @throws ClientException
	 * @return
	 */
	List<DocumentModel> findDossier(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO)
			throws ClientException;

	/**
	 * Recherche {@link FichePresentationOEP}
	 * 
	 * @param coreSession
	 * @param critereRecherche
	 * @return
	 */
	List<DocumentModel> findOEP(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO)
			throws ClientException;

	/**
	 * Recherche {@link FichePresentationAVI}
	 * 
	 * @param coreSession
	 * @param critereRecherche
	 * @return
	 */
	List<DocumentModel> findAVI(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO)
			throws ClientException;

	/**
	 * Recherche {@link FichePresentationDOC}
	 * 
	 * @param coreSession
	 * @param critereRecherche
	 * @return
	 */
	List<DocumentModel> findDOC(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO)
			throws ClientException;

	List<DocumentModel> findAUD(final CoreSession coreSession, final CritereRechercheDTO critereRechercheDTO)
			throws ClientException;

	RechercherEvenementResponse findMessageWS(CritereRechercheDTO critereRechercheDTO, CoreSession session)
			throws ClientException;

	List<DocumentModel> findDPG(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO)
			throws ClientException;

	List<DocumentModel> findSD(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO) throws ClientException;

	List<DocumentModel> findJSS(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO)
			throws ClientException;

	List<DocumentModel> findDR(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO) throws ClientException;

	List<DocumentModel> findDecret(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO)
			throws ClientException;

}
