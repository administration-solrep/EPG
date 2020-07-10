package fr.dila.solonepg.page.main.onglet;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonepg.page.an.loi.ApplicationDesLoisPage;
import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.dila.solonepg.page.main.pan.view.ParamStatField;
import fr.dila.solonepg.page.main.pan.view.TableauMaitreViewPage;
import fr.dila.solonepg.webtest.webdriver020.report.validator.PanReportValidator;

public class PilotageANPage extends EPGWebPage {

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Application des lois")
	private WebElement applicationDesLois;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Export Global")
	private WebElement exportGlobal;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Paramétrages des statistiques")
	private WebElement paramStats;
	
	@FindBy(how = How.ID, using = "displayedStatsParametersPopup:configureStatsParametersForm:nxl_legislatures:document_properties:nxw_liste_legislatures_inputPub")
	private WebElement inputLegis;
	
	private String popupHeaderId = "displayedStatsParametersPopup:configureReportsStatsParametersPanelHeader";
	
	private String addLegisBtnId = "displayedStatsParametersPopup:configureStatsParametersForm:nxl_legislatures:document_properties:nxw_liste_legislatures_addPubImage";

	public ApplicationDesLoisPage goToApplicationDesLoisPage() {
		ApplicationDesLoisPage applicationDesLoisPage = clickToPage(applicationDesLois, ApplicationDesLoisPage.class);
		waitElementVisibilityById("upperContentView");
		return applicationDesLoisPage;
	}

	public TableauMaitreViewPage goToApplicationDesLoisTextMaitreViewPage() {
		TableauMaitreViewPage applicationDesLoisPage = clickToPage(applicationDesLois, TableauMaitreViewPage.class);
		waitElementVisibilityById("upperContentView");
		return applicationDesLoisPage;
	}

	public void logError(String error) {
		getFlog().actionFailed(error);
	}

	public <T extends PanReportValidator> T getSelectedPage(String title, Class<T> clazz) {
		waitElementVisibilityByLinkText(title);
		WebElement el = getElementByLinkText(title);
		T click = clickToPage(el, clazz);
		waitForPageSourcePart("'id':'espace_activite_normative'", 30);
		return click;

	}

	public void openExportPopUp() {
		getFlog().startAction("Ouverture de la pop-up d'export global");
		waitElementVisibilityByLinkText("Export Global");
		exportGlobal.click();
		getFlog().endAction();
	}

	public void openParamStatsPopUp() {
		getFlog().startAction("Ouverture de la pop-up de paramétrage du pan");
		waitElementVisibilityByLinkText("Paramétrages des statistiques");
		paramStats.click();
		waitElementVisibilityById(popupHeaderId);
		getFlog().endAction();
	}

