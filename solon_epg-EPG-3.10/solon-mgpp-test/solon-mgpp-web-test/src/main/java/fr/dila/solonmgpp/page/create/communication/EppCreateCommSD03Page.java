package fr.dila.solonmgpp.page.create.communication;

import java.util.Calendar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailCommSD03Page;
import fr.dila.solonmgpp.utils.DateUtils;

public class EppCreateCommSD03Page extends AbstractEppCreateComm {

    public static final String TYPE_COMM = "SD-03 : Résultat du vote sur un sujet déterminé";
    public static final String DATE_VOTE_INPUT_DATE = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateVoteInputDate";
    public static final String OBJET_ID = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_objet";
    public static final String HORODATAGE = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_horodatage";

    public static final String SENS_AVIS = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_sensAvis";

    public EppDetailCommSD03Page createCommSD03(String sensAvis) {
        checkValue(COMMUNICATION, TYPE_COMM);
        checkValueContain(HORODATAGE, DateUtils.format(Calendar.getInstance()));
        setSensAvis(sensAvis);
        return publier();
    }

    public void setSensAvis(final String sensAvis) {
        final WebElement sensAvisElem = getDriver().findElement(By.id(SENS_AVIS));
        getFlog().action("Selectionne \"" + sensAvis + "\" dans le select \"Sens Avis\"");
        final Select select = new Select(sensAvisElem);
        select.selectByValue(sensAvis);
        // FAVORABLE"
    }

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailCommSD03Page.class;
    }

}
