package fr.dila.solonmgpp.api.enumeration;

public enum CorbeilleTypeObjet {

    DOSSIER, MESSAGE, OEP, AVI, FICHE_LOI, FICHE_DR, FICHE_DR_67, FICHE_DECRET, FICHE_IE, FICHE_341, FICHE_DPG, FICHE_SD, FICHE_JSS, FICHE_DOC, FICHE_AUD;

    public static CorbeilleTypeObjet findByName(String type) {
        for (CorbeilleTypeObjet corbeilleTypeObjet : values()) {
            if (corbeilleTypeObjet.toString().equals(type)) {
                return corbeilleTypeObjet;
            }
        }
        return MESSAGE;
    }
}
