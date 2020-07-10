package fr.dila.solonepg.web.admin.parametres;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webapp.helpers.EventManager;

import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.service.DossierBordereauService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

/**
 * 
 * Bean pour les paramêtres technique : surcharge du bean ParametreActionsBean du socle transverse.
 * @author arolin
 *
 */
@Name("parametreActions")
@Scope(CONVERSATION)
@Install(precedence = Install.FRAMEWORK +1)
public class ParametreActionsBean extends fr.dila.st.web.administration.parametre.ParametreActionsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Override
    public String updateDocument() throws ClientException {
        try {
            DocumentModel changeableDocument = saveDocument();
            updateBordereau(changeableDocument);
            return raiseEvent(changeableDocument);
        } catch (Exception e) {
            throw ClientException.wrap(e);
        }
    }
    
    /**
     * Enregistrement du document.
     * 
     * @return
     * @throws ClientException
     */
    protected DocumentModel saveDocument() throws ClientException{
        DocumentModel changeableDocument = navigationContext.getChangeableDocument();
        changeableDocument = documentManager.saveDocument(changeableDocument);
        documentManager.save();
        // some changes (versioning) happened server-side, fetch new one
        navigationContext.invalidateCurrentDocument();
        return changeableDocument;
    }
    
    /**
     * Envoi de l'evenement signalant que le document a changé et redirection.
     * 
     * @param changeableDocument
     * @return
     * @throws ClientException
     */
    protected String raiseEvent(DocumentModel changeableDocument) throws ClientException {
        facesMessages.add(StatusMessage.Severity.INFO, resourcesAccessor.getMessages().get("st.parametre.modified"));
        EventManager.raiseEventsOnDocumentChange(changeableDocument);
        return navigationContext.navigateToDocument(changeableDocument, "after-edit");
    }
    
    /**
     * Si l'on modifie une des expressions régulières vérifié dans le bordereau, on les réinitialise dans le service associe.
     * 
     * @param changeableDocument
     * @throws ClientException
     */
    protected void updateBordereau(DocumentModel changeableDocument) throws ClientException {
        if (changeableDocument == null) {
            return;
        }
        //on récupère le nom du paramètre technique modifié
        String idParamTechniqueUpdated = changeableDocument.getName();
        if (StringUtils.isEmpty(idParamTechniqueUpdated)) {
            return;
        }
        if (idParamTechniqueUpdated.equals(SolonEpgParametreConstant.FORMAT_REFERENCE_LOI) || idParamTechniqueUpdated.equals(SolonEpgParametreConstant.FORMAT_NUMERO_ORDRE) || idParamTechniqueUpdated.equals(SolonEpgParametreConstant.FORMAT_REFERENCE_ORDONNANCE)) {
            DossierBordereauService dossierBordereauService = SolonEpgServiceLocator.getDossierBordereauService();
            dossierBordereauService.reset();
        }
    }
    
}
