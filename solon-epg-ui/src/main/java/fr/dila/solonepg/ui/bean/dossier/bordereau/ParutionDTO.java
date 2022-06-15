package fr.dila.solonepg.ui.bean.dossier.bordereau;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.mapper.MapDoc2BeanComplexeTypeProcess;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ParutionDTO {

    public ParutionDTO() {
        super();
    }

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_POUR_FOURNITURE_EPREUVE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private Calendar dateFournitureEpreuve;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_VECTEUR_PUBLICATION_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private List<String> vecteurPublication;

    private List<String> vecteurPublicationLibelle;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_PUBLICATION_INTEGRALE_OU_EXTRAIT_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String integraleOuExtraitPublication;

    private String integraleOuExtraitPublicationLibelle;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_PUBLICATION_CONJOINTE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private List<String> publicationsConjointes;

    private Map<String, String> mapPublicationConjointes;

    @NxProp(
        xpath = RetourDilaConstants.RETOUR_DILA_SCHEMA_PREFIX +
        ":" +
        RetourDilaConstants.RETOUR_DILA_MODE_PARUTION_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String modeParution;

    private String modeParutionLibelle;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_DELAI_PUBLICATION_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String delaiPublication;

    private String delaiPublicationLibelle;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_DATE_PRECISEE_PUBLICATION_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private Calendar datePreciseePublication;

    private Boolean isDatePreciseeVisible = Boolean.FALSE;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_NUMERO_TEXTE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String numeroTexte;

    @NxProp(
        xpath = RetourDilaConstants.RETOUR_DILA_SCHEMA_PREFIX +
        ":" +
        RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_JORF_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String numeroTexteJO;

    @NxProp(
        xpath = RetourDilaConstants.RETOUR_DILA_SCHEMA_PREFIX +
        ":" +
        RetourDilaConstants.RETOUR_DILA_PAGE_PARUTION_JORF_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private Long pageJO;

    @NxProp(
        xpath = RetourDilaConstants.RETOUR_DILA_SCHEMA_PREFIX + ":" + RetourDilaConstants.RETOUR_DILA_TITRE_OFFICIEL,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String titreOfficiel;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_ZONE_COM_SGG_DILA_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String zoneCommentaireSggDila;

    @NxProp(
        xpath = RetourDilaConstants.RETOUR_DILA_SCHEMA_PREFIX +
        ":" +
        RetourDilaConstants.RETOUR_DILA_PARUTION_BO_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        process = MapDoc2BeanComplexeTypeProcess.class
    )
    private List<ParutionBoDTO> parutionBO;

    private Boolean isModifiableZoneComSggDila = Boolean.FALSE;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_DECRET_NUMEROTE_XPATH,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private Boolean isDecretNumerote = Boolean.FALSE;

    public Calendar getDateFournitureEpreuve() {
        return dateFournitureEpreuve;
    }

    public void setDateFournitureEpreuve(Calendar dateFournitureEpreuve) {
        this.dateFournitureEpreuve = dateFournitureEpreuve;
    }

    public List<String> getVecteurPublication() {
        return vecteurPublication;
    }

    public void setVecteurPublication(List<String> vecteurPublication) {
        this.vecteurPublication = vecteurPublication;
    }

    public List<String> getVecteurPublicationLibelle() {
        return vecteurPublicationLibelle;
    }

    public void setVecteurPublicationLibelle(List<String> vecteurPublicationLibelle) {
        this.vecteurPublicationLibelle = vecteurPublicationLibelle;
    }

    public String getIntegraleOuExtraitPublication() {
        return integraleOuExtraitPublication;
    }

    public void setIntegraleOuExtraitPublication(String integraleOuExtraitPublication) {
        this.integraleOuExtraitPublication = integraleOuExtraitPublication;
    }

    public String getIntegraleOuExtraitPublicationLibelle() {
        return integraleOuExtraitPublicationLibelle;
    }

    public void setIntegraleOuExtraitPublicationLibelle(String integraleOuExtraitPublicationLibelle) {
        this.integraleOuExtraitPublicationLibelle = integraleOuExtraitPublicationLibelle;
    }

    public List<String> getPublicationsConjointes() {
        return publicationsConjointes;
    }

    public void setPublicationsConjointes(List<String> publicationsConjointes) {
        this.publicationsConjointes = publicationsConjointes;
    }

    public Map<String, String> getMapPublicationConjointes() {
        return mapPublicationConjointes;
    }

    public void setMapPublicationConjointes(Map<String, String> mapPublicationConjointes) {
        this.mapPublicationConjointes = mapPublicationConjointes;
    }

    public String getModeParution() {
        return modeParution;
    }

    public void setModeParution(String modeParution) {
        this.modeParution = modeParution;
    }

    public String getModeParutionLibelle() {
        return modeParutionLibelle;
    }

    public void setModeParutionLibelle(String modeParutionLibelle) {
        this.modeParutionLibelle = modeParutionLibelle;
    }

    public String getDelaiPublication() {
        return delaiPublication;
    }

    public void setDelaiPublication(String delaiPublication) {
        this.delaiPublication = delaiPublication;
    }

    public String getDelaiPublicationLibelle() {
        return delaiPublicationLibelle;
    }

    public void setDelaiPublicationLibelle(String delaiPublicationLibelle) {
        this.delaiPublicationLibelle = delaiPublicationLibelle;
    }

    public Calendar getDatePreciseePublication() {
        return datePreciseePublication;
    }

    public void setDatePreciseePublication(Calendar datePreciseePublication) {
        this.datePreciseePublication = datePreciseePublication;
    }

    public Boolean getIsDatePreciseeVisible() {
        return isDatePreciseeVisible;
    }

    public void setIsDatePreciseeVisible(Boolean isDatePreciseeVisible) {
        this.isDatePreciseeVisible = isDatePreciseeVisible;
    }

    public String getNumeroTexte() {
        return numeroTexte;
    }

    public void setNumeroTexte(String numeroTexte) {
        this.numeroTexte = numeroTexte;
    }

    public String getNumeroTexteJO() {
        return numeroTexteJO;
    }

    public void setNumeroTexteJO(String numeroTexteJO) {
        this.numeroTexteJO = numeroTexteJO;
    }

    public Long getPageJO() {
        return pageJO;
    }

    public void setPageJO(Long pageJO) {
        this.pageJO = pageJO;
    }

    public String getTitreOfficiel() {
        return titreOfficiel;
    }

    public void setTitreOfficiel(String titreOfficiel) {
        this.titreOfficiel = titreOfficiel;
    }

    public String getZoneCommentaireSggDila() {
        return zoneCommentaireSggDila;
    }

    public void setZoneCommentaireSggDila(String zoneCommentaireSggDila) {
        this.zoneCommentaireSggDila = zoneCommentaireSggDila;
    }

    public Boolean getIsModifiableZoneComSggDila() {
        return isModifiableZoneComSggDila;
    }

    public void setIsModifiableZoneComSggDila(Boolean isModifiableZoneComSggDila) {
        this.isModifiableZoneComSggDila = isModifiableZoneComSggDila;
    }

    public List<ParutionBoDTO> getParutionBO() {
        return parutionBO;
    }

    public void setParutionBO(List<ParutionBoDTO> parutionBO) {
        this.parutionBO = parutionBO;
    }

    public Boolean getDecretNumerote() {
        return isDecretNumerote;
    }

    public void setDecretNumerote(Boolean decretNumerote) {
        isDecretNumerote = decretNumerote;
    }
}
