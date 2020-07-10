/**
 * 
 */
package fr.dila.solonepg.core.fondedossier;

import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModelRoot;
import fr.dila.st.core.domain.STDomainObjectImpl;

/**
 * @author antoine Rolin
 *
 */
public class FondDeDossierModelRootImpl extends STDomainObjectImpl implements FondDeDossierModelRoot {
    
    private static final long serialVersionUID = -7008135879083984533L;

    /**
     * @param document
     */
    public FondDeDossierModelRootImpl(DocumentModel document) {
        super(document);
    }

    
    /**
     * Getter/Setter
     */

    @Override
    public List<String> getFormatsAutorises() {
        return getListStringProperty(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_ROOT_DOCUMENT_SCHEMA, SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_ROOT_FORMAT_AUTORISE_PROPERTY);
    }

    @Override
    public void setFormatsAutorises(List<String> formatsAutorises) {
        setProperty(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_ROOT_DOCUMENT_SCHEMA, SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_ROOT_FORMAT_AUTORISE_PROPERTY, formatsAutorises);
    }

}
