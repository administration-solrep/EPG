package fr.dila.solonmgpp.api.node;

import fr.sword.xsd.solon.epp.Ministere;

/**
 * Interface des objets métiers ministère.
 * 
 * @author jtremeaux
 */
public interface MinistereNode extends TableReferenceEppNode {
    
  /**
     * Retourne l'identifiant technique de l'enregistrement
     * @return
     */
    String getIdentifiant();
    
    
    /**
     * Renseigne l'identifiant technique de l'enregistrement
     * @param identifiant
     */
    void setIdentifiant(String identifiant);
    
    /**
     * Retourne le nom du ministere
     * @return le nom du ministere
     */
    String getNom();

    /**
     * Renseigne le nom du ministere
     * @param nom le nom du ministere
     */
    void setNom(String nom);

    /**
     * Retourne le libellé du ministere
     * @return le libellé du ministere
     */
    String getLibelle();

    /**
     * Renseigne le libellé du ministere
     * @param libelleMinistre le libellé du ministere
     */
    void setLibelle(String libelle);

    /**
     * Retourne l'appellation du ministère
     * @return l'appellation du ministère
     */
    String getAppellation();
    
    /**
     * Renseigne l'appellation du ministère
     * @param appellation l'appellation du ministère
     */
    void setAppellation(String appellation);

    /**
     * Retourne l'Edition
     * @return l'édition
     */
    String getEdition();

    /**
     * Renseigne l'édition
     * @param edition l'édition
     */
    void setEdition(String edition);

    /**
     * Retourne l'identifiant du gouvernement du ministere
     * @return l'identifiant du gouvernmeent du ministere
     */
    String getGouvernement();

    /**
     * Renseigne l'identifiant du gouvernement du ministere
     * @param gouvernement l'identifiant du gouvernement du ministere
     */
    void setGouvernement(String gouvernement);
    
    void remapField(Ministere ministere);
    
    Ministere toMinistereXsd();
}
