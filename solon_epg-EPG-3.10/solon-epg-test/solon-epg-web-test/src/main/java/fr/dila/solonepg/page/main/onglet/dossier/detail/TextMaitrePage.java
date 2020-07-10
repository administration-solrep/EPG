package fr.dila.solonepg.page.main.onglet.dossier.detail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonepg.page.an.loi.DetailDecretPage;
import fr.dila.solonepg.page.an.loi.ModificationMesurePage;
import fr.sword.naiad.commons.webtest.WebPage;
import junit.framework.Assert;

public class TextMaitrePage extends MenuItemPage {

	public static final String	MOT_CLE_ID					= "texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_motCle_dos";
	public static final String	MOT_CLE_XPATH				= "//textarea[contains(@id, 'nxw_activite_normative_widget_motCle_dos')]";
	public static final String	NUMERO_LOCK_TEXT_ID			= "texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_numero_lock_dos:nxw_activite_normative_widget_numero_lock_dos_text";
	public static final String	NUMERO_LOCK_CHECK_ID		= "texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_numero_lock_dos:nxw_activite_normative_widget_numero_lock_dos_check";
	public static final String	NUMERO_INTERNE_ID			= "texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_numeroInterne_dos:nxw_activite_normative_widget_numeroInterne_dos_text";
	public static final String	INTITULE_ID					= "texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_titre_acte_field_dos:nxw_activite_normative_widget_titre_acte_field_dos_text";
	public static final String	NATURE_DU_TEXT_ID			= "texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_natureTexte_dos:nxw_activite_normative_widget_natureTexte_dos_select_one_menu";
	public static final String	NATURE_DU_VOTE_ID			= "texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_procedureVote_dos:nxw_activite_normative_widget_procedureVote_dos_select_one_menu";
	public static final String	SAUVEGADER_BTN_PATH			= "//span[@id='texte_maitre_Form:texte_maitre_action']/a[1]/img";
	public static final String	RECHARGER_BTN_PATH			= "//span[@id='texte_maitre_Form:texte_maitre_action']/a[2]/img";
	public static final String	EDIT_DATA_BTN_ID			= "texte_maitre_mesures:nxl_texte_maitre_mesures_listing@INDEX@:nxw_activite_normative_widget_mesure_edit@INDEX@:nxw_activite_normative_widget_mesure_edit@INDEX@edit_img";
	public static final String	SAUVEGARDER_DETAIL_PATH		= "//span[@id='texte_maitre_mesures:mesure_actions']/a[1]/img";

	public static final String	DETAIL_DECRETS_ID			= "texte_maitre_mesures:nxl_texte_maitre_mesures_listing@INDEX@:nxw_activite_normative_widget_mesure_show_decret@INDEX@:nxw_activite_normative_widget_mesure_show_decret@INDEX@decrets_img";
	public static final String	MINISTERE_PILOTE_BTN_ID		= "texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_ministereResp_dos_findButton";
	public static final String	APPLICATION_DE_LOI_RADIO_ID	= "texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_applicationDirecte_dos:nxw_activite_normative_widget_applicationDirecte_dos_checkbox:0";
	public static final String	OBSERVATION_ID				= "texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_observation_dos";
	public static final String	OBSERVATION_XPATH			= "//textarea[contains(@id, 'nxw_activite_normative_widget_observation_dos')]";
	public static final String	CHAMP_LIBRE_ID				= "texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_champLibre_dos";
	public static final String	CHAMP_LIBRE_XPATH			= "//textarea[contains(@id, 'nxw_activite_normative_widget_champLibre_dos')]";
	public static final String	LOI_ABILITATION_RADIO_ID	= "texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_dispositionHabilitation_dos:nxw_activite_normative_widget_dispositionHabilitation_dos_checkbox:0";
	public static final String	DATE_DE_REUNION_ID			= "texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_dateReunionProgrammation_dosInputDate";
	public static final String	DATE_MISE_EN_CIRCULATION_ID	= "texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_dateCirculationCompteRendu_dosInputDate";

