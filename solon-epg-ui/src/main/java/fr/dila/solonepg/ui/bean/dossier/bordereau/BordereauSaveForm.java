package fr.dila.solonepg.ui.bean.dossier.bordereau;

import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.mapper.MapDoc2BeanIntegerStringProcess;
import fr.dila.st.ui.validators.annot.SwLength;
import java.util.ArrayList;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class BordereauSaveForm {

    public BordereauSaveForm() {
        super();
    }

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("typeActe")
    @SwLength(max = 10)
    private String typeActe;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("titreActe")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String titreActe;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_CATEGORIE_ACTE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("categorieActe")
    @SwLength(max = 50)
    private String categorieActe;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_PUBLICATION_RAPPORT_PRESENTATION_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("publicationRapportPresentation")
    private Boolean publicationRapportPresentation;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_DATE_PUBLICATION_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("datePublicationSouhaitee")
    private Calendar datePublicationSouhaitee;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_DATE_SIGNATURE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("dateSignature")
    private Calendar dateSignature;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_NOM_RESPONSABLE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("nomResponsable")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String nomResponsable;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_PRENOM_RESPONSABLE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("prenomResponsable")
    @SwLength(max = 150)
    private String prenomResponsable;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_QUALITE_RESPONSABLE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("qualiteResponsable")
    @SwLength(max = 250)
    private String qualiteResponsable;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_TEL_RESPONSABLE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("telResponsable")
    @SwLength(max = 150)
    private String telResponsable;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_MAIL_RESPONSABLE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("melResponsable")
    @SwLength(max = 150)
    private String melResponsable;

    @NxProp(
        xpath = ConseilEtatConstants.CONSEIL_ETAT_PRIORITE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("prioriteCE")
    @SwLength(max = 50)
    private String prioriteCE;

    @NxProp(
        xpath = ConseilEtatConstants.CONSEIL_ETAT_SECTION_CE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("sectionCE")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String sectionCE;

    @NxProp(
        xpath = ConseilEtatConstants.CONSEIL_ETAT_RAPPORTEUR_CE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("rapporteur")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String rapporteur;

    @NxProp(
        xpath = ConseilEtatConstants.CONSEIL_ETAT_DATE_TRANSMISSION_SECTION_CE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("dateTransmissionSectionCE")
    private Calendar dateTransmissionSectionCE;

    @NxProp(
        xpath = ConseilEtatConstants.CONSEIL_ETAT_DATE_SECTION_CE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("datePrevisionnelleSectionCe")
    private Calendar datePrevisionnelleSectionCe;

    @NxProp(
        xpath = ConseilEtatConstants.CONSEIL_ETAT_DATE_AG_CE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("datePrevisionnelleAgCe")
    private Calendar datePrevisionnelleAgCe;

    @NxProp(
        xpath = ConseilEtatConstants.CONSEIL_ETAT_NUMERO_ISA_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("numeroISA")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String numeroISA;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_POUR_FOURNITURE_EPREUVE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("dateFournitureEpreuve")
    private Calendar dateFournitureEpreuve;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_VECTEUR_PUBLICATION_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("vecteurPublication[]")
    private ArrayList<String> vecteurPublication;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_PUBLICATION_INTEGRALE_OU_EXTRAIT_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("integraleOuExtraitPublication")
    private String integraleOuExtraitPublication;

    @FormParam("publicationsConjointes")
    private String publicationsConjointes;

    @NxProp(
        xpath = RetourDilaConstants.RETOUR_DILA_SCHEMA_PREFIX +
        ":" +
        RetourDilaConstants.RETOUR_DILA_MODE_PARUTION_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("modeParution")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String modeParution;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_DELAI_PUBLICATION_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("delaiPublication")
    @SwLength(max = 2)
    private String delaiPublication;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_DATE_PRECISEE_PUBLICATION_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("datePreciseePublication")
    private Calendar datePreciseePublication;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_ZONE_COM_SGG_DILA_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("zoneCommentaireSggDila")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String zoneCommentaireSggDila;

    @FormParam("transpositionDirectives[]")
    private ArrayList<TranspositionApplicationDetailDTO> transpositionDirectives = new ArrayList<>();

    @FormParam("applicationLoiOrdonnances[]")
    private ArrayList<TranspositionApplicationDetailDTO> applicationLoiOrdonnances = new ArrayList<>();

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_ACTE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("baseLegale")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String baseLegale;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_NATURE_TEXTE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("natureTexte")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String natureTexte;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_NUMERO_TEXTE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("numeroTexte")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String numeroTexte;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_DATE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("dateBaseLegale")
    private Calendar dateBaseLegale;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_TEXTE_ENTREPRISE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("isTexteRubriqueEntreprise")
    private boolean isTexteRubriqueEntreprise;

    @FormParam("datesEffetRubriqueEntreprise[]")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private ArrayList<String> datesEffetRubriqueEntreprise;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_INDEXATION_RUBRIQUE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("rubriques[]")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private ArrayList<String> rubriques;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_INDEXATION_MOTS_CLES_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("motsCles[]")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private ArrayList<String> motsCles;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_INDEXATION_CHAMP_LIBRE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("champsLibres[]")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private ArrayList<String> champsLibres;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX + ":" + DossierSolonEpgConstants.PERIODICITE_RAPPORT,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("periodiciteRapport")
    private String periodiciteRapport;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_HABILITATION_REF_LOI_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("referenceLoiHabilitation")
    private String referenceLoiHabilitation;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_HABILITATION_NUMERO_ARTICLES_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("numeroArticleHabilitation")
    private String numeroArticleHabilitation;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_HABILITATION_COMMENTAIRE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("commentaireHabilitation")
    private String commentaireHabilitation;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_HABILITATION_NUMERO_ORDRE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        process = MapDoc2BeanIntegerStringProcess.class
    )
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("numeroOrdreHabilitation")
    private Integer numeroOrdreHabilitation;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_DECRET_NUMEROTE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    @FormParam("decretNumerote")
    private Boolean decretNumerote;

    public String getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }

    public String getTitreActe() {
        return titreActe;
    }

    public void setTitreActe(String titreActe) {
        this.titreActe = titreActe;
    }

    public String getCategorieActe() {
        return categorieActe;
    }

    public void setCategorieActe(String categorieActe) {
        this.categorieActe = categorieActe;
    }

    public Boolean getPublicationRapportPresentation() {
        return publicationRapportPresentation;
    }

    public void setPublicationRapportPresentation(Boolean publicationRapportPresentation) {
        this.publicationRapportPresentation = publicationRapportPresentation;
    }

    public Calendar getDatePublicationSouhaitee() {
        return datePublicationSouhaitee;
    }

    public void setDatePublicationSouhaitee(Calendar datePublicationSouhaitee) {
        this.datePublicationSouhaitee = datePublicationSouhaitee;
    }

    public Calendar getDateSignature() {
        return dateSignature;
    }

    public void setDateSignature(Calendar dateSignature) {
        this.dateSignature = dateSignature;
    }

    public String getNomResponsable() {
        return nomResponsable;
    }

    public void setNomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
    }

    public String getPrenomResponsable() {
        return prenomResponsable;
    }

    public void setPrenomResponsable(String prenomResponsable) {
        this.prenomResponsable = prenomResponsable;
    }

    public String getQualiteResponsable() {
        return qualiteResponsable;
    }

    public void setQualiteResponsable(String qualiteResponsable) {
        this.qualiteResponsable = qualiteResponsable;
    }

    public String getTelResponsable() {
        return telResponsable;
    }

    public void setTelResponsable(String telResponsable) {
        this.telResponsable = telResponsable;
    }

    public String getMelResponsable() {
        return melResponsable;
    }

    public void setMelResponsable(String melResponsable) {
        this.melResponsable = melResponsable;
    }

    public String getPrioriteCE() {
        return prioriteCE;
    }

    public void setPrioriteCE(String prioriteCE) {
        this.prioriteCE = prioriteCE;
    }

    public String getSectionCE() {
        return sectionCE;
    }

    public void setSectionCE(String sectionCE) {
        this.sectionCE = sectionCE;
    }

    public String getRapporteur() {
        return rapporteur;
    }

    public void setRapporteur(String rapporteur) {
        this.rapporteur = rapporteur;
    }

    public Calendar getDateTransmissionSectionCE() {
        return dateTransmissionSectionCE;
    }

    public void setDateTransmissionSectionCE(Calendar dateTransmissionSectionCE) {
        this.dateTransmissionSectionCE = dateTransmissionSectionCE;
    }

    public Calendar getDatePrevisionnelleSectionCe() {
        return datePrevisionnelleSectionCe;
    }

    public void setDatePrevisionnelleSectionCe(Calendar datePrevisionnelleSectionCe) {
        this.datePrevisionnelleSectionCe = datePrevisionnelleSectionCe;
    }

    public Calendar getDatePrevisionnelleAgCe() {
        return datePrevisionnelleAgCe;
    }

    public void setDatePrevisionnelleAgCe(Calendar datePrevisionnelleAgCe) {
        this.datePrevisionnelleAgCe = datePrevisionnelleAgCe;
    }

    public String getNumeroISA() {
        return numeroISA;
    }

    public void setNumeroISA(String numeroISA) {
        this.numeroISA = numeroISA;
    }

    public Calendar getDateFournitureEpreuve() {
        return dateFournitureEpreuve;
    }

    public void setDateFournitureEpreuve(Calendar dateFournitureEpreuve) {
        this.dateFournitureEpreuve = dateFournitureEpreuve;
    }

    public ArrayList<String> getVecteurPublication() {
        return vecteurPublication;
    }

    public void setVecteurPublication(ArrayList<String> vecteurPublication) {
        this.vecteurPublication = vecteurPublication;
    }

    public String getIntegraleOuExtraitPublication() {
        return integraleOuExtraitPublication;
    }

    public void setIntegraleOuExtraitPublication(String integraleOuExtraitPublication) {
        this.integraleOuExtraitPublication = integraleOuExtraitPublication;
    }

    public String getPublicationsConjointes() {
        return publicationsConjointes;
    }

    public void setPublicationsConjointes(String publicationsConjointes) {
        this.publicationsConjointes = publicationsConjointes;
    }

    public String getModeParution() {
        return modeParution;
    }

    public void setModeParution(String modeParution) {
        this.modeParution = modeParution;
    }

    public String getDelaiPublication() {
        return delaiPublication;
    }

    public void setDelaiPublication(String delaiPublication) {
        this.delaiPublication = delaiPublication;
    }

    public Calendar getDatePreciseePublication() {
        return datePreciseePublication;
    }

    public void setDatePreciseePublication(Calendar datePreciseePublication) {
        this.datePreciseePublication = datePreciseePublication;
    }

    public String getZoneCommentaireSggDila() {
        return zoneCommentaireSggDila;
    }

    public void setZoneCommentaireSggDila(String zoneCommentaireSggDila) {
        this.zoneCommentaireSggDila = zoneCommentaireSggDila;
    }

    public ArrayList<TranspositionApplicationDetailDTO> getTranspositionDirectives() {
        return transpositionDirectives;
    }

    public void setTranspositionDirectives(ArrayList<TranspositionApplicationDetailDTO> transpositionDirectives) {
        this.transpositionDirectives = transpositionDirectives;
    }

    public ArrayList<TranspositionApplicationDetailDTO> getApplicationLoiOrdonnances() {
        return applicationLoiOrdonnances;
    }

    public void setApplicationLoiOrdonnances(ArrayList<TranspositionApplicationDetailDTO> applicationLoiOrdonnances) {
        this.applicationLoiOrdonnances = applicationLoiOrdonnances;
    }

    public String getBaseLegale() {
        return baseLegale;
    }

    public void setBaseLegale(String baseLegale) {
        this.baseLegale = baseLegale;
    }

    public String getNatureTexte() {
        return natureTexte;
    }

    public void setNatureTexte(String natureTexte) {
        this.natureTexte = natureTexte;
    }

    public String getNumeroTexte() {
        return numeroTexte;
    }

    public void setNumeroTexte(String numeroTexte) {
        this.numeroTexte = numeroTexte;
    }

    public Calendar getDateBaseLegale() {
        return dateBaseLegale;
    }

    public void setDateBaseLegale(Calendar dateBaseLegale) {
        this.dateBaseLegale = dateBaseLegale;
    }

    public boolean getIsTexteRubriqueEntreprise() {
        return isTexteRubriqueEntreprise;
    }

    public void setIsTexteRubriqueEntreprise(boolean isTexteRubriqueEntreprise) {
        this.isTexteRubriqueEntreprise = isTexteRubriqueEntreprise;
    }

    public ArrayList<String> getDatesEffetRubriqueEntreprise() {
        return datesEffetRubriqueEntreprise;
    }

    public void setDatesEffetRubriqueEntreprise(ArrayList<String> datesEffetRubriqueEntreprise) {
        this.datesEffetRubriqueEntreprise = datesEffetRubriqueEntreprise;
    }

    public ArrayList<String> getRubriques() {
        return rubriques;
    }

    public void setRubriques(ArrayList<String> rubriques) {
        this.rubriques = rubriques;
    }

    public ArrayList<String> getMotsCles() {
        return motsCles;
    }

    public void setMotsCles(ArrayList<String> motsCles) {
        this.motsCles = motsCles;
    }

    public ArrayList<String> getChampsLibres() {
        return champsLibres;
    }

    public void setChampsLibres(ArrayList<String> champsLibres) {
        this.champsLibres = champsLibres;
    }

    public String getPeriodiciteRapport() {
        return periodiciteRapport;
    }

    public void setPeriodiciteRapport(String periodiciteRapport) {
        this.periodiciteRapport = periodiciteRapport;
    }

    public String getReferenceLoiHabilitation() {
        return referenceLoiHabilitation;
    }

    public void setReferenceLoiHabilitation(String referenceLoiHabilitation) {
        this.referenceLoiHabilitation = referenceLoiHabilitation;
    }

    public String getNumeroArticleHabilitation() {
        return numeroArticleHabilitation;
    }

    public void setNumeroArticleHabilitation(String numeroArticleHabilitation) {
        this.numeroArticleHabilitation = numeroArticleHabilitation;
    }

    public String getCommentaireHabilitation() {
        return commentaireHabilitation;
    }

    public void setCommentaireHabilitation(String commentaireHabilitation) {
        this.commentaireHabilitation = commentaireHabilitation;
    }

    public Integer getNumeroOrdreHabilitation() {
        return numeroOrdreHabilitation;
    }

    public void setNumeroOrdreHabilitation(Integer numeroOrdreHabilitation) {
        this.numeroOrdreHabilitation = numeroOrdreHabilitation;
    }

    public Boolean getDecretNumerote() {
        return decretNumerote;
    }

    public void setDecretNumerote(Boolean decretNumerote) {
        this.decretNumerote = decretNumerote;
    }
}
