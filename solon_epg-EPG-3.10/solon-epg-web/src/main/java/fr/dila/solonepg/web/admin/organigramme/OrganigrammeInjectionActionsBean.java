package fr.dila.solonepg.web.admin.organigramme;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.api.client.InjectionEpgGvtDTO;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.service.EpgInjectionGouvernementService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.util.ExcelUtil;
import fr.dila.st.api.constant.STViewConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.administration.organigramme.OrganigrammeTreeBean;

/**
 * Injection de gouvernement.
 * 
 * @author jbrunet
 */
@Name("organigrammeInjectionActions")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.APPLICATION + 1)
public class OrganigrammeInjectionActionsBean extends fr.dila.ss.web.admin.organigramme.OrganigrammeInjectionActionsBean {
    protected static final String INJECTION_GOUVERNEMENT_XLS_FILENAME = "injection_gouvernement.xls";

	protected List<InjectionEpgGvtDTO> listCompared;
	
	/**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    private static final STLogger LOGGER = STLogFactory.getLog(OrganigrammeInjectionActionsBean.class);
    
    /**
     * Action préliminaire à l'injection : création du DTO extrait
     * 
     * @return
     * @throws ClientException 
     */
    public void preparerInjection() throws ClientException {
        LOGGER.info(documentManager, STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC);
        resetErrorProperties();
        // Récupération du fichier sur le serveur
        String injectionExcelDirPath = STServiceLocator.getConfigService().getValue(SolonEpgConfigConstant.SOLON_INJECTION_GOUVERNEMENT_DIRECTORY);
    	final File report = new File(injectionExcelDirPath + "/" + INJECTION_GOUVERNEMENT_XLS_FILENAME);
        if (!report.exists()) {
            setErrorName(resourcesAccessor.getMessages().get(ERROR_MSG_NO_FILE_SELECTED));
            facesMessages.add(StatusMessage.Severity.ERROR, resourcesAccessor.getMessages().get(ERROR_MSG_NO_FILE_SELECTED));
            setNoPageError(false);
            throw new EPGException("Fichier Excel d'injection non présent");
        } else {
            try {
            	EpgInjectionGouvernementService epgInjectionGouvernementService = SolonEpgServiceLocator.getEpgInjectionGouvernementService();
                // Récupération du DTO d'injection
            	listInjection = epgInjectionGouvernementService.prepareInjection(documentManager, report);
                
            	// on réinitialise les variables de l'édition de fichier
                resetProperties();
                if (listInjection == null) {
                	throw new EPGException("Aucun gouvernement n'a été récupéré : erreur lors de la lecture du fichier");
                }                
                
            } catch (final Exception exc) {
                LOGGER.error(documentManager, STLogEnumImpl.FAIL_PROCESS_INJECTION_GOUVERNEMENT_TEC, exc);
                setNoPageError(false);
                setErrorName(exc.getMessage());
                facesMessages.add(StatusMessage.Severity.ERROR, resourcesAccessor.getMessages().get("ss.organigramme.injection.ajout.echec"));
                facesMessages.add(StatusMessage.Severity.ERROR, exc.getMessage());
                throw new EPGException("Récupération impossible du gouvernement depuis le fichier Excel");
            } 
        }
    }
    
    /**
     * Action préliminaire à l'injection : création du DTO de comparaison
     * 
     * @return
     * @throws ClientException 
     */
    public void preparerComparaisonEPG() throws ClientException {
        LOGGER.info(documentManager, STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC);
        try {
        	// Création du DTO de comparaison pour l'affichage
            listCompared = SolonEpgServiceLocator.getEpgInjectionGouvernementService().createComparedDTO(documentManager, listInjection);
            
        } catch (final Exception exc) {
            LOGGER.error(documentManager, STLogEnumImpl.FAIL_PROCESS_INJECTION_GOUVERNEMENT_TEC, exc);
            facesMessages.add(StatusMessage.Severity.ERROR, resourcesAccessor.getMessages().get("ss.organigramme.injection.ajout.echec"));
            facesMessages.add(StatusMessage.Severity.ERROR, exc.getMessage());
            throw new EPGException("Récupération impossible du gouvernement depuis le fichier Excel");
        }
    }
    
