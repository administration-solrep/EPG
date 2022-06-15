package fr.dila.solonmgpp.core.dto;

import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.util.SolonDateConverter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.actions.Action;

/**
 * Implementation de {@link CritereRechercheDTO}
 *
 * @author asatre
 *
 */
public class CritereRechercheDTOImpl extends AbstractMapDTO implements CritereRechercheDTO, Serializable {
    private static final String LA_RECHERCHE_NECESSITE_UN_MENU = "La recherche nécessite un menu";

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
    public static final String CODE_LECTURE = "niveauLecture";
    public static final String NIVEAU_LECTURE = "niveauLectureNumero";
    public static final String EMETTEUR = "emetteur";
    public static final String DESTINATAIRE = "destinataire";
    public static final String COPIE = "destinataireCopie";
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
    public static final String HORODATAGE = "horodatageDebut";
    public static final String HORODATAGE_MAX = "horodatageFin";
    public static final String DATE_AR = "dateArDebut";
    public static final String DATE_AR_MAX = "dateArFin";
    public static final String DATE_DEMANDE = "dateDemandeDebut";
    public static final String DATE_DEMANDE_MAX = "dateDemandeFin";
    public static final String TYPE_LOI = "typeLoi";
    public static final String NATURE_LOI = "natureLoi";
    public static final String AUTEURS = "auteur";
    public static final String CO_AUTEURS = "coauteur";
    public static final String INTITULE = "intitule";
    public static final String DESCRIPTION = "description";
    public static final String URL_DOSSIER_AN = "urlDossierAn";
    public static final String URL_DOSSIER_SENAT = "urlDossierSenat";
    public static final String COSIGNATAIRE = "cosignataire";
    public static final String DATE_DEPOT_TEXTE_MAX = "dateDepotTexteFin";
    public static final String DATE_DEPOT_TEXTE = "dateDepotTexteDebut";
    public static final String RESULTAT_CMP = "resultatCMP";
    public static final String DATE_CMP = "dateCMPDebut";
    public static final String DATE_CMP_MAX = "dateCMPFin";
    public static final String DEPOT_NUMERO = "numeroDepotTexte";
    public static final String COMMISSION_SAISIE_AU_FOND = "commissionSaisieAuFond";
    public static final String COMMISSION_SAISIE_POUR_AVIS = "commissionSaisiePourAvis";
    public static final String DATE_RETRAIT = "dateRetraitDebut";
    public static final String DATE_RETRAIT_MAX = "dateRetraitFin";
    public static final String DATE_DISTRIBUTION = "dateDistributionElectroniqueDebut";
    public static final String DATE_DISTRIBUTION_MAX = "dateDistributionElectroniqueFin";
    public static final String NATURE_RAPPORT = "natureRapport";
    public static final String RAPPORTEUR = "rapporteurList";
    public static final String TITRE = "titre";
    public static final String DATE_DEPOT_RAPPORT = "dateDepotRapportDebut";
    public static final String DATE_DEPOT_RAPPORT_MAX = "dateDepotRapportFin";
    public static final String NUMERO_DEPOT_RAPPORT = "numeroDepotRapport";
    public static final String COMMISSION_SAISIE = "commissionSaisie";
    public static final String COMMISSIONS = "commissions";
    public static final String DATE_REFUS = "dateRefusDebut";
    public static final String DATE_REFUS_MAX = "dateRefusFin";
    public static final String LIBELLE_ANNEXES = "libelleAnnexe";
    public static final String DATE_ENGAGEMENT_PROCEDURE = "dateEngagementProcedureDebut";
    public static final String DATE_ENGAGEMENT_PROCEDURE_MAX = "dateEngagementProcedureFin";
    public static final String DATE_REFUS_PROCEDURE_ENGAGEMENT_AN = "dateRefusProcedureEngagementAn";
    public static final String DATE_REFUS_PROCEDURE_ENGAGEMENT_AN_MAX = "dateRefusProcedureEngagementAnMax";
    public static final String DATE_REFUS_PROCEDURE_ENGAGEMENT_SENAT = "dateRefusProcedureEngagementSenat";
    public static final String DATE_REFUS_PROCEDURE_ENGAGEMENT_SENAT_MAX = "dateRefusProcedureEngagementSenatMax";
    public static final String DATE_ADOPTION = "dateAdoptionDebut";
    public static final String DATE_ADOPTION_MAX = "dateAdoptionFin";
    public static final String NUMERO_TEXTE_ADOPTE = "numeroTexteAdopte";
    public static final String SORT_ADOPTION = "sortAdoption";
    public static final String POSITION_ALERTE = "positionAlerte";
    public static final String REDEPOT = "redepot";
    public static final String MOTIF_IRRECEVABILITE = "motifIrrecevabilite";
    public static final String DATE = "dateDebut";
    public static final String DATE_MAX = "dateFin";
    public static final String ANNEE_RAPPORT = "anneeRapport";
    public static final String URL_BASE_LEGALE = "urlBaseLegale";
    public static final String BASE_LEGALE = "baseLegale";
    public static final String TITULAIRES = "parlementaireTitulaireList";
    public static final String SUPPLEANT = "parlementaireSuppleantList";
    public static final String DATE_PROMULGATION = "datePromulgationDebut";
    public static final String DATE_PROMULGATION_MAX = "datePromulgationFin";
    public static final String DATE_PUBLICATION = "datePublicationDebut";
    public static final String DATE_PUBLICATION_MAX = "datePublicationFin";
    public static final String NUMERO_LOI = "numeroLoi";
    public static final String TYPE_ACTE = "typeActe";
    public static final String DATE_ACTE = "dateActeDebut";
    public static final String DATE_ACTE_MAX = "dateActeFin";
    public static final String DATE_CONVOCATION = "dateConvocationDebut";
    public static final String DATE_CONVOCATION_MAX = "dateConvocationFin";
    public static final String DATE_DESIGNATION = "dateDesignationDebut";
    public static final String DATE_DESIGNATION_MAX = "dateDesignationFin";
    public static final String NUMERO_JO = "numeroJo";
    public static final String PAGE_JO = "pageJo";
    public static final String ANNEE_JO = "anneeJo";
    public static final String DATE_JO = "dateJoDebut";
    public static final String DATE_JO_MAX = "dateJoFin";
    public static final String NUMERO_RUBRIQUE = "numeroRubrique";
    public static final String URL_PUBLICATION = "urlPublication";
    public static final String ECHEANCE = "echeance";
    public static final String SENS_AVIS = "sensAvis";
    public static final String NOMBRE_SUFFRAGE = "suffrageExprime";
    public static final String VOTE_POUR = "votePour";
    public static final String VOTE_CONTRE = "voteContre";
    public static final String ABSTENTION = "abstention";
    public static final String BULLETIN_BLANC = "bulletinBlanc";
    public static final String DATE_CADUCITE = "dateCaduciteDebut";
    public static final String DATE_CADUCITE_MAX = "dateCaduciteFin";
    public static final String RAPPORT_PARLEMENT = "rapportParlement";
    public static final String DOSSIER_CIBLE = "dossierCible";
    public static final String DATE_CONGRES = "dateCongresDebut";
    public static final String DATE_CONGRES_MAX = "dateCongresFin";
    public static final String DOSSIER_LEGISLATIF = "dossierLegislatif";
    public static final String VISA_INTERNE = "visa_interne";
    public static final String EN_ATTENTE = "en_attente";
    public static final String IDENTIFIANT_METIER = "identifiantMetier";
    public static final String RECTIFICATIF = "rectificatif";
    public static final String PIECE_JOINTE_LABEL = "pieceJointeLabel";
    public static final String PIECE_JOINTE_FULL_TEXT = "pieceJointeFullText";
    public static final String DATE_PRESENTATION = "datePresentationDebut";
    public static final String DATE_PRESENTATION_MAX = "datePresentationFin";
    public static final String DATE_LETTRE_PM = "dateLettrePmDebut";
    public static final String DATE_LETTRE_PM_MAX = "dateLettrePmFin";
    public static final String DATE_VOTE = "dateVoteDebut";
    public static final String DATE_VOTE_MAX = "dateVoteFin";
    public static final String DATE_DECLARATION = "dateDeclarationDebut";
    public static final String DATE_DECLARATION_MAX = "dateDeclarationFin";
    public static final String DEMANDE_VOTE = "demandeVote";
    public static final String GROUPE_PARLEMENTAIRE = "groupeParlementaire";
    public static final String ORGANISME = "organisme";
    public static final String DATE_AUDITION = "dateAuditionDebut";
    public static final String DATE_AUDITION_MAX = "dateAuditionFin";
    public static final String FONCTION = "fonction";
    public static final String PERSONNE = "personne";
    public static final String DATE_REFUS_ENG_PROC_ACC = "dateRefusEngagementProcedureDebut";
    public static final String DATE_REFUS_ENG_PROC_ACC_MAX = "dateRefusEngagementProcedureFin";
    public static final String DATE_REFUS_ENG_PROC_ACC_ASS1 = "dateRefusAssemblee1Debut";
    public static final String DATE_REFUS_ENG_PROC_ACC_ASS1_MAX = "dateRefusAssemblee1Fin";
    public static final String DATE_CONF_ASS2 = "dateConferenceAssemblee2Debut";
    public static final String DATE_CONF_ASS2_MAX = "dateConferenceAssemblee2Fin";
    public static final String DECISION_ENG_PROC_ACC = "decisionProcAcc";

