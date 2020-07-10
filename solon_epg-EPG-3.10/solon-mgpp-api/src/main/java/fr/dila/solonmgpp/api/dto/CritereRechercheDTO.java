package fr.dila.solonmgpp.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.SortInfo;

/**
 * DTO de critere de recherche pour la recherche simple
 * 
 * @author asatre
 * 
 */
public interface CritereRechercheDTO extends Map<String, Serializable> {

    public static final String PROCEDURE = "procedure";
    public static final String TYPE_EVENEMENT = "typeEvenement";
    public static final String TYPE_ACTES = "typeActes";
    public static final String NOR = "nor";
    public static final String NOR_LOI = "norLoi";
    public static final String OBJET = "objet";
    public static final String NOM_ORGANISME = "nomOrganisme";
    public static final String MENU = "menu";
    public static final String NUMERO_NOR = "numeroNor";
    public static final String TITRE_ACTE = "titreActe";
    public static final String CODE_LECTURE = "codeLecture";
    public static final String NIVEAU_LECTURE = "niveauLecture";
    public static final String EMETTEUR = "emetteur";
    public static final String DESTINATAIRE = "destinataire";
    public static final String COPIE = "copie";
    public static final String DATE_EVENEMENT_MAX = "dateEvenementMax";
    public static final String DATE_EVENEMENT_MIN = "dateEvenementMin";
    public static final String ID_EVENEMENT = "idEvenement";
    public static final String IDS_CORBEILLE = "idsCorbeille";
    public static final String PRESENCE_PIECE_JOINTE = "presencePieceJointe";
    public static final String ETAT_MESSAGE = "etatMessage";
    public static final String ETAT_MESSAGES = "etatMessages";
    public static final String ETAT_EVENEMENT = "etatEvenement";
    public static final String ANCIENNETE_MESSAGE = "ancienneteMessage";
    public static final String ID_EVENEMENT_PRECEDENT = "idEvenementPrecedent";
    public static final String ID_DOSSIER = "idDossier";
    public static final String EMETTEURS = "emetteurs";
    public static final String DESTINATAIRES = "destinataires";
    public static final String COPIES = "copies";
    public static final String HORODATAGE = "horodatage";
    public static final String HORODATAGE_MAX = "horodatageMax";
    public static final String NIVEAU_LECTURE_CODE = "niveauLectureCode";
    public static final String NIVEAU_LECTURE_NIVEAU = "niveauLectureNiveau";
    public static final String DATE_AR = "dateAr";
    public static final String DATE_AR_MAX = "dateArMax";
    public static final String DATE_DEMANDE = "dateDemande";
    public static final String DATE_DEMANDE_MAX = "dateDemandeMax";
    public static final String TYPE_LOI = "typeLoi";
    public static final String NATURE_LOI = "natureLoi";
    public static final String AUTEURS = "auteurs";
    public static final String CO_AUTEURS = "coAuteurs";
    public static final String INTITULE = "intitule";
    public static final String COMMENTAIRE = "commentaire";
    public static final String URL_DOSSIER_AN = "urlDossierAn";
    public static final String URL_DOSSIER_SENAT = "urlDossierSenat";
    public static final String CO_SIGNATAIRE_COLLECTIF = "coSignataireCollectif";
    public static final String DATE_DEPOT_TEXTE_MAX = "dateDepotTexteMax";
    public static final String DATE_DEPOT_TEXTE = "dateDepotTexte";
    public static final String RESULTAT_CMP = "resultatCMP";
    public static final String DATE_CMP = "dateCMP";
    public static final String DATE_CMP_MAX = "dateCMPMax";
    public static final String DEPOT_NUMERO = "depotNumero";
    public static final String COMMISSION_SAISIE_AU_FOND = "commissionSaisieAuFond";
    public static final String COMMISSION_SAISIE_POUR_AVIS = "commissionSaisiePourAvis";
    public static final String DATE_RETRAIT = "dateRetrait";
    public static final String DATE_RETRAIT_MAX = "dateRetraitMax";
    public static final String DATE_DISTRIBUTION = "dateDistribution";
    public static final String DATE_DISTRIBUTION_MAX = "dateDistributionMax";
    public static final String NATURE_RAPPORT = "natureRapport";
    public static final String RAPPORTEUR = "rapporteur";
    public static final String TITRE = "titre";
    public static final String DATE_DEPOT_RAPPORT = "dateDepotRapport";
    public static final String DATE_DEPOT_RAPPORT_MAX = "dateDepotRapportMax";
    public static final String NUMERO_DEPOT_RAPPORT = "numeroDepotRapport";
    public static final String COMMISSION_SAISIE = "commissionSaisie";
    public static final String COMMISSIONS = "commissions";
    public static final String DATE_REFUS = "dateRefus";
    public static final String DATE_REFUSMAX = "dateRefusmax";
    public static final String LIBELLE_ANNEXES = "libelleAnnexes";
    public static final String DATE_ENGAGEMENT_PROCEDURE = "dateEngagementProcedure";
    public static final String DATE_ENGAGEMENT_PROCEDURE_MAX = "dateEngagementProcedureMax";
    public static final String DATE_REFUS_PROCEDURE_ENGAGEMENT_AN = "dateRefusProcedureEngagementAn";
    public static final String DATE_REFUS_PROCEDURE_ENGAGEMENT_AN_MAX = "dateRefusProcedureEngagementAnMax";
    public static final String DATE_REFUS_PROCEDURE_ENGAGEMENT_SENAT = "dateRefusProcedureEngagementSenat";
    public static final String DATE_REFUS_PROCEDURE_ENGAGEMENT_SENAT_MAX = "dateRefusProcedureEngagementSenatMax";
    public static final String DATE_ADOPTION = "dateAdoption";
    public static final String DATE_ADOPTION_MAX = "dateAdoptionMax";
    public static final String NUMERO_TEXTE_ADOPTE = "numeroTexteAdopte";
    public static final String SORT_ADOPTION = "sortAdoption";
    public static final String POSITION_ALERTE = "positionAlerte";
    public static final String REDEPOT = "redepot";
    public static final String MOTIF_IRRECEVABILITE = "motifIrrecevabilite";
    public static final String DATE = "date";
    public static final String DATE_MAX = "dateMax";
    public static final String ANNEE_RAPPORT = "anneeRapport";
    public static final String URL_BASE_LEGALE = "urlBaseLegale";
    public static final String BASE_LEGALE = "baseLegale";
    public static final String TITULAIRES = "titulaires";
    public static final String SUPPLEANT = "suppleant";
    public static final String DATE_PROMULGATION = "datePromulgation";
    public static final String DATE_PROMULGATION_MAX = "datePromulgationMax";
    public static final String DATE_PUBLICATION = "datePublication";
    public static final String DATE_PUBLICATION_MAX = "datePublicationMax";
    public static final String NUMERO_LOI = "numeroLoi";
    public static final String TYPE_ACTE = "typeActe";
    public static final String DATE_ACTE = "dateActe";
    public static final String DATE_ACTE_MAX = "dateActeMax";
    public static final String DATE_CONVOCATION = "dateConvocation";
    public static final String DATE_CONVOCATION_MAX = "dateConvocationMax";
    public static final String DATE_DESIGNATION = "dateDesignation";
    public static final String DATE_DESIGNATION_MAX = "dateDesignationMax";
    public static final String NUMERO_JO = "numeroJo";
    public static final String PAGE_JO = "pageJo";
    public static final String ANNEE_JO = "anneeJo";
    public static final String DATE_JO = "dateJo";
    public static final String DATE_JO_MAX = "dateJoMax";
    public static final String NUMERO_RUBRIQUE = "numeroRubrique";
    public static final String URL_PUBLICATION = "urlPublication";
    public static final String ECHEANCE = "echeance";
    public static final String SENS_AVIS = "sensAvis";
    public static final String NOMBRE_SUFFRAGE = "nombreSuffrage";
    public static final String VOTE_POUR = "votePour";
    public static final String VOTE_CONTRE = "voteContre";
    public static final String ABSTENTION = "abstention";
    public static final String BULLETIN_BLANC = "bulletinBlanc";
    public static final String DATE_CADUCITE = "dateCaducite";
    public static final String DATE_CADUCITE_MAX = "dateCaduciteMax";
    public static final String RAPPORT_PARLEMENT = "rapportParlement";
    public static final String DOSSIER_CIBLE = "dossierCible";
    public static final String DATE_CONGRES = "dateCongres";
    public static final String DATE_CONGRES_MAX = "dateCongresMax";
    public static final String DOSSIER_LEGISLATIF = "dossierLegislatif";
    public static final String VISA_INTERNE = "visa_interne";
    public static final String EN_ATTENTE = "en_attente";
    public static final String IDENTIFIANT_METIER = "identifiantMetier";
    public static final String RECTIFICATIF = "rectificatif";
    public static final String PIECE_JOINTE_LABEL = "pieceJointeLabel";
    public static final String PIECE_JOINTE_FULL_TEXT = "pieceJointeFullText";
    public static final String DATE_PRESENTATION = "datePresentation";
    public static final String DATE_PRESENTATION_MAX = "datePresentationMax";
    public static final String DATE_LETTRE_PM = "dateLettrePm";
    public static final String DATE_LETTRE_PM_MAX = "dateLettrePmMax";
    public static final String DATE_VOTE = "dateVote";
    public static final String DATE_VOTE_MAX = "dateVoteMax";
    public static final String DATE_DECLARATION = "dateDeclaration";
    public static final String DATE_DECLARATION_MAX = "dateDeclarationMax";
    public static final String DEMANDE_VOTE = "demandeVote";
    public static final String GROUPE_PARLEMENTAIRE = "groupeParlementaire";
    public static final String ORGANISME = "organisme";    
    public static final String DATE_AUDITION = "dateAudition";
    public static final String DATE_AUDITION_MAX = "dateAuditionMax";
    public static final String FONCTION = "fonction";
    public static final String PERSONNE = "personne";
    public static final String DATE_REFUS_ENG_PROC_ACC = "dateRefusEngagementProcedure";
    public static final String DATE_REFUS_ENG_PROC_ACC_MAX = "dateRefusEngagementProcedureMax";
    public static final String DATE_REFUS_ENG_PROC_ACC_ASS1 = "dateRefusAssemblee1";
    public static final String DATE_REFUS_ENG_PROC_ACC_ASS1_MAX = "dateRefusAssemblee1Max";
    public static final String DATE_CONF_ASS2 = "dateConferenceAssemblee2";
    public static final String DATE_CONF_ASS2_MAX = "dateConferenceAssemblee2Max";
    public static final String DECISION_ENG_PROC_ACC = "decisionProcAcc";


    
    List<String> getTypeEvenement();

