package fr.dila.solonepg.api.dto;

import fr.dila.solonepg.api.cases.TranspositionDirective;
import java.util.Date;

/**
 * DTO pour {@link TranspositionDirective}
 *
 * @author asatre
 *
 */
public interface TranspositionDirectiveDTO {
    String getId();

    String getNumero();

    Date getDateDirective();

    String getTitreOfficiel();

    Date getDateEcheance();

    Boolean getTabAffichageMarcheInt();

    Date getDateProchainTabAffichage();

    String getMinisterePilote();

    String getMinisterePiloteLabel();

    Boolean getAchevee();

    String getObservation();

    void setId(String id);

    void setNumero(String numero);

    void setDateDirective(Date dateDirective);

    void setTitreOfficiel(String titre);

    void setDateEcheance(Date dateEcheance);

    void setTabAffichageMarcheInt(Boolean tabAffichageMarcheInt);

    void setDateProchainTabAffichage(Date dateProchainTabAffichage);

    void setMinisterePilote(String ministerePilote);

    void setMinisterePiloteLabel(String ministerePilote);

    void setAchevee(Boolean achevee);

    void setObservation(String observation);

    void setDirectionResponsable(String directionResponsable);

    String getDirectionResponsable();

    void setEtat(String etat);

    String getEtat();

    /**
     * remap field from DTO to DocumentModel
     *
     * @param transpositionDirective
     * @return
     */
    TranspositionDirective remapField(TranspositionDirective transpositionDirective);
}
