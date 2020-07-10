package fr.dila.solonepg.web.feuilleroute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.CorbeilleService;
import fr.dila.solonepg.api.service.DocumentRoutingService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.espace.CorbeilleActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.web.context.NavigationContextBean;

@Name("filterRoutingTaskActionBean")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.FRAMEWORK)
public class FilterRoutingTaskActionBean {

	private static final STLogger LOGGER = STLogFactory.getLog(FilterRoutingTaskActionBean.class);
	
	@In(required = true, create = true)
	protected SSPrincipal						ssPrincipal;

	@In(create = true, required = true)
	protected transient CorbeilleActionsBean	corbeilleActions;
	
	@In(create = true, required = true)
	private transient CoreSession				documentManager;

	@In(create = true, required = false)
	private transient NavigationContextBean		navigationContext;

	public Boolean isRoutingTaskTypeValiderShown() {
		final DocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
		DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
		return documentRoutingService.isRoutingTaskTypeValiderAllowed(ssPrincipal, dossierLink);

	}

	// TODO:: pourquoi ne pas utiliser les données en vocabulaires plutot que de tout définir en dur à la main comme ça?
	public Boolean isRoutingTaskTypeValiderCorrectionShown() {
		DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
		if (dossierLink == null) {
			return Boolean.FALSE;
		} else {
			String routingTaskType = dossierLink.getRoutingTaskType();
			if (routingTaskType == null) {
				return Boolean.FALSE;
			}
			Integer routingTaskTypeInt = Integer.parseInt(routingTaskType);
			Integer[] typeTaskAllowedArray = { 2, 11 };
			List<Integer> typeTaskAllowedList = Arrays.asList(typeTaskAllowedArray);
			return typeTaskAllowedList.contains(routingTaskTypeInt);
		}
	}

	public Boolean isRoutingTaskTypeRefuserValidationShown() {
		DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
		if (dossierLink == null) {
			return Boolean.FALSE;
		} else {
			String routingTaskType = dossierLink.getRoutingTaskType();
			if (routingTaskType == null) {
				return Boolean.FALSE;
			}
			Integer routingTaskTypeInt = Integer.parseInt(routingTaskType);
			Integer[] typeTaskAllowedArray = { 2, 3, 4, 15,
					Integer.parseInt(VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER) };
			List<Integer> typeTaskAllowedList = Arrays.asList(typeTaskAllowedArray);
			final List<String> groups = ssPrincipal.getGroups();
			return typeTaskAllowedList.contains(routingTaskTypeInt)
					|| (Integer.valueOf(5).equals(routingTaskTypeInt) && groups
							.contains(SolonEpgBaseFunctionConstant.ETAPE_POUR_ATTRIBUTION_SGG_EXECUTOR))
					|| (Integer.valueOf(7).equals(routingTaskTypeInt) && groups
							.contains(SolonEpgBaseFunctionConstant.ETAPE_POUR_SIGNATURE_EXECUTOR))
					|| (Integer.valueOf(Integer.parseInt(VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER)).equals(
							routingTaskTypeInt) && groups
							.contains(SolonEpgBaseFunctionConstant.ETAPE_BON_A_TIRER_CREATOR))
					|| (Integer.valueOf(8).equals(routingTaskTypeInt) && groups
							.contains(SolonEpgBaseFunctionConstant.ETAPE_POUR_CONTRESEING_EXECUTOR));
		}

	}

	public Boolean isRoutingTaskTypeNonConcerneShown() {
		DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
		if (dossierLink == null) {
			return Boolean.FALSE;
		} else {
			String routingTaskType = dossierLink.getRoutingTaskType();
			if (routingTaskType == null) {
				return Boolean.FALSE;
			}
			Integer routingTaskTypeInt = Integer.parseInt(routingTaskType);
			Integer[] typeTaskAllowedArray = { 2, 3, 4, 15 };
			List<Integer> typeTaskAllowedList = Arrays.asList(typeTaskAllowedArray);
			final List<String> groups = ssPrincipal.getGroups();
			return typeTaskAllowedList.contains(routingTaskTypeInt)
					|| (Integer.valueOf(5).equals(routingTaskTypeInt) && groups
							.contains(SolonEpgBaseFunctionConstant.ETAPE_POUR_ATTRIBUTION_SGG_EXECUTOR))
					|| (Integer.valueOf(7).equals(routingTaskTypeInt) && groups
							.contains(SolonEpgBaseFunctionConstant.ETAPE_POUR_SIGNATURE_EXECUTOR))
					|| (Integer.valueOf(8).equals(routingTaskTypeInt) && groups
							.contains(SolonEpgBaseFunctionConstant.ETAPE_POUR_CONTRESEING_EXECUTOR));
		}
	}

	public Boolean isRoutingTaskTypeRetourModificationShown() {
		DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
		if (dossierLink == null) {
			return Boolean.FALSE;
		} else {
			String routingTaskType = dossierLink.getRoutingTaskType();
			if (routingTaskType == null) {
				return Boolean.FALSE;
			}
			Integer routingTaskTypeInt = Integer.parseInt(routingTaskType);
			Integer[] typeTaskAllowedArray = { 5, 12, 13, 14 };
			List<Integer> typeTaskAllowedList = Arrays.asList(typeTaskAllowedArray);
			return typeTaskAllowedList.contains(routingTaskTypeInt);

		}
	}

	/**
	 * Méthode qui permet de savoir si l'étape en cours du dossier est de type 'Pour avis CE'
	 * 
	 * @return VRAI ou FAUX selon si pour avis CE ou non
	 */
	public Boolean isEtapePourAvisCE(boolean useUnrestricted) {
		final StringBuilder routingTaskType = new StringBuilder();
		final List<DocumentModel> dossiersLinksList = new ArrayList<DocumentModel>();
		if (useUnrestricted) {
			try {
				new UnrestrictedSessionRunner(documentManager) {
					@Override
					public void run() throws ClientException {
						CorbeilleService cs = SolonEpgServiceLocator.getCorbeilleService();
						dossiersLinksList.addAll(cs.findDossierLink(session, navigationContext.getCurrentDocument().getId()));
						if (dossiersLinksList != null && !dossiersLinksList.isEmpty()) {
							DossierLink dossierLink = dossiersLinksList.get(0).getAdapter(DossierLink.class);
							if (dossierLink != null) {
								routingTaskType.append(dossierLink.getRoutingTaskType());
							}
						}
					}
				}.runUnrestricted();
			} catch (ClientException ce) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_SESSION_FONC, ce.getMessage(), ce);
			}
		} else {
			DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
			if (dossierLink != null) {
				routingTaskType.append(dossierLink.getRoutingTaskType());
			}
		}

		if (StringUtil.isBlank(routingTaskType.toString())) {
			return Boolean.FALSE;
		} else {
			if (VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE.equals(routingTaskType.toString())) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		}
	}
}
