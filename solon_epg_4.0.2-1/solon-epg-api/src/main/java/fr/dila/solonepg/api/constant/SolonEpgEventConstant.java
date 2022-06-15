package fr.dila.solonepg.api.constant;

/**
 * Constantes des événements de SOLON EPG.
 *
 * @author jtremeaux
 */
public final class SolonEpgEventConstant {
    // *************************************************************
    // Catégorie d'Événements
    // *************************************************************

    /**
     * type d'événement lié au traitement papier
     */
    public static final String CATEGORY_TRAITEMENT_PAPIER = "ListeTraitementPapier";

    /**
     * type d'événement lié à la procédure parlementaire
     */
    public static final String CATEGORY_PROCEDURE_PARLEMENT = "ProcedureParlementaire";

    // *************************************************************
    // Événements pour l'audit trail
    // *************************************************************

    /**
     * Événement levé après la création d'un caseLink dans la distribution
     */
    public static final String AFTER_CASE_LINK_CREATED_EVENT = "afterCaseLinkCreated";

    /**
     * Événement levé après la validation automatique d'une étape pour information.
     */
    public static final String AFTER_VALIDATION_POUR_INFORMATION_EVENT = "afterValidationPourInformationEvent";

    /**
     * action de création d'un document
     */
    public static final String ACTION_CREATE = "creation";

    /**
     * action de modification d'un document
     */
    public static final String ACTION_UPDATE = "modification";

    /**
     * action de suppression d'un document
     */
    public static final String ACTION_DELETE = "suppression";

    /**
     * création d'un répertoire dans une arborescence de document fdd;
     */
    public static final String CREATE_FOLDER_FDD = "creationRepertoireFdd";

    /**
     * Commentaire lors de la création d'un répertoire fdd
     */
    public static final String COMMENT_CREATE_FOLDER_FDD = "Création d'un répertoire dans le fond de dossier";

    /**
     * modification d'un répertoire dans une arborescence de document du fond de dossier
     */
    public static final String UPDATE_FOLDER_FDD = "modificationRepertoireFdd";

    /**
     * Commentaire lors de la modification d'un répertoire du fond de dossier
     */
    public static final String COMMENT_UPDATE_FOLDER_FDD = "Mise à jour du répertoire du Fond de Dossier : ";

    /**
     * suppresion d'un répertoire et de ses fils dans une arborescence de document du fond de dossier
     */
    public static final String DELETE_FOLDER_FDD = "suppressionRepertoireFdd";

    /**
     * Commentaire lors de la suppression d'un répertoire du fond de dossier
     */
    public static final String COMMENT_DELETE_FOLDER_FDD = "Suppression dans le fond de dossier du répertoire  ";

    /**
     * création d'un fichier dans une arborescence de document fdd;
     */
    public static final String CREATE_FILE_FDD = "creationDocumentFdd";

    /**
     * Commentaire lors de la création d'un fichier fdd
     */
    public static final String COMMENT_CREATE_FILE_FDD = "Création du fichier du fond de dossier : '";

    /**
     * création d'un fichier dans une arborescence de document parapheur;
     */
    public static final String CREATE_FILE_PARAPHEUR = "creationDocumentParapheur";

    /**
     * Commentaire lors de la création d'un fichier parapheur
     */
    public static final String COMMENT_CREATE_FILE_PARAPHEUR = "Création du fichier du parapheur : '";

    /**
     * modification d'un fichier dans une arborescence de document du fond de dossier
     */
    public static final String UPDATE_FILE_FDD = "modificationDocumentFdd";

    /**
     * Commentaire lors de la modification d'un fichier du fond de dossier
     */
    public static final String COMMENT_UPDATE_FILE_FDD = "Mise à jour fichier Fond de Dossier : '";

    /**
     * Suppression d'un fichier dans une arborescence de document du fond de dossier
     */
    public static final String DELETE_FILE_FDD = "suppressionDocumentFdd";

    /**
     * Commentaire lors de la suppression d'un fichier du fond de dossier
     */
    public static final String COMMENT_DELETE_FILE_FDD = "Suppression fichier Fond de Dossier :' ";

    /**
     * modification d'un fichier dans une arborescence de document du parapheur
     */
    public static final String UPDATE_FILE_PARAPHEUR = "modificationDocumentParapheur";

