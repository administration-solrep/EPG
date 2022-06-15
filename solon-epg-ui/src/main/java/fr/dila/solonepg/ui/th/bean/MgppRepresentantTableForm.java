package fr.dila.solonepg.ui.th.bean;

import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.platform.actions.Action;

public class MgppRepresentantTableForm {
    private String nom;
    private String type;
    private String titre;
    private List<String> lstColonnes;
    private Map<String, List<String>> items;
    private List<Action> tableActions;
    private List<Action> lineActions;

    public MgppRepresentantTableForm(String nom, String type, String titre) {
        this.nom = nom;
        this.type = type;
        this.titre = titre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public List<String> getLstColonnes() {
        return lstColonnes;
    }

    public void setLstColonnes(List<String> lstColonnes) {
        this.lstColonnes = lstColonnes;
    }

    public Map<String, List<String>> getItems() {
        return items;
    }

    public void setItems(Map<String, List<String>> items) {
        this.items = items;
    }

    public List<Action> getTableActions() {
        return tableActions;
    }

    public void setTableActions(List<Action> tableActions) {
        this.tableActions = tableActions;
    }

    public List<Action> getLineActions() {
        return lineActions;
    }

    public void setLineActions(List<Action> lineActions) {
        this.lineActions = lineActions;
    }
}
