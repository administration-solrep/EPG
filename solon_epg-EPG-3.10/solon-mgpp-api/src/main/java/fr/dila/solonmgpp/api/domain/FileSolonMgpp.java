package fr.dila.solonmgpp.api.domain;

import fr.dila.st.api.domain.file.File;

public interface FileSolonMgpp extends File {

    public static final String FILE_SOLON_MGPP_DOC_TYPE = "FileSolonMgpp";
    public static final String FILE_SOLON_MGPP_SCHEMA = "file_solon_mgpp";
    public static final String FILE_SOLON_MGPP_PREFIX = "fmgpp";

    public static final String ID_FICHE = "idFiche";

    void setIdFiche(String idFiche);

}
