package fr.dila.solonepg.ui.bean.dossier.similaire;

public class DossierSimilaireDTO {

    public DossierSimilaireDTO() {}

    private String titre;

    private String nor;

    private String statut;

    private String auteur;

    private int indexCourant;

    private String idDossier;

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getIndexCourant() {
        return indexCourant;
    }

    public void setIndexCourant(int indexCourant) {
        this.indexCourant = indexCourant;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }
}
