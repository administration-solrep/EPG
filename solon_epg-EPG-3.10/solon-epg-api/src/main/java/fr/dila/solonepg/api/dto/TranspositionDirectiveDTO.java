package fr.dila.solonepg.api.dto;

import java.util.Date;

import fr.dila.solonepg.api.cases.TranspositionDirective;

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

    String getTitre();

    Date getDateEcheance();

    Boolean getTabAffichageMarcheInt();

    Date getDateProchainTabAffichage();

    String getMinisterePilote();

    Boolean getAchevee();

    String getObservation();

    void setNumero(String numero);

    void setDateDirective(Date dateDirective);

    void setTitre(String titre);

    void setDateEcheance(Date dateEcheance);

    void setTabAffichageMarcheInt(Boolean tabAffichageMarcheInt);

    void setDateProchainTabAffichage(Date dateProchainTabAffichage);

    void setMinisterePilote(String ministerePilote);

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
