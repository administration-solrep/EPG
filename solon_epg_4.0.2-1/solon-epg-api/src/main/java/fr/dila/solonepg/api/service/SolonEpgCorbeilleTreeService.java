package fr.dila.solonepg.api.service;

import fr.dila.ss.api.enums.TypeRegroupement;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Service qui permet de gérer l'arbre des corbeilles
 *
 */
public interface SolonEpgCorbeilleTreeService {
    /**
     * Retourne la liste des corbeilles pour le type de regroupement
     * donné (par poste, par type d'étape, par type d'acte).
     *
     * @param session
     * @param regroupement
     * @return
     */
    Map<String, Integer> getCorbeilleTree(CoreSession session, TypeRegroupement regroupement);
}
