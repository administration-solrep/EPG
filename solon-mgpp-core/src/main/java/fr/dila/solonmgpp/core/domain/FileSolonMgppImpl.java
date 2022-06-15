package fr.dila.solonmgpp.core.domain;

import fr.dila.solonmgpp.api.domain.FileSolonMgpp;
import fr.dila.st.core.domain.file.FileImpl;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.BlobNotFoundException;

public class FileSolonMgppImpl extends FileImpl implements FileSolonMgpp {
    private static final long serialVersionUID = -50125367596597232L;

    public static final String FILE_SOLON_MGPP_DOC_TYPE = "FileSolonMgpp";
    public static final String FILE_SOLON_MGPP_SCHEMA = "file_solon_mgpp";
    public static final String FILE_SOLON_MGPP_PREFIX = "fmgpp";

    public static final String ID_FICHE = "idFiche";

    public FileSolonMgppImpl(DocumentModel document) {
        super(document);
    }

    @Override
    public void setIdFiche(String idFiche) {
        PropertyUtil.setProperty(document, FILE_SOLON_MGPP_SCHEMA, ID_FICHE, idFiche);
    }

    @Override
    public String getSafeFilename() {
        try {
            return getFilename();
        } catch (BlobNotFoundException e) {
            // do nothing
        }
        return document.getTitle();
    }

    @Override
    public String getCreator() {
        return DublincoreSchemaUtils.getCreator(document);
    }

    @Override
    public Calendar getCreatedDate() {
        return DublincoreSchemaUtils.getCreatedDate(document);
    }
}