    private static final long serialVersionUID = -2228482479711800842L;

    private List<SortInfo> sortInfos;

    public CritereRechercheDTOImpl() {
        super();
        initList();
    }

    public CritereRechercheDTOImpl(Action menu) {
        super();
        if (menu == null) {
            throw new UnsupportedOperationException(LA_RECHERCHE_NECESSITE_UN_MENU);
        } else {
            init(menu.getId());
        }
    }

    public CritereRechercheDTOImpl(String menu) {
        super();
        init(menu);
    }

    private void init(String menu) {
        initList();

        if (StringUtils.isNotBlank(menu)) {
            setMenu(menu);
            // initialisation des restrictions obligatoires
            SolonMgppServiceLocator.getRechercheService().initCritereRechercheDTO(this);
        } else {
            throw new UnsupportedOperationException(LA_RECHERCHE_NECESSITE_UN_MENU);
        }
    }

    private void initList() {
        setIdsCorbeille(new ArrayList<String>());
        setVisaInterne(new ArrayList<String>());
        setTypeEvenement(new ArrayList<String>());
        setTypeActes(new ArrayList<String>());
        setSortInfos(new ArrayList<SortInfo>());
        setEtatMessages(new ArrayList<String>());
    }

    @Override
    protected Date getDate(String key) {
        Object value = get(key);
        return value instanceof String
            ? SolonDateConverter.DATE_SLASH.parseToDateOrNull((String) value)
            : super.getDate(key);
    }

