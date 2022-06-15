package fr.dila.solonepg.core.operation.distribution;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Opération permettant de remettre les droits sur les dossiers links d'un ministère
 *
 * @author user
 *
 */
@Operation(
    id = DroitDossierLink.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "Remettre les droits sur les dossiers links d'un ministère",
    description = DroitDossierLink.DESCRIPTION
)
public class DroitDossierLink {
    public static final String ID = "SolonEpg.CaseLink.MajDroit";

    public static final String DESCRIPTION =
        "Cette opération permet de remettre les droits sur les dossiers links d'un ministère";

    @Context
    protected OperationContext context;

    @Context
    protected CoreSession session;

    @Param(name = "idMinistere")
    protected String idMinistere;

    @OperationMethod
    public void majDroitsDossier() {
        EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        dossierDistributionService.updateDossierLinksACLs(session, idMinistere);
    }
}
