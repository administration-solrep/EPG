package fr.dila.solonepg.core.content.template.factories;

import fr.dila.ss.api.constant.SSEventConstant;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.content.template.factories.SimpleTemplateBasedFactory;
import org.nuxeo.ecm.platform.content.template.service.TemplateItemDescriptor;

/**
 *
 * Une fabrique pour les feuilles de route, dont l'utilité est uniquement de setter le documentRouteId sur la feuille de route par défaut
 *
 * Note : on logge aussi la création d'un modèle de feuille de route ici.
 *
 * @author jgomez
 */
public class FeuilleRouteFactory extends SimpleTemplateBasedFactory {
    private static final Log LOGGER = LogFactory.getLog(FeuilleRouteFactory.class);

    @Override
    public void createContentStructure(DocumentModel eventDoc) {
        initSession(eventDoc);

        final JournalService journalService = STServiceLocator.getJournalService();
        if (eventDoc.isVersion() || !isTargetEmpty(eventDoc)) {
            return;
        }

        if (eventDoc.getName() != null && eventDoc.getName().startsWith("FDR_INJECTE_")) {
            return;
        }

        for (TemplateItemDescriptor item : template) {
            StringBuilder itemPath = new StringBuilder(eventDoc.getPathAsString());
            if (item.getPath() != null) {
                itemPath.append("/").append(item.getPath());
            }
            DocumentModel newChild = session.createDocumentModel(itemPath.toString(), item.getId(), item.getTypeName());
            DublincoreSchemaUtils.setTitle(newChild, item.getTitle());
            DublincoreSchemaUtils.setDescription(newChild, item.getDescription());
            setProperties(item.getProperties(), newChild);
            newChild = session.createDocument(newChild);
            setAcl(item.getAcl(), newChild.getRef());

            // Positionne le document route id sur l'étape d'initialisation
            setDocumentRouteIdOnDefaultInitStep(newChild);
        }

        // on logge l'action de création du modèle de feuille de route
        journaliserCreationElement(eventDoc, journalService);
    }

    protected void journaliserCreationElement(DocumentModel eventDoc, final JournalService journalService) {
        String comment = "Création du modèle de feuille de route [" + eventDoc.getTitle() + "]";
        journalService.journaliserActionAdministration(session, SSEventConstant.CREATE_MODELE_FDR_EVENT, comment);
    }

    protected void setDocumentRouteIdOnDefaultInitStep(DocumentModel newChild) {
        SSRouteStep step = newChild.getAdapter(SSRouteStep.class);
        String fdrId = session.getParentDocument(newChild.getRef()).getId();
        step.setDocumentRouteId(fdrId);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Mise en place du champ documentRouteId " + fdrId + "sur l'étape d'initialisation ");
        }
        session.saveDocument(newChild);
    }
}
