package fr.dila.solonepg.core.event;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Listener exécuté après la validation automatique d'une étape pour information.
 * 
 * @author arolin
 */
public class AfterValidationPourInformationListener implements PostCommitEventListener {

	@Override
	public void handleEvent(final EventBundle events) throws ClientException {
		if (!events.containsEventName(SolonEpgEventConstant.AFTER_VALIDATION_POUR_INFORMATION_EVENT)) {
			return;
		}
		for (final Event event : events) {
			if (event.getName().equals(SolonEpgEventConstant.AFTER_VALIDATION_POUR_INFORMATION_EVENT)) {
				handleEvent(event);
			}
		}
	}

	private void handleEvent(final Event event) throws ClientException {
		final EventContext eventCtx = event.getContext();
		// récupération des propriétés de l'événement
		final Map<String, Serializable> eventProperties = eventCtx.getProperties();
		final String dossierId = (String) eventProperties
				.get(SolonEpgEventConstant.AFTER_VALIDATION_POUR_INFORMATION_DOSSIER_ID_PARAM);
		final String titreActe = (String) eventProperties
				.get(SolonEpgEventConstant.AFTER_VALIDATION_POUR_INFORMATION_DOSSIER_TITRE_ACTE_PARAM);
		final String nor = (String) eventProperties
				.get(SolonEpgEventConstant.AFTER_VALIDATION_POUR_INFORMATION_DOSSIER_NOR_PARAM);
		final String routeStepMailboxId = (String) eventProperties
				.get(SolonEpgEventConstant.AFTER_VALIDATION_POUR_INFORMATION_ROUTE_STEP_MAILBOX_ID_PARAM);

		// Détermine la liste des utilisateurs du poste
		final String posteId = SSServiceLocator.getMailboxPosteService().getPosteIdFromMailboxId(routeStepMailboxId);
		final PosteNode posteNode = STServiceLocator.getSTPostesService().getPoste(posteId);
		final List<STUser> userList = posteNode.getUserList();

		// TODO : à mettre dans le texte et le corps du mail dans les paramètres technique ou dans la conf ARN
		final String objet = "[Solon] Nouveau dossier transmis pour information";
		final String texte = "Le dossier \"${titreActe}\" (${nor}) vous a été transmis pour information.";

		final Map<String, Object> variablesMap = new HashMap<String, Object>();
		variablesMap.put("titreActe", titreActe);
		variablesMap.put("nor", nor);
		STServiceLocator.getSTMailService().sendTemplateHtmlMailToUserList(eventCtx.getCoreSession(), userList, objet,
				texte, variablesMap, Collections.singletonList(dossierId));
	}
}
