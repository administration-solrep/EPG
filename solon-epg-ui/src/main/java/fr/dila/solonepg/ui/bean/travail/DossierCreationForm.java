package fr.dila.solonepg.ui.bean.travail;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.FormParam;

@SwBean(keepdefaultValue = true)
public class DossierCreationForm {
    @FormParam("poste")
    private String poste;

    @SwRequired
    @FormParam("typeActe")
    private String typeActe;

    @FormParam("entite")
    private String entite;

    private Map<String, String> mapEntite = new HashMap<>();

    @FormParam("direction")
    private String direction;

    private Map<String, String> mapDirection = new HashMap<>();

    @FormParam("norPrm")
    private String norPrm;

    @FormParam("norCopie")
    private String norCopie;

    @FormParam("norRectificatif")
    private String norRectificatif;

    @FormParam("mesuresTransposition[]")
    private ArrayList<MesureTranspositionForm> mesuresTransposition = new ArrayList<>();

    @FormParam("mesuresApplication[]")
    private ArrayList<MesureApplicationForm> mesuresApplication = new ArrayList<>();

    @FormParam("ordonnance")
    private OrdonnanceForm ordonnance;

    public DossierCreationForm() {}

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }

    public String getEntite() {
        return entite;
    }

    public void setEntite(String entite) {
        this.entite = entite;
    }

    public Map<String, String> getMapEntite() {
        return mapEntite;
    }

    public void setMapEntite(Map<String, String> mapEntite) {
        this.mapEntite = mapEntite;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Map<String, String> getMapDirection() {
        return mapDirection;
    }

    public void setMapDirection(Map<String, String> mapDirection) {
        this.mapDirection = mapDirection;
    }

    public String getNorPrm() {
        return norPrm;
    }

    public void setNorPrm(String norPrm) {
        this.norPrm = norPrm;
    }

    public String getNorCopie() {
        return norCopie;
    }

    public void setNorCopie(String norCopie) {
        this.norCopie = norCopie;
    }

    public String getNorRectificatif() {
        return norRectificatif;
    }

    public void setNorRectificatif(String norRectificatif) {
        this.norRectificatif = norRectificatif;
    }

    public ArrayList<MesureTranspositionForm> getMesuresTransposition() {
        return mesuresTransposition;
    }

    public void setMesuresTransposition(ArrayList<MesureTranspositionForm> mesuresTransposition) {
        this.mesuresTransposition = mesuresTransposition;
    }

    public ArrayList<MesureApplicationForm> getMesuresApplication() {
        return mesuresApplication;
    }

    public void setMesuresApplication(ArrayList<MesureApplicationForm> mesuresApplication) {
        this.mesuresApplication = mesuresApplication;
    }

    public List<MesureApplicationForm> getMesuresApplicationLoi() {
        return mesuresApplication.stream().filter(MesureApplicationForm::isLoi).collect(Collectors.toList());
    }

    public List<MesureApplicationForm> getMesuresApplicationOrdonnance() {
        return mesuresApplication.stream().filter(MesureApplicationForm::isOrdonnance).collect(Collectors.toList());
    }

    public OrdonnanceForm getOrdonnance() {
        return ordonnance;
    }

    public void setOrdonnance(OrdonnanceForm ordonnance) {
        this.ordonnance = ordonnance;
    }
}
