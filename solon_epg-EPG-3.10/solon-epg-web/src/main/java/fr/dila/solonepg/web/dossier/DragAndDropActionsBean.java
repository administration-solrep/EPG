package fr.dila.solonepg.web.dossier;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.ScopeType;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.jboss.seam.core.Events;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.service.DndService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.constant.SSFondDeDossierConstants;

@Name("dragAndDropActions")
@Scope(ScopeType.EVENT)
@Install(precedence = Install.FRAMEWORK)
public class DragAndDropActionsBean implements Serializable {

	/**
	 * Ce WebBean permet de gérer le déplacement de documents en Drag and Drop
	 * 
	 * @author acleuet
	 */
	private static final long				serialVersionUID	= 1L;

	@In(create = true, required = false)
	private CoreSession						documentManager;

	@In(create = true, required = false)
	private FacesMessages					facesMessages;

	@In(create = true)
	private Map<String, String>				messages;

	@In(create = true, required = false)
	private DndService						dndService;

	@In(required = true, create = true)
	protected NuxeoPrincipal				currentUser;

	@In(create = true, required = false)
	protected transient NavigationContext	navigationContext;

	private static final Log				log					= LogFactory.getLog(DragAndDropActionsBean.class);

	public DragAndDropActionsBean() {
		// do nothing
	}

	@WebRemote
	public String moveWithId(final String dndDocId, final String dndContainerId) {
		final String debug = "move " + dndDocId + " into " + dndContainerId + ": ";
		dndService = SolonEpgServiceLocator.getDndService();
		String actionDone = dndService.processMove(documentManager, dndDocId, dndContainerId, currentUser,
				navigationContext);
		if (actionDone == null) {
			facesMessages.add(StatusMessage.Severity.WARN, messages.get("label.drag.and.drop"));
			return debug + "move_impossible";
		} else {
			facesMessages.add(StatusMessage.Severity.INFO, actionDone);
			Events.instance().raiseEvent(SSFondDeDossierConstants.FOND_DE_DOSSIER_TREE_CHANGED_EVENT);
			Events.instance().raiseEvent(SolonEpgParapheurConstants.PARAPHEUR_CHANGED_EVENT);
			return debug + actionDone;
		}

	}

	/**
	 * Renvoi vrai si on déplace un document du Parapheur vers le parapheur
	 * 
	 * @param dndDocId
	 * @param dndContainerId
	 * @return
	 */
	@WebRemote
	public Boolean isParapheurToParapheur(final String dndDocId, final String dndContainerId) {
		dndService = SolonEpgServiceLocator.getDndService();
		final IdRef docId = dndService.extractId(dndDocId);
		final IdRef newContainerId = dndService.extractId(dndContainerId);
		DocumentModel doc;
		DocumentModel newContainerDoc;
		try {
			newContainerDoc = documentManager.getDocument(newContainerId);
			doc = documentManager.getDocument(docId);
			if (doc.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)
					&& newContainerDoc.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE)) {
				return true;
			}
		} catch (ClientException e) {
			log.error(
					"Une erreur s'est produite lors de la vérification du répertoire de provenance et de destination",
					e);
			return false;
		}
		return false;
	}

	/**
	 * Renvoi vrai si on déplace un document du Fond de dossier vers le Fond de dossier
	 * 
	 * @param dndDocId
	 * @param dndContainerId
	 * @return
	 */
	@WebRemote
	public Boolean isFDDtoFDD(final String dndDocId, final String dndContainerId) {
		dndService = SolonEpgServiceLocator.getDndService();
		final IdRef docId = dndService.extractId(dndDocId);
		final IdRef newContainerId = dndService.extractId(dndContainerId);
		DocumentModel doc;
		DocumentModel newContainerDoc;
		try {
			newContainerDoc = documentManager.getDocument(newContainerId);
			doc = documentManager.getDocument(docId);
			if (doc.getType().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)
					&& newContainerDoc.getType().equals(
							SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)) {
				return true;
			}
		} catch (ClientException e) {
			log.error(
					"Une erreur s'est produite lors de la vérification du répertoire de provenance et de destination",
					e);
			return false;
		}
		return false;
	}

	/**
	 * Renvoi vrai si on transfère un document du FDD vers le parapheur ou du parapheur vers le FDD
	 * 
	 * @param dndDocId
	 * @param dndContainerId
	 * @return
	 */
	@WebRemote
	public Boolean isFDDToParapheurOrParapheurToFDD(final String dndDocId, final String dndContainerId) {
		dndService = SolonEpgServiceLocator.getDndService();
		final IdRef docId = dndService.extractId(dndDocId);
		final IdRef newContainerId = dndService.extractId(dndContainerId);
		DocumentModel doc;
		DocumentModel newContainerDoc;
		try {
			newContainerDoc = documentManager.getDocument(newContainerId);
			doc = documentManager.getDocument(docId);
			if ((doc.getType().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE) && newContainerDoc
					.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE))
					|| (doc.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE) && newContainerDoc
							.getType().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE))) {
				return true;
			}
		} catch (ClientException e) {
			log.error(
					"Une erreur s'est produite lors de la vérification du répertoire de provenance et de destination",
					e);
			return false;
		}
		return false;
	}
}
