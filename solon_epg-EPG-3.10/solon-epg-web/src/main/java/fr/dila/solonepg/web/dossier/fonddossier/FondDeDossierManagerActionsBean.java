package fr.dila.solonepg.web.dossier.fonddossier;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.core.Events;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.common.utils.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.constant.SSFondDeDossierConstants;
import fr.dila.ss.api.constant.SSTreeConstants;
import fr.dila.ss.api.exception.SSException;
import fr.dila.ss.api.tree.SSTreeFile;
import fr.dila.ss.web.tree.SSTreeManagerActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;

@Synchronized(timeout = 10000)
@Name("fondDeDossierManagerActions")
@Scope(ScopeType.SESSION)
public class FondDeDossierManagerActionsBean extends SSTreeManagerActionsBean {

	/**
	 * Serial UID
	 */
	private static final long		serialVersionUID	= -2935440158131527511L;

	private static final STLogger	LOGGER				= STLogFactory.getLog(FondDeDossierManagerActionsBean.class);

	protected static final String	NEW_FOLDER_TITLE	= "Nouveau répertoire";

	@In(required = true, create = true)
	protected NuxeoPrincipal		currentUser;

	public boolean					erreurSuppression	= false;

	/**
	 * Reset the file properties
	 * 
	 */
	@Override
	public void resetFileUploadProperties() {
		uploadsAvailable = Integer.MAX_VALUE;
	}

	@Override
	protected void createSpecificFile(SSTreeFile fichier, String filename) throws ClientException {
		final FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();

		// On vérifie si le fichier existe déjà pour ne pas en ajouter un du même nom : contrainte STILA
		if (!fddService.checkNameUnicity(documentManager, filename, getSelectedNodeId(),
				navigationContext.getCurrentDocument())) {
			final String message = resourcesAccessor.getMessages().get("feedback.solonepg.document.filename.double");
			throw new EPGException(message);
		}

		fddService.createFile(documentManager, ssPrincipal, filename, fichier.getContent(), getSelectedNodeId(),
				navigationContext.getCurrentDocument());
		setIsErrorOccurred(false);
		setErrorName(null);
	}

	@Override
	protected void updateSpecificFile(SSTreeFile fichier, String filename) throws ClientException {
		final FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();
		fddService.updateFile(documentManager, getSelectedDocument(getSelectedNodeId()), filename,
				fichier.getContent(), ssPrincipal, navigationContext.getCurrentDocument());
		setIsErrorOccurred(false);
		setErrorName(null);
	}

	@Override
	protected DocumentModel createBareFile() throws SSException {
		final FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();
		return fddService.createBareFondDeDossierFichier(documentManager);
	}

	@Override
	public void deleteFile() throws ClientException {
		erreurSuppression = false;
		LOGGER.info(documentManager, STLogEnumImpl.DEL_FILE_FONC);
		resetErrorProperties();
		final FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();

		if (selectedNodeId != null) {
			List<DocumentModel> fichiersEpreuvages = fddService.getEpreuvesFiles(documentManager, navigationContext
					.getCurrentDocument().getAdapter(Dossier.class));

			boolean isFileInEpreuve = false;
			if (fichiersEpreuvages != null) {
				for (DocumentModel myFile : fichiersEpreuvages) {
					if (selectedNodeId.equals(myFile.getId())) {
						isFileInEpreuve = true;
					}
				}
			}
			if (isFileInEpreuve) {
				// test si l'utilisateur est autorisé à créer l'étape pour fourniture d'épreuve
				final List<String> groups = currentUser.getGroups();
				if (!groups.contains(SolonEpgBaseFunctionConstant.ETAPE_FOURNITURE_EPREUVE_CREATOR)) {
					// TODO : faire fonctionner ce message
					facesMessages.add(StatusMessage.Severity.WARN,
							"Vous n'avez pas le droit de supprimer un fichier d'épreuvage");
					erreurSuppression = true;
					return;

				}
			}
		}

		if (selectedNodeId == null || choixSuppression == null) {
			setIsErrorOccurred(true);
		} else if (SSTreeConstants.DELETE_ALL_CHOICE.equals(choixSuppression)) {
			// on supprime le document et toutes les versions qui lui sont associées
			fddService.deleteFileWithType(documentManager, getSelectedDocument(getSelectedNodeId()),
					navigationContext.getCurrentDocument(),
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE);
			resetProperties();
			treeChangeEvent();
		} else if (SSTreeConstants.DELETE_CURRENT_VERSION_CHOICE.equals(choixSuppression)) {
			// Restauration de l'avant dernière version du noeud dans l'arborescence du fond de dossier.
			fddService
					.restoreToPreviousVersion(documentManager, selectedNodeId, navigationContext.getCurrentDocument());
			resetProperties();
			treeChangeEvent();
		} else {
			setIsErrorOccurred(true);
		}
	}

