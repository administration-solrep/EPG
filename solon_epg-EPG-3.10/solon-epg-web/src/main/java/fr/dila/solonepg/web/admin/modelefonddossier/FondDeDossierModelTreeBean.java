package fr.dila.solonepg.web.admin.modelefonddossier;

import static org.jboss.seam.ScopeType.EVENT;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.richfaces.component.UITree;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModelNode;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModelType;
import fr.dila.solonepg.api.service.FondDeDossierModelService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

/**
 * Classe de gestion de l'arbre contenant l'arborescence d'un modèle de fond de dossier.
 * 
 * @author ARN
 */
@Name("fondDeDossierModelTree")
@Scope(ScopeType.SESSION)
public class FondDeDossierModelTreeBean implements Serializable {
	
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = -4725108091555897065L;

    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(FondDeDossierModelTreeBean.class);
    
    private TreeNode<FondDeDossierModelNode> rootNode;
    
    private String fondDossierModelIdentifier;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;
    
    @In(create = true, required = false)
    protected transient NavigationContext navigationContext;
    
    @In(create = true, required = false)
    protected transient FondDeDossierModelManagerActionsBean fondDeDossierModelManagerActions;
    
    /**
     * charge l'arbre contenant l'arborescence du fond de dossier.
     * 
     * @throws ClientException
     */
    private void loadFondDossierModelTree(CoreSession session, DocumentModel fddModeleDocument) throws ClientException {
        
        rootNode = new TreeNodeImpl<FondDeDossierModelNode>();
        
        if (fddModeleDocument == null || fddModeleDocument.getId() == null) {
            log.info(" arbre du fond de dossier vide");
        } else{
            // on récupère les répertoire qui composent la racine du fond de dossier modèle
            final FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
            List<FondDeDossierModelNode> repertoiresRacine = fondDeDossierModelService.getFondDeDossierModelRootNode(session, fddModeleDocument);
            int counter = 1;
            // on parcourt les répertoires racine
            for (FondDeDossierModelNode elementFddNoeud : repertoiresRacine) {
                TreeNodeImpl<FondDeDossierModelNode> nodeImpl = new TreeNodeImpl<FondDeDossierModelNode>();
                //les noeuds de la racine sont non supprimables : on le definit dans le type de noeud
                elementFddNoeud.setType(FondDeDossierModelType.FOND_DOSSIER_FOLDER_UNDELETABLE_ROOT);
                nodeImpl.setData(elementFddNoeud);
    
                // on ajoute tous les élements fils du répertoire racine en utilisant une méthode récursive
                addFondDossierModelElement(elementFddNoeud.getId(), nodeImpl, session);
    
                rootNode.addChild(counter, nodeImpl);
                counter++;
            }
        }
    }
    
    /**
     * Méthode récursive qui ajoute les fils d'un noeud.
     * 
     * @param documentId nom du document contenant les documents fils
     * 
     * @param node noeud dans lequel les sous-groupes sont ajoutés
     * 
     * @throws ClientException
     */
    private void addFondDossierModelElement(String documentId, TreeNodeImpl<FondDeDossierModelNode> node, CoreSession session) throws ClientException
    {
    	List<FondDeDossierModelNode> childs;
    	final FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
    	childs = fondDeDossierModelService.getChildrenFondDeDossierModelNode(documentId, session);
    	
    	if(childs != null && !childs.isEmpty())
    	{
            int counter = 1;
            for (FondDeDossierModelNode childGroup : childs) {
                TreeNodeImpl<FondDeDossierModelNode> nodeImpl = new TreeNodeImpl<FondDeDossierModelNode>();
                //test
                nodeImpl.setData(childGroup);
                addFondDossierModelElement(childGroup.getId(), nodeImpl, session);

                node.addChild(counter, nodeImpl);

                counter++;
            }
    	}
    }
    
    /**
     * Méthode qui renvoie l'arborescence complète du modèle de fond de dossier.
     * 
     * @return l'arborescence complète du fond de dossier chargé
     * 
     * @throws ClientException
     */
    public TreeNode<FondDeDossierModelNode> getFondDeDossierModele() throws ClientException 
    {
        String currentFondDossierModelId = getCurrentFondDossierModelId();
        if (rootNode == null || fondDossierModelIdentifier == null || !currentFondDossierModelId.equals(fondDossierModelIdentifier) ) {
    	    log.info("tree reloaded ");
    	    fondDossierModelIdentifier = currentFondDossierModelId;
            loadFondDossierModelTree(documentManager,getFondDeDossierModelDocument());
        }
        return rootNode;
    }
    
    /**
     * Rècupère l'identifiant du modèle de fond de dossier courant.
     * 
     */
    public String getCurrentFondDossierModelId() {
        String idFondDossierModel =null;
        DocumentModel doc =  fondDeDossierModelManagerActions.getCurrentFondDossierModel();
        //DocumentModel doc = navigationContext.getCurrentDocument();
        if(doc != null && doc.getId()!= null){
            idFondDossierModel=doc.getId();
        } 
        return idFondDossierModel;
    }
    
    public String getFondDossierModelIdentifier() {
        return fondDossierModelIdentifier;
    }

    public void setFondDossierModelIdentifier(String fondDossierModelIdentifier) {
        this.fondDossierModelIdentifier = fondDossierModelIdentifier;
    }
    
    @Observer(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_CHANGED_EVENT)
    public void reloadFondDeDossierTree() throws ClientException
    {
    	log.info("reload fond de dossier modele tree");
    	loadFondDossierModelTree(documentManager,getFondDeDossierModelDocument());
    }
    
    
    /**
     * Get the current FondDeDossierModelDocument.
     *
     */
    @Factory(value = "fondDeDossierModelDocument", scope = EVENT)
    public DocumentModel getFondDeDossierModelDocument() {
    	//le document courant est le modèle de fond de dossier.
        DocumentModel doc = fondDeDossierModelManagerActions.getCurrentFondDossierModel();
    	if(doc == null || !doc.getType().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_TYPE)){
    	    doc = null;
    	}
        return doc;
    }

	/**
	 * method used to auto expend the fond de dossier tree.
	 *
	 */
	public Boolean adviseNodeOpened(UITree tree) {
            return Boolean.TRUE;
		}
}