    @Override
    protected List<String> getListString(String key) {
        Object value = get(key);
        return value instanceof String ? Collections.singletonList((String) value) : super.getListString(key);
    }

    @Override
    public List<String> getTypeEvenement() {
        return getListString(TYPE_EVENEMENT);
    }

    @Override
    public void setTypeEvenement(List<String> typeEvenement) {
        putListString(TYPE_EVENEMENT, typeEvenement);
    }

    @Override
    public List<String> getTypeActes() {
        return getListString(TYPE_ACTES);
    }

    @Override
    public void setTypeActes(List<String> typeActe) {
        putListString(TYPE_ACTES, typeActe);
    }

    @Override
    public String getNor() {
        return getString(NOR);
    }

    @Override
    public void setNor(String nor) {
        put(NOR, nor);
    }

    @Override
    public String getNorLoi() {
        return getString(NOR_LOI);
    }

    @Override
    public void setNorLoi(String norLoi) {
        put(NOR_LOI, norLoi);
    }

    @Override
    public String getObjet() {
        return getString(OBJET);
    }

    @Override
    public void setObjet(String objet) {
        put(OBJET, objet);
    }

    @Override
    public String getNomOrganisme() {
        return getString(NOM_ORGANISME);
    }

    @Override
    public void setNomOrganisme(String nomOrganisme) {
        put(NOM_ORGANISME, nomOrganisme);
    }

    @Override
    public String getMenu() {
        return getString(MENU);
    }

    @Override
    public void setMenu(String menu) {
        put(MENU, menu);
    }

    @Override
    public String getType() {
        return getString("");
    }

    @Override
    public String getDocIdForSelection() {
        return getString("");
    }

    @Override
    public String getNumeroNor() {
        return getString(NUMERO_NOR);
    }

    @Override
    public void setNumeroNor(String numeroNor) {
        put(NUMERO_NOR, numeroNor);
    }

