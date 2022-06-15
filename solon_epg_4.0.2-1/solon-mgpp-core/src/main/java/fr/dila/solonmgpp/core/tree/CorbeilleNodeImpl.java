package fr.dila.solonmgpp.core.tree;

import fr.dila.solonmgpp.api.constant.SolonMgppCorbeilleConstant;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.api.enumeration.CorbeilleTypeObjet;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import java.util.ArrayList;
import java.util.List;

/**
 * Noeud de l'arbre des corbeilles.
 *
 * @author asatre
 */
public class CorbeilleNodeImpl implements CorbeilleNode {
    private static final long serialVersionUID = 2303522743918325057L;

    private String type;

    private String name;

    private String description;

    private String id;

    private String routingTaskType;

    private boolean opened;

    private List<CorbeilleNode> corbeilleNodeList;

    private List<MessageDTO> messageDTO;

    private String typeActe;

    private CorbeilleTypeObjet typeObjet;

    private Long count;

    private Boolean adoption;

    public CorbeilleNodeImpl() {
        corbeilleNodeList = new ArrayList<>();
        messageDTO = new ArrayList<>();
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
    public List<CorbeilleNode> getCorbeilleNodeList() {
        return corbeilleNodeList;
    }

    @Override
    public void setCorbeilleNodeList(List<CorbeilleNode> corbeilleNodeList) {
        this.corbeilleNodeList = corbeilleNodeList;
    }

    @Override
    public boolean isOpened() {
        return opened;
    }

    @Override
    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<MessageDTO> getMessageDTO() {
        return messageDTO;
    }

    @Override
    public void setMessageDTO(List<MessageDTO> messageDTO) {
        this.messageDTO = messageDTO;
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
    public CorbeilleTypeObjet getTypeObjet() {
        return typeObjet;
    }

    @Override
    public void setTypeObjet(CorbeilleTypeObjet typeObjet) {
        this.typeObjet = typeObjet;
    }

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public Boolean isAdoption() {
        return adoption != null && adoption;
    }

    public void setAdoption(Boolean adoption) {
        this.adoption = adoption;
    }

    /**
     * Retourne vrai si le noeud de l'organigramme est une corbeille.
     *
     * @return Condition
     */
    @Override
    public boolean isTypeCorbeille() {
        return SolonMgppCorbeilleConstant.CORBEILLE_NODE.equals(type);
    }
}
