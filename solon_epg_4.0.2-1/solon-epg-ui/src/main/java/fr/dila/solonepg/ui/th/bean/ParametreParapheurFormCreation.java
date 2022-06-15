package fr.dila.solonepg.ui.th.bean;

import fr.dila.solonepg.api.constant.SolonEpgParametrageParapheurConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.mapper.MapDoc2BeanFilesListUploadProcess;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import java.util.ArrayList;
import javax.ws.rs.FormParam;

@SwBean
public class ParametreParapheurFormCreation {
    @SwNotEmpty
    @FormParam("estNonVide")
    @NxProp(
        docType = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_XPATH_EST_NON_VIDE
    )
    private boolean estNonVide;

    @FormParam("nbDocAccepteMax")
    @NxProp(
        docType = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_XPATH_NB_DOC_ACCEPTE_MAX
    )
    private Long nbDocAccepteMax;

    @FormParam("feuilleStyleFiles")
    @NxProp(
        docType = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_XPATH_FEUILLE_STYLE_FILES,
        process = MapDoc2BeanFilesListUploadProcess.class
    )
    private ArrayList<String> feuilleStyleFiles;

    @SwNotEmpty
    @FormParam("formatAutorise")
    @NxProp(
        docType = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageParapheurConstants.PARAMETRAGE_PARAPHEUR_XPATH_FORMAT_AUTORISE
    )
    private ArrayList<String> formatsAutorises;

    private boolean isFieldNbDocAccepteMaxEditable;

    @FormParam("parapheurFolderId")
    private String parapheurFolderId;

    @FormParam("parapheurFolderLabel")
    private String parapheurFolderLabel;

    public ParametreParapheurFormCreation() {}

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

    public ArrayList<String> getFeuilleStyleFiles() {
        return feuilleStyleFiles;
    }

    public void setFeuilleStyleFiles(ArrayList<String> feuilleStyleFiles) {
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

    public boolean isFieldNbDocAccepteMaxEditable() {
        return isFieldNbDocAccepteMaxEditable;
    }

    public void setFieldNbDocAccepteMaxEditable(boolean isFieldNbDocAccepteMaxEditable) {
        this.isFieldNbDocAccepteMaxEditable = isFieldNbDocAccepteMaxEditable;
    }
}
