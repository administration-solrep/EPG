package fr.dila.solonmgpp.web.content;

public class NotificationDTO {

    private boolean isCorbeilleModified = false;

    private boolean isEvenementModified = false;

    public boolean isEvenementModified() {
        return isEvenementModified;
    }

    public void setEvenementModified(boolean isEvenementModified) {
        this.isEvenementModified = isEvenementModified;
    }

    public boolean isCorbeilleModified() {
        return isCorbeilleModified;
    }

    public void setCorbeilleModified(boolean isCorbeilleModified) {
        this.isCorbeilleModified = isCorbeilleModified;
    }
}
