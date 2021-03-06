package fr.dila.solonmgpp.core.descriptor;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.common.xmap.annotation.XNodeMap;
import org.nuxeo.common.xmap.annotation.XObject;

import fr.dila.solonmgpp.api.descriptor.EvenementMetaDonneesDescriptor;
import fr.dila.solonmgpp.api.descriptor.PropertyDescriptor;

/**
 * Descripteur des metadonnées d'un evenement.
 * 
 * @author asatre
 */
@XObject("evenementMetadonnees")
public class EvenementMetaDonneesDescriptorImpl implements EvenementMetaDonneesDescriptor {

    /**
     * Propriétés qui peuvent / doivent être fournies pour ce type d'evenement.
     */
    @XNodeMap(value = "property", key = "@name", type = HashMap.class, componentType = PropertyDescriptorImpl.class)
    private Map<String, PropertyDescriptor> property;
    
    /**
     * Constructeur par défaut de VersionSchemaDescriptor.
     */
    public EvenementMetaDonneesDescriptorImpl() {

    }

	@Override
	public void setProperty(Map<String, PropertyDescriptor> property) {
		this.property = property;
	}

	@Override
	public Map<String, PropertyDescriptor> getProperty() {
		return property;
	}

}
