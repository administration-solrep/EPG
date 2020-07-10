package fr.dila.solonmgpp.page.epg.espaceEpg;

import fr.dila.solonmgpp.page.epg.commun.BandeauMenuMgppDeclaration;
import fr.dila.solonmgpp.page.epg.commun.SolonEpgPage;

public class DeclarationPage extends SolonEpgPage {

    public BandeauMenuMgppDeclaration getBandeauMenuMgppDeclaration() {
        return getPage(BandeauMenuMgppDeclaration.class);
    }

}
