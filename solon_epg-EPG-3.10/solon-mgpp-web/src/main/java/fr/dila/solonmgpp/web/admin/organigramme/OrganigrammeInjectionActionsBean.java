package fr.dila.solonmgpp.web.admin.organigramme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

import fr.dila.solonepg.api.client.InjectionEpgGvtDTO;
import fr.dila.solonmgpp.api.constant.SolonMgppEventConstants;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.api.client.InjectionGvtDTO;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STViewConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.administration.organigramme.OrganigrammeTreeBean;

/**
 * Injection de gouvernement.
 * 
 * @author jbrunet
 */
@Name("organigrammeInjectionActions")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.APPLICATION + 2)
public class OrganigrammeInjectionActionsBean extends
		fr.dila.solonepg.web.admin.organigramme.OrganigrammeInjectionActionsBean {

	private static final long serialVersionUID = 1L;

	private static final STLogger LOGGER = STLogFactory.getLog(OrganigrammeInjectionActionsBean.class);

	@In(required = true, create = true)
	private SSPrincipal ssPrincipal;

	//private Boolean isPreparingInjection = false;
	
	private StringBuilder injectionErrorMessage=new StringBuilder();

	public OrganigrammeInjectionActionsBean() {
		super();
		listCompared = new ArrayList<InjectionEpgGvtDTO>();
		
	}

	public String executeInjectionEPP() {
		try {
			SolonMgppServiceLocator.getMGPPInjectionGouvernementService().executeInjection(documentManager,
					listInjection);
			facesMessages.add(StatusMessage.Severity.INFO,
					resourcesAccessor.getMessages().get("ss.organigramme.injection.ajout.succes"));
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_PROCESS_INJECTION_GOUVERNEMENT_TEC, e);
			facesMessages.add(
					StatusMessage.Severity.ERROR,
					resourcesAccessor.getMessages().get("ss.organigramme.injection.ajout.echec") + " : "
							+ e.getMessage());
		}
		Events.instance().raiseEvent(OrganigrammeTreeBean.ORGANIGRAMME_CHANGED_EVENT);
		return STViewConstant.ORGANIGRAMME_VIEW_MANAGE;
	}
	
	private void reinitInjection(){
		if ( !StringUtils.isBlank(injectionErrorMessage.toString()) ){
			injectionErrorMessage.delete(0,injectionErrorMessage.length());
		}
		
		listCompared.clear();
	}

	/**
	 * Prépare l'injection
	 * 
	 * @return vue récapitulative pour l'injection
	 * @throws ClientException
	 */
	public String injecterGouvernementEPP() throws ClientException {
		reinitInjection();
		
		// Récupération des données du fichier Excel
		try {
			preparerInjection();
			LOGGER.info(documentManager, STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC,
					"Fin de préparation de l'injection (partie lecture fichier)");
			
			final EventProducer eventProducer = STServiceLocator.getEventProducer();
			final Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
			eventProperties.put(SolonMgppEventConstants.LIST_COMPARAISON_EPP_PARAM, (Serializable) listCompared);
			eventProperties.put(SolonMgppEventConstants.LIST_INJECTION_EPP_PARAM, (Serializable) listInjection);
			eventProperties.put(SolonMgppEventConstants.INJECTION_ERROR, injectionErrorMessage);
			final InlineEventContext inlineEventContext = new InlineEventContext(documentManager, ssPrincipal,eventProperties);
			Event injectionEvent = inlineEventContext.newEvent(SolonMgppEventConstants.INJECTION_EPP_PREPARATION);
			eventProducer.fireEvent(injectionEvent);

		} catch (Exception e) {

			return STViewConstant.ORGANIGRAMME_VIEW_MANAGE;
		}

		return "view_injection_epp";
	}

	public String getMandatType(InjectionGvtDTO newMin) {
		return (newMin.getNorEPP() == null ? "Ministère" : "Ministère délégué");
	}

	public InjectionGvtDTO getNewGovernment() {
		if (listCompared.isEmpty()) {
			return null;
		} else {
			return listCompared.get(0).getImportedGvt();
		}
	}

	/**
	 * Récupère les entités à l'exception du gouvernement
	 * 
	 * @return
	 */
	public List<InjectionEpgGvtDTO> getListComparedWithoutGovernment() {
		List<InjectionEpgGvtDTO> resultList = new ArrayList<InjectionEpgGvtDTO>();
		for (InjectionEpgGvtDTO entity : listCompared) {
			if ((entity.getBaseGvt() != null && !entity.getBaseGvt().isGvt())
					|| (entity.getImportedGvt() != null && !entity.getImportedGvt().isGvt())) {
				resultList.add(entity);
			}
		}
		return listCompared;
	}

	public Boolean checkInjection() {
		if (listCompared.size() >= listInjection.size()){
			if ( !StringUtils.isBlank(injectionErrorMessage.toString()) ){
				injectionErrorMessage.delete(0,injectionErrorMessage.length());
			}
		}
		return (listCompared.size() < listInjection.size());

	}
	
	public Boolean hasInjectionEventError(){
		//Tant qu'on n'a pas de message d'erreur tout va bien
		if (StringUtils.isBlank(injectionErrorMessage.toString())) {
			
			return false;
		} else {
			return true;
		}
	}

	public String getInjectionErrorMessage() {
		return injectionErrorMessage.toString();
	}
}