	public void fillParamStatsPopUp(ParamStatField fields) {
		getFlog().startAction("Remplissage des valeurs de la pop-up");
		if (fields.getEc_debutLegislature() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_debut_legis_encours:nxw_ec_debut_legislatureInputDate",
					"Début de la législature", fields.getEc_debutLegislature());

		if (fields.getBs_ec_prom_deb() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_bs_en_cours:nxw_intervalle_bs_ec_promulgation_loi_widget:nxw_intervalle_bs_ec_promulgation_loi_widget_fromInputDate",
					"Intervalle de promulgation Lois :", fields.getBs_ec_prom_deb());
		
		if (fields.getBs_ec_prom_fin() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_bs_en_cours:nxw_intervalle_bs_ec_promulgation_loi_widget:nxw_intervalle_bs_ec_promulgation_loi_widget_toInputDate",
					"Intervalle de promulgation Lois :", fields.getBs_ec_prom_fin());
		
		if (fields.getBs_ec_pub_deb() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_bs_en_cours:nxw_intervalle_bs_ec_publication_decret_widget:nxw_intervalle_bs_ec_publication_decret_widget_fromInputDate",
					"Intervalle de publication Décrets :", fields.getBs_ec_pub_deb());
		
		if (fields.getBs_ec_pub_fin() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_bs_en_cours:nxw_intervalle_bs_ec_publication_decret_widget:nxw_intervalle_bs_ec_publication_decret_widget_toInputDate",
					"Intervalle de publication Décrets :", fields.getBs_ec_pub_fin());
		
		if (fields.getTp_ec_prom_deb() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_parl_en_cours:nxw_intervalle_tp_ec_promulgation_loi_widget:nxw_intervalle_tp_ec_promulgation_loi_widget_fromInputDate",
					"Intervalle de promulgation Lois :", fields.getTp_ec_prom_deb());
		
		if (fields.getTp_ec_prom_fin() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_parl_en_cours:nxw_intervalle_tp_ec_promulgation_loi_widget:nxw_intervalle_tp_ec_promulgation_loi_widget_toInputDate",
					"Intervalle de promulgation Lois :", fields.getTp_ec_prom_fin());
		
		if (fields.getTp_ec_pub_deb() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_parl_en_cours:nxw_intervalle_tp_ec_publication_decret_widget:nxw_intervalle_tp_ec_publication_decret_widget_fromInputDate",
					"Intervalle de publication Décrets :", fields.getTp_ec_pub_deb());
		
		if (fields.getTp_ec_pub_fin() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_parl_en_cours:nxw_intervalle_tp_ec_publication_decret_widget:nxw_intervalle_tp_ec_publication_decret_widget_toInputDate",
					"Intervalle de publication Décrets :", fields.getTp_ec_pub_fin());
		
		if (fields.getTl_ec_prom_deb() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_legis_en_cours:nxw_intervalle_tl_ec_promulgation_loi_widget:nxw_intervalle_tl_ec_promulgation_loi_widget_fromInputDate",
					"Intervalle de promulgation Lois :", fields.getTl_ec_prom_deb());
		
		if (fields.getTl_ec_prom_fin() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_legis_en_cours:nxw_intervalle_tl_ec_promulgation_loi_widget:nxw_intervalle_tl_ec_promulgation_loi_widget_toInputDate",
					"Intervalle de promulgation Lois :", fields.getTl_ec_prom_fin());

		if (fields.getTl_ec_pub_deb() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_legis_en_cours:nxw_intervalle_tl_ec_publication_decret_widget:nxw_intervalle_tl_ec_publication_decret_widget_fromInputDate",
					"Intervalle de publication Décrets :", fields.getTl_ec_pub_deb());
		
		if (fields.getTl_ec_pub_fin() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_legis_en_cours:nxw_intervalle_tl_ec_publication_decret_widget:nxw_intervalle_tl_ec_publication_decret_widget_toInputDate",
					"Intervalle de publication Décrets :", fields.getTl_ec_pub_fin());

		// legislatures precedente

		if (fields.getPre_debutLegislature() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_debut_legis_preced:nxw_preced_debut_legislatureInputDate",
					"Début de la législature", fields.getPre_debutLegislature());

		if (fields.getBs_pre_prom_deb() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_bs_preced:nxw_intervalle_bs_preced_promulgation_loi_widget:nxw_intervalle_bs_preced_promulgation_loi_widget_fromInputDate",
					"Intervalle de promulgation Lois :", fields.getBs_pre_prom_deb());
		
		if (fields.getBs_pre_prom_fin() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_bs_preced:nxw_intervalle_bs_preced_promulgation_loi_widget:nxw_intervalle_bs_preced_promulgation_loi_widget_toInputDate",
					"Intervalle de promulgation Lois :", fields.getBs_pre_prom_fin());

		if (fields.getBs_pre_pub_deb() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_bs_preced:nxw_intervalle_bs_preced_publication_decret_widget:nxw_intervalle_bs_preced_publication_decret_widget_fromInputDate",
					"Intervalle de publication Décrets :", fields.getBs_pre_pub_deb());
		
		if (fields.getBs_pre_pub_fin() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_bs_preced:nxw_intervalle_bs_preced_publication_decret_widget:nxw_intervalle_bs_preced_publication_decret_widget_toInputDate",
					"Intervalle de publication Décrets :", fields.getBs_pre_pub_fin());

		if (fields.getTp_pre_prom_deb() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_parl_preced:nxw_intervalle_tp_preced_promulgation_loi_widget:nxw_intervalle_tp_preced_promulgation_loi_widget_fromInputDate",
					"Intervalle de promulgation Lois :", fields.getTp_pre_prom_deb());
		
		if (fields.getTp_pre_prom_fin() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_parl_preced:nxw_intervalle_tp_preced_promulgation_loi_widget:nxw_intervalle_tp_preced_promulgation_loi_widget_toInputDate",
					"Intervalle de promulgation Lois :", fields.getTp_pre_prom_fin());

		if (fields.getTp_pre_pub_deb() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_parl_preced:nxw_intervalle_tp_preced_publication_decret_widget:nxw_intervalle_tp_preced_publication_decret_widget_fromInputDate",
					"Intervalle de publication Décrets :", fields.getTp_pre_pub_deb());
		
		if (fields.getTp_pre_pub_fin() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_parl_preced:nxw_intervalle_tp_preced_publication_decret_widget:nxw_intervalle_tp_preced_publication_decret_widget_toInputDate",
					"Intervalle de publication Décrets :", fields.getTp_pre_pub_fin());

		if (fields.getTl_pre_prom_deb() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_legis_preced:nxw_intervalle_tl_preced_promulgation_loi_widget:nxw_intervalle_tl_preced_promulgation_loi_widget_fromInputDate",
					"Intervalle de promulgation Lois :", fields.getTl_pre_prom_deb());
		
		if (fields.getTl_pre_prom_fin() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_legis_preced:nxw_intervalle_tl_preced_promulgation_loi_widget:nxw_intervalle_tl_preced_promulgation_loi_widget_toInputDate",
					"Intervalle de promulgation Lois :", fields.getTl_pre_prom_fin());

		if (fields.getTl_pre_pub_deb() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_legis_preced:nxw_intervalle_tl_preced_publication_decret_widget:nxw_intervalle_tl_preced_publication_decret_widget_fromInputDate",
					"Intervalle de publication Décrets :", fields.getTl_pre_pub_deb());
		
		if (fields.getTl_pre_pub_fin() != null)
			fillElement("displayedStatsParametersPopup:configureStatsParametersForm:nxl_taux_legis_preced:nxw_intervalle_tl_preced_publication_decret_widget:nxw_intervalle_tl_preced_publication_decret_widget_toInputDate",
					"Intervalle de publication Décrets :", fields.getTl_pre_pub_fin());
		getFlog().endAction();
	}

