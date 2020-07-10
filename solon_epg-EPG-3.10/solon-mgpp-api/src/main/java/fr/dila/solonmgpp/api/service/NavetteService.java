package fr.dila.solonmgpp.api.service;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.sword.xsd.solon.epp.EppEvtContainer;
import fr.sword.xsd.solon.epp.EvenementType;

/**
 * Interface du service de gestion des navettes
 * 
 * @author asatre
 * 
 */
public interface NavetteService {

    /**
     * Recuperation des navettes d'une {@link FicheLoi}
     * 
     * @param session
     * @param idFicheLoi
     * @return
     * @throws ClientException
     */
    List<DocumentModel> fetchNavette(CoreSession session, String idFicheLoi) throws ClientException;

    /**
     * Gestion de la création/mise à jour des navettes.
     * 
     * @param session
     * @param idEvenement
     * @param typeEvenement
     * @throws ClientException
     */
    void addNavetteToFicheLoi(CoreSession session, String idEvenement, EvenementType typeEvenement) throws ClientException;
    
    /**
     * Gestion de la création/mise à jour des navettes.
     * 
     * @param session
     * @param eppEvtContainer
     * @throws ClientException
     */
    void addNavetteToFicheLoi(CoreSession session, EppEvtContainer eppEvtContainer) throws ClientException;

    /**
     * recherche si des navettes existe pour la {@link FicheLoi}
     * 
     * @param session
     * @param idFicheLoi
     * @return
     * @throws ClientException
     */
    Boolean hasNavette(CoreSession session, String idFicheLoi) throws ClientException;

}
