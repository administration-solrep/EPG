package fr.dila.solonepg.adamant.service;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.adamant.model.ExtractionStatus;
import fr.dila.solonepg.api.cases.Dossier;

/**
 * Service dédié à la gestion des lots d'extraction de dossiers.
 * 
 * @author tlombard
 */
public interface ExtractionLotService {
	/**
	 * Renvoie la liste des lots actuellement dans le statut indiqué.
	 * 
	 * @param status
	 *            un objet ExtractionStatus
	 * @return une liste de DossierExtractionLot potentiellement vide.
	 */
	List<DossierExtractionLot> getLotsByExtractionStatus(ExtractionStatus status) throws ClientException;

	/**
	 * Supprime le lien entre le dossier et le lot.
	 * 
	 * @param dossier
	 * @return true si le lot est vide à la suite de cette suppression, false sinon
	 */
	boolean deleteFromLot(CoreSession session, Dossier dossier, DossierExtractionLot lot) throws ClientException;

	/**
	 * Met à jour le statut d'un lot d'extraction de dossiers.
	 * 
	 * @param lot
	 * @param status
	 */
	void updateLotStatus(DossierExtractionLot lot, ExtractionStatus status) throws ClientException;

	/**
	 * Compte le nombre de dossiers restant à extraire dans un lot.
	 * 
	 * @param lot
	 *            DossierExtractionLot
	 * @return
	 */
	int countDossiers(CoreSession session, DossierExtractionLot lot) throws ClientException;

	/**
	 * Cherche et renvoie le prochain dossier dans le lot indiqué en paramètre.
	 * 
	 * @param lot
	 *            objet DossierExtractionLot
	 * @throws ClientException
	 */
	Dossier getNextDossierInLot(CoreSession session, DossierExtractionLot lot) throws ClientException;

	/**
	 * Crée un lot et assigne à chaque documents dont l'id est listé l'id du lot correspondant
	 * 
	 * @param ids
	 * @param documentManager
	 * @throws Exception 
	 */
	void createAndAssignLotToDocumentsByIds(List<String> ids, CoreSession documentManager) throws ClientException;
}
