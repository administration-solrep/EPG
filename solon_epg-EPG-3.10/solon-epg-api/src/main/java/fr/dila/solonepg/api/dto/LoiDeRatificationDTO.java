package fr.dila.solonepg.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.api.cases.LoiDeRatification;

public interface LoiDeRatificationDTO extends Map<String, Serializable> {

    LoiDeRatification remapField(LoiDeRatification loiDeRatification) throws ClientException;

    String getId();

    void setId(String id);

    String getTermeDepot();
    void setTermeDepot(String termeDepot);

    Date getDateLimiteDepot();
    void setDateLimiteDepot(Date dateLimiteDepot);

    Date getDateLimitePublication();
    void setDateLimitePublication(Date dateLimitePublication);
    
    String getNumeroNor();
    void setNumeroNor(String numeroNor);
    
    String getTitreActe();
    void setTitreActe(String titreActe);
    
    Date getDateSaisineCE();
    void setDateSaisineCE(Date dateSaisineCE);
    
    Date getDateExamenCE();
    void setDateExamenCE(Date dateExamenCE);
    
    String getSectionCE();
    void setSectionCE(String sectionCE);
    
    Date getDateExamenCM();
    void setDateExamenCM(Date dateExamenCM);
    
    Boolean getRenvoiDecret();
    void setRenvoiDecret(Boolean renvoiDecret);
    
    String getChambreDepot();
    void setChambreDepot(String chambreDepot);
    
    Date getDateDepot();
    void setDateDepot(Date dateDepot);
    
    String getNumeroDepot();
    void setNumeroDepot(String numeroDepot);
    
    String getTitreOfficiel();
    void setTitreOfficiel(String titreOfficiel);

    Date getDatePublication();
    void setDatePublication(Date datePublication);

    Boolean getTitreActeLocked();
    void setTitreActeLocked(Boolean titreActeLocked);

    Boolean getDateSaisineCELocked();
    void setDateSaisineCELocked(Boolean dateSaisineCELocked);

    Boolean getDateExamenCELocked();
    void setDateExamenCELocked(Boolean dateExamenCELocked);

    Boolean getSectionCELocked();
    void setSectionCELocked(Boolean sectionCELocked);

    Boolean getTitreOfficielLocked();
    void setTitreOfficielLocked(Boolean titreOfficielLocked);

    Boolean getDatePublicationLocked();
    void setDatePublicationLocked(Boolean datePublicationLocked);

    Boolean getChambreDepotLocked();
    void setChambreDepotLocked(Boolean chambreDepotLocked);

    Boolean getDateDepotLocked();
    void setDateDepotLocked(Boolean dateDepotLocked);

    Boolean getNumeroDepotLocked();
    void setNumeroDepotLocked(Boolean numeroDepotLocked);
    
    Boolean hasValidation();
    void setValidation(Boolean validation);

    String getLienJORFLegifrance();
    void setLienJORFLegifrance(String lienJORFLegifrance);
    
}
