package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.service.ElasticParametrageService;
import fr.dila.st.api.parametre.STParametre;
import fr.dila.st.core.cache.ParamRefRelationCache;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * Implémentation du service permettant de gérer les paramètres du moteur elastic search
 *
 * @author Amaury Bianchi
 */
public class ElasticParametrageServiceImpl implements ElasticParametrageService {
    private static final String QUERY_PARAMETRE_FOLDER =
        "SELECT * FROM " + SolonEpgConstant.PARAMETRE_ES_FOLDER_DOCUMENT_TYPE;

    private static final String QUERY_PARAMETRE =
        "SELECT p.ecm:uuid AS id FROM " + SolonEpgConstant.PARAMETRE_ES_DOCUMENT_TYPE + " AS p WHERE p.ecm:name = ?";

    private static final String QUERY_LIST_PARAMETRE =
        "SELECT r.ecm:uuid AS id FROM " +
        SolonEpgConstant.PARAMETRE_ES_DOCUMENT_TYPE +
        " AS r  WHERE r.ecm:parentId = ?";

    private ParamRefRelationCache<String> cache;
    private String parametreFolderDocId;

    public ElasticParametrageServiceImpl() {
        cache = new ParamRefRelationCache<String>(QUERY_PARAMETRE);
    }

    /**
     * charge le document associé au folder contenant les parametres. L'id du document est gardé dans
     * parametreFolderDocId pour des chargement ulterieur évitant une requete SQL à la base.
     */
    @Override
    public DocumentModel getParametreFolder(CoreSession session) {
        if (parametreFolderDocId == null) {
            DocumentModelList list = session.query(QUERY_PARAMETRE_FOLDER);
            if (list == null || list.size() <= 0) {
                throw new NuxeoException("Racine des parametres non trouvée");
            } else if (list.size() > 1) {
                throw new NuxeoException("Plusieurs racines des parametres trouvées");
            }

            DocumentModel doc = list.get(0);
            parametreFolderDocId = doc.getId();
            return doc;
        } else {
            return session.getDocument(new IdRef(parametreFolderDocId));
        }
    }

    @Override
    public String getParametreValue(CoreSession session, String id) {
        STParametre stParametre = getParametre(session, id);
        if (stParametre != null) {
            return stParametre.getValue();
        }

        return null;
    }

    @Override
    public STParametre getParametre(CoreSession session, String name) {
        DocumentModel doc = cache.retrieveDocument(session, name);
        return doc == null ? null : doc.getAdapter(STParametre.class);
    }

    @Override
    public List<DocumentModel> getESParametres(CoreSession session) {
        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SolonEpgConstant.PARAMETRE_ES_DOCUMENT_TYPE,
            QUERY_LIST_PARAMETRE,
            new Object[] { getParametreFolder(session).getId() }
        );
    }

    @Override
    public void updateESParametre(CoreSession session, DocumentModel documentModel, String formValeur) {
        STParametre param = documentModel.getAdapter(STParametre.class);
        param.setValue(formValeur);
        session.saveDocument(param.getDocument());
        session.save();
    }
}
