package fr.dila.solonmgpp.page.epg.ws;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.sword.naiad.commons.webtest.WebPage;

public class LoginWsPage extends WebPage{
  
  @FindBy(how = How.ID, id = "username")
  private WebElement usernameElt;

  @FindBy(how = How.ID, id = "password")
  private WebElement passwordElt;

  @FindBy(how = How.ID, id = "url")
  private WebElement urlElt;
  
  @FindBy(how = How.ID, id = "request")
  private WebElement requestElt;
  
  @FindBy(how = How.ID, id = "submit")
  private WebElement okButtonElt;

  /**
   * Soumet le formulaire de login
   */
  public <T extends WebPage> T submitLogin(final String login, final String password, final String url, final String request, final Class<T> pageClazz) {
      getFlog().startAction(String.format("Connexion Web Service en tant que [%s]", login));
      setUsername(login);
      setPassword(password);
      setUrl(url);
      setRequest(request);
      submit();
      
      getFlog().endAction();
      return getPage(pageClazz);
  }

  private void submit() {
      getFlog().actionClickButton("Connexion");
      okButtonElt.click();
  }

  public void setUsername(final String username) {
      fillField("Identifiant", usernameElt, username);
  }

  public void setPassword(final String password) {
      fillField("Mot de passe", passwordElt, password);
  }
  
  public void setUrl(final String url) {
      fillField("Url", urlElt, url);
  }

  public void setRequest(final String request) {
      fillField("Xml", requestElt, request);
  }

}
