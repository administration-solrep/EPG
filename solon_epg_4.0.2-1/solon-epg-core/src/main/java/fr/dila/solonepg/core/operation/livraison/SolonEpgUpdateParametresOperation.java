package fr.dila.solonepg.core.operation.livraison;

import com.google.common.collect.ImmutableList;
import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.core.operation.STVersion;
import fr.dila.st.core.operation.utils.AbstractUpdateParametersOperation;
import fr.dila.st.core.operation.utils.ArchivageParametreBean;
import fr.dila.st.core.operation.utils.ParametreBean;
import java.util.List;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.core.api.CoreSession;

@Operation(
    id = SolonEpgUpdateParametresOperation.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "Met à jour les paramètres technique et les paramètres d'archivage",
    description = SolonEpgUpdateParametresOperation.DESCRIPTION
)
@STVersion(version = "4.0.0")
public class SolonEpgUpdateParametresOperation extends AbstractUpdateParametersOperation {
    /**
     * Identifiant technique de l'opération.
     */
    public static final String ID = "SolonEpg.Livraison.Update.Parametres";

    public static final String DESCRIPTION =
        "Cette opération met à jour les paramètres technique et les paramètres  d'archivage";

    @Context
    protected OperationContext context;

    @Context
    protected CoreSession session;

    @Override
    protected OperationContext getContext() {
        return context;
    }

