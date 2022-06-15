package fr.dila.solonepg.adamant.dao;

import fr.dila.solonepg.adamant.model.DossierExtractionBordereau;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.adamant.model.ExtractionStatus;
import fr.dila.solonepg.adamant.model.RetourVitamBordereauLivraison;
import fr.dila.solonepg.adamant.model.RetourVitamRapportVersement;
import fr.dila.solonepg.api.cases.Dossier;
import java.util.Collection;
import java.util.List;

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
     */
    DossierExtractionLot createLot();

    /**
     * Renvoie la liste des DossierExtractionLot ayant le statut indiqué. Les
     * lots sont retournés par date de création croissante.
     *
     * @param status
     * @return
     */
    List<DossierExtractionLot> listLotsByStatusSortedByDateAsc(ExtractionStatus status);

    /**
     * Met à jour et sauvegarde en base de données le statut d'un lot.
     *
     * @param lot
     * @param status
     */
    void updateLotStatus(DossierExtractionLot lot, ExtractionStatus status);

    /**
     * Supprime le dossier du lot.
     * @param dossier
     * @param lot
     */
    void deleteFromLot(Dossier dossier, DossierExtractionLot lot);

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
    Collection<DossierExtractionBordereau> getBordereaux(DossierExtractionLot lot);

    /**
     * Réalise la sauvegarde/persistence en base de données du bordereau de Livraison.
     * @param ligne du bordereau
     */
    void saveLineBordereauLivraison(RetourVitamBordereauLivraison bordereau);

    /**
     * Réalise la sauvegarde/persistence en base de données du rapport de versement.
     * @param ligne du bordereau
     */
    void saveLineRapportVersement(final RetourVitamRapportVersement rapport);
}