    void setTypeEvenement(List<String> typeEvenement);

    String getProcedure();
    
    void setProcedure(String procedure);
    
    String getNor();

    void setNor(String nor);
    
    String getNorLoi();

    void setNorLoi(String norLoi);

    String getObjet();

    void setObjet(String objet);

    String getNomOrganisme();

    void setNomOrganisme(String nomOrganisme);

    String getMenu();

    void setMenu(String menu);
    
    Long getBulletinBlanc();

    void setBulletin(Long bulletinBlanc);

    String getNumeroNor();

    void setNumeroNor(String numeroNor);

    String getTitreActe();

    void setTitreActe(String titreActe);

    List<String> getTypeActes();

    void setTypeActes(List<String> typeActe);

    void setCodeLecture(String codeLecture);

    void setNiveauLecture(Long niveauLecture);

    void setEmetteur(String emetteur);

    void setDestinataire(String destinataire);

    void setCopie(String copie);

    void setDateEvenementMax(Date dateEvenementMax);

    void setDateEvenementMin(Date dateEvenementMin);

    void setIdEvenement(String idEvenement);

    String getCodeLecture();

    Long getNiveauLecture();

    String getEmetteur();

    String getDestinataire();

    String getCopie();

    Date getDateEvenementMax();

    Date getDateEvenementMin();

