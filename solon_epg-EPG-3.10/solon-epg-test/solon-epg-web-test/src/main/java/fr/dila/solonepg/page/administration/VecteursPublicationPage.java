package fr.dila.solonepg.page.administration;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.dila.st.webdriver.helper.AutoCompletionHelper;
import fr.dila.st.webdriver.helper.NameShortener;

/**
 * 
 * @author admin
 */
public class VecteursPublicationPage extends EPGWebPage {

	private static final String	FIND_NOR_BUTTON_ID	= "bulletins_officiels_properties:nxl_bulletin_officiel_layout_nor:nxw_bo_nor_select_findButton";

	private static final String	INTITULE_INPUT_ID	= "bulletins_officiels_properties:nxl_bulletin_officiel_layout_intitule:nxw_bo_intitule_select";

	private static final String	AJOUTER_BUTTON_ID	= "bulletins_officiels_properties:button_addBO";

	private static final String	NOR_INPUT_ID		= "bulletins_officiels_properties:nxl_bulletin_officiel_layout_nor:nxw_bo_nor_select_suggest";

	@FindBy(id = "vecteur_publication_properties:button_add_vecteur")
	public WebElement			ajouteVecteurPublication;

	@FindBy(id = "vecteur_publication_properties:button_save_vecteur")
	public WebElement			saveVecteurPublication;

	public void setMinistere(final String ministere) {
		selectInOrganigramme(ministere, FIND_NOR_BUTTON_ID);
	}

	public void setIntitule(final String intitule) {
		final WebElement elem = getDriver().findElement(By.id(INTITULE_INPUT_ID));
		fillField("Intitulé du Bulletin Officiel *", elem, intitule);
	}

	public void ajouterBulletin() {
		WebElement body = findElement(By.cssSelector("body"));
		getDriver().findElement(By.id(AJOUTER_BUTTON_ID)).click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(body));
	}

	public void setNor(final String nor) {
		setAutocompleteValue(new NameShortener(nor, 0), By.id(NOR_INPUT_ID), 0, true);
	}

	public void ajouterVecteurPublication(String nom) {
		ajouteVecteurPublication.click();

		waitForPageSourcePart("Ajout d'un nouveau vecteur de publication", TIMEOUT_IN_SECONDS);

		String inputId = "vecteur_publication_properties:vecteur_publication_content:nxl_vecteur_publication_listing";
		String inputIdEnd = ":nxw_vp_intitule";
		waitForPageSourcePartDisplayed(By.id(inputId + inputIdEnd), TIMEOUT_IN_SECONDS);

		List<WebElement> inputs = getDriver().findElements(
				By.xpath("//input[contains(@id, '" + inputId + "')][contains(@id, '" + inputIdEnd + "')]"));
		WebElement input = inputs.get(inputs.size() - 1);
		fillField("vecteur de publication", input, nom);

		saveVecteurPublication.click();

		waitForPageSourcePart("Sauvegarde des vecteurs de publication", TIMEOUT_IN_SECONDS);
	}

}
