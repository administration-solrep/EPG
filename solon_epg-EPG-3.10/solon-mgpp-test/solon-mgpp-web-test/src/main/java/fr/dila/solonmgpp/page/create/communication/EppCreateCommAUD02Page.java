package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailCommAUD02Page;

public class EppCreateCommAUD02Page extends AbstractEppCreateComm {

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailCommAUD02Page.class;

    }
    
    public EppDetailCommAUD02Page createCommAUD02(String commission) {
        addCommission(commission);
        return publier();
        
    }
}