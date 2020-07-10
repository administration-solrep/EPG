package fr.dila.solonepg.api.fonddossier;

/**
 * Modele Fond De Dossier  Solon EPG
 * <p>Represente le modèle  "fond de dossier" dans l'espace d'administration.</p>
 * 
 * @author arolin
 */
public interface FondDeDossierModel extends FondDeDossier {

    /**
     * Nom du répertoire de l'espace d'administration contenant les modèles de fond de dossier.
     */
    public static final String SOLONEPG_REPOSITORY_MODEL_FDD = "fond-dossier";
    
    
    ///////////////////
    // getter/setter
    //////////////////
    
    /**
     * Getter  typeActe : (au moins 34 type d'actes possible : amnsitie, arrêté, ordonnace)
     * 
     * @return String
     */
    String getTypeActe();

    void setTypeActe(String typeActe); 
}
