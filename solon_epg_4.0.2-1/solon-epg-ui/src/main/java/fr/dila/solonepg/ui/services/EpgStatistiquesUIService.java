package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.EpgBirtReportList;
import fr.dila.solonepg.ui.bean.StatutArchivageDossierList;
import fr.dila.solonepg.ui.services.impl.StatistiquesItem;
import fr.dila.ss.api.birt.BirtReport;
import fr.dila.ss.ui.services.SSStatistiquesUIService;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public interface EpgStatistiquesUIService extends SSStatistiquesUIService {
    Map<Integer, StatistiquesItem> getStatistquesMap(SpecificContext context, String type);

    Map<Integer, StatistiquesItem> getStatistquesMapSgg(SpecificContext context);

    EpgBirtReportList getStatistquesMapSggAsList(SpecificContext context);

    EpgBirtReportList getStatistquesMapGlobalAsList(SpecificContext context);

    String getHtmlReportURL(SpecificContext context);

    BirtReport getBirtReport(SpecificContext context);

    List<String> getScalarParamsForBirtReport(SpecificContext context);

    List<SelectValueDTO> getVecteurPublicationSelectValues(SpecificContext context);

    File genererStatistiques(SpecificContext context);

    boolean canViewSggStat(NuxeoPrincipal user);

    StatutArchivageDossierList getStatutArchivageDossierList(SpecificContext context);

    void exportListResultats(SpecificContext context);

    Map<Integer, StatistiquesItem> getStatistquesMapGlobal(SpecificContext context);
}
