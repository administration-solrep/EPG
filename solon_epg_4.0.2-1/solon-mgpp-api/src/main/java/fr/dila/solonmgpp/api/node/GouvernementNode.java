package fr.dila.solonmgpp.api.node;

import fr.sword.xsd.solon.epp.Gouvernement;

/**
 * Interface des objets métiers gouvernement.
 *
 * @author jtremeaux
 */
public interface GouvernementNode extends TableReferenceEppNode {
    /**
     * Retourne l'identifiant technique de l'enregistrement
     * @return
     */
    String getIdentifiant();

    /**
     * Renseigne l'identifiant technique de l'enregistrement
     * @param id
     */
    void setIdentifiant(String id);

    /**
     * Retourne l'appellation du gouvernement
     * @return l'appellation du gouvernement
     */
    String getAppellation();

    /**
     * Renseigne l'appellation du gouvernement
     * @param appellation Appellation du gouvernement
     */
    void setAppellation(String appellation);

    void remapField(Gouvernement gouvernement);

    Gouvernement toGouvernementXsd();
}
