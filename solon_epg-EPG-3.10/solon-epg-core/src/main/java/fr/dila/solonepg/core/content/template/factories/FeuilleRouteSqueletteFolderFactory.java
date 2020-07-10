package fr.dila.solonepg.core.content.template.factories;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.content.template.factories.SimpleTemplateBasedFactory;
import org.nuxeo.ecm.platform.content.template.service.TemplateItemDescriptor;

import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.st.core.schema.DublincoreSchemaUtils;

/**
 * Fabrique du répertoire racine des squelettes de feuilles de route.
 */
public class FeuilleRouteSqueletteFolderFactory extends SimpleTemplateBasedFactory {

	@Override
	public void createContentStructure(DocumentModel doc) throws ClientException {
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

			// Valide le squelette de feuille de route
			DocumentRoute route = newChild.getAdapter(DocumentRoute.class);
			route.setValidated(session);
		}
	}
}
