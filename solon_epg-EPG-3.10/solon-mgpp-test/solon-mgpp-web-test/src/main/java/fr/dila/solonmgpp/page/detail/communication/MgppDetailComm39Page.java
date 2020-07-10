package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;

public class MgppDetailComm39Page extends AbstractMgppDetailComm {

    public static final String AUTEUR = "nxw_metadonnees_evenement_auteur";
    public static final String OBJET = "meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_objet";
    public static final String DATE_DEPOT_TEXTE = "meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_dateDepotTexte";
    public static final String NUMERO_DEPOT_TEXTE = "meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_numeroDepotTexte";

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return null;
    }

}
