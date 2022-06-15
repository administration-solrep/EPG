package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.migration.MigrationLoggerModel;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Service permettant de gérer un catalogue de modèles de feuille de route de l'application SOLON EPG.
 *
 * @author jtremeaux
 */
public interface FeuilleRouteModelService extends fr.dila.ss.api.service.FeuilleRouteModelService {
    /**
     * Recherche et retourne le modèle de feuille de route par défaut pour initialiser un dossier.
     *
     * @param session Session
     * @param typeActeId Identifiant technique du type d'acte (obligatoire)
     * @param ministereId Identifiant technique du ministère (obligatoire)
     * @param directionId Identifiant technique de la direction (obligatoire)
     * @return Modèle de feuille de route par défaut
     */
    SSFeuilleRoute getDefaultRoute(CoreSession session, String typeActeId, String ministereId, String directionId);

    /**
     * Migre les modèles de feuille de route d'un ministère ou une direction vers un autre ministère / direction.
     *
     * @param session
     * @param oldNode
     * @param newNode
     * @param migrationLoggerModel
     */
    @Override
    void migrerModeleFdrMinistere(
        CoreSession session,
        EntiteNode oldNode,
        EntiteNode newNode,
        MigrationLoggerModel migrationLoggerModel,
        Boolean desactivateModelFdr
    );

    /**
     * Renvoie les modèles de feuille de route rattaché à l'entité et à la direction si <b>hasDirection</b> est vrai qui ne sont pas à l'état deleted
     *
     * @param session
     * @param ministereId
     * @param directionId
     * @param hasDirection
     * @return les modèles de feuille de route rattaché à l'entité et à la direction si <b>hasDirection</b> est vrai.
     */
    List<DocumentModel> getNonDeletedFdrModelFromMinistereAndDirection(
        CoreSession session,
        String ministereId,
        String directionId,
        boolean hasDirection
    );

    /**
     * Retourne le répertoire racine des squelettes de feuilles de route.
     *
     * @return Répertoire des squelettes de feuilles de route
     */
    DocumentModel getFeuilleRouteSqueletteFolder(CoreSession session);

    /**
     * FEV525 Crée les modèles d'un ministère en utilisant les squelettes actifs.
     *
     * @return : le nombre de modèles créés
     */
    Integer creeModelesViaSquelette(
        CoreSession session,
        String ministereId,
        String bdcId,
        String cdmId,
        String scdmId,
        String cpmId
    );

    /**
     * Indique s'il existe déjà un squelette validé avec le type d'acte indiqué.
     *
     * @return true si un tel squelette est trouvé.
     */
    boolean existsSqueletteWithTypeActe(CoreSession session, String typeActeId);

    /**
     * Retourne la liste des squelettes actifs.
     *
     */
    List<DocumentModel> getAllValidatedSquelette(CoreSession session);

    /**
     * FEV525
     * Genere le titre d'un modele de feuille de route à partir de son ministere, poste, type d'acte et titre libre.
     * Le format est "*trigramme_ministère* - *lettre_direction* - *type_acte* - intitulé libre"
     * Seul l'intitulé libre est obligatoire.
     */
    String creeTitleModeleFDR(String idMinistere, String idDirection, String idTypeActe, String titreLibre);

    /**
     * Recherche et retourne le modèle de feuille de route par défaut global.
     *
     * @param session Session
     * @return Modèle de feuille de route par défaut global
     */
    FeuilleRoute getDefaultRouteGlobal(CoreSession session);

    /*
     * Vérifie qui le modèle par defaut est bien unique pour sa combinaison ministère direction type acte
     */
    boolean isModeleDefautUnique(CoreSession session, SolonEpgFeuilleRoute route);
}
