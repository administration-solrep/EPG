package fr.dila.solonepg.ui.enums;

import fr.dila.st.ui.enums.ActionEnum;

public enum EpgActionEnum implements ActionEnum {
    ABANDON_SELECTION_TOOL,
    ACTION_DOSSIER_VALIDER_REFUS_AVIS,
    ACTION_DOSSIER_VALIDER_REFUS_RETOUR_MODIF,
    ACTIVITE_PARLEMENTAIRE_DOWNLOAD,
    ADD_CORBEILLE_DOSSIER_SUIVI,
    ADD_DOCUMENT_FDD,
    ADD_DOCUMENT_PARAPHEUR,
    ADD_DOSSIER_SUIVI,
    ADD_MOTS_CLES,
    ADD_REPERTOIRE_FDD,
    ADD_RUBRIQUE,
    ADD_SIGNATAIRE,
    ADMIN_MENU_ARCHIVAGE_ADAMANT,
    ADMIN_MENU_ARCHIVAGE_INTERMEDIAIRE,
    ADMIN_MENU_ARCHIVAGE_PARAM_ARCH,
    ADMIN_MENU_DOSSIER_ABANDON,
    ADMIN_MENU_DOSSIER_FDD,
    ADMIN_MENU_DOSSIER_PARAPHEUR,
    ADMIN_MENU_DOSSIER_SUPPRESSION,
    ADMIN_MENU_DOSSIER_TABLE_REFERENCE,
    ADMIN_MENU_DOSSIER_VECTEUR,
    ADMIN_MENU_MODELE_CREER_MASSE,
    ADMIN_MENU_MODELE_SQUELETTE,
    ADMIN_MENU_PARAM_PARAM,
    ADMIN_MENU_PARAM_PARAM_INDEXATION,
    ADMIN_MENU_PARAM_PARAM_MOTEUR,
    AJOUTER_URGENCE_DOSSIER,
    COLLER_DOCUMENT_PARAPHEUR,
    CONFIRM_IMPORT_GOUVERNEMENT_EPP,
    COPY_NODE,
    COPY_NODE_MINISTERE_US,
    CREATE_LIST_DEMANDE_EPREUVE,
    CREATE_LIST_DEMANDE_PUBLICATION,
    CREATE_LIST_MISE_EN_SIGNATURE,
    CREATE_LIST_MISE_EN_SIGNATURE_DOSSIER,
    CREATE_LIST_MISE_EN_SIGNATURE_MAILBOX,
    CREATE_LIST_MOTS_CLES,
    DELETE_DOCUMENT_FDD,
    DELETE_DOCUMENT_PARAPHEUR,
    DELETE_REPERTOIRE_FDD,
    DOSSIER_ANNULER_MESURE_NOMINATIVE,
    DOSSIER_REDONNER_MESURE_NOMINATIVE,
    DOSSIER_RETOUR_POUR_MODIFICATION,
    DOSSIER_SUBSTITUER_FDR_FROM_NOR,
    DOSSIER_TERMINE_SANS_PUBLICATION,
    DOSSIER_VALIDER_CORRECTION_AVIS,
    DOSSIER_VALIDER_CORRECTION_BON_A_TIRER,
    DOSSIER_VALIDER_NON_CONCERNE,
    DOSSIER_VALIDER_NOR_ATTRIBUER,
    DOSSIER_VALIDER_REFUS,
    DOSSIER_VALIDER_REFUS_CONTRESEIGN,
    EDITER_PARAPHEURS,
    EDITE_DOCUMENT_FDD,
    EDITE_DOCUMENT_PARAPHEUR,
    EDIT_LIST_MOTS_CLES,
    EDIT_REPERTOIRE_FDD,
    EMPTY_SELECTION_TOOL,
    EXPORT_DOSSIERS_ARCHIVES,
    INJECTION_GOUV_EXPORT,
    NEW_VERSION_DOCUMENT_FDD,
    NEW_VERSION_DOCUMENT_PARAPHEUR,
    PARAPHEUR_EXPORT,
    PASTE_DIRECTION_WITHOUT_USER,
    PASTE_DIRECTION_WITH_USERS,
    PASTE_ENTITY_WITHOUT_USER,
    PASTE_ENTITY_WITH_USERS,
    PASTE_NODE_WITHOUT_USER,
    PASTE_NODE_WITH_USERS,
    PASTE_US_WITHOUT_USER,
    PASTE_US_WITH_USERS,
    PRINT_LIST_BIRT,
    REATTRIBUER_LIST_MOTS_CLES,
    REATTRIBUTION_NOR_SELECTION,
    RECHERCHE_EXPERTE_DOSSIER_EXPORT,
    RECHERCHE_RAPIDE_DOSSIER_EXPORT,
    REDEMARRER_DOSSIER,
    RELANCER_DOSSIER_ABANDONNE,
    REMOVE_DOSSIERS_LIST,
    REMOVE_LIST_MOTS_CLES,
    REMOVE_MOTS_CLES,
    REMOVE_RUBRIQUE,
    REMOVE_SIGNATAIRE,
    RENAME_DOCUMENT_PARAPHEUR,
    RETIRER_DOSSIERS,
    SAISINE_RECTIFICATIVE,
    SQUELETTE_MODIFIER,
    SQUELETTE_RETOUR,
    SQUELETTE_SUPPRIMER,
    SQUELETTE_UNLOCK,
    SQUELETTE_VALIDER,
    SUBSTITUTION_MASSE_SELECTION_TOOL,
    SUBSTITUTION_POSTE_SELECTION,
    SUIVI_GESTION_LISTES,
    SUPPRIMER_DOSSIERS,
    SUPPRIMER_MASSE_SELECTION_TOOL,
    SUPPRIMER_URGENCE_DOSSIER,
    TAB_DOSSIERS_SUPPRIMER_CONSULTATION,
    TAB_DOSSIERS_SUPPRIMER_SUIVI,
    TAB_DYN_DOSSIER_EXPORT,
    TAB_SQUELETTE_CREATION,
    TAB_SQUELETTE_DUPLIQUER,
    TAB_SQUELETTE_MODIFICATION,
    TAB_SQUELETTE_SUPPRIMER,
    TRAITER_SERIE_LIST,
    TRANSFERT_SELECTION,
    UPDATE_STEP_SQUELETTE,
    VALIDATE_STEP_SELECTION_TOOL,
    VALIDER_DOSSIERS,
    VIEW_HISTORY_DOCUMENT_FDD,
    VIEW_HISTORY_DOCUMENT_PARAPHEUR;

    @Override
    public String getName() {
        return name();
    }
}