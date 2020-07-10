package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

import fr.dila.solonepg.api.administration.IndexationMotCle;
import fr.dila.solonepg.api.administration.IndexationRubrique;
import fr.dila.solonepg.api.migration.MigrationLoggerModel;
import fr.dila.st.api.organigramme.EntiteNode;

public interface IndexationEpgService extends Serializable {

	DocumentModelList findAllIndexationRubrique(CoreSession session) throws ClientException;

	DocumentModelList findAllIndexationMotCle(CoreSession session) throws ClientException;

	void createIndexationRubrique(CoreSession session, String intitule) throws ClientException;

	DocumentModel createIndexationMotCle(CoreSession session, String intitule, List<String> ministereIds)
			throws ClientException;

	void updateIndexationMotCle(CoreSession session, IndexationMotCle indexationMotCle) throws ClientException;

	void updateIndexationRubrique(CoreSession session, IndexationRubrique indexationRubrique) throws ClientException;

	void deleteIndexationMotCle(CoreSession session, IndexationMotCle indexationMotCle) throws ClientException;

	void deleteIndexationRubrique(CoreSession session, IndexationRubrique indexationRubrique) throws ClientException;

	List<EntiteNode> findAllMinistereForIndexation(CoreSession session) throws ClientException;

	List<String> findAllIndexationMotCleForDossier(CoreSession session, DocumentModel dossierDoc, String indexation)
			throws ClientException;

	List<String> findAllIndexationRubrique(CoreSession session, String indexation) throws ClientException;

	void updateIndexationMotCleAfterChange(CoreSession session, IndexationMotCle indexationMotCle)
			throws ClientException;

	/**
	 * Migre les mots clés de l'écran "Gestion de l'indexation".
	 * 
	 * @param session
	 * @param ancienMinistereId
	 * @param nouveauMinistereId
	 * @param migrationLoggerModel
	 * @throws ClientException
	 */
	void migrerGestionIndexation(CoreSession session, String ancienMinistereId, String nouveauMinistereId,
			MigrationLoggerModel migrationLoggerModel) throws ClientException;

	/**
	 * Récupère les mots clés de l'indexation lié au ministère ayant l'id <b>idMinistere</b>
	 * 
	 * @param session
	 * @param idMinistere
	 * @return
	 * @throws ClientException
	 */
	List<DocumentModel> getIndexationMotCleQueryFromIdMinistere(CoreSession session, String idMinistere)
			throws ClientException;
}
