package fr.dila.solonepg.api.service;

import fr.dila.st.api.parametre.STParametre;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Service permettant de gérer les paramètres du moteur elastic search
 *
 * @author Amaury Bianchi
 */
public interface ElasticParametrageService {
    /**
     * Retourne la valeur du paramètre avec l'identifiant id.
     *
     * @param session
     *            Session
     * @param anid
     *            Identifiant technique du paramètre
     * @return Valeur du paramètre
     */
    String getParametreValue(CoreSession session, String anid);

    /**
     * Retourne le document paramètre avec l'identifieant id .
     *
     * @param session
     *            Session
     * @param anid
     *            Identifiant technique du paramètre
     * @return Objet métier paramètre
     */
    STParametre getParametre(CoreSession session, String anid);

    /**
     * Retourne la racine des paramètres.
     *
     * @param session
     *            Session
     * @return Document racine des paramètres
     */
    DocumentModel getParametreFolder(CoreSession session);

    /**
     * Retourne la liste des paramètres
     * @param session
     * @return
     */
    List<DocumentModel> getESParametres(CoreSession session);

    /**
     * Sauvegarde le paramètre
     * @param session
     * @param documentModel
     * @param formValeur
     */
    void updateESParametre(CoreSession session, DocumentModel documentModel, String formValeur);
}
