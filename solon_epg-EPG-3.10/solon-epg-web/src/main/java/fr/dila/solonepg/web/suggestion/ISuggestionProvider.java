package fr.dila.solonepg.web.suggestion;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;

public interface ISuggestionProvider {

	public abstract String getName();

	public abstract List<String> getSuggestions(Object input)
			throws ClientException, Exception;

}