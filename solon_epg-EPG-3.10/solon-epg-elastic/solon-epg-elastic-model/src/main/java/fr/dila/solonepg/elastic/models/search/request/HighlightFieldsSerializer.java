package fr.dila.solonepg.elastic.models.search.request;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

class HighlightFieldsSerializer extends JsonSerializer<List<HighlightField>> {

	@Override
	public void serialize(List<HighlightField> hfl, JsonGenerator jgen, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		jgen.writeStartObject();

		for (HighlightField hf : hfl) {
			jgen.writeFieldName(hf.getType());
			jgen.writeStartObject();

			jgen.writeObjectField(HighlightField.FRAGMENT_SIZE, hf.getFragmentSize());
			jgen.writeObjectField(HighlightField.NUMBER_OF_FRAGMENTS, hf.getNumberOfFragments());

			jgen.writeEndObject();

		}

		jgen.writeEndObject();
	}

}
