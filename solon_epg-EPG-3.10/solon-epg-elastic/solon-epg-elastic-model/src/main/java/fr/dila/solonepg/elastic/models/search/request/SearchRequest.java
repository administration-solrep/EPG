package fr.dila.solonepg.elastic.models.search.request;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Modèle pour une requête de recherche, transférable après sérialization à
 * ElasticSearch.
 * 
 * @author tlombard
 */
@JsonIgnoreProperties
public class SearchRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FROM = "from";
	public static final String SIZE = "size";
	public static final String SOURCE = "_source";
	public static final String QUERY = "query";
	public static final String STORED_FIELDS = "stored_fields";
	public static final String DOCVALUE_FIELDS = "docvalue_fields";
	public static final String AGGREGATIONS = "aggregations";
	public static final String HIGHLIGHT = "highlight";
	public static final String SORT = "sort";

	@JsonProperty(FROM)
	private Integer from;

	@JsonProperty(SIZE)
	private int size;

	@JsonProperty(QUERY)
	private BoolQuery query;

	/**
	 * Impacte les performances ; préférer {@link #docvalueFields} ou {@link #storedFields}.
	 * 
	 * On désactive par défaut.
	 */
	@JsonProperty(SOURCE)
	private Boolean source = false;

	/**
	 * Permet de récupérer les valeurs même sur un champ analysé.
	 * 
	 * Nécessite d'être activé sur le champ.
	 */
	@JsonProperty(STORED_FIELDS)
	private List<String> storedFields;

	/**
	 * Permet de récupérer les valeurs pour les types non analysés ; performance ++
	 * 
	 * Activé par défaut pour tous les champs.
	 * 
	 * A utiliser pour les champs non indexés.
	 */
	@JsonProperty(DOCVALUE_FIELDS)
	private List<String> docvalueFields;

	@JsonProperty(AGGREGATIONS)
	private Map<String, Aggregation> aggregations;

	@JsonProperty(HIGHLIGHT)
	private Highlight highlight;

	@JsonProperty(SORT)
	private List<SortElement> sort;

	public SearchRequest(Integer from, int size, List<String> storedFields, List<String> docvalueFields) {
		super();
		this.from = from;
		this.size = size;
		this.storedFields = storedFields;
		this.docvalueFields = docvalueFields;
	}

	public Integer getFrom() {
		return from;
	}

	public void setFrom(Integer from) {
		this.from = from;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<String> getStoredFields() {
		return storedFields;
	}

	public void setStoredFields(List<String> storedFields) {
		this.storedFields = storedFields;
	}

	public List<String> getDocvalueFields() {
		return docvalueFields;
	}

	public void setDocvalueFields(List<String> docvalueFields) {
		this.docvalueFields = docvalueFields;
	}

	public Highlight getHighlight() {
		return highlight;
	}

	public void setHighlight(Highlight highlight) {
		this.highlight = highlight;
	}

	public Boolean getSource() {
		return source;
	}

	public void setSource(Boolean source) {
		this.source = source;
	}

	public BoolQuery getQuery() {
		return query;
	}

	public void setQuery(BoolQuery query) {
		this.query = query;
	}

	public Map<String, Aggregation> getAggregations() {
		return aggregations;
	}

	public void setAggregations(Map<String, Aggregation> aggregations) {
		this.aggregations = aggregations;
	}

	public List<SortElement> getSort() {
		return sort;
	}

	public void setSort(List<SortElement> sort) {
		this.sort = sort;
	}

}