    @Override
    public String getTitreActe() {
        return getString(TITRE_ACTE);
    }

    @Override
    public void setTitreActe(String titreActe) {
        put(TITRE_ACTE, titreActe);
    }

    @Override
    public void setCodeLecture(String codeLecture) {
        put(CODE_LECTURE, codeLecture);
    }

    @Override
    public void setNiveauLecture(Long niveauLecture) {
        put(NIVEAU_LECTURE, niveauLecture);
    }

    @Override
    public void setEmetteur(String emetteur) {
        put(EMETTEUR, emetteur);
    }

    @Override
    public void setDestinataire(String destinataire) {
        put(DESTINATAIRE, destinataire);
    }

    @Override
    public void setCopie(String copie) {
        put(COPIE, copie);
    }

    @Override
    public void setDateEvenementMax(Date dateEvenementMax) {
        put(DATE_EVENEMENT_MAX, dateEvenementMax);
    }

    @Override
    public void setDateEvenementMin(Date dateEvenementMin) {
        put(DATE_EVENEMENT_MIN, dateEvenementMin);
    }

    @Override
    public void setIdEvenement(String idEvenement) {
        put(ID_EVENEMENT, idEvenement);
    }

    @Override
    public String getCodeLecture() {
        return getString(CODE_LECTURE);
    }

    @Override
    public Long getNiveauLecture() {
        try {
            return getLong(NIVEAU_LECTURE);
        } catch (ClassCastException e) {
            // string a parser en long
            String str = getString(NIVEAU_LECTURE);
            if (!StringUtils.isBlank(str)) {
                return Long.parseLong(str);
            }
        }
        return null;
    }

    @Override
    public String getEmetteur() {
        return getString(EMETTEUR);
    }

    @Override
    public String getDestinataire() {
        return getString(DESTINATAIRE);
    }

    @Override
    public String getCopie() {
        return getString(COPIE);
    }

    @Override
    public Date getDateEvenementMax() {
        return getDate(DATE_EVENEMENT_MAX);
    }

    @Override
    public Date getDateEvenementMin() {
        return getDate(DATE_EVENEMENT_MIN);
    }

    @Override
    public String getIdEvenement() {
        return getString(ID_EVENEMENT);
    }

    @Override
    public List<String> getIdsCorbeille() {
        return getListString(IDS_CORBEILLE);
    }

    @Override
    public void setIdsCorbeille(List<String> idsCorbeille) {
        putListString(IDS_CORBEILLE, idsCorbeille);
    }

    @Override
    public void setVisaInterne(List<String> interne) {
        putListString(VISA_INTERNE, interne);
    }

    @Override
    public List<String> getVisaInterne() {
        return getListString(VISA_INTERNE);
    }

    @Override
    public void setEnAttente(Boolean enAttente) {
        put(EN_ATTENTE, enAttente);
    }

    @Override
    public Boolean isEnAttente() {
        try {
            return getBoolean(EN_ATTENTE);
        } catch (ClassCastException e) {
            // string a parser en boolean
            String str = getString(EN_ATTENTE);
            if (!StringUtils.isBlank(str)) {
                return Boolean.parseBoolean(str);
            }
        }
        return null;
    }

    @Override
    public void setSortInfos(List<SortInfo> sortInfos) {
        this.sortInfos = sortInfos;
    }

    @Override
    public List<SortInfo> getSortInfos() {
        return this.sortInfos;
    }

    @Override
    public void setPresencePieceJointe(Boolean presencePieceJointe) {
        put(PRESENCE_PIECE_JOINTE, presencePieceJointe);
    }

    @Override
    public Boolean getPresencePieceJointe() {
        try {
            return getBoolean(PRESENCE_PIECE_JOINTE);
        } catch (ClassCastException e) {
            // string a parser en boolean
            String str = getString(PRESENCE_PIECE_JOINTE);
            if (!StringUtils.isBlank(str)) {
                return Boolean.parseBoolean(str);
            }
        }
        return null;
    }

    @Override
    public void setEtatMessages(List<String> etatMessages) {
        putListString(ETAT_MESSAGES, etatMessages);
    }

    @Override
    public List<String> getEtatMessages() {
        return getListString(ETAT_MESSAGES);
    }

