package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;

import fr.dila.solonmgpp.page.commun.CommonWebPage;
import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.sword.naiad.commons.webtest.WebPage;

public class MgppCreateComm28Page extends AbstractMgppCreateComm{

    public static final String OBJET = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_objet";
    public static final String DATE_PROMULGATION_INPUT = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_datePromulgationInputDate";
    public static final String NUMERO_NOR = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_nor";
    public static final String TITRE = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_titre";
    public static final String DATE_PUBLICATION = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_datePublicationInputDate";

    
    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return null;
    }

    public <T extends WebPage> T fillObjetEtDatePromulgation(final String objet, final String datePromulgation, final Class<T> pageClazz) {
        fillField("Objet", getDriver().findElement(By.id(OBJET)), objet);
        fillField("Date promulgation", getDriver().findElement(By.id(DATE_PROMULGATION_INPUT)), datePromulgation);
        getDriver().findElement(By.xpath("//*[@value='Publier']")).click();
        waitForPageLoaded(getDriver());
        return getPage(pageClazz);
    }
    
    public void checkValues(final String numero_nor, final String titre, final String datePublication){
        checkValue(NUMERO_NOR, numero_nor, Boolean.FALSE);
        checkValue(TITRE, titre, Boolean.FALSE);
        checkValue(DATE_PUBLICATION, datePublication, Boolean.FALSE);
        
    }
}
