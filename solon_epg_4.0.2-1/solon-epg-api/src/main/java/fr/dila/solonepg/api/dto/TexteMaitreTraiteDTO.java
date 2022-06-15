package fr.dila.solonepg.api.dto;

import fr.dila.solonepg.api.cases.TexteMaitre;
import java.util.Date;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface TexteMaitreTraiteDTO {
    String getId();

    void setId(String id);

    String getCategorie();

    void setCategorie(String categorie);

    String getOrganisation();

    void setOrganisation(String organisation);

    String getThematique();

    void setThematique(String thematique);

    String getTitreActe();

    void setTitreActe(String titreActe);

    Date getDateSignature();

    void setDateSignature(Date dateSignature);

    Date getDateEntreeEnVigueur();

    void setDateEntreeEnVigueur(Date dateEntreeEnVigueur);

    Boolean getAutorisationRatification();

    void setAutorisationRatification(Boolean autorisationRatification);

    Boolean getPublication();

    void setPublication(Boolean publication);

    String getObservation();

    void setObservation(String observation);

    String getMinisterePilote();

    void setMinisterePilote(String ministerePilote);

    String getDegrePriorite();

    void setDegrePriorite(String degrePriorite);

    Date getDatePJL();

    void setDatePJL(Date datePJL);

    Boolean getEtudeImpact();

    void setEtudeImpact(Boolean etudeImpact);

    Boolean getDispoEtudeImpact();

    void setDispoEtudeImpact(Boolean dispoEtudeImpact);

    Date getDateDepotRatification();

    void setDateDepotRatification(Date dateDepotRatification);

    void refresh(CoreSession session);

    DocumentModel remapField(TexteMaitre texteMaitre, CoreSession session);

    DocumentModel remapDecret(CoreSession session);

    DocumentModel remapRatification(CoreSession session);
}
