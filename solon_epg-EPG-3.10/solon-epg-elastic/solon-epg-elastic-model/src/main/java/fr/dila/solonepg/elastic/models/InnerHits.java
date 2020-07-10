package fr.dila.solonepg.elastic.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InnerHits {

	public static final String		DOCUMENTS	= "documents";

	@JsonProperty(DOCUMENTS)
	private ElasticInnerDocument	documents;

	public ElasticInnerDocument getDocuments() {
		return documents;
	}

	public void setDocuments(ElasticInnerDocument documents) {
		this.documents = documents;
	}

	// INNER CLASSES

	static public class ElasticInnerDocument {

		public static final String	HITS	= "hits";

		@JsonProperty(HITS)
		private ElasticInnerHits	hits;

		public ElasticInnerHits getHits() {
			return hits;
		}

		public void setHits(ElasticInnerHits hits) {
			this.hits = hits;
		}

	}

	static public class ElasticInnerHits implements Serializable {

		private static final long		serialVersionUID	= 6853423885546800116L;

		public static final String		TOTAL				= "total";
		public static final String		MAX_SCORE			= "max_score";
		public static final String		HITS				= "hits";

		@JsonProperty(TOTAL)
		private Long					total;

		@JsonProperty(MAX_SCORE)
		private Long					max_score;

		@JsonProperty(HITS)
		private List<ElasticInnerHit>	hits;

		public Long getTotal() {
			return total;
		}

		public void setTotal(Long total) {
			this.total = total;
		}

		public Long getMax_score() {
			return max_score;
		}

		public void setMax_score(Long max_score) {
			this.max_score = max_score;
		}

		public List<ElasticInnerHit> getHits() {
			return hits;
		}

		public void setHits(List<ElasticInnerHit> hits) {
			this.hits = hits;
		}

	}

	static public class ElasticInnerHit implements Serializable {

		private static final long	serialVersionUID	= -667527154984411616L;

		public static final String	NESTED				= "_nested";
		public static final String	SCORE				= "_score";
		public static final String	FIELDS				= "fields";

		@JsonProperty(NESTED)
		private InnerNested			nested;

		@JsonProperty(SCORE)
		private BigDecimal			score;

		@JsonProperty(FIELDS)
		private InnerFields			fields;

		public InnerNested getNested() {
			return nested;
		}

		public void setNested(InnerNested nested) {
			this.nested = nested;
		}

		public BigDecimal getScore() {
			return score;
		}

		public void setScore(BigDecimal score) {
			this.score = score;
		}

		public InnerFields getFields() {
			return fields;
		}

		public void setFields(InnerFields fields) {
			this.fields = fields;
		}

	}

	static public class InnerNested implements Serializable {

		private static final long	serialVersionUID	= 1L;

		public static final String	OFFSET				= "offset";
		public static final String	FIELD				= "field";

		@JsonProperty(OFFSET)
		private Long				offset;

		@JsonProperty(FIELD)
		private String				field;

		public Long getOffset() {
			return offset;
		}

		public void setOffset(Long offset) {
			this.offset = offset;
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}
	}

	static public class InnerFields implements Serializable {

		private static final long	serialVersionUID	= 1L;

		public static final String	IDS					= ElasticDossier.DOCUMENTS + "." + ElasticDocument.UID;

		@JsonProperty(IDS)
		private List<String>		ids;

		public List<String> getIds() {
			return ids;
		}

		public void setIds(List<String> ids) {
			this.ids = ids;
		}
	}

}
