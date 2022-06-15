package fr.dila.solonepg.ui.bean;

import fr.dila.st.ui.bean.ColonneInfo;
import java.util.List;
import java.util.Objects;

public interface MgppExportable {
    String getExportName();

    List<ColonneInfo> getListeColonnes();

    List<List<String>> getDataForExport();

    default String[] getHeaders() {
        return getListeColonnes().stream().map(ColonneInfo::getLabel).filter(Objects::nonNull).toArray(String[]::new);
    }
}
