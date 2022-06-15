package fr.dila.solon.birt.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializationUtils {
	/**
	 * Effectue la serialization des paramètres pour les adresser à Birt.
	 * 
	 * @param parameters
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String serialize(SolonBirtParameters parameters) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(parameters);
	}

	/**
	 * Effectue la deserialization d'un objet String et retourne un objet
	 * SolonBirtParameters.
	 * 
	 * @param parametersStr
	 * @return
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public static SolonBirtParameters deserialize(String parametersStr)
			throws JsonMappingException, JsonProcessingException {
		return new ObjectMapper().readValue(parametersStr, SolonBirtParameters.class);
	}
}
