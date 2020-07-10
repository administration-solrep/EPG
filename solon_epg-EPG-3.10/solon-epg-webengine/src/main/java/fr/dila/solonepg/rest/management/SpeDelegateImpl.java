package fr.dila.solonepg.rest.management;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.EventContextImpl;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.CorbeilleService;
import fr.dila.solonepg.api.service.DossierDistributionService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.InformationsParlementairesService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.core.cases.typescomplexe.ParutionBoImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.spe.WsUtil;
import fr.dila.solonepg.rest.api.SpeDelegate;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.constant.STWebserviceConstant;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.JetonService;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.ProfileService;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.xsd.solon.epg.TypeModification;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePERequest;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePEResponse;
import fr.sword.xsd.solon.spe.EnvoyerRetourPERequest;
import fr.sword.xsd.solon.spe.EnvoyerRetourPEResponse;
import fr.sword.xsd.solon.spe.PEActeRetourBo;
import fr.sword.xsd.solon.spe.PEActeRetourJo;
import fr.sword.xsd.solon.spe.PEDemandeType;
import fr.sword.xsd.solon.spe.PEFichier;
import fr.sword.xsd.solon.spe.PERetourEpreuvage;
import fr.sword.xsd.solon.spe.PERetourGestion;
import fr.sword.xsd.solon.spe.PERetourPublicationBo;
import fr.sword.xsd.solon.spe.PERetourPublicationJo;
import fr.sword.xsd.solon.spe.PEstatut;

/**
 * Permet de gerer toutes les operations sur spe.
 */
public class SpeDelegateImpl implements SpeDelegate {

	protected CoreSession		session;

	public static final String	ERROR_READ_WRITE_MSG	= "Vous n'avez pas les droits pour voir ou modifier ce dossier";

	/**
	 * Logger.
	 */
	private static final Log	log						= LogFactory.getLog(SpeDelegateImpl.class);

	public SpeDelegateImpl(CoreSession documentManager) {
		session = documentManager;
	}

	@Override
	public EnvoyerPremiereDemandePEResponse envoyerPremiereDemandePE(EnvoyerPremiereDemandePERequest request) {
		// note : stub utilisé pour tester dans qa et via selenium l'envoi des demandes de publication à la DILA

		// initialisation variable réponse
		EnvoyerPremiereDemandePEResponse envoyerPremiereDemandePEResponse = new EnvoyerPremiereDemandePEResponse();

		// log l'action dans le journal d'administration
		logWebServiceAction(SolonEpgEventConstant.WEBSERVICE_ENVOYER_PREMIERE_DEMANDE_EVENT,
				SolonEpgEventConstant.WEBSERVICE_ENVOYER_PREMIERE_DEMANDE_COMMENT_PARAM);
		envoyerPremiereDemandePEResponse.setStatus(PEstatut.OK);
		return envoyerPremiereDemandePEResponse;
	}

