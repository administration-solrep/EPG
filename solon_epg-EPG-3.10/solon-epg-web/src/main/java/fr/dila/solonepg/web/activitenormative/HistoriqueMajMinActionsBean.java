package fr.dila.solonepg.web.activitenormative;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.trash.TrashService;
import org.nuxeo.ecm.platform.contentview.jsf.ContentView;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.api.Framework;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants.MAJ_TARGET;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.HistoriqueMajMinisterielleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;


/**
 * Bean pour l'historique des mises à jour ministérielles
 * @author jgomez
 *
 */
@Name("majMinActions")
@Scope(ScopeType.CONVERSATION)
public class HistoriqueMajMinActionsBean implements Serializable{

	private static final long serialVersionUID = 135648L;

	@In(create = true, required = false)
    protected ActiviteNormativeActionsBean activiteNormativeActions;
	
    @In(create = true)
    protected transient ContentViewActions contentViewActions;
    
    @In(create = true)
    protected NavigationContext navigationContext;
    
    @In(create = true, required = false)
    protected transient TexteMaitreActionsBean texteMaitreActions;
    
    @In(create = true, required = false)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;
    
    @In(create = true, required = true)
    protected transient CoreSession documentManager;
    
    @In(create = true)
    protected transient DocumentsListsManager documentsListsManager;
    
    private static final Log LOGGER = LogFactory.getLog(HistoriqueMajMinActionsBean.class);
    
	/**
	 * Retourne la cible sur laquelle porte l'historique ( application des lois, ordonnances, transposition)
	 * @return la cible
	 */
	public MAJ_TARGET getTarget(){
		if (activiteNormativeActions.isInOrdonnances()){
			return MAJ_TARGET.ORDONNANCE;
		}
		if (activiteNormativeActions.isInApplicationDesLois()){
			return MAJ_TARGET.APPLICATION_LOI;
		}
		if (activiteNormativeActions.isInTransposition()){
			return  MAJ_TARGET.TRANSPOSITION;
		}
		if (activiteNormativeActions.isInOrdonnances38C()){
            return  MAJ_TARGET.HABILITATION;
        }
		if (activiteNormativeActions.isInApplicationDesOrdonnances()){
			return MAJ_TARGET.APPLICATION_ORDONNANCE;
		}
		return null;
	}
	
	/**
	 * Ouvre le dossier ayant pour identifiant dossierId dans l'espace d'activité normative
	 * @param dossierId l'identifiant de dossier à afficher
	 * @return la vue de l'activité normative
	 */
	public String openDossier(String dossierId) {
	       try{
	    	   navigationContext.navigateToId(dossierId);
	       }
	       catch (ClientException e){
	           String message = "L'ouverture du dossier " + StringUtils.defaultString(dossierId) + " a rencontré une erreur";
	    	   facesMessages.add(StatusMessage.Severity.ERROR, message);
	    	   LOGGER.error(message, e);
	  		   return null;
	       }
	       return SolonEpgViewConstant.VIEW_ESPACE_ACTIVITE_NORMATIVE;
	}
	
	/**
	 * Ouvre l'activité normative ayant pour numéro la variable numéro
	 * @param numero le numéro de l'activité normative
	 * @return la vue
	 */
	public String openMesure(String numero){
		   if (numero == null){
			   String message =  "La référence est nulle";
	           facesMessages.add(StatusMessage.Severity.ERROR,message);
			   LOGGER.error(message);
	           return null;
		   }
		   ActiviteNormativeService service = SolonEpgServiceLocator.getActiviteNormativeService();
		   DocumentModel activiteNormativeDoc;
		   try {
			   	activiteNormativeDoc = service.findActiviteNormativeDocByNumero(documentManager,numero);
		   } 
		   catch (ClientException e) {
			   String message =  "La recherche du numéro a rencontré une erreur " + numero;
			   LOGGER.error(message);
			   facesMessages.add(StatusMessage.Severity.ERROR,message);
			   return null;
		   }
		   if (activiteNormativeDoc == null){
			   String message =  "La référence " + StringUtils.defaultString(numero) + " n'est rattachée à aucun document";
	           facesMessages.add(StatusMessage.Severity.ERROR,message);
			   return null;
		   }
	       String view;
	       try {
	    	   view = texteMaitreActions.navigateToActiviteNormative(activiteNormativeDoc);
	       } 
	       catch (ClientException e) {
	    	   String message =  "La navigation vers l'activité normative n'a pas pu être effectuée";
	    	   LOGGER.error(message);
	    	   facesMessages.add(StatusMessage.Severity.ERROR,message);
			   return null;
	       }
	       return view;
	}

	
	/**
	 * Supprime toutes les mises à jour ministérielles de la cible courante (transposition, directive, ordonnance)
	 * @throws Exception
	 */
	public void massDelete() throws Exception{
	   HistoriqueMajMinisterielleService service = SolonEpgServiceLocator.getHistoriqueMajMinisterielleService();
	   List<DocumentModel> docs = service.getHistoriqueMaj(documentManager, this.getTarget());
	   if (!docs.isEmpty()) {
            TrashService trashService = Framework.getService(TrashService.class);
            trashService.trashDocuments(docs);
            Object[] params = { docs.size() };
            facesMessages.add(StatusMessage.Severity.INFO, "#0 " + resourcesAccessor.getMessages().get("label.epg.document.supprime" + (docs.size()>1 ? "s": "") ), params);
        } 
	}
	
	/**
	 * Supprime la sélection
	 * @throws Exception
	 */
	public void deleteSelection() throws Exception{
		ContentView contentView = contentViewActions.getCurrentContentView();
		if (contentView == null){
			return;
		}
        String selectionListName = contentView.getSelectionListName();
        if (documentsListsManager.isWorkingListEmpty(selectionListName)) {
            return;
        }
        List<DocumentModel> docs = documentsListsManager.getWorkingList(selectionListName);
        if (!docs.isEmpty()) {
            TrashService trashService = Framework.getService(TrashService.class);
            trashService.trashDocuments(docs);
            Object[] params = { docs.size() };
	        facesMessages.add(StatusMessage.Severity.INFO, "#0 " + resourcesAccessor.getMessages().get("label.epg.document.supprime" + (docs.size()>1 ? "s": "") ), params);
	     }
        documentsListsManager.resetWorkingList(selectionListName);
	}
	
	/**
	 * Détermine si la liste de sélection des mises à jour ministérielles est vide
	 * @return vrai si la liste de sélection est vide
	 */
	public Boolean isMajMinListEmpty(){
		return documentsListsManager.isWorkingListEmpty(ActiviteNormativeConstants.MAJ_MIN_SELECTION_LIST);
	}
	
}