    /**
     * Commentaire lors de la modification d'un fichier du parapheur
     */
    public static final String COMMENT_UPDATE_FILE_PARAPHEUR = "Mise à jour fichier Parapheur : '";

    /**
     * suppression d'un fichier dans une arborescence de document du parapheur
     */
    public static final String DELETE_FILE_PARAPHEUR = "supressionDocumentParapheur";

    /**
     * Commentaire lors de la suppression d'un fichier du parapheur
     */
    public static final String COMMENT_DELETE_FILE_PARAPHEUR = "Supression fichier Parapheur : '";

    /**
     * Récupération des informations du fichier du parapheur non réalisée
     */
    public static final String FAIL_GET_FILE_INFO_PARAPHEUR = "failGetFileInfoParapheur";

    /**
     * Commentaire lors de la modification d'un fichier du parapheur
     */
    public static final String COMMENT_FAIL_GET_FILE_INFO_PARAPHEUR =
        "Mise à jour du bordereau non effectué suite à la mauvaise exploitation de la feuille de style ";

    /**
     * déplacement d'un fichier dans une arborescence de document parapheur;
     */
    public static final String MOVE_FILE_PARAPHEUR = "deplacementDocumentParapheur";

    /**
     * Commentaire lors du déplacement d'un fichier du parapheur
     */
    public static final String COMMENT_MOVE_FILE_PARAPHEUR = "Déplacement du fichier Parapheur : '";

    /**
     * déplacement d'un fichier dans une arborescence de document du fond de dossier;
     */
    public static final String MOVE_FILE_FDD = "deplacementDocumentFDD";

    /**
     * Commentaire lors du déplacement d'un fichier du Fond de dossier
     */
    public static final String COMMENT_MOVE_FILE_FDD = "Déplacement du fichier Fond de dossier : '";

    /**
     * Événement de validation avec correction de l'étape en cours.
     */
    public static final String DOSSIER_VALIDER_AVEC_CORRECTION_EVENT = "validerAvecCorrection";

    /**
     * Commentaire de l'événement de validation avec correction de l'étape en cours.
     */
    public static final String DOSSIER_VALIDER_AVEC_CORRECTION_COMMENT_PARAM =
        "label.journal.comment.validerAvecCorrection";

    /**
     * Événement de validation avec retour pour modification de l'étape en cours.
     */
    public static final String DOSSIER_VALIDER_RETOUR_MODIFICATION_EVENT = "validerRetourModification";

    /**
     * Commentaire de l'événement de validation avec retour pour modification de l'étape en cours.
     */
    public static final String DOSSIER_VALIDER_RETOUR_MODIFICATION_COMMENT_PARAM =
        "label.journal.comment.validerRetourModification";

    /**
     * Événement de journalisation d'envoi de notif échoué
     */
    public static final String NOTIF_ECHOUEE_EVENT = "echecNotif";

    /**
     * Commentaire de l'événement de journalisation d'un envoi de notif échoué
     */
    public static final String NOTIF_ECHOUEE_COMMENT = "label.journal.comment.echecNotif";

    /**
     * Événement de journalisation d'un rejeu de notif échoué
     */
    public static final String NOTIF_ABANDON_REJEU_EVENT = "echecRejeuNotif";

    /**
     * Commentaire de l'événement de journalisation d'un rejeu de notif échoué
     */
    public static final String NOTIF_ABANDON_REJEU_COMMENT = "label.journal.comment.echecRejeuNotif";

    /**
     * Événement de journalisation d'envoi de notif réussi
     */
    public static final String NOTIF_REUSSIE_EVENT = "reussiteNotif";

    /**
     * Commentaire de l'événement de journalisation d'un envoi de notif réussi
     */
    public static final String NOTIF_REUSSIE_COMMENT = "label.journal.comment.reussiteNotif";

    public static final String FDD_IMPRESSION_ZIP_COMMENT = "label.journal.comment.impression.zip.fdd";

    public static final String FDD_IMPRESSION_ZIP_EVENT = "impressionZipFdd";

    public static final String PARAPHEUR_IMPRESSION_ZIP_COMMENT = "label.journal.comment.impression.zip.parapheur";

    public static final String PARAPHEUR_IMPRESSION_ZIP_EVENT = "impressionZipParapheur";

