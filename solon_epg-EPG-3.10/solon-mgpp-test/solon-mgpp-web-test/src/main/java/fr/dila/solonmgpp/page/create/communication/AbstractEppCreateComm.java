package fr.dila.solonmgpp.page.create.communication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.sword.xsd.solon.epp.PieceJointeType;

public abstract class AbstractEppCreateComm extends AbstractCreateComm {
	
    public static final String PUBLIER_BTN_ID = "evenement_metadonnees:createEvtSucc_submitButton";

    private static final Set<String> EDITABLE_TAG_SET = new HashSet<String>();

    static {
        EDITABLE_TAG_SET.add("a");
        EDITABLE_TAG_SET.add("input");
        EDITABLE_TAG_SET.add("select");
    }

    public enum Mode {
        READ, EDIT;
    }

    public static final String COMMUNICATION = "nxw_metadonnees_evenement_libelle";
    public static final String DESTINATAIRE = "nxw_metadonnees_evenement_destinataire_row";
    public static final String OBJET_INPUT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_objet";
    public static final String BASE_LEGALE = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_baseLegale";
    public static final String IDENTIFIANT_DOSSIER_INPUT = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_identifiant_dossier";

    public static final String ADD_PIECE_JOINTE = "evenement_metadonnees:%s_addPj";
    public static final String PIECE_JOINTE_TITRE = "evenement_metadonnees:%s";
    public static final String PIECE_JOINTE_URL = "//span[@id='evenement_metadonnees:%s_pieceJointePanel']/table/tbody/tr[2]/td[2]/div/input";
    public static final String ADD_PJFILE = "evenement_metadonnees:%s_0_addPjf";
    public static final String PIECE_JOINTE_FILE_IMG = "//span[@id='evenement_metadonnees:%s_pieceJointePanel']/table/tbody/tr[3]/td[2]/table/tbody/tr/td/div/span/img";
    public static final String ADD_FILE_HEADER = "editFilePanelHeader";
    
    public static final String DATE_JO_INPUT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateJoInputDate";
    public static final String DATE_DESIGNATION_INPUT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateDesignationInputDate";

    public static final String AUTEUR_INPUT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_auteur_suggest";
    public static final String AUTEUR_SUGGEST = "//table[@id='evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_auteur_suggestionBox:suggest']/tbody/tr/td[2]";
    public static final String AUTEUR_DELETE = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_auteur_delete";
    
    public static final String COMMISSIOM_INPUT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_commissions_suggest";
    public static final String COMMISSIOM_SUGGEST = "//table[@id='evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_commissions_suggestionBox:suggest']/tbody/tr/td[2]";
    public static final String COMMISSIOM_DELETE = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_commissions_list:0:nxw_metadonnees_version_commissions_delete";
    
    
    public static final String INTITULE = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_intitule";

    /**
     * Recherche d'un element par id<br/>
     * 
     * @param id
     * @param mode
     */
    public void checkElementPresent(final String id, final Mode mode) {
        getFlog().check("Recherche de l'element : " + id + " em mode : " + mode);
        final WebElement element = getDriver().findElement(By.id(id));
        final List<WebElement> childs = new ArrayList<WebElement>();
        childs.add(element);
        getAllChild(element, childs);

        Boolean editableFound = Boolean.FALSE;
        for (final WebElement webElement : childs) {
            final String tagName = webElement.getTagName();
            if (EDITABLE_TAG_SET.contains(tagName)) {
                editableFound = Boolean.TRUE;
            }
        }

        switch (mode) {
        case READ:
            if (editableFound) {
                getFlog().checkFailed(id + "found but in edit mode");
            }
            break;
        case EDIT:
            if (!editableFound) {
                getFlog().checkFailed(id + "found but in read-only mode");
            }
            break;
        }
    }

    public void checkElementNotPresent(final String id) {
        getFlog().check("Recherche de l'element : " + id);
        try {
            final WebElement element = getDriver().findElement(By.id(id));
            if (element != null) {
                getFlog().checkFailed(id + "found");
            }
        } catch (final NoSuchElementException e) {
            // ok
        }
    }

