package fr.dila.solonepg.web.feuilleroute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.comment.api.CommentableDocument;

import fr.dila.solonepg.api.domain.comment.Comment;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.domain.comment.STComment;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.core.service.CommentManagerImpl;
import fr.dila.st.core.service.STServiceLocator;

/**
 * WebBean permettant de gérer les notes d'étapes.
 * 
 * @author asatre
 */
@Name("routeStepNoteActions")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.FRAMEWORK + 1)
public class RouteStepNoteActionsBean extends fr.dila.ss.web.feuilleroute.RouteStepNoteActionsBean implements
		Serializable {

	private static final long	serialVersionUID	= -3840942017141321485L;

	@Override
	public List<DocumentModel> getCommentList(DocumentModel routeStepDoc) throws ClientException {
		List<DocumentModel> listComment = new ArrayList<DocumentModel>();
		CommentableDocument commentableDoc = routeStepDoc.getAdapter(CommentableDocument.class);
		if (commentableDoc != null) {
			List<DocumentModel> docComments = commentableDoc.getComments();
			if (docComments != null) {
				for (DocumentModel documentModel : docComments) {
					if (isVisible(documentModel)) {
						listComment.add(documentModel);
					}
				}
			}
		}
		return listComment;
	}

	@Override
	public boolean hasComment(DocumentModel routeStepDoc) throws ClientException {
		CommentableDocument commentableDoc = routeStepDoc.getAdapter(CommentableDocument.class);
		if (commentableDoc != null) {
			List<DocumentModel> docComments = commentableDoc.getComments();
			if (docComments != null && !docComments.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public String getVisiblity(Comment comment) throws ClientException {
		String result = null;
		if (comment != null) {
			final String visibleToMinistereId = comment.getVisibleToMinistereId();
			final String visibleToUniteStructurelleId = comment.getVisibleToUniteStructurelleId();
			final String visibleToPosteId = comment.getVisibleToPosteId();

			if (visibleToMinistereId != null) {
				OrganigrammeNode ministere = STServiceLocator.getSTMinisteresService().getEntiteNode(
						visibleToMinistereId);
				if (ministere != null) {
					result = ministere.getLabel();
				} else {
					result = "Ministère inconnu : " + visibleToMinistereId;
				}
			} else if (visibleToUniteStructurelleId != null) {
				OrganigrammeNode uniteStructurelle = STServiceLocator.getSTUsAndDirectionService()
						.getUniteStructurelleNode(visibleToUniteStructurelleId);
				if (uniteStructurelle != null) {
					result = uniteStructurelle.getLabel();
				} else {
					result = "Unité inconnue : " + visibleToUniteStructurelleId;
				}
			} else if (visibleToPosteId != null) {
				OrganigrammeNode poste = STServiceLocator.getSTPostesService().getPoste(visibleToPosteId);
				if (poste != null) {
					result = poste.getLabel();
				} else {
					result = "Poste inconnu : " + visibleToPosteId;
				}
			}
		}

		return result;
	}

	private Boolean isVisible(DocumentModel commentModel) throws ClientException {
		final Comment comment = commentModel.getAdapter(Comment.class);
		STComment stComment = commentModel.getAdapter(STComment.class);
		final String author = comment.getAuthor();

		if (author != null && author.equals(ssPrincipal.getName())) {
			return Boolean.TRUE;
		}
		
		if (((CommentManagerImpl) SSServiceLocator.getCommentManager()).isInAuthorPoste(stComment,
				ssPrincipal.getName())) {
			return true;
		}

		final String visibleToMinistereId = comment.getVisibleToMinistereId();
		final String visibleToUniteStructurelleId = comment.getVisibleToUniteStructurelleId();
		final String visibleToPosteId = comment.getVisibleToPosteId();

		if (visibleToMinistereId != null) {
			return ssPrincipal.getMinistereIdSet().contains(visibleToMinistereId);

		} else if (visibleToUniteStructurelleId != null) {
			return ssPrincipal.getDirectionIdSet().contains(visibleToUniteStructurelleId);

		} else if (visibleToPosteId != null) {
			return ssPrincipal.getPosteIdSet().contains(visibleToPosteId);

		}

		return Boolean.TRUE;
	}
}
