package fr.dila.solonepg.core.domain.comment;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.domain.comment.Comment;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.core.domain.comment.STCommentImpl;
import fr.dila.st.core.util.PropertyUtil;

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
        return PropertyUtil.getStringProperty(document,
                SolonEpgSchemaConstant.COMMENT_SCHEMA, SolonEpgSchemaConstant.COMMENT_VISIBLE_TO_MINISTERE_ID_PROPERTY);
    }

    @Override
    public void setVisibleToMinistereId(String visibleToMinistereId) {
        PropertyUtil.setProperty(document,
                SolonEpgSchemaConstant.COMMENT_SCHEMA, SolonEpgSchemaConstant.COMMENT_VISIBLE_TO_MINISTERE_ID_PROPERTY,
                visibleToMinistereId);
    }

    @Override
    public String getVisibleToUniteStructurelleId() {
        return PropertyUtil.getStringProperty(document,
                SolonEpgSchemaConstant.COMMENT_SCHEMA, SolonEpgSchemaConstant.COMMENT_VISIBLE_TO_UNITE_STRUCTURELLE_ID_PROPERTY);
    }

    @Override
    public void setVisibleToUniteStructurelleId(String visibleToUniteStructurelleId) {
        PropertyUtil.setProperty(document,
                SolonEpgSchemaConstant.COMMENT_SCHEMA, SolonEpgSchemaConstant.COMMENT_VISIBLE_TO_UNITE_STRUCTURELLE_ID_PROPERTY,
                visibleToUniteStructurelleId);
    }

    @Override
    public String getVisibleToPosteId() {
        return PropertyUtil.getStringProperty(document,
                SolonEpgSchemaConstant.COMMENT_SCHEMA, SolonEpgSchemaConstant.COMMENT_VISIBLE_TO_POSTE_ID_PROPERTY);
    }

    @Override
    public void setVisibleToPosteId(String visibleToPosteId) {
        PropertyUtil.setProperty(document,
                SolonEpgSchemaConstant.COMMENT_SCHEMA, SolonEpgSchemaConstant.COMMENT_VISIBLE_TO_POSTE_ID_PROPERTY,
                visibleToPosteId);
    }

	@Override
	public String getVisiblity() {
		String visibility = null;
		if(getVisibleToMinistereId()!=null){
			visibility = getVisibleToMinistereId();
		}else if(getVisibleToUniteStructurelleId()!=null){
			visibility = getVisibleToUniteStructurelleId();
		}else if(getVisibleToPosteId()!=null){
			visibility = getVisibleToPosteId();
		}
		return visibility;
	}

	@Override
	public void setVisibility(String visibility) {
		if(visibility != null && !visibility.isEmpty()){
			if(visibility.contains(STConstant.PREFIX_MIN)){
				setVisibleToMinistereId(visibility.replace(STConstant.PREFIX_MIN, ""));
			}else if(visibility.contains(STConstant.PREFIX_US)){
				setVisibleToUniteStructurelleId(visibility.replace(STConstant.PREFIX_US, ""));
			}else if(visibility.contains(STConstant.PREFIX_POSTE)){
				setVisibleToPosteId(visibility.replace(STConstant.PREFIX_POSTE, ""));
			}
		}
	}
}
