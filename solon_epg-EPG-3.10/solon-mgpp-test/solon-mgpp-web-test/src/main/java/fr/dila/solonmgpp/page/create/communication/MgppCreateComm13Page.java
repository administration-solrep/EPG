package fr.dila.solonmgpp.page.create.communication;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm13Page;
import fr.sword.xsd.solon.epp.NiveauLectureCode;
import fr.sword.xsd.solon.epp.PieceJointeType;

public class MgppCreateComm13Page extends AbstractMgppCreateComm {

  public static final String NUMERO_TEXTE_ADOPTE_INPUT = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_numeroTexteAdopte";
  public static final String DATE_ADOPTION_INPUT = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_dateAdoptionInputDate";
  
  private static final String LETTRE_TITRE = "PJLPour" + getDossierId();
  private static final String LETTRE_PJ = "src/main/attachments/doc2010.doc";
  private static final String LETTRE_URL = "http://PJLPour" + getDossierId() + "@solon-epg.fr";
  private static final String TEXTE_TRANSMIS_TITRE = "PJTTPour" + getDossierId();
  private static final String  TEXTE_TRANSMIS_URL = "http://PJTTPour" + getDossierId() + "@solon-epg.fr";
  private static final String TEXTE_TRANSMIS_PJ = "src/main/attachments/doc2011.doc";
  private static final String DESTINATAIRE_NODE = "//*[@id='evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_destinataire_node']";
  
  
  
  
  @Override
  protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
    return MgppDetailComm13Page.class;
  }


  public MgppDetailComm13Page createComm13(final String destinataire, final Integer niveauLecture, final NiveauLectureCode organisme, final String dateAdoption, final String numeroTexte) {

    setDestinataire(destinataire);
    setNiveauLecture(niveauLecture, organisme);

    if (niveauLecture != null) {
      checkValue(NIVEAU_LECTURE_INPUT, niveauLecture.toString());
    }

    if(StringUtils.isNotBlank(numeroTexte)){
      setNumeroTexte(numeroTexte);
      checkValue(NUMERO_TEXTE_ADOPTE_INPUT, numeroTexte);
    }

    if(StringUtils.isNotBlank(dateAdoption)){
      setDateAdoption(dateAdoption);
      checkValue(DATE_ADOPTION_INPUT, dateAdoption);
    }

    addPieceJointe(PieceJointeType.LETTRE, LETTRE_TITRE, LETTRE_URL, LETTRE_PJ);
    sleep(1);
    addPieceJointe(PieceJointeType.TEXTE_TRANSMIS, TEXTE_TRANSMIS_TITRE, TEXTE_TRANSMIS_URL, TEXTE_TRANSMIS_PJ);
    
    return publier();
  }

  public void setNumeroTexte(String numero) {
    final WebElement elem = getDriver().findElement(By.id(NUMERO_TEXTE_ADOPTE_INPUT));
    fillField("Numéro texte adopté", elem, numero);
  }

  public void setDateAdoption(String dateAdoption) {
    final WebElement elem = getDriver().findElement(By.id(DATE_ADOPTION_INPUT));
    fillField("Date adoption", elem, dateAdoption);
  }


}
