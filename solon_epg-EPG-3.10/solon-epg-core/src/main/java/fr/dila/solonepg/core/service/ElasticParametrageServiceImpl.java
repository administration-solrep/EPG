package fr.dila.solonepg.core.service;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.service.ElasticParametrageService;
import fr.dila.st.api.parametre.STParametre;
import fr.dila.st.core.cache.ParamRefRelationCache;

/**
 * Implémentation du service permettant de gérer les paramètres du moteur elastic search
 * 
 * @author Amaury Bianchi
 */
public class ElasticParametrageServiceImpl implements ElasticParametrageService {

	private static final String				QUERY_PARAMETRE_FOLDER	= "SELECT * FROM "
																			+ SolonEpgConstant.PARAMETRE_ES_FOLDER_DOCUMENT_TYPE;

	private static final String				QUERY_PARAMETRE			= "SELECT p.ecm:uuid AS id FROM "
																			+ SolonEpgConstant.PARAMETRE_ES_DOCUMENT_TYPE
																			+ " AS p WHERE p.ecm:name = ?";

	private ParamRefRelationCache<String>	cache;
	private String							parametreFolderDocId;

	public ElasticParametrageServiceImpl() {
		cache = new ParamRefRelationCache<String>(QUERY_PARAMETRE);
	}

	/**
	 * charge le document associé au folder contenant les parametres. L'id du document est gardé dans
	 * parametreFolderDocId pour des chargement ulterieur évitant une requete SQL à la base.
	 */
	@Override
	public DocumentModel getParametreFolder(CoreSession session) throws ClientException {
		if (parametreFolderDocId == null) {
			DocumentModelList list = session.query(QUERY_PARAMETRE_FOLDER);
			if (list == null || list.size() <= 0) {
				throw new ClientException("Racine des parametres non trouvée");
			} else if (list.size() > 1) {
				throw new ClientException("Plusieurs racines des parametres trouvées");
			}

			DocumentModel doc = list.get(0);
			parametreFolderDocId = doc.getId();
			return doc;
		} else {
			return session.getDocument(new IdRef(parametreFolderDocId));
		}
	}

	@Override
	public String getParametreValue(CoreSession session, String id) throws ClientException {
		STParametre stParametre = getParametre(session, id);
		if (stParametre != null) {
			return stParametre.getValue();
		}

		return null;
	}

	@Override
	public STParametre getParametre(CoreSession session, String name) throws ClientException {
		DocumentModel doc = cache.retrieveDocument(session, name);
		return doc == null ? null : doc.getAdapter(STParametre.class);
	}
}
