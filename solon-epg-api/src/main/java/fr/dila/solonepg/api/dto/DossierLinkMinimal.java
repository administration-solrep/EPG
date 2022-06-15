package fr.dila.solonepg.api.dto;

import fr.dila.ss.api.recherche.IdLabel;
import java.util.Calendar;

public class DossierLinkMinimal {
    private boolean retourPourModification;
    private String dossierId;
    private IdLabel idLabel;
    private Calendar dateCreation;

    public DossierLinkMinimal(String dossierId, IdLabel idLabel, boolean retourPourModification) {
        this.setDossierId(dossierId);
        this.setIdLabel(idLabel);
        this.setRetourPourModification(retourPourModification);
    }

    public DossierLinkMinimal(
        String dossierId,
        IdLabel idLabel,
        boolean retourPourModification,
        Calendar dateCreation
    ) {
        this(dossierId, idLabel, retourPourModification);
        this.dateCreation = dateCreation;
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

    public boolean getRetourPourModification() {
        return retourPourModification;
    }

    public void setDateCreation(Calendar dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Calendar getDateCreation() {
        return dateCreation;
    }
}
