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
    String getVisibleToMinistereId();

    /**
     * Renseigne l'identifiant technique du ministère qui peut voir ce commentaire.
     *
     * @param visibleToMinistereId Identifiant technique du ministère
     */
    void setVisibleToMinistereId(String visibleToMinistereId);

    /**
     * Retourne l'identifiant technique de l'unité structurelle qui peut voir ce commentaire.
     *
     * @return Identifiant technique de l'unité structurelle
     */
    String getVisibleToUniteStructurelleId();

    /**
     * Renseigne l'identifiant technique de l'unité structurelle qui peut voir ce commentaire.
     *
     * @param visibleToUniteStructurelleId Identifiant technique de l'unité structurelle
     */
    void setVisibleToUniteStructurelleId(String visibleToUniteStructurelleId);

    /**
     * Retourne l'identifiant technique du poste qui peut voir ce commentaire.
     *
     * @return Identifiant technique du poste
     */
    String getVisibleToPosteId();

    /**
     * Renseigne l'identifiant technique du poste qui peut voir ce commentaire.
     *
     * @param visibleToPosteId Identifiant technique du poste
     */
    void setVisibleToPosteId(String visibleToPosteId);

    /**
     * Retourn la visibilité
     *
     * @return Identifiant technique + id
     */
    String getVisiblity();

    /**
     * Renseigne l'identifiant technique du poste qui peut voir ce commentaire.
     *
     * @param visibleToPosteId Identifiant technique + id
     */
    void setVisibility(String visibility);

    /**
     * Retourn le type de visibilité
     *
     * @return Préfix
     */
    String getTypeVisiblity();
}
