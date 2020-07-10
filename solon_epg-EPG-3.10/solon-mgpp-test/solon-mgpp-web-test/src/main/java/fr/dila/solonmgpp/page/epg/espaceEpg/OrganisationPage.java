package fr.dila.solonmgpp.page.epg.espaceEpg;

import fr.dila.solonmgpp.page.epg.commun.BandeauMenuMgppOrganisation;
import fr.dila.solonmgpp.page.epg.commun.SolonEpgPage;

public class OrganisationPage extends SolonEpgPage {
    
    public BandeauMenuMgppOrganisation getBandeauMenuMgppOrganisation(){
        return getPage(BandeauMenuMgppOrganisation.class);
    }
    
}
