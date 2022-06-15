package fr.dila.solonepg.ui.services.mgpp.impl;

import static fr.dila.ss.core.service.SSServiceLocator.getBirtGenerationService;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.ui.bean.MgppEcheancierPromulgationDTO;
import fr.dila.solonepg.ui.bean.MgppEcheancierPromulgationList;
import fr.dila.solonepg.ui.bean.MgppSuiviEcheancesDTO;
import fr.dila.solonepg.ui.bean.MgppSuiviEcheancesList;
import fr.dila.solonepg.ui.contentview.mgpp.EcheancierPromulgationPageProvider;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppCalendrierUIService;
import fr.dila.solonepg.ui.th.bean.MgppSuiviEcheancesListForm;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.ss.api.birt.BirtReport;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.th.bean.PaginationForm;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;

public class MgppCalendrierUIServiceImpl implements MgppCalendrierUIService {

    @Override
    public MgppEcheancierPromulgationList getEcheancierPromulgation(SpecificContext context) {
        CoreSession session = context.getSession();
        MgppEcheancierPromulgationList echeancierList = new MgppEcheancierPromulgationList();

        PaginationForm form = context.getFromContextData(STContextDataKey.PAGINATION_FORM);

        EcheancierPromulgationPageProvider provider = form.getPageProvider(
            session,
            "echeancierPromulgationPageProvider"
        );

        echeancierList.setListe(
            provider
                .getCurrentPage()
                .stream()
                .filter(MgppEcheancierPromulgationDTO.class::isInstance)
                .map(MgppEcheancierPromulgationDTO.class::cast)
                .collect(Collectors.toList())
        );
        echeancierList.setNbTotal((int) provider.getResultsCount());

        return echeancierList;
    }

    @Override
    public MgppSuiviEcheancesList getSuiviEcheances(SpecificContext context) {
        CoreSession session = context.getSession();
        MgppSuiviEcheancesList suiviEcheancesList = new MgppSuiviEcheancesList();

        MgppSuiviEcheancesListForm form = context.getFromContextData(MgppContextDataKey.SUIVI_ECHEANCES_LIST_FORM);

        CoreQueryDocumentPageProvider provider = form.getPageProvider(
            session,
            "suiviEcheancesPageProvider",
            "fpdr:",
            null
        );

        suiviEcheancesList.setListe(
            provider
                .getCurrentPage()
                .stream()
                .filter(Objects::nonNull)
                .map(this::dmToSuiviEcheancesDto)
                .collect(Collectors.toList())
        );
        suiviEcheancesList.setNbTotal((int) provider.getResultsCount());

        return suiviEcheancesList;
    }

    private MgppSuiviEcheancesDTO dmToSuiviEcheancesDto(DocumentModel dm) {
        FichePresentationDR ficheDR = dm.getAdapter(FichePresentationDR.class);

        MgppSuiviEcheancesDTO suiviEcheDto = new MgppSuiviEcheancesDTO();
        suiviEcheDto.setFicheId(dm.getId());
        suiviEcheDto.setNor(ficheDR.getIdDossier());
        suiviEcheDto.setObjet(ficheDR.getObjet());
        suiviEcheDto.setDateDepotEffectif(ficheDR.getDateDepotEffectif());
        suiviEcheDto.setDestinataire1Rapport(
            ficheDR.getDestinataire1Rapport().stream().map(ResourceHelper::getString).collect(Collectors.joining(", "))
        );

        return suiviEcheDto;
    }

    @Override
    public Blob generateReport(SpecificContext context) {
        String birtReportId = Objects.requireNonNull(context.getFromContextData(SSContextDataKey.BIRT_REPORT_ID));
        BirtReport birtReport = getBirtReport(birtReportId);
        BirtOutputFormat outputFormat = context.getFromContextData(SSContextDataKey.BIRT_OUTPUT_FORMAT);

        String reportName = birtReport.getFile();
        reportName = reportName.substring(0, reportName.length() - ("rptdesign".length() + 1));
        String fileName = FileUtils.sanitizePathTraversal(reportName + "." + outputFormat.getExtension());

        return SSServiceLocator
            .getSSBirtService()
            .generateReportResults(birtReport.getId(), fileName, new HashMap<>(), outputFormat);
    }

    private BirtReport getBirtReport(String id) {
        return getBirtGenerationService().getReport(id);
    }
}
