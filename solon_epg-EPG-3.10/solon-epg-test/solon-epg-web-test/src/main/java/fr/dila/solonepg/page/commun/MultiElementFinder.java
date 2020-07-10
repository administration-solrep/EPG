package fr.dila.solonepg.page.commun;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.sword.naiad.commons.webtest.WebPage;

/** Vérifie plusieurs contenus possible sur la page courante selon:<ul>
 * <li>les classes CSS fournies</li>
 * <li>un contenu</li>
 * </ul>
 * et renvoie l'index (+1) du contenu trouvé. 0 veut dire qu'on n'a rien trouvé.
 * On peut récuperer par la suite le contenu des éléments trouvés, que l'on a stocké pendant la phase de recherche. Ceci afin d'éviter les StaleElementException, et les problèmes
 * d'éléments qui disparaissent (message d'erreur ou de succès par exemple)
 * @author eboussaton
 *  **/
public class MultiElementFinder {

	String[] content;
	final WebDriver driver;
	int foundCategory;
	
	public MultiElementFinder(final WebDriver driver) {
		this.driver = driver;
	}
	
    public int byCssClass(final String... cssClasses) {
		content = new String[cssClasses.length];
		
		try {
			foundCategory = new WebDriverWait(driver, 10).until(new ExpectedCondition<Integer>() {
	            @Override
	            public Integer apply(final WebDriver wdriver) {
	            	for (int n=0;n<cssClasses.length;n++) {
	            		List<WebElement> elems = wdriver.findElements(By.className(cssClasses[n]));
	            		if (!elems.isEmpty()) {
	            			content[n] = elems.get(0).getText();
	            			return n+1;
	            		}
	            	}
	               return 0;
	            }
	        });
		} catch (TimeoutException e) {
			// rien à faire, on a pas trouvé d'élément du tout => on va renvoyer 0
		}
        return foundCategory;
    }
    
    /** Vérifie plusieurs contenus possible sur la page courante, et renvoie l'index du contenu trouvé **/
    public int bySourcePart(final String... searchStrings) {
        int foundCategory = new WebDriverWait(driver, WebPage.TIMEOUT_IN_SECONDS).until(new ExpectedCondition<Integer>() {
            @Override
            public Integer apply(final WebDriver wdriver) {
            	String pageSource = wdriver.getPageSource();
            	for (int n=0;n<searchStrings.length;n++) {
	               if (pageSource.contains(searchStrings[n])) {
	            	   return n+1;
	               }
            	}
               return 0;
            }
        });
        return foundCategory;
    }
    
    public String getContent() {
    	return content[foundCategory-1];
    }
}
