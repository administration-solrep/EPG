package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteSignale;
import java.io.Serializable;
import java.util.Date;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Interface du service sur les textes signalés de l'espace de suivi.
 *
 * @author asatre
 */
public interface TexteSignaleService extends Serializable {
    void verser(CoreSession session, Dossier dossier, String type);

    void retirer(CoreSession session, Dossier dossier);

    void save(
        CoreSession session,
        String idDossier,
        Date date,
        String vecteurPublication,
        String observation,
        String type
    );

    /**
     * Recherche un texte signale par son idDossier, creation si non existant
     *
     * @param session
     * @param idDossier
     * @return
     */
    TexteSignale findTexteSignale(CoreSession session, String idDossier, String type);
}
