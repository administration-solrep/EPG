package fr.dila.solonepg.api.birt;

import java.io.Serializable;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.st.api.domain.STDomainObject;

public interface BirtRefreshFichier extends STDomainObject, Serializable {

    String getOwner();
    
    void setOwner(String owner) throws ClientException;
    
    String getFileName();
    
    void setFileName(String fileName) throws ClientException;
    
    Calendar getDateRequest();
    
    void setDateRequest(Calendar date) throws ClientException;
    
    Blob getHtmlContent();
    
    void setHtmlContent(Blob htmlContent) throws ClientException;
    
    Blob getXlsContent();
    
    void setXlsContent(Blob xlsContent) throws ClientException;
    
    Blob getPdfContent();
    
    void setPdfContent(Blob pdfContent) throws ClientException;
    
    boolean isRefreshing() throws ClientException;
    
    void setRefreshing(boolean refreshing) throws ClientException;
    
    String getParamList();
    
    void setParamList(String paramList) throws ClientException;
}
