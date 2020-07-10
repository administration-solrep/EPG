package fr.dila.solonepg.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.api.cases.Habilitation;

public interface HabilitationDTO extends Map<String, Serializable> {
    
    String getId();
    void setId(String id);
    
    String getNumeroOrdre();
    void setNumeroOrdre(String numeroOrdre);

    String getMinisterePilote();
    void setMinisterePilote(String ministerePilote);

    Boolean getCodification();
    void setCodification(Boolean codification);
    
    String getArticle();
    void setArticle(String article);

    String getObjetRIM();
    void setObjetRIM(String objetRIM);

    String getTypeHabilitation();
    void setTypeHabilitation(String typeHabilitation);
    
    String getConvention();
    void setConvention(String convention);
    
    Date getDateTerme();
    void setDateTerme(Date dateTerme);
    
    String getConventionDepot();
    void setConventionDepot(String conventionDepot);
    
    Date getDatePrevisionnelleSaisineCE();
    void setDatePrevisionnelleSaisineCE(Date datePrevisionnelleSaisineCE);
    
    Date getDatePrevisionnelleCM();
    void setDatePrevisionnelleCM(Date datePrevisionnelleCM);
    
    String getObservation();
    void setObservation(String observation);
    
    String getLegislature();
    void setLegislature(String legislature);
    
    List<String> getOrdonnanceIds();
    void setOrdonnanceIds(List<String> ordonnanceIds);
    
    List<String> getOrdonnanceIdsInvalidated();
    void setOrdonnanceIdsInvalidated(List<String> decretIds);
    
    Habilitation remapField(Habilitation habilitation) throws ClientException;

    Boolean isValidate();
    void setValidate(Boolean validation);
    
    Boolean hasValidation();
    void setValidation(Boolean validation);
}
