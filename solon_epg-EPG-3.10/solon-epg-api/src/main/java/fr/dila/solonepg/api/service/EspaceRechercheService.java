package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.criteria.FeuilleRouteCriteria;

/**
 * Service permettant de gérer les favoris de recherche, les favoris de consultation et les
 * derniers résultats consultés. 
 * 
 * @author asatre
 * @author jtremeaux
 */
public interface EspaceRechercheService extends Serializable {
	
	enum DocKind {
		FEUILLE_ROUTE,
		DOSSIER,
		USER
	};

    /**
     * Crée un nouveau document favori de recherche (uniquement en mémoire).
     * 
     * @param session Session
     * @param type Type de favori de recherche
     * @return Nouveau document favori de recherche
     * @throws ClientException
     */
    DocumentModel createBareFavorisRecherche(CoreSession session, String type) throws ClientException;

    /**
     * Ajoute un document à la liste des derniers résultats consultés.
     * Chaque document est ajouté une seule fois à la liste, et la liste contient au plus un nombre
     * d'éléments paramétrables.
     * 
     * @param session Session
     * @param userworkspacePath Workspace de l'utilisateur
     * @param docId id du document à ajouter aux favoris
     * @param kind type de resultat
     * @throws ClientException
     */
    void addDocumentModelToResultatsConsultes(CoreSession session, String userworkspacePath, String docId, DocKind kind) throws ClientException;
	
    /**
     * Ajoute une liste de documents aux favoris de consultation.
     * 
     * @param session Session
     * @param userworkspacePath Workspace de l'utilisateur
     * @param documentModelList Liste de documents à ajouter aux favoris
     * @return Nombre de documents ajoutés aux favoris de consultation
     * @throws ClientException
     */
	int addToFavorisConsultation(CoreSession session, String userworkspacePath, List<DocumentModel> documentModelList) throws ClientException;

	/**
	 * Reture une liste de documents des favoris de consultation.
	 * 
	 * @param session Session
	 * @param userworkspacePath Workspace de l'utilisateur
	 * @param docsList Liste de documents à retirer des favoris
	 * @throws ClientException
	 */
	void removeFromFavorisConsultation(CoreSession session, String userworkspacePath, List<DocumentModel> docsList) throws ClientException;
	
	List<DocumentModel> addToFavorisRecherche(CoreSession session, List<String> postes, String intitule, String query, String type) throws ClientException;

	/**
	 * Crée un favori de recherche dans les espaces de travail personnels de tous les
	 * utilisateurs de tous les postes fournis en paramètre.
	 * 
	 * @param session Session
	 * @param posteList Postes de diffusion
	 * @param favorisRechercheDoc Document du favori de recherche à créer
	 * @return Liste des utilisateurs en erreur
	 * @throws ClientException
	 */
    List<DocumentModel> addToFavorisRecherche(CoreSession session, final List<String> posteList, final DocumentModel favorisRechercheDoc) throws ClientException;

    void removeDossierFromFavorisConsultation(CoreSession session, String userworkspacePath) throws ClientException;

    void removeDossierFromDerniersResultatsConsultes(CoreSession session, String userworkspacePath) throws ClientException;

    void removeUserFromDerniersResultatsConsultes(CoreSession coreSession, String userworkspacePath, Set<String> userToRemove) throws ClientException;

    void removeUserFromFavorisConsultations(CoreSession coreSession, String userworkspacePath, Set<String> userToRemove) throws ClientException;
	
    /**
     * Retourne les critères de recherche correspondant au favori de recherche de feuille de route.
     * 
     * @return Critères de recherche de feuille de route
     */
    FeuilleRouteCriteria getFeuilleRouteCriteria(DocumentModel favorisRechercheDoc) throws ClientException;

	/**
	 * Renvoie true si l'utilisateur peut utiliser la recherche libre, false
	 * sinon.
	 * 
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	boolean canUseRechercheLibre(CoreSession session) throws ClientException;
}
