package fr.dila.solonepg.api.enumeration;

public enum EpgVecteurPublication {
    BODMR("BODMR"),
    DOCUMENTS_ADMINISTRATIFS("Documents administratifs"),
    JOURNAL_OFFICIEL("Journal Officiel"),
    JO_DOCUMENTS_ADMINISTRATIFS("JO + Documents administratifs");

    private final String intitule;

    EpgVecteurPublication(String intitule) {
        this.intitule = intitule;
    }

    public String getIntitule() {
        return intitule;
    }
}
