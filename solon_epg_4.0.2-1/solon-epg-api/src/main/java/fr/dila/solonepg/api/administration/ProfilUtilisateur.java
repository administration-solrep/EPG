/**
 *
 */
package fr.dila.solonepg.api.administration;

import fr.dila.st.api.domain.STDomainObject;
import fr.dila.st.api.user.STProfilUtilisateur;
import java.io.Serializable;
import java.util.List;

/**
 * Profil Utilisateur Interface for solon epg.
 *
 * @author antoine Rolin
 *
 */
public interface ProfilUtilisateur extends STProfilUtilisateur, STDomainObject, Serializable {
    // /////////////////
    // Profil utilisateur method
    // ////////////////

    // /////////////////
    // Profil utilisateur getter/setter
    // ////////////////

    // /////////////////
    // preferences utilisateur property
    // ////////////////

    // /////////////////
    // Dossier colonnes affichees property
    // ////////////////

    /**
     * Récupère la liste des identifiants des colonnes affichées dans l'espace de traitement
     */
    List<String> getIdsDisplayedColumnsEspaceTraitement();

    /**
     * Définit la liste des identifiants des colonnes affichées dans l'espace de traitement
     *
     * @param idsDisplayedColumnsEspaceTraitement
     */
    void setIdsDisplayedColumnsEspaceTraitement(List<String> idsDisplayedColumnsEspaceTraitement);

    /**
     * Récupère la liste des identifiants des colonnes affichées dans les instances de feuille de route
     */
    List<String> getIdsDisplayedColumnsInstanceFeuilleRoute();

    /**
     * Définit la liste des identifiants des colonnes affichées dans les instances de feuille de route
     *
     * @param idsDisplayedColumnsInstanceFeuilleRoute
     */
    void setIdsDisplayedColumnsInstanceFeuilleRoute(List<String> idsDisplayedColumnsInstanceFeuilleRoute);

    // /////////////
    // notification property
    // ////////////////

    /**
     * Récupère les types d'actes pour lesquels on applique les notifications
     */
    List<String> getNotificationTypeActes();

    /**
     * Définit les types d'actes pour lesquels on applique les notifications
     *
     * @param notificationTypeActes
     */
    void setNotificationTypeActes(List<String> notificationTypeActes);

    Boolean getNotificationDossierUrgent();

    void setNotificationDossierUrgent(Boolean notificationDossierUrgent);

    Boolean getNotificationRetourSgg();

    void setNotificationRetourSgg(Boolean notificationRetourSgg);

    Boolean getNotificationMesureNominative();

    void setNotificationMesureNominative(Boolean notificationMesureNominative);

    Boolean getNotificationMailSiFranchissementAutomatique();

    void setNotificationMailSiFranchissementAutomatique(Boolean notificationMailSiFranchissementAutomatique);

    Boolean getPresenceSolonEdit();

    void setPresenceSolonEdit(Boolean liveEdit);

    Boolean getMailSiMajCE();

    void setMailSiMajCE(Boolean majCE);

    /**
     * Renvoie la liste concacténée des identifiants des derniers dossiers sur lesquels l'utilisateur est intervenu
     */
    String getDerniersDossiersIntervention();

    /**
     * Défini la liste concaténée des identifiants des derniers dossiers sur lesquels l'utilisateur est intervenu
     */
    void setDerniersDossiersIntervention(String listeIdsDerniersDossierIntervention);

    /**
     * Défini le nombre de dossier à afficher dans les contentviews
     */
    void setNbDossiersVisiblesMax(Integer nb);

    /**
     * Renvoie lenombre de dossier à afficher dans les contents views
     */
    Integer getNbDossiersVisiblesMax();

    /**
     * Défini l'id du poste de l'utilisateur par defaut lors de la création d'un dossier
     */
    void setPosteDefaut(Integer idPoste);

    /**
     * Renvoie l'id du poste de l'utilisateur par defaut lors de la création d'un dossier
     */
    String getPosteDefaut();
}
