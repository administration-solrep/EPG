package fr.dila.solonmgpp.page.epg.espaceEpg;

import fr.dila.solonmgpp.page.epg.commun.BandeauMenuMgppNomination;
import fr.dila.solonmgpp.page.epg.commun.SolonEpgPage;

public class NominationPage extends SolonEpgPage {
    
    public BandeauMenuMgppNomination getBandeauMenuMgppNomination(){
        return getPage(BandeauMenuMgppNomination.class);
    }
    
}
