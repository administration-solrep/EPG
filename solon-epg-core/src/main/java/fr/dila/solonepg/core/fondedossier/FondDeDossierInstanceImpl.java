package fr.dila.solonepg.core.fondedossier;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierInstance;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public class FondDeDossierInstanceImpl extends FondDeDossierImpl implements FondDeDossierInstance {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -4453400489729234323L;

    public FondDeDossierInstanceImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public List<String> getFormatsAutorises() {
        return PropertyUtil.getStringListProperty(
            document,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_DOCUMENT_SCHEMA,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FORMAT_AUTORISE_PROPERTY
        );
    }

    @Override
    public void setFormatsAutorises(List<String> formatsAutorises) {
        setProperty(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_DOCUMENT_SCHEMA,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FORMAT_AUTORISE_PROPERTY,
            formatsAutorises
        );
    }
}
