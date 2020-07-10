package fr.dila.solonmgpp.web.tree;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Remove;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.core.Events;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.ui.web.util.files.FileUtils;
import org.nuxeo.ecm.webapp.filemanager.UploadItemHolder;
import org.richfaces.component.html.HtmlInputText;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeFichierDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.core.validator.PieceJointeFichierValidator;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.MD5Util;

/**
 * Bean seam de gestion des fichiers des pieces jointes
 * 
 * @author asatre
 */
@Synchronized(timeout = 10000)
@Name("pieceJointeTreeManagerActions")
@Scope(ScopeType.SESSION)
public class PieceJointeTreeManagerActionsBean implements Serializable {

    private static final long serialVersionUID = -3486558978767123945L;

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(PieceJointeTreeManagerActionsBean.class);    

    @In(create = true, required = false)
    protected UploadItemHolder fileUploadHolder;

    @In(create = true, required = false)
    protected NavigationContextBean navigationContext;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    private PieceJointeDTO currentPieceJointe;

    private String errorName;

    private final Map<String, Blob> mapBlobFDD = new HashMap<String, Blob>();
    
    private String fileName;

    /**
     * Nombre de fichier téléchargeable par défaut
     */
    private int uploadsAvailable = Integer.MAX_VALUE;

    private PieceJointeFichierDTO pieceJointeFichierDoc;

    private Boolean visible = false;

    @Remove
    @Destroy
    public void destroy() {
        LOGGER.debug(documentManager, STLogEnumImpl.REMOVE_WORKSPACE_ACTIONS_BEAN_TEC) ;        
    }

    /**
     * Reset the temp properties
     * 
     */
    public void resetProperties() {
        uploadsAvailable = Integer.MAX_VALUE;
        setUploadedFiles(null);
        if (fileUploadHolder != null) {
            fileUploadHolder.reset();
        }
        errorName = null;

        mapBlobFDD.clear();

        setVisible(false);
    }

    /**
     * Ajout ou Modification d'un fichier dans un répertoire.
     * 
     * @throws IOException
     * 
     */
    public void ajoutDocument() throws ClientException, IOException {        
        LOGGER.info(documentManager, STLogEnumImpl.CREATE_FILE_FONC) ;
        
        if (currentPieceJointe == null) {
            errorName = "Aucune pièce jointe séléctionnée";
        } else if ((mapBlobFDD == null || mapBlobFDD.isEmpty()) && (getUploadedFiles() == null || getUploadedFiles().isEmpty())) {
            errorName = "Aucun fichier sélectionné";
        } else {
            try {

                // récupération des données du fichier courant
                List<UploadItem> uploadItemList = new ArrayList<UploadItem>();

                if (getUploadedFiles() != null) {
                    uploadItemList.addAll(getUploadedFiles());
                }

                List<PieceJointeFichierDTO> list = currentPieceJointe.getFichier();
                if (list == null) {
                    list = new ArrayList<PieceJointeFichierDTO>();
                }

                for (UploadItem uploadItem : uploadItemList) {
                    // récupération du nom du fichier
                    String filename = FileUtils.getCleanFileName(uploadItem.getFileName());
                    File file = uploadItem.getFile();

                    PieceJointeFichierValidator pieceJointeFichierValidator = PieceJointeFichierValidator.getInstance();
                    try {
                        pieceJointeFichierValidator.validatePieceJointeFichierFileName(filename);
                    } catch (ClientException e) {
                        errorName = e.getMessage();
                        return;
                    }

                    // récupération du contenu du fichier
                    Blob blob = FileUtils.createSerializableBlob(new FileInputStream(file), filename, null);
                    PieceJointeFichierDTO pieceJointeFichierDTO = SolonMgppServiceLocator.getPieceJointeService().createPieceJointeFichierFromBlob(blob, documentManager);

                    list.add(pieceJointeFichierDTO);

                }

                // ajout des fichiers FDD
                for (Blob blob : mapBlobFDD.values()) {

                    String computedMd5 = MD5Util.getMD5Hash(blob.getByteArray());
                    blob.setDigest(computedMd5);

                    PieceJointeFichierDTO pieceJointeFichierDTO = SolonMgppServiceLocator.getPieceJointeService().createPieceJointeFichierFromBlob(blob, documentManager);

                    list.add(pieceJointeFichierDTO);
                }

                currentPieceJointe.setFichier(list);

                resetProperties();
                currentPieceJointe = null;

            } finally {
                if (fileUploadHolder != null) {
                    fileUploadHolder.reset();
                }
            }
        }
    }

