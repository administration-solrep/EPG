package fr.dila.solonepg.api.parapheur;

import fr.dila.st.api.domain.STDomainObject;
import java.io.Serializable;

/**
 * Racine des Modeles de Parapheur Solon EPG
 * <p>Represente la racine des modèles de  "Parapheur" dans l'espace d'administration.</p>
 *
 * @author arolin
 */
public interface ParapheurModelRoot extends STDomainObject, Serializable {
    /**
     * Nom de la racine de l'espace d'administration contenant les modèles de Parapheur.
     */
    public static final String SOLONEPG_REPOSITORY_MODEL_PARAPHEUR = "parapheur";
    ///////////////////
    // getter/setter
    //////////////////

}
