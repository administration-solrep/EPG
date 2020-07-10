package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommAlerte15Page;

public class MgppCreateCommAlerte15Page extends AbstractMgppCreateComm {

    public static final String DESTINATAIRE = "nxw_metadonnees_evenement_destinataire_row";

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailCommAlerte15Page.class;
    }

    public MgppDetailCommAlerte15Page createCommAlerte15(final String destinataire) {

        setDestinataire(destinataire);
        return publier();
    }

}
