package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommAlerte11Page;

public class MgppCreateCommAlerte11Page extends AbstractMgppCreateComm {
    
    public static final String DESTINATAIRE = "nxw_metadonnees_evenement_destinataire_row";
    
    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailCommAlerte11Page.class;
    }

    public MgppDetailCommAlerte11Page createCommAlerte11(final String objet, final String destinataire){
        
        setObjet(objet);
        setDestinataire(destinataire);
        return publier();
      }
    
    
    
}
