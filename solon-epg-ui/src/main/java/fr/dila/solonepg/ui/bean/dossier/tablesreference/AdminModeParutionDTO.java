package fr.dila.solonepg.ui.bean.dossier.tablesreference;

import static fr.dila.solonepg.api.constant.ModeParutionConstants.MODE_PARUTION_DOCUMENT_TYPE;

import fr.dila.solonepg.api.constant.ModeParutionConstants;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class AdminModeParutionDTO {

    public AdminModeParutionDTO() {}

    @NxProp(docType = MODE_PARUTION_DOCUMENT_TYPE, xpath = STSchemaConstant.ECM_UUID_XPATH)
    @FormParam("id")
    private String id;

    @NxProp(docType = MODE_PARUTION_DOCUMENT_TYPE, xpath = ModeParutionConstants.MODE_PARUTION_MODE_XPATH)
    @SwNotEmpty
    @FormParam("mode")
    private String mode;

    @NxProp(docType = MODE_PARUTION_DOCUMENT_TYPE, xpath = ModeParutionConstants.MODE_PARUTION_DATE_DEBUT_XPATH)
    @FormParam("dateDebut")
    private Calendar dateDebut;

    @NxProp(docType = MODE_PARUTION_DOCUMENT_TYPE, xpath = ModeParutionConstants.MODE_PARUTION_DATE_FIN_XPATH)
    @FormParam("dateFin")
    private Calendar dateFin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Calendar getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Calendar dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Calendar getDateFin() {
        return dateFin;
    }

    public void setDateFin(Calendar dateFin) {
        this.dateFin = dateFin;
    }
}
