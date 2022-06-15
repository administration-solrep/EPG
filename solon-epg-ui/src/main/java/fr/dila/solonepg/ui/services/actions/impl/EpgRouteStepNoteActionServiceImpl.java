package fr.dila.solonepg.ui.services.actions.impl;

import fr.dila.solonepg.api.domain.comment.Comment;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.services.actions.EpgRouteStepNoteActionService;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.ui.bean.fdr.NoteEtapeFormDTO;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.services.SSUIServiceLocator;
import fr.dila.ss.ui.services.actions.impl.RouteStepNoteActionServiceImpl;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.comment.api.CommentableDocument;

public class EpgRouteStepNoteActionServiceImpl
    extends RouteStepNoteActionServiceImpl
    implements EpgRouteStepNoteActionService {

    @Override
    public List<DocumentModel> getCommentList(SpecificContext context) {
        DocumentModel routeStepDoc = context.getFromContextData(SSContextDataKey.STEP_DOC);
        DocumentModel doc = context.getFromContextData(SSContextDataKey.COMMENT_DOC);
        CommentableDocument commentableDoc = routeStepDoc.getAdapter(CommentableDocument.class);
        List<DocumentModel> visibleComments = new ArrayList<>();
        if (commentableDoc != null) {
            List<DocumentModel> comments = new ArrayList<>();
            if (SSConstant.ROUTE_STEP_DOCUMENT_TYPE.equals(doc.getType())) {
                comments = commentableDoc.getComments();
            } else if (STConstant.COMMENT_DOCUMENT_TYPE.equals(doc.getType())) {
                comments = commentableDoc.getComments(doc);
            }
            comments.stream().filter(com -> isVisible(context, com)).forEach(visibleComments::add);
        }
        return visibleComments;
    }

    @Override
    public NoteEtapeFormDTO getNoteEtape(SpecificContext context) {
        DocumentModel commentDoc = context.getCurrentDocument();
        Comment comment = commentDoc.getAdapter(Comment.class);
        EpgOrganigrammeService organigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
        NoteEtapeFormDTO commentDto = new NoteEtapeFormDTO();
        commentDto.setCommentContent(comment.getTexte());
        commentDto.setIdNode(comment.getVisiblity());
        if (StringUtils.isNotEmpty(comment.getVisiblity())) {
            commentDto.setMapIdNode(
                Collections.singletonMap(
                    comment.getVisiblity(),
                    organigrammeService.getOrganigrammeNodeById(comment.getVisiblity()).getLabel()
                )
            );
        } else {
            commentDto.setMapIdNode(null);
        }
        commentDto.setTypeRestriction(comment.getTypeVisiblity());
        return commentDto;
    }

    private boolean isVisible(SpecificContext context, DocumentModel commentModel) {
        SSPrincipal ssPrincipal = (SSPrincipal) context.getSession().getPrincipal();
        final Comment comment = commentModel.getAdapter(Comment.class);
        final String author = comment.getAuthor();

        context.putInContextData(SSContextDataKey.COMMENT_DOC, commentModel);
        if (
            author != null &&
            author.equals(ssPrincipal.getName()) ||
            SSUIServiceLocator.getSSCommentManagerUIService().isInAuthorPoste(context)
        ) {
            return true;
        }

        final String visibleToMinistereId = comment.getVisibleToMinistereId();
        final String visibleToUniteStructurelleId = comment.getVisibleToUniteStructurelleId();
        final String visibleToPosteId = comment.getVisibleToPosteId();
        boolean visible = true;

        if (visibleToMinistereId != null) {
            visible = ssPrincipal.getMinistereIdSet().contains(visibleToMinistereId);
        } else if (visibleToUniteStructurelleId != null) {
            visible = ssPrincipal.getDirectionIdSet().contains(visibleToUniteStructurelleId);
        } else if (visibleToPosteId != null) {
            visible = ssPrincipal.getPosteIdSet().contains(visibleToPosteId);
        }

        return visible;
    }
}
