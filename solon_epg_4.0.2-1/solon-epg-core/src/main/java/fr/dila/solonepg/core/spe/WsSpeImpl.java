package fr.dila.solonepg.core.spe;

import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.spe.WsSpe;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import org.nuxeo.ecm.core.api.DocumentModel;

public class WsSpeImpl implements WsSpe {
    private static final long serialVersionUID = 1L;

    protected DocumentModel document;

    public WsSpeImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public DocumentModel getDocument() {
        return this.document;
    }

    @Override
    public void setDocument(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public String getPosteId() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.SPE_SCHEMA,
            SolonEpgSchemaConstant.SPE_POSTE_ID_PROPERTY
        );
    }

    @Override
    public void setPosteId(String posteId) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.SPE_SCHEMA,
            SolonEpgSchemaConstant.SPE_POSTE_ID_PROPERTY,
            posteId
        );
    }

    @Override
    public String getWebservice() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.SPE_SCHEMA,
            SolonEpgSchemaConstant.SPE_WEBSERVICE_PROPERTY
        );
    }

    @Override
    public void setWebservice(String webservice) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.SPE_SCHEMA,
            SolonEpgSchemaConstant.SPE_WEBSERVICE_PROPERTY,
            webservice
        );
    }

    @Override
    public int getNbEssais() {
        return PropertyUtil
            .getLongProperty(document, SolonEpgSchemaConstant.SPE_SCHEMA, SolonEpgSchemaConstant.SPE_NB_ESSAIS_PROPERTY)
            .intValue();
    }

    @Override
    public void setNbEssais(int nbEssais) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.SPE_SCHEMA,
            SolonEpgSchemaConstant.SPE_NB_ESSAIS_PROPERTY,
            nbEssais
        );
    }

    @Override
    public String getIdDossier() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.SPE_SCHEMA,
            SolonEpgSchemaConstant.SPE_ID_DOSSIER_PROPERTY
        );
    }

    @Override
    public void setIdDossier(String idDossier) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.SPE_SCHEMA,
            SolonEpgSchemaConstant.SPE_ID_DOSSIER_PROPERTY,
            idDossier
        );
    }

    @Override
    public String getTypePublication() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.SPE_SCHEMA,
            SolonEpgSchemaConstant.SPE_TYPE_PUBLICATION_PROPERTY
        );
    }

    @Override
    public void setTypePublication(String typePublication) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.SPE_SCHEMA,
            SolonEpgSchemaConstant.SPE_TYPE_PUBLICATION_PROPERTY,
            typePublication
        );
    }
}
