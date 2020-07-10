package fr.dila.solonepg.webtest.webdriver020.report.validator;

import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import fr.dila.solonepg.page.commun.EPGWebPage;

public abstract class AbstractReportValidator extends EPGWebPage {

    public void trigger(String script, WebElement element) {
        ((JavascriptExecutor) this.getDriver()).executeScript(script, element);
    }

    public Object trigger(String script) {
        return ((JavascriptExecutor) this.getDriver()).executeScript(script);
    }

    private void openTab(String url) {
        String script = "var d=document,a=d.createElement('a');a.target='_blank';a.href='%s';a.innerHTML='.';d.body.appendChild(a);return a";
        Object element = trigger(String.format(script, url));
        if (element instanceof WebElement) {
            WebElement anchor = (WebElement) element;
            anchor.click();
            trigger("var a=arguments[0];a.parentNode.removeChild(a);", anchor);
        } else {
            throw new JavaScriptException(element, "Unable to open tab", 1);
        }
    }

    public void openReportInNewTab() {
        this.openTab(this.getPath());
    }

    public abstract void validate(String reportName);

    protected abstract String getPath();
}
