package fr.dila.solonepg.core.content.template.factories;

import fr.dila.solonepg.api.service.FondDeDossierModelService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.content.template.factories.SimpleTemplateBasedFactory;
import org.nuxeo.ecm.platform.content.template.service.TemplateItemDescriptor;

/**
 * Factory permettant de créer les arborescences par défaut des modèles de fonds de dossier.
 *
 * @author antoine Rolin
 */
public class FondDossierFolderTemplateBasedFactory extends SimpleTemplateBasedFactory {

    //création des modèles de fond de dossier
    @Override
    public void createContentStructure(DocumentModel eventDoc) {
        initSession(eventDoc);

        if (eventDoc.isVersion() || !isTargetEmpty(eventDoc)) {
            return;
        }

        setAcl(acl, eventDoc.getRef());

        //parcours de tous les modèle de fond de dossier
        for (TemplateItemDescriptor item : template) {
            //création des propriétés simples (titre, chemin et nom)
            StringBuilder itemPath = new StringBuilder(eventDoc.getPathAsString());
            if (item.getPath() != null) {
                itemPath.append("/").append(item.getPath());
            }
            DocumentModel newChild = session.createDocumentModel(itemPath.toString(), item.getId(), item.getTypeName());
            DublincoreSchemaUtils.setTitle(newChild, item.getTitle());
            //création propriété spécifique (type acte)
            setProperties(item.getProperties(), newChild);
            newChild = session.createDocument(newChild);
            setAcl(item.getAcl(), newChild.getRef());

            //import fond de dossier model Service
            final FondDeDossierModelService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierModelService();

            //create the repository
            fondDeDossierService.createFondDeDossierDefaultRepository(newChild, session);
        }
    }
}
