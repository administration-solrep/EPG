package fr.dila.solonepg.api.fonddossier;

import fr.dila.solonepg.api.treenode.EpgTreeNode;
import java.io.Serializable;

/**
 * Représentation d'un noeud de l'arboresence d'un modèle  du fond de dossier
 *
 * @author Antoine Rolin
 *
 */
public class FondDeDossierModelNode extends EpgTreeNode implements Serializable {
    private static final long serialVersionUID = -1193960175512473199L;

    /**
     * type du noeud (racine ou répertoire)
     */
    private FondDeDossierModelType type;

    /**
     * Constructeur.
     */
    public FondDeDossierModelNode() {}

    /**
     * Constructeur.
     */
    public FondDeDossierModelNode(FondDeDossierModelType type, String ident, String name) {
        super(ident, name);
        this.type = type;
    }

    public String getTypeValue() {
        return type.getValue();
    }

    public FondDeDossierModelType getType() {
        return type;
    }

    public void setType(FondDeDossierModelType type) {
        this.type = type;
    }
}
