package fr.dila.solonepg.core.content.template.factories;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.content.template.factories.SimpleTemplateBasedFactory;
import org.nuxeo.ecm.platform.content.template.service.TemplateItemDescriptor;

import fr.dila.solonepg.api.constant.SolonEpgProfilUtilisateurConstants;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.schema.DublincoreSchemaUtils;

/**
 * Factory permettant d'initialiser les types complexes du document profil
 * utilisateur.
 * 
 * @author antoine Rolin
 */
public class WorkspaceTemplateBasedFactory extends SimpleTemplateBasedFactory {

	@Override
	public void createContentStructure(DocumentModel eventDoc) throws ClientException {
		initSession(eventDoc);

		if (eventDoc.isVersion() || !isTargetEmpty(eventDoc)) {
			return;
		}

		setAcl(acl, eventDoc.getRef());

		for (TemplateItemDescriptor item : template) {
			StringBuilder itemPath = new StringBuilder(eventDoc.getPathAsString());
			if (item.getPath() != null) {
				itemPath.append("/").append(item.getPath());
			}
			DocumentModel newChild = session.createDocumentModel(itemPath.toString(), item.getId(), item.getTypeName());
			DublincoreSchemaUtils.setTitle(newChild, item.getTitle());
			DublincoreSchemaUtils.setDescription(newChild, item.getDescription());
			setProperties(item.getProperties(), newChild);
			// on définit les propriétés par défaut de type complexe
			if (item.getTypeName().equals(SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_DOCUMENT_TYPE)) {
				ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator
						.getProfilUtilisateurService();
				newChild = profilUtilisateurService.initDefaultAvailablesColumnsNames(newChild, session);
				newChild = profilUtilisateurService.initDefaultvalues(newChild, session);
			}
			newChild = session.createDocument(newChild);
			setAcl(item.getAcl(), newChild.getRef());
		}
	}

}
