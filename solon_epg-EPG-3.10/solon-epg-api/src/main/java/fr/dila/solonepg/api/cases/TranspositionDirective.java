package fr.dila.solonepg.api.cases;

import java.util.Calendar;
import java.util.List;

/**
 * Transposition de directive européenne ({@link ActiviteNormative}).
 * 
 * @author asatre
 * 
 */
public interface TranspositionDirective extends TexteMaitre {

    String getId();

    Calendar getDateDirective();

    String getTitre();

    Calendar getDateEcheance();

    Boolean isTabAffichageMarcheInt();

    Calendar getDateProchainTabAffichage();

    Boolean isAchevee();

    List<String> getDossiersNor();

    void setDateDirective(Calendar dateDirective);

    void setTitre(String titre);

    void setDateEcheance(Calendar dateEcheance);

    void setTabAffichageMarcheInt(Boolean tabAffichageMarcheInt);

    void setDateProchainTabAffichage(Calendar dateProchainTabAffichage);

    void setAchevee(Boolean achevee);

    void setDossiersNor(List<String> dossierNor);

    void setDirectionResponsable(String directionResponsable);

    String getDirectionResponsable();

    Calendar getDateTranspositionEffective();

    void setDateTranspositionEffective(Calendar dateTranspositionEffective);
    
    String getEtat();

    void setEtat(String etat);

}
