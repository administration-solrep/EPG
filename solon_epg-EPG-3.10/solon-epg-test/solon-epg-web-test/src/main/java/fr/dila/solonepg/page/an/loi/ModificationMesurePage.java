package fr.dila.solonepg.page.an.loi;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.sword.naiad.commons.webtest.WebPage;

public class ModificationMesurePage extends EPGWebPage {

	public static final String	NUMERO_ORDRE_ID					= "editMesurePanelForm:nxl_texte_maitre_mesure:nxw_activite_normative_widget_mesure_numeroOrdre_row";
	public static final String	VALIDER_BTN_PATH				= "//input[@id='editMesurePanelForm:validerMesure']";
	private static final String	ARTICLE_DE_LA_LOI_XPATH			= "//textarea[contains(@id, 'nxw_activite_normative_widget_mesure_article_row')]";
	private static final String	CODE_MODIFIE_ID					= "editMesurePanelForm:nxl_texte_maitre_mesure:nxw_activite_normative_widget_mesure_codeModifie_row";
	private static final String	BASE_LEGALE_XPATH				= "//textarea[contains(@id, 'nxw_activite_normative_widget_mesure_baseLegale_row')]";
	private static final String	OBJET_XPATH						= "//textarea[contains(@id, 'nxw_activite_normative_widget_mesure_objetRIM_row')]";
	private static final String	MINISTERE_PILOTE_ID				= "editMesurePanelForm:nxl_texte_maitre_mesure:nxw_activite_normative_widget_data_ministerePilote_row_select_one_menu";
	private static final String	DIRECTION_RESPONSABLE_XPATH		= "//textarea[contains(@id, 'nxw_activite_normative_widget_mesure_directionResponsable_row')]";
	private static final String	CONSULTATION_OBLIGATOIRE_XPATH	= "//textarea[contains(@id, 'nxw_activite_normative_widget_mesure_consultationsHCE_row')]";
	private static final String	CALENDRIER_CONSULTATION_ID		= "editMesurePanelForm:nxl_texte_maitre_mesure:nxw_activite_normative_widget_mesure_calendrierConsultationsHCE_row";
	private static final String	DATE_SAISIE_ID					= "editMesurePanelForm:nxl_texte_maitre_mesure:nxw_activite_normative_widget_mesure_datePrevisionnelleSaisineCE_rowInputDate";
	private static final String	OBJECTIF_PUBLICATION_ID			= "editMesurePanelForm:nxl_texte_maitre_mesure:nxw_activite_normative_widget_mesure_dateObjectifPublication_rowInputDate";
	private static final String	OBSERVATIONS_XPATH				= "//textarea[contains(@id, 'nxw_activite_normative_widget_mesure_observation_row')]";
	private static final String	TYPE_DE_MESURE_ID				= "editMesurePanelForm:nxl_texte_maitre_mesure:nxw_activite_normative_widget_mesure_typeMesure_row";
	private static final String	POLE_CHARGE_DE_MISSION_ID		= "editMesurePanelForm:nxl_texte_maitre_mesure:nxw_activite_normative_widget_mesure_poleChargeMission_row";
	private static final String	AMENDEMENT_ID					= "editMesurePanelForm:nxl_texte_maitre_mesure:nxw_activite_normative_widget_mesure_amendement_row:nxw_activite_normative_widget_mesure_amendement_row_checkbox:0";
	private static final String	DATE_DE_PASSAGE_EN_CM_ID		= "editMesurePanelForm:nxl_texte_maitre_mesure:nxw_activite_normative_widget_mesure_datePassageCM_rowInputDate";
	private static final String	RESPONSABLE_AMENDEMENT_ID		= "editMesurePanelForm:nxl_texte_maitre_mesure:nxw_activite_normative_widget_mesure_responsableAmendement_row";
	private static final String	NUMERO_QUESTION_XPATH			= "//textarea[contains(@id, 'nxw_activite_normative_widget_mesure_numeroQuestion_row')]";
	private static final String	CHAMP_LIBRE_XPATH				= "//textarea[contains(@id, 'nxw_activite_normative_widget_data_champLibre_row')]";
	private static final String	VALIDER_LES_MODIFS_ID			= "editMesurePanelForm:nxl_texte_maitre_mesure:nxw_activite_normative_widget_validation_row:nxw_activite_normative_widget_validation_row_check";

	public void setNumeroOrdre(String numeroOrdre) {
		this.fillElement(NUMERO_ORDRE_ID, "N° ordre", numeroOrdre);
	}

