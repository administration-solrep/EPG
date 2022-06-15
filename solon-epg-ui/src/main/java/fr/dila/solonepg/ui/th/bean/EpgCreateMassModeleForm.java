package fr.dila.solonepg.ui.th.bean;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwRequired;
import javax.ws.rs.FormParam;

@SwBean
public class EpgCreateMassModeleForm {
    @SwRequired
    @FormParam("ministere")
    private String ministere;

    @SwRequired
    @FormParam("bdc")
    private String bdc;

    @SwRequired
    @FormParam("chargeMission")
    private String chargeMission;

    @SwRequired
    @FormParam("secretariatCM")
    private String secretariatCM;

    @SwRequired
    @FormParam("conseillerPM")
    private String conseillerPM;

    public EpgCreateMassModeleForm() {}

    public String getMinistere() {
        return ministere;
    }

    public void setMinistere(String ministere) {
        this.ministere = ministere;
    }

    public String getBdc() {
        return bdc;
    }

    public void setBdc(String bdc) {
        this.bdc = bdc;
    }

    public String getChargeMission() {
        return chargeMission;
    }

    public void setChargeMission(String chargeMission) {
        this.chargeMission = chargeMission;
    }

    public String getSecretariatCM() {
        return secretariatCM;
    }

    public void setSecretariatCM(String secretariatCM) {
        this.secretariatCM = secretariatCM;
    }

    public String getConseillerPM() {
        return conseillerPM;
    }

    public void setConseillerPM(String conseillerPM) {
        this.conseillerPM = conseillerPM;
    }
}
