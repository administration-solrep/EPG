package fr.dila.solonepg.elastic.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ElasticParametrageService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.elastic.models.ElasticDocument;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.search.FacetEnum;
import fr.dila.solonepg.elastic.models.search.SearchCriteria;
import fr.dila.solonepg.elastic.models.search.SearchCriteria.SortType;
import fr.dila.solonepg.elastic.models.search.request.Aggregation;
import fr.dila.solonepg.elastic.models.search.request.BoolQuery;
import fr.dila.solonepg.elastic.models.search.request.Highlight;
import fr.dila.solonepg.elastic.models.search.request.HighlightField;
import fr.dila.solonepg.elastic.models.search.request.InnerHits;
import fr.dila.solonepg.elastic.models.search.request.NestedClause;
import fr.dila.solonepg.elastic.models.search.request.QueryClause;
import fr.dila.solonepg.elastic.models.search.request.RangeClause;
import fr.dila.solonepg.elastic.models.search.request.SearchRequest;
import fr.dila.solonepg.elastic.models.search.request.SimpleQueryString;
import fr.dila.solonepg.elastic.models.search.request.SortElement;
import fr.dila.solonepg.elastic.models.search.request.TermClause;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;

public class SearchRequestMapper implements ISearchRequestMapper {
	private List<String> getDossierFullTextFieldsAndPonderation(CoreSession session) throws ClientException {
		if (session == null) {
			return getDefaultsPonderations();
		}

		return ParametrePonderation.toStringAllDossier(session);
	}

	private static List<String> getDocumentFullTextFieldsAndPonderation() throws ClientException {
		return Arrays.asList(new String[] { ElasticDocument.FILE_DATA + "^1.0", ElasticDocument.DC_TITLE + "^1.0" });
	}

	private static List<String> getDocumentFieldsAndPonderation(CoreSession session) throws ClientException {
		if (session == null) {
			return Arrays.asList(new String[] { ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE + "^2.0",
					ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA + "^1.0" });
		}
		ArrayList<String> list = new ArrayList<String>();

		// Titre document
		list.add(ParametrePonderation.DOCUMENT_TITRE.toString(session));
		list.add(ParametrePonderation.DOCUMENT_FILEDATA.toString(session));

		return list;
	}

	@Override
	public SearchRequest all() {
		SearchRequest request = new SearchRequest(0, 500000,
				Collections.<String>emptyList(), Collections.<String>singletonList(ElasticDossier.UID));
		return request;
	}

	@Override
	public SearchRequest from(SearchCriteria criteria, CoreSession session) throws ClientException {
		Integer page = criteria.getPage();
		Integer pageSize = criteria.getPageSize();

		int from = (page - 1) * pageSize;

		// Fonctionnement elastic
		// docvalue_fields : permet de récupérer les valeurs pour les types non analysés ; performance ++
		//                   activé par défaut pour tous les champs
		// stored_fields : permet de récupérer les valeurs même sur un champ analysé
		//                 nécessite d'être activé sur le champ
		List<String> storedFields = Arrays.asList(new String[] {
				ElasticDossier.DOS_TITRE_ACTE
		});
		List<String> docvalueFields = Arrays.asList(new String[] {
				ElasticDossier.UID, /* pour le debugging */
				ElasticDossier.DOS_MINISTERE_RESP, ElasticDossier.DOS_STATUT, ElasticDossier.DOS_NUMERO_NOR,
				ElasticDossier.DOS_CREATED, ElasticDossier.RETDILA_DATE_PARUTION_JORF, /* informations affichées */
		});

		SearchRequest request = new SearchRequest(from, pageSize, storedFields, docvalueFields);

		initQuery(request);

		addVecteurPublication(criteria, request);
		addCategorieActe(criteria, request);
		addStatut(criteria, request);
		addMinistereResponsable(criteria, request);
		addTypeActe(criteria, request);
		addStatutArchivage(criteria, request);

		addDatesPublication(criteria, request);
		addDatesCreation(criteria, request);

		addArchive(criteria, request);

		addFulltext(criteria, request, session);

		addAggregations(request);

		addHighlight(request);

		addSort(criteria, request);

		addPerms(criteria, request);

		addDroitsNomination(criteria, request);

		return request;
	}