	public void validerParamStatsPopUp() {
		getFlog().startAction("validation du formulaire de la pop up de paramétrage");
		clickBtn(getDriver().findElement(By.id("displayedStatsParametersPopup:configureStatsParametersForm:validParamAN")));
		getFlog().endAction();
	}

	
	public void checkExportPopupValues(ParamStatField fields) {
		getFlog().startAction("Vérification des valeurs de la popup d'export");

		String tmp = "";
		waitElementVisibilityById("displayedExportReportParametersPopup:configureReportsParametersForm:nxl_an_intervalle_bilanSemestriel_promulgation:nxw_an_intervalle_bilanSemestriel_promulgation_widget:nxw_an_intervalle_bilanSemestriel_promulgation_widget_fromInputDate");
		
		// Bilan Semestriel
		tmp = findElement(By.id("displayedExportReportParametersPopup:configureReportsParametersForm:nxl_an_intervalle_bilanSemestriel_promulgation:" +
				"nxw_an_intervalle_bilanSemestriel_promulgation_widget:nxw_an_intervalle_bilanSemestriel_promulgation_widget_fromInputDate")).getAttribute("value");
		Assert.assertEquals("Echec de comparaison de la date de début de promulgation du Bilan semestriel", fields.getBs_ec_prom_deb(), tmp);
		
		tmp = findElement(By.id("displayedExportReportParametersPopup:configureReportsParametersForm:nxl_an_intervalle_bilanSemestriel_promulgation:" +
				"nxw_an_intervalle_bilanSemestriel_promulgation_widget:nxw_an_intervalle_bilanSemestriel_promulgation_widget_toInputDate")).getAttribute("value");
		Assert.assertEquals("Echec de comparaison de la date de fin de promulgation du Bilan semestriel", fields.getBs_ec_prom_fin(), tmp);

		tmp = findElement(By.id("displayedExportReportParametersPopup:configureReportsParametersForm:nxl_an_intervalle_bilanSemestriel_publication:" +
				"nxw_an_intervalle_bilanSemestriel_publication_widget:nxw_an_intervalle_bilanSemestriel_publication_widget_fromInputDate")).getAttribute("value");
		Assert.assertEquals("Echec de comparaison de la date de début de publication du Bilan semestriel", fields.getBs_ec_pub_deb(), tmp);
		
		tmp = findElement(By.id("displayedExportReportParametersPopup:configureReportsParametersForm:nxl_an_intervalle_bilanSemestriel_publication:" +
				"nxw_an_intervalle_bilanSemestriel_publication_widget:nxw_an_intervalle_bilanSemestriel_publication_widget_toInputDate")).getAttribute("value");
		Assert.assertEquals("Echec de comparaison de la date de fin de publication du Bilan semestriel", fields.getBs_ec_pub_fin(), tmp);

		// taux d'execution des lois

		tmp = findElement(By.id("displayedExportReportParametersPopup:configureReportsParametersForm:nxl_an_intervalle_taux_legislature_promulgation:" +
				"nxw_an_intervalle_taux_legislature_promulgation_widget:nxw_an_intervalle_taux_legislature_promulgation_widget_fromInputDate")).getAttribute("value");
		Assert.assertEquals("Echec de comparaison de la date de début de promulgation du Taux d'exécution (législature)", fields.getTl_ec_prom_deb(), tmp);
		
		tmp = findElement(By.id("displayedExportReportParametersPopup:configureReportsParametersForm:nxl_an_intervalle_taux_legislature_promulgation:" +
				"nxw_an_intervalle_taux_legislature_promulgation_widget:nxw_an_intervalle_taux_legislature_promulgation_widget_toInputDate")).getAttribute("value");
		Assert.assertEquals("Echec de comparaison de la date de fin de promulgation du Taux d'exécution (législature)", fields.getTl_ec_prom_fin(), tmp);

		tmp = findElement(By.id("displayedExportReportParametersPopup:configureReportsParametersForm:nxl_an_intervalle_taux_legislature_publication:" +
				"nxw_an_intervalle_taux_legislature_publication_widget:nxw_an_intervalle_taux_legislature_publication_widget_fromInputDate")).getAttribute("value");
		Assert.assertEquals("Echec de comparaison de la date de début de publication du Taux d'exécution (législature)",fields.getTl_ec_pub_deb(), tmp);
		
		tmp = findElement(By.id("displayedExportReportParametersPopup:configureReportsParametersForm:nxl_an_intervalle_taux_legislature_publication:" +
				"nxw_an_intervalle_taux_legislature_publication_widget:nxw_an_intervalle_taux_legislature_publication_widget_toInputDate")).getAttribute("value");
		Assert.assertEquals("Echec de comparaison de la date de fin de publication du Taux d'exécution (législature)", fields.getTl_ec_pub_fin(), tmp);
		 
		//Début de législature

		tmp = findElement(By.id("displayedExportReportParametersPopup:configureReportsParametersForm:nxl_an_date_debut_legislature:nxw_an_date_debut_legislatureInputDate")).getAttribute("value");
		Assert.assertEquals("Echec de comparaison de la date de début de la législature", fields.getEc_debutLegislature(), tmp);

		// indicateurs lolf

		tmp = findElement(By.id("displayedExportReportParametersPopup:configureReportsParametersForm:nxl_an_intervalle_taux_parlementaire_promulgation:" +
				"nxw_an_intervalle_taux_parlementaire_promulgation_widget:nxw_an_intervalle_taux_parlementaire_promulgation_widget_fromInputDate")).getAttribute("value");
		Assert.assertEquals("Echec de comparaison de la date de début de promulgation du Taux d'exécution (parlementaire)", fields.getTp_ec_prom_deb(), tmp);
		
		tmp = findElement(By.id("displayedExportReportParametersPopup:configureReportsParametersForm:nxl_an_intervalle_taux_parlementaire_promulgation:" +
				"nxw_an_intervalle_taux_parlementaire_promulgation_widget:nxw_an_intervalle_taux_parlementaire_promulgation_widget_toInputDate")).getAttribute("value");
		Assert.assertEquals("Echec de comparaison de la date de fin de promulgation du Taux d'exécution (parlementaire)", fields.getTp_ec_prom_fin(), tmp);

		tmp = findElement(By.id("displayedExportReportParametersPopup:configureReportsParametersForm:nxl_an_intervalle_taux_parlementaire_publication:" +
				"nxw_an_intervalle_taux_parlementaire_publication_widget:nxw_an_intervalle_taux_parlementaire_publication_widget_fromInputDate")).getAttribute("value");
		Assert.assertEquals("Echec de comparaison de la date de début de publication du Taux d'exécution (parlementaire)", fields.getTp_ec_pub_deb(), tmp);
		
		tmp = findElement(By.id("displayedExportReportParametersPopup:configureReportsParametersForm:nxl_an_intervalle_taux_parlementaire_publication:" +
				"nxw_an_intervalle_taux_parlementaire_publication_widget:nxw_an_intervalle_taux_parlementaire_publication_widget_toInputDate")).getAttribute("value");
		Assert.assertEquals("Echec de comparaison de la date de fin de publication du Taux d'exécution (parlementaire)", fields.getTp_ec_pub_fin(), tmp);
		
		getFlog().endAction();

	}

	/**
	 * Ajoute une législature à la liste des legislature
	 * Nécessite que la popup soit ouverte
	 * @param legis
	 */
	public void addLegislature(String legis) {
		fillField("Législatures", inputLegis, legis);		
		
		WebElement buttonAdd = findElement(By.id(addLegisBtnId));
		buttonAdd.click();
		sleep(2);
	}
	
	/**
	 * Sélectionne une législature en cours
	 * Nécessite que la popup soit ouverte et que la législature soit présente dans la liste
	 * @param legis
	 */
	public void setLegislatureEnCours(String legis) {
		getFlog().action("Selectionne \"" + legis + "\" dans le select \"Législature en cours\"");
		final Select select = new Select(findElement(By.xpath("//select[contains(@id,'nxw_legislature_encours_selectOneMenu')]")));
		select.selectByValue(legis);
	}
}
