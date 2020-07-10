package fr.dila.solonmgpp.page.epg.commun;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonmgpp.page.epg.mgpp.decretpr.DecretPrPage;
import fr.dila.solonmgpp.page.epg.mgpp.jss.JoursSupplementairesSeancePage;

public class BandeauMenuMgppOrganisation extends SolonEpgPage {

    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Décret du Président de la République")
    private WebElement decretPR;

    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Jours supplémentaires de séance")
    private WebElement joursSupplémentaires;

    /**
     * Va vers l'espace décrêts président de la république
     * 
     * @return
     */
    public DecretPrPage goToDecretPR() {
        return click(decretPR, DecretPrPage.class);
    }

    /**
     * Va vers l'espace décrêts président de la république
     * 
     * @return
     */
    public JoursSupplementairesSeancePage goToJoursSupplementairesSeance() {
        return click(joursSupplémentaires, JoursSupplementairesSeancePage.class);
    }
}
