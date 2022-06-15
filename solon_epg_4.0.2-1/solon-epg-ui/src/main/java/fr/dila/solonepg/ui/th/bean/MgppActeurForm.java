package fr.dila.solonepg.ui.th.bean;

import fr.dila.st.ui.annot.SwBean;
import javax.ws.rs.FormParam;

@SwBean
public class MgppActeurForm {
    @FormParam("idActeur")
    private String idActeur;

    public MgppActeurForm() {
        idActeur = null;
    }

    public String getIdActeur() {
        return idActeur;
    }

    public void setIdActeur(String idActeur) {
        this.idActeur = idActeur;
    }
}
