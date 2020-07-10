package fr.dila.solonepg.api.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;

/**
 * Service de distribution des dossiers de SOLON EPG.
 * 
 * @author jtremeaux
 */
public interface DossierDistributionService extends fr.dila.ss.api.service.DossierDistributionService {

	/**
	 * Donne un avis favorable avec correction pour l'étape de feuille de route lié au dossier du dossier link.
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            Dossier
	 * @param dossierLinkDoc
	 *            dossierLink à valider
	 * @param userId
	 *            identifiantUtilisateur
	 * @throws ClientException
	 */
	void validerEtapeCorrection(CoreSession session, DocumentModel dossierDoc, DocumentModel dossierLinkDoc)
			throws ClientException;

	/**
	 * Exécute l'étape avec "non concerné" pour l'étape de feuille de route lié au dossier du dossier link. Ajout
	 * automatiquement une étape "retour pour modification" après l'étape validée.
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            Dossier
	 * @param dossierLinkDoc
	 *            dossierLink à valider
	 * @throws ClientException
	 */
	void validerEtapeNonConcerneAjoutEtape(CoreSession session, DocumentModel dossierDoc, DocumentModel dossierLinkDoc)
			throws ClientException;

	/**
	 * Exécute l'étape avec "retour pour modification" pour l'étape de feuille de route lié au dossier du dossier link.
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            Dossier
	 * @param dossierLinkDoc
	 *            dossierLink à valider
	 * @throws ClientException
	 */
	void validerEtapeRetourModification(CoreSession session, DocumentModel dossierDoc, DocumentModel dossierLinkDoc)
			throws ClientException;

	/**
	 * Exécute l'étape avec "retour pour modification" pour l'étape de feuille de route lié au dossier du dossier link.
	 * Ajout automatiquement une étape "retour pour modification" et une copie de l'étape validée après l'étape validée.
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            Dossier
	 * @param dossierLinkDoc
	 *            dossierLink à valider
	 * @param posteID
	 *            poste à indiquer dans la nouvelle étape
	 * @throws ClientException
	 */
	void validerEtapeRetourModificationAjoutEtape(CoreSession session, DocumentModel dossierDoc,
			DocumentModel dossierLinkDoc, String posteID) throws ClientException;

	/**
	 * Refus d'une étape : gère le cas des validationStatus différents dans le cas de l'avis CE cf RG-INS-FDR-20
	 * 
	 * @param session
	 * @param dossierDoc
	 * @param dossierLinkDoc
	 * @param validationStatus
	 * @throws ClientException
	 */
	void validerEtapeRefus(CoreSession session, DocumentModel dossierDoc, DocumentModel dossierLinkDoc,
			String validationStatus) throws ClientException;

	/**
	 * Validation d'un étape : gère le cas des validationStatus différents dans le cas de l'avis CE cf RG-INS-FDR-20
	 * 
	 * @param session
	 * @param dossierDoc
	 * @param dossierLinkDoc
	 * @param validationStatus
	 * @throws ClientException
	 */
	void validerEtape(CoreSession session, DocumentModel dossierDoc, DocumentModel dossierLinkDoc,
			String validationStatus) throws ClientException;

	/**
	 * Renseigne les données du DossierLink après sa création.
	 * 
	 * @param session
	 *            Session
	 * @param dossierLinkDoc
	 *            Document DossierLink
	 * @throws ClientException
	 */
	void setDossierLinksFields(CoreSession session, DocumentModel dossierLinkDoc) throws ClientException;

	/**
	 * Recherche et démarre la feuille de route associée au dossier.
	 * 
	 * @param session
	 *            Session
	 * @param dossier
	 *            Dossier
	 * @param posteId
	 *            Identifiant technique du poste de la première étape
	 * @return Instance de feuille de route
	 * @throws ClientException
	 *             ClientException
	 */
	DocumentRoute startDefaultRoute(CoreSession session, Dossier dossier, String posteId) throws ClientException;

	/**
	 * Permet de lancer le dossier (approuve le CaseLink de la première étape).
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            Dossier
	 * @param dossierLinkDoc
	 *            ActionableCaseLink de la première étape
	 * @throws ClientException
	 *             ClientException
	 */
	void lancerDossier(CoreSession session, DocumentModel dossierDoc, DocumentModel dossierLinkDoc)
			throws ClientException;

