package fr.dila.solonepg.core.parapheur;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.parapheur.ParapheurModel;
import fr.dila.st.core.domain.STDomainObjectImpl;

public class ParapheurModelImpl extends STDomainObjectImpl implements ParapheurModel {

    /**
     * Serial UID
     */
    private static final long serialVersionUID = -3096382478514080717L;

    public ParapheurModelImpl(DocumentModel docModel) {
        super(docModel);
    }

    @Override
    public String getTypeActe() {
        return getStringProperty(SolonEpgParapheurConstants.PARAPHEUR_MODEL_DOCUMENT_SCHEMA, SolonEpgParapheurConstants.PARAPHEUR_MODEL_TYPE_ACTE_PROPERTY);
    }

    @Override
    public void setTypeActe(String typeActe) {
        setProperty(SolonEpgParapheurConstants.PARAPHEUR_MODEL_DOCUMENT_SCHEMA, SolonEpgParapheurConstants.PARAPHEUR_MODEL_TYPE_ACTE_PROPERTY, typeActe);
    }
}
