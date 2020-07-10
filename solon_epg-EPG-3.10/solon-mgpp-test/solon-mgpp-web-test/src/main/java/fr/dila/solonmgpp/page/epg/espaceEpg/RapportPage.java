package fr.dila.solonmgpp.page.epg.espaceEpg;

import fr.dila.solonmgpp.page.epg.commun.BandeauMenuMgppRapport;
import fr.dila.solonmgpp.page.epg.commun.SolonEpgPage;

public class RapportPage extends SolonEpgPage {
    
    public BandeauMenuMgppRapport getBandeauMenuMgppRapport(){
        return getPage(BandeauMenuMgppRapport.class);
    }
    
}
