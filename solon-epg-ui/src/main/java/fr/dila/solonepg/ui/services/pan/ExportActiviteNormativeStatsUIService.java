package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.api.birt.ExportPANStat;
import fr.dila.solonepg.ui.bean.pan.PanTreeNode;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public interface ExportActiviteNormativeStatsUIService {
    void exportPanStats(SpecificContext context);

    boolean isExporting(SpecificContext context);

    void initExportParameters(SpecificContext context);

    Boolean isAllowedToExportAN(SpecificContext context);

    void createZipFile(SpecificContext context);

    List<PanTreeNode> getExportTreeNodes(SpecificContext context);

    void publierLegisPrec(SpecificContext context);

    Boolean isAllowedToEditParameters(SpecificContext context);

    Boolean isAllowedToPublishLegislature(SpecificContext context);

    Boolean isAllowedToViewJournalTechnique(SpecificContext context);

    Boolean displayExportTreeNode(SpecificContext context);

    void exportLegisPrec(SpecificContext context);

    boolean isLegisPrecExported(SpecificContext context);

    boolean isLegisExporting(SpecificContext context);

    List<ExportPANStat> getAllExportPanStatDoc(CoreSession documentManager, SSPrincipal ssPrincipal);
}
