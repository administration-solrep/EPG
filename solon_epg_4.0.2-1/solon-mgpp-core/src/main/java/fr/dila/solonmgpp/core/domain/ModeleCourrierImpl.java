package fr.dila.solonmgpp.core.domain;

import static fr.dila.solonmgpp.api.constant.ModeleCourrierSchemaConstants.SCHEMA_NAME;
import static fr.dila.solonmgpp.api.constant.ModeleCourrierSchemaConstants.TYPES_COMMUNICATION_PROP;

import fr.dila.solonmgpp.api.domain.ModeleCourrier;
import fr.dila.ss.core.tree.SSTreeFileImpl;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ModeleCourrierImpl extends SSTreeFileImpl implements ModeleCourrier {
    private static final long serialVersionUID = 1L;

    public ModeleCourrierImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public List<String> getTypesCommuncation() {
        return PropertyUtil.getStringListProperty(document, SCHEMA_NAME, TYPES_COMMUNICATION_PROP);
    }

    @Override
    public void setTypesCommuncation(List<String> value) {
        PropertyUtil.setProperty(document, SCHEMA_NAME, TYPES_COMMUNICATION_PROP, value);
    }
}
