package fr.dila.solonepg.ui.bean.pan;

import org.nuxeo.ecm.core.api.Blob;

/**
 * Contient les rapports générés pour un ensemble de paramètres et les
 * paramètres.
 */
public class ReportResultatData {
    private Blob htmlContent;
    private Blob pdfContent;
    private Blob xlsContent;
    private String paramList;

    public ReportResultatData() {
        super();
    }

    public ReportResultatData(Blob htmlContent, Blob pdfContent, Blob xlsContent, String paramList) {
        this();
        this.htmlContent = htmlContent;
        this.pdfContent = pdfContent;
        this.xlsContent = xlsContent;
        this.paramList = paramList;
    }

    public Blob getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(Blob htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Blob getPdfContent() {
        return pdfContent;
    }

    public void setPdfContent(Blob pdfContent) {
        this.pdfContent = pdfContent;
    }

    public Blob getXlsContent() {
        return xlsContent;
    }

    public void setXlsContent(Blob xlsContent) {
        this.xlsContent = xlsContent;
    }

    public String getParamList() {
        return paramList;
    }

    public void setParamList(String paramList) {
        this.paramList = paramList;
    }
}
