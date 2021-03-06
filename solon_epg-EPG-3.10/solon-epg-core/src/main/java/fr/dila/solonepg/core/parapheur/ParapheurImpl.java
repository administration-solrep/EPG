package fr.dila.solonepg.core.parapheur;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.parapheur.Parapheur;
import fr.dila.st.core.domain.STDomainObjectImpl;

public class ParapheurImpl extends STDomainObjectImpl implements Parapheur {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -596717968875393240L;

    public ParapheurImpl(DocumentModel docModel) {
        super(docModel);
    }
}
