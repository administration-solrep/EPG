package fr.dila.solonepg.api.feuilleroute;

import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import java.io.Serializable;

/**
 * Interface des feuilles de route de SOLON EPG.
 *
 * @author jtremeaux
 */
public interface SolonEpgFeuilleRoute extends SSFeuilleRoute, Serializable {
    /**
     * Retourne l'identifiant technique de la direction.
     *
     * @return Identifiant technique de la direction
     */
    String getDirection();

    /**
     * Renseigne l'identifiant technique de la direction.
     *
     * @param direction
     *            Identifiant technique de la direction
     */
    void setDirection(String direction);

    /**
     * Retourne l'identifiant technique du type d'acte.
     *
     * @return Identifiant technique du type d'acte
     */
    String getTypeActe();

    /**
     * Renseigne l'identifiant technique du type d'acte.
     *
     * @param typeActe
     *            Identifiant technique du type d'acte
     */
    void setTypeActe(String typeActe);

    /**
     * Retourne le numéro de la feuille de route.
     *
     * @return Numéro de le feuille de route
     */
    Long getNumero();

    /**
     * Renseigne le numéro de la feuille de route.
     *
     * @param Numéro
     *            de le feuille de route
     */
    void setNumero(Long numero);

    /**
     * Retourne vrai si cette feuille de route est par défaut globalement.
     *
     * @return Feuille de route par défaut globalement
     */
    boolean isFeuilleRouteDefautGlobal();

    boolean isSqueletteFeuilleRoute();
}
