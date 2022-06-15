package fr.dila.solonmgpp.core.dto;

import fr.dila.solonmgpp.api.dto.CorbeilleDTO;
import fr.dila.solonmgpp.api.enumeration.CorbeilleTypeObjet;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation de {@link CorbeilleDTO}
 *
 * @author asatre
 *
 */
public class CorbeilleDTOImpl implements CorbeilleDTO, Serializable {
    private static final long serialVersionUID = -3803197948702130870L;

    private String idCorbeille;
    private String nom;
    private String description;
    private List<CorbeilleDTO> corbeille;
    private String type;
    private String routingTaskType;
    private String typeActe;
    private String ifMember;
    private String parent;
    private Boolean adoption;
    private CorbeilleTypeObjet typeObjet;

    public CorbeilleDTOImpl() {
        corbeille = new ArrayList<CorbeilleDTO>();
        idCorbeille = "";
    }

    @Override
    public String getIdCorbeille() {
        return idCorbeille;
    }

    @Override
    public void setIdCorbeille(String idCorbeille) {
        this.idCorbeille = idCorbeille;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<CorbeilleDTO> getCorbeille() {
        return corbeille;
    }

    @Override
    public void setCorbeille(List<CorbeilleDTO> corbeille) {
        this.corbeille = corbeille;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getRoutingTaskType() {
        return routingTaskType;
    }

    @Override
    public void setRoutingTaskType(String routingTaskType) {
        this.routingTaskType = routingTaskType;
    }

    @Override
    public String getTypeActe() {
        return typeActe;
    }

    @Override
    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }

    @Override
    public void setIfMember(String ifMember) {
        this.ifMember = ifMember;
    }

    @Override
    public String getIfMember() {
        return ifMember;
    }

    @Override
    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public String getParent() {
        return parent;
    }

    @Override
    public CorbeilleTypeObjet getTypeObjet() {
        return typeObjet;
    }

    @Override
    public void setTypeObjet(CorbeilleTypeObjet typeObjet) {
        this.typeObjet = typeObjet;
    }

    @Override
    public Boolean isAdoption() {
        return adoption;
    }

    @Override
    public void setAdoption(Boolean adoption) {
        this.adoption = adoption;
    }
}
