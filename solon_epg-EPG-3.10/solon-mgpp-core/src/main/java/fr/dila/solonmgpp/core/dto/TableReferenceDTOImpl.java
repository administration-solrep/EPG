package fr.dila.solonmgpp.core.dto;

import java.util.Date;

import fr.dila.solonmgpp.api.dto.TableReferenceDTO;
import fr.dila.st.core.util.DateUtil;

public abstract class TableReferenceDTOImpl implements TableReferenceDTO {

    private String id;
    private Date dateDebut;
    private Date dateFin;
    private String proprietaire;
    private String nom;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Date getDateDebut() {
        return DateUtil.copyDate(dateDebut);
    }

    @Override
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = DateUtil.copyDate(dateDebut);
    }

    @Override
    public Date getDateFin() {
        return DateUtil.copyDate(dateFin);
    }

    @Override
    public void setDateFin(Date dateFin) {
        this.dateFin = DateUtil.copyDate(dateFin);
    }

    @Override
    public String getProprietaire() {
        return proprietaire;
    }

    @Override
    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

}
