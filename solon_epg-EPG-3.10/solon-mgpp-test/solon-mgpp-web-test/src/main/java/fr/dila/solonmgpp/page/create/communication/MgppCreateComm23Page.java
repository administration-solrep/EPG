package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm23Page;
import fr.sword.xsd.solon.epp.PieceJointeType;

public class MgppCreateComm23Page extends AbstractMgppCreateComm{

  private static final String LETTRE_PM_TITRE = "PJPMPour" + getDossierId();
  private static final String LETTRE_PM_PJ = "src/main/attachments/doc2009.doc";
  private static final String LETTRE_PM_URL = "http://PJPMPour" + getDossierId() + "@solon-epg.fr";
  
  @Override
  protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
    return MgppDetailComm23Page.class;
  }

  public MgppDetailComm23Page createComm23(final String destinataire){
    
    setDestinataire(destinataire);
    
    addPieceJointe(PieceJointeType.LETTRE_PM, LETTRE_PM_TITRE, LETTRE_PM_URL, LETTRE_PM_PJ);
   
    return publier();
  }

}
