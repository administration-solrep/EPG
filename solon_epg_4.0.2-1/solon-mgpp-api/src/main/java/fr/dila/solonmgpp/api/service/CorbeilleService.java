package fr.dila.solonmgpp.api.service;

import fr.dila.solonmgpp.api.dto.CorbeilleDTO;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.ss.api.security.principal.SSPrincipal;
import java.util.List;
import java.util.Set;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Interface du service des corbeilles pour l'interaction epp/mgpp
 *
 * @author asatre
 *
 */
public interface CorbeilleService {
    /**
     * retourne la corbeille correspondant a l'id pour l'utilisateur gouvernement de SOLON EPP
     *
     * @param idCorbeille
     * @param ssPrincipal
     * @param session
     * @return
     */
    List<CorbeilleDTO> findCorbeille(String idCorbeille, SSPrincipal ssPrincipal, CoreSession session);

    /**
     * retourne un CorbeilleDTO de la map de corbeilles
     *
     * @param idCorbeille
     * @param session
     * @return
     */
    CorbeilleDTO findSimpleCorbeille(String idCorbeille, CoreSession session);

    /**
     * retourne l'id du dossier parlementaire auquel appartient la corbeille
     *
     * @param idCorbeille
     * @param session
     * @return
     */
    String findDossierParlementaireForCorbeille(String idCorbeille, CoreSession session);

    /**
     * Recherche l'id du {@link CorbeilleDescriptor} en fonction de l'id du menu courant
     *
     * @param idCorbeille
     * @param session
     * @return
     */
    String fetchCorbeille(String idCorbeille, CoreSession session);

    /**
     * Retourne la liste des actions possibles sur les evenements en fonction des droits de l'utilisateur courant
     *
     * @param session
     * @param ssPrincipal
     * @param currentEvenement
     * @return
     */
    Set<String> findActionPossible(CoreSession session, SSPrincipal ssPrincipal, EvenementDTO currentEvenement);

    /**
     * Nombre de dossier dans une corbeille MGPP
     *
     * @param session
     * @param corbeilleNode
     * @return
     */
    Long countDossier(CoreSession session, CorbeilleNode corbeilleNode);

    /**
     * Nombre de dossier dans une corbeille MGPP sans vérification des acls
     *
     * @param session
     * @param corbeilleNode
     * @return
     */
    Long countDossierUnrestricted(CoreSession documentManager, CorbeilleNode corbeilleNode);

    /**
     * requete pour les corbeilles avec des dossiers
     *
     * @param corbeilleNode
     * @param params
     * @return
     */
    StringBuilder getDossierQuery(CorbeilleNode corbeilleNode, List<Object> params);

    /**
     * Récupère l'info en BDD sur l'existence de communication non_traitees/en_cours_redaction pour une corbeille donnée
     *
     * @param session
     * @param idCorbeille
     */
    boolean hasCorbeilleMessages(CoreSession session, String idCorbeille);

    /**
     * Récupère l'info en BDD sur l'existence de communication non_traitees/en_cours_redaction pour une liste de
     * corbeilles données <br>
     * Si au moins une corbeille a des messages = true sinon false
     *
     * @param session
     * @param idsCorbeilles
     * @return
     */
    boolean haveCorbeillesMessages(CoreSession session, Set<String> idsCorbeilles);
}
