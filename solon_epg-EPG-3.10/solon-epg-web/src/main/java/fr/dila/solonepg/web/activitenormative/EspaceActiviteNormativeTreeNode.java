package fr.dila.solonepg.web.activitenormative;

import java.util.ArrayList;
import java.util.List;

import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;

/**
 * Noeud pour un element
 * 
 * @author asatre
 *
 */
public class EspaceActiviteNormativeTreeNode{

    
	/**
     * clé identifiant
     */
    private final String key;
    
    public Boolean opened;
    
    private Boolean loaded;
    
	private final String label;

	private EspaceActiviteNormativeTreeNode parent;
	
	private List<EspaceActiviteNormativeTreeNode> children;
	
	private ActiviteNormativeEnum espaceActiviteNormativeEnum;
	
	private List<String> legislatures;

    public EspaceActiviteNormativeTreeNode(String label, String key, ActiviteNormativeEnum espaceActiviteNormativeEnum){
        this.key = key;
        this.label = label;
        this.children = new ArrayList<EspaceActiviteNormativeTreeNode>();
        opened = Boolean.FALSE;
        loaded=Boolean.FALSE;
        this.espaceActiviteNormativeEnum = espaceActiviteNormativeEnum;
    }
    
    public String getKey(){
        return key;
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
		return label;
	}

	public List<EspaceActiviteNormativeTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<EspaceActiviteNormativeTreeNode> chidren) {
		this.children = chidren;
	}


	public void setParent(EspaceActiviteNormativeTreeNode parent) {
		this.parent = parent;
	}

	public EspaceActiviteNormativeTreeNode getParent() {
		return parent;
	}

	public void addChild(EspaceActiviteNormativeTreeNode treeNode) {
		children.add(treeNode);
	}

    public void setEspaceActiviteNormativeEnum(ActiviteNormativeEnum espaceActiviteNormativeEnum) {
        this.espaceActiviteNormativeEnum = espaceActiviteNormativeEnum;
    }

    public ActiviteNormativeEnum getEspaceActiviteNormativeEnum() {
        return espaceActiviteNormativeEnum;
    }
    
	/**
	 * Retourne le nom particulier de l'activité normative
	 * applicationLoi pour des loi d'application
	 * transposition pour les directives de transposition
	 * ordonnance pour les ordonnance
	 * ordonnance38C pour les habilitations
	 * traite pour les traités et accord
	 * 
	 * @return la valeur de l'attribut servant à distinguer les activités normatives
	 */
    public String getName(){
    	return espaceActiviteNormativeEnum.getAttributSchema();
    }

	public List<String> getLegislatures() {
		return legislatures;
	}

	public void setLegislatures(List<String> legislatures) {
		this.legislatures = legislatures;
	}

}
