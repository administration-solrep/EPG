package fr.dila.solonepg.api.service;

import fr.dila.ss.api.migration.MigrationLoggerModel;
import fr.dila.ss.api.service.SSChangementGouvernementService;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Interface du service de gestion du changement de gouvernement.
 *
 * @author arolin
 */
public interface ChangementGouvernementService extends SSChangementGouvernementService {
    void reattribuerMinistereActiviteNormative(
        CoreSession session,
        OrganigrammeNode oldMinistereNode,
        OrganigrammeNode newMinistereNode
    );

    /**
     * Migre les bulletins officiels de l'écran "Liste des bulletins officiels".
     *
     * @param session
     * @param oldNode
     * @param newNode
     */
    void migrerBulletinOfficiel(
        CoreSession session,
        OrganigrammeNode oldNode,
        OrganigrammeNode newNode,
        MigrationLoggerModel migrationLoggerModel
    );

    StringBuilder getDossiersHavingPosteCreator();

    /**
     * Met à jour le champ technique posteCreator utilisé pour la visibilité du dossier dans l'infoCentre de l'espace de suivi.
     *
     * @param session
     * @param oldNode
     * @param newNode
     */
    void updateDossierCreatorPoste(
        CoreSession session,
        OrganigrammeNode oldNode,
        OrganigrammeNode newNode,
        MigrationLoggerModel migrationLoggerModel
    );

    /**
     * Réattribue les NORs des Dossiers d'une nouvelle direction.
     *
     * @param session
     * @param oldMinistereNode
     * @param oldDirectionNode
     * @param newMinistereNode
     * @param newDirectionNode
     */
    void reattribuerNorDossierDirection(
        CoreSession session,
        OrganigrammeNode oldMinistereNode,
        OrganigrammeNode oldDirectionNode,
        OrganigrammeNode newMinistereNode,
        OrganigrammeNode newDirectionNode,
        MigrationLoggerModel migrationLoggerModel,
        Map<String, String> norReattributions
    );

    /**
     * Recherche et renvoie les dossiers à réattribuer.
     * @param session
     * @param hasDirection
     * @param oldMinistereNodeId
     * @param oldDirectionNodeId
     * @return
     */
    List<String> retrieveDossierToReattribuer(
        CoreSession session,
        boolean hasDirection,
        String oldMinistereNodeId,
        String oldDirectionNodeId
    );

    /**
     * Réattribue les NORs des Dossiers d'un nouveau ministère.
     *
     * @param session
     * @param oldNode
     * @param newNode
     * @param norReattributionsPubConj
     *            la liste des réattributions de nor sur les dossiers à
     *            publications conjointes
     */
    void reattribuerNorDossierMinistere(
        CoreSession session,
        OrganigrammeNode oldNode,
        OrganigrammeNode newNode,
        MigrationLoggerModel migrationLoggerModel,
        Map<String, String> norReattributionsPubConj
    );

    /**
     * Migre les mots clés de l'écran "Gestion de l'indexation".
     *
     * @param session
     * @param oldNode
     * @param newNode
     */
    void migrerGestionIndexation(
        CoreSession session,
        OrganigrammeNode oldNode,
        OrganigrammeNode newNode,
        MigrationLoggerModel migrationLoggerModel
    );

    /**
     * Migre les ministères de la table de référence.
     *
     * @param session
     * @param oldNode
     * @param newNode
     */
    void migrerTableReferenceMinistere(
        CoreSession session,
        OrganigrammeNode oldNode,
        OrganigrammeNode newNode,
        MigrationLoggerModel migrationLoggerModel
    );

    /**
     * Migre les directions de la table de référence ( directions PRM ).
     *
     * @param session
     * @param oldNode
     * @param newNode
     */
    void migrerTableReferenceDirection(
        CoreSession session,
        OrganigrammeNode oldMinistereNode,
        OrganigrammeNode oldDirectionNode,
        OrganigrammeNode newMinistereNode,
        OrganigrammeNode newDirectionNode,
        MigrationLoggerModel migrationLoggerModel
    );

    /**
     * Migre les postes de la table de référence ( corbeilles de retour au DAN ).
     *
     * @param session
     * @param oldPosteNode
     * @param newPosteNode
     * @param migrationLoggerModel
     */
    void migrerTableReferencePoste(
        CoreSession session,
        OrganigrammeNode oldPosteNode,
        OrganigrammeNode newPosteNode,
        MigrationLoggerModel migrationLoggerModel
    );
}
