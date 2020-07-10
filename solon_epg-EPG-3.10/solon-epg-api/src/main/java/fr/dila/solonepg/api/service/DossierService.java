package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Dossier;

/**
 * Service permettant de gérer le cycle de vie du dossier SOLON EPG.
 * 
 * @author jtremeaux
 */
public interface DossierService extends Serializable {

	/**
	 * Crée un dossier Solon EPG, les documents parapheur + fond de dossier associés, ainsi que l'objet Case associé au
	 * dossier. Lève un évènement pour démarrer la feuille de route par défaut.
	 * 
	 * @param session
	 *            Session
	 * @param dossier
	 *            Dossier
	 * @param posteId
	 *            Identifiant technique du poste de la première étape de feuille de route
	 * @param norDossierCopieFdr
	 *            Numero Nor du dossier pour copier la feuille de route (facultatif)
	 * @parma userMailBox MailBox
	 * @return Dossier créé
	 * @throws ClientException
	 */
	Dossier createDossier(CoreSession session, DocumentModel dossierDoc, String posteId, Mailbox userMailBox, String norDossierCopieFdr)
			throws ClientException;

	// TODO arn voir si plus pertinent de merger les 2 methodes
	/**
	 * Crée un dossier Solon EPG pour les webservices, les documents parapheur + fond de dossier associés, ainsi que
	 * l'objet Case associé au dossier. Methode destinée au webservice car l'utilisateur webservices peut créer un
	 * dossier dans n'importe quel ministère. Initialise les droits du dossier Lève un évènement pour démarrer la
	 * feuille de route par défaut.
	 * 
	 * @param session
	 *            Session
	 * @param dossier
	 *            Dossier
	 * @param posteId
	 *            Identifiant technique du poste de la première étape de feuille de route
	 * @parma userMailBox MailBox
	 * @return Dossier créé
	 * @throws ClientException
	 */
	Dossier createDossierWs(CoreSession session, DocumentModel dossierDoc, String posteId, Mailbox userMailBox)
			throws ClientException;

	/**
	 * Crée un dossier Solon EPG, et les documents parapheur + fond de dossier associés. Lève un évènement pour démarrer
	 * la feuille de route par défaut.
	 * 
	 * @param session
	 *            Session
	 * @param dossier
	 *            Dossier
	 * @param posteId
	 *            Identifiant technique du poste de la première étape de feuille de route
	 * @param norDossierCopieFdr
	 *            Numero Nor du dossier pour copier la feuille de route (facultatif)
	 * @return Dossier créé
	 * @throws ClientException
	 */
	Dossier createDossier(CoreSession session, Dossier dossier, String posteId, String norDossierCopieFdr) throws ClientException;

	/**
	 * Initialise les ACL du dossier lors de sa création en Unrestricted.
	 * 
	 * @param session
	 *            Session
	 * @param dossierDocList
	 *            liste de Dossier
	 */
	void initDossierCreationAclUnrestricted(CoreSession session, final List<Dossier> dossierDocList)
			throws ClientException;

	/**
	 * Initialise les ACL indexation du dossier lors du changement de cycle de vie dans une session Unrestricted.
	 * 
	 * @param session
	 *            Session
	 * @param dossiersList
	 *            Dossier
	 */
	void initDossierIndexationAclUnrestricted(CoreSession session, List<Dossier> dossiersList) throws ClientException;

	/**
	 * Renvoie vrai si le dossier est reattribuable.
	 * 
	 * @param dossier
	 *            Dossier
	 * @return vrai si le dossier est reattribuable.
	 * @throws ClientException
	 */
	Boolean isReattribuable(Dossier dossier);

	/**
	 * Renvoie vrai si le dossier est transferable.
	 * 
	 * @param dossier
	 *            Dossier
	 * @return vrai si le dossier est transferable.
	 * @throws ClientException
	 */
	Boolean isTransferable(Dossier dossier);

	/**
	 * Transfert de dossiers clos à un autre ministere.
	 * 
	 * @param docList
	 *            liste des dossiers à transferer
	 * @param ministereAttache
	 *            id du ministere
	 * @param directionAttachee
	 *            id de la direction
	 * @param documentManager
	 *            {@link CoreSession}
	 * @throws ClientException
	 */
	void transfertDossiersUnrestricted(List<DocumentModel> docList, String ministereAttache, String directionAttachee,
			CoreSession session) throws ClientException;

	/**
	 * Met à jour le Ministère et la Direction de rattachement du dossier, ainsi que les droits du dossier.
	 * 
	 * @param ministere
	 * @param direction
	 * @param ministereOnly
	 * @param dossier
	 * @param session
	 * @throws ClientException
	 */
	void updateDossierWhenTransfert(String ministere, String direction, boolean ministereOnly, Dossier dossier,
			CoreSession session) throws ClientException;

	/**
	 * Reattribution de dossiers à un autre ministere.
	 * 
	 * @param docList
	 *            liste des dossiers à transferer
	 * @param ministere
	 *            id du ministere
	 * @param direction
	 *            id de la direction
	 * @param documentManager
	 *            {@link CoreSession}
	 * @throws ClientException
	 */
	void reattribuerDossiersUnrestricted(List<DocumentModel> docList, String ministere, String directio,
			CoreSession session) throws ClientException;

