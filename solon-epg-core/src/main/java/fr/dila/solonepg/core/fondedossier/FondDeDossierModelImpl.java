package fr.dila.solonepg.core.fondedossier;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModel;
import fr.dila.st.core.domain.STDomainObjectImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class FondDeDossierModelImpl extends STDomainObjectImpl implements FondDeDossierModel {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -6216105505631344306L;

    public FondDeDossierModelImpl(DocumentModel docModel) {
        super(docModel);
    }

    // getter /setter to fond de dossier elements

    @Override
    public String getTypeActe() {
        return getStringProperty(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_SCHEMA,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_TYPE_ACTE_PROPERTY
        );
    }

    @Override
    public void setTypeActe(String typeActe) {
        setProperty(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_SCHEMA,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_TYPE_ACTE_PROPERTY,
            typeActe
        );
    }
}
