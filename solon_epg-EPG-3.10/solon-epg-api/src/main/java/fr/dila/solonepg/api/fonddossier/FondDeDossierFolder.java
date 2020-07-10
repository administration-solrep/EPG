package fr.dila.solonepg.api.fonddossier;

import fr.dila.ss.api.fondDeDossier.SSFondDeDossierFolder;

/**
 * Interface d'un Répertoire d'un fond de dossier Solon EPG
 * 
 */
public interface FondDeDossierFolder extends SSFondDeDossierFolder {

    /**
     * Signale si le repertoire de fond de dossier peut être supprimé ou non.
     * 
     * @return Boolean
     */
    Boolean isDeletable();

    /**
     * Définit si le repertoire de fond de dossier peut être supprimé ou non.
     * 
     * @param 
     */
    void setIsDeletable(Boolean estSupprimable);
    
    /**
     * Signale si le repertoire de fond de dossier peut être supprimé ou non.
     * 
     * @return String
     */
    String isDeletableStr();
}
