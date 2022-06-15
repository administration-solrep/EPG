package fr.dila.solonmgpp.api.logging.enumerationCodes;

import fr.dila.st.api.logging.STLogEnum;
import fr.dila.st.api.logging.enumerationCodes.STCodes;
import fr.dila.st.api.logging.enumerationCodes.STObjetsEnum;
import fr.dila.st.api.logging.enumerationCodes.STPorteesEnum;
import fr.dila.st.api.logging.enumerationCodes.STTypesEnum;

/**
 * Enumération des logs info codifiés du MGPP Code : 000_000_000 <br />
 *
 *
 */
public enum MgppLogEnumImpl implements STLogEnum {
    /**
     * CANCEL REPRESENTANT OEP TEC : {@link STTypesEnum#CANCEL}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#REPRESENTANT_OEP_TEC}
     */
    CANCEL_REPRESENTANT_OEP_TEC(
        STTypesEnum.CANCEL,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.FICHE_LOI,
        "Fiche présentation OEP annulée."
    ),

    /**
     * GET FICHE LOI : {@link STTypesEnum#GET}_{@link STPorteesEnum#TECHNIQUE}_{@link MgppObjetsEnum#FICHE_LOI}
     */
    GET_FICHE_LOI_TEC(
        STTypesEnum.GET,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.FICHE_LOI,
        "2 fiches lois trouvées pour un dossier. Fusion en une fiche."
    ),

