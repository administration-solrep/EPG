package fr.dila.solonmgpp.page.create.communication;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm01Page;
import fr.sword.xsd.solon.epp.NiveauLectureCode;
import fr.sword.xsd.solon.epp.PieceJointeType;
import fr.sword.xsd.solon.epp.TypeLoi;

public class MgppCreateComm01Page extends AbstractMgppCreateComm {

    public static final String TYPE_COMM = "LEX-01 : Pjl - Dépôt";
    public static final String TYPE_LOI_SELECT = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_typeLoi";
    public static final String AUTEUR_INPUT = "//span[@id='nxw_metadonnees_evenement_auteursuggestDiv']/input";
    public static final String AUTEUR_SUGGEST = "//table[@class='dr-sb-ext-decor-3 rich-sb-ext-decor-3']/tbody/tr/td/div/table/tbody/tr/td[2]";
    public static final String AUTEUR_DELETE = "//div[@id='nxw_metadonnees_evenement_auteur']/table/tbody/tr[2]/td/span/span/a";
    public static final String COAUTEUR_INPUT = "//span[@id='nxw_metadonnees_evenement_coauteur_suggestDiv']/input";
    public static final String COAUTEUR_SUGGEST = "//table[@id='evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_coauteur_suggestionBox:suggest']/tbody/tr/td[2]";
    public static final String COAUTEUR_RESET = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_coauteur_list:0:nxw_metadonnees_evenement_coauteur_delete";
    public static final String INTITULE = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_intitule";
    
    private static final String TITRE1 = "PJTPour" + getDossierId();
    private static final String TITRE2 = "PJEPour" + getDossierId();
    private static final String TITRE3 = "PJDPPour" + getDossierId();
    private static final String TITRE4 = "PJLPMPour" + getDossierId();
    
    private static final String PJ1 = "src/main/attachments/doc2007.doc";
    private static final String PJ2 = "src/main/attachments/doc2007.doc";
    private static final String[] PJ3 = new String[]{"//*[@id='FDDeditFileForm:fondDeDossierTree:0:0::fondDeDossierFichier:text']/table/tbody/tr/td/div/div/input"};
//    private static final String[] PJ4 = new String[]{"//*[@id='PAReditFileForm:parapheurTree:0:0::parapheurFichier:text']/table/tbody/tr/td/div/span/input", 
//                                                     "//*[@id='PAReditFileForm:parapheurTree:1:0::parapheurFichier:text']/table/tbody/tr/td/div/span/input"};
    private static final String[] PJ4 = new String[]{"//*[@id='PAReditFileForm:parapheurTree:0:0::parapheurFichier:text']/table/tbody/tr/td/div/span/input"};

    
    private static final String URL1 = "http://" + getDossierId() + "@solon-epg.fr";
    private static final String URL2 = "http://EXPOSE_DES_MOTIFS_" + getDossierId() + "@solon-epg.fr";
    private static final String URL3 = "url@gmail.fr";
    private static final String URL4 = "";
    
    public void setTypeLoi(final TypeLoi loi) {
        final WebElement elem = getDriver().findElement(By.id(TYPE_LOI_SELECT));
        getFlog().action("Selectionne \"" + loi + "\" dans le select \"Type loi\"");
        final Select select = new Select(elem);
        select.selectByValue(loi.name());
    }

    public void addAuteur(final String auteur) {
        final WebElement elem = getDriver().findElement(By.xpath(AUTEUR_INPUT));
        fillField("Auteur", elem, auteur);
        waitForPageSourcePart(By.xpath(AUTEUR_SUGGEST), TIMEOUT_IN_SECONDS);
        final WebElement suggest = getDriver().findElement(By.xpath(AUTEUR_SUGGEST));
        suggest.click();
        waitForPageSourcePart(By.xpath(AUTEUR_DELETE), TIMEOUT_IN_SECONDS);
    }

    public void addCoAuteur(final String coAuteur) {
      //implicit wait
      getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

      final WebElement elemCoAut = getDriver().findElement(By.xpath(COAUTEUR_INPUT));
      //fillField("Coauteur", elemCoAut, coAuteur);
      for(int i = 0; i < coAuteur.length(); i++){
          elemCoAut.sendKeys(String.valueOf(coAuteur.charAt(i)));
          //implicit wait
          getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
      }
      
      waitForPageSourcePart(By.xpath(COAUTEUR_SUGGEST), TIMEOUT_IN_SECONDS);
      WebElement suggestCoAut = getDriver().findElement(By.xpath(COAUTEUR_SUGGEST));
//      if( !suggestCoAut.isDisplayed() ){
//          elemCoAut.clear();
//          //implicit wait
//          getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//          getFlog().startAction("deuxieme attentat de remplir le coauteur");
//          fillField("Coauteur", elemCoAut, coAuteur);
//          waitForPageSourcePart(By.xpath(COAUTEUR_SUGGEST), TIMEOUT_IN_SECONDS);
//          suggestCoAut = getDriver().findElement(By.xpath(COAUTEUR_SUGGEST));
//          getFlog().endAction();
//      }
      suggestCoAut.click();
      waitForPageSourcePart(By.id(COAUTEUR_RESET), TIMEOUT_IN_SECONDS);
    }

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailComm01Page.class;
    }
    
    /*@Override
    protected Class<? extends CommonWebPage> getCreateResultPageClassNotAsDetailPage() {
        return ProcedureLegislativePage.class;
    }*/

    public MgppDetailComm01Page createComm01(String destinataire, String objet, String nor, String auteur, 
            String coauteur, Integer niveauLecture, NiveauLectureCode organisme, String intitule) {

      checkValue(OBJET_INPUT, objet);
      checkValue(INTITULE, intitule);
      
      setDestinataire(destinataire);
      
      setNiveauLecture(niveauLecture, organisme);
      setNor(nor);
      setTypeLoi(TypeLoi.LOI);
      addAuteur(auteur);
      sleep(1);
      addCoAuteur(coauteur);
      
      addPieceJointeDepuis(PieceJointeType.EXPOSE_DES_MOTIFS, ADD_PJFILE_DEPUIS_FDD, TITRE3, URL3, PJ3);
      sleep(1);
      addPieceJointeDepuis(PieceJointeType.LETTRE_PM, ADD_PJFILE_DEPUIS_PARAPHEUR, TITRE4, URL4, PJ4);
      sleep(1);
      addPieceJointe(PieceJointeType.TEXTE, TITRE1, URL1, PJ1);
      sleep(1);
      addPieceJointe(PieceJointeType.DECRET_PRESENTATION,TITRE2, URL2, PJ2);
      sleep(1);
      
      return publier();
    }
    
}
