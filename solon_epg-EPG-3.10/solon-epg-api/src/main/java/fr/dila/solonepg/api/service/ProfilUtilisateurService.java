package fr.dila.solonepg.api.service;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ui.web.directory.VocabularyEntry;

import fr.dila.st.api.feuilleroute.STRouteStep;
import fr.dila.st.api.service.STProfilUtilisateurService;

/**
 * Service qui permet de gérer les profils utilisateurs.
 * 
 * @author arolin
 */
public interface ProfilUtilisateurService extends STProfilUtilisateurService {

	// /////////////////
	// vérifications de l'envoi de mail de notification
	// ////////////////

	/**
	 * Renvoient les notifications mails aux utilisateurs lors de la validation d'une étape d'un dossier.
	 * 
	 * @param routeStep
	 *            etape courante du dossier
	 * @param dossierDoc
	 *            documentModel du dossier
	 * @param session
	 * @throws ClientException
	 */
	void checkAndSendMailNotificationValidationEtape(STRouteStep routeStep, DocumentModel dossierDoc,
			CoreSession session) throws ClientException;

	// /////////////////
	// méthode sur l'affichage des colonnes
	// ////////////////

	/**
	 * Retourne la liste des colonnes autorisés pour une liste de dossier
	 * 
	 * @param session
	 * @return liste des colonnes autorisées pour une liste de dossier
	 * @throws ClientException
	 */
	List<String> getAllowedDossierColumn(CoreSession session) throws ClientException;

	/**
	 * Retourne la liste des colonnes autorisées pour les instances de feuille de route
	 * 
	 * @param session
	 * @return liste des colonne autorisés pour les isntance s de feuille de route
	 * @throws ClientException
	 */
	List<String> getAllowedFeuilleRouteInstanceColumn(CoreSession session) throws ClientException;

	/**
	 * Retourn la liste des colonnes affichées dans l'espace de traitement de l'utilisateur
	 * 
	 * @param session
	 * @return liste des colonnes affichées dans l'espace de traitement de l'utilisateur
	 * @throws ClientException
	 */
	List<String> getUserEspaceTraitementColumn(CoreSession session) throws ClientException;

	/**
	 * Retourne la liste des colonnes affichées dans les instances de feuille de route de l'utilisateur
	 * 
	 * @param session
	 * @return liste des colonne affichées dans les instances de feuille de route de l'utilisateur
	 * @throws ClientException
	 */
	List<String> getUserFeuilleRouteInstanceColumn(CoreSession session) throws ClientException;

	/**
	 * Retourne la liste des colonnes autorisés pour l'espace de traitement
	 * 
	 * @param session
	 * @return liste des colonne autorisés pour l'espace de traitement sous forme de liste de VocabularyEntry
	 * @throws ClientException
	 */
	List<VocabularyEntry> getVocEntryAllowedEspaceTraitementColumn(CoreSession session) throws ClientException;

	/**
	 * Retourne la liste des colonnes autorisés pour les instances de feuille de route
	 * 
	 * @param session
	 * @return liste des colonne autorisés pour les instances de feuille de route sous forme de liste de VocabularyEntry
	 * @throws ClientException
	 */
	List<VocabularyEntry> getVocEntryAllowedFeuilleRouteInstanceColumn(CoreSession session) throws ClientException;

	/**
	 * Retourn la liste des colonnes affichées dans l'espace de traitement de l'utilisateur
	 * 
	 * @param session
	 * @return liste des colonnes affichées dans l'espace de traitement de l'utilisateur sous forme de liste de
	 *         VocabularyEntry
	 * @throws ClientException
	 */
	List<VocabularyEntry> getVocEntryUserEspaceTraitementColumn(CoreSession session) throws ClientException;

	/**
	 * Retourne la liste des colonnes affichées dans les instances de feuille de route de l'utilisateur
	 * 
	 * @param session
	 * @return liste des colonne affichées dans les instances de feuille de route de l'utilisateur sous forme de liste
	 *         de VocabularyEntry
	 * @throws ClientException
	 */
	List<VocabularyEntry> getVocEntryUserFeuilleRouteInstanceColumn(CoreSession session) throws ClientException;

	/**
	 * Retourne le nombre de dossier à afficher dans les corbeilles de l'utilisateur
	 * 
	 */
	Long getUtilisateurPageSize(CoreSession session) throws ClientException;

	/**
	 * Instancie la liste par colonnes par défaut de l'espace de traitement et des instances de feuille de route du
	 * document profilUtilisateur.
	 * 
	 * @param profilUtilisateur
	 *            docModel de type ProfilUtilisateur
	 * @return
	 */
	DocumentModel initDefaultAvailablesColumnsNames(DocumentModel profilUtilisateur, CoreSession session)
			throws ClientException;

	/**
	 * Initialise les valeurs par défaut du profil utilisateur document profilUtilisateur.
	 * 
	 * @param profilUtilisateur
	 *            docModel de type ProfilUtilisateur
	 * @return
	 */
	DocumentModel initDefaultvalues(DocumentModel profilUtilisateur, CoreSession session) throws ClientException;

	/**
	 * Ajout d'un dossier à la liste des cinq derniers dossiers sur lesquels l'utilisateur est intervenu
	 * 
	 * @param session
	 * @param idDossier
	 */
	void addDossierToListDerniersDossierIntervention(CoreSession session, String idDossier);

}
