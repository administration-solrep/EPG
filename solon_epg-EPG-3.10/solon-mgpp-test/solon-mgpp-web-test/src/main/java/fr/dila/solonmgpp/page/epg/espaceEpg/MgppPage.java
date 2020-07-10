package fr.dila.solonmgpp.page.epg.espaceEpg;

import fr.dila.solonmgpp.page.epg.commun.BandeauMenuMgpp;
import fr.dila.solonmgpp.page.epg.commun.SolonEpgPage;


public class MgppPage extends SolonEpgPage {
    
    public BandeauMenuMgpp getBandeauMenuMgpp(){
        return getPage(BandeauMenuMgpp.class);
    }
    
}
