package fr.dila.solonepg.page.recherche;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.dila.solonepg.page.recherche.dernierresultat.DernierFeuilleDeRouteResultat;

public class RechercheExpertePage extends EPGWebPage {

    private static final String CATEGORY_SELECTOR_ID = "smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_categorySelector";

    private static final String FILED_SELECTOR_ID = "smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_rowSelector";

    private static final String NOR_INPUT_VAUE_ID = "smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_numeroNor:nxw_requeteur_dossier_d_dos_numeroNor:nxw_fulltextWidget_1";

    public static final String AJOUTER_BOUTTON_PATH = "//input[@value='Ajouter']";

    public static final String EDIT_ALERTE_BOUTTON_PATH = "//input[@value='Éditer une alerte']";

    public static final String SUGGEST_ID = "smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_idCreateurDossier:nxw_requeteur_dossier_d_dos_idCreateurDossier:nxw_userWidget_1_suggest";

    private static final String ROW_1 = "//*[@id='smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:dataTable:tbody_element']/tr/td[1]";

    private static final String RECHERCHER_BOUTTON_PATH = "//input[@id='smartSearchForm:recherche_requeteur']";

    public static final String MODIFIER_LA_RECHERCHE_BOUTTON_PATH = "//input[@value='Modifier la recherche']";

    public static final String AJOUTER_AUX_TABLEAUX_DYNAMIQUES_PATH = "//input[@id='smartSearchForm:addToTabDynamique']";

    public static final String AJOUTER_AUX_FAVORIS_PATH = "//input[@id='smartSearchForm:addToFav']";

    public CreerAlertePage createNumeroDeNorQuery(String fieldValue) {
        final WebElement typeDacteSelect = findElement(By.id(CATEGORY_SELECTOR_ID));
        getFlog().action("Selectionne Dossier dans le select \"requete category\"");
        final Select select = new Select(typeDacteSelect);
        select.selectByVisibleText("Dossier");
        waitForPageSourcePart("Information sur l'acte : numéro NOR", 10);
        sleep(5);
        WebElement typeDacteSelect2 = findElement(By.id(FILED_SELECTOR_ID));

        final List<WebElement> elements = findDisplayedElements(By.id(FILED_SELECTOR_ID));
        for (WebElement webElement : elements) {
            if (webElement.isDisplayed()) {
                typeDacteSelect2 = webElement;
                break;
            }
        }

        getFlog().action("Selectionne 'Information sur l'acte : numéro NOR' dans le select \"requete field\"");
        final Select select2 = new Select(typeDacteSelect2);
        select2.selectByVisibleText("Information sur l'acte : numéro NOR");
        waitElementVisibilityById(NOR_INPUT_VAUE_ID);

        final WebElement elem = findElement(By.id(NOR_INPUT_VAUE_ID));
        fillField("", elem, fieldValue);
        getElementByXpath(AJOUTER_BOUTTON_PATH).click();

        sleep(5);
        new WebDriverWait(getDriver(), 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ROW_1)));

        getElementByXpath(EDIT_ALERTE_BOUTTON_PATH).click();

        waitForPageSourcePart("Requête experte enregistré(e)", TIMEOUT_IN_SECONDS);
        return (CreerAlertePage) getPage(CreerAlertePage.class);

    }

    public void setReqCategory(String category) {

        WebElement reqEl = getElementById(CATEGORY_SELECTOR_ID);
        getFlog().action("Selectionne \"" + category + "\" dans le select \"Requete Categorie\"");
        Select select = new Select(reqEl);
        select.selectByValue(category);
    }

    public void setReqField(String field) {
        WebElement reqField = getElementById(FILED_SELECTOR_ID);
        getFlog().action("Selectionne \"" + field + "\" dans le select \"Requete Field\"");
        Select select = new Select(reqField);
        select.selectByValue(field);

    }

    public void setAdminsggASuggest() {
        waitElementVisibilityById(SUGGEST_ID);
        WebElement reqSugg = getElementById(SUGGEST_ID);
        fillField("", reqSugg, "adm");
        waitElementVisibilityById("smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_idCreateurDossier:nxw_requeteur_dossier_d_dos_idCreateurDossier:nxw_userWidget_1_suggestionBox");
        WebElement elementByPath = getElementByXpath("//table[@id='smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_idCreateurDossier:nxw_requeteur_dossier_d_dos_idCreateurDossier:nxw_userWidget_1_suggestionBox:suggest']/tbody/tr[4]/td[2]");
        elementByPath.click();
        waitForPageSourcePart("adminsgg", TIMEOUT_IN_SECONDS);
        waitElementVisibilityById("smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_idCreateurDossier:nxw_requeteur_dossier_d_dos_idCreateurDossier:nxw_userWidget_1_selectionGroup");
    }

    public void ajouter() {
        getElementByXpath(AJOUTER_BOUTTON_PATH).click();
        waitElementVisibilityByXpath("//*[contains(@id, 'smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:dataTable:0')]");
    }

    public DernierFeuilleDeRouteResultat rechercher() {
        sleep(1);
        getElementByXpath(RECHERCHER_BOUTTON_PATH).click();
        sleep(1);
        waitElementVisibilityByXpath(MODIFIER_LA_RECHERCHE_BOUTTON_PATH);
        return getPage(DernierFeuilleDeRouteResultat.class);

    }

    public void retourAuDossier() {
        WebElement el = getElementById("form_espace_recherche:espaceRechercheTree:node:0:node:0::feuilleNodeLabel_O");
        el.click();
    }

    public AjouterAuxTableauxDynamiquesPage ajouterAuxTableauxDynamiques() {
        getElementByXpath(AJOUTER_AUX_TABLEAUX_DYNAMIQUES_PATH).click();
        waitElementVisibilityById("espaceRechercheDossierContentListPanel");
        return this.getPage(AjouterAuxTableauxDynamiquesPage.class);
    }

    public AjouterAuxFavorisPage ajouterAuxFavoris() {
        getElementByXpath(AJOUTER_AUX_FAVORIS_PATH).click();
        waitElementVisibilityById("espaceRechercheDossierContentListPanel");
        return getPage(AjouterAuxFavorisPage.class);
    }
}