    @Override
    public void setEtatMessage(String etatMessage) {
        put(ETAT_MESSAGE, etatMessage);
    }

    @Override
    public String getEtatMessage() {
        return getString(ETAT_MESSAGE);
    }

    @Override
    public void setEtatEvenement(String etatEvenement) {
        put(ETAT_EVENEMENT, etatEvenement);
    }

    @Override
    public String getEtatEvenement() {
        return getString(ETAT_EVENEMENT);
    }

    @Override
    public void setAncienneteMessage(Long ancienneteMessage) {
        put(ANCIENNETE_MESSAGE, ancienneteMessage);
    }

    @Override
    public Long getAncienneteMessage() {
        try {
            return getLong(ANCIENNETE_MESSAGE);
        } catch (ClassCastException e) {
            // string a parser en long
            String str = getString(ANCIENNETE_MESSAGE);
            if (!StringUtils.isBlank(str)) {
                return Long.parseLong(str);
            }
        }
        return null;
    }

    @Override
    public String getIdEvenementPrecedent() {
        return getString(ID_EVENEMENT_PRECEDENT);
    }

    @Override
    public String getIdDossier() {
        return getString(ID_DOSSIER);
    }

    @Override
    public void setIdDossier(String idDossier) {
        put(ID_DOSSIER, idDossier);
    }

    @Override
    public List<String> getEmetteurs() {
        return getListString(EMETTEURS);
    }

    @Override
    public List<String> getDestinataires() {
        return getListString(DESTINATAIRES);
    }

    @Override
    public List<String> getCopies() {
        return getListString(COPIES);
    }

    @Override
    public Date getHorodatage() {
        return getDate(HORODATAGE);
    }

    @Override
    public Date getHorodatageMax() {
        return getDate(HORODATAGE_MAX);
    }

    @Override
    public Date getDateAr() {
        return getDate(DATE_AR);
    }

    @Override
    public Date getDateArMax() {
        return getDate(DATE_AR_MAX);
    }

    @Override
    public Date getDateDemande() {
        return getDate(DATE_DEMANDE);
    }

    @Override
    public Date getDateDemandeMax() {
        return getDate(DATE_DEMANDE_MAX);
    }

    @Override
    public String getTypeLoi() {
        return getString(TYPE_LOI);
    }

    @Override
    public String getNatureLoi() {
        return getString(NATURE_LOI);
    }

    @Override
    public List<String> getAuteurs() {
        return getListString(AUTEURS);
    }

    @Override
    public List<String> getCoAuteurs() {
        return getListString(CO_AUTEURS);
    }

    @Override
    public String getIntitule() {
        return getString(INTITULE);
    }

    @Override
    public String getDescription() {
        return getString(DESCRIPTION);
    }

    @Override
    public String getUrlDossierAn() {
        return getString(URL_DOSSIER_AN);
    }

    @Override
    public String getUrlDossierSenat() {
        return getString(URL_DOSSIER_SENAT);
    }

    @Override
    public String getCosignataire() {
        return getString(COSIGNATAIRE);
    }

    @Override
    public Date getDateDepotTexte() {
        return getDate(DATE_DEPOT_TEXTE);
    }

    @Override
    public Date getDateDepotTexteMax() {
        return getDate(DATE_DEPOT_TEXTE_MAX);
    }

    @Override
    public String getResultatCMP() {
        return getString(RESULTAT_CMP);
    }

    @Override
    public Date getDateCMP() {
        return getDate(DATE_CMP);
    }

    @Override
    public Date getDateCMPMax() {
        return getDate(DATE_CMP_MAX);
    }

    @Override
    public String getDepotNumero() {
        return getString(DEPOT_NUMERO);
    }

    @Override
    public List<String> getCommissionSaisieAuFond() {
        return getListString(COMMISSION_SAISIE_AU_FOND);
    }

    @Override
    public List<String> getCommissionSaisiePourAvis() {
        return getListString(COMMISSION_SAISIE_POUR_AVIS);
    }

    @Override
    public Date getDateRetrait() {
        return getDate(DATE_RETRAIT);
    }

    @Override
    public Date getDateRetraitMax() {
        return getDate(DATE_RETRAIT_MAX);
    }

    @Override
    public Date getDateDistributionElectronique() {
        return getDate(DATE_DISTRIBUTION);
    }