    /**
     * EMPTY NOR : {@link STTypesEnum#GET}_{@link STPorteesEnum#TECHNIQUE}_{@link MgppObjetsEnum#NOR}
     */
    EMPTY_NOR(
        STTypesEnum.GET,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.NOR,
        "Le champ NOR du Décret d'ouverture de la session extraordinaire est vide"
    ),
    /**
     * DELETE FICHE LOI : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_{@link MgppObjetsEnum#FICHE_LOI}
     */
    DEL_FICHE_LOI_TEC(STTypesEnum.DELETE, STPorteesEnum.TECHNIQUE, MgppObjetsEnum.FICHE_LOI, "Suppression fiche loi"),
    /**
     * DELETE REPRESENTANT OEP : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#REPRESENTANT_OEP}
     */
    DEL_REPRESENTANT_OEP_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.REPRESENTANT_OEP,
        "Suppression représentant OEP"
    ),
    /**
     * DELETE REPRESENTANT AVI : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#REPRESENTANT_AVI}
     */
    DEL_REPRESENTANT_AVI_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.REPRESENTANT_AVI,
        "Suppression représentant AVI (nominé)"
    ),
    /**
     * DELETE REPRESENTANT AUD : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#REPRESENTANT_AUD}
     */
    DEL_REPRESENTANT_AUD_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.REPRESENTANT_AUD,
        "Suppression représentant AUD"
    ),
    /**
     * DELETE FILE MGPP : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_{@link MgppObjetsEnum#FILE_MGPP}
     */
    DEL_FILE_MGPP_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.FILE_MGPP,
        "Suppression fichier SOLON MGPP"
    ),
    /**
     * DELETE ACTIVITE PARLEMENTAIRE : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#ACT_PARLEMENTAIRE}
     */
    DEL_ACT_PARLEMENTAIRE_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.ACT_PARLEMENTAIRE,
        "Suppression activité parlementaire"
    ),
    /**
     * DELETE SEMAINE PARLEMENTAIRE : {@link STTypesEnum#DELETE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#SEM_PARLEMENTAIRE}
     */
    DEL_SEM_PARLEMENTAIRE_TEC(
        STTypesEnum.DELETE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.SEM_PARLEMENTAIRE,
        "Suppression semaine parlementaire"
    ),

    /**
     * Erreur lors de la récupération de la fiche. : {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#FICHE_LOI}
     */
    FAIL_GET_FICHE_LOI_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.FICHE_LOI,
        "Erreur lors de la récupération de la fiche."
    ),

    /**
     * FAIL DELETE FICHE LOI : {@link STTypesEnum#FAIL_DEL}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#FICHE_LOI}
     */
    FAIL_DEL_FICHE_LOI_TEC(
        STTypesEnum.FAIL_DEL,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.FICHE_LOI,
        "Erreur lors de la suppression d'un fiche loi"
    ),

    /**
     * Erreur lors de la crération de la fiche. : {@link STTypesEnum#FAIL_CREATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#FICHE_LOI}
     */
    FAIL_CREATE_FICHE_LOI_TEC(
        STTypesEnum.FAIL_CREATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.FICHE_LOI,
        "Erreur lors de la création d'une fiche de loi"
    ),

    /**
     * FAIL UPDATE_FICHE_LOI_TEC : {@link STTypesEnum#FAIL_UPDATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#FICHE_LOI}
     */
    FAIL_UPDATE_FICHE_LOI_TEC(
        STTypesEnum.FAIL_UPDATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.FICHE_LOI,
        "Erreur lors de la mise à jour de la fiche de loi"
    ),

    /**
     * Echec connexion WS EPP : {@link STTypesEnum#FAIL_HTTP}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#WS_EPP}
     */
    FAIL_CON_WS_TEC(STTypesEnum.FAIL_HTTP, STPorteesEnum.TECHNIQUE, MgppObjetsEnum.WS_EPP, "Echec de connexion WS EPP"),
    /**
     * Echec récupération WS EPP : {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#WS_EPP}
     */
    FAIL_GET_WS_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.WS_EPP,
        "Echec de récupération du WS EPP"
    ),
    /**
     * Echec transcription DTO : {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_{@link MgppObjetsEnum#DTO}
     */
    FAIL_TRANS_DTO_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.DTO,
        "echec lors de la transcription d'un DTO en container"
    ),

    /**
     * Erreur de recuperation du modeCreation : {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#DTO}
     */
    FAIL_GET_MODE_CREATION_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.DTO,
        "Erreur de recuperation du modeCreation"
    ),

    /**
     * DELETE REPRESENTANT AVI : {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#REPRESENTANT_AVI}
     */
    FAIL_GET_REPRESENTANT_AVI_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.REPRESENTANT_AVI,
        "echec de récupération des representants de la fiche AVI"
    ),

    /**
     * DELETE REPRESENTANT OEP : {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#REPRESENTANT_OEP}
     */
    FAIL_GET_REPRESENTANT_OEP_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.REPRESENTANT_OEP,
        "echec de récupération des representants de la fiche OEP"
    ),

    /**
     * FAIL GET FICHE OEP : {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#FICHE_OEP}
     */
    FAIL_GET_FICHE_PRESENTATION_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.FICHE_PRESENTATION,
        "echec de récupération de la fiche de présentation"
    ),

    /**
     * DELETE REPRESENTANT DR : {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#REPRESENTANT_DR}
     */
    FAIL_GET_REPRESENTANT_DR_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.REPRESENTANT_DR,
        "echec de récupération des representants de la fiche DR"
    ),

    /**
     * FAIL CREATE REPRESENTANT OEP : {@link STTypesEnum#FAIL_CREATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#REPRESENTANT_OEP}
     */
    FAIL_CREATE_REPRESENTANT_OEP_TEC(
        STTypesEnum.FAIL_CREATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.REPRESENTANT_OEP,
        "echec lors de la création des representants de la fiche OEP"
    ),

    /**
     * Echec de récupération des messages: {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#MESSAGE}
     */
    FAIL_GET_MESSAGE_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.MESSAGE,
        "Erreur lors de la recupération des messages."
    ),

    /**
     * Echec lors de la recupération de la piece jointe. {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link STObjetsEnum#PIECE_JOINTE}
     */
    FAIL_GET_PIECE_JOINTE_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        STObjetsEnum.PIECE_JOINTE,
        "Echec lors de la recupération de la piece jointe."
    ),

    /**
     * FAIL GET MIME TYPE. {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#PIECE_JOINTE}
     */
    FAIL_GET_MIME_TYPE_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        STObjetsEnum.PIECE_JOINTE,
        "Impossible de récupérer le mimetype service."
    ),

    /**
     * Erreur lors de la création d'un piece jointe depuis un blob. {@link STTypesEnum#FAIL_CREATE}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link STObjetsEnum#PIECE_JOINTE}
     */
    FAIL_CREATE_PIECE_JOINTE_FROM_BLOB_TEC(
        STTypesEnum.FAIL_CREATE,
        STPorteesEnum.TECHNIQUE,
        STObjetsEnum.PIECE_JOINTE,
        "Erreur lors de la création d'un piece jointe depuis un blob"
    ),

    /**
     * Erreur lors de la mise à jour du cache tdr epp {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#TDR_EPP}
     */
    FAIL_UPDATE_TDR_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.TDR_EPP,
        "Erreur lors de la mise à jour du cache des tables de références EPP"
    ),

    /**
     * Plusieurs occurences d'infos pour une corbeille {@link STTypesEnum#OCCURRENCE_MULTI}_
     * {@link STPorteesEnum#TECHNIQUE}_{@link MgppObjetsEnum#TABLE_MGPP_INFO_CORBEILLE}
     */
    ANO_OCC_INFO_CORBEILLE_TEC(
        STTypesEnum.OCCURRENCE_MULTI,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.TABLE_MGPP_INFO_CORBEILLE,
        "Plusieurs occurrences d'info pour une corbeille"
    ),

    /**
     * Erreur de récup. infos corbeilles {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#FONCTIONNELLE}_
     * {@link MgppObjetsEnum#TABLE_MGPP_INFO_CORBEILLE}
     */
    FAIL_GET_INFO_CORBEILLE_FONC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.FONCTIONNELLE,
        MgppObjetsEnum.TABLE_MGPP_INFO_CORBEILLE,
        "Erreur de récupération des informations liées aux corbeilles"
    ),

    /**
     * Erreur de récup. infos corbeilles {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#TABLE_MGPP_INFO_CORBEILLE}
     */
    FAIL_GET_INFO_CORBEILLE_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.TABLE_MGPP_INFO_CORBEILLE,
        "Erreur tec de récupération des informations liées aux corbeilles"
    ),

    // *********************************************** UPDATE DOCUMENTS
    // **************************************************
    /**
     * UPDATE_FICHE_LOI_TEC : {@link STTypesEnum#UPDATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#FICHE_LOI}
     */
    UPDATE_FICHE_LOI_TEC(
        STTypesEnum.UPDATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.FICHE_LOI,
        "Mise à jour de la fiche de loi"
    ),

    /**
     * UPDATE_FICHE_PRESENTATION_DR : {@link STTypesEnum#UPDATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#FICHE_PRESENTATION_DR}
     */
    UPDATE_FICHE_PRESENTATION_DR_TEC(
        STTypesEnum.UPDATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.REPRESENTANT_DR,
        "Mise à jour de la fiche de présentation DR"
    ),

    /**
     * UPDATE_FICHE_PRESENTATION_DPG : {@link STTypesEnum#UPDATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#FICHE_PRESENTATION_DPG}
     */
    UPDATE_FICHE_PRESENTATION_DPG_TEC(
        STTypesEnum.UPDATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.DECLARATION_POLITIQUE_GENERALE,
        "Mise à jour de la fiche de présentation DPG"
    ),

    /**
     * UPDATE_FICHE_PRESENTATION_SD : {@link STTypesEnum#UPDATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#FICHE_PRESENTATION_SD}
     */
    UPDATE_FICHE_PRESENTATION_SD_TEC(
        STTypesEnum.UPDATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.DECLARATION_SUJET_DETERMINE,
        "Mise à jour de la fiche de présentation SD"
    ),

    /**
     * UPDATE_FICHE_PRESENTATION_JSS : {@link STTypesEnum#UPDATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#FICHE_PRESENTATION_JSS}
     */
    UPDATE_FICHE_PRESENTATION_JSS_TEC(
        STTypesEnum.UPDATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.DECLARATION_ARTICLE_23_8C,
        "Mise à jour de la fiche de présentation JSS"
    ),

    /**
     * UPDATE_FICHE_PRESENTATION_DOC : {@link STTypesEnum#UPDATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#FICHE_PRESENTATION_DOC}
     */
    UPDATE_FICHE_PRESENTATION_DOC_TEC(
        STTypesEnum.UPDATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.DECLARATION_ARTICLE_23_8C,
        "Mise à jour de la fiche de présentation DOC"
    ),

    /**
     * UPDATE NAVETTE : {@link STTypesEnum#UPDATE}_{@link STPorteesEnum#TECHNIQUE}_{@link MgppObjetsEnum#NAVETTE}
     */
    UPDATE_NAVETTE_TEC(STTypesEnum.UPDATE, STPorteesEnum.TECHNIQUE, MgppObjetsEnum.NAVETTE, "updateNavette"),

    /**
     * Mise à jour de la table MGPP_INFO_CORBEILLE : {@link STTypesEnum#UPDATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#TABLE_MGPP_INFO_CORBEILLE}
     */
    UPDATE_MGPP_INFO_CORB_TEC(
        STTypesEnum.UPDATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.TABLE_MGPP_INFO_CORBEILLE,
        "Mise à jour de la table MGPP_INFO_CORBEILLE"
    ),

    /**************************************************** CREATE ***************************************************************/
    /**
     * CREATE ACTIVITE PARLEMENTAIRE : {@link STTypesEnum#CREATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#ACT_PARLEMENTAIRE}
     */
    CREATE_ACT_PARLEMENTAIRE_TEC(
        STTypesEnum.CREATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.ACT_PARLEMENTAIRE,
        "Création d'une nouvelle activité parlementaire"
    ),

    /**
     * CREATE SEMAINE PARLEMENTAIRE : {@link STTypesEnum#CREATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#SEM_PARLEMENTAIRE}
     */
    CREATE_SEM_PARLEMENTAIRE_TEC(
        STTypesEnum.CREATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.SEM_PARLEMENTAIRE,
        "Création d'une semaine parlementaire"
    ),

    /**
     * CREATE FICHE LOI TEC : {@link STTypesEnum#CREATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#FICHE_LOI}
     */
    CREATE_FICHE_LOI_TEC(
        STTypesEnum.CREATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.FICHE_LOI,
        "création d'une fiche de loi"
    ),

    // *******************************calendar*********************************************
    /**
     * START BATCH PURGE CALENDAR : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#CALENDAR}
     */
    START_BATCH_PURGE_CALENDAR_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.CALENDAR,
        "Batch de purge de calendriers - début"
    ),
    /**
     * END BATCH PURGE CALENDAR : {@link STTypesEnum#END}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#CALENDAR}
     */
    END_BATCH_PURGE_CALENDAR_TEC(
        STTypesEnum.END,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.CALENDAR,
        "Batch de purge de calendriers  - fin"
    ),
    /**
     * FAIL BATCH PURGE CALENDAR : {@link STTypesEnum#FAIL_PROCESS}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#CALENDAR}
     */
    FAIL_BATCH_PURGE_CALENDAR_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.CALENDAR,
        "Echec lors du Batch de purge de calendriers"
    ),

    // ********************************EVENT BUILDER***************************************
    /**
     * FAIL BUILD FOLDER : {@link STTypesEnum#FAIL_CREATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#FOLDER_BUILDER}
     */
    FAIL_BUILD_FOLDER_TEC(
        STTypesEnum.FAIL_CREATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.FOLDER_BUILDER,
        "Erreur lors de la construction d'un dossier"
    ),

    // ********************************CONTAINER BUILDER***************************************
    /**
     * FAIL BUILD CONTAINER : {@link STTypesEnum#FAIL_CREATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#CONTAINER_BUILDER}
     */
    FAIL_BUILD_CONTAINER_TEC(
        STTypesEnum.FAIL_CREATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.CONTAINER_BUILDER,
        "Erreur lors de la construction d'un container"
    ),

    // ********************************CONTAINER BUILDER***************************************
    /**
     * FAIL BUILD EVENT : {@link STTypesEnum#FAIL_CREATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#EVENT_BUILDER}
     */
    FAIL_BUILD_EVENT_TEC(
        STTypesEnum.FAIL_CREATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.EVENT_BUILDER,
        "Erreur lors de la construction d'un evenement"
    ),

    // ********************************PIECE JOINTE BUILDER***************************************
    /**
     * FAIL BUILD PIECE JOINTE : {@link STTypesEnum#FAIL_CREATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#PIECE_JOINTE_BUILDER}
     */
    FAIL_BUILD_PIECE_JOINTE_TEC(
        STTypesEnum.FAIL_CREATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.PIECE_JOINTE_BUILDER,
        "Erreur lors de la construction d'une piece jointe"
    ),

    // ***********************************************************INSTITUTION*****************************************
    /**
     * FAIL CREATE INSTITUTION : {@link STTypesEnum#FAIL_CREATE}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#INSTITUTION}
     */
    FAIL_CREATE_INSTITUTION_TEC(
        STTypesEnum.FAIL_CREATE,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.INSTITUTION,
        "Erreur  lors de la création d'une institution"
    ),
    // ***********************************************************Requêteur MGPP
    // *****************************************

    /**
     * INIT MGPP REQUESTOR : {@link STTypesEnum#INIT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#MGPP_REQUESTOR}
     */
    INIT_MGPP_REQUESTOR_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.MGPP_REQUESTOR,
        "Initialisation requêteur de MGPP "
    ),

    /**
     * ECHEC MGPP REQUESTOR : {@link STTypesEnum#FAIL_PROCESS}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#MGPP_REQUESTOR}
     */
    FAIL_MGPP_REQUESTOR_PROCESS_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.MGPP_REQUESTOR,
        "Requêteur MGPP Erreur"
    ),

    /**
     * MGPP REQUESTOR : {@link STTypesEnum#PROCESS}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#MGPP_REQUESTOR}
     */
    MGPP_REQUESTOR_PROCESS_TEC(
        STTypesEnum.PROCESS,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.MGPP_REQUESTOR,
        "Requêteur MGPP Info"
    ),

    // ************************************************NOTIFICATION*************************************************************************
    /**
     * ACCUSER RECEPTION AUTOMATIQUE : {@link STTypesEnum#DEFAULT}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#NOTIFICATION}
     */
    ACCUSER_RECEPTION_AUTOMATIQUE_TEC(
        STTypesEnum.DEFAULT,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.NOTIFICATION,
        "Accuser reception automatique"
    ),

    /**
     * FAIL NOTIFY EVENEMENT : {@link STTypesEnum#FAIL_NOTIFICATION}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#NOTIFICATION}
     */
    FAIL_NOTIFY_EVENT_TEC(
        STTypesEnum.FAIL_NOTIFICATION,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.NOTIFICATION,
        "Erreur lors de la notification d'un evenement"
    ),

    /**
     * FAIL GET DATE NOTIFICATION : {@link STTypesEnum#FAIL_GET}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#DATE_NOTIFICATION}
     */
    FAIL_GET_DATE_NOTIFICATION_TEC(
        STTypesEnum.FAIL_GET,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.DATE_NOTIFICATION,
        "Erreur de récupération de la date d'arrivée de la notification"
    ),
    // ************************************************ADD
    // OEP*************************************************************************

    /**
     * ADD OEP PROCESS TEC : {@link STTypesEnum#PROCESS}_{@link STPorteesEnum#TECHNIQUE}_{@link MgppObjetsEnum#ADD_OEP}
     */
    ADD_OEP_PROCESS_TEC(STTypesEnum.PROCESS, STPorteesEnum.TECHNIQUE, MgppObjetsEnum.ADD_OEP, "ADD OEP PROCESS"),

    /**
     * FAIL ADD OEP PROCESS TEC : {@link STTypesEnum#FAIL_PROCESS}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#ADD_OEP}
     */
    FAIL_ADD_OEP_PROCESS_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.ADD_OEP,
        "FAIL ADD OEP PROCESS"
    ),

    // *************************************************COURRIERS**************************************************
    /**
     * Echec de génération du courrier : {@link STTypesEnum#FAIL_GENERATE}_{@link STPorteesEnum#FONCTIONNELLE}_
     * {@link MgppObjetsEnum#COURRIER}
     */
    FAIL_GENERATE_COURRIER_FONC(
        STTypesEnum.FAIL_GENERATE,
        STPorteesEnum.FONCTIONNELLE,
        MgppObjetsEnum.COURRIER,
        "Echec de génération du courrier"
    ),
    // ************************************************WIDGETS
    // GENERATION*************************************************************************

    /**
     * START GENERATE WIDGETS : {@link STTypesEnum#START}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#WIDGET_GENERATOR}
     */
    INIT_GENERATE_WIDGETS_TEC(
        STTypesEnum.INIT,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.WIDGET_GENERATOR,
        "Génération des widgets experts et déploiement."
    ),

    /**
     * FAIL GENERATE WIDGETS : {@link STTypesEnum#FAIL_PROCESS}_{@link STPorteesEnum#TECHNIQUE}_
     * {@link MgppObjetsEnum#WIDGET_GENERATOR}
     */
    FAIL_GENERATE_WIDGETS_TEC(
        STTypesEnum.FAIL_PROCESS,
        STPorteesEnum.TECHNIQUE,
        MgppObjetsEnum.WIDGET_GENERATOR,
        "Echec lors de la Génération des widgets."
    );

    private STCodes type;
    private STCodes portee;
    private STCodes objet;
    private String text;

    MgppLogEnumImpl(STCodes type, STCodes portee, STCodes objet, String text) {
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
