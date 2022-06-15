package fr.dila.solonepg.ui.th.model.bean;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwId;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class EpgStatForm {
    @SwId
    @FormParam("ministereId-key")
    private String ministereId;

    @SwId
    @FormParam("posteId-key")
    private String posteId;

    @SwId
    @FormParam("uniteId-key")
    private String uniteId;

    @FormParam("periodeDebut")
    private Calendar periodeDebut;

    @FormParam("periodeFin")
    private Calendar periodeFin;

    @FormParam("dateValue")
    private Calendar date;

    @FormParam("vecteurPublication")
    private String vecteurPublication;

    @FormParam("periodeType")
    private String periodeType;

    @FormParam("periodeValue")
    private String periodeValue;

    @FormParam("rubriques")
    private String rubrique;

    @FormParam("motsCles")
    private String motsCles;

    @FormParam("champLibre")
    private String champLibre;

    @FormParam("statutArchivage")
    private String statutArchivage;

    public EpgStatForm() {}

    public String getMinistereId() {
        return ministereId;
    }

    public void setMinistereId(String ministereId) {
        this.ministereId = ministereId;
    }

    public String getPosteId() {
        return posteId;
    }

    public void setPosteId(String posteId) {
        this.posteId = posteId;
    }

    public String getUniteId() {
        return uniteId;
    }

    public void setUniteId(String uniteId) {
        this.uniteId = uniteId;
    }

    public Calendar getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(Calendar periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public Calendar getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(Calendar periodeFin) {
        this.periodeFin = periodeFin;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getVecteurPublication() {
        return vecteurPublication;
    }

    public void setVecteurPublication(String vecteurPublication) {
        this.vecteurPublication = vecteurPublication;
    }

    public String getPeriodeType() {
        return periodeType;
    }

    public void setPeriodeType(String periodeType) {
        this.periodeType = periodeType;
    }

    public String getPeriodeValue() {
        return periodeValue;
    }

    public void setPeriodeValue(String periodeValue) {
        this.periodeValue = periodeValue;
    }

    public String getRubrique() {
        return rubrique;
    }

    public void setRubrique(String rubrique) {
        this.rubrique = rubrique;
    }

    public String getMotsCles() {
        return motsCles;
    }

    public void setMotsCles(String motsCles) {
        this.motsCles = motsCles;
    }

    public String getChampLibre() {
        return champLibre;
    }

    public void setChampLibre(String champLibre) {
        this.champLibre = champLibre;
    }

    public String getStatutArchivage() {
        return statutArchivage;
    }

    public void setStatutArchivage(String statutArchivage) {
        this.statutArchivage = statutArchivage;
    }
}
