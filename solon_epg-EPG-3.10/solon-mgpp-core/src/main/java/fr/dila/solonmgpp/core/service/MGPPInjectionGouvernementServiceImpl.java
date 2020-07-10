package fr.dila.solonmgpp.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.client.InjectionEpgGvtDTO;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.core.client.InjectionEpgGvtDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonmgpp.api.node.GouvernementNode;
import fr.dila.solonmgpp.api.node.IdentiteNode;
import fr.dila.solonmgpp.api.node.MandatNode;
import fr.dila.solonmgpp.api.node.MinistereNode;
import fr.dila.solonmgpp.api.service.MGPPInjectionGouvernementService;
import fr.dila.solonmgpp.api.service.TableReferenceService;
import fr.dila.solonmgpp.core.node.ActeurNodeImpl;
import fr.dila.solonmgpp.core.node.GouvernementNodeImpl;
import fr.dila.solonmgpp.core.node.IdentiteNodeImpl;
import fr.dila.solonmgpp.core.node.MandatNodeImpl;
import fr.dila.solonmgpp.core.node.MinistereNodeImpl;
import fr.dila.ss.api.client.InjectionGvtDTO;
import fr.dila.ss.core.client.InjectionGvtDTOImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.xsd.solon.epp.Institution;
import fr.sword.xsd.solon.epp.Mandat;
import fr.sword.xsd.solon.epp.TypeMandat;

/**
 * 
 * @author jbrunet
 * 
 *         Service d'injection du gouvernement EPP via le MGPP
 * 
 */
public class MGPPInjectionGouvernementServiceImpl implements MGPPInjectionGouvernementService {

	private static final long		serialVersionUID		= 1L;

	private static final STLogger	LOGGER					= STLogFactory
																	.getLog(MGPPInjectionGouvernementService.class);

	protected static final String	ENTITE_AJOUT			= "Nouveau";
	protected static final String	ENTITE_INCHANGE			= "Inchangé";
	protected List<InjectionGvtDTO>	lstGouvACreer			= new ArrayList<InjectionGvtDTO>();
	protected List<InjectionGvtDTO>	lstGouvACreerSansNorEPP	= new ArrayList<InjectionGvtDTO>();
	protected List<InjectionGvtDTO>	lstGouvAModifier		= new ArrayList<InjectionGvtDTO>();
	protected List<InjectionGvtDTO>	lstGouvACreerAvecNorEPP	= new ArrayList<InjectionGvtDTO>();

	/**
	 * Récupère la ligne correspondant au gouvernement (1ère ligne)
	 * 
	 * @param listInjection
	 * @return
	 */
	public InjectionGvtDTO getNewGovernment(List<InjectionGvtDTO> listInjection) {
		return listInjection.get(0);
	}

	/**
	 * Permet d'initialiser les listes des éléments à traiter lors de l'injection
	 * 
	 * @param listInjection
	 * @return
	 */
	private void initAllEntities(List<InjectionGvtDTO> listInjection) {
		lstGouvACreer.clear();
		lstGouvACreerAvecNorEPP.clear();
		lstGouvACreerSansNorEPP.clear();
		lstGouvAModifier.clear();

		// On parcourt la liste des injections à faire afin de prétrier les gouvernements/ministères en fonction des
		// actions à effectuer
		for (InjectionGvtDTO entity : listInjection) {
			//
			if (!entity.isGvt() && entity.isaCreerSolon() && !entity.isaModifierSolon()
					&& (!entity.isaCreerReponses() || entity.getNorEPP() != null)) {
				lstGouvAModifier.add(entity);
			}
			if (!entity.isGvt() && entity.isaCreerReponses() && entity.getNorEPP() == null) {
				lstGouvACreerSansNorEPP.add(entity);
			}
			if (!entity.isGvt() && entity.isNouvelleEntiteEPP()
					&& (!entity.isaCreerReponses() || entity.getNorEPP() != null)) {
				lstGouvACreerAvecNorEPP.add(entity);
			}
			if (!entity.isGvt() && entity.isaCreerSolon() && !entity.isaModifierSolon()
					&& (!entity.isaCreerReponses() || entity.getNorEPP() != null)) {
				lstGouvACreer.add(entity);
			}
		}
	}

	/**
	 * Récupère toutes les entités qui ne sont pas de type gouvernement
	 * 
	 * @param listInjection
	 * @return
	 */
	public List<InjectionGvtDTO> getAllEntities(List<InjectionGvtDTO> listInjection) {
		List<InjectionGvtDTO> formattedList = new ArrayList<InjectionGvtDTO>();
		for (InjectionGvtDTO entity : listInjection) {
			if (!entity.isGvt()) {
				formattedList.add(entity);
			}
		}
		return formattedList;
	}

