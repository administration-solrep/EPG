package fr.dila.solonepg.core.operation.distribution;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.ecm.platform.routing.api.DocumentRouteStep;
import fr.dila.ecm.platform.routing.api.DocumentRoutingConstants;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.WsSpeService;
import fr.dila.solonepg.core.filter.RouteStepFilter;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STOperationConstant;
import fr.dila.st.api.constant.STWebserviceConstant;
import fr.dila.st.api.feuilleroute.STRouteStep;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.api.service.JetonService;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Opération appellée à la fin de la chaine de création de CaseLink.
 * 
 * @author bgamard
 * @author arolin
 */
@Operation(id = CaseLinkCreatedOperation.ID, category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY, label = "Case Link created", description = "Fire event when CaseLink is created.")
public class CaseLinkCreatedOperation {
	/**
	 * Identifiant technique de l'opération.
	 */
	public final static String	ID		= "SolonEpg.CaseLink.Created";

	private static final Log	LOGGER	= LogFactory.getLog(CaseLinkCreatedOperation.class);

	@Context
	protected OperationContext	context;

	@OperationMethod
	public void caseLinkCreated() throws Exception {
		CoreSession session = context.getCoreSession();
		DocumentRouteStep step = (DocumentRouteStep) context.get(DocumentRoutingConstants.OPERATION_STEP_DOCUMENT_KEY);
		STRouteStep routeStep = step.getDocument().getAdapter(STRouteStep.class);

		@SuppressWarnings("unchecked")
		List<DocumentModel> caseDocList = (List<DocumentModel>) context.get(STOperationConstant.OPERATION_CASES_KEY);

		if (caseDocList != null && caseDocList.size() > 0) {
			DocumentModel dossierDoc = caseDocList.get(0);
			// On envoie un évenement qui va envoyer un mail de notification aux utilisateurs
			EventProducer eventProducer = STServiceLocator.getEventProducer();
			Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
			// on met le dossier et l'étape de feuille de route en paramètre
			eventProperties.put(SolonEpgEventConstant.AFTER_CASE_LINK_CREATED_DOSSIER_DOCID_PARAM, dossierDoc.getId());
			eventProperties.put(SolonEpgEventConstant.AFTER_CASE_LINK_CREATED_ROUTE_STEP_DOCID_PARAM, routeStep
					.getDocument().getId());
			InlineEventContext inlineEventContext = new InlineEventContext(session, session.getPrincipal(),
					eventProperties);
			eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgEventConstant.AFTER_CASE_LINK_CREATED_EVENT));

			String typeEtape = routeStep.getType();
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);

			if (VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE.equals(typeEtape)) {
				Boolean isEtapePrecedentePourAvisCE = false;
				// on récupère l'étape précédente
				final Filter routeStepFilter = new RouteStepFilter();
				final DocumentModel previousStep = SolonEpgServiceLocator.getFeuilleRouteService()
						.findPreviousStepInFolder(session, routeStep.getDocument(), routeStepFilter, false);
				if (previousStep != null) {
					final STRouteStep previousStepDoc = previousStep.getAdapter(STRouteStep.class);
					if (previousStepDoc.getType().equals(VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE)) {
						isEtapePrecedentePourAvisCE = true;
					}
				}
				// Si l'étape précédente est une étape de type pour avis CE on ne crée pas un nouveau jeton
				if (!isEtapePrecedentePourAvisCE) {
					final JetonService jetonService = STServiceLocator.getJetonService();
					jetonService.addDocumentInBasket(session, STWebserviceConstant.CHERCHER_DOSSIER,
							TableReference.MINISTERE_CE, dossierDoc, dossier.getNumeroNor(), null, null);
				}
			} else if ((VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE.equals(typeEtape)
					|| VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(typeEtape) || VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO
					.equals(typeEtape))) {
				LOGGER.info("Début de validation d'une étape concernant la publication à la DILA, type de l'étape "
						+ typeEtape);
				ConfigService configService = STServiceLocator.getConfigService();
				Boolean isInjectionInProgress = configService
						.getBooleanValue(SolonEpgConfigConstant.MODE_INJECTION_IN_PROGRESS);
				if (isInjectionInProgress && dossier.getIsDossierIssuInjection()) {
					LOGGER.info("Une injection est en cours, le dossier est issue d'une injection, rien n'est envoyé au service de publication de la DILA ");
				} else {
					LOGGER.info("On effectue une action dans le cadre de la publication à la DILA car injectionEnCours = "
							+ isInjectionInProgress + ", dossierIssuInjection = " + dossier.getIsDossierIssuInjection());

					LOGGER.info("Mise en place du flag de passage dans l'envoi de premiereDemandePublicationPourDila");
					// Met le flag de demande de publication sur le dossier, à mettre en dessous de l'envoi quand il y
					// aura une gestion des erreurs.
					dossier.setIsAfterDemandePublication(true);
					session.saveDocument(dossier.getDocument());
					session.save();

					RetourDila retourDila = dossierDoc.getAdapter(RetourDila.class);
					WsSpeService wsSpeService = SolonEpgServiceLocator.getWsSpeService();
					if (retourDila.getIsPublicationEpreuvageDemandeSuivante()) {
						// si l'étape n'est pas la première demande de publication ou d'épreuvage, on envoie un mail.
						LOGGER.info("Epreuvage : Pas d'appel au web service de la DILA, envoie d'un mail");
						wsSpeService.envoiDemandePublicationSuivante(dossier, session, typeEtape, routeStep);
					} else {
						// si l'étape est la première demande de publication ou d'épreuvage, on envoi une requete
						// webservice à la dila contenant les informations du dossier et le type de publication.
						LOGGER.info("Envoi d'une requête au service de publication DILA");
						wsSpeService.envoiPremiereDemandePublicationPourDila(dossier,
								STWebserviceConstant.ENVOYER_PREMIERE_DEMANDE_PE, session, typeEtape, routeStep);
					}
					LOGGER.info("Fin d'action pour la publication DILA");
				}
				LOGGER.info("Fin de validation d'une étape concernant la publication à la DILA, type de l'étape "
						+ typeEtape);
			}

		}

	}
}
