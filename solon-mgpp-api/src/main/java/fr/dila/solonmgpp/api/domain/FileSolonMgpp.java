package fr.dila.solonmgpp.api.domain;

import fr.dila.st.api.domain.file.File;
import java.util.Calendar;

public interface FileSolonMgpp extends File {
    void setIdFiche(String idFiche);

    String getSafeFilename();

    String getCreator();

    Calendar getCreatedDate();
}
