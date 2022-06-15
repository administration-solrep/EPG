package fr.dila.solonepg.api.documentmodel;

import fr.dila.st.api.domain.STDomainObject;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Liste Traitement Papier Solon EPG.
 * <p>Represente le modèle  "ListeTraitementPapier".</p>
 *
 */
public interface ListeTraitementPapier extends STDomainObject, Serializable {
    ///////////////////
    // getter/setter property
    //////////////////

    /**
     * Getter numeroListe
     *
     * @return String
     */
    Long getNumeroListe();

    void setNumeroListe(Long numeroListe);

    /**
     * Getter identifianst dossiers de la liste
     *
     * @return List<String>
     */
    List<String> getIdsDossier();

    void setIdsDossier(List<String> idsDossier);

    /**
     * Getter type de la Liste : "mise en signature", "demandes d’épreuves" ou "demandes de publication"
     *
     * @return String
     */
    String getTypeListe();

    void setTypeListe(String typeListe);

    /**
     * Getter type Signature : "SGG", "PM" ou "PR"
     *
     * @return String
     */
    String getTypeSignature();

    void setTypeSignature(String typeSignature);

    /**
     * Getter date de Generation de la Liste
     *
     * @return Calendar
     */
    Calendar getDateGenerationListe();

    void setDateGenerationListe(Calendar dateGenerationListe);
}
