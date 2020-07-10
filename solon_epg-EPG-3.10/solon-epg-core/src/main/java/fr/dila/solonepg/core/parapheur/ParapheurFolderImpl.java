/**
 * 
 */
package fr.dila.solonepg.core.parapheur;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.ss.core.tree.SSTreeFolderImpl;
import fr.dila.st.core.util.UnrestrictedGetOrSetPropertyDocumentRunner;

/**
 * Implémentation des répertoires de parapheur.
 *
 * @author antoine Rolin
 *
 */
public class ParapheurFolderImpl extends SSTreeFolderImpl implements ParapheurFolder {

    private static final long serialVersionUID = -9055475069469959205L;

    protected boolean isFull = false;
    /**
     * @param document
     */
    public ParapheurFolderImpl(DocumentModel document) {
        super(document);
    }

    @Override
    public Boolean getEstNonVide() {
        return getBooleanProperty(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA, SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_EST_NON_VIDE_PROPERTY);
    }

    @Override
    public void setEstNonVide(Boolean estVide) {
        setProperty(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA, SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_EST_NON_VIDE_PROPERTY,estVide);
    }

    @Override
    public Long getNbDocAccepteMax() {
        return getLongProperty(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA, SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_NB_DOC_ACCEPTE_MAX_PROPERTY);
    }
    
    @Override
    public Long getNbDocAccepteMaxUnrestricted(CoreSession session) {
        Long  nbDocAccepteMax = null;
        try {
            nbDocAccepteMax =(Long) new UnrestrictedGetOrSetPropertyDocumentRunner(session,document).getProperty(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA, SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_NB_DOC_ACCEPTE_MAX_PROPERTY);
        } catch (ClientException e) {
            throw new RuntimeException("Erreur lors de de la récupération du nombre maximum de document accepte",e);
        }
        return nbDocAccepteMax;
    }

    @Override
    public void setNbDocAccepteMax(Long nbDocAccepteMax) {
        setProperty(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA, SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_NB_DOC_ACCEPTE_MAX_PROPERTY,nbDocAccepteMax);
    }

    @Override
    public List<Map<String, Serializable>> getFeuilleStyleFiles() {
        return getCollectionFileProperty(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA,SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_FEUILLE_STYLE_FILES_PROPERTY);
    }

    @Override
    public void setFeuilleStyleFiles(List<Map<String, Serializable>> feuilleStyleFiles) {
        setProperty(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA, SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_FEUILLE_STYLE_FILES_PROPERTY,feuilleStyleFiles);
    }

    @Override
    public List<String> getFormatsAutorises() {
        return getListStringProperty(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA, SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_FORMAT_AUTORISE_PROPERTY);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getFormatsAutorisesUnrestricted(CoreSession session) {
        List<String>  formatsAutorises = null;
        try {
            formatsAutorises = (List<String>) new UnrestrictedGetOrSetPropertyDocumentRunner(session,document).getProperty(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA, SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_FORMAT_AUTORISE_PROPERTY);
        } catch (ClientException e) {
            throw new RuntimeException("Erreur lors de de la récupération des formats autorises",e);
        }
        return formatsAutorises;
    }
    
    @Override
    public void setFormatsAutorises(List<String> formatsAutorises) {
        setProperty(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA, SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_FORMAT_AUTORISE_PROPERTY,formatsAutorises);
    }

    @Override 
    public boolean isFull() {
        return isFull;
    }
    
    public void setIsFull(boolean isFull) {
        this.isFull = isFull;
    }
}
