package fr.dila.solonmgpp.api.service;

import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Interface de gestion du parametrage du mgpp
 *
 * @author asatre
 *
 */
public interface ParametreMgppService {
    /**
     * Recherche du parametrage MGPP (document unique)
     *
     * @param session
     * @return
     */
    ParametrageMgpp findParametrageMgpp(CoreSession session);

    /**
     * sauvegarde du parametrage avecverification ou non de la connectivité avec EPP
     *
     * @param documentManager
     * @param parametrageMgpp
     * @param checkConnexion
     * @return
     */
    ParametrageMgpp saveParametrageMgpp(
        CoreSession documentManager,
        ParametrageMgpp parametrageMgpp,
        Boolean checkConnexion
    );
}
