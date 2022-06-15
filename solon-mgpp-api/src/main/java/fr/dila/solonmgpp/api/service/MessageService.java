package fr.dila.solonmgpp.api.service;

import fr.dila.solonmgpp.api.dto.CorbeilleDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.sword.xsd.solon.epp.Message;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.SortInfo;

/**
 * Interface du service des messages pour l'interaction epp/mgpp
 *
 * @author asatre
 *
 */
public interface MessageService {
    /**
     * COnstruction d'un {@link MessageDTO} à partir d'un {@link Message}
     *
     * @param message
     * @param session
     * @return
     */
    MessageDTO buildMessageDTOFromMessage(Message message, CoreSession session);

    /**
     * retourne la liste des evenements correspondant a l'id de la corbeille pour l'utilisateur gouvernement de SOLON EPP<br/>
     * + mise en cache des messages
     *
     * @param currentMenu
     *
     * @return
     */
    List<MessageDTO> findMessageByCorbeille(
        SSPrincipal ssPrincipal,
        CorbeilleDTO corbeilleDTO,
        CoreSession session,
        String currentMenu
    );

    /**
     * Invalide le cache corbeille en cas d'écart avec EPP.
     *
     * @param ssPrincipal Utilisateur
     * @param corbeilleDTO Corbeille à vérifier
     * @param messageCount Nombre de messages récupérés depuis EPP
     */
    void invalidateCacheCorbeille(SSPrincipal ssPrincipal, CorbeilleDTO corbeilleDTO, int messageCount);

    /**
     * vidage du cache pour un utilisateur
     *
     * @param corbeilleDTO
     */
    void clearCache(SSPrincipal ssPrincipal);

    /**
     * Filtre sur une corbeille via WebService EPP
     *
     * @param corbeilleNode
     * @param session
     * @param mapFiltrable
     * @param sortInfos
     * @return
     */
    List<MessageDTO> filtrerCorbeille(
        CorbeilleNode corbeilleNode,
        CoreSession session,
        Map<String, Serializable> mapFiltrable,
        List<SortInfo> sortInfos,
        SSPrincipal ssPrincipal,
        String currentMenu
    );
}
