package fr.dila.solonepg.ui.bean;

import fr.dila.st.ui.bean.ColonneInfo;
import java.util.ArrayList;
import java.util.List;

public class ModeleCourrierDTO {
    private List<String> modeleCourriers = new ArrayList<>();
    private List<ColonneInfo> lstColonnes = new ArrayList<>();

    public ModeleCourrierDTO() {
        super();
        buildUserColonnes();
    }

    public List<String> getModeleCourriers() {
        return modeleCourriers;
    }

    public void setModeleCourriers(List<String> modeleCourriers) {
        this.modeleCourriers = modeleCourriers;
    }

    public List<ColonneInfo> getLstColonnes() {
        return lstColonnes;
    }

    public void setLstColonnes(List<ColonneInfo> lstColonnes) {
        this.lstColonnes = lstColonnes;
    }

    private void buildUserColonnes() {
        ColonneInfo colonne = new ColonneInfo("admin.modele.courrier.table.name", false, true, false, true);
        lstColonnes.add(colonne);
    }
}
