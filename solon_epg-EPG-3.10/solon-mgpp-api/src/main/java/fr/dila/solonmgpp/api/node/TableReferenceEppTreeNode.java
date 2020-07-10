package fr.dila.solonmgpp.api.node;

import java.util.List;

public interface TableReferenceEppTreeNode {
  
  public TableReferenceEppNode getNode();
  
  public String getId();
  
  public List<TableReferenceEppTreeNode> getChildren();
  
  public void setChildren(List<TableReferenceEppTreeNode> children);
  
  public void addChild(TableReferenceEppTreeNode node);
  
  public TableReferenceEppTreeNode getParent();
  
  public void setParent(TableReferenceEppTreeNode parent);
  
  public boolean isLoaded();
  
  public void setLoaded(boolean loaded);
  
  public boolean isOpened();
  
  public void setOpened(boolean opened);

  public String getTypeValue();

  public void setTypeValue(String typeValue);
  
  public String getLabel();
  
  public String getStyle();
}
