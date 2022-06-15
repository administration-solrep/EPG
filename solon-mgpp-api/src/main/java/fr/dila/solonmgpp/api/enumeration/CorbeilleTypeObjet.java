package fr.dila.solonmgpp.api.enumeration;

public enum CorbeilleTypeObjet {
    DOSSIER(null),
    MESSAGE(null),
    OEP("FichePresentationOEP"),
    AVI("FichePresentationAVI"),
    FICHE_LOI("FicheLoi"),
    FICHE_DR("FichePresentationDR"),
    FICHE_DR_67("FichePresentationDR"),
    FICHE_DECRET("FichePresentationDecret"),
    FICHE_IE("FichePresentationIE"),
    FICHE_341("FichePresentation341"),
    FICHE_DPG("FichePresentationDPG"),
    FICHE_SD("FichePresentationSD"),
    FICHE_JSS("FichePresentationJSS"),
    FICHE_DOC("FichePresentationDOC"),
    FICHE_AUD("FichePresentationAUD");

    private final String docTypeName;

    CorbeilleTypeObjet(String docTypeName) {
        this.docTypeName = docTypeName;
    }

    public static CorbeilleTypeObjet findByName(String type) {
        for (CorbeilleTypeObjet corbeilleTypeObjet : values()) {
            if (corbeilleTypeObjet.toString().equals(type)) {
                return corbeilleTypeObjet;
            }
        }
        return MESSAGE;
    }

    public String getDocTypeName() {
        return docTypeName;
    }
}
