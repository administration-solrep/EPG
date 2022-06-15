package fr.dila.solonepg.ui.th.bean;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwLength;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import javax.ws.rs.FormParam;

@SwBean
public class AttenteSignatureForm {
    @SwNotEmpty
    @SwId
    @FormParam("dossierId")
    private String dossierId;

    @FormParam("dateDemandePublicationTS")
    private String dateDemandePublicationTS;

    @FormParam("vecteurPublicationTS")
    private String vecteurPublicationTS;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("observation")
    private String observation;

    public AttenteSignatureForm() {}

    public String getDossierId() {
        return dossierId;
    }

    public void setDossierId(String dossierId) {
        this.dossierId = dossierId;
    }

    public String getDateDemandePublicationTS() {
        return dateDemandePublicationTS;
    }

    public void setDateDemandePublicationTS(String dateDemandePublicationTS) {
        this.dateDemandePublicationTS = dateDemandePublicationTS;
    }

    public String getVecteurPublicationTS() {
        return vecteurPublicationTS;
    }

    public void setVecteurPublicationTS(String vecteurPublicationTS) {
        this.vecteurPublicationTS = vecteurPublicationTS;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
