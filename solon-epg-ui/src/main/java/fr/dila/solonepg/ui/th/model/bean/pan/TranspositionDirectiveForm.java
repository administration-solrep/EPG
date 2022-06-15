package fr.dila.solonepg.ui.th.model.bean.pan;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwLength;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean(keepdefaultValue = true)
public class TranspositionDirectiveForm {
    @FormParam("numero")
    private String numero;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("titre")
    private String titre;

    @SwId
    @FormParam("ministerePilote")
    private String ministerePilote;

    @FormParam("ministerePiloteLabel")
    private String ministerePiloteLabel;

    @FormParam("direction")
    private String direction;

    @FormParam("dateAdoption")
    private Calendar dateAdoption;

    @FormParam("dateEcheance")
    private Calendar dateEcheance;

    @FormParam("dateProchainTabAffichage")
    private Calendar dateProchainTabAffichage;

    @FormParam("dateTranspositionEffective")
    private Calendar dateTranspositionEffective;

    @FormParam("observation")
    private String observation;

    @FormParam("champLibre")
    private String champLibre;

    @FormParam("etat")
    private String etat;

    @FormParam("achevee")
    private boolean isAchevee;

    @FormParam("isDroitConforme")
    private boolean isDroitConforme;

    public TranspositionDirectiveForm() {}

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMinisterePilote() {
        return ministerePilote;
    }

    public void setMinisterePilote(String ministerePilote) {
        this.ministerePilote = ministerePilote;
    }

    public String getMinisterePiloteLabel() {
        return ministerePiloteLabel;
    }

    public void setMinisterePiloteLabel(String ministerePiloteLabel) {
        this.ministerePiloteLabel = ministerePiloteLabel;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Calendar getDateAdoption() {
        return dateAdoption;
    }

    public void setDateAdoption(Calendar dateAdoption) {
        this.dateAdoption = dateAdoption;
    }

    public Calendar getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Calendar dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public Calendar getDateProchainTabAffichage() {
        return dateProchainTabAffichage;
    }

    public void setDateProchainTabAffichage(Calendar dateProchainTabAffichage) {
        this.dateProchainTabAffichage = dateProchainTabAffichage;
    }

    public Calendar getDateTranspositionEffective() {
        return dateTranspositionEffective;
    }

    public void setDateTranspositionEffective(Calendar dateTranspositionEffective) {
        this.dateTranspositionEffective = dateTranspositionEffective;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getChampLibre() {
        return champLibre;
    }

    public void setChampLibre(String champLibre) {
        this.champLibre = champLibre;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public boolean isAchevee() {
        return isAchevee;
    }

    public void setAchevee(boolean isAchevee) {
        this.isAchevee = isAchevee;
    }

    public boolean isDroitConforme() {
        return isDroitConforme;
    }

    public void setDroitConforme(boolean isDroitConforme) {
        this.isDroitConforme = isDroitConforme;
    }
}
