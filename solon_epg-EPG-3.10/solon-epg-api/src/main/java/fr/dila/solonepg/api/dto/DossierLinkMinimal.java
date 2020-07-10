package fr.dila.solonepg.api.dto;

public class DossierLinkMinimal {

    private Boolean retourPourModification;
    private String dossierId;
    private IdLabel idLabel;

    public DossierLinkMinimal(String dossierId, IdLabel idLabel, Boolean retourPourModification) {
        this.setDossierId(dossierId);
        this.setIdLabel(idLabel);
        this.setRetourPourModification(retourPourModification);
    }

    public void setIdLabel(IdLabel idLabel) {
        this.idLabel = idLabel;
    }

    public IdLabel getIdLabel() {
        return idLabel;
    }

    public void setDossierId(String dossierId) {
        this.dossierId = dossierId;
    }

    public String getDossierId() {
        return dossierId;
    }

    public void setRetourPourModification(Boolean retourPourModification) {
        this.retourPourModification = retourPourModification;
    }

    public Boolean getRetourPourModification() {
        return retourPourModification;
    }

}