    protected static HttpServletResponse getHttpServletResponse() {
        ServletResponse response = null;
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            response = (ServletResponse) facesContext.getExternalContext().getResponse();
        }
        if (response != null && response instanceof HttpServletResponse) {
            return (HttpServletResponse) response;
        }
        return null;
    }
    
    /**
     * Exporte le gouvernement dans un fichier Excel .xls
     */
    public void exportGouvernement() {
		HttpServletResponse response = getHttpServletResponse();
        if (response == null) {
            return;
        }

        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");

        response.addHeader("Content-Disposition", "attachment; filename=\"gouvernement.xls\"");
        
        OutputStream outputStream;
        try {
	        // récupération réponse
	        outputStream = response.getOutputStream();
	        InputStream inputStream = ExcelUtil.createExportGvt(documentManager).getInputStream();
	        BufferedInputStream fif = new BufferedInputStream(inputStream);
	        // copie le fichier dans le flux de sortie
	        int data;
	        while ((data = fif.read()) != -1) {
	            outputStream.write(data);
	        }
	        outputStream.flush();
	        outputStream.close();
	        FacesContext.getCurrentInstance().responseComplete();
    	} catch (IOException e) {
    		LOGGER.error(documentManager, STLogEnumImpl.FAIL_CREATE_EXCEL_TEC, "Erreur lors de la réponse",e);
    	}
    }
    
    /**
     * Enregistre le fichier sur le serveur
     * @return
     */
    public String saveExcelInjection() {
    	
    	resetErrorProperties();
        if (getCurrentFile() == null) {
            setErrorName(resourcesAccessor.getMessages().get(ERROR_MSG_NO_FILE_SELECTED));
            facesMessages.add(StatusMessage.Severity.ERROR, resourcesAccessor.getMessages().get(ERROR_MSG_NO_FILE_SELECTED));
            setNoPageError(false);
        } else {
	    	try {
		    	InputStream in = new FileInputStream(currentFile);
		    	
		    	ConfigService configService = STServiceLocator.getConfigService();
		        String injectionExcelDirPath = configService.getValue(SolonEpgConfigConstant.SOLON_INJECTION_GOUVERNEMENT_DIRECTORY);
		    	
		    	// Vérification de l'existence du répertoire
		    	File generatedFilesDir = new File(injectionExcelDirPath);
		        if (!generatedFilesDir.exists()) {
		            generatedFilesDir.mkdirs();
		        }
		    	
		    	// Création du fichier
		    	final File injectionFile = new File(injectionExcelDirPath + "/" + INJECTION_GOUVERNEMENT_XLS_FILENAME);
		        if (!injectionFile.exists()) {
		        	injectionFile.createNewFile();
		        }
		        
		        // Enregistrement
		        final FileOutputStream outputStream = new FileOutputStream(injectionFile);
		        byte[] buf = new byte[1024];
		        int len;
		        while ((len = in.read(buf)) > 0) {
		        	outputStream.write(buf, 0, len);
		        }
		        in.close();
		        outputStream.close();
		        // on remet currentFile à null
		        resetProperties();
		        facesMessages.add(StatusMessage.Severity.INFO, resourcesAccessor.getMessages().get("ss.organigramme.injection.save.file"));
	    	} catch(IOException e) {
	    		LOGGER.error(documentManager, STLogEnumImpl.FAIL_CREATE_EXCEL_TEC, 
	    				"Erreur lors de l'enregistrement du fichier Excel d'injection du gouvernement",e);
	    		facesMessages.add(StatusMessage.Severity.INFO, resourcesAccessor.getMessages().get("ss.organigramme.injection.save.file.fail"));
	    	}
        }
        return STViewConstant.ORGANIGRAMME_VIEW_MANAGE;
    }
    
    /**
     * Teste la présence du fichier excel d'injection sur le serveur
     * 
     * @return
     */
    public boolean isFichierInjectionDisponible() {
    	String injectionExcelDirPath = STServiceLocator.getConfigService().getValue(SolonEpgConfigConstant.SOLON_INJECTION_GOUVERNEMENT_DIRECTORY);
    	final File report = new File(injectionExcelDirPath + "/" + INJECTION_GOUVERNEMENT_XLS_FILENAME);
        return report.exists();
    }
    
    public boolean isModified(String baseString, String importedString) {
    	if (baseString == null) {
    		return importedString!=null;
    	}
    	return !baseString.equals(importedString);
    }
    
    public String executeInjectionEPG() {
    	try {
    		LOGGER.info(documentManager, STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC);
    		SolonEpgServiceLocator.getEpgInjectionGouvernementService().executeInjection(documentManager,listInjection);
    		facesMessages.add(StatusMessage.Severity.INFO, resourcesAccessor.getMessages().get("ss.organigramme.injection.ajout.succes"));
    	} catch (ClientException e) {
    		LOGGER.error(documentManager, STLogEnumImpl.FAIL_PROCESS_INJECTION_GOUVERNEMENT_TEC, e);
    		facesMessages.add(StatusMessage.Severity.ERROR, resourcesAccessor.getMessages().get("ss.organigramme.injection.ajout.echec"));
    		facesMessages.add(StatusMessage.Severity.ERROR, e.getMessage());
    	}
    	Events.instance().raiseEvent(OrganigrammeTreeBean.ORGANIGRAMME_CHANGED_EVENT);
    	return STViewConstant.ORGANIGRAMME_VIEW_MANAGE;
    }
    
    /**
     * Prépare l'injection
     * @return vue récapitulative avant injection
     */
    public String injecterGouvernementEPG() {
    	// Récupération des données du fichier Excel
    	try {
    		preparerInjection();
    		preparerComparaisonEPG();
    	} catch (ClientException e) {
    		return STViewConstant.ORGANIGRAMME_VIEW_MANAGE;
    	}
    	return "view_injection_epg";
    }
    
    public String cancelInjection() {
    	resetProperties();
    	resetErrorProperties();
    	if (listInjection != null) {
    		listInjection.clear();
    	}
    	if (listCompared != null) {
    		listCompared.clear();
    	}
    	return STViewConstant.ORGANIGRAMME_VIEW_MANAGE;
    }

	public List<InjectionEpgGvtDTO> getListCompared() {
		return listCompared;
	}

	public void setListCompared(List<InjectionEpgGvtDTO> listCompared) {
		this.listCompared = listCompared;
	}
	
	public void generatePdf() {
		final HttpServletResponse response = getHttpServletResponse();
        if (response == null) {
            return;
        }
        EpgInjectionGouvernementService epgInjectionGouvernementService = SolonEpgServiceLocator.getEpgInjectionGouvernementService();
        
        response.reset();
        try {
	        final OutputStream outputStream = response.getOutputStream();
	        
	        // Génération du pdf
	        epgInjectionGouvernementService.generatePdf(documentManager,outputStream, listCompared);
	
	        // prepare pdf response
	        response.setHeader("Content-Type", "application/pdf");
	        response.setHeader("Content-Disposition", "inline; filename=injection.pdf");
	
	        FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        	LOGGER.error(documentManager, STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC, "Erreur de génération du pdf", e);
        	return;
        }
	}
}
