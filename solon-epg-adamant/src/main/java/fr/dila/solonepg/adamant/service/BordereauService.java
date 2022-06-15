package fr.dila.solonepg.adamant.service;

import fr.dila.solonepg.adamant.model.DossierExtractionBordereau;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.core.dto.DossierArchivageStatDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public interface BordereauService {
    /**
     * Initialisation du bordereau à partir des infos du dossier.
     *
     * @return
     */
    DossierExtractionBordereau initBordereau(Dossier dossier, DossierExtractionLot lot);

    /**
     * Complète le bordereau avec le poids du fichier généré et son empreinte.
     *
     * @param bordereau
     * @param sipFile
     */
    void completeBordereau(DossierExtractionBordereau bordereau, File sipFile, String messageIdentifierArchive)
        throws IOException;

    /**
     * Sauvegarde le bordereau en BDD
     *
     * @param bordereau
     */
    void saveBordereau(DossierExtractionBordereau bordereau);

    /**
     * Génère le bordereau du lot sous forme de fichier, déposé dans le
     * répertoire dont le chemin est indiqué en paramètre.
     *
     * @param lot
     *            lot dont le bordereau est à extraire.
     * @param folder
     *            pointeur vers le répertoire
     */
    void generateBordereauFile(CoreSession session, DossierExtractionLot lot, File folder) throws IOException;

    /**
     * Récupérer la liste des Dossiers archivés pour l'affichage dans les statistique avec la pagination
     *
     * @param statutAfter
     *            statut archivage après changement
     * @param dateDebut
     *            date début période
     * @param dateFin
     *            date fin période
     * @param offset
     *            offset
     * @param limit
     *            nombre dossier affiché
     */
    List<DossierArchivageStatDTO> getDossierArchivageStatResultList(
        CoreSession session,
        String statutAfter,
        String dateDebut,
        String dateFin,
        Long offset,
        Long limit,
        String sortInfos
    );

    /**
     * Compter le nombre de Dossier en fonction des données du bean pour la pagination
     *
     * @param statutAfter
     *            statut archivage après changement
     * @param dateDebut
     *            date début période
     * @param dateFin
     *            date fin période
     */
    Integer getCountDossierArchivageStatResult(String statutAfter, String dateDebut, String dateFin);

    /**
     * Récupérer la liste complète des Dossiers archivés pour l'esport au format csv des donées
     *
     * @param statutAfter
     *            statut archivage après changement
     * @param dateDebut
     *            date début période
     * @param dateFin
     *            date fin période
     * @param offset
     *            offset
     * @param limit
     *            nombre dossier affiché
     */
    List<DossierArchivageStatDTO> getDossierArchivageStatFullResultList(
        CoreSession session,
        String statutAfter,
        String dateDebut,
        String dateFin,
        String sortInfo
    );
}
