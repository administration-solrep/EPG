package fr.dila.solonepg.ui.bean;

import java.util.Calendar;

public class SaisineRectificativeDTO {

    public SaisineRectificativeDTO() {
        super();
    }

    private String fichier;
    private String auteur;
    private String version;
    private Calendar date;
    private String repertoire;

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getRepertoire() {
        return repertoire;
    }

    public void setRepertoire(String repertoire) {
        this.repertoire = repertoire;
    }
}