	/**
	 * Ajoute un nouveau gouvernement dans l'organigramme
	 * 
	 * @param session
	 * @param newGov
	 * @throws ClientException
	 */
	private void addNewGovernment(CoreSession session, InjectionGvtDTO newGov) throws ClientException {
		TableReferenceService tableRefService = SolonMgppServiceLocator.getTableReferenceService();
		Calendar cal = Calendar.getInstance();
		cal.setTime(newGov.getDateDeDebut());
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		GouvernementNode gouvernementNode = new GouvernementNodeImpl();
		gouvernementNode.setDateDebut(cal.getTime());
		gouvernementNode.setAppellation(newGov.getLibelleLong());
		tableRefService.createGouvernement(gouvernementNode, session);
		String idGov = (gouvernementNode.getIdentifiant() == null ? gouvernementNode.toGouvernementXsd().getId()
				: gouvernementNode.getIdentifiant());
		LOGGER.info(STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC, "Identifiant du nouveau gouvernement : " + idGov);
		newGov.setId(idGov);
	}

	@Override
	public List<InjectionEpgGvtDTO> createComparedDTO(CoreSession documentManager, List<InjectionGvtDTO> listInjection,
			List<InjectionEpgGvtDTO> resultList) throws ClientException {

		Map<String, InjectionGvtDTO> mapMinObtenus = SolonEpgServiceLocator.getEpgInjectionGouvernementService()
				.createFromInjectionDto(documentManager, listInjection);
		OrganigrammeNode currentGvt = STServiceLocator.getSTGouvernementService().getCurrentGouvernement();

		for (InjectionGvtDTO newInject : listInjection) {
			InjectionEpgGvtDTOImpl result = null;
			// Cas du Gouvernement
			if (newInject.isGvt()) {
				result = new InjectionEpgGvtDTOImpl(ENTITE_AJOUT, new InjectionGvtDTOImpl(null, false, null,
						currentGvt.getLabel(), null, null, null, null, null, currentGvt.getDateDebut(),
						currentGvt.getDateFin(), true), newInject);

			} else {
				// Si modifier = 0 && creer = 0 : inchangé
				if (!newInject.isaCreerSolon() && !newInject.isaModifierSolon() && !newInject.isNouvelleEntiteEPP()
						&& !(newInject.isaCreerReponses() && newInject.getNorEPP() == null)) {
					InjectionGvtDTO minObtenu = mapMinObtenus.get(newInject.getNor());
					minObtenu.setOrdreProtocolaireReponses(newInject.getOrdreProtocolaireReponses());

					result = new InjectionEpgGvtDTOImpl(ENTITE_INCHANGE, minObtenu, null);

				} else if (newInject.isaCreerSolon() && newInject.isaModifierSolon()) {
					// cas impossible : a créer ET à modifier
					throw new EPGException("L'entité de NOR : " + newInject.getNor() + " est à modifier et à créer");
				} else {
					result = new InjectionEpgGvtDTOImpl(ENTITE_AJOUT, null, newInject);

				}
			}

			if (result != null) {

				if (result.getImportedGvt() != null) {
					result.setHasNewIdentite(hasNewIdentite(result.getImportedGvt(), documentManager));
					result.setHasNewMandat(hasNewMandat(result.getImportedGvt()));
					result.setHasNewMinistere(hasNewMinistere(result.getImportedGvt(), documentManager));
				}
				resultList.add(result);
			}
		}
		return resultList;

	}

