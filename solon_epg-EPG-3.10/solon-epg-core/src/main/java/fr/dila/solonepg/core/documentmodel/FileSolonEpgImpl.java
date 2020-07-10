package fr.dila.solonepg.core.documentmodel;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.documentmodel.FileSolonEpg;
import fr.dila.ss.core.tree.SSTreeFileImpl;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.util.PropertyUtil;

/**
 * Implementation Interface FileSolonEpg : utilisée pour les fichiers du parapheurs et du fond de dossiers de SOLON EPG.
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
        return getStringProperty(DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA, DossierSolonEpgConstants.FILE_SOLON_EPG_ENTITE_PROPERTY);
    }

    @Override
    public void setEntite(String entiteDernierTraitement) {
        setProperty(DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA, DossierSolonEpgConstants.FILE_SOLON_EPG_ENTITE_PROPERTY,entiteDernierTraitement);
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
        setProperty(DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA, DossierSolonEpgConstants.FILE_SOLON_EPG_RELATED_DOCUMENT_PROPERTY,idDocument);
    }

    @Override
    public String getRelatedDocument() {
        return getStringProperty(DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA, DossierSolonEpgConstants.FILE_SOLON_EPG_RELATED_DOCUMENT_PROPERTY);
    }

    @Override
    public void setFiletypeId(Integer filetypeId) {
        setProperty(DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA, DossierSolonEpgConstants.FILE_SOLON_EPG_FILETYPE_ID,filetypeId);
    }

    @Override
    public Integer getFiletypeId() {
        return  PropertyUtil.getIntegerProperty(document,DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,  DossierSolonEpgConstants.FILE_SOLON_EPG_FILETYPE_ID);
    }

}
