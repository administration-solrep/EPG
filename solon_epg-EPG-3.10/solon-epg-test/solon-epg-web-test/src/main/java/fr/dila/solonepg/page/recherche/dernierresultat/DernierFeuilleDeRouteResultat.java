package fr.dila.solonepg.page.recherche.dernierresultat;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.administration.ViewModelePage;
import fr.dila.solonepg.page.recherche.RechercheModeleFeuilleDeRoutePage;
import fr.sword.naiad.commons.webtest.WebPage;

public class DernierFeuilleDeRouteResultat extends AbstractDernierResultat {

    public static final String FDR_CHECKBOX_TEMPLATE_ID = "recherche_resultats_consultes_modele_feuille_route:nxl_recherche_feuille_route_model_listing@INDEX@:nxw_listing_ajax_selection_box@INDEX@";
    public static final String AJOUTER_AUX_FAVORIS_DE_CONSULTATION_PATH = "//input[@value='Ajouter aux favoris de consultation']";
    public static final String MODIFIER_LA_RECHERCHE_BTN_PATH = "//input[@value='Modifier la recherche']";
    public static final String RESULT_CHECKBOX_TEMPLATE_ID = "recherche_modele_feuille_route_resultat:nxl_recherche_feuille_route_model_listing@INDEX@:nxw_listing_ajax_selection_box@INDEX@";

    @Override
    protected String getAjouterAuxFavrisDeConsultation() {
        return AJOUTER_AUX_FAVORIS_DE_CONSULTATION_PATH;
    }

    @Override
    protected String getCheckBoxId() {
        return FDR_CHECKBOX_TEMPLATE_ID;
    }

    /**
     * Clic on the result in the grid in order to get the detail(By link)
     * 
     * @param resultTitle
     */
    public void getDetail(String resultTitle) {
        By detailBy = By.className("subbodyContainer");
        WebElement detailContainer = null;
        try {
            // si le détail du dossier est déjà ouvert, il faut vérifier le raffraichissement
            detailContainer = findElement(detailBy);
        } catch (NoSuchElementException e) {
            // nothing
        }
        
        By linkText = By.linkText(resultTitle);
        clickContextMenuToPage(getDriver().findElement(linkText), "Ouvrir", ViewModelePage.class);
        
        if (detailContainer != null) {
            new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(detailContainer));
        }
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(detailBy));
    }

    /**
     * Click on the result in a list to load another page
     * 
     * @param resultTitle
     */
    public void getDetailPage(String resultTitle) {
        By linkText = By.linkText(resultTitle);
        clickToPage(findElement(linkText), WebPage.class);
    }

    public void modifierLaRecherche() {
    	getFlog().actionClickButton("Modifier la recherche");
        getElementByXpath(MODIFIER_LA_RECHERCHE_BTN_PATH).click();
        waitElementVisibilityById(RechercheModeleFeuilleDeRoutePage.RECHERCHER_BOUTTON_ID);
    }

    public void ajouterAuxFavorisDeConsultation() {
        getFlog().actionClickButton("Ajouter aux favoris de consultation");
        clickToPage(findElement(By.xpath(AJOUTER_AUX_FAVORIS_DE_CONSULTATION_PATH)), WebPage.class);
    }
}