    public static final String DOSSIER_IMPRESSION_PDF_COMMENT = "label.journal.comment.impression.pdf.dossier";

    public static final String DOSSIER_IMPRESSION_PDF_EVENT = "impressionPdfDossier";

    /**
     * Commentaire de l'évènement de changement de statut du type cloture
     */
    public static final String STATUT_DOSSIER_SUPPRIME_COMMENT = "label.journal.comment.supprimé";

    /**
     * Commentaire de l'évènement de suppression de dossier
     */
    public static final String SUPPRESSION_DOSSIER_COMMENT = "label.journal.dossier.supprimer";
    /**
     * Événement de journalisation de suppression de dossier
     */
    public static final String SUPPRESSION_DOSSIER_EVENT = "suppressionDossierEvent";

    // *************************************************************
    // Événements audit trail : Administration
    // *************************************************************

    /**
     * Événement Administration : modification de l'écran de paramètrage de l'application
     */
    public static final String PARAM_APPLI_UPDATE_EVENT = "parametrageApplicationUpdateEvent";

    /**
     * Événement Administration : modification de l'écran de paramètrage ADAMANT
     */
    public static final String PARAM_ADAMANT_UPDATE_EVENT = "parametrageAdamantUpdateEvent";

    /**
     * modification d'un répertoire du parapheur dans l'administration
     */
    public static final String UPDATE_MODELE_PARAPHEUR_EVENT = "modificationModeleParapheurEvent";

    /**
     * creation d'un répertoire du parapheur dans l'administration
     */
    public static final String CREATE_MODELE_FDD_EVENT = "creationModeleFddEvent";

    /**
     * modification d'un répertoire du parapheur dans l'administration
     */
    public static final String UPDATE_MODELE_FDD_EVENT = "modificationModeleFddEvent";

    /**
     * suppression d'un répertoire du parapheur dans l'administration
     */
    public static final String DELETE_MODELE_FDD_EVENT = "suppressionModeleFddEvent";

    /**
     * Suppression d'un dossier
     */
    public static final String DOSSIER_SUPPRIME_EVENT = "dossierSupprimeEvent";

    /**
     * modification d'un squelette de feuille de route.
     */
    public static final String UPDATE_SQUELETTE_FDR_EVENT = "modificationSqueletteFdrEvent";

    /**
     * duplication d'un squelette de feuille de route.
     */
    public static final String DUPLICATION_SQUELETTE_FDR_EVENT = "duplicationSqueletteFdrEvent";

    /**
     * Événement d'invalidation d'un squelette de feuille de route (avant).
     */
    public static final String BEFORE_INVALIDATE_SQUELETTE = "beforeInvalidateSquelette";

    /**
     * Événement d'invalidation d'un squelette de feuille de route (après).
     */
    public static final String AFTER_INVALIDATE_SQUELETTE = "afterInvalidateSquelette";

    /**
     * creation d'un squelette de feuille de route.
     */
    public static final String CREATE_SQUELETTE_FDR_EVENT = "creationSqueletteFdrEvent";

    public static final String EVENT_SUBSTITUTION_FDR = "fdrSubstitutionEvent";

    /**
     * commentaire : substitution de la feuille de route
     */
    public static final String COMMENT_SUBSTITUTION_FDR = "label.journal.comment.fdrSubstitution";

    // *************************************************************
    // Événements audit trail : Webservice ws epg
    // *************************************************************

    /**
     * Événement web service "attribuerNor" : création d'un dossier
     */
    public static final String WEBSERVICE_ATTRIBUER_NOR_EVENT = "attribuerNor";

    /**
     * Commentaire de l'événement web service "attribuerNor" : création d'un dossier
     */
    public static final String WEBSERVICE_ATTRIBUER_NOR_COMMENT_PARAM = "label.journal.comment.attribuerNor";

    /**
     * Événement web service "donnerAvisCe"
     */
    public static final String WEBSERVICE_DONNER_AVIS_CE_EVENT = "donnerAvisCe";

    /**
     * Commentaire de l'événement web service "donnerAvisCe"
     */
    public static final String WEBSERVICE_DONNER_AVIS_CE_COMMENT_PARAM = "label.journal.comment.donnerAvisCe";

    /**
     * Événement web service "modifierDossierCe"
     */
    public static final String WEBSERVICE_MODIFIER_DOSSIER_CE_EVENT = "modifierDossierCe";

