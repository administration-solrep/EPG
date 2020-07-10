package fr.dila.solonepg.page.main.onglet.dossier.detail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class BordereauPage extends MenuItemPage {

	public static final String	TITRE_ACTE_ID				= "bordereauForm:nxl_bordereau_donnees_principales:nxw_titre_acte_field";
	public static final String	NOM_DU_RESPONSABLE_ID		= "bordereauForm:nxl_bordereau_responsable_acte:nxw_nom_resp_dossier_field";
	public static final String	QUALITE_DU_RESPONSABLE_ID	= "bordereauForm:nxl_bordereau_responsable_acte:nxw_qualite_resp_dossier_field";
	public static final String	TEL_DU_RESPONSABLE_ID		= "bordereauForm:nxl_bordereau_responsable_acte:nxw_tel_resp_dossier_field";
	public static final String	PRENOM_DU_RESPONSABLE_ID	= "bordereauForm:nxl_bordereau_responsable_acte:nxw_prenom_resp_dossier_field";
	public static final String	MODE_PARUTION_XPATH			= "//select[contains(@id, 'nxw_mode_parution_field_selectOneMenu')]";
	public static final String	DELAI_PUBLICATION_ID		= "bordereauForm:nxl_bordereau_parution:nxw_delai_publication_field";
	public static final String	RUBRIQUES_ID				= "bordereauForm:nxl_bordereau_indexation:nxw_indexation_rubriques_select_index_menu";
	public static final String	ADD_RUBRIQUES_ID			= "bordereauForm:nxl_bordereau_indexation:nxw_indexation_rubriques_addIndexImage";
	public static final String	SECTION_CE_ID				= "bordereauForm:nxl_bordereau_ce:nxw_section_ce_field";
	public static final String	RAPPORTEUR_ID				= "bordereauForm:nxl_bordereau_ce:nxw_rapporteur_ce_field";
	public static final String	DATE_TRANSMISSION_CE_ID		= "bordereauForm:nxl_bordereau_ce:nxw_date_transmission_section_ce_fieldInputDate";
	public static final String	DATE_SECTION_CE_ID			= "bordereauForm:nxl_bordereau_ce:nxw_date_section_ce_fieldInputDate";
	public static final String	DATE_AG_CE_ID				= "bordereauForm:nxl_bordereau_ce:nxw_date_ag_ce_fieldInputDate";
	public static final String	NUMERO_ISA_ID				= "bordereauForm:nxl_bordereau_ce:nxw_numero_isa_field";
	private static final String	DATE_FOURNITURE_EPREUVE_ID	= "bordereauForm:nxl_bordereau_parution:nxw_pour_fourniture_epreuve_fieldInputDate";
	public static final String	VECTEURPUB_ID				= "bordereauForm:nxl_bordereau_parution:nxw_vecteur_publication_field_selectVecteurs";
	public static final String	ADD_VECTEURPUB_ID			= "bordereauForm:nxl_bordereau_parution:nxw_vecteur_publication_field_addVecteurImage";
	public static final String	PUBLICATION_CONJOINTE		= "bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_inputPub";
	public static final String	ADD_PUBLICATION_CONJOINTE	= "bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_addPubImage";
	public static final String	NOR_ID						= "bordereauForm:nxl_bordereau_donnees_principales:nxw_numero_nor_field";
	public static final String	SECTION_CE_FOLDABLE_ID		= "foldableBoxCE";
	public static final String	TITRE_ACTE_XPATH			= "//textarea[contains(@id, 'nxw_titre_acte_field')]";

	/**
	 * Set titre Acte
	 */
	public void setTitreActe(String titreActe) {
		WebElement element = this.getElementByXpath(TITRE_ACTE_XPATH);
		this.fillField("Titre de l'acte", element, titreActe);
	}

	/**
	 * Set Nom du responsable
	 */
	public void setNomDuResponsable(String nom) {
		WebElement element = this.getElementById(NOM_DU_RESPONSABLE_ID);
		this.fillField("Nom du responsable du dossier", element, nom);
	}

	/**
	 * Set Qualite du responsable
	 */
	public void setQualiteDuResponsable(String qualite) {
		WebElement element = this.getElementById(QUALITE_DU_RESPONSABLE_ID);
		this.fillField("Qualité resp. dossier", element, qualite);
	}

	/**
	 * Set Tel du responsable
	 */
	public void setTelDuResponsable(String tel) {
		WebElement element = this.getElementById(TEL_DU_RESPONSABLE_ID);
		this.fillField("Tél. resp. dossier", element, tel);
	}

	/**
	 * Set Prenom du responsable
	 */
	public void setPrenomDuResponsable(String prenom) {
		WebElement element = this.getElementById(PRENOM_DU_RESPONSABLE_ID);
		this.fillField("Prénom resp. dossier", element, prenom);
	}

	/**
	 * Set Mode Parution
	 */
	public void setModeParution(String textValue) {
		WebElement element = findElement(By.xpath(MODE_PARUTION_XPATH));
		getFlog().action("Selectionne \"" + textValue + "\" dans le select \"Mode parution\"");
		Select select = new Select(element);
		select.selectByVisibleText(textValue);
	}

	public void setVecteurPublication(String textValue) {
		WebElement element = this.getElementById(VECTEURPUB_ID);
		getFlog().action("Selectionne \"" + textValue + "\" dans le select \"Vecteur de publication\"");
		Select select = new Select(element);
		select.selectByVisibleText(textValue);

		// Save Rubriques
		this.getElementById(ADD_VECTEURPUB_ID).click();
	}

	/**
	 * Set Delai publication
	 */
	public void setDelaiPublication(String textValue) {
		WebElement element = this.getElementById(DELAI_PUBLICATION_ID);
		getFlog().action("Selectionne \"" + textValue + "\" dans le select \"Délai publication\"");
		Select select = new Select(element);
		select.selectByVisibleText(textValue);
	}

	/**
	 * Set Rubriques
	 */
	public void setRubriques(String textValue) {
		WebElement element = this.getElementById(RUBRIQUES_ID);
		getFlog().action("Selectionne \"" + textValue + "\" dans le select \"Rubriques\"");
		Select select = new Select(element);
		select.selectByVisibleText(textValue);

		// Save Rubriques
		this.getElementById(ADD_RUBRIQUES_ID).click();
	}

	protected String getSavePath() {
		return "//img[@title='Sauvegarder le bordereau']";
	}

	/**
	 * Enregistrer
	 */
	public void enregistrer() {
		this.getElementByXpath(getSavePath()).click();
	}

	public void ensureCeUnfolded(String waitElementId) {
		// Si élément non visible on déplie la section CE
		if (!findElement(By.id(SECTION_CE_ID)).isDisplayed()) {
			WebElement sectionCEDeplie = this.getElementById(SECTION_CE_FOLDABLE_ID);
			sectionCEDeplie.click();
			this.waitElementVisibilityById(waitElementId);
		}
	}

	/**
	 * Set section CE
	 */
	public void setSectionCE(String sectionCE) {
		ensureCeUnfolded(SECTION_CE_ID);
		WebElement element = this.getElementById(SECTION_CE_ID);
		this.fillField("Section du CE", element, sectionCE);
	}

	/**
	 * Set rapporteur
	 */
	public void setRapporteur(String rapporteur) {
		ensureCeUnfolded(RAPPORTEUR_ID);
		WebElement element = this.getElementById(RAPPORTEUR_ID);
		this.fillField("Rapporteur", element, rapporteur);
	}

	/**
	 * Set date Transmission CE
	 */
	public void setDateTransmissionCE(String dateTransmissionCe) {
		ensureCeUnfolded(DATE_TRANSMISSION_CE_ID);
		WebElement element = this.getElementById(DATE_TRANSMISSION_CE_ID);
		this.fillField("Date transm. section CE", element, dateTransmissionCe);
	}

	/**
	 * Set date prévisionnelle Section CE
	 */
	public void setDateSectionCe(String dateSectionCe) {
		ensureCeUnfolded(DATE_SECTION_CE_ID);
		WebElement element = this.getElementById(DATE_SECTION_CE_ID);
		this.fillField("Date prévisionnelle section CE", element, dateSectionCe);
	}

	/**
	 * Set date Ag ce
	 */
	public void setDateAgce(String dateAgCe) {
		ensureCeUnfolded(DATE_AG_CE_ID);
		WebElement element = this.getElementById(DATE_AG_CE_ID);
		this.fillField("Date prévisionnelle AG CE", element, dateAgCe);
	}

	/**
	 * Set numero_isa
	 */
	public void setNumeroIsa(String numeroIsa) {
		ensureCeUnfolded(NUMERO_ISA_ID);
		WebElement element = this.getElementById(NUMERO_ISA_ID);
		this.fillField("Numéro ISA", element, numeroIsa);
	}

	public void setDatePourFournitureEpreuve(String dateEpreuve) {
		WebElement element = this.getElementById(DATE_FOURNITURE_EPREUVE_ID);

		this.fillField("Pour fourniture d'épreuve", element, dateEpreuve);
	}

	/**
	 * Set publication conjointe
	 */
	public void setPublicationConjointe(String publicationConjointe) {
		WebElement element = this.getElementById(PUBLICATION_CONJOINTE);
		this.fillField("Publication conjointe", element, publicationConjointe);
		this.getElementById(ADD_PUBLICATION_CONJOINTE).click();
	}

	public String getNOR() {
		WebElement element = this.getElementById(NOR_ID);
		return element.getText();
	}
}
