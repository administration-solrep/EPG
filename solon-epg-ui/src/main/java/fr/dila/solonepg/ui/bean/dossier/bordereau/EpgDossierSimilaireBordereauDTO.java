package fr.dila.solonepg.ui.bean.dossier.bordereau;

public class EpgDossierSimilaireBordereauDTO extends EpgBordereauDTO {

    public EpgDossierSimilaireBordereauDTO() {
        super();
        setIsCopy(true);
    }

    private boolean hasNextDossier;

    private boolean hasPreviousDossier;

    public boolean getHasNextDossier() {
        return hasNextDossier;
    }

    public void setHasNextDossier(boolean hasNextDossier) {
        this.hasNextDossier = hasNextDossier;
    }

    public boolean getHasPreviousDossier() {
        return hasPreviousDossier;
    }

    public void setHasPreviousDossier(boolean hasPreviousDossier) {
        this.hasPreviousDossier = hasPreviousDossier;
    }
}
