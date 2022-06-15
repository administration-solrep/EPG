package fr.dila.solonepg.ui.th.bean;

import java.util.List;

public interface ModeleCourrierInterfaceForm {
    String getModeleName();

    void setModeleName(String modeleName);

    List<String> getTypesCommunication();

    void setTypesCommunication(List<String> typesCommunication);
}
