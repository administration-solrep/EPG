package fr.dila.solonepg.ui.bean.pan;

import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Noeud pour un element
 *
 * @author asatre
 *
 */
public class PanTreeNode implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8906321277415752039L;

    /**
     * clé identifiant
     */
    private String key;

    private boolean opened;

    private boolean loaded;

    private String label;

    private PanTreeNode parent;

    private List<PanTreeNode> children;

    private ActiviteNormativeEnum espaceActiviteNormativeEnum;

    private List<String> legislatures;

    public PanTreeNode(String label, String key, ActiviteNormativeEnum espaceActiviteNormativeEnum) {
        this.key = key;
        this.label = label;
        this.children = new ArrayList<>();
        this.espaceActiviteNormativeEnum = espaceActiviteNormativeEnum;
    }

    public String getKey() {
        return key;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean isOpened) {
        opened = isOpened;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public String getLabel() {
        return label;
    }

    public List<PanTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<PanTreeNode> chidren) {
        this.children = chidren;
    }

    public void setParent(PanTreeNode parent) {
        this.parent = parent;
    }

    public PanTreeNode getParent() {
        return parent;
    }

    public void addChild(PanTreeNode treeNode) {
        children.add(treeNode);
    }

    public void setEspaceActiviteNormativeEnum(ActiviteNormativeEnum espaceActiviteNormativeEnum) {
        this.espaceActiviteNormativeEnum = espaceActiviteNormativeEnum;
    }

    public ActiviteNormativeEnum getEspaceActiviteNormativeEnum() {
        return espaceActiviteNormativeEnum;
    }

    /**
     * Retourne le nom particulier de l'activité normative applicationLoi pour des
     * loi d'application transposition pour les directives de transposition
     * ordonnance pour les ordonnance ordonnance38C pour les habilitations traite
     * pour les traités et accord
     *
     * @return la valeur de l'attribut servant à distinguer les activités normatives
     */
    public String getName() {
        return espaceActiviteNormativeEnum.getAttributSchema();
    }

    public List<String> getLegislatures() {
        return legislatures;
    }

    public void setLegislatures(List<String> legislatures) {
        this.legislatures = legislatures;
    }
}
