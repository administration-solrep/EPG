package fr.dila.solonepg.page.recherche;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.dila.solonepg.page.recherche.dernierresultat.DernierFeuilleDeRouteResultat;

public class RechercheModeleFeuilleDeRoutePage extends EPGWebPage {

    public static final String INTITULE_ID = "searchModeleFeuilleRouteForm:nxl_recherche_modele_feuille_route_feuille_route:nxw_feuille_route_title";
    public static final String NUMERO_ID = "searchModeleFeuilleRouteForm:nxl_recherche_modele_feuille_route_feuille_route:nxw_feuille_route_numero";
    public static final String TYPE_ACTE_LIST_ID = "searchModeleFeuilleRouteForm:nxl_recherche_modele_feuille_route_feuille_route:nxw_feuille_route_type_acte";
    public static final String RECHERCHER_BOUTTON_ID = "searchModeleFeuilleRouteForm:search";
    public static final String AJOUTER_AUX_FAVORIS_DE_RECHERCHE_BTN_PATH = "//input[@value='Ajouter aux favoris de recherche']";
    public static final String REINITIALISER_BTN_PATH = "//input[@value='Réinitialiser']";

    /**
     * @param value
     */
    public void setIntitule(String value) {
        WebElement el = getElementById(INTITULE_ID);
        fillField("Intitulé", el, value);
    }

    /**
     * @param numero
     */
    public void setNumero(String numero) {
        WebElement el = getElementById(NUMERO_ID);
        fillField("Numéro", el, numero);
    }

    /**
     * @param textValue
     */
    public void setTypeActe(String textValue) {
        final WebElement typeDacteSelect = getElementById(TYPE_ACTE_LIST_ID);
        getFlog().action("Selectionne \"" + textValue + "\" dans le select \"Type d'acte\"");
        final Select select = new Select(typeDacteSelect);
        select.selectByVisibleText(textValue);
    }

    /**
     * Clic sur le boutton Rechercher
     */
    public DernierFeuilleDeRouteResultat rechercher() {
        getElementById(RECHERCHER_BOUTTON_ID).click();
        waitElementVisibilityById("upperContentView");
        return getPage(DernierFeuilleDeRouteResultat.class);
    }

    public AjouterAuxFavorisPage ajouterAuxFavoris() {
        getElementByXpath(AJOUTER_AUX_FAVORIS_DE_RECHERCHE_BTN_PATH).click();
        waitElementVisibilityById("espaceRechercheDossierContentListPanel");
        return getPage(AjouterAuxFavorisPage.class);
    }

    public void reinitialiser() {
        getElementByXpath(REINITIALISER_BTN_PATH).click();
    }
}
