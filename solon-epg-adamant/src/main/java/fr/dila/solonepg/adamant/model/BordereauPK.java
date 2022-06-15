package fr.dila.solonepg.adamant.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BordereauPK implements Serializable {
    private static final long serialVersionUID = -2809713986741670683L;

    @Column(name = "ID_DOSSIER")
    protected String idDossier;

    @Column(name = "ID_EXTRACTION_LOT")
    protected Integer idLot;

    public BordereauPK() {}

    public BordereauPK(String idDossier, Integer idLot) {
        this.idDossier = idDossier;
        this.idLot = idLot;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public Integer getIdLot() {
        return idLot;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idDossier == null) ? 0 : idDossier.hashCode());
        result = prime * result + ((idLot == null) ? 0 : idLot.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings({ "squid:S1142", "squid:S00121" })
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BordereauPK other = (BordereauPK) obj;
        if (idDossier == null) {
            if (other.idDossier != null) {
                return false;
            }
        } else if (!idDossier.equals(other.idDossier)) {
            return false;
        }
        if (idLot == null) {
            if (other.idLot != null) {
                return false;
            }
        } else if (!idLot.equals(other.idLot)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BordereauPK [idDossier=" + idDossier + ", idLot=" + idLot + "]";
    }
}
