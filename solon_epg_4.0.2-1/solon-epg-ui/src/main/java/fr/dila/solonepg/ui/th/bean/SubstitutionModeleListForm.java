package fr.dila.solonepg.ui.th.bean;

import fr.dila.ss.ui.th.bean.ModeleFDRListForm;
import fr.dila.st.ui.annot.SwBean;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;

@SwBean(keepdefaultValue = true)
public class SubstitutionModeleListForm extends ModeleFDRListForm {
    @QueryParam("filtreTypeActe")
    @FormParam("filtreTypeActe")
    private boolean isFiltreTypeActe;

    @QueryParam("filtreMinistere")
    @FormParam("filtreMinistere")
    private boolean isFiltreMinistere;

    public SubstitutionModeleListForm() {}

    public boolean getIsFiltreTypeActe() {
        return isFiltreTypeActe;
    }

    public void setIsFiltreTypeActe(boolean isFiltreTypeActe) {
        this.isFiltreTypeActe = isFiltreTypeActe;
    }

    public boolean getIsFiltreMinistere() {
        return isFiltreMinistere;
    }

    public void setIsFiltreMinistere(boolean isFiltreMinistere) {
        this.isFiltreMinistere = isFiltreMinistere;
    }
}
