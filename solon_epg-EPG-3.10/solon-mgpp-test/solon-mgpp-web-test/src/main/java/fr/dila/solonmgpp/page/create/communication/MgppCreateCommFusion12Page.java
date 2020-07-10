package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommFusion12Page;

public class MgppCreateCommFusion12Page extends AbstractMgppCreateComm {

    public static final String DESTINATAIRE = "nxw_metadonnees_evenement_destinataire_row";

    public static final String DOSSIER_CIBLE = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_dossierCible";

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailCommFusion12Page.class;
    }

    public MgppDetailCommFusion12Page createCommFusion12(final String dossierCible, final String destinataire) {

        setDossiercible(dossierCible);
        setDestinataire(destinataire);
        return publier();
    }

    public void setDossiercible(final String dossierCible) {
        final WebElement elem = getDriver().findElement(By.id(DOSSIER_CIBLE));
        fillField("Dossier cible", elem, dossierCible);
    }

}
