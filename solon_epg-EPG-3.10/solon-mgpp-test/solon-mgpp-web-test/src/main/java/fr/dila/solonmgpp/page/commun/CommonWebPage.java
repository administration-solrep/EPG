package fr.dila.solonmgpp.page.commun;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonmgpp.page.Organigramme;

public class CommonWebPage extends fr.dila.st.webdriver.model.CommonWebPage {

	public static String		ADD_FILE_INPUT				= "//*[contains(@id, 'editFileForm') and contains(@id, 'Files:file') and not(contains(@id, 'Items'))]"; // "editFileForm:uploadParapheurFiles:file";
	public static String		ADD_FILE					= "//*[contains(@id, 'editFileForm') and contains(@id, 'ButtonImage')]/img";							// "editFileForm:editFileButtonImage"

	public static final String	MESSAGE_LISTING_ROW_1		= "corbeille_message_list:nxl_corbeille_message_listing_layout_1:nxw_idDossier_widget_1:msgName";
	public static final String	MESSAGE_LISTING_ROW_2		= "corbeille_message_list:nxl_corbeille_message_listing_layout_2:nxw_idDossier_widget_2:msgName";
	public static final String	DEVEROUILLER				= "//img[@alt='Déverrouiller']";
	public static final String	VEROUILLER					= "//img[@alt='Verrouiller']";
	public static final String	VEROUILLER_FICHE			= "//img[@alt='Verrouiller fiche']";
	public static final String	DEVEROUILLER_FICHE			= "//img[@alt='Déverrouiller fiche']";
	public static final String	DOSSIER_VEROUILLE_MESSAGE	= "'Dossier verrouillé'";

	public final WebDriver		driver						= getDriver();

	protected String getAddFileInput() {
		return ADD_FILE_INPUT;
	}

	protected String getAddFile() {
		return ADD_FILE;
	}

