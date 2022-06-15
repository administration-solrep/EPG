package fr.dila.solonepg.core.migration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "DossierSupprime")
@Table(name = "ID_NOR_DOSSIERS_SUPPRIMES")
public class TableDossierSupprime {
    @Id
    @Column(name = "ID", nullable = false)
    private String id;

    @Column(name = "NOR")
    private String nor;

    public TableDossierSupprime() {
        this(null, null);
    }

    public TableDossierSupprime(String id, String nor) {
        super();
        this.id = id;
        this.nor = nor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }
}
