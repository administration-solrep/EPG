package fr.dila.solonepg.elastic.models.search.request;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

class SimpleQueryStringSerializer extends JsonSerializer<SimpleQueryString> {

	@Override
	public void serialize(SimpleQueryString sqs, JsonGenerator jgen, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		jgen.writeStartObject();

		jgen.writeFieldName(SimpleQueryString.SIMPLE_QUERY_STRING);

		jgen.writeStartObject();

		jgen.writeObjectField(SimpleQueryString.QUERY, sqs.getQuery());
		jgen.writeObjectField(SimpleQueryString.FIELDS, sqs.getFields());
		jgen.writeObjectField(SimpleQueryString.FLAGS, sqs.getFlags());
		jgen.writeObjectField(SimpleQueryString.DEFAULT_OPERATOR, sqs.getDefaultOperator());
		jgen.writeObjectField(SimpleQueryString.LENIENT, sqs.isLenient());
		jgen.writeObjectField(SimpleQueryString.ANALYZE_WILDCARD, sqs.isAnalyzeWildcard());
		jgen.writeObjectField(SimpleQueryString.BOOST, sqs.getBoost());

		jgen.writeEndObject();

		jgen.writeEndObject();
	}

}
