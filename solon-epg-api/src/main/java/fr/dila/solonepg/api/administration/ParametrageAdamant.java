package fr.dila.solonepg.api.administration;

import fr.dila.st.api.domain.STDomainObject;
import java.io.Serializable;
import java.util.List;

/**
 * Interface du document contenant les paramètres d'ADAMANT.
 *
 * @author BBE
 */
public interface ParametrageAdamant extends STDomainObject, Serializable {
    ///////////////////
    // getter/setter Corbeilles
    //////////////////

    /**
     * Getter numeroSolon
     * @return numeroSolon
     */
    Long getNumeroSolon();

    /**
     * Setter numeroSolon
     * @param numeroSolon
     */
    void setNumeroSolon(Integer numeroSolon);

    /**
     * Getter originatingAgencyBlocIdentifier
     * @return originatingAgencyBlocIdentifier
     */
    String getOriginatingAgencyBlocIdentifier();

    /**
     * Setter originatingAgencyBlocIdentifier
     * @param originatingAgencyBlocIdentifier
     */
    void setOriginatingAgencyBlocIdentifier(String originatingAgencyBlocIdentifier);

    /**
     * Getter submissionAgencyBlocIdentifier
     * @return submissionAgencyBlocIdentifier
     */
    String getSubmissionAgencyBlocIdentifier();

    /**
     * Setter submissionAgencyBlocIdentifier
     * @param submissionAgencyBlocIdentifier
     */
    void setSubmissionAgencyBlocIdentifier(String submissionAgencyBlocIdentifier);

    /**
     * Getter archivalProfile
     * @return archivalProfile
     */
    String getArchivalProfile();

    /**
     * Setter archivalProfile
     * @param archivalProfile
     */
    void setArchivalProfile(String archivalProfile);

    /**
     * Getter archivalProfileSpecific
     * @return archivalProfileSpecific
     */
    String getArchivalProfileSpecific();

    /**
     * Setter archivalProfileSpecific
     * @param archivalProfileSpecific
     */
    void setArchivalProfileSpecific(String archivalProfileSpecific);

    /**
     * Getter originatingAgencyIdentifier
     * @return originatingAgencyIdentifier
     */
    String getOriginatingAgencyIdentifier();

    /**
     * Setter originatingAgencyIdentifier
     * @param originatingAgencyIdentifier
     */
    void setOriginatingAgencyIdentifier(String originatingAgencyIdentifier);

    /**
     * Getter submissionAgencyIdentifier
     * @return submissionAgencyIdentifier
     */
    String getSubmissionAgencyIdentifier();

    /**
     * Setter submissionAgencyIdentifier
     * @param submissionAgencyIdentifier
     */
    void setSubmissionAgencyIdentifier(String submissionAgencyIdentifier);

    /**
     * Getter delaiEligibilite
     * @return delaiEligibilite
     */
    Long getDelaiEligibilite();

    /**
     * Setter delaiEligibilite
     * @param delaiEligibilite
     */
    void setDelaiEligibilite(Integer delaiEligibilite);

    /**
     * Getter nbDossierExtraction
     * @return nbDossierExtraction
     */
    Long getNbDossierExtraction();

    /**
     * Setter nbDossierExtraction
     * @param nbDossierExtraction
     */
    void setNbDossierExtraction(Integer nbDossierExtraction);

    /**
     * Getter vecteurPublication
     * @return vecteurPublication
     */
    List<String> getVecteurPublication();

    /**
     * Setter vecteurPublication
     * @param vecteurPublication
     */
    void setVecteurPublication(List<String> vecteurPublication);

    /**
     * Getter typeActe
     * @return typeActe
     */
    List<String> getTypeActe();

    /**
     * Setter typeActe
     * @param typeActe
     */
    void setTypeActe(List<String> typeActe);
}