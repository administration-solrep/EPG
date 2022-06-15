package fr.dila.solonepg.adamant.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractAdamantCommonData {
    /**
     * Nom complet du fichier SIP
     */
    @Column(name = "NOM_SIP")
    private String nomSip;

    /**
     * Numéro du paquet.
     */
    @Column(name = "ID_PAQUET")
    private String idPaquet;

    /**
     * Empreinte du SIP calculée d'après l'algorithme SHA-512.
     */
    @Column(name = "EMPREINTE")
    private String empreinte;

    /**
     * Poids du SIP en octets.
     */
    @Column(name = "POIDS")
    private Long poids;

    /**
     * Type d'acte en toutes lettres.
     */
    @Column(name = "TYPE_ACTE")
    private String typeActe;

    /**
     * Statut du dossier.
     */
    @Column(name = "STATUT")
    private String statut;

    /**
     * Version de SOLON dans laquelle le dossier a été généré.
     */
    @Column(name = "VERSION")
    private String version;

    /**
     * Statut d'archivage après livraison
     */
    @Column(name = "STATUT_ARCHIVAGE_AFTER")
    private String statutArchivageAfter;

    public String getNomSip() {
        return nomSip;
    }

    public void setNomSip(String nomSip) {
        this.nomSip = nomSip;
    }

    public String getIdPaquet() {
        return idPaquet;
    }

    public void setIdPaquet(String idPaquet) {
        this.idPaquet = idPaquet;
    }

    public String getEmpreinte() {
        return empreinte;
    }

    public void setEmpreinte(String empreinte) {
        this.empreinte = empreinte;
    }

    public Long getPoids() {
        return poids;
    }

    public void setPoids(Long poids) {
        this.poids = poids;
    }

    public String getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatutArchivageAfter() {
        return statutArchivageAfter;
    }

    public void setStatutArchivageAfter(String statutArchivageAfter) {
        this.statutArchivageAfter = statutArchivageAfter;
    }
}