    @Override
    public Date getDateDistributionElectroniqueMax() {
        return getDate(DATE_DISTRIBUTION_MAX);
    }

    @Override
    public String getNatureRapport() {
        return getString(NATURE_RAPPORT);
    }

    @Override
    public List<String> getRapporteur() {
        return getListString(RAPPORTEUR);
    }

    @Override
    public String getTitre() {
        return getString(TITRE);
    }

    @Override
    public Date getDateDepotRapport() {
        return getDate(DATE_DEPOT_RAPPORT);
    }

    @Override
    public Date getDateDepotRapportMax() {
        return getDate(DATE_DEPOT_RAPPORT_MAX);
    }

    @Override
    public String getNumeroDepotRapport() {
        return getString(NUMERO_DEPOT_RAPPORT);
    }

    @Override
    public List<String> getCommissionSaisie() {
        return getListString(COMMISSION_SAISIE);
    }

    @Override
    public List<String> getCommissions() {
        return getListString(COMMISSIONS);
    }

    @Override
    public Date getDateRefus() {
        return getDate(DATE_REFUS);
    }

    @Override
    public Date getDateRefusMax() {
        return getDate(DATE_REFUS_MAX);
    }

    @Override
    public List<String> getLibelleAnnexe() {
        return getListString(LIBELLE_ANNEXES);
    }

    @Override
    public Date getDateEngagementProcedure() {
        return getDate(DATE_ENGAGEMENT_PROCEDURE);
    }

    @Override
    public Date getDateEngagementProcedureMax() {
        return getDate(DATE_ENGAGEMENT_PROCEDURE_MAX);
    }

    @Override
    public Date getDateRefusProcedureEngagementAn() {
        return getDate(DATE_REFUS_PROCEDURE_ENGAGEMENT_AN);
    }

    @Override
    public Date getDateRefusProcedureEngagementAnMax() {
        return getDate(DATE_REFUS_PROCEDURE_ENGAGEMENT_AN_MAX);
    }

    @Override
    public Date getDateRefusProcedureEngagementSenat() {
        return getDate(DATE_REFUS_PROCEDURE_ENGAGEMENT_SENAT);
    }

    @Override
    public Date getDateRefusProcedureEngagementSenatMax() {
        return getDate(DATE_REFUS_PROCEDURE_ENGAGEMENT_SENAT_MAX);
    }

    @Override
    public Date getDateAdoption() {
        return getDate(DATE_ADOPTION);
    }

    @Override
    public Date getDateAdoptionMax() {
        return getDate(DATE_ADOPTION_MAX);
    }

    @Override
    public String getNumeroTexteAdopte() {
        return getString(NUMERO_TEXTE_ADOPTE);
    }

    @Override
    public String getSortAdoption() {
        return getString(SORT_ADOPTION);
    }

    @Override
    public Boolean getPositionAlerte() {
        try {
            return getBoolean(POSITION_ALERTE);
        } catch (ClassCastException e) {
            // string a parser en boolean
            String str = getString(POSITION_ALERTE);
            if (!StringUtils.isBlank(str)) {
                return Boolean.parseBoolean(str);
            }
        }
        return null;
    }

    @Override
    public Boolean getRedepot() {
        try {
            return getBoolean(REDEPOT);
        } catch (ClassCastException e) {
            // string a parser en boolean
            String str = getString(REDEPOT);
            if (!StringUtils.isBlank(str)) {
                return Boolean.parseBoolean(str);
            }
        }
        return null;
    }

    @Override
    public Boolean getRectificatif() {
        try {
            return getBoolean(RECTIFICATIF);
        } catch (ClassCastException e) {
            // string a parser en boolean
            String str = getString(RECTIFICATIF);
            if (!StringUtils.isBlank(str)) {
                return Boolean.parseBoolean(str);
            }
        }
        return null;
    }

    @Override
    public String getMotifIrrecevabilite() {
        return getString(MOTIF_IRRECEVABILITE);
    }

    @Override
    public Date getDate() {
        return getDate(DATE);
    }

    @Override
    public Date getDateMax() {
        return getDate(DATE_MAX);
    }

    @Override
    public Long getAnneeRapport() {
        try {
            return getLong(ANNEE_RAPPORT);
        } catch (ClassCastException e) {
            // string a parser en long
            return Long.parseLong(getString(ANNEE_RAPPORT));
        }
    }

