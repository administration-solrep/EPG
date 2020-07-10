package fr.dila.solonepg.web.dossier.parapheur;

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
import org.nuxeo.ecm.core.api.DocumentModel;
import org.richfaces.model.TreeNode;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.parapheur.ParapheurFile;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.dossier.DossierDistributionActionsBean;
import fr.dila.solonepg.web.espace.CorbeilleActionsBean;
import fr.dila.solonepg.web.tree.TreeBean;
import fr.dila.ss.api.tree.SSTreeFolder;
import fr.dila.ss.api.tree.SSTreeNode;
import fr.dila.st.web.dossier.DossierLockActionsBean;

/**
 * Classe de gestion de l'arbre contenant l'arborescence d'un parapheur.
 * 
 * @author ARN
 */
@Name("parapheurTree")
@Scope(ScopeType.SESSION)
public class ParapheurTreeBean extends TreeBean implements Serializable {

	/**
	 * Serial UID.
	 */
	private static final long							serialVersionUID	= -4725108091555897065L;

	/**
	 * Logger.
	 */
	private static final Log							log					= LogFactory
																					.getLog(ParapheurTreeBean.class);

	private String										parapheurIdentifier;

	@In(create = true, required = false)
	protected transient DossierLockActionsBean			dossierLockActions;

	@In(create = true, required = false)
	protected transient DossierDistributionActionsBean	dossierDistributionActions;

	@In(create = true, required = false)
	protected transient CorbeilleActionsBean			corbeilleActions;

	protected ParapheurService							parapheurService;

	protected FondDeDossierService						fondDeDossierService;

	/**
	 * Détermine si le parapheur est modifiable.
	 * 
	 * @return condition
	 * @throws ClientException
	 */
	@Factory(value = "isParapheurUpdatable", scope = EVENT)
	public boolean isParapheurUpdatable() throws ClientException {

		boolean isParapheurUpdatable = true;

		// Vérifie que l'utilisateur possède la fonction unitaire
		if (!currentUser.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_PARAPHEUR_UPDATER)) {
			isParapheurUpdatable = false;
		} else if (!dossierLockActions.getCanUnlockCurrentDossier()) {
			// Vérifie que le dossier en cours est verrouillé
			isParapheurUpdatable = false;
		} else if (corbeilleActions.getCurrentDossierLink() == null
				&& !currentUser.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UPDATER)) {
			// Si le dossier n'est pas distribué, seul l'administrateur fonctionnel a le droit modifier le parapheur
			isParapheurUpdatable = false;
		} else if (!dossierDistributionActions.isEtapePourImpressionUpdater()) {
			// À l'étape "pour impression", le dossier est en lecture seule sauf pour les administrateurs
			isParapheurUpdatable = false;
		} else if (getFondDeDossierService().isDossierEtapePourAvisCE(navigationContext.getCurrentDocument(),
				documentManager)) {
			// Si le document est à une étape 'Pour avis CE' on ne peut pas modifier le parapheur (FEV505)
			isParapheurUpdatable = false;
		} else if (navigationContext.getCurrentDocument() != null) {
			Dossier dossier = navigationContext.getCurrentDocument().getAdapter(Dossier.class);
			isParapheurUpdatable = !VocabularyConstants.STATUT_NOR_ATTRIBUE.equals(dossier.getStatut());
		}

		return isParapheurUpdatable;
	}

	@Override
	protected List<ParapheurFolder> getRootFolders() throws ClientException {
		return getParapheurService().getParapheurRootNode(documentManager, getParapheurDocument());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<ParapheurFolder> getChildrenFolder(DocumentModel parentDoc) throws ClientException {
		return (List<ParapheurFolder>) getParapheurService().getChildrenFolder(documentManager, parentDoc);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<ParapheurFile> getChildrenFile(DocumentModel parentDoc) throws ClientException {
		return (List<ParapheurFile>) getParapheurService().getChildrenFile(documentManager, parentDoc);
	}

	/**
	 * Méthode qui renvoie l'arborescence complète du parapheur.
	 * 
	 * @return l'arborescence complète du parapheur chargé
	 * @throws ClientException
	 */
	public TreeNode<SSTreeNode> getParapheur() throws ClientException {
		String currentParapheurModelId = getCurrentParapheurId();
		log.debug("récupération du parapheur : dossierOldIdentifier = |" + parapheurIdentifier
				+ "|dossierCurrentIdentifier|" + currentParapheurModelId);
		// si l'arborescence est vide ou si le parapheur a changé on recharge l'arbre.
		if (rootNode == null || parapheurIdentifier == null || !currentParapheurModelId.equals(parapheurIdentifier)) {
			log.debug("rechargement de l'arborescence du parapheur");
			parapheurIdentifier = currentParapheurModelId;
			loadTree();
		}
		return rootNode;
	}

	/**
	 * Rècupère l'identifiant du parapheur courant.
	 * 
	 */
	public String getCurrentParapheurId() {
		String idParapheur = null;
		DocumentModel parapheur = getParapheurDocument();
		if (parapheur != null) {
			idParapheur = parapheur.getId();
		}
		return idParapheur;
	}

	@Observer(SolonEpgParapheurConstants.PARAPHEUR_CHANGED_EVENT)
	public void reloadParapheurTree() throws ClientException {
		if (log.isInfoEnabled()) {
			log.debug("reload parapheur tree");
		}
		loadTree();
	}

	/**
	 * Get the current ParapheurDocument.
	 */
	@Factory(value = "parapheurDocument", scope = EVENT)
	public DocumentModel getParapheurDocument() {
		// on récupère le parapheur à partir du document courant (dossier)
		DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		if (dossierDoc != null) {
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			return dossier.getParapheur(documentManager).getDocument();
		} else {
			return null;
		}

	}

	@Override
	protected DocumentModel getArborescenceDocument() throws ClientException {
		return getParapheurDocument();
	}

	protected ParapheurService getParapheurService() {
		if (parapheurService == null) {
			parapheurService = SolonEpgServiceLocator.getParapheurService();
		}
		return parapheurService;
	}

	public Boolean isFolderFull(SSTreeFolder folder) {
		if (folder != null) {
			// on récupère le nombre maximum de fichiers acceptes
			Long ndDocAcceptemax = ((ParapheurFolder) folder).getNbDocAccepteMax();
			int nbFile = 0;
			try {
				nbFile = getChildrenFile(folder.getDocument()).size();
			} catch (ClientException e) {
				// TODO LOGGER
			}
			// on vérifie la possibilité d'ajout de document
			return (ndDocAcceptemax != null && nbFile >= ndDocAcceptemax.intValue());
		}
		return false;
	}

	/**
	 * @author ABI
	 * 
	 *         Sert à vérifier que l'utilisateur ait le droit de gérer le dossier "Epreuve"
	 * 
	 * @param folder
	 * @return
	 */
	public Boolean isEpreuveAndUnauthorized(SSTreeFolder folder) {
		if (folder == null) {
			return false;
		}
		// si l'étape n'est pas "pour fourniture d'épreuves" on ne se pose pas de question
		if (!folder.getTitle().equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EPREUVES_NAME)) {
			return true;
		}
		// test si l'utilisateur est autorisé à créer l'étape pour fourniture d'épreuve
		final List<String> groups = currentUser.getGroups();
		return groups.contains(SolonEpgBaseFunctionConstant.ETAPE_FOURNITURE_EPREUVE_CREATOR);
	}

	protected FondDeDossierService getFondDeDossierService() {
		if (fondDeDossierService == null) {
			fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
		}
		return fondDeDossierService;
	}
}
