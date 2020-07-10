package fr.dila.solonmgpp.page.epg.commun;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonmgpp.page.epg.mgpp.rapport.DepotDeRapportPage;
import fr.dila.solonmgpp.page.epg.mgpp.rapport.DocumentPage;

public class BandeauMenuMgppRapport extends SolonEpgPage {

    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Dépôt de rapport")
    private WebElement depotRapport;

    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Documents")
    private WebElement document;

    /**
     * Va vers l'espace de dépôt de rapport
     * 
     * @return
     */
    public DepotDeRapportPage goToDeportRapport() {
        return click(depotRapport, DepotDeRapportPage.class);
    }

    /**
     * Va vers l'espace de Document
     * 
     * @return
     */
    public DocumentPage goToDocumment() {
        return click(document, DocumentPage.class);
    }

}
