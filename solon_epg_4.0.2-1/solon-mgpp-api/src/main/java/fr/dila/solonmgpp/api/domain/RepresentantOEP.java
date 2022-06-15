package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface de representation d'un representant d'un OEP
 *
 * @author asatre
 *
 */
public interface RepresentantOEP {
    String getIdFicheRepresentationOEP();

    void setIdFicheRepresentationOEP(String idFicheRepresentationOEP);

    String getType();

    void setType(String type);

    String getRepresentant();

    void setRepresentant(String representant);

    String getFonction();

    void setFonction(String fonction);

    Calendar getDateDebut();

    void setDateDebut(Calendar dateDebut);

    Calendar getDateFin();

    void setDateFin(Calendar dateFin);

    Long getNumeroMandat();

    void setNumeroMandat(Long numeroMandat);

    String getAutoriteDesignation();

    void setAutoriteDesignation(String autoriteDesignation);

    String getCommissionSaisie();

    void setCommissionSaisie(String commissionSaisie);

    DocumentModel getDocument();
}