	public void setMotCle(String motCle) {
		WebElement element = this.getElementByXpath(MOT_CLE_XPATH);
		this.fillField("Titre de l'acte", element, motCle);
	}

	public void setNumeroLock(String numero) {
		this.fillElement(NUMERO_LOCK_TEXT_ID, "N°", numero);
	}

	public void setNumeroInterne(String numeroInterne) {
		this.fillElement(NUMERO_INTERNE_ID, "N° interne", numeroInterne);
	}

	public void setIntitule(String intitule) {
		this.fillElement(INTITULE_ID, "Intitulé", intitule);
	}

	public void setNatureDuText(String nature) {
		WebElement el = this.getElementById(NATURE_DU_TEXT_ID);
		Select select = new Select(el);
		select.selectByVisibleText(nature);
	}

	public void setNatureDuVote(String nature) {
		WebElement el = this.getElementById(NATURE_DU_VOTE_ID);
		Select select = new Select(el);
		select.selectByVisibleText(nature);
	}

	public void setMinisterePilote(String ministere) {
		selectInOrganigramme(ministere, MINISTERE_PILOTE_BTN_ID);
	}

	public void selectApplicationDeLoiImmediate() {
		this.getElementById(APPLICATION_DE_LOI_RADIO_ID).click();
	}

	public void selectNumeroLock() {
		this.getElementById(NUMERO_LOCK_CHECK_ID).click();
	}

	public void setObservation(String ovservation) {
		WebElement element = this.getElementByXpath(OBSERVATION_XPATH);
		this.fillField("Observation", element, ovservation);
	}

	public void setChampLibre(String chammLibre) {
		WebElement element = this.getElementByXpath(CHAMP_LIBRE_XPATH);
		this.fillField("Champ libre", element, chammLibre);
	}

	public void selectLoiAbilitation() {
		this.getElementById(LOI_ABILITATION_RADIO_ID).click();
	}

	public void setDateDeReunion(String date) {
		this.fillElement(DATE_DE_REUNION_ID, "Date de la réunion de programmation", date);
	}

	public void setDateMiseEnCirculation(String date) {
		this.fillElement(DATE_MISE_EN_CIRCULATION_ID, "Date de mise en circulation du CR de la réunion", date);
	}

	public void sauvegarder() {
		clickToPage(findElement(By.xpath(SAUVEGADER_BTN_PATH)), WebPage.class);
	}

	public void recharger() {
		clickToPage(findElement(By.xpath(RECHARGER_BTN_PATH)), WebPage.class);
	}

	public void sauvegarderDetail() {
		clickToPage(findElement(By.xpath(SAUVEGARDER_DETAIL_PATH)), WebPage.class);
	}

	public DetailDecretPage detailDecretPage(int index) {

		String replaceBy = index == 0 ? "" : "_" + index;
		String elId = DETAIL_DECRETS_ID.replace("@INDEX@", replaceBy);
		this.getElementById(elId).click();
		this.waitElementVisibilityById("anormativedecret");
		return this.getPage(DetailDecretPage.class);
	}

	public ModificationMesurePage edit(int index) {
		String replaceBy = index == 0 ? "" : "_" + index;
		String elId = EDIT_DATA_BTN_ID.replace("@INDEX@", replaceBy);
		this.getElementById(elId).click();
		this.waitElementVisibilityById("editMesurePanelCDiv");
		return this.getPage(ModificationMesurePage.class);

	}

	public ModificationMesurePage edit() {
		return this.edit(0);
	}

	public void assertMinisterePiloteValue(String ministereId) {

		WebElement el = this
				.getElementById("texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_ministereResp_dos_nodeId");
		String value = el.getAttribute("value");
		Assert.assertEquals(value, ministereId);
	}

	public void lockIntitule() {
		this.getElementById(
				"texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_titre_acte_field_dos:nxw_activite_normative_widget_titre_acte_field_dos_check")
				.click();
	}

	public void lockMinisterePilote() {
		this.getElementById(
				"texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_ministereResp_lock_dos:nxw_activite_normative_widget_ministereResp_lock_dos_check")
				.click();
	}
}
