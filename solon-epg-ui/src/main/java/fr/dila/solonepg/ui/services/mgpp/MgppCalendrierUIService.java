package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.solonepg.ui.bean.MgppEcheancierPromulgationList;
import fr.dila.solonepg.ui.bean.MgppSuiviEcheancesList;
import fr.dila.st.ui.th.model.SpecificContext;
import org.nuxeo.ecm.core.api.Blob;

public interface MgppCalendrierUIService {
    MgppEcheancierPromulgationList getEcheancierPromulgation(SpecificContext context);

    MgppSuiviEcheancesList getSuiviEcheances(SpecificContext context);

    Blob generateReport(SpecificContext context);
}
