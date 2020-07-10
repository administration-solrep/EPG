package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommGenerique11Page;

public class MgppCreateCommGenerique11Page extends AbstractMgppCreateComm {

    
    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailCommGenerique11Page.class;
    }

    public MgppDetailCommGenerique11Page createCommGenerique11(final String objet, final String destinataire){
        
        setObjet(objet);
        setDestinataire(destinataire);
        return publier();
      }
    
    
}
