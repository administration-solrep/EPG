package fr.dila.solonepg.api.service;

import fr.dila.ss.api.service.SSOrganigrammeService;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.user.STUser;
import java.util.List;
import java.util.Set;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Implémentation du service d'organigramme pour SOLON EPG.
 *
 * @author fesposito
 * @author jtremeaux
 */
public interface EpgOrganigrammeService extends SSOrganigrammeService {
    /**
     * Recherche pour un poste donné l'ensemble des lettres NOR ministère + direction correspondantes. Remonte
     * l'organigramme afin de trouver les lettres NOR.
     *
     * @param posteId
     *            Identifiant technique du poste
     * @return Ensemble des lettres NOR ministère + direction
     */
    Set<String> findLettreNorByPoste(String posteId);

    /**
     *
     * @param profil
     * @return la list des utilisateurs du profil
     */
    List<STUser> getUserFromProfil(String profil);

    /**
     * Retourne le ministère à partir des 3 lettres Nor qui lui sont associé
     *
     * @param nor
     * @return EpgEntiteNode
     */
    EntiteNode getMinistereFromNor(String nor);

    /**
     * Retourne la direction à partir du ministère et de la lettre Nor associé à cette direction.
     *
     * @param EntiteNode
     *            ministereNode
     * @param nor
     * @return EpgUniteStructurelleNode
     */
    UniteStructurelleNode getDirectionFromMinistereAndNor(EntiteNode ministereNode, String nor);

    /**
     * Crée un poste désactivé.
     * @param id
     * @return
     */
    PosteNode createDesactivatePoste(CoreSession coreSession, String id, String label);

    UniteStructurelleNode createDesactivateUniteStructurelleModel(String id);

    List<OrganigrammeNode> getAllDirectionList();
}
