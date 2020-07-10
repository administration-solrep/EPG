package fr.dila.solonepg.adamant.service;

import java.io.File;
import java.util.Date;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.adamant.model.DossierExtractionBordereau;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.api.cases.Dossier;

/**
 * Service dédié à l'extraction d'un dossier.
 * 
 * @author tlombard
 */
public interface DossierExtractionService {
	/**
	 * Génère l'archive pour un dossier.
	 * @throws Exception 
	 *
	 * @throws ClientException
	 */
	void genererDossierArchive(CoreSession session, Dossier dossier, Map<String, String> messages,
			DossierExtractionLot lot, DossierExtractionBordereau bordereau) throws ClientException;

	int getDelaisEligibilite(CoreSession session) throws ClientException;

	/**
	 * Vérifie l'éligibilité d'un document
	 * 
	 * @param doc
	 * @param session
	 * @throws ClientException
	 * 
	 */
	boolean isDocumentEligible(Dossier dossier, CoreSession session) throws ClientException;

	/**
	 * Récupère ou crée le répertoire d'extraction pour le lot.
	 * 
	 * @param lot
	 * @return un pointeur vers le répertoire.
	 * @throws ClientException
	 */
	File getOrCreateExtractionLotFolder(DossierExtractionLot lot) throws ClientException;

	/**
	 * Ajouter un nombre de mois a une date
	 * @param date
	 * @param month
	 * @return
	 */
	Date addMonthToDate(Date date, int month);
	
	/**
	 * Check si le dossier contient possède des fichiers dans le borderau/ fond de dossier 
	 * Et vérifie si certain de ces fichiers sont en .zip
	 * 
	 * @param documentModel
	 * @return
	 * @throws ClientException 
	 */
	boolean checkFile(DocumentModel documentModel, CoreSession documentManager) throws ClientException;

	/**
	 * Récupère dans la configuration le nombre maximal de dossiers à traiter
	 * lors d'une occurrence du batch.
	 * 
	 * @param session
	 * @return valeur int
	 * @throws ClientException 
	 */
	int getMaxDossiersATraiter(CoreSession session) throws ClientException;

	/**
	 * Récupère dans la configuration le numero Solon
	 * 
	 * @param session
	 * @return valeur int
	 * @throws ClientException 
	 */
	int getNumeroSolon(CoreSession session) throws ClientException;

}
