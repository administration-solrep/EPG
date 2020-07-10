package fr.dila.solonepg.web.rest;

import static org.jboss.seam.ScopeType.EVENT;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.VersioningOption;
import org.nuxeo.ecm.core.api.impl.blob.StreamingBlob;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.schema.FacetNames;
import org.nuxeo.ecm.core.versioning.VersioningService;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.service.STServiceLocator;

@Name("uploadFileRestlet")
@Scope(EVENT)
@Install(precedence = Install.DEPLOYMENT + 1)
public class UploadFileRestlet extends org.nuxeo.ecm.platform.ui.web.restAPI.UploadFileRestlet {

    private static final long serialVersionUID = -4195856678916127715L;

    @Override
    protected void saveFileToDocument(String filename, DocumentModel dm, String blobPropertyName, String filenamePropertyName, InputStream is)
            throws IOException, PropertyException, ClientException {
        // force remove lock
        STServiceLocator.getSTLockService().unlockDocUnrestricted(getDocumentManager(), dm);

        Blob blob = StreamingBlob.createFromStream(is).persist();
        blob.setFilename(filename);

        dm.setPropertyValue(blobPropertyName, (Serializable) blob);
        dm.setPropertyValue(filenamePropertyName, filename);

        if (dm.hasFacet(FacetNames.VERSIONABLE)) {
            // force le verionning si versionable
            dm.putContextData(VersioningService.VERSIONING_OPTION, VersioningOption.MAJOR);
        }

        getDocumentManager().saveDocument(dm);

        getDocumentManager().save();

        if (SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE.equals(dm.getType())
                || SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE.equals(dm.getType())) {
            // journalisation de l'action dans les logs
            SolonEpgServiceLocator.getTreeService().logAction(dm, getDocumentManager(), filename);
        }
    }
}
