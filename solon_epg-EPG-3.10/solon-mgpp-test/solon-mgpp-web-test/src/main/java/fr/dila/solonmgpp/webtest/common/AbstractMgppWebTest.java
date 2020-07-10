package fr.dila.solonmgpp.webtest.common;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.TimeoutException;

import fr.dila.solonepg.webtest.common.AbstractEpgWebTest;
import fr.dila.solonepg.page.commun.LoginPage;
import fr.dila.solonmgpp.page.epg.espaceEpg.TraitementPageForMGGP;
import fr.dila.solonmgpp.page.epg.ws.LoginWsPage;
import fr.dila.st.webdriver.ws.WsResultPage;
import fr.dila.solonepp.page.CorbeillePage;
import fr.dila.solonmgpp.webtest.helper.url.UrlEpgHelper;
import fr.dila.solonmgpp.webtest.helper.url.UrlEppHelper;
import fr.dila.st.webdriver.model.STUser;
import fr.sword.naiad.commons.webtest.WebPage;
import fr.sword.naiad.commons.webtest.logger.FunctionalLogger;

public class AbstractMgppWebTest extends AbstractEpgWebTest {
    
    private static final String WS_EPG =  "/technique/xml_post.html";

    //**************************LOGIN****************************//
    @Override
	public TraitementPageForMGGP loginAsAdminEpg(){
        getFlog().action("Connexion en tant qu'admin");
        return loginEpg("adminsgg", "adminsgg", TraitementPageForMGGP.class);
    }
    
    public CorbeillePage loginAsAdminEpp(){
        getFlog().action("Connexion en tant qu'admin");
        return loginEpp("adminsgg", "adminsgg", CorbeillePage.class);
    }
    
    public TraitementPageForMGGP loginEpg(String username, String password){
        getFlog().action("Connexion en tant qu'admin");
        return loginEpg(username, password, TraitementPageForMGGP.class);
    }
    
    @Override
	public WsResultPage loginWSEpg(String username, String password, String url, String xml){
        getFlog().action("Ouverture de la page de login de WS");
        LoginWsPage loginWsPage = WebPage.goToPage(getDriver(), getFlog(), getLoginEpgWSUrl(), LoginWsPage.class);        
        return loginWsPage.submitLogin(username, password, url, xml, WsResultPage.class);
    }    
    
    public CorbeillePage loginEpp(String username, String password){
        getFlog().action("Connexion en tant qu'admin");
        return loginEpp(username, password, CorbeillePage.class);
    }
    
    public static String getLoginEpgUrl(){
        String appUrl = ((UrlEpgHelper)UrlEpgHelper.getInstance()).getEpgUrl();
        String loginUrl = appUrl;
        if(!loginUrl.endsWith("/")){
            loginUrl += "/";
        }
        loginUrl += "login.jsp";
        return loginUrl;
    }
    
    public static String getLoginEpgWSUrl(){
        String appUrl = ((UrlEpgHelper)UrlEpgHelper.getInstance()).getEpgUrl();
        String loginUrl = appUrl;
        if(!loginUrl.endsWith("/")){
            loginUrl += "/";
        }
        loginUrl += WS_EPG ;
        return loginUrl;
    }
    

    @Override
	public LoginPage goToLoginWSPage(){
        String loginUrl = getLoginEpgWSUrl();
        getFlog().action("Acces à la page de login ["+loginUrl+"]");
        return WebPage.goToPage(getDriver(),getFlog(), loginUrl, LoginPage.class);
    }
    
    public static String getLoginEppUrl(){
        String appUrl = ((UrlEppHelper)UrlEppHelper.getInstance()).getEppUrl();
        String loginUrl = appUrl;
        if(!loginUrl.endsWith("/")){
            loginUrl += "/";
        }
        loginUrl += "login.jsp";
        return loginUrl;
    }
    
    @Override
	public LoginPage goToLoginEpgPage(){
        String loginUrl = getLoginEpgUrl();
        getFlog().action("Acces à la page de login ["+loginUrl+"]");
        return WebPage.goToPage(getDriver(),getFlog(), loginUrl, LoginPage.class);
    }
    
