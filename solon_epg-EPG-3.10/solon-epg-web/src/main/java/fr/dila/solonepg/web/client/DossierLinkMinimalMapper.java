package fr.dila.solonepg.web.client;

import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.dto.DossierLinkMinimal;
import fr.dila.solonepg.api.dto.IdLabel;

/**
 * Extrait un resultat de la requête ... sur les dossiers link
 * 
 * Attend une map d'une ligne contenant un champ FlexibleQueryMaker.COL_COUNT
 * 
 * @author spesnel
 */
public class DossierLinkMinimalMapper {

    private final ResourcesAccessor ressourceAccessor;

    public DossierLinkMinimalMapper(ResourcesAccessor resourcesAccessor) {
        this.ressourceAccessor = resourcesAccessor;
    }

    public DossierLinkMinimal doMapping(DossierLink dossierLink) {

        String dossierId = dossierLink.getDossierId();
        String taskLabel = dossierLink.getRoutingTaskLabel();
        String taskType = dossierLink.getRoutingTaskType();
        String mailboxLabel = dossierLink.getRoutingTaskMailboxLabel();
        String dossierLinkId = dossierLink.getId();

        IdLabel idLabel = new IdLabel(dossierLinkId, buildDossierLinkLabel(ressourceAccessor, taskLabel, mailboxLabel));
        Boolean retourPourModification = Boolean.FALSE;
        if (SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETOUR_MODIFICATION_VALUE.equals(taskType)) {
            retourPourModification = Boolean.TRUE;
        }
        return new DossierLinkMinimal(dossierId, idLabel, retourPourModification);
    }

    private String buildDossierLinkLabel(ResourcesAccessor resourcesAccessor, String routingTaskLabel, String routingTaskMailboxLabel) {
        StringBuffer labelFormat = new StringBuffer("reponses.dossier.liste.etape.label");
        if (resourcesAccessor == null) {
            return labelFormat.append("[").append(routingTaskLabel).append("][").append(routingTaskMailboxLabel).append("]").toString();
        } else {
            return resourcesAccessor.getMessages().get(routingTaskLabel)+" "+routingTaskMailboxLabel;
        }
    }
}
