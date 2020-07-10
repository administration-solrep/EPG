package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.Dossier;

public interface NORService extends Serializable {
	/**
	 * Création d'un nouveau NOR. NOR = Code ministère (3 lettres) + Code direction (1 lettre) + Année (2 chiffres) +
	 * Compteur (5 chiffres) + Code acte (1 lettre) Incrémente la séquence du numéro de NOR.
	 * 
	 * @param norActe
	 *            Lettre de NOR de l'acte (ex. "L")
	 * @param norMinistere
	 *            Lettres de NOR du ministère (ex. "ECO")
	 * @param norDirection
	 *            Lettre de NOR de la direction (ex. "A")
	 * @return Nouveau NOR (ex. "ECOX9800017L")
	 * @throws ClientException
	 */
	String createNOR(String norActe, String norMinistere, String norDirection) throws ClientException;

	/**
	 * Création du NOR d'un dossier de type Rectificatif à partir du dossier rectifié.
	 * 
	 * @param norDossier
	 *            NOR du dossier à rectifier (ex. "ECOX9800017L")
	 * @return Nouveau NOR (ex. "ECOX9800017Z")
	 * @throws ClientException
	 */
	String createRectificatifNOR(Dossier dossierRectifie) throws ClientException;

	/**
	 * Renvoie la lettre associé au nombre de rectificatif créé pour un dossier.
	 * 
	 * @param nbRectificatif
	 * @return String la lettre associé au nombre de rectificatif créé pour un dossier.
	 */
	String getLettreFromNbRectificatif(Long nbRectificatif);

	/**
	 * Récupération d'un dossier à partir de son NOR
	 * 
	 * @param session
	 * @param nor
	 * @return dossier
	 * @throws ClientException
	 */
	DocumentModel getDossierFromNOR(CoreSession session, String nor) throws ClientException;

	/**
	 * Récupération d'un rectificatif à partir du NOR du dossier Initial et du numéro du rectificatif (1er, 2ème ou
	 * 3ème)
	 * 
	 * @param session
	 * @param nor
	 * @return dossier
	 * @throws ClientException
	 */
	Dossier getRectificatifFromNORAndNumRect(CoreSession session, String nor, Long nbRectificatif)
			throws ClientException;

	/**
	 * Récupération d'un dossier à partir de son NOR
	 * 
	 * @param session
	 * @param nor
	 * @return dossier
	 * @throws ClientException
	 */
	Dossier findDossierFromNOR(CoreSession session, String nor) throws ClientException;

	/**
	 * Réattribution du NOR : renvoi le nor modifié cf UC-SOL-SEL-18
	 * 
	 * @param dossierReattribue
	 *            dossier à rectifier (ex. "ECOX9800017L")
	 * @param norMinistere
	 *            norMinistere CCO
	 * @return Nouveau NOR (ex. "CCOX9800017L")
	 * @throws ClientException
	 */
	String reattributionMinistereNOR(Dossier dossierReattribue, String norMinistere) throws ClientException;

	/**
	 * Réattribution du NOR : renvoi le nor modifié cf UC-SOL-SEL-18
	 * 
	 * @param dossierReattribue
	 *            dossier à rectifier (ex. "ECOX9800017L")
	 * @param norMinistere
	 *            norMinistere CCO
	 * @param norDirection
	 *            norDirection D
	 * @return Nouveau NOR (ex. "CCOD9800017L")
	 * @throws ClientException
	 */
	String reattributionDirectionNOR(Dossier dossierReattribue, String norMinistere, String norDirection)
			throws ClientException;

	/**
	 * 
	 * @param coreSession
	 * @param nor
	 * @return
	 * @throws ClientException
	 */
	List<String> findDossierIdsFromNOR(CoreSession coreSession, String nor) throws ClientException;

	/**
	 * @param session
	 *            The CoreSession
	 * @return set of available NOR
	 * @throws ClientException
	 */
	Set<String> retrieveAvailableNorList(final CoreSession session) throws ClientException;

	boolean checkNorUnicity(final CoreSession session, String nor) throws ClientException;
	
	/**
	 * Récupération du dossier via son numéro ISA
	 * 
	 * @param session
	 * @param nor
	 * @return
	 * @throws ClientException
	 */
	DocumentModel getDossierFromISA(final CoreSession session, final String nor) throws ClientException;

	boolean isStructNorValide(String nor);

	/**
	 * Récupération d'un dossier à partir de son NOR en respectant les droit d'accès
	 * 
	 * @param session
	 * @param nor
	 * @return dossier
	 * @throws ClientException
	 */
	DocumentModel getDossierFromNORWithACL(CoreSession session, String nor) throws ClientException;

}
