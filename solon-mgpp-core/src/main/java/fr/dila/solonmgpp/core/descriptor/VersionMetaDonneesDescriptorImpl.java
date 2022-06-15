package fr.dila.solonmgpp.core.descriptor;

import fr.dila.solonmgpp.api.descriptor.VersionMetaDonneesDescriptor;
import fr.dila.st.api.descriptor.parlement.PropertyDescriptor;
import fr.dila.st.core.descriptor.parlement.PropertyDescriptorImpl;
import java.util.HashMap;
import java.util.Map;
import org.nuxeo.common.xmap.annotation.XNodeMap;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * Descripteur des métadonnées d'une version.
 *
 * @author asatre
 */
@XObject("versionMetadonnees")
public class VersionMetaDonneesDescriptorImpl implements VersionMetaDonneesDescriptor {
    /**
     * Propriétés qui peuvent / doivent être fournies pour ce type de version.
     */
    @XNodeMap(value = "property", key = "@name", type = HashMap.class, componentType = PropertyDescriptorImpl.class)
    private Map<String, PropertyDescriptor> property;

    /**
     * Constructeur par défaut de VersionSchemaDescriptor.
     */
    public VersionMetaDonneesDescriptorImpl() {}

    @Override
    public void setProperty(Map<String, PropertyDescriptor> property) {
        this.property = property;
    }

    @Override
    public Map<String, PropertyDescriptor> getProperty() {
        return property;
    }
}
