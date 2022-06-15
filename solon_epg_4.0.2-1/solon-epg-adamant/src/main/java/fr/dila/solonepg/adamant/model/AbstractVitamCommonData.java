package fr.dila.solonepg.adamant.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractVitamCommonData extends AbstractAdamantCommonData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false, columnDefinition = "integer")
    private Integer id;

    /**
     * Numéro nor du dossier
     */
    @Column(name = "NUMERONOR")
    private String nor;

    /**
     * Statut de livraison VITAM
     */
    @Column(name = "STATUT_LIVRAISON")
    private String statutLivraison;

    /**
     * Date de livraison VITAM
     */
    @Column(name = "DATE_LIVRAISON")
    private Date dateLivraison;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public String getStatutLivraison() {
        return statutLivraison;
    }

    public void setStatutLivraison(String statutLivraison) {
        this.statutLivraison = statutLivraison;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }
}
