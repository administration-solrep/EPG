package fr.dila.solonepg.web.document;

import static org.jboss.seam.annotations.Install.FRAMEWORK;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.dossier.TranspositionActionsBean;
import fr.dila.st.api.constant.STEventConstant;

/**
 * @see fr.dila.st.web.dossier.DossierLockActionsBean
 * 
 * @author asatre
 */
@Name("dossierLockActions")
@Scope(ScopeType.EVENT)
@Install(precedence = FRAMEWORK + 1)
public class DossierLockActionsBean extends fr.dila.st.web.dossier.DossierLockActionsBean {

	private static final long	serialVersionUID	= 8630351980879767190L;

	@Override
	public void lockDossier(DocumentModel dossierDoc) throws ClientException {
		super.lockDossier(dossierDoc);
		// Refresh de la contentview
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		// Refresh du dossier
		Events.instance().raiseEvent(STEventConstant.CURRENT_DOCUMENT_CHANGED_EVENT);
		// On renseigne l'info dans le profilUtilisateur
		ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
		profilUtilisateurService.addDossierToListDerniersDossierIntervention(documentManager, dossierDoc.getId());
	}

	@Override
	public void unlockDossier(DocumentModel dossierDoc) throws ClientException {
		// Remise à zéro des erreurs de transposition des directives
		TranspositionActionsBean.errorTransposition = null;
		// Vérification cohérence des champs pour les textes entreprise
		Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		if (dossier.getTexteEntreprise() && dossier.getDateEffet().isEmpty()) {
			String message = resourcesAccessor.getMessages().get(
					"feedback.solonepg.bordereau.date.effet.obligatoire.si.texte.entreprise");
			facesMessages.add(StatusMessage.Severity.WARN, message);
			// Refresh de la contentview
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		} else {
			super.unlockDossier(dossierDoc);
			// Refresh de la contentview
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
			// Refresh du dossier
			Events.instance().raiseEvent(STEventConstant.CURRENT_DOCUMENT_CHANGED_EVENT);
			// On renseigne l'info dans le profilUtilisateur
			ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
			profilUtilisateurService.addDossierToListDerniersDossierIntervention(documentManager, dossierDoc.getId());
		}
	}

}
