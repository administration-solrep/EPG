package fr.dila.solonepg.ui.enums;

public enum EpgListTraitementDateEnum {
    DATE_RETOUR_PM("dateRetourPM", "suivi.detailListe.modale.traitementSerie.dateRetourPM"),
    DATE_RETOUR_SGG("dateRetourSGG", "suivi.detailListe.modale.traitementSerie.dateRetourSGG"),
    DATE_ENVOI_PR("dateEnvoiPR", "suivi.detailListe.modale.traitementSerie.dateEnvoiPR"),
    DATE_RETOUR_PR("dateRetourPR", "suivi.detailListe.modale.traitementSerie.dateRetourPR"),
    DATE_DEMANDE_EPREUVE("dateDemandeEpreuve", "suivi.detailListe.modale.traitementSerie.dateDemandeEpreuve");

    private final String type;
    private final String label;

    EpgListTraitementDateEnum(String type, String label) {
        this.type = type;
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }
}
