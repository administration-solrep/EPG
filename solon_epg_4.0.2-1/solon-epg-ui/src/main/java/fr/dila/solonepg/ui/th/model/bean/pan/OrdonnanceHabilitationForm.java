package fr.dila.solonepg.ui.th.model.bean.pan;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwLength;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class OrdonnanceHabilitationForm {
    @FormParam("numeroNor")
    private String numeroNor;

    @FormParam("objet")
    private String objet;

    @FormParam("ministerePilote")
    private String ministerePilote;

    @FormParam("legislature")
    private String legislature;

    @FormParam("dateSaisineCE")
    private Calendar dateSaisineCE;

    @FormParam("datePassageCM")
    private Calendar datePassageCM;

    @FormParam("datePublication")
    private Calendar datePublication;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("titreOfficiel")
    private String titreOfficiel;

    @FormParam("numero")
    private String numero;

    @FormParam("depotRatification")
    private String depotRatification;

    @FormParam("dateLimiteDepot")
    private Calendar dateLimiteDepot;

    public OrdonnanceHabilitationForm() {}

    public String getNumeroNor() {
        return numeroNor;
    }

    public void setNumeroNor(String numeroNor) {
        this.numeroNor = numeroNor;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getMinisterePilote() {
        return ministerePilote;
    }

    public void setMinisterePilote(String ministerePilote) {
        this.ministerePilote = ministerePilote;
    }

    public String getLegislature() {
        return legislature;
    }

    public void setLegislature(String legislature) {
        this.legislature = legislature;
    }

    public Calendar getDateSaisineCE() {
        return dateSaisineCE;
    }

    public void setDateSaisineCE(Calendar dateSaisineCE) {
        this.dateSaisineCE = dateSaisineCE;
    }

    public Calendar getDatePassageCM() {
        return datePassageCM;
    }

    public void setDatePassageCM(Calendar datePassageCM) {
        this.datePassageCM = datePassageCM;
    }

    public Calendar getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Calendar datePublication) {
        this.datePublication = datePublication;
    }

    public String getTitreOfficiel() {
        return titreOfficiel;
    }

    public void setTitreOfficiel(String titreOfficiel) {
        this.titreOfficiel = titreOfficiel;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDepotRatification() {
        return depotRatification;
    }

    public void setDepotRatification(String depotRatification) {
        this.depotRatification = depotRatification;
    }

    public Calendar getDateLimiteDepot() {
        return dateLimiteDepot;
    }

    public void setDateLimiteDepot(Calendar dateLimiteDepot) {
        this.dateLimiteDepot = dateLimiteDepot;
    }
}
