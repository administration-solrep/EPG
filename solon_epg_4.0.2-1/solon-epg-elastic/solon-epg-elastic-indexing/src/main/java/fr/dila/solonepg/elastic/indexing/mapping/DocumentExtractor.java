package fr.dila.solonepg.elastic.indexing.mapping;

import java.io.IOException;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.convert.api.ConversionException;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;

public class DocumentExtractor extends DefaultComponent implements IDocumentExtractor {
    private ConversionService conversionService;

    @Override
    public void activate(ComponentContext context) {
        conversionService = Framework.getService(ConversionService.class);
    }

    @Override
    public void deactivate(ComponentContext context) {
        conversionService = null;
    }

    @Override
    public String parseTextFromFileData(Blob blob) throws IOException, ConversionException {
        BlobHolder holder = new SimpleBlobHolder(blob);
        BlobHolder result = conversionService.convert("any2text", holder, null);
        return result.getBlob().getString();
    }
}
