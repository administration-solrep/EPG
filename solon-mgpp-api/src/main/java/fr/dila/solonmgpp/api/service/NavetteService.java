package fr.dila.solonmgpp.api.service;

import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.sword.xsd.solon.epp.EppEvtContainer;
import fr.sword.xsd.solon.epp.EvenementType;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

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
     */
    List<DocumentModel> fetchNavette(CoreSession session, String idFicheLoi);

    /**
     * Gestion de la création/mise à jour des navettes.
     *
     * @param session
     * @param idEvenement
     * @param typeEvenement
     */
    void addNavetteToFicheLoi(CoreSession session, String idEvenement, EvenementType typeEvenement);

    /**
     * Gestion de la création/mise à jour des navettes.
     *
     * @param session
     * @param eppEvtContainer
     */
    void addNavetteToFicheLoi(CoreSession session, EppEvtContainer eppEvtContainer);

    /**
     * recherche si des navettes existe pour la {@link FicheLoi}
     *
     * @param session
     * @param idFicheLoi
     * @return
     */
    Boolean hasNavette(CoreSession session, String idFicheLoi);
}
