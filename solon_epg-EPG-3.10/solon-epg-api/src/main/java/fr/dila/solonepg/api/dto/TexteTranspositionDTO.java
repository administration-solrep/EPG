package fr.dila.solonepg.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteTransposition;
import fr.dila.solonepg.api.cases.TranspositionDirective;

/**
 * DTO pour {@link TranspositionDirective}
 * 
 * @author asatre
 * 
 */
public interface TexteTranspositionDTO extends Map<String, Serializable> {

    String getNature();

    String getTypeActe();

    String getNumeroNor();

    String getIntitule();

    String getMinisterePilote();

    String getEtapeSolon();

    Date getDatePublication();

    String getNumeroTextePublie();

    String getTitreTextePublie();

    void setNature(String nature);

    void setTypeActe(String typeActe);

    void setNumeroNor(String numeroNor);

    void setIntitule(String intitule);

    void setMinisterePilote(String ministerePilote);

    void setEtapeSolon(String etapeSolon);

    void setDatePublication(Date datePublication);

    void setNumeroTextePublie(String numeroTextePublie);

    void setTitreTextePublie(String titreTextePublie);

    String getId();

    void setId(String id);

    Boolean hasValidation();

    void setValidation(Boolean validation);

    Boolean getValidate();

    void setValidate(Boolean validation);

    /**
     * remap field from DTO to DocumentModel
     * 
     * @param texteTransposition
     * @return
     */
    TexteTransposition remapField(TexteTransposition texteTransposition);

    /**
     * remap field from dossier to DTO
     * 
     * @param dossier
     * @param session
     */
    void remapField(Dossier dossier, CoreSession session);

}
