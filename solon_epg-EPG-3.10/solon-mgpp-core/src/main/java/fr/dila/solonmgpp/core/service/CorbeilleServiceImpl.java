package fr.dila.solonmgpp.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.In;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepp.rest.api.WSEpp;
import fr.dila.solonmgpp.api.constant.SolonMgppBaseFunctionConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppCorbeilleConstant;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
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
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.dto.CorbeilleDTO;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.enumeration.CorbeilleTypeObjet;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.CorbeilleService;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.solonmgpp.core.descriptor.CorbeilleDescriptor;
import fr.dila.solonmgpp.core.descriptor.CorbeilleNodeDescriptor;
import fr.dila.solonmgpp.core.dto.CorbeilleDTOImpl;
import fr.dila.solonmgpp.core.util.WSErrorHelper;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STDossierLinkConstant;
import fr.dila.st.api.constant.STQueryConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.rest.client.HttpTransactionException;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epp.Action;
import fr.sword.xsd.solon.epp.ChercherCorbeilleRequest;
import fr.sword.xsd.solon.epp.ChercherCorbeilleResponse;
import fr.sword.xsd.solon.epp.Corbeille;
import fr.sword.xsd.solon.epp.HasCommunicationNonTraiteesResponse.CorbeilleInfos;
import fr.sword.xsd.solon.epp.Section;

/**
 * Implementation de {@link CorbeilleService}
 * 
 * @author asatre
 * 
 */
public class CorbeilleServiceImpl extends DefaultComponent implements CorbeilleService {

	private static final STLogger			LOGGER						= STLogFactory
																				.getLog(CorbeilleServiceImpl.class);

	public static final String				CORBEILLE_EXTENSION_POINT	= "corbeille";

	@In(create = true, required = false)
	private static final String				QUERY_HAS_COMM_ALL_SQL		= STQueryConstant.SELECT
																				+ SolonMgppCorbeilleConstant.COL_CORBEILLE_MGPP_INFO_CORBEILLE
																				+ ", "
																				+ SolonMgppCorbeilleConstant.COL_HASMESS_MGPP_INFO_CORBEILLE
																				+ STQueryConstant.FROM
																				+ SolonMgppCorbeilleConstant.MGPP_INFO_CORBEILLE_TABLE_NAME
																				+ STQueryConstant.WHERE
																				+ SolonMgppCorbeilleConstant.COL_HASMESS_MGPP_INFO_CORBEILLE
																				+ STQueryConstant.EQUAL + " 1 ";

	private Map<String, List<CorbeilleDTO>>	mapCorbeille				= null;

	private final Set<String>				setCorbeilleLoaded			= new HashSet<String>();
	private Map<String, List<CorbeilleDTO>>	corbeilleMap;

	/**
	 * Mapping action, idCorbeille
	 */
	private Map<String, String>				fetchMap;

	@Override
	public void activate(ComponentContext context) {
		corbeilleMap = new HashMap<String, List<CorbeilleDTO>>();
		fetchMap = new HashMap<String, String>();
	}

