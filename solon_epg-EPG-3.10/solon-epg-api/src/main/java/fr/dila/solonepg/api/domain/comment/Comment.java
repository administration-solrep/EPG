package fr.dila.solonepg.api.domain.comment;

import fr.dila.st.api.domain.comment.STComment;

/**
 * Interface de l'objet métier commentaire de SOLON EPG.
 * 
 * @author jtremeaux
 */
public interface Comment extends STComment {
    /**
     * Retourne l'identifiant technique du ministère qui peut voir ce commentaire.
     * 
     * @return Identifiant technique du ministère
     */
    public String getVisibleToMinistereId();

    /**
     * Renseigne l'identifiant technique du ministère qui peut voir ce commentaire.
     * 
     * @param visibleToMinistereId Identifiant technique du ministère
     */
    public void setVisibleToMinistereId(String visibleToMinistereId);

    /**
     * Retourne l'identifiant technique de l'unité structurelle qui peut voir ce commentaire.
     * 
     * @return Identifiant technique de l'unité structurelle
     */
    public String getVisibleToUniteStructurelleId();

    /**
     * Renseigne l'identifiant technique de l'unité structurelle qui peut voir ce commentaire.
     * 
     * @param visibleToUniteStructurelleId Identifiant technique de l'unité structurelle
     */
    public void setVisibleToUniteStructurelleId(String visibleToUniteStructurelleId);

    /**
     * Retourne l'identifiant technique du poste qui peut voir ce commentaire.
     * 
     * @return Identifiant technique du poste
     */
    public String getVisibleToPosteId();

    /**
     * Renseigne l'identifiant technique du poste qui peut voir ce commentaire.
     * 
     * @param visibleToPosteId Identifiant technique du poste
     */
    public void setVisibleToPosteId(String visibleToPosteId);

    /**
     * Retourn la visibilité
     * 
     * @return Identifiant technique + id
     */
    public String getVisiblity();

    /**
     * Renseigne l'identifiant technique du poste qui peut voir ce commentaire.
     * 
     * @param visibleToPosteId Identifiant technique + id
     */
    public void setVisibility(String visibility);
}