	@Override
	public EnvoyerRetourPEResponse envoyerRetourPE(EnvoyerRetourPERequest request) {
		final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
				.getDossierDistributionService();
		final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
		final NORService norService = SolonEpgServiceLocator.getNORService();
		final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
		final FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
		final ActiviteNormativeService activiteNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();
		final STParametreService paramService = STServiceLocator.getSTParametreService();
		final ProfileService profileService = STServiceLocator.getProfileService();
		final STMailService mailService = STServiceLocator.getSTMailService();

		// récupération des informations de la requete
		PEDemandeType demandeType = request.getType();
		final List<DocumentModel> dossiersList = new ArrayList<DocumentModel>();
		log.info("initialisation envoyerRetourPE service : type de demande = '" + demandeType.toString());

		// initialisation variable réponse
		EnvoyerRetourPEResponse envoyerRetourPEResponse = new EnvoyerRetourPEResponse();
		// par defaut, on considère que la réponse est en échec
		envoyerRetourPEResponse.setStatus(PEstatut.KO);
		// verification des droits de l'utilisateur pour le service
		List<String> userGroupList = ((SSPrincipal) session.getPrincipal()).getGroups();
		if (!userGroupList.contains(STWebserviceConstant.ENVOYER_RETOUR_PE)) {
			envoyerRetourPEResponse.setMessageErreur("Vous n'avez pas les droits pour accéder à ce service !");
			return envoyerRetourPEResponse;
		}

		final List<String> norNonTrouve = new ArrayList<String>();
		// on détermine la type de demande
		if (demandeType.equals(PEDemandeType.EPREUVAGE)) {
			PERetourEpreuvage peRetourEpreuvage = request.getRetourEpreuvage();

			// récupération du dossier
			String nor = peRetourEpreuvage.getNor();
			DocumentModel dossierDoc = null;

			try {
				dossierDoc = norService.getDossierFromNOR(session, nor);
				dossiersList.add(dossierDoc);
			} catch (ClientException e) {
				log.error("Erreur lors de la récupération du dossier " + nor + " : ", e);
				envoyerRetourPEResponse.setMessageErreur("Erreur lors de la récupération du dossier " + nor);
				return envoyerRetourPEResponse;
			}

			if (dossierDoc == null) {
				log.warn("Le nor du dossier '" + nor + "' n'a pas été trouvé");
				envoyerRetourPEResponse.setMessageErreur("Le nor du dossier '" + nor + "' n'a pas été trouvé.");
				return envoyerRetourPEResponse;
			}

			// création du fichier dans le répertoire du fdd epreuve
			PEFichier peFichier = peRetourEpreuvage.getEpreuve();
			try {

				// On cherche à savoir si le répertoire épreuves est dans le fond de dossier sinon on le met dans le
				// parapheur
				List<DocumentModel> repertoiresFDD = fondDeDossierService.getAllFddFolder(session,
						dossierDoc.getAdapter(Dossier.class));
				boolean vieuxDossier = false;
				for (DocumentModel fddf : repertoiresFDD) {
					FondDeDossierFolder fddRepert = fddf.getAdapter(FondDeDossierFolder.class);

					if (SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES.equals(fddRepert.getName())
							|| SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES.equals(fddRepert
									.getTitle())) {
						vieuxDossier = true;
						WsUtil.createFileInFdd(session, dossierDoc, peFichier,
								SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES);

					}
				}
				if (!vieuxDossier) {
					WsUtil.createFileInParapheur(session, dossierDoc, peFichier,
							SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EPREUVES_NAME);
				}

			} catch (ClientException e) {
				log.error("Erreur lors de la création des fichiers de l'Epreuvage", e);
				envoyerRetourPEResponse.setMessageErreur("Erreur lors de la création des fichiers de l'Epreuvage");
				return envoyerRetourPEResponse;
			}
			// note : le dossier n'est pas publié dans le cas d'un dossier pour epreuve
			envoyerRetourPEResponse.setStatus(PEstatut.OK);
			// log l'action dans le journal d'administration
			logWebServiceActionDossier(SolonEpgEventConstant.WEBSERVICE_ENVOYER_RETOUR_PE_EVENT,
					SolonEpgEventConstant.WEBSERVICE_ENVOYER_RETOUR_PE_COMMENT_PARAM,
					dossierDoc.getAdapter(Dossier.class));
		} else if (demandeType.equals(PEDemandeType.PUBLICATION_JO)) {
			PERetourPublicationJo peRetourPublication = request.getRetourPublicationJo();
			final List<PEActeRetourJo> pEActeRetourList = peRetourPublication.getActe();
			if (pEActeRetourList != null && pEActeRetourList.size() > 0) {
				// récupération des informations communes
				PERetourGestion peRetourGestion = peRetourPublication.getGestion();
				final Calendar dateParutionJorf = peRetourGestion.getDateParution().toGregorianCalendar();

				// on ouvre une unrestricted session pour mettre à jour les droits d'indexation à la publication
				try {
					new UnrestrictedSessionRunner(session) {
						@Override
						public void run() throws ClientException {
							for (PEActeRetourJo peActeRetour : pEActeRetourList) {
								String nor = peActeRetour.getNor();
								DocumentModel dossierDoc = norService.getDossierFromNOR(session, nor);
								if (dossierDoc == null) {
									norNonTrouve.add(nor);
								} else {
									dossiersList.add(dossierDoc);
									// maj des données parutions Jorf
									RetourDila retourDila = dossierDoc.getAdapter(RetourDila.class);
									retourDila.setDateParutionJorf(dateParutionJorf);
									if (StringUtils.isNotEmpty(peActeRetour.getNumeroTexte())) {
										retourDila.setNumeroTexteParutionJorf(peActeRetour.getNumeroTexte());
									}

									retourDila.setPageParutionJorf((long) peActeRetour.getPage());
									retourDila.setTitreOfficiel(peActeRetour.getTitreOfficiel());

									// Passe le dossier à l'état publié si ce n'est pas déjà le cas
									Dossier dossier = retourDila.getDocument().getAdapter(Dossier.class);
									if (!dossier.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
										dossierService.publierDossier(dossier, session);
										dossier.save(session);
									} else {
										activiteNormativeService.setPublicationInfo(retourDila, session);
										dossier.save(session);
									}
									// log l'action dans le journal d'administration
									logWebServiceActionDossier(
											SolonEpgEventConstant.WEBSERVICE_ENVOYER_RETOUR_PE_EVENT,
											SolonEpgEventConstant.WEBSERVICE_ENVOYER_RETOUR_PE_COMMENT_PARAM, dossier);
									if (dossier.getTypeActe().equals(
											TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES)) {
										// Appel du WebService TransmissionDatePublicationJO de EPP
										InformationsParlementairesService infosParService = SolonEpgServiceLocator
												.getInformationsParlementairesService();
										try {
											infosParService.callWsCreerVersionEvt48(session, dossier);
										} catch (Exception e) {
											throw new ClientException(e.getMessage());
										}
									}
									if (dossierService.hasEtapeAvisCE(session, dossier)) {
										// Création d'un nouveau jeton car on est dans étape pour Avis CE
										final JetonService jetonService = STServiceLocator.getJetonService();
										if (dossier != null) {
											jetonService.addDocumentInBasket(session, STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER,
													TableReference.MINISTERE_CE, dossierDoc, dossier.getNumeroNor(),
													TypeModification.PUBLICATION.name(), null);
										}
									}
								}
							}
						}
					}.runUnrestricted();
				} catch (ClientException e) {
					log.error("Erreur lors de la publication JO", e);
					envoyerRetourPEResponse.setMessageErreur("Erreur lors de la publication JO " + e.getMessage());
					return envoyerRetourPEResponse;
				}
				if (pEActeRetourList.size() == 1 && !norNonTrouve.isEmpty()) {
					envoyerRetourPEResponse.setStatus(PEstatut.KO);
				}else {
					envoyerRetourPEResponse.setStatus(PEstatut.OK);
				}
			}
		} else if (demandeType.equals(PEDemandeType.PUBLICATION_BO)) {
			PERetourPublicationBo peRetourPublication = request.getRetourPublicationBo();
			final List<PEActeRetourBo> pEActeRetourBoList = peRetourPublication.getActe();
			if (pEActeRetourBoList != null && pEActeRetourBoList.size() > 0) {
				// récupération des informations communes
				PERetourGestion peRetourGestion = peRetourPublication.getGestion();
				final Calendar dateParutionBo = peRetourGestion.getDateParution().toGregorianCalendar();

				// on ouvre une unrestricted session pour mettre à jour les droits d'indexation à la publication
				try {
					new UnrestrictedSessionRunner(session) {
						@Override
						public void run() throws ClientException {
							for (PEActeRetourBo peActeRetour : pEActeRetourBoList) {
								String nor = peActeRetour.getNor();
								DocumentModel dossierDoc = norService.getDossierFromNOR(session, nor);
								if (dossierDoc == null) {
									norNonTrouve.add(nor);
								} else {
									// maj des données parutions Bo
									dossiersList.add(dossierDoc);
									RetourDila retourDila = dossierDoc.getAdapter(RetourDila.class);
									List<ParutionBo> parutionBoList = retourDila.getParutionBo();
									if (parutionBoList == null) {
										parutionBoList = new ArrayList<ParutionBo>();
									}

									ParutionBo parutionBo = new ParutionBoImpl();
									parutionBo.setDateParutionBo(dateParutionBo);
									if (StringUtils.isNotEmpty(peActeRetour.getNumeroTexte())) {
										parutionBo.setNumeroTexteParutionBo(peActeRetour.getNumeroTexte());
									}

									parutionBo.setPageParutionBo((long) peActeRetour.getPage());
									parutionBoList.add(parutionBo);
									retourDila.setParutionBo(parutionBoList);

									// Passe le dossier à l'état publié si ce n'est pas déjà le cas
									Dossier dossier = retourDila.getDocument().getAdapter(Dossier.class);
									if (!dossier.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
										dossierService.publierDossier(dossier, session);
									} else {
										dossier.save(session);
									}
									// log l'action dans le journal d'administration
									logWebServiceActionDossier(
											SolonEpgEventConstant.WEBSERVICE_ENVOYER_RETOUR_PE_EVENT,
											SolonEpgEventConstant.WEBSERVICE_ENVOYER_RETOUR_PE_COMMENT_PARAM, dossier);
								}
							}
						}
					}.runUnrestricted();
				} catch (ClientException e) {
					log.error("Erreur lors de la publication BO", e);
					envoyerRetourPEResponse.setMessageErreur("Erreur lors de la publication BO " + e.getMessage());
					return envoyerRetourPEResponse;
				}
				if (pEActeRetourBoList.size() == 1 && !norNonTrouve.isEmpty()) {
					envoyerRetourPEResponse.setStatus(PEstatut.KO);
				}else {
					envoyerRetourPEResponse.setStatus(PEstatut.OK);
				}
			}
		}

		// Gestion des erreurs
		final List<DocumentModel> dossierLinkDocEnErreurList = new ArrayList<DocumentModel>();

		// récupère le dossierLink lié au dossier
		try {
			new UnrestrictedSessionRunner(session) {
				@Override
				public void run() throws ClientException {
					for (DocumentModel dossierDoc : dossiersList) {
						DocumentModel dossierLinkDoc = null;
						List<DocumentModel> dossierLinkDocList = null;
						try {
							dossierLinkDocList = corbeilleService.findDossierLink(session, dossierDoc.getId());
						} catch (Exception e) {
							log.error("erreur lors de la récupération des dossiers link liés au dossier : ", e);
						}

						if (dossierLinkDocList != null) {
							for (DocumentModel dossierLinkDocElement : dossierLinkDocList) {
								// on verifie que le dossierLink est bien à l'étap "pour publication pu pour euprevage"
								DossierLink dossierLink = dossierLinkDocElement.getAdapter(DossierLink.class);
								if (VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO.equals(dossierLink
										.getRoutingTaskType())
										|| VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(dossierLink
												.getRoutingTaskType())
										|| VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE.equals(dossierLink
												.getRoutingTaskType())) {
									dossierLinkDoc = dossierLinkDocElement;
									break;
								}
							}
						}

						if (dossierLinkDoc != null) {
							try {
								dossierDistributionService.validerEtape(session, dossierDoc, dossierLinkDoc,
										STSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_VALIDE_AUTOMATIQUEMENT_VALUE);
							} catch (Exception e) {
								dossierLinkDocEnErreurList.add(dossierDoc);
								log.error("erreur lors de la validation de l'etape : ", e);
							}
						}
					}
				}
			}.runUnrestricted();
		} catch (ClientException e) {
			log.error("Erreur lors de la gestion du dossier link", e);
		}

		// En cas d'erreur, envoi de mail
		if (dossierLinkDocEnErreurList.size() > 0) {
			try {
				envoyerRetourPEResponse.setStatus(PEstatut.KO);
				String mailContent, mailObject, email;
				List<STUser> users;
				List<String> emailsList;
				Map<String, Object> param;
				users = profileService
						.getUsersFromBaseFunction(STBaseFunctionConstant.ADMIN_FONCTIONNEL_EMAIL_RECEIVER);
				emailsList = new ArrayList<String>();
				for (STUser user : users) {
					email = user.getEmail();
					if (email != null && !email.isEmpty()) {
						emailsList.add(email);
					}
				}

				StringBuilder dossierNorBuilder = new StringBuilder();
				if (!emailsList.isEmpty()) {
					for (final DocumentModel doc : dossierLinkDocEnErreurList) {
						final Dossier dossier = doc.getAdapter(Dossier.class);
						if (dossierNorBuilder.length() != 0) {
							dossierNorBuilder.append(",");
						}
						dossierNorBuilder.append(dossier.getNumeroNor());
					}
					// Envoyer Un seul Mail pour tous les dossier en erreur
					mailContent = paramService.getParametreValue(session,
							SolonEpgParametreConstant.NOTIFICATION_ERREUR_VALIDATION_AUTOMATIQUE_TEXTE);
					mailObject = paramService.getParametreValue(session,
							SolonEpgParametreConstant.NOTIFICATION_ERREUR_VALIDATION_AUTOMATIQUE_OBJET);
					param = new HashMap<String, Object>();
					param.put("NOR", dossierNorBuilder.toString());
					mailService.sendTemplateMail(emailsList, mailObject, mailContent, param);
				}
			} catch (ClientException ce) {
				log.error("Erreur lors de l'envoi de mail d'erreur validation etape.", ce);
			}
		}

		return envoyerRetourPEResponse;
	}

