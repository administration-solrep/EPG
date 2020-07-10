package fr.dila.solonepg.api.criteria;

/**
 * Critère de recherche des feuilles de routes de SOLON EPG.
 * 
 * @author jtremeaux
 */
public class FeuilleRouteCriteria extends fr.dila.ss.api.criteria.FeuilleRouteCriteria {
    /**
     * Numéro.
     */
    private String numero;
    
    /**
     * Type d'acte.
     */
    private String typeActe;
    
    /**
     * Type d'acte non défini.
     */
    private boolean typeActeNull;
    
    /**
     * Getter de numero.
     *
     * @return numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Setter de numero.
     *
     * @param numero numero
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * Getter de typeActe.
     *
     * @return typeActe
     */
    public String getTypeActe() {
        return typeActe;
    }

    /**
     * Setter de typeActe.
     *
     * @param typeActe typeActe
     */
    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }

    /**
     * Getter de typeActeNull.
     *
     * @return typeActeNull
     */
    public boolean getTypeActeNull() {
        return typeActeNull;
    }

    /**
     * Setter de typeActeNull.
     *
     * @param typeActeNull typeActeNull
     */
    public void setTypeActeNull(boolean typeActeNull) {
        this.typeActeNull = typeActeNull;
    }
}
