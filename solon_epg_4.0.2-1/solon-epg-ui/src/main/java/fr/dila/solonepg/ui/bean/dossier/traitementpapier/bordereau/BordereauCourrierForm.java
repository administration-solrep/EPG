package fr.dila.solonepg.ui.bean.dossier.traitementpapier.bordereau;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.QueryParam;

@SwBean
public class BordereauCourrierForm {
    @SwRequired
    @SwNotEmpty
    @QueryParam("auteur")
    private String auteur;

    private Map<String, String> mapAuteur = new HashMap<>();

    @QueryParam("coAuteur")
    private ArrayList<String> coAuteur;

    private Map<String, String> mapCoAuteur = new HashMap<>();

    @SwRequired
    @SwNotEmpty
    @QueryParam("modeleCourrier")
    private String modeleCourrier;

    @SwRequired
    @SwNotEmpty
    @SwId
    @QueryParam("dossierId")
    private String dossierId;

    public BordereauCourrierForm() {
        this("", new ArrayList<>(), "", "");
    }

    public BordereauCourrierForm(String auteur, List<String> coAuteur, String modeleCourrier, String dossierId) {
        this.auteur = auteur;
        this.coAuteur = new ArrayList<>(coAuteur);
        this.modeleCourrier = modeleCourrier;
        this.dossierId = dossierId;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public Map<String, String> getMapAuteur() {
        return mapAuteur;
    }

    public void setMapAuteur(Map<String, String> mapAuteur) {
        this.mapAuteur = mapAuteur;
    }

    public ArrayList<String> getCoAuteur() {
        return coAuteur;
    }

    public void setCoAuteur(ArrayList<String> coAuteur) {
        this.coAuteur = coAuteur;
    }

    public Map<String, String> getMapCoAuteur() {
        return mapCoAuteur;
    }

    public void setMapCoAuteur(Map<String, String> mapCoAuteur) {
        this.mapCoAuteur = mapCoAuteur;
    }

    public String getModeleCourrier() {
        return modeleCourrier;
    }

    public void setModeleCourrier(String modeleCourrier) {
        this.modeleCourrier = modeleCourrier;
    }

    public String getDossierId() {
        return dossierId;
    }

    public void setDossierId(String dossierId) {
        this.dossierId = dossierId;
    }
}
