package fr.dila.solonepg.core.operation.livraison;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.st.core.util.FileUtils;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.platform.filemanager.api.FileImporterContext;
import org.nuxeo.ecm.platform.filemanager.api.FileManager;
import org.nuxeo.runtime.api.Framework;

/**
 * Opération permettant d'importer une archive créée par l'export Nuxeo.
 *
 * @author jgomez
 */
@Operation(
    id = ImportOperation.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "Import une archive dans le dépôt Nuxeo",
    description = ImportOperation.DESCRIPTION
)
public class ImportOperation {
    /**
     * Identifiant technique de l'opération.
     */
    public static final String ID = "SolonEpg.Livraison.Import";

    public static final String DESCRIPTION = "Cette opération importe une archive Nuxeo dans le dépôt";

    @Context
    protected OperationContext context;

    @Context
    protected CoreSession session;

    @Param(name = "localSourcePath")
    protected String localSourcePath;

    @Param(name = "targetRepoPath")
    protected String targetRepoPath;

    private static final Log LOGGER = LogFactory.getLog(ImportOperation.class);

    /**
     * Importe les documentModel du fichier export.zip dans le document situé dans 'nuxeoRootpath'.
     *
     * @param filePath
     *            chemin du fichier export.zip
     * @param nuxeoRootpath
     *            path nuxeo dans lequel on va créer les documentModels
     */
    public void importDocument(String filePath, String nuxeoRootpath) {
        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Import de l'archive de : " + localSourcePath + " vers " + targetRepoPath);
            }
            File file = new File(filePath);
            String fileName = FileUtils.sanitizePathTraversal(filePath);
            Blob blob = new FileBlob(file);
            FileImporterContext context = FileImporterContext
                .builder(session, blob, nuxeoRootpath)
                .overwrite(true)
                .fileName(fileName)
                .build();
            FileManager fileManager = Framework.getService(FileManager.class);
            fileManager.createOrUpdateDocument(context);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Fin de l'import de l'archive");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new NuxeoException(e.toString());
        }
    }

    @OperationMethod
    public void importArchive() {
        importDocument(localSourcePath, targetRepoPath);
    }
}