	/**
	 * Met à jour le Ministère et la Direction de rattachement du dossier, son nor, son titre ainsi que les droits du
	 * dossier. Met à jour uniquement le ministère si
	 * <p>
	 * ministereOnly
	 * </p>
	 * est vrai
	 * 
	 * @param ministere
	 * @param direction
	 * @param ministereOnly
	 * @param norMinistere
	 * @param norDirection
	 * @param norService
	 * @param dossier
	 * @throws ClientException
	 */
	public void updateDossierWhenReattribution(final String ministere, final String direction,
			final boolean ministereOnly, boolean reattributionNor, final String norMinistere,
			final String norDirection, final NORService norService, final Dossier dossier, final CoreSession session)
			throws ClientException;

	/**
	 * @param updatePubConjointes
	 *            Si true, les publications conjointes sont mises à jour directement (cas de la réattribution). Si false
	 *            (cas de la migration), elles seront mises à jour dans un second temps, lorsque tous les dossiers
	 *            auront été migrés.
	 * @return true si le dossier a des publications conjointes.
	 */
	boolean updateDossierWhenReattribution(String ministere, String direction, boolean ministereOnly,
			boolean reattributionNor, String norMinistere, String norDirection, NORService norService, Dossier dossier,
			CoreSession session, boolean checkNorUnicity, ActiviteNormative activiteNormative) throws ClientException;

	/**
	 * @param doUpdate
	 *            true si on doit réellement réaliser l'update, false si la méthode ne sert qu'à détecteur.
	 * @return
	 * @throws ClientException
	 */
	boolean checkDossierPublicationsConjointesWhenReattribution(Dossier dossier, final CoreSession session)
			throws ClientException;

	void updateDossierWhenReattributionJetonCE(CoreSession session, List<DocumentModel> dossierToReattribuer)
			throws ClientException;

	/**
	 * Marque un dossier comme candidat à la suppression.
	 * 
	 * @param session
	 * @param dossierDoc
	 * @throws Exception
	 */
	void candidateDossierToSuppression(CoreSession session, final DocumentModel dossierDoc) throws Exception;

	/**
	 * Abandonner un dossier.
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            le dossier a abndonner
	 * @throws ClientException
	 */
	void abandonDossier(CoreSession session, DocumentModel dossierDoc) throws ClientException;

	/**
	 * Retire le dossier de la liste de suppression.
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            le document a retirer
	 * @throws ClientException
	 */
	void retirerDossierFromSuppressionListUnrestricted(CoreSession session, DocumentModel dossierDoc)
			throws ClientException;

	/**
	 * Valider la transmission du dossier vers la liste de supression.
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            le document a transmettre
	 * @throws ClientException
	 */
	void validerTransmissionToSuppressionListUnrestricted(CoreSession session, DocumentModel dossierDoc)
			throws ClientException;

	/**
	 * Retirer le dossier de la liste d'abandon des dossier.
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            le dossier a retirer
	 * @throws ClientException
	 */
	void retirerAbandonDossier(CoreSession session, DocumentModel dossierDoc) throws ClientException;

	void restartAbandonDossier(CoreSession session, final DocumentModel dossierDoc) throws ClientException;

	/**
	 * Retire le dossier de la base d'archivage intermediaire et le maintenir dans la base d eprodcution pour une
	 * determinee saisi
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            le dossier a retirer
	 * @param dureeManitienDossierEnProduction
	 *            duree du Manitien du Dossier dans la base Production
	 * @throws ClientException
	 */
	void retirerDossierFromListCandidatsArchivageIntermediaireUnrestricted(CoreSession session,
			DocumentModel dossierDoc, int dureeManitienDossierEnProduction) throws ClientException;

	/**
	 * Verse le dossier dans la base d'archivage intermediaire.
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            le dossier a verser
	 * @throws ClientException
	 */
	void verserDossierDansBaseArchivageIntermediaireUnrestricted(CoreSession session, DocumentModel dossierDoc)
			throws ClientException;

	/**
	 * Retire le caractère mesure nominative du dossier.
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            Dossier
	 * @throws ClientException
	 */
	void annulerMesureNominativeUnrestricted(CoreSession session, DocumentModel dossierDoc) throws ClientException;

	/**
	 * Ajoute le caractère mesure nominative du dossier.
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            Dossier
	 * @throws ClientException
	 */
	void ajouterMesureNominativeUnrestricted(CoreSession session, DocumentModel dossierDoc) throws ClientException;

	void abandonDossierPourReprise(CoreSession session, DocumentModel dossierDoc) throws ClientException;

	/**
	 * Met à jour le dossier suite à un changement de type d'acte(UC-SOL-DOS-26)
	 * 
	 * @param dossier
	 *            dossier courant à modifier
	 * @param newTypeActe
	 *            nouveeu type d'acte
	 * @param session
	 *            session
	 * @return vrai si le type d'acte a changé
	 */
	Boolean updateDossierWhenTypeActeUpdated(DocumentModel dossier, CoreSession session) throws ClientException;

