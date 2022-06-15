package fr.dila.solonepg.api.parapheur;

/**
 * MOdèle Parapheur Solon EPG.
 * <p>Represente le modèle  "Parapheur" dans l'espace d'administration.</p>
 *
 */
public interface ParapheurModel extends Parapheur {
    /**
     * Nom du workspace de l'espace d'administration contenant les modèles de parapheur.
     */
    public static final String SOLONEPG_REPOSITORY_MODEL_PARAPHEUR = "parapheur";

    ///////////////////
    // Method
    //////////////////

    /**
     * Getter  typeActe : (au moins 34 type d'actes posssible : amnsitie, arrêté, ordonnace)
     *
     * @return String
     */
    String getTypeActe();

    void setTypeActe(String typeActe);
}