	public void setArticleDeLaLoi(String article) {
		WebElement element = this.getElementByXpath(ARTICLE_DE_LA_LOI_XPATH);
		this.fillField("Article de la loi", element, article);
	}

	public void setCodeModifie(String codeModifie) {
		this.fillElement(CODE_MODIFIE_ID, "Code modifié", codeModifie);
	}

	public void setBaseLegale(String baseLegale) {
		WebElement element = this.getElementByXpath(BASE_LEGALE_XPATH);
		this.fillField("Base légale", element, baseLegale);
	}

	public void setObjet(String objet) {
		WebElement element = this.getElementByXpath(OBJET_XPATH);
		this.fillField("Objet", element, objet);
	}

	public void setMinisterePilote(String ministerePilote) {
		this.getElementById(
				"editMesurePanelForm:nxl_texte_maitre_mesure:nxw_activite_normative_widget_data_ministerePilote_row_findButton")
				.click();
		WebPage.sleep(2);
		this.getElementByXpath(
				"//td[contains(@id,'editMesurePanelForm:nxl_texte_maitre_mesure:nxw_activite_normative_widget_data_ministerePilote_row_tree')]/span[contains(text(),'"
						+ ministerePilote + "')]").findElement(By.xpath("./../a/img")).click();
		getFlog().action("Selectionne \"" + ministerePilote + "\" dans le widget \"Ministère pilote\"");
	}

	public void setDirectionResponsable(String directionResponsable) {
		WebElement element = this.getElementByXpath(DIRECTION_RESPONSABLE_XPATH);
		this.fillField("Direction responsable", element, directionResponsable);
	}

	public void setConsulttionObligatoireHorsCE(String consultation) {
		WebElement element = this.getElementByXpath(CONSULTATION_OBLIGATOIRE_XPATH);
		this.fillField("Consultation obligatoire hors CE", element, consultation);
	}

	public void setCalendrierConsultation(String calendrierConsulttion) {
		this.fillElement(CALENDRIER_CONSULTATION_ID, "Calendrier consultations hors CE", calendrierConsulttion);
	}

	public void setDateSaisieCE(String date) {
		this.fillElement(DATE_SAISIE_ID, "Date prévisionnelle de saisine CE", date);
	}

	public void setObjectivePublication(String objectifPublication) {
		this.fillElement(OBJECTIF_PUBLICATION_ID, "Objectif de publication", objectifPublication);
	}

	public void setObservations(String observations) {
		WebElement element = this.getElementByXpath(OBSERVATIONS_XPATH);
		this.fillField("Observations", element, observations);
	}

	public void setTypeMesure(String typeMesure) {

		final WebElement typeMesureSelect = this.getElementById(TYPE_DE_MESURE_ID);
		getFlog().action("Selectionne \"" + typeMesure + "\" dans le select \"Type de mesure\"");
		final Select select = new Select(typeMesureSelect);
		select.selectByVisibleText(typeMesure);
	}

	public void setPoleChargeDeMission(String poleCharge) {
		final WebElement poleChargeSelect = this.getElementById(POLE_CHARGE_DE_MISSION_ID);
		getFlog().action("Selectionne \"" + poleCharge + "\" dans le select \"Pôle chargé de mission\"");
		final Select select = new Select(poleChargeSelect);
		select.selectByVisibleText(poleCharge);

	}

	public void setAmendement() {
		this.getElementById(AMENDEMENT_ID).click();
	}

	public void setDateDePassageEnCM(String datePassaageEnCM) {
		this.fillElement(DATE_DE_PASSAGE_EN_CM_ID, "Date de passage en CM", datePassaageEnCM);
	}

	public void setNumeroQuestion(String numeroQuestion) {
		WebElement element = this.getElementByXpath(NUMERO_QUESTION_XPATH);
		this.fillField("Numéro question parlementaire", element, numeroQuestion);
	}

	public void setResponsableAmendement(String amendement) {
		final WebElement respAmendSelect = this.getElementById(RESPONSABLE_AMENDEMENT_ID);
		getFlog().action("Selectionne \"" + amendement + "\" dans le select \"Responsable amendement\"");
		final Select select = new Select(respAmendSelect);
		select.selectByVisibleText(amendement);

	}

	public void setChampLibre(String champLibre) {
		WebElement element = this.getElementByXpath(CHAMP_LIBRE_XPATH);
		this.fillField("Champ libre", element, champLibre);
	}

	public void selectValiderLesModifs() {
		this.getElementById(VALIDER_LES_MODIFS_ID).click();
	}

	public void valider() {
		this.getElementByXpath(VALIDER_BTN_PATH).click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By
				.id("editMesurePanelCDiv")));
	}

}
