package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.st.api.vocabulary.VocabularyEntry;
import java.io.Serializable;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Service pour les tables de référence
 *
 */
public interface TableReferenceService extends Serializable {
    DocumentModel getTableReference(CoreSession session);

    /**
     * Retourne l'identifiant du ministère PRM.
     *
     * @param session
     * @return l'identifiant du ministère PRM.
     */
    String getMinisterePrm(CoreSession session);

    /**
     * Récupère les identifiants et les nor (label) des directions du premier ministre qui sont peuvent être
     * sélectionnées par tous les utilisateurs lors de la création du dossier ( RG-DOS-CRE-19 )
     *
     * @param session
     * @return les nor des directions du premier ministre qui sont peuvent être sélectionnées par tous les utilisateurs
     *         lors de la création du dossier ( RG-DOS-CRE-19 )
     */
    List<VocabularyEntry> getNorDirectionsForCreation(CoreSession session);

    List<VocabularyEntry> getRetoursDAN(CoreSession session);

    /**
     * Récupère la liste des modes de parution existants en base de données
     *
     * @param documentManager
     * @return
     */
    List<DocumentModel> getModesParutionList(CoreSession documentManager);

    /**
     * Récupère la liste des modes de parution existants en base de données et qui sont actifs (dont la date de début
     * est antérieure et la date de fin postérieure à la date du jour)
     *
     * @param documentManager
     * @return
     */
    List<DocumentModel> getModesParutionActifsList(CoreSession documentManager);

    DocumentModel initModeParution(CoreSession session);

    /**
     * Créé un nouveau documentModel pour un mode de parution
     *
     * @param session
     * @param mode
     * @return le document créé
     */
    DocumentModel createModeParution(CoreSession session, ModeParution mode);

    /**
     * Met à jour un documentModel pour un mode de parution
     *
     * @param session
     * @param mode
     * @return le document modifié
     */
    DocumentModel updateModeParution(CoreSession session, ModeParution mode);

    /**
     * Retourne le libellé du mode de parution
     * @param session session
     * @param id id du mode
     * @return le libellé du mode de parution
     */
    String getModeParutionLabel(CoreSession session, String id);

    void deleteModeParution(CoreSession session, ModeParution mode);

    /**
     * Récupère la valeur du champ identifiant poste publication
     *
     * @param session
     * @return
     */
    String getPostePublicationId(CoreSession session);

    /**
     * Récupère la valeur du champ identifiant poste DAN
     *
     * @param session
     * @return
     */
    String getPosteDanId(CoreSession session);

    /**
     * Récupère la valeur du champ identifiant poste DAN
     *
     * @param session
     * @return
     */
    String getPosteDanIdAvisRectificatifCE(CoreSession session);

    /**
     * Récupère les modes de parution qui ont pour nom celui passé en paramètres
     *
     * @param session
     * @param name
     * @return
     */
    List<String> getModesParutionListFromName(CoreSession session, String name);
}
