package fr.dila.solonepg.page.commun;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.inject.internal.Lists;

public class Organigramme extends EPGWebPage {

    protected String organigrameId;

    /**
     * @deprecated utiliser {@link #openOrganigramme(By, WebElement, String)} en spécifiant comment identifier le
     *             container cible pour la sélection.
     */
    @Deprecated
    public void openOrganigramme(final WebElement baseElement, String elemId) { 
        openOrganigramme(null, baseElement, elemId);
    }

    /**
     * modalContainerBy l'identifiant du composant racine de la popup organigramme. Si null, on cherche une fenêtre
     *              modale avec un rich-tree à l'intérieur sur l'ensemble de la page ; s'il y a plusieurs fenêtres
     *              modales, cela peut alors échouer car si l'élément dont on surveille la visibilité n'est pas le bon,
     *              on va provoquer un timeout.
     * @baseElement le bouton d'ouverture de l'organigramme
     * @elemId l'identifiant du bouton d'ouverture
     * 
     * On ouvre l'organigramme en cliquant sur l'élément fourni. Au cas où, on donne également son id, pour le rappeler
     * au cas où il a disparu.
     */
    public void openOrganigramme(final By modalContainerBy, final WebElement baseElement, String elemId) { 
        try {
            baseElement.click();
        } catch (StaleElementReferenceException e) {
            // L'élément a disparu du DOM, on tente de le retrouver
            getFlog().action("L'élément a disparu du DOM : on tente de le rechercher à nouveau par son id pour cliquer dessus");
            getDriver().findElement(By.id(elemId)).click();
        }
        Collection<By> bys = Lists.newArrayList();
        if (modalContainerBy != null) {
            bys.add(modalContainerBy);
        }
        bys.add(By.className("rich-mp-content-table"));
        bys.add(By.className("rich-tree"));
        
        WebElement organigrammeElem = new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(
                ExpectedConditions.visibilityOfElementLocated(new ByChained(bys.toArray(new By[bys.size()]))));
        String id = organigrammeElem.getAttribute("id");
        this.organigrameId = id;
    }

    public void closeOrganigramme() {
        final WebElement closeButton = getDriver().findElement(By.xpath("//*[@class=\"rich-modalpanel\"]/div[2]/div/div[2]/div/a"));
        closeButton.click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By.id(this.organigrameId)));
    }

    public void selectElement(final String name) {
        final String xpath = String.format("//*[@id='%s']//span[text()='%s']", organigrameId, name);
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        final WebElement spanElement = findElement(By.xpath(xpath));
        final WebElement td = spanElement.findElement(By.xpath(".."));
        final WebElement addButton = td.findElement(By.xpath(".//a/img"));
        addButton.click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By.id(this.organigrameId)));
    }
    
    /**
     * 
     * @param elements - élément successifs à sélectionner dans l'arborescence
     * (parent, enfant niveau 1, enfant niveau 2, ...). Il faut utiliser l'identifiant de l'élément (+) quand on
     * sélectionne le dernier élément.
     * @param checkFieldId - champ dont il faut vérifier le rafraichissement lors de la dernière sélection
     */
    public void selectMultipleElement(final List<By> elements, String checkFieldId) {
        WebElement checkField = null;
        if (elements != null && elements.size() > 0) {
            Iterator<By> it = elements.iterator();
            while(it.hasNext()){
                By elementBy = it.next();
                WebElement element = new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(elementBy));;
                String element_id = element.getAttribute("id");
                WebElement container = null;
                if (element_id.endsWith(":handle:img:collapsed")) {
                    String container_id = element_id.replaceAll(":handle:img:collapsed$", "");
                    // uncollapse event destroy and recreate item -> track container staleness
                    container = findElement(By.id(container_id));
                    Assert.assertNotNull(container);
                }
                if (! it.hasNext()) {
                    // si on est sur le dernier élément, on récupère le checkFieldId pour tester son rafraichissement
                    checkField = findElement(By.id(checkFieldId));
                }
                element.click();
                if (checkField != null) {
                    // c'est le dernier élément, on attend la substition du checkField
                    new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(checkField));
                } else if (container != null) {
                    // wait for tree container replacement
                    new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(container));
                }
            }
        }
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By.id(this.organigrameId)));
    }
}
