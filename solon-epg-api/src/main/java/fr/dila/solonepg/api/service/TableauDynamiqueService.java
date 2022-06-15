package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.enums.FavorisRechercheType;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Service qui permet de gérer les tableaux dynamiques
 *
 * @author asatre
 */
public interface TableauDynamiqueService extends Serializable {
    /**
     * Liste les tableaux dynamiques de l'utilisateur courant
     *
     * @param session
     *            {@link CoreSession}
     * @return les tableaux dynamiques du user
     */
    List<DocumentModel> findAll(CoreSession session);

    /**
     * Liste les tableaux dynamiques de l'utilisateur courant qu'il a créé lui-même
     *
     * @param session
     *            {@link CoreSession}
     * @return les tableaux dynamiques du user
     */
    List<DocumentModel> findMine(CoreSession session);

    /**
     * Creation d'un tableau dynamqiue
     *
     * @param session
     * @param type
     * @param query
     * @param idDestinataires
     *            liste d'id de postes/unités structurelles
     * @param intitule
     */
    Map<Integer, String> createTableauDynamique(
        CoreSession session,
        FavorisRechercheType type,
        String query,
        List<String> idDestinataires,
        String intitule
    );

    /**
     * mise a jour d'un tableau dynamique
     *
     * @param session
     * @param tabModel
     * @param query
     * @param idDestinataires
     *            liste d'id de postes/unités structurelles
     * @param intitule
     */
    Map<Integer, String> updateTableauDynamique(
        CoreSession session,
        DocumentModel tabModel,
        String query,
        List<String> idDestinataires,
        String intitule
    );

    /**
     * Suppression d'un tableau dynamqiue
     *
     * @param session
     * @param tabModel
     */
    void deleteTableauDynamique(CoreSession session, DocumentModel tabModel);

    /**
     * Retourne True si le nombre de tableaux dynamiques pour l'utilisateur courant est inférieur au nombre maximal
     * autorisé (via le parametrage de l'application).
     */
    Boolean userIsUnderQuota(CoreSession session, String userName);
}
