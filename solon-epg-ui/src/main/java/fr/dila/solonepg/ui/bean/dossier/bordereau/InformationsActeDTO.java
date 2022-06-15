package fr.dila.solonepg.ui.bean.dossier.bordereau;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.st.ui.annot.NxProp;
import java.util.Calendar;

public class InformationsActeDTO {

    public InformationsActeDTO() {
        super();
    }

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX + ":" + DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String statut;

    private String statutLibelle;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String typeActe;

    private String typeActeLibelle;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String nor;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String titreActe;

    private String titreActeLibelle;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_CATEGORIE_ACTE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String categorieActe;

    private String categorieActeLibelle;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_PUBLICATION_RAPPORT_PRESENTATION_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private Boolean publicationRapportPresentation;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_DATE_PUBLICATION_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private Calendar datePublicationSouhaitee;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_DATE_SIGNATURE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private Calendar dateSignature;

    @NxProp(
        xpath = RetourDilaConstants.RETOUR_DILA_SCHEMA_PREFIX +
        ":" +
        RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_JORF_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private Calendar datePublication;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_STATUT_ARCHIVAGE,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String statutArchivage;

    private String statutArchivageLibelle;

    private String idBordereauVersement;

    private Boolean isEditableTypeActe = Boolean.FALSE;
    private Boolean isVisibleCategorieActe = Boolean.FALSE;
    private Boolean isVisiblePublicationRapport = Boolean.FALSE;
    private Boolean isModifiableDateSignature = Boolean.FALSE;
    private Boolean isVisibleStatutArchivage = Boolean.FALSE;

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getStatutLibelle() {
        return statutLibelle;
    }

    public void setStatutLibelle(String statutLibelle) {
        this.statutLibelle = statutLibelle;
    }

    public String getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }

    public String getTypeActeLibelle() {
        return typeActeLibelle;
    }

    public void setTypeActeLibelle(String typeActeLibelle) {
        this.typeActeLibelle = typeActeLibelle;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public String getTitreActe() {
        return titreActe;
    }

    public void setTitreActe(String titreActe) {
        this.titreActe = titreActe;
    }

    public String getTitreActeLibelle() {
        return titreActeLibelle;
    }

    public void setTitreActeLibelle(String titreActeLibelle) {
        this.titreActeLibelle = titreActeLibelle;
    }

    public String getCategorieActe() {
        return categorieActe;
    }

    public void setCategorieActe(String categorieActe) {
        this.categorieActe = categorieActe;
    }

    public String getCategorieActeLibelle() {
        return categorieActeLibelle;
    }

    public void setCategorieActeLibelle(String categorieActeLibelle) {
        this.categorieActeLibelle = categorieActeLibelle;
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

    public Calendar getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Calendar datePublication) {
        this.datePublication = datePublication;
    }

    public String getStatutArchivage() {
        return statutArchivage;
    }

    public void setStatutArchivage(String statutArchivage) {
        this.statutArchivage = statutArchivage;
    }

    public String getStatutArchivageLibelle() {
        return statutArchivageLibelle;
    }

    public void setStatutArchivageLibelle(String statutArchivageLibelle) {
        this.statutArchivageLibelle = statutArchivageLibelle;
    }

    public String getIdBordereauVersement() {
        return idBordereauVersement;
    }

    public void setIdBordereauVersement(String idBordereauVersement) {
        this.idBordereauVersement = idBordereauVersement;
    }

    public Boolean getIsEditableTypeActe() {
        return isEditableTypeActe;
    }

    public void setIsEditableTypeActe(Boolean isEditableTypeActe) {
        this.isEditableTypeActe = isEditableTypeActe;
    }

    public Boolean getIsVisibleCategorieActe() {
        return isVisibleCategorieActe;
    }

    public void setIsVisibleCategorieActe(Boolean isVisibleCategorieActe) {
        this.isVisibleCategorieActe = isVisibleCategorieActe;
    }

    public Boolean getIsVisiblePublicationRapport() {
        return isVisiblePublicationRapport;
    }

    public void setIsVisiblePublicationRapport(Boolean isVisiblePublicationRapport) {
        this.isVisiblePublicationRapport = isVisiblePublicationRapport;
    }

    public Boolean getIsModifiableDateSignature() {
        return isModifiableDateSignature;
    }

    public void setIsModifiableDateSignature(Boolean isModifiableDateSignature) {
        this.isModifiableDateSignature = isModifiableDateSignature;
    }

    public Boolean getIsVisibleStatutArchivage() {
        return isVisibleStatutArchivage;
    }

    public void setIsVisibleStatutArchivage(Boolean isVisibleStatutArchivage) {
        this.isVisibleStatutArchivage = isVisibleStatutArchivage;
    }
}
