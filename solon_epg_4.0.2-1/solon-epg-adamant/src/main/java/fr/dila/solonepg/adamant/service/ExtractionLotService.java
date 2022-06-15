package fr.dila.solonepg.adamant.service;

import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.adamant.model.ExtractionStatus;
import fr.dila.solonepg.api.cases.Dossier;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

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
    List<DossierExtractionLot> getLotsByExtractionStatus(ExtractionStatus status);

    /**
     * Supprime le lien entre le dossier et le lot.
     *
     * @param dossier
     * @return true si le lot est vide à la suite de cette suppression, false sinon
     */
    boolean deleteFromLot(CoreSession session, Dossier dossier, DossierExtractionLot lot);

    /**
     * Met à jour le statut d'un lot d'extraction de dossiers.
     *
     * @param lot
     * @param status
     */
    void updateLotStatus(DossierExtractionLot lot, ExtractionStatus status);

    /**
     * Compte le nombre de dossiers restant à extraire dans un lot.
     *
     * @param lot
     *            DossierExtractionLot
     * @return
     */
    int countDossiers(CoreSession session, DossierExtractionLot lot);

    /**
     * Cherche et renvoie les dossiers présent dans le lot indiqué en paramètre.
     *
     * @param lot
     *            objet DossierExtractionLot
     */
    List<DocumentModel> getDossiersInLot(CoreSession session, DossierExtractionLot lot);

    /**
     * Crée un lot et assigne à chaque dossier l'id du lot correspondant.
     *
     * @param idsDossiers les ids des dossier à créer dans le lot d'archivage
     */
    void createAndAssignLotToDocumentsByIds(List<String> idsDossiers);
}
