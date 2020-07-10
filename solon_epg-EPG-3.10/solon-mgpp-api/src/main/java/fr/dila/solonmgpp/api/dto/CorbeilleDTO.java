package fr.dila.solonmgpp.api.dto;

import java.util.List;

import fr.dila.solonmgpp.api.enumeration.CorbeilleTypeObjet;
import fr.sword.xsd.solon.epp.Corbeille;

/**
 * DTO de representation d'une corbeille EPP
 * 
 * @see Corbeille
 * @author asatre
 * 
 */
public interface CorbeilleDTO {

    String getIdCorbeille();

    void setIdCorbeille(String idCorbeille);

    String getNom();

    void setNom(String nom);

    String getDescription();

    void setDescription(String description);

    List<CorbeilleDTO> getCorbeille();

    void setCorbeille(List<CorbeilleDTO> corbeille);

    String getType();

    void setType(String type);

    String getRoutingTaskType();

    void setRoutingTaskType(String routingTaskType);

    String getTypeActe();

    void setTypeActe(String typeActe);

    String getIfMember();

    void setIfMember(String ifMember);
    
    String getParent();
    
    void setParent(String parent);

    CorbeilleTypeObjet getTypeObjet();

    void setTypeObjet(CorbeilleTypeObjet typeObjet);

    Boolean isAdoption();

    void setAdoption(Boolean adoption);
}
