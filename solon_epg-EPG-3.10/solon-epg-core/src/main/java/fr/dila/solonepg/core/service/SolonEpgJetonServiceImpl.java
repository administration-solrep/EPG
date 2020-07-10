package fr.dila.solonepg.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.constant.STWebserviceConstant;
import fr.dila.st.api.jeton.JetonDoc;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.JetonServiceImpl;
import fr.dila.st.core.util.StringUtil;

public class SolonEpgJetonServiceImpl extends JetonServiceImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2109940679562180188L;
	
	
	/**
	 * select id from jetonDoc where id_jeton = ? and type_ws = ? and id_owner = ? and and type_modification = ? AND id_doc IN ? order by created
	 */
	private static final String	QUERY_JETON_BY_ID_DOSS		= "SELECT jt.ecm:uuid AS id FROM "
																			+ STConstant.JETON_DOC_TYPE
																			+ " as jt WHERE jt."
																			+ STSchemaConstant.JETON_DOCUMENT_SCHEMA_PREFIX
																			+ ":"
																			+ STSchemaConstant.JETON_DOCUMENT_ID_JETON_DOC
																			+ " = ?  AND jt."
																			+ STSchemaConstant.JETON_DOCUMENT_SCHEMA_PREFIX
																			+ ":"
																			+ STSchemaConstant.JETON_DOCUMENT_WEBSERVICE
																			+ " = ? AND jt."
																			+ STSchemaConstant.JETON_DOCUMENT_SCHEMA_PREFIX
																			+ ":"
																			+ STSchemaConstant.JETON_DOCUMENT_ID_OWNER
																			+ " = ? AND jt." 
																			+ STSchemaConstant.JETON_DOCUMENT_SCHEMA_PREFIX
																			+ ":" 
																			+ STSchemaConstant.JETON_DOCUMENT_TYPE_MODIFICATION 
																			+ " = ? AND jt." 
																			+ STSchemaConstant.JETON_DOCUMENT_SCHEMA_PREFIX
																			+ ":" 
																			+ STSchemaConstant.JETON_DOCUMENT_ID_DOC 
																			+ " IN (";
	private static final String	QUERY_JETON_BY_ID_DOSS_ORDER_BY = " ORDER BY jt."
																			+ STSchemaConstant.JETON_DOCUMENT_SCHEMA_PREFIX
																			+ ":"
																			+ STSchemaConstant.JETON_DOC_CREATED_PROPERTY;

	
	/**
	 * Récupère les jetons présents dans le panier
	 * Traitement spécifique pour le WS ChercherModificationDossier
	 * 
	 * @param session
	 * @param typeWebservice
	 * @param owner
	 * @return
	 * @throws ClientException
	 */
	@Override
	protected List<DocumentModel> getJetonsInBasket(final CoreSession session, final String typeWebservice,
			final String owner, Long limitResults) throws ClientException {
		if (STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER.equals(typeWebservice)) {
			List<DocumentModel> jetonsDocsList = getJetonsByJetonNumber(session, NUMERO_PANIER, typeWebservice, owner, 1L);
			return getJetonsDossierRelatedInBasket(session, jetonsDocsList);
		} else {
			return getJetonsByJetonNumber(session, NUMERO_PANIER, typeWebservice, owner, limitResults);
		}
	}
	
	@Override
	protected boolean updateJetons(final CoreSession session, String typeWebservice, String owner,
			final List<DocumentModel> jetonsDocsPanier, final List<DocumentModel> jetonsDocList, 
			final List<DocumentModel> documents, final Long jetonResultSize, final Long numeroJeton) throws ClientException {
		boolean lastSending = true;
		// S'il y a assez de place dans un jeton pour contenir les documents
		// Ou cas spécifique du ws chercher modification dossier epg
		if (STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER.equals(typeWebservice) || jetonsDocsPanier.size() <= jetonResultSize.intValue()) {
			// il n'y a pas d'autres jetons qui suivent (dernier envoi = true)
			for (final DocumentModel jetonDocDoc : jetonsDocsPanier) {
				setDocumentsAndJetonNumber(session, jetonDocDoc, jetonsDocList, documents, numeroJeton);
			}
			// Dans le cas du WS Epg Chercher modification Dossier, on peut avoir plus de retour que la limite jeton,
			// on check si le panier est vide
			if (STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER.equals(typeWebservice)) {
				lastSending = isBasketEmpty(session, typeWebservice, owner);
			}
		} else {
			// Les documents sont trop nombreux pour être contenus dans un seul résultat,
			// Il faut en remplir un, et annoncer qu'il y a encore des résultats en attente de lecture (dernier
			// envoi = false)
			proceedHighNumberOfJetonDoc(session, jetonsDocsPanier, jetonResultSize, jetonsDocList, documents,
					numeroJeton);
			lastSending = isBasketEmpty(session, typeWebservice, owner);
		}
		return lastSending;
	}

	/**
	 * récupère tous les jetons pour le ws chercherModificationDossier d'un même dossier présents dans le panier 
	 * @param session
	 * @param numeroPanier
	 * @param typeWebservice
	 * @param owner
	 * @param limitResults
	 * @return
	 * @throws ClientException 
	 */
	private List<DocumentModel> getJetonsDossierRelatedInBasket(final CoreSession session, final List<DocumentModel> jetonsDocsList) throws ClientException {
		// On doit récupérer les jetons dont l'id doc est celui d'un dossier qu'on a récupéré dans jetonsDocsList
		// et dont le type de modification est identique afin de tout remonter en une seule fois. 
		final List<DocumentModel> jetonsDocsCompleted = new ArrayList<DocumentModel>(); 
		
		Map<String, Set<String>> typeModifIdsDocsMap = new HashMap<String, Set<String>>();		
		for (DocumentModel jetonDoc : jetonsDocsList) {
			JetonDoc jeton = jetonDoc.getAdapter(JetonDoc.class);
			String typeModif = jeton.getTypeModification();
			Set<String> idsDocsSet = typeModifIdsDocsMap.get(typeModif);
			if (idsDocsSet == null) {
				idsDocsSet = new HashSet<String>();
			}
			idsDocsSet.add(jeton.getIdDoc());
			typeModifIdsDocsMap.put(typeModif, idsDocsSet);
		}
		
		for (Entry<String, Set<String>> entry : typeModifIdsDocsMap.entrySet()) {
			StringBuilder query = new StringBuilder(QUERY_JETON_BY_ID_DOSS)
					.append(StringUtil.getQuestionMark(entry.getValue().size()))
					.append(") ")
					.append(QUERY_JETON_BY_ID_DOSS_ORDER_BY);
			List<Object> params = new ArrayList<Object>();
			params.add(NUMERO_PANIER);
			params.add(STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER);
			params.add(TableReference.MINISTERE_CE);
			params.add(entry.getKey());
			params.addAll(entry.getValue());
			List<DocumentModel> othersJetonsDocs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, STConstant.JETON_DOC_TYPE,
					query.toString(), params.toArray());
			jetonsDocsCompleted.addAll(othersJetonsDocs);
		}
		
		return jetonsDocsCompleted;
	}
}
