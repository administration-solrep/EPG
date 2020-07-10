package fr.dila.solonmgpp.page.create.communication;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonmgpp.page.Organigramme;
import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.sword.xsd.solon.epp.NiveauLectureCode;
import fr.sword.xsd.solon.epp.PieceJointeType;

public abstract class AbstractMgppCreateComm extends AbstractCreateComm {

    public static final String IDENTIFIANT_COMMUNICATION = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_identifiant";
    public static final String NIVEAU_LECTURE_SELECT = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_niveauLecture:nxw_metadonnees_evenement_niveauLecture_select_one_menu";
    public static final String NIVEAU_LECTURE_INPUT = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_niveauLecture:nxw_metadonnees_evenement_niveauLecture_input";
    public static final String OBJET_INPUT = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_objet";
    public static final String NOR_INPUT = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_nor";
    public static final String INTITULE = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_intitule";
    public static final String DESTINATAIRE = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_destinataire_findButton";
    
    public static final String ADD_PIECE_JOINTE = "evenement_metadonnees:%s_addPj";
    public static final String PIECE_JOINTE_TITRE = "evenement_metadonnees:%s";
    public static final String PIECE_JOINTE_URL_FIELD = "//span[@id='evenement_metadonnees:%s_pieceJointePanel']/table/tbody/tr[2]/td[2]/input";
    public static final String PIECE_JOINTE_TITRE_FIELD = "//span[@id='evenement_metadonnees:%s_pieceJointePanel']/table/tbody/tr[1]/td[2]/input";
    
    //les 3 icones pour ajouter des fichiers
    public static final String ADD_PJFILE = "//span[@id='evenement_metadonnees:%s_pieceJointePanel']/table/tbody/tr[1]/td[4]/span[1]/a/img";
    public static final String ADD_PJFILE_DEPUIS_FDD = "//span[@id='evenement_metadonnees:%s_pieceJointePanel']/table/tbody/tr[1]/td[4]/span[2]/a/img";
    public static final String ADD_PJFILE_DEPUIS_PARAPHEUR = "//span[@id='evenement_metadonnees:%s_pieceJointePanel']/table/tbody/tr[1]/td[4]/span[3]/a/img";
    
    public static final String PIECE_JOINTE_FILE_IMG = "//span[@id='evenement_metadonnees:%s_pieceJointePanel']/table/tbody/tr[3]/td[2]/table/tbody/tr/td/span/img";
    public static final String ADD_FILE_HEADER = "editFilePanelHeader";
    public static final String ADD_FILE_HEADER_FDD = "FDDeditFilePanelHeader";
    public static final String ADD_FILE_HEADER_PAR = "PAReditFilePanelHeader";

    private By destinataireModalContainerBy = By.id("evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_destinataire_selectNodeOrganigrammePanelContainer");

    public void setNiveauLecture(final Integer niveau, final NiveauLectureCode code) {
        final WebElement niveauLectureElem = getDriver().findElement(By.id(NIVEAU_LECTURE_SELECT));
        getFlog().action("Selectionne \"" + code + "\" dans le select \"Niveau de lecture\"");
        final Select select = new Select(niveauLectureElem);
        select.selectByValue(code.name());
        if (niveau != null) {
            waitForPageSourcePart(By.id(NIVEAU_LECTURE_INPUT), TIMEOUT_IN_SECONDS);
            final WebElement niveauElem = getDriver().findElement(By.id(NIVEAU_LECTURE_INPUT));
            fillField("Niveau de lecture", niveauElem, niveau.toString());
        }
    }
    
    public void setIntitule(final String intitule) {
        final WebElement elem = getDriver().findElement(By.id(INTITULE));
        fillField("Intitulé", elem, intitule);
    }


    public void setObjet(final String objet) {
        final WebElement elem = getDriver().findElement(By.id(OBJET_INPUT));
        fillField("Objet", elem, objet);
    }

    
    

    public void setNor(final String nor) {
        final WebElement elem = getDriver().findElement(By.id(NOR_INPUT));
        fillField("Nor", elem, nor);
    }

