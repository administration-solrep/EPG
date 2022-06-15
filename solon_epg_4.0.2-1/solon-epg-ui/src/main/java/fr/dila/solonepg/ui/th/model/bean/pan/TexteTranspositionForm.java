package fr.dila.solonepg.ui.th.model.bean.pan;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwLength;
import fr.dila.st.ui.validators.annot.SwRegex;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean(keepdefaultValue = true)
public class TexteTranspositionForm {
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("nature")
    private String nature;

    @FormParam("numeroNor")
    private String numeroNor;

    @SwId
    @FormParam("ministerePilote")
    private String ministerePilote;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("intitule")
    private String intitule;

    @FormParam("datePublication")
    private Calendar datePublication;

    @SwRegex(value = "20[0-9]{2}-[0-9]+")
    @FormParam("numeroTextePublie")
    private String numeroTextePublie;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("titreOfficiel")
    private String titreOfficiel;

    public TexteTranspositionForm() {}

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getNumeroNor() {
        return numeroNor;
    }

    public void setNumeroNor(String numeroNor) {
        this.numeroNor = numeroNor;
    }

    public String getMinisterePilote() {
        return ministerePilote;
    }

    public void setMinisterePilote(String ministerePilote) {
        this.ministerePilote = ministerePilote;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Calendar getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Calendar datePublication) {
        this.datePublication = datePublication;
    }

    public String getNumeroTextePublie() {
        return numeroTextePublie;
    }

    public void setNumeroTextePublie(String numeroTextePublie) {
        this.numeroTextePublie = numeroTextePublie;
    }

    public String getTitreOfficiel() {
        return titreOfficiel;
    }

    public void setTitreOfficiel(String titreOfficiel) {
        this.titreOfficiel = titreOfficiel;
    }
}
