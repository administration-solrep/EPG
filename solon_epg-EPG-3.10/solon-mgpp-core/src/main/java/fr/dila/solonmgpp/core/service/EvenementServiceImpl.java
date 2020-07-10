package fr.dila.solonmgpp.core.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.platform.uidgen.UIDSequencer;
import org.nuxeo.ecm.platform.uidgen.service.ServiceHelper;
import org.nuxeo.ecm.platform.uidgen.service.UIDGeneratorService;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepp.rest.api.WSEpp;
import fr.dila.solonepp.rest.api.WSEvenement;
import fr.dila.solonmgpp.api.constant.BuilderConstant;
import fr.dila.solonmgpp.api.constant.TypeEvenementConstants;
import fr.dila.solonmgpp.api.constant.VocabularyConstants;
import fr.dila.solonmgpp.api.descriptor.MetaDonneesDescriptor;
import fr.dila.solonmgpp.api.descriptor.PropertyDescriptor;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.api.domain.FileSolonMgpp;
import fr.dila.solonmgpp.api.domain.Navette;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.domain.RepresentantAUD;
import fr.dila.solonmgpp.api.domain.RepresentantOEP;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.node.MandatNode;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.api.service.EvenementService;
import fr.dila.solonmgpp.api.service.TableReferenceService;
import fr.dila.solonmgpp.core.builder.ContainerBuilder;
import fr.dila.solonmgpp.core.builder.EvenementBuilder;
import fr.dila.solonmgpp.core.builder.PieceJointeBuilder;
import fr.dila.solonmgpp.core.dto.EvenementDTOImpl;
import fr.dila.solonmgpp.core.util.WSErrorHelper;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.rest.client.HttpTransactionException;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epp.AccuserReceptionRequest;
import fr.sword.xsd.solon.epp.AccuserReceptionResponse;
import fr.sword.xsd.solon.epp.AnnulerEvenementRequest;
import fr.sword.xsd.solon.epp.AnnulerEvenementResponse;
import fr.sword.xsd.solon.epp.ChercherDossierRequest;
import fr.sword.xsd.solon.epp.ChercherDossierResponse;
import fr.sword.xsd.solon.epp.ChercherEvenementRequest;
import fr.sword.xsd.solon.epp.ChercherEvenementResponse;
import fr.sword.xsd.solon.epp.CreationType;
import fr.sword.xsd.solon.epp.CreerVersionRequest;
import fr.sword.xsd.solon.epp.CreerVersionResponse;
import fr.sword.xsd.solon.epp.CritereRechercheEvenement;
import fr.sword.xsd.solon.epp.EppAUD01;
import fr.sword.xsd.solon.epp.EppBaseEvenement;
import fr.sword.xsd.solon.epp.EppDOC01;
import fr.sword.xsd.solon.epp.EppEvt01;
import fr.sword.xsd.solon.epp.EppEvt10;
import fr.sword.xsd.solon.epp.EppEvt13;
import fr.sword.xsd.solon.epp.EppEvt24;
import fr.sword.xsd.solon.epp.EppEvt28;
import fr.sword.xsd.solon.epp.EppEvt29;
import fr.sword.xsd.solon.epp.EppEvt30;
import fr.sword.xsd.solon.epp.EppEvt31;
import fr.sword.xsd.solon.epp.EppEvt32;
import fr.sword.xsd.solon.epp.EppEvt35;
import fr.sword.xsd.solon.epp.EppEvt36;
import fr.sword.xsd.solon.epp.EppEvt44;
import fr.sword.xsd.solon.epp.EppEvt49;
import fr.sword.xsd.solon.epp.EppEvt51;
import fr.sword.xsd.solon.epp.EppEvtAlerte;
import fr.sword.xsd.solon.epp.EppEvtContainer;
import fr.sword.xsd.solon.epp.EppJSS02;
import fr.sword.xsd.solon.epp.EppPG01;
import fr.sword.xsd.solon.epp.EppPG04;
import fr.sword.xsd.solon.epp.EppSD01;
import fr.sword.xsd.solon.epp.EppSD03;
import fr.sword.xsd.solon.epp.EtatEvenement;
import fr.sword.xsd.solon.epp.EtatMessage;
import fr.sword.xsd.solon.epp.EvenementType;
import fr.sword.xsd.solon.epp.EvtId;
import fr.sword.xsd.solon.epp.InitialiserEvenementRequest;
import fr.sword.xsd.solon.epp.InitialiserEvenementResponse;
import fr.sword.xsd.solon.epp.Institution;
import fr.sword.xsd.solon.epp.MajInterneRequest;
import fr.sword.xsd.solon.epp.MajInterneResponse;
import fr.sword.xsd.solon.epp.Mandat;
import fr.sword.xsd.solon.epp.Message;
import fr.sword.xsd.solon.epp.ModeCreationVersion;
import fr.sword.xsd.solon.epp.NiveauLectureCode;
import fr.sword.xsd.solon.epp.NotifierTransitionRequest;
import fr.sword.xsd.solon.epp.NotifierTransitionResponse;
import fr.sword.xsd.solon.epp.Organisme;
import fr.sword.xsd.solon.epp.RapportParlement;
import fr.sword.xsd.solon.epp.RechercherEvenementRequest;
import fr.sword.xsd.solon.epp.RechercherEvenementResponse;
import fr.sword.xsd.solon.epp.SupprimerVersionRequest;
import fr.sword.xsd.solon.epp.SupprimerVersionResponse;
import fr.sword.xsd.solon.epp.TypeActe;
import fr.sword.xsd.solon.epp.ValidationType;
import fr.sword.xsd.solon.epp.ValiderVersionRequest;
import fr.sword.xsd.solon.epp.ValiderVersionResponse;
import fr.sword.xsd.solon.epp.Version;

public class EvenementServiceImpl implements EvenementService {

	private static final String		DOSSIER_NOT_FOUND_EXCEPTION								= "DossierNotFoundException";
	private static final String		ECHEC_TRANSITION_TRAITER								= "Echec de transition 'à traiter'";

	private static final String		ACTIVE													= "active";

	private static final String		SOLON_MGPP_POILITIQUE_GENERALE_SEQUENCER				= "MGPP_POILITIQUE_GENERALE_SEQUENCER_";
	private static final String		SOLON_MGPP_SUJET_DETERMINE_SEQUENCER					= "MGPP_SUJET_DETERMINE_SEQUENCER_";
	private static final String		SOLON_MGPP_ARTICLE_28_3C_SEQUENCER						= "MGPP_ARTICLE_28_3C_SEQUENCER_";
	private static final String		SOLON_MGPP_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES_SEQUENCER	= "MGPP_DOCUMENTS_TRANSMIS_SEQUENCER_";
	private static final String		DECLARATION_POILITIQUE_GENERALE							= "PG";
	private static final String		DECLARATION_SUJET_DETERMINE								= "SD";
	private static final String		JOURS_SUPPLÉMENTAIRES_DE_SEANCE							= "JSS";
	private static final String		DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES						= "DOC";

	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger	LOGGER													= STLogFactory
																									.getLog(EvenementServiceImpl.class);

	@Override
	public EvenementDTO findEvenement(final String idEvenement, final String numVersion, final CoreSession session,
			boolean gererFichePresentation) throws ClientException {
		return findEvenement(idEvenement, numVersion, session, gererFichePresentation, true);
	}

