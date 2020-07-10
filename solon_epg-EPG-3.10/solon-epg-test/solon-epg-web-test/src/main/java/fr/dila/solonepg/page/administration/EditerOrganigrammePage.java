package fr.dila.solonepg.page.administration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class EditerOrganigrammePage extends EPGWebPage {

	private static final String	SUIVI_AN_ID			= "editEntiteForm:nxl_entite:nxw_suivi_activite_normative_select:0";

	private static final String	ENREGISTRER_BTN_ID	= "editEntiteForm:button_edit";

	public static final String	PRENOM_MEMBRE_ID	= "editEntiteForm:nxl_entite:nxw_membre_gouvernement_prenom";

	public static final String	NOM_MEMBRE_ID		= "editEntiteForm:nxl_entite:nxw_membre_gouvernement_nom";

	public void setSuiviAn() {
		this.getElementById(SUIVI_AN_ID).click();
	}

	public void setNorEntiteParent(String entiteHtmlId, String value) {
		getElementById(entiteHtmlId).sendKeys(value);
	}

	public void enregistrer() {
		this.getElementById(ENREGISTRER_BTN_ID).click();
	}

	public void setPrenomMembreGouvernement(String name) {
		WebElement element = this.getElementById(PRENOM_MEMBRE_ID);
		this.fillField("Prénom resp. dossier", element, name);
	}

	public void setNomMembreGouvernement(String name) {
		WebElement element = this.getElementById(NOM_MEMBRE_ID);
		this.fillField("Prénom resp. dossier", element, name);
	}

}
