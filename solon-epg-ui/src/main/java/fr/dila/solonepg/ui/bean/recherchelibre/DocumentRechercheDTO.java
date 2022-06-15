package fr.dila.solonepg.ui.bean.recherchelibre;

import fr.dila.st.ui.utils.XssSanitizerUtils;

public class DocumentRechercheDTO {
    private String nom;

    private String version;

    private String extrait;

    public DocumentRechercheDTO() {}

    public DocumentRechercheDTO(String nom, String version, String extrait) {
        this.setNom(nom);
        this.version = version;
        this.setExtrait(extrait);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        XssSanitizerUtils.stripXSS(nom);
        this.nom = nom;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getExtrait() {
        return extrait;
    }

    public void setExtrait(String extrait) {
        XssSanitizerUtils.stripXSS(extrait);
        this.extrait = extrait;
    }
}
