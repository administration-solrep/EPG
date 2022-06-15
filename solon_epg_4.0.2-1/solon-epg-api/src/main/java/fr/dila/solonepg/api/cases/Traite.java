package fr.dila.solonepg.api.cases;

import java.util.Calendar;

/**
 * Interface pour les Traite et Accords de l'activite normative.
 *
 * @author asatre
 *
 */
public interface Traite extends TexteMaitre {
    String getId();

    String getCategorie();

    void setCategorie(String categorie);

    String getOrganisation();

    void setOrganisation(String organisation);

    String getThematique();

    void setThematique(String thematique);

    Calendar getDateSignature();

    void setDateSignature(Calendar dateSignature);

    Boolean hasAutorisationRatification();

    void setAutorisationRatification(Boolean autorisationRatification);

    Boolean isPublication();

    void setPublication(Boolean publication);

    String getNorDecret();

    void setNorDecret(String norDecret);

    String getDegrePriorite();

    void setDegrePriorite(String degreProrite);

    Calendar getDatePJL();

    void setDatePJL(Calendar datePJL);

    Boolean hasEtudeImpact();

    void setEtudeImpact(Boolean etudeImpact);

    Boolean isDispoEtudeImpact();

    void setDispoEtudeImpact(Boolean dispoEtudeImpact);

    String getNorLoiRatification();

    void setNorLoiRatification(String norLoiRatification);

    Calendar getDateDepotRatification();

    void setDateDepotRatification(Calendar dateDepotRatification);
}