    @Override
    protected List<ParametreBean> getParametreBeans() {
        return ImmutableList.of(
            new ParametreBean(
                STParametreConstant.DELAI_CANDIDATURE_ABANDON_DOSSIERS,
                "Abandon Dossiers : Délai pour la candidature à l'abandon des dossiers"
            ),
            new ParametreBean(
                STParametreConstant.DELAI_ABANDON_DOSSIERS,
                "Abandon Dossiers : Délai pour l'abandon des dossiers"
            ),
            new ParametreBean(
                STParametreConstant.OBJET_MAIL_DOSSIER_CANDIDAT_ABANDONNE,
                "Abandon Dossiers : Objet du courriel listant les dossiers sélectionnés pour être abandonnés"
            ),
            new ParametreBean(
                STParametreConstant.TEXT_MAIL_DOSSIER_CANDIDAT_ABANDONNE,
                "Abandon Dossiers : Texte du courriel listant les dossiers sélectionnés pour être abandonnés"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.OBJET_MAIL_CONFIRM_ALERT,
                "Alertes : Objet du courriel de confirmation du maintien d'alerte"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.NOTIFICATION_ERREUR_HEAP_SPACE_OBJET,
                "Alertes : Objet mél d’erreur d’allocation dynamique de mémoire (heapspace)"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.NOTIFICATION_ERREUR_HEAP_SPACE_TEXTE,
                "Alertes : Texte mél d’erreur d’allocation dynamique de mémoire (heapspace)"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.OBJET_MAIL_ALERT,
                "Alertes : Objet du courriel de résultat d'alerte"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.TEXTE_MAIL_CONFIRM_ALERT,
                "Alertes : Texte du courriel de confirmation du maintien d'alerte"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.TEXTE_MAIL_ALERT,
                "Alertes : Texte du courriel de résultat d'alerte"
            ),
            new ParametreBean(
                STParametreConstant.AMPLIATION_MAIL_OBJET,
                "Ampliation : Objet du courriel signalant la présence d'une ampliation"
            ),
            new ParametreBean(
                STParametreConstant.AMPLIATION_MAIL_TEXT,
                "Ampliation : Texte du courriel signalant la présence d'une ampliation"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.DELAI_CLOTURE_TXT_NON_PUB,
                "Batch : Délai de clôture après la signature des textes non publiés au JO"
            ),
            new ParametreBean(
                STParametreConstant.OBJET_MAIL_SUIVI_BATCH_NOTIFICATION,
                "Batch : Objet du courriel de notification de suivi des batchs"
            ),
            new ParametreBean(
                STParametreConstant.OBJET_MAIL_DOSSIERS_LINK_INCOHERENT,
                "Batch : Objet du courriel de notification du batch liens de dossiers incohérents"
            ),
            new ParametreBean(
                STParametreConstant.TEXTE_MAIL_SUIVI_BATCH_NOTIFICATION,
                "Batch : Texte du courriel de notification de suivi des batchs"
            ),
            new ParametreBean(
                STParametreConstant.TEXTE_MAIL_DOSSIERS_LINK_INCOHERENT,
                "Batch : Texte du courriel de notification du batch liens de dossiers incohérents"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.BATCH_HEBDO_PURGE_TENTATIVES_CONNEXION_EVENT,
                "Batch Hebdomadaire - Jour d'exécution pour le batch purgeTentativesConnexionEvent"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.BATCH_MENSUEL_DOSSIER_CANDIDAT_ABANDON,
                "Batch Mensuel - Jour d'exécution pour le batch des dossiers candidats à l'abandon"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.OBJET_MAIL_DILA_PARAMETERS,
                "DILA : Objet du courriel paramètres d’hébergement DILA"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.TEXTE_MAIL_DILA_PARAMETERS,
                "DILA : Texte courriel paramètres d’hébergement DILA"
            ),
            new ParametreBean(
                STParametreConstant.MAIL_ADMIN_TECHNIQUE,
                "Divers : Adresse courriel de l'administrateur de l'application"
            ),
            new ParametreBean(STParametreConstant.PAGE_RENSEIGNEMENTS_ID, "Divers : Page de renseignements"),
            new ParametreBean(
                STParametreConstant.PARAMETRE_TAILLE_PIECES_JOINTES,
                "Divers : Taille maximale des pièces jointes en MO"
            ),
            new ParametreBean(
                STParametreConstant.ADRESSE_URL_APPLICATION,
                "Divers : Url application transmise par courriel"
            ),
            new ParametreBean(STParametreConstant.ADRESSE_URL_DIDACTICIEL, "Divers : Url didacticiel"),
            new ParametreBean(
                STParametreConstant.OBJET_MAIL_ALERTE_DOSSIERS_BLOQUES,
                "Dossiers bloqués : Objet du courriel d’alerte"
            ),
            new ParametreBean(
                STParametreConstant.TEXTE_MAIL_ALERTE_DOSSIERS_BLOQUES,
                "Dossiers bloqués : Texte du courriel d’alerte"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.NOTIFICATION_ERREUR_VALIDATION_AUTOMATIQUE_OBJET,
                "Feuille de route : Objet du courriel de notification d'erreur lors de la validation automatique de feuille de route"
            ),
            new ParametreBean(
                STParametreConstant.NOTIFICATION_MAIL_VALIDATION_FEUILLE_ROUTE_OBJET,
                "Feuille de route : Objet du courriel de notification de validation"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.NOTIFICATION_ERREUR_VALIDATION_AUTOMATIQUE_TEXTE,
                "Feuille de route : Texte du courriel de notification d'erreur lors de la validation automatique de feuille de route"
            ),
            new ParametreBean(
                STParametreConstant.NOTIFICATION_MAIL_VALIDATION_FEUILLE_ROUTE_TEXT,
                "Feuille de route : Texte du courriel de notification de validation"
            ),
            new ParametreBean(
                STParametreConstant.DELAI_RENOUVELLEMENT_MOTS_DE_PASSE,
                "Mot de passe : Délai de renouvellement des mots de passe"
            ),
            new ParametreBean(
                STParametreConstant.DELAI_PREVENANCE_RENOUVELLEMENT_MOT_DE_PASSE,
                "Mot de passe : Délai de prévenance de renouvellement de mot de passe"
            ),
            new ParametreBean(
                STParametreConstant.OBJET_MAIL_PREVENANCE_RENOUVELLEMENT_MOT_DE_PASSE,
                "Mot de passe : Objet du courriel de prévenance du renouvellement de mot de passe"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.OBJET_MEL_NOUVEAU_MDP,
                "Mot de passe : Objet du courriel de notification de changement de mot de passe"
            ),
            new ParametreBean(
                STParametreConstant.NOTIFICATION_MAIL_CREATION_UTILISATEUR_OBJET,
                "Mot de passe : Objet courriel de notification de création utilisateur/demande de mot de passe"
            ),
            new ParametreBean(
                STParametreConstant.TEXTE_MAIL_PREVENANCE_RENOUVELLEMENT_MOT_DE_PASSE,
                "Mot de passe : Texte du courriel de prévenance du renouvellement de mot de passe"
            ),
            new ParametreBean(
                STParametreConstant.TEXTE_MAIL_PREVENANCE_RENOUVELLEMENT_MOT_DE_PASSE,
                "Mot de passe : Texte du courriel de prévenance du renouvellement de mot de passe"
            ),
            new ParametreBean(
                STParametreConstant.TEXTE_MAIL_NOUVEAU_MDP,
                "Mot de passe : Texte du courriel de notification de création utilisateur/demande de mot de passe"
            ),
            new ParametreBean(
                STParametreConstant.NOTIFICATION_MAIL_CREATION_UTILISATEUR_TEXT,
                "Mot de passe : Texte du courriel de notification de changement de mot de passe"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.OBJET_MAIL_NOR_DOSSIERS_INCOHERENT,
                "NOR : Objet du courriel de NORs incohérents"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.TEXTE_MAIL_NOR_DOSSIERS_INCOHERENT,
                "NOR : Texte du courriel de NORs incohérents"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.OBJET_MAIL_ABANDON_NOTIF,
                "Notification : Objet du courriel d'abandon d'une notification"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.OBJET_MAIL_ECHEC_REJEU_NOTIF,
                "Notification : Objet du courriel d'échec d'un rejeu d'une notification"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.TEXTE_MAIL_ABANDON_NOTIF,
                "Notification : Texte du courriel d'abandon d'une notification"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.TEXTE_MAIL_ECHEC_REJEU_NOTIF,
                "Notification : Texte du courriel d’échec d'un rejeu d'une notification"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.ANNEE_DEPART_STATISTIQUE_MESURE_PUBLICATION,
                "PAN : Activité normative – Année de départ des statistiques sur les mois et années de publication"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.FLAG_AFFICHAGE_PUBLIER_DOSSIER,
                "PAN : Flag pour l'affichage du bouton publier dossier"
            ),
            new ParametreBean(SolonEpgParametreConstant.FORMAT_NUMERO_ORDRE, "PAN : Format - Numéro ordre"),
            new ParametreBean(SolonEpgParametreConstant.FORMAT_REFERENCE_LOI, "PAN : Format - Références lois"),
            new ParametreBean(
                SolonEpgParametreConstant.FORMAT_REFERENCE_ORDONNANCE,
                "PAN : Format - Références ordonnances"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.PAGE_SUIVI_APPLICATION_LOIS,
                "PAN : Page de suivi de l'application des lois",
                null,
                null,
                "https://extraqual.pm.rie.gouv.fr/suivi.html"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.ACTIVATION_PUBLICATION_ECHEANCIER_BDJ,
                "PAN : Activation de la publication de l'échéancier à la BDJ"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.ACTIVATION_PUBLICATION_BILAN_SEMESTRIEL_BDJ,
                "PAN : Activation de la publication des bilans semestriels à la BDJ"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.OBJET_MAIL_DEMANDE_PUBLI_SUIVANTE,
                "Publication : Objet du courriel de demande de publication"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.OBJET_MAIL_ECHEC_DEMANDE_PUBLI,
                "Publication : Objet du courriel d’échec de demande de publication"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.TEXTE_MAIL_DEMANDE_PUBLI_SUIVANTE,
                "Publication : Texte du courriel de demande de publication"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.TEXTE_MAIL_ECHEC_DEMANDE_PUBLI,
                "Publication : Texte du courriel d’échec de demande de publication"
            ),
            new ParametreBean(
                STParametreConstant.DELAI_ELIMINATION_DOSSIERS,
                "Suppression dossiers : Délai d'élimination des dossiers"
            ),
            new ParametreBean(
                STParametreConstant.OBJET_MAIL_DOSSIER_AFTER_VALIDATION_TRANSMISSION,
                "Suppression dossiers : Objet du courriel listant les dossiers candidats à l'élimination pour les administrateurs ministériels"
            ),
            new ParametreBean(
                STParametreConstant.OBJET_MAIL_DOSSIER_ELIMINATION,
                "Suppression dossiers : Objet du courriel listant les dossiers sélectionnés pour être éliminés"
            ),
            new ParametreBean(
                STParametreConstant.TEXT_MAIL_DOSSIER_AFTER_VALIDATION_TRANSMISSION,
                "Suppression dossiers : Texte du courriel listant les dossiers candidats à l'élimination pour les administrateurs ministériels"
            ),
            new ParametreBean(
                STParametreConstant.TEXT_MAIL_DOSSIER_ELIMINATION,
                "Suppression dossiers : Texte du courriel listant les dossiers sélectionnés pour être éliminés"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.ACTIVATION_ENVOI_INFO_PUBLICATION_CE,
                "WS : Activation de l'envoi des informations de publication au CE"
            ),
            new ParametreBean(
                STParametreConstant.OBJET_MAIL_ERREUR_CREATION_JETON,
                "WS : Objet du courriel de notification d’erreur de création de jeton"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.URL_WEBSERVICE_PUBLICATION_ECHEANCIER_BDJ,
                "WS : Url du webservice de publication de l'échéancier à la BDJ"
            ),
            new ParametreBean(
                SolonEpgParametreConstant.URL_WEBSERVICE_PUBLICATION_BILAN_SEMESTRIEL_BDJ,
                "WS : Url du webservice de publication des bilans semestriels à la BDJ"
            ),
            new ParametreBean(
                STParametreConstant.TEXTE_MAIL_ERREUR_CREATION_JETON,
                "WS : Texte du courriel de notification d’erreur de création de jeton"
            ),
            new ParametreBean(
                STParametreConstant.ADRESSE_URL_DIDACTICIEL,
                STParametreConstant.ADRESSE_URL_DIDACTICIEL_TITRE,
                STParametreConstant.ADRESSE_URL_DIDACTICIEL_DESC,
                STParametreConstant.ADRESSE_URL_DIDACTICIEL_UNIT,
                "http://extraqual.pm.rie.gouv.fr/demat/didac/index.html"
            ),
            // Parametre archivage
            new ArchivageParametreBean(
                STParametreConstant.BATCH_MENSUEL_DOSSIER_CANDIDAT_ARCHIVAGE_DEFINITIVE,
                "Batch Mensuel - Jour d'exécution pour le batch des dossiers candidats à l'archivage définitif"
            ),
            new ArchivageParametreBean(
                STParametreConstant.BATCH_MENSUEL_DOSSIER_CANDIDAT_ARCHIVAGE_INTERMEDIAIRE,
                "Batch Mensuel - Jour d'exécution pour le batch des dossiers candidats à l'archivage intermédiaire"
            ),
            new ArchivageParametreBean(
                STParametreConstant.BATCH_MENSUEL_DOSSIER_CANDIDAT_ARCHIVAGE_INTERMEDIAIRE,
                "Batch Mensuel - Jour d'exécution pour le batch des dossiers candidats à l'archivage intermédiaire"
            ),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_1, "Durée d'utilité administrative (Amnistie)"),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_4,
                "Durée d'utilité administrative (Arrêté du Premier ministre)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_5,
                "Durée d'utilité administrative (Arrêté du Président de la République)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_39,
                "Durée d'utilité administrative (Arrêté du Président de la République individuel)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_46,
                "Durée d'utilité administrative (Arrêté en Conseil d'Etat)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_3,
                "Durée d'utilité administrative (Arrêté interministériel)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_37,
                "Durée d'utilité administrative (Arrêté interministériel individuel)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_2,
                "Durée d'utilité administrative (Arrêté ministériel)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_36,
                "Durée d'utilité administrative (Arrêté ministériel individuel)"
            ),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_6, "Durée d'utilité administrative (Avenant)"),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_7, "Durée d'utilité administrative (Avis)"),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_8, "Durée d'utilité administrative (Circulaire)"),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_9, "Durée d'utilité administrative (Citation)"),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_10, "Durée d'utilité administrative (Communiqué)"),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_11, "Durée d'utilité administrative (Convention)"),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_12, "Durée d'utilité administrative (Décision)"),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_19,
                "Durée d'utilité administrative (Décret de publication de traité ou accord)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_38,
                "Durée d'utilité administrative (Arrêté du Premier ministre individuel)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_43,
                "Durée d'utilité administrative (Décret du Président de la République individuel)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_16,
                "Durée d'utilité administrative (Décret en conseil des ministres)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_42,
                "Durée d'utilité administrative (Décret en conseil des ministres individuel)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_14,
                "Durée d'utilité administrative (Décret en Conseil d'Etat)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_13,
                "Durée d'utilité administrative (Décret en Conseil d'Etat de l'article 37 second alinéa de la Constitution)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_15,
                "Durée d'utilité administrative (Décret en Conseil d'Etat et conseil des ministres)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_41,
                "Durée d'utilité administrative (Décret en Conseil d'Etat et conseil des ministres individuel)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_40,
                "Durée d'utilité administrative (Décret en Conseil d'Etat individuel)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_18,
                "Durée d'utilité administrative (Décret simple)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_44,
                "Durée d'utilité administrative (Décret simple individuel)"
            ),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA, "Durée d'utilité administrative (défaut)"),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_20,
                "Durée d'utilité administrative (Délibération)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_21,
                "Durée d'utilité administrative (Demande d'avis au Conseil d'Etat)"
            ),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_22, "Durée d'utilité administrative (Divers)"),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_23, "Durée d'utilité administrative (Exequatur)"),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_49,
                "Durée d'utilité administrative (Informations parlementaires)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_24,
                "Durée d'utilité administrative (Instruction)"
            ),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_25, "Durée d'utilité administrative (Liste)"),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_26, "Durée d'utilité administrative (Loi)"),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_28,
                "Durée d'utilité administrative (Loi constitutionnelle)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_27,
                "Durée d'utilité administrative (Loi de l'article 53 de la Constitution)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_29,
                "Durée d'utilité administrative (Loi organique)"
            ),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_30, "Durée d'utilité administrative (Note)"),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_31, "Durée d'utilité administrative (Ordonnance)"),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_32, "Durée d'utilité administrative (Rapport)"),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_45,
                "Durée d'utilité administrative (Rapport au parlement)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_33,
                "Durée d'utilité administrative (Rectificatif)"
            ),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_34, "Durée d'utilité administrative (Tableau)"),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_48,
                "Durée d'utilité administrative (Textes non publiés au JO)"
            ),
            new ArchivageParametreBean(
                SolonEpgParametreConstant.DUA_47,
                "Durée d'utilité administrative (Décret du Président de la République en Conseil d'Etat)"
            ),
            new ArchivageParametreBean(SolonEpgParametreConstant.DUA_38, "Durée d'utilité administrative"),
            new ArchivageParametreBean(
                STParametreConstant.DELAI_VERSEMENT_BASE_INTERMEDIAIRE,
                "Archivage intermédiaire : Durée pour le versement des dossiers"
            ),
            new ArchivageParametreBean(
                STParametreConstant.OBJET_MAIL_DOSSIER_CANDIDAT_ARCHIVAGE_DEFINITIVE,
                "Archivage définitif : Objet du courriel listant les dossiers candidats au versement"
            ),
            new ArchivageParametreBean(
                STParametreConstant.OBJET_MAIL_DOSSIER_CANDIDAT_ARCHIVAGE_INTERMEDIAIRE,
                "Archivage intermédiaire : Objet du courriel listant les dossiers candidats au versement"
            ),
            new ArchivageParametreBean(
                STParametreConstant.OBJET_MAIL_DOSSIERS_EXPORT_ARCHIVAGE_DEFINITIVE,
                "Archivage définitif : Objet du courriel listant les dossiers exportés pour archivage définitif"
            ),
            new ArchivageParametreBean(
                STParametreConstant.TEXT_MAIL_DOSSIER_CANDIDAT_ARCHIVAGE_DEFINITIVE,
                "Archivage définitif : Texte du courriel listant les dossiers candidats au versement"
            ),
            new ArchivageParametreBean(
                STParametreConstant.TEXT_MAIL_DOSSIER_CANDIDAT_ARCHIVAGE_INTERMEDIAIRE,
                "Archivage intermédiaire : Texte du courriel listant les dossiers candidats au versement"
            ),
            new ArchivageParametreBean(
                STParametreConstant.TEXT_MAIL_DOSSIERS_EXPORT_ARCHIVAGE_DEFINITIVE,
                "Archivage définitif : Texte du courriel listant les dossiers exportés pour archivage définitif"
            )
        );
    }
}
