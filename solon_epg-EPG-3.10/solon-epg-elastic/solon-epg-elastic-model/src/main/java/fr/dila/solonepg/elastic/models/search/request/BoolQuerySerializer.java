package fr.dila.solonepg.elastic.models.search.request;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

class BoolQuerySerializer extends JsonSerializer<BoolQuery> {

	@Override
	public void serialize(BoolQuery bq, JsonGenerator jgen, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		jgen.writeStartObject();

		jgen.writeFieldName(BoolQuery.BOOL);

		jgen.writeStartObject();

		List<QueryClause> mustSubClauses = bq.getMustSubClauses();
		if (mustSubClauses != null) {
			jgen.writeObjectField(BoolQuery.MUST, mustSubClauses);
		}
		List<QueryClause> shouldSubClauses = bq.getShouldSubClauses();
		if (shouldSubClauses != null) {
			jgen.writeObjectField(BoolQuery.SHOULD, shouldSubClauses);
		}
		jgen.writeObjectField(BoolQuery.DISABLE_COORD, bq.getDisableCoord());
		jgen.writeObjectField(BoolQuery.ADJUST_PURE_NEGATIVE, bq.getAdjustPureNegative());
		jgen.writeObjectField(BoolQuery.BOOST, bq.getBoost());

		jgen.writeEndObject();

		jgen.writeEndObject();

	}

}
