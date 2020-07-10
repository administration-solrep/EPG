package fr.dila.solonmgpp.web.fichepresentation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.platform.ui.web.util.files.FileUtils;
import org.nuxeo.ecm.webapp.filemanager.UploadItemHolder;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.transaction.TransactionHelper;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import fr.dila.solonmgpp.api.constant.SolonMgppI18nConstant;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FileSolonMgpp;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.corbeille.CorbeilleTreeBean;
import fr.dila.solonmgpp.web.filter.FilterActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.MD5Util;

/**
 * Bean de gestion des fichiers du fond de dossier pour les fiches de presentation
 * 
 * @author asatre
 * 
 */
@Name("fondDossierFichePresentationActions")
@Scope(ScopeType.CONVERSATION)
public class FondDossierFichePresentationActionsBean implements Serializable, ReloadableBean {

    private static final long serialVersionUID = 961648605023222217L;

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(FondDossierFichePresentationActionsBean.class);

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = true)
    protected transient FilterActionsBean filterActions;

    @In(create = true, required = true)
    protected transient FichePresentationOEPActionsBean fichePresentationOEPActions;

    @In(create = true, required = true)
    protected transient FichePresentationAVIActionsBean fichePresentationAVIActions;

    @In(create = true, required = true)
    protected transient FichePresentationIEActionsBean fichePresentationIEActions;

    @In(create = true, required = true)
    protected transient FichePresentationDOCActionsBean fichePresentationDOCActions;

    @In(create = true, required = true)
    protected transient FichePresentationSDActionsBean fichePresentationSDActions;

    @In(create = true, required = true)
    protected transient FichePresentationDPGActionsBean fichePresentationDPGActions;

    @In(create = true, required = true)
    protected transient FichePresentationJSSActionsBean fichePresentationJSSActions;

    @In(create = true, required = true)
    protected transient FichePresentationAUDActionsBean fichePresentationAUDActions;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true, required = false)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected UploadItemHolder fileUploadHolder;

    @In(create = true, required = false)
    protected CorbeilleTreeBean corbeilleTree;

    private DocumentModel currentDoc;

    private String fileId;

    private String errorName;

    /**
     * recuperation des fichiers suivant menu secondaire en cours
     */
    public List<DocumentModel> getCurrentFiles() {
        try {
            if (filterActions.isInOEP()) {
                currentDoc = fichePresentationOEPActions.getFicheOEP();
            } else if (filterActions.isInAVI()) {
                currentDoc = fichePresentationAVIActions.getFicheAVI();
            } else if (filterActions.isInInterventionExterieure()) {
                currentDoc = fichePresentationIEActions.getFicheIEDoc();
            } else if (filterActions.isInAUD()) {
                currentDoc = fichePresentationAUDActions.getFiche();
            } else if (filterActions.isInDOC()) {
                currentDoc = fichePresentationDOCActions.getFiche();
            } else if (filterActions.isInDPG()) {
                currentDoc = fichePresentationDPGActions.getFiche();
            } else if (filterActions.isInSD()) {
                currentDoc = fichePresentationSDActions.getFiche();
            } else if (filterActions.isInJSS()) {
                currentDoc = fichePresentationJSSActions.getFiche();
            }

            if (currentDoc != null) {
                return SolonMgppServiceLocator.getFondDossierService().findFileFor(documentManager, currentDoc);
            } else {
                facesMessages.add(StatusMessage.Severity.WARN, "Aucune fiche trouvée");
                TransactionHelper.setTransactionRollbackOnly();
            }
        } catch (ClientException e) {
            LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, e);
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
        }

        return null;
    }

    public void removeFile(DocumentModel file) {
        try {
            SolonMgppServiceLocator.getFondDossierService().removeFileFor(documentManager, file);
        } catch (ClientException e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_DEL_FICHE_LOI_TEC, e);
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
        }
    }

    public void resetProperties() {
        setUploadedFiles(null);
        if (fileUploadHolder != null) {
            fileUploadHolder.reset();
        }
        errorName = null;
    }

    /**
     * Ajout ou Modification d'un fichier dans un répertoire.
     * 
     * @throws IOException
     * 
     */
    public void ajoutDocument() {

        try {

            if (currentDoc == null) {
                facesMessages.add(StatusMessage.Severity.WARN, "Aucune fiche trouvée");
                TransactionHelper.setTransactionRollbackOnly();
                return;
            }

            // récupération des données du fichier courant
            List<UploadItem> uploadItemList = new ArrayList<UploadItem>();

            if (getUploadedFiles() != null) {
                uploadItemList.addAll(getUploadedFiles());
            }

            for (UploadItem uploadItem : uploadItemList) {
                // récupération du nom du fichier
                String filename = FileUtils.getCleanFileName(uploadItem.getFileName());
                File file = uploadItem.getFile();

                // récupération du contenu du fichier
                Blob blob = FileUtils.createSerializableBlob(new FileInputStream(file), filename, null);

                SolonMgppServiceLocator.getFondDossierService().addFileFor(documentManager, currentDoc, blob, navigationContext.getCurrentEvenement());

                facesMessages.add(StatusMessage.Severity.INFO, "Fichier ajouté");

                refreshCorbeilleAterFileUpload();

            }

            resetProperties();
        } catch (Exception e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_CREATE_FICHE_LOI_TEC, e);
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();

        } finally {
            if (fileUploadHolder != null) {
                fileUploadHolder.reset();
            }
        }
    }

    private void refreshCorbeilleAterFileUpload() {
        if (currentDoc.hasSchema(FichePresentationOEP.SCHEMA)) {
            // creation d'un EVT49 suite a l'ajout d'un fichier pour un OEP => refresh corbeille
            corbeilleTree.forceRefreshNoReset();
        } else if (currentDoc.hasSchema(FichePresentationAVI.SCHEMA)) {
            // creation d'un EVT32 suite a l'ajout d'un fichier pour un AVI => refresh corbeille
            corbeilleTree.forceRefreshNoReset();
        }
    }

    public void fileUploadListener(UploadEvent event) throws Exception {

        if (event == null || event.getUploadItem() == null || event.getUploadItem().getFileName() == null) {
            errorName = "Le fichier est vide";
            return;
        }

        // on transmet le fichier dans le bean dédié et on écrase l'ancienne valeur;
        Collection<UploadItem> uploadData = getUploadedFiles();
        if (uploadData == null) {
            uploadData = new ArrayList<UploadItem>();
        }
        uploadData.add(event.getUploadItem());
        setUploadedFiles(uploadData);
    }

    public Collection<UploadItem> getUploadedFiles() {
        if (fileUploadHolder != null) {
            return fileUploadHolder.getUploadedFiles();
        } else {
            return null;
        }
    }

    public void setUploadedFiles(Collection<UploadItem> uploadedFiles) {
        if (fileUploadHolder != null) {
            fileUploadHolder.setUploadedFiles(uploadedFiles);
        }
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorName() {
        return errorName;
    }

    public String getLibelleAjouterFichier() {
        if (filterActions.isInOEP()) {
            return resourcesAccessor.getMessages().get(SolonMgppI18nConstant.AJOUT_FICHIER_OEP);
        } else {
            return resourcesAccessor.getMessages().get(SolonMgppI18nConstant.AJOUT_FICHIER_DEFAULT);
        }
    }

    /**
     * Telechargement d'un fichier du FDD
     */
    public void downloadFile() {

        FileSolonMgpp fileSolonMgpp = null;
        try {
            fileSolonMgpp = documentManager.getDocument(new IdRef(fileId)).getAdapter(FileSolonMgpp.class);
        } catch (ClientException ce) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC, ce);
            facesMessages.add(StatusMessage.Severity.WARN, ce.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
            return;
        }

        try {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.reset();
            response.setHeader("Content-disposition", "attachment;filename=" + fileSolonMgpp.getFilename());
            response.setContentLength(fileSolonMgpp.getContent().getByteArray().length);
            response.getOutputStream().write(fileSolonMgpp.getContent().getByteArray());
            response.setContentType(fileSolonMgpp.getMimeType());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_LOI_TEC);
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
        }
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileId() {
        return fileId;
    }

    @Override
    public String reload() {
        return null;
    }

}
