package fr.dila.solonepg.elastic.models.search.request;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = AggregationSerializer.class)
public class Aggregation implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TERMS = "terms";
	public static final String FIELD = "field";
	public static final String SIZE = "size";
	public static final String MIN_DOC_COUNT = "min_doc_count";
	public static final String SHARD_MIN_DOC_COUNT = "shard_min_doc_count";
	public static final String SHOW_TERM_DOC_COUNT_ERROR = "show_term_doc_count_error";
	public static final String ORDER = "order";

	private String field;
	private int size;
	private int minDocCount;
	private int shardMinDocCount;
	private boolean showTermDocCountError;
	private List<Map<String, String>> orders;

	public Aggregation(String field, int size, int minDocCount, int shardMinDocCount,
			boolean showTermDocCountError) {
		super();
		this.field = field;
		this.size = size;
		this.minDocCount = minDocCount;
		this.shardMinDocCount = shardMinDocCount;
		this.showTermDocCountError = showTermDocCountError;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getMinDocCount() {
		return minDocCount;
	}

	public void setMinDocCount(int minDocCount) {
		this.minDocCount = minDocCount;
	}

	public int getShardMinDocCount() {
		return shardMinDocCount;
	}

	public void setShardMinDocCount(int shardMinDocCount) {
		this.shardMinDocCount = shardMinDocCount;
	}

	public boolean isShowTermDocCountError() {
		return showTermDocCountError;
	}

	public void setShowTermDocCountError(boolean showTermDocCountError) {
		this.showTermDocCountError = showTermDocCountError;
	}

	public List<Map<String, String>> getOrders() {
		return orders;
	}

	public void setOrders(List<Map<String, String>> orders) {
		this.orders = orders;
	}

}
