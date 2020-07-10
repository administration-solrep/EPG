package fr.dila.solonepg.elastic.models.search.request;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

class TermClauseSerializer extends JsonSerializer<TermClause> {

	@Override
	public void serialize(TermClause tc, JsonGenerator jgen, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		jgen.writeStartObject();

		jgen.writeFieldName(TermClause.TERM);

		jgen.writeStartObject();

		jgen.writeFieldName(tc.getType());

		jgen.writeStartObject();
		jgen.writeObjectField(TermClause.VALUE, tc.getValue());
		jgen.writeObjectField(TermClause.BOOST, tc.getBoost());
		jgen.writeEndObject();

		jgen.writeEndObject();

		jgen.writeEndObject();
	}

}