    public LoginPage goToLoginEppPage(){
        String loginUrl = getLoginEppUrl();
        getFlog().action("Acces à la page de login ["+loginUrl+"]");
        return WebPage.goToPage(getDriver(),getFlog(), loginUrl, LoginPage.class);
    }
    
    
    public <T extends WebPage> T loginEpp(String username, String password, Class<T> pageClazz){
//        LoginPage loginPage = goToLoginEppPage();
        return loginEpp(username, password, pageClazz, 0);
//        return loginPage.submitLogin(username, password, pageClazz);
    }
    
    public <T extends WebPage> T loginEpp(STUser sTUser, Class<T> pageClazz){
        LoginPage loginPage = goToLoginEppPage();
        return loginPage.submitLogin(sTUser.getLogin(), sTUser.getPassword(), pageClazz);
    }
    
    //*************************LOGOUT**************************//
    
    @Override
	public LoginPage logoutWS(){
        String logoutUrl = getLogoutEpgUrl();
        getFlog().action("Acces au logout ["+logoutUrl+"]");
        return WebPage.goToPage(getDriver(),getFlog(), logoutUrl, LoginPage.class);
    }
    
    public static String getLogoutEpgUrl(){
        String appUrl = ((UrlEpgHelper)UrlEpgHelper.getInstance()).getEpgUrl();
        String loginUrl = appUrl;
        if(!loginUrl.endsWith("/")){
            loginUrl += "/";
        }
        loginUrl += "logout";
        return loginUrl;
    }
    
    public static String getLogoutEppUrl(){
        String appUrl = ((UrlEppHelper)UrlEppHelper.getInstance()).getEppUrl();
        String loginUrl = appUrl;
        if(!loginUrl.endsWith("/")){
            loginUrl += "/";
        }
        loginUrl += "logout";
        return loginUrl;
    }

    @Override
	public LoginPage logoutEpg(){
        String logoutUrl = getLogoutEpgUrl();
        getFlog().action("Acces au logout ["+logoutUrl+"]");
        this.userLogged = false;
        return WebPage.goToPage(getDriver(),getFlog(), logoutUrl, LoginPage.class);
    }
    
    public LoginPage logoutEpp(){
        String logoutUrl = getLogoutEppUrl();
        getFlog().action("Acces au logout ["+logoutUrl+"]");
        return WebPage.goToPage(getDriver(),getFlog(), logoutUrl, LoginPage.class);
    }
        
    /**
     * Retourne le loggueur fonctionnel
     */
    @Override
	public FunctionalLogger getFlog(){
        return (FunctionalLogger) super.getFlog();
    }
    
    public <T extends WebPage> T loginEpp(String username, String password, Class<T> pageClazz, int cnt) {
    	if (cnt == 5) {
    		throw new RuntimeException("On n'arrive plus à se connecter à l'application ! On quitte");
    	}
        LoginPage loginPage = goToLoginEppPage();
        T page = loginPage.submitLogin(username, password, pageClazz);
        
        if (page.hasElement(By.xpath("//*[contains(text(), \"Unable to connect\")]"))) {
        	return loginEpp(username, password, pageClazz, cnt+1);
        } 

        // cas d'erreur incompréhensible (on s'assure bien avant de cliquer sur 'connexion' que les champs sont remplis, mais malgré tout
        // on a parfois cette erreur
        if (page.hasElement(By.xpath("//*[contains(text(), \"Identifiant non renseigné\")]"))) {
        	getFlog().warn("#SAUVETAGE#: On est tombé sur l'erreur 'Identifiant non renseigné' alors on retente le login");
        	return loginEpp(username, password, pageClazz, cnt+1);
        }
        // On s'assure que les onglets sont bien affichés
        try {
        	page.waitForPageSourcePart("'id':'espace_recherche'", 30);
        } catch (TimeoutException e) {
	        // cas d'erreur qui se produit parfois: page d'erreur suite à la connexion
	        if (getDriver().getCurrentUrl().contains("nuxeo_error") || page.hasElement(By.cssSelector("title:contains('Erreur')"))) {
	        	return loginEpp(username, password, pageClazz, cnt+1);
	        }
        }
        return page;
    }
}
