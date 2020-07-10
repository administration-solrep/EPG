package fr.dila.solonepg.core.archive;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.common.utils.Path;
import org.nuxeo.common.utils.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentLocation;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.io.DocumentTranslationMap;
import org.nuxeo.ecm.core.io.ExportedDocument;
import org.nuxeo.ecm.core.io.impl.AbstractDocumentWriter;
import org.nuxeo.ecm.core.io.impl.DocumentTranslationMapImpl;

import fr.dila.solonepg.api.cases.Dossier;

public class SolonEpgZipWriter extends AbstractDocumentWriter {

    protected ZipOutputStream out;
    protected CoreSession session;
    
    public SolonEpgZipWriter(CoreSession session, ZipOutputStream out) {
        this.out = out;
        this.session = session;
    }

    @Override
    public DocumentTranslationMap write(ExportedDocument doc) throws IOException {
        Path path = doc.getPath();
        DocumentModel dossierDoc = null;
        try {
            dossierDoc = session.getDocument(new PathRef(path.uptoSegment(6).toString()));
        } catch (ClientException e) {
            throw new IOException(e);
        }
        path = path.removeFirstSegments(7);
        //TODO put in constant
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        String nomDossier = "Dossier_" + dossier.getNumeroNor();
        
        List<String> pathList = new ArrayList<String>();
        pathList.add(nomDossier);
        pathList.addAll(Arrays.asList(path.segments()));
        String[] segments = new String[pathList.size()];
        pathList.toArray(segments);
        path = Path.createFromSegments(segments);
        
        writeDocument(path.toString(), doc);
        // keep location unchanged
        DocumentLocation oldLoc = doc.getSourceLocation();
        String oldServerName = oldLoc.getServerName();
        DocumentRef oldDocRef = oldLoc.getDocRef();
        DocumentTranslationMap map = new DocumentTranslationMapImpl(oldServerName, oldServerName);
        map.put(oldDocRef, oldDocRef);
        return map;
    }

    private void writeDocument(String path, ExportedDocument doc) throws IOException {
        //nettoyage du chemin
        if (path.charAt(0) == '/') {
            path = path.replaceFirst("/", "");
        }
        path = StringUtils.toAscii(path);
        
        //cas d'un document avec un fichier blob, pas de sous dossier
        if (doc.getBlobs().size() == 0) {
            path += '/';
            ZipEntry entry = new ZipEntry(path);
            short filesCount = (short) doc.getFilesCount();
            // On renseigne la data "extra" du zip avec le nombre de files dans l'export
            // Alloue un tableau de 2 byte pour renseigner la data
            byte[] extra = ByteBuffer.allocate(2).putShort(filesCount).array();
            entry.setExtra(extra);
            out.putNextEntry(entry);
            out.closeEntry();
        }

        ZipEntry entry = null;
        
        // write blobs
        Map<String, Blob> blobs = doc.getBlobs();
        for (Map.Entry<String, Blob> blobEntry : blobs.entrySet()) {
            Blob blobToChange = blobEntry.getValue();
            
            entry = new ZipEntry(path);
            out.putNextEntry(entry);
            InputStream inStream = null;
            try {
                inStream = blobToChange.getStream();
                FileUtils.copy(inStream, out);
            } finally {
                if (inStream != null) {
                    inStream.close();
                }
                out.closeEntry();
            }
        }
    }

    public void close() {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                // do nothing
            } finally {
                out = null;
            }
        }
    }
}