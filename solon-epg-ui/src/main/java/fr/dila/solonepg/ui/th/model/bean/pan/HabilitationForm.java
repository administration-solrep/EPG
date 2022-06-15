package fr.dila.solonepg.ui.th.model.bean.pan;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwLength;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class HabilitationForm {
    @FormParam("numeroOrdre")
    private String numeroOrdre;

    @FormParam("article")
    private String article;

    @FormParam("objet")
    private String objet;

    @FormParam("typeHabilitation")
    private String typeHabilitation;

    @SwId
    @FormParam("ministerePilote")
    private String ministerePilote;

    @FormParam("termeHabilitation")
    private String termeHabilitation;

    @FormParam("dateTerme")
    private Calendar dateTerme;

    @FormParam("depotRatification")
    private String depotRatification;

    @FormParam("datePrevisionnelleSaisineCE")
    private Calendar datePreviSaisineCE;

    @FormParam("datePrevisionnelleCM")
    private Calendar datePreviCM;

    @FormParam("observation")
    private String observations;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("legislature")
    private String legislature;

    @FormParam("codification")
    private Boolean codification;

    public HabilitationForm() {}

    public String getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setNumeroOrdre(String numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String object) {
        this.objet = object;
    }

    public String getTypeHabilitation() {
        return typeHabilitation;
    }

    public void setTypeHabilitation(String typeHabilitation) {
        this.typeHabilitation = typeHabilitation;
    }

    public String getMinisterePilote() {
        return ministerePilote;
    }

    public void setMinisterePilote(String ministerePilote) {
        this.ministerePilote = ministerePilote;
    }

    public String getTermeHabilitation() {
        return termeHabilitation;
    }

    public void setTermeHabilitation(String termeHabilitation) {
        this.termeHabilitation = termeHabilitation;
    }

    public Calendar getDateTerme() {
        return dateTerme;
    }

    public void setDateTerme(Calendar dateTerme) {
        this.dateTerme = dateTerme;
    }

    public String getDepotRatification() {
        return depotRatification;
    }

    public void setDepotRatification(String depotRatification) {
        this.depotRatification = depotRatification;
    }

    public Calendar getDatePreviSaisineCE() {
        return datePreviSaisineCE;
    }

    public void setDatePreviSaisineCE(Calendar dateSaisineCE) {
        this.datePreviSaisineCE = dateSaisineCE;
    }

    public Calendar getDatePreviCM() {
        return datePreviCM;
    }

    public void setDatePreviCM(Calendar datePreviCM) {
        this.datePreviCM = datePreviCM;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getLegislature() {
        return legislature;
    }

    public void setLegislature(String legislature) {
        this.legislature = legislature;
    }

    public Boolean getCodification() {
        return codification;
    }

    public void setCodification(Boolean codification) {
        this.codification = codification;
    }
}
