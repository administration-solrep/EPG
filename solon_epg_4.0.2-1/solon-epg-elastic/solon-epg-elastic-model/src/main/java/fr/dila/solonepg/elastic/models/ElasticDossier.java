package fr.dila.solonepg.elastic.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ElasticDossier implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String UID = "UID";
    public static final String DELETED = "deleted";
    public static final String CONSETAT_DATE_AG_CE = "consetat:dateAgCe";
    public static final String CONSETAT_DATE_SECTION_CE = "consetat:dateSectionCe";
    public static final String CONSETAT_DATE_TRANSMISSION_SECTION_CE = "consetat:dateTransmissionSectionCe";
    public static final String CONSETAT_NUMERO_ISA = "consetat:numeroISA";
    public static final String CONSETAT_PRIORITE = "consetat:priorite";
    public static final String CONSETAT_RAPPORTEUR_CE = "consetat:rapporteurCe";
    public static final String CONSETAT_RECEPTION_AVIS_CE = "consetat:receptionAvisCE";
    public static final String CONSETAT_SECTION_CE = "consetat:sectionCe";
    public static final String DC_CREATOR = "dc:creator";
    public static final String DC_LAST_CONTRIBUTOR = "dc:lastContributor";
    public static final String DC_LOCKED = "dc:locked";
    public static final String DC_LOCK_OWNER = "dc:lockOwner";
    public static final String DC_MODIFIED = "dc:modified";
    public static final String DC_TITLE = "dc:title";
    public static final String DOS_APPLICATION_LOI = "dos:applicationLoi";
    public static final String DOS_ARCHIVE = "dos:archive";
    public static final String DOS_ARRIVEE_SOLON_TS = "dos:arriveeSolonTS";
    public static final String DOS_BASE_LEGALE_ACTE = "dos:baseLegaleActe";
    public static final String DOS_BASE_LEGALE_NUMERO_TEXTE = "dos:baseLegaleNumeroTexte";
    public static final String DOS_CATEGORIE_ACTE = "dos:categorieActe";
    public static final String DOS_CREATED = "dos:created";
    public static final String DOS_DATE_EFFET = "dos:dateEffet";
    public static final String DOS_DATE_ENVOI_JO_TS = "dos:dateEnvoiJoTS";
    public static final String DOS_DATE_PRECISEE_PUBLICATION = "dos:datePreciseePublication";
    public static final String DOS_DATE_PUBLICATION = "dos:datePublication";
    public static final String DOS_DATE_SIGNATURE = "dos:dateSignature";
    public static final String DOS_DATE_VERSEMENT_TS = "dos:dateVersementTS";
    public static final String DOS_DECRET_NUMEROTE = "dos:decretNumerote";
    public static final String DOS_DELAI_PUBLICATION = "dos:delaiPublication";
    public static final String DOS_DIRECTION_ATTACHE = "dos:directionAttache";
    public static final String DOS_DIRECTION_ATTACHE_ID = "dos:directionAttacheId";
    public static final String DOS_DIRECTION_RESP = "dos:directionResp";
    public static final String DOS_DIRECTION_RESP_ID = "dos:directionRespId";
    public static final String DOS_DOSSIER_COMPLET_ERREUR = "dos:dossierCompletErreur";
    public static final String DOS_EMETTEUR = "dos:emetteur";
    public static final String DOS_HABILITATION_COMMENTAIRE = "dos:habilitationCommentaire";
    public static final String DOS_HABILITATION_NUMERO_ARTICLES = "dos:habilitationNumeroArticles";
    public static final String DOS_HABILITATION_NUMERO_ORDRE = "dos:habilitationNumeroOrdre";
    public static final String DOS_HABILITATION_REF_LOI = "dos:habilitationRefLoi";
    public static final String DOS_ID_CREATEUR_DOSSIER = "dos:idCreateurDossier";
    public static final String DOS_ID_DOCUMENT_PARAPHEUR = "dos:idDocumentParapheur";
    public static final String DOS_IS_URGENT = "dos:isUrgent";
    public static final String DOS_LIBRE = "dos:libre";
    public static final String DOS_MAIL_RESP_DOSSIER = "dos:mailRespDossier";
    public static final String DOS_MESURE_NOMINATIVE = "dos:mesureNominative";
    public static final String DOS_MINISTERE_ATTACHE = "dos:ministereAttache";
    public static final String DOS_MINISTERE_ATTACHE_ID = "dos:ministereAttacheId";
    public static final String DOS_MINISTERE_RESP = "dos:ministereResp";
    public static final String DOS_MINISTERE_RESP_ID = "dos:ministereRespId";
    public static final String DOS_MOTSCLES = "dos:motscles";
    public static final String DOS_NOM_COMPLET_CHARGES_MISSIONS = "dos:nomCompletChargesMissions";
    public static final String DOS_NOM_COMPLET_CONSEILLERS_PMS = "dos:nomCompletConseillersPms";
    public static final String DOS_NOM_RESP_DOSSIER = "dos:nomRespDossier";
    public static final String DOS_NUMERO_NOR = "dos:numeroNor";
    public static final String DOS_POUR_FOURNITURE_EPREUVE = "dos:pourFournitureEpreuve";
    public static final String DOS_PRENOM_RESP_DOSSIER = "dos:prenomRespDossier";
    public static final String DOS_PUBLICATION_INTEGRALE_OU_EXTRAIT = "dos:publicationIntegraleOuExtrait";
    public static final String DOS_PUBLICATION_RAPPORT_PRESENTATION = "dos:publicationRapportPresentation";
    public static final String DOS_PUBLICATIONS_CONJOINTES = "dos:publicationsConjointes";
    public static final String DOS_QUALITE_RESP_DOSSIER = "dos:qualiteRespDossier";
    public static final String DOS_RUBRIQUES = "dos:rubriques";
    public static final String DOS_STATUT = "dos:statut";
    public static final String DOS_STATUT_ARCHIVAGE = "dos:statutArchivage";
    public static final String DOS_STATUT_ARCHIVAGE_ID = "dos:statutArchivageId";
    public static final String DOS_TEL_RESP_DOSSIER = "dos:telRespDossier";
    public static final String DOS_TEXTE_ENTREPRISE = "dos:texteEntreprise";
    public static final String DOS_TITRE_ACTE = "dos:titreActe";
    public static final String DOS_TRANSPOSITION_DIRECTIVE = "dos:transpositionDirective";
    public static final String DOS_TRANSPOSITION_ORDONNANCE = "dos:transpositionOrdonnance";
    public static final String DOS_TYPE_ACTE = "dos:typeActe";
    public static final String DOS_VECTEUR_PUBLICATION = "dos:vecteurPublication";
    public static final String DOS_ZONE_COM_SGG_DILA = "dos:zoneComSggDila";
    public static final String ECM_CURRENT_LIFE_CYCLE_STATE = "ecm:currentLifeCycleState";
    public static final String RETDILA_DATE_PARUTION_JORF = "retdila:dateParutionJorf";
    public static final String RETDILA_MODE_PARUTION = "retdila:modeParution";
    public static final String RETDILA_NUMERO_TEXTE_PARUTION_JORF = "retdila:numeroTexteParutionJorf";
    public static final String RETDILA_PAGE_PARUTION_JORF = "retdila:pageParutionJorf";
    public static final String RETDILA_PARUTION_BO = "retdila:parutionBo";
    public static final String RETDILA_TITRE_OFFICIEL = "retdila:titreOfficiel";
    public static final String STEPS = "steps";
    public static final String TP_AMPLIATION_DESTINATAIRE_MAILS = "tp:ampliationDestinataireMails";
    public static final String TP_CHEMIN_CROIX = "tp:cheminCroix";
    public static final String TP_COMMENTAIRE_REFERENCE = "tp:commentaireReference";
    public static final String TP_DATE_ARRIVE_PAPIER = "tp:dateArrivePapier";
    public static final String TP_DATE_RETOUR = "tp:dateRetour";
    public static final String TP_MOTIF_RETOUR = "tp:motifRetour";
    public static final String TP_NOMS_SIGNATAIRES_COMMUNICATION = "tp:nomsSignatairesCommunication";
    public static final String TP_NOMS_SIGNATAIRES_RETOUR = "tp:nomsSignatairesRetour";
    public static final String TP_PRIORITE = "tp:priorite";
    public static final String TP_PUBLICATION_DATE = "tp:publicationDate";
    public static final String TP_PUBLICATION_DATE_DEMANDE = "tp:publicationDateDemande";
    public static final String TP_PUBLICATION_DATE_ENVOI_JO = "tp:publicationDateEnvoiJo";
    public static final String TP_PUBLICATION_DELAI = "tp:publicationDelai";
    public static final String TP_PUBLICATION_EPREUVE_EN_RETOUR = "tp:publicationEpreuveEnRetour";
    public static final String TP_PUBLICATION_MODE_PUBLICATION = "tp:publicationModePublication";
    public static final String TP_PUBLICATION_NOM_PUBLICATION = "tp:publicationNomPublication";
    public static final String TP_PUBLICATION_NUMERO_LISTE = "tp:publicationNumeroListe";
    public static final String TP_RETOUR_A = "tp:retourA";
    public static final String TP_RETOUR_DU_BONA_TITRER_LE = "tp:retourDuBonaTitrerLe";
    public static final String TP_SIGNATAIRE = "tp:signataire";
    public static final String TP_TEXTE_APUBLIER = "tp:texteAPublier";
    /* gestion des droits/acls */
    public static final String PERMS = "perms";

    // champ pour la recherche sur les permissions
    public static final String PERMS_KEYWORD = "perms.keyword";

    public static final String DOCUMENTS = "documents";
    public static final String DOCUMENTS_UID = DOCUMENTS + "." + UID;

    @JsonProperty(UID)
    private String uid;

    @JsonProperty(DELETED)
    private boolean deleted = false;

    @JsonProperty(DOCUMENTS)
    private List<ElasticDocument> documents = new ArrayList<>();

    /* consetat:... */
    @JsonProperty(CONSETAT_DATE_AG_CE)
    private Date consetatDateAgCe;

    @JsonProperty(CONSETAT_DATE_SECTION_CE)
    private Date consetatDateSectionCe;

    @JsonProperty(CONSETAT_DATE_TRANSMISSION_SECTION_CE)
    private Date consetatDateTransmissionSectionCe;

    @JsonProperty(CONSETAT_NUMERO_ISA)
    private String consetatNumeroISA;

    @JsonProperty(CONSETAT_PRIORITE)
    private String consetatPriorite;

    @JsonProperty(CONSETAT_RAPPORTEUR_CE)
    private String consetatRapporteurCe;

    @JsonProperty(CONSETAT_RECEPTION_AVIS_CE)
    private Date consetatReceptionAvisCE;

    @JsonProperty(CONSETAT_SECTION_CE)
    private String consetatSectionCe;

    @JsonProperty(DC_LAST_CONTRIBUTOR)
    private String dcLastContributor;

    @JsonProperty(DC_LOCKED)
    private Boolean dcLocked;

    @JsonProperty(DC_LOCK_OWNER)
    private String dcLockOwner;

    @JsonProperty(DC_TITLE)
    private String dcTitle;

    @JsonProperty(DC_MODIFIED)
    private Date dcModified;

    /* dos:... */
    @JsonProperty(DOS_APPLICATION_LOI)
    private List<ElasticApplicationLoi> dosApplicationLoi = new ArrayList<>();

    @JsonProperty(DOS_ARCHIVE)
    private Boolean dosArchive;

    @JsonProperty(DOS_ARRIVEE_SOLON_TS)
    private Boolean dosArriveeSolonTS;

    @JsonProperty(DOS_BASE_LEGALE_ACTE)
    private String dosBaseLegaleActe;

    @JsonProperty(DOS_BASE_LEGALE_NUMERO_TEXTE)
    private String dosBaseLegaleNumeroTexte;

    @JsonProperty(DOS_CATEGORIE_ACTE)
    private String dosCategorieActe;

    @JsonProperty(DOS_CREATED)
    private Date dosCreated;

    @JsonProperty(DOS_DATE_EFFET)
    private List<Date> dosDateEffet = new ArrayList<>();

    @JsonProperty(DOS_DATE_ENVOI_JO_TS)
    private Date dosDateEnvoiJoTS;

    @JsonProperty(DOS_DATE_PRECISEE_PUBLICATION)
    private Date dosDatePreciseePublication;

    @JsonProperty(DOS_DATE_PUBLICATION)
    private Date dosDatePublication;

    @JsonProperty(DOS_DATE_SIGNATURE)
    private Date dosDateSignature;

    @JsonProperty(DOS_DATE_VERSEMENT_TS)
    private Date dosDateVersementTS;

    @JsonProperty(DOS_DECRET_NUMEROTE)
    private Boolean dosDecretNumerote;

    @JsonProperty(DOS_DELAI_PUBLICATION)
    private String dosDelaiPublication;

    @JsonProperty(DOS_DIRECTION_ATTACHE_ID)
    private String dosDirectionAttacheId;

    @JsonProperty(DOS_DIRECTION_ATTACHE)
    private String dosDirectionAttache;

    @JsonProperty(DOS_DIRECTION_RESP_ID)
    private String dosDirectionRespId;

    @JsonProperty(DOS_DIRECTION_RESP)
    private String dosDirectionResp;

    @JsonProperty(DOS_DOSSIER_COMPLET_ERREUR)
    private String dosDossierCompletErreur;

    @JsonProperty(DOS_EMETTEUR)
    private String dosEmetteur;

    @JsonProperty(DOS_HABILITATION_COMMENTAIRE)
    private String dosHabilitationCommentaire;

    @JsonProperty(DOS_HABILITATION_NUMERO_ARTICLES)
    private String dosHabilitationNumeroArticles;

    @JsonProperty(DOS_HABILITATION_NUMERO_ORDRE)
    private String dosHabilitationNumeroOrdre;

    @JsonProperty(DOS_HABILITATION_REF_LOI)
    private String dosHabilitationRefLoi;

    @JsonProperty(DOS_ID_CREATEUR_DOSSIER)
    private String dosIdCreateurDossier;

    @JsonProperty(DOS_ID_DOCUMENT_PARAPHEUR)
    private String dosIdDocumentParapheur;

    @JsonProperty(DOS_IS_URGENT)
    private Boolean dosIsUrgent;

    @JsonProperty(DOS_LIBRE)
    private List<String> dosLibre = new ArrayList<>();

    @JsonProperty(DOS_MAIL_RESP_DOSSIER)
    private String dosMailRespDossier;

    @JsonProperty(DOS_MESURE_NOMINATIVE)
    private Boolean dosMesureNominative;

    @JsonProperty(DOS_MINISTERE_ATTACHE)
    private String dosMinistereAttache;

    @JsonProperty(DOS_MINISTERE_ATTACHE_ID)
    private String dosMinistereAttacheId;

    @JsonProperty(DOS_MINISTERE_RESP)
    private String dosMinistereResp;

    @JsonProperty(DOS_MINISTERE_RESP_ID)
    private String dosMinistereRespId;

    @JsonProperty(DOS_MOTSCLES)
    private List<String> dosMotscles = new ArrayList<>();

    @JsonProperty(DOS_NOM_COMPLET_CHARGES_MISSIONS)
    private List<String> dosNomCompletChargesMissions = new ArrayList<>();

    @JsonProperty(DOS_NOM_COMPLET_CONSEILLERS_PMS)
    private List<String> dosNomCompletConseillersPms = new ArrayList<>();

    @JsonProperty(DOS_NOM_RESP_DOSSIER)
    private String dosNomRespDossier;

    @JsonProperty(DOS_NUMERO_NOR)
    private String dosNumeroNor;

    @JsonProperty(DOS_POUR_FOURNITURE_EPREUVE)
    private Date dosPourFournitureEpreuve;

    @JsonProperty(DOS_PRENOM_RESP_DOSSIER)
    private String dosPrenomRespDossier;

    @JsonProperty(DOS_PUBLICATION_INTEGRALE_OU_EXTRAIT)
    private String dosPublicationIntegraleOuExtrait;

    @JsonProperty(DOS_PUBLICATION_RAPPORT_PRESENTATION)
    private Boolean dosPublicationRapportPresentation;

    @JsonProperty(DOS_PUBLICATIONS_CONJOINTES)
    private List<String> dosPublicationsConjointes = new ArrayList<>();

    @JsonProperty(DOS_QUALITE_RESP_DOSSIER)
    private String dosQualiteRespDossier;

    @JsonProperty(DOS_RUBRIQUES)
    private List<String> dosRubriques = new ArrayList<>();

    @JsonProperty(DOS_STATUT)
    private String dosStatut;

    @JsonProperty(DOS_STATUT_ARCHIVAGE)
    private String dosStatutArchivage;

    @JsonProperty(DOS_STATUT_ARCHIVAGE_ID)
    private String dosStatutArchivageId;

    @JsonProperty(DOS_TEL_RESP_DOSSIER)
    private String dosTelRespDossier;

    @JsonProperty(DOS_TITRE_ACTE)
    private String dosTitreActe;

    @JsonProperty(DOS_TRANSPOSITION_DIRECTIVE)
    private List<ElasticTranspositionDirective> dosTranspositionDirective = new ArrayList<>();

    @JsonProperty(DOS_TRANSPOSITION_ORDONNANCE)
    private List<ElasticTranspositionOrdonnance> dosTranspositionOrdonnance = new ArrayList<>();

    @JsonProperty(DOS_TYPE_ACTE)
    private String dosTypeActe;

    @JsonProperty(DOS_VECTEUR_PUBLICATION)
    private List<String> dosVecteurPublication = new ArrayList<>();

    @JsonProperty(DOS_ZONE_COM_SGG_DILA)
    private String dosZoneComSggDila;

    @JsonProperty(DOS_TEXTE_ENTREPRISE)
    private Boolean dosTexteEntreprise;

    /* ecm:... */
    @JsonProperty(ECM_CURRENT_LIFE_CYCLE_STATE)
    private String ecmCurrentLifeCycleState;

    /* retdila:... */
    @JsonProperty(RETDILA_DATE_PARUTION_JORF)
    private Date retdilaDateParutionJorf;

    @JsonProperty(RETDILA_MODE_PARUTION)
    private String retdilaModeParution;

    @JsonProperty(RETDILA_NUMERO_TEXTE_PARUTION_JORF)
    private String retdilaNumeroTexteParutionJorf;

    @JsonProperty(RETDILA_PAGE_PARUTION_JORF)
    private Long retdilaPageParutionJorf;

    @JsonProperty(RETDILA_PARUTION_BO)
    private List<ElasticParutionBo> retdilaParutionBo = new ArrayList<>();

    @JsonProperty(RETDILA_TITRE_OFFICIEL)
    private String retdilaTitreOfficiel;

    /* rtsk:... */
    @JsonProperty(STEPS)
    private List<ElasticStep> steps = new ArrayList<>();

    /* tp:... */
    @JsonProperty(TP_AMPLIATION_DESTINATAIRE_MAILS)
    private List<String> tpAmpliationDestinataireMails = new ArrayList<>();

    @JsonProperty(TP_CHEMIN_CROIX)
    private Boolean tpCheminCroix;

    @JsonProperty(TP_COMMENTAIRE_REFERENCE)
    private String tpCommentaireReference;

    @JsonProperty(TP_DATE_ARRIVE_PAPIER)
    private Date tpDateArrivePapier;

    @JsonProperty(TP_DATE_RETOUR)
    private Date tpDateRetour;

    @JsonProperty(TP_MOTIF_RETOUR)
    private String tpMotifRetour;

    @JsonProperty(TP_NOMS_SIGNATAIRES_COMMUNICATION)
    private String tpNomsSignatairesCommunication;

    @JsonProperty(TP_NOMS_SIGNATAIRES_RETOUR)
    private String tpNomsSignatairesRetour;

    @JsonProperty(TP_PRIORITE)
    private String tpPriorite;

    @JsonProperty(TP_PUBLICATION_DATE)
    private Date tpPublicationDate;

    @JsonProperty(TP_PUBLICATION_DATE_DEMANDE)
    private Date tpPublicationDateDemande;

    @JsonProperty(TP_PUBLICATION_DATE_ENVOI_JO)
    private Date tpPublicationDateEnvoiJo;

    @JsonProperty(TP_PUBLICATION_DELAI)
    private String tpPublicationDelai;

    @JsonProperty(TP_PUBLICATION_EPREUVE_EN_RETOUR)
    private Boolean tpPublicationEpreuveEnRetour;

    @JsonProperty(TP_PUBLICATION_MODE_PUBLICATION)
    private String tpPublicationModePublication;

    @JsonProperty(TP_PUBLICATION_NOM_PUBLICATION)
    private String tpPublicationNomPublication;

    @JsonProperty(TP_PUBLICATION_NUMERO_LISTE)
    private String tpPublicationNumeroListe;

    @JsonProperty(TP_RETOUR_A)
    private String tpRetourA;

    @JsonProperty(TP_RETOUR_DU_BONA_TITRER_LE)
    private Date tpRetourDuBonaTitrerLe;

    @JsonProperty(TP_SIGNATAIRE)
    private String tpSignataire;

    @JsonProperty(TP_TEXTE_APUBLIER)
    private Boolean tpTexteAPublier;

    @JsonProperty(PERMS)
    private Collection<String> perms;

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
     * @return the consetatPriorite
     */
    public String getConsetatPriorite() {
        return consetatPriorite;
    }

    /**
     * @param consetatPriorite
     *            the consetatPriorite to set
     */
    public void setConsetatPriorite(String consetatPriorite) {
        this.consetatPriorite = consetatPriorite;
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
    public Date getConsetatReceptionAvisCE() {
        return consetatReceptionAvisCE;
    }

    /**
     * @param consetatReceptionAvisCE
     *            the consetatReceptionAvisCE to set
     */
    public void setConsetatReceptionAvisCE(Date consetatReceptionAvisCE) {
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

    public String getDcLastContributor() {
        return dcLastContributor;
    }

    public void setDcLastContributor(String dcLastContributor) {
        this.dcLastContributor = dcLastContributor;
    }

    public Boolean getDcLocked() {
        return dcLocked;
    }

    public void setDcLocked(Boolean dcLock) {
        this.dcLocked = dcLock;
    }

    public String getDcLockOwner() {
        return dcLockOwner;
    }

    public void setDcLockOwner(String dcLockOwner) {
        this.dcLockOwner = dcLockOwner;
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
     * @return the dosApplicationLoi
     */
    public List<ElasticApplicationLoi> getDosApplicationLoi() {
        return dosApplicationLoi;
    }

    /**
     * @param dosApplicationLoi
     *            the dosApplicationLoi to set
     */
    public void setDosApplicationLoi(List<ElasticApplicationLoi> dosApplicationLoi) {
        this.dosApplicationLoi = dosApplicationLoi;
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
     * @return the dosDateEffet
     */
    public List<Date> getDosDateEffet() {
        return dosDateEffet;
    }

    /**
     * @param dosDateEffet
     *            the dosDateEffet to set
     */
    public void setDosDateEffet(List<Date> dosDateEffet) {
        this.dosDateEffet = dosDateEffet;
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

    public String getDosDirectionAttacheId() {
        return dosDirectionAttacheId;
    }

    public void setDosDirectionAttacheId(String dosDirectionAttacheId) {
        this.dosDirectionAttacheId = dosDirectionAttacheId;
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

    public void setDosDirectionRespId(String dosDirectionRespId) {
        this.dosDirectionRespId = dosDirectionRespId;
    }

    public String getDosDirectionRespId() {
        return dosDirectionRespId;
    }

    public String getDosDirectionResp() {
        return dosDirectionResp;
    }

    public void setDosDirectionResp(String dosDirectionResp) {
        this.dosDirectionResp = dosDirectionResp;
    }

    public String getDosDossierCompletErreur() {
        return dosDossierCompletErreur;
    }

    public void setDosDossierCompletErreur(String dosDossierCompletErreur) {
        this.dosDossierCompletErreur = dosDossierCompletErreur;
    }

    /**
     * @return the dosEmetteur
     */
    public String getDosEmetteur() {
        return dosEmetteur;
    }

    /**
     * @param dosEmetteur
     *            the dosEmetteur to set
     */
    public void setDosEmetteur(String dosEmetteur) {
        this.dosEmetteur = dosEmetteur;
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

    public String getDosMinistereAttacheId() {
        return dosMinistereAttacheId;
    }

    public void setDosMinistereAttacheId(String dosMinistereAttacheId) {
        this.dosMinistereAttacheId = dosMinistereAttacheId;
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

    public String getDosMinistereRespId() {
        return dosMinistereRespId;
    }

    public void setDosMinistereRespId(String dosMinistereRespId) {
        this.dosMinistereRespId = dosMinistereRespId;
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
    public List<ElasticTranspositionDirective> getDosTranspositionDirective() {
        return dosTranspositionDirective;
    }

    /**
     * @param dosTranspositionDirective
     *            the dosTranspositionDirective to set
     */
    public void setDosTranspositionDirective(List<ElasticTranspositionDirective> dosTranspositionDirective) {
        this.dosTranspositionDirective = dosTranspositionDirective;
    }

    /**
     * @return the dosTranspositionOrdonnance
     */
    public List<ElasticTranspositionOrdonnance> getDosTranspositionOrdonnance() {
        return dosTranspositionOrdonnance;
    }

    /**
     * @param dosTranspositionOrdonnance
     *            the dosTranspositionOrdonnance to set
     */
    public void setDosTranspositionOrdonnance(List<ElasticTranspositionOrdonnance> dosTranspositionOrdonnance) {
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

    /**
     * @return the ecmCurrentLifeCycleState
     */
    public String getEcmCurrentLifeCycleState() {
        return ecmCurrentLifeCycleState;
    }

    /**
     * @param ecmCurrentLifeCycleState
     *            the ecmCurrentLifeCycleState to set
     */
    public void setEcmCurrentLifeCycleState(String ecmCurrentLifeCycleState) {
        this.ecmCurrentLifeCycleState = ecmCurrentLifeCycleState;
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
    public List<ElasticParutionBo> getRetdilaParutionBo() {
        return retdilaParutionBo;
    }

    /**
     * @param retdilaParutionBo
     *            the retdilaParutionBo to set
     */
    public void setRetdilaParutionBo(List<ElasticParutionBo> retdilaParutionBo) {
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

    public List<ElasticDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<ElasticDocument> documents) {
        this.documents = documents;
    }

    public List<ElasticStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ElasticStep> steps) {
        this.steps = steps;
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
