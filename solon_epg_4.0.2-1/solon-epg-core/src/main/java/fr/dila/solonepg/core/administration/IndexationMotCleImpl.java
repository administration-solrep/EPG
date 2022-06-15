package fr.dila.solonepg.core.administration;

import fr.dila.solonepg.api.administration.IndexationMotCle;
import fr.dila.solonepg.api.constant.SolonEpgIndextionConstants;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.core.domain.STDomainObjectImpl;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * IndexationMotCle Implementation.
 *
 * @author asatre
 */
public class IndexationMotCleImpl extends STDomainObjectImpl implements IndexationMotCle {
    private static final long serialVersionUID = -6647057632643222767L;

    public IndexationMotCleImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getIntitule() {
        return getStringProperty(
            SolonEpgIndextionConstants.INDEXATION_MOT_CLE_SCHEMA,
            SolonEpgIndextionConstants.INDEXATION_MOT_CLE_INTITULE
        );
    }

    @Override
    public void setIntitule(String intitule) {
        setProperty(
            SolonEpgIndextionConstants.INDEXATION_MOT_CLE_SCHEMA,
            SolonEpgIndextionConstants.INDEXATION_MOT_CLE_INTITULE,
            intitule
        );
    }

    @Override
    public List<String> getMinistereIds() {
        return getListStringProperty(
            SolonEpgIndextionConstants.INDEXATION_MOT_CLE_SCHEMA,
            SolonEpgIndextionConstants.INDEXATION_MOT_CLE_MINISTERE_IDS
        );
    }

    @Override
    public void setMinistereIds(List<String> ministereIds) {
        setProperty(
            SolonEpgIndextionConstants.INDEXATION_MOT_CLE_SCHEMA,
            SolonEpgIndextionConstants.INDEXATION_MOT_CLE_MINISTERE_IDS,
            ministereIds
        );
    }

    @Override
    public List<String> getMotsCles() {
        return getListStringProperty(
            SolonEpgIndextionConstants.INDEXATION_MOT_CLE_SCHEMA,
            SolonEpgIndextionConstants.INDEXATION_MOT_CLE_MOTS_CLES
        );
    }

    @Override
    public void setMotsCles(List<String> motsCles) {
        setProperty(
            SolonEpgIndextionConstants.INDEXATION_MOT_CLE_SCHEMA,
            SolonEpgIndextionConstants.INDEXATION_MOT_CLE_MOTS_CLES,
            motsCles
        );
    }

    @Override
    public String getAuthor() {
        return DublincoreSchemaUtils.getCreator(document);
    }

    @Override
    public Boolean isAuthor(CoreSession session) {
        SSPrincipal principal = (SSPrincipal) session.getPrincipal();
        String author = getAuthor();
        return (author != null && author.equals(principal.getName()));
    }
}
