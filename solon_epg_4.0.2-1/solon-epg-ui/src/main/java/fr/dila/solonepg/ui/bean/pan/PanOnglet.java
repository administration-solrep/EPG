package fr.dila.solonepg.ui.bean.pan;

import fr.dila.st.ui.bean.Onglet;
import org.apache.commons.lang3.StringUtils;

public class PanOnglet extends Onglet {
    private String idBirtReport = StringUtils.EMPTY;
    private String altIdBirtReport = StringUtils.EMPTY;
    private String switchLabel = StringUtils.EMPTY;

    public String getIdBirtReport() {
        return idBirtReport;
    }

    public void setIdBirtReport(String idBirtReport) {
        this.idBirtReport = idBirtReport;
    }

    public boolean hasBirt() {
        return !StringUtils.isEmpty(idBirtReport);
    }

    public String getAltIdBirtReport() {
        return altIdBirtReport;
    }

    public void setAltIdBirtReport(String altIdBirtReport) {
        this.altIdBirtReport = altIdBirtReport;
    }

    public String getSwitchLabel() {
        return switchLabel;
    }

    public void setSwitchLabel(String switchLabel) {
        this.switchLabel = switchLabel;
    }
}
