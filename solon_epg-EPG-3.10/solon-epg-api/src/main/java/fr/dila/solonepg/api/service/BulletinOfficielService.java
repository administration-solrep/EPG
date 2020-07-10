package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

import fr.dila.solonepg.api.dto.VecteurPublicationDTO;
import fr.dila.solonepg.api.migration.MigrationLoggerModel;

public interface BulletinOfficielService extends Serializable {

	DocumentModelList getAllActiveBulletinOfficielOrdered(CoreSession session) throws ClientException;

	/**
	 * Creation d'un nouveau bulletin officiel
	 * 
	 * @param session
	 *            {@link CoreSession}
	 * @param nor
	 *            id du ministere ou NOR
	 * @param intitule
	 *            intitule du bo
	 * @throws ClientException
	 */
	void create(CoreSession session, String nor, String intitule) throws ClientException;

	void delete(CoreSession session, String identifier) throws ClientException;

	List<DocumentModel> getAllBulletinOfficielOrdered(DocumentModel currentDocument, CoreSession session)
			throws ClientException;

	/**
	 * Migre les bulletins officiels de l'écran "Liste des bulletins officiels".
	 * 
	 * @param session
	 * @param ancienNorMinistere
	 * @param nouveauNorMinistere
	 * @param migrationLoggerModel
	 * @throws ClientException
	 */
	void migrerBulletinOfficiel(CoreSession session, String ancienNorMinistere, String nouveauNorMinistere,
			MigrationLoggerModel migrationLoggerModel) throws ClientException;

	/**
	 * Renvoie la liste des bulletins officiels pour un nor ministère donné.
	 * 
	 * @param session
	 * @param ministereNor
	 * @return la liste des bulletins officiels pour un nor ministère donné.
	 * @throws ClientException
	 */
	List<DocumentModel> getBulletinOfficielQueryFromNor(CoreSession session, String ministereNor)
			throws ClientException;

	/**
	 * Permet de créer une ligne vide dans les vecteurs de publication (utilisation dans l'administration des vecteurs)
	 * 
	 * @param session
	 * @return Le Vecteur créé
	 * @throws ClientException
	 */
	DocumentModel initVecteurPublication(CoreSession session) throws ClientException;

	/**
	 * Permet de sauvegarder la liste des vecteurs de publication passés en paramètre, l'intégralité des vecteurs doit
	 * être initialisée au préalable
	 * 
	 * @param documentManager
	 * @param lstVecteurs
	 * @throws ClientException
	 */
	void saveVecteurPublication(CoreSession documentManager, List<VecteurPublicationDTO> lstVecteurs)
			throws ClientException;

	/**
	 * Retourne l'ensemble des vecteurs de l'application, qu'ils soient actifs ou non
	 * 
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	List<DocumentModel> getAllVecteurPublication(CoreSession session) throws ClientException;

	/**
	 * Retourne une liste de vecteurs de publication actifs
	 * 
	 * @param documentManager
	 * @return
	 * @throws ClientException
	 */
	List<DocumentModel> getAllActifVecteurPublication(CoreSession documentManager) throws ClientException;

	/**
	 * Retourne vrai si l'intégralité de la liste des vecteurs (représentés par leur UUID) passée en paramètre est
	 * active, faux sinon
	 * 
	 * @param documentManager
	 * @param name
	 *            : libellé du vecteur
	 * @return bolean
	 * @throws ClientException
	 */
	boolean isAllVecteurPublicationActif(CoreSession documentManager, List<String> idsVecteur) throws ClientException;

	/**
	 * Renvoie vrai si tous les bulletins officiels passés sont actifs.
	 * 
	 * @param documentManager
	 * @param names
	 * @return boolean
	 * @throws ClientException
	 */
	public boolean isAllBulletinOfficielActif(CoreSession documentManager, List<String> names) throws ClientException;

	/**
	 * Retourne le libellé pour le journal officiel (position 1)
	 * 
	 * @param documentManager
	 * @return
	 * @throws ClientException
	 */
	String getLibelleForJO(CoreSession documentManager) throws ClientException;

	/**
	 * Retourne l'identifiant de vecteur correspondant à un libellé passé en paramètre
	 * 
	 * @param documentManager
	 * @param libelle
	 * @return vide si l'élément n'a pas été trouvé, l'identifiant du premier document trouvé sinon
	 * @throws ClientException
	 */
	String getIdForLibelle(CoreSession documentManager, String libelle) throws ClientException;

	/**
	 * Tente de convertir les identifiants en libellés, si on ne trouve pas le vecteur, on rajoute son identifiant
	 * (c'est probablement un bulletinOfficiel)
	 * 
	 * @param documentManager
	 * @param idVecteurs
	 * @return la liste des libellés
	 */
	List<String> convertVecteurIdListToLabels(CoreSession documentManager, List<String> idVecteurs);

	/**
	 * Tente de convertir un identifiant en libellé, si on ne trouve pas le vecteur, on rajoute son identifiant (c'est
	 * probablement un bulletinOfficiel)
	 * 
	 * @param documentManager
	 * @param idVecteur
	 * @return le libellé
	 */
	String convertVecteurIdToLabel(CoreSession documentManager, String idVecteur);

}
