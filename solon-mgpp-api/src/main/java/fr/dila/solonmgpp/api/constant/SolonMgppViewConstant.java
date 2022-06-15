package fr.dila.solonmgpp.api.constant;

import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;

/**
 * Vues de l'application SOLON MGPP.
 *
 * @author asatre
 */
public final class SolonMgppViewConstant {
    /**
     * Vue des corbeille (messages)
     */
    public static final String VIEW_CORBEILLE_MESSAGE = "view_corbeille_message";

    /**
     * Vue des corbeille (dossiers)
     */
    public static final String VIEW_CORBEILLE_DOSSIER = "view_corbeille_dossier";

    /**
     * Vue de creation d'un evenement
     */
    public static final String VIEW_CREATE_EVENEMENT = "create_metadonnee_evenement";

    /**
     * Vue rectifier un événement
     */
    public static final String VIEW_RECTIFIER_EVENEMENT = "rectifier_metadonnee_evenement";

    /**
     * Vue completer un événement
     */
    public static final String VIEW_COMPLETER_EVENEMENT = "completer_metadonnee_evenement";

    /**
     * Vue modifier un événement
     */
    public static final String VIEW_MODIFIER_EVENEMENT = "modifier_metadonnee_evenement";

    /**
     * Vue de creation d'une fiche d'une {@link FichePresentationOEP}
     */
    public static final String VIEW_CREATE_OEP = "view_create_oep";

    /**
     * Vue de creation d'une fiche d'une {@link FichePresentationAVI}
     */
    public static final String VIEW_CREATE_AVI = "view_create_avi";

    /**
     * Vue de creation d'une fiche d'une {@link FichePresentationDOC}
     */
    public static final String VIEW_CREATE_DOC = "view_create_doc";

    /**
     * Vue de creation d'une fiche d'une {@link FichePresentationAUD}
     */
    public static final String VIEW_CREATE_AUD = "view_create_aud";

    /**
     * Vue de creation d'une fiche d'une {@link FichePresentationIE}
     */
    public static final String VIEW_CREATE_IE = "view_create_ie";

    /**
     * Vue de creation d'une fiche d'une {@link FichePresentation341}
     */
    public static final String VIEW_CREATE_341 = "view_create_341";

    /**
     * Vue de listing des {@link FichePresentationOEP}
     */
    public static final String VIEW_LISTE_OEP = "view_liste_oep";

    /**
     * Vue de listing des {@link FichePresentationAVI}
     */
    public static final String VIEW_LISTE_AVI = "view_liste_avi";

    /**
     * Vue de listing des {@link FicheLoi}
     */
    public static final String VIEW_LISTE_FICHE_LOI = "view_liste_fiche_loi";

    /**
     * Vue de listing des {@link FichePresentationDR} sans les article 67
     */
    public static final String VIEW_LISTE_DR = "view_liste_dr";

    /**
     * Vue de listing des {@link FichePresentationDR} article 67
     */
    public static final String VIEW_LISTE_DR_67 = "view_liste_dr_67";

    /**
     * Vue de listing des {@link FichePresentationDecret}
     */
    public static final String VIEW_LISTE_DECRET = "view_liste_decret";

    /**
     * Vue de listing des {@link FichePresentationIE}
     */
    public static final String VIEW_LISTE_IE = "view_liste_ie";

    /**
     * Vue de listing des {@link FichePresentation341}
     */
    public static final String VIEW_LISTE_341 = "view_liste_341";

    /**
     * Vue de listing des {@link FichePresentationDPG}
     */
    public static final String VIEW_LISTE_DPG = "view_liste_dpg";

    /**
     * Vue de listing des {@link FichePresentationSD}
     */
    public static final String VIEW_LISTE_SD = "view_liste_sd";

    /**
     * Vue de listing des {@link FichePresentationJSS}
     */
    public static final String VIEW_LISTE_JSS = "view_liste_jss";

    /**
     * Vue de listing des {@link FichePresentationDOC}
     */
    public static final String VIEW_LISTE_DOC = "view_liste_doc";

    /**
     * Vue de listing des {@link FichePresentationAUD}
     */
    public static final String VIEW_LISTE_AUD = "view_liste_aud";

    /**
     * Vue de details des {@link FichePresentationOEP}
     */
    public static final String VIEW_DETAILS_OEP = "view_details_oep";

    /**
     * Vue de details des {@link FichePresentationAVI}
     */
    public static final String VIEW_DETAILS_AVI = "view_details_avi";