	private void addPerms(SearchCriteria criteria, SearchRequest request) {
		Collection<String> permsUser = criteria.getPermsUtilisateur();
		// si l'utilisateur n'a pas de poste on en ajoute un virtuel pour éviter de renvoyer tous les résultats
		if (permsUser == null) {
			permsUser = new ArrayList<String>();
		}

		if (permsUser.isEmpty()) {
			permsUser.add("-1");
		}

		addFacet(permsUser, request, ElasticDossier.PERMS, true);
	}

	private void addDroitsNomination(SearchCriteria criteria, SearchRequest request) {
		if (false == criteria.isHasDroitsNomination()) {
			Collection<String> hecant = new ArrayList<String>();
			hecant.add("false");
			addFacet(hecant, request, ElasticDossier.DOS_MESURE_NOMINATIVE);
		}

	}

	/**
	 * Initialisation de la partie query
	 * 
	 * @param request
	 */
	private void initQuery(SearchRequest request) {
		BoolQuery boolQuery = new BoolQuery(1, false, true);

		boolQuery.setMustSubClauses(new ArrayList<QueryClause>());

		request.setQuery(boolQuery);
	}

	/**
	 * @param request
	 */
	private void addAggregations(SearchRequest request) {
		Map<String, Aggregation> aggregations = new HashMap<String, Aggregation>();

		Aggregation agg0 = new Aggregation(ElasticDossier.DOS_MINISTERE_RESP, 100, 1, 0, false);
		List<Map<String, String>> orders = new ArrayList<Map<String, String>>();
		HashMap<String, String> order0 = new HashMap<String, String>();
		order0.put("_count", "desc");
		HashMap<String, String> order1 = new HashMap<String, String>();
		order1.put("_term", "asc");
		orders.add(order0);
		orders.add(order1);
		agg0.setOrders(orders);
		aggregations.put(FacetEnum.RESPONSABLE.getFacetName(), agg0);

		Aggregation agg1 = new Aggregation(ElasticDossier.DOS_VECTEUR_PUBLICATION, 100, 1, 0, false);
		orders = new ArrayList<Map<String, String>>();
		order1 = new HashMap<String, String>();
		order1.put("_term", "asc");
		orders.add(order1);
		agg1.setOrders(orders);
		aggregations.put(FacetEnum.VECTEUR_PUBLICATION.getFacetName(), agg1);

		Aggregation agg2 = new Aggregation(ElasticDossier.DOS_STATUT, 100, 1, 0, false);
		orders = new ArrayList<Map<String, String>>();
		order1 = new HashMap<String, String>();
		order1.put("_term", "asc");
		orders.add(order1);
		agg2.setOrders(orders);
		aggregations.put(FacetEnum.STATUT.getFacetName(), agg2);

		Aggregation agg3 = new Aggregation(ElasticDossier.DOS_CATEGORIE_ACTE, 100, 1, 0, false);
		orders = new ArrayList<Map<String, String>>();
		order1 = new HashMap<String, String>();
		order1.put("_term", "asc");
		orders.add(order1);
		agg3.setOrders(orders);
		aggregations.put(FacetEnum.CATEGORIE_ACTE.getFacetName(), agg3);

		Aggregation agg4 = new Aggregation(ElasticDossier.DOS_TYPE_ACTE, 100, 1, 0, false);
		orders = new ArrayList<Map<String, String>>();
		order1 = new HashMap<String, String>();
		order1.put("_term", "asc");
		orders.add(order1);
		agg4.setOrders(orders);
		aggregations.put(FacetEnum.TYPE_ACTE.getFacetName(), agg4);

		Aggregation agg5 = new Aggregation(ElasticDossier.DOS_STATUT_ARCHIVAGE_ID, 100, 1, 0, false);
		orders = new ArrayList<Map<String, String>>();
		order1 = new HashMap<String, String>();
		order1.put("_term", "asc");
		orders.add(order1);
		agg5.setOrders(orders);
		aggregations.put(FacetEnum.STATUT_ARCHIVAGE.getFacetName(), agg5);

		request.setAggregations(aggregations);
	}

