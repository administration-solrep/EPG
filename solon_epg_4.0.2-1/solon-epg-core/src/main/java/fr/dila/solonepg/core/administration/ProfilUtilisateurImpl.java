package fr.dila.solonepg.core.administration;

import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.api.constant.SolonEpgProfilUtilisateurConstants;
import fr.dila.st.api.constant.STProfilUtilisateurConstants;
import fr.dila.st.core.domain.STDomainObjectImpl;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Profil Utilisateur Implementation.
 *
 * @author antoine Rolin
 *
 */
public class ProfilUtilisateurImpl extends STDomainObjectImpl implements ProfilUtilisateur {
    private static final long serialVersionUID = 713929437623188732L;

    // /////////////////
    // Profil utilisateur protected method
    // ////////////////

    public ProfilUtilisateurImpl(DocumentModel document) {
        super(document);
    }

    // /////////////////
    // Dossier colonnes affichees property
    // ////////////////

    @Override
    public List<String> getIdsDisplayedColumnsEspaceTraitement() {
        return getListStringProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_ID_COLONNE_ESPACE_TRAITEMENT_LIST_PROPERTY
        );
    }

    @Override
    public void setIdsDisplayedColumnsEspaceTraitement(List<String> idsDisplayedColumnsEspaceTraitement) {
        setProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_ID_COLONNE_ESPACE_TRAITEMENT_LIST_PROPERTY,
            idsDisplayedColumnsEspaceTraitement
        );
    }

    @Override
    public List<String> getIdsDisplayedColumnsInstanceFeuilleRoute() {
        return getListStringProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_ID_COLONNE_INSTANCE_FEUILLE_ROUTE_LIST_PROPERTY
        );
    }

    @Override
    public void setIdsDisplayedColumnsInstanceFeuilleRoute(List<String> idsDisplayedColumnsInstanceFeuilleRoute) {
        setProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_ID_COLONNE_INSTANCE_FEUILLE_ROUTE_LIST_PROPERTY,
            idsDisplayedColumnsInstanceFeuilleRoute
        );
    }

    // /////////////////
    // notification property
    // ////////////////

    @Override
    public List<String> getNotificationTypeActes() {
        return getListStringProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_NOTIFICATION_TYPE_ACTES_PROPERTY
        );
    }

    @Override
    public void setNotificationTypeActes(List<String> notificationTypeActes) {
        setProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_NOTIFICATION_TYPE_ACTES_PROPERTY,
            notificationTypeActes
        );
    }

    @Override
    public Boolean getNotificationDossierUrgent() {
        return getBooleanProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_NOTIFICATION_DOSSIER_URGENT_PROPERTY
        );
    }

    @Override
    public void setNotificationDossierUrgent(Boolean notificationDossierUrgent) {
        setProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_NOTIFICATION_DOSSIER_URGENT_PROPERTY,
            notificationDossierUrgent
        );
    }

    @Override
    public Boolean getNotificationRetourSgg() {
        return getBooleanProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_NOTIFICATION_RETOUR_SGG_PROPERTY
        );
    }

    @Override
    public void setNotificationRetourSgg(Boolean notificationRetourSgg) {
        setProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_NOTIFICATION_RETOUR_SGG_PROPERTY,
            notificationRetourSgg
        );
    }

    @Override
    public Boolean getNotificationMesureNominative() {
        return getBooleanProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_NOTIFICATION_MESURE_NOMINATIVE_PROPERTY
        );
    }

    @Override
    public void setNotificationMesureNominative(Boolean notificationMesureNominative) {
        setProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_NOTIFICATION_MESURE_NOMINATIVE_PROPERTY,
            notificationMesureNominative
        );
    }

    @Override
    public Boolean getNotificationMailSiFranchissementAutomatique() {
        return getBooleanProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_NOTIFICATION_MAIL_SI_FRANCHISSEMENT_AUTOMATIQUE_PROPERTY
        );
    }

    @Override
    public void setNotificationMailSiFranchissementAutomatique(Boolean notificationMailSiFranchissementAutomatique) {
        setProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_NOTIFICATION_MAIL_SI_FRANCHISSEMENT_AUTOMATIQUE_PROPERTY,
            notificationMailSiFranchissementAutomatique
        );
    }

    @Override
    public Boolean getPresenceSolonEdit() {
        return getBooleanProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_PRESENCE_SOLON_EDIT
        );
    }

    @Override
    public void setPresenceSolonEdit(Boolean solonEdit) {
        setProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_PRESENCE_SOLON_EDIT,
            solonEdit
        );
    }

    @Override
    public Calendar getDernierChangementMotDePasse() {
        return PropertyUtil.getCalendarProperty(
            document,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            STProfilUtilisateurConstants.PROFIL_UTILISATEUR_DERNIER_CHANGEMENT_MDP_PROPERTY
        );
    }

    @Override
    public void setDernierChangementMotDePasse(Calendar dernierChangementMotDePasse) {
        PropertyUtil.setProperty(
            document,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            STProfilUtilisateurConstants.PROFIL_UTILISATEUR_DERNIER_CHANGEMENT_MDP_PROPERTY,
            dernierChangementMotDePasse
        );
    }

    @Override
    public Boolean getMailSiMajCE() {
        return getBooleanProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_MAIL_SI_MAJ_CE
        );
    }

    @Override
    public void setMailSiMajCE(Boolean majCE) {
        setProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_MAIL_SI_MAJ_CE,
            majCE
        );
    }

    @Override
    public String getDerniersDossiersIntervention() {
        return getStringProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_LISTE_DERNIERS_DOSSIERS_INTERVENTION
        );
    }

    @Override
    public void setDerniersDossiersIntervention(String listeIdsDerniersDossierIntervention) {
        setProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_LISTE_DERNIERS_DOSSIERS_INTERVENTION,
            listeIdsDerniersDossierIntervention
        );
    }

    @Override
    public Integer getNbDossiersVisiblesMax() {
        Object nbDossiersPage = document.getProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_NB_DOSSIERS_VISIBLES_MAX_PROPERTY
        );
        if (nbDossiersPage != null) {
            return Integer.valueOf(nbDossiersPage.toString());
        } else {
            return null;
        }
    }

    @Override
    public void setNbDossiersVisiblesMax(Integer nbDossierPage) {
        setProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_NB_DOSSIERS_VISIBLES_MAX_PROPERTY,
            nbDossierPage
        );
    }

    @Override
    public void setPosteDefaut(Integer idPoste) {
        setProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_POSTE_DEFAUT,
            idPoste
        );
    }

    @Override
    public String getPosteDefaut() {
        return getStringProperty(
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
            SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_POSTE_DEFAUT
        );
    }
}
