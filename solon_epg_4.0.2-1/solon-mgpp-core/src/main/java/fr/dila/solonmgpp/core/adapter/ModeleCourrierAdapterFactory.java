package fr.dila.solonmgpp.core.adapter;

import fr.dila.solonmgpp.api.constant.MgppDocTypeConstants;
import fr.dila.solonmgpp.core.domain.ModeleCourrierImpl;
import fr.sword.naiad.nuxeo.commons.core.adapter.AbstractAdapterFactoryOnType;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ModeleCourrierAdapterFactory extends AbstractAdapterFactoryOnType {

    public ModeleCourrierAdapterFactory() {
        super(MgppDocTypeConstants.MODELE_COURRIER_TYPE);
    }

    @Override
    protected Object adapt(DocumentModel document, Class<?> clazz) {
        return new ModeleCourrierImpl(document);
    }
}