	private void addVecteurPublication(SearchCriteria criteria, SearchRequest request) {
		addFacet(criteria.getVecteurPublication(), request, ElasticDossier.DOS_VECTEUR_PUBLICATION);
	}

	private void addArchive(SearchCriteria criteria, SearchRequest request) {
		Boolean boolArchive = criteria.isArchive();
		Boolean boolProd = criteria.isProd();
		if (boolArchive ^ boolProd) {
			ArrayList<String> values = new ArrayList<String>();
			values.add(boolArchive.toString());
			addFacet(values, request, ElasticDossier.DOS_ARCHIVE);
		}
	}

	private void addCategorieActe(SearchCriteria criteria, SearchRequest request) {
		addFacet(criteria.getCategorieActe(), request, ElasticDossier.DOS_CATEGORIE_ACTE);
	}

	private void addTypeActe(SearchCriteria criteria, SearchRequest request) {
		addFacet(criteria.getTypeActe(), request, ElasticDossier.DOS_TYPE_ACTE);
	}

	private void addStatut(SearchCriteria criteria, SearchRequest request) {
		addFacet(criteria.getStatut(), request, ElasticDossier.DOS_STATUT);
	}

	private void addMinistereResponsable(SearchCriteria criteria, SearchRequest request) {
		addFacet(criteria.getResponsable(), request, ElasticDossier.DOS_MINISTERE_RESP);
	}

	private void addStatutArchivage(SearchCriteria criteria, SearchRequest request) {
		// Si des statuts équivalents à initial sont présents, ils sont remplacés par tous les statuts équivalent à initial
		Collection<String> selectedStatutArchivages = criteria.getStatutArchivage();
		boolean isInitialSelected = false;
		Iterator<String> i = selectedStatutArchivages.iterator();
		while (i.hasNext()) {
			String statutArchivage = i.next();
			if (VocabularyConstants.STATUT_ARCHIVAGE_INITIAL_FACET.contains(statutArchivage)) {
				isInitialSelected = true;
				i.remove();
			}
		}
		if (isInitialSelected) {
			selectedStatutArchivages.addAll(VocabularyConstants.STATUT_ARCHIVAGE_INITIAL_FACET);
		}
		addFacet(criteria.getStatutArchivage(), request, ElasticDossier.DOS_STATUT_ARCHIVAGE_ID, true);
	}

	private void addFacet(Collection<String> checkedValues, SearchRequest request, String field) {
		addFacet(checkedValues, request, field, false);
	}

	private void addFacet(Collection<String> checkedValues, SearchRequest request, String field, boolean should) {
		if (checkedValues != null && !checkedValues.isEmpty()) {
			List<QueryClause> must = request.getQuery().getMustSubClauses();

			BoolQuery query = new BoolQuery(1, false, true);
			List<QueryClause> clauses = new ArrayList<QueryClause>();

			for (String value : checkedValues) {
				clauses.add(new TermClause(1, field, value));
			}

			if (should) {
				query.setShouldSubClauses(clauses);
			} else {
				query.setMustSubClauses(clauses);
			}
			must.add(query);
		}
	}

	private void addDatesPublication(SearchCriteria criteria, SearchRequest request) throws ClientException {
		addDatesRange(ElasticDossier.RETDILA_DATE_PARUTION_JORF, criteria.getDatePublicationMin(),
				criteria.getDatePublicationMax(), request);
	}

	private void addDatesCreation(SearchCriteria criteria, SearchRequest request) throws ClientException {
		addDatesRange(ElasticDossier.DOS_CREATED, criteria.getDateCreationMin(), criteria.getDateCreationMax(), request);
	}

