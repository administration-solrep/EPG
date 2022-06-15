package fr.dila.solonepg.api.birt;

import fr.dila.solon.birt.common.BirtOutputFormat;
import java.io.Serializable;
import org.nuxeo.ecm.core.api.Blob;

public interface BirtReportContent extends Serializable {
    Blob getHtmlContent();

    void setHtmlContent(Blob htmlContent);

    Blob getXlsContent();

    void setXlsContent(Blob xlsContent);

    Blob getPdfContent();

    void setPdfContent(Blob pdfContent);

    default Blob getContent(BirtOutputFormat format) {
        if (BirtOutputFormat.XLS == format) {
            return this.getXlsContent();
        } else if (BirtOutputFormat.PDF == format) {
            return this.getPdfContent();
        } else {
            return this.getHtmlContent();
        }
    }
}