    public void fileUploadListener(UploadEvent event) throws Exception {
        if (event == null || event.getUploadItem() == null || event.getUploadItem().getFileName() == null) {
            errorName = "Le fichier est vide";
            return;
        }
        if (currentPieceJointe == null) {
            errorName = "Aucune pièce jointe séléctionnée";
            return;
        }

        PieceJointeFichierValidator pieceJointeFichierValidator = PieceJointeFichierValidator.getInstance();
        try {
            String filename = FileUtils.getCleanFileName(event.getUploadItem().getFileName());
            pieceJointeFichierValidator.validatePieceJointeFichierFileName(filename);
        } catch (ClientException e) {
            errorName = e.getMessage();
            return;
        }

        // on transmet le fichier dans le bean dédié et on écrase l'ancienne valeur;
        Collection<UploadItem> uploadData = getUploadedFiles();
        if (uploadData == null) {
            uploadData = new ArrayList<UploadItem>();
        }

        UploadItem uploadedItem = event.getUploadItem();
        DatedUploadItem datedUploadItem = new DatedUploadItem(uploadedItem.getFileName(), uploadedItem.getFileSize(),
        		uploadedItem.getContentType(), uploadedItem.getFile());
        datedUploadItem.setStartDate(new Date());
        if (!isUploadSameTime(datedUploadItem)) {
            uploadData.add(datedUploadItem);
        }
    	setUploadedFiles(uploadData);
    }
    
    /**
     * Solution de conoutnement pour el Mantis M155426
     * Lors de l'ajout d'une pièce jointe, celle ci s'ajoute systématiquement deux fois. 
     * Dans le code cette action est perçu comme deux évènements différent avec le même fichier uploader. 
     * 
     * Pour chaque évènement, on ajoute au fichier uploadé une date d'upload pour ensuite vérifier qu'il n'y ai pas 
     * plusieurs upload dans la même seconde. Si c'est le cas on n'effectue pas le second ajout . 
     * 
     * @param item
     * @return
     */
    private boolean isUploadSameTime(DatedUploadItem item) {
    	
    	for (UploadItem uploadedItem : fileUploadHolder.getUploadedFiles()) {
    		DatedUploadItem datedUploadedItem = (DatedUploadItem) uploadedItem;
    		if (datedUploadedItem.getStartDate() != null && item.getStartDate() != null) {
    			long intervalle = Math.abs((datedUploadedItem.getStartDate().getTime() - item.getStartDate().getTime()) / 1000);
    			if (intervalle < 1) {
    				return true;
    			}
    		}
    	}
    	return false;
    }

    /**
     * Getter/setter pour les fichiers.
     */

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

    public int getUploadsAvailable() {
        return uploadsAvailable;
    }

    public void setUploadsAvailable(int uploadsAvailable) {
        this.uploadsAvailable = uploadsAvailable;
    }

    public PieceJointeFichierDTO getPieceJointeFichierDoc() {
        return pieceJointeFichierDoc;
    }

    public void setPieceJointeFichierDoc(PieceJointeFichierDTO pieceJointeFichierDoc) {
        this.pieceJointeFichierDoc = pieceJointeFichierDoc;
    }

    public PieceJointeDTO getCurrentPieceJointe() {
        return currentPieceJointe;
    }

    public void setCurrentPieceJointe(PieceJointeDTO currentPieceJointe) {
        this.currentPieceJointe = currentPieceJointe;
        if (fileUploadHolder != null) {
            fileUploadHolder.reset();
        }
        setVisible(true);
    }

    /**
     * Ajout de piece jointe via le FDD
     * 
     * @param currentPieceJointe
     * @param evenementDTO
     * @throws ClientException
     */
    public void setCurrentPieceJointeFDD(PieceJointeDTO currentPieceJointe, EvenementDTO evenementDTO) throws ClientException {
        setCurrentPieceJointe(currentPieceJointe, evenementDTO);
        Events.instance().raiseEvent(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_TREE_CHANGED_EVENT);
    }

    /**
     * Ajout de piece jointe via le Parapheur
     * 
     * @param currentPieceJointe
     * @param evenementDTO
     * @throws ClientException
     */
    public void setCurrentPieceJointeParapheur(PieceJointeDTO currentPieceJointe, EvenementDTO evenementDTO) throws ClientException {
        setCurrentPieceJointe(currentPieceJointe, evenementDTO);
        Events.instance().raiseEvent(SolonEpgParapheurConstants.PARAPHEUR_CHANGED_EVENT);
    }

