package fr.dila.solonepg.web.dossier.fonddossier;

import static org.jboss.seam.ScopeType.EVENT;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.richfaces.model.TreeNode;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFile;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.dossier.DossierDistributionActionsBean;
import fr.dila.solonepg.web.espace.CorbeilleActionsBean;
import fr.dila.solonepg.web.feuilleroute.FilterRoutingTaskActionBean;
import fr.dila.solonepg.web.tree.TreeBean;
import fr.dila.ss.api.constant.SSFondDeDossierConstants;
import fr.dila.ss.api.tree.SSTreeFolder;
import fr.dila.ss.api.tree.SSTreeNode;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STWebserviceConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.dossier.DossierLockActionsBean;
import fr.sword.xsd.solon.epg.TypeModification;

/**
 * Classe de gestion de l'arbre contenant l'arborescence du fond de dossier.
 * 
 * @author ARN
 */
@Synchronized(timeout = 10000)
@Name("fondDeDossierTree")
@Scope(ScopeType.SESSION)
public class FondDeDossierTreeBean extends TreeBean {

	/**
	 * Serial UID
	 */
	private static final long							serialVersionUID	= 7166994463363454637L;

	/**
	 * Logger.
	 */
	private static final STLogger						LOGGER				= STLogFactory.getLog(FondDeDossierTreeBean.class);
	
	@In(create = true, required = false)
	protected transient DossierLockActionsBean			dossierLockActions;

	@In(create = true, required = false)
	protected transient DossierDistributionActionsBean	dossierDistributionActions;

	@In(create = true, required = false)
	protected transient CorbeilleActionsBean			corbeilleActions;
	
	@In(create = true, required = false)
	private transient FilterRoutingTaskActionBean		filterRoutingTaskActionBean;

	protected FondDeDossierService						fondDeDossierService;

	protected String									selectedNodeId;

