package fr.dila.solonmgpp.api.descriptor;

import java.util.Map;

/**
 * Interface de description des metadonnees d'un evenement
 * @author asatre
 *
 */
public interface EvenementMetaDonneesDescriptor {

	void setProperty(Map<String, PropertyDescriptor> property);

	Map<String, PropertyDescriptor> getProperty();
	
}
