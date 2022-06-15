package fr.dila.solonepg.api.parapheur;

import fr.dila.ss.api.tree.SSTreeFolder;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Répertoire d'un Modele de Parapheur Solon EPG
 * <p>Represente le répertoire d'un modèle de  "Parapheur" dans l'espace d'administration.</p>
 *
 * @author arolin
 */
public interface ParapheurFolder extends SSTreeFolder {
    /**
     * Nom de la racine de l'espace d'administration contenant les modèles de Parapheur.
     */
    public static final String SOLONEPG_REPOSITORY_MODEL_PARAPHEUR = "parapheur";

    ///////////////////
    // getter/setter
    //////////////////

    /**
     * Signale si le repertoire de parapheur peut être vide ou non.
     *
     * @return Boolean
     */
    Boolean getEstNonVide();

    /**
     * Définit si le repertoire de parapheur peut être vide ou non.
     *
     * @param isModelesParapheurCreated
     */
    void setEstNonVide(Boolean estVide);

    /**
     * Récupère le nombre maximal de document contenu par le répertoire.
     *
     * @return Long
     */
    Long getNbDocAccepteMax();

    /**
     * Récupère le nombre maximal de document contenu par le répertoire sans vérification de droit.
     *
     * @param session
     *
     * @return Long
     */
    Long getNbDocAccepteMaxUnrestricted(CoreSession session);

    /**
     * Définit le nombre maximal de document contenu par le répertoire.
     *
     * @param nbDocAccepteMax
     */
    void setNbDocAccepteMax(Long nbDocAccepteMax);

    /**
     * Récupère les identifiants des feuilles de styles liées au répertoire.
     *
     * @return List<Map<String, Serializable>>
     */
    List<Map<String, Serializable>> getFeuilleStyleFiles();

    /**
     * Définit les fichiers des feuilles de styles liées au répertoire.
     *
     * @param nbDocAccepteMax
     */
    void setFeuilleStyleFiles(List<Map<String, Serializable>> feuilleStyleFiles);

    /**
     * Getter liste des formats autorisés dans ce répertoire (png, odt, ...)
     *
     * @return List<String> formats autorisés
     */
    List<String> getFormatsAutorises();

    /**
     * Getter liste des formats autorisés dans ce répertoire (png, odt, ...) sans vérification de droit.
     *
     * @param session
     *
     * @return List<String> formats autorisés
     */
    List<String> getFormatsAutorisesUnrestricted(CoreSession session);

    /**
     * Setter liste des formats autorisés dans ce répertoire  (png, odt, ...)
     *
     * @param formatsAutorises formats Autorises
     */
    void setFormatsAutorises(List<String> formatsAutorises);

    boolean isFull();

    void setIsFull(boolean isFull);
}
