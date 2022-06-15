package fr.dila.solonepg.core.dto;

import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETOUR_MODIFICATION_VALUE;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.dto.DossierLinkMinimal;
import fr.dila.ss.api.recherche.IdLabel;
import fr.dila.st.core.util.ResourceHelper;

/**
 * Extrait un resultat de la requête ... sur les dossiers link
 *
 * Attend une map d'une ligne contenant un champ FlexibleQueryMaker.COL_COUNT
 *
 * @author spesnel
 */
public final class DossierLinkMinimalMapper {

    public static DossierLinkMinimal doMapping(DossierLink dossierLink) {
        String dossierId = dossierLink.getDossierId();
        String taskType = dossierLink.getRoutingTaskType();

        IdLabel idLabel = getIdLabelFromDossierLink(dossierLink);
        idLabel.setIdDossier(dossierId);
        boolean retourPourModification = ROUTING_TASK_VALIDATION_STATUS_RETOUR_MODIFICATION_VALUE.equals(taskType);
        return new DossierLinkMinimal(dossierId, idLabel, retourPourModification, dossierLink.getDateCreation());
    }

    private static String buildDossierLinkLabel(String routingTaskLabel, String routingTaskMailboxLabel) {
        return routingTaskLabel + " " + routingTaskMailboxLabel;
    }

    public static IdLabel getIdLabelFromDossierLink(DossierLink dossierLink) {
        String taskLabel = ResourceHelper.getString(dossierLink.getRoutingTaskLabel());
        String mailboxLabel = dossierLink.getRoutingTaskMailboxLabel();
        String dossierLinkId = dossierLink.getId();
        return new IdLabel(dossierLinkId, buildDossierLinkLabel(taskLabel, mailboxLabel), dossierLink.getDossierId());
    }

    private DossierLinkMinimalMapper() {}
}
