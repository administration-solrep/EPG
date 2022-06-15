package fr.dila.solonepg.ui.th.model.bean;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwLength;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import javax.ws.rs.FormParam;

@SwBean
public class MgppParamForm {
    @FormParam("formUrl")
    @SwNotEmpty
    @SwLength(min = 1, max = 2000)
    private String url;

    @FormParam("formUsername")
    @SwNotEmpty
    @SwLength(min = 1, max = 200)
    private String username;

    @FormParam("formPassword")
    @SwNotEmpty
    @SwLength(min = 1, max = 200)
    private String password;

    @FormParam("formPersistance")
    @SwNotEmpty
    @SwLength(min = 1, max = 19)
    private Long nbJourPers;

    @FormParam("formAssemblee")
    @SwNotEmpty
    @SwLength(min = 1, max = 2000)
    private String presAN;

    @FormParam("formSenat")
    @SwNotEmpty
    @SwLength(min = 1, max = 2000)
    private String presSenat;

    @FormParam("formDelaiSec")
    @SwNotEmpty
    @SwLength(min = 1, max = 19)
    private Long delaiVerifSec;

    @FormParam("formEntetePM")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String initComPM;

    @FormParam("formEnteteDirecteurAdjointSGG")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String initComDirecteurAdjointSGG;

    @FormParam("formEnteteSGG")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String initComSGG;

    @FormParam("formText")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String textAccueil;

    @FormParam("formDelaiPurge")
    @SwNotEmpty
    @SwLength(min = 1, max = 19)
    private Long delaiPurgeMois;

    @FormParam("formFiltreLoi")
    @SwNotEmpty
    @SwLength(min = 1, max = 19)
    private Long filtreLoi;

    @FormParam("minChargeRelation")
    private Boolean initCourrier;

    public MgppParamForm() {
        //default constructor
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getNbJourPers() {
        return nbJourPers;
    }

    public void setNbJourPers(Long nbJourPers) {
        this.nbJourPers = nbJourPers;
    }

    public String getPresAN() {
        return presAN;
    }

    public void setPresAN(String presAN) {
        this.presAN = presAN;
    }

    public String getPresSenat() {
        return presSenat;
    }

    public void setPresSenat(String presSenat) {
        this.presSenat = presSenat;
    }

    public Long getDelaiVerifSec() {
        return delaiVerifSec;
    }

    public void setDelaiVerifSec(Long delaiVerifSec) {
        this.delaiVerifSec = delaiVerifSec;
    }

    public String getInitComPM() {
        return initComPM;
    }

    public void setInitComPM(String initComPM) {
        this.initComPM = initComPM;
    }

    public String getInitComDirecteurAdjointSGG() {
        return initComDirecteurAdjointSGG;
    }

    public void setInitComDirecteurAdjointSGG(String initComDirecteurAdjointSGG) {
        this.initComDirecteurAdjointSGG = initComDirecteurAdjointSGG;
    }

    public String getInitComSGG() {
        return initComSGG;
    }

    public void setInitComSGG(String initComSGG) {
        this.initComSGG = initComSGG;
    }

    public String getTextAccueil() {
        return textAccueil;
    }

    public void setTextAccueil(String textAccueil) {
        this.textAccueil = textAccueil;
    }

    public Long getDelaiPurgeMois() {
        return delaiPurgeMois;
    }

    public void setDelaiPurgeMois(Long delaiPurgeMois) {
        this.delaiPurgeMois = delaiPurgeMois;
    }

    public Long getFiltreLoi() {
        return filtreLoi;
    }

    public void setFiltreLoi(Long filtreLoi) {
        this.filtreLoi = filtreLoi;
    }

    public Boolean getInitCourrier() {
        return initCourrier;
    }

    public void setInitCourrier(Boolean initCourrier) {
        this.initCourrier = initCourrier;
    }
}
