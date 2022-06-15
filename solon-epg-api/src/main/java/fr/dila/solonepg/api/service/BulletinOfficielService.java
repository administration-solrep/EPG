package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.enumeration.EpgVecteurPublication;
import fr.dila.ss.api.migration.MigrationLoggerModel;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.SortInfo;

public interface BulletinOfficielService extends Serializable {
    DocumentModelList getAllActiveBulletinOfficielOrdered(CoreSession session);

    DocumentModelList getAllActiveBulletinOfficielOrdered(final CoreSession session, List<SortInfo> sorts);

    /**
     * Creation d'un nouveau bulletin officiel
     *
     * @param session
     *            {@link CoreSession}
     * @param nor
     *            id du ministere ou NOR
     * @param intitule
     *            intitule du bo
     */
    void create(CoreSession session, String nor, String intitule);

    void create(final CoreSession session, final Integer idMinistere, final String intitule);

    void delete(CoreSession session, String identifier);

    /**
     * Renvoie la liste des bulletins officiels actifs pour le ministère du document courant et des bulletins officiels
     * actuellement rattachés au document courant.
     *
     * @param currentDocument
     * @param session
     * @param bulletinIds
     * @return
     */
    List<DocumentModel> getAllBulletinOfficielOrdered(
        DocumentModel currentDocument,
        CoreSession session,
        List<String> bulletinIds
    );

    /**
     * Migre les bulletins officiels de l'écran "Liste des bulletins officiels".
     *
     * @param session
     * @param ancienNorMinistere
     * @param nouveauNorMinistere
     * @param migrationLoggerModel
     */
    void migrerBulletinOfficiel(
        CoreSession session,
        String ancienNorMinistere,
        String nouveauNorMinistere,
        MigrationLoggerModel migrationLoggerModel
    );

    /**
     * Renvoie la liste des bulletins officiels pour un nor ministère donné.
     *
     * @param session
     * @param ministereNor
     * @return la liste des bulletins officiels pour un nor ministère donné.
     */
    List<DocumentModel> getBulletinOfficielQueryFromNor(CoreSession session, String ministereNor);

    /**
     * Permet de créer une ligne vide dans les vecteurs de publication (utilisation dans l'administration des vecteurs)
     *
     * @param session
     * @return Le Vecteur créé
     */
    DocumentModel initVecteurPublication(CoreSession session);

    /**
     * Permet de sauvegarder un vecteurs de publication passés en paramètre,
     *
     * @param session
     * @param vecteurDoc le vecteur à persister
     * @param create {true} si on doit créer le vecteur
     */
    void saveVecteurPublication(CoreSession session, VecteurPublication vecteurDoc, boolean create);

    /**
     * Retourne l'ensemble des vecteurs de l'application, qu'ils soient actifs ou non
     *
     * @param session
     * @return
     */
    List<DocumentModel> getAllVecteurPublication(CoreSession session);

    List<DocumentModel> getAllVecteurPublication(CoreSession session, List<SortInfo> sorts);

    /**
     * Récupère une map des vecteur de publication contenant leur intitulé et leur id
     * @param session
     * @return map des vecteur de publication contenant leur intitulé et leur id
     */
    Map<String, String> getAllVecteurPublicationIdIntitulePair(CoreSession session);

    Map<String, String> getAllBulletinOfficielIdIntitulePair(CoreSession session);

    /**
     * Retourne une liste de vecteurs de publication actifs
     *
     * @param documentManager
     * @return
     */
    List<DocumentModel> getAllActifVecteurPublication(CoreSession documentManager);

    /**
     * @return la liste des pairs {clé,valeur} des vecteurs de publications actifs
     */
    Map<String, String> getAllActifVecteurPublicationIdIntitulePair(CoreSession session);

    /**
     * Retourne vrai si l'intégralité de la liste des vecteurs (représentés par leur UUID) passée en paramètre est
     * active, faux sinon
     *
     * @param documentManager
     * @param name
     *            : libellé du vecteur
     * @return bolean
     */
    boolean isAllVecteurPublicationActif(CoreSession documentManager, List<String> idsVecteur);

    /**
     * Renvoie vrai si tous les bulletins officiels passés sont actifs.
     *
     * @param documentManager
     * @param names
     * @return boolean
     */
    boolean isAllBulletinOfficielActif(CoreSession documentManager, List<String> names);

    /**
     * Retourne le libellé pour le journal officiel (position 1)
     *
     * @param documentManager
     * @return
     */
    String getLibelleForJO(CoreSession documentManager);

    /**
     * Retourne l'identifiant de vecteur correspondant à un vecteur passé en paramètre
     *
     * @param session
     * @param vecteurPublication
     * @return vide si l'élément n'a pas été trouvé, l'identifiant du premier document trouvé sinon
     */
    String getIdForLibelle(CoreSession session, EpgVecteurPublication vecteurPublication);

    /**
     * Retourne l'identifiant de vecteur correspondant à un libellé passé en paramètre
     *
     * @param documentManager
     * @param libelle
     * @return vide si l'élément n'a pas été trouvé, l'identifiant du premier document trouvé sinon
     */
    String getIdForLibelle(CoreSession documentManager, String libelle);

    /**
     * Tente de convertir les identifiants en libellés, si on ne trouve pas le vecteur, on rajoute son identifiant
     * (c'est probablement un bulletinOfficiel)
     *
     * @param documentManager
     * @param idVecteurs
     * @return la liste des libellés
     */
    List<String> convertVecteurIdListToLabels(CoreSession documentManager, List<String> idVecteurs);

    /**
     * Tente de convertir un identifiant en libellé, si on ne trouve pas le vecteur, on rajoute son identifiant (c'est
     * probablement un bulletinOfficiel)
     *
     * @param documentManager
     * @param idVecteur
     * @return le libellé
     */
    String convertVecteurIdToLabel(CoreSession documentManager, String idVecteur);
}
