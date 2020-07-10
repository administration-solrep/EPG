package fr.dila.solonepg.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.StringUtils;
import org.apache.xerces.dom.ElementNSImpl;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.w3c.dom.NodeList;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteTransposition;
import fr.dila.solonepg.api.cases.TranspositionDirective;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.dto.TexteTranspositionDTO;
import fr.dila.solonepg.api.service.TranspositionDirectiveService;
import fr.dila.solonepg.rest.client.WSEurlexCaller;
import fr.dila.solonepg.rest.client.WSEurlexException;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.sword.wsdl.solon.eurlex.ExpressionType;
import fr.sword.wsdl.solon.eurlex.Literal;
import fr.sword.wsdl.solon.eurlex.NOTICE;
import fr.sword.wsdl.solon.eurlex.ResultType;
import fr.sword.wsdl.solon.eurlex.SearchRequest;
import fr.sword.wsdl.solon.eurlex.SearchResults;

/**
 * Implementation de {@link TranspositionDirectiveService}
 * 
 * @author asatre
 * 
 */
public class TranspositionDirectiveServiceImpl implements TranspositionDirectiveService {

	private static final long		serialVersionUID	= -7647363980451152980L;

	/**
	 * Logger.
	 */
	private static final STLogger	LOGGER				= STLogFactory.getLog(TranspositionDirectiveServiceImpl.class);

	@Override
	public TranspositionDirective findOrCreateTranspositionDirective(final CoreSession session, final String numero)
			throws ClientException {

		final StringBuilder queryBuilder = new StringBuilder(" SELECT t.ecm:uuid as id FROM ");
		queryBuilder.append(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE);
		queryBuilder.append(" as t WHERE t.");
		queryBuilder.append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(TexteMaitreConstants.NUMERO);
		queryBuilder.append(" = ? ");

		final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, queryBuilder.toString(),
				new Object[] { numero });

		if (list.size() > 1) {
			throw new ClientException("Plusieurs directives trouvées. Impossibilité de choisir.");
		}