	private boolean hasNewMinistere(InjectionGvtDTO newMin, CoreSession documentManager) throws ClientException {
		TableReferenceService tableRefService = SolonMgppServiceLocator.getTableReferenceService();
		if (newMin == null) {
			return false;
		} else if ((newMin.isaCreerReponses() && newMin.getNorEPP() == null)) {
			return true;
		} else if (newMin.isaModifierSolon() || (newMin.isaCreerSolon() && newMin.getNorEPP() == null)) {
			if (tableRefService.getMinistereNode(documentManager, newMin.getLibelleLong(), newMin.getFormule(),
					newMin.getLibelleLong(), newMin.getLibelleCourt()) == null) {
				return true;
			}
		} else if (newMin.isaCreerSolon() && newMin.getNorEPP() != null) {
			List<Mandat> listMandats = tableRefService.getMandatsByNor(newMin.getNorEPP(), documentManager);
			if (listMandats != null && !listMandats.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private boolean hasNewIdentite(InjectionGvtDTO newMin, CoreSession documentManager) throws ClientException {
		TableReferenceService tableRefService = SolonMgppServiceLocator.getTableReferenceService();
		if (newMin == null) {
			return false;
		} else if ((newMin.isaCreerReponses() && newMin.getNorEPP() == null) || newMin.isNouvelleEntiteEPP()) {
			return true;
		} else if (newMin.isaCreerSolon() || newMin.isaModifierSolon()) {
			if (tableRefService.getIdentiteNode(documentManager, newMin.getPrenomNom()) == null) {
				return true;
			}
		}
		return false;
	}

	private boolean hasNewMandat(InjectionGvtDTO newMin) {
		if (newMin == null) {
			return false;
		}
		return ((newMin.isaCreerReponses() && newMin.getNorEPP() == null) || newMin.isaCreerSolon() || newMin
				.isaModifierSolon());
	}

	@Override
	public void executeInjection(CoreSession session, List<InjectionGvtDTO> listInjection) throws ClientException {
		try {
			InjectionGvtDTO newGouv = getNewGovernment(listInjection);

			initAllEntities(listInjection);
			// Modification du gouvernement
			addNewGovernment(session, newGouv);
			// Réponses à créer et NOR EPP vide
			// Ajout de ministère, de mandat et d'identité
			createMinistereMandatIdentite(session, lstGouvACreerSansNorEPP, newGouv);
			// Ajout de nouvelle Identité
			createNouvelleIdentiteEPP(session, lstGouvACreerAvecNorEPP);
			// SOLON à modifier = 1
			createMandatSolonModifier(session, lstGouvAModifier, newGouv);
			// SOLON à créer = 1
			createMandatSolonCreer(session, lstGouvACreer, newGouv);
		} catch (ClientException e) {
			throw new EPGException("Echec lors de l'injection du gouvernement (" + e.getMessage() + ")", e);
		}
	}

	/**
	 * Crée un nouveau ministère rattaché à un gouvernement
	 * 
	 * @param session
	 * @param newMin
	 * @param newGov
	 *            : le gouvernement de rattachement
	 * @return
	 * @throws ClientException
	 */
	private MinistereNode createMinistere(CoreSession session, InjectionGvtDTO newMin, InjectionGvtDTO newGov)
			throws ClientException {
		Calendar cal = Calendar.getInstance();
		MinistereNode ministereNode = new MinistereNodeImpl();
		ministereNode.setGouvernement(newGov.getId());
		cal.setTime(newMin.getDateDeDebut());
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		ministereNode.setDateDebut(cal.getTime());
		ministereNode.setAppellation(newMin.getLibelleLong());
		ministereNode.setNom(newMin.getLibelleLong());
		ministereNode.setEdition(newMin.getLibelleCourt());
		ministereNode.setLibelle(newMin.getFormule());
		SolonMgppServiceLocator.getTableReferenceService().createMinistere(ministereNode, session);
		String idMin = ministereNode.getIdentifiant();
		newMin.setId(idMin);
		LOGGER.info(STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC, "Identifiant du nouveau ministère : " + idMin);
		return ministereNode;
	}

	/**
	 * Instancie un nouveau mandat. Celui ci n'est pas créé dans les tables de références.
	 * 
	 * @param session
	 * @param newMin
	 * @return
	 * @throws ClientException
	 */
	private MandatNode createMandat(CoreSession session, InjectionGvtDTO newMin) throws ClientException {
		Calendar cal = Calendar.getInstance();
		MandatNode mandatNode = new MandatNodeImpl();
		cal.setTime(newMin.getDateDeDebut());
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		mandatNode.setDateDebut(cal.getTime());
		mandatNode.setTypeMandat(TypeMandat.MINISTERE.value());
		mandatNode.setOrdreProtocolaire(Long.valueOf(newMin.getOrdreProtocolaireReponses()));
		mandatNode.setAppellation(newMin.getLibelleLong());
		mandatNode.setProprietaire(Institution.GOUVERNEMENT.name());
		if (newMin.getNorEPP() == null) {
			mandatNode.setTitre("Ministre");
		} else {
			mandatNode.setTitre("Ministre délégué");
		}
		mandatNode.setNor(newMin.getNor());
		return mandatNode;
	}

	/**
	 * Crée une nouvelle identité
	 * 
	 * @param session
	 * @param newMin
	 * @return
	 */
	private IdentiteNode createIdentite(CoreSession session, InjectionGvtDTO newMin) {
		IdentiteNode identiteNode = new IdentiteNodeImpl();
		identiteNode.setActeur(new ActeurNodeImpl());
		identiteNode.setCivilite(newMin.getCivilite());
		if (newMin.getPrenom() == null || newMin.getNom() == null) {
			String[] parts = newMin.getPrenomNom().split(" ");
			identiteNode.setPrenom(parts[0]);
			StringBuilder builder = new StringBuilder();
			for (int i = 1; i < parts.length; i++) {
				builder.append(parts[i]);
			}
			identiteNode.setNom(builder.toString());
		} else {
			identiteNode.setNom(newMin.getNom());
			identiteNode.setPrenom(newMin.getPrenom());
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(newMin.getDateDeDebut());
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		identiteNode.setDateDebut(cal.getTime());
		return identiteNode;
	}

	/**
	 * Ajoute dans les tables de références les éléments suivants : -Ministère -Mandat correspondat -Identité rattachée
	 * 
	 * @param session
	 * @param newEntities
	 * @param newGov
	 * @throws ClientException
	 */
	private void createMinistereMandatIdentite(CoreSession session, List<InjectionGvtDTO> newEntities,
			InjectionGvtDTO newGov) throws ClientException {
		for (InjectionGvtDTO newEntity : newEntities) {
			IdentiteNode identiteNode = createIdentite(session, newEntity);
			MinistereNode ministereNode = createMinistere(session, newEntity, newGov);
			MandatNode mandatNode = createMandat(session, newEntity);
			mandatNode.setIdentite(identiteNode);
			mandatNode.setMinistere(ministereNode.getIdentifiant());
			SolonMgppServiceLocator.getTableReferenceService().createMandat(mandatNode, session);
		}
	}

	/**
	 * Ajoute aux tables de référence les nouvelles identités
	 * 
	 * @param session
	 * @param newEntities
	 * @throws ClientException
	 */
	private void createNouvelleIdentiteEPP(CoreSession session, List<InjectionGvtDTO> newEntities)
			throws ClientException {
		TableReferenceService tableRefService = SolonMgppServiceLocator.getTableReferenceService();
		for (InjectionGvtDTO newEntity : newEntities) {
			IdentiteNode identiteNode = createIdentite(session, newEntity);
			tableRefService.createIdentite(identiteNode, session);
		}
	}

	/**
	 * Applique les traitements correspondants à SOLON à modifier = 1
	 * 
	 * @param session
	 * @param newEntities
	 * @param newGov
	 * @throws ClientException
	 */
	private void createMandatSolonModifier(CoreSession session, List<InjectionGvtDTO> newEntities,
			InjectionGvtDTO newGov) throws ClientException {
		TableReferenceService tableRefService = SolonMgppServiceLocator.getTableReferenceService();
		for (InjectionGvtDTO newEntity : newEntities) {
			MandatNode mandatNode = createMandat(session, newEntity);
			IdentiteNode identiteNode = tableRefService.getIdentiteNode(session, newEntity.getPrenomNom());
			if (identiteNode == null) {
				identiteNode = createIdentite(session, newEntity);
			}
			mandatNode.setIdentite(identiteNode);
			// Gestion du ministère lié
			MinistereNode ministereNode = tableRefService.getMinistereNode(session, newEntity.getLibelleLong(),
					newEntity.getFormule(), newEntity.getLibelleLong(), newEntity.getLibelleCourt());
			if (ministereNode == null) {
				ministereNode = createMinistere(session, newEntity, newGov);
			}
			mandatNode.setMinistere(ministereNode.getIdentifiant());
			tableRefService.createMandat(mandatNode, session);
		}
	}

	/**
	 * Applique les traitements correspondants à SOLON à créer = 1
	 * 
	 * @param session
	 * @param newEntities
	 * @param newGov
	 * @throws ClientException
	 */
	private void createMandatSolonCreer(CoreSession session, List<InjectionGvtDTO> newEntities, InjectionGvtDTO newGov)
			throws ClientException {
		TableReferenceService tableRefService = SolonMgppServiceLocator.getTableReferenceService();
		for (InjectionGvtDTO newEntity : newEntities) {
			MinistereNode ministereNode = createMinistere(session, newEntity, newGov);
			MandatNode mandatNode = createMandat(session, newEntity);
			IdentiteNode identiteNode = tableRefService.getIdentiteNode(session, newEntity.getPrenomNom());
			if (identiteNode == null) {
				identiteNode = createIdentite(session, newEntity);
			}
			mandatNode.setIdentite(identiteNode);
			// Gestion du ministère lié
			if (newEntity.getNorEPP() == null) {
				mandatNode.setMinistere(ministereNode.getIdentifiant());
			} else {
				List<Mandat> listMandats = tableRefService.getMandatsByNor(newEntity.getNorEPP(), session);
				if (listMandats == null || listMandats.isEmpty()) {
					mandatNode.setMinistere(ministereNode.getIdentifiant());
				} else {
					mandatNode.setMinistere(listMandats.get(0).getIdMinistere());
				}
			}
			tableRefService.createMandat(mandatNode, session);
		}
	}

}
