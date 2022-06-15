package fr.dila.solonepg.api.logging.enumerationCodes;

import fr.dila.st.api.logging.STLogEnum;
import fr.dila.st.api.logging.enumerationCodes.STCodes;
import fr.dila.st.api.logging.enumerationCodes.STObjetsEnum;
import fr.dila.st.api.logging.enumerationCodes.STPorteesEnum;
import fr.dila.st.api.logging.enumerationCodes.STTypesEnum;

/**
 * Enumération des logs info codifiés de l'EPG Code : 000_000_000 <br />
 *
 *
 */
public enum EpgLogEnumImpl implements STLogEnum {
    // **************************************** SUPPRESSION DOCUMENTS **********************************************
    /**
     * DELETE NOTIF : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#NOTIF}
     */
    DEL_NOTIF_TEC(STTypesEnum.DELETE, STPorteesEnum.TECHNIQUE, EpgObjetsEnum.NOTIF, "Suppression notification"),
    /**
     * DELETE MESURE : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#MESURE}
     */
    DEL_MESURE_TEC(STTypesEnum.DELETE, STPorteesEnum.TECHNIQUE, EpgObjetsEnum.MESURE, "Suppression mesure applicative"),
    /**
     * DELETE ALERTE EPG : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#ALERTE_EPG}
     */
    DEL_ALERT_EPG_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.ALERTE_EPG,
        "Suppression alerte E.P.G."
    ),
    /**
     * DELETE ALERTE EPG : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#ALERTE_EPG}
     */
    DEL_ALERT_EPG_FONC(
        STTypesEnum.DELETE,
        STPorteesEnum.FONCTIONNELLE,
        EpgObjetsEnum.ALERTE_EPG,
        "Suppression fonctionnelle alerte E.P.G."
    ),
    /**
     * DELETE AMPLIATION : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#AMPLIATION}
     */
    DEL_AMPLIATION_TEC(STTypesEnum.DELETE, STPorteesEnum.TECHNIQUE, EpgObjetsEnum.AMPLIATION, "Suppression ampliation"),
    /**
     * DELETE REPERTOIRE FDD : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#FDD}
     */
    DEL_REP_FDD_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.REP_FDD,
        "Suppression répertoire fond de dossier"
    ),
    /**
     * DELETE INDEXATION : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#INDEXATION}
     */
    INFO_INDEXATION_TEC(STTypesEnum.DEFAULT, STPorteesEnum.TECHNIQUE, EpgObjetsEnum.INDEXATION, "Info indexation"),
    /**
     * DELETE INDEXATION : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#INDEXATION}
     */
    DEL_INDEXATION_TEC(STTypesEnum.DELETE, STPorteesEnum.TECHNIQUE, EpgObjetsEnum.INDEXATION, "Suppression indexation"),
    /**
     * ERROR ADD INDEXATION : {@link STTypesEnum#FAIL_ADD}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#INDEXATION}
     */
    ERROR_ADD_INDEXATION_TEC(
        STTypesEnum.FAIL_ADD,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.INDEXATION,
        "Erreur lors de l'insertion de la nouvelle rubrique d'indexation"
    ),
    /**
     * DELETE LISTE TRAITEMENT PAPIER : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#LST_TRAITEMENT_PAPIER}
     */
    DEL_LST_TRAITEMENT_PAPIER_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.LST_TRAITEMENT_PAPIER,
        "Suppression liste traitement papier"
    ),
    /**
     * DELETE REPERTOIRE PARAPHEUR : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#PARAPHEUR}
     */
    DEL_REP_PARAPHEUR_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.REP_PARAPHEUR,
        "Suppression répertoire parapheur"
    ),
    /**
     * DELETE TABLEAU DYNAMIQUE : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#TAB_DYNAMIQUE}
     */
    DEL_TAB_DYN_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.TAB_DYNAMIQUE,
        "Suppression tableau dynamique"
    ),
    /**
     * DELETE MODELE REP FDD : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#MOD_FDD}
     */
    DEL_MOD_REP_FDD_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.MOD_FDD,
        "Suppression modèle de fond de dossier"
    ),
    /**
     * DELETE WS SPE : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#WS_SPE}
     */
    DEL_WS_SPE_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.WS_SPE,
        "Suppression notification WS Spe"
    ),
    /**
     * Suppression BirtRefreshFichier : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BIRT_REFRESH_FICHIER}
     */
    DEL_BIRT_REFRESH_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BIRT_REFRESH_FICHIER,
        "Suppression document BirtRefreshFichier"
    ),
    /**
     * Suppression ExportPANStat : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_DEL_EXPORT_PAN_STATS}
     */
    DEL_EXPORT_PAN_STATS_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_EXPORT_PAN_STATS,
        "Suppression document ExportPANStat"
    ),

    // ************************************************* CREATION DOCUMENTS
    // **********************************************
    /**
     * Initialisation vecteur de publication : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#VECTEUR_PUB}
     */
    INIT_VECTEUR_PUB_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.VECTEUR_PUB,
        "Initialisation vecteur publication"
    ),
    /**
     * creation vecteur de publication : {@link STTypesEnum#CREATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#VECTEUR_PUB}
     */
    CREATE_VECTEUR_PUB_TEC(
        STTypesEnum.CREATE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.VECTEUR_PUB,
        "Création vecteur publication"
    ),

    /**
     * Création mise à jour ministérielle :{@link STTypesEnum#CREATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#MAJ_MIN}
     */
    CREATE_MAJ_MIN_TEC(
        STTypesEnum.CREATE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.MAJ_MIN,
        "Création mise à jour ministérielle"
    ),

    // *********************************************** RECUPERATION DOCUMENTS
    // ********************************************

    // *********************************************** UPDATE DOCUMENTS
    // **************************************************
    /**
     * Mise à jour bulletin officiel : {@link STTypesEnum#UPDATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BULLETIN_OFFICIEL}
     */
    UPDATE_BULLETIN_OFF_TEC(
        STTypesEnum.UPDATE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BULLETIN_OFFICIEL,
        "Mise à jour bulletin officiel"
    ),
    /**
     * Mise à jour vecteur publication : {@link STTypesEnum#UPDATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#VECTEUR_PUB}
     */
    UPDATE_VECTEUR_PUB_TEC(
        STTypesEnum.UPDATE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.VECTEUR_PUB,
        "Mise à jour vecteur de publication"
    ),

    // *********************************************** ANOMALIES
    // *********************************************************
    /**
     * Le nor apparait en plusieurs exemplaires : {@link STTypesEnum#OCCURRENCE_MULTI}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#NOR}
     */
    ANO_NOR_MULTI_TEC(
        STTypesEnum.OCCURRENCE_MULTI,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.NOR,
        "Le nor apparait en plusieurs exemplaires"
    ),

    // ****************************************************BATCH***********************************************************
    /**
     * Début du batch de lanceur général PAN et statistiques : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_LANCEUR_GENERAL_PAN}
     */
    INIT_B_LANCEUR_GENERAL_PAN_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_LANCEUR_GENERAL_PAN,
        "Début du batch lanceur général PAN et statistiques"
    ),
    /**
     * Echec dans l'exécution du batch lanceur général PAN et statistique: {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link STObjetsEnum#BATCH_LANCEUR_GENERAL}
     */
    FAIL_PROCESS_B_LANCEUR_GENERAL_PAN_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_LANCEUR_GENERAL_PAN,
        "Echec dans l'exécution du batch lanceur général PAN et statistiques"
    ),
    /**
     * Fin du batch de lanceur général PAN et statistiques : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_LANCEUR_GENERAL_PAN}
     */
    END_B_LANCEUR_GENERAL_PAN_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_LANCEUR_GENERAL_PAN,
        "Fin du batch lanceur général PAN et statistiques"
    ),
    /**
     * Début du batch de lanceur général alert : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_LANCEUR_GENERAL_ALERT}
     */
    INIT_B_LANCEUR_GENERAL_ALERT_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_LANCEUR_GENERAL_ALERT,
        "Début du batch lanceur général alert"
    ),
    /**
     * Echec dans l'exécution du batch lanceur général alert: {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_LANCEUR_GENERAL_ALERT}
     */
    FAIL_PROCESS_B_LANCEUR_GENERAL_ALERT_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_LANCEUR_GENERAL_ALERT,
        "Echec dans l'exécution du batch lanceur général alert"
    ),
    /**
     * Fin du batch de lanceur général alert : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_LANCEUR_GENERAL_ALERT}
     */
    END_B_LANCEUR_GENERAL_ALERT_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_LANCEUR_GENERAL_ALERT,
        "Fin du batch lanceur général alert"
    ),
    /**
     * Début du batch de lanceur général divers : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_LANCEUR_GENERAL_DIVERS}
     */
    INIT_B_LANCEUR_GENERAL_DIVERS_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_LANCEUR_GENERAL_DIVERS,
        "Début du batch lanceur général divers"
    ),
    /**
     * Echec dans l'exécution du batch lanceur général divers: {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_LANCEUR_GENERAL_DIVERS}
     */
    FAIL_PROCESS_B_LANCEUR_GENERAL_DIVERS_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_LANCEUR_GENERAL_DIVERS,
        "Echec dans l'exécution du batch lanceur général divers"
    ),
    /**
     * Fin du batch de lanceur général divers : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_LANCEUR_GENERAL_DIVERS}
     */
    END_B_LANCEUR_GENERAL_DIVERS_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_LANCEUR_GENERAL_DIVERS,
        "Fin du batch lanceur général divers"
    ),

    /**
     * Début du batch d'archivage définitif : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_ARCHIV_DEF}
     */
    INIT_B_ARCHIV_DEF_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_ARCHIV_DEF,
        "Début du batch d'archivage définitif"
    ),
    /**
     * Fin du batch d'archivage définitif : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_ARCHIV_DEF}
     */
    END_B_ARCHIV_DEF_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_ARCHIV_DEF,
        "Fin du batch d'archivage définitif"
    ),
    /**
     * Echec dans l'execution du batch d'archivage définitif : {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_ARCHIV_DEF}
     */
    FAIL_PROCESS_B_ARCHIV_DEF_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_ARCHIV_DEF,
        "Echec dans l'exécution du batch d'archivage définitif"
    ),
    /**
     * Début du batch d'archivage intermédiaire : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_ARCHIV_INTER}
     */
    INIT_B_ARCHIV_INTER_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_ARCHIV_INTER,
        "Début du batch d'archivage intermédiaire"
    ),
    /**
     * Fin du batch d'archivage intermédiaire : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_ARCHIV_INTER}
     */
    END_B_ARCHIV_INTER_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_ARCHIV_INTER,
        "Fin du batch d'archivage intermédiaire"
    ),
    /**
     * Echec dans l'execution du batch d'archivage intermédiaire : {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_ARCHIV_INTER}
     */
    FAIL_PROCESS_B_ARCHIV_INTER_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_ARCHIV_INTER,
        "Echec dans l'exécution du batch d'archivage intermédiaire"
    ),
    /**
     * Début du batch de suppression des dossiers links : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_DEL_DL}
     */
    INIT_B_DEL_DL_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_DL,
        "Début du batch de suppression des dossiers links"
    ),
    /**
     * Fin du batch de suppression des dossiers links : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_DEL_DL}
     */
    END_B_DEL_DL_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_DL,
        "Fin du batch de suppression des dossiers links"
    ),
    /**
     * Echec dans l'execution du batch de suppression des dossiers links : {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_DEL_DL}
     */
    FAIL_PROCESS_B_DEL_DL_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_DL,
        "Echec dans l'exécution du batch de suppression des dossiers links"
    ),
    /**
     * Début du batch de suppression des dossiers links : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_DEL_DOS}
     */
    INIT_B_DEL_DOS_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_DOS,
        "Début du batch de suppression des dossiers"
    ),
    /**
     * Fin du batch de suppression des dossiers links : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_DEL_DOS}
     */
    END_B_DEL_DOS_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_DOS,
        "Fin du batch de suppression des dossiers"
    ),
    /**
     * Echec dans l'execution du batch de suppression des dossiers links : {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_DEL_DOS}
     */
    FAIL_PROCESS_B_DEL_DOS_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_DOS,
        "Echec dans l'exécution du batch de suppression des dossiers"
    ),
    /**
     * Début du batch de confirmation des alertes : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_CONFIRM_ALERT}
     */
    INIT_B_CONFIRM_ALERT_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_CONFIRM_ALERT,
        "Début du batch de confirmation des alertes"
    ),
    /**
     * Fin du batch de confirmation des alertes : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_CONFIRM_ALERT}
     */
    END_B_CONFIRM_ALERT_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_CONFIRM_ALERT,
        "Fin du batch de confirmation des alertes"
    ),
    /**
     * Début du batch de correction de nor après injection : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_CORRECT_NOR}
     */
    INIT_B_CORRECT_NOR_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_CORRECT_NOR,
        "Début du batch de correction de nor après injection"
    ),
    /**
     * Fin du batch de correction de nor après injection : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_CORRECT_NOR}
     */
    END_B_CORRECT_NOR_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_CORRECT_NOR,
        "Fin du batch de correction de nor après injection"
    ),
    /**
     * Début du batch de détection des nor incohérents : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_NOR_INCONSISTENT}
     */
    INIT_B_NOR_INCONSISTENT_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_NOR_INCONSISTENT,
        "Début du batch de détection des nor incohérents"
    ),
    /**
     * Fin du batch de détection des nor incohérents : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_NOR_INCONSISTENT}
     */
    END_B_NOR_INCONSISTENT_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_NOR_INCONSISTENT,
        "Fin du batch de détection des nor incohérents"
    ),
    /**
     * Execution du batch de détection des nors incohérents : {@link STTypesEnum#PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_NOR_INCONSISTENT}
     */
    PROCESS_B_NOR_INCONSISTENT_TEC(
        STTypesEnum.PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_NOR_INCONSISTENT,
        "Exécution du batch de détection des nor incohérents"
    ),
    /**
     * Début du batch d'abandon des dossiers : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_ABANDON}
     */
    INIT_B_ABANDON_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_ABANDON,
        "Début du batch d'abandon des dossiers"
    ),
    /**
     * Fin du batch d'abandon des dossiers : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_ABANDON}
     */
    END_B_ABANDON_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_ABANDON,
        "Fin du batch d'abandon des dossiers"
    ),
    /**
     * Echec dans l'execution du batch d'abandon des dossiers : {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_ABANDON}
     */
    FAIL_PROCESS_B_ABANDON_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_ABANDON,
        "Echec dans l'exécution du batch d'abandon des dossiers"
    ),
    /**
     * Début du batch de candidature d'abandon des dossiers : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_CANDIDAT_ABANDON}
     */
    INIT_B_CANDIDAT_ABANDON_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_CANDIDAT_ABANDON,
        "Début du batch de candidature d'abandon des dossiers"
    ),
    /**
     * Fin du batch de candidature d'abandon des dossiers : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_CANDIDAT_ABANDON}
     */
    END_B_CANDIDAT_ABANDON_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_CANDIDAT_ABANDON,
        "Fin du batch de candidature d'abandon des dossiers"
    ),
    /**
     * Début du batch d'élimination des dossiers : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_ELIMINATION}
     */
    INIT_B_ELIMINATION_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_ELIMINATION,
        "Début du batch d'élimination des dossiers"
    ),
    /**
     * Fin du batch d'élimination des dossiers : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_ELIMINATION}
     */
    END_B_ELIMINATION_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_ELIMINATION,
        "Fin du batch d'élimination des dossiers"
    ),
    /**
     * Début du batch de notification aux WS : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_NOTIFICATION}
     */
    INIT_B_NOTIF_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_NOTIFICATION,
        "Début du batch de notification (demande de publication) aux WS"
    ),
    /**
     * Fin du batch de notification aux WS : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_NOTIFICATION}
     */
    END_B_NOTIF_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_NOTIFICATION,
        "Fin du batch de notification (demande de publication) aux WS"
    ),
    /**
     * Echec dans l'execution du batch de notification aux WS : {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_NOTIFICATION}
     */
    FAIL_PROCESS_B_NOTIF_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_NOTIFICATION,
        "Echec dans l'exécution du batch de notification aux WS"
    ),
    /**
     * Début du batch de génération des statistiques : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_GENERATE_STATS}
     */
    INIT_B_GENERATE_STATS_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_GENERATE_STATS,
        "Début du batch de génération des statistiques"
    ),
    /**
     * Fin du batch de notification aux WS : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_GENERATE_STATS}
     */
    END_B_GENERATE_STATS_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_GENERATE_STATS,
        "Fin du batch de génération des statistiques"
    ),
    /**
     * Echec dans l'execution du batch de notification aux WS : {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_GENERATE_STATS}
     */
    FAIL_PROCESS_B_GENERATE_STATS_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_GENERATE_STATS,
        "Echec dans l'exécution du batch de génération des statistiques"
    ),
    /**
     * Début du batch de suppression des notifications (maj corbeille) : {@link STTypesEnum#INIT}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_DEL_NOTIF}
     */
    INIT_B_DEL_NOTIF_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_NOTIF,
        "Début du batch de suppression des notifications reçues pour la mise à jour corbeille"
    ),
    /**
     * Fin du batch de suppression des notifications (maj corbeille) : {@link STTypesEnum#END}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_DEL_NOTIF}
     */
    END_B_DEL_NOTIF_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_NOTIF,
        "Fin du batch de suppression des notifications reçues pour la mise à jour corbeille"
    ),
    /**
     * Echec dans l'execution du batch de suppression des notifications (maj corbeille) :
     * {@link STTypesEnum#FAIL_PROCESS}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_DEL_NOTIF}
     */
    FAIL_PROCESS_B_DEL_NOTIF_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_NOTIF,
        "Echec dans l'exécution du batch de suppression des notifications reçues pour la mise à jour corbeille"
    ),
    /**
     * Début du batch de suppression des textes signalés : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_DEL_TXT_SIGNALES}
     */
    INIT_B_DEL_TXT_SIGNALES_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_TXT_SIGNALES,
        "Début du batch de suppression des textes signalés"
    ),
    /**
     * Fin du batch de suppression des textes signalés : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_DEL_TXT_SIGNALES}
     */
    END_B_DEL_TXT_SIGNALES_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_TXT_SIGNALES,
        "Fin du batch de suppression des textes signalés"
    ),
    /**
     * Echec dans l'execution du batch de suppression des textes signalés : {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_DEL_TXT_SIGNALES}
     */
    FAIL_PROCESS_B_DEL_TXT_SIGNALES_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_TXT_SIGNALES,
        "Echec dans l'exécution du batch de suppression des textes signalés"
    ),
    /**
     * Début du batch de suppression des rapports birt rafraichit : {@link STTypesEnum#INIT}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_DEL_BIRT_REFRESH}
     */
    INIT_B_DEL_BIRT_REFRESH_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_BIRT_REFRESH,
        "Début du batch de suppression des rapports birt rafraichit"
    ),
    /**
     * Echec dans l'exécution du batch de suppression des rapports birt rafraichit : {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_DEL_BIRT_REFRESH}
     */
    FAIL_PROCESS_B_DEL_BIRT_REFRESH_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_BIRT_REFRESH,
        "Echec dans l'exécution du batch de suppression des rapports birt rafraichit"
    ),
    /**
     * Fin du batch de suppression des rapports birt rafraichit : {@link STTypesEnum#END}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_DEL_BIRT_REFRESH}
     */
    END_B_DEL_BIRT_REFRESH_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_BIRT_REFRESH,
        "Fin du batch de suppression des rapports birt rafraichit"
    ),
    /**
     * Début du batch de maj table mgpp_info_corbeille : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_UPDATE_MGPP_INFO_CORBEILLE}
     */
    INIT_B_UPDATE_INFO_CORBEILLE_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_UPDATE_MGPP_INFO_CORBEILLE,
        "Début du batch de mise à jour de la table MGPP_INFO_CORBEILLE"
    ),
    /**
     * Echec dans l'exécution du batch de maj table mgpp_info_corbeille : {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_UPDATE_MGPP_INFO_CORBEILLE}
     */
    FAIL_PROCESS_B_UPDATE_INFO_CORBEILLE_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_UPDATE_MGPP_INFO_CORBEILLE,
        "Echec dans l'exécution du batch de mise à jour de la table MGPP_INFO_CORBEILLE"
    ),
    /**
     * Fin du batch de maj table mgpp_info_corbeille : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_UPDATE_MGPP_INFO_CORBEILLE}
     */
    END_B_UPDATE_INFO_CORBEILLE_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_UPDATE_MGPP_INFO_CORBEILLE,
        "Fin du batch de mise à jour de la table MGPP_INFO_CORBEILLE"
    ),
    /**
     * Début du batch de suppression des Statistiques PAN exportées: {@link STTypesEnum#INIT}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_DEL_EXPORT_PAN_STATS}
     */
    INIT_B_DEL_EXPORT_PAN_STATS_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_EXPORT_PAN_STATS,
        "Début du batch de suppression des exports des statistiques PAN"
    ),
    /**
     * Echec dans l'exécution du batch de suppression des ExportPANStat : {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_DEL_EXPORT_PAN_STATS}
     */
    FAIL_PROCESS_B_PAN_STATS_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_EXPORT_PAN_STATS,
        "Echec dans l'exécution du batch de suppression des ExportPANStat"
    ),
    /**
     * Echec générique dans le PAN : {@link STTypesEnum#ACTION_LOG}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#PAN}
     */
    LOG_EXCEPTION_PAN_TEC(
        STTypesEnum.ACTION_LOG,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.PAN,
        "Exception remontée dans le PAN"
    ),
    /**
     * Fin du batch de suppression des Statistiques PAN exportées : {@link STTypesEnum#END}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_DEL_EXPORT_PAN_STATS}
     */
    END_B_DEL_EXPORT_PAN_STATS_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_DEL_EXPORT_PAN_STATS,
        "Fin du batch de suppression des exports des statistiques PAN"
    ),
    /**
     * Début du batch de cloture des dossiers type d'acte 'Textes non publiés au JO' : {@link STTypesEnum#INIT}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_CLOSE_TXT_NON_PUB}
     */
    INIT_B_CLOSE_TXT_NON_PUB_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_CLOSE_TXT_NON_PUB,
        "Début du batch de cloture des dossiers type d'acte 'Textes non publiés au JO'"
    ),
    /**
     * Exécution du batch de cloture des dossiers type d'acte 'Textes non publiés au JO' : {@link STTypesEnum#PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_CLOSE_TXT_NON_PUB}
     */
    PROCESS_B_CLOSE_TXT_NON_PUB_TEC(
        STTypesEnum.PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_CLOSE_TXT_NON_PUB,
        "Exécution du batch de cloture des dossiers type d'acte 'Textes non publiés au JO'"
    ),
    /**
     * Echec dans l'exécution du batch de cloture des dossiers type d'acte 'Textes non publiés au JO' :
     * {@link STTypesEnum#FAIL_PROCESS}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_CLOSE_TXT_NON_PUB}
     */
    FAIL_PROCESS_B_CLOSE_TXT_NON_PUB_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_CLOSE_TXT_NON_PUB,
        "Echec dans l'exécution du batch de cloture des dossiers type d'acte 'Textes non publiés au JO'"
    ),
    /**
     * Fin du batch de cloture des dossiers type d'acte 'Textes non publiés au JO' : {@link STTypesEnum#END}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_CLOSE_TXT_NON_PUB}
     */
    END_B_CLOSE_TXT_NON_PUB_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_CLOSE_TXT_NON_PUB,
        "Fin du batch de cloture des dossiers type d'acte 'Textes non publiés au JO'"
    ),
    /**
     * Début du batch de mise à jour du plan de classement : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_INIT_CASE_ROOT}
     */
    INIT_B_INIT_CASE_ROOT_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_INIT_CASE_ROOT,
        "Début du batch de mise à jour du plan de classement"
    ),
    /**
     * FIn du batch de mise à jour du plan de classement : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_INIT_CASE_ROOT}
     */
    END_B_INIT_CASE_ROOT_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_INIT_CASE_ROOT,
        "Fin du batch de mise à jour du plan de classement"
    ),
    /**
     * Echec de validation du squelette : {@link STTypesEnum#FAIL_VALIDATE}_{@link STPorteesEnum#FONCTIONNELLE}_
     * {@link EpgObjetsEnum#SQUELETTE}
     */

    FAIL_INVALIDATE_SQUELETTE(
        STTypesEnum.FAIL_INVALIDATE,
        STPorteesEnum.FONCTIONNELLE,
        EpgObjetsEnum.SQUELETTE,
        "Echec de validation du squelette"
    ),

    // ******************************** Export des dossiers ***********************************************

    /**
     * Début du batch d'export des dossiers : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_EXPORT_DOSSIERS}
     */
    INIT_B_EXPORT_DOSSIERS_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_EXPORT_DOSSIERS,
        "Début du batch d'export des dossiers"
    ),

    /**
     * Echec dans l'execution du batch d'export des dossiers: {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_EXPORT_DOSSIERS}
     */
    FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_EXPORT_DOSSIERS,
        "Echec dans l'exécution du batch d'export des dossiers"
    ),

    /**
     * Exécution du batch de cloture des dossiers type d'acte 'Textes non publiés au JO' : {@link STTypesEnum#PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_CLOSE_TXT_NON_PUB}
     */
    PROCESS_B_EXPORT_DOSSIERS_TEC(
        STTypesEnum.PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_EXPORT_DOSSIERS,
        "Export d'un dossier"
    ),

    /**
     * Fin du batch de confirmation des alertes : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_CONFIRM_ALERT}
     */
    END_B_EXPORT_DOSSIERS_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_EXPORT_DOSSIERS,
        "Fin du batch d'export des alertes"
    ),

    // ************************************** FEV548 : Injection au BDJ ***********************************************
    /**
     * Début du batch d'injection au BDJ :
     * {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_INJECTION_TM_BDJ}
     */
    INIT_B_INJECTION_TM_BDJ_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_INJECTION_TM_BDJ,
        "Début du batch d'injection des textes-maitres au BDJ"
    ),
    /**
     * Echec dans l'exécution du batch d'injection au BDJ :
     * {@link STTypesEnum#FAIL_PROCESS}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_INJECTION_TM_BDJ}
     */
    FAIL_PROCESS_B_INJECTION_TM_BDJ_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_INJECTION_TM_BDJ,
        "Echec dans l'exécution du batch d'injection des textes-maitres au BDJ"
    ),
    /**
     * Fin du batch d'injection au BDJ :
     * {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_INJECTION_TM_BDJ}
     */
    END_B_INJECTION_TM_BDJ_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_INJECTION_TM_BDJ,
        "Fin du batch d'injection des textes maitres au BDJ"
    ),

    // ************************************** FEV572 : Batch Adamant ***********************************************
    /**
     * Début du batch d'extraction Adamant
     * {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_EXTRACTION_ADAMANT}
     */
    INIT_B_EXTRACTION_ADAMANT_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_EXTRACTION_ADAMANT,
        "Début du batch d'extraction ADAMANT"
    ),
    /**
     * Exécution du batch d'extraction Adamant : {@link STTypesEnum#PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_CLOSE_TXT_NON_PUB}
     */
    PROCESS_B_EXTRACTION_ADAMANT_TEC(
        STTypesEnum.PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_EXTRACTION_ADAMANT,
        "Exécution du batch d'extraction ADAMANT"
    ),
    /**
     * Echec dans l'execution du batch d'extraction Adamant : {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_EXTRACTION_ADAMANT}
     */
    FAIL_PROCESS_B_EXTRACTION_ADAMANT_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_EXTRACTION_ADAMANT,
        "Echec dans l'exécution du batch d'extraction ADAMANT"
    ),
    /**
     * Fin du batch d'archivage Adamant
     * {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_EXTRACTION_ADAMANT}
     */
    END_B_EXTRACTION_ADAMANT_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_EXTRACTION_ADAMANT,
        "Fin du batch d'extraction ADAMANT"
    ),
    /**
     * Début du batch d'extraction Adamant
     * {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_RETOUR_VITAM}
     */
    INIT_B_RETOUR_VITAM_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_RETOUR_VITAM,
        "Début du batch de retour VITAM"
    ),
    /**
     * Exécution du batch de retour VITAM
     * {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_RETOUR_VITAM}
     */
    PROCESS_B_RETOUR_VITAM_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_RETOUR_VITAM,
        "Exécution du batch de retour VITAM"
    ),
    /**
     * Echec dans l'execution du batch de retour VITAM : {@link STTypesEnum#FAIL_PROCESS}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#BATCH_RETOUR_VITAM}
     */
    FAIL_PROCESS_B_RETOUR_VITAM_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_RETOUR_VITAM,
        "Echec dans l'exécution du batch de retour VITAM"
    ),
    /**
     * Fin du batch de retour VITAM
     * {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BATCH_RETOUR_VITAM}
     */
    END_B_RETOUR_VITAM_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BATCH_RETOUR_VITAM,
        "Fin du batch de retour VITAM"
    ),

    // ************************************************WEBSERVICES*****************************************************
    /**
     * Initialisation WebService Attribuer nor : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#WS_ATTRIBUER_NOR}
     */
    INIT_WS_ATTRIBUER_NOR_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.WS_ATTRIBUER_NOR,
        "Initialisation du WS Attribuer NOR"
    ),
    /**
     * Initialisation WebService Creer Dossier : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#WS_CREER_DOSSIER}
     */
    INIT_WS_CREER_DOSSIER_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.WS_CREER_DOSSIER,
        "Initialisation du WS Creer Dossier"
    ),
    /**
     * Initialisation WebService Modifier Dossier : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#WS_MODIFIER_DOSSIER}
     */
    INIT_WS_MODIFIER_DOSSIER_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.WS_MODIFIER_DOSSIER,
        "Initialisation du WS Modifier Dossier"
    ),
    /**
     * Initialisation WebService Chercher Dossier : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#WS_CHERCHER_DOSSIER}
     */
    INIT_WS_CHERCHER_DOSSIER(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.WS_CHERCHER_DOSSIER,
        "Initialisation du WS Chercher Dossier"
    ),
    /**
     * Initialisation WebService Donner Avis CE : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#WS_DONNER_AVIS_CE}
     */
    INIT_WS_DONNER_AVIS_CE(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.WS_DONNER_AVIS_CE,
        "Initialisation du WS Donner avis CE"
    ),
    /**
     * Initialisation WebService Modifier Dossier CE : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#WS_MODIFIER_AVIS_CE}
     */
    INIT_WS_MODIFIER_DOSSIER_CE(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.WS_MODIFIER_DOSSIER_CE,
        "Initialisation du WS Modifier Dossier CE"
    ),

    /**
     * Initialisation WebService Chercher Dossier : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#WS_CHERCHER_DOSSIER}
     */
    INIT_WS_CHERCHER_MODIFICATION_DOSSIER(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.WS_CHERCHER_MODIFICATION_DOSSIER,
        "Initialisation du WS Chercher modification Dossier"
    ),
    // ************************************************ECHEC D'ACTIONS*************************************************
    // Création
    /**
     * Echec de création de la notification : {@link STTypesEnum#FAIL_CREATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#NOTIF}
     */
    FAIL_CREATE_NOTIF_TEC(
        STTypesEnum.FAIL_CREATE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.NOTIF,
        "Echec de création de la notification"
    ),
    /**
     * Echec de création d'un NOR : {@link STTypesEnum#FAIL_CREATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#NOR}
     */
    FAIL_CREATE_NOR_TEC(
        STTypesEnum.FAIL_CREATE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.NOR,
        "Echec de création d'un NOR"
    ),
    /**
     * Echec d'ajout du vecteur de publication : {@link STTypesEnum#FAIL_ADD}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#VECTEUR_PUB}
     */
    FAIL_ADD_VECTEUR_TEC(
        STTypesEnum.FAIL_ADD,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.VECTEUR_PUB,
        "Echec d'ajout du vecteur de publication"
    ),
    /**
     * Echec d'ajout du NOR dans le PAN : {@link STTypesEnum#FAIL_ADD}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#PAN}
     */
    FAIL_ADD_NOR_PAN(
        STTypesEnum.FAIL_ADD,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.PAN,
        "Echec d'ajout du NOR dans le PAN"
    ),
    /**
     * Echec d'appel du ws epp : {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#WS_EPP}
     */
    FAIL_GET_WS_EPP_TEC(STTypesEnum.FAIL_GET, STPorteesEnum.TECHNIQUE, EpgObjetsEnum.WS_EPP, "Échec d'appel du ws epp"),

    // Mise à jour
    /**
     * Echec dans la mise à jour des statistiques de l'activité normative : {@link STTypesEnum#FAIL_UPDATE}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#STATS_AN}
     */
    FAIL_UPDATE_STATS_AN_TEC(
        STTypesEnum.FAIL_UPDATE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.STATS_AN,
        "Echec dans la mise à jour des statistiques de l'activité normative"
    ),
    /**
     * Géneration des rapports : {@link STTypesEnum#FAIL_GENERATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#BIRT_REFRESH_FICHIER}
     */
    FAIL_GENERATE_BIRT_REPORT_TEC(
        STTypesEnum.FAIL_GENERATE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.BIRT_REFRESH_FICHIER,
        "Erreur lors de la géneration des rapports"
    ),
    /**
     * Echec de la mise à jour du statut dossier : {@link STTypesEnum#FAIL_UPDATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#STATUT_DOSSIER}
     */
    FAIL_UPDATE_STATUT_DOSSIER_TEC(
        STTypesEnum.FAIL_UPDATE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.STATUT_DOSSIER,
        "Echec de la mise à jour du statut dossier"
    ),
    /**
     * Echec de la sauvegarde d'un mode de parution : {@link STTypesEnum#FAIL_SAVE}_{@link STPorteesEnum#FONCTIONNELLE}_
     * {@link EpgObjetsEnum#MODE_PARUTION}
     */
    FAIL_SAVE_MODE_PARUTION_FONC(
        STTypesEnum.FAIL_SAVE,
        STPorteesEnum.FONCTIONNELLE,
        EpgObjetsEnum.MODE_PARUTION,
        "Echec de la sauvegarde d'un mode de parution"
    ),
    /**
     * Echec de la sauvegarde d'un vecteur de publication : {@link STTypesEnum#FAIL_SAVE}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#VECTEUR_PUB}
     */
    FAIL_SAVE_VECTEUR_TEC(
        STTypesEnum.FAIL_SAVE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.VECTEUR_PUB,
        "Echec de la sauvegarde d'un vecteur de publication"
    ),

    // Récupération
    /**
     * Echec de récupération des modes de parution : {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#FONCTIONNELLE}_
     * {@link EpgObjetsEnum#MODE_PARUTION}
     */
    FAIL_GET_MODE_PARUTION_FONC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.FONCTIONNELLE,
        EpgObjetsEnum.MODE_PARUTION,
        "Echec de récupération des modes de parution"
    ),
    /**
     * Echec de récupération des modes de parution : {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#MODE_PARUTION}
     */
    FAIL_GET_MODE_PARUTION_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.MODE_PARUTION,
        "Echec de récupération des modes de parution (tec)"
    ),
    /**
     * Echec de récupération de la section CE : {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#SECTION_CE}
     */
    FAIL_GET_SECTION_CE_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.SECTION_CE,
        "Echec de récupération de la section CE"
    ),
    /**
     * Echec de récupération d'un vecteur de publication : {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#VECTEUR_PUB}
     */
    FAIL_GET_VECTEUR_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.VECTEUR_PUB,
        "Echec de récupération d'un vecteur de publication"
    ),

    // Navigation
    FAIL_NAVIGATE_TO_TAB_DYN_FONC(
        STTypesEnum.FAIL_NAVIGATE,
        STPorteesEnum.FONCTIONNELLE,
        EpgObjetsEnum.TAB_DYNAMIQUE,
        "Echec de navigation vers les tableaux dynamiques"
    ),

    // ********************************************************SUIVI*****************************
    /**
     * Echec de diffusion Echeancier Promulgation : {@link STTypesEnum#FAIL_DIFFUSION}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#SUIVI}
     */
    FAIL_DIFFUSER_ECHEANCIER_PROMULGATION_TEC(
        STTypesEnum.FAIL_DIFFUSION,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.SUIVI,
        "Echec de diffusion echeancier Promulgation"
    ),

    /**
     * Echec de diffusion calendrier parlementaire : {@link STTypesEnum#FAIL_DIFFUSION}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#SUIVI}
     */
    FAIL_DIFFUSER_CALENDRIER_PARLEMENTAIRE_TEC(
        STTypesEnum.FAIL_DIFFUSION,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.SUIVI,
        "Echec de diffusion calendrier parlementaire"
    ),

    // *************************************************TABLEAU DE PROGRAMMATION***********************************
    /**
     * Echec de publication de tableau de suivi d'habilitation : {@link STTypesEnum#FAIL_PUBLISH}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#TABLEAU_PROGRAMMATION}
     */
    FAIL_PUBLIER_TABLEAU_SUIVI_HABILITATION_TEC(
        STTypesEnum.FAIL_PUBLISH,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.TABLEAU_PROGRAMMATION,
        "Echec de publication de tableau de suivi d'habilitation"
    ),
    /**
     * Echec de publication du tableau de suivi html : {@link STTypesEnum#FAIL_PUBLISH}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#TABLEAU_SUIVI}
     */
    FAIL_PUBLISH_TABLEAU_SUIVI_TEC(
        STTypesEnum.FAIL_PUBLISH,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.SUIVI,
        "Echec de publication du tableau de suivi html"
    ),

    /**
     * Echec de publication du tableau de suivi vers le BDJ :
     * {@link STTypesEnum#FAIL_PUBLISH}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#TABLEAU_SUIVI}
     */
    FAIL_PUBLISH_TABLEAU_SUIVI_BDJ_TEC(
        STTypesEnum.FAIL_PUBLISH,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.SUIVI,
        "Echec de publication du tableau de suivi vers la bdj"
    ),

    /**
     * Succès de la publication du tableau de suivi vers le BDJ :
     * {@link STTypesEnum#FAIL_PUBLISH}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#TABLEAU_SUIVI}
     */
    END_PUBLISH_TABLEAU_SUIVI_BDJ_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.SUIVI,
        "Succès de publication du tableau de suivi vers la bdj"
    ),
    /**
     * Début opération EPG.Parapheur.checkComplet : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#OPERATION_CHECK_PARAPHEUR_COMPLET}
     */
    INIT_OPERATION_CHECK_PARAPHEUR_COMPLET(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.OPERATION_CHECK_PARAPHEUR_COMPLET,
        "Début opération EPG.Parapheur.checkComplet"
    ),
    /**
     * Fin opération EPG.Parapheur.checkComplet : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#OPERATION_CHECK_PARAPHEUR_COMPLET}
     */
    END_OPERATION_CHECK_PARAPHEUR_COMPLET(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.OPERATION_CHECK_PARAPHEUR_COMPLET,
        "Fin opération EPG.Parapheur.checkComplet"
    ),
    /**
     * Opération EPG.Parapheur.checkComplet en cours : {@link STTypesEnum#PROCESS}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#OPERATION_CHECK_PARAPHEUR_COMPLET}
     */
    PROCESS_OPERATION_CHECK_PARAPHEUR_COMPLET(
        STTypesEnum.PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.OPERATION_CHECK_PARAPHEUR_COMPLET,
        "Opération EPG.Parapheur.checkComplet en cours"
    ),
    /**
     * Erreur lors du traitement EPG.Parapheur.checkComplet : {@link STTypesEnum#FAIL_PROCESS}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link EpgObjetsEnum#OPERATION_CHECK_PARAPHEUR_COMPLET}
     */
    FAIL_OPERATION_CHECK_PARAPHEUR_COMPLET(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.OPERATION_CHECK_PARAPHEUR_COMPLET,
        "Erreur lors du traitement EPG.Parapheur.checkComplet"
    ),

    // **************************************************MIGRATION***********************************************
    /**
     * Migration de NOR : {@link STTypesEnum#MIGRATE}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#NOR}
     */
    MIGRATE_NOR_TEC(STTypesEnum.MIGRATE, STPorteesEnum.TECHNIQUE, EpgObjetsEnum.NOR, "Migration de NOR"),

    // **************************************************ADAMANT***********************************************
    /**
     * Echec de génération de l'archive ADAMANT pour un dossier :
     * {@link STTypesEnum#FAIL_CREATE}_{@link STPorteesEnum#TECHNIQUE}_{@link EpgObjetsEnum#NOR}
     */
    FAIL_GENERER_DOSSIER_ARCHIVE(
        STTypesEnum.FAIL_CREATE,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.ARCHIVAGE_DEFINITIF,
        "Echec de la génération de l'archive"
    ),
    /**
     * Echec dans l'execution d'une requete elastic search
     */
    FAIL_ELASTIC_SEARCH_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        EpgObjetsEnum.ELASTIC_SEARCH,
        "Echec dans l'exécution d'une requete elastic search"
    );

    private STCodes type;
    private STCodes portee;
    private STCodes objet;
    private String text;

    EpgLogEnumImpl(STCodes type, STCodes portee, STCodes objet, String text) {
        this.type = type;
        this.portee = portee;
        this.objet = objet;
        this.text = text;
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();
        code
            .append(type.getCodeNumberStr())
            .append(SEPARATOR_CODE)
            .append(portee.getCodeNumberStr())
            .append(SEPARATOR_CODE)
            .append(objet.getCodeNumberStr());
        return code.toString();
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public String toString() {
        StringBuilder loggable = new StringBuilder();
        loggable.append("[CODE:").append(getCode()).append("] ").append(this.text);
        return loggable.toString();
    }
}
