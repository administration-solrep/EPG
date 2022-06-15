package fr.dila.solonmgpp.core.dto;

import fr.dila.solonmgpp.api.dto.IdentiteDTO;
import java.util.Date;

/**
 * Implementation de {@link IdentiteDTO}
 *
 * @author admin
 *
 */
public class IdentiteDTOImpl extends TableReferenceDTOImpl implements IdentiteDTO {
    private String prenom;
    private String civilite;
    private Date dateDebutMandat;
    private Date datefinMandat;
    private String titleModifie;

    @Override
    public String getPrenom() {
        return prenom;
    }

    @Override
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public String getTitle() {
        return civilite + " " + getNom() + " " + prenom;
    }

    @Override
    public void setTitleModifie(String title) {
        this.titleModifie = title;
    }

    @Override
    public String getCivilite() {
        return civilite;
    }

    @Override
    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    @Override
    public Date getDateDebutMandat() {
        return dateDebutMandat;
    }

    @Override
    public void setDateDebutMandat(Date dateDebutMandat) {
        this.dateDebutMandat = dateDebutMandat;
    }

    @Override
    public Date getDateFinMandat() {
        return datefinMandat;
    }

    @Override
    public void setDateFinMandat(Date dateFinMandat) {
        this.datefinMandat = dateFinMandat;
    }

    @Override
    public String getTitleModifie() {
        return titleModifie;
    }
}