    /**
     * Commentaire de l'événement web service "modifierDossierCe"
     */
    public static final String WEBSERVICE_MODIFIER_DOSSIER_CE_COMMENT_PARAM = "label.journal.comment.modifierDossierCe";

    /**
     * Événement web service "chercherDossierCe"
     */
    public static final String WEBSERVICE_CHERCHER_DOSSIER_CE_EVENT = "chercherDossierCe";

    /**
     * Commentaire de l'événement web service "chercherDossierCe"
     */
    public static final String WEBSERVICE_CHERCHER_DOSSIER_CE_COMMENT_PARAM = "label.journal.comment.chercherDossierCe";

    /**
     * Événement web service "chercherDossier"
     */
    public static final String WEBSERVICE_CHERCHER_DOSSIER_EVENT = "chercherDossier";

    /**
     * Commentaire de l'événement web service "chercherDossier"
     */
    public static final String WEBSERVICE_CHERCHER_DOSSIER_COMMENT_PARAM = "label.journal.comment.chercherDossier";
    /**
     * Evenement web service "creerDossier"
     */
    public static final String WEBSERVICE_CREER_DOSSIER_EVENT = "creerDossier";

    /**
     * Commentaire de l'évènement web service "creerDossier"
     */
    public static final String WEBSERVICE_CREER_DOSSIER_COMMENT_PARAM = "label.journal.comment.creerDossier";

    /**
     * Evenement web service "modifierDossier"
     */
    public static final String WEBSERVICE_MODIFIER_DOSSIER_EVENT = "modifierDossier";

    /**
     * Commentaire de l'évènement web service "modifierDossier"
     */
    public static final String WEBSERVICE_MODIFIER_DOSSIER_COMMENT_PARAM = "label.journal.comment.modifierDossier";

    /**
     * Événement web service "chercherModificationDossierCe"
     */
    public static final String WEBSERVICE_CHERCHER_MODIFICATION_DOSSIER_CE_EVENT = "chercherModificationDossierCe";

    /**
     * Commentaire de l'événement web service "chercherModificationDossierCe"
     */
    public static final String WEBSERVICE_CHERCHER_MODIFICATION_DOSSIER_CE_COMMENT_PARAM =
        "label.journal.comment.chercherModificationDossierCe";

    // *************************************************************
    // Événements audit trail : Webservice ws spe
    // *************************************************************

    /**
     * Événement web service "envoyerRetourPe"
     */
    public static final String WEBSERVICE_ENVOYER_PREMIERE_DEMANDE_EVENT = "envoyerPremiereDemande";

    /**
     * Commentaire de l'événement web service "envoyerRetourPe"
     */
    public static final String WEBSERVICE_ENVOYER_PREMIERE_DEMANDE_COMMENT_PARAM =
        "label.journal.comment.envoyerPremiereDemande";

    /**
     * Événement web service "envoyerRetourPe"
     */
    public static final String WEBSERVICE_ENVOYER_RETOUR_PE_EVENT = "envoyerRetourPe";

    /**
     * Commentaire de l'événement web service "envoyerRetourPe"
     */
    public static final String WEBSERVICE_ENVOYER_RETOUR_PE_COMMENT_PARAM = "label.journal.comment.envoyerRetourPe";

    // *************************************************************
    // Événements des dossiers
    // *************************************************************
    /**
     * Après la création du dossier (avant le démarrage de la feuille de route).
     */
    public static final String AFTER_DOSSIER_CREATED = "afterDossierCreated";

    /**
     * Changement de statut d'un dossier
     */
    public static final String DOSSIER_STATUT_CHANGED = "dossierStatutChanged";

    /**
     * Commentaire de l'événement de changement de statut du type abandon de dossier
     */
    public static final String STATUT_DOSSIER_ABANDON_COMMENT = "label.journal.comment.abandonDossier";

    /**
     * Commentaire de l'événement de changement de statut du type relance de dossier
     */
    public static final String STATUT_DOSSIER_RELANCE_COMMENT = "label.journal.comment.relanceDossier";

    /**
     * Commentaire de l'événement de changement de statut du type terminé sans publication
     */
    public static final String TERMINER_DOSSIER_SANS_PUBLICATION_COMMENT =
        "label.journal.comment.terminerDossierSansPublication";