    @Override
    public String getUrlBaseLegale() {
        return getString(URL_BASE_LEGALE);
    }

    @Override
    public String getBaseLegale() {
        return getString(BASE_LEGALE);
    }

    @Override
    public List<String> getTitulaires() {
        return getListString(TITULAIRES);
    }

    @Override
    public List<String> getSuppleant() {
        return getListString(SUPPLEANT);
    }

    @Override
    public Date getDatePromulgation() {
        return getDate(DATE_PROMULGATION);
    }

    @Override
    public Date getDatePromulgationMax() {
        return getDate(DATE_PROMULGATION_MAX);
    }

    @Override
    public Date getDatePublication() {
        return getDate(DATE_PUBLICATION);
    }

    @Override
    public Date getDatePublicationMax() {
        return getDate(DATE_PUBLICATION_MAX);
    }

    @Override
    public String getNumeroLoi() {
        return getString(NUMERO_LOI);
    }

    @Override
    public String getTypeActe() {
        return getString(TYPE_ACTE);
    }

    @Override
    public Date getDateActe() {
        return getDate(DATE_ACTE);
    }

    @Override
    public Date getDateActeMax() {
        return getDate(DATE_ACTE_MAX);
    }

    @Override
    public Date getDateConvocation() {
        return getDate(DATE_CONVOCATION);
    }

    @Override
    public Date getDateConvocationMax() {
        return getDate(DATE_CONVOCATION_MAX);
    }

    @Override
    public Date getDateDesignation() {
        return getDate(DATE_DESIGNATION);
    }

    @Override
    public Date getDateDesignationMax() {
        return getDate(DATE_DESIGNATION_MAX);
    }

    @Override
    public Long getNumeroJo() {
        try {
            return getLong(NUMERO_JO);
        } catch (ClassCastException e) {
            // string a parser en long
            return Long.parseLong(getString(NUMERO_JO));
        }
    }

    @Override
    public Long getPageJo() {
        try {
            return getLong(PAGE_JO);
        } catch (ClassCastException e) {
            // string a parser en long
            return Long.parseLong(getString(PAGE_JO));
        }
    }

    @Override
    public Long getAnneeJo() {
        try {
            return getLong(ANNEE_JO);
        } catch (ClassCastException e) {
            // string a parser en long
            return Long.parseLong(getString(ANNEE_JO));
        }
    }

    @Override
    public Date getDateJo() {
        return getDate(DATE_JO);
    }

    @Override
    public Date getDateJoMax() {
        return getDate(DATE_JO_MAX);
    }

    @Override
    public Long getNumeroRubrique() {
        try {
            return getLong(NUMERO_RUBRIQUE);
        } catch (ClassCastException e) {
            // string a parser en long
            return Long.parseLong(getString(NUMERO_RUBRIQUE));
        }
    }

    @Override
    public String getUrlPublication() {
        return getString(URL_PUBLICATION);
    }

    @Override
    public String getEcheance() {
        return getString(ECHEANCE);
    }

    @Override
    public String getSensAvis() {
        return getString(SENS_AVIS);
    }

    @Override
    public Long getSuffrageExprime() {
        try {
            return getLong(NOMBRE_SUFFRAGE);
        } catch (ClassCastException e) {
            // string a parser en long
            return Long.parseLong(getString(NOMBRE_SUFFRAGE));
        }
    }

    @Override
    public Long getVotePour() {
        try {
            return getLong(VOTE_POUR);
        } catch (ClassCastException e) {
            // string a parser en long
            return Long.parseLong(getString(VOTE_POUR));
        }
    }

    @Override
    public Long getVoteContre() {
        try {
            return getLong(VOTE_CONTRE);
        } catch (ClassCastException e) {
            // string a parser en long
            return Long.parseLong(getString(VOTE_CONTRE));
        }
    }

    @Override
    public Long getAbstention() {
        try {
            return getLong(ABSTENTION);
        } catch (ClassCastException e) {
            // string a parser en long
            return Long.parseLong(getString(ABSTENTION));
        }
    }

    @Override
    public Date getDateCaducite() {
        return getDate(DATE_CADUCITE);
    }

    @Override
    public Date getDateCaduciteMax() {
        return getDate(DATE_CADUCITE_MAX);
    }

