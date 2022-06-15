package fr.dila.solonmgpp.api.service;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.sword.xsd.solon.epp.RechercherEvenementResponse;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface du service de gestion de la recherche dans SOLON MGPP
 *
 * @author asatre
 *
 */
public interface RechercheService {
    /**
     * Initialise les restrictions par rapport au menu courant
     *
     * @param critereRechercheDTO
     * @return
     */
    CritereRechercheDTO initCritereRechercheDTO(CritereRechercheDTO critereRechercheDTO);

    /**
     * Recherche de message dans EPP par critere de recherche
     *
     * @param critereRechercheDTO
     * @return
     */
    List<MessageDTO> findMessage(CritereRechercheDTO critereRechercheDTO, CoreSession session);

    /**
     * Recherche de {@link Dossier}
     *
     * @param coreSession
     * @param critereRechercheDTO
     * @return
     */
    List<DocumentModel> findDossier(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO);

    /**
     * Recherche {@link FichePresentationOEP}
     *
     * @param coreSession
     * @param critereRecherche
     * @return
     */
    List<DocumentModel> findOEP(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO);

    /**
     * Recherche {@link FichePresentationAVI}
     *
     * @param coreSession
     * @param critereRecherche
     * @return
     */
    List<DocumentModel> findAVI(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO);

    /**
     * Recherche {@link FichePresentationDOC}
     *
     * @param coreSession
     * @param critereRecherche
     * @return
     */
    List<DocumentModel> findDOC(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO);

    List<DocumentModel> findAUD(final CoreSession coreSession, final CritereRechercheDTO critereRechercheDTO);

    RechercherEvenementResponse findMessageWS(CritereRechercheDTO critereRechercheDTO, CoreSession session);

    List<DocumentModel> findDPG(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO);

    List<DocumentModel> findSD(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO);

    List<DocumentModel> findJSS(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO);

    List<DocumentModel> findDR(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO);

    List<DocumentModel> findDecret(CoreSession coreSession, CritereRechercheDTO critereRechercheDTO);
}
