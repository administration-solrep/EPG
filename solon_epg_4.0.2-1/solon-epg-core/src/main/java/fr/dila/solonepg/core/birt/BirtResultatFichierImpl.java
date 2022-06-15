package fr.dila.solonepg.core.birt;

import fr.dila.solonepg.api.birt.BirtResultatFichier;
import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;

public class BirtResultatFichierImpl implements BirtResultatFichier {
    private static final long serialVersionUID = 1L;

    private final DocumentModel document;

    public BirtResultatFichierImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public String getReportName() {
        return PropertyUtil.getStringProperty(
            document,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_FILE_NAME_PROPERTY
        );
    }

    @Override
    public void setReportName(String reportName) {
        PropertyUtil.setProperty(
            document,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_FILE_NAME_PROPERTY,
            reportName
        );
    }

    @Override
    public Blob getHtmlContent() {
        return PropertyUtil.getBlobProperty(
            document,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PROPERTY
        );
    }

    @Override
    public void setHtmlContent(Blob htmlContent) {
        PropertyUtil.setProperty(
            document,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PROPERTY,
            htmlContent
        );
    }

    @Override
    public Blob getXlsContent() {
        return PropertyUtil.getBlobProperty(
            document,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_CSV_PROPERTY
        );
    }

    @Override
    public void setXlsContent(Blob xlsContent) {
        PropertyUtil.setProperty(
            document,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_CSV_PROPERTY,
            xlsContent
        );
    }

    @Override
    public Blob getPdfContent() {
        return PropertyUtil.getBlobProperty(
            document,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PDF_PROPERTY
        );
    }

    @Override
    public void setPdfContent(Blob pdfContent) {
        PropertyUtil.setProperty(
            document,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PDF_PROPERTY,
            pdfContent
        );
    }

    @Override
    public Calendar getDateModified() {
        return PropertyUtil.getCalendarProperty(
            document,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_MODIFIED
        );
    }

    @Override
    public void setDateModified(Calendar date) {
        PropertyUtil.setProperty(
            document,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_MODIFIED,
            Calendar.getInstance()
        );
    }
}
