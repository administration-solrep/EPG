package fr.dila.solonepg.ui.th.model.bean;

import com.google.gson.Gson;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwRegex;
import java.util.Map;
import javax.ws.rs.FormParam;

@SwBean
public class MgppRechercheRapideForm {
    @FormParam("idDossierParlementaire")
    @SwRegex("[a-zA-Z0-9_]+")
    private String idDossierParlementaire;

    @FormParam("idDossier")
    private String idDossier;

    @FormParam("numeroNor")
    private String numeroNor;

    @FormParam("objet")
    private String objet;

    @FormParam("titreActe")
    private String titreActe;

    @FormParam("nomOrganisme")
    private String nomOrganisme;

    public MgppRechercheRapideForm() {}

    @SuppressWarnings("unchecked")
    public MgppRechercheRapideForm(String json) {
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(json, Map.class);

        if (map != null) {
            setIdDossierParlementaire((String) map.get("idDossierParlementaire"));
            setIdDossier((String) map.get("idDossier"));
            setNumeroNor((String) map.get("numeroNor"));
            setObjet((String) map.get("objet"));
            setTitreActe((String) map.get("titreActe"));
            setNomOrganisme((String) map.get("nomOrganisme"));
        }
    }

    public String getIdDossierParlementaire() {
        return idDossierParlementaire;
    }

    public void setIdDossierParlementaire(String idDossierParlementaire) {
        this.idDossierParlementaire = idDossierParlementaire;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public String getNumeroNor() {
        return numeroNor;
    }

    public void setNumeroNor(String numeroNor) {
        this.numeroNor = numeroNor;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getTitreActe() {
        return titreActe;
    }

    public void setTitreActe(String titreActe) {
        this.titreActe = titreActe;
    }

    public String getNomOrganisme() {
        return nomOrganisme;
    }

    public void setNomOrganisme(String nomOrganisme) {
        this.nomOrganisme = nomOrganisme;
    }
}
