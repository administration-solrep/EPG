package fr.dila.solonepg.core.dto.activitenormative.injectionbdj;

import java.io.Serializable;

/**
 * Décret d'application unitaire à envoyer au BDJ (élément Decret).
 *
 * @author tlombard
 */
public class DecretApplicatifDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nor;

    private String titreLong;

    public DecretApplicatifDTO() {
        super();
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public String getTitreLong() {
        return titreLong;
    }

    public void setTitreLong(String titreLong) {
        this.titreLong = titreLong;
    }
}
