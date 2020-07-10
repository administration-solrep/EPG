package fr.dila.solonmgpp.page.epg.ws;

import org.openqa.selenium.By;

import fr.dila.solonmgpp.page.epg.commun.SolonEpgPage;

public class WsResultPage extends SolonEpgPage {

    public void doSubmit(){
        getDriver().findElement(By.xpath("//*[@value='OK']")).click();
        waitForPageLoaded(getDriver());
    }
    
}