    /**
     * Vue de details des {@link FichePresentationDecret}
     */
    public static final String VIEW_DETAILS_DECRET = "view_details_decret";

    /**
     * Vue de details des {@link FichePresentationDOC}
     */
    public static final String VIEW_DETAILS_DOC = "view_details_doc";

    /**
     * Vue de details des {@link FichePresentationDR}
     */
    public static final String VIEW_DETAILS_DR = "view_details_dr";

    /**
     * Vue de details des {@link FichePresentationAUD}
     */
    public static final String VIEW_DETAILS_AUD = "view_details_aud";

    public static final String VIEW_DETAILS_DPG = "view_details_dpg";

    public static final String VIEW_DETAILS_SD = "view_details_sd";

    public static final String VIEW_DETAILS_JSS = "view_details_jss";

    /**
     * Vue de suivi
     */
    public static final String VIEW_SUIVI = "view_suivi";

    public static final String CATEGORY_RAPORT = "MGPP_RAPORT_CATEGORY";

    public static final String CATEGORY_NOMINATION = "MGPP_NOMINATION_CATEGORY";

    public static final String CATEGORY_ORGANISATION = "MGPP_ORGANISATION_CATEGORY";

    public static final String CATEGORY_DECLARATION = "MGPP_DECLARATION_CATEGORY";

    public static final String CATEGORY_RAPORT_ID = "mgpp_raport_category_id";

    public static final String CATEGORY_NOMINATION_ID = "mgpp_nomination_category_id";

    public static final String CATEGORY_ORGANISATION_ID = "mgpp_organisation_category_id";

    public static final String CATEGORY_DECLARATION_ID = "mgpp_declaration_category_id";

    /**
     * Vue de recherche
     */
    public static final String VIEW_RECHERCHE = "view_espace_mgpp_recherche";
    public static final String VIEW_RECHERCHE_RESULTAT = "view_recherche_mgpp_resultats_fiche";
    public static final String VIEW_RECHERCHE_RESULTAT_LOI = VIEW_RECHERCHE_RESULTAT + "_loi";
    public static final String VIEW_RECHERCHE_RESULTAT_OEP = VIEW_RECHERCHE_RESULTAT + "_oep";
    public static final String VIEW_RECHERCHE_RESULTAT_AVI = VIEW_RECHERCHE_RESULTAT + "_avi";
    public static final String VIEW_RECHERCHE_RESULTAT_DR = VIEW_RECHERCHE_RESULTAT + "_dr";
    public static final String VIEW_RECHERCHE_RESULTAT_DPG = VIEW_RECHERCHE_RESULTAT + "_dpg";
    public static final String VIEW_RECHERCHE_RESULTAT_SD = VIEW_RECHERCHE_RESULTAT + "_sd";
    public static final String VIEW_RECHERCHE_RESULTAT_JSS = VIEW_RECHERCHE_RESULTAT + "_jss";
    public static final String VIEW_RECHERCHE_RESULTAT_DOC = VIEW_RECHERCHE_RESULTAT + "_doc";
    public static final String VIEW_RECHERCHE_RESULTAT_AUD = VIEW_RECHERCHE_RESULTAT + "_aud";
    public static final String VIEW_RECHERCHE_RESULTAT_DECRET = VIEW_RECHERCHE_RESULTAT + "_decret";
    public static final String VIEW_RECHERCHE_RESULTAT_341 = VIEW_RECHERCHE_RESULTAT + "_341";
    public static final String VIEW_RECHERCHE_RESULTAT_IE = VIEW_RECHERCHE_RESULTAT + "_ie";

    public static final String ESPACE_PARAMETRAGE_MGPP = "view_parametrage_mgpp";

    public static final String TABLE_REFERENCE_EPP = "view_table_reference_epp";
    public static final String TABLE_REFERENCE_GOUVERNEMENT_EPP = "table_reference_gouvernement_epp";
    public static final String TABLE_REFERENCE_MINISTERE_EPP = "table_reference_ministere_epp";
    public static final String TABLE_REFERENCE_MANDAT_EPP = "table_reference_mandat_epp";

    /**
     * Recherche avancée
     */
    public static final String VIEW_RECHERCHE_AVANCEE = "view_recherche_avancee";

    /**
     * Recherche avancée résultat
     */
    public static final String VIEW_RECHERCHE_AVANCEE_RESULTAT = "view_recherche_avancee_resultat";

    public static final String OEP_FICHES_PUBLIEES_PREFIX = "oep_diffusion_";

    private SolonMgppViewConstant() {
        // private default constructor
    }
}
