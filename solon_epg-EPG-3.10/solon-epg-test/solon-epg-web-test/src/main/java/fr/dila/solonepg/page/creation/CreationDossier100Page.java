package fr.dila.solonepg.page.creation;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreationDossier100Page extends AbstractCreationDossierPage {

	public static final String	CREATE_DOSSIER			= "formCreationDossier:buttonCreationDossier";
	public static final String	TYPE_ACTE_LIST_ID		= "creation_dossier_100:nxl_creation_dossier_layout_100_a:nxw_type_acte_field_select_one_menu";
	public static final String	BOUTON_SUIVANT_ID		= "creation_dossier_100:buttonSuivant";
	public static final String	MINISTERE_RESPONSABLE	= "creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_findButton";
	public static final String	DIRECTION_CONCERNE		= "creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_findButton";
	public static final String	BOUTON_TERMINER_ID		= "creation_dossier_100:buttonTerminer";
	public static final String	POSTE_CONCERNE			= "creation_dossier_select_poste:nxl_creation_dossier_layout_select_poste:nxw_dossier_creation_choix_poste_poste_findButton";

	public void createDossier() {
		getDriver().findElement(By.id(CREATE_DOSSIER)).click();
		waitForPageSourcePart("Création d'un dossier", TIMEOUT_IN_SECONDS);
	}

	public void setMinistereResponable(final String ministere) {
		selectInOrganigramme(ministere, MINISTERE_RESPONSABLE);
	}

	public void setMinistereResponable(final List<String> pathToMinistere) {
		selectMultipleInOrganigramme(pathToMinistere, MINISTERE_RESPONSABLE);
	}

	public void setDirectionConcerne(List<String> ar) {
		selectDestinataireFromOrganigramme(ar, DIRECTION_CONCERNE);
	}

	public void setPosteConcerne(List<String> ar) {
		selectDestinataireFromOrganigramme(ar, POSTE_CONCERNE);
	}

	public void setTypeActe(String textValue) {
		final WebElement typeDacteSelect = getDriver().findElement(By.id(TYPE_ACTE_LIST_ID));
		getFlog().action("Selectionne \"" + textValue + "\" dans le select \"Type d'acte\"");
		// le changement de type d'acte rafraichit le champ
		String id = typeDacteSelect.getAttribute("id");
		final Select select = new Select(typeDacteSelect);
		select.selectByVisibleText(textValue);
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(typeDacteSelect));
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
	}

	@Override
	protected String getButtonSuivantId() {
		return BOUTON_SUIVANT_ID;
	}

	@Override
	protected String getButtonTerminerId() {
		return BOUTON_TERMINER_ID;
	}

}
