package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.Date;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteSignale;

/**
 * Interface du service sur les textes signalés de l'espace de suivi.
 * 
 * @author asatre
 */
public interface TexteSignaleService extends Serializable {

    void verser(CoreSession session, Dossier dossier, String type) throws ClientException;

    void retirer(CoreSession session, Dossier dossier) throws ClientException;

    void save(CoreSession session, String idDossier, Date date, String vecteurPublication, String observation, String type) throws ClientException;

    /**
     * Recherche un texte signale par son idDossier, creation si non existant
     * 
     * @param session
     * @param idDossier
     * @return
     * @throws ClientException
     */
    TexteSignale findTexteSignale(CoreSession session, String idDossier, String type) throws ClientException;

}
