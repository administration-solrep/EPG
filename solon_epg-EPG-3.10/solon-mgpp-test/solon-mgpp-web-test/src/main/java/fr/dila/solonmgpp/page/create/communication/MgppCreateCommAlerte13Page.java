package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommAlerte13Page;

public class MgppCreateCommAlerte13Page  extends AbstractMgppCreateComm {
    
    public static final String DESTINATAIRE = "nxw_metadonnees_evenement_destinataire_row";
    
    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailCommAlerte13Page.class;
    }

    public MgppDetailCommAlerte13Page createCommAlerte13(final String destinataire){
        
        setDestinataire(destinataire);
        return publier();
      }
    
    
    
}
