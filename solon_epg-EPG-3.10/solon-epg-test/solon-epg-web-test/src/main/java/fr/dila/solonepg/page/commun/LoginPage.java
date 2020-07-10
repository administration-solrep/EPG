package fr.dila.solonepg.page.commun;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.sword.naiad.commons.webtest.WebPage;

public class LoginPage extends EPGWebPage {

	public static final String RENSEIGNEMENT_LINKTEXT = "Pour tout renseignement sur les conditions";

	/**
	 * Valeur extraite du template de parametre dans solonepg-content-template-contrib.xml
	 */
	private static final String RENSEIGNEMENT_LINK_URL = "http://extraqual.pm.ader.gouv.fr/demat/index.html";
	
    @FindBy(how = How.ID, id = "username")
    private WebElement usernameElt;

    @FindBy(how = How.ID, id = "password")
    private WebElement passwordElt;

    @FindBy(how = How.XPATH, using = "//input[@class='login_button']")
    private WebElement loginButtonElt;

    /**
     * Soumet le formulaire de login
     */
    public <T extends WebPage> T submitLogin(final String login, final String password, final Class<T> pageClazz) {
        getFlog().startAction(String.format("Connexion en tant que [%s]", login));
        waitForPageLoaded(getDriver());
        setUsername(login);
        setPassword(password);
        submit();
        getFlog().endAction();
        return getPage(pageClazz);
    }

    private void submit() {
        WebElement element = getElementByCssSelector("body");
        getFlog().actionClickButton("Connexion");
        loginButtonElt.click();
        // wait for page reloading
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(element));
        waitForPageLoaded(getDriver());
    }

    public void setUsername(final String username) {
        usernameElt.click();
        fillField("Identifiant", usernameElt, username);
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(new ExpectedCondition<String>() {
            @Override
            public String apply(WebDriver driver) {
                return usernameElt.getAttribute("value").equals(username) ? username : null;
            }
        });
    }

    public void setPassword(final String password) {
        passwordElt.click();
        fillField("Mot de passe", passwordElt, password);
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(new ExpectedCondition<String>() {
            @Override
            public String apply(WebDriver driver) {
                return passwordElt.getAttribute("value").equals(password) ? password : null;
            }
        });
    }
    
    public void checkLinkRenseignement(){
		getFlog().check("Test la présence du lien ("+RENSEIGNEMENT_LINKTEXT+"...)");
		if(!hasLinkRenseignement()){
			getFlog().checkFailed("Le lien (" + RENSEIGNEMENT_LINKTEXT+"...) n'est pas présent");
		}
	}
    
    private boolean hasLinkRenseignement() {
		final List<WebElement> webElements = getElementsBy(By.partialLinkText(RENSEIGNEMENT_LINKTEXT));
		if (webElements.isEmpty()) {
			return false;
		}
		WebElement webElement = webElements.get(0);
		String url = webElement.getAttribute("href");
		return url != null && url.equals(RENSEIGNEMENT_LINK_URL);
	}

}
