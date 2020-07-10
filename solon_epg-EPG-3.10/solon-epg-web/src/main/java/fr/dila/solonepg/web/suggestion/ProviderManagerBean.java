package fr.dila.solonepg.web.suggestion;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * Une classe pour regrouper les classes de suggestion provider.
 * Sans doute inutile avec plus de connaissance seam/jsf.
 * 
 * @author jgz
 *
 */
@Name("providerManager")
@Scope(ScopeType.CONVERSATION)
public class ProviderManagerBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@In(create = true, required = true)
	protected ISuggestionProvider nomAuteurProvider;
	@In(create = true, required = true)
	protected ISuggestionProvider indexAssembleeProvider;
	@In(create = true, required = true)
	protected ISuggestionProvider indexMinistereProvider;
	@In(create = true, required = true)
	protected ISuggestionProvider ministereInterrogeProvider;
	private Map<String,Object>  provider_map;
	
	@PostConstruct
	public void initialize() {
		this.provider_map =  new HashMap<String,Object>();
	}
	
	@Factory("suggestionProviderMap")
	public Map<String,Object> createSuggestionProviderMap() {
		this.register(nomAuteurProvider);
		this.register(indexAssembleeProvider);
		this.register(indexMinistereProvider);
		this.register(ministereInterrogeProvider);
		Map<String,Object> resultMap =  new HashMap<String,Object>();
		resultMap.putAll(provider_map);
		
		return resultMap;
	}
	
	public void register(ISuggestionProvider provider)
	{
		provider_map.put(provider.getName(),provider);
	}

}
