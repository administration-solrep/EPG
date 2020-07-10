package fr.dila.solonepg.webtest.common;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import fr.dila.solonepg.page.commun.LoginPage;
import fr.dila.solonepg.page.main.onglet.TraitementPage;
import fr.dila.solonepg.webtest.helper.url.UrlEpgHelper;
import fr.dila.st.webdriver.model.STUser;
import fr.dila.st.webdriver.ws.LoginWsPage;
import fr.dila.st.webdriver.ws.WsResultPage;
import fr.sword.naiad.commons.webtest.WebPage;
import fr.sword.naiad.commons.webtest.helper.AbstractWebTest;
import fr.sword.naiad.commons.webtest.logger.FunctionalLogger;
import junit.framework.Assert;

public class AbstractEpgWebTest extends AbstractWebTest {

	public static final int		TIMEOUT_SEC	= 20;
	private static final String	WS_EPG		= "/technique/xml_post.html";

	protected boolean			userLogged	= false;

	// **************************LOGIN****************************//
	public TraitementPage loginAsAdminEpg() {
		getFlog().action("Connexion en tant qu'admin");
		return loginEpg("adminsgg", "adminsgg", TraitementPage.class);
	}

	/** On se connecte en considérant que login=mot de passe **/
	public TraitementPage loginEpg(String username) {
		return loginEpg(username, username);
	}

	private TraitementPage loginEpg(String username, String password) {
		getFlog().action("Connexion en tant que " + username);
		return loginEpg(username, password, TraitementPage.class);
	}

	public static String getLoginEpgUrl() {
		String appUrl = ((UrlEpgHelper) UrlEpgHelper.getInstance()).getEpgUrl();
		String loginUrl = appUrl;
		if (!loginUrl.endsWith("/")) {
			loginUrl += "/";
		}
		loginUrl += "login.jsp";
		return loginUrl;
	}

	public static String getLoginEpgWSUrl() {
		String appUrl = ((UrlEpgHelper) UrlEpgHelper.getInstance()).getEpgUrl();
		String loginUrl = appUrl;
		if (!loginUrl.endsWith("/")) {
			loginUrl += "/";
		}
		loginUrl += WS_EPG;
		return loginUrl;
	}

	public LoginPage goToLoginWSPage() {
		String loginUrl = getLoginEpgWSUrl();
		getFlog().action("Acces à la page de login [" + loginUrl + "]");
		return WebPage.goToPage(getDriver(), getFlog(), loginUrl, LoginPage.class);
	}

	public LoginPage goToLoginEpgPage() {
		String loginUrl = getLoginEpgUrl();
		getFlog().action("Acces à la page de login [" + loginUrl + "]");
		LoginPage page = WebPage.goToPage(getDriver(), getFlog(), loginUrl, LoginPage.class);
		page.waitForPageLoaded(getDriver());
		return page;
	}

	public <T extends WebPage> T loginEpg(String username, String password, Class<T> pageClazz) {
		Assert.assertFalse(userLogged);
		userLogged = true;
		LoginPage loginPage = goToLoginEpgPage();
		T page = loginPage.submitLogin(username, password, pageClazz);
		page.waitForPageSourcePart("'id':'espace_recherche'", TIMEOUT_SEC);
		return page;
	}

	public <T extends WebPage> T loginEpg(STUser sTUser, Class<T> pageClazz) {
		LoginPage loginPage = goToLoginEpgPage();
		return loginPage.submitLogin(sTUser.getLogin(), sTUser.getPassword(), pageClazz);
	}

	// *************************LOGOUT**************************//

	public LoginPage logoutWS() {
		String logoutUrl = getLogoutEpgUrl();
		getFlog().action("Acces au logout [" + logoutUrl + "]");
		Assert.assertTrue(userLogged);
		userLogged = false;
		return WebPage.goToPage(getDriver(), getFlog(), logoutUrl, LoginPage.class);
	}

	public WsResultPage loginWSEpg(String username, String password, String url, String xml) {
		getFlog().action("Ouverture de la page de login de WS");
		LoginWsPage loginWsPage = WebPage.goToPage(getDriver(), getFlog(), getLoginEpgWSUrl(), LoginWsPage.class);
		userLogged = true;
		return loginWsPage.submitLogin(username, password, url, xml, WsResultPage.class);
	}

	public static String getLogoutEpgUrl() {
		String appUrl = ((UrlEpgHelper) UrlEpgHelper.getInstance()).getEpgUrl();
		String loginUrl = appUrl;
		if (!loginUrl.endsWith("/")) {
			loginUrl += "/";
		}
		loginUrl += "logout";
		return loginUrl;
	}

	public LoginPage logoutEpg() {
		String logoutUrl = getLogoutEpgUrl();
		getFlog().action("Acces au logout [" + logoutUrl + "]");
		Assert.assertTrue(userLogged);
		userLogged = false;
		return WebPage.goToPage(getDriver(), getFlog(), logoutUrl, LoginPage.class);
	}

	/**
	 * Retourne le loggueur fonctionnel
	 */
	@Override
	public FunctionalLogger getFlog() {
		return (FunctionalLogger) super.getFlog();
	}

	/**
	 * Maximize the browser
	 */
	protected void maximizeBrowser() {

		getDriver().manage().window().setPosition(new Point(0, 0));
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dim = new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight());
		getDriver().manage().window().setSize(dim);
	}

	// Connexion pour les tests liés à la présence du verrou
	public TraitementPage loginMinSPO_Epg() {
		getFlog().action("Connexion en tant qu'admin");
		return loginEpg("JGOMIS", "JGOMIS", TraitementPage.class);
	}

	public TraitementPage loginMinOME_Epg() {
		getFlog().action("Connexion en tant qu'admin");
		return loginEpg("NGUIRIABOYE", "NGUIRIABOYE", TraitementPage.class);
	}

	public TraitementPage loginContribMinOME() {
		getFlog().action("Connexion en tant que contributeur Min OME");
		return loginEpg("TLANDRY", "TLANDRY", TraitementPage.class);
	}

	public TraitementPage loginContribMinSPO() {
		getFlog().action("Connexion en tant que contributeur Min SPO");
		return loginEpg("CBELGACEM", "CBELGACEM", TraitementPage.class);
	}

}
