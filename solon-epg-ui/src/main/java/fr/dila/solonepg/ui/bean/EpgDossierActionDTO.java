package fr.dila.solonepg.ui.bean;

public class EpgDossierActionDTO {
    private boolean canUserUpdateParapheur;
    private boolean canUserUpdateFdd;
    private boolean canExecuteStep;
    private boolean isDossierDeleted;
    private boolean isUrgent;
    private boolean isMesureNominative;
    private boolean hasTypeActeMesureNominative;
    private boolean isTexteSignale;
    private boolean canSendSaisineOrPieceComplementaire;
    private boolean hasSignataire;
    private boolean isDossierAbandonne;
    private boolean solonEditAvailable;

    public EpgDossierActionDTO() {
        // Default constructor
    }

    public boolean getCanUserUpdateParapheur() {
        return canUserUpdateParapheur;
    }

    public void setCanUserUpdateParapheur(boolean canUserUpdateParapheur) {
        this.canUserUpdateParapheur = canUserUpdateParapheur;
    }

    public boolean getCanUserUpdateFdd() {
        return canUserUpdateFdd;
    }

    public void setCanUserUpdateFdd(boolean canUserUpdateFdd) {
        this.canUserUpdateFdd = canUserUpdateFdd;
    }

    public boolean getIsDossierDeleted() {
        return isDossierDeleted;
    }

    public void setIsDossierDeleted(boolean isDossierDeleted) {
        this.isDossierDeleted = isDossierDeleted;
    }

    public boolean getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(boolean isUrgent) {
        this.isUrgent = isUrgent;
    }

    public boolean getIsMesureNominative() {
        return isMesureNominative;
    }

    public void setIsMesureNominative(boolean isMesureNominative) {
        this.isMesureNominative = isMesureNominative;
    }

    public boolean isCanExecuteStep() {
        return canExecuteStep;
    }

    public void setCanExecuteStep(boolean canExecuteStep) {
        this.canExecuteStep = canExecuteStep;
    }

    public boolean getHasTypeActeMesureNominative() {
        return hasTypeActeMesureNominative;
    }

    public void setHasTypeActeMesureNominative(boolean hasTypeActeMesureNominative) {
        this.hasTypeActeMesureNominative = hasTypeActeMesureNominative;
    }

    public boolean getIsTexteSignale() {
        return isTexteSignale;
    }

    public void setIsTexteSignale(boolean isTexteSignale) {
        this.isTexteSignale = isTexteSignale;
    }

    public boolean isCanSendSaisineOrPieceComplementaire() {
        return canSendSaisineOrPieceComplementaire;
    }

    public void setCanSendSaisineOrPieceComplementaire(boolean canSendSaisineOrPieceComplementaire) {
        this.canSendSaisineOrPieceComplementaire = canSendSaisineOrPieceComplementaire;
    }

    public boolean getHasSignataire() {
        return hasSignataire;
    }

    public void setHasSignataire(boolean hasSignataire) {
        this.hasSignataire = hasSignataire;
    }

    public boolean getIsDossierAbandonne() {
        return isDossierAbandonne;
    }

    public void setIsDossierAbandonne(boolean isDossierAbandonne) {
        this.isDossierAbandonne = isDossierAbandonne;
    }

    public boolean getSolonEditAvailable() {
        return solonEditAvailable;
    }

    public void setSolonEditAvailable(boolean solonEditAvailable) {
        this.solonEditAvailable = solonEditAvailable;
    }
}
