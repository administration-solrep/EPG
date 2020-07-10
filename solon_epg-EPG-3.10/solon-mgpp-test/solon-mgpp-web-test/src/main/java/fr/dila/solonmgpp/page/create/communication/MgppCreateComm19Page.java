package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm19Page;
import fr.sword.xsd.solon.epp.PieceJointeType;

public class MgppCreateComm19Page extends AbstractMgppCreateComm{

  private static final String LETTRE_PM_VERS_AN_TITRE = "PJPMVANPour" + getDossierId();
  private static final String LETTRE_PM_VERS_AN_PJ = "src/main/attachments/PJPMVAN.doc";
  private static final String LETTRE_PM_VERS_AN_URL = "http://PJPMVANPour" + getDossierId() + "@solon-epg.fr";
  private static final String LETTRE_PM_VERS_SENAT_TITRE = "PJPMVSEPour" + getDossierId();
  private static final String LETTRE_PM_VERS_SENAT_URL = "http://PJPMVSEPour" + getDossierId() + "@solon-epg.fr";
  private static final String LETTRE_PM_VERS_SENAT_PJ = "src/main/attachments/PJPMVSE.doc";
  
  @Override
  protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
    return MgppDetailComm19Page.class;
  }

  
  public MgppDetailComm19Page createComm19(final String destinataire){
    
    setDestinataire(destinataire);
    
    addPieceJointe(PieceJointeType.LETTRE_PM_VERS_AN, LETTRE_PM_VERS_AN_TITRE, LETTRE_PM_VERS_AN_URL, LETTRE_PM_VERS_AN_PJ);
    sleep(1);
    addPieceJointe(PieceJointeType.LETTRE_PM_VERS_SENAT, LETTRE_PM_VERS_SENAT_TITRE, LETTRE_PM_VERS_SENAT_URL, LETTRE_PM_VERS_SENAT_PJ);
    sleep(1);
    
    return publier();
    
  }

}
