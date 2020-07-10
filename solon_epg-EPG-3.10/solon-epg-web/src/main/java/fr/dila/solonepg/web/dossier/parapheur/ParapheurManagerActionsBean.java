package fr.dila.solonepg.web.dossier.parapheur;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.schema.FacetNames;
import org.richfaces.event.UploadEvent;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.service.FeuilleStyleService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.constant.SSTreeConstants;
import fr.dila.ss.api.exception.SSException;
import fr.dila.ss.api.tree.SSTreeFile;
import fr.dila.ss.web.tree.SSTreeManagerActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.StringUtil;

@Name("parapheurManagerActions")
@Scope(ScopeType.SESSION)
public class ParapheurManagerActionsBean extends SSTreeManagerActionsBean {

	private static final long		serialVersionUID	= -3943702680859682495L;
	private static final STLogger	LOGGER				= STLogFactory.getLog(ParapheurManagerActionsBean.class);

	@In(required = true, create = true)
	protected NuxeoPrincipal		currentUser;

	public boolean					erreurSuppression	= false;

	@Override
	protected void createSpecificFile(SSTreeFile fichier, String filename) throws ClientException {
		final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
		final FeuilleStyleService feuilleStyleService = SolonEpgServiceLocator.getFeuilleStyleService();
		// récupération des métadonnées du dossier
		InputStream fichierStream = null;
		String feuilleStyleError = null;
		try {
			fichierStream = fichier.getContent().getStream();
		} catch (IOException exc) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_STREAM_FONC, exc);
		}

		// On vérifie si le fichier existe déjà pour ne pas en ajouter un du même nom : contrainte STILA
		if (!parapheurService.checkNameUnicity(documentManager, filename, selectedNodeName,
				navigationContext.getCurrentDocument())) {
			final String message = resourcesAccessor.getMessages().get("feedback.solonepg.document.filename.double");
			throw new EPGException(message);
		}

		if (fichierStream != null) {
			feuilleStyleError = feuilleStyleService.updateDossierMetadataFromParapheurFile(documentManager,
					fichierStream, filename, getSelectedDocument(getSelectedNodeId()),
					navigationContext.getCurrentDocument());
		}
		parapheurService.createParapheurFile(documentManager, filename, fichier.getContent(), selectedNodeName,
				navigationContext.getCurrentDocument());
		setIsErrorOccurred(false);
		setErrorName(null);
		treeChangeEvent();

		if (feuilleStyleError != null) {
			if (feuilleStyleError.equals(SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_DATE_BAD_FORMAT)) {
				// Affiche un message pour signaler à l'utilisateur que la date de signature n'a pas pu être reportée
				// dans le bordereau
				final String message = resourcesAccessor.getMessages().get(
						SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_DATE_BAD_FORMAT);
				facesMessages.add(StatusMessage.Severity.INFO, message);
			} else if (feuilleStyleError.equals(SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_EXCEPTION_THROW)) {
				// Affiche un message pour signaler à l'utilisateur que le titre et la date de signature n'ont pas pu
				// être reportés dans le bordereau
				final String message = resourcesAccessor.getMessages().get(
						SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_EXCEPTION_THROW);
				facesMessages.add(StatusMessage.Severity.INFO, message);
			} else if (feuilleStyleError.equals(SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_TITRE_ACTE_NOT_FOUND)) {
				// Affiche un message pour signaler à l'utilisateur que le titre et la date de signature n'ont pas pu
				// être reportés dans le bordereau
				final String message = resourcesAccessor.getMessages().get(
						SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_TITRE_ACTE_NOT_FOUND);
				facesMessages.add(StatusMessage.Severity.INFO, message);
			}
		}
	}

	@Override
	protected void updateSpecificFile(SSTreeFile fichier, String filename) throws ClientException {
		final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
		parapheurService.updateFile(documentManager, getSelectedDocument(selectedNodeId), filename,
				fichier.getContent(), ssPrincipal, navigationContext.getCurrentDocument());
		setIsErrorOccurred(false);
		setErrorName(null);
		treeChangeEvent();
	}

	@Override
	protected DocumentModel createBareFile() throws SSException {
		final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
		return parapheurService.createBareParapheurFile(documentManager);
	}

	@Override
	protected void treeChangeEvent() {
		super.treeChangeEvent();
		Events.instance().raiseEvent(SolonEpgParapheurConstants.PARAPHEUR_CHANGED_EVENT);
	}

	@Override
	public void deleteFile() throws ClientException {
		erreurSuppression = false;
		LOGGER.info(documentManager, STLogEnumImpl.DEL_FILE_FONC);
		resetErrorProperties();
		final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();

		if (selectedNodeId != null) {
			List<DocumentModel> fichiersEpreuvages = parapheurService.getEpreuvesFiles(documentManager,
					navigationContext.getCurrentDocument().getAdapter(Dossier.class));

			boolean isFileInEpreuve = false;
			for (DocumentModel myFile : fichiersEpreuvages) {
				if (selectedNodeId.equals(myFile.getId())) {
					isFileInEpreuve = true;
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

		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		if (selectedNodeId == null || choixSuppression == null) {
			setIsErrorOccurred(true);
		} else if (choixSuppression.equals(SSTreeConstants.DELETE_ALL_CHOICE)) {
			// on supprime le document et toutes les versions qui lui sont associées
			parapheurService.deleteFile(documentManager, getSelectedDocument(getSelectedNodeId()), dossierDoc);
			resetProperties();
			treeChangeEvent();
		} else if (choixSuppression.equals(SSTreeConstants.DELETE_CURRENT_VERSION_CHOICE)) {
			// Restauration de l'avant dernière version du noeud dans l'arborescence du fond de dossier.
			parapheurService.restoreToPreviousVersion(documentManager, selectedNodeId, dossierDoc);
			resetProperties();
			treeChangeEvent();
		} else {
			setIsErrorOccurred(true);
		}
		parapheurService.checkParapheurComplet(dossierDoc, documentManager);
	}

	/**
	 * récupère les formats autorisés pour ajouter un fichier.
	 * 
	 * @return acceptedTypes
	 */
	public String getAcceptedTypes() throws ClientException {
		DocumentModel selectedFolder = getSelectedDocument(selectedNodeId);
		if (selectedFolder != null) {
			// récupération de la liste des formats de fichiers autorisés
			if (selectedFolder.hasFacet(FacetNames.VERSIONABLE)
					|| SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE.equals(selectedFolder.getType())) {
				// récupération du répertoire parent du fichier
				selectedFolder = documentManager.getParentDocument(selectedFolder.getRef());
			}
			// récupération des formats autorisés pour le répertoire
			final ParapheurFolder parapheurFolder = selectedFolder.getAdapter(ParapheurFolder.class);
			final List<String> formatsAutorisesList = parapheurFolder.getFormatsAutorises();
			acceptedTypes = StringUtil.join(formatsAutorisesList, ",");
		}
		return acceptedTypes;
	}

	public void setAcceptedTypes(final String acceptedTypes) {
		this.acceptedTypes = acceptedTypes;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean checkUploadAvailable(UploadEvent event) {
		if (!super.checkUploadAvailable(event)) {
			return false;
		}
		DocumentModel parapheurFolderDoc = getSelectedDocument(getSelectedNodeId());
		if (parapheurFolderDoc.hasSchema(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_SCHEMA)) {
			ParapheurFolder folder = parapheurFolderDoc.getAdapter(ParapheurFolder.class);
			int nbCurrentDoc = 0;
			try {
				List<SSTreeFile> childrenFiles = (List<SSTreeFile>) SolonEpgServiceLocator.getParapheurService()
						.getChildrenFile(documentManager, parapheurFolderDoc);
				nbCurrentDoc = childrenFiles.size();
			} catch (ClientException exc) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_FILE_FONC, exc);
				return false;
			}
			// Si la limite de nombre maximal de fichier a été atteinte on desactive l'upload
			Long nbDocMax = folder.getNbDocAccepteMax();
			// Certains répertoires n'ont pas de nombre max
			if (nbDocMax != null) {
				if ((getUploadedFiles() != null && getUploadedFiles().size() >= (nbDocMax - nbCurrentDoc))
						|| nbCurrentDoc >= nbDocMax || (event.getUploadItems().size() > (nbDocMax - nbCurrentDoc))) {
					return false;
				}
			}
		}

		return true;
	}

	public boolean isErreurSuppression() {
		return erreurSuppression;
	}

	public void setErreurSuppression(boolean erreurSuppression) {
		this.erreurSuppression = erreurSuppression;
	}

}
