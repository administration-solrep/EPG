package fr.dila.solonepg.api.documentmodel;

import fr.dila.ss.api.fondDeDossier.SSFondDeDossierFile;
import java.util.Calendar;

/**
 * Interface FileSolonEpg : utilisée pour les fichiers du parapheurs et du fond de dossiers de SOLON EPG.
 *
 * @author ARN
 */
public interface FileSolonEpg extends SSFondDeDossierFile {
    ///////////////////
    // getter/setter property
    //////////////////

    /**
     * récupère l'entité de l'utilisateur ayant effectué le dernier traitement.
     *
     * @return String
     */
    String getEntite();

    /**
     * définit l'entité de l'utilisateur ayant effectué le dernier traitement.
     *
     * @param entiteDernierTraitement
     */
    void setEntite(String entite);

    /**
     * récupère le login de l'utilisateur ayant effectué le dernier traitement.
     *
     * @return String
     */
    String getUserDernierTraitement();

    /**
     * récupère la date du dernier traitement.
     *
     * @return Calendar
     */
    Calendar getDateDernierTraitement();

    /**
     * Met en place un identifiant qui fait référence au dossier.
     * Dénormalisation utilisée pour la recherche.
     * @param idDocument
     */
    void setRelatedDocument(String idDocument);

    /**
     * Retourne l'identificant du dossier.
     * @return
     */
    String getRelatedDocument();

    /**
     * Met en place l'attribut identifiant du fichier pour pouvoir distinguer les fichiers qui présent dans le répertoire acte ou fond de dossier (ou extrait ou autre parapheur)
     * @param filetypeId
     */
    void setFiletypeId(Long filetypeId);

    /**
     * Retourne l'identifiant du fichier
     * @param filetypeId
     */
    Long getFiletypeId();

    boolean isEditing();

    void setEditing(boolean editing);

    void setEditing(boolean editing, boolean disableVersioning, boolean disableDublincoreListener);
}
