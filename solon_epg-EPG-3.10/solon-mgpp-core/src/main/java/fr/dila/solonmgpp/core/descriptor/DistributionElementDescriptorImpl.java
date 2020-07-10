package fr.dila.solonmgpp.core.descriptor;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeMap;
import org.nuxeo.common.xmap.annotation.XObject;

import fr.dila.solonmgpp.api.descriptor.DistributionElementDescriptor;

/**
 * Implementation de {@link DistributionElementDescriptor}
 * 
 * @author asatre
 */
@XObject
public class DistributionElementDescriptorImpl implements DistributionElementDescriptor{

    /**
     * Ensemble des institutions autorisées.
     */
    @XNodeMap(value = "institution", key = "@name", type = HashMap.class, componentType = String.class)
    private Map<String, String> institution;

    /**
     * Si vrai, cette institution doit être renseignée.
     */
    @XNode("@obligatoire")
    private boolean obligatoire;

    @Override
    public Map<String, String> getInstitution() {
        return institution;
    }

    @Override
    public void setInstitution(Map<String, String> institution) {
        this.institution = institution;
    }

    @Override
    public boolean isObligatoire() {
        return obligatoire;
    }

    @Override
    public void setObligatoire(boolean obligatoire) {
        this.obligatoire = obligatoire;
    }
}
