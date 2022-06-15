package fr.dila.solonepg.api.birt;

import fr.dila.st.api.domain.STDomainObject;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;

public interface ExportPANStat extends STDomainObject, Serializable {
    String getOwner();

    void setOwner(String owner);

    Calendar getDateRequest();

    void setDateRequest(Calendar date);

    boolean isExporting();

    void setExporting(boolean refreshing);

    List<String> getExportedLegislatures();

    void setExportedLegislatures(List<String> exportedLegislature);

    void setFileContent(Blob xlsContent);

    Blob getFileContent();

    String getName();

    /**
     * Retourne faux si l'export concerne uniquement une législature précdente
     * Vrai sinon
     * @return
     */
    boolean isCurLegis(CoreSession session);
}