	/**
	 * Publie le dossier.
	 * 
	 * @param dossier
	 *            dossier courant à modifier
	 * @param newTypeActe
	 *            nouveeu type d'acte
	 * @param session
	 *            session
	 * @return vrai si le type d'acte a changé
	 */
	void publierDossier(Dossier dossier, CoreSession session) throws ClientException;

	/**
	 * Retourne la liste des dossiers ayant pour ministère <b>ministereId</b> et pour direction <b>directionId</b> si
	 * <b>hasDirection</b> est vrai.
	 * 
	 * @param session
	 * @param ministereId
	 * @param directionId
	 * @param hasDirection
	 * @return la liste des dossiers ayant pour ministère <b>ministereId</b> et pour direction <b>directionId</b> si
	 *         <b>hasDirection</b> est vrai.
	 * @throws ClientException
	 */
	List<DocumentModel> getDossierRattacheToMinistereAndDirection(CoreSession session, String ministereId,
			String directionId, boolean hasDirection) throws ClientException;

	/**
	 * recherche de dossier par idDossier (ie rattachement avec le MGPP)
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	Dossier findDossierFromIdDossier(CoreSession session, String idDossier) throws ClientException;

	/**
	 * recherche des dossiers par id des Dossier (ie rattachement avec le MGPP)
	 * 
	 * @param session
	 * @param ids
	 * @return
	 * @throws ClientException
	 */
	List<DocumentModel> findDossiersFromIdsDossier(CoreSession session, List<String> ids) throws ClientException;

	/**
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	DocumentModel findFicheLoiDocumentFromMGPP(CoreSession session, String idDossier) throws ClientException;

	/**
	 * Clos un dossier : supprime les étapes à venir, valide les étapes en cours, passe le statut à clos
	 * 
	 * @param session
	 * @param dossier
	 * @throws ClientException
	 */
	void cloreDossierUnrestricted(CoreSession session, Dossier dossier) throws ClientException;

	/**
	 * Met à jour les publications conjointes d'un dossier lors de son changement de nor de oldNor vers newNor,
	 * potentiellement dans le cadre d'un batch (migration de gouvernement)
	 * 
	 * @param dossier
	 *            Dossier
	 * @param session
	 *            CoreSession Nuxeo
	 * @param oldNor
	 *            ancien nor du dossier
	 * @param newNor
	 *            nouveau nor du dossier
	 * @param norReattributionsPubConj
	 *            Lot de migrations de Nor (migration de gouvernement), null si reattribution simple.
	 */
	void updatePublicationsConjointes(Dossier dossier, CoreSession session, String oldNor, String newNor,
			Map<String, String> norReattributionsPubConj) throws ClientException;

	/**
	 * Utilisé lors de la suppression d'un dossier inscrit le nor et l'id dans la table id_nor_dossiers_supprimes
	 * 
	 * @param nor
	 * @param id
	 * @return
	 */
	void saveNorIdDossierSupprime(String nor, String id) throws ClientException;

	/**
	 * Va chercher le nor du dossier dans la table id_nor_dossiers_supprimes grace à son id
	 */
	String getNorDossierSupprime(String id) throws ClientException;

	/**
	 * Va chercher les ids des dossiers dans la table id_nor_dossiers_supprimes grace à leur nor
	 */
	List<String> getIdsDossiersSupprimes(String nor) throws ClientException;
	
	/**
	 * Récupère la liste des dossiers rattachés à un lot d'extraction de
	 * dossiers (ADAMANT), dans la limite d'un nombre maximal fourni en
	 * paramètre (s'il est nul on retourne la liste complète).
	 * 
	 * @param idExtractionLot
	 *            id technique du lot
	 * @param limit
	 *            nombre maximal de dossiers à retourner.
	 * @return une liste de DocumentModel
	 * @throws ClientException
	 */
	List<DocumentModel> findDossiersFromIdExtractionLot(CoreSession session, Integer idExtractionLot, Integer limit) throws ClientException;
	
	/**
	 * Retourne true si le dossier est issue de la reprise de solon v1
	 * 
	 * @param dossier
	 */
	boolean isDossierReprise(Dossier dossier);
	
	/**
	 * Archive les dossiers links associés au dossier.
	 * @param session
	 * @param dossierDoc
	 */
	void archiveDossiersLinks(final CoreSession session, final DocumentModel dossierDoc) throws ClientException;

	/**
	 * Retourne true si le dossier possède l'étape "Pour avis CE" dans sa feuille de route
	 * @param session
	 * @param dossier
	 */
	boolean hasEtapeAvisCE(CoreSession session, Dossier dossier) throws ClientException;

	/**
	 * Retourne true si le dossier possède l'étape "Pour avis CE" à l'état "Avis rendu" dans sa feuille de route
	 * @param session
	 * @param dossier
	 */
	boolean hasEtapeAvisCEValide(CoreSession session, Dossier dossier) throws ClientException;
	
	/**
	 * recherche de dossier par idDossier (ie rattachement avec le MGPP)
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	DocumentModel findDossierDocFromIdDossier(CoreSession session, String idDossier) throws ClientException;
}
