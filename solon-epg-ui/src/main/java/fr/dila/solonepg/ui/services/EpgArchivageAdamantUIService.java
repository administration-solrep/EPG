package fr.dila.solonepg.ui.services;

import java.util.Date;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public interface EpgArchivageAdamantUIService {
    /*
     * Compter le nombre de dossier éligible en fonction du statut et des date sélectionné
     */
    int countDossierEligible(final CoreSession session, String statut, Date startDate, Date endDate);

    /**
     * Crée un nouveau lot pour l'archivage
     * @param session
     */
    void creerLotArchivage(CoreSession session, String statut, Date startDate, Date endDate);

    /**
     * Compte le nombre de dossiers éligibles à l'archivage pour une période donnée
     * @param session
     * @param statut Statut des dossiers à récuperer
     * @param startDate Date de début de la période
     * @param endDate Date de fin de la période
     * @return le nombre de dossier éligible à l'archivage
     */
    Integer doQueryCountDosEligible(CoreSession session, String statut, Date startDate, Date endDate);

    /**
     * Récupère les id de dossiers éligibles à l'archivage pour une période donnée
     * @param session
     * @param statut Statut des dossiers à récuperer
     * @param startDate Date de début de la période
     * @param endDate Date de fin de la période
     * @return id des dossiers éligibles à l'archivage
     */
    List<String> doQueryListIdDosEligible(final CoreSession session, String statut, Date startDate, Date endDate);
}
