package fr.dila.solonepg.elastic.models.search.request;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

class NestedClauseSerializer extends JsonSerializer<NestedClause> {
	@Override
	public void serialize(NestedClause nc, JsonGenerator jgen, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		jgen.writeStartObject();

		jgen.writeFieldName(NestedClause.NESTED);

		jgen.writeStartObject();

		jgen.writeObjectField(NestedClause.QUERY, nc.getQuery());
		jgen.writeObjectField(NestedClause.PATH, nc.getPath());
		jgen.writeObjectField(NestedClause.IGNORE_UNMAPPED, nc.isIgnoreUnmapped());
		jgen.writeObjectField(NestedClause.SCORE_MODE, nc.getScoreMode());
		jgen.writeObjectField(NestedClause.BOOST, nc.getBoost());
		jgen.writeObjectField(NestedClause.INNER_HITS, nc.getInnerHits());

		jgen.writeEndObject();

		jgen.writeEndObject();
	}
}
