package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm10Page;
import fr.sword.xsd.solon.epp.NiveauLectureCode;
import fr.sword.xsd.solon.epp.PieceJointeType;

public class MgppCreateComm10Page extends AbstractMgppCreateComm{

    private static final String DESTINATAIRE_NODE = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_destinataire_node"; 
    private static final String COPIE_NODE_PATH = "//*[@id='evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_copie_global_region']/table/tbody/tr/td"; 
    
  @Override
  protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
    return MgppDetailComm10Page.class;
  }


  public MgppDetailComm10Page createComm10(Integer niveauLecture, NiveauLectureCode organisme, String titre, String url, String pj ){
    
      checkValue(DESTINATAIRE_NODE, "Sénat");
      checkValue(By_enum.XPATH, COPIE_NODE_PATH, "Assemblée nationale", Boolean.TRUE);

      setNiveauLecture(niveauLecture, organisme);
      addPieceJointe(PieceJointeType.LETTRE_PM,titre, url, pj);

      return publier();
  }
  
}
