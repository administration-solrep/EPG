package fr.dila.solonepg.elastic.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;

public class ElasticParutionBo implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String DATE_PARUTION_BO = "dateParutionBo";
    public static final String NUMERO_TEXTE_PARUTION_BO = "numeroTexteParutionBo";

    @JsonProperty(DATE_PARUTION_BO)
    private Date dateParutionBo;

    @JsonProperty(NUMERO_TEXTE_PARUTION_BO)
    private String numeroTexteParutionBo;

    /**
     * @return the dateParutionBo
     */
    public Date getDateParutionBo() {
        return dateParutionBo;
    }

    /**
     * @param dateParutionBo
     *            the dateParutionBo to set
     */
    public void setDateParutionBo(Date dateParutionBo) {
        this.dateParutionBo = dateParutionBo;
    }

    /**
     * @return the numeroTexteParutionBo
     */
    public String getNumeroTexteParutionBo() {
        return numeroTexteParutionBo;
    }

    /**
     * @param numeroTexteParutionBo
     *            the numeroTexteParutionBo to set
     */
    public void setNumeroTexteParutionBo(String numeroTexteParutionBo) {
        this.numeroTexteParutionBo = numeroTexteParutionBo;
    }
}
