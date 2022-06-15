package fr.dila.solonepg.api.recherche.dossier;

import fr.dila.st.api.domain.STDomainObject;
import java.io.Serializable;

/**
 *
 * Une requête de dossier est l'objet qui contient les données
 * communes aux requêtes de dossier simple et expert.
 *
 * @author jgomez
 *
 */

public interface RequeteDossier extends STDomainObject, Serializable {
    /**
     * Si vrai, la recherche est effectuée dans la base de production
     * @param dansBaseProduction
     */
    public void setDansBaseProduction(Boolean dansBaseProduction);

    /**
     * Retourne un booléen indiquant si la recherche est effectué dans la base de production
     */
    public Boolean getDansBaseProduction();

    /**
     * Si vrai, la recherhe est effectuée dans la base d'archivage
     * @param dansBaseArchivageIntermediaire
     */
    public void setDansBaseArchivageIntermediaire(Boolean dansBaseArchivageIntermediaire);

    /**
     * Retourne un booléen indiquant si la recherche est effectué dans la base d'archivage intermédiaire
     */
    public Boolean getDansBaseArchivageIntermediaire();

    /**
     * Retourn vrai si au moins une des bases est sélectionnée (base de production ou base d'archivage intermédiaire).
     * @return
     */
    public boolean hasBasesSelected();

    /**
     * Si sensibiliteCasse est à vrai, la recherche est effectuée avec la sensiblité à la casse
     * pour les méta-données, sinon non.
     * @param sensibiliteCasse
     */
    public void setSensibiliteCasse(Boolean sensibiliteCasse);

    /**
     * Retourne un booléen indiquant si la sensibilité à la casse est activée
     */
    public Boolean getSensibiliteCasse();
}
