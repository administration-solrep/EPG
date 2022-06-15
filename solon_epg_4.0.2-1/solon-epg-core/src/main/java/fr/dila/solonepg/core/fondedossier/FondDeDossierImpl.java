package fr.dila.solonepg.core.fondedossier;

import fr.dila.solonepg.api.fonddossier.FondDeDossier;
import fr.dila.st.core.domain.STDomainObjectImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class FondDeDossierImpl extends STDomainObjectImpl implements FondDeDossier {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 5717864451984688836L;

    public FondDeDossierImpl(DocumentModel doc) {
        super(doc);
    }
}
