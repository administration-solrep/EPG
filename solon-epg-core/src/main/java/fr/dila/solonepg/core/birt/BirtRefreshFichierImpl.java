package fr.dila.solonepg.core.birt;

import fr.dila.solonepg.api.birt.BirtRefreshFichier;
import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.solonepg.api.constant.SolonEpgLifeCycleConstants;
import fr.dila.st.core.domain.STDomainObjectImpl;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;

public class BirtRefreshFichierImpl extends STDomainObjectImpl implements BirtRefreshFichier {
    private static final long serialVersionUID = 1L;

    public BirtRefreshFichierImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getOwner() {
        return getStringProperty(
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_OWNER_PROPERTY
        );
    }

    @Override
    public void setOwner(String owner) {
        setProperty(
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_OWNER_PROPERTY,
            owner
        );
    }

    @Override
    public String getFileName() {
        return getStringProperty(
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_FILE_NAME_PROPERTY
        );
    }

    @Override
    public void setFileName(String fileName) {
        setProperty(
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_FILE_NAME_PROPERTY,
            fileName
        );
    }

    @Override
    public Calendar getDateRequest() {
        return getDateProperty(
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_DATE_PROPERTY
        );
    }

    @Override
    public void setDateRequest(Calendar date) {
        setProperty(
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_DATE_PROPERTY,
            date
        );
    }

    @Override
    public Blob getHtmlContent() {
        return getBlobProperty(
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_CONTENT_PROPERTY
        );
    }

    @Override
    public void setHtmlContent(Blob htmlContent) {
        setProperty(
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_CONTENT_PROPERTY,
            htmlContent
        );
    }

    @Override
    public Blob getXlsContent() {
        return getBlobProperty(
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_CONTENT_CSV_PROPERTY
        );
    }

    @Override
    public void setXlsContent(Blob xlsContent) {
        setProperty(
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_CONTENT_CSV_PROPERTY,
            xlsContent
        );
    }

    @Override
    public Blob getPdfContent() {
        return getBlobProperty(
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_CONTENT_PDF_PROPERTY
        );
    }

    @Override
    public void setPdfContent(Blob pdfContent) {
        setProperty(
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_CONTENT_PDF_PROPERTY,
            pdfContent
        );
    }

    @Override
    public boolean isRefreshing() {
        return SolonEpgLifeCycleConstants.REFRESHING_STATE.equals(document.getCurrentLifeCycleState());
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
            document.followTransition(SolonEpgLifeCycleConstants.TO_REFRESHING_TRANSITION);
        } else {
            document.followTransition(SolonEpgLifeCycleConstants.TO_DONE_TRANSITION);
        }
    }

    @Override
    public String getParamList() {
        return getStringProperty(
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_PARAM_LIST_PROPERTY
        );
    }

    @Override
    public void setParamList(String paramList) {
        setProperty(
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_SCHEMA,
            ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_PARAM_LIST_PROPERTY,
            paramList
        );
    }
}
