package fr.dila.solonepg.web.admin.adamant;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.administration.ParametrageAdamant;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgParametrageAdamantConstants;
import fr.dila.solonepg.api.service.ParametrageAdamantService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.admin.AdministrationActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;

/**
 * Bean de gestion du paramètrage Adamant : edition des propriétés du paramètrage Adamant.
 * 
 * @author bbe
 */
@Name("parametrageAdamantAdministrationActions")
@Scope(ScopeType.PAGE)
public class ParametrageAdamantAdministrationActionsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Logger.
	 */
	private static final Log LOGGER = LogFactory.getLog(ParametrageAdamantAdministrationActionsBean.class);

	@In(create = true, required = true)
	protected transient CoreSession 							documentManager;

	@In(create = true, required = false)
	protected transient FacesMessages 							facesMessages;

	@In(create = true)
	protected transient ResourcesAccessor 						resourcesAccessor;

	@In(create = true)
	protected transient AdministrationActionsBean 				administrationActions;

	protected DocumentModel 									parametrageAdamant;

	/**
	 * Champ utilisé pour afficher les erreurs de validation
	 */
	protected String errorName = null;

	public String valider() throws ClientException {

		ParametrageAdamant parametrageAdamantDoc = parametrageAdamant.getAdapter(ParametrageAdamant.class);

		//on vérifie que les champs obligatoire sont remplis
		if (parametrageAdamantDoc.getNumeroSolon() == null 
				|| parametrageAdamantDoc.getOriginatingAgencyBlocIdentifier().isEmpty() 
				|| parametrageAdamantDoc.getSubmissionAgencyBlocIdentifier().isEmpty() 
				|| parametrageAdamantDoc.getArchivalProfile().isEmpty() 
				|| parametrageAdamantDoc.getOriginatingAgencyIdentifier().isEmpty() 
				|| parametrageAdamantDoc.getSubmissionAgencyIdentifier().isEmpty() 
				|| parametrageAdamantDoc.getDelaiEligibilite() == null 
				|| parametrageAdamantDoc.getNbDossierExtraction() == null ) {
			String message = resourcesAccessor.getMessages().get("label.epg.parametrage.adamant.error");
			facesMessages.add(StatusMessage.Severity.WARN, message);
			LOGGER.warn(message);
			errorName=message;
			return null;
		}

		//log les modifications des paramètrages de l'application hors ministère
		final ParametrageAdamantService parametreAdamantService = SolonEpgServiceLocator.getParametrageAdamantService();
		parametreAdamantService.logAllDocumentUpdate(documentManager, parametrageAdamant);

		//enregistrement des modifications
		documentManager.saveDocument(parametrageAdamantDoc.getDocument());
		Events.instance().raiseEvent(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_CHANGED_EVENT);
		String message = resourcesAccessor.getMessages().get("label.epg.parametrage.adamant.enregistre");
		facesMessages.add(StatusMessage.Severity.INFO, message);
		return administrationActions.navigateToParamAdamant();
	}

	public String annuler() throws ClientException {
		parametrageAdamant=null;
		return administrationActions.navigateToEspaceAdministration();
	}

	/**
	 * Controle l'accès à la vue correspondante
	 * 
	 */
	public boolean isAccessAuthorized() {
		SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
		return (ssPrincipal.isAdministrator() || ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_PARAM_ADAMANT));
	}

	/**
	 * Get the DocumentModel ParametrageAdamant
	 * @throws ClientException 
	 */
	public DocumentModel getParametrageAdamant() throws ClientException {
		if(parametrageAdamant== null){
			ParametrageAdamantService parametrageAdamantService = SolonEpgServiceLocator.getParametrageAdamantService();
			ParametrageAdamant param = parametrageAdamantService.getParametrageAdamantDocument(documentManager);
			parametrageAdamant = param.getDocument();
		}
		return parametrageAdamant;
	}

	public void setParametrageAdamant(DocumentModel parametrageAdamant) {
		this.parametrageAdamant = parametrageAdamant;
	}

	public String getErrorName() {
		return errorName;
	}

	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

}