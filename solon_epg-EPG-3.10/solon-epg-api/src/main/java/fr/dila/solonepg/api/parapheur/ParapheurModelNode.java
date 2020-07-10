package fr.dila.solonepg.api.parapheur;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import fr.dila.solonepg.api.treenode.EpgTreeNode;

/**
 * Représentation d'un noeud de l'arboresence d'un modèle  du fond de dossier
 * 
 * @author Antoine Rolin
 * 
 */
public class ParapheurModelNode extends  EpgTreeNode implements Serializable {

    private static final long serialVersionUID = -1193960175512473199L;

    /**
     * type du noeud (racine ou répertoire)
     */
    private ParapheurModelType type;
    
    /**
     * Propriétés spécifiques lié au noeud du parapheur
     */

    /**
     * propriété signalant si le noeud est  "non vide" cad qu'il devra obligatoire rmeent contenir un document pour que l'on puisse passer le dossier à l'état initié.
     */
    private Boolean estNonVide;

    /**
     * Nombre maximals de documents pouvant être contenu par le noeud.
     */
    private Long nbDocAccepteMax;

    /**
     * listes  des feuilles de style contenu par le noeud.
     */
    private List<File> feuilleStyleFiles;

    /**
     * listes des formats autorisés du noeud.
     */
    private List<String> formatAutorise;
    
    /**
     * Constructeur.
     */
    public ParapheurModelNode() {
    }

    /**
     * Constructeur.
     * 
     * @param type
     *            type
     * @param id
     *            id
     * @param name
     *            name
     */
    public ParapheurModelNode(ParapheurModelType type, String id, String name) {
        super(id,name);
        this.type = type;
    }

    public String getTypeValue() {
        return type.getValue();
    }

    public ParapheurModelType getType() {
        return type;
    }

    public void setType(ParapheurModelType type) {
        this.type = type;
    }

    public Boolean getEstNonVide() {
        return estNonVide;
    }

    public void setEstNonVide(Boolean estNonVide) {
        this.estNonVide = estNonVide;
    }

    public Long getNbDocAccepteMax() {
        return nbDocAccepteMax;
    }

    public void setNbDocAccepteMax(Long nbDocAccepteMax) {
        this.nbDocAccepteMax = nbDocAccepteMax;
    }


    public List<File> getFeuilleStyleFiles() {
        return feuilleStyleFiles;
    }

    public void setFeuilleStyleFiles(List<File> feuilleStyleFiles) {
        this.feuilleStyleFiles = feuilleStyleFiles;
    }

    public List<String> getFormatAutorise() {
        return formatAutorise;
    }

    public void setFormatAutorise(List<String> formatAutorise) {
        this.formatAutorise = formatAutorise;
    }
}
