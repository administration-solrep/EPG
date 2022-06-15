package fr.dila.solonmgpp.core.builder;

import fr.dila.solonmgpp.api.organigramme.InstitutionNode;

/**
 * Implementation de {@link InstitutionNode}
 * @author asatre
 *
 */
public class InstitutionNodeImpl implements InstitutionNode {
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
