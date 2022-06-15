package fr.dila.solonepg.core.documentmodel;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.documentmodel.FileSolonEpg;
import fr.dila.ss.core.tree.SSTreeFileImpl;
import fr.dila.st.core.override.listener.DublinCoreListener;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.VersioningOption;
import org.nuxeo.ecm.core.api.versioning.VersioningService;

/**
 * Implementation Interface FileSolonEpg : utilisée pour les fichiers du
 * parapheurs et du fond de dossiers de SOLON EPG.
 *
 */
public class FileSolonEpgImpl extends SSTreeFileImpl implements FileSolonEpg {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -914322775097387701L;

    /**
     * @param document
     */
    public FileSolonEpgImpl(DocumentModel document) {
        super(document);
    }

    @Override
    public String getEntite() {
        return getStringProperty(
            DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,
            DossierSolonEpgConstants.FILE_SOLON_EPG_ENTITE_PROPERTY
        );
    }

    @Override
    public void setEntite(String entiteDernierTraitement) {
        setProperty(
            DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,
            DossierSolonEpgConstants.FILE_SOLON_EPG_ENTITE_PROPERTY,
            entiteDernierTraitement
        );
    }

    @Override
    public String getUserDernierTraitement() {
        return DublincoreSchemaUtils.getLastContributor(document);
    }

    @Override
    public Calendar getDateDernierTraitement() {
        return DublincoreSchemaUtils.getModifiedDate(document);
    }

    @Override
    public void setRelatedDocument(String idDocument) {
        setProperty(
            DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,
            DossierSolonEpgConstants.FILE_SOLON_EPG_RELATED_DOCUMENT_PROPERTY,
            idDocument
        );
    }

    @Override
    public String getRelatedDocument() {
        return getStringProperty(
            DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,
            DossierSolonEpgConstants.FILE_SOLON_EPG_RELATED_DOCUMENT_PROPERTY
        );
    }

    @Override
    public void setFiletypeId(Long filetypeId) {
        setProperty(
            DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,
            DossierSolonEpgConstants.FILE_SOLON_EPG_FILETYPE_ID,
            filetypeId
        );
    }

    @Override
    public Long getFiletypeId() {
        return PropertyUtil.getLongProperty(
            document,
            DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,
            DossierSolonEpgConstants.FILE_SOLON_EPG_FILETYPE_ID
        );
    }

    @Override
    public boolean isEditing() {
        return PropertyUtil.getBooleanProperty(
            document,
            DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,
            DossierSolonEpgConstants.FILE_SOLON_EPG_EDITING_PROPERTY
        );
    }

    @Override
    public void setEditing(boolean editing) {
        setEditing(editing, false, false);
    }

    @Override
    public void setEditing(boolean editing, boolean disableVersioning, boolean disableDublincoreListener) {
        PropertyUtil.setProperty(
            document,
            DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,
            DossierSolonEpgConstants.FILE_SOLON_EPG_EDITING_PROPERTY,
            editing
        );
        if (disableVersioning) {
            document.putContextData(VersioningService.VERSIONING_OPTION, VersioningOption.NONE);
        }
        if (disableDublincoreListener) {
            document.putContextData(DublinCoreListener.DISABLE_DUBLINCORE_LISTENER, true);
        }
    }
}
