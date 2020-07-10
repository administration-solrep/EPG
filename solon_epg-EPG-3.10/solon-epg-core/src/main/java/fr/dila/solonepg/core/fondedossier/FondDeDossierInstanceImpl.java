package fr.dila.solonepg.core.fondedossier;

import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierInstance;
import fr.dila.st.core.util.PropertyUtil;


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
        return PropertyUtil.getStringListProperty(document,SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_DOCUMENT_SCHEMA, SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FORMAT_AUTORISE_PROPERTY);
    }

    @Override
    public void setFormatsAutorises(List<String> formatsAutorises) {
        setProperty(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_DOCUMENT_SCHEMA, SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FORMAT_AUTORISE_PROPERTY, formatsAutorises);
    }

}