	/**
	 * Logge l'action effectue par webservice dans le journal d'administration.
	 * 
	 * @throws ClientException
	 */
	protected void logWebServiceAction(String name, String comment) {
		// log du webservice dans le journal d'administration
		EventProducer producer = STServiceLocator.getEventProducer();
		// création de l'évenement
		final EventContextImpl envContext = new EventContextImpl(session, session.getPrincipal());
		envContext.setProperty("comment", comment);
		// on lance un evenement
		try {
			producer.fireEvent(envContext.newEvent(name));
		} catch (ClientException e) {
			log.error("erreur lors de l'envoi d'un log pour le journal technique. Nom de l'event" + name, e);
		}
	}

	/**
	 * Log l'action effectuée par webservice dans le journal du dossier
	 * 
	 * @param name
	 *            nom de l'évènement de log
	 * @param comment
	 *            commentaire qui sera rédigé dans le log
	 * @param dossier
	 *            dossier auquel on ajoute le log
	 */
	protected void logWebServiceActionDossier(String name, String comment, Dossier dossier) {
		try {
			JournalService journalService = SolonEpgServiceLocator.getJournalService();
			journalService.journaliserActionAdministration(session, dossier.getDocument(), name, comment);
		} catch (Exception e) {
			log.error("erreur lors de l'envoi d'un log pour le journal du dossier. Nom de l'event" + name, e);
		}
	}

}
