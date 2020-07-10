package fr.dila.solonepg.api.birt;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.st.api.domain.STDomainObject;

public interface ExportPANStat extends STDomainObject, Serializable {

    String getOwner();
    
    void setOwner(String owner) throws ClientException;
    
    Calendar getDateRequest();
    
    void setDateRequest(Calendar date) throws ClientException;
    
    boolean isExporting() throws ClientException;
    
    void setExporting(boolean refreshing) throws ClientException;
    
    List<String> getExportedLegislatures();
    
    void setExportedLegislatures(List<String> exportedLegislature);
    
    void setFileContent(Blob xlsContent) throws ClientException;
    
    Blob getFileContent();
    
    String getName();
    
    /**
     * Retourne faux si l'export concerne uniquement une législature précdente
     * Vrai sinon
     * @return
     */
    boolean isCurLegis(CoreSession session);

}
