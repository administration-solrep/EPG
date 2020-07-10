package fr.dila.solonmgpp.api.tree;

import java.io.Serializable;
import java.util.List;

import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.api.enumeration.CorbeilleTypeObjet;

/**
 * Interface d'un noeud de l'arbre des corbeilles.
 * 
 * @author asatre
 */
public interface CorbeilleNode extends Serializable {

	String getType();

	void setType(String type);

	String getName();

	void setName(String name);

	String getDescription();

	void setDescription(String description);

	List<CorbeilleNode> getCorbeilleNodeList();

	void setCorbeilleNodeList(List<CorbeilleNode> corbeilleNodeList);

	boolean isOpened();

	void setOpened(boolean opened);

	String getId();

	void setId(String idCorbeille);

	List<MessageDTO> getMessageDTO();

	void setMessageDTO(List<MessageDTO> messageDTO);

	String getRoutingTaskType();

	void setRoutingTaskType(String routingTaskType);

	String getTypeActe();

	void setTypeActe(String typeActe);

	CorbeilleTypeObjet getTypeObjet();

	void setTypeObjet(CorbeilleTypeObjet typeObjet);

	Long getCount();

	void setCount(Long countDossier);

	Boolean isAdoption();

	void setAdoption(Boolean adoption);

	boolean isTypeCorbeille();

}