    @Override
    public String getDossierCible() {
        return getString(DOSSIER_CIBLE);
    }

    @Override
    public String getRapportParlement() {
        return getString(RAPPORT_PARLEMENT);
    }

    @Override
    public Date getDateCongres() {
        return getDate(DATE_CONGRES);
    }

    @Override
    public Date getDateCongresMax() {
        return getDate(DATE_CONGRES_MAX);
    }

    @Override
    public List<String> getDossierLegislatif() {
        return getListString(DOSSIER_LEGISLATIF);
    }

    @Override
    public String getProcedure() {
        return getString(PROCEDURE);
    }

    @Override
    public void setProcedure(String procedure) {
        put(PROCEDURE, procedure);
    }

    @Override
    public String getIdentifiantMetier() {
        return getString(IDENTIFIANT_METIER);
    }

    @Override
    public void setIdentifiantMetier(String procedure) {
        put(IDENTIFIANT_METIER, procedure);
    }

    @Override
    public Long getBulletinBlanc() {
        return getLong(BULLETIN_BLANC);
    }

    @Override
    public void setBulletin(Long bulletinBlanc) {
        put(BULLETIN_BLANC, bulletinBlanc);
    }

    @Override
    public String getPieceJointeLabel() {
        return getString(PIECE_JOINTE_LABEL);
    }

    @Override
    public String getPieceJointeFullText() {
        return getString(PIECE_JOINTE_FULL_TEXT);
    }

    @Override
    public Date getDatePresentation() {
        return getDate(DATE_PRESENTATION);
    }

    @Override
    public Date getDatePresentationMax() {
        return getDate(DATE_PRESENTATION_MAX);
    }

    @Override
    public Date getDateLettrePm() {
        return getDate(DATE_LETTRE_PM);
    }

    @Override
    public Date getDateLettrePmMax() {
        return getDate(DATE_LETTRE_PM_MAX);
    }

    @Override
    public Date getDateVote() {
        return getDate(DATE_VOTE);
    }

    @Override
    public Date getDateVoteMax() {
        return getDate(DATE_VOTE_MAX);
    }

    @Override
    public Date getDateDeclaration() {
        return getDate(DATE_DECLARATION);
    }

    @Override
    public Date getDateDeclarationMax() {
        return getDate(DATE_DECLARATION_MAX);
    }

    @Override
    public Boolean getDemandeVote() {
        try {
            return getBoolean(DEMANDE_VOTE);
        } catch (ClassCastException e) {
            // string a parser en boolean
            String str = getString(DEMANDE_VOTE);
            if (!StringUtils.isBlank(str)) {
                return Boolean.parseBoolean(str);
            }
        }
        return null;
    }

    @Override
    public List<String> getGroupeParlementaire() {
        return getListString(GROUPE_PARLEMENTAIRE);
    }

    @Override
    public List<String> getOrganisme() {
        return getListString(ORGANISME);
    }

    @Override
    public Date getDateAudition() {
        return getDate(DATE_AUDITION);
    }

    @Override
    public Date getDateAuditionMax() {
        return getDate(DATE_AUDITION_MAX);
    }

    @Override
    public String getPersonne() {
        return getString(PERSONNE);
    }

    @Override
    public String getFonction() {
        return getString(FONCTION);
    }

    @Override
    public Date getDateRefusEngagementProcAcc() {
        return getDate(DATE_REFUS_ENG_PROC_ACC);
    }

    @Override
    public Date getDateRefusEngagementProcAccAssMax() {
        return getDate(DATE_REFUS_ENG_PROC_ACC_MAX);
    }

    @Override
    public Date getDateRefusEngagementProcAccAss1() {
        return getDate(DATE_REFUS_ENG_PROC_ACC_ASS1);
    }

    @Override
    public Date getDateRefusEngagementProcAccAss1Max() {
        return getDate(DATE_REFUS_ENG_PROC_ACC_ASS1_MAX);
    }

    @Override
    public Date getDateConfPresidentsAss2() {
        return getDate(DATE_CONF_ASS2);
    }

    @Override
    public Date getDateConfPresidentsAss2Max() {
        return getDate(DATE_CONF_ASS2_MAX);
    }

    @Override
    public String getDecisionEngProcAcc() {
        return getString(DECISION_ENG_PROC_ACC);
    }
}
