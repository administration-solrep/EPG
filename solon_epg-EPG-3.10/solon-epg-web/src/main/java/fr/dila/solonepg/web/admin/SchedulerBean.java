package fr.dila.solonepg.web.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.impl.blob.StreamingBlob;
import org.nuxeo.ecm.platform.filemanager.api.FileManager;
import org.nuxeo.ecm.platform.ui.web.util.files.FileUtils;
import org.nuxeo.runtime.api.Framework;

import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.dto.JobDTO;
import fr.dila.solonepg.api.service.SchedulerService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.constant.STVocabularyConstants;

@Name("schedulerSolonEpg")
@Scope(ScopeType.SESSION)
public class SchedulerBean {

    @In(create = true, required = true)
    protected transient CoreSession documentManager;
    
	private List<JobDTO> listJob;
	
	public List<JobDTO> findAllCronJob() throws ClientException {
		if (listJob == null) {
			// on va chercher les job du cron une seul fois vu qu'il ne peuvent
			// pas changer en cours de session
			SchedulerService schedulerService = SolonEpgServiceLocator.getSchedulerService();
			listJob = schedulerService.findAllCronJob();
		}
		return listJob;
	}

	public String execute(JobDTO jobDTO) throws ClientException {
		SchedulerService schedulerService = SolonEpgServiceLocator.getSchedulerService();
		schedulerService.execute(jobDTO, documentManager);

		return SolonEpgViewConstant.ESPACE_ADMIN_BATCH;
	}

	/**
	 * import DocumentModel : utilisé pour l'import des feuilles de route
	 */

	
	/**
	 * Importe les modèles de feuille de route.
	 */
    public void importDocument() throws ClientException {
        // TODO à passer en parametre
        String filePath = "/opt/export.zip";
        String nuxeoFdrRootPath = "/case-management/workspaces/admin";
        importDocument(filePath,nuxeoFdrRootPath);
    }
	

    /**
     * Importe les documentModel situé le fichier export.zip dans le document situé dans 'nuxeoRootpath'.
     * 
     * @param filePath chemin du fichier export.zip
     * @param nuxeoRootpath path nuxeo dans lequel on va créer les documentModels
     * @throws ClientException
     */
    public void importDocument(String filePath, String nuxeoRootpath) throws ClientException {
        try {
            File file = new File(filePath);
            String fileName = FileUtils.getCleanFileName(filePath);
            Blob blob = StreamingBlob.createFromFile(file);
            // TODO passer par un service
            /*DocumentModel createdDoc =*/ Framework.getService(FileManager.class).createDocumentFromBlob(documentManager, blob, nuxeoRootpath, true, fileName);
        } catch (Exception e) {
            throw new ClientException(e.toString());
        }
    }
    
    /**
     * Controle l'accès à la vue correspondante
     * 
     */
    public boolean isAccessAuthorized() {
    	SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
    	return (ssPrincipal.isAdministrator());
    }
	
	//TODO deplacer dans un bean SolonVocabularyBean 
	
	/**
	 * Renvoie la liste des noms de tout les répertoires (utilisé pour générer toutes les tables des vocabulaires).
	 */
	public List<String> getAllDirectoryName() {
	    List<String> allDirectoryName = new ArrayList<String>();
	    allDirectoryName.add(VocabularyConstants.DOSSIER_METADATA_DIRECTORY_NAME);
	    allDirectoryName.add(VocabularyConstants.FORMAT_FICHIER);
	    allDirectoryName.add(VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME);
	    allDirectoryName.add(VocabularyConstants.NATURE);
	    allDirectoryName.add(VocabularyConstants.DELAI_PUBLICATION);
        allDirectoryName.add(VocabularyConstants.MODE_PARUTION);
        allDirectoryName.add(VocabularyConstants.TYPE_PUBLICATION);
        allDirectoryName.add(VocabularyConstants.TYPE_ACTE_VOCABULARY);
        allDirectoryName.add(VocabularyConstants.ORGANIGRAMME_TYPE_NODE);
        allDirectoryName.add(STVocabularyConstants.BORDEREAU_LABEL);
        allDirectoryName.add(STVocabularyConstants.TRAITEMENT_PAPIER_LABEL);
        allDirectoryName.add(VocabularyConstants.FDR_COLUMN);
        allDirectoryName.add(VocabularyConstants.STATUT_ETAPE_RECHERCHE_DIRECTORY);
        //note :vocabulaire traitement papier
        allDirectoryName.add(VocabularyConstants.TYPE_SIGNATAIRE_TP_DIRECTORY_NAME);
        allDirectoryName.add(VocabularyConstants.NIVEAU_PRIORITE_TP_DIRECTORY_NAME);
        allDirectoryName.add(VocabularyConstants.TYPE_AVIS_TP_DIRECTORY_NAME);
        allDirectoryName.add(VocabularyConstants.TYPE_AVIS_TP_DIRECTORY_NAME);
        
        //TODO vocabulaire reponse : voir si nécessaire 
        allDirectoryName.add(VocabularyConstants.GROUPE_POLITIQUE);
        allDirectoryName.add(VocabularyConstants.VERROU);
        allDirectoryName.add(VocabularyConstants.NIVEAU_VISIBILITE);
        //TODO voir si utile
        allDirectoryName.add(VocabularyConstants.MINISTERES_DIRECTORY_NAME);
        // vocabulaire ST
        allDirectoryName.add(STVocabularyConstants.ROUTING_TASK_TYPE_VOCABULARY);
        allDirectoryName.add(STSchemaConstant.USER_TITLE);
        allDirectoryName.add(VocabularyConstants.BOOLEAN_VOCABULARY);
        //vocabulaire AN
        allDirectoryName.add(VocabularyConstants.NATURE_TEXTE_DIRECTORY);
        allDirectoryName.add(VocabularyConstants.PROCEDURE_VOTE_DIRECTORY);
        allDirectoryName.add(VocabularyConstants.TYPE_MESURE_VOCABULARY);
        allDirectoryName.add(VocabularyConstants.VOC_TYPE_HABILITATION);
        allDirectoryName.add(VocabularyConstants.VOC_DIRECTIVE_ETAT);
        
//        final DirectoryService directoryService = STServiceLocator.getDirectoryService();
//        allDirectoryName = directoryService.getDirectoryNames();
	    
	    return allDirectoryName;
	}
	
}