    String getIdEvenement();

    List<String> getIdsCorbeille();

    void setIdsCorbeille(List<String> idsCorbeille);
    
    List<String> getVisaInterne();

    void setVisaInterne(List<String> interne);

    Boolean isEnAttente();

    void setEnAttente(Boolean enAttente);

    void setSortInfos(List<SortInfo> sortInfos);

    List<SortInfo> getSortInfos();

    void setEtatMessages(List<String> etatMessages);

    List<String> getEtatMessages();

    void setEtatMessage(String etatMessage);

    String getEtatMessage();

    void setEtatEvenement(String etatEvenement);
    
    String getEtatEvenement();

    void setPresencePieceJointe(Boolean presencePieceJointe);

    Boolean getPresencePieceJointe();

    void setAncienneteMessage(Long ancienneteMessage);

    Long getAncienneteMessage();

    String getIdEvenementPrecedent();

    String getIdDossier();

    void setIdDossier(String idDossier);

    List<String> getEmetteurs();

    List<String> getDestinataires();

    List<String> getCopies();

    Date getHorodatage();

    Date getHorodatageMax();

    String getNiveauLectureCode();

    Long getNiveauLectureNiveau();

    Date getDateAr();

    Date getDateArMax();
    
    Date getDateDemande();

    Date getDateDemandeMax();