	@Override
	public EvenementDTO findEvenement(final String idEvenement, final String numVersion, final CoreSession session,
			boolean gererFichePresentation, final boolean returnErrorIfNull) throws ClientException {

		// chargement des tables de references lors du chargement du premier evenement
		SolonMgppServiceLocator.getTableReferenceService().loadAllTableReference(session);

		String cacheVersion = ACTIVE;

		if (StringUtils.isNotBlank(numVersion)) {
			cacheVersion = numVersion;
		}

		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final ChercherEvenementRequest chercherEvenementRequest = new ChercherEvenementRequest();

		final EvtId evtId = new EvtId();
		evtId.setId(idEvenement);

		if (StringUtils.isNotBlank(numVersion)) {
			final String[] vers = numVersion.split("\\.");
			if (vers.length == 2) {
				final Version version = new Version();
				version.setMajeur(Integer.parseInt(vers[0]));
				version.setMineur(Integer.parseInt(vers[1]));
				evtId.setVersion(version);
			}
		}

		chercherEvenementRequest.getIdEvenement().add(evtId);

		ChercherEvenementResponse chercherEvenementResponse = null;

		try {
			chercherEvenementResponse = wsEvenement.chercherEvenement(chercherEvenementRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}
		if ((chercherEvenementResponse == null || chercherEvenementResponse.getStatut() == null || !TraitementStatut.OK
				.equals(chercherEvenementResponse.getStatut())) && !returnErrorIfNull) {
			return null;
		}
		if (chercherEvenementResponse == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la récupération de la communication a échoué.");
		} else if (chercherEvenementResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(chercherEvenementResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la récupération de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(chercherEvenementResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}

		if (chercherEvenementResponse.getEvenement().size() > 1) {
			throw new ClientException("La récupération de la version " + cacheVersion
					+ " de la communication a retourné plusieurs résultats.");
		}

		final EvenementDTO evenementDTO = new EvenementDTOImpl();

		for (final EppEvtContainer eppEvtContainer : chercherEvenementResponse.getEvenement()) {

			// construction générique de l'evenementDTO
			EvenementBuilder.getInstance().buildEvenementDTOFromContainer(evenementDTO, eppEvtContainer, session);
			if (gererFichePresentation) {
				SolonMgppServiceLocator.getDossierService().gererFichePresentation(eppEvtContainer, false, session);

				// Création de la navette si besoin
				SolonMgppServiceLocator.getNavetteService().addNavetteToFicheLoi(session, eppEvtContainer);
			}
			break;

		}

		return evenementDTO;

	}

	@Override
	public EvenementDTO createEmptyEvenementDTO(final String typeEvenement, CoreSession session) throws ClientException {
		final EvenementDTO evenementDTO = new EvenementDTOImpl();
		final Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put(EvenementDTO.NAME, typeEvenement);
		evenementDTO.put(EvenementDTO.TYPE_EVENEMENT, (Serializable) map);

		// recupération du container
		final EppBaseEvenement eppBaseEvenement = ContainerBuilder.getInstance().buildEppEvtFromEvenementDTO(
				evenementDTO, session);

		if (eppBaseEvenement != null) {
			// initialisation générique de l'evenementDTO
			EvenementBuilder.getInstance().initEvenementDTOWithEppEvt(evenementDTO, eppBaseEvenement, session);

			// remap des pieces jointes
			PieceJointeBuilder.getInstance().buildPieceJointeWithEppEvt(evenementDTO, eppBaseEvenement, session);
		}
		return evenementDTO;

	}

	@Override
	public EppEvtContainer createEppEvtContainerFromEvenementDTO(final EvenementDTO evenementDTO,
			final Boolean hasContenu, CoreSession session) throws ClientException {

		final EppBaseEvenement eppBaseEvenement = ContainerBuilder.getInstance().buildEppEvtFromEvenementDTO(
				evenementDTO, session);

		if (eppBaseEvenement != null) {
			PieceJointeBuilder.getInstance().buildPieceJointeFromEvenementDTO(evenementDTO, eppBaseEvenement,
					hasContenu, session);

			final EppEvtContainer eppEvtContainer = new EppEvtContainer();
			setEppEvtInEppEvtContainerAndAssignType(eppBaseEvenement, eppEvtContainer,
					evenementDTO.getTypeEvenementName(), session);

			return eppEvtContainer;
		}
		return null;

	}

	/**
	 * set l'{@link EppBaseEvenement} dans le bon setter de {@link EppEvtContainer} et renseigne le
	 * {@link EvenementType}
	 * 
	 * @param eppBaseEvenement
	 * @param eppEvtContainer
	 * @param string
	 */
	private void setEppEvtInEppEvtContainerAndAssignType(final EppBaseEvenement eppBaseEvenement,
			final EppEvtContainer eppEvtContainer, final String typeEvenement, final CoreSession session) {

		Boolean set = Boolean.FALSE;

		String evenementType = typeEvenement.replaceAll(BuilderConstant.NOT_ALPHA, "");

		set = setTypeInContainer(eppBaseEvenement, eppEvtContainer, set, evenementType, session);

		if (!set) {
			// retry sans le EVT_ en prefix ie EVT_ALERT
			final String type = typeEvenement.replaceFirst("EVT_", "");
			evenementType = type.replaceAll(BuilderConstant.NOT_ALPHA, "");
			setTypeInContainer(eppBaseEvenement, eppEvtContainer, set, evenementType, session);
		}

	}

	private Boolean setTypeInContainer(final EppBaseEvenement eppBaseEvenement, final EppEvtContainer eppEvtContainer,
			Boolean set, final String evenementType, final CoreSession session) {
		// search with same formatter
		for (final EvenementType evenementTypeEnum : EvenementType.values()) {
			if (evenementTypeEnum.value().replaceAll(BuilderConstant.NOT_ALPHA, "").equals(evenementType)) {
				Field field = null;
				// get correct EppEvt
				try {
					field = eppEvtContainer.getClass().getDeclaredField(evenementTypeEnum.value().toLowerCase());
					if (field != null) {
						field.setAccessible(true);
						field.set(eppEvtContainer, eppBaseEvenement);
						eppEvtContainer.setType(evenementTypeEnum);
						set = Boolean.TRUE;
					}
				} catch (final Exception e) {
					try {
						field = eppEvtContainer.getClass().getDeclaredField(
								evenementTypeEnum.value().toLowerCase().replaceAll(BuilderConstant.NOT_ALPHA, ""));
						if (field != null) {
							field.setAccessible(true);
							field.set(eppEvtContainer, eppBaseEvenement);
							eppEvtContainer.setType(evenementTypeEnum);
							set = Boolean.TRUE;
						}
					} catch (final Exception e1) {
						try {
							final String type = ContainerBuilder.EVT_PREFIX
									+ evenementType.substring(0, 1).toUpperCase()
									+ evenementType.substring(1, evenementType.length()).toLowerCase();
							field = eppEvtContainer.getClass().getDeclaredField(type);
							if (field != null) {
								field.setAccessible(true);
								field.set(eppEvtContainer, eppBaseEvenement);
								eppEvtContainer.setType(evenementTypeEnum);
								set = Boolean.TRUE;
							}
						} catch (final Exception e2) {
							try {
								final String type = evenementTypeEnum.value().toLowerCase()
										.replaceAll(BuilderConstant.NOT_ALPHA, "")
										.replaceAll(ContainerBuilder.QUATER_LOWER, ContainerBuilder.QUATER_UPPER);
								field = eppEvtContainer.getClass().getDeclaredField(type);
								if (field != null) {
									field.setAccessible(true);
									field.set(eppEvtContainer, eppBaseEvenement);
									eppEvtContainer.setType(evenementTypeEnum);
									set = Boolean.TRUE;
								}
							} catch (final Exception e3) {
								try {
									final String type = evenementTypeEnum.value().toLowerCase()
											.replaceAll(BuilderConstant.NOT_ALPHA, "")
											.replaceAll(ContainerBuilder.TER_LOWER, ContainerBuilder.TER_UPPER);
									field = eppEvtContainer.getClass().getDeclaredField(type);
									if (field != null) {
										field.setAccessible(true);
										field.set(eppEvtContainer, eppBaseEvenement);
										eppEvtContainer.setType(evenementTypeEnum);
										set = Boolean.TRUE;
									}
								} catch (final Exception e4) {
									try {
										final String type = evenementTypeEnum.value().toLowerCase()
												.replaceAll(BuilderConstant.NOT_ALPHA, "")
												.replaceAll(ContainerBuilder.BISAB_LOWER, ContainerBuilder.BISAB_UPPER);
										field = eppEvtContainer.getClass().getDeclaredField(type);
										if (field != null) {
											field.setAccessible(true);
											field.set(eppEvtContainer, eppBaseEvenement);
											eppEvtContainer.setType(evenementTypeEnum);
											set = Boolean.TRUE;
										}
									} catch (final Exception e5) {
										try {
											final String type = evenementTypeEnum.value().toLowerCase()
													.replaceAll(BuilderConstant.NOT_ALPHA, "")
													.replaceAll(ContainerBuilder.BIS_LOWER, ContainerBuilder.BIS_UPPER);
											field = eppEvtContainer.getClass().getDeclaredField(type);
											if (field != null) {
												field.setAccessible(true);
												field.set(eppEvtContainer, eppBaseEvenement);
												eppEvtContainer.setType(evenementTypeEnum);
												set = Boolean.TRUE;
											}
										} catch (final Exception e6) {

											try {
												final String type = evenementTypeEnum
														.value()
														.toLowerCase()
														.replaceAll(BuilderConstant.NOT_ALPHA, "")
														.replaceAll(ContainerBuilder.QUINQUIES_LOWER,
																ContainerBuilder.QUINQUIES_UPPER);
												field = eppEvtContainer.getClass().getDeclaredField(type);
												if (field != null) {
													field.setAccessible(true);
													field.set(eppEvtContainer, eppBaseEvenement);
													eppEvtContainer.setType(evenementTypeEnum);
													set = Boolean.TRUE;
												}
											} catch (final Exception e7) {
												LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e7);
											}
										}
									}
								}
							}
						}
					}

				} finally {
					if (field != null) {
						field.setAccessible(false);
					}
				}
				break;
			}
		}
		return set;
	}

	@Override
	public EvenementDTO initialiserEvenement(final String typeEvenement, final CoreSession session)
			throws ClientException {
		return initialiserEvenementSuccessif(null, typeEvenement, session);
	}

	@Override
	public void createEvenement(final EvenementDTO evenementDTO, final boolean publie, final CoreSession session)
			throws ClientException {
		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();

		final CreationType creationType = extractModeCreation(evenementDTO, publie, session);
		creerVersionRequest.setModeCreation(creationType);

		final EppEvtContainer eppEvtContainer = createEppEvtContainerFromEvenementDTO(evenementDTO, Boolean.TRUE,
				session);

		creerDeuxiemeCommunication(evenementDTO, session, wsEvenement, creerVersionRequest, eppEvtContainer);

		creerVersionRequest.setEvenement(eppEvtContainer);

		CreerVersionResponse creerVersionResponse = null;
		try {
			creerVersionResponse = wsEvenement.creerVersion(creerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
			throw new ClientException(e);
		}

		if (creerVersionResponse == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
		} else if (creerVersionResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(creerVersionResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(creerVersionResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}
		// Reconstruire l'evenement du Response, faut vider pour eviter duplication des champs de type list
		evenementDTO.clear();
		EvenementBuilder.getInstance().buildEvenementDTOFromContainer(evenementDTO,
				creerVersionResponse.getEvenement(), session);
		SolonMgppServiceLocator.getDossierService().gererFichePresentation(creerVersionResponse.getEvenement(), publie,
				session);

		// Création de la navette si besoin
		SolonMgppServiceLocator.getNavetteService().addNavetteToFicheLoi(session, creerVersionResponse.getEvenement());
	}

	private void creerDeuxiemeCommunication(final EvenementDTO evenementDTO, final CoreSession session,
			final WSEvenement wsEvenement, final CreerVersionRequest creerVersionRequest,
			final EppEvtContainer eppEvtContainer) throws ClientException {

		// pour une alerte creation de 2 communication
		EppEvtAlerte evtAlerte = null;
		if (eppEvtContainer.getEvtAlerte01() != null) {
			evtAlerte = eppEvtContainer.getEvtAlerte01();
		} else if (eppEvtContainer.getEvtAlerte03() != null) {
			evtAlerte = eppEvtContainer.getEvtAlerte03();
		} else if (eppEvtContainer.getEvtAlerte04() != null) {
			evtAlerte = eppEvtContainer.getEvtAlerte04();
		} else if (eppEvtContainer.getEvtAlerte05() != null) {
			evtAlerte = eppEvtContainer.getEvtAlerte05();
		} else if (eppEvtContainer.getEvtAlerte06() != null) {
			evtAlerte = eppEvtContainer.getEvtAlerte06();
		} else if (eppEvtContainer.getEvtAlerte07() != null) {
			evtAlerte = eppEvtContainer.getEvtAlerte07();
		} else if (eppEvtContainer.getEvtAlerte08() != null) {
			evtAlerte = eppEvtContainer.getEvtAlerte08();
		} else if (eppEvtContainer.getEvtAlerte09() != null) {
			evtAlerte = eppEvtContainer.getEvtAlerte09();
		} else if (eppEvtContainer.getEvtAlerte10() != null) {
			evtAlerte = eppEvtContainer.getEvtAlerte10();
		}

		if (evtAlerte != null) {

			evtAlerte.setDestinataire(Institution.ASSEMBLEE_NATIONALE);
			evtAlerte.getCopie().clear();
			evtAlerte.getCopie().add(Institution.SENAT);

			creerVersionRequest.setEvenement(eppEvtContainer);

			CreerVersionResponse creerVersionResponse = null;
			try {
				creerVersionResponse = wsEvenement.creerVersion(creerVersionRequest);
			} catch (final HttpTransactionException e) {
				LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
				throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
			} catch (final Exception e) {
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
				throw new ClientException(e);
			}

			if (creerVersionResponse == null) {
				throw new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
			} else if (creerVersionResponse.getStatut() == null
					|| !TraitementStatut.OK.equals(creerVersionResponse.getStatut())) {

				ClientException cltExp = new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
								+ WSErrorHelper.buildCleanMessage(creerVersionResponse.getMessageErreur()));
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, cltExp);
				throw cltExp;
			}

			evtAlerte.setDestinataire(Institution.SENAT);
			evtAlerte.getCopie().clear();
			evtAlerte.getCopie().add(Institution.ASSEMBLEE_NATIONALE);
		}
	}

	private CreationType extractModeCreation(final EvenementDTO evenementDTO, final boolean publie,
			final CoreSession session) {
		CreationType creationType = publie ? CreationType.PUBLIER : CreationType.CREER_BROUILLON;
		try {
			final ModeCreationVersion modeCreation = (evenementDTO.getVersionCouranteModeCreation() == null ? null
					: ModeCreationVersion.fromValue(evenementDTO.getVersionCouranteModeCreation()));
			if (modeCreation != null) {
				switch (modeCreation) {
					case BROUILLON_COMPLETION:
					case PUBLIE_COMPLETION:
						creationType = publie ? CreationType.COMPLETER_PUBLIER : CreationType.COMPLETER_BROUILLON;
						break;
					case BROUILLON_RECTIFICATION:
					case PUBLIE_RECTIFICATION:
						creationType = publie ? CreationType.RECTIFIER_PUBLIER : CreationType.RECTIFIER_BROUILLON;
						break;
					default:
						creationType = publie ? CreationType.PUBLIER : CreationType.CREER_BROUILLON;
						break;
				}
			} else {
				creationType = publie ? CreationType.PUBLIER : CreationType.CREER_BROUILLON;
			}
		} catch (final Exception e) {
			LOGGER.warn(session, MgppLogEnumImpl.FAIL_GET_MODE_CREATION_TEC,
					evenementDTO.getVersionCouranteModeCreation(), e);
			creationType = publie ? CreationType.PUBLIER : CreationType.CREER_BROUILLON;
		}
		return creationType;
	}

	@Override
	public void createEvenementEppEvt01Brouillon(final Dossier dossier, final CoreSession session)
			throws ClientException {
		final ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator.getParametreMgppService().findParametrageMgpp(
				session);
		final TableReferenceService tableReference = SolonMgppServiceLocator.getTableReferenceService();
		final String numeroNor = dossier.getNumeroNor();

		// recherche du dossier dans EPP
		WSEpp wsEpp = null;

		try {
			wsEpp = SolonMgppWsLocator.getWSEpp(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final ChercherDossierRequest chercherDossierRequest = new ChercherDossierRequest();
		chercherDossierRequest.getIdDossier().add(numeroNor);
		ChercherDossierResponse chercherDossierResponse = null;

		try {
			chercherDossierResponse = wsEpp.chercherDossier(chercherDossierRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, e);
			throw new ClientException(e);
		}

		if (chercherDossierResponse == null) {
			throw new ClientException("Erreur de communication avec SOLON EPP, la récupération du dossier a échoué.");
		} else if ((TraitementStatut.KO.equals(chercherDossierResponse.getStatut()) && chercherDossierResponse
				.getMessageErreur().contains(DOSSIER_NOT_FOUND_EXCEPTION))
				|| (TraitementStatut.OK.equals(chercherDossierResponse.getStatut()) && chercherDossierResponse
						.getDossier().isEmpty())) {
			// pas de dossier => creation de l'evenement EppEvt01 a l'etat Brouillon
			WSEvenement wsEvenement = null;

			try {
				wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
			} catch (final WSProxyFactoryException e) {
				LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
				throw new ClientException(e);
			}

			// initialisation
			final EvenementDTO evenementDTO = initialiserEvenement(TypeEvenementConstants.TYPE_EVENEMENT_EVT01, session);

			// creation
			final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
			creerVersionRequest.setModeCreation(CreationType.CREER_BROUILLON);

			final EppEvtContainer eppEvtContainer = createEppEvtContainerFromEvenementDTO(evenementDTO, Boolean.TRUE,
					session);

			final EppEvt01 eppEvt01 = eppEvtContainer.getEvt01();
			eppEvt01.setNor(numeroNor);
			eppEvt01.setIdDossier(numeroNor);
			// epp reclame un destinataire...
			eppEvt01.setDestinataire(null); // MANTIS #41885
			// epp reclame une copie...
			eppEvt01.getCopie().clear(); // MANTIS #41885

			// modification du titre acte
			eppEvt01.setIntitule(dossier.getTitreActe());
			eppEvt01.setObjet(cleanTitreActe(dossier));

			// initialisation auteur/co-auteur
			String auteur = parametrageMgpp.getAuteurLex01();
			MandatNode mandatAuteur = tableReference.getMandat(auteur, session);
			eppEvt01.setAuteur(mandatAuteur.toMandatXsd());
			List<Mandat> mandatsCoAut = tableReference.getMandatsByNor(numeroNor.substring(0, 3), session);
			eppEvt01.getCoAuteur().addAll(mandatsCoAut);

			eppEvtContainer.setEvt01(eppEvt01);
			eppEvtContainer.setType(EvenementType.EVT_01);
			creerVersionRequest.setEvenement(eppEvtContainer);

			CreerVersionResponse creerVersionResponse = null;
			try {
				creerVersionResponse = wsEvenement.creerVersion(creerVersionRequest);
			} catch (final HttpTransactionException e) {
				LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
				throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
			} catch (final Exception e) {
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
				throw new ClientException(e);
			}

			if (creerVersionResponse == null) {
				throw new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
			} else if (creerVersionResponse.getStatut() == null
					|| !TraitementStatut.OK.equals(creerVersionResponse.getStatut())) {
				ClientException clientExc = new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
								+ WSErrorHelper.buildCleanMessage(creerVersionResponse.getMessageErreur()));
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
				throw clientExc;
			}

			if (creerVersionResponse.getEvenement() != null) {
				final DossierService dossierService = SolonMgppServiceLocator.getDossierService();
				// creation de la fiche dossier suite a la creation d'une communication 01
				dossierService.attachIdDossierToDosier(session, dossier, numeroNor);
			}
		} else if (!TraitementStatut.KO.equals(chercherDossierResponse.getStatut())) {
			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la récupération du dossier a échoué."
							+ WSErrorHelper.buildCleanMessage(chercherDossierResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, clientExc);
			throw clientExc;
		}
	}

	@Override
	public String cleanTitreActe(final Dossier dossier) {
		String cleanTypeActe = null;
		if (StringUtils.isNotBlank(dossier.getTitreActe())) {
			cleanTypeActe = dossier.getTitreActe().replaceFirst("(?iu)Projet de loi constitutionnel", "");
			cleanTypeActe = cleanTypeActe.replaceFirst("(?iu)Projet de loi organique", "");
			cleanTypeActe = cleanTypeActe.replaceFirst("(?iu)Projet de loi", "");
			// trim
			cleanTypeActe = cleanTypeActe.trim();
		}

		return cleanTypeActe;
	}

	@Override
	public EvenementDTO initialiserEvenementSuccessif(final EvenementDTO evenementDTOPrecedent,
			final String typeEvenement, final CoreSession session) throws ClientException {
		if (StringUtils.isBlank(typeEvenement)) {
			throw new ClientException("Le type de communication est obligatoire");
		}

		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final InitialiserEvenementRequest initialiserEvenementRequest = new InitialiserEvenementRequest();

		EvenementType evenementType = null;

		String evtType = typeEvenement.replaceAll(BuilderConstant.NOT_ALPHA, "");

		// search with same formatter
		for (final EvenementType evenementTypeEnum : EvenementType.values()) {
			if (evenementTypeEnum.value().replaceAll(BuilderConstant.NOT_ALPHA, "").equals(evtType)) {
				evenementType = evenementTypeEnum;
				break;
			}
		}

		if (evenementType == null) {
			// retry sans le EVT_ en prefix ie EVT_ALERTE
			final String type = typeEvenement.replaceFirst("EVT_", "");
			evtType = type.replaceAll(BuilderConstant.NOT_ALPHA, "");

			// search with same formatter
			for (final EvenementType evenementTypeEnum : EvenementType.values()) {
				if (evenementTypeEnum.value().replaceAll(BuilderConstant.NOT_ALPHA, "").equals(evtType)) {
					evenementType = evenementTypeEnum;
					break;
				}
			}
		}

		if (evenementType == null) {
			throw new ClientException("Le type de communication n'a pas été trouvé dans SOLON EPP (" + typeEvenement
					+ ")");
		}

		initialiserEvenementRequest.setTypeEvenement(evenementType);

		if (evenementDTOPrecedent != null) {
			initialiserEvenementRequest.setIdEvenementPrecedent(evenementDTOPrecedent.getIdEvenement());
		}

		initialiserEvenementRequest.setAllMeta(Boolean.FALSE);

		InitialiserEvenementResponse initialiserEvenementResponse = null;

		try {
			initialiserEvenementResponse = wsEvenement.initialiserEvenement(initialiserEvenementRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_INIT_COMM_TEC, e);
			throw new ClientException(e);
		}

		if (initialiserEvenementResponse == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, l'initialisation de la communication a échoué.");
		} else if (initialiserEvenementResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(initialiserEvenementResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, l'initialisation de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(initialiserEvenementResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}

		final EppEvtContainer eppEvtContainer = initialiserEvenementResponse.getEvenement();

		if (eppEvtContainer.getEvt01() != null) {
			final EppEvt01 eppEvt01 = eppEvtContainer.getEvt01();
			eppEvt01.setNiveauLecture(null);
		} else if (eppEvtContainer.getEvt13() != null) {
			final EppEvt13 eppEvt13 = eppEvtContainer.getEvt13();
			eppEvt13.setNiveauLecture(null);
		} else if (eppEvtContainer.getEvt10() != null) {

			final EppEvt10 eppEvt10 = eppEvtContainer.getEvt10();
			eppEvt10.setNiveauLecture(null);
			// Pour l'engagement de la procédure accélérée, l'assemblée destinataire de la communication est l'assemblée
			// de dépôt
			final FicheLoi ficheLoi = SolonMgppServiceLocator.getDossierService().findOrCreateFicheLoi(session,
					eppEvt10.getIdDossier());

			if (StringUtils.isNotEmpty(ficheLoi.getAssembleeDepot())) {
				try {
					final Institution destinataire = Institution.fromValue(ficheLoi.getAssembleeDepot());
					eppEvt10.setDestinataire(destinataire);
					eppEvt10.getCopie().clear();

					// init copie en fonction de emetteur/destinataire
					final MetaDonneesDescriptor metaDonneesDescriptor = SolonMgppServiceLocator.getMetaDonneesService()
							.getEvenementType(TypeEvenementConstants.TYPE_EVENEMENT_EVT10);

					if (metaDonneesDescriptor != null) {
						final Map<String, PropertyDescriptor> mapProperty = metaDonneesDescriptor.getEvenement()
								.getProperty();

						// si emetteur et destinataire sont settes on met les autres en copie
						final PropertyDescriptor pCopie = mapProperty.get(EvenementDTO.DESTINATAIRE_COPIE);
						if (pCopie != null) {
							final List<String> institutions = pCopie.getListInstitutions();

							if (eppEvt10.getEmetteur() != null) {
								institutions.remove(eppEvt10.getEmetteur().value());
							}

							if (eppEvt10.getDestinataire() != null) {
								institutions.remove(eppEvt10.getDestinataire().value());
							}

							for (final String institution : institutions) {
								eppEvt10.getCopie().add(Institution.fromValue(institution));
							}

						}
					}

				} catch (final Exception e) {
					LOGGER.warn(session, STLogEnumImpl.FAIL_INIT_COMM_TEC, e);
				}
			}

		} else {
			String idDossier = evenementDTOPrecedent == null ? null : evenementDTOPrecedent.getIdDossier();
			EppBaseEvenement pgEvt = this.getPolitiqueGeneraleEvt(eppEvtContainer);
			if (pgEvt != null) {
				idDossier = idDossier == null ? this.generateIdDossierPG() : idDossier;
				pgEvt.setIdDossier(idDossier);
			}

			if (pgEvt == null) {
				pgEvt = this.getSujetDetermineEvt(eppEvtContainer);
				if (pgEvt != null) {
					idDossier = idDossier == null ? this.generateIdDossierSD() : idDossier;
					pgEvt.setIdDossier(idDossier);
				}
			}

			if (pgEvt == null) {
				pgEvt = this.getArticle283CEvt(eppEvtContainer);
				if (pgEvt != null) {
					idDossier = idDossier == null ? this.generateIdDossierJSS() : idDossier;
					pgEvt.setIdDossier(idDossier);
				}
			}

			if (pgEvt == null) {
				pgEvt = this.getDocsTransmisAuxAssembleesEvt(eppEvtContainer);
				if (pgEvt != null) {
					idDossier = idDossier == null ? this.generateIdDossierDOC() : idDossier;
					pgEvt.setIdDossier(idDossier);
				}
			}

			// RG-SD-CRE-02
			if (evenementDTOPrecedent != null
					&& TypeEvenementConstants.SD01.equals(evenementDTOPrecedent.getTypeEvenementName())
					&& eppEvtContainer.getSD02() != null) {
				EppBaseEvenement eppBaseEvenementPrecedent = buildEppEvtFromEvenementDTO(evenementDTOPrecedent, session);
				if (eppBaseEvenementPrecedent instanceof EppSD01) {
					EppSD01 eppSD01 = (EppSD01) eppBaseEvenementPrecedent;
					if (eppSD01.getGroupeParlementaire() != null) {
						for (Organisme groupe : eppSD01.getGroupeParlementaire()) {
							if (!isOrganismePresent(eppEvtContainer.getSD02().getGroupeParlementaire(), groupe)) {
								eppEvtContainer.getSD02().getGroupeParlementaire().add(groupe);
							}
						}
					}
				}
			}

			if (evenementDTOPrecedent != null
					&& TypeEvenementConstants.PG01.equals(evenementDTOPrecedent.getTypeEvenementName())
					&& eppEvtContainer.getPG02() != null) {
				EppBaseEvenement eppBaseEvenementPrecedent = buildEppEvtFromEvenementDTO(evenementDTOPrecedent, session);
				if (eppBaseEvenementPrecedent instanceof EppPG01) {
					EppPG01 eppPG01 = (EppPG01) eppBaseEvenementPrecedent;
					FichePresentationDPG ficheDPG = SolonMgppServiceLocator.getDossierService()
							.findFichePresentationDPG(session, eppPG01.getIdDossier());
					if (ficheDPG != null && ficheDPG.getDatePresentation() != null) {
						GregorianCalendar currentCalendar = new GregorianCalendar();
						currentCalendar.setTime(ficheDPG.getDatePresentation().getTime());
						try {
							eppEvtContainer.getPG02().setDatePresentation(
									DatatypeFactory.newInstance().newXMLGregorianCalendar(currentCalendar));
						} catch (DatatypeConfigurationException e) {
							LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_COMM_TEC, e);
						}
					} else if (eppPG01.getDatePresentation() != null) {
						eppEvtContainer.getPG02().setDatePresentation(eppPG01.getDatePresentation());
					}
					if (ficheDPG != null && ficheDPG.getObjet() != null) {
						eppEvtContainer.getPG02().setObjet(ficheDPG.getObjet());
					} else if (eppPG01.getObjet() != null) {
						eppEvtContainer.getPG02().setObjet(eppPG01.getObjet());
					}
				}
			}

		}

		final EvenementDTO evenementDTO = new EvenementDTOImpl();

		// construction générique de l'evenementDTO
		EvenementBuilder.getInstance().buildEvenementDTOFromContainer(evenementDTO, eppEvtContainer, session);

		return evenementDTO;
	}

	@Override
	public void rectifierEvenement(final EvenementDTO evenementDTO, final boolean publier, final CoreSession session)
			throws ClientException {

		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
		creerVersionRequest
				.setModeCreation(publier ? CreationType.RECTIFIER_PUBLIER : CreationType.RECTIFIER_BROUILLON);

		final EppEvtContainer eppEvtContainer = createEppEvtContainerFromEvenementDTO(evenementDTO, Boolean.TRUE,
				session);
		creerVersionRequest.setEvenement(eppEvtContainer);

		CreerVersionResponse creerVersionResponse = null;
		try {
			creerVersionResponse = wsEvenement.creerVersion(creerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_COMM_TEC, "Rectification échouée", e);
			throw new ClientException(e);
		}

		if (creerVersionResponse == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la rectification de la communication a échoué.");
		} else if (creerVersionResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(creerVersionResponse.getStatut())) {
			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la rectification de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(creerVersionResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}

		SolonMgppServiceLocator.getDossierService().gererFichePresentation(creerVersionResponse.getEvenement(),
				publier, session);

	}

	@Override
	public void completerEvenement(final EvenementDTO evenementDTO, final boolean publier, final CoreSession session)
			throws ClientException {
		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
		creerVersionRequest
				.setModeCreation(publier ? CreationType.COMPLETER_PUBLIER : CreationType.COMPLETER_BROUILLON);

		final EppEvtContainer eppEvtContainer = createEppEvtContainerFromEvenementDTO(evenementDTO, Boolean.TRUE,
				session);
		creerVersionRequest.setEvenement(eppEvtContainer);

		CreerVersionResponse creerVersionResponse = null;
		try {
			creerVersionResponse = wsEvenement.creerVersion(creerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_COMM_TEC, "Echec de complétion", e);
			throw new ClientException(e);
		}

		if (creerVersionResponse == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la completion de la communication a échoué.");
		} else if (creerVersionResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(creerVersionResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la completion de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(creerVersionResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}

		SolonMgppServiceLocator.getDossierService().gererFichePresentation(creerVersionResponse.getEvenement(),
				publier, session);
	}

	@Override
	public void traiterEvenement(final MessageDTO messageDTO, final EvenementDTO evenementDTO,
			final CoreSession session, final DossierLink dossierLink) throws ClientException {

		if (messageDTO == null) {
			throw new ClientException("Impossible de traiter cette communication, message vide");
		}

		if (evenementDTO == null) {
			throw new ClientException("Impossible de traiter cette communication, communication vide");
		}

		WSEpp wsEpp = null;

		try {
			wsEpp = SolonMgppWsLocator.getWSEpp(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final NotifierTransitionRequest notifierTransitionRequest = new NotifierTransitionRequest();
		notifierTransitionRequest.setEtat(EtatMessage.TRAITE);
		notifierTransitionRequest.setIdEvenement(evenementDTO.getIdEvenement());

		NotifierTransitionResponse notifierTransitionResponse = null;
		try {
			notifierTransitionResponse = wsEpp.notifierTransition(notifierTransitionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_COMM_TEC, ECHEC_TRANSITION_TRAITER, e);
			throw new ClientException(e);
		}

		if (notifierTransitionResponse == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, le passage à traiter de la communication a échoué.");
		} else if (notifierTransitionResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(notifierTransitionResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, le passage à traiter de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(notifierTransitionResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}

		// RG-EVT-TRA-06
		if (EtatEvenement.ANNULE.value().equals(messageDTO.getEtatEvenement())) {

			return;

		} else {
			final DossierService dossierService = SolonMgppServiceLocator.getDossierService();

			dossierService.updateFicheLoi(session, evenementDTO);

			final Dossier dossier = dossierService.findDossierFromIdDossier(session, evenementDTO.getIdDossier());

			if (dossierLink != null) {
				// RG-EVT-TRA-02
				SolonMgppServiceLocator.getDossierService().updateBordereau(session, evenementDTO, dossier);
			}

			dossierService.updateFondDossier(session, evenementDTO, dossier);

		}

		final DossierService dossierService = SolonMgppServiceLocator.getDossierService();

		if (evenementDTO.getTypeEvenementName().equals(TypeEvenementConstants.TYPE_EVENEMENT_EVT51)) {
			final FichePresentationOEP ficheOEP = SolonMgppServiceLocator.getDossierService().findFicheOEP(session,
					evenementDTO.getIdDossier());
			if (ficheOEP != null) {

				EppBaseEvenement eppBaseEvenement = buildEppEvtFromEvenementDTO(evenementDTO, session);
				if (eppBaseEvenement instanceof EppEvt51) {
					final EppEvt51 eppEvt51 = (EppEvt51) eppBaseEvenement;
					final Calendar dateJO = eppEvt51.getDateJo() != null ? eppEvt51.getDateJo().toGregorianCalendar()
							: null;

					if (Institution.ASSEMBLEE_NATIONALE.equals(eppEvt51.getEmetteur())) {

						final List<DocumentModel> listRepresentant = dossierService.fetchRepresentantOEP(session,
								VocabularyConstants.REPRESENTANT_TYPE_AN, ficheOEP.getDocument().getId());
						createRepresentantFromEvt51(session, ficheOEP, listRepresentant, eppEvt51.getTitulaires(),
								VocabularyConstants.REPRESENTANT_TYPE_AN, VocabularyConstants.FONCTION_TITULAIRE_ID,
								dateJO);
						createRepresentantFromEvt51(session, ficheOEP, listRepresentant, eppEvt51.getSuppleant(),
								VocabularyConstants.REPRESENTANT_TYPE_AN, VocabularyConstants.FONCTION_SUPPLEANT_ID,
								dateJO);
						session.save();

					} else if (Institution.SENAT.equals(eppEvt51.getEmetteur())) {

						final List<DocumentModel> listRepresentant = dossierService.fetchRepresentantOEP(session,
								VocabularyConstants.REPRESENTANT_TYPE_SE, ficheOEP.getDocument().getId());
						createRepresentantFromEvt51(session, ficheOEP, listRepresentant, eppEvt51.getTitulaires(),
								VocabularyConstants.REPRESENTANT_TYPE_SE, VocabularyConstants.FONCTION_TITULAIRE_ID,
								dateJO);
						createRepresentantFromEvt51(session, ficheOEP, listRepresentant, eppEvt51.getSuppleant(),
								VocabularyConstants.REPRESENTANT_TYPE_SE, VocabularyConstants.FONCTION_SUPPLEANT_ID,
								dateJO);
						session.save();

					}
				}
			}
		}

		// Règle RG-PG-CRE-04
		if (TypeEvenementConstants.isEvenementPG(evenementDTO.getTypeEvenementName())) {
			EppBaseEvenement eppBaseEvenement = buildEppEvtFromEvenementDTO(evenementDTO, session);
			if (eppBaseEvenement instanceof EppPG04) {
				EppPG04 eppPG04 = (EppPG04) eppBaseEvenement;
				// trouver la fiche de presentation FichePresentationDPG
				FichePresentationDPG fichePresentationDPG = dossierService.findFichePresentationDPG(session,
						eppPG04.getIdDossier());
				fichePresentationDPG.assignData(eppPG04);
				session.saveDocument(fichePresentationDPG.getDocument());
			}

		}

		// Règle RG-SD-CRE-01
		if (TypeEvenementConstants.SD01.equals(evenementDTO.getTypeEvenementName())) {
			EppBaseEvenement eppBaseEvenement = buildEppEvtFromEvenementDTO(evenementDTO, session);
			if (eppBaseEvenement instanceof EppSD01) {
				EppSD01 eppSD01 = (EppSD01) eppBaseEvenement;
				FichePresentationSD fichePresentationSD = dossierService.findOrCreateFicheSD(session,
						evenementDTO.getIdDossier());
				fichePresentationSD.setObjet(eppSD01.getObjet());
				if (eppSD01.getDateDemande() != null) {
					fichePresentationSD.setDateDemande(eppSD01.getDateDemande().toGregorianCalendar());
				}

				List<String> availableGroupeParlementaire = new ArrayList<String>();
				if (eppSD01.getGroupeParlementaire() != null) {
					for (final Organisme organisme : eppSD01.getGroupeParlementaire()) {
						availableGroupeParlementaire.add(organisme.getId());
					}
					fichePresentationSD.setGroupeParlementaire(availableGroupeParlementaire);
				}
				session.saveDocument(fichePresentationSD.getDocument());
			}
		}

		// Règle RG-SD-CRE-04
		if (TypeEvenementConstants.SD03.equals(evenementDTO.getTypeEvenementName())) {
			EppBaseEvenement eppBaseEvenement = buildEppEvtFromEvenementDTO(evenementDTO, session);
			if (eppBaseEvenement instanceof EppSD03) {
				EppSD03 eppSD03 = (EppSD03) eppBaseEvenement;
				FichePresentationSD fichePresentationSD = dossierService.findOrCreateFicheSD(session,
						evenementDTO.getIdDossier());
				if (eppSD03.getDateVote() != null) {
					fichePresentationSD.setDateVote(eppSD03.getDateVote().toGregorianCalendar());
				}

				if (eppSD03.getSensVote() != null) {
					fichePresentationSD.setSensAvis(eppSD03.getSensVote().value());
				}

				if (eppSD03.getNombreSuffrage() != null) {
					fichePresentationSD.setSuffrageExprime(eppSD03.getNombreSuffrage().longValue());
				}

				if (eppSD03.getVotePour() != null) {
					fichePresentationSD.setVotePour(eppSD03.getVotePour().longValue());
				}

				if (eppSD03.getVoteContre() != null) {
					fichePresentationSD.setVoteContre(eppSD03.getVoteContre().longValue());
				}

				if (eppSD03.getAbstention() != null) {
					fichePresentationSD.setAbstention(eppSD03.getAbstention().longValue());
				}
				// Save the doc
				session.saveDocument(fichePresentationSD.getDocument());
			}
		}

		if (TypeEvenementConstants.JSS02.equals(evenementDTO.getTypeEvenementName())) {
			EppBaseEvenement eppBaseEvenement = buildEppEvtFromEvenementDTO(evenementDTO, session);
			if (eppBaseEvenement instanceof EppJSS02) {
				EppJSS02 eppJSS02 = (EppJSS02) eppBaseEvenement;
				FichePresentationJSS fichePresentationJSS = dossierService.findOrCreateFicheJSS(session,
						evenementDTO.getIdDossier());
				fichePresentationJSS.setObjet(eppJSS02.getObjet());
				// Save doc
				session.saveDocument(fichePresentationJSS.getDocument());
			}
		}
	}

	private EppBaseEvenement buildEppEvtFromEvenementDTO(final EvenementDTO evenementDTO, final CoreSession session) {
		EppBaseEvenement eppBaseEvenement = null;
		try {
			eppBaseEvenement = ContainerBuilder.getInstance().buildEppEvtFromEvenementDTO(evenementDTO, session);
		} catch (final ClientException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_TRANS_DTO_TEC);
		}
		return eppBaseEvenement;
	}

	private void createRepresentantFromEvt51(final CoreSession session, final FichePresentationOEP fiche,
			final List<DocumentModel> listRepresentant, final List<Mandat> mandats, final String typeRepresentant,
			final String fonction, final Calendar dateJO) throws ClientException {
		for (final Mandat mandat : mandats) {

			Boolean found = Boolean.FALSE;
			for (final DocumentModel documentModel : listRepresentant) {

				final RepresentantOEP representantOEP = documentModel.getAdapter(RepresentantOEP.class);

				if (mandat.getId().equals(representantOEP.getRepresentant())) {
					found = Boolean.TRUE;
					representantOEP.setFonction(fonction);
					session.saveDocument(representantOEP.getDocument());
					break;
				}
			}

			if (!found) {
				// creation d'un nouveau representant
				final DocumentModel modelDesired = new DocumentModelImpl("/case-management/fiche-dossier", ""
						+ Calendar.getInstance().getTimeInMillis(), RepresentantOEP.DOC_TYPE);

				modelDesired.setPathInfo(fiche.getDocument().getPathAsString(), "REP-" + UUID.randomUUID().toString()
						+ "-" + typeRepresentant);
				final RepresentantOEP representantOEP = modelDesired.getAdapter(RepresentantOEP.class);
				representantOEP.setIdFicheRepresentationOEP(fiche.getDocument().getId());
				representantOEP.setRepresentant(mandat.getId());
				representantOEP.setFonction(fonction);
				representantOEP.setDateDebut(dateJO);
				representantOEP.setType(typeRepresentant);

				session.createDocument(representantOEP.getDocument());
			}
		}
	}

	@Override
	public void accepterVersion(final EvenementDTO evenementDTO, final CoreSession session) throws ClientException {
		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final ValiderVersionRequest validerVersionRequest = new ValiderVersionRequest();
		validerVersionRequest.setIdEvenement(evenementDTO.getIdEvenement());
		validerVersionRequest.setModeValidation(ValidationType.ACCEPTER);

		ValiderVersionResponse validerVersionResponse = null;
		try {
			validerVersionResponse = wsEvenement.validerVersion(validerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_COMM_TEC, "Echec d'acceptation de version", e);
			throw new ClientException(e);
		}

		if (validerVersionResponse == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, l'acceptation de la communication a échoué.");
		} else if (validerVersionResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(validerVersionResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, l'acceptation de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(validerVersionResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}
	}

	@Override
	public void rejeterVersion(final EvenementDTO evenementDTO, final CoreSession session) throws ClientException {
		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final ValiderVersionRequest validerVersionRequest = new ValiderVersionRequest();
		validerVersionRequest.setIdEvenement(evenementDTO.getIdEvenement());
		validerVersionRequest.setModeValidation(ValidationType.REJETER);

		ValiderVersionResponse validerVersionResponse = null;
		try {
			validerVersionResponse = wsEvenement.validerVersion(validerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_COMM_TEC, "Echec du rejet de la version", e);
			throw new ClientException(e);
		}

		if (validerVersionResponse == null) {
			throw new ClientException("Erreur de communication avec SOLON EPP, le rejet de la communication a échoué.");
		} else if (validerVersionResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(validerVersionResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, le rejet de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(validerVersionResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}
	}

	@Override
	public void accuserReceptionVersion(final EvenementDTO evenementDTO, final CoreSession session)
			throws ClientException {

		final String idEvenement = evenementDTO.getIdEvenement();
		final Integer majeur = evenementDTO.getVersionCouranteMajeur();
		final Integer mineur = evenementDTO.getVersionCouranteMineur();

		if (evenementDTO.getTypeEvenementName().equals(TypeEvenementConstants.TYPE_EVENEMENT_EVT24)) {
			accuserReception(idEvenement, majeur, mineur, session, EvenementType.EVT_24);
		} else {
			accuserReception(idEvenement, majeur, mineur, session, null);
		}

	}

	@Override
	public void accuserReception(final String idEvenement, final EvenementType evenementType, final CoreSession session)
			throws ClientException {
		// AR de la version courante
		accuserReception(idEvenement, -1, null, session, evenementType);
	}

	private void accuserReception(final String idEvenement, Integer majeur, final Integer mineur,
			final CoreSession session, final EvenementType evenementType) throws ClientException {
		WSEvenement wsEvenement = null;

		if (majeur != null && majeur == -1) {
			majeur = null;
		}

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final AccuserReceptionRequest accuserReceptionRequest = new AccuserReceptionRequest();

		final EvtId evtId = new EvtId();
		evtId.setId(idEvenement);

		if (majeur != null && mineur != null) {
			final Version version = new Version();
			version.setMajeur(majeur);
			version.setMineur(mineur);
			evtId.setVersion(version);
		}

		accuserReceptionRequest.setIdEvenement(evtId);

		AccuserReceptionResponse accuserReceptionResponse = null;
		try {
			accuserReceptionResponse = wsEvenement.accuserReception(accuserReceptionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_COMM_TEC, "Echec de l'AR", e);
			throw new ClientException(e);
		}

		if (accuserReceptionResponse == null) {
			throw new ClientException("Erreur de communication avec SOLON EPP, l'AR de la communication a échoué.");
		} else if (accuserReceptionResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(accuserReceptionResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, l'AR de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(accuserReceptionResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}

		if (evenementType != null && evenementType.equals(EvenementType.EVT_24)) {
			// Notification du texte définitif
			final ChercherEvenementRequest chercherEvenementRequest = new ChercherEvenementRequest();

			chercherEvenementRequest.getIdEvenement().add(evtId);

			ChercherEvenementResponse chercherEvenementResponse = null;

			try {
				chercherEvenementResponse = wsEvenement.chercherEvenement(chercherEvenementRequest);
			} catch (final HttpTransactionException e) {
				LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
				throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
			} catch (final Exception e) {
				LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
				throw new ClientException(e);
			}

			if (chercherEvenementResponse == null) {
				throw new ClientException(
						"Erreur de communication avec SOLON EPP, la récupération de la communication a échoué.");
			} else if (chercherEvenementResponse.getStatut() == null
					|| !TraitementStatut.OK.equals(chercherEvenementResponse.getStatut())) {

				ClientException clientExc = new ClientException(
						"Erreur de communication avec SOLON EPP, la récupération de la communication a échoué."
								+ WSErrorHelper.buildCleanMessage(chercherEvenementResponse.getMessageErreur()));
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
				throw clientExc;
			}

			if (chercherEvenementResponse.getEvenement().size() > 1) {
				throw new ClientException(
						"La récupération de la version courante de la communication a retourné plusieurs résultats.");
			}

			for (final EppEvtContainer eppEvtContainer : chercherEvenementResponse.getEvenement()) {
				final EppEvt24 eppEvt24 = eppEvtContainer.getEvt24();
				if (eppEvt24 != null) {
					final FicheLoi ficheLoi = SolonMgppServiceLocator.getDossierService().findOrCreateFicheLoi(session,
							eppEvt24.getIdDossier());

					STServiceLocator.getSTLockService().unlockDocUnrestricted(session, ficheLoi.getDocument());
					Version versionCourante = eppEvt24.getVersionCourante();
					if (versionCourante != null && versionCourante.getMajeur() == 1 && versionCourante.getMineur() == 0
							&& eppEvt24.getHorodatage() != null) {
						ficheLoi.setDateReception(eppEvt24.getHorodatage().toGregorianCalendar());
						session.saveDocument(ficheLoi.getDocument());
					}

					Dossier dossier;
					try {
						// try by idDossier
						dossier = SolonEpgServiceLocator.getDossierService().findDossierFromIdDossier(session,
								eppEvt24.getIdDossier());
						if (dossier == null) {
							// try by nor
							dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(session,
									eppEvt24.getIdDossier());
						}

						if (dossier != null) {
							STServiceLocator.getSTLockService().unlockDocUnrestricted(session, dossier.getDocument());

							dossier.setAdoption(Boolean.TRUE);

							ficheLoi.setEcheancierPromulgation(Boolean.TRUE);
							session.saveDocument(ficheLoi.getDocument());

							session.saveDocument(dossier.getDocument());
						}
					} catch (final ClientException e) {
						LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC,
								"mise a jour de l'adoption impossible", e);
					}

					session.save();

				}
			}
		}

		if (EvenementType.EVT_39.equals(evenementType) || EvenementType.EVT_43_BIS.equals(evenementType)) {
			LOGGER.info(session, MgppLogEnumImpl.CREATE_FICHE_LOI_TEC,
					"verification et creation fiche 341 si necessaire pour " + idEvenement);
			SolonMgppServiceLocator.getDossierService().createFiche341(session, idEvenement, null);
		}
	}

	@Override
	public void abandonnerVersion(final EvenementDTO evenementDTO, final CoreSession session) throws ClientException {
		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final ValiderVersionRequest validerVersionRequest = new ValiderVersionRequest();
		validerVersionRequest.setIdEvenement(evenementDTO.getIdEvenement());
		validerVersionRequest.setModeValidation(ValidationType.ABANDONNER);

		ValiderVersionResponse validerVersionResponse = null;
		try {
			validerVersionResponse = wsEvenement.validerVersion(validerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_COMM_TEC, "Echec de l'abandon", e);
			throw new ClientException(e);
		}

		if (validerVersionResponse == null) {
			throw new ClientException("Erreur de communication avec SOLON EPP, l'abandon de la communication a échoué.");
		} else if (validerVersionResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(validerVersionResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, l'abandon de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(validerVersionResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}
	}

	@Override
	public void annulerEvenement(final EvenementDTO evenementDTO, final CoreSession session) throws ClientException {
		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final AnnulerEvenementRequest annulerEvenementRequest = new AnnulerEvenementRequest();
		annulerEvenementRequest.setIdEvenement(evenementDTO.getIdEvenement());

		AnnulerEvenementResponse annulerEvenementResponse = null;
		try {
			annulerEvenementResponse = wsEvenement.annulerEvenement(annulerEvenementRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_COMM_TEC, "Echec de l'annulation", e);
			throw new ClientException(e);
		}

		if (annulerEvenementResponse == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, l'annulation de la communication a échoué.");
		} else if (annulerEvenementResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(annulerEvenementResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, l'annulation de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(annulerEvenementResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}
	}

	@Override
	public void supprimerEvenement(final EvenementDTO evenementDTO, final CoreSession session) throws ClientException {
		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final SupprimerVersionRequest supprimerVersionRequest = new SupprimerVersionRequest();

		final EvtId evtId = new EvtId();
		evtId.setId(evenementDTO.getIdEvenement());

		final Version version = new Version();
		version.setMajeur(evenementDTO.getVersionCouranteMajeur());
		version.setMineur(evenementDTO.getVersionCouranteMineur());

		evtId.setVersion(version);

		supprimerVersionRequest.setIdEvenement(evtId);

		SupprimerVersionResponse supprimerVersionResponse = null;
		try {
			supprimerVersionResponse = wsEvenement.supprimerVersion(supprimerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_REMOVE_COMM_TEC, e);
			throw new ClientException(e);
		}

		if (supprimerVersionResponse == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, l'annulation de la communication a échoué.");
		} else if (supprimerVersionResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(supprimerVersionResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, l'annulation de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(supprimerVersionResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}
	}

	@Override
	public void modifierEvenement(final EvenementDTO evenementDTO, final boolean publier, final CoreSession session)
			throws ClientException {
		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();

		final CreationType creationType = extractModeCreation(evenementDTO, publier, session);
		creerVersionRequest.setModeCreation(creationType);

		final EppEvtContainer eppEvtContainer = createEppEvtContainerFromEvenementDTO(evenementDTO, Boolean.TRUE,
				session);
		creerVersionRequest.setEvenement(eppEvtContainer);

		CreerVersionResponse creerVersionResponse = null;
		try {
			creerVersionResponse = wsEvenement.creerVersion(creerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_COMM_TEC, e);
			throw new ClientException(e);
		}

		if (creerVersionResponse == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la modification de la communication a échoué.");
		} else if (creerVersionResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(creerVersionResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la modification de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(creerVersionResponse.getMessageErreur()));

			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}

	}

	@Override
	public void createEvenementEppEvt28Brouillon(final Dossier dossier, final CoreSession session)
			throws ClientException {

		// recherche dernier evenement EVT28
		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final RechercherEvenementRequest rechercherEvenementRequest28 = new RechercherEvenementRequest();

		final CritereRechercheEvenement critereRechercheEvenement28 = new CritereRechercheEvenement();
		critereRechercheEvenement28.setIdDossier(dossier.getIdDossier());
		critereRechercheEvenement28.setTypeEvenement(EvenementType.EVT_28);

		rechercherEvenementRequest28.setParCritere(critereRechercheEvenement28);

		RechercherEvenementResponse rechercherEvenementResponse28 = null;

		try {
			rechercherEvenementResponse28 = wsEvenement.rechercherEvenement(rechercherEvenementRequest28);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_MESSAGE_TEC, e);
			throw new ClientException(e);
		}

		if (rechercherEvenementResponse28 == null) {
			throw new ClientException("Erreur de communication avec SOLON EPP, la récupération des messages a échoué.");
		} else if (rechercherEvenementResponse28.getStatut() == null
				|| !TraitementStatut.OK.equals(rechercherEvenementResponse28.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la récupération des messages a échoué."
							+ WSErrorHelper.buildCleanMessage(rechercherEvenementResponse28.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}

		if (rechercherEvenementResponse28.getMessage().isEmpty()) {
			// pas de EVT28 existant
			final RechercherEvenementRequest rechercherEvenementRequest24 = new RechercherEvenementRequest();

			final CritereRechercheEvenement critereRechercheEvenement24 = new CritereRechercheEvenement();
			critereRechercheEvenement24.setIdDossier(dossier.getIdDossier());
			critereRechercheEvenement24.setTypeEvenement(EvenementType.EVT_24);

			rechercherEvenementRequest24.setParCritere(critereRechercheEvenement24);

			RechercherEvenementResponse rechercherEvenementResponse24 = null;

			try {
				rechercherEvenementResponse24 = wsEvenement.rechercherEvenement(rechercherEvenementRequest24);
			} catch (final HttpTransactionException e) {
				LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
				throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
			} catch (final Exception e) {
				LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_MESSAGE_TEC, e);
				throw new ClientException(e);
			}

			if (rechercherEvenementResponse24 == null) {
				throw new ClientException(
						"Erreur de communication avec SOLON EPP, la récupération des messages a échoué.");
			} else if (rechercherEvenementResponse24.getStatut() == null
					|| !TraitementStatut.OK.equals(rechercherEvenementResponse24.getStatut())) {

				ClientException clientExc = new ClientException(
						"Erreur de communication avec SOLON EPP, la récupération des messages a échoué."
								+ WSErrorHelper.buildCleanMessage(rechercherEvenementResponse24.getMessageErreur()));
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
				throw clientExc;
			}

			Message last24 = null;

			for (final Message message : rechercherEvenementResponse24.getMessage()) {
				if (last24 == null
						|| last24.getDateEvenement() != null
						&& message.getDateEvenement() != null
						&& DateUtil.xmlGregorianCalendarToDate(last24.getDateEvenement()).before(
								DateUtil.xmlGregorianCalendarToDate(message.getDateEvenement()))) {
					last24 = message;
				}
			}

			if (last24 != null) {
				// creation de l'evenement EppEvt28 a l'etat Brouillon destinataire AN
				final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
				creerVersionRequest.setModeCreation(CreationType.CREER_BROUILLON);

				final EppEvtContainer eppEvtContainer = new EppEvtContainer();

				final EppEvt28 eppEvt28 = new EppEvt28();
				eppEvt28.setIdDossier(last24.getIdDossier());
				eppEvt28.setIdEvenementPrecedent(last24.getIdEvenement());

				eppEvt28.setDestinataire(Institution.ASSEMBLEE_NATIONALE);
				eppEvt28.getCopie().clear();

				final RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
				eppEvt28.setDatePublication(DateUtil.calendarToXMLGregorianCalendar(retourDila.getDateParutionJorf()));
				eppEvt28.setTitre(retourDila.getTitreOfficiel());
				eppEvt28.setNor(dossier.getNumeroNor());
				eppEvt28.setNumeroLoi(retourDila.getNumeroTexteParutionJorf());

				if (retourDila.getPageParutionJorf() != null) {
					eppEvt28.setPageJo(retourDila.getPageParutionJorf().intValue());
				}

				// TODO attente retour DILA sur le sujet
				// eppEvt28.setNumeroJo(value);
				// eppEvt28.setDatePromulgation(value)

				eppEvtContainer.setEvt28(eppEvt28);
				eppEvtContainer.setType(EvenementType.EVT_28);
				creerVersionRequest.setEvenement(eppEvtContainer);

				CreerVersionResponse creerVersionResponseAN = null;
				try {
					creerVersionResponseAN = wsEvenement.creerVersion(creerVersionRequest);
				} catch (final HttpTransactionException e) {
					LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
					throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
				} catch (final Exception e) {
					LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
					throw new ClientException(e);
				}

				if (creerVersionResponseAN == null) {
					throw new ClientException(
							"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
				} else if (creerVersionResponseAN.getStatut() == null
						|| !TraitementStatut.OK.equals(creerVersionResponseAN.getStatut())) {
					ClientException clientExc = new ClientException(
							"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
									+ WSErrorHelper.buildCleanMessage(creerVersionResponseAN.getMessageErreur()));
					LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
					throw clientExc;
				}

				// creation de l'evenement EppEvt28 a l'etat Brouillon destinataire SE
				eppEvt28.setDestinataire(Institution.SENAT);
				eppEvt28.getCopie().clear();

				eppEvtContainer.setEvt28(eppEvt28);
				creerVersionRequest.setEvenement(eppEvtContainer);

				CreerVersionResponse creerVersionResponseSenat = null;
				try {
					creerVersionResponseSenat = wsEvenement.creerVersion(creerVersionRequest);
				} catch (final HttpTransactionException e) {
					LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
					throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
				} catch (final Exception e) {
					LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
					throw new ClientException(e);
				}

				if (creerVersionResponseSenat == null) {
					throw new ClientException(
							"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
				} else if (creerVersionResponseSenat.getStatut() == null
						|| !TraitementStatut.OK.equals(creerVersionResponseSenat.getStatut())) {
					ClientException clientExc = new ClientException(
							"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
									+ WSErrorHelper.buildCleanMessage(creerVersionResponseSenat.getMessageErreur()));
					LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
					throw clientExc;

				}

			}
		}
	}

	@Override
	public void createEvenementEppEvt44Brouillon(final Dossier dossier, final CoreSession session)
			throws ClientException {

		final String numeroNor = dossier.getNumeroNor();
		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final RechercherEvenementRequest rechercherEvenementRequest = new RechercherEvenementRequest();

		final CritereRechercheEvenement critereRechercheEvenement = new CritereRechercheEvenement();
		critereRechercheEvenement.setIdDossier(numeroNor);
		critereRechercheEvenement.setTypeEvenement(EvenementType.EVT_44);

		rechercherEvenementRequest.setParCritere(critereRechercheEvenement);

		RechercherEvenementResponse rechercherEvenementResponse = null;

		try {
			rechercherEvenementResponse = wsEvenement.rechercherEvenement(rechercherEvenementRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_MESSAGE_TEC, e);
			throw new ClientException(e);
		}

		if (rechercherEvenementResponse == null) {
			throw new ClientException("Erreur de communication avec SOLON EPP, la récupération des messages a échoué.");
		} else if (rechercherEvenementResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(rechercherEvenementResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la récupération des messages a échoué."
							+ WSErrorHelper.buildCleanMessage(rechercherEvenementResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;

		}

		if (rechercherEvenementResponse.getMessage().isEmpty()) {

			// pas de communication => creation de l'evenement EppEvt44 a l'etat Brouillon
			final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
			creerVersionRequest.setModeCreation(CreationType.CREER_BROUILLON);

			final EppEvtContainer eppEvtContainer = new EppEvtContainer();
			final EppEvt44 eppEvt44 = new EppEvt44();
			// eppEvt44.setNorLoi(numeroNor);
			eppEvt44.setIdDossier(numeroNor);
			// pour AN
			eppEvt44.setDestinataire(Institution.ASSEMBLEE_NATIONALE);
			eppEvt44.getCopie().clear();

			final DossierService dossierService = SolonMgppServiceLocator.getDossierService();
			FichePresentationDR fichePresentationDR = dossierService.findFicheDR(session, numeroNor);
			eppEvt44.setObjet(fichePresentationDR.getObjet());

			RapportParlement rapportParlement = null;
			if (fichePresentationDR.getRapportParlement() != null) {
				rapportParlement = RapportParlement.fromValue(fichePresentationDR.getRapportParlement());
			}
			eppEvt44.setRapportParlement(rapportParlement);
			eppEvt44.setBaseLegale(dossier.getBaseLegaleActe());

			eppEvtContainer.setEvt44(eppEvt44);
			eppEvtContainer.setType(EvenementType.EVT_44);

			creerVersionRequest.setEvenement(eppEvtContainer);

			CreerVersionResponse creerVersionResponseAN = null;
			try {
				creerVersionResponseAN = wsEvenement.creerVersion(creerVersionRequest);
			} catch (final HttpTransactionException e) {
				LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
				throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
			} catch (final Exception e) {
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
				throw new ClientException(e);
			}

			if (creerVersionResponseAN == null) {
				throw new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
			} else if (creerVersionResponseAN.getStatut() == null
					|| !TraitementStatut.OK.equals(creerVersionResponseAN.getStatut())) {
				ClientException clientExc = new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
								+ WSErrorHelper.buildCleanMessage(creerVersionResponseAN.getMessageErreur()));
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
				throw clientExc;

			}

			// pour SENAT
			eppEvtContainer.getEvt44().setDestinataire(Institution.SENAT);
			eppEvtContainer.getEvt44().getCopie().clear();

			creerVersionRequest.setEvenement(eppEvtContainer);

			CreerVersionResponse creerVersionResponseSenat = null;
			try {
				creerVersionResponseSenat = wsEvenement.creerVersion(creerVersionRequest);
			} catch (final HttpTransactionException e) {
				LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
				throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
			} catch (final Exception e) {
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
				throw new ClientException(e);
			}

			if (creerVersionResponseSenat == null) {
				throw new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
			} else if (creerVersionResponseSenat.getStatut() == null
					|| !TraitementStatut.OK.equals(creerVersionResponseSenat.getStatut())) {
				ClientException clientExc = new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
								+ WSErrorHelper.buildCleanMessage(creerVersionResponseSenat.getMessageErreur()));
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
				throw clientExc;

			}
		}

	}

	@Override
	public void createEvenementEppEvt36Brouillon(final CoreSession session,
			final FichePresentationIE fichePresentationIE) throws ClientException {

		// creation de 2 communications

		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
		creerVersionRequest.setModeCreation(CreationType.CREER_BROUILLON);

		final EppEvtContainer eppEvtContainer = new EppEvtContainer();
		final EppEvt36 eppEvt36 = new EppEvt36();
		eppEvt36.setIdDossier(fichePresentationIE.getIdentifiantProposition());

		final Mandat mandat = new Mandat();
		mandat.setId(fichePresentationIE.getAuteur());
		eppEvt36.setAuteur(mandat);

		eppEvt36.setDate(DateUtil.calendarToXMLGregorianCalendar(fichePresentationIE.getDate()));
		eppEvt36.setObjet(fichePresentationIE.getIntitule());
		eppEvt36.setCommentaire(fichePresentationIE.getObservation());

		// pour AN
		eppEvt36.setDestinataire(Institution.ASSEMBLEE_NATIONALE);
		eppEvt36.getCopie().clear();

		eppEvtContainer.setEvt36(eppEvt36);
		eppEvtContainer.setType(EvenementType.EVT_36);
		creerVersionRequest.setEvenement(eppEvtContainer);

		CreerVersionResponse creerVersionResponseAN = null;
		try {
			creerVersionResponseAN = wsEvenement.creerVersion(creerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
			throw new ClientException(e);
		}

		if (creerVersionResponseAN == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
		} else if (creerVersionResponseAN.getStatut() == null
				|| !TraitementStatut.OK.equals(creerVersionResponseAN.getStatut())) {
			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(creerVersionResponseAN.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;

		}

		// pour SENAT
		eppEvtContainer.getEvt36().setDestinataire(Institution.SENAT);
		eppEvtContainer.getEvt36().getCopie().clear();

		creerVersionRequest.setEvenement(eppEvtContainer);

		CreerVersionResponse creerVersionResponseSenat = null;
		try {
			creerVersionResponseSenat = wsEvenement.creerVersion(creerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
			throw new ClientException(e);
		}

		if (creerVersionResponseSenat == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
		} else if (creerVersionResponseSenat.getStatut() == null
				|| !TraitementStatut.OK.equals(creerVersionResponseSenat.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(creerVersionResponseSenat.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}

	}

	@Override
	public void createEvenementEppEvt49Brouillon(final CoreSession session, final FileSolonMgpp file,
			final FichePresentationOEP fichePresentationOEP, final EvenementDTO evenementDTO) throws ClientException {

		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
		creerVersionRequest.setModeCreation(CreationType.CREER_BROUILLON);

		final EppEvtContainer eppEvtContainer = new EppEvtContainer();
		final EppEvt49 eppEvt49 = new EppEvt49();
		eppEvt49.setIdDossier(fichePresentationOEP.getIdDossier());
		eppEvt49.setObjet(fichePresentationOEP.getNomOrganisme());
		eppEvt49.setBaseLegale(fichePresentationOEP.getTexteRef());

		// ASSEMBLEE_NATIONALE
		eppEvt49.setDestinataire(Institution.ASSEMBLEE_NATIONALE);
		eppEvt49.getCopie().clear();

		// recupération des représentants
		final DossierService dossierService = SolonMgppServiceLocator.getDossierService();
		final List<DocumentModel> listRepresentantAN = dossierService.fetchRepresentantOEP(session,
				VocabularyConstants.REPRESENTANT_TYPE_AN, fichePresentationOEP.getDocument().getId());

		for (final DocumentModel documentModel : listRepresentantAN) {
			final RepresentantOEP representantOEP = documentModel.getAdapter(RepresentantOEP.class);
			if (representantOEP != null) {
				final Mandat mandat = new Mandat();
				mandat.setId(representantOEP.getRepresentant());
				if (VocabularyConstants.FONCTION_TITULAIRE_ID.equals(representantOEP.getFonction())) {
					eppEvt49.getTitulaires().add(mandat);
				} else if (VocabularyConstants.FONCTION_SUPPLEANT_ID.equals(representantOEP.getFonction())) {
					eppEvt49.getSuppleant().add(mandat);
				}
			}
		}

		eppEvtContainer.setEvt49(eppEvt49);
		eppEvtContainer.setType(EvenementType.EVT_49);
		creerVersionRequest.setEvenement(eppEvtContainer);

		CreerVersionResponse creerVersionResponseAN = null;
		try {
			creerVersionResponseAN = wsEvenement.creerVersion(creerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
			throw new ClientException(e);
		}

		if (creerVersionResponseAN == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
		} else if (creerVersionResponseAN.getStatut() == null
				|| !TraitementStatut.OK.equals(creerVersionResponseAN.getStatut())) {
			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(creerVersionResponseAN.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;

		}

		// SENAT
		eppEvt49.setDestinataire(Institution.SENAT);
		eppEvt49.getCopie().clear();

		final List<DocumentModel> listRepresentantSE = dossierService.fetchRepresentantOEP(session,
				VocabularyConstants.REPRESENTANT_TYPE_SE, fichePresentationOEP.getDocument().getId());
		eppEvt49.getTitulaires().clear();
		eppEvt49.getSuppleant().clear();

		for (final DocumentModel documentModel : listRepresentantSE) {
			final RepresentantOEP representantOEP = documentModel.getAdapter(RepresentantOEP.class);
			if (representantOEP != null) {
				final Mandat mandat = new Mandat();
				mandat.setId(representantOEP.getRepresentant());
				if (VocabularyConstants.FONCTION_TITULAIRE_ID.equals(representantOEP.getFonction())) {
					eppEvt49.getTitulaires().add(mandat);
				} else if (VocabularyConstants.FONCTION_SUPPLEANT_ID.equals(representantOEP.getFonction())) {
					eppEvt49.getSuppleant().add(mandat);
				}
			}
		}

		eppEvtContainer.setEvt49(eppEvt49);
		eppEvtContainer.setType(EvenementType.EVT_49);

		creerVersionRequest.setEvenement(eppEvtContainer);

		CreerVersionResponse creerVersionResponseSE = null;
		try {
			creerVersionResponseSE = wsEvenement.creerVersion(creerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
			throw new ClientException(e);
		}

		if (creerVersionResponseSE == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
		} else if (creerVersionResponseSE.getStatut() == null
				|| !TraitementStatut.OK.equals(creerVersionResponseSE.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(creerVersionResponseSE.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;

		}

	}

	@Override
	public void createEvenementEppEvt32Brouillon(final CoreSession session, final FileSolonMgpp file,
			final FichePresentationAVI fichePresentationAVI, final EvenementDTO evenementDTO) throws ClientException {

		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
		creerVersionRequest.setModeCreation(CreationType.CREER_BROUILLON);

		final EppEvtContainer eppEvtContainer = new EppEvtContainer();
		final EppEvt32 eppEvt32 = new EppEvt32();
		eppEvt32.setIdDossier(fichePresentationAVI.getIdDossier());
		eppEvt32.setObjet(fichePresentationAVI.getNomOrganisme());

		// ASSEMBLEE_NATIONALE
		eppEvt32.setDestinataire(Institution.ASSEMBLEE_NATIONALE);
		eppEvt32.getCopie().clear();

		eppEvtContainer.setEvt32(eppEvt32);
		eppEvtContainer.setType(EvenementType.EVT_32);
		creerVersionRequest.setEvenement(eppEvtContainer);

		CreerVersionResponse creerVersionResponseAN = null;
		try {
			creerVersionResponseAN = wsEvenement.creerVersion(creerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
			throw new ClientException(e);
		}

		if (creerVersionResponseAN == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
		} else if (creerVersionResponseAN.getStatut() == null
				|| !TraitementStatut.OK.equals(creerVersionResponseAN.getStatut())) {
			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(creerVersionResponseAN.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;

		}

		// SENAT
		eppEvt32.setDestinataire(Institution.SENAT);
		eppEvt32.getCopie().clear();

		eppEvtContainer.setEvt32(eppEvt32);
		eppEvtContainer.setType(EvenementType.EVT_32);

		creerVersionRequest.setEvenement(eppEvtContainer);

		CreerVersionResponse creerVersionResponseSE = null;
		try {
			creerVersionResponseSE = wsEvenement.creerVersion(creerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
			throw new ClientException(e);
		}

		if (creerVersionResponseSE == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
		} else if (creerVersionResponseSE.getStatut() == null
				|| !TraitementStatut.OK.equals(creerVersionResponseSE.getStatut())) {
			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(creerVersionResponseSE.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;

		}
	}

	@Override
	public void createEvenementEppAUD01Brouillon(CoreSession session, FileSolonMgpp file,
			FichePresentationAUD fichePresentationAUD, EvenementDTO evenementDTO) throws ClientException {

		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
		creerVersionRequest.setModeCreation(CreationType.CREER_BROUILLON);

		final EppEvtContainer eppEvtContainer = new EppEvtContainer();
		final EppAUD01 eppAUD01 = new EppAUD01();
		eppAUD01.setIdDossier(fichePresentationAUD.getIdDossier());
		eppAUD01.setObjet(fichePresentationAUD.getNomOrganisme());

		List<DocumentModel> listRepresentant = SolonMgppServiceLocator.getDossierService().fetchPersonneAUD(session,
				fichePresentationAUD.getDocument().getId());

		RepresentantAUD finalRepresentantAUD = null;
		boolean oneReprensentantAUD = true;
		if (listRepresentant != null && !listRepresentant.isEmpty()) {
			for (DocumentModel representant : listRepresentant) {
				RepresentantAUD representantAUD = representant.getAdapter(RepresentantAUD.class);
				if (representantAUD.getDateFin() == null || representantAUD.getDateFin().before(Calendar.getInstance())) {
					if (oneReprensentantAUD && finalRepresentantAUD == null) {
						finalRepresentantAUD = representantAUD;
					} else {
						finalRepresentantAUD = null;
						oneReprensentantAUD = false;
					}
				}
			}
		}
		if (finalRepresentantAUD != null) {
			eppAUD01.setPersonne(finalRepresentantAUD.getPersonne());
			eppAUD01.setFonction(finalRepresentantAUD.getFonction());
		}
		eppAUD01.setBaseLegale(fichePresentationAUD.getBaseLegale());

		Organisme organisme = new Organisme();
		organisme.setId(fichePresentationAUD.getIdOrganismeEPP());
		organisme.setIdCommun(fichePresentationAUD.getIdOrganismeEPP());
		organisme.setNom(fichePresentationAUD.getNomOrganisme());

		eppAUD01.setOrganisme(organisme);

		// ASSEMBLEE_NATIONALE
		eppAUD01.setDestinataire(Institution.ASSEMBLEE_NATIONALE);
		eppAUD01.getCopie().clear();

		eppEvtContainer.setAUD01(eppAUD01);
		eppEvtContainer.setType(EvenementType.AUD_01);
		creerVersionRequest.setEvenement(eppEvtContainer);

		CreerVersionResponse creerVersionResponseAN = null;
		try {
			creerVersionResponseAN = wsEvenement.creerVersion(creerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
			throw new ClientException(e);
		}

		if (creerVersionResponseAN == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
		} else if (creerVersionResponseAN.getStatut() == null
				|| !TraitementStatut.OK.equals(creerVersionResponseAN.getStatut())) {
			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(creerVersionResponseAN.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;

		}

		// SENAT
		eppAUD01.setDestinataire(Institution.SENAT);
		eppAUD01.getCopie().clear();

		eppEvtContainer.setAUD01(eppAUD01);
		eppEvtContainer.setType(EvenementType.AUD_01);

		creerVersionRequest.setEvenement(eppEvtContainer);

		CreerVersionResponse creerVersionResponseSE = null;
		try {
			creerVersionResponseSE = wsEvenement.creerVersion(creerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
			throw new ClientException(e);
		}

		if (creerVersionResponseSE == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
		} else if (creerVersionResponseSE.getStatut() == null
				|| !TraitementStatut.OK.equals(creerVersionResponseSE.getStatut())) {
			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(creerVersionResponseSE.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;

		}
	}

	@Override
	public void createEvenementEppDOC01Brouillon(CoreSession session, FileSolonMgpp file,
			FichePresentationDOC fichePresentationDOC, EvenementDTO evenementDTO) throws ClientException {

		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
		creerVersionRequest.setModeCreation(CreationType.CREER_BROUILLON);

		final EppEvtContainer eppEvtContainer = new EppEvtContainer();
		final EppDOC01 eppDOC01 = new EppDOC01();
		eppDOC01.setIdDossier(fichePresentationDOC.getIdDossier());
		eppDOC01.setObjet(fichePresentationDOC.getObjet());
		if (fichePresentationDOC.getDateLettrePm() != null) {
			eppDOC01.setDateLettrePm(DateUtil.calendarToXMLGregorianCalendar(fichePresentationDOC.getDateLettrePm()));
		}
		eppDOC01.setBaseLegale(fichePresentationDOC.getBaseLegale());

		// ASSEMBLEE_NATIONALE
		eppDOC01.setDestinataire(Institution.ASSEMBLEE_NATIONALE);
		eppDOC01.getCopie().clear();

		eppEvtContainer.setDOC01(eppDOC01);
		eppEvtContainer.setType(EvenementType.DOC_01);
		creerVersionRequest.setEvenement(eppEvtContainer);

		CreerVersionResponse creerVersionResponseAN = null;
		try {
			creerVersionResponseAN = wsEvenement.creerVersion(creerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
			throw new ClientException(e);
		}

		if (creerVersionResponseAN == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
		} else if (creerVersionResponseAN.getStatut() == null
				|| !TraitementStatut.OK.equals(creerVersionResponseAN.getStatut())) {
			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(creerVersionResponseAN.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;

		}

		// SENAT
		eppDOC01.setDestinataire(Institution.SENAT);
		eppDOC01.getCopie().clear();

		eppEvtContainer.setDOC01(eppDOC01);
		eppEvtContainer.setType(EvenementType.DOC_01);

		creerVersionRequest.setEvenement(eppEvtContainer);

		CreerVersionResponse creerVersionResponseSE = null;
		try {
			creerVersionResponseSE = wsEvenement.creerVersion(creerVersionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
			throw new ClientException(e);
		}

		if (creerVersionResponseSE == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
		} else if (creerVersionResponseSE.getStatut() == null
				|| !TraitementStatut.OK.equals(creerVersionResponseSE.getStatut())) {
			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(creerVersionResponseSE.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;

		}
	}

	@Override
	public void createEvenementEppEvt29Brouillon(final CoreSession session,
			final FichePresentationDecret fichePresentationDecret) throws ClientException {

		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final List<Message> messagesAN = rechercherMessage(session, wsEvenement, fichePresentationDecret.getNor(),
				EvenementType.EVT_29, Institution.ASSEMBLEE_NATIONALE);

		final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
		creerVersionRequest.setModeCreation(CreationType.CREER_BROUILLON);
		final EppEvtContainer eppEvtContainer = new EppEvtContainer();
		final EppEvt29 eppEvt29 = new EppEvt29();

		if (messagesAN == null || messagesAN.size() == 0) {

			eppEvt29.setIdDossier(fichePresentationDecret.getNor());

			// ASSEMBLEE_NATIONALE
			eppEvt29.setDestinataire(Institution.ASSEMBLEE_NATIONALE);
			eppEvt29.setEmetteur(Institution.GOUVERNEMENT);
			eppEvt29.setObjet(fichePresentationDecret.getIntitule());
			eppEvt29.setTypeActe(TypeActe.DECRET_PR);
			if (StringUtils.isNotEmpty(fichePresentationDecret.getNumJO())) {
				eppEvt29.setNumeroJo(new Integer(fichePresentationDecret.getNumJO()));
			}

			if (fichePresentationDecret.getDateJO() != null) {
				eppEvt29.setDateJo(DateUtil.calendarToXMLGregorianCalendar(fichePresentationDecret.getDateJO()));
			}

			eppEvt29.getCopie().clear();
			eppEvtContainer.setEvt29(eppEvt29);
			eppEvtContainer.setType(EvenementType.EVT_29);
			creerVersionRequest.setEvenement(eppEvtContainer);

			CreerVersionResponse creerVersionResponseAN = null;
			try {
				creerVersionResponseAN = wsEvenement.creerVersion(creerVersionRequest);
			} catch (final HttpTransactionException e) {
				LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
				throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
			} catch (final Exception e) {
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
				throw new ClientException(e);
			}

			if (creerVersionResponseAN == null) {
				throw new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
			} else if (creerVersionResponseAN.getStatut() == null
					|| !TraitementStatut.OK.equals(creerVersionResponseAN.getStatut())) {

				ClientException clientExc = new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
								+ WSErrorHelper.buildCleanMessage(creerVersionResponseAN.getMessageErreur()));
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
				throw clientExc;

			}

		}

		final List<Message> messagesSenat = rechercherMessage(session, wsEvenement, fichePresentationDecret.getNor(),
				EvenementType.EVT_29, Institution.SENAT);

		if (messagesSenat == null || messagesSenat.size() == 0) {

			eppEvt29.setIdDossier(fichePresentationDecret.getNor());

			// SENAT
			eppEvt29.setDestinataire(Institution.SENAT);
			eppEvt29.setEmetteur(Institution.GOUVERNEMENT);
			eppEvt29.setObjet(fichePresentationDecret.getIntitule());
			eppEvt29.setTypeActe(TypeActe.DECRET_PR);
			if (StringUtils.isNotEmpty(fichePresentationDecret.getNumJO())) {
				eppEvt29.setNumeroJo(new Integer(fichePresentationDecret.getNumJO()));
			}

			if (fichePresentationDecret.getDateJO() != null) {
				eppEvt29.setDateJo(DateUtil.calendarToXMLGregorianCalendar(fichePresentationDecret.getDateJO()));
			}

			eppEvt29.getCopie().clear();
			eppEvtContainer.setEvt29(eppEvt29);
			eppEvtContainer.setType(EvenementType.EVT_29);

			creerVersionRequest.setEvenement(eppEvtContainer);

			CreerVersionResponse creerVersionResponseSE = null;
			try {
				creerVersionResponseSE = wsEvenement.creerVersion(creerVersionRequest);
			} catch (final HttpTransactionException e) {
				LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
				throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
			} catch (final Exception e) {
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
				throw new ClientException(e);
			}

			if (creerVersionResponseSE == null) {
				throw new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
			} else if (creerVersionResponseSE.getStatut() == null
					|| !TraitementStatut.OK.equals(creerVersionResponseSE.getStatut())) {

				ClientException clientExc = new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
								+ WSErrorHelper.buildCleanMessage(creerVersionResponseSE.getMessageErreur()));
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
				throw clientExc;

			}
		}
	}

	@Override
	public void createEvenementEppEvt30Brouillon(final CoreSession session,
			final FichePresentationDecret fichePresentationDecret) throws ClientException {

		if (StringUtils.isEmpty(fichePresentationDecret.getNorOuvertureSessionExt())) {
			LOGGER.warn(session, MgppLogEnumImpl.EMPTY_NOR);
			throw new ClientException("Le champ NOR du Décret d'ouverture de la session extraordinaire est vide");
		}

		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		List<Message> messageList = null;
		Message last29 = null;
		final EppEvtContainer eppEvtContainer = new EppEvtContainer();
		final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
		creerVersionRequest.setModeCreation(CreationType.CREER_BROUILLON);
		final EppEvt30 eppEvt30 = new EppEvt30();

		final List<Message> messagesAN = rechercherMessage(session, wsEvenement,
				fichePresentationDecret.getNorOuvertureSessionExt(), EvenementType.EVT_30,
				Institution.ASSEMBLEE_NATIONALE);

		if (messagesAN == null || messagesAN.size() == 0) {

			messageList = rechercherMessage(session, wsEvenement, fichePresentationDecret.getNorOuvertureSessionExt(),
					EvenementType.EVT_29, Institution.ASSEMBLEE_NATIONALE);

			for (final Message message : messageList) {
				if (last29 == null
						|| last29.getDateEvenement() != null
						&& message.getDateEvenement() != null
						&& DateUtil.xmlGregorianCalendarToDate(last29.getDateEvenement()).before(
								DateUtil.xmlGregorianCalendarToDate(message.getDateEvenement()))) {
					last29 = message;
				}
			}

			if (last29 != null) {
				eppEvt30.setIdDossier(fichePresentationDecret.getNorOuvertureSessionExt());
				eppEvt30.setIdEvenementPrecedent(last29.getIdEvenement());

				// ASSEMBLEE_NATIONALE
				eppEvt30.setDestinataire(Institution.ASSEMBLEE_NATIONALE);
				eppEvt30.getCopie().clear();
				eppEvtContainer.setEvt30(eppEvt30);
				eppEvtContainer.setType(EvenementType.EVT_30);
				creerVersionRequest.setEvenement(eppEvtContainer);

				CreerVersionResponse creerVersionResponseAN = null;
				try {
					creerVersionResponseAN = wsEvenement.creerVersion(creerVersionRequest);
				} catch (final HttpTransactionException e) {
					LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
					throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
				} catch (final Exception e) {
					LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
					throw new ClientException(e);
				}

				if (creerVersionResponseAN == null) {
					throw new ClientException(
							"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
				} else if (creerVersionResponseAN.getStatut() == null
						|| !TraitementStatut.OK.equals(creerVersionResponseAN.getStatut())) {
					ClientException clientExc = new ClientException(
							"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
									+ WSErrorHelper.buildCleanMessage(creerVersionResponseAN.getMessageErreur()));
					LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
					throw clientExc;
				}
			}
		}
		final List<Message> messagesSenat = rechercherMessage(session, wsEvenement,
				fichePresentationDecret.getNorOuvertureSessionExt(), EvenementType.EVT_30, Institution.SENAT);

		if (messagesSenat == null || messagesSenat.size() == 0) {

			// SENAT
			messageList = rechercherMessage(session, wsEvenement, fichePresentationDecret.getNorOuvertureSessionExt(),
					EvenementType.EVT_29, Institution.SENAT);

			Message last29Senat = null;

			for (final Message message : messageList) {
				if (last29Senat == null
						|| last29Senat.getDateEvenement() != null
						&& message.getDateEvenement() != null
						&& DateUtil.xmlGregorianCalendarToDate(last29Senat.getDateEvenement()).before(
								DateUtil.xmlGregorianCalendarToDate(message.getDateEvenement()))) {
					last29Senat = message;
				}
			}

			if (last29Senat != null) {
				eppEvt30.setIdDossier(fichePresentationDecret.getNorOuvertureSessionExt());
				eppEvt30.setIdEvenementPrecedent(last29Senat.getIdEvenement());
				eppEvt30.setDestinataire(Institution.SENAT);
				eppEvt30.setEmetteur(Institution.GOUVERNEMENT);
				eppEvt30.setObjet(fichePresentationDecret.getIntitule());
				eppEvt30.setTypeActe(TypeActe.DECRET_PR);
				if (StringUtils.isNotEmpty(fichePresentationDecret.getNumJO())) {
					eppEvt30.setNumeroJo(new Integer(fichePresentationDecret.getNumJO()));
				}

				if (fichePresentationDecret.getDateJO() != null) {
					eppEvt30.setDateJo(DateUtil.calendarToXMLGregorianCalendar(fichePresentationDecret.getDateJO()));
				}

				eppEvt30.getCopie().clear();

				eppEvtContainer.setEvt30(eppEvt30);
				eppEvtContainer.setType(EvenementType.EVT_30);

				creerVersionRequest.setEvenement(eppEvtContainer);

				CreerVersionResponse creerVersionResponseSE = null;
				try {
					creerVersionResponseSE = wsEvenement.creerVersion(creerVersionRequest);
				} catch (final HttpTransactionException e) {
					LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
					throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
				} catch (final Exception e) {
					LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
					throw new ClientException(e);
				}

				if (creerVersionResponseSE == null) {
					throw new ClientException(
							"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
				} else if (creerVersionResponseSE.getStatut() == null
						|| !TraitementStatut.OK.equals(creerVersionResponseSE.getStatut())) {
					ClientException clientExc = new ClientException(
							"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
									+ WSErrorHelper.buildCleanMessage(creerVersionResponseSE.getMessageErreur()));
					LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
					throw clientExc;

				}
			}
		}
	}

	@Override
	public void checkProcedureAcceleree(final CoreSession session, final EvenementDTO evenementDTO)
			throws ClientException {

		if (TypeEvenementConstants.TYPE_EVENEMENT_EVT19.equals(evenementDTO.getTypeEvenementName())) {
			final DossierService dossierService = SolonMgppServiceLocator.getDossierService();

			final FicheLoi fiche = dossierService.findOrCreateFicheLoi(session, evenementDTO.getIdDossier());
			final List<DocumentModel> navettes = SolonMgppServiceLocator.getNavetteService().fetchNavette(session,
					fiche.getDocument().getId());

			final Map<String, List<Long>> mapLecture = new HashMap<String, List<Long>>();

			for (final DocumentModel doc : navettes) {
				final Navette navette = doc.getAdapter(Navette.class);
				final Long niveauLecture = navette.getNiveauLecture();
				final String codeLecture = navette.getCodeLecture();
				if (niveauLecture != null) {
					List<Long> listCourante = mapLecture.get(codeLecture);
					if (listCourante == null) {
						listCourante = new ArrayList<Long>();
					}
					listCourante.add(niveauLecture);
					mapLecture.put(codeLecture, listCourante);
				}
			}

			final List<Long> listNiveauAn = mapLecture.get(NiveauLectureCode.AN.value());
			final List<Long> listNiveauSenat = mapLecture.get(NiveauLectureCode.SENAT.value());
			if (listNiveauAn != null && listNiveauSenat != null) {
				if (listNiveauAn.size() == listNiveauSenat.size() && listNiveauAn.size() == 1) {
					// une seule navette pour chacun, verification de la procedure accélérée
					if (fiche.getProcedureAcceleree() == null) {
						throw new ClientException("La procédure accélérée doit être engagée.");
					}
				}
			}
			// dans tous les autres cas on verifie rien

		}

	}

	@Override
	public void createEvenementEppEvt31Brouillon(final CoreSession session,
			final FichePresentationDecret fichePresentationDecret) throws ClientException {

		if (StringUtils.isEmpty(fichePresentationDecret.getNorLoi())) {
			LOGGER.warn(session, MgppLogEnumImpl.EMPTY_NOR);
			throw new ClientException("Le champ NOR de la loi est vide");
		}

		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final List<Message> messagesAN = rechercherMessage(session, wsEvenement, fichePresentationDecret.getNorLoi(),
				EvenementType.EVT_31, Institution.ASSEMBLEE_NATIONALE);
		List<Message> messageList = null;
		Message lastMessage = null;

		final EppEvtContainer eppEvtContainer = new EppEvtContainer();
		final EppEvt31 eppEvt31 = new EppEvt31();
		final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
		creerVersionRequest.setModeCreation(CreationType.CREER_BROUILLON);

		if (messagesAN == null || messagesAN.size() == 0) {
			messageList = rechercherMessage(session, wsEvenement, fichePresentationDecret.getNorLoi(),
					EvenementType.EVT_01, Institution.ASSEMBLEE_NATIONALE);

			for (final Message message : messageList) {
				if (lastMessage == null
						|| lastMessage.getDateEvenement() != null
						&& message.getDateEvenement() != null
						&& DateUtil.xmlGregorianCalendarToDate(lastMessage.getDateEvenement()).before(
								DateUtil.xmlGregorianCalendarToDate(message.getDateEvenement()))) {
					lastMessage = message;
				}
			}

			if (lastMessage == null) {
				messageList = rechercherMessage(session, wsEvenement, fichePresentationDecret.getNorLoi(),
						EvenementType.EVT_02, Institution.ASSEMBLEE_NATIONALE);
				for (final Message message : messageList) {
					if (lastMessage == null
							|| lastMessage.getDateEvenement() != null
							&& message.getDateEvenement() != null
							&& DateUtil.xmlGregorianCalendarToDate(lastMessage.getDateEvenement()).before(
									DateUtil.xmlGregorianCalendarToDate(message.getDateEvenement()))) {
						lastMessage = message;
					}
				}
			}

			if (lastMessage != null) {
				eppEvt31.setIdDossier(fichePresentationDecret.getNorLoi());

				// ASSEMBLEE_NATIONALE
				eppEvt31.setDestinataire(Institution.ASSEMBLEE_NATIONALE);
				eppEvt31.setEmetteur(Institution.GOUVERNEMENT);
				if (StringUtils.isNotEmpty(fichePresentationDecret.getNumJO())) {
					eppEvt31.setNumeroJo(new Integer(fichePresentationDecret.getNumJO()));
				}

				if (fichePresentationDecret.getDateJO() != null) {
					eppEvt31.setDateJo(DateUtil.calendarToXMLGregorianCalendar(fichePresentationDecret.getDateJO()));
				}
				eppEvt31.setIdEvenementPrecedent(lastMessage.getIdEvenement());
				eppEvt31.getCopie().clear();
				eppEvtContainer.setEvt31(eppEvt31);
				eppEvtContainer.setType(EvenementType.EVT_31);
				creerVersionRequest.setEvenement(eppEvtContainer);

				CreerVersionResponse creerVersionResponseAN = null;
				try {
					creerVersionResponseAN = wsEvenement.creerVersion(creerVersionRequest);
				} catch (final HttpTransactionException e) {
					LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
					throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
				} catch (final Exception e) {
					LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
					throw new ClientException(e);
				}

				if (creerVersionResponseAN == null) {
					throw new ClientException(
							"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
				} else if (creerVersionResponseAN.getStatut() == null
						|| !TraitementStatut.OK.equals(creerVersionResponseAN.getStatut())) {

					ClientException clientExc = new ClientException(
							"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
									+ WSErrorHelper.buildCleanMessage(creerVersionResponseAN.getMessageErreur()));
					LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
					throw clientExc;
				}

			}
		}
		final List<Message> messagesSenat = rechercherMessage(session, wsEvenement,
				fichePresentationDecret.getNorLoi(), EvenementType.EVT_31, Institution.SENAT);

		if (messagesSenat == null || messagesSenat.size() == 0) {
			messageList = rechercherMessage(session, wsEvenement, fichePresentationDecret.getNorLoi(),
					EvenementType.EVT_01, Institution.SENAT);
			lastMessage = null;

			for (final Message message : messageList) {
				if (lastMessage == null
						|| lastMessage.getDateEvenement() != null
						&& message.getDateEvenement() != null
						&& DateUtil.xmlGregorianCalendarToDate(lastMessage.getDateEvenement()).before(
								DateUtil.xmlGregorianCalendarToDate(message.getDateEvenement()))) {
					lastMessage = message;
				}
			}

			if (lastMessage == null) {
				messageList = rechercherMessage(session, wsEvenement, fichePresentationDecret.getNorLoi(),
						EvenementType.EVT_02, Institution.SENAT);
				for (final Message message : messageList) {
					if (lastMessage == null
							|| lastMessage.getDateEvenement() != null
							&& message.getDateEvenement() != null
							&& DateUtil.xmlGregorianCalendarToDate(lastMessage.getDateEvenement()).before(
									DateUtil.xmlGregorianCalendarToDate(message.getDateEvenement()))) {
						lastMessage = message;
					}
				}
			}

			if (lastMessage != null) {
				eppEvt31.setIdDossier(fichePresentationDecret.getNorLoi());
				eppEvt31.setIdEvenementPrecedent(lastMessage.getIdEvenement());
				// SENAT
				eppEvt31.setDestinataire(Institution.SENAT);

				eppEvt31.getCopie().clear();
				eppEvtContainer.setEvt31(eppEvt31);
				eppEvtContainer.setType(EvenementType.EVT_31);

				creerVersionRequest.setEvenement(eppEvtContainer);

				CreerVersionResponse creerVersionResponseSE = null;
				try {
					creerVersionResponseSE = wsEvenement.creerVersion(creerVersionRequest);
				} catch (final HttpTransactionException e) {
					LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
					throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
				} catch (final Exception e) {
					LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
					throw new ClientException(e);
				}

				if (creerVersionResponseSE == null) {
					throw new ClientException(
							"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
				} else if (creerVersionResponseSE.getStatut() == null
						|| !TraitementStatut.OK.equals(creerVersionResponseSE.getStatut())) {
					ClientException clientExc = new ClientException(
							"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
									+ WSErrorHelper.buildCleanMessage(creerVersionResponseSE.getMessageErreur()));
					LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
					throw clientExc;

				}
			}
		}
	}

	@Override
	public void createEvenementEppEvt35Brouillon(final CoreSession session,
			final FichePresentationDecret fichePresentationDecret) throws ClientException {

		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final EppEvtContainer eppEvtContainer = new EppEvtContainer();
		final EppEvt35 eppEvt35 = new EppEvt35();
		final CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
		creerVersionRequest.setModeCreation(CreationType.CREER_BROUILLON);

		final List<Message> messagesAN = rechercherMessage(session, wsEvenement, fichePresentationDecret.getNor(),
				EvenementType.EVT_35, Institution.ASSEMBLEE_NATIONALE);

		if (messagesAN == null || messagesAN.size() == 0) {

			eppEvt35.setIdDossier(fichePresentationDecret.getNor());

			// ASSEMBLEE_NATIONALE
			eppEvt35.setDestinataire(Institution.ASSEMBLEE_NATIONALE);
			eppEvt35.setEmetteur(Institution.GOUVERNEMENT);
			if (StringUtils.isNotEmpty(fichePresentationDecret.getNumJO())) {
				eppEvt35.setNumeroJo(new Integer(fichePresentationDecret.getNumJO()));
			}

			if (fichePresentationDecret.getDateJO() != null) {
				eppEvt35.setDateJo(DateUtil.calendarToXMLGregorianCalendar(fichePresentationDecret.getDateJO()));
			}

			eppEvt35.getCopie().clear();
			eppEvtContainer.setEvt35(eppEvt35);
			eppEvtContainer.setType(EvenementType.EVT_35);
			creerVersionRequest.setEvenement(eppEvtContainer);

			CreerVersionResponse creerVersionResponseAN = null;
			try {
				creerVersionResponseAN = wsEvenement.creerVersion(creerVersionRequest);
			} catch (final HttpTransactionException e) {
				LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
				throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
			} catch (final Exception e) {
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
				throw new ClientException(e);
			}

			if (creerVersionResponseAN == null) {
				throw new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
			} else if (creerVersionResponseAN.getStatut() == null
					|| !TraitementStatut.OK.equals(creerVersionResponseAN.getStatut())) {

				ClientException clientExc = new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
								+ WSErrorHelper.buildCleanMessage(creerVersionResponseAN.getMessageErreur()));
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
				throw clientExc;
			}

		}
		final List<Message> messagesSenat = rechercherMessage(session, wsEvenement, fichePresentationDecret.getNor(),
				EvenementType.EVT_35, Institution.SENAT);

		if (messagesSenat == null || messagesSenat.size() == 0) {
			eppEvt35.setIdDossier(fichePresentationDecret.getNor());

			// SENAT
			eppEvt35.setDestinataire(Institution.SENAT);

			eppEvt35.getCopie().clear();
			eppEvtContainer.setEvt35(eppEvt35);
			eppEvtContainer.setType(EvenementType.EVT_35);

			creerVersionRequest.setEvenement(eppEvtContainer);

			CreerVersionResponse creerVersionResponseSE = null;
			try {
				creerVersionResponseSE = wsEvenement.creerVersion(creerVersionRequest);
			} catch (final HttpTransactionException e) {
				LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
				throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
			} catch (final Exception e) {
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
				throw new ClientException(e);
			}

			if (creerVersionResponseSE == null) {
				throw new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué.");
			} else if (creerVersionResponseSE.getStatut() == null
					|| !TraitementStatut.OK.equals(creerVersionResponseSE.getStatut())) {
				ClientException clientExc = new ClientException(
						"Erreur de communication avec SOLON EPP, la création de la communication a échoué."
								+ WSErrorHelper.buildCleanMessage(creerVersionResponseSE.getMessageErreur()));
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
				throw clientExc;
			}
		}

	}

	private List<Message> rechercherMessage(final CoreSession session, final WSEvenement wsEvenement,
			final String idDossier, final EvenementType evenementType, final Institution institution)
			throws ClientException {

		final RechercherEvenementRequest rechercherEvenementRequest01 = new RechercherEvenementRequest();

		final CritereRechercheEvenement critereRechercheEvenement01 = new CritereRechercheEvenement();
		critereRechercheEvenement01.setIdDossier(idDossier);
		critereRechercheEvenement01.setTypeEvenement(evenementType);
		critereRechercheEvenement01.setDestinataire(institution);

		rechercherEvenementRequest01.setParCritere(critereRechercheEvenement01);

		RechercherEvenementResponse rechercherEvenementResponse01 = null;

		try {
			rechercherEvenementResponse01 = wsEvenement.rechercherEvenement(rechercherEvenementRequest01);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_MESSAGE_TEC, e);
			throw new ClientException(e);
		}

		if (rechercherEvenementResponse01 == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la récupération de la communication a échoué.");
		} else if (rechercherEvenementResponse01.getStatut() == null
				|| !TraitementStatut.OK.equals(rechercherEvenementResponse01.getStatut())) {
			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la récupération de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(rechercherEvenementResponse01.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}

		return rechercherEvenementResponse01.getMessage();
	}

	/**
	 * mettre En Attente pour Relancer
	 * 
	 * @param session
	 * @param idEvenement
	 * @param mettreEnAttente
	 * @throws ClientException
	 */
	@Override
	public void mettreEnAttenteRelancer(CoreSession session, String idEvenement, boolean mettreEnAttente)
			throws ClientException {
		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		MajInterneRequest majInterneRequest = new MajInterneRequest();
		MajInterneResponse majInterneResponse;
		majInterneRequest.setIdEvenement(idEvenement);
		if (mettreEnAttente) {
			majInterneRequest.getInterne().add("EN_ATTENTE");
		}

		try {
			majInterneResponse = wsEvenement.majInterne(majInterneRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_COMM_TEC, ECHEC_TRANSITION_TRAITER, e);
			throw new ClientException(e);
		}

		if (majInterneResponse == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la récupération de la communication a échoué.");
		} else if (majInterneResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(majInterneResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la récupération de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(majInterneResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}
	}

	@Override
	public String generateIdDossierDOC() {
		return generateIdDossier(DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES,
				SOLON_MGPP_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES_SEQUENCER);
	}

	private EppBaseEvenement getPolitiqueGeneraleEvt(EppEvtContainer eppEvtContainer) {
		if (eppEvtContainer != null) {

			if (eppEvtContainer.getPG01() != null) {
				return eppEvtContainer.getPG01();
			} else if (eppEvtContainer.getPG02() != null) {
				return eppEvtContainer.getPG02();
			} else if (eppEvtContainer.getPG03() != null) {
				return eppEvtContainer.getPG03();
			} else if (eppEvtContainer.getPG04() != null) {
				return eppEvtContainer.getPG04();
			} else if (eppEvtContainer.getGENERIQUE11() != null) {
				return eppEvtContainer.getGENERIQUE11();
			} else if (eppEvtContainer.getALERTE11() != null) {
				return eppEvtContainer.getALERTE11();
			} else if (eppEvtContainer.getFUSION11() != null) {
				return eppEvtContainer.getFUSION11();
			}
		}
		return null;
	}

	private EppBaseEvenement getArticle283CEvt(EppEvtContainer eppEvtContainer) {
		if (eppEvtContainer != null) {

			if (eppEvtContainer.getJSS01() != null) {
				return eppEvtContainer.getJSS01();
			} else if (eppEvtContainer.getJSS02() != null) {
				return eppEvtContainer.getJSS02();
			} else if (eppEvtContainer.getGENERIQUE13() != null) {
				return eppEvtContainer.getGENERIQUE13();
			} else if (eppEvtContainer.getALERTE13() != null) {
				return eppEvtContainer.getALERTE13();
			} else if (eppEvtContainer.getFUSION13() != null) {
				return eppEvtContainer.getFUSION13();
			}
		}
		return null;
	}

	private EppBaseEvenement getDocsTransmisAuxAssembleesEvt(EppEvtContainer eppEvtContainer) {
		if (eppEvtContainer != null) {

			if (eppEvtContainer.getDOC01() != null) {
				return eppEvtContainer.getDOC01();
			} else if (eppEvtContainer.getGENERIQUE15() != null) {
				return eppEvtContainer.getGENERIQUE15();
			} else if (eppEvtContainer.getALERTE15() != null) {
				return eppEvtContainer.getALERTE15();
			} else if (eppEvtContainer.getFUSION15() != null) {
				return eppEvtContainer.getFUSION15();
			}
		}
		return null;
	}

	private EppBaseEvenement getSujetDetermineEvt(EppEvtContainer eppEvtContainer) {
		if (eppEvtContainer != null) {

			if (eppEvtContainer.getSD01() != null) {
				return eppEvtContainer.getSD01();
			} else if (eppEvtContainer.getSD02() != null) {
				return eppEvtContainer.getSD02();
			} else if (eppEvtContainer.getSD03() != null) {
				return eppEvtContainer.getSD03();
			} else if (eppEvtContainer.getGENERIQUE12() != null) {
				return eppEvtContainer.getGENERIQUE12();
			} else if (eppEvtContainer.getALERTE12() != null) {
				return eppEvtContainer.getALERTE12();
			} else if (eppEvtContainer.getFUSION12() != null) {
				return eppEvtContainer.getFUSION12();
			}
		}
		return null;
	}

	private String generateIdDossierPG() {
		return generateIdDossier(DECLARATION_POILITIQUE_GENERALE, SOLON_MGPP_POILITIQUE_GENERALE_SEQUENCER);
	}

	private String generateIdDossierSD() {
		return generateIdDossier(DECLARATION_SUJET_DETERMINE, SOLON_MGPP_SUJET_DETERMINE_SEQUENCER);
	}

	private String generateIdDossierJSS() {
		return generateIdDossier(JOURS_SUPPLÉMENTAIRES_DE_SEANCE, SOLON_MGPP_ARTICLE_28_3C_SEQUENCER);
	}

	private String generateIdDossier(String prefix, String sequence) {
		StringBuilder idossier = new StringBuilder();
		final Calendar date = Calendar.getInstance();
		final String annee = Integer.toString(date.get(Calendar.YEAR)).substring(2, 4);

		final UIDGeneratorService uidGeneratorService = ServiceHelper.getUIDGeneratorService();
		final UIDSequencer sequencer = uidGeneratorService.getSequencer();
		final String compteur = String.format("%04d", sequencer.getNext(sequence + annee) - 1);

		return idossier.append(prefix).append(annee).append(compteur).toString();
	}

	@Override
	public void enCoursDeTraitementEvenement(MessageDTO messageDTO, EvenementDTO evenementDTO, CoreSession session,
			DossierLink dossierLink) throws ClientException {

		if (messageDTO == null) {
			throw new ClientException("Impossible de traiter cette communication, message vide");
		}

		if (evenementDTO == null) {
			throw new ClientException("Impossible de traiter cette communication, communication vide");
		}

		WSEpp wsEpp = null;

		try {
			wsEpp = SolonMgppWsLocator.getWSEpp(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final NotifierTransitionRequest notifierTransitionRequest = new NotifierTransitionRequest();
		notifierTransitionRequest.setEtat(EtatMessage.EN_COURS_TRAITEMENT);
		notifierTransitionRequest.setIdEvenement(evenementDTO.getIdEvenement());

		NotifierTransitionResponse notifierTransitionResponse = null;
		try {
			notifierTransitionResponse = wsEpp.notifierTransition(notifierTransitionRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_COMM_TEC, ECHEC_TRANSITION_TRAITER, e);
			throw new ClientException(e);
		}

		if (notifierTransitionResponse == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, le passage à traiter de la communication a échoué.");
		} else if (notifierTransitionResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(notifierTransitionResponse.getStatut())) {

			ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, le passage à en cours de traitement de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(notifierTransitionResponse.getMessageErreur()));
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
			throw clientExc;
		}
	}

	private boolean isOrganismePresent(List<Organisme> lstOrgs, Organisme orgRecherche) {
		int i = 0;
		boolean found = false;
		if (lstOrgs != null && !lstOrgs.isEmpty()) {

			while (i < lstOrgs.size() && !found) {
				if (lstOrgs.get(i) != null && lstOrgs.get(i).getId().equals(orgRecherche.getId())) {
					found = true;
				}
				i++;
			}
		}
		return found;
	}

}
