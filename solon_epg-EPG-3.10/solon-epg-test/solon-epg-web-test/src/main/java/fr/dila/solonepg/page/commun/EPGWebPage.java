package fr.dila.solonepg.page.commun;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Lists;

import fr.dila.solonepg.page.main.onglet.RecherchePage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.DossierDetailMenu;

public class EPGWebPage extends fr.dila.st.webdriver.model.CommonWebPage {

	public static final String	ADD_FILE_CLICK				= "//div[contains(@id, 'createFile') and contains(@id, 'Files:add') and not(contains(@id, 'Items')) and contains(@class, 'rich-fileupload-button rich-fileupload-font')]";
	public static final String	ADD_FILE_INPUT				= "//input[contains(@id, 'createFile') and contains(@id, 'Files:') and not(contains(@id, 'Items'))]";																		// "editFileForm:uploadParapheurFiles:file";
	public static final String	ADD_FILE					= "//*[contains(@id, 'createFile') and contains(@id, 'ButtonImage')]/img";																									// "editFileForm:editFileButtonImage"

	public static final String	MESSAGE_LISTING_ROW_1		= "corbeille_message_list:nxl_corbeille_message_listing_layout_1:nxw_idDossier_widget_1:msgName";
	public static final String	MESSAGE_LISTING_ROW_2		= "corbeille_message_list:nxl_corbeille_message_listing_layout_2:nxw_idDossier_widget_2:msgName";
	public static final String	DEVEROUILLER				= "//img[@alt='Déverrouiller']";
	public static final String	VEROUILLER					= "//img[@alt='Verrouiller']";

	public static final String	NOR_INPUT_ID				= "userMetaServicesSearchForm:numeroNor";
	public static final String	RECHERCHE_BUTTON_ID			= "userMetaServicesSearchForm:rechercheNumeroSubmitButton";

	// Profil Utilisateur
	public static final String	PROFIL_UTILISATUER_BTN_ID	= "servicesForm:profilutilisateurImageButton";
	public static final String	PROFIL_UTILISATEUR_LINK_ID	= "servicesForm:profilutilisateurTextButton";

	// Redéfinition des champs statics à surcharger
	static {
		TIMEOUT_IN_SECONDS = 300;
	}

	/**
	 * Ouvre une corbeille donnée
	 * 
	 * @param corbeilleName
	 *            le nom de la corbeille à ouvrir
	 */
	public void openCorbeille(String corbeilleName) {
		getFlog().actionClickLink(corbeilleName);
		waitForPageSourcePartDisplayed(By.xpath("//*[contains(text(), '" + corbeilleName + "')]"), TIMEOUT_IN_SECONDS);
		getDriver().findElement(By.xpath("//*[contains(text(), \"" + corbeilleName + "\")]")).click();
		waitForPageLoaded(getDriver());

	}

	/**
	 * Ouvre une corbeille donnée
	 * 
	 * @param corbeilleName
	 *            le nom de la corbeille à ouvrir
	 */
	public <T extends EPGWebPage> T openCorbeille(String corbeilleName, Class<T> clazz) {
		getFlog().actionClickLink(corbeilleName);
		waitForPageSourcePartDisplayed(By.xpath("//*[contains(text(), '" + corbeilleName + "')]"), TIMEOUT_IN_SECONDS);
		getDriver().findElement(By.xpath("//*[contains(text(), \"" + corbeilleName + "\")]")).click();
		waitForPageLoaded(getDriver());
		return getPage(clazz);
	}

	/**
	 * Ouvre le panneau de filtres
	 */
	public void openFiltre() {
		getFlog().actionClickLink("Filtres");
		getDriver().findElement(By.partialLinkText("Filtres")).click();
	}

	/**
	 * Lance l'action de filtrer
	 */
	public void doRechercheRapide() {
		getFlog().actionClickButton("Filtrer");
		getDriver().findElement(By.xpath("//*[contains(@id, 'form_filtre:_filter_filtrer')]")).click();
	}

	/**
	 * @deprecated Utiliser {{@link #selectMultipleInOrganigramme(List, String)}} ou {@link #selectMultipleByInOrganigramme(List, String)}
	 *             qui attendent correctement la mise à jour du champs caché avant de rendre le contrôle. L'utilisation
	 *             de cette méthode peut laisser le champ du formulaire vide s'il n'y a pas temporisation (
	 *             l'organigramme est fermé mais le champ du formulaire est vide lors de l'envoi).
	 */
	@Deprecated
	public void selectInOrganigramme(final String destinataire, final String id) {
		getFlog().action("Sélection " + destinataire + " dans organigramme");
		final WebElement element = getDriver().findElement(By.id(id));
		final Organigramme organigramme = getPage(Organigramme.class);
		organigramme.openOrganigramme(element, id);
		organigramme.selectElement(destinataire);
	}

