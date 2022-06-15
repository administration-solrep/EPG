package fr.dila.solonepg.api.treenode;

import java.io.Serializable;

/**
 * Représentation d'un noeud d'une arborescence de document dans solon epg.
 *
 * @author ARN
 */
public class EpgTreeNode implements Serializable {
    private static final long serialVersionUID = 2769937640277210505L;

    /**
     * nom du noeud.
     */
    private String name;

    /**
     * id du noeud.
     */
    private String id;

    /**
     * type de document du noeud.
     */
    private String documentType;

    /**
     * createur du document du noeud (login).
     */
    private String createur;

    /**
     * profondeur du noeud.
     */
    private int depth;

    /**
     * Constructeur.
     */
    public EpgTreeNode() {}

    /**
     * Constructeur.
     */
    public EpgTreeNode(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Récupère le nom du noeud.
     */
    public String getName() {
        return name;
    }

    /**
     * définit le nom du noeud.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Récupère l'id du noeud.
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * définit l'id du noeud.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Récupère le type de document du noeud.
     *
     * @return
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * définit le type de document du noeud.
     *
     * @param documentType
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * Récupère la profondeur du noeud.
     *
     * @return int
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Définit la profondeur du noeud.
     *
     * @return int
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getCreateur() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }
}
