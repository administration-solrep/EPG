package fr.dila.solonepg.core.domain.comment;

import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.domain.comment.Comment;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.domain.comment.STCommentImpl;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implémentation de l'objet métier commentaire de SOLON EPG.
 *
 * @author jtremeaux
 * @author asatre
 */
public class CommentImpl extends STCommentImpl implements Comment {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    private static final String PREFIX_MIN = OrganigrammeType.MINISTERE.getValue() + "-";
    private static final String PREFIX_DIR = OrganigrammeType.DIRECTION.getValue() + "-";
    private static final String PREFIX_PST = OrganigrammeType.POSTE.getValue() + "-";

    /**
     * Constructeur de CommentImpl.
     *
     * @param doc Document
     */
    public CommentImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getVisibleToMinistereId() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.COMMENT_SCHEMA,
            SolonEpgSchemaConstant.COMMENT_VISIBLE_TO_MINISTERE_ID_PROPERTY
        );
    }

    @Override
    public void setVisibleToMinistereId(String visibleToMinistereId) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.COMMENT_SCHEMA,
            SolonEpgSchemaConstant.COMMENT_VISIBLE_TO_MINISTERE_ID_PROPERTY,
            visibleToMinistereId
        );
    }

    @Override
    public String getVisibleToUniteStructurelleId() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.COMMENT_SCHEMA,
            SolonEpgSchemaConstant.COMMENT_VISIBLE_TO_UNITE_STRUCTURELLE_ID_PROPERTY
        );
    }

    @Override
    public void setVisibleToUniteStructurelleId(String visibleToUniteStructurelleId) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.COMMENT_SCHEMA,
            SolonEpgSchemaConstant.COMMENT_VISIBLE_TO_UNITE_STRUCTURELLE_ID_PROPERTY,
            visibleToUniteStructurelleId
        );
    }

    @Override
    public String getVisibleToPosteId() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.COMMENT_SCHEMA,
            SolonEpgSchemaConstant.COMMENT_VISIBLE_TO_POSTE_ID_PROPERTY
        );
    }

    @Override
    public void setVisibleToPosteId(String visibleToPosteId) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.COMMENT_SCHEMA,
            SolonEpgSchemaConstant.COMMENT_VISIBLE_TO_POSTE_ID_PROPERTY,
            visibleToPosteId
        );
    }

    @Override
    public String getVisiblity() {
        String visibility = null;
        if (getVisibleToMinistereId() != null) {
            visibility = getVisibleToMinistereId();
        } else if (getVisibleToUniteStructurelleId() != null) {
            visibility = getVisibleToUniteStructurelleId();
        } else if (getVisibleToPosteId() != null) {
            visibility = getVisibleToPosteId();
        }
        return visibility;
    }

    @Override
    public String getTypeVisiblity() {
        String visibility = "NON";
        if (StringUtils.isNotEmpty(getVisibleToMinistereId())) {
            visibility = OrganigrammeType.MINISTERE.getValue();
        } else if (StringUtils.isNotEmpty(getVisibleToUniteStructurelleId())) {
            visibility = OrganigrammeType.DIRECTION.getValue();
        } else if (StringUtils.isNotEmpty(getVisibleToPosteId())) {
            visibility = OrganigrammeType.POSTE.getValue();
        }
        return visibility;
    }

    @Override
    public void setVisibility(String visibility) {
        setVisibleToMinistereId(null);
        setVisibleToUniteStructurelleId(null);
        setVisibleToPosteId(null);
        if (StringUtils.isNotEmpty(visibility)) {
            if (visibility.contains(PREFIX_MIN)) {
                setVisibleToMinistereId(RegExUtils.removeAll(visibility, PREFIX_MIN));
            } else if (visibility.contains(PREFIX_DIR)) {
                setVisibleToUniteStructurelleId(RegExUtils.removeAll(visibility, PREFIX_DIR));
            } else if (visibility.contains(PREFIX_PST)) {
                setVisibleToPosteId(RegExUtils.removeAll(visibility, PREFIX_PST));
            }
        }
    }
}
