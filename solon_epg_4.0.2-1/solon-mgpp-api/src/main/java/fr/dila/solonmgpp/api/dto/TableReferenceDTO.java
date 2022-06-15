package fr.dila.solonmgpp.api.dto;

import java.util.Date;

/**
 * Interface generique des tables de references
 *
 * @author asatre
 *
 */
public interface TableReferenceDTO {
    void setId(String id);

    String getId();

    String getTitle();

    String getTitleModifie();

    void setTitleModifie(String title);

    Date getDateDebut();

    void setDateDebut(Date dateDebut);

    Date getDateFin();

    void setDateFin(Date dateFin);

    void setProprietaire(String proprietaire);

    String getProprietaire();

    void setNom(String nom);

    String getNom();

    Date getDateDebutMandat();

    void setDateDebutMandat(Date dateDebutMandat);

    Date getDateFinMandat();

    void setDateFinMandat(Date dateFinMandat);
}
