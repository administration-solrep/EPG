package fr.dila.solonepg.api.dto;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteMaitre;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * DTO representant une ordonnance
 *
 * @author asatre
 *
 */
public interface OrdonnanceDTO extends Map<String, Serializable> {
    String getId();
    void setId(String id);

    void setTitreActe(String titreActe);
    String getTitreActe();

    String getNumeroNor();
    void setNumeroNor(String numeroNor);

    String getMinisterePilote();
    void setMinisterePilote(String ministerePilote);

    String getTitreOfficiel();
    void setTitreOfficiel(String titreOfficiel);

    Date getDatePublication();
    void setDatePublication(Date datePublication);

    String getObjet();
    void setObjet(String objet);

    String getMotCle();
    void setMotCle(String motCle);

    Boolean getCodification();
    void setCodification(Boolean codification);

    String getObservation();
    void setObservation(String observation);

    void setNumeroInterne(String numeroInterne);
    String getNumeroInterne();

    void setNumero(String numero);
    String getNumero();

    /**
     * true of 38C
     * @return
     */
    Boolean isDispositionHabilitation();
    void setDispositionHabilitation(Boolean dispositionHabilitation);

    DocumentModel remapField(TexteMaitre texteMaitre);

    void setTitreActeLocked(Boolean titreActeLocked);
    Boolean getTitreActeLocked();

    Boolean isMinisterePiloteLocked();
    void setMinisterePiloteLocked(Boolean ministerePiloteLocked);

    Boolean isNumeroInterneLocked();
    void setNumeroInterneLocked(Boolean numeroInterneLocked);

    Boolean getDatePublicationLocked();
    void setDatePublicationLocked(Boolean datePublicationLocked);

    Boolean getTitreOfficielLocked();
    void setTitreOfficielLocked(Boolean titreOfficielLocked);

    void setLienJORFLegifrance(String lienJORFLegifrance);
    String getLienJORFLegifrance();

    void refresh(Dossier dossier, CoreSession documentManager);

    void setDecretIds(List<String> decretIds);
    List<String> getDecretIds();

    Boolean getRatifie();
    void setRatifie(Boolean ratifie);

    List<String> getDecretIdsInvalidated();
    void setDecretIdsInvalidated(List<String> decretIds);

    Boolean isNumeroLocked();
    void setNumeroLocked(Boolean numeroLocked);
}
