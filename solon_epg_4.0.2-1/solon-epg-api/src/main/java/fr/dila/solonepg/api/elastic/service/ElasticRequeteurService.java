package fr.dila.solonepg.api.elastic.service;

import java.io.IOException;

public interface ElasticRequeteurService {
    /**
     * Suppression du document courant dans l'index elastic
     * @param id Id du document
     */
    void deleteDocument(String id) throws IOException;
}