    /**
     * Commentaire de l'événement de changement de statut du type NOR attribué
     */
    public static final String STATUT_DOSSIER_NOR_ATTRIBUE_COMMENT = "label.journal.comment.norAttribue";

    /**
     * Commentaire de l'évènement de changement de statut du type cloture
     */
    public static final String STATUT_DOSSIER_CLOS_COMMENT = "label.journal.comment.clos";

    /**
     * Après la création du dossier (avant le démarrage de la feuille de route).
     */
    public static final String INJECTION_AFTER_DOSSIER_CREATED = "injectionAfterDossierCreated";

    /**
     * Après la création du dossier (avant le démarrage de la feuille de route).
     */
    public static final String AFTER_VALIDER_TRANSMISSION_TO_SUPPRESSION = "afterValiderTransmissionToSuppression";

    /**
     * Après le transfert du dossier.
     */
    public static final String AFTER_DOSSIER_TRANSFERT = "afterDossierTransfert";

    /**
     * Après la réattribuation du dossier.
     */
    public static final String AFTER_DOSSIER_REATTRIBUTION = "afterDossierReattribution";

    public static final String DOCUMENT_ID_LIST = "documentIdList";

    /**
     * Après la saisine rectificative du dossier.
     */
    public static final String AFTER_SAISINE_RECTIFICATIVE = "saisineRectificative";

    /**
     * Après l'envoi de pièces complémentaires
     */
    public static final String AFTER_ENVOI_PIECES_COMPLEMENTAIRES = "envoiPiecesComplementaires";

    // *************************************************************
    // Paramètres des événements inline
    // *************************************************************

    /**
     * Paramètre de l'événement "afterCaseLinkCreated" : Dossier.
     */
    public static final String AFTER_CASE_LINK_CREATED_DOSSIER_DOCID_PARAM = "dossierDocId";

    /**
     * Paramètre de l'événement "afterCaseLinkCreated" : RouteStep.
     */
    public static final String AFTER_CASE_LINK_CREATED_ROUTE_STEP_DOCID_PARAM = "routeStepDocId";

    /**
     * Paramètre de l'événement "afterValidationPourInformation" : dossierId.
     */
    public static final String AFTER_VALIDATION_POUR_INFORMATION_DOSSIER_ID_PARAM = "dossierId";

    /**
     * Paramètre de l'événement "afterValidationPourInformation" : dossierTitreActe.
     */
    public static final String AFTER_VALIDATION_POUR_INFORMATION_DOSSIER_TITRE_ACTE_PARAM = "dossierTitreActe";

    /**
     * Paramètre de l'événement "afterValidationPourInformation" : dossierNor.
     */
    public static final String AFTER_VALIDATION_POUR_INFORMATION_DOSSIER_NOR_PARAM = "dossierNor";

    /**
     * Paramètre de l'événement "afterValidationPourInformation" : RouteStepMailboxId.
     */
    public static final String AFTER_VALIDATION_POUR_INFORMATION_ROUTE_STEP_MAILBOX_ID_PARAM = "routeStepMailboxId";

    /**
     * Dossier SOLON EPG.
     */
    public static final String DOSSIER_EVENT_PARAM = "dossier";

    /**
     * Identifiant technique du poste.
     */
    public static final String POSTE_ID_EVENT_PARAM = "posteId";

    /**
     * Nom Ministere.
     */
    public static final String MINISTERE_EVENT_PARAM = "ministereId";

    /**
     * Nom Direction.
     */
    public static final String DIRECTION_EVENT_PARAM = "directionId";

    /**
     * Nor du dossier pour copier la feuille de route
     */
    public static final String NOR_DOSSIER_COPIE_FDR_PARAM = "norDosierCopieFdr";

    /**
     * Evenement lors du changement de document dans la recherche
     */
    public static final String CURRENT_DOCUMENT_SEARCH_CHANGED_EVENT = "currentDocumentSearchChangedEvent";

    /**
     * Parametre du chemin pour la sauvegarde du resultat consulte
     */
    public static final String RESULTAT_CONSULTE_CURRENT_PATH = "resultatConsulteCurrentPath";

    /**
     * type de resultat consulte
     */
    public static final String RESULTAT_CONSULTE_TYPE = "resultatConsulteType";

