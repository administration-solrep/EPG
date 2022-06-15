package fr.dila.solonepg.core.dossier.numeronor;

import fr.dila.solonepg.api.dossier.NumeroNorInfo;

public class NumeroNorInfoImpl implements NumeroNorInfo {
    protected String numeroNor;

    public static final int COMPTEUR_LENGTH = 5;

    public NumeroNorInfoImpl(final String numeroNor) {
        this.numeroNor = numeroNor;
    }

    @Override
    public String getNumeroNor() {
        return numeroNor;
    }

    @Override
    public String getMinistere() {
        return this.numeroNor.substring(0, 3);
    }

    @Override
    public String getDirection() {
        return this.numeroNor.substring(3, 4);
    }

    @Override
    public int getAnnee() {
        return new Integer(this.numeroNor.substring(4, 6)).intValue();
    }

    @Override
    public int getCompteur() {
        return new Integer(this.numeroNor.substring(6, 11)).intValue();
    }

    @Override
    public String getActe() {
        return this.numeroNor.substring(this.numeroNor.length() - 1);
    }

    @Override
    public NumeroNorInfo reglerCompteur(int compteur) {
        String strCompteur = Integer.valueOf(compteur).toString();
        int add = COMPTEUR_LENGTH - strCompteur.length();
        StringBuilder newCompteurBuilder = new StringBuilder();
        for (int i = 0; i < add; i++) {
            newCompteurBuilder.append("0");
        }
        newCompteurBuilder.append(compteur);
        this.numeroNor =
            this.getMinistere() +
            this.getDirection() +
            this.getAnnee() +
            newCompteurBuilder.toString() +
            this.getActe();

        return this;
    }
}
