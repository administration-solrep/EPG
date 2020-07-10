package fr.dila.solonepg.adamant.dao;

import java.util.Collection;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.adamant.model.DossierExtractionBordereau;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.adamant.model.ExtractionStatus;
import fr.dila.solonepg.api.cases.Dossier;

/**
 * Interface DAO associé aux objets liés à l'extraction Adamant.
 * 
 * @author tlombard
 */
public interface DossierExtractionDao {
	/**
	 * Création d'un nouveau lot.
	 * 
	 * @return le lot créé.
	 * @throws ClientException
	 */
	DossierExtractionLot createLot() throws ClientException;

	/**
	 * Renvoie la liste des DossierExtractionLot ayant le statut indiqué. Les
	 * lots sont retournés par date de création croissante.
	 * 
	 * @param status
	 * @return
	 */
	List<DossierExtractionLot> listLotsByStatusSortedByDateAsc(ExtractionStatus status) throws ClientException;

	/**
	 * Met à jour et sauvegarde en base de données le statut d'un lot.
	 * 
	 * @param lot
	 * @param status
	 * @throws ClientException
	 */
	void updateLotStatus(DossierExtractionLot lot, ExtractionStatus status) throws ClientException;
	
	/**
	 * Supprime le dossier du lot.
	 * @param dossier
	 * @param lot
	 */
	void deleteFromLot(Dossier dossier, DossierExtractionLot lot) throws ClientException;
	
	/**
	 * Réalise la sauvegarde/persistence en base de données du bordereau.
	 * @param bordereau
	 */
	void saveBordereau(DossierExtractionBordereau bordereau);
	
	/**
	 * Récupère en BDD les bordereaux associés au lot.
	 * @param lot un lot d'extraction.
	 * @return une collection de bordereaux
	 */
	Collection<DossierExtractionBordereau> getBordereaux(DossierExtractionLot lot) throws ClientException;
}
