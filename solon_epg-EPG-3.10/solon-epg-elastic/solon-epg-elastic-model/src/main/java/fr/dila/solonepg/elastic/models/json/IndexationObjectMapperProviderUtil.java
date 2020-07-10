package fr.dila.solonepg.elastic.models.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class IndexationObjectMapperProviderUtil {

	/**
	 * Sérialiseur pour les dossiers à indexer
	 */
	public static ObjectMapper newIndexationDossierInstance() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.CLOSE_CLOSEABLE, true);
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, true);
		objectMapper.configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, true);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, false);

		registerDateWorkaroundModule(objectMapper);
		return objectMapper;
	}

	/**
	 * Sérialiseur/désérialiseur pour les la communication elasticsearch
	 */
	public static ObjectMapper newElasticsearchQueryResponseInstance() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		registerDateWorkaroundModule(objectMapper);

		return objectMapper;
	}

	private static void registerDateWorkaroundModule(ObjectMapper objectMapper) {
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Date.class, new CustomDateDeserializer());
		objectMapper.registerModule(module);
	}

}
