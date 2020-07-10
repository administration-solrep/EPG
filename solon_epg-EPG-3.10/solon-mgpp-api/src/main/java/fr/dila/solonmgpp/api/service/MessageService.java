package fr.dila.solonmgpp.api.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.SortInfo;

import fr.dila.solonmgpp.api.dto.CorbeilleDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.sword.xsd.solon.epp.Message;

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
     * @throws ClientException
     */
    List<MessageDTO> findMessageByCorbeille(SSPrincipal ssPrincipal, CorbeilleDTO corbeilleDTO, CoreSession session, String currentMenu)
            throws ClientException;

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
     * @throws ClientException
     */
    List<MessageDTO> filtrerCorbeille(CorbeilleNode corbeilleNode, CoreSession session, Map<String, Serializable> mapFiltrable,
            List<SortInfo> sortInfos, SSPrincipal ssPrincipal, String currentMenu) throws ClientException;

}
