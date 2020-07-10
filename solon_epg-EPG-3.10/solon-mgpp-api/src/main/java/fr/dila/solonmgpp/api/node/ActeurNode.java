package fr.dila.solonmgpp.api.node;

import fr.sword.xsd.solon.epp.Acteur;

public interface ActeurNode {

 String getIdentifiant();
  
 void setIdentifiant(String identifiant);
  
 String getIdentiteId();

 void setIdentiteId(String identiteId);
  
 void remapField(Acteur acteur) ;
    
 Acteur acteurToXsd() ;
}
