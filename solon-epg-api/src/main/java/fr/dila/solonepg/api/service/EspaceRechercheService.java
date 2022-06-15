package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.criteria.EpgFeuilleRouteCriteria;
import fr.dila.solonepg.api.enums.FavorisRechercheType;
import fr.dila.solonepg.api.recherche.ResultatConsulte;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Service permettant de gérer les favoris de recherche, les favoris de consultation et les
 * derniers résultats consultés.
 *
 * @author asatre
 * @author jtremeaux
 */
public interface EspaceRechercheService extends Serializable {
    enum DocKind {
        FEUILLE_ROUTE,
        DOSSIER,
        USER
    }

    /**
     * Récupère le document favori de consultation de l'utilisateur
     *
     * @param userworkspacePath Workspace de l'utilisateur
     * @param session Session
     * @return Document favori de consultation
     */
    DocumentModel getFavorisConsultation(final String userworkspacePath, final CoreSession session);

    /**
     * Crée un nouveau document favori de recherche (uniquement en mémoire).
     *
     * @param session Session
     * @param type Type de favori de recherche
     * @return Nouveau document favori de recherche
     */
    DocumentModel createBareFavorisRecherche(CoreSession session, FavorisRechercheType type);

    /**
     * Recherche le document résultat consultés de l'utilisateur. Le crée si besoin
     *
     * @param session
     *            Session
     * @param userworkspacePath
     *            Workspace de l'utilisateur
     * @return Document résultat consultés de l'utilisateur
     */
    ResultatConsulte getResultatConsulte(final CoreSession session, final String userworkspacePath);

    /**
     * Ajoute un document à la liste des derniers résultats consultés.
     * Chaque document est ajouté une seule fois à la liste, et la liste contient au plus un nombre
     * d'éléments paramétrables.
     *
     * @param session Session
     * @param userworkspacePath Workspace de l'utilisateur
     * @param docId id du document à ajouter aux favoris
     * @param kind type de resultat
     */
    void addDocumentModelToResultatsConsultes(
        CoreSession session,
        String userworkspacePath,
        String docId,
        DocKind kind
    );

    /**
     * Ajoute une liste de documents aux favoris de consultation.
     *
     * @param session Session
     * @param userworkspacePath Workspace de l'utilisateur
     * @param documentModelList Liste de documents à ajouter aux favoris
     * @return Nombre de documents ajoutés aux favoris de consultation
     */
    int addToFavorisConsultation(CoreSession session, String userworkspacePath, List<DocumentModel> documentModelList);

    /**
     * Reture une liste de documents des favoris de consultation.
     *
     * @param session Session
     * @param userworkspacePath Workspace de l'utilisateur
     * @param docsList Liste de documents à retirer des favoris
     */
    void removeFromFavorisConsultation(CoreSession session, String userworkspacePath, List<DocumentModel> docsList);

    List<DocumentModel> addToFavorisRecherche(
        CoreSession session,
        List<String> postes,
        String intitule,
        String query,
        FavorisRechercheType type
    );

    /**
     * Crée un favori de recherche dans les espaces de travail personnels de tous les
     * utilisateurs de tous les postes fournis en paramètre.
     *
     * @param session Session
     * @param posteList Postes de diffusion
     * @param favorisRechercheDoc Document du favori de recherche à créer
     * @return Liste des utilisateurs en erreur
     */
    List<DocumentModel> addToFavorisRecherche(
        CoreSession session,
        final List<String> posteList,
        final DocumentModel favorisRechercheDoc
    );

    void removeAllDossierFromFavorisConsultation(CoreSession session, String userworkspacePath);

    void removeAllDossierFromDerniersResultatsConsultes(CoreSession session, String userworkspacePath);

    void removeModeleFromDerniersResultatsConsultes(
        CoreSession session,
        String userworkspacePath,
        Set<String> modeleToRemove
    );

    void removeModeleFromFavorisConsultation(CoreSession session, String userworkspacePath, Set<String> modeleToRemove);

    void removeDossierFromFavorisConsultation(
        CoreSession session,
        String userworkspacePath,
        Set<String> dossierToRemove
    );

    void removeDossierFromDerniersResultatsConsultes(
        CoreSession session,
        String userworkspacePath,
        Set<String> dossierToRemove
    );

    void removeUserFromDerniersResultatsConsultes(
        CoreSession coreSession,
        String userworkspacePath,
        Set<String> userToRemove
    );

    void removeUserFromFavorisConsultations(
        CoreSession coreSession,
        String userworkspacePath,
        Set<String> userToRemove
    );

    /**
     * Retourne les critères de recherche correspondant au favori de recherche de feuille de route.
     *
     * @return Critères de recherche de feuille de route
     */
    EpgFeuilleRouteCriteria getFeuilleRouteCriteria(DocumentModel favorisRechercheDoc);

    /**
     * Renvoie true si l'utilisateur peut utiliser la recherche libre, false
     * sinon.
     *
     * @param session
     * @return
     */
    boolean canUseRechercheLibre(CoreSession session);
}
