package fr.dila.solonepg.ui.th.bean;

import fr.dila.solonepg.api.constant.SolonEpgParametrageParapheurConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.DocumentDTO;
import fr.dila.st.ui.mapper.MapDoc2BeanFilesFilesProcess;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.FormParam;

@SwBean
public class ParametreParapheurFormConsultation {
    @NxProp(
        docType = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_XPATH_EST_NON_VIDE
    )
    private boolean estNonVide;

    @NxProp(
        docType = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_XPATH_NB_DOC_ACCEPTE_MAX
    )
    private Long nbDocAccepteMax;

    @NxProp(
        docType = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_XPATH_FEUILLE_STYLE_FILES,
        process = MapDoc2BeanFilesFilesProcess.class
    )
    private List<DocumentDTO> feuilleStyleFiles;

    @NxProp(
        docType = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_XPATH_FORMAT_AUTORISE
    )
    private ArrayList<String> formatsAutorises;

    @FormParam("parapheurFolderId")
    private String parapheurFolderId;

    @FormParam("parapheurFolderLabel")
    private String parapheurFolderLabel;

    public ParametreParapheurFormConsultation() {}

    public boolean isEstNonVide() {
        return estNonVide;
    }

    public void setEstNonVide(boolean estNonVide) {
        this.estNonVide = estNonVide;
    }

    public Long getNbDocAccepteMax() {
        return nbDocAccepteMax;
    }

    public void setNbDocAccepteMax(Long nbDocAccepteMax) {
        this.nbDocAccepteMax = nbDocAccepteMax;
    }

    public List<DocumentDTO> getFeuilleStyleFiles() {
        return feuilleStyleFiles;
    }

    public void setFeuilleStyleFiles(List<DocumentDTO> feuilleStyleFiles) {
        this.feuilleStyleFiles = feuilleStyleFiles;
    }

    public ArrayList<String> getFormatsAutorises() {
        return formatsAutorises;
    }

    public void setFormatsAutorises(ArrayList<String> formatsAutorises) {
        this.formatsAutorises = formatsAutorises;
    }

    public String getParapheurFolderId() {
        return parapheurFolderId;
    }

    public void setParapheurFolderId(String parapheurFolderId) {
        this.parapheurFolderId = parapheurFolderId;
    }

    public String getParapheurFolderLabel() {
        return parapheurFolderLabel;
    }

    public void setParapheurFolderLabel(String parapheurFolderLabel) {
        this.parapheurFolderLabel = parapheurFolderLabel;
    }
}
