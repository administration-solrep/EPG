package fr.dila.solonepg.api.service;

import org.nuxeo.ecm.core.api.CoreSession;

public interface EpgArchivageAdamantService {
    /**
     * Récupère le nombre de dossiers en cours d'extraction
     * @return le nombre de dossiers en cours d'extraction
     */
    int getNbDosExtracting(CoreSession session);
}
