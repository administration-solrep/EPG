package fr.dila.solonmgpp.core.node;

import fr.dila.solonmgpp.api.node.TableReferenceEppNode;
import fr.dila.solonmgpp.api.node.TableReferenceEppTreeNode;
import java.util.ArrayList;
import java.util.List;

public class TableReferenceEppTreeNodeImpl implements TableReferenceEppTreeNode {
    private List<TableReferenceEppTreeNode> children;
    private TableReferenceEppTreeNode parent;
    private final TableReferenceEppNode tableReferenceEppNode;

    private boolean loaded;

    private boolean opened;

    public TableReferenceEppTreeNodeImpl(TableReferenceEppNode tableReferenceEppNode) {
        children = new ArrayList<>();
        this.tableReferenceEppNode = tableReferenceEppNode;
    }

    public TableReferenceEppTreeNodeImpl(
        TableReferenceEppNode tableReferenceEppNode,
        TableReferenceEppTreeNode parentNode
    ) {
        children = new ArrayList<>();
        this.tableReferenceEppNode = tableReferenceEppNode;
        this.parent = parentNode;
    }

    public TableReferenceEppNode getNode() {
        return tableReferenceEppNode;
    }

    public String getId() {
        return tableReferenceEppNode.getIdentifiant();
    }

    public List<TableReferenceEppTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TableReferenceEppTreeNode> children) {
        this.children = children;
    }

    public void addChild(TableReferenceEppTreeNode node) {
        children.add(node);
    }

    public TableReferenceEppTreeNode getParent() {
        return parent;
    }

    public void setParent(TableReferenceEppTreeNode parent) {
        this.parent = parent;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public String getTypeValue() {
        return tableReferenceEppNode.getTypeValue();
    }

    public void setTypeValue(String typeValue) {}

    public String getLabel() {
        return tableReferenceEppNode.getLabel();
    }

    public String getStyle() {
        return tableReferenceEppNode.getStyle();
    }
}
