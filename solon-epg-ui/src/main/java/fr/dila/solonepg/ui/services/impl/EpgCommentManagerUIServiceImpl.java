package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.domain.comment.Comment;
import fr.dila.solonepg.ui.services.EpgCommentManagerUIService;
import fr.dila.ss.ui.bean.fdr.NoteEtapeFormDTO;
import fr.dila.ss.ui.services.comment.SSCommentManagerUIServiceImpl;
import fr.dila.st.ui.th.model.SpecificContext;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;

public class EpgCommentManagerUIServiceImpl
    extends SSCommentManagerUIServiceImpl
    implements EpgCommentManagerUIService {

    @Override
    protected void addComment(
        SpecificContext context,
        DocumentModel docToComment,
        DocumentModel commentDoc,
        NoteEtapeFormDTO noteDto
    ) {
        DocumentModel newComment = buildNewComment(context, docToComment, commentDoc, noteDto);

        if (StringUtils.isNotEmpty(noteDto.getIdNode())) {
            Comment comment = newComment.getAdapter(Comment.class);
            comment.setVisibility(noteDto.getVisibility());
            context.getSession().saveDocument(newComment);
        }

        validateComment(context.getSession(), newComment);
    }

    @Override
    protected void setCommentData(
        SpecificContext context,
        DocumentModel commentDoc,
        NoteEtapeFormDTO noteEtapeFormDTO
    ) {
        super.setCommentData(context, commentDoc, noteEtapeFormDTO);
        Comment comment = commentDoc.getAdapter(Comment.class);
        comment.setVisibility(noteEtapeFormDTO.getVisibility());
        context.getSession().saveDocument(commentDoc);
    }
}
