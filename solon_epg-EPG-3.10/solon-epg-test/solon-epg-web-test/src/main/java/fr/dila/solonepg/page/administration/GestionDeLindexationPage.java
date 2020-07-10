package fr.dila.solonepg.page.administration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class GestionDeLindexationPage extends EPGWebPage {

	private static final String	RUBRIQUE_INPUT_ID	= "add_rubrique:input_rubrique";
	private static final String	ADD_RUBRIQUE_ID		= "add_rubrique:addRCommandLink";

	private static final String	MOT_CLE_INPUT_ID	= "add_mcl:input_motcle";
	private static final String	ADD_MOT_CLE_ID		= "add_mcl:_findButton";

	private static final String	MOT_CLE_C_INPUT_ID	= "add_mc:input_motcleC";
	private static final String	ADD_MOT_CLE_C_ID	= "add_mc:addMC";

	/**
	 * add rubrique
	 * 
	 * @param value
	 */
	public void addRubriques(String value) {

		waitForPageSourcePart(By.id(RUBRIQUE_INPUT_ID), TIMEOUT_IN_SECONDS);
		WebElement rubEl = getElementById(RUBRIQUE_INPUT_ID);

		List<String> allValues = new ArrayList<String>();
		if (value.contains(";")) {
			allValues.addAll(Arrays.asList(value.split(";")));
		} else {
			allValues.add(value);
		}

		this.fillField("", rubEl, value);
		this.getElementById(ADD_RUBRIQUE_ID).click();
		String lastValue = allValues.get(allValues.size() - 1);
		waitForPageSourcePart(lastValue, TIMEOUT_IN_SECONDS);

	}

	public void addMotCle(String value, List<String> ar) {
		waitForPageSourcePartDisplayed(By.id(MOT_CLE_INPUT_ID), TIMEOUT_IN_SECONDS);
		WebElement motCle = this.getElementById(MOT_CLE_INPUT_ID);
		this.fillField("", motCle, value);
		waitForPageSourcePartDisplayed(By.id(ADD_MOT_CLE_ID), TIMEOUT_IN_SECONDS);

		getFlog().startAction("Ouverture de l'organigramme");
		this.selectItemInOrganigramme(ADD_MOT_CLE_ID, ar);
		getFlog().endAction();
	}

	public void addMotCle_C(String value) {
		WebElement motCle = this.getElementById(MOT_CLE_C_INPUT_ID);
		this.fillField("", motCle, value);

		this.getElementById(ADD_MOT_CLE_C_ID).click();

		List<String> allValues = new ArrayList<String>();
		if (value.contains(";")) {
			allValues.addAll(Arrays.asList(value.split(";")));
		} else {
			allValues.add(value);
		}
		String lastValue = allValues.get(allValues.size() - 1);
		waitForPageSourcePart(lastValue, TIMEOUT_IN_SECONDS);

		for (String mtc : allValues) {
			this.assertTrue(mtc);
		}

	}

	public void selectItemInOrganigramme(String buttonId, List<String> listId) {

		this.getElementById(buttonId).click();
		this.waitForPageSourcePart(By.xpath("//*[@class=\"rich-mp-container\"]"), TIMEOUT_IN_SECONDS);

		for (int i = 0; i < listId.size(); i++) {
			String current = listId.get(i);
			waitForPageSourcePartDisplayed(By.id(current), TIMEOUT_IN_SECONDS);
			this.getElementById(current).click();
			if (i < listId.size() - 1) {
				String next = listId.get(i + 1);
				// Wait the visibility of next item
				this.waitElementVisibilityById(next);
			}
		}
		this.waitForPageSourcePartHide(By.xpath("//*[@class=\"rich-mp-container\"]"), TIMEOUT_IN_SECONDS);

	}

}
