package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.recherche.EpgDossierListingDTO;
import fr.dila.ss.api.service.SSArchiveService;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipOutputStream;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface du service d'archive de solon epg
 *
 * @author arolin
 *
 */
public interface ArchiveService extends SSArchiveService {
    /**
     * Initialise un ZipStream et y ajoute les fichiers sélectionnés.
     */
    ZipOutputStream initWriteZipStreamAndAddFile(
        List<DocumentModel> files,
        OutputStream outputStream,
        CoreSession session
    );

    void retirerArchivageDefinitive(final List<DocumentModel> dossierDocs, CoreSession session);

    String getActionFeuilleRouteButtonLabel(String routingTaskType, String labelKey);

    /**
     * Retourne le label qui correspond au pictogramme pour une étape de feuille de route
     */
    String idIconeToLabelFDR(String routingTaskType, String validationStatusId);

    /**
     * Retourne la date de publication au JO du dossier
     */
    Calendar getDateParutionJorfFromRetourDila(Dossier dossier);

    /**
     * Retourne true si le dossier est issue de la reprise
     */
    boolean isDossierReprise(Dossier dossier);

    String getValidStatusToLabel(String key);

    long countAllDossiersArchivageIntermediaire(CoreSession session);

    List<EpgDossierListingDTO> getAllDossiersArchivageIntermediaire(CoreSession session);

    long countAllDossiersArchivageDefinitif(CoreSession session);

    List<EpgDossierListingDTO> getAllDossiersArchivageDefinitif(CoreSession session);
}