	public void createFolder() {
		if (selectedNodeId != null && selectedNodeType != null) {
			super.createFolder(getSelectedDocument(selectedNodeId), selectedNodeType, NEW_FOLDER_TITLE);
		}
	}

	public void createFolderBefore() {
		if (selectedNodeId != null && selectedNodeType != null) {
			super.createFolderBefore(getSelectedDocument(selectedNodeId), selectedNodeType, NEW_FOLDER_TITLE);
		}
	}

	public void createFolderAfter() {
		if (selectedNodeId != null && selectedNodeType != null) {
			super.createFolderAfter(getSelectedDocument(selectedNodeId), selectedNodeType, NEW_FOLDER_TITLE);
		}
	}

	/**
	 * Renommage d'un répertoire.
	 * 
	 */
	public void renameFolder() {
		if (selectedNodeId != null && selectedNodeTitle != null) {
			super.renameFolder(getSelectedDocument(getSelectedNodeId()), getSelectedNodeTitle());
		}
	}

	/**
	 * récupère les formats autorisés pour ajouter un fichier.
	 * 
	 * @return acceptedTypes
	 */
	public String getAcceptedTypes() {
		DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		if (dossierDoc != null) {
			// récupération de la liste des formats de fichiers autorisés
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			acceptedTypes = StringUtils.join(dossier.getFondDeDossier(documentManager).getFormatsAutorises(), ",");
		}
		return acceptedTypes;
	}

	public void setAcceptedTypes(final String acceptedTypes) {
		this.acceptedTypes = acceptedTypes;
	}

	/**
	 * Retourne vrai si l'utilisateur a le droit de modifier le fond de dossier.
	 * 
	 * @throws ClientException
	 */
	public boolean canUserUpdateFondDossier() throws ClientException {
		// L'utilisateur ne peut pas modifier les dossiers à l'état terminé
		DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		if (dossier.isDone()) {
			return false;
		}

		// L'utilisateur peut uniquement modifier les dossiers qu'il a verrouillé
		if (!lockActions.getCanUnlockCurrentDoc()) {
			return false;
		}

		// L'administrateur fonctionnel peut modifier les fonds de dossier
		if (ssPrincipal.getGroups().contains(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UPDATER)) {
			return true;
		}

		// L'administrateur ministériel peut modifier les fonds de dossier des dossiers de ses ministères
		if (!ssPrincipal.getGroups().contains(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_MIN_UPDATER)) {
			return true;
		}

		// L'utilisateur lambda peut modifier les fond de dossier des dossiers qui lui sont distribués
		return true;
	}

	@Override
	protected void treeChangeEvent() {
		super.treeChangeEvent();
		Events.instance().raiseEvent(SSFondDeDossierConstants.FOND_DE_DOSSIER_TREE_CHANGED_EVENT);
	}

	public boolean isErreurSuppression() {
		return erreurSuppression;
	}

	public void setErreurSuppression(boolean erreurSuppression) {
		this.erreurSuppression = erreurSuppression;
	}
}
