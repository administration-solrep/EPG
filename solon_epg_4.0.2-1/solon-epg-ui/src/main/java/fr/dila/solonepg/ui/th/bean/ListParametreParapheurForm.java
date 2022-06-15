package fr.dila.solonepg.ui.th.bean;

import fr.dila.st.ui.annot.SwBean;
import java.util.ArrayList;
import javax.ws.rs.FormParam;

@SwBean
public class ListParametreParapheurForm {

    public ListParametreParapheurForm() {}

    @FormParam("parametreParapheurFormCreations[]")
    private ArrayList<ParametreParapheurFormCreation> parametreParapheurFormCreations = new ArrayList<>();

    @FormParam("typeActe")
    private String typeActe;

    public ArrayList<ParametreParapheurFormCreation> getParametreParapheurFormCreations() {
        return parametreParapheurFormCreations;
    }

    public void setParametreParapheurFormCreations(
        ArrayList<ParametreParapheurFormCreation> parametreParapheurFormCreations
    ) {
        this.parametreParapheurFormCreations = parametreParapheurFormCreations;
    }

    public String getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }
}
