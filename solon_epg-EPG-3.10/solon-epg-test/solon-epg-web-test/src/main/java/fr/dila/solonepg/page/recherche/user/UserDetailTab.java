package fr.dila.solonepg.page.recherche.user;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class UserDetailTab extends EPGWebPage {

    public static final String ENREGISTRER_BTN_ID = "editUser:saveUserButton";

    public void enregistrer() {
        this.getElementById(ENREGISTRER_BTN_ID).click();
        sleep(1);
    }
}
