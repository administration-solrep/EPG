package fr.dila.solonmgpp.page.epg.commun;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.dila.st.webdriver.framework.CustomPageFactory;
import fr.dila.st.webdriver.helper.AutoCompletionHelper;
import fr.dila.st.webdriver.helper.NameShortener;
import fr.sword.naiad.commons.webtest.WebPage;
import fr.sword.naiad.commons.webtest.logger.WebLogger;


public class SolonEpgPage extends EPGWebPage {
	
	@FindBy(xpath="//div[@id='SHOW_REPOSITORY']/div/div/a")
    private WebElement espaceTraitement;
    
    @FindBy(xpath="//div[@id='espace_creation']/div/div/a")
    private WebElement espaceCreation;
    
    @FindBy(xpath="//div[@id='espace_recherche']/div/div/a")
    private WebElement recherche;
    
    @FindBy(xpath="//div[@id='espace_suivi']/div/div/a")
    private WebElement suivi;
    
    @FindBy(xpath="//div[@id='espace_administration']/div/div/a")
    private WebElement administration;
    
    @FindBy(xpath="//div[@id='espace_activite_normative']/div/div/a")
    private WebElement pilotageAN;
    
    @FindBy(xpath="//div[@id='espace_parlementaire']/div/div/a")
    private WebElement espaceParlementaire;
	
    public SolonEpgPage(){
        super();
    }
        
    public BandeauMenuEpg getBandeauMenu(){
        return getPage(BandeauMenuEpg.class);
    }
    
    /**
     * Retourne une attente de 30 secondes
     * @return
     */
    public WebDriverWait thirtySecondsWait(){
        return getWait(30);
    }


    public WebDriverWait getWait(Integer secCount) {
        WebDriverWait wait = new WebDriverWait(getDriver(), secCount);
        return wait;
    }
    
    /**
     * Retourne une attente de 2 secondes
     * @return
     */
    public WebDriverWait twoSecondsWait(){
         return getWait(2);
    }
    /**
     * Retourne une attente de 1 seconde
     * @return
     */
    public WebDriverWait oneSecondsWait(){
         return getWait(1);
    }
    
    /**
     * Clique droit sur un élement
     * @param element
     */
    public void rightClick(WebElement element) {
        Actions builderq = new Actions(super.getDriver());
        Action rClick = builderq.contextClick(element).build();
        rClick.perform();
    }
    
    /**
     * Bouge vers l'élement
     * @param element
     */
    public void moveToward(WebElement element) {
        Actions builderq = new Actions(super.getDriver());
        Action mClick = builderq.moveToElement(element).build();
        mClick.perform();
    }
    
    /**
     * Clique droit sur un élement et va à la page 
     * @param element
     */
    public <T extends WebPage> T rightClick(WebElement element, Class<T> pageClass) {
        rightClick(element);
        return getPage(pageClass);
    }
    
    /**
     * Raccourci pour findElement
     * @param by le by de paramêtre
     * @return
     */
    public WebElement findElement(By by){
        return getDriver().findElement(by);
    }
    
    public <T extends WebPage> T click(WebElement element, Class<T> class1) {
        if (element == null){
            throw new NoSuchElementException("Pas d'élément trouvé");
        }
        getFlog().startAction("Clique sur un lien " + element.getText());
        element.click();
        T page =  getPage(class1);
        getFlog().endAction();
        return page;
    }
    
    public static <T extends WebPage> T getPage(final WebDriver driver, final WebLogger webLogger, final Class<T> pageClazz) {
        final T page = CustomPageFactory.initElements(driver, pageClazz);
        page.setWebLogger(webLogger);
        page.setDriver(driver);
        page.checkIfLoaded();
        return page;
    }
    
    public <T extends WebPage> T getPage(final Class<T> pageClazz) {
        return getPage(getDriver(), getFlog(), pageClazz);
    }
    
    public void setAutocompleteValue(NameShortener shortener, By valeurBy) {
        setAutocompleteValue(shortener, valeurBy, 0, false);
    }
    
    public void setAutocompleteValue(NameShortener shortener, By valeurBy, int index, Boolean nospan) {
        AutoCompletionHelper.setBoxValue(getFlog(), getDriver(), valeurBy, shortener, index, nospan);
    }
    
    /**
     * Retourne vrai si le tableau de résultat contie
     * @param numeroQuestionComplet
     * @return
     */
    public Boolean containsQuestion(String numeroQuestionComplet){
        By tableResultsBy = new ByChained(By.className("dataOutput"), By.partialLinkText(numeroQuestionComplet));
        Boolean found = false;
        try{
            findElement(tableResultsBy);
            found = true;
        } catch( NoSuchElementException e){
            found = false;
        }
        return found;
    }
    
    /**
     * Cliquer sur une image
     * @param imgTitle
     */
    public void clickOnImg(String imgTitle) {
        By imgBy = getImgBy(imgTitle);
        waitForPageSourcePartDisplayed(imgBy, 15);
        WebElement imgElt = findElement(imgBy);
        imgElt.click();
    }

    public By getImgBy(String imgTitle) {
        String imgExpr = String.format("//img[@title = \"%s\"]", imgTitle);
        By imgBy = By.xpath(imgExpr);
        return imgBy;
    }
    
    public boolean containsEltLocatedBy(By by) {
        return !getDriver().findElements(by).isEmpty();
    }
    
    public void clickBtn(WebElement btn) {
        getFlog().startAction("Clique sur le bouton " + btn.getAttribute("value"));
        btn.click();
        getFlog().endAction();
    }
    
    public void clickLien(WebElement lien) {
        getFlog().startAction("Clique sur le lien " + lien.getText());
        lien.click();
        getFlog().endAction();
    }
    
}
