package fr.dila.solonepg.core.operation.distribution;

import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonepg.api.service.DossierDistributionService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

/**
 * Opération permettant de remettre les droits sur les dossiers links d'un ministère
 * 
 * @author user
 * 
 */
@Operation(id = DroitDossierLink.ID, category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY, label = "Remettre les droits sur les dossiers links d'un ministère", description = DroitDossierLink.DESCRIPTION)
public class DroitDossierLink {

	public final static String	ID			= "SolonEpg.CaseLink.MajDroit";

	public final static String	DESCRIPTION	= "Cette opération permet de remettre les droits sur les dossiers links d'un ministère";

	@Context
	protected OperationContext	context;

	@Context
	protected CoreSession		session;

	@Param(name = "idMinistere")
	protected String			idMinistere;

	@OperationMethod
	public void majDroitsDossier() throws ClientException {
		DossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
		dossierDistributionService.updateDossierLinksACLs(session, idMinistere);
	}
}
