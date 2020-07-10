package fr.dila.solonmgpp.api.service;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonmgpp.api.domain.ParametrageMgpp;

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
     * @throws ClientException
     */
    ParametrageMgpp findParametrageMgpp(CoreSession session) throws ClientException;

    /**
     * sauvegarde du parametrage avecverification ou non de la connectivité avec EPP
     * 
     * @param documentManager
     * @param parametrageMgpp
     * @param checkConnexion
     * @return
     * @throws ClientException
     */
    ParametrageMgpp saveParametrageMgpp(CoreSession documentManager, ParametrageMgpp parametrageMgpp, Boolean checkConnexion) throws ClientException;

}
