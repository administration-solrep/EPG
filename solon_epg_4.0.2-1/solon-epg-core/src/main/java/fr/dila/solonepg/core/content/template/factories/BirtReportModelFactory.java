package fr.dila.solonepg.core.content.template.factories;

import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.schema.FileSchemaUtils;
import fr.dila.st.core.util.BlobUtils;
import java.io.InputStream;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.content.template.factories.SimpleTemplateBasedFactory;
import org.nuxeo.ecm.platform.content.template.service.TemplateItemDescriptor;
import org.nuxeo.runtime.api.Framework;

public class BirtReportModelFactory extends SimpleTemplateBasedFactory {

    @Override
    public void createContentStructure(DocumentModel doc) {
        initSession(doc);

        if (doc.isVersion() || !isTargetEmpty(doc)) {
            return;
        }

        setAcl(acl, doc.getRef());

        for (TemplateItemDescriptor item : template) {
            StringBuilder itemPath = new StringBuilder(doc.getPathAsString());
            if (item.getPath() != null) {
                itemPath.append("/").append(item.getPath());
            }

            DocumentModel newChild = session.createDocumentModel(itemPath.toString(), item.getId(), item.getTypeName());

            DublincoreSchemaUtils.setTitle(newChild, item.getTitle());
            DublincoreSchemaUtils.setDescription(newChild, item.getDescription());
            setProperties(item.getProperties(), newChild);
            String reportName = (String) newChild.getProperty("birtreportmodel", "reportName");

            InputStream inputStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("birtReports" + "/" + reportName);
            if (inputStream == null) {
                inputStream = Framework.getResourceLoader().getResourceAsStream("birtReports" + "/" + reportName);
            }

            Blob blob = BlobUtils.createSerializableBlob(inputStream, reportName, null);
            FileSchemaUtils.setContent(newChild, blob);

            newChild = session.createDocument(newChild);
            setAcl(item.getAcl(), newChild.getRef());
        }
    }
}