    private void setCurrentPieceJointe(PieceJointeDTO currentPieceJointe, EvenementDTO evenementDTO) throws ClientException {
        this.currentPieceJointe = currentPieceJointe;
        if (fileUploadHolder != null) {
            fileUploadHolder.reset();
        }

        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        UIComponent component = findComponent(root, "metadonnees_evenement_nor");

        String nor = null;
        if (component instanceof HtmlInputText) {
            nor = (String) ((HtmlInputText) component).getSubmittedValue();
        }

        if (StringUtils.isBlank(nor)) {
            component = findComponent(root, "metadonnees_evenement_identifiant_dossier");
            if (component instanceof HtmlInputText) {
                nor = (String) ((HtmlInputText) component).getSubmittedValue();
            }
        }

        if (StringUtils.isBlank(nor)) {
            // recherche dans le currentEvenement
            nor = evenementDTO.getNor();
            if (StringUtils.isBlank(nor)) {
                // recherche dans le currentEvenement
                nor = evenementDTO.getIdDossier();
            }
        }

        if (StringUtils.isNotBlank(nor)) {
            if (navigationContext.getCurrentDocument() == null || !navigationContext.getCurrentDocument().getAdapter(Dossier.class).getNumeroNor().equals(nor)) {
                // recherche via numeroNor
                Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager, nor);
                if (dossier != null) {
                    navigationContext.setCurrentDocument(dossier.getDocument());

                } else {
                    // recherche via idDossier
                    dossier = SolonEpgServiceLocator.getDossierService().findDossierFromIdDossier(documentManager, nor);
                    if (dossier != null) {
                        navigationContext.setCurrentDocument(dossier.getDocument());

                    } else {
                        navigationContext.setCurrentDocument(null);
                    }
                }
            }
        } else {
            navigationContext.setCurrentDocument(null);
        }

        setVisible(true);
    }

    
     public boolean isLinkedToDossier(EvenementDTO evenementDTO) throws ClientException {

        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        UIComponent component = findComponent(root, "metadonnees_evenement_nor");

        String nor = null;
        if (component instanceof HtmlInputText) {
            nor = (String) ((HtmlInputText) component).getSubmittedValue();
        }

        if (StringUtils.isBlank(nor)) {
            component = findComponent(root, "metadonnees_evenement_identifiant_dossier");
            if (component instanceof HtmlInputText) {
                nor = (String) ((HtmlInputText) component).getSubmittedValue();
            }
        }

        if (StringUtils.isBlank(nor)) {
            // recherche dans le currentEvenement
            nor = evenementDTO.getNor();
            if (StringUtils.isBlank(nor)) {
                // recherche dans le currentEvenement
                nor = evenementDTO.getIdDossier();
            }
        }

        if (StringUtils.isNotBlank(nor)) {
            if (navigationContext.getCurrentDocument() == null || !navigationContext.getCurrentDocument().getAdapter(Dossier.class).getNumeroNor().equals(nor)) {
                // recherche via numeroNor
                Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager, nor);
                if (dossier != null) {
                   return true;

                }
                dossier = SolonEpgServiceLocator.getDossierService().findDossierFromIdDossier(documentManager, nor);
                
                if (dossier != null) {
                        return true;
                } 
                    return false;
            }else{
                if(navigationContext.getCurrentDocument().getAdapter(Dossier.class).getNumeroNor().equals(nor)){
                    return true;  
                }
            }
        } else{
            return false;
        }

        return false;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorName() {
        return errorName;
    }

    public String getPieceJointeType() {
        if (currentPieceJointe != null) {
            return currentPieceJointe.getType();
        }
        return null;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setFromBlob(Blob blob, String itemId) {
        if (mapBlobFDD.get(itemId) == null && blob != null) {
            mapBlobFDD.put(itemId, blob);
        } else {
            mapBlobFDD.remove(itemId);
        }
    }

    private UIComponent findComponent(UIComponent uiComp, String ident) {
        if (uiComp.getId().endsWith(ident)) {
            return uiComp;
        }
        Iterator<UIComponent> kids = uiComp.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent found = findComponent(kids.next(), ident);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public void clearUploadData(ActionEvent even) {
        try {
            // test if a single file was cleared....
            if (fileName != null && !"".equals(fileName)) {

                Iterator<UploadItem> iter = fileUploadHolder.getUploadedFiles().iterator();
                while (iter.hasNext()) {
                    UploadItem uploadItem = iter.next();
                    if (fileName.equals(uploadItem.getFileName())) {
                        fileUploadHolder.getUploadedFiles().remove(uploadItem);
                        break;
                    }
                }
            }

            else {
                if (fileUploadHolder.getUploadedFiles() != null) {
                    fileUploadHolder.getUploadedFiles().clear();
                }
            }
        } catch (Exception e) {
            LOGGER.debug(documentManager, STLogEnumImpl.FAIL_CLEAR_UPLOADED_DATA_TEC);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