    /**
     * id du resultat consulte
     */
    public static final String RESULTAT_CONSULTE_ID = "resultatConsulteId";

    public static final String TRAITEMENT_PAPIER_UPDATE = "traitementPapierUpdate";

    public static final String BATCH_EVENT_PURGE_CALENDRIER_BATCH = "purgeCalendrierBatch";

    public static final String BATCH_EVENT_UPDATE_FICHE_AFTER_UPDATE_BORDEREAU =
        "updateFichePresentationAfterUpdateBordereau";

    // *************************************************************
    // Événements des fonds de dossier
    // *************************************************************
    public static final String CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR = "FondDeDossierReserveMin";
    public static final String CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG =
        "FondDeDossierReserveMinEtSgg";
    public static final String CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG = "FondDeDossierReserveSgg";
    public static final String CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER = "FondDeDossierAllUSer";

    // *************************************************************
    // Événements des exports
    // *************************************************************
    public static final String DOSSIERS_RECHERCHE_RAPIDE_EXPORT_EVENT = "dossiersRechercheRapideExportEvent";
    public static final String DOSSIERS_ARCHIVAGE_INTERMEDIAIRE_EXPORT_EVENT =
        "dossiersArchivageIntermediaireExportEvent";
    public static final String DOSSIERS_ARCHIVAGE_DEFINITIF_EXPORT_EVENT = "dossiersArchivageDefinitifExportEvent";
    public static final String DOSSIERS_SUPPRESSION_CONSULTATION_EXPORT_EVENT =
        "dossiersSuppressionConsultationExportEvent";
    public static final String DOSSIERS_SUPPRESSION_SUIVI_EXPORT_EVENT = "dossiersSuppressionSuiviExportEvent";
    public static final String DOSSIERS_ABANDON_EXPORT_EVENT = "dossiersAbandonExportEvent";
    public static final String DOSSIERS_TAB_DYN_EXPORT_EVENT = "dossiersTabDynExportEvent";

    public static final String REQUEST_DTO = "requestDto";
    public static final String NOR = "nor";
    public static final String ID_TAB_DYN = "idTabDyn";

    // *************************************************************
    // Événements des batchs
    // *************************************************************
    public static final String LANCEUR_GENERAL_PAN_STATS = "lanceurGeneralBatchPANEvent";
    public static final String LANCEUR_GENERAL_ALERT = "lanceurGeneralAlertBatchEvent";
    public static final String LANCEUR_GENERAL_DIVERS = "lanceurGeneralDiversBatchEvent";
    public static final String BATCH_EVENT_CONFIRM_ALERT = "confirmAlertEvent";
    public static final String BATCH_EVENT_STAT_GENERATION_RESULTAT = "statsGenerationResultatBatch";
    public static final String BATCH_EVENT_STAT_ONE_GENERATION = "onestatGenerationBatch";
    public static final String BATCH_EVENT_TEXTE_SIGNALE = "textesSignalesEvent";
    public static final String BATCH_EVENT_SUPPRESSION_NOTIFICATION = "suppressionNotificationEvent";
    public static final String BATCH_EVENT_WS_NOTIFICATION = "wsNotificationEvent";
    public static final String BATCH_EVENT_DOSSIER_ABANDON = "dossierAbandon";
    public static final String BATCH_EVENT_DOSSIER_CANDIDAT_ABANDON = "dossierCandidatAbandon";
    public static final String BATCH_EVENT_DOSSIER_CANDIDAT_ARCHIVAGE_INTERMEDIAIRE =
        "dossierCandidatToArchivageIntermediaire";
    public static final String BATCH_EVENT_DOSSIER_CANDIDAT_ARCHIVAGE_DEFINITIF =
        "dossierCandidatToArchivageDefinitive";
    public static final String BATCH_EVENT_DOSSIER_ELIMINATION = "dossierElimination";
    public static final String BATCH_EVENT_DETECT_NOR_INCONSISTENT = "detectNorInconsistent";
    public static final String BATCH_EVENT_DELETE_BIRT_REFRESH = "deleteBirtRefreshEvent";
    public static final String BATCH_EVENT_DELETE_EXPORT_PAN_STATS_EVENT = "deleteExportPanStatsBatch";
    public static final String BATCH_EVENT_CLEAN_FAVORIS_RECHERCHE = "cleanDeletedFavorisRechercheEvent";
    public static final String BATCH_EVENT_UPDATE_INFO_CORBEILLE = "updateMgppInfoCorbeilleEvent";
    public static final String BATCH_EVENT_CLOSE_TXT_NON_PUB = "closeTextesNonPubliesEvent";
    public static final String BATCH_EVENT_INIT_CASE_ROOT = "initCaseRootEvent";
    public static final String BATCH_EVENT_INDEXATION_DOSSIERS = "indexationDossiersEvent";
    public static final String BATCH_EVENT_INJECTION_TM_BDJ = "injectionTextesMaitresBdjEvent";
    public static final String BATCH_EVENT_EXTRACTION_ADAMANT = "extractionAdamantBatchEvent";
    public static final String BATCH_EVENT_RETOUR_VITAM = "retourVitamBatchEvent";

