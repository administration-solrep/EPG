package fr.dila.solonepg.ui.bean;

public class EpgSqueletteActionDTO {
    private boolean isEtatValidee;

    private boolean isEtatBrouillon;

    private Boolean isLock;

    private Boolean isLockByCurrentUser;

    public EpgSqueletteActionDTO() {}

    public boolean getIsEtatValidee() {
        return isEtatValidee;
    }

    public void setEtatValidee(boolean isEtatValidee) {
        this.isEtatValidee = isEtatValidee;
    }

    public boolean getIsEtatBrouillon() {
        return isEtatBrouillon;
    }

    public void setEtatBrouillon(boolean isEtatBrouillon) {
        this.isEtatBrouillon = isEtatBrouillon;
    }

    public Boolean getIsLock() {
        return isLock;
    }

    public void setIsLock(Boolean isLock) {
        this.isLock = isLock;
    }

    public Boolean getIsLockByCurrentUser() {
        return isLockByCurrentUser;
    }

    public void setIsLockByCurrentUser(Boolean isLockByCurrentUser) {
        this.isLockByCurrentUser = isLockByCurrentUser;
    }
}