    String getTypeLoi();

    String getNatureLoi();

    List<String> getAuteurs();

    List<String> getCoAuteurs();

    String getIntitule();

    String getCommentaire();

    String getUrlDossierAn();

    String getUrlDossierSenat();

    String getCoSignataireCollectif();

    String getResultatCMP();

    Date getDateCMP();

    Date getDateCMPMax();

    String getDepotNumero();

    List<String> getCommissionSaisieAuFond();

    List<String> getCommissionSaisiePourAvis();

    Date getDateRetrait();

    Date getDateRetraitMax();

    Date getDateDistributionElectronique();

    Date getDateDistributionElectroniqueMax();

    String getNatureRapport();

    List<String> getRapporteur();

    String getTitre();

    Date getDateDepotRapport();

    Date getDateDepotRapportMax();

    String getNumeroDepotRapport();

    List<String> getCommissionSaisie();

    List<String> getCommissions();

    Date getDateRefus();

    Date getDateRefusMax();

    List<String> getLibelleAnnexe();

    Date getDateEngagementProcedure();

    Date getDateEngagementProcedureMax();

    Date getDateRefusProcedureEngagementAn();

    Date getDateRefusProcedureEngagementAnMax();

    Date getDateRefusProcedureEngagementSenat();

    Date getDateRefusProcedureEngagementSenatMax();

    Date getDateAdoption();

    Date getDateAdoptionMax();

    String getNumeroTexteAdopte();

    String getSortAdoption();

    Boolean getPositionAlerte();

    Boolean getRedepot();

    Boolean getRectificatif();
    
    String getMotifIrrecevabilite();

    Date getDate();

    Date getDateMax();

    Long getAnneeRapport();

    String getUrlBaseLegale();

    String getBaseLegale();

    List<String> getTitulaires();

    List<String> getSuppleant();

    Date getDatePromulgation();

    Date getDatePromulgationMax();

    Date getDatePublication();

    Date getDatePublicationMax();

    String getNumeroLoi();

    String getTypeActe();

    Date getDateActe();

    Date getDateActeMax();

    Date getDateConvocation();

    Date getDateConvocationMax();

    Date getDateDesignation();

    Date getDateDesignationMax();

    Long getNumeroJo();

    Long getPageJo();

    Long getAnneeJo();

    Date getDateJo();

    Date getDateJoMax();

    Long getNumeroRubrique();

    String getUrlPublication();

    String getEcheance();

    String getSensAvis();

    Long getSuffrageExprime();

    Long getVotePour();

    Long getVoteContre();

    Long getAbstention();

    Date getDateCaducite();

    Date getDateCaduciteMax();

    String getDossierCible();

    String getRapportParlement();

    Date getDateCongres();

    Date getDateCongresMax();

    List<String> getDossierLegislatif();

    Date getDateDepotTexte();

    Date getDateDepotTexteMax();

    String getIdentifiantMetier();
    
    void setIdentifiantMetier(String identifiantMetier);
    
    String getPieceJointeLabel();
    
    String getPieceJointeFullText();
    
    Date getDatePresentation();

    Date getDatePresentationMax();

    Date getDateLettrePm();

    Date getDateLettrePmMax();

    Date getDateVote();

    Date getDateVoteMax();

    Date getDateDeclaration();

    Date getDateDeclarationMax();
    
    Boolean getDemandeVote() ;

    List<String> getGroupeParlementaire();
    
    List<String> getOrganisme() ;

    Date getDateAudition();

    Date getDateAuditionMax();
    
    String getPersonne() ;
    
    String getFonction() ;
    
    Date getDateRefusEngagementProcAcc();
    
    Date getDateRefusEngagementProcAccAssMax();
    
    Date getDateRefusEngagementProcAccAss1();
    
    Date getDateRefusEngagementProcAccAss1Max();
    
    Date getDateConfPresidentsAss2();
    
    Date getDateConfPresidentsAss2Max();
    
    String getDecisionEngProcAcc();
    
}