    public void addPieceJointe(final PieceJointeType pieceJointeType, final String titre, final String url, final String file) {
      final WebElement elem = getDriver().findElement(By.id(String.format(ADD_PIECE_JOINTE, pieceJointeType.name())));
      elem.click();
      waitForPageSourcePartDisplayed(By.xpath(String.format(PIECE_JOINTE_URL_FIELD, pieceJointeType.name())), TIMEOUT_IN_SECONDS);
      
      final WebElement titleElem = getDriver().findElement(By.xpath(String.format(PIECE_JOINTE_TITRE_FIELD, pieceJointeType.name())));
      fillField("Titre", titleElem, titre);  
      
        final WebElement urlElem = getDriver().findElement(By.xpath(String.format(PIECE_JOINTE_URL_FIELD, pieceJointeType.name())));
        fillField("url", urlElem, url);

        
        final WebElement pjfBtn = getDriver().findElement(By.xpath(String.format(ADD_PJFILE, pieceJointeType.name())));
        pjfBtn.click();

        waitForPageSourcePartDisplayed(By.id(ADD_FILE_HEADER), TIMEOUT_IN_SECONDS);

        final String fileName = addAttachment(file);

        waitForPageSourcePartDisplayed(By.xpath(String.format(PIECE_JOINTE_FILE_IMG, pieceJointeType.name())), TIMEOUT_IN_SECONDS);
        final WebElement fileElem = getDriver().findElement(By.xpath(String.format(PIECE_JOINTE_FILE_IMG, pieceJointeType.name())));
        if (!fileName.equals(fileElem.getAttribute("title"))) {
            getFlog().actionFailed("Erreur d'ajout de la piece jointe [" + pieceJointeType + ", " + fileName + "]");
        }

    }
    
    public void addPieceJointeDepuis(final PieceJointeType pieceJointeType, final String ajouterDepuis, final String titre, final String url, final String clickable[]) {
      final WebElement elem = getDriver().findElement(By.id(String.format(ADD_PIECE_JOINTE, pieceJointeType.name())));
      elem.click();
      waitForPageSourcePartDisplayed(By.xpath(String.format(PIECE_JOINTE_URL_FIELD, pieceJointeType.name())), TIMEOUT_IN_SECONDS);
      
      final WebElement titleElem = getDriver().findElement(By.xpath(String.format(PIECE_JOINTE_TITRE_FIELD, pieceJointeType.name())));
      fillField("Titre", titleElem, titre);  
      
        if(StringUtils.isNotBlank(url)){
          final WebElement urlElem = getDriver().findElement(By.xpath(String.format(PIECE_JOINTE_URL_FIELD, pieceJointeType.name())));
          fillField("url", urlElem, url);
        }
        
        
        String path   =String.format(ajouterDepuis, pieceJointeType.name()) ;
        final WebElement pjfBtn = getDriver().findElement(By.xpath(path));
        pjfBtn.click();

        if(ajouterDepuis.equals(ADD_PJFILE_DEPUIS_FDD)){
          waitForPageSourcePartDisplayed(By.xpath("//div[contains(@id, '" + ADD_FILE_HEADER_FDD + "')]"), TIMEOUT_IN_SECONDS);
        } else {
          waitForPageSourcePartDisplayed(By.xpath("//div[contains(@id, '" + ADD_FILE_HEADER_PAR + "')]"), TIMEOUT_IN_SECONDS);
        }
        
        for(int i=0; i< clickable.length; i++){
          getDriver().findElement(By.xpath(clickable[i])).click();
          new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.elementSelectionStateToBe(By.xpath(clickable[i]), true));
        }

        if(clickable.length > 1){
            sleep(1);
        }
        
        if(ajouterDepuis.equals(ADD_PJFILE_DEPUIS_FDD)) {
            getDriver().findElement(By.id("FDDeditFileForm:FDDeditFileButtonImage")).click();
        } else {
            getDriver().findElement(By.id("PAReditFileForm:PAReditFileButtonImage")).click();
        }
        
        if(clickable.length > 1){
            sleep(1);
        }
        
    }

    protected void setDestinataire(final String destinataire) {
        getFlog().action("Sélection " + destinataire + " dans organigramme");
        final Organigramme organigramme = getPage(Organigramme.class);
        organigramme.selectDestinataireFromOrganigramme(
                destinataireModalContainerBy,
                By.xpath(String.format("//td[./*[contains(text(), '%s')]]/a", destinataire)), // sélection par identifiant
                DESTINATAIRE);
    }

    @Override
	protected abstract Class<? extends AbstractDetailComm> getCreateResultPageClass();
    
    //protected abstract Class<? extends CommonWebPage> getCreateResultPageClassNotAsDetailPage();

}
