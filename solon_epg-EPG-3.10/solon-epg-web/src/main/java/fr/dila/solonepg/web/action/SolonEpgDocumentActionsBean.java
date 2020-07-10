package fr.dila.solonepg.web.action;

import static org.jboss.seam.ScopeType.CONVERSATION;

import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.ss.api.service.FeuilleRouteModelService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.web.action.DocumentActionsBean;

/**
 * Surchage du DocumentActionsBean de nuxeo
 */
@Name("documentActions")
@Scope(CONVERSATION)
@Install(precedence = Install.DEPLOYMENT + 1)
public class SolonEpgDocumentActionsBean extends DocumentActionsBean {

	private static final long serialVersionUID = 1L;

	@Override
	public String createDocument(String typeName) throws ClientException {
		// Workaround de la NPE si on crée un modèle après avoir filtré dans la page de gestion des modèles
		if (STConstant.FEUILLE_ROUTE_DOCUMENT_TYPE.equals(typeName)) {
			final FeuilleRouteModelService feuilleRouteModelService = SSServiceLocator.getFeuilleRouteModelService();
			DocumentModel feuilleRouteModelFolder = feuilleRouteModelService
					.getFeuilleRouteModelFolder(documentManager);
			navigationContext.setCurrentDocument(feuilleRouteModelFolder);
		}
		return super.createDocument(typeName);
	}
}