	/**
	 * Détermine si le fond de dossier est modifiable.
	 * 
	 * @return condition
	 * @throws ClientException
	 */
	@Factory(value = "isFondDossierUpdatable", scope = EVENT)
	public boolean isFondDossierUpdatable() throws ClientException {

		boolean isFondDossierUpdatable = true;

		// Vérifie que l'utilisateur possède la fonction unitaire
		if (!currentUser.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_FOND_DOSSIER_UPDATER)) {
			isFondDossierUpdatable = false;
		} else if (!dossierLockActions.getCanUnlockCurrentDossier()) {
			// Vérifie que le dossier en cours est verrouillé
			isFondDossierUpdatable = false;
		} else if (corbeilleActions.getCurrentDossierLink() == null
				&& !currentUser.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UPDATER)) {
			// Si le dossier n'est pas distribué, seul l'administrateur fonctionnel a le droit modifier le fond de
			// dossier
			isFondDossierUpdatable = false;
		} else if (!dossierDistributionActions.isEtapePourImpressionUpdater()) {
			isFondDossierUpdatable = false;
		} else if (navigationContext.getCurrentDocument() != null) {
			Dossier dossier = navigationContext.getCurrentDocument().getAdapter(Dossier.class);
			isFondDossierUpdatable = !VocabularyConstants.STATUT_NOR_ATTRIBUE.equals(dossier.getStatut());
		}
		
		// Cas précis du dossier à l'étape "pour avis CE" et l'utilisateur du groupe EnvoiSaisinePieceComplementaireExecutor
		// et que le dossier est verrouillé
		if (!isFondDossierUpdatable 
				&& currentUser.isMemberOf(SolonEpgBaseFunctionConstant.CONSEIL_ETAT_ENVOI_SAISINE_PIECE_COMPLEMENTAIRE_EXECUTOR)
				&& filterRoutingTaskActionBean.isEtapePourAvisCE(true)
				&& dossierLockActions.getCanUnlockCurrentDossier()) {
			isFondDossierUpdatable = true;
		}
		
		return isFondDossierUpdatable;
	}

	@Override
	protected List<FondDeDossierFolder> getRootFolders() throws ClientException {
		return getFondDeDossierService().getAllVisibleRootFolder(documentManager, getDossierDocument(), currentUser);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<FondDeDossierFolder> getChildrenFolder(DocumentModel parentDoc) throws ClientException {
		return (List<FondDeDossierFolder>) getFondDeDossierService().getChildrenFolder(documentManager, parentDoc);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<FondDeDossierFile> getChildrenFile(DocumentModel parentDoc) throws ClientException {
		return (List<FondDeDossierFile>) getFondDeDossierService().getChildrenFile(documentManager, parentDoc);
	}

	/**
	 * Méthode qui renvoie l'arborescence complète du fond de dossier.
	 * 
	 * @return l'arborescence complète du fond de dossier chargé
	 * @throws ClientException
	 */
	public TreeNode<SSTreeNode> getFondDeDossier() throws ClientException {
		String currentDossierId = getCurrentDossierId();
		if (rootNode == null || dossierIdentifier == null || !currentDossierId.equals(dossierIdentifier)) {
			if (LOGGER.isDebugEnable()) {
				LOGGER.debug(documentManager, STLogEnumImpl.LOG_DEBUG_TEC, "tree reloaded");
			}
			dossierIdentifier = currentDossierId;
			loadTree();
		}
		return rootNode;
	}

	/**
	 * Get the current fond de dossier Document.
	 */
	@Factory(value = "fondDeDossierDocument", scope = EVENT)
	public DocumentModel getFondDeDossierDocument() {
		// on récupère le parapheur à partir du document courant (dossier)
		DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		if (dossierDoc == null) {
			return null;
		} else {
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			return dossier.getFondDeDossier(documentManager).getDocument();
		}

	}

	@Override
	protected DocumentModel getArborescenceDocument() throws ClientException {
		return getFondDeDossierDocument();
	}

	@Observer(SSFondDeDossierConstants.FOND_DE_DOSSIER_TREE_CHANGED_EVENT)
	public void reloadFondDeDossierTree() throws ClientException {
		if (LOGGER.isDebugEnable()) {
			LOGGER.debug(documentManager, STLogEnumImpl.LOG_DEBUG_TEC, "reload fond de dossier tree");
		}
		loadTree();
	}

	public FondDeDossierService getFondDeDossierService() {
		if (fondDeDossierService == null) {
			fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
		}
		return fondDeDossierService;
	}

	public Boolean isEpreuveAndUnauthorized(SSTreeFolder folder) {
		if (folder == null) {
			return false;
		}
		// si l'étape n'est pas "pour fourniture d'épreuves" on ne se pose pas de question
		if (!folder.getTitle().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES)) {
			return true;
		}
		// test si l'utilisateur est autorisé à créer l'étape pour fourniture d'épreuve
		final List<String> groups = currentUser.getGroups();
		return groups.contains(SolonEpgBaseFunctionConstant.ETAPE_FOURNITURE_EPREUVE_CREATOR);
	}

	/**
	 * Pour l'étape 'Pour avis CE' fonction qui regarde si nécessité de mettre une étoile devant le nom du fichier pour
	 * signifier à transmettre pour saisine rectificative ou pièce complémentaire
	 * 
	 * @param item
	 * @return
	 */
	public String getPrefixFichierIfATransmettreSaisineOuPieceComplementaire(FondDeDossierFile item) {
		// On met par défaut le préfix à vide
		String prefix = "";
		// On regarde si le dossier est dans l'étape pour avis CE
		if (corbeilleActions.isEtapePourAvisCE()) {
			// On récupère le dossier lié au document
			DocumentModel dossierDoc = navigationContext.getCurrentDocument();
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			// recherche du nom du dossier parent
			String nomDossierParent = getNomParentFolderForFichier(item);
			if (nomDossierParent.equals("Saisine rectificative")) {
				// On regarde si le document doit être transmis pour saisine rectificative si le document est dans le
				// dossier
				Calendar dateSaisineRectificative = dossierDistributionActions
						.getDateDerniereSaisineRectificative(documentManager, dossier);
				if (item.getModifiedDate() == null && dateSaisineRectificative != null
						&& item.getDateDernierTraitement().compareTo(dateSaisineRectificative) > 0) {
					prefix = "*";
				} else if (item.getModifiedDate() != null && dateSaisineRectificative != null
						&& item.getModifiedDate().compareTo(dateSaisineRectificative) > 0) {
					prefix = "*";
				}
			} else {
				// On regarde si le document doit être transmis si le document est dans les autres dossiers
				// Dans ce cas il s'agit d'un envoi de pièces complémentaires. Ce n'est pas de la saisine rectificative
				Calendar dateEnvoiPiecesComplementaires = dossierDistributionActions
						.getDateDerniereTransmissionPiecesComplementaires(documentManager, dossier);
				if (item.getModifiedDate() == null && dateEnvoiPiecesComplementaires != null
						&& item.getDateDernierTraitement().compareTo(dateEnvoiPiecesComplementaires) > 0) {
					prefix = "*";
				} else if (item.getModifiedDate() != null && dateEnvoiPiecesComplementaires != null
						&& item.getModifiedDate().compareTo(dateEnvoiPiecesComplementaires) > 0) {
					prefix = "*";
				}
			}
		}

		return prefix;
	}

	/**
	 * Retourne le nom du dossier parent pour un fichier donné
	 * 
	 * @param item
	 * @return
	 */
	public String getNomParentFolderForFichier(FondDeDossierFile item) {
		String nomDossierParent = "";
		DocumentModel itemDoc = item.getDocument();
		String itemPath = itemDoc.getPath().toString();
		String[] listPath = itemPath.split("/");
		if (listPath.length > 2) {
			nomDossierParent = listPath[listPath.length - 2];
		}
		return nomDossierParent;
	}

	/**
	 * Permet d'afficher le nombre de saisine rectificative (numéro de rose) pour un fichier donné
	 * 
	 * @param item
	 * @return
	 */
	public String getNumeroDeRosePourSaisineRectificative(FondDeDossierFile item) throws ClientException {
		if (corbeilleActions.isEtapePourAvisCE()) {
			// On récupère le dossier lié au document
			DocumentModel dossierDoc = navigationContext.getCurrentDocument();
			// Récupération des version du fichier
			Long compteurJetonFichier = 0L;
			List<DocumentModel> fileVersionList = null;
			try {
				fileVersionList = documentManager.getVersions(item.getDocument().getRef());
			} catch (ClientException ce) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_VERSION_FILE_FONC, "Erreur lors de la récupération des versions du document " + item.getDocument().getRef(), ce);
			}
			if (fileVersionList != null && !fileVersionList.isEmpty()) {
				List<String> idsVersionsDocs = new ArrayList<String>();
				for (DocumentModel fileVersionDoc : fileVersionList) {
					idsVersionsDocs.add(fileVersionDoc.getId());
				}
				StringBuilder chercherJetonQuery = new StringBuilder("SELECT j.ecm:uuid as id FROM ")
						.append(STConstant.JETON_DOC_TYPE)
						.append(" as j WHERE j.jtd:id_doc = ? AND j.jtd:type_webservice = ? AND j.jtd:type_modification = ? AND j.jtd:ids_complementaires IN (")
						.append(StringUtil.getQuestionMark(idsVersionsDocs.size()))
						.append(")");
				List<Object> params = new ArrayList<Object>();
				params.add(dossierDoc.getId());
				params.add(STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER);
				params.add(TypeModification.SAISINE_RECTIFICATIVE.name());
				params.addAll(idsVersionsDocs);
				
				try {
					compteurJetonFichier = QueryUtils.doUnrestrictedCountQuery(documentManager, QueryUtils.ufnxqlToFnxqlQuery(chercherJetonQuery.toString()), params.toArray());
				} catch (final ClientException ce) {
					LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_JETON_TEC, "Echec du dénombrement de jeton pour la saisine du dossier : " + dossierDoc.getId(), ce);
				}
			}
			if (compteurJetonFichier.equals(0L)) {
				return "";
			}
			return compteurJetonFichier.toString();
		}
		return "";
	}

	public String getSelectedNodeId() {
		return selectedNodeId;
	}

	public void setSelectedNodeId(String selectedNodeId) {
		if (!selectedNodeId.equals(this.selectedNodeId)) {
			corbeilleActions.resetIsEtapePourAvisCE();
		}
		this.selectedNodeId = selectedNodeId;
	}

}
