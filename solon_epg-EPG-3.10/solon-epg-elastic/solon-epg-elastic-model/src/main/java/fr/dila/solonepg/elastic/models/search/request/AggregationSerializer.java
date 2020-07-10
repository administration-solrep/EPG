package fr.dila.solonepg.elastic.models.search.request;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class AggregationSerializer extends JsonSerializer<Aggregation> {

	@Override
	public void serialize(Aggregation agg, JsonGenerator jgen, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		jgen.writeStartObject();

		jgen.writeFieldName(Aggregation.TERMS);

		jgen.writeStartObject();

		jgen.writeObjectField(Aggregation.FIELD, agg.getField());
		jgen.writeObjectField(Aggregation.SIZE, agg.getSize());
		jgen.writeObjectField(Aggregation.MIN_DOC_COUNT, agg.getMinDocCount());
		jgen.writeObjectField(Aggregation.SHARD_MIN_DOC_COUNT, agg.getShardMinDocCount());
		jgen.writeObjectField(Aggregation.SHOW_TERM_DOC_COUNT_ERROR, agg.isShowTermDocCountError());
		jgen.writeObjectField(Aggregation.ORDER, agg.getOrders());

		jgen.writeEndObject();

		jgen.writeEndObject();
	}

}
