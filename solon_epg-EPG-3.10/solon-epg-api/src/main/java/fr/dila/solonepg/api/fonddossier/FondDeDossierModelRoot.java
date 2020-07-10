package fr.dila.solonepg.api.fonddossier;

import java.io.Serializable;
import java.util.List;

import fr.dila.st.api.domain.STDomainObject;

/**
 * Racine des Modeles de Fond De Dossier  Solon EPG
 * <p>Represente la racine des modèles de  "fond de dossier" dans l'espace d'administration.</p>
 * 
 * @author arolin
 */
public interface FondDeDossierModelRoot extends STDomainObject, Serializable {

    /**
     * Nom de la racine de l'espace d'administration contenant les modèles de fond de dossier.
     */
    public static final String SOLONEPG_REPOSITORY_MODEL_FDD = "fond-dossier";
    
    
    ///////////////////
    // getter/setter
    //////////////////
    
    /**
     * Getter liste des formats autorisés dans le fond de dossier (png, odt, ...)
     * 
     * @return List<String> formats autorisés
     */
    List<String> getFormatsAutorises();

    /**
     * Setter liste des formats autorisés dans le fond de dossier (png, odt, ...)
     * 
     * @param formatsAutorises formats Autorises
     */
    void setFormatsAutorises(List<String> formatsAutorises);
}
