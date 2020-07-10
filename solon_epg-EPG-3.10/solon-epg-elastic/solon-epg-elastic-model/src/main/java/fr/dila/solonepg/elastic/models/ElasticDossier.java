package fr.dila.solonepg.elastic.models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElasticDossier implements Serializable {

	private static final long				serialVersionUID										= 1L;

	public static final String				UID														= "UID";
	public static final String				DELETED													= "deleted";
	public static final String				CASE_DOCUMENTS_ID										= "case:documentsId";
	public static final String				CMDIST_ALL_ACTION_PARTICIPANT_MAILBOXES					= "cmdist:all_action_participant_mailboxes";
	public static final String				CMDIST_ALL_COPY_PARTICIPANT_MAILBOXES					= "cmdist:all_copy_participant_mailboxes";
	public static final String				CMDIST_INITIAL_ACTION_EXTERNAL_PARTICIPANT_MAILBOXES	= "cmdist:initial_action_external_participant_mailboxes";
	public static final String				CMDIST_INITIAL_ACTION_INTERNAL_PARTICIPANT_MAILBOXES	= "cmdist:initial_action_internal_participant_mailboxes";
	public static final String				CMDIST_INITIAL_COPY_EXTERNAL_PARTICIPANT_MAILBOXES		= "cmdist:initial_copy_external_participant_mailboxes";
	public static final String				CMDIST_INITIAL_COPY_INTERNAL_PARTICIPANT_MAILBOXES		= "cmdist:initial_copy_internal_participant_mailboxes";
	public static final String				COMMON_ICON												= "common:icon";
	public static final String				COMMON_ICON_EXPANDED									= "common:icon-expanded";
	public static final String				COMMON_SIZE												= "common:size";
	public static final String				CONSETAT_DATE_AG_CE										= "consetat:dateAgCe";
	public static final String				CONSETAT_DATE_SAISINE_CE								= "consetat:dateSaisineCE";
	public static final String				CONSETAT_DATE_SECTION_CE								= "consetat:dateSectionCe";
	public static final String				CONSETAT_DATE_SORTIE_CE									= "consetat:dateSortieCE";
	public static final String				CONSETAT_DATE_TRANSMISSION_SECTION_CE					= "consetat:dateTransmissionSectionCe";
	public static final String				CONSETAT_NUMERO_ISA										= "consetat:numeroISA";
	public static final String				CONSETAT_RAPPORTEUR_CE									= "consetat:rapporteurCe";
	public static final String				CONSETAT_RECEPTION_AVIS_CE								= "consetat:receptionAvisCE";
	public static final String				CONSETAT_SECTION_CE										= "consetat:sectionCe";
	public static final String				DC_CONTRIBUTORS											= "dc:contributors";
	public static final String				DC_COVERAGE												= "dc:coverage";
	public static final String				DC_CREATED												= "dc:created";
	public static final String				DC_CREATOR												= "dc:creator";
	public static final String				DC_EXPIRED												= "dc:expired";
	public static final String				DC_FORMAT												= "dc:format";
	public static final String				DC_ISSUED												= "dc:issued";
	public static final String				DC_LANGUAGE												= "dc:language";
	public static final String				DC_LAST_CONTRIBUTOR										= "dc:lastContributor";
	public static final String				DC_MODIFIED												= "dc:modified";
	public static final String				DC_NATURE												= "dc:nature";
	public static final String				DC_RIGHTS												= "dc:rights";
	public static final String				DC_SOURCE												= "dc:source";
	public static final String				DC_SUBJECTS												= "dc:subjects";
	public static final String				DC_TITLE												= "dc:title";
	public static final String				DC_VALID												= "dc:valid";
	public static final String				DOS_ADOPTION											= "dos:adoption";
	public static final String				DOS_APPLICATION_LOI										= "dos:applicationLoi";
	public static final String				DOS_APPLICATION_LOI_REFS								= "dos:applicationLoiRefs";
	public static final String				DOS_ARCHIVE												= "dos:archive";
	public static final String				DOS_ARRIVEE_SOLON_TS									= "dos:arriveeSolonTS";
	public static final String				DOS_BASE_LEGALE_ACTE									= "dos:baseLegaleActe";
	public static final String				DOS_BASE_LEGALE_DATE									= "dos:baseLegaleDate";
	public static final String				DOS_BASE_LEGALE_NATURE_TEXTE							= "dos:baseLegaleNatureTexte";
	public static final String				DOS_BASE_LEGALE_NUMERO_TEXTE							= "dos:baseLegaleNumeroTexte";
	public static final String				DOS_CANDIDAT											= "dos:candidat";
	public static final String				DOS_CATEGORIE_ACTE_ID									= "dos:categorieActeId";
	public static final String				DOS_CATEGORIE_ACTE										= "dos:categorieActe";
	public static final String				DOS_CREATED												= "dos:created";
	public static final String				DOS_DATE_CANDIDATURE									= "dos:dateCandidature";
	public static final String				DOS_DATE_DE_MAINTIEN_EN_PRODUCTION						= "dos:dateDeMaintienEnProduction";
	public static final String				DOS_DATE_ENVOI_JO_TS									= "dos:dateEnvoiJoTS";
	public static final String				DOS_DATE_PRECISEE_PUBLICATION							= "dos:datePreciseePublication";
	public static final String				DOS_DATE_PUBLICATION									= "dos:datePublication";
	public static final String				DOS_DATE_SIGNATURE										= "dos:dateSignature";
	public static final String				DOS_DATE_VERSEMENT_ARCHIVAGE_INTERMEDIAIRE				= "dos:dateVersementArchivageIntermediaire";
	public static final String				DOS_DATE_VERSEMENT_TS									= "dos:dateVersementTS";
	public static final String				DOS_DECRET_NUMEROTE										= "dos:decretNumerote";
	public static final String				DOS_DELAI_PUBLICATION									= "dos:delaiPublication";
	public static final String				DOS_DELETED												= "dos:deleted";
	public static final String				DOS_DIRECTION_ATTACHE_ID								= "dos:directionAttacheId";
	public static final String				DOS_DIRECTION_ATTACHE									= "dos:directionAttache";
	public static final String				DOS_DIRECTION_RESP_ID									= "dos:directionRespId";
	public static final String				DOS_DIRECTION_RESP										= "dos:directionResp";
	public static final String				DOS_DISPOSITION_HABILITATION							= "dos:dispositionHabilitation";
	public static final String				DOS_HABILITATION_COMMENTAIRE							= "dos:habilitationCommentaire";
	public static final String				DOS_HABILITATION_DATE_TERME								= "dos:habilitationDateTerme";
	public static final String				DOS_HABILITATION_NUMERO_ARTICLES						= "dos:habilitationNumeroArticles";
	public static final String				DOS_HABILITATION_NUMERO_ORDRE							= "dos:habilitationNumeroOrdre";
	public static final String				DOS_HABILITATION_REF_LOI								= "dos:habilitationRefLoi";
	public static final String				DOS_HABILITATION_TERME									= "dos:habilitationTerme";
	public static final String				DOS_ID_CREATEUR_DOSSIER									= "dos:idCreateurDossier";
	public static final String				DOS_ID_DOCUMENT_FDD										= "dos:idDocumentFDD";
	public static final String				DOS_ID_DOCUMENT_PARAPHEUR								= "dos:idDocumentParapheur";
	public static final String				DOS_ID_DOSSIER											= "dos:idDossier";
	public static final String				DOS_INDEXATION_DIR										= "dos:indexationDir";
	public static final String				DOS_INDEXATION_DIR_PUB									= "dos:indexationDirPub";
	public static final String				DOS_INDEXATION_MIN										= "dos:indexationMin";
	public static final String				DOS_INDEXATION_MIN_PUB									= "dos:indexationMinPub";
	public static final String				DOS_INDEXATION_SGG										= "dos:indexationSgg";
	public static final String				DOS_INDEXATION_SGG_PUB									= "dos:indexationSggPub";
	public static final String				DOS_IS_ACTE_REFERENCE_FOR_NUMERO_VERSION				= "dos:isActeReferenceForNumeroVersion";
	public static final String				DOS_IS_AFTER_DEMANDE_PUBLICATION						= "dos:isAfterDemandePublication";
	public static final String				DOS_IS_DOSSIER_ISSU_INJECTION							= "dos:isDossierIssuInjection";
	public static final String				DOS_IS_PARAPHEUR_COMPLET								= "dos:isParapheurComplet";
	public static final String				DOS_IS_PARAPHEUR_FICHIER_INFO_NON_RECUPERE				= "dos:isParapheurFichierInfoNonRecupere";
	public static final String				DOS_IS_PARAPHEUR_TYPE_ACTE_UPDATED						= "dos:isParapheurTypeActeUpdated";
	public static final String				DOS_IS_URGENT											= "dos:isUrgent";
	public static final String				DOS_LAST_DOCUMENT_ROUTE									= "dos:lastDocumentRoute";
	public static final String				DOS_LIBRE												= "dos:libre";
	public static final String				DOS_MAIL_RESP_DOSSIER									= "dos:mailRespDossier";
	public static final String				DOS_MESURE_NOMINATIVE									= "dos:mesureNominative";
	public static final String				DOS_MINISTERE_ATTACHE_ID								= "dos:ministereAttacheId";
	public static final String				DOS_MINISTERE_ATTACHE									= "dos:ministereAttache";
	public static final String				DOS_MINISTERE_RESP_ID									= "dos:ministereRespId";
	public static final String				DOS_MINISTERE_RESP										= "dos:ministereResp";
	public static final String				DOS_MOTSCLES											= "dos:motscles";
	public static final String				DOS_NB_DOSSIER_RECTIFIE									= "dos:nbDossierRectifie";
	public static final String				DOS_NOM_COMPLET_CHARGES_MISSIONS						= "dos:nomCompletChargesMissions";
	public static final String				DOS_NOM_COMPLET_CONSEILLERS_PMS							= "dos:nomCompletConseillersPms";
	public static final String				DOS_NOM_COMPLET_RESP_DOSSIER							= "dos:nomCompletRespDossier";
	public static final String				DOS_NOM_RESP_DOSSIER									= "dos:nomRespDossier";
	public static final String				DOS_NOR_ATTRIBUE										= "dos:norAttribue";
	public static final String				DOS_NUMERO_NOR											= "dos:numeroNor";
	public static final String				DOS_NUMERO_VERSION_ACTE_OU_EXTRAIT						= "dos:numeroVersionActeOuExtrait";
	public static final String				DOS_PERIODICITE											= "dos:periodicite";
	public static final String				DOS_PERIODICITE_RAPPORT									= "dos:periodiciteRapport";
	public static final String				DOS_POSTE_CREATOR										= "dos:posteCreator";
	public static final String				DOS_POUR_FOURNITURE_EPREUVE								= "dos:pourFournitureEpreuve";
	public static final String				DOS_PRENOM_RESP_DOSSIER									= "dos:prenomRespDossier";
	public static final String				DOS_PUBLICATION_INTEGRALE_OU_EXTRAIT					= "dos:publicationIntegraleOuExtrait";
	public static final String				DOS_PUBLICATION_RAPPORT_PRESENTATION					= "dos:publicationRapportPresentation";
	public static final String				DOS_PUBLICATIONS_CONJOINTES								= "dos:publicationsConjointes";
	public static final String				DOS_PUBLIE												= "dos:publie";
	public static final String				DOS_QUALITE_RESP_DOSSIER								= "dos:qualiteRespDossier";
	public static final String				DOS_RUBRIQUES											= "dos:rubriques";
	public static final String				DOS_STATUT_ID											= "dos:statutId";
	public static final String				DOS_STATUT												= "dos:statut";
	public static final String				DOS_STATUT_ARCHIVAGE_ID									= "dos:statutArchivageId";
	public static final String				DOS_STATUT_ARCHIVAGE									= "dos:statutArchivage";
	public static final String				DOS_TEL_RESP_DOSSIER									= "dos:telRespDossier";
	public static final String				DOS_TITRE_ACTE											= "dos:titreActe";
	public static final String				DOS_TRANSPOSITION_DIRECTIVE								= "dos:transpositionDirective";
	public static final String				DOS_TRANSPOSITION_DIRECTIVE_TITRE						= "dos:transpositionDirective.titre";
	public static final String				DOS_TRANSPOSITION_DIRECTIVE_COMMENTAIRE					= "dos:transpositionDirective.commentaire";
	public static final String				DOS_TRANSPOSITION_DIRECTIVE_NUMERO						= "dos:transpositionDirectiveNumero";
	public static final String				DOS_TRANSPOSITION_ORDO_REFS								= "dos:transpositionOrdoRefs";
	public static final String				DOS_TRANSPOSITION_ORDONNANCE							= "dos:transpositionOrdonnance";
	public static final String				DOS_TYPE_ACTE											= "dos:typeActe";
	public static final String				DOS_VECTEUR_PUBLICATION									= "dos:vecteurPublication";
	public static final String				DOS_ZONE_COM_SGG_DILA									= "dos:zoneComSggDila";
	public static final String				DOS_TEXTE_ENTREPRISE									= "dos:texteEntreprise";
	public static final String				DOS_DATE_EFFET											= "dos:dateEffet";
	public static final String				NOTE_MIME_TYPE											= "note:mime_type";
	public static final String				NOTE_NOTE												= "note:note";
	public static final String				RETDILA_DATE_PARUTION_JORF								= "retdila:dateParutionJorf";
	public static final String				RETDILA_DATE_PROMULGATION								= "retdila:datePromulgation";
	public static final String				RETDILA_IS_PUBLICATION_EPREUVAGE_DEMANDE_SUIVANTE		= "retdila:isPublicationEpreuvageDemandeSuivante";
	public static final String				RETDILA_LEGISLATURE_PUBLICATION							= "retdila:legislaturePublication";
	public static final String				RETDILA_MODE_PARUTION									= "retdila:modeParution";
	public static final String				RETDILA_NUMERO_TEXTE_PARUTION_JORF						= "retdila:numeroTexteParutionJorf";
	public static final String				RETDILA_PAGE_PARUTION_JORF								= "retdila:pageParutionJorf";
	public static final String				RETDILA_PARUTION_BO										= "retdila:parutionBo";
	public static final String				RETDILA_TITRE_OFFICIEL									= "retdila:titreOfficiel";
	public static final String				TP_AMPLIATION_DESTINATAIRE_MAILS						= "tp:ampliationDestinataireMails";
	public static final String				TP_AMPLIATION_HISTORIQUE								= "tp:ampliationHistorique";
	public static final String				TP_AUTRES_DESTINATAIRES_COMMUNICATION					= "tp:autresDestinatairesCommunication";
	public static final String				TP_CABINET_PM_COMMUNICATION								= "tp:cabinetPmCommunication";
	public static final String				TP_CABINET_SG_COMMUNICATION								= "tp:cabinetSgCommunication";
	public static final String				TP_CHARGE_MISSION_COMMUNICATION							= "tp:chargeMissionCommunication";
	public static final String				TP_CHEMIN_CROIX											= "tp:cheminCroix";
	public static final String				TP_COMMENTAIRE_REFERENCE								= "tp:commentaireReference";
	public static final String				TP_DATE_ARRIVE_PAPIER									= "tp:dateArrivePapier";
	public static final String				TP_DATE_RETOUR											= "tp:dateRetour";
	public static final String				TP_EPREUVE												= "tp:epreuve";
	public static final String				TP_MOTIF_RETOUR											= "tp:motifRetour";
	public static final String				TP_NOMS_SIGNATAIRES_COMMUNICATION						= "tp:nomsSignatairesCommunication";
	public static final String				TP_NOMS_SIGNATAIRES_RETOUR								= "tp:nomsSignatairesRetour";
	public static final String				TP_NOUVELLE_DEMANDE_EPREUVES							= "tp:nouvelleDemandeEpreuves";
	public static final String				TP_NUMEROS_LISTE_SIGNATURE_FIELD						= "tp:numerosListeSignatureField";
	public static final String				TP_PAPIER_ARCHIVE										= "tp:papierArchive";
	public static final String				TP_PERSONNES_NOMMEES									= "tp:personnesNommees";
	public static final String				TP_PRIORITE												= "tp:priorite";
	public static final String				TP_PUBLICATION_DATE										= "tp:publicationDate";
	public static final String				TP_PUBLICATION_DATE_DEMANDE								= "tp:publicationDateDemande";
	public static final String				TP_PUBLICATION_DATE_ENVOI_JO							= "tp:publicationDateEnvoiJo";
	public static final String				TP_PUBLICATION_DELAI									= "tp:publicationDelai";
	public static final String				TP_PUBLICATION_EPREUVE_EN_RETOUR						= "tp:publicationEpreuveEnRetour";
	public static final String				TP_PUBLICATION_MODE_PUBLICATION							= "tp:publicationModePublication";
	public static final String				TP_PUBLICATION_NOM_PUBLICATION							= "tp:publicationNomPublication";
	public static final String				TP_PUBLICATION_NUMERO_LISTE								= "tp:publicationNumeroListe";
	public static final String				TP_RETOUR_A												= "tp:retourA";
	public static final String				TP_RETOUR_DU_BONA_TITRER_LE								= "tp:retourDuBonaTitrerLe";
	public static final String				TP_SIGNATAIRE											= "tp:signataire";
	public static final String				TP_SIGNATURE_DESTINATAIRE_CPM							= "tp:signatureDestinataireCPM";
	public static final String				TP_SIGNATURE_DESTINATAIRE_SGG							= "tp:signatureDestinataireSGG";
	public static final String				TP_SIGNATURE_PM											= "tp:signaturePm";
	public static final String				TP_SIGNATURE_PR											= "tp:signaturePr";
	public static final String				TP_SIGNATURE_SGG										= "tp:signatureSgg";
	public static final String				TP_TEXTE_APUBLIER										= "tp:texteAPublier";
	public static final String				TP_TEXTE_SOUMIS_AVALIDATION								= "tp:texteSoumisAValidation";
	/* gestion des droits/acls */
	public static final String				PERMS													= "perms";

	public static final String				DOCUMENTS												= "documents";

	@JsonProperty(UID)
	private String							uid;
	@JsonProperty(DELETED)
	private boolean							deleted = false;
	@JsonProperty(CASE_DOCUMENTS_ID)
	private List<String>					caseDocumentsId;
	@JsonProperty(DOCUMENTS)
	private List<ElasticDocument>			documents												= new ArrayList<ElasticDocument>();

	/* cmdist:... */
	@JsonProperty(CMDIST_ALL_ACTION_PARTICIPANT_MAILBOXES)
	private List<String>					cmdistAllActionParticipantMailboxes;
	@JsonProperty(CMDIST_ALL_COPY_PARTICIPANT_MAILBOXES)
	private List<String>					cmdistAllCopyParticipantMailboxes;
	@JsonProperty(CMDIST_INITIAL_ACTION_EXTERNAL_PARTICIPANT_MAILBOXES)
	private List<String>					cmdistInitialActionExternalParticipantMailboxes;
	@JsonProperty(CMDIST_INITIAL_ACTION_INTERNAL_PARTICIPANT_MAILBOXES)
	private List<String>					cmdistInitialActionInternalParticipantMailboxes;
	@JsonProperty(CMDIST_INITIAL_COPY_EXTERNAL_PARTICIPANT_MAILBOXES)
	private List<String>					cmdistInitialCopyExternalParticipantMailboxes;
	@JsonProperty(CMDIST_INITIAL_COPY_INTERNAL_PARTICIPANT_MAILBOXES)
	private List<String>					cmdistInitialCopyInternalParticipantMailboxes;

	/* common:... */
	@JsonProperty(COMMON_ICON)
	private String							commonIcon;
	@JsonProperty(COMMON_ICON_EXPANDED)
	private String							commonIconExpanded;
	@JsonProperty(COMMON_SIZE)
	private String							commonSize;

	/* consetat:... */
	@JsonProperty(CONSETAT_DATE_AG_CE)
	private Date							consetatDateAgCe;
	@JsonProperty(CONSETAT_DATE_SAISINE_CE)
	private Date							consetatDateSaisineCE;
	@JsonProperty(CONSETAT_DATE_SECTION_CE)
	private Date							consetatDateSectionCe;
	@JsonProperty(CONSETAT_DATE_SORTIE_CE)
	private Date							consetatDateSortieCE;
	@JsonProperty(CONSETAT_DATE_TRANSMISSION_SECTION_CE)
	private Date							consetatDateTransmissionSectionCe;
	@JsonProperty(CONSETAT_NUMERO_ISA)
	private String							consetatNumeroISA;
	@JsonProperty(CONSETAT_RAPPORTEUR_CE)
	private String							consetatRapporteurCe;
	@JsonProperty(CONSETAT_RECEPTION_AVIS_CE)
	private String							consetatReceptionAvisCE;
	@JsonProperty(CONSETAT_SECTION_CE)
	private String							consetatSectionCe;

	/* dc:... */
	@JsonProperty(DC_CONTRIBUTORS)
	private String[]						dcContributors;
	@JsonProperty(DC_COVERAGE)
	private String							dcCoverage;
	@JsonProperty(DC_CREATED)
	private Date							dcCreated;
	@JsonProperty(DC_CREATOR)
	private String							dcCreator;
	@JsonProperty(DC_EXPIRED)
	private Date							dcExpired;
	@JsonProperty(DC_FORMAT)
	private String							dcFormat;
	@JsonProperty(DC_ISSUED)
	private String							dcIssued;
	@JsonProperty(DC_LANGUAGE)
	private String							dcLanguage;
	@JsonProperty(DC_LAST_CONTRIBUTOR)
	private String							dcLastContributor;
	@JsonProperty(DC_MODIFIED)
	private Date							dcModified;
	@JsonProperty(DC_NATURE)
	private String							dcNature;
	@JsonProperty(DC_RIGHTS)
	private String							dcRights;
	@JsonProperty(DC_SOURCE)
	private String							dcSource;
	@JsonProperty(DC_SUBJECTS)
	private String[]						dcSubjects;
	@JsonProperty(DC_TITLE)
	private String							dcTitle;
	@JsonProperty(DC_VALID)
	private String							dcValid;

	/* dos:... */
	@JsonProperty(DOS_ADOPTION)
	private Boolean							dosAdoption;
	@JsonProperty(DOS_APPLICATION_LOI)
	private List<Map<String, Serializable>>	dosApplicationLoi;
	@JsonProperty(DOS_APPLICATION_LOI_REFS)
	private List<String>					dosApplicationLoiRefs;
	@JsonProperty(DOS_ARCHIVE)
	private Boolean							dosArchive;
	@JsonProperty(DOS_ARRIVEE_SOLON_TS)
	private Boolean							dosArriveeSolonTS;
	@JsonProperty(DOS_BASE_LEGALE_ACTE)
	private String							dosBaseLegaleActe;
	@JsonProperty(DOS_BASE_LEGALE_DATE)
	private Date							dosBaseLegaleDate;
	@JsonProperty(DOS_BASE_LEGALE_NATURE_TEXTE)
	private String							dosBaseLegaleNatureTexte;
	@JsonProperty(DOS_BASE_LEGALE_NUMERO_TEXTE)
	private String							dosBaseLegaleNumeroTexte;
	@JsonProperty(DOS_CANDIDAT)
	private String							dosCandidat;
	@JsonProperty(DOS_CATEGORIE_ACTE_ID)
	private String							dosCategorieActeId;
	@JsonProperty(DOS_CATEGORIE_ACTE)
	private String							dosCategorieActe;
	@JsonProperty(DOS_CREATED)
	private Date							dosCreated;
	@JsonProperty(DOS_DATE_CANDIDATURE)
	private Date							dosDateCandidature;
	@JsonProperty(DOS_DATE_DE_MAINTIEN_EN_PRODUCTION)
	private Date							dosDateDeMaintienEnProduction;
	@JsonProperty(DOS_DATE_ENVOI_JO_TS)
	private Date							dosDateEnvoiJoTS;
	@JsonProperty(DOS_DATE_PRECISEE_PUBLICATION)
	private Date							dosDatePreciseePublication;
	@JsonProperty(DOS_DATE_PUBLICATION)
	private Date							dosDatePublication;
	@JsonProperty(DOS_DATE_SIGNATURE)
	private Date							dosDateSignature;
	@JsonProperty(DOS_DATE_VERSEMENT_ARCHIVAGE_INTERMEDIAIRE)
	private Date							dosDateVersementArchivageIntermediaire;
	@JsonProperty(DOS_DATE_VERSEMENT_TS)
	private Date							dosDateVersementTS;
	@JsonProperty(DOS_DECRET_NUMEROTE)
	private Boolean							dosDecretNumerote;
	@JsonProperty(DOS_DELAI_PUBLICATION)
	private String							dosDelaiPublication;
	@JsonProperty(DOS_DELETED)
	private Boolean							dosDeleted;
	@JsonProperty(DOS_DIRECTION_ATTACHE_ID)
	private String							dosDirectionAttacheId;
	@JsonProperty(DOS_DIRECTION_ATTACHE)
	private String							dosDirectionAttache;
	@JsonProperty(DOS_DIRECTION_RESP_ID)
	private String							dosDirectionRespId;
	@JsonProperty(DOS_DIRECTION_RESP)
	private String							dosDirectionResp;
	@JsonProperty(DOS_DISPOSITION_HABILITATION)
	private Boolean							dosDispositionHabilitation;
	@JsonProperty(DOS_HABILITATION_COMMENTAIRE)
	private String							dosHabilitationCommentaire;
	@JsonProperty(DOS_HABILITATION_DATE_TERME)
	private Date							dosHabilitationDateTerme;
	@JsonProperty(DOS_HABILITATION_NUMERO_ARTICLES)
	private String							dosHabilitationNumeroArticles;
	@JsonProperty(DOS_HABILITATION_NUMERO_ORDRE)
	private String							dosHabilitationNumeroOrdre;
	@JsonProperty(DOS_HABILITATION_REF_LOI)
	private String							dosHabilitationRefLoi;
	@JsonProperty(DOS_HABILITATION_TERME)
	private String							dosHabilitationTerme;
	@JsonProperty(DOS_ID_CREATEUR_DOSSIER)
	private String							dosIdCreateurDossier;
	@JsonProperty(DOS_ID_DOCUMENT_FDD)
	private String							dosIdDocumentFDD;
	@JsonProperty(DOS_ID_DOCUMENT_PARAPHEUR)
	private String							dosIdDocumentParapheur;
	@JsonProperty(DOS_ID_DOSSIER)
	private String							dosIdDossier;
	@JsonProperty(DOS_INDEXATION_DIR)
	private Boolean							dosIndexationDir;
	@JsonProperty(DOS_INDEXATION_DIR_PUB)
	private Boolean							dosIndexationDirPub;
	@JsonProperty(DOS_INDEXATION_MIN)
	private Boolean							dosIndexationMin;
	@JsonProperty(DOS_INDEXATION_MIN_PUB)
	private Boolean							dosIndexationMinPub;
	@JsonProperty(DOS_INDEXATION_SGG)
	private Boolean							dosIndexationSgg;
	@JsonProperty(DOS_INDEXATION_SGG_PUB)
	private Boolean							dosIndexationSggPub;
	@JsonProperty(DOS_IS_ACTE_REFERENCE_FOR_NUMERO_VERSION)
	private Boolean							dosIsActeReferenceForNumeroVersion;
	@JsonProperty(DOS_IS_AFTER_DEMANDE_PUBLICATION)
	private Boolean							dosIsAfterDemandePublication;
	@JsonProperty(DOS_IS_DOSSIER_ISSU_INJECTION)
	private Boolean							dosIsDossierIssuInjection;
	@JsonProperty(DOS_IS_PARAPHEUR_COMPLET)
	private Boolean							dosIsParapheurComplet;
	@JsonProperty(DOS_IS_PARAPHEUR_FICHIER_INFO_NON_RECUPERE)
	private Boolean							dosIsParapheurFichierInfoNonRecupere;
	@JsonProperty(DOS_IS_PARAPHEUR_TYPE_ACTE_UPDATED)
	private Boolean							dosIsParapheurTypeActeUpdated;
	@JsonProperty(DOS_IS_URGENT)
	private Boolean							dosIsUrgent;
	@JsonProperty(DOS_LAST_DOCUMENT_ROUTE)
	private String							dosLastDocumentRoute;
	@JsonProperty(DOS_LIBRE)
	private List<String>					dosLibre;
	@JsonProperty(DOS_MAIL_RESP_DOSSIER)
	private String							dosMailRespDossier;
	@JsonProperty(DOS_MESURE_NOMINATIVE)
	private Boolean							dosMesureNominative;
	@JsonProperty(DOS_MINISTERE_ATTACHE_ID)
	private String							dosMinistereAttacheId;
	@JsonProperty(DOS_MINISTERE_ATTACHE)
	private String							dosMinistereAttache;
	@JsonProperty(DOS_MINISTERE_RESP_ID)
	private String							dosMinistereRespId;
	@JsonProperty(DOS_MINISTERE_RESP)
	private String							dosMinistereResp;
	@JsonProperty(DOS_MOTSCLES)
	private List<String>					dosMotscles;
	@JsonProperty(DOS_NB_DOSSIER_RECTIFIE)
	private Long							dosNbDossierRectifie;
	@JsonProperty(DOS_NOM_COMPLET_CHARGES_MISSIONS)
	private List<String>					dosNomCompletChargesMissions;
	@JsonProperty(DOS_NOM_COMPLET_CONSEILLERS_PMS)
	private List<String>					dosNomCompletConseillersPms;
	@JsonProperty(DOS_NOM_COMPLET_RESP_DOSSIER)
	private String							dosNomCompletRespDossier;
	@JsonProperty(DOS_NOM_RESP_DOSSIER)
	private String							dosNomRespDossier;
	@JsonProperty(DOS_NOR_ATTRIBUE)
	private Boolean							dosNorAttribue;
	@JsonProperty(DOS_NUMERO_NOR)
	private String							dosNumeroNor;
	@JsonProperty(DOS_NUMERO_VERSION_ACTE_OU_EXTRAIT)
	private Long							dosNumeroVersionActeOuExtrait;
	@JsonProperty(DOS_PERIODICITE)
	private String							dosPeriodicite;
	@JsonProperty(DOS_PERIODICITE_RAPPORT)
	private String							dosPeriodiciteRapport;
	@JsonProperty(DOS_POSTE_CREATOR)
	private String							dosPosteCreator;
	@JsonProperty(DOS_POUR_FOURNITURE_EPREUVE)
	private Date							dosPourFournitureEpreuve;
	@JsonProperty(DOS_PRENOM_RESP_DOSSIER)
	private String							dosPrenomRespDossier;
	@JsonProperty(DOS_PUBLICATION_INTEGRALE_OU_EXTRAIT)
	private String							dosPublicationIntegraleOuExtrait;
	@JsonProperty(DOS_PUBLICATION_RAPPORT_PRESENTATION)
	private Boolean							dosPublicationRapportPresentation;
	@JsonProperty(DOS_PUBLICATIONS_CONJOINTES)
	private List<String>					dosPublicationsConjointes;
	@JsonProperty(DOS_PUBLIE)
	private Boolean							dosPublie;
	@JsonProperty(DOS_QUALITE_RESP_DOSSIER)
	private String							dosQualiteRespDossier;
	@JsonProperty(DOS_RUBRIQUES)
	private List<String>					dosRubriques;
	@JsonProperty(DOS_STATUT_ID)
	private String							dosStatutId;
	@JsonProperty(DOS_STATUT)
	private String							dosStatut;
	@JsonProperty(DOS_STATUT_ARCHIVAGE_ID)
	private String							dosStatutArchivageId;
	@JsonProperty(DOS_STATUT_ARCHIVAGE)
	private String							dosStatutArchivage;
	@JsonProperty(DOS_TEL_RESP_DOSSIER)
	private String							dosTelRespDossier;
	@JsonProperty(DOS_TITRE_ACTE)
	private String							dosTitreActe;
	@JsonProperty(DOS_TRANSPOSITION_DIRECTIVE)
	private List<Map<String, Serializable>>	dosTranspositionDirective;
	@JsonProperty(DOS_TRANSPOSITION_DIRECTIVE_NUMERO)
	private List<String>					dosTranspositionDirectiveNumero;
	@JsonProperty(DOS_TRANSPOSITION_ORDO_REFS)
	private List<String>					dosTranspositionOrdoRefs;
	@JsonProperty(DOS_TRANSPOSITION_ORDONNANCE)
	private List<Map<String, Serializable>>	dosTranspositionOrdonnance;
	@JsonProperty(DOS_TYPE_ACTE)
	private String							dosTypeActe;
	@JsonProperty(DOS_VECTEUR_PUBLICATION)
	private List<String>					dosVecteurPublication;
	@JsonProperty(DOS_ZONE_COM_SGG_DILA)
	private String							dosZoneComSggDila;
	@JsonProperty(DOS_TEXTE_ENTREPRISE)
	private Boolean							dosTexteEntreprise;
	@JsonProperty(DOS_DATE_EFFET)
	private List<Date>						dosDateEffet;

	/* note:... */
	@JsonProperty(NOTE_MIME_TYPE)
	private String							noteMimeType;
	@JsonProperty(NOTE_NOTE)
	private String							noteNote;

	/* retdila:... */
	@JsonProperty(RETDILA_DATE_PARUTION_JORF)
	private Date							retdilaDateParutionJorf;
	@JsonProperty(RETDILA_DATE_PROMULGATION)
	private Date							retdilaDatePromulgation;
	@JsonProperty(RETDILA_IS_PUBLICATION_EPREUVAGE_DEMANDE_SUIVANTE)
	private Boolean							retdilaIsPublicationEpreuvageDemandeSuivante;
	@JsonProperty(RETDILA_LEGISLATURE_PUBLICATION)
	private String							retdilaLegislaturePublication;
	@JsonProperty(RETDILA_MODE_PARUTION)
	private String							retdilaModeParution;
	@JsonProperty(RETDILA_NUMERO_TEXTE_PARUTION_JORF)
	private String							retdilaNumeroTexteParutionJorf;
	@JsonProperty(RETDILA_PAGE_PARUTION_JORF)
	private Long							retdilaPageParutionJorf;
	@JsonProperty(RETDILA_PARUTION_BO)
	private List<Map<String, Serializable>>	retdilaParutionBo;
	@JsonProperty(RETDILA_TITRE_OFFICIEL)
	private String							retdilaTitreOfficiel;

	/* tp:... */
	@JsonProperty(TP_AMPLIATION_DESTINATAIRE_MAILS)
	private List<String>					tpAmpliationDestinataireMails;
	@JsonProperty(TP_AMPLIATION_HISTORIQUE)
	private List<Map<String, Serializable>>	tpAmpliationHistorique;
	@JsonProperty(TP_AUTRES_DESTINATAIRES_COMMUNICATION)
	private List<Map<String, Serializable>>	tpAutresDestinatairesCommunication;
	@JsonProperty(TP_CABINET_PM_COMMUNICATION)
	private List<Map<String, Serializable>>	tpCabinetPmCommunication;
	@JsonProperty(TP_CABINET_SG_COMMUNICATION)
	private List<Map<String, Serializable>>	tpCabinetSgCommunication;
	@JsonProperty(TP_CHARGE_MISSION_COMMUNICATION)
	private List<Map<String, Serializable>>	tpChargeMissionCommunication;
	@JsonProperty(TP_CHEMIN_CROIX)
	private Boolean							tpCheminCroix;
	@JsonProperty(TP_COMMENTAIRE_REFERENCE)
	private String							tpCommentaireReference;
	@JsonProperty(TP_DATE_ARRIVE_PAPIER)
	private Date							tpDateArrivePapier;
	@JsonProperty(TP_DATE_RETOUR)
	private Date							tpDateRetour;
	@JsonProperty(TP_EPREUVE)
	private Map<String, Serializable>		tpEpreuve;

	@JsonProperty(TP_MOTIF_RETOUR)
	private String							tpMotifRetour;
	@JsonProperty(TP_NOMS_SIGNATAIRES_COMMUNICATION)
	private String							tpNomsSignatairesCommunication;
	@JsonProperty(TP_NOMS_SIGNATAIRES_RETOUR)
	private String							tpNomsSignatairesRetour;
	@JsonProperty(TP_NOUVELLE_DEMANDE_EPREUVES)
	private Map<String, Serializable>		tpNouvelleDemandeEpreuves;
	@JsonProperty(TP_NUMEROS_LISTE_SIGNATURE_FIELD)
	private List<Map<String, Serializable>>	tpNumerosListeSignatureField;
	@JsonProperty(TP_PAPIER_ARCHIVE)
	private Boolean							tpPapierArchive;
	@JsonProperty(TP_PERSONNES_NOMMEES)
	private String							tpPersonnesNommees;
	@JsonProperty(TP_PRIORITE)
	private String							tpPriorite;
	@JsonProperty(TP_PUBLICATION_DATE)
	private Date							tpPublicationDate;
	@JsonProperty(TP_PUBLICATION_DATE_DEMANDE)
	private Date							tpPublicationDateDemande;
	@JsonProperty(TP_PUBLICATION_DATE_ENVOI_JO)
	private Date							tpPublicationDateEnvoiJo;
	@JsonProperty(TP_PUBLICATION_DELAI)
	private String							tpPublicationDelai;
	@JsonProperty(TP_PUBLICATION_EPREUVE_EN_RETOUR)
	private Boolean							tpPublicationEpreuveEnRetour;
	@JsonProperty(TP_PUBLICATION_MODE_PUBLICATION)
	private String							tpPublicationModePublication;
	@JsonProperty(TP_PUBLICATION_NOM_PUBLICATION)
	private String							tpPublicationNomPublication;
	@JsonProperty(TP_PUBLICATION_NUMERO_LISTE)
	private String							tpPublicationNumeroListe;
	@JsonProperty(TP_RETOUR_A)
	private String							tpRetourA;
	@JsonProperty(TP_RETOUR_DU_BONA_TITRER_LE)
	private Date							tpRetourDuBonaTitrerLe;
	@JsonProperty(TP_SIGNATAIRE)
	private String							tpSignataire;
	@JsonProperty(TP_SIGNATURE_DESTINATAIRE_CPM)
	private String							tpSignatureDestinataireCPM;
	@JsonProperty(TP_SIGNATURE_DESTINATAIRE_SGG)
	private String							tpSignatureDestinataireSGG;
	@JsonProperty(TP_SIGNATURE_PM)
	private Map<String, Serializable>		tpSignaturePm;
	@JsonProperty(TP_SIGNATURE_PR)
	private Map<String, Serializable>		tpSignaturePr;
	@JsonProperty(TP_SIGNATURE_SGG)
	private Map<String, Serializable>		tpSignatureSgg;
	@JsonProperty(TP_TEXTE_APUBLIER)
	private Boolean							tpTexteAPublier;
	@JsonProperty(TP_TEXTE_SOUMIS_AVALIDATION)
	private Boolean							tpTexteSoumisAValidation;

	@JsonProperty(PERMS)
	private Collection<String>				perms;

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the caseDocumentsId
	 */
	public List<String> getCaseDocumentsId() {
		return caseDocumentsId;
	}

	/**
	 * @param caseDocumentsId
	 *            the caseDocumentsId to set
	 */
	public void setCaseDocumentsId(List<String> caseDocumentsId) {
		this.caseDocumentsId = caseDocumentsId;
	}

	/**
	 * @return the cmdistAllActionParticipantMailboxes
	 */
	public List<String> getCmdistAllActionParticipantMailboxes() {
		return cmdistAllActionParticipantMailboxes;
	}

	/**
	 * @param cmdistAllActionParticipantMailboxes
	 *            the cmdistAllActionParticipantMailboxes to set
	 */
	public void setCmdistAllActionParticipantMailboxes(List<String> cmdistAllActionParticipantMailboxes) {
		this.cmdistAllActionParticipantMailboxes = cmdistAllActionParticipantMailboxes;
	}

	/**
	 * @return the cmdistAllCopyParticipantMailboxes
	 */
	public List<String> getCmdistAllCopyParticipantMailboxes() {
		return cmdistAllCopyParticipantMailboxes;
	}

	/**
	 * @param cmdistAllCopyParticipantMailboxes
	 *            the cmdistAllCopyParticipantMailboxes to set
	 */
	public void setCmdistAllCopyParticipantMailboxes(List<String> cmdistAllCopyParticipantMailboxes) {
		this.cmdistAllCopyParticipantMailboxes = cmdistAllCopyParticipantMailboxes;
	}

	/**
	 * @return the cmdistInitialActionExternalParticipantMailboxes
	 */
	public List<String> getCmdistInitialActionExternalParticipantMailboxes() {
		return cmdistInitialActionExternalParticipantMailboxes;
	}

	/**
	 * @param cmdistInitialActionExternalParticipantMailboxes
	 *            the cmdistInitialActionExternalParticipantMailboxes to set
	 */
	public void setCmdistInitialActionExternalParticipantMailboxes(
			List<String> cmdistInitialActionExternalParticipantMailboxes) {
		this.cmdistInitialActionExternalParticipantMailboxes = cmdistInitialActionExternalParticipantMailboxes;
	}

	/**
	 * @return the cmdistInitialActionInternalParticipantMailboxes
	 */
	public List<String> getCmdistInitialActionInternalParticipantMailboxes() {
		return cmdistInitialActionInternalParticipantMailboxes;
	}

	/**
	 * @param cmdistInitialActionInternalParticipantMailboxes
	 *            the cmdistInitialActionInternalParticipantMailboxes to set
	 */
	public void setCmdistInitialActionInternalParticipantMailboxes(
			List<String> cmdistInitialActionInternalParticipantMailboxes) {
		this.cmdistInitialActionInternalParticipantMailboxes = cmdistInitialActionInternalParticipantMailboxes;
	}

	/**
	 * @return the cmdistInitialCopyExternalParticipantMailboxes
	 */
	public List<String> getCmdistInitialCopyExternalParticipantMailboxes() {
		return cmdistInitialCopyExternalParticipantMailboxes;
	}

	/**
	 * @param cmdistInitialCopyExternalParticipantMailboxes
	 *            the cmdistInitialCopyExternalParticipantMailboxes to set
	 */
	public void setCmdistInitialCopyExternalParticipantMailboxes(
			List<String> cmdistInitialCopyExternalParticipantMailboxes) {
		this.cmdistInitialCopyExternalParticipantMailboxes = cmdistInitialCopyExternalParticipantMailboxes;
	}

	/**
	 * @return the cmdistInitialCopyInternalParticipantMailboxes
	 */
	public List<String> getCmdistInitialCopyInternalParticipantMailboxes() {
		return cmdistInitialCopyInternalParticipantMailboxes;
	}

	/**
	 * @param cmdistInitialCopyInternalParticipantMailboxes
	 *            the cmdistInitialCopyInternalParticipantMailboxes to set
	 */
	public void setCmdistInitialCopyInternalParticipantMailboxes(
			List<String> cmdistInitialCopyInternalParticipantMailboxes) {
		this.cmdistInitialCopyInternalParticipantMailboxes = cmdistInitialCopyInternalParticipantMailboxes;
	}

	/**
	 * @return the commonIcon
	 */
	public String getCommonIcon() {
		return commonIcon;
	}

	/**
	 * @param commonIcon
	 *            the commonIcon to set
	 */
	public void setCommonIcon(String commonIcon) {
		this.commonIcon = commonIcon;
	}

	/**
	 * @return the commonIconExpanded
	 */
	public String getCommonIconExpanded() {
		return commonIconExpanded;
	}

	/**
	 * @param commonIconExpanded
	 *            the commonIconExpanded to set
	 */
	public void setCommonIconExpanded(String commonIconExpanded) {
		this.commonIconExpanded = commonIconExpanded;
	}

	/**
	 * @return the commonSize
	 */
	public String getCommonSize() {
		return commonSize;
	}

	/**
	 * @param commonSize
	 *            the commonSize to set
	 */
	public void setCommonSize(String commonSize) {
		this.commonSize = commonSize;
	}

	/**
	 * @return the consetatDateAgCe
	 */
	public Date getConsetatDateAgCe() {
		return consetatDateAgCe;
	}

	/**
	 * @param consetatDateAgCe
	 *            the consetatDateAgCe to set
	 */
	public void setConsetatDateAgCe(Date consetatDateAgCe) {
		this.consetatDateAgCe = consetatDateAgCe;
	}

	/**
	 * @return the consetatDateSaisineCE
	 */
	public Date getConsetatDateSaisineCE() {
		return consetatDateSaisineCE;
	}

	/**
	 * @param consetatDateSaisineCE
	 *            the consetatDateSaisineCE to set
	 */
	public void setConsetatDateSaisineCE(Date consetatDateSaisineCE) {
		this.consetatDateSaisineCE = consetatDateSaisineCE;
	}

	/**
	 * @return the consetatDateSectionCe
	 */
	public Date getConsetatDateSectionCe() {
		return consetatDateSectionCe;
	}

	/**
	 * @param consetatDateSectionCe
	 *            the consetatDateSectionCe to set
	 */
	public void setConsetatDateSectionCe(Date consetatDateSectionCe) {
		this.consetatDateSectionCe = consetatDateSectionCe;
	}

	/**
	 * @return the consetatDateSortieCE
	 */
	public Date getConsetatDateSortieCE() {
		return consetatDateSortieCE;
	}

	/**
	 * @param consetatDateSortieCE
	 *            the consetatDateSortieCE to set
	 */
	public void setConsetatDateSortieCE(Date consetatDateSortieCE) {
		this.consetatDateSortieCE = consetatDateSortieCE;
	}

	/**
	 * @return the consetatDateTransmissionSectionCe
	 */
	public Date getConsetatDateTransmissionSectionCe() {
		return consetatDateTransmissionSectionCe;
	}

	/**
	 * @param consetatDateTransmissionSectionCe
	 *            the consetatDateTransmissionSectionCe to set
	 */
	public void setConsetatDateTransmissionSectionCe(Date consetatDateTransmissionSectionCe) {
		this.consetatDateTransmissionSectionCe = consetatDateTransmissionSectionCe;
	}

	/**
	 * @return the consetatNumeroISA
	 */
	public String getConsetatNumeroISA() {
		return consetatNumeroISA;
	}

	/**
	 * @param consetatNumeroISA
	 *            the consetatNumeroISA to set
	 */
	public void setConsetatNumeroISA(String consetatNumeroISA) {
		this.consetatNumeroISA = consetatNumeroISA;
	}

	/**
	 * @return the consetatRapporteurCe
	 */
	public String getConsetatRapporteurCe() {
		return consetatRapporteurCe;
	}

	/**
	 * @param consetatRapporteurCe
	 *            the consetatRapporteurCe to set
	 */
	public void setConsetatRapporteurCe(String consetatRapporteurCe) {
		this.consetatRapporteurCe = consetatRapporteurCe;
	}

	/**
	 * @return the consetatReceptionAvisCE
	 */
	public String getConsetatReceptionAvisCE() {
		return consetatReceptionAvisCE;
	}

	/**
	 * @param consetatReceptionAvisCE
	 *            the consetatReceptionAvisCE to set
	 */
	public void setConsetatReceptionAvisCE(String consetatReceptionAvisCE) {
		this.consetatReceptionAvisCE = consetatReceptionAvisCE;
	}

	/**
	 * @return the consetatSectionCe
	 */
	public String getConsetatSectionCe() {
		return consetatSectionCe;
	}

	/**
	 * @param consetatSectionCe
	 *            the consetatSectionCe to set
	 */
	public void setConsetatSectionCe(String consetatSectionCe) {
		this.consetatSectionCe = consetatSectionCe;
	}

	/**
	 * @return the dcContributors
	 */
	public String[] getDcContributors() {
		return dcContributors;
	}

	/**
	 * @param dcContributors
	 *            the dcContributors to set
	 */
	public void setDcContributors(String[] dcContributors) {
		this.dcContributors = dcContributors;
	}

	/**
	 * @return the dcCoverage
	 */
	public String getDcCoverage() {
		return dcCoverage;
	}

	/**
	 * @param dcCoverage
	 *            the dcCoverage to set
	 */
	public void setDcCoverage(String dcCoverage) {
		this.dcCoverage = dcCoverage;
	}

	/**
	 * @return the dcCreated
	 */
	public Date getDcCreated() {
		return dcCreated;
	}

	/**
	 * @param dcCreated
	 *            the dcCreated to set
	 */
	public void setDcCreated(Date dcCreated) {
		this.dcCreated = dcCreated;
	}

	/**
	 * @return the dcCreator
	 */
	public String getDcCreator() {
		return dcCreator;
	}

	/**
	 * @param dcCreator
	 *            the dcCreator to set
	 */
	public void setDcCreator(String dcCreator) {
		this.dcCreator = dcCreator;
	}

	/**
	 * @return the dcExpired
	 */
	public Date getDcExpired() {
		return dcExpired;
	}

	/**
	 * @param dcExpired
	 *            the dcExpired to set
	 */
	public void setDcExpired(Date dcExpired) {
		this.dcExpired = dcExpired;
	}

	/**
	 * @return the dcFormat
	 */
	public String getDcFormat() {
		return dcFormat;
	}

	/**
	 * @param dcFormat
	 *            the dcFormat to set
	 */
	public void setDcFormat(String dcFormat) {
		this.dcFormat = dcFormat;
	}

	/**
	 * @return the dcIssued
	 */
	public String getDcIssued() {
		return dcIssued;
	}

	/**
	 * @param dcIssued
	 *            the dcIssued to set
	 */
	public void setDcIssued(String dcIssued) {
		this.dcIssued = dcIssued;
	}

	/**
	 * @return the dcLanguage
	 */
	public String getDcLanguage() {
		return dcLanguage;
	}

	/**
	 * @param dcLanguage
	 *            the dcLanguage to set
	 */
	public void setDcLanguage(String dcLanguage) {
		this.dcLanguage = dcLanguage;
	}

	/**
	 * @return the dcLastContributor
	 */
	public String getDcLastContributor() {
		return dcLastContributor;
	}

	/**
	 * @param dcLastContributor
	 *            the dcLastContributor to set
	 */
	public void setDcLastContributor(String dcLastContributor) {
		this.dcLastContributor = dcLastContributor;
	}

	/**
	 * @return the dcModified
	 */
	public Date getDcModified() {
		return dcModified;
	}

	/**
	 * @param dcModified
	 *            the dcModified to set
	 */
	public void setDcModified(Date dcModified) {
		this.dcModified = dcModified;
	}

	/**
	 * @return the dcNature
	 */
	public String getDcNature() {
		return dcNature;
	}

	/**
	 * @param dcNature
	 *            the dcNature to set
	 */
	public void setDcNature(String dcNature) {
		this.dcNature = dcNature;
	}

	/**
	 * @return the dcRights
	 */
	public String getDcRights() {
		return dcRights;
	}

	/**
	 * @param dcRights
	 *            the dcRights to set
	 */
	public void setDcRights(String dcRights) {
		this.dcRights = dcRights;
	}

	/**
	 * @return the dcSource
	 */
	public String getDcSource() {
		return dcSource;
	}

	/**
	 * @param dcSource
	 *            the dcSource to set
	 */
	public void setDcSource(String dcSource) {
		this.dcSource = dcSource;
	}

	/**
	 * @return the dcSubjects
	 */
	public String[] getDcSubjects() {
		return dcSubjects;
	}

	/**
	 * @param dcSubjects
	 *            the dcSubjects to set
	 */
	public void setDcSubjects(String[] dcSubjects) {
		this.dcSubjects = dcSubjects;
	}

	/**
	 * @return the dcTitle
	 */
	public String getDcTitle() {
		return dcTitle;
	}

	/**
	 * @param dcTitle
	 *            the dcTitle to set
	 */
	public void setDcTitle(String dcTitle) {
		this.dcTitle = dcTitle;
	}

	/**
	 * @return the dcValid
	 */
	public String getDcValid() {
		return dcValid;
	}

	/**
	 * @param dcValid
	 *            the dcValid to set
	 */
	public void setDcValid(String dcValid) {
		this.dcValid = dcValid;
	}

	/**
	 * @return the dosAdoption
	 */
	public Boolean getDosAdoption() {
		return dosAdoption;
	}

	/**
	 * @param dosAdoption
	 *            the dosAdoption to set
	 */
	public void setDosAdoption(Boolean dosAdoption) {
		this.dosAdoption = dosAdoption;
	}

	/**
	 * @return the dosApplicationLoi
	 */
	public List<Map<String, Serializable>> getDosApplicationLoi() {
		return dosApplicationLoi;
	}

	/**
	 * @param dosApplicationLoi
	 *            the dosApplicationLoi to set
	 */
	public void setDosApplicationLoi(List<Map<String, Serializable>> dosApplicationLoi) {
		this.dosApplicationLoi = dosApplicationLoi;
	}

	/**
	 * @return the dosApplicationLoiRefs
	 */
	public List<String> getDosApplicationLoiRefs() {
		return dosApplicationLoiRefs;
	}

	/**
	 * @param dosApplicationLoiRefs
	 *            the dosApplicationLoiRefs to set
	 */
	public void setDosApplicationLoiRefs(List<String> dosApplicationLoiRefs) {
		this.dosApplicationLoiRefs = dosApplicationLoiRefs;
	}

	/**
	 * @return the dosArchive
	 */
	public Boolean getDosArchive() {
		return dosArchive;
	}

	/**
	 * @param dosArchive
	 *            the dosArchive to set
	 */
	public void setDosArchive(Boolean dosArchive) {
		this.dosArchive = dosArchive;
	}

	/**
	 * @return the dosArriveeSolonTS
	 */
	public Boolean getDosArriveeSolonTS() {
		return dosArriveeSolonTS;
	}

	/**
	 * @param dosArriveeSolonTS
	 *            the dosArriveeSolonTS to set
	 */
	public void setDosArriveeSolonTS(Boolean dosArriveeSolonTS) {
		this.dosArriveeSolonTS = dosArriveeSolonTS;
	}

	/**
	 * @return the dosBaseLegaleActe
	 */
	public String getDosBaseLegaleActe() {
		return dosBaseLegaleActe;
	}

	/**
	 * @param dosBaseLegaleActe
	 *            the dosBaseLegaleActe to set
	 */
	public void setDosBaseLegaleActe(String dosBaseLegaleActe) {
		this.dosBaseLegaleActe = dosBaseLegaleActe;
	}

	/**
	 * @return the dosBaseLegaleDate
	 */
	public Date getDosBaseLegaleDate() {
		return dosBaseLegaleDate;
	}

	/**
	 * @param dosBaseLegaleDate
	 *            the dosBaseLegaleDate to set
	 */
	public void setDosBaseLegaleDate(Date dosBaseLegaleDate) {
		this.dosBaseLegaleDate = dosBaseLegaleDate;
	}

	/**
	 * @return the dosBaseLegaleNatureTexte
	 */
	public String getDosBaseLegaleNatureTexte() {
		return dosBaseLegaleNatureTexte;
	}

	/**
	 * @param dosBaseLegaleNatureTexte
	 *            the dosBaseLegaleNatureTexte to set
	 */
	public void setDosBaseLegaleNatureTexte(String dosBaseLegaleNatureTexte) {
		this.dosBaseLegaleNatureTexte = dosBaseLegaleNatureTexte;
	}

	/**
	 * @return the dosBaseLegaleNumeroTexte
	 */
	public String getDosBaseLegaleNumeroTexte() {
		return dosBaseLegaleNumeroTexte;
	}

	/**
	 * @param dosBaseLegaleNumeroTexte
	 *            the dosBaseLegaleNumeroTexte to set
	 */
	public void setDosBaseLegaleNumeroTexte(String dosBaseLegaleNumeroTexte) {
		this.dosBaseLegaleNumeroTexte = dosBaseLegaleNumeroTexte;
	}

	/**
	 * @return the dosCandidat
	 */
	public String getDosCandidat() {
		return dosCandidat;
	}

	/**
	 * @param dosCandidat
	 *            the dosCandidat to set
	 */
	public void setDosCandidat(String dosCandidat) {
		this.dosCandidat = dosCandidat;
	}

	/**
	 * @return the dosCategorieActe
	 */
	public String getDosCategorieActe() {
		return dosCategorieActe;
	}

	/**
	 * @param dosCategorieActe
	 *            the dosCategorieActe to set
	 */
	public void setDosCategorieActe(String dosCategorieActe) {
		this.dosCategorieActe = dosCategorieActe;
	}

	/**
	 * @return the dosCreated
	 */
	public Date getDosCreated() {
		return dosCreated;
	}

	/**
	 * @param dosCreated
	 *            the dosCreated to set
	 */
	public void setDosCreated(Date dosCreated) {
		this.dosCreated = dosCreated;
	}

	/**
	 * @return the dosDateCandidature
	 */
	public Date getDosDateCandidature() {
		return dosDateCandidature;
	}

	/**
	 * @param dosDateCandidature
	 *            the dosDateCandidature to set
	 */
	public void setDosDateCandidature(Date dosDateCandidature) {
		this.dosDateCandidature = dosDateCandidature;
	}

	/**
	 * @return the dosDateDeMaintienEnProduction
	 */
	public Date getDosDateDeMaintienEnProduction() {
		return dosDateDeMaintienEnProduction;
	}

	/**
	 * @param dosDateDeMaintienEnProduction
	 *            the dosDateDeMaintienEnProduction to set
	 */
	public void setDosDateDeMaintienEnProduction(Date dosDateDeMaintienEnProduction) {
		this.dosDateDeMaintienEnProduction = dosDateDeMaintienEnProduction;
	}

	/**
	 * @return the dosDateEnvoiJoTS
	 */
	public Date getDosDateEnvoiJoTS() {
		return dosDateEnvoiJoTS;
	}

	/**
	 * @param dosDateEnvoiJoTS
	 *            the dosDateEnvoiJoTS to set
	 */
	public void setDosDateEnvoiJoTS(Date dosDateEnvoiJoTS) {
		this.dosDateEnvoiJoTS = dosDateEnvoiJoTS;
	}

	/**
	 * @return the dosDatePreciseePublication
	 */
	public Date getDosDatePreciseePublication() {
		return dosDatePreciseePublication;
	}

	/**
	 * @param dosDatePreciseePublication
	 *            the dosDatePreciseePublication to set
	 */
	public void setDosDatePreciseePublication(Date dosDatePreciseePublication) {
		this.dosDatePreciseePublication = dosDatePreciseePublication;
	}

	/**
	 * @return the dosDatePublication
	 */
	public Date getDosDatePublication() {
		return dosDatePublication;
	}

	/**
	 * @param dosDatePublication
	 *            the dosDatePublication to set
	 */
	public void setDosDatePublication(Date dosDatePublication) {
		this.dosDatePublication = dosDatePublication;
	}

	/**
	 * @return the dosDateSignature
	 */
	public Date getDosDateSignature() {
		return dosDateSignature;
	}

	/**
	 * @param dosDateSignature
	 *            the dosDateSignature to set
	 */
	public void setDosDateSignature(Date dosDateSignature) {
		this.dosDateSignature = dosDateSignature;
	}

	/**
	 * @return the dosDateVersementArchivageIntermediaire
	 */
	public Date getDosDateVersementArchivageIntermediaire() {
		return dosDateVersementArchivageIntermediaire;
	}

	/**
	 * @param dosDateVersementArchivageIntermediaire
	 *            the dosDateVersementArchivageIntermediaire to set
	 */
	public void setDosDateVersementArchivageIntermediaire(Date dosDateVersementArchivageIntermediaire) {
		this.dosDateVersementArchivageIntermediaire = dosDateVersementArchivageIntermediaire;
	}

	/**
	 * @return the dosDateVersementTS
	 */
	public Date getDosDateVersementTS() {
		return dosDateVersementTS;
	}

	/**
	 * @param dosDateVersementTS
	 *            the dosDateVersementTS to set
	 */
	public void setDosDateVersementTS(Date dosDateVersementTS) {
		this.dosDateVersementTS = dosDateVersementTS;
	}

	/**
	 * @return the dosDecretNumerote
	 */
	public Boolean getDosDecretNumerote() {
		return dosDecretNumerote;
	}

	/**
	 * @param dosDecretNumerote
	 *            the dosDecretNumerote to set
	 */
	public void setDosDecretNumerote(Boolean dosDecretNumerote) {
		this.dosDecretNumerote = dosDecretNumerote;
	}

	/**
	 * @return the dosDelaiPublication
	 */
	public String getDosDelaiPublication() {
		return dosDelaiPublication;
	}

	/**
	 * @param dosDelaiPublication
	 *            the dosDelaiPublication to set
	 */
	public void setDosDelaiPublication(String dosDelaiPublication) {
		this.dosDelaiPublication = dosDelaiPublication;
	}

	/**
	 * @return the dosDeleted
	 */
	public Boolean getDosDeleted() {
		return dosDeleted;
	}

	/**
	 * @param dosDeleted
	 *            the dosDeleted to set
	 */
	public void setDosDeleted(Boolean dosDeleted) {
		this.dosDeleted = dosDeleted;
	}

	/**
	 * @return the dosDirectionAttache
	 */
	public String getDosDirectionAttache() {
		return dosDirectionAttache;
	}

	/**
	 * @param dosDirectionAttache
	 *            the dosDirectionAttache to set
	 */
	public void setDosDirectionAttache(String dosDirectionAttache) {
		this.dosDirectionAttache = dosDirectionAttache;
	}

	/**
	 * @return the dosDirectionResp
	 */
	public String getDosDirectionResp() {
		return dosDirectionResp;
	}

	/**
	 * @param dosDirectionResp
	 *            the dosDirectionResp to set
	 */
	public void setDosDirectionResp(String dosDirectionResp) {
		this.dosDirectionResp = dosDirectionResp;
	}

	/**
	 * @return the dosDispositionHabilitation
	 */
	public Boolean getDosDispositionHabilitation() {
		return dosDispositionHabilitation;
	}

	/**
	 * @param dosDispositionHabilitation
	 *            the dosDispositionHabilitation to set
	 */
	public void setDosDispositionHabilitation(Boolean dosDispositionHabilitation) {
		this.dosDispositionHabilitation = dosDispositionHabilitation;
	}

	/**
	 * @return the dosHabilitationCommentaire
	 */
	public String getDosHabilitationCommentaire() {
		return dosHabilitationCommentaire;
	}

	/**
	 * @param dosHabilitationCommentaire
	 *            the dosHabilitationCommentaire to set
	 */
	public void setDosHabilitationCommentaire(String dosHabilitationCommentaire) {
		this.dosHabilitationCommentaire = dosHabilitationCommentaire;
	}

	/**
	 * @return the dosHabilitationDateTerme
	 */
	public Date getDosHabilitationDateTerme() {
		return dosHabilitationDateTerme;
	}

	/**
	 * @param dosHabilitationDateTerme
	 *            the dosHabilitationDateTerme to set
	 */
	public void setDosHabilitationDateTerme(Date dosHabilitationDateTerme) {
		this.dosHabilitationDateTerme = dosHabilitationDateTerme;
	}

	/**
	 * @return the dosHabilitationNumeroArticles
	 */
	public String getDosHabilitationNumeroArticles() {
		return dosHabilitationNumeroArticles;
	}

	/**
	 * @param dosHabilitationNumeroArticles
	 *            the dosHabilitationNumeroArticles to set
	 */
	public void setDosHabilitationNumeroArticles(String dosHabilitationNumeroArticles) {
		this.dosHabilitationNumeroArticles = dosHabilitationNumeroArticles;
	}

	/**
	 * @return the dosHabilitationNumeroOrdre
	 */
	public String getDosHabilitationNumeroOrdre() {
		return dosHabilitationNumeroOrdre;
	}

	/**
	 * @param dosHabilitationNumeroOrdre
	 *            the dosHabilitationNumeroOrdre to set
	 */
	public void setDosHabilitationNumeroOrdre(String dosHabilitationNumeroOrdre) {
		this.dosHabilitationNumeroOrdre = dosHabilitationNumeroOrdre;
	}

	/**
	 * @return the dosHabilitationRefLoi
	 */
	public String getDosHabilitationRefLoi() {
		return dosHabilitationRefLoi;
	}

	/**
	 * @param dosHabilitationRefLoi
	 *            the dosHabilitationRefLoi to set
	 */
	public void setDosHabilitationRefLoi(String dosHabilitationRefLoi) {
		this.dosHabilitationRefLoi = dosHabilitationRefLoi;
	}

	/**
	 * @return the dosHabilitationTerme
	 */
	public String getDosHabilitationTerme() {
		return dosHabilitationTerme;
	}

	/**
	 * @param dosHabilitationTerme
	 *            the dosHabilitationTerme to set
	 */
	public void setDosHabilitationTerme(String dosHabilitationTerme) {
		this.dosHabilitationTerme = dosHabilitationTerme;
	}

	/**
	 * @return the dosIdCreateurDossier
	 */
	public String getDosIdCreateurDossier() {
		return dosIdCreateurDossier;
	}

	/**
	 * @param dosIdCreateurDossier
	 *            the dosIdCreateurDossier to set
	 */
	public void setDosIdCreateurDossier(String dosIdCreateurDossier) {
		this.dosIdCreateurDossier = dosIdCreateurDossier;
	}

	/**
	 * @return the dosIdDocumentFDD
	 */
	public String getDosIdDocumentFDD() {
		return dosIdDocumentFDD;
	}

	/**
	 * @param dosIdDocumentFDD
	 *            the dosIdDocumentFDD to set
	 */
	public void setDosIdDocumentFDD(String dosIdDocumentFDD) {
		this.dosIdDocumentFDD = dosIdDocumentFDD;
	}

	/**
	 * @return the dosIdDocumentParapheur
	 */
	public String getDosIdDocumentParapheur() {
		return dosIdDocumentParapheur;
	}

	/**
	 * @param dosIdDocumentParapheur
	 *            the dosIdDocumentParapheur to set
	 */
	public void setDosIdDocumentParapheur(String dosIdDocumentParapheur) {
		this.dosIdDocumentParapheur = dosIdDocumentParapheur;
	}

	/**
	 * @return the dosIdDossier
	 */
	public String getDosIdDossier() {
		return dosIdDossier;
	}

	/**
	 * @param dosIdDossier
	 *            the dosIdDossier to set
	 */
	public void setDosIdDossier(String dosIdDossier) {
		this.dosIdDossier = dosIdDossier;
	}

	/**
	 * @return the dosIndexationDir
	 */
	public Boolean getDosIndexationDir() {
		return dosIndexationDir;
	}

	/**
	 * @param dosIndexationDir
	 *            the dosIndexationDir to set
	 */
	public void setDosIndexationDir(Boolean dosIndexationDir) {
		this.dosIndexationDir = dosIndexationDir;
	}

	/**
	 * @return the dosIndexationDirPub
	 */
	public Boolean getDosIndexationDirPub() {
		return dosIndexationDirPub;
	}

	/**
	 * @param dosIndexationDirPub
	 *            the dosIndexationDirPub to set
	 */
	public void setDosIndexationDirPub(Boolean dosIndexationDirPub) {
		this.dosIndexationDirPub = dosIndexationDirPub;
	}

	/**
	 * @return the dosIndexationMin
	 */
	public Boolean getDosIndexationMin() {
		return dosIndexationMin;
	}

	/**
	 * @param dosIndexationMin
	 *            the dosIndexationMin to set
	 */
	public void setDosIndexationMin(Boolean dosIndexationMin) {
		this.dosIndexationMin = dosIndexationMin;
	}

	/**
	 * @return the dosIndexationMinPub
	 */
	public Boolean getDosIndexationMinPub() {
		return dosIndexationMinPub;
	}

	/**
	 * @param dosIndexationMinPub
	 *            the dosIndexationMinPub to set
	 */
	public void setDosIndexationMinPub(Boolean dosIndexationMinPub) {
		this.dosIndexationMinPub = dosIndexationMinPub;
	}

	/**
	 * @return the dosIndexationSgg
	 */
	public Boolean getDosIndexationSgg() {
		return dosIndexationSgg;
	}

	/**
	 * @param dosIndexationSgg
	 *            the dosIndexationSgg to set
	 */
	public void setDosIndexationSgg(Boolean dosIndexationSgg) {
		this.dosIndexationSgg = dosIndexationSgg;
	}

	/**
	 * @return the dosIndexationSggPub
	 */
	public Boolean getDosIndexationSggPub() {
		return dosIndexationSggPub;
	}

	/**
	 * @param dosIndexationSggPub
	 *            the dosIndexationSggPub to set
	 */
	public void setDosIndexationSggPub(Boolean dosIndexationSggPub) {
		this.dosIndexationSggPub = dosIndexationSggPub;
	}

	/**
	 * @return the dosIsActeReferenceForNumeroVersion
	 */
	public Boolean getDosIsActeReferenceForNumeroVersion() {
		return dosIsActeReferenceForNumeroVersion;
	}

	/**
	 * @param dosIsActeReferenceForNumeroVersion
	 *            the dosIsActeReferenceForNumeroVersion to set
	 */
	public void setDosIsActeReferenceForNumeroVersion(Boolean dosIsActeReferenceForNumeroVersion) {
		this.dosIsActeReferenceForNumeroVersion = dosIsActeReferenceForNumeroVersion;
	}

	/**
	 * @return the dosIsAfterDemandePublication
	 */
	public Boolean getDosIsAfterDemandePublication() {
		return dosIsAfterDemandePublication;
	}

	/**
	 * @param dosIsAfterDemandePublication
	 *            the dosIsAfterDemandePublication to set
	 */
	public void setDosIsAfterDemandePublication(Boolean dosIsAfterDemandePublication) {
		this.dosIsAfterDemandePublication = dosIsAfterDemandePublication;
	}

	/**
	 * @return the dosIsDossierIssuInjection
	 */
	public Boolean getDosIsDossierIssuInjection() {
		return dosIsDossierIssuInjection;
	}

	/**
	 * @param dosIsDossierIssuInjection
	 *            the dosIsDossierIssuInjection to set
	 */
	public void setDosIsDossierIssuInjection(Boolean dosIsDossierIssuInjection) {
		this.dosIsDossierIssuInjection = dosIsDossierIssuInjection;
	}

	/**
	 * @return the dosIsParapheurComplet
	 */
	public Boolean getDosIsParapheurComplet() {
		return dosIsParapheurComplet;
	}

	/**
	 * @param dosIsParapheurComplet
	 *            the dosIsParapheurComplet to set
	 */
	public void setDosIsParapheurComplet(Boolean dosIsParapheurComplet) {
		this.dosIsParapheurComplet = dosIsParapheurComplet;
	}

	/**
	 * @return the dosIsParapheurFichierInfoNonRecupere
	 */
	public Boolean getDosIsParapheurFichierInfoNonRecupere() {
		return dosIsParapheurFichierInfoNonRecupere;
	}

	/**
	 * @param dosIsParapheurFichierInfoNonRecupere
	 *            the dosIsParapheurFichierInfoNonRecupere to set
	 */
	public void setDosIsParapheurFichierInfoNonRecupere(Boolean dosIsParapheurFichierInfoNonRecupere) {
		this.dosIsParapheurFichierInfoNonRecupere = dosIsParapheurFichierInfoNonRecupere;
	}

	/**
	 * @return the dosIsParapheurTypeActeUpdated
	 */
	public Boolean getDosIsParapheurTypeActeUpdated() {
		return dosIsParapheurTypeActeUpdated;
	}

	/**
	 * @param dosIsParapheurTypeActeUpdated
	 *            the dosIsParapheurTypeActeUpdated to set
	 */
	public void setDosIsParapheurTypeActeUpdated(Boolean dosIsParapheurTypeActeUpdated) {
		this.dosIsParapheurTypeActeUpdated = dosIsParapheurTypeActeUpdated;
	}

	/**
	 * @return the dosIsUrgent
	 */
	public Boolean getDosIsUrgent() {
		return dosIsUrgent;
	}

	/**
	 * @param dosIsUrgent
	 *            the dosIsUrgent to set
	 */
	public void setDosIsUrgent(Boolean dosIsUrgent) {
		this.dosIsUrgent = dosIsUrgent;
	}

	/**
	 * @return the dosLastDocumentRoute
	 */
	public String getDosLastDocumentRoute() {
		return dosLastDocumentRoute;
	}

	/**
	 * @param dosLastDocumentRoute
	 *            the dosLastDocumentRoute to set
	 */
	public void setDosLastDocumentRoute(String dosLastDocumentRoute) {
		this.dosLastDocumentRoute = dosLastDocumentRoute;
	}

	/**
	 * @return the dosLibre
	 */
	public List<String> getDosLibre() {
		return dosLibre;
	}

	/**
	 * @param dosLibre
	 *            the dosLibre to set
	 */
	public void setDosLibre(List<String> dosLibre) {
		this.dosLibre = dosLibre;
	}

	/**
	 * @return the dosMailRespDossier
	 */
	public String getDosMailRespDossier() {
		return dosMailRespDossier;
	}

	/**
	 * @param dosMailRespDossier
	 *            the dosMailRespDossier to set
	 */
	public void setDosMailRespDossier(String dosMailRespDossier) {
		this.dosMailRespDossier = dosMailRespDossier;
	}

	/**
	 * @return the dosMesureNominative
	 */
	public Boolean getDosMesureNominative() {
		return dosMesureNominative;
	}

	/**
	 * @param dosMesureNominative
	 *            the dosMesureNominative to set
	 */
	public void setDosMesureNominative(Boolean dosMesureNominative) {
		this.dosMesureNominative = dosMesureNominative;
	}

	/**
	 * @return the dosMinistereAttache
	 */
	public String getDosMinistereAttache() {
		return dosMinistereAttache;
	}

	/**
	 * @param dosMinistereAttache
	 *            the dosMinistereAttache to set
	 */
	public void setDosMinistereAttache(String dosMinistereAttache) {
		this.dosMinistereAttache = dosMinistereAttache;
	}

	/**
	 * @return the dosMinistereResp
	 */
	public String getDosMinistereResp() {
		return dosMinistereResp;
	}

	/**
	 * @param dosMinistereResp
	 *            the dosMinistereResp to set
	 */
	public void setDosMinistereResp(String dosMinistereResp) {
		this.dosMinistereResp = dosMinistereResp;
	}

	/**
	 * @return the dosMotscles
	 */
	public List<String> getDosMotscles() {
		return dosMotscles;
	}

	/**
	 * @param dosMotscles
	 *            the dosMotscles to set
	 */
	public void setDosMotscles(List<String> dosMotscles) {
		this.dosMotscles = dosMotscles;
	}

	/**
	 * @return the dosNbDossierRectifie
	 */
	public Long getDosNbDossierRectifie() {
		return dosNbDossierRectifie;
	}

	/**
	 * @param dosNbDossierRectifie
	 *            the dosNbDossierRectifie to set
	 */
	public void setDosNbDossierRectifie(Long dosNbDossierRectifie) {
		this.dosNbDossierRectifie = dosNbDossierRectifie;
	}

	/**
	 * @return the dosNomCompletChargesMissions
	 */
	public List<String> getDosNomCompletChargesMissions() {
		return dosNomCompletChargesMissions;
	}

	/**
	 * @param dosNomCompletChargesMissions
	 *            the dosNomCompletChargesMissions to set
	 */
	public void setDosNomCompletChargesMissions(List<String> dosNomCompletChargesMissions) {
		this.dosNomCompletChargesMissions = dosNomCompletChargesMissions;
	}

	/**
	 * @return the dosNomCompletConseillersPms
	 */
	public List<String> getDosNomCompletConseillersPms() {
		return dosNomCompletConseillersPms;
	}

	/**
	 * @param dosNomCompletConseillersPms
	 *            the dosNomCompletConseillersPms to set
	 */
	public void setDosNomCompletConseillersPms(List<String> dosNomCompletConseillersPms) {
		this.dosNomCompletConseillersPms = dosNomCompletConseillersPms;
	}

	/**
	 * @return the dosNomCompletRespDossier
	 */
	public String getDosNomCompletRespDossier() {
		return dosNomCompletRespDossier;
	}

	/**
	 * @param dosNomCompletRespDossier
	 *            the dosNomCompletRespDossier to set
	 */
	public void setDosNomCompletRespDossier(String dosNomCompletRespDossier) {
		this.dosNomCompletRespDossier = dosNomCompletRespDossier;
	}

	/**
	 * @return the dosNomRespDossier
	 */
	public String getDosNomRespDossier() {
		return dosNomRespDossier;
	}

	/**
	 * @param dosNomRespDossier
	 *            the dosNomRespDossier to set
	 */
	public void setDosNomRespDossier(String dosNomRespDossier) {
		this.dosNomRespDossier = dosNomRespDossier;
	}

	/**
	 * @return the dosNorAttribue
	 */
	public Boolean getDosNorAttribue() {
		return dosNorAttribue;
	}

	/**
	 * @param dosNorAttribue
	 *            the dosNorAttribue to set
	 */
	public void setDosNorAttribue(Boolean dosNorAttribue) {
		this.dosNorAttribue = dosNorAttribue;
	}

	/**
	 * @return the dosNumeroNor
	 */
	public String getDosNumeroNor() {
		return dosNumeroNor;
	}

	/**
	 * @param dosNumeroNor
	 *            the dosNumeroNor to set
	 */
	public void setDosNumeroNor(String dosNumeroNor) {
		this.dosNumeroNor = dosNumeroNor;
	}

	/**
	 * @return the dosNumeroVersionActeOuExtrait
	 */
	public Long getDosNumeroVersionActeOuExtrait() {
		return dosNumeroVersionActeOuExtrait;
	}

	/**
	 * @param dosNumeroVersionActeOuExtrait
	 *            the dosNumeroVersionActeOuExtrait to set
	 */
	public void setDosNumeroVersionActeOuExtrait(Long dosNumeroVersionActeOuExtrait) {
		this.dosNumeroVersionActeOuExtrait = dosNumeroVersionActeOuExtrait;
	}

	/**
	 * @return the dosPeriodicite
	 */
	public String getDosPeriodicite() {
		return dosPeriodicite;
	}

	/**
	 * @param dosPeriodicite
	 *            the dosPeriodicite to set
	 */
	public void setDosPeriodicite(String dosPeriodicite) {
		this.dosPeriodicite = dosPeriodicite;
	}

	/**
	 * @return the dosPeriodiciteRapport
	 */
	public String getDosPeriodiciteRapport() {
		return dosPeriodiciteRapport;
	}

	/**
	 * @param dosPeriodiciteRapport
	 *            the dosPeriodiciteRapport to set
	 */
	public void setDosPeriodiciteRapport(String dosPeriodiciteRapport) {
		this.dosPeriodiciteRapport = dosPeriodiciteRapport;
	}

	/**
	 * @return the dosPosteCreator
	 */
	public String getDosPosteCreator() {
		return dosPosteCreator;
	}

	/**
	 * @param dosPosteCreator
	 *            the dosPosteCreator to set
	 */
	public void setDosPosteCreator(String dosPosteCreator) {
		this.dosPosteCreator = dosPosteCreator;
	}

	/**
	 * @return the dosPourFournitureEpreuve
	 */
	public Date getDosPourFournitureEpreuve() {
		return dosPourFournitureEpreuve;
	}

	/**
	 * @param dosPourFournitureEpreuve
	 *            the dosPourFournitureEpreuve to set
	 */
	public void setDosPourFournitureEpreuve(Date dosPourFournitureEpreuve) {
		this.dosPourFournitureEpreuve = dosPourFournitureEpreuve;
	}

	/**
	 * @return the dosPrenomRespDossier
	 */
	public String getDosPrenomRespDossier() {
		return dosPrenomRespDossier;
	}

	/**
	 * @param dosPrenomRespDossier
	 *            the dosPrenomRespDossier to set
	 */
	public void setDosPrenomRespDossier(String dosPrenomRespDossier) {
		this.dosPrenomRespDossier = dosPrenomRespDossier;
	}

	/**
	 * @return the dosPublicationIntegraleOuExtrait
	 */
	public String getDosPublicationIntegraleOuExtrait() {
		return dosPublicationIntegraleOuExtrait;
	}

	/**
	 * @param dosPublicationIntegraleOuExtrait
	 *            the dosPublicationIntegraleOuExtrait to set
	 */
	public void setDosPublicationIntegraleOuExtrait(String dosPublicationIntegraleOuExtrait) {
		this.dosPublicationIntegraleOuExtrait = dosPublicationIntegraleOuExtrait;
	}

	/**
	 * @return the dosPublicationRapportPresentation
	 */
	public Boolean getDosPublicationRapportPresentation() {
		return dosPublicationRapportPresentation;
	}

	/**
	 * @param dosPublicationRapportPresentation
	 *            the dosPublicationRapportPresentation to set
	 */
	public void setDosPublicationRapportPresentation(Boolean dosPublicationRapportPresentation) {
		this.dosPublicationRapportPresentation = dosPublicationRapportPresentation;
	}

	/**
	 * @return the dosPublicationsConjointes
	 */
	public List<String> getDosPublicationsConjointes() {
		return dosPublicationsConjointes;
	}

	/**
	 * @param dosPublicationsConjointes
	 *            the dosPublicationsConjointes to set
	 */
	public void setDosPublicationsConjointes(List<String> dosPublicationsConjointes) {
		this.dosPublicationsConjointes = dosPublicationsConjointes;
	}

	/**
	 * @return the dosPublie
	 */
	public Boolean getDosPublie() {
		return dosPublie;
	}

	/**
	 * @param dosPublie
	 *            the dosPublie to set
	 */
	public void setDosPublie(Boolean dosPublie) {
		this.dosPublie = dosPublie;
	}

	/**
	 * @return the dosQualiteRespDossier
	 */
	public String getDosQualiteRespDossier() {
		return dosQualiteRespDossier;
	}

	/**
	 * @param dosQualiteRespDossier
	 *            the dosQualiteRespDossier to set
	 */
	public void setDosQualiteRespDossier(String dosQualiteRespDossier) {
		this.dosQualiteRespDossier = dosQualiteRespDossier;
	}

	/**
	 * @return the dosRubriques
	 */
	public List<String> getDosRubriques() {
		return dosRubriques;
	}

	/**
	 * @param dosRubriques
	 *            the dosRubriques to set
	 */
	public void setDosRubriques(List<String> dosRubriques) {
		this.dosRubriques = dosRubriques;
	}

	/**
	 * @return the dosStatut
	 */
	public String getDosStatut() {
		return dosStatut;
	}

	/**
	 * @param dosStatut
	 *            the dosStatut to set
	 */
	public void setDosStatut(String dosStatut) {
		this.dosStatut = dosStatut;
	}

	/**
	 * @return the dosStatutArchivage
	 */
	public String getDosStatutArchivage() {
		return dosStatutArchivage;
	}

	/**
	 * @param dosStatutArchivage
	 *            the dosStatutArchivage to set
	 */
	public void setDosStatutArchivage(String dosStatutArchivage) {
		this.dosStatutArchivage = dosStatutArchivage;
	}

	/**
	 * @return the dosTelRespDossier
	 */
	public String getDosTelRespDossier() {
		return dosTelRespDossier;
	}

	/**
	 * @param dosTelRespDossier
	 *            the dosTelRespDossier to set
	 */
	public void setDosTelRespDossier(String dosTelRespDossier) {
		this.dosTelRespDossier = dosTelRespDossier;
	}

	/**
	 * @return the dosTitreActe
	 */
	public String getDosTitreActe() {
		return dosTitreActe;
	}

	/**
	 * @param dosTitreActe
	 *            the dosTitreActe to set
	 */
	public void setDosTitreActe(String dosTitreActe) {
		this.dosTitreActe = dosTitreActe;
	}

	/**
	 * @return the dosTranspositionDirective
	 */
	public List<Map<String, Serializable>> getDosTranspositionDirective() {
		return dosTranspositionDirective;
	}

	/**
	 * @param dosTranspositionDirective
	 *            the dosTranspositionDirective to set
	 */
	public void setDosTranspositionDirective(List<Map<String, Serializable>> dosTranspositionDirective) {
		this.dosTranspositionDirective = dosTranspositionDirective;
	}

	/**
	 * @return the dosTranspositionDirectiveNumero
	 */
	public List<String> getDosTranspositionDirectiveNumero() {
		return dosTranspositionDirectiveNumero;
	}

	/**
	 * @param dosTranspositionDirectiveNumero
	 *            the dosTranspositionDirectiveNumero to set
	 */
	public void setDosTranspositionDirectiveNumero(List<String> dosTranspositionDirectiveNumero) {
		this.dosTranspositionDirectiveNumero = dosTranspositionDirectiveNumero;
	}

	/**
	 * @return the dosTranspositionOrdoRefs
	 */
	public List<String> getDosTranspositionOrdoRefs() {
		return dosTranspositionOrdoRefs;
	}

	/**
	 * @param dosTranspositionOrdoRefs
	 *            the dosTranspositionOrdoRefs to set
	 */
	public void setDosTranspositionOrdoRefs(List<String> dosTranspositionOrdoRefs) {
		this.dosTranspositionOrdoRefs = dosTranspositionOrdoRefs;
	}

	/**
	 * @return the dosTranspositionOrdonnance
	 */
	public List<Map<String, Serializable>> getDosTranspositionOrdonnance() {
		return dosTranspositionOrdonnance;
	}

	/**
	 * @param dosTranspositionOrdonnance
	 *            the dosTranspositionOrdonnance to set
	 */
	public void setDosTranspositionOrdonnance(List<Map<String, Serializable>> dosTranspositionOrdonnance) {
		this.dosTranspositionOrdonnance = dosTranspositionOrdonnance;
	}

	/**
	 * @return the dosTypeActe
	 */
	public String getDosTypeActe() {
		return dosTypeActe;
	}

	/**
	 * @param dosTypeActe
	 *            the dosTypeActe to set
	 */
	public void setDosTypeActe(String dosTypeActe) {
		this.dosTypeActe = dosTypeActe;
	}

	/**
	 * @return the dosVecteurPublication
	 */
	public List<String> getDosVecteurPublication() {
		return dosVecteurPublication;
	}

	/**
	 * @param dosVecteurPublication
	 *            the dosVecteurPublication to set
	 */
	public void setDosVecteurPublication(List<String> dosVecteurPublication) {
		this.dosVecteurPublication = dosVecteurPublication;
	}

	/**
	 * @return the dosZoneComSggDila
	 */
	public String getDosZoneComSggDila() {
		return dosZoneComSggDila;
	}

	/**
	 * @param dosZoneComSggDila
	 *            the dosZoneComSggDila to set
	 */
	public void setDosZoneComSggDila(String dosZoneComSggDila) {
		this.dosZoneComSggDila = dosZoneComSggDila;
	}

	public Boolean getDosTexteEntreprise() {
		return dosTexteEntreprise;
	}

	public void setDosTexteEntreprise(Boolean dosTexteEntreprise) {
		this.dosTexteEntreprise = dosTexteEntreprise;
	}

	public List<Date> getDosDateEffet() {
		return dosDateEffet;
	}

	public void setDosDateEffet(List<Date> dosDateEffet) {
		this.dosDateEffet = dosDateEffet;
	}

	/**
	 * @return the noteMimeType
	 */
	public String getNoteMimeType() {
		return noteMimeType;
	}

	/**
	 * @param noteMimeType
	 *            the noteMimeType to set
	 */
	public void setNoteMimeType(String noteMimeType) {
		this.noteMimeType = noteMimeType;
	}

	/**
	 * @return the noteNote
	 */
	public String getNoteNote() {
		return noteNote;
	}

	/**
	 * @param noteNote
	 *            the noteNote to set
	 */
	public void setNoteNote(String noteNote) {
		this.noteNote = noteNote;
	}

	/**
	 * @return the retdilaDateParutionJorf
	 */
	public Date getRetdilaDateParutionJorf() {
		return retdilaDateParutionJorf;
	}

	/**
	 * @param retdilaDateParutionJorf
	 *            the retdilaDateParutionJorf to set
	 */
	public void setRetdilaDateParutionJorf(Date retdilaDateParutionJorf) {
		this.retdilaDateParutionJorf = retdilaDateParutionJorf;
	}

	/**
	 * @return the retdilaDatePromulgation
	 */
	public Date getRetdilaDatePromulgation() {
		return retdilaDatePromulgation;
	}

	/**
	 * @param retdilaDatePromulgation
	 *            the retdilaDatePromulgation to set
	 */
	public void setRetdilaDatePromulgation(Date retdilaDatePromulgation) {
		this.retdilaDatePromulgation = retdilaDatePromulgation;
	}

	/**
	 * @return the retdilaIsPublicationEpreuvageDemandeSuivante
	 */
	public Boolean getRetdilaIsPublicationEpreuvageDemandeSuivante() {
		return retdilaIsPublicationEpreuvageDemandeSuivante;
	}

	/**
	 * @param retdilaIsPublicationEpreuvageDemandeSuivante
	 *            the retdilaIsPublicationEpreuvageDemandeSuivante to set
	 */
	public void setRetdilaIsPublicationEpreuvageDemandeSuivante(Boolean retdilaIsPublicationEpreuvageDemandeSuivante) {
		this.retdilaIsPublicationEpreuvageDemandeSuivante = retdilaIsPublicationEpreuvageDemandeSuivante;
	}

	/**
	 * @return the retdilaLegislaturePublication
	 */
	public String getRetdilaLegislaturePublication() {
		return retdilaLegislaturePublication;
	}

	/**
	 * @param retdilaLegislaturePublication
	 *            the retdilaLegislaturePublication to set
	 */
	public void setRetdilaLegislaturePublication(String retdilaLegislaturePublication) {
		this.retdilaLegislaturePublication = retdilaLegislaturePublication;
	}

	/**
	 * @return the retdilaModeParution
	 */
	public String getRetdilaModeParution() {
		return retdilaModeParution;
	}

	/**
	 * @param retdilaModeParution
	 *            the retdilaModeParution to set
	 */
	public void setRetdilaModeParution(String retdilaModeParution) {
		this.retdilaModeParution = retdilaModeParution;
	}

	/**
	 * @return the retdilaNumeroTexteParutionJorf
	 */
	public String getRetdilaNumeroTexteParutionJorf() {
		return retdilaNumeroTexteParutionJorf;
	}

	/**
	 * @param retdilaNumeroTexteParutionJorf
	 *            the retdilaNumeroTexteParutionJorf to set
	 */
	public void setRetdilaNumeroTexteParutionJorf(String retdilaNumeroTexteParutionJorf) {
		this.retdilaNumeroTexteParutionJorf = retdilaNumeroTexteParutionJorf;
	}

	/**
	 * @return the retdilaPageParutionJorf
	 */
	public Long getRetdilaPageParutionJorf() {
		return retdilaPageParutionJorf;
	}

	/**
	 * @param retdilaPageParutionJorf
	 *            the retdilaPageParutionJorf to set
	 */
	public void setRetdilaPageParutionJorf(Long retdilaPageParutionJorf) {
		this.retdilaPageParutionJorf = retdilaPageParutionJorf;
	}

	/**
	 * @return the retdilaParutionBo
	 */
	public List<Map<String, Serializable>> getRetdilaParutionBo() {
		return retdilaParutionBo;
	}

	/**
	 * @param retdilaParutionBo
	 *            the retdilaParutionBo to set
	 */
	public void setRetdilaParutionBo(List<Map<String, Serializable>> retdilaParutionBo) {
		this.retdilaParutionBo = retdilaParutionBo;
	}

	/**
	 * @return the retdilaTitreOfficiel
	 */
	public String getRetdilaTitreOfficiel() {
		return retdilaTitreOfficiel;
	}

	/**
	 * @param retdilaTitreOfficiel
	 *            the retdilaTitreOfficiel to set
	 */
	public void setRetdilaTitreOfficiel(String retdilaTitreOfficiel) {
		this.retdilaTitreOfficiel = retdilaTitreOfficiel;
	}

	/**
	 * @return the tpAmpliationDestinataireMails
	 */
	public List<String> getTpAmpliationDestinataireMails() {
		return tpAmpliationDestinataireMails;
	}

	/**
	 * @param tpAmpliationDestinataireMails
	 *            the tpAmpliationDestinataireMails to set
	 */
	public void setTpAmpliationDestinataireMails(List<String> tpAmpliationDestinataireMails) {
		this.tpAmpliationDestinataireMails = tpAmpliationDestinataireMails;
	}

	/**
	 * @return the tpAmpliationHistorique
	 */
	public List<Map<String, Serializable>> getTpAmpliationHistorique() {
		return tpAmpliationHistorique;
	}

	/**
	 * @param tpAmpliationHistorique
	 *            the tpAmpliationHistorique to set
	 */
	public void setTpAmpliationHistorique(List<Map<String, Serializable>> tpAmpliationHistorique) {
		this.tpAmpliationHistorique = tpAmpliationHistorique;
	}

	/**
	 * @return the tpAutresDestinatairesCommunication
	 */
	public List<Map<String, Serializable>> getTpAutresDestinatairesCommunication() {
		return tpAutresDestinatairesCommunication;
	}

	/**
	 * @param tpAutresDestinatairesCommunication
	 *            the tpAutresDestinatairesCommunication to set
	 */
	public void setTpAutresDestinatairesCommunication(List<Map<String, Serializable>> tpAutresDestinatairesCommunication) {
		this.tpAutresDestinatairesCommunication = tpAutresDestinatairesCommunication;
	}

	/**
	 * @return the tpCabinetPmCommunication
	 */
	public List<Map<String, Serializable>> getTpCabinetPmCommunication() {
		return tpCabinetPmCommunication;
	}

	/**
	 * @param tpCabinetPmCommunication
	 *            the tpCabinetPmCommunication to set
	 */
	public void setTpCabinetPmCommunication(List<Map<String, Serializable>> tpCabinetPmCommunication) {
		this.tpCabinetPmCommunication = tpCabinetPmCommunication;
	}

	/**
	 * @return the tpCabinetSgCommunication
	 */
	public List<Map<String, Serializable>> getTpCabinetSgCommunication() {
		return tpCabinetSgCommunication;
	}

	/**
	 * @param tpCabinetSgCommunication
	 *            the tpCabinetSgCommunication to set
	 */
	public void setTpCabinetSgCommunication(List<Map<String, Serializable>> tpCabinetSgCommunication) {
		this.tpCabinetSgCommunication = tpCabinetSgCommunication;
	}

	/**
	 * @return the tpChargeMissionCommunication
	 */
	public List<Map<String, Serializable>> getTpChargeMissionCommunication() {
		return tpChargeMissionCommunication;
	}

	/**
	 * @param tpChargeMissionCommunication
	 *            the tpChargeMissionCommunication to set
	 */
	public void setTpChargeMissionCommunication(List<Map<String, Serializable>> tpChargeMissionCommunication) {
		this.tpChargeMissionCommunication = tpChargeMissionCommunication;
	}

	/**
	 * @return the tpCheminCroix
	 */
	public Boolean getTpCheminCroix() {
		return tpCheminCroix;
	}

	/**
	 * @param tpCheminCroix
	 *            the tpCheminCroix to set
	 */
	public void setTpCheminCroix(Boolean tpCheminCroix) {
		this.tpCheminCroix = tpCheminCroix;
	}

	/**
	 * @return the tpCommentaireReference
	 */
	public String getTpCommentaireReference() {
		return tpCommentaireReference;
	}

	/**
	 * @param tpCommentaireReference
	 *            the tpCommentaireReference to set
	 */
	public void setTpCommentaireReference(String tpCommentaireReference) {
		this.tpCommentaireReference = tpCommentaireReference;
	}

	/**
	 * @return the tpDateArrivePapier
	 */
	public Date getTpDateArrivePapier() {
		return tpDateArrivePapier;
	}

	/**
	 * @param tpDateArrivePapier
	 *            the tpDateArrivePapier to set
	 */
	public void setTpDateArrivePapier(Date tpDateArrivePapier) {
		this.tpDateArrivePapier = tpDateArrivePapier;
	}

	/**
	 * @return the tpDateRetour
	 */
	public Date getTpDateRetour() {
		return tpDateRetour;
	}

	/**
	 * @param tpDateRetour
	 *            the tpDateRetour to set
	 */
	public void setTpDateRetour(Date tpDateRetour) {
		this.tpDateRetour = tpDateRetour;
	}

	public Map<String, Serializable> getTpEpreuve() {
		return tpEpreuve;
	}

	public void setTpEpreuve(Map<String, Serializable> tpEpreuve) {
		this.tpEpreuve = tpEpreuve;
	}

	/**
	 * @return the tpMotifRetour
	 */
	public String getTpMotifRetour() {
		return tpMotifRetour;
	}

	/**
	 * @param tpMotifRetour
	 *            the tpMotifRetour to set
	 */
	public void setTpMotifRetour(String tpMotifRetour) {
		this.tpMotifRetour = tpMotifRetour;
	}

	/**
	 * @return the tpNomsSignatairesCommunication
	 */
	public String getTpNomsSignatairesCommunication() {
		return tpNomsSignatairesCommunication;
	}

	/**
	 * @param tpNomsSignatairesCommunication
	 *            the tpNomsSignatairesCommunication to set
	 */
	public void setTpNomsSignatairesCommunication(String tpNomsSignatairesCommunication) {
		this.tpNomsSignatairesCommunication = tpNomsSignatairesCommunication;
	}

	/**
	 * @return the tpNomsSignatairesRetour
	 */
	public String getTpNomsSignatairesRetour() {
		return tpNomsSignatairesRetour;
	}

	/**
	 * @param tpNomsSignatairesRetour
	 *            the tpNomsSignatairesRetour to set
	 */
	public void setTpNomsSignatairesRetour(String tpNomsSignatairesRetour) {
		this.tpNomsSignatairesRetour = tpNomsSignatairesRetour;
	}

	/**
	 * @return the tpNouvelleDemandeEpreuves
	 */
	public Map<String, Serializable> getTpNouvelleDemandeEpreuves() {
		return tpNouvelleDemandeEpreuves;
	}

	/**
	 * @param tpNouvelleDemandeEpreuves
	 *            the tpNouvelleDemandeEpreuves to set
	 */
	public void setTpNouvelleDemandeEpreuves(Map<String, Serializable> tpNouvelleDemandeEpreuves) {
		this.tpNouvelleDemandeEpreuves = tpNouvelleDemandeEpreuves;
	}

	/**
	 * @return the tpNumerosListeSignatureField
	 */
	public List<Map<String, Serializable>> getTpNumerosListeSignatureField() {
		return tpNumerosListeSignatureField;
	}

	/**
	 * @param tpNumerosListeSignatureField
	 *            the tpNumerosListeSignatureField to set
	 */
	public void setTpNumerosListeSignatureField(List<Map<String, Serializable>> tpNumerosListeSignatureField) {
		this.tpNumerosListeSignatureField = tpNumerosListeSignatureField;
	}

	/**
	 * @return the tpPapierArchive
	 */
	public Boolean getTpPapierArchive() {
		return tpPapierArchive;
	}

	/**
	 * @param tpPapierArchive
	 *            the tpPapierArchive to set
	 */
	public void setTpPapierArchive(Boolean tpPapierArchive) {
		this.tpPapierArchive = tpPapierArchive;
	}

	/**
	 * @return the tpPersonnesNommees
	 */
	public String getTpPersonnesNommees() {
		return tpPersonnesNommees;
	}

	/**
	 * @param tpPersonnesNommees
	 *            the tpPersonnesNommees to set
	 */
	public void setTpPersonnesNommees(String tpPersonnesNommees) {
		this.tpPersonnesNommees = tpPersonnesNommees;
	}

	/**
	 * @return the tpPriorite
	 */
	public String getTpPriorite() {
		return tpPriorite;
	}

	/**
	 * @param tpPriorite
	 *            the tpPriorite to set
	 */
	public void setTpPriorite(String tpPriorite) {
		this.tpPriorite = tpPriorite;
	}

	/**
	 * @return the tpPublicationDate
	 */
	public Date getTpPublicationDate() {
		return tpPublicationDate;
	}

	/**
	 * @param tpPublicationDate
	 *            the tpPublicationDate to set
	 */
	public void setTpPublicationDate(Date tpPublicationDate) {
		this.tpPublicationDate = tpPublicationDate;
	}

	/**
	 * @return the tpPublicationDateDemande
	 */
	public Date getTpPublicationDateDemande() {
		return tpPublicationDateDemande;
	}

	/**
	 * @param tpPublicationDateDemande
	 *            the tpPublicationDateDemande to set
	 */
	public void setTpPublicationDateDemande(Date tpPublicationDateDemande) {
		this.tpPublicationDateDemande = tpPublicationDateDemande;
	}

	/**
	 * @return the tpPublicationDateEnvoiJo
	 */
	public Date getTpPublicationDateEnvoiJo() {
		return tpPublicationDateEnvoiJo;
	}

	/**
	 * @param tpPublicationDateEnvoiJo
	 *            the tpPublicationDateEnvoiJo to set
	 */
	public void setTpPublicationDateEnvoiJo(Date tpPublicationDateEnvoiJo) {
		this.tpPublicationDateEnvoiJo = tpPublicationDateEnvoiJo;
	}

	/**
	 * @return the tpPublicationDelai
	 */
	public String getTpPublicationDelai() {
		return tpPublicationDelai;
	}

	/**
	 * @param tpPublicationDelai
	 *            the tpPublicationDelai to set
	 */
	public void setTpPublicationDelai(String tpPublicationDelai) {
		this.tpPublicationDelai = tpPublicationDelai;
	}

	/**
	 * @return the tpPublicationEpreuveEnRetour
	 */
	public Boolean getTpPublicationEpreuveEnRetour() {
		return tpPublicationEpreuveEnRetour;
	}

	/**
	 * @param tpPublicationEpreuveEnRetour
	 *            the tpPublicationEpreuveEnRetour to set
	 */
	public void setTpPublicationEpreuveEnRetour(Boolean tpPublicationEpreuveEnRetour) {
		this.tpPublicationEpreuveEnRetour = tpPublicationEpreuveEnRetour;
	}

	/**
	 * @return the tpPublicationModePublication
	 */
	public String getTpPublicationModePublication() {
		return tpPublicationModePublication;
	}

	/**
	 * @param tpPublicationModePublication
	 *            the tpPublicationModePublication to set
	 */
	public void setTpPublicationModePublication(String tpPublicationModePublication) {
		this.tpPublicationModePublication = tpPublicationModePublication;
	}

	/**
	 * @return the tpPublicationNomPublication
	 */
	public String getTpPublicationNomPublication() {
		return tpPublicationNomPublication;
	}

	/**
	 * @param tpPublicationNomPublication
	 *            the tpPublicationNomPublication to set
	 */
	public void setTpPublicationNomPublication(String tpPublicationNomPublication) {
		this.tpPublicationNomPublication = tpPublicationNomPublication;
	}

	/**
	 * @return the tpPublicationNumeroListe
	 */
	public String getTpPublicationNumeroListe() {
		return tpPublicationNumeroListe;
	}

	/**
	 * @param tpPublicationNumeroListe
	 *            the tpPublicationNumeroListe to set
	 */
	public void setTpPublicationNumeroListe(String tpPublicationNumeroListe) {
		this.tpPublicationNumeroListe = tpPublicationNumeroListe;
	}

	/**
	 * @return the tpRetourA
	 */
	public String getTpRetourA() {
		return tpRetourA;
	}

	/**
	 * @param tpRetourA
	 *            the tpRetourA to set
	 */
	public void setTpRetourA(String tpRetourA) {
		this.tpRetourA = tpRetourA;
	}

	/**
	 * @return the tpRetourDuBonaTitrerLe
	 */
	public Date getTpRetourDuBonaTitrerLe() {
		return tpRetourDuBonaTitrerLe;
	}

	/**
	 * @param tpRetourDuBonaTitrerLe
	 *            the tpRetourDuBonaTitrerLe to set
	 */
	public void setTpRetourDuBonaTitrerLe(Date tpRetourDuBonaTitrerLe) {
		this.tpRetourDuBonaTitrerLe = tpRetourDuBonaTitrerLe;
	}

	/**
	 * @return the tpSignataire
	 */
	public String getTpSignataire() {
		return tpSignataire;
	}

	/**
	 * @param tpSignataire
	 *            the tpSignataire to set
	 */
	public void setTpSignataire(String tpSignataire) {
		this.tpSignataire = tpSignataire;
	}

	/**
	 * @return the tpSignatureDestinataireCPM
	 */
	public String getTpSignatureDestinataireCPM() {
		return tpSignatureDestinataireCPM;
	}

	/**
	 * @param tpSignatureDestinataireCPM
	 *            the tpSignatureDestinataireCPM to set
	 */
	public void setTpSignatureDestinataireCPM(String tpSignatureDestinataireCPM) {
		this.tpSignatureDestinataireCPM = tpSignatureDestinataireCPM;
	}

	/**
	 * @return the tpSignatureDestinataireSGG
	 */
	public String getTpSignatureDestinataireSGG() {
		return tpSignatureDestinataireSGG;
	}

	/**
	 * @param tpSignatureDestinataireSGG
	 *            the tpSignatureDestinataireSGG to set
	 */
	public void setTpSignatureDestinataireSGG(String tpSignatureDestinataireSGG) {
		this.tpSignatureDestinataireSGG = tpSignatureDestinataireSGG;
	}

	/**
	 * @return the tpTexteAPublier
	 */
	public Boolean getTpTexteAPublier() {
		return tpTexteAPublier;
	}

	/**
	 * @param tpTexteAPublier
	 *            the tpTexteAPublier to set
	 */
	public void setTpTexteAPublier(Boolean tpTexteAPublier) {
		this.tpTexteAPublier = tpTexteAPublier;
	}

	/**
	 * @return the tpTexteSoumisAValidation
	 */
	public Boolean getTpTexteSoumisAValidation() {
		return tpTexteSoumisAValidation;
	}

	/**
	 * @param tpTexteSoumisAValidation
	 *            the tpTexteSoumisAValidation to set
	 */
	public void setTpTexteSoumisAValidation(Boolean tpTexteSoumisAValidation) {
		this.tpTexteSoumisAValidation = tpTexteSoumisAValidation;
	}

	public Map<String, Serializable> getTpSignaturePm() {
		return tpSignaturePm;
	}

	public void setTpSignaturePm(Map<String, Serializable> tpSignaturePm) {
		this.tpSignaturePm = tpSignaturePm;
	}

	public Map<String, Serializable> getTpSignaturePr() {
		return tpSignaturePr;
	}

	public void setTpSignaturePr(Map<String, Serializable> tpSignaturePr) {
		this.tpSignaturePr = tpSignaturePr;
	}

	public Map<String, Serializable> getTpSignatureSgg() {
		return tpSignatureSgg;
	}

	public void setTpSignatureSgg(Map<String, Serializable> tpSignatureSgg) {
		this.tpSignatureSgg = tpSignatureSgg;
	}

	/**
	 * @return the dosDirectionAttacheId
	 */
	public String getDosDirectionAttacheId() {
		return dosDirectionAttacheId;
	}

	/**
	 * @param dosDirectionAttacheId
	 *            the dosDirectionAttacheId to set
	 */
	public void setDosDirectionAttacheId(String dosDirectionAttacheId) {
		this.dosDirectionAttacheId = dosDirectionAttacheId;
	}

	/**
	 * @return the dosDirectionRespId
	 */
	public String getDosDirectionRespId() {
		return dosDirectionRespId;
	}

	/**
	 * @param dosDirectionRespId
	 *            the dosDirectionRespId to set
	 */
	public void setDosDirectionRespId(String dosDirectionRespId) {
		this.dosDirectionRespId = dosDirectionRespId;
	}

	/**
	 * @return the dosMinistereAttacheId
	 */
	public String getDosMinistereAttacheId() {
		return dosMinistereAttacheId;
	}

	/**
	 * @param dosMinistereAttacheId
	 *            the dosMinistereAttacheId to set
	 */
	public void setDosMinistereAttacheId(String dosMinistereAttacheId) {
		this.dosMinistereAttacheId = dosMinistereAttacheId;
	}

	/**
	 * @return the dosMinistereRespId
	 */
	public String getDosMinistereRespId() {
		return dosMinistereRespId;
	}

	/**
	 * @param dosMinistereRespId
	 *            the dosMinistereRespId to set
	 */
	public void setDosMinistereRespId(String dosMinistereRespId) {
		this.dosMinistereRespId = dosMinistereRespId;
	}

	/**
	 * @return the dosStatutId
	 */
	public String getDosStatutId() {
		return dosStatutId;
	}

	/**
	 * @param dosStatutId
	 *            the dosStatutId to set
	 */
	public void setDosStatutId(String dosStatutId) {
		this.dosStatutId = dosStatutId;
	}

	/**
	 * @return the dosStatutArchivageId
	 */
	public String getDosStatutArchivageId() {
		return dosStatutArchivageId;
	}

	/**
	 * @param dosStatutArchivageId
	 *            the dosStatutArchivageId to set
	 */
	public void setDosStatutArchivageId(String dosStatutArchivageId) {
		this.dosStatutArchivageId = dosStatutArchivageId;
	}

	/**
	 * @return the dosCategorieActeId
	 */
	public String getDosCategorieActeId() {
		return dosCategorieActeId;
	}

	/**
	 * @param dosCategorieActeId
	 *            the dosCategorieActeId to set
	 */
	public void setDosCategorieActeId(String dosCategorieActeId) {
		this.dosCategorieActeId = dosCategorieActeId;
	}

	public List<ElasticDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<ElasticDocument> documents) {
		this.documents = documents;
	}

	public ElasticDossier(ElasticFields ef) {
		List<String> numeroNor = ef.getNumeronor();
		if(numeroNor!=null) {
			this.setDosNumeroNor(numeroNor.get(0));
		}
		
		List<String> minResp = ef.getMinistereresp();
		if (minResp != null) {
			this.setDosMinistereResp(minResp.get(0));
		}
		
		List<Date> dateParution = ef.getDateparutionjorf();
		if (dateParution != null) {
			this.setRetdilaDateParutionJorf(dateParution.get(0));
		}
		
		List<Timestamp> listCreated = ef.getDosCreated();
		if (listCreated != null) {
			Timestamp tsDoscreated = listCreated.get(0);
			this.setDosCreated(new Date(tsDoscreated.getTime()));
		}

		List<String> titres = ef.getTitreacte();
		if (titres != null) {
			this.setDosTitreActe(titres.get(0));
		}
		
		List<String> statut = ef.getStatut();
		if(statut!=null) {
			this.setDosStatut(statut.get(0));
		}
	}

	public ElasticDossier() {
		super();
	}

	public Collection<String> getPerms() {
		return perms;
	}

	public void setPerms(Collection<String> perms) {
		this.perms = perms;
	}
}
