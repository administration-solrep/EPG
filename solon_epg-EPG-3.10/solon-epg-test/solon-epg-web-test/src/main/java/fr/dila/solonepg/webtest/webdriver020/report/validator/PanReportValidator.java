package fr.dila.solonepg.webtest.webdriver020.report.validator;

import java.util.Set;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;

public class PanReportValidator extends AbstractReportValidator {

    @Override
    public void validate(String reportName) {

        // Save The main window
        String mainWindowHandle = this.getDriver().getWindowHandle();

        String e1 = "The following items have errors";
        String e2 = "Can not load the report query";
        try {
            // Open report in new tab
            this.openReportInNewTab();
            // Switch to the report tab
            this.switchWindow();

            boolean errorElement1 = !getElementsBy(By.xpath("//*[contains(text(), \"" + e1 + "\")]")).isEmpty();
            boolean errorElement2 = !getElementsBy(By.xpath("//*[contains(text(), \"" + e2 + "\")]")).isEmpty();
            if (errorElement1 || errorElement2) {
                getFlog().checkFailed("Erreur lors de la validation du rapport '" + reportName + "'");
                Assert.fail("Erreur lors de la validation du rapport '" + reportName + "'");
            }

        } finally {
            // Close Current Tab=>Report Tab
            this.getDriver().close();
            // Back to main tab
            this.getDriver().switchTo().window(mainWindowHandle);
        }

    }

    public void switchWindow() throws NoSuchWindowException, NoSuchWindowException {
        Set<String> handles = this.getDriver().getWindowHandles();
        String current = this.getDriver().getWindowHandle();
        handles.remove(current);
        String newTab = handles.iterator().next();
        this.getDriver().switchTo().window(newTab);
    }

    @Override
    protected String getPath() {
        WebElement el = this.getElementByXpath("//iframe");
        if (el != null) {
            return el.getAttribute("src");
        }
        return null;
    }

}
