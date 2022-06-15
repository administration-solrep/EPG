package fr.dila.solonepg.ui.th.bean;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwMin;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class MgppMandatForm {
    @FormParam("idMandat")
    private String idMandat;

    @FormParam("typeMandat")
    private String typeMandat;

    @SwNotEmpty
    @FormParam("titre")
    private String titre;

    @SwNotEmpty
    @FormParam("appellation")
    private String appellation;

    @FormParam("nor")
    private String nor;

    @SwRequired
    @FormParam("ordreProtocolaire")
    @SwMin(0)
    private Long ordreProtocolaire;

    @SwRequired
    @FormParam("dateDebutMandat")
    private Calendar dateDebutMandat;

    @FormParam("dateFinMandat")
    private Calendar dateFinMandat;

    public MgppMandatForm() {
        this.appellation = null;
        this.dateDebutMandat = Calendar.getInstance();
        this.dateFinMandat = null;
        this.idMandat = null;
        this.nor = null;
        this.ordreProtocolaire = null;
        this.titre = null;
        this.typeMandat = null;
    }

    public String getIdMandat() {
        return idMandat;
    }

    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    public String getTypeMandat() {
        return typeMandat;
    }

    public void setTypeMandat(String typeMandat) {
        this.typeMandat = typeMandat;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public Long getOrdreProtocolaire() {
        return ordreProtocolaire;
    }

    public void setOrdreProtocolaire(Long ordreProtocolaire) {
        this.ordreProtocolaire = ordreProtocolaire;
    }

    public Calendar getDateDebutMandat() {
        return dateDebutMandat;
    }

    public void setDateDebutMandat(Calendar dateDebutMandat) {
        this.dateDebutMandat = dateDebutMandat;
    }

    public Calendar getDateFinMandat() {
        return dateFinMandat;
    }

    public void setDateFinMandat(Calendar dateFinMandat) {
        this.dateFinMandat = dateFinMandat;
    }
}
