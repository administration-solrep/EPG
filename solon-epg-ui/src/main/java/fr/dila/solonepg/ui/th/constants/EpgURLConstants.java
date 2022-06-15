package fr.dila.solonepg.ui.th.constants;

import fr.dila.st.ui.th.constants.STURLConstants;

public class EpgURLConstants {
    public static final String BASE_URL_FDR = "/recherche/fdr";

    public static final String ADMIN_ARCHIVAGE_DEFINITIF = "/admin/archivage/definitif";
    public static final String AJAX_ADMIN_ARCHIVAGE_DEFINITIF = STURLConstants.AJAX_PREFIX + ADMIN_ARCHIVAGE_DEFINITIF;
    public static final String AJAX_ARCHIVAGE_DEFINITIF = "/ajax/archivage/definitif";

    public static final String ADMIN_ARCHIVAGE_INTERMEDIAIRE = "/admin/archivage/intermediaire";
    public static final String AJAX_ARCHIVAGE_INTERMEDIAIRE = "/ajax/archivage/intermediaire";

    public static final String ADMIN_DOSSIERS_ABANDON = "/admin/dossiers/abandon";
    public static final String AJAX_ADMIN_DOSSIERS_ABANDON = STURLConstants.AJAX_PREFIX + ADMIN_DOSSIERS_ABANDON;

    public static final String RECHERCHE_FAVORIS_UTILISATEURS = "/recherche/favoris/utilisateurs";
    public static final String AJAX_RECHERCHE_FAVORIS_UTILISATEURS =
        STURLConstants.AJAX_PREFIX + RECHERCHE_FAVORIS_UTILISATEURS;
    public static final String RECHERCHE_FAVORIS_UTILISATEUR = RECHERCHE_FAVORIS_UTILISATEURS + "/utilisateur";

    public static final String URL_AJAX_DERNIERS_RESULTATS = "/ajax/recherche/derniers/resultats/consultes";
    public static final String URL_AJAX_DOSSIER_FILTRER = "/ajax/dossier/filtrer";
    public static final String URL_AJAX_DOSSIER_RECHERCHE = "/ajax/dossier/recherche";
    public static final String URL_AJAX_FAVORI_RECHERCHE = "/ajax/recherche/favoris/executer/resultats";
    public static final String URL_AJAX_PUBLICATION_MINISTERIELLE_TABLE =
        "/ajax/admin/dossiers/vecteurs/publication/liste";
    public static final String URL_AJAX_SQUELETTES = "/ajax/fdr/squelettes";
    public static final String URL_AJAX_SAISINE_RECTIFICATIVE = "/ajax/saisine/rectificative";
    public static final String URL_AJAX_SUIVI_LIBRE_ACTIVITE_PARLEMENTAIRE = "/ajax/suiviLibre/activiteParlementaire";
    public static final String URL_AJAX_SUIVI_LISTE = "/ajax/suivi/liste";
    public static final String URL_AJAX_TD = "/ajax/suivi/tableaux/resultats";
    public static final String URL_AJAX_VECTEUR_PUBLICATION_TABLE = "/ajax/admin/dossiers/vecteurs/liste";
    public static final String URL_ALERTES = "/suivi/alertes/%s#main_content";
    public static final String URL_ANNUAIRE = "/recherche/users/liste";
    public static final String URL_DOSSIER_CREATION = "/travail/dossier/creation";
    public static final String URL_DOSSIER_SAISINE_RECTIFICATIVE = "/dossier/%s/saisine/rectificative";
    public static final String URL_INDEXATION_ADD_SIGNATAIRE = "/admin/dossiers/table/reference/signataire/ajouter";
    public static final String URL_INDEXATION_DELETE_SIGNATAIRE =
        "/admin/dossiers/table/reference/signataire/supprimer";
    public static final String URL_INDEXATION = "/admin/param/indexation";
    public static final String URL_INDEXATION_ADD_RUBRIQUE = URL_INDEXATION + "/rubrique/ajouter";
    public static final String URL_INDEXATION_DELETE_RUBRIQUE = URL_INDEXATION + "/rubrique/supprimer";
    public static final String URL_INDEXATION_CREATE_LIST = URL_INDEXATION + "/liste/creer";
    public static final String URL_INDEXATION_LIST = URL_INDEXATION + "/liste/";
    public static final String URL_INDEXATION_INIT_CREATE_LIST = URL_INDEXATION_LIST + "creer/init";
    public static final String URL_MGPP_COURRIERS = "/admin/mgpp/courriers/";
    public static final String URL_MGPP_FICHE_CONSULT = "/mgpp/dossierSimple/%s/fiche#main_content";
    public static final String URL_MOTEUR_AJAX = "/ajax/admin/paramMoteur";
    public static final String URL_MOTEUR_LIST = "/admin/param/moteur";
    public static final String URL_MOTEUR_CONSULT = URL_MOTEUR_LIST + "/%s";
    public static final String URL_MOTEUR_EDIT = URL_MOTEUR_LIST + "/%s/editer";
    public static final String URL_PAGE_DERNIERS_RESULTATS = "/recherche/derniers/resultats/%s";
    public static final String URL_PAGE_FAVORI_RECHERCHE = "/recherche/favoris/executer/%s";
    public static final String URL_PAGE_TD = "/suivi/tableaux/executer/%s";
    public static final String URL_SEARCH_USER = "/recherche/user/rechercher";
    public static final String URL_SQUELETTES = "/admin/fdr/squelettes";
    public static final String URL_SUIVI_LIBRE_ACTIVITE_PARLEMENTAIRE = "/suiviLibre/activiteParlementaire";
    public static final String URL_SUIVI_LIBRE_APPLICATION_LOIS = "/suiviLibre/applicationLois";
    public static final String URL_SUIVI_LIBRE_APPLICATION_ORDONNANCES = "/suiviLibre/applicationOrdonnances";
    public static final String URL_SUIVI_LIBRE_DEPOT_AMENDEMENTS = "/suiviLibre/depotAmendements";
    public static final String URL_SUIVI_LIBRE_SUIVI_ORDONNANCES = "/suiviLibre/suiviOrdonnances";

    public static final String URL_ARCHIVAGE_ADAMANT = "/admin/archivage/adamant";
    public static final String URL_PARAMETRE_ADAMANT = "/admin/archivage/parametreAdamant";
    public static final String URL_PARAM_PARAM = "/admin/param/param";
    public static final String URL_PAN_PARAM = "/admin/param/pan";
    public static final String URL_ADMIN_DOSSIERS_FDD = "/admin/dossiers/fdd";
    public static final String URL_ADMIN_DOSSIERS_PARAPHEUR = "/admin/dossiers/parapheur";
    public static final String URL_ADMIN_DOSSIERS_TABLE_REFERENCE = "/admin/dossiers/table/reference";
    public static final String URL_SUIVI_LISTE = "/suivi/liste";
    public static final String URL_SUIVI_LISTES_GESTION = "/suivi/listes/gestion";
    public static final String URL_VECTEUR_PUBLICATION = "/admin/dossiers/vecteurs#main_content";
    public static final String URL_VALIDER_REFUS = "valider/refus";
}
