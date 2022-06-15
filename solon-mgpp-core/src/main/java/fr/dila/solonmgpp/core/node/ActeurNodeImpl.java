package fr.dila.solonmgpp.core.node;

import fr.dila.solonmgpp.api.node.ActeurNode;
import fr.sword.xsd.solon.epp.Acteur;

public class ActeurNodeImpl implements ActeurNode {
    private String identifiant;
    private String identiteId;

    public void remapField(Acteur acteur) {
        this.setIdentifiant(acteur.getId());
    }

    public Acteur acteurToXsd() {
        Acteur acteur = new Acteur();
        acteur.setId(this.getIdentifiant());
        return acteur;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getIdentiteId() {
        return identiteId;
    }

    public void setIdentiteId(String identiteId) {
        this.identiteId = identiteId;
    }
}