		if (list.size() == 1) {
			return list.get(0).getAdapter(TranspositionDirective.class);
		} else {
			// create TranspositionDirective
			DocumentModel modelDesired = session
					.createDocumentModel(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE);
			modelDesired.setPathInfo(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_PATH, UUID.randomUUID().toString());

			final TranspositionDirective transpositionDirective = modelDesired.getAdapter(TranspositionDirective.class);
			transpositionDirective.setNumero(numero);

			modelDesired = session.createDocument(transpositionDirective.getDocument());

			session.save();

			return modelDesired.getAdapter(TranspositionDirective.class);

		}

	}

	@Override
	public TranspositionDirective updateTranspositionDirectiveWithCheckUnique(final CoreSession session,
			final TranspositionDirective transpositionDirective) throws ClientException {

		checkUnicity(session, transpositionDirective.getNumero(), transpositionDirective.getId());

		final DocumentModel doc = session.saveDocument(transpositionDirective.getDocument());

		return doc.getAdapter(TranspositionDirective.class);
	}

	private void checkUnicity(final CoreSession session, final String numero, final String uuid) throws ClientException {

		final StringBuilder queryBuilder = new StringBuilder(" SELECT t.ecm:uuid as id FROM ");
		queryBuilder.append(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE);
		queryBuilder.append(" as t WHERE t.");
		queryBuilder.append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(TexteMaitreConstants.NUMERO);
		queryBuilder.append(" = ? AND t.ecm:uuid != ? ");

		final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, queryBuilder.toString(), new Object[] {
						numero, uuid });

		if (!list.isEmpty()) {
			throw new ClientException("ce numéro est déjà attribué à une autre directive");
		}

	}

	@Override
	public TranspositionDirective updateTranspositionDirective(final CoreSession session,
			final TranspositionDirective transpositionDirective) throws ClientException {

		final DocumentModel doc = session.saveDocument(transpositionDirective.getDocument());

		return doc.getAdapter(TranspositionDirective.class);
	}

	@Override
	public Set<String> findLinkedDossierNor(final TranspositionDirective transpositionDirective,
			final CoreSession session) throws ClientException {

		final StringBuilder queryBuilder = new StringBuilder(" SELECT d.");
		queryBuilder.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY);
		queryBuilder.append(" as nor FROM ");
		queryBuilder.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
		queryBuilder.append(" as d WHERE d.");
		queryBuilder.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(DossierSolonEpgConstants.TRANSPOSITION_DIRECTIVE_NUMERO);
		queryBuilder.append(" = ? ");

		final Set<String> nors = new HashSet<String>();

		IterableQueryResult iter = null;
		try {
			iter = QueryUtils.doUFNXQLQuery(session, queryBuilder.toString(),
					new Object[] { transpositionDirective.getNumero() });
			final Iterator<Map<String, Serializable>> iterator = iter.iterator();
			while (iterator.hasNext()) {
				final Map<String, Serializable> map = iterator.next();
				final String nor = (String) map.get("nor");
				nors.add(nor);
			}
		} finally {
			if (iter != null) {
				iter.close();
			}
		}
		return nors;

	}

	@Override
	public void attachDirectiveEuropenneToDossier(final Dossier dossier, final CoreSession session)
			throws ClientException {
		final List<ComplexeType> list = dossier.getTranspositionDirective();

		if (list == null || list.isEmpty()) {
			return;
		}

		final List<String> listTdeu = new ArrayList<String>();

		for (final ComplexeType complexeType : list) {
			final String numero = (String) complexeType.getSerializableMap().get(
					DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY);
			listTdeu.add(numero);

			final TranspositionDirective transpositionDirective = findOrCreateTranspositionDirective(session, numero);
			final Set<String> listNor = new HashSet<String>(transpositionDirective.getDossiersNor());

			listNor.add(dossier.getNumeroNor());
			transpositionDirective.setDossiersNor(new ArrayList<String>(listNor));
			session.saveDocument(transpositionDirective.getDocument());

			session.save();
		}

		dossier.setTranspositionDirectiveNumero(listTdeu);

		session.saveDocument(dossier.getDocument());

		session.save();
	}

	@Override
	public List<TexteTransposition> fetchTexteTransposition(final TranspositionDirective transpositionDirective,
			final CoreSession session) throws ClientException {

		final List<String> params = new ArrayList<String>();
		params.addAll(transpositionDirective.getDossiersNor());

		final List<TexteTransposition> listTexte = new ArrayList<TexteTransposition>();

		if (!params.isEmpty()) {

			final StringBuilder queryBuilder = new StringBuilder(" SELECT d.ecm:uuid as id FROM ");
			queryBuilder.append(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE);
			queryBuilder.append(" as d WHERE d.");
			queryBuilder.append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX);
			queryBuilder.append(":");
			queryBuilder.append(TexteMaitreConstants.NUMERO_NOR);
			queryBuilder.append(" IN  (");
			queryBuilder.append(StringUtil.getQuestionMark(params.size()));
			queryBuilder.append(" ) ");

			final List<String> listId = QueryUtils.doUFNXQLQueryForIdsList(session, queryBuilder.toString(),
					params.toArray());

			for (final String id : listId) {
				final DocumentModel doc = session.getDocument(new IdRef(id));
				if (doc != null) {
					final TexteTransposition ttr = doc.getAdapter(TexteTransposition.class);
					params.remove(ttr.getNumeroNor());
					listTexte.add(ttr);
				}
			}
		}

		for (final String nor : params) {
			// creation des TexteTransposition n'existant pas
			listTexte.add(createTexteTransposition(session, nor));
		}

		return listTexte;

	}

	private TexteTransposition createTexteTransposition(final CoreSession session, final String nor)
			throws ClientException {

		DocumentModel modelDesired = session
				.createDocumentModel(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE);
		modelDesired.setPathInfo(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_PATH, UUID.randomUUID().toString());

		final TexteTransposition texteTransposition = modelDesired.getAdapter(TexteTransposition.class);
		texteTransposition.setNumeroNor(nor);

		modelDesired = session.createDocument(texteTransposition.getDocument());

		return modelDesired.getAdapter(TexteTransposition.class);
	}

	@Override
	public TranspositionDirective saveTexteTranspositionDTO(final TranspositionDirective transpositionDirective,
			final List<TexteTranspositionDTO> listTexteTranspositionDTO, final CoreSession session)
			throws ClientException {

		final List<String> listNor = new ArrayList<String>();

		for (final TexteTranspositionDTO texteTranspositionDTO : listTexteTranspositionDTO) {

			final String numeroNor = texteTranspositionDTO.getNumeroNor();

			if (StringUtils.isNotEmpty(numeroNor)) {
				final ActiviteNormative activiteNormative = SolonEpgServiceLocator.getActiviteNormativeService()
						.findOrCreateActiviteNormativeByNor(session, numeroNor);

				TexteTransposition texteTransposition = activiteNormative.getDocument().getAdapter(
						TexteTransposition.class);

				if (texteTranspositionDTO.getValidate()) {
					texteTransposition = texteTranspositionDTO.remapField(texteTransposition);
				}

				session.saveDocument(texteTransposition.getDocument());

				listNor.add(numeroNor);

				// Ajout dans le journal du PAN
				if (texteTranspositionDTO.getValidate()) {
					STServiceLocator.getJournalService().journaliserActionPAN(session, null,
							SolonEpgEventConstant.MODIF_TXT_TRANSPO_EVENT,
							SolonEpgEventConstant.MODIF_TXT_TRANSPO_COMMENT + " ["
									+ texteTranspositionDTO.getNumeroNor() + "] transposant la directive ["
									+ transpositionDirective.getNumero() + "]",
							SolonEpgEventConstant.CATEGORY_LOG_PAN_TRANSPO_DIRECTIVES);
				}
			}

		}

		transpositionDirective.setDossiersNor(listNor);

		final DocumentModel doc = session.saveDocument(transpositionDirective.getDocument());

		return doc.getAdapter(TranspositionDirective.class);

	}

	@Override
	public void refreshTexteTransposition(final List<TexteTranspositionDTO> listTexteTranspositionDTO,
			final CoreSession session) throws ClientException {

		for (final TexteTranspositionDTO texteTranspositionDTO : listTexteTranspositionDTO) {

			final String numeroNor = texteTranspositionDTO.getNumeroNor();

			if (StringUtils.isNotEmpty(numeroNor)) {
				final Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(session, numeroNor);
				texteTranspositionDTO.remapField(dossier, session);
			}
		}

	}

	@Override
	public TranspositionDirective reloadTransposition(TranspositionDirective transpositionDirective,
			final CoreSession session) throws ClientException {

		transpositionDirective = findOrCreateTranspositionDirective(session, transpositionDirective.getNumero());

		final ActiviteNormative activiteNormative = transpositionDirective.getDocument().getAdapter(
				ActiviteNormative.class);

		activiteNormative.setTransposition(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE);

		final Set<String> setNor = findLinkedDossierNor(transpositionDirective, session);
		setNor.addAll(transpositionDirective.getDossiersNor());
		transpositionDirective.setDossiersNor(new ArrayList<String>(setNor));

		return updateTranspositionDirective(session, transpositionDirective);
	}

	@Override
	public String findDirectiveEurlexWS(String reference, String annee, String titre) {

		// Récupération de la config pour appeler le webservice Eurlex
		ConfigService configService = STServiceLocator.getConfigService();
		String eurlexURL = configService.getValue(SolonEpgConfigConstant.EURLEX_WEBSERVICE_URL);
		String eurlexUserName = configService.getValue(SolonEpgConfigConstant.EURLEX_WEBSERVICE_USER_NAME);
		String eurlexMDP = configService.getValue(SolonEpgConfigConstant.EURLEX_WEBSERVICE_MDP);
		String titreDirective = null;
		try {

			String refFormatee = new String(new char[4 - reference.length()]).replace("\0", "0") + reference;
			LOGGER.debug(STLogEnumImpl.LOG_DEBUG_TEC, "Préparation de l'appel au Web Service Eurlex");
			// Construction de la requête
			SearchRequest request = new SearchRequest();
			request.setExpertQuery("SELECT TI_DISPLAY WHERE DN = 3" + annee + "L" + refFormatee);
			LOGGER.debug(STLogEnumImpl.LOG_DEBUG_TEC, "Requête experte :" + "SELECT TI_DISPLAY WHERE DN = 3" + annee
					+ "L" + refFormatee);
			request.setPage(1);
			request.setPageSize(1);
			request.setSearchLanguage(fr.sword.wsdl.solon.eurlex.SearchLanguageType.FR);
			final WSEurlexCaller caller = new WSEurlexCaller();
			try {
				final SearchResults response = caller.rechercherExistenceTranspositionDirective(request,
						eurlexUserName, eurlexMDP, eurlexURL);

				List<ResultType> listResults = response.getResult();
				// On prend le premier résultat trouvé
				if (listResults != null && !listResults.isEmpty()) {
					LOGGER.debug(STLogEnumImpl.LOG_DEBUG_TEC, "Le webservice Eurlex a trouvé des résultats");

					ResultType firstResult = listResults.get(0);
					if (firstResult != null && firstResult.getContent() != null
							&& firstResult.getContent().getNOTICEAndCONTENTURL() != null
							&& !firstResult.getContent().getNOTICEAndCONTENTURL().isEmpty()) {
						NOTICE notice = (NOTICE) (firstResult.getContent().getNOTICEAndCONTENTURL().get(0));
						if (notice != null && notice.getEXPRESSIONAndINVERSEAndMANIFESTATION() != null
								&& !notice.getEXPRESSIONAndINVERSEAndMANIFESTATION().isEmpty()) {
							ExpressionType lit = (ExpressionType) (notice.getEXPRESSIONAndINVERSEAndMANIFESTATION()
									.get(0));
							if (lit != null
									&& lit.getEXPRESSIONCASELAWIDENTIFIERCASEAndEXPRESSIONCASELAWPARTIESAndEXPRESSIONSUBTITLE() != null
									&& !lit.getEXPRESSIONCASELAWIDENTIFIERCASEAndEXPRESSIONCASELAWPARTIESAndEXPRESSIONSUBTITLE()
											.isEmpty()) {
								@SuppressWarnings("rawtypes")
								JAXBElement litE = lit
										.getEXPRESSIONCASELAWIDENTIFIERCASEAndEXPRESSIONCASELAWPARTIESAndEXPRESSIONSUBTITLE()
										.get(0);
								if (litE != null && litE.getValue() != null) {
									Literal litEValue = (Literal) litE.getValue();
									if (litEValue != null) {
										ElementNSImpl litValueVal = (ElementNSImpl) litEValue.getVALUE();
										if (litValueVal != null) {
											NodeList nodeListe = litValueVal.getChildNodes();
											if (nodeListe != null && nodeListe.getLength() > 0
													&& nodeListe.item(0) != null) {
												titreDirective = nodeListe.item(0).getNodeValue();
												LOGGER.debug(STLogEnumImpl.LOG_DEBUG_TEC,
														"La directive a pour titre : " + titreDirective);
											}
										}
									}
								}
							}
						}
					}
				}
			} catch (WSEurlexException e) {
				return titreDirective;
			}
			return titreDirective;
		} catch (Exception e) {
			LOGGER.error(STLogEnumImpl.LOG_EXCEPTION_TEC, e);
			return titreDirective;
		}
	}
}
