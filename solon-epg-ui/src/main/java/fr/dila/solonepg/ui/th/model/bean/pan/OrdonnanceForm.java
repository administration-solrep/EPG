package fr.dila.solonepg.ui.th.model.bean.pan;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwLength;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean(keepdefaultValue = true)
public class OrdonnanceForm {
    @FormParam("numeroNor")
    private String numeroNor;

    @FormParam("numero")
    private String numero;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("titreOfficiel")
    private String titre;

    @SwId
    @FormParam("ministerePilote")
    private String ministerePilote;

    @FormParam("ministerePiloteLabel")
    private String ministerePiloteLabel;

    @FormParam("numeroInterne")
    private String numeroInterne;

    @FormParam("datePublication")
    private Calendar datePublication;

    @FormParam("observation")
    private String observation;

    @FormParam("champLibre")
    private String champLibre;

    @FormParam("renvoiDecret")
    private boolean renvoiDecret;

    @FormParam("ratifie")
    private boolean ratifie;

    @FormParam("fondementConstitutionnel")
    private boolean fondementConstitutionnel;

    public OrdonnanceForm() {}

    public String getNumeroNor() {
        return numeroNor;
    }

    public void setNumeroNor(String numeroNor) {
        this.numeroNor = numeroNor;
    }

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

    public String getNumeroInterne() {
        return numeroInterne;
    }

    public void setNumeroInterne(String numeroInterne) {
        this.numeroInterne = numeroInterne;
    }

    public Calendar getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Calendar datePublication) {
        this.datePublication = datePublication;
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

    public boolean isRenvoiDecret() {
        return renvoiDecret;
    }

    public void setRenvoiDecret(boolean renvoiDecret) {
        this.renvoiDecret = renvoiDecret;
    }

    public boolean isRatifie() {
        return ratifie;
    }

    public void setRatifie(boolean ratifie) {
        this.ratifie = ratifie;
    }

    public boolean isFondementConstitutionnel() {
        return fondementConstitutionnel;
    }

    public void setFondementConstitutionnel(boolean fondementConstitutionnel) {
        this.fondementConstitutionnel = fondementConstitutionnel;
    }
}
