package fr.dila.solonepg.ui.th.model.bean.pan;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean(keepdefaultValue = true)
public class TraiteForm {
    @SwNotEmpty
    @FormParam("intitule")
    private String intitule;

    @SwNotEmpty
    @FormParam("dateSignature")
    private Calendar dateSignature;

    @SwRequired
    @FormParam("publicationJORF")
    private Boolean publication;

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Calendar getDateSignature() {
        return dateSignature;
    }

    public void setDateSignature(Calendar dateSignature) {
        this.dateSignature = dateSignature;
    }

    public Boolean getPublication() {
        return publication;
    }

    public void setPublication(Boolean publication) {
        this.publication = publication;
    }
}
