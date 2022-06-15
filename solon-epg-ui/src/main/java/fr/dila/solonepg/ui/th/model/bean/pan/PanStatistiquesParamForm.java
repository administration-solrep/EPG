package fr.dila.solonepg.ui.th.model.bean.pan;

import fr.dila.st.ui.annot.SwBean;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.FormParam;

@SwBean
public class PanStatistiquesParamForm {
    @FormParam("legislatures")
    private ArrayList<String> legislatures;

    @FormParam("legislatureEnCours")
    private String legislatureEnCours;

    public PanStatistiquesParamForm() {}

    public List<String> getLegislatures() {
        return legislatures;
    }

    public void setLegislatures(ArrayList<String> legislatures) {
        this.legislatures = legislatures;
    }

    public String getLegislatureEnCours() {
        return legislatureEnCours;
    }

    public void setLegislatureEnCours(String legislatureEnCours) {
        this.legislatureEnCours = legislatureEnCours;
    }
}
