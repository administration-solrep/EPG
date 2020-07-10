package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.sword.xsd.solon.epp.PieceJointeType;

public class MgppCreateCommJSS01Page extends AbstractMgppCreateComm {

    private static final String DATE_LETTRE_PM_INPUT_DATE_ID = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_dateLettrePmInputDate";

    private static final String LETTRE_PM_TITRE = "PJLPM";
    private static final String LETTRE_PM_URL = "url@gmail.fr";
    private static final String LETTRE_PM_PJ = "src/main/attachments/doc2007.doc";

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailCommJSS01Page.class;
    }

    public MgppDetailCommJSS01Page createCommJSS01(final String objet, final String destinataire, final String dateLettrePM) {
        setObjet(objet);
        setDestinataire(destinataire);
        setDateLettrePM(dateLettrePM);
        addPieceJointe(PieceJointeType.LETTRE_PM, LETTRE_PM_TITRE, LETTRE_PM_URL, LETTRE_PM_PJ);
        return publier();
        //return getPage(MgppDetailCommJSS01Page.class);
    }

    public void setDateLettrePM(final String dateLettrePM) {
        final WebElement elem = getDriver().findElement(By.id(DATE_LETTRE_PM_INPUT_DATE_ID));
        fillField("Date de la lettre du Premier ministre", elem, dateLettrePM);
    }

}
