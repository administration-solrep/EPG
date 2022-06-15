package fr.dila.solonepg.ui.bean;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import javax.ws.rs.FormParam;

@SwBean
public class EpgIndexationMotCleForm {
    @SwRequired
    @FormParam("label")
    private String label;

    @SwNotEmpty
    @FormParam("ministereId")
    private String ministereId;

    public EpgIndexationMotCleForm() {}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMinistereId() {
        return ministereId;
    }

    public void setMinistereId(String ministereId) {
        this.ministereId = ministereId;
    }
}
