package fr.dila.solonmgpp.web.admin.tablereferenceepp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.richfaces.component.UITree;
import org.richfaces.event.NodeExpandedEvent;

import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.node.TableReferenceEppNode;
import fr.dila.solonmgpp.api.node.TableReferenceEppTreeNode;
import fr.dila.solonmgpp.api.service.TableReferenceService;
import fr.dila.solonmgpp.core.node.TableReferenceEppTreeNodeImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;

@Name("tableReferenceEppTree")
@Scope(ScopeType.SESSION)
public class TableReferenceEppTreeBean implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -382187017085695651L;


	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger LOGGER = STLogFactory.getLog(TableReferenceEppTreeBean.class);

  
  @In(create = true, required = false)
  protected CoreSession documentManager;
  
  @In(create = true, required = false)
  protected transient FacesMessages facesMessages;
  
  protected List<TableReferenceEppTreeNode> rootNodes;
  
  public String navigateToTableRefEPP() {
    reloadTree();
    return SolonMgppViewConstant.TABLE_REFERENCE_EPP;
  }
  
  public List<TableReferenceEppTreeNode> getTableReference() {
    if (rootNodes == null) {
        rootNodes = loadTree();
    }
    return rootNodes;
  }
  
  public void cleanTree() {
    rootNodes = null;
  }

  public void reloadTree() {
    rootNodes = loadTree();
  }

  public List<TableReferenceEppTreeNode> loadTree() {
    
    TableReferenceService tableReferenceService = SolonMgppServiceLocator.getTableReferenceService();

    List<TableReferenceEppTreeNode> rootNodes = new ArrayList<TableReferenceEppTreeNode>();
    try {
      List<TableReferenceEppNode> baseGroups = tableReferenceService.getGouvernementList(documentManager);
      TableReferenceEppTreeNode dataTreeNode;
      for (TableReferenceEppNode groupNode : baseGroups) {
        dataTreeNode = new TableReferenceEppTreeNodeImpl(groupNode);
        //charge au moins un enfants pour afficher le +
        if(groupNode.isHasChildren()!=null && Boolean.TRUE.equals(groupNode.isHasChildren())) {
          dataTreeNode.addChild(new TableReferenceEppTreeNodeImpl(null));
        }
        rootNodes.add(dataTreeNode);
      }
    } catch (ClientException e) {
        LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_GOUVERNEMENT_TEC, e);
        facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
    }

    return rootNodes;
  }
  
  public void changeExpandListener(NodeExpandedEvent event) {
    UIComponent component = event.getComponent();
    if (component instanceof UITree) {
        UITree treeComponent = (UITree)component;            
        Object value = treeComponent.getRowData();
        if (value instanceof TableReferenceEppTreeNode) {
          TableReferenceEppTreeNode dataTreeNode = (TableReferenceEppTreeNode)value;
            if (dataTreeNode.isOpened()) {
                dataTreeNode.setOpened(false);
            } else {
                dataTreeNode.setOpened(true);
                //reload les enfants afin d'afficher les éventuelles modifs ou verrous
                addSubGroups(dataTreeNode);
            }
        }
    }
}

  public void addSubGroups(TableReferenceEppTreeNode dataTreeNode) {
    List<TableReferenceEppNode> subGroups;

    // Efface les enfants existant
    dataTreeNode.getChildren().clear();
    
    TableReferenceEppNode group = dataTreeNode.getNode();

    if (group != null) {
      try {
        TableReferenceService tableReferenceService = SolonMgppServiceLocator.getTableReferenceService();
        subGroups = tableReferenceService.getChildrenList(dataTreeNode.getNode(), documentManager);
        if (subGroups != null && !subGroups.isEmpty()) {
          TableReferenceEppTreeNode childTreeNode;
            for (TableReferenceEppNode childGroup : subGroups) {
              
              childTreeNode = new TableReferenceEppTreeNodeImpl(childGroup);
             //charge au moins un enfants pour afficher le +
              if(childGroup.isHasChildren()!=null && Boolean.TRUE.equals(childGroup.isHasChildren())) {
                childTreeNode.addChild(new TableReferenceEppTreeNodeImpl(null));
              }
              dataTreeNode.addChild(childTreeNode);
            }
        }
        } catch (ClientException e) {
          LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_TABLE_REFERENCE_TEC, e);
          facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
      }
    }
  }
  
  public Boolean adviseNodeOpened(UITree treeComponent) {
      Object value = treeComponent.getRowData();
      if(value instanceof TableReferenceEppTreeNode) {
        TableReferenceEppTreeNode dataTreeNode = (TableReferenceEppTreeNode)value;
          return dataTreeNode.isOpened();
      }
      return null;
  }
}