    // ***********************************************
    // Constantes pour les migrations de gouvernement
    // ***********************************************
    public static final String MIGRATION_GVT_TYPE_MIGRATION = "typeMigration";
    public static final String MIGRATION_WITH_DOSSIER_CLOS = "migrationWithDossierClos";
    public static final String MIGRATION_GVT_DELETE_OLD = "deleteOldElementOrganigramme";
    public static final String MIGRATION_GVT_OLD_ELEMENT = "oldElementOrganigramme";
    public static final String MIGRATION_GVT_NEW_ELEMENT = "newElementOrganigramme";
    public static final String MIGRATION_GVT_OLD_MINISTERE = "oldMinistereElementOrganigramme";
    public static final String MIGRATION_GVT_NEW_MINISTERE = "newMinistereElementOrganigramme";
    public static final String MIGRATION_GVT_LOGGER = "idLogger";
    public static final String TYPE_ACTE_EVENT_PARAM = "typeActe";
    public static final String TYPE_ACTE_FL_EVENT_VALUE = "fiche_loi";
    public static final String TYPE_ACTE_DR_EVENT_VALUE = "rapport_parlement";
    public static final String NOR_PRE_REATTRIBUTION_VALUE = "nor_pre_reattribution";

    // ******************************************************
    // Constantes pour la journalisation des actions du PAN
    // ******************************************************
    // *************************************************************
    public static final String CATEGORY_LOG_PAN_APPLICATION_LOIS = "PANApplicationsLois";
    public static final String CATEGORY_LOG_PAN_TRANSPO_DIRECTIVES = "PANTranspositionsDirectives";
    public static final String CATEGORY_LOG_PAN_APPLICATION_ORDO = "PANApplicationOrdonnances";
    public static final String CATEGORY_LOG_PAN_SUIVI_HABILITATIONS = "PANSuiviHabilitations";
    public static final String CATEGORY_LOG_PAN_RATIFICATION_ORDO = "PANRatificationOdonnances";
    public static final String CATEGORY_LOG_PAN_TRAITES_ACCORD = "PANTraitesAccords";
    public static final String CATEGORY_LOG_PAN_GENERAL = "PANGeneral";

    //*********************************************************************
    // Constantes pour les évènements de l'espace de suivi / statistique
    //*********************************************************************
    //*********************************************************************
    public static final String SEND_STAT_DOSSIER_ARCHIVE_EVENT = "sendStatDossiersArchives";
    public static final String SEND_STAT_DOSSIER_ARCHIVE_LIST_DOS_PROPERTY = "listDossierDTO";
    public static final String SEND_STAT_DOSSIER_ARCHIVE_RECIPIENT_PROPERTY = "recipient";
    public static final String SEND_STAT_DOSSIER_ARCHIVE_STARTDATE_PROPERTY = "startDate";
    public static final String SEND_STAT_DOSSIER_ARCHIVE_ENDDATE_PROPERTY = "endDate";