    public void checkValueContain(final String id, final String value) {
        getFlog().check("Verifie la valeur de l'element : " + id + ", expected start: " + value);
        checkValue(id, value, Boolean.FALSE);
    }

    public void checkValue(final String id, final String value) {
        getFlog().check("Verifie la valeur de l'element : " + id + ", expected : " + value);
        checkValue(id, value, Boolean.TRUE);
    }

    public void setObjet(final String objet) {
        final WebElement elem = getDriver().findElement(By.id(OBJET_INPUT));
        fillField("Objet", elem, objet);
    }

    public void addPieceJointe(final PieceJointeType pieceJointeType, final String url, final String file) {
        final WebElement elem = getDriver().findElement(By.id(String.format(ADD_PIECE_JOINTE, pieceJointeType.name())));
        elem.click();
        waitForPageSourcePartDisplayed(By.xpath(String.format(PIECE_JOINTE_URL, pieceJointeType.name())), TIMEOUT_IN_SECONDS);

        final WebElement urlElem = getDriver().findElement(By.xpath(String.format(PIECE_JOINTE_URL, pieceJointeType.name())));
        fillField("url", urlElem, url);

        final WebElement pjfBtn = getDriver().findElement(By.id(String.format(ADD_PJFILE, pieceJointeType.name())));
        pjfBtn.click();

        waitForPageSourcePartDisplayed(By.id(ADD_FILE_HEADER), TIMEOUT_IN_SECONDS);

        final String fileName = addAttachment(file);

        waitForPageSourcePartDisplayed(By.xpath(String.format(PIECE_JOINTE_FILE_IMG, pieceJointeType.name())), TIMEOUT_IN_SECONDS);
        final WebElement fileElem = getDriver().findElement(By.xpath(String.format(PIECE_JOINTE_FILE_IMG, pieceJointeType.name())));
        if (!fileName.equals(fileElem.getAttribute("title"))) {
            getFlog().actionFailed("Erreur d'ajout de la piece jointe [" + pieceJointeType + ", " + fileName + "]");
        }

    }

    public void setIdentifiantDossier(final String idDossier) {
        final WebElement elem = getDriver().findElement(By.id(IDENTIFIANT_DOSSIER_INPUT));
        fillField("Identifiant dossier", elem, idDossier);
    }

    public void setbaseLegal(final String baseLegal) {
        final WebElement elem = getDriver().findElement(By.id(BASE_LEGALE));
        fillField("Base légale", elem, baseLegal);
    }

    public void setDateJO(final String dateJO) {
        final WebElement elem = getDriver().findElement(By.id(DATE_JO_INPUT));
        fillField("Date JO", elem, dateJO);
    }

    public void setDateDesignation(final String dateDesignation) {
        final WebElement elem = getDriver().findElement(By.id(DATE_DESIGNATION_INPUT));
        fillField("Date désignation", elem, dateDesignation);
    }

    public void addAuteur(final String auteur) {
        final WebElement elem = getDriver().findElement(By.id(AUTEUR_INPUT));
        fillField("Auteur", elem, auteur);
        waitForPageSourcePart(By.xpath(AUTEUR_SUGGEST), TIMEOUT_IN_SECONDS);
        final WebElement suggest = getDriver().findElement(By.xpath(AUTEUR_SUGGEST));
        suggest.click();
        waitForPageSourcePart(By.id(AUTEUR_DELETE), TIMEOUT_IN_SECONDS);
    }
    
    public void addCommission(final String commission) {
        final WebElement elem = getDriver().findElement(By.id(COMMISSIOM_INPUT));
        fillField("Commission(s)", elem, commission);
        waitForPageSourcePart(By.xpath(COMMISSIOM_SUGGEST), TIMEOUT_IN_SECONDS);
        final WebElement suggest = getDriver().findElement(By.xpath(COMMISSIOM_SUGGEST));
        suggest.click();
        waitForPageSourcePart(By.id(COMMISSIOM_DELETE), TIMEOUT_IN_SECONDS);
    }


    protected abstract Class<? extends AbstractDetailComm> getCreateResultPageClass();

}
