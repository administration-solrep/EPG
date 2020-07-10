package fr.dila.solonepg.web.recherche;

import java.util.ArrayList;
import java.util.List;

/**
 * Noeud pour un element de recherche
 * 
 * @author asatre
 *
 */
public class EspaceRechercheTreeNode{

    
    public Boolean opened;
    
    private Boolean loaded;
    
	private final EspaceRechercheEnum type;
	
	private List<EspaceRechercheTreeNode> children;
	
	private EspaceRechercheTreeNode parent;

	public EspaceRechercheTreeNode(EspaceRechercheEnum type, EspaceRechercheTreeNode parent){
        this.children = new ArrayList<EspaceRechercheTreeNode>();
        this.type = type;
        this.parent = parent;
        opened = Boolean.TRUE;
        loaded=Boolean.TRUE;
    }
    
    public Boolean isOpened(){
        return opened;
    }
    
    public void setOpened(Boolean isOpened){
        opened = isOpened;
    }
    
    public Boolean isLoaded(){
        return loaded;
    }
    
    public void setLoaded(Boolean loaded){
        this.loaded = loaded;
    }

	public String getLabel() {
		return type.getLabel();
	}
	
	public String getId() {
		return type.getId();
	}

	public String getType() {
		return type.getType();
	}
	
	public String getLink() {
		if(parent != null){
			return parent.getEnumType().getLink() + getEnumType().getLink();
		}
		return type.getLink();
	}
	
	public String getImg() {
		return type.getImg();
	}
	
	public EspaceRechercheEnum getEnumType(){
		return type;
	}
	

	public List<EspaceRechercheTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<EspaceRechercheTreeNode> chidren) {
		this.children = chidren;
	}

	public void addChild(EspaceRechercheTreeNode treeNode) {
		children.add(treeNode);
	}

    public EspaceRechercheTreeNode getParent() {
		return parent;
	}

	public void setParent(EspaceRechercheTreeNode parent) {
		this.parent = parent;
	}

}
