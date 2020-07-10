package fr.dila.solonmgpp.page;

import java.util.ArrayList;
import java.util.Iterator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Organigramme extends fr.dila.solonepg.page.commun.Organigramme {

    public void openOrganigramme(final WebElement baseElement) {
        // final WebElement elementFind = baseElement.findElement(By.xpath(".//span/table/tbody/tr/td/a/img"));
        // elementFind.click();
        baseElement.click();
        waitForPageSourcePart(By.xpath("//*[@class=\"rich-modalpanel\"]"), TIMEOUT_IN_SECONDS);
    }

    public void openEPPOrganigramme(final WebElement baseElement) {
        final WebElement elementFind = baseElement.findElement(By.xpath(".//span/table/tbody/tr/td/a/img"));
        elementFind.click();
        waitForPageSourcePart(By.xpath("//*[@class=\"rich-modalpanel\"]"), TIMEOUT_IN_SECONDS);
    }

    public void selectMultipleElement(final ArrayList<String> elements) {
        if (elements != null && elements.size() > 0) {
            Iterator<String> it = elements.iterator();
            while (it.hasNext()) {
                String element_id = it.next();
                new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='" + element_id + "']")));
                final WebElement element = getDriver().findElement(By.xpath("//*[@id='" + element_id + "']"));
                element.click();
                // final WebElement td = spanElement.findElement(By.xpath(".."));
                // final WebElement addButton = td.findElement(By.xpath(".//a/img"));
                // addButton.click();
            }

            waitForPageSourcePartHide(By.xpath("//*[@class=\"rich-modalpanel\"]"), TIMEOUT_IN_SECONDS);
        }

    }

}