	private void addDatesRange(String field, String dateMin, String dateMax, SearchRequest request)
			throws ClientException {
		if (StringUtil.isNotEmpty(dateMin) || StringUtil.isNotEmpty(dateMax)) {
			List<QueryClause> must = request.getQuery().getMustSubClauses();

			BoolQuery query = new BoolQuery(1, false, true);

			List<QueryClause> subMust = new ArrayList<QueryClause>();

			if (StringUtil.isNotEmpty(dateMin)) {
				Date date = DateUtil.parse(dateMin).getTime();
				subMust.add(new RangeClause(1, field, date, null, true, true));
			}

			if (StringUtil.isNotEmpty(dateMax)) {
				Date date = DateUtil.parse(dateMax).getTime();
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.DATE, 1);
				c.add(Calendar.MINUTE, -1);
				date = c.getTime();
				subMust.add(new RangeClause(1, field, null, date, true, true));
			}

			query.setMustSubClauses(subMust);
			must.add(query);
		}
	}

	private void addFulltext(SearchCriteria criteria, SearchRequest request, CoreSession session)
			throws ClientException {
		String fulltext = criteria.getFulltext();

		if (StringUtil.isNotEmpty(fulltext)) {

			if (criteria.isExpressionExacte()) {
				fulltext = "\"" + fulltext + "\"";
			}

			List<QueryClause> must = request.getQuery().getMustSubClauses();

			BoolQuery query = new BoolQuery(1, false, true);

			List<QueryClause> should = new ArrayList<QueryClause>();

			should.add(new SimpleQueryString(1, fulltext, getDossierFullTextFieldsAndPonderation(session), -1, "or",
					false, false));

			NestedClause nested = new NestedClause(1, ElasticDossier.DOCUMENTS, false, "sum");
			InnerHits innerHits = new InnerHits(false, 0, false, false, false, null, Collections.singletonList(ElasticDossier.DOCUMENTS + "." + ElasticDocument.UID));

			nested.setInnerHits(innerHits);

			BoolQuery query2 = new BoolQuery(1, false, true);
			List<QueryClause> innerMust = new ArrayList<QueryClause>();
			innerMust.add(new SimpleQueryString(1.0, fulltext, getDocumentFieldsAndPonderation(session), -1, "or",
					false, false));
			if (criteria.isDocCurrentVersion()) {
				innerMust.add(new TermClause(1.0, ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_CURRENTVERSION,
						"true"));
			}
			query2.setMustSubClauses(innerMust);
			nested.setQuery(query2);

			should.add(nested);

			query.setShouldSubClauses(should);

			must.add(query);
		}
	}

	private void addHighlight(SearchRequest request) {
		Highlight highlight = new Highlight(false);

		List<HighlightField> fields = new ArrayList<HighlightField>();
		fields.add(new HighlightField(ElasticDossier.DOS_TITRE_ACTE, 1500, 1));
		highlight.setFields(fields);

		request.setHighlight(highlight);
	}

	@Override
	public SearchRequest from(String documentId, String fulltext, CoreSession session) throws ClientException {
		SearchRequest request = new SearchRequest(null, 1, Arrays.asList(new String[] { ElasticDocument.DC_TITLE }),
				Arrays.asList(new String[] { ElasticDocument.UID }));

		// query
		BoolQuery query = new BoolQuery(1.0, false, true);

		List<QueryClause> must = new ArrayList<QueryClause>();
		must.add(new TermClause(1.0, ElasticDocument.UID, documentId));
		query.setMustSubClauses(must);

		List<QueryClause> should = new ArrayList<QueryClause>();
		should.add(new SimpleQueryString(1.0, fulltext, getDocumentFullTextFieldsAndPonderation(), -1, "or", false,
				false));
		query.setShouldSubClauses(should);

		request.setQuery(query);
		List<String> docvalueFields = new ArrayList<String>();
		docvalueFields.add("uid:major_version");
		docvalueFields.add("uid:minor_version");
		request.setDocvalueFields(docvalueFields);

		// highlight
		Highlight highlight = new Highlight(false);

		List<HighlightField> fields = new ArrayList<HighlightField>();
		fields.add(new HighlightField(ElasticDocument.DC_TITLE, 500, 1));
		fields.add(new HighlightField(ElasticDocument.FILE_DATA, 150, 1));
		highlight.setFields(fields);

		request.setHighlight(highlight);

		return request;
	}

	private void addSort(SearchCriteria criteria, SearchRequest request) {
		List<SortElement> sortElements = new ArrayList<SortElement>();
		SortType sortType = criteria.getSortType();
		sortElements.add(new SortElement(sortType.getField(), "desc".equals(sortType.getOrder())));

		request.setSort(sortElements);
	}

	private List<String> getDefaultsPonderations() {
		return Arrays.asList(new String[] { ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE + "^2.0",
				ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA + "^1.0",
				ElasticDossier.DOS_NUMERO_NOR + "^10.0", ElasticDossier.DOS_TITRE_ACTE + "^5.0" });
	}

	private static enum ParametrePonderation {
		DOSSIER_TITRE_ACTE(ElasticDossier.DOS_TITRE_ACTE, "parametre-titreacte"),

		DOSSIER_NUMERO_NOR(ElasticDossier.DOS_NUMERO_NOR, "parametre-numeronor"),

		DOSSIER_CHARGES_MISSION(ElasticDossier.DOS_NOM_COMPLET_CHARGES_MISSIONS, "parametre-chargemission"),

		DOSSIER_CONSEILLERS_PM(ElasticDossier.DOS_NOM_COMPLET_CONSEILLERS_PMS, "parametre-conseillerpm"),

		DOSSIER_INFO_JO(ElasticDossier.RETDILA_TITRE_OFFICIEL, "parametre-info-jo"),

		DOSSIER_COM_SGG_DILA(ElasticDossier.DOS_ZONE_COM_SGG_DILA, "parametre-comm-sgg-dila"),

		DOSSIER_BASE_LEGALE(ElasticDossier.DOS_BASE_LEGALE_ACTE, "parametre-base-legale"),

		DOSSIER_RUBRIQUES(ElasticDossier.DOS_RUBRIQUES, "parametre-rubrique"),

		DOSSIER_MOTS_CLEFS(ElasticDossier.DOS_MOTSCLES, "parametre-mots-clefs"),

		DOSSIER_CHAMPS_LIBRES(ElasticDossier.DOS_LIBRE, "parametre-champs-libres"),

		DOSSIER_CE_RAPPORTEUR(ElasticDossier.CONSETAT_RAPPORTEUR_CE, "parametre-champs-ce"),

		DOSSIER_CE_SECTION(ElasticDossier.CONSETAT_SECTION_CE, "parametre-champs-ce"),

		DOSSIER_DIRECTIVE_TITRE(ElasticDossier.DOS_TRANSPOSITION_DIRECTIVE_TITRE, "parametre-champs-directive"),

		DOSSIER_DIRECTIVE_COMMENTAIRE(ElasticDossier.DOS_TRANSPOSITION_DIRECTIVE_COMMENTAIRE, "parametre-champs-directive"),

		DOCUMENT_TITRE(ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE, "parametre-titredocument"),

		DOCUMENT_FILEDATA(ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA, "parametre-textedoc");

		private String	nomChamp;
		private String	nomParam;

		ParametrePonderation(String pNomChamp, String pNomParam) {
			nomChamp = pNomChamp;
			nomParam = pNomParam;
		}

		/**
		 * Renvoie les pondérations sur l'ensemble des champs du dossier.
		 * 
		 * @param session
		 * @return
		 * @throws ClientException
		 */
		static List<String> toStringAllDossier(CoreSession session) throws ClientException {
			ArrayList<String> list = new ArrayList<String>();

			for (ParametrePonderation paramPonderation : values()) {
				if (paramPonderation.name().startsWith("DOSSIER_")) {
					String str = paramPonderation.toString(session);
					if (str != null) {
						list.add(str);
					}
				}
			}

			return list;
		}

		String toString(CoreSession session) throws ClientException {
			ElasticParametrageService elasticService = SolonEpgServiceLocator.getElasticParametrageService();

			String paramValue = elasticService.getParametreValue(session, nomParam);
			float poids = 1.0f;
			if (paramValue != null) {
				poids = Float.parseFloat(paramValue);
			}

			return nomChamp + "^" + poids;
		}
	}
}
