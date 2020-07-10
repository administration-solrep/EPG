package fr.dila.solonepg.page.recherche;

import java.util.List;

import org.openqa.selenium.WebElement;

import fr.dila.solonepg.page.commun.EPGWebPage;

/**
 * s
 * 
 * @author admin
 */
public class RechercheUtilisateurPage extends EPGWebPage {

    private static final String USER_IDENTIFIANT_ID = "searchUserForm:nxl_recherche_utilisateur:nxw_username";

    private static final String VALIDATE_RECHERCHE_BUTTON = "//input[@id='searchUserForm:validate']";

    public static final String REINITIALISER_RECHECRCHE_BOUTTON_PATH = "//input[@value='Réinitialiser la recherche']";

    public static final String DIRECTION = "searchUserForm:nxl_recherche_utilisateur:nxw_poste_findButton";

    public void setIdentifiant(final String username) {
        final WebElement elem = getElementById(USER_IDENTIFIANT_ID);
        fillField("Identifiant", elem, username);
    }

    public CommonRechercheUtilisateurPage rechercher() {
        final WebElement elem = getElementByXpath(VALIDATE_RECHERCHE_BUTTON);
        CommonRechercheUtilisateurPage page = clickToPage(elem, CommonRechercheUtilisateurPage.class);
        waitForPageSourcePart("Résultats de la recherche", TIMEOUT_IN_SECONDS);
        return page;
    }

    public RechercheUtilisateurPage initialiserRecherche() {
        final WebElement elem = getElementByXpath(REINITIALISER_RECHECRCHE_BOUTTON_PATH);
        RechercheUtilisateurPage page = clickToPage(elem, RechercheUtilisateurPage.class);
        waitForPageLoaded(getDriver());
        waitForPageSourcePart("Identifiant", TIMEOUT_IN_SECONDS);
        return page;
    }

    public void setDirection(List<String> ar) {
        sleep(2);
        selectDestinataireFromOrganigramme(ar, DIRECTION);
    }
}