	public void doRapidSearch(String corbeilleName, final String idDossier) {
		// procedureLegislativePage.deployFirstElementCorbeille();
		// WebDriverWait wait = new WebDriverWait(getDriver(), 15);
		// wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("corbeilleForm:corbeilleRegion:status.start")));
		openCorbeille(corbeilleName);

		getFlog().check("Vérification de la présence du  " + idDossier);
		Assert.assertTrue(this.hasElement(By.xpath("//*[contains(text(), '" + idDossier + "')]")));

		openFiltre();

		getDriver().findElement(By.xpath("//td[contains(text(), 'Identifiant dossier')]/../td[2]/input")).sendKeys(
				idDossier);

		doRechercheRapide();

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

	/**
	 * @deprecated utiliser {@link #selectMultipleByInOrganigramme(By, List, String)}
	 */
	public void selectDestinataireFromOrganigramme(final List<String> ar, final String elementToClick) {
		selectMultipleInOrganigramme(null, ar, elementToClick);
	}

	public void selectDestinataireFromOrganigramme(final By modalContainerBy, final By destinataire, final String elementToClick) {
		selectMultipleByInOrganigramme(modalContainerBy, Collections.singletonList(destinataire), elementToClick);
	}

	/**
	 * @deprecated utiliser {@link #selectMultipleByInOrganigramme(By, List, String)}
	 */
	public void selectMultipleInOrganigramme(final List<String> ar, final String id) {
		selectMultipleInOrganigramme(null, ar, id);
	}

	/**
	 * @deprecated utiliser {@link #selectMultipleByInOrganigramme(By, List, String)}
	 */
	public void selectMultipleInOrganigramme(final By modalContainerBy, final List<String> ar, final String id) {
		// ar is a list of ids
		List<By> bys = Lists.newArrayList();
		for (String elementId : ar) {
			bys.add(By.id(elementId));
		}
		selectMultipleByInOrganigramme(modalContainerBy, bys, id);
	}

	/**
	 * @deprecated utiliser {@link #selectMultipleByInOrganigramme(By, List, String)}
	 */
	public void selectMultipleByInOrganigramme(final List<By> ar, final String id) {
		selectMultipleByInOrganigramme(null, ar, id);
	}

	public void selectMultipleByInOrganigramme(final By modalContainerBy, final List<By> ar, final String id) {
		// find related hidden input field to check update status after selection
		Assert.assertTrue(id.endsWith("findButton"));
		String fieldId = null;
		boolean isInput = true;
		{
			// on a 2 cas, soit un rafraichissement d'input, soit de region
			fieldId = id.replaceAll("findButton$", "nodeId");
			WebElement field;
			try {
				field = findElement(By.id(fieldId));
			} catch (NoSuchElementException e) {
				// consider a list-field if nodeId field is not here
				fieldId = id.replaceAll("findButton$", "listRegion");
				field = findElement(By.id(fieldId));
				isInput = false;
			}
		}
		
		getFlog().action("Sélection multiple dans organigramme");
		this.waitElementVisibilityById(id);
		final WebElement element = getDriver().findElement(By.id(id));
		final Organigramme organigramme = getPage(Organigramme.class);
		organigramme.openOrganigramme(modalContainerBy, element, id);
		organigramme.selectMultipleElement(ar, fieldId);

		if (isInput) {
			// then wait for a new input with a value set
			String xpath = String.format("//*[@id='%s' and string-length(@value) != 0]", fieldId);
			new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		} else {
			// or wait for element list to be updated
			new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.id(fieldId)));
		}
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

	public void deverouiller_par_alt() {
		getFlog().action("deverouiller par alt");
		new WebDriverWait(getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By.xpath(DEVEROUILLER)));
		getDriver().findElement(By.xpath(DEVEROUILLER)).click();
	}

	public OngletMenuEpg getOngletMenu() {
		return getPage(OngletMenuEpg.class);
	}

	public ProfilUtilisateurPage openProfilUtilisateurByLink() {
		return openProfilUtilisateur(PROFIL_UTILISATEUR_LINK_ID);
	}

	public ProfilUtilisateurPage openProfilUtilisateurByButton() {
		return openProfilUtilisateur(PROFIL_UTILISATUER_BTN_ID);
	}

	private ProfilUtilisateurPage openProfilUtilisateur(String id) {
		this.getElementById(id).click();
		this.waitElementVisibilityById("displayedProfilUtilisateurPopup:profilUtilisateurPanelCDiv");
		return this.getPage(ProfilUtilisateurPage.class);
	}

	public DossierDetailMenu getDossierDetail(String nor) {
		By linkText = By.linkText(nor);
		waitElementVisibilityByLinkText(nor);
		return clickToPage(findElement(linkText), DossierDetailMenu.class);
	}

	public RecherchePage rechercheNor(String expression) {
		WebElement norInputElement = this.getElementById(NOR_INPUT_ID);
		fillField("nor", norInputElement, expression);
		return clickToPage(this.getElementById(RECHERCHE_BUTTON_ID), RecherchePage.class);
	}
}