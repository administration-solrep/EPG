package fr.dila.solonepg.core.cases;

import fr.dila.solonepg.api.cases.DossiersSignales;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.st.core.domain.STDomainObjectImpl;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public class DossiersSignalesImpl extends STDomainObjectImpl implements DossiersSignales {
    private static final long serialVersionUID = -8174534597540786374L;

    public DossiersSignalesImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public List<String> getDossiersIds() {
        return getListStringProperty(
            DossierSolonEpgConstants.DOSSIERS_SIGNALES_SCHEMA,
            DossiersSignales.DOSSIERS_SIGNALES_DOSSIERS_IDS_PROPERTY
        );
    }

    @Override
    public void setDossiersIds(List<String> dossiersId) {
        setProperty(
            DossierSolonEpgConstants.DOSSIERS_SIGNALES_SCHEMA,
            DossiersSignales.DOSSIERS_SIGNALES_DOSSIERS_IDS_PROPERTY,
            dossiersId
        );
    }
}
