package fr.dila.solonepg.api.dto;

import fr.dila.solonepg.api.cases.Decret;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface DecretDTO extends Map<String, Serializable> {
    Decret remapField(Decret decret);

    void setId(String id);
    String getId();

    String getNumeroNor();
    void setNumeroNor(String numeroNor);

    String getTitreOfficiel();
    void setTitreOfficiel(String titreOfficiel);

    String getTypeActe();
    void setTypeActe(String typeActe);

    String getTypeActeLabel();
    void setTypeActeLabel(String typeActe);

    Date getDateSaisineCE();
    void setDateSaisineCE(Date dateSaisineCE);

    String getSectionCE();
    void setSectionCE(String sectionCE);

    Date getDateSectionCE();
    void setDateSortieCE(Date dateSortieCE);

    String getRapporteurCE();
    void setRapporteurCE(String rapporteurCE);

    String getReferenceAvisCE();
    void setReferenceAvisCE(String referenceAvisCE);

    String getNumerosTextes();
    void setNumerosTextes(String numerosTextes);

    Date getDateSignature();
    void setDateSignature(Date dateSignature);

    Date getDatePublication();
    void setDatePublication(Date datePublication);

    String getDelaiPublication();
    void setDelaiPublication(String delaiPublication);

    String getNumeroJOPublication();
    void setNumeroJOPublication(String numeroJOPublication);

    Long getNumeroPage();
    void setNumeroPage(Long numeroPage);

    String getObservation();
    void setObservation(String observation);

    String getLienJORFLegifrance();
    void setLienJORFLegifrance(String lienJORFLegifrance);

    Date getDateExamenCE();
    void setDateSectionCE(Date dateExamenCE);

    Boolean getDateSectionCELocked();
    void setDateSectionCELocked(Boolean dateSectionCeLocked);

    Boolean getDatePublicationLocked();
    void setDatePublicationLocked(Boolean datePublicationLocked);

    Boolean getDateSaisineCELocked();
    void setDateSaisineCELocked(Boolean dateSaisineCELocked);

    Boolean getDateSortieCELocked();
    void setDateSortieCELocked(Boolean dateSortieCELocked);

    Boolean getNumeroJOPublicationLocked();
    void setNumeroJOPublicationLocked(Boolean numeroJOPublication);

    Boolean getNumeroPageLocked();
    void setNumeroPageLocked(Boolean numeroPageLocked);

    Boolean getRapporteurCELocked();
    void setRapporteurCELocked(Boolean rapporteurCeLocked);

    Boolean getSectionCeLocked();
    void setSectionCeLocked(Boolean sectionCeLocked);

    Boolean getTitreOfficielLocked();
    void setTitreOfficielLocked(Boolean titreDecretLocked);

    Boolean getTypeActeLocked();
    void setTypeActeLocked(Boolean typeActeLocked);

    /**
     * Validation statistique
     * @return
     */
    Boolean hasValidation();
    void setValidation(Boolean validation);

    Boolean getValidate();
    void setValidate(Boolean validation);

    Decret remapFieldReprise(Decret decret);

    /**
     * Validation utilisateur du rattachement décret/mesure
     */
    Boolean hasValidationLink();
    void setValidationLink(Boolean validationLink);
}
