package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.api.service.SSProfilUtilisateurService;
import fr.dila.st.api.vocabulary.VocabularyEntry;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Service qui permet de gérer les profils utilisateurs.
 *
 * @author arolin
 */
public interface ProfilUtilisateurService extends SSProfilUtilisateurService<ProfilUtilisateur> {
    // /////////////////
    // vérifications de l'envoi de mail de notification
    // ////////////////

    /**
     * Renvoient les notifications mails aux utilisateurs lors de la validation d'une étape d'un dossier.
     *
     * @param routeStep
     *            etape courante du dossier
     * @param dossierDoc
     *            documentModel du dossier
     * @param session
     */
    void checkAndSendMailNotificationValidationEtape(
        SSRouteStep routeStep,
        DocumentModel dossierDoc,
        CoreSession session
    );

    // /////////////////
    // méthode sur l'affichage des colonnes
    // ////////////////

    /**
     * Retourne la liste des colonnes autorisés pour une liste de dossier
     *
     * @param session
     * @return liste des colonnes autorisées pour une liste de dossier
     */
    List<String> getAllowedDossierColumn(CoreSession session);

    /**
     * Retourne la liste des colonnes autorisées pour les instances de feuille de route
     *
     * @param session
     * @return liste des colonne autorisés pour les isntance s de feuille de route
     */
    List<String> getAllowedFeuilleRouteInstanceColumn(CoreSession session);

    /**
     * Retourn la liste des colonnes affichées dans l'espace de traitement de l'utilisateur
     *
     * @param session
     * @return liste des colonnes affichées dans l'espace de traitement de l'utilisateur
     */
    List<String> getUserEspaceTraitementColumn(CoreSession session);

    /**
     * Retourne la liste des colonnes affichées dans les instances de feuille de route de l'utilisateur
     *
     * @param session
     * @return liste des colonne affichées dans les instances de feuille de route de l'utilisateur
     */
    List<String> getUserFeuilleRouteInstanceColumn(CoreSession session);

    /**
     * Retourne la liste des colonnes autorisés pour l'espace de traitement
     *
     * @param session
     * @return liste des colonne autorisés pour l'espace de traitement sous forme de liste de VocabularyEntry
     */
    List<VocabularyEntry> getVocEntryAllowedEspaceTraitementColumn(CoreSession session);

    /**
     * Retourne la liste des colonnes autorisés pour l'espace de traitement
     *
     * @param session
     * @return liste des colonne autorisés pour l'espace de traitement sous forme de liste de VocabularyEntry
     */
    List<VocabularyEntry> getVocEntryAllAllowedEspaceTraitementColumn(CoreSession session);

    /**
     * Retourne la liste des colonnes autorisés pour les instances de feuille de route
     *
     * @param session
     * @return liste des colonne autorisés pour les instances de feuille de route sous forme de liste de VocabularyEntry
     */
    List<VocabularyEntry> getVocEntryAllowedFeuilleRouteInstanceColumn(CoreSession session);

    /**
     * Retourn la liste des colonnes affichées dans l'espace de traitement de l'utilisateur
     *
     * @param session
     * @return liste des colonnes affichées dans l'espace de traitement de l'utilisateur sous forme de liste de
     *         VocabularyEntry
     */
    List<VocabularyEntry> getVocEntryUserEspaceTraitementColumn(CoreSession session);

    /**
     * Retourne la liste des colonnes affichées dans les instances de feuille de route de l'utilisateur
     *
     * @param session
     * @return liste des colonne affichées dans les instances de feuille de route de l'utilisateur sous forme de liste
     *         de VocabularyEntry
     */
    List<VocabularyEntry> getVocEntryUserFeuilleRouteInstanceColumn(CoreSession session);

    /**
     * Retourne le nombre de dossier à afficher dans les corbeilles de l'utilisateur
     *
     */
    Long getUtilisateurPageSize(CoreSession session);

    /**
     * Instancie la liste par colonnes par défaut de l'espace de traitement et des instances de feuille de route du
     * document profilUtilisateur.
     *
     * @param profilUtilisateur
     *            docModel de type ProfilUtilisateur
     * @return
     */
    DocumentModel initDefaultAvailablesColumnsNames(DocumentModel profilUtilisateur, CoreSession session);

    /**
     * Initialise les valeurs par défaut du profil utilisateur document profilUtilisateur.
     *
     * @param profilUtilisateur
     *            docModel de type ProfilUtilisateur
     * @return
     */
    DocumentModel initDefaultvalues(DocumentModel profilUtilisateur, CoreSession session);

    void removeDossierFromListDerniersDossierIntervention(CoreSession session, String idDossier);
}