	/**
	 * Ouvre une corbeille donnée
	 * 
	 * @param corbeilleName
	 *            le nom de la corbeille à ouvrir
	 */
	public void openCorbeille(String corbeilleName) {
		getFlog().actionClickLink(corbeilleName);
		By byPath = By.xpath("//*[contains(text(), \"" + corbeilleName + "\")]");
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(byPath));
		getDriver().findElement(byPath).click();
		waitForPageLoaded(getDriver());

	}

	/**
	 * Ouvre le panneau de filtres
	 */
	public void openFiltre() {
		getFlog().actionClickLink("Filtres");
		getDriver().findElement(By.partialLinkText("Filtres")).click();
		// on vérifie qu'un des champs du filtre est visible
		this.assertElementPresent(By_enum.XPATH, "//td[contains(text(), 'Identifiant dossier')]/../td[2]/input");
	}

	/**
	 * Lance l'action de filtrer
	 */
	public void doRechercheRapide() {
		getFlog().actionClickButton("Filtrer");
		clickBtn(getDriver().findElement(By.xpath("//*[contains(@id, 'form_filtre:_filter_filtrer')]")));
		waitForPageLoaded(getDriver());
	}

	/**
	 * Lance l'action de filtrer
	 */
	public <T extends CommonWebPage> T doRechercheRapide(final Class<T> pageClazz) {
		getFlog().actionClickButton("Filtrer");
		clickBtn(getDriver().findElement(By.xpath("//*[contains(@id, 'form_filtre:_filter_filtrer')]")));
		waitForPageLoaded(getDriver());
		return getPage(pageClazz);
	}

	public <T extends CommonWebPage> T openRecordFromList(final String dossierTitre, final Class<T> pageClazz) {

		WebElement dossierLink = null;

		try {
			dossierLink = getDriver().findElement(By.xpath("//a/span[contains(text(),'" + dossierTitre + "')]"));
		} catch (NoSuchElementException nsee) {// org.openqa.selenium.
			try {
				dossierLink = getDriver().findElement(By.linkText(dossierTitre));
			} catch (NoSuchElementException nsee2) {// org.openqa.selenium.
				try {
					dossierLink = getDriver().findElement(By.partialLinkText(dossierTitre));
				} catch (NoSuchElementException nsee3) {// org.openqa.selenium.
					getFlog().checkFailed("le record avec le lien " + dossierTitre + " ne peut pas etre trouve");
					dossierLink = null;
				}
			}
		}

		dossierLink.click();
		waitForPageLoaded(getDriver());

		return getPage(pageClazz);
	}

	public void doRapidSearch(String corbeilleName, final String idDossier) {
		// procedureLegislativePage.deployFirstElementCorbeille();
		// WebDriverWait wait = new WebDriverWait(getDriver(), 15);
		// wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("corbeilleForm:corbeilleRegion:status.start")));
		openCorbeille(corbeilleName);

		// ABI : pourquoi faire ça? si on le recherche, c'est bien qu'il n'est pas forcément visible de base
		// getFlog().check("Vérification de la présence du  " + idDossier);
		// Assert.assertTrue(this.hasElement(By.xpath("//*[contains(text(), '" + idDossier + "')]")));

		openFiltre();

		getDriver().findElement(By.xpath("//td[contains(text(), 'Identifiant dossier')]/../td[2]/input")).sendKeys(
				idDossier);

		doRechercheRapide();

	}

	public <T extends CommonWebPage> T doRapidSearch(String corbeilleName, final String idDossier,
			final Class<T> pageClazz) {
		openCorbeille(corbeilleName);
		openFiltre();

		getDriver().findElement(By.xpath("//td[contains(text(), 'Identifiant dossier')]/../td[2]/input")).sendKeys(
				idDossier);
		return doRechercheRapide(pageClazz);
	}

	public void doRapidSearchByAttribute(final String attributeLabel, final String value) {

		getFlog().check("Vérification de la présence du  " + value);
		Assert.assertTrue(this.hasElement(By.xpath("//*[contains(text(), '" + value + "')]")));

		openFiltre();

		getDriver().findElement(By.xpath("//td[contains(text(), '" + attributeLabel + "')]/../td[2]/input")).sendKeys(
				value);

		doRechercheRapide();

	}

	public void doRapidSearchAndClickElement(String corbeilleName, final String NUM_NOR) {
		doRapidSearch(corbeilleName, NUM_NOR);

		getDriver().findElement(By.xpath("//a/span[contains(text(),'" + NUM_NOR + "')]")).click();
	}

	public void selectDestinataireFromOrganigramme(final ArrayList<String> ar, final String elementToClick) {
		selectMultipleInOrganigramme(ar, elementToClick);
	}

	/**
	 * 
	 * @param ar
	 *            : arrayList contenant les ID des elements a cliquer
	 * @param id
	 *            : pour definir sur quel element ouvrir l'organigramme
	 */
	public void selectMultipleInOrganigramme(final ArrayList<String> ar, final String id) {
		getFlog().action("Sélection multiple dans organigramme");
		final WebElement element = getDriver().findElement(By.id(id));
		final Organigramme organigramme = getPage(Organigramme.class);
		organigramme.openOrganigramme(element);
		organigramme.selectMultipleElement(ar);
	}

	public void verouiller_par_alt() {
		getFlog().action("verouiller par alt");
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath(VEROUILLER)));
		getDriver().findElement(By.xpath(VEROUILLER)).click();
		waitForPageLoaded(getDriver());
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By
				.xpath("//*[contains(text()," + DOSSIER_VEROUILLE_MESSAGE + ")]")));
	}

	public void verouiller_fiche_par_alt() {
		getFlog().action("verouiller fiche par alt");
		new WebDriverWait(getDriver(), 15)
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath(VEROUILLER_FICHE)));
		getDriver().findElement(By.xpath(VEROUILLER_FICHE)).click();
		// new WebDriverWait(getDriver(),
		// 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath(DEVEROUILLER_FICHE)));//testcase 029
	}

	public void deverouiller_par_alt() {
		getFlog().action("deverouiller par alt");
		new WebDriverWait(getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath(DEVEROUILLER)));
		getDriver().findElement(By.xpath(DEVEROUILLER)).click();
	}

	public void deverouiller_fiche_par_alt() {
		getFlog().action("deverouiller fiche par alt");
		new WebDriverWait(getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath(DEVEROUILLER_FICHE)));
		getDriver().findElement(By.xpath(DEVEROUILLER_FICHE)).click();
		// waitForPageLoaded(getDriver());
	}

	public static String getDossierId() {
		String dossierId_prefix = "CCOZ";
		String dossierId_suffix = "00000L";
		String dossierId_year = "14";

		try {
			dossierId_year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2);
		} catch (Exception e) {
		}

		return dossierId_prefix + dossierId_year + dossierId_suffix;
	}

}