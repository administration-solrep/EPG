package fr.dila.solonepg.page.main.pan.view;

import fr.dila.solonepg.page.main.onglet.dossier.detail.DossierDetailMenu;
import fr.dila.solonepg.page.main.pan.view.mesure.MesureViewPage;

public class TableauMaitreDossierDetail extends DossierDetailMenu {

    public MesureViewPage getMesureViewPage() {
        this.waitElementVisibilityById("mesure_div");
        return this.getPage(MesureViewPage.class);
    }
}
