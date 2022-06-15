package fr.dila.solonepg.ui.th.bean;

import fr.dila.st.ui.annot.SwBean;
import java.util.ArrayList;
import javax.ws.rs.FormParam;

@SwBean
public class FavorisRechercheForm {
    @FormParam("intitule")
    private String intitule;

    @FormParam("postes")
    private ArrayList<String> postes;

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public ArrayList<String> getPostes() {
        return postes;
    }

    public void setPostes(ArrayList<String> postes) {
        this.postes = postes;
    }
}
