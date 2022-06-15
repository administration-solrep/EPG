/**
 *
 */
package fr.dila.solonepg.api.administration;

import fr.dila.st.api.domain.STDomainObject;
import java.io.Serializable;
import java.util.List;

/**
 * Table de reference Interface for solon epg.
 *
 * @author asatre
 *
 */
public interface TableReference extends STDomainObject, Serializable {
    public static final String MINISTERE_CE = "ministereCe";

    List<String> getCabinetPM();

    void setCabinetPM(List<String> cabinetPM);

    List<String> getChargesMission();

    void setChargesMission(List<String> chargesMission);

    List<String> getSignataires();

    void setSignataires(List<String> signataires);

    String getMinisterePM();

    void setMinisterePM(String ministerePM);

    String getMinistereCE();

    void setMinistereCE(String ministereCE);

    String getPostePublicationId();

    void setPostePublicationId(String postePublicationId);

    String getPosteDanId();

    void setPosteDanId(String posteDanId);

    String getPosteDanIdAvisRectificatifCE();

    void setPosteDanIdAvisRectificatifCE(String posteDanIdAvisRectificatifCE);

    // /////////////////
    // getter/setter Vecteur publication multiples
    // ////////////////

    /**
     * Getter VecteurPublicationMultiples.
     */
    List<String> getVecteurPublicationMultiples();

    /**
     * Setter VecteurPublicationMultiples
     *
     * @param vecteurPublicationMultiples
     */
    void setVecteurPublicationMultiples(List<String> vecteurPublicationMultiples);

    // /////////////////
    // getter/setter choix direction PM création dossier
    // ////////////////

    /**
     * Getter directionsPM.
     */
    List<String> getDirectionsPM();

    /**
     * Setter directionsPM
     *
     * @param directionsPM
     */
    void setDirectionsPM(List<String> directionsPM);

    List<String> getSignatureCPM();

    void setSignatureCPM(List<String> signatureCPM);

    List<String> getSignatureSGG();

    void setSignatureSGG(List<String> signatureSGG);

    List<String> getTypesActe();

    void setTypesActe(List<String> typesActe);

    List<String> getRetourDAN();

    void setRetourDAN(List<String> retoursDAN);
}
