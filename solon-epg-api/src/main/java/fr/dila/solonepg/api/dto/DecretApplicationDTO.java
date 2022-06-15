package fr.dila.solonepg.api.dto;

import fr.dila.solonepg.api.cases.Decret;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface DecretApplicationDTO extends Map<String, Serializable> {
    Decret remapField(Decret decret);

    void setId(String id);
    String getId();

    String getNumeroNor();
    void setNumeroNor(String numeroNor);

    String getTitreDecret();
    void setTitreDecret(String titreDecret);

    String getEtapeSolon();
    void setEtapeSolon(String etapeSolon);

    String getArticleOrdonnance();
    void setArticleOrdonnance(String articleOrdonnance);

    String getTitreOfficiel();
    void setTitreOfficiel(String titreOfficiel);

    String getLienJORFLegifrance();
    void setLienJORFLegifrance(String lienJORFLegifrance);

    Date getDatePublication();
    void setDatePublication(Date datePublication);

    Boolean getDatePublicationLocked();
    void setDatePublicationLocked(Boolean datePublicationLocked);

    Boolean getTitreDecretLocked();
    void setTitreDecretLocked(Boolean titreDecretLocked);

    Boolean getTitreOfficielLocked();
    void setTitreOfficielLocked(Boolean titreOfficielLocked);

    Boolean hasValidation();
    void setValidation(Boolean validation);

    String getReferenceDispositionRatification();
    void setReferenceDispositionRatification(String referenceDispositionRatification);

    Boolean isValidate();
    void setValidate(Boolean validation);

    /**
     * Validation utilisateur du rattachement décret/mesure
     */
    Boolean hasValidationLink();
    void setValidationLink(Boolean validationLink);
}
