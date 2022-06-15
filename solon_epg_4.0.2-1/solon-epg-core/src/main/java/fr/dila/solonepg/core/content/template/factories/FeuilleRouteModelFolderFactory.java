package fr.dila.solonepg.core.content.template.factories;

import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.content.template.factories.SimpleTemplateBasedFactory;
import org.nuxeo.ecm.platform.content.template.service.TemplateItemDescriptor;

/**
 * Fabrique du répertoire racine des modèles de feuilles de route.
 *
 * @author jtremeaux
 */
public class FeuilleRouteModelFolderFactory extends SimpleTemplateBasedFactory {

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
            newChild = session.createDocument(newChild);
            setAcl(item.getAcl(), newChild.getRef());

            // Valide le modèle de feuille de route
            FeuilleRoute route = newChild.getAdapter(FeuilleRoute.class);
            route.setValidated(session);
        }
    }
}
