package fr.dila.solonmgpp.api.domain;

import fr.dila.ss.api.tree.SSTreeFile;
import java.util.List;

public interface ModeleCourrier extends SSTreeFile {
    List<String> getTypesCommuncation();

    void setTypesCommuncation(List<String> value);
}
