package fr.dila.solonmgpp.core.domain;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.FileSolonMgpp;
import fr.dila.st.core.domain.file.FileImpl;

public class FileSolonMgppImpl extends FileImpl implements FileSolonMgpp {

    private static final long serialVersionUID = -50125367596597232L;

    public FileSolonMgppImpl(DocumentModel document) {
        super(document);
    }

    @Override
    public void setIdFiche(String idFiche) {
        PropertyUtil.setProperty(document, FILE_SOLON_MGPP_SCHEMA, ID_FICHE, idFiche);
    }

}
