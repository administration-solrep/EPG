package fr.dila.solonepg.page.administration;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class CreationEtapePage extends EPGWebPage {

	private static final String FIND_DESTINATAIRE		= "document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_findButton";

	private static final String	TYPE_ETAPE_LIST_ID	= "document_create:nxl_routing_task_detail:routing_task_type";

	public void setTypeEtape(String textValue) {
		final WebElement typeDacteSelect = getDriver().findElement(By.id(TYPE_ETAPE_LIST_ID));
		getFlog().action("Selectionne \"" + textValue + "\" dans le select \"Type d'etape\"");
		final Select select = new Select(typeDacteSelect);
		select.selectByVisibleText(textValue);
	}

	public void setDestinataire(List<String> ar) {
		selectDestinataireFromOrganigramme(ar, FIND_DESTINATAIRE);
	}

	public CreationModelePage creer() {
		getDriver().findElement(By.id("document_create:button_create")).click();
		waitForPageSourcePart("Etape enregistrée", TIMEOUT_IN_SECONDS);
		return getPage(CreationModelePage.class);
	}

}
