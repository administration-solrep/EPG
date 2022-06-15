package fr.dila.solonepg.core.content.template.factories;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParametrageApplicationConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModelRoot;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.content.template.factories.SimpleTemplateBasedFactory;
import org.nuxeo.ecm.platform.content.template.service.TemplateItemDescriptor;

/**
 * Factory permettant d'initaliser les types complexe par défaut de la racine des modèles de fonds de dossier et du du document paramètrage de l'applicaton.
 *
 * @author antoine Rolin
 */
public class AdminWorkspaceTemplateBasedFactory extends SimpleTemplateBasedFactory {

    @Override
    public void createContentStructure(DocumentModel eventDoc) {
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
            //on définit les propriétés par défaut de type complexe
            if (item.getTypeName().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_ROOT_DOCUMENT_TYPE)) {
                //on definit le formats de fichiers autorisés par défaut pour le fond de dossier ici
                FondDeDossierModelRoot fondDeDossierModelRoot = newChild.getAdapter(FondDeDossierModelRoot.class);
                fondDeDossierModelRoot.setFormatsAutorises(
                    DossierSolonEpgConstants.FORMAT_AUTORISE_LIST_EXTEND_FOLDER_FOND_DOSSIER
                );
            } else if (
                item.getTypeName().equals(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE)
            ) {
                ParametrageApplication parametrageApplication = newChild.getAdapter(ParametrageApplication.class);
                parametrageApplication.setMetadonneDisponibleColonneCorbeille(
                    SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DEFAULT_ESPACE_TRAITEMENT_COLUMN_LIST
                );
            }
            newChild = session.createDocument(newChild);
            setAcl(item.getAcl(), newChild.getRef());
        }
    }
}
