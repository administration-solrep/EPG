package fr.dila.solonepg.web.csv;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.platform.contentview.jsf.ContentView;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.api.PageSelections;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.web.contentview.DossierPageProvider;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.web.contentview.STJournalAdminPageProvider;
import fr.dila.st.web.pdf.PdfPrintActionsBean;

/**
 * WebBean de gestion de l'affichage et de l'impression en csv.
 * 
 */
@Name("csvPrintActions")
@Scope(ScopeType.EVENT)
@Install(precedence = Install.FRAMEWORK + 1)
public class CsvPrintActionsBean extends PdfPrintActionsBean implements Serializable {
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 8273332267581594277L;

	private String							exportName;
	
	@In(required = true, create = true)
	private SSPrincipal						ssPrincipal;
	
	@In(create = true, required = true)
	protected transient CoreSession			documentManager;
	
	@In(create = true)
    protected ResourcesAccessor				resourcesAccessor;
    
	@In(create = true)
	protected transient FacesMessages		facesMessages;

	/**
	 * Affiche la liste des résultats dans un pdf en récupérant tout les résultats et pas uniquement les résultats
	 * affichés.
	 * 
	 * @param view_name
	 * @throws Exception
	 */
	@Override
	public void doRenderViewList(String view_name) throws Exception {
		ContentView contentView = contentViewActions.getCurrentContentView();
		if (contentView != null && contentView.getCurrentPageProvider() != null) {
			PageProvider<?> provider = contentView.getCurrentPageProvider();
			PageSelections<?> pageSelection = provider.getCurrentSelectPage();

			if (!provider.hasError() && pageSelection != null && pageSelection.getEntries() != null
					&& pageSelection.getEntries().size() > 0) {

				if (provider instanceof CoreQueryDocumentPageProvider) {

					// récupération de tous les résultats
					CoreQueryDocumentPageProvider coreProvider = (CoreQueryDocumentPageProvider) provider;
					coreProvider.setPageSize(provider.getResultsCount());
					coreProvider.setSelectedEntries(coreProvider.setCurrentPage(1));
					Map<String, Serializable> props = coreProvider.getProperties();
					props.put("exportName", exportName);
					coreProvider.setProperties(props);
				} else if (provider instanceof DossierPageProvider) {

					// récupération de tous les résultats
					DossierPageProvider dossierPageProvider = (DossierPageProvider) provider;
					dossierPageProvider.setPageSize(provider.getResultsCount());
					dossierPageProvider.setSelectedEntries(dossierPageProvider.setCurrentPage(1));
					Map<String, Serializable> props = dossierPageProvider.getProperties();
					props.put("exportName", exportName);
					dossierPageProvider.setProperties(props);
				} else if (provider instanceof STJournalAdminPageProvider) {

					// récupération de tous les résultats
					STJournalAdminPageProvider dossierPageProvider = (STJournalAdminPageProvider) provider;
					dossierPageProvider.setPageSize(provider.getResultsCount());
					dossierPageProvider.setSelectedEntries(dossierPageProvider.setCurrentPage(1));
					Map<String, Serializable> props = dossierPageProvider.getProperties();
					props.put("exportName", exportName);
					dossierPageProvider.setProperties(props);
				}

			}
		}
		super.doRenderView(view_name);
	}

	public void doRenderViewList(String view_name, String exportName) throws Exception {
		this.exportName = exportName;
		doRenderViewList(view_name);
	}

	public String getCurrentDate() {
		SimpleDateFormat formatter = DateUtil.simpleDateFormat("dd/MM/yyyy");
		return formatter.format(new Date());
	}
	
	public void sendSearchUserResult() throws ClientException {
		// récupération du résultat de recherche
		List<DocumentModel> usersDocs = new ArrayList<DocumentModel>();
		ContentView contentView = contentViewActions.getCurrentContentView();
		if (contentView != null && contentView.getCurrentPageProvider() != null) {
			PageProvider<?> provider = contentView.getCurrentPageProvider();
			PageSelections<?> pageSelection = provider.getCurrentSelectPage();

			if (!provider.hasError() && pageSelection != null && pageSelection.getEntries() != null
					&& pageSelection.getEntries().size() > 0) {

				if (provider instanceof CoreQueryDocumentPageProvider) {
					// récupération de tous les résultats
					CoreQueryDocumentPageProvider coreProvider = (CoreQueryDocumentPageProvider) provider;
					coreProvider.setPageSize(provider.getResultsCount());
					coreProvider.setSelectedEntries(coreProvider.setCurrentPage(0));
					usersDocs = coreProvider.getCurrentPage();
				}
			}
		}
		boolean isAdmin = ssPrincipal.isAdministrator()
				|| ssPrincipal.isMemberOf(STBaseFunctionConstant.UTILISATEUR_UPDATER);
		boolean isAdminMinisteriel = ssPrincipal.isMemberOf(STBaseFunctionConstant.UTILISATEUR_MINISTERE_UPDATER);

		String recipient = ssPrincipal.getEmail();
		if (StringUtil.isBlank(recipient)) {
			facesMessages.add(StatusMessage.Severity.WARN, resourcesAccessor.getMessages().get("feedback.solonepg.no.mail"));
		} else {
			// Post commit event
			EventProducer eventProducer = STServiceLocator.getEventProducer();
			Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
			eventProperties.put(SolonEpgEventConstant.SEND_SEARCH_USERS_RESULTS_IS_ADMIN_PROPERTY, isAdmin);
			eventProperties.put(SolonEpgEventConstant.SEND_SEARCH_USERS_RESULTS_IS_ADMIN_MINISTERIEL_PROPERTY, isAdminMinisteriel);
			eventProperties.put(SolonEpgEventConstant.SEND_SEARCH_USERS_RESULTS_USERS_PROPERTY, (Serializable) usersDocs);
			eventProperties.put(SolonEpgEventConstant.SEND_SEARCH_USERS_RESULTS_RECIPIENT_PROPERTY, recipient);
			HashSet<String> ministeres = new HashSet<String>();
			if (isAdminMinisteriel) {
				ministeres.addAll(ssPrincipal.getMinistereIdSet());
			}
			eventProperties.put(SolonEpgEventConstant.SEND_SEARCH_USERS_RESULTS_ADMIN_MINISTERIEL_MINISTERES_PROPERTY, ministeres);
		
			InlineEventContext eventContext = new InlineEventContext(documentManager, ssPrincipal, eventProperties);
			eventProducer.fireEvent(eventContext.newEvent(SolonEpgEventConstant.SEND_SEARCH_USERS_RESULTS_EVENT));
			
			facesMessages.add(StatusMessage.Severity.INFO, resourcesAccessor.getMessages().get("feedback.solonepg.export"));
		}
	}

}
