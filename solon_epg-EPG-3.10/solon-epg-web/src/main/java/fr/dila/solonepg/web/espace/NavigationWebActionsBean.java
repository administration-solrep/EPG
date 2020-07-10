package fr.dila.solonepg.web.espace;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.model.NoSuchDocumentException;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.cm.web.mailbox.CaseManagementMailboxActionsBean;
import fr.dila.solonepg.api.constant.SolonEpgActionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.lock.STLockActionsBean;

@Name("navigationWebActions")
@Scope(CONVERSATION)
@Install(precedence = Install.APPLICATION + 1)
public class NavigationWebActionsBean extends fr.dila.st.web.action.NavigationWebActionsBean implements Serializable {

	/**
	 * serial version uid
	 */
	private static final long								serialVersionUID	= -3502006568645857219L;

	private static final Log								log					= LogFactory
																						.getLog(NavigationWebActionsBean.class);

	@In(create = true, required = false)
	protected transient CaseManagementMailboxActionsBean	cmMailboxActions;

	@In(create = true, required = false)
	protected transient ActionManager						actionManager;

	@In(create = true)
	protected STLockActionsBean								stLockActions;

	@In(create = true)
	protected ResourcesAccessor								resourcesAccessor;

	@In(create = true, required = false)
	protected FacesMessages									facesMessages;

	/**
	 * Surcharge de l'action du socle transverse afin de : 1)lancer un log dans le journal technique si le document est
	 * un modèle de feuille de route. note : on teste si le document est une feuille de route ici et pas dans le
	 * NotificationAuditEventLogger car on ne pourrait pas faire la différence entre un modèle de feuille de route et
	 * une feuille de route. 2) vérifier que l'on possède le verrou sur le document
	 * 
	 * @param doc
	 *            Document à supprimer
	 * @return Vue
	 * @throws ClientException
	 */
	@Override
	public String deleteDocument(DocumentModel doc) throws ClientException {
		final JournalService journalService = STServiceLocator.getJournalService();
		try {
			// Vérifie si le document n'est pas verrouillé
			if (stLockActions.isDocumentLocked(doc)) {
				String errorMessage = resourcesAccessor.getMessages().get(STLockActionsBean.LOCK_LOST_ERROR_MSG);
				facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
				return null;
			}

			String view = super.deleteDocument(doc);
			if (doc.getType().equals(STConstant.FEUILLE_ROUTE_DOCUMENT_TYPE)) {
				// on logge l'action de suppression du modèle de feuille de route
				String comment = "Suppression du modèle de feuille de route [" + doc.getTitle() + "]";
				journalService.journaliserActionAdministration(documentManager,
						SolonEpgEventConstant.DELETE_MODELE_FDR_EVENT, comment);
			}

			return view;

		} catch (ClientException e) {
			if (ExceptionUtils.getRootCause(e) instanceof NoSuchDocumentException) {
				// le document a été supprimé par un autre utilisateur ou l'utilisateur n'a plus les droits pour voir le
				// document : on le signale à l'utilisateur sans renvoyer une exception
				String errorMessage = resourcesAccessor.getMessages().get(STConstant.NO_SUCH_DOCUMENT_ERROR_MSG);
				facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
				log.warn(e);
			} else {
				log.error(e);
				facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());

			}

			return null;
		}

	}

	/**
	 * Renvoie vrai si l'utilisateur est dans l'espace de création ou de traitement.
	 * 
	 * @return vrai si l'utilisateur est dans l'espace de création ou de traitement.
	 */
	public boolean isInEspaceRechercheOrInEspaceAdministration() {
		String currentMainMenuActionLabel;
		if (currentMainMenuAction == null || currentMainMenuAction.toString() == null) {
			return false;
		}
		currentMainMenuActionLabel = currentMainMenuAction.toString();
		return currentMainMenuActionLabel.equals(SolonEpgActionConstant.ESPACE_RECHERCHE)
				|| currentMainMenuActionLabel.equals(SolonEpgActionConstant.ESPACE_ADMINISTRATION);
	}

	/**
	 * Renvoie vrai si l'utilisateur est dans l'espace parlemetaire.
	 * 
	 * @return vrai si l'utilisateur est dans l'espace parlemetaire.est dans l'espace parlemetaire.
	 */
	public boolean isInEspaceParlemetaire() {
		String currentMainMenuActionLabel;
		if (currentMainMenuAction == null || currentMainMenuAction.toString() == null) {
			return false;
		}
		currentMainMenuActionLabel = currentMainMenuAction.toString();
		return currentMainMenuActionLabel.equals(SolonEpgActionConstant.ESPACE_PARLEMENTAIRE);
	}

	public String navigateToDocument(String docId) {
		try {
			DocumentModel doc = documentManager.getDocument(new IdRef(docId));
			return navigationContext.navigateToDocument(doc);
		} catch (ClientException e) {
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			return null;
		}
	}

}
