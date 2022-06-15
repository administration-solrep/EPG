package fr.dila.solonepg.ui.bean;

import fr.dila.st.ui.bean.DossierDTO;
import java.util.ArrayList;
import java.util.List;

public class EpgDossierDTO extends DossierDTO {
    private List<EpgDossierDTO> lstFolders = new ArrayList<>();
    private List<String> acceptedFileTypes = new ArrayList<>();

    public EpgDossierDTO() {
        super();
    }

    public List<EpgDossierDTO> getLstFolders() {
        return lstFolders;
    }

    public void setLstFolders(List<EpgDossierDTO> lstFolders) {
        this.lstFolders = lstFolders;
    }

    public List<String> getAcceptedFileTypes() {
        return acceptedFileTypes;
    }

    public void setAcceptedFileTypes(List<String> acceptedFileTypes) {
        this.acceptedFileTypes = acceptedFileTypes;
    }
}