	/**
	 * Recherche le poste à attribuer à la première étape de feuille de route. Si l'utilisateur a un seul poste : on
	 * choisit celui-ci. Sinon, recherche le poste correspondant aux 4 premières lettres du NOR. Sinon, retourne null.
	 * L'utilisateur devra choisir manuellement le poste.
	 * 
	 * @param posteIdSet
	 *            Postes de l'utilisateur
	 * @param norMinistere
	 *            Lettres de NOR ministère (ex. "ECO")
	 * @param norDirection
	 *            Lettres de NOR direction (ex. "A")
	 * @return Poste trouvé ou null
	 * @throws ClientException
	 */
	String getPosteForNor(Set<String> posteIdSet, String norMinistere, String norDirection) throws ClientException;

	/**
	 * Initialise l'ACL du DossierLink à sa création.
	 * 
	 * @param session
	 *            Session
	 * @param dossierLinkDoc
	 *            Document DossierLink
	 * @param dossierDoc
	 *            Document dossier
	 * @throws ClientException
	 */
	void initDossierLinkAcl(CoreSession session, DocumentModel dossierLinkDoc, DocumentModel dossierDoc)
			throws ClientException;

	/**
	 * Action "NOR Attribué" (passe du dossier à l'état publié.
	 * 
	 * @param session
	 *            Session
	 * @param dossierDoc
	 *            Dossier
	 * @throws ClientException
	 */
	void norAttribue(CoreSession session, DocumentModel dossierDoc) throws ClientException;

	/**
	 * Mise a jour des champs de denormalisation des {@link DossierLink} lié au {@link Dossier}
	 * 
	 * @see DocumentModifiedListener
	 * @param coreSession
	 * @param dossier
	 * @throws ClientException
	 */
	void updateDossierLinksFields(CoreSession session, Dossier dossier) throws ClientException;

	/**
	 * 
	 * @param session
	 * @param dossiersList
	 * @throws ClientException
	 */
	void validerAutomatiquementEtapePourPublication(CoreSession session) throws ClientException;

	/**
	 * Vérifie la complétude du bordereau et la cohérence des données avec les étapes suivantes
	 * 
	 * @param session
	 * @param dossier
	 * @param nextStepsDoc
	 * @throws ClientException
	 */
	void checkDossierBeforeValidateStep(CoreSession session, Dossier dossier, List<DocumentModel> nextStepsDoc)
			throws ClientException;

	/**
	 * Modifie les acls suite aux modifications de ministère de rattachement des dossiers
	 * 
	 * @param session
	 * @param dossier
	 * @throws ClientException
	 */
	void updateDossierLinksACLs(final CoreSession session, String idMinistere) throws ClientException;

	/**
	 * FEV507 : Identifie et renvoie la ou les étapes à contrôler parmi les étapes précédant l'étape courante.
	 * 
	 * On remonte les étapes à partir de l'étape passée en paramètre jusqu'à la
	 * première occurrence de :
	 * <li>- Une étape pour avis affectée à un poste identifié comme « Chargé de
	 * mission SGG » et/ou « Conseiller du Premier ministre »</li>
	 * <li>- une étape // avec une étape Pour avis dedans, elle-même affectée à un
	 * poste identifié comme « Chargé de mission SGG » et/ou « Conseiller du Premier
	 * ministre »</li>
	 * 
	 * @param session
	 * @param stepDoc DocumentModel associée à l'étape en cours
	 * @return
	 * @throws ClientException 
	 */
	Collection<DocumentModel> findAvisChargesDeMissionSteps(final CoreSession session, DocumentModel stepDoc) throws ClientException;

	void avisRectificatif(CoreSession session, DocumentModel dossierDocList) throws ClientException;

	/**
	 * Recherche et démarre la feuille de route associée au dossier.
	 * 
	 * @param session
	 *            Session
	 * @param dossier
	 *            Dossier
	 * @param posteId
	 *            Identifiant technique du poste de la première étape
	 * @param norDossierCopieFdr
	 *            Numero NOR du dossier pour copier la feuille de route
	 * @return Instance de feuille de route
	 * @throws ClientException
	 *             ClientException
	 */
	DocumentRoute startRouteCopieFromDossier(CoreSession session, DocumentModel dossier, String posteId, String norDossierCopieFdr) throws ClientException;
}
