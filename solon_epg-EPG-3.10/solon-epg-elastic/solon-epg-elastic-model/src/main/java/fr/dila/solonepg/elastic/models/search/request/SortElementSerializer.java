package fr.dila.solonepg.elastic.models.search.request;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SortElementSerializer extends JsonSerializer<SortElement> {

	@Override
	public void serialize(SortElement se, JsonGenerator jgen, SerializerProvider arg2)
			throws IOException, JsonProcessingException {
		jgen.writeStartObject();

		jgen.writeFieldName(se.getType());

		jgen.writeStartObject();

		jgen.writeObjectField("order", se.isDesc() ? "desc" : "asc");

		jgen.writeEndObject();

		jgen.writeEndObject();
	}

}