    // ******************
    // Evénements du PAN
    // ******************
    public static final String AJOUT_TM_EVENT = "ajoutTexteMaitre";
    public static final String MODIF_TM_EVENT = "modificationTexteMaitre";
    public static final String AJOUT_MESURE_EVENT = "ajoutMesure";
    public static final String MODIF_MESURE_EVENT = "modificationMesure";
    public static final String SUPPR_MESURE_EVENT = "suppressionMesure";
    public static final String AJOUT_DECRET_APP_EVENT = "ajoutDecretApplication";
    public static final String MODIF_DECRET_APP_EVENT = "modificationDecretApplication";
    public static final String SUPPR_DECRET_APP_EVENT = "suppressionDecretApplication";
    public static final String VALID_DECRET_APP_EVENT = "validationDecretApplication";
    public static final String AJOUT_ORDO_EVENT = "ajoutOrdonnance";
    public static final String MODIF_ORDO_EVENT = "modificationOrdonnance";
    public static final String SUPPR_ORDO_EVENT = "suppressionOrdonnance";
    public static final String MODIF_DECRET_PUB_EVENT = "modificationDecretPublication";
    public static final String AJOUT_RATIF_EVENT = "ajoutLoiRatification";
    public static final String MODIF_RATIF_EVENT = "modificationLoiRatification";
    public static final String SUPPR_RATIF_EVENT = "suppressionLoiRatification";
    public static final String AJOUT_TXT_TRANSPO_EVENT = "ajoutTexteTransposition";
    public static final String MODIF_TXT_TRANSPO_EVENT = "modificationTexteTransposition";
    public static final String SUPPR_TXT_TRANSPO_EVENT = "suppressionTexteTransposition";
    public static final String AJOUT_HABIL_EVENT = "ajoutHabilitation";
    public static final String MODIF_HABIL_EVENT = "modificationHabilitation";
    public static final String SUPPR_HABIL_EVENT = "suppressionHabilitation";
    public static final String PUBLI_STAT_EVENT = "publicationStatistique";
    public static final String PARAM_PAN_UPDATE_EVENT = "parametragePan";
    public static final String AJOUT_TM_COMMENT = "label.journal.comment.ajoutTexteMaitre";
    public static final String MODIF_TM_COMMENT = "label.journal.comment.modificationTexteMaitre";
    public static final String AJOUT_MESURE_COMMENT = "label.journal.comment.ajoutMesure";
    public static final String MODIF_MESURE_COMMENT = "label.journal.comment.modificationMesure";
    public static final String SUPPR_MESURE_COMMENT = "label.journal.comment.suppressionMesure";
    public static final String AJOUT_DECRET_APP_COMMENT = "label.journal.comment.ajoutDecretApplication";
    public static final String MODIF_DECRET_APP_COMMENT = "label.journal.comment.modificationDecretApplication";
    public static final String SUPPR_DECRET_APP_COMMENT = "label.journal.comment.suppressionDecretApplication";
    public static final String VALID_DECRET_APP_COMMENT = "label.journal.comment.validationDecretApplication";
    public static final String AJOUT_ORDO_COMMENT = "label.journal.comment.ajoutOrdonnance";
    public static final String MODIF_ORDO_COMMENT = "label.journal.comment.modificationOrdonnance";
    public static final String SUPPR_ORDO_COMMENT = "label.journal.comment.suppressionOrdonnance";
    public static final String MODIF_DECRET_PUB_COMMENT = "label.journal.comment.modificationDecretPublication";
    public static final String AJOUT_RATIF_COMMENT = "label.journal.comment.ajoutLoiRatification";
    public static final String MODIF_RATIF_COMMENT = "label.journal.comment.modificationLoiRatification";
    public static final String SUPPR_RATIF_COMMENT = "label.journal.comment.suppressionLoiRatification";
    public static final String AJOUT_TXT_TRANSPO_COMMENT = "label.journal.comment.ajoutTexteTransposition";
    public static final String MODIF_TXT_TRANSPO_COMMENT = "label.journal.comment.modificationTexteTransposition";
    public static final String SUPPR_TXT_TRANSPO_COMMENT = "label.journal.comment.suppressionTexteTransposition";
    public static final String AJOUT_HABIL_COMMENT = "label.journal.comment.ajoutHabilitation";
    public static final String MODIF_HABIL_COMMENT = "label.journal.comment.modificationHabilitation";
    public static final String SUPPR_HABIL_COMMENT = "label.journal.comment.suppressionHabilitation";
    public static final String PUBLI_STAT_COMMENT = "label.journal.comment.publicationStatistique";
    public static final String PARAM_PAN_UPDATE_COMMENT = "label.journal.comment.parametragePan";
    public static final String VALIDATION_AUTO_COMMENT = "label.journal.comment.validationAuto";

    private SolonEpgEventConstant() {
        // Private defaulet constructor
    }
}
