package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ui.web.directory.VocabularyEntry;

import fr.dila.solonepg.api.dto.tablereference.ModeParutionDTO;

/**
 * Service pour les tables de référence
 * 
 */
public interface TableReferenceService extends Serializable {

	DocumentModel getTableReference(CoreSession session) throws ClientException;

	/**
	 * Retourne l'identifiant du ministère PRM.
	 * 
	 * @param session
	 * @return l'identifiant du ministère PRM.
	 * @throws ClientException
	 */
	String getMinisterePrm(CoreSession session) throws ClientException;

	/**
	 * Récupère les identifiants et les nor (label) des directions du premier ministre qui sont peuvent être
	 * sélectionnées par tous les utilisateurs lors de la création du dossier ( RG-DOS-CRE-19 )
	 * 
	 * @param session
	 * @return les nor des directions du premier ministre qui sont peuvent être sélectionnées par tous les utilisateurs
	 *         lors de la création du dossier ( RG-DOS-CRE-19 )
	 */
	List<VocabularyEntry> getNorDirectionsForCreation(CoreSession session) throws ClientException;

	List<VocabularyEntry> getRetoursDAN(CoreSession session) throws ClientException;
	
	
	/**
	 * Récupère la liste des modes de parution existants en base de données
	 * 
	 * @param documentManager
	 * @return
	 * @throws ClientException
	 */
	List<DocumentModel> getModesParutionList(CoreSession documentManager) throws ClientException;

	/**
	 * Récupère la liste des modes de parution existants en base de données et qui sont actifs (dont la date de début
	 * est antérieure et la date de fin postérieure à la date du jour)
	 * 
	 * @param documentManager
	 * @return
	 * @throws ClientException
	 */
	List<DocumentModel> getModesParutionActifsList(CoreSession documentManager) throws ClientException;

	/**
	 * Créé un nouveau documentModel pour un mode de parution
	 * 
	 * @param session
	 * @param mode
	 * @throws ClientException
	 */
	void createModeParution(CoreSession session, ModeParutionDTO mode) throws ClientException;

	/**
	 * Met à jour un documentModel pour un mode de parution
	 * 
	 * @param session
	 * @param mode
	 * @throws ClientException
	 */
	void updateModeParution(CoreSession session, ModeParutionDTO mode) throws ClientException;

	/**
	 * Récupère la valeur du champ identifiant poste publication
	 * 
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	String getPostePublicationId(CoreSession session) throws ClientException;

	/**
	 * Récupère la valeur du champ identifiant poste DAN
	 * 
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	String getPosteDanId(CoreSession session) throws ClientException;
	
	/**
	 * Récupère la valeur du champ identifiant poste DAN
	 * 
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	String getPosteDanIdAvisRectificatifCE(CoreSession session) throws ClientException;

	/**
	 * Récupère les modes de parution qui ont pour nom celui passé en paramètres
	 * 
	 * @param session
	 * @param name
	 * @return
	 * @throws ClientException
	 */
	List<String> getModesParutionListFromName(CoreSession session, String name) throws ClientException;

}