	@Override
	public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
		if (extensionPoint.equals(CORBEILLE_EXTENSION_POINT)) {
			CorbeilleDescriptor descriptor = (CorbeilleDescriptor) contribution;
			List<CorbeilleDTO> corbeillesDTO = buildCorbeilleDTOFromCorbeilleDescriptor(descriptor
					.getCorbeilleNodeList());
			corbeilleMap.put(descriptor.getName(), corbeillesDTO);
			fetchMap.put(descriptor.getAction(), descriptor.getName());
		} else {
			throw new IllegalArgumentException("Unknown extension point: " + extensionPoint);
		}
	}

	/**
	 * COnstruit une liste de {@link CorbeilleDTO} à partir de la liste des {@link CorbeilleNodeDescriptor}
	 * 
	 * @param descriptors
	 * @return
	 */
	private List<CorbeilleDTO> buildCorbeilleDTOFromCorbeilleDescriptor(final List<CorbeilleNodeDescriptor> descriptors) {
		List<CorbeilleDTO> list = new ArrayList<CorbeilleDTO>();

		for (CorbeilleNodeDescriptor corbeilleNodeDescriptor : descriptors) {
			CorbeilleDTO corbeilleDTO = new CorbeilleDTOImpl();
			corbeilleDTO.setIdCorbeille(corbeilleNodeDescriptor.getName());
			corbeilleDTO.setNom(corbeilleNodeDescriptor.getLabel());
			corbeilleDTO.setRoutingTaskType(corbeilleNodeDescriptor.getRoutingTaskType());
			corbeilleDTO.setTypeActe(corbeilleNodeDescriptor.getTypeActe());
			corbeilleDTO.setCorbeille(new ArrayList<CorbeilleDTO>());
			corbeilleDTO.setType(SolonMgppCorbeilleConstant.MGPP_NODE);
			corbeilleDTO.setIfMember(corbeilleNodeDescriptor.getIfMember());
			corbeilleDTO.setTypeObjet(CorbeilleTypeObjet.findByName(corbeilleNodeDescriptor.getType()));
			corbeilleDTO.setAdoption(corbeilleNodeDescriptor.isAdoption());
			corbeilleDTO.setParent(corbeilleNodeDescriptor.getParent());
			list.add(corbeilleDTO);
		}

		return list;
	}

	private void findAllCorbeille(CoreSession session) throws ClientException {

		WSEpp wsEpp = null;
		try {
			wsEpp = SolonMgppWsLocator.getWSEpp(session);
		} catch (WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(e);
		}

		ChercherCorbeilleResponse chercherCorbeilleResponse = null;
		try {
			chercherCorbeilleResponse = wsEpp.chercherCorbeille(new ChercherCorbeilleRequest());
		} catch (HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (Exception e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		if (chercherCorbeilleResponse == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la récupération des corbeilles a échoué.");
		} else if (chercherCorbeilleResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(chercherCorbeilleResponse.getStatut())) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, chercherCorbeilleResponse.getMessageErreur());
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la récupération des corbeilles a échoué."
							+ WSErrorHelper.buildCleanMessage(chercherCorbeilleResponse.getMessageErreur()));
		}

		mapCorbeille = new HashMap<String, List<CorbeilleDTO>>();

		for (Section section : chercherCorbeilleResponse.getSection()) {

			CorbeilleDTO corbeilleDTOParent = buildCorbeilleDTOFromSection(section);

			// recuperation des sections (corbeille avec emis et recu)
			for (Corbeille corbeille : section.getCorbeille()) {
				CorbeilleDTO corbeilleDTO = buildCorbeilleDTOFromCorbeille(corbeille);
				corbeilleDTOParent.getCorbeille().add(corbeilleDTO);
			}

			List<CorbeilleDTO> list = new ArrayList<CorbeilleDTO>();
			list.add(corbeilleDTOParent);
			mapCorbeille.put(corbeilleDTOParent.getIdCorbeille(), list);
		}

		for (Corbeille corbeille : chercherCorbeilleResponse.getCorbeille()) {
			// recupération corbeille simple
			CorbeilleDTO corbeilleDTO = buildCorbeilleDTOFromCorbeille(corbeille);

			List<CorbeilleDTO> list = new ArrayList<CorbeilleDTO>();
			list.add(corbeilleDTO);
			mapCorbeille.put(corbeilleDTO.getIdCorbeille(), list);
		}

	}

	private CorbeilleDTO buildCorbeilleDTOFromSection(final Section section) {
		CorbeilleDTO corbeilleDTO = new CorbeilleDTOImpl();
		corbeilleDTO.setDescription(section.getDescription());
		corbeilleDTO.setIdCorbeille(section.getIdSection());
		corbeilleDTO.setNom(section.getNom());
		corbeilleDTO.setCorbeille(new ArrayList<CorbeilleDTO>());
		corbeilleDTO.setType(SolonMgppCorbeilleConstant.ROOT_NODE);
		corbeilleDTO.setTypeObjet(CorbeilleTypeObjet.MESSAGE);
		return corbeilleDTO;
	}

	/**
	 * Construit une {@link CorbeilleDTO} à partir d'une {@link Corbeille}
	 * 
	 * @param corbeille
	 * @return
	 */
	private CorbeilleDTO buildCorbeilleDTOFromCorbeille(final Corbeille corbeille) {
		CorbeilleDTO corbeilleDTO = new CorbeilleDTOImpl();
		corbeilleDTO.setDescription(corbeille.getDescription());
		corbeilleDTO.setIdCorbeille(corbeille.getIdCorbeille());
		corbeilleDTO.setNom(corbeille.getNom());
		corbeilleDTO.setType(SolonMgppCorbeilleConstant.CORBEILLE_NODE);
		corbeilleDTO.setTypeObjet(CorbeilleTypeObjet.MESSAGE);
		return corbeilleDTO;
	}

	@Override
	public List<CorbeilleDTO> findCorbeille(final String idCorbeille, SSPrincipal ssPrincipal, CoreSession session)
			throws ClientException {

		synchronized (this) {
			if (mapCorbeille == null) {
				findAllCorbeille(session);
			}

			String corbeille = fetchCorbeille(idCorbeille, session);

			// recuperation de la corbeille EPP
			List<CorbeilleDTO> corbeillesDTO = mapCorbeille.get(corbeille);

			if (corbeillesDTO == null) {
				corbeillesDTO = new ArrayList<CorbeilleDTO>();
			}

			// ajout des corbeilles spécifiques au MGPP
			addCorbeilleMGPP(corbeillesDTO, corbeille);

			corbeillesDTO = mapCorbeille.get(corbeille);

			if (corbeillesDTO == null) {
				corbeillesDTO = new ArrayList<CorbeilleDTO>();
			}

			List<CorbeilleDTO> corbeilles = new ArrayList<CorbeilleDTO>();

			Map<String, Boolean> cacheMemberOf = new HashMap<String, Boolean>();

			for (CorbeilleDTO corbeilleDTO : corbeillesDTO) {

				String group = corbeilleDTO.getIfMember();

				if (StringUtils.isBlank(group)) {
					group = SolonMgppBaseFunctionConstant.CORBEILLE_MGPP_UPDATER;
				}

				if (cacheMemberOf.get(group) == null) {
					if (ssPrincipal.isMemberOf(group)) {
						cacheMemberOf.put(group, Boolean.TRUE);
					} else {
						cacheMemberOf.put(group, Boolean.FALSE);
					}
				}

				// verif des droits de visibilité de la corbeille
				if (cacheMemberOf.get(group)) {
					corbeilles.add(corbeilleDTO);
				}
			}

			return corbeilles;

		}

	}

	@Override
	public String fetchCorbeille(String idCorbeille, CoreSession session) throws ClientException {
		String corbeille = fetchMap.get(idCorbeille);
		if (corbeille == null) {
			String message = "Type de corbeille inconnue : " + idCorbeille;
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_CORBEILLE_TEC, message);
			throw new ClientException(message);
		}
		return fetchMap.get(idCorbeille);
	}

	private void addCorbeilleMGPP(final List<CorbeilleDTO> corbeillesDTO, final String corbeille) {
		if (!setCorbeilleLoaded.contains(corbeille)) {
			setCorbeilleLoaded.add(corbeille);
			List<CorbeilleDTO> mgppCorbeilleList = corbeilleMap.get(corbeille);
			for (CorbeilleDTO mgppCorbeille : mgppCorbeilleList) {
				if (mgppCorbeille.getParent() == null) {
					corbeillesDTO.add(mgppCorbeille);
				} else {
					for (CorbeilleDTO corbeilleParent : corbeillesDTO) {
						if (corbeilleParent.getIdCorbeille().equals(mgppCorbeille.getParent())) {
							corbeilleParent.getCorbeille().add(mgppCorbeille);
							break;
						} else {
							corbeillesDTO.add(mgppCorbeille);
						}
					}
				}
			}

			mapCorbeille.put(corbeille, corbeillesDTO);
		}
	}

	@Override
	public Set<String> findActionPossible(CoreSession session, SSPrincipal ssPrincipal, EvenementDTO evenementDTO) {

		if (evenementDTO == null || evenementDTO.getActionSuivante() == null) {
			return new HashSet<String>();
		}

		Set<String> actions = new HashSet<String>(evenementDTO.getActionSuivante());

		if (!ssPrincipal.isMemberOf(SolonMgppBaseFunctionConstant.AR_EVENEMENT)) {
			actions.remove(Action.ACCUSER_RECEPTION.value());
		}

		if (!ssPrincipal.isMemberOf(SolonMgppBaseFunctionConstant.ACCEPTER_EVENEMENT)) {
			actions.remove(Action.ACCEPTER.value());
		}

		if (!ssPrincipal.isMemberOf(SolonMgppBaseFunctionConstant.REJETER_EVENEMENT)) {
			actions.remove(Action.REJETER.value());
		}

		if (!ssPrincipal.isMemberOf(SolonMgppBaseFunctionConstant.SUPPRIMER_VERSION)) {
			actions.remove(Action.SUPPRIMER.value());
		}

		if (!ssPrincipal.isMemberOf(SolonMgppBaseFunctionConstant.CREER_ALERTE)) {
			actions.remove(Action.LEVER_ALERTE.value());
			actions.remove(Action.CREER_ALERTE.value());
		}

		return actions;
	}

	@Override
	public Long countDossier(CoreSession session, CorbeilleNode corbeilleNode) throws ClientException {

		long start = System.nanoTime();
		Long count = 0L;
		switch (corbeilleNode.getTypeObjet()) {

			case DOSSIER:
				List<Object> params = new ArrayList<Object>();

				long step1 = System.nanoTime();
				StringBuilder queryDossier = getDossierQuery(corbeilleNode, params);
				String query = QueryUtils.ufnxqlToFnxqlQuery(queryDossier.toString());
				long step2 = System.nanoTime();
				count = QueryUtils.doCountQuery(session, query, params.toArray());
				long tmp = System.nanoTime();
				System.out.println((tmp - start) + "=" + (step1 - start) + "+" + (step2 - step1) + "+" + (tmp - step2));
				break;

			case MESSAGE:
				Long result = 0L;
				result += corbeilleNode.getMessageDTO().size();
				for (CorbeilleNode childNode : corbeilleNode.getCorbeilleNodeList()) {
					result += childNode.getMessageDTO().size();
				}
				count = result;
				break;

			case AVI:
				StringBuilder queryAvi = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
				queryAvi.append(FichePresentationAVI.DOC_TYPE);
				queryAvi.append(" as d ");

				count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(queryAvi.toString()));
				break;

			case OEP:
				StringBuilder queryOep = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
				queryOep.append(FichePresentationOEP.DOC_TYPE);
				queryOep.append(" as d ");

				count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(queryOep.toString()));
				break;

			case FICHE_LOI:

				Calendar date = Calendar.getInstance();
				final ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator.getParametreMgppService()
						.findParametrageMgpp(session);
				Long iFiltre = parametrageMgpp.getFiltreDateCreationLoi();
				date.add(Calendar.MONTH, (int) -iFiltre);
				List<Object> param = new ArrayList<Object>();
				param.add(date);
				StringBuilder queryFicheLoi = new StringBuilder("SELECT d.ecm:uuid as id , d.floi:dateCreation FROM ");
				queryFicheLoi.append(FicheLoi.DOC_TYPE);
				queryFicheLoi.append(" as d ");
				queryFicheLoi.append(" WHERE d.");
				queryFicheLoi.append(FicheLoi.PREFIX);
				queryFicheLoi.append(":");
				queryFicheLoi.append(FicheLoi.DATE_CREATION);
				queryFicheLoi.append(" > ?");

				count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(queryFicheLoi.toString()),
						param.toArray());
				break;

			case FICHE_DR:
				StringBuilder queryDR = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
				queryDR.append(FichePresentationDR.DOC_TYPE);
				queryDR.append(" as d WHERE d.fpdr:rapportParlement != 'RAPPORT_RELATIF_ARTICLE_67_LOI_N_2004-1343'");

				count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(queryDR.toString()));
				break;

			case FICHE_DR_67:
				StringBuilder queryDR67 = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
				queryDR67.append(FichePresentationDR.DOC_TYPE);
				queryDR67.append(" as d WHERE d.fpdr:rapportParlement = 'RAPPORT_RELATIF_ARTICLE_67_LOI_N_2004-1343'");

				count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(queryDR67.toString()));
				break;

			case FICHE_DECRET:
				StringBuilder queryDecret = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
				queryDecret.append(FichePresentationDecret.DOC_TYPE);
				queryDecret.append(" as d");

				count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(queryDecret.toString()));
				break;

			case FICHE_IE:
				StringBuilder queryIE = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
				queryIE.append(FichePresentationIE.DOC_TYPE);
				queryIE.append(" as d");

				count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(queryIE.toString()));
				break;

			case FICHE_341:
				StringBuilder query341 = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
				query341.append(FichePresentation341.DOC_TYPE);
				query341.append(" as d");

				count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(query341.toString()));
				break;

			case FICHE_DPG:
				StringBuilder queryDPG = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
				queryDPG.append(FichePresentationDPG.DOC_TYPE);
				queryDPG.append(" as d");

				count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(queryDPG.toString()));
				break;

			case FICHE_SD:
				StringBuilder querySD = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
				querySD.append(FichePresentationSD.DOC_TYPE);
				querySD.append(" as d");

				count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(querySD.toString()));
				break;

			case FICHE_JSS:
				StringBuilder queryJSS = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
				queryJSS.append(FichePresentationJSS.DOC_TYPE);
				queryJSS.append(" as d");

				count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(queryJSS.toString()));
				break;

			case FICHE_DOC:
				StringBuilder queryDOC = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
				queryDOC.append(FichePresentationDOC.DOC_TYPE);
				queryDOC.append(" as d");

				count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(queryDOC.toString()));
				break;

			case FICHE_AUD:
				StringBuilder queryAUD = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
				queryAUD.append(FichePresentationAUD.DOC_TYPE);
				queryAUD.append(" as d");

				count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(queryAUD.toString()));
				break;

		}

		return count;
	}

	@Override
	public Long countDossierUnrestricted(CoreSession session, CorbeilleNode corbeilleNode) throws ClientException {

		long start = System.nanoTime();
		Long count = 0L;
		switch (corbeilleNode.getTypeObjet()) {

			case DOSSIER:
				List<Object> params = new ArrayList<Object>();

				long step1 = System.nanoTime();
				StringBuilder queryDossier = getDossierQuery(corbeilleNode, params);
				String query = QueryUtils.ufnxqlToFnxqlQuery(queryDossier.toString());
				long step2 = System.nanoTime();
				count = QueryUtils.doUnrestrictedCountQuery(session, query, params.toArray());
				long tmp = System.nanoTime();
				System.out.println((tmp - start) + "=" + (step1 - start) + "+" + (step2 - step1) + "+" + (tmp - step2));
				break;
			default:
				break;
		}

		return count;
	}

	@Override
	public StringBuilder getDossierQuery(CorbeilleNode corbeilleNode, List<Object> params) {

		StringBuilder queryDossier = new StringBuilder("SELECT dl.ecm:uuid as id FROM ");
		queryDossier.append(DossierLink.class.getSimpleName());
		queryDossier.append(" as dl, ");
		queryDossier.append(Dossier.class.getSimpleName());
		queryDossier.append(" as d WHERE dl.");
		queryDossier.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
		queryDossier.append(":");
		queryDossier.append(STSchemaConstant.CASE_LINK_CASE_DOCUMENT_ID_PROPERTY);
		queryDossier.append(" = d.ecm:uuid AND dl.");
		queryDossier.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
		queryDossier.append(":");
		queryDossier.append(STDossierLinkConstant.DOSSIER_LINK_ROUTING_TASK_TYPE_PROPERTY);
		queryDossier.append(" = ? AND d.");
		queryDossier.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
		queryDossier.append(":");
		queryDossier.append(DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_PROPERTY);
		queryDossier.append(" IN ('");
		queryDossier.append(corbeilleNode.getTypeActe());
		queryDossier.append("') AND dl.");
		queryDossier.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
		queryDossier.append(":");
		queryDossier.append(DossierSolonEpgConstants.DOSSIER_LINK_CASE_LIFE_CYCLE_STATE_PROPERTY);
		queryDossier.append(" = ? AND dl.");
		queryDossier.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
		queryDossier.append(":");
		queryDossier.append(DossierSolonEpgConstants.ARCHIVE_DOSSIER_PROPERTY);
		queryDossier.append(" = ? AND dl.");
		queryDossier.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
		queryDossier.append(":");
		queryDossier.append(DossierSolonEpgConstants.DELETED);
		queryDossier.append(" = ? ");

		params.add(corbeilleNode.getRoutingTaskType());
		params.add(Dossier.DossierState.running.toString());
		params.add(0);
		params.add(0);

		if (corbeilleNode.isAdoption()) {
			queryDossier.append(" AND d.");
			queryDossier.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
			queryDossier.append(":");
			queryDossier.append(DossierSolonEpgConstants.ADOPTION);
			queryDossier.append(" = ? ");
			params.add(1);
		}

		// queryDossier.append(" AND testAcl(d.ecm:uuid) = 1 ");

		return queryDossier;
	}

	@Override
	public boolean hasCorbeilleMessages(CoreSession session, String idCorbeille) throws ClientException {
		StringBuilder query = new StringBuilder(QUERY_HAS_COMM_ALL_SQL).append(STQueryConstant.AND)
				.append(SolonMgppCorbeilleConstant.COL_CORBEILLE_MGPP_INFO_CORBEILLE)
				.append(STQueryConstant.EQUAL_PARAM);
		List<CorbeilleInfos> list = getInfosCorbeilleFromQuery(session, query.toString(), new Object[] { idCorbeille });
		return !list.isEmpty();
	}

	@Override
	public boolean haveCorbeillesMessages(CoreSession session, Set<String> idsCorbeilles) throws ClientException {
		StringBuilder query = new StringBuilder(QUERY_HAS_COMM_ALL_SQL).append(STQueryConstant.AND).append(
				QueryUtils.getQuestionMarkQueryWithColumn(SolonMgppCorbeilleConstant.COL_CORBEILLE_MGPP_INFO_CORBEILLE,
						idsCorbeilles.size()));
		List<CorbeilleInfos> infos = getInfosCorbeilleFromQuery(session, query.toString(), idsCorbeilles.toArray());
		return !infos.isEmpty();
	}

	/**
	 * Retourne une liste de CorbeilleInfos avec une requête en paramètre. /!\ la requête en paramètre doit avoir les
	 * colonnes "CORBEILLE" et "HASMESS" dans le select
	 * 
	 * @param session
	 * @param query
	 * @return
	 * @throws ClientException
	 */
	private List<CorbeilleInfos> getInfosCorbeilleFromQuery(CoreSession session, String query, Object[] params) {
		List<CorbeilleInfos> corbeillesInfosList = new ArrayList<CorbeilleInfos>();
		String[] colonnes = { FlexibleQueryMaker.COL_ID, STSchemaConstant.DUBLINCORE_TITLE_XPATH };
		IterableQueryResult res = null;
		try {
			res = QueryUtils.doSqlQuery(session, colonnes, query, params);
			if (res != null) {
				final Iterator<Map<String, Serializable>> iterator = res.iterator();
				while (iterator.hasNext()) {
					corbeillesInfosList.add(getInfosFromRow(iterator.next()));
				}
			}
		} catch (ClientException exc) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_INFO_CORBEILLE_TEC, exc);
		} finally {
			if (res != null) {
				res.close();
			}
		}

		return corbeillesInfosList;
	}

	/**
	 * Renvoi un CorbeilleInfos avec idCorbeille et hasNonTraitees renseignés avec la row en paramètre /!\ la row doit
	 * contenir les colonnes "FlexibleQueryMaker.COL_ID" pour corbeille et "STSchemaConstant.DUBLINCORE_TITLE_XPATH"
	 * pour hasmess
	 * 
	 * @param row
	 * @return
	 */
	private CorbeilleInfos getInfosFromRow(Map<String, Serializable> row) {
		CorbeilleInfos infos = new CorbeilleInfos();
		String corbeilleId = (String) row.get(FlexibleQueryMaker.COL_ID);
		Boolean hasMessage = Boolean.valueOf((String) row.get(STSchemaConstant.DUBLINCORE_TITLE_XPATH));
		infos.setIdCorbeille(corbeilleId);
		infos.setHasNonTraitees(hasMessage.booleanValue());
		return infos;
	}
}
