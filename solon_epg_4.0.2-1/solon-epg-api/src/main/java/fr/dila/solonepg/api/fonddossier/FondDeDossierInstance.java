package fr.dila.solonepg.api.fonddossier;

import java.util.List;

/**
 * Instance Fond De Dossier Solon EPG
 * <p>Represente l'instance du "fond de dossier" contenu par le dossier.</p>
 *
 * @author arolin
 */
public interface FondDeDossierInstance extends FondDeDossier {
    /**
     * Getter liste des formats autorisés dans ce répertoire (png, odt, ...)
     *
     * @return List<String> formats autorisés
     */
    List<String> getFormatsAutorises();

    /**
     * Setter liste des formats autorisés dans ce répertoire  (png, odt, ...)
     *
     * @param formatsAutorises formats Autorises
     */
    void setFormatsAutorises(List<String> formatsAutorises);
}
