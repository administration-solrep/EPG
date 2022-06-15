package fr.dila.solonepg.ui.bean.pan.birt;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwMonthFormat;
import fr.dila.st.ui.validators.annot.SwYearFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.stream.Stream;
import javax.ws.rs.QueryParam;

@SwBean
public class PanBirtParamForm {
    @QueryParam("intervalle1Debut")
    private Calendar debutIntervalle1;

    @QueryParam("intervalle1Fin")
    private Calendar finIntervalle1;

    @QueryParam("intervalle2Debut")
    private Calendar debutIntervalle2;

    @QueryParam("intervalle2Fin")
    private Calendar finIntervalle2;

    @QueryParam("debutLegislature")
    private Calendar debutLegislature;

    @QueryParam("legislatures")
    private String legislatures;

    @QueryParam("periode")
    private String periode;

    @QueryParam("mois")
    @SwMonthFormat
    private Integer mois;

    @QueryParam("annee")
    @SwYearFormat(minYear = 2007, maxCurrentYear = true)
    private Integer annee;

    @QueryParam("dossierId")
    private String dossierId;

    @QueryParam("ministerePilote")
    private String ministerePilote;

    public PanBirtParamForm() {
        super();
    }

    public Calendar getDebutLegislature() {
        return debutLegislature;
    }

    public void setDebutLegislature(Calendar debutLegislature) {
        this.debutLegislature = debutLegislature;
    }

    public String getLegislatures() {
        return legislatures;
    }

    public void setLegislatures(String legislatures) {
        this.legislatures = legislatures;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Integer getMois() {
        return mois;
    }

    public void setMois(Integer mois) {
        this.mois = mois;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public String getDossierId() {
        return dossierId;
    }

    public void setDossierId(String dossierId) {
        this.dossierId = dossierId;
    }

    public Calendar getDebutIntervalle1() {
        return debutIntervalle1;
    }

    public void setDebutIntervalle1(Calendar debutIntervalle1) {
        this.debutIntervalle1 = debutIntervalle1;
    }

    public Calendar getFinIntervalle1() {
        return finIntervalle1;
    }

    public void setFinIntervalle1(Calendar finIntervalle1) {
        this.finIntervalle1 = finIntervalle1;
    }

    public Calendar getDebutIntervalle2() {
        return debutIntervalle2;
    }

    public void setDebutIntervalle2(Calendar debutIntervalle2) {
        this.debutIntervalle2 = debutIntervalle2;
    }

    public Calendar getFinIntervalle2() {
        return finIntervalle2;
    }

    public void setFinIntervalle2(Calendar finIntervalle2) {
        this.finIntervalle2 = finIntervalle2;
    }

    public String getMinisterePilote() {
        return ministerePilote;
    }

    public void setMinisterePilote(String ministerePilote) {
        this.ministerePilote = ministerePilote;
    }

    public boolean isNull() {
        return Stream
            .of(
                debutIntervalle1,
                finIntervalle1,
                debutIntervalle2,
                finIntervalle2,
                debutLegislature,
                legislatures,
                periode,
                mois,
                annee,
                dossierId,
                ministerePilote
            )
            .allMatch(Objects::isNull);
    }
}
