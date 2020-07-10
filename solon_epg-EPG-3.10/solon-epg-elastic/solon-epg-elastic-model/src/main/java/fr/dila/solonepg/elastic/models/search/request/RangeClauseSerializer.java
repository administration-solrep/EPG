package fr.dila.solonepg.elastic.models.search.request;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import fr.dila.st.core.util.DateUtil;

class RangeClauseSerializer extends JsonSerializer<RangeClause> {

	@Override
	public void serialize(RangeClause rc, JsonGenerator jgen, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		jgen.writeStartObject();

		jgen.writeFieldName(RangeClause.RANGE);

		jgen.writeStartObject();

		jgen.writeFieldName(rc.getType());

		jgen.writeStartObject();
		jgen.writeObjectField(RangeClause.FROM, DateUtil.formatDatetimeISO8601(rc.getFrom()));
		jgen.writeObjectField(RangeClause.TO, DateUtil.formatDatetimeISO8601(rc.getTo()));
		jgen.writeObjectField(RangeClause.INCLUDE_LOWER, rc.isIncludeLower());
		jgen.writeObjectField(RangeClause.INCLUDE_UPPER, rc.isIncludeUpper());
		jgen.writeObjectField(RangeClause.BOOST, rc.getBoost());
		jgen.writeEndObject();

		jgen.writeEndObject();

		jgen.writeEndObject();
	}

}
