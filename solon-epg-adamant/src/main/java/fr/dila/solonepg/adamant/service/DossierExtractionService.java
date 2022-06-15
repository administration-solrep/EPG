package fr.dila.solonepg.adamant.service;

import fr.dila.solonepg.adamant.model.DossierExtractionBordereau;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.api.cases.Dossier;
import java.io.File;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.platform.audit.api.LogEntry;

/**
 * Service dédié à l'extraction d'un dossier.
 *
 * @author tlombard
 */
public interface DossierExtractionService {
    /**
     * Génère l'archive pour un dossier.
     * @throws Exception
     */
    void genererDossierArchive(
        CoreSession session,
        Dossier dossier,
        DossierExtractionLot lot,
        DossierExtractionBordereau bordereau
    );

    int getDelaisEligibilite(CoreSession session);

    /*
     * Retourne la liste des Vecteurs de publication éligible à l'archivage
     */
    List<String> getVecteurPublicationEligibilite(CoreSession session);

    /*
     * Retourne la liste des type d'acte éligible à l'archivage
     */
    List<String> getTypeActeEligibilite(CoreSession session);

    /**
     * Vérifie l'éligibilité d'un document
     *
     * @param doc
     * @param session
     * @throws NuxeoException
     *
     */
    boolean isDocumentEligible(Dossier dossier, CoreSession session);

    /**
     * Récupère ou crée le répertoire d'extraction pour le lot.
     *
     * @param lot
     * @return un pointeur vers le répertoire.
     */
    File getOrCreateExtractionLotFolder(DossierExtractionLot lot);

    /**
     * Check si le dossier contient possède des fichiers dans le borderau/ fond de dossier
     * Et vérifie si certain de ces fichiers sont en .zip
     *
     * @param documentModel
     * @return
     */
    boolean checkFile(DocumentModel documentModel, CoreSession documentManager);

    /**
     * Récupère dans la configuration le nombre maximal de dossiers à traiter
     * lors d'une occurrence du batch.
     *
     * @param session
     * @return valeur int
     */
    int getMaxDossiersATraiter(CoreSession session);

    /**
     * Récupère dans la configuration le numero Solon
     *
     * @param session
     * @return valeur int
     */
    int getNumeroSolon(CoreSession session);

    List<LogEntry> getLogEntryList(String idDossier);
}
