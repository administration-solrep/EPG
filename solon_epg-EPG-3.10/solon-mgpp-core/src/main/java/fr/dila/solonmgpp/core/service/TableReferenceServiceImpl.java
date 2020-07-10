package fr.dila.solonmgpp.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepp.rest.api.WSEpp;
import fr.dila.solonmgpp.api.dto.IdentiteDTO;
import fr.dila.solonmgpp.api.dto.OrganismeDTO;
import fr.dila.solonmgpp.api.dto.TableReferenceDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.node.ActeurNode;
import fr.dila.solonmgpp.api.node.GouvernementNode;
import fr.dila.solonmgpp.api.node.IdentiteNode;
import fr.dila.solonmgpp.api.node.MandatNode;
import fr.dila.solonmgpp.api.node.MinistereNode;
import fr.dila.solonmgpp.api.node.TableReferenceEppNode;
import fr.dila.solonmgpp.api.service.TableReferenceService;
import fr.dila.solonmgpp.core.dto.IdentiteDTOImpl;
import fr.dila.solonmgpp.core.dto.OrganismeDTOImpl;
import fr.dila.solonmgpp.core.node.ActeurNodeImpl;
import fr.dila.solonmgpp.core.node.GouvernementNodeImpl;
import fr.dila.solonmgpp.core.node.IdentiteNodeImpl;
import fr.dila.solonmgpp.core.node.MandatNodeImpl;
import fr.dila.solonmgpp.core.node.MinistereNodeImpl;
import fr.dila.solonmgpp.core.util.WSErrorHelper;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.rest.client.HttpTransactionException;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epp.Acteur;
import fr.sword.xsd.solon.epp.ActionObjetReference;
import fr.sword.xsd.solon.epp.ChercherMandatParNORRequest;
import fr.sword.xsd.solon.epp.ChercherMandatParNORResponse;
import fr.sword.xsd.solon.epp.ChercherTableDeReferenceRequest;
import fr.sword.xsd.solon.epp.ChercherTableDeReferenceResponse;
import fr.sword.xsd.solon.epp.Civilite;
import fr.sword.xsd.solon.epp.Gouvernement;
import fr.sword.xsd.solon.epp.Identite;
import fr.sword.xsd.solon.epp.MajTableRequest;
import fr.sword.xsd.solon.epp.MajTableResponse;
import fr.sword.xsd.solon.epp.Mandat;
import fr.sword.xsd.solon.epp.Ministere;
import fr.sword.xsd.solon.epp.ObjetContainer;
import fr.sword.xsd.solon.epp.ObjetType;
import fr.sword.xsd.solon.epp.Organisme;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Implementation de {@link TableReferenceService}
 * 
 * @author asatre
 * 
 */
public class TableReferenceServiceImpl implements TableReferenceService {

	private static final String				ORGANISME				= "Organisme";
	private static final String				IDENTITE				= "Identite";
	private static final String				MANDATS_SANS_MINISTERES	= "Mandats sans Ministeres";
	private static final String				WILDCARD				= "WILDCARD";
	private static final String				BLANK					= " ";
	/**
	 * Surcouche du socle transverse du logger log4J
	 */
	private static final STLogger			LOGGER					= STLogFactory
																			.getLog(TableReferenceServiceImpl.class);

	private final Map<String, IdentiteDTO>	mapCacheIdentite		= new HashMap<String, IdentiteDTO>();
	private final Map<String, OrganismeDTO>	mapCacheOrganisme		= new HashMap<String, OrganismeDTO>();

	private final Calendar					lastUpdateCache			= Calendar.getInstance();

	public TableReferenceServiceImpl() {
		// Default constructor
	}

	@Override
	public void loadAllTableReference(CoreSession session) throws ClientException {

		synchronized (this) {

			if (!mapCacheIdentite.isEmpty()) {
				// chargement deja effectué
				return;
			}

			majCacheIdentites(session, null);

			// chargement des Organismes
			majCacheOrganismes(session, null);
		}
	}

	@Override
	public void loadAllTableReferenceActifAndInactif(CoreSession session) throws ClientException {

		synchronized (this) {

			if (!mapCacheIdentite.isEmpty()) { // chargement deja effectué
				return;
			}
			// chargement des identités et mandats
			majCacheIdentitesActifAndInactif(session, null);

			// chargement des Organismes
			majCacheOrganismes(session, null);
		}
	}

	@Override
	public TableReferenceDTO findTableReferenceByIdAndType(String identifiant, String tableRef, Date date,
			CoreSession session) throws ClientException {
		return findTableReferenceByIdAndType(identifiant, tableRef, date, session, false);
	}

	@Override
	public TableReferenceDTO findTableReferenceByIdAndType(String identifiant, String tableRef, Date date,
			CoreSession session, boolean skipDate) throws ClientException {
		loadAllTableReference(session);

		TableReferenceDTO result = null;

		if (date == null) {
			date = Calendar.getInstance().getTime();
		}

		if (IDENTITE.equals(tableRef)) {
			IdentiteDTO identiteDTO = mapCacheIdentite.get(identifiant);
			if (identiteDTO == null) {
				miseAjour(session, identifiant);
				identiteDTO = mapCacheIdentite.get(identifiant);
			}
			if (identiteDTO != null
					&& (skipDate || ((identiteDTO.getDateDebut().compareTo(date) <= 0 && (identiteDTO.getDateFin() == null || identiteDTO
							.getDateFin().compareTo(date) >= 0))))) {
				result = identiteDTO;
			}
		} else if (ORGANISME.equals(tableRef)) {
			OrganismeDTO organismeDTO = mapCacheOrganisme.get(identifiant);
			if (organismeDTO == null) {
				// On tente un rechargement depuis le cache de l'organisme précisément
				ChercherTableDeReferenceRequest request = getRequest(ObjetType.ORGANISME, true,
						Collections.singletonList(identifiant), null);
				majCacheOrganismes(session, request);
				organismeDTO = mapCacheOrganisme.get(identifiant);
			}
			if (organismeDTO != null && organismeDTO.getDateDebut() != null
					&& organismeDTO.getDateDebut().compareTo(date) <= 0
					&& (organismeDTO.getDateFin() == null || organismeDTO.getDateFin().compareTo(date) >= 0)) {
				result = organismeDTO;
			}
		}

		return result;
	}

	@Override
	public List<TableReferenceDTO> searchTableReference(String search, String tableReference, CoreSession session)
			throws ClientException {
		loadAllTableReference(session);

		Set<TableReferenceDTO> result = new HashSet<TableReferenceDTO>();
		Date date = Calendar.getInstance().getTime();
		ArrayList<TableReferenceDTO> listReturn = new ArrayList<TableReferenceDTO>();

		if (IDENTITE.equals(tableReference)) {
			String[] pattern = search.split(BLANK);
			for (IdentiteDTO identiteDTO : mapCacheIdentite.values()) {
				if (identiteDTO.getDateDebut().compareTo(date) <= 0
						&& (identiteDTO.getDateFin() == null || identiteDTO.getDateFin().compareTo(date) >= 0)) {
					for (String key : pattern) {
						if (identiteDTO.getNom().toLowerCase().contains(key.toLowerCase())) {
							result.add(identiteDTO);
						} else if (identiteDTO.getPrenom().toLowerCase().contains(key.toLowerCase())) {
							result.add(identiteDTO);
						}
					}
				}
			}
		} else if (ORGANISME.equals(tableReference)) {
			String[] pattern = search.split(BLANK);
			for (OrganismeDTO organismeDTO : mapCacheOrganisme.values()) {
				if (organismeDTO.getDateDebut().compareTo(date) <= 0
						&& (organismeDTO.getDateFin() == null || organismeDTO.getDateFin().compareTo(date) >= 0)) {
					for (String key : pattern) {
						if (organismeDTO.getNom().toLowerCase().contains(key.toLowerCase())) {
							result.add(organismeDTO);
						}
					}
				}
			}
		}
		listReturn.addAll(result);
		return listReturn;
	}

	// Va rechercher les auteurs et leur mandat lors de la recherche par suggestion
	@Override
	public List<TableReferenceDTO> searchTableReferenceAuteurSuggestion(String search, String tableReference,
			CoreSession session, String proprietaire, String organismeType) throws ClientException {
		// Mise à jour de la table auteur/mandat qui est en cache - prend en compte les actifs et les inactifs
		loadAllTableReferenceActifAndInactif(session);
		Set<String> resultIdMandat = new HashSet<String>();
		Date date = Calendar.getInstance().getTime();
		final List<IdentiteDTO> identites = new ArrayList<IdentiteDTO>();
		ArrayList<TableReferenceDTO> listReturn = new ArrayList<TableReferenceDTO>();
		// Ici, on n'effectue la recherche que sur la table identité
		if (IDENTITE.equals(tableReference)) {
			String[] pattern = search.split(BLANK);
			for (IdentiteDTO identiteDTO : mapCacheIdentite.values()) {
				// On vérifie que l'auteur correspond bien à la recherche par suggestion
				if (StringUtils.isNotBlank(proprietaire) && proprietaire.equals(identiteDTO.getProprietaire())
						|| WILDCARD.equals(proprietaire)) {
					for (String key : pattern) {
						if (identiteDTO.getNom().toLowerCase().contains(key.toLowerCase())
								|| identiteDTO.getPrenom().toLowerCase().contains(key.toLowerCase())) {
							String title = identiteDTO.getTitle();
							DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
							String dateDebut = "";
							if (identiteDTO.getDateDebutMandat() != null
									&& identiteDTO.getDateDebutMandat().toString().trim() != "") {
								dateDebut = formatter.format(identiteDTO.getDateDebutMandat());
							}
							String dateFin = "";
							if (identiteDTO.getDateFinMandat() != null
									&& identiteDTO.getDateFinMandat().toString().trim() != "") {
								dateFin = formatter.format(identiteDTO.getDateFinMandat());
							}
							// Si on a une date de fin et que cette dernière est antérieure à la date actuelle, on
							// ajoute la date dans le titre
							if (StringUtil.isNotEmpty(dateFin) && date.compareTo(identiteDTO.getDateFinMandat()) > 0) {
								identiteDTO.setTitleModifie(title + " (" + dateDebut + " - " + dateFin + ")");
							} else {
								// Sinon on garde juste le titre normal (à savoir M/Mme Nom Prénom)
								identiteDTO.setTitleModifie(identiteDTO.getTitle());
							}
							// On vérifie l'unicité
							if (resultIdMandat.isEmpty() || resultIdMandat.contains(identiteDTO.getId()) == false) {
								// Si pas déjà présent dans la liste, on ajoute l'objet, et on renseigne son id dans le
								// hashset resultIdMandat pour des vérifications ultérieures
								resultIdMandat.add(identiteDTO.getId());
								identites.add(identiteDTO);
							}
						}
					}
				}
			}
		}
		// On trie par 'ordre alphabétique et par date de mandat
		Collections.sort(identites, IdentiteDTO.COMPARE);
		listReturn.addAll(identites);
		return listReturn;
	}

	@Override
	public List<TableReferenceDTO> searchTableReference(String search, String tableReference, CoreSession session,
			String proprietaire, String organismeType) throws ClientException {
		loadAllTableReference(session);

		Set<TableReferenceDTO> result = new HashSet<TableReferenceDTO>();
		Date date = Calendar.getInstance().getTime();
		ArrayList<TableReferenceDTO> listReturn = new ArrayList<TableReferenceDTO>();

		if (IDENTITE.equals(tableReference)) {
			String[] pattern = search.split(BLANK);
			for (IdentiteDTO identiteDTO : mapCacheIdentite.values()) {
				if (StringUtils.isNotBlank(proprietaire) && proprietaire.equals(identiteDTO.getProprietaire())
						|| WILDCARD.equals(proprietaire)) {
					if (identiteDTO.getDateDebut().compareTo(date) <= 0
							&& (identiteDTO.getDateFin() == null || identiteDTO.getDateFin().compareTo(date) >= 0)) {
						for (String key : pattern) {
							if (identiteDTO.getNom().toLowerCase().contains(key.toLowerCase())
									|| identiteDTO.getPrenom().toLowerCase().contains(key.toLowerCase())) {
								result.add(identiteDTO);
							}
						}
					}
				}
			}
		} else if (ORGANISME.equals(tableReference)) {
			String[] pattern = search.split(BLANK);

			if (!StringUtils.isBlank(organismeType)) {
				// le cas de groupe parlementaire
				String organismeTypeValue = "";
				// Pour Groupe Parlementaire
				if (organismeType.equals("GROUPE_PARLEMENTAIRE")) {
					organismeTypeValue = "GROUPE";
				}
				for (OrganismeDTO organismeDTO : mapCacheOrganisme.values()) {
					if ((organismeDTO.getDateDebut() != null && organismeDTO.getDateDebut().compareTo(date) <= 0)
							&& (organismeDTO.getDateFin() == null || organismeDTO.getDateFin().compareTo(date) >= 0)
							&& (organismeDTO.getType() != null && organismeDTO.getType().value()
									.startsWith(organismeTypeValue))) {
						for (String key : pattern) {
							if (organismeDTO.getNom().toLowerCase().contains(key.toLowerCase())) {
								result.add(organismeDTO);
							}
						}
					}
				}
			} else {
				// On garde le test d'avant
				for (OrganismeDTO organismeDTO : mapCacheOrganisme.values()) {
					if (StringUtils.isNotBlank(proprietaire) && proprietaire.equals(organismeDTO.getProprietaire())
							|| WILDCARD.equals(proprietaire)) {
						if (organismeDTO.getDateDebut().compareTo(date) <= 0
								&& (organismeDTO.getDateFin() == null || organismeDTO.getDateFin().compareTo(date) >= 0)) {
							for (String key : pattern) {
								if (organismeDTO.getNom().toLowerCase().contains(key.toLowerCase())) {
									result.add(organismeDTO);
								}
							}
						}
					}
				}
			}
		}
		listReturn.addAll(result);
		return listReturn;
	}

	@Override
	public TableReferenceDTO findTableReferenceByIdAndTypeWS(String identifiant, String tableReference, Date date,
			CoreSession session) throws ClientException {

		synchronized (this) {
			TableReferenceDTO tableReferenceDTO = findTableReferenceByIdAndType(identifiant, tableReference, date,
					session);
			if (tableReferenceDTO == null) {
				// force cache reload
				mapCacheIdentite.clear();
				loadAllTableReference(session);
			}

			return findTableReferenceByIdAndType(identifiant, tableReference, date, session);
		}
	}

	@Override
	public List<TableReferenceEppNode> getGouvernementList(CoreSession session) throws ClientException {
		List<TableReferenceEppNode> gouvernementList = new ArrayList<TableReferenceEppNode>();

		WSEpp wsEpp = getWSEpp(session);

		ObjetContainer objetContainer = getSearchTableReferenceResult(wsEpp, session, ObjetType.GOUVERNEMENT, null);

		for (Gouvernement gouvernement : objetContainer.getGouvernement()) {
			GouvernementNode gouvernementNode = new GouvernementNodeImpl();
			gouvernementNode.remapField(gouvernement);
			gouvernementList.add(gouvernementNode);
		}
		
		Collections.sort(gouvernementList, new Comparator<TableReferenceEppNode>() {
			@Override
			public int compare(TableReferenceEppNode node1, TableReferenceEppNode node2) {
				int res = 0;
				Date dateN1 = node1.getDateDebut();
				Date dateN2 = node2.getDateDebut();
				if (dateN1 != null) {
					res = dateN1.compareTo(dateN2);
					if (res == 0) {
						String label1 = node1.getLabel();
						res = label1.compareTo(node2.getLabel());
					}
				} else if (dateN2 != null) {
					res = -1;					
				}
				return res;
			}			
		});

		// TCH::mantis 0051440
		GouvernementNode gouvernementNode = new GouvernementNodeImpl();
		gouvernementNode.setIdentifiant(MANDATS_SANS_MINISTERES);
		gouvernementNode.setAppellation(MANDATS_SANS_MINISTERES);
		gouvernementNode.setHasChildren(Boolean.TRUE);
		gouvernementList.add(gouvernementNode);

		return gouvernementList;
	}

	@Override
	public List<TableReferenceEppNode> getChildrenList(TableReferenceEppNode parentNode, CoreSession session)
			throws ClientException {
		List<TableReferenceEppNode> result = new ArrayList<TableReferenceEppNode>();
		WSEpp wsEpp = getWSEpp(session);
		String parentId = parentNode.getIdentifiant();
		if (ObjetType.GOUVERNEMENT.value().equals(parentNode.getTypeValue())) {
			if (MANDATS_SANS_MINISTERES.equals(parentId)) {
				ObjetContainer objetContainer = getSearchTableReferenceResult(wsEpp, session, ObjetType.MANDAT,
						parentId);
				for (Mandat mandat : objetContainer.getMandat()) {
					result.add(getMandatNodeFromMandat(mandat));
				}
			} else {
				ObjetContainer objetContainer = getSearchTableReferenceResult(wsEpp, session, ObjetType.MINISTERE,
						parentId);
				for (Ministere ministere : objetContainer.getMinistere()) {
					MinistereNode ministereNode = new MinistereNodeImpl();
					ministereNode.remapField(ministere);
					result.add(ministereNode);
				}
			}
		} else if (ObjetType.MINISTERE.value().equals(parentNode.getTypeValue())) {
			ObjetContainer objetContainer = getSearchTableReferenceResult(wsEpp, session, ObjetType.MANDAT, parentId);
			for (Mandat mandat : objetContainer.getMandat()) {
				result.add(getMandatNodeFromMandat(mandat));
			}
		}
		return result;
	}

	@Override
	public void createGouvernement(GouvernementNode gouvernementNode, CoreSession session) throws ClientException {
		updateOrCreateGouvernement(gouvernementNode, session, ActionObjetReference.AJOUTER);
	}

	@Override
	public void updateGouvernement(GouvernementNode gouvernementNode, CoreSession session) throws ClientException {
		updateOrCreateGouvernement(gouvernementNode, session, ActionObjetReference.MODIFIER);
	}

	@Override
	public void createMinistere(MinistereNode ministereNode, CoreSession session) throws ClientException {
		updateOrCreateMinistere(ministereNode, session, ActionObjetReference.AJOUTER);
	}

	@Override
	public void updateMinistere(MinistereNode ministereNode, CoreSession session) throws ClientException {
		updateOrCreateMinistere(ministereNode, session, ActionObjetReference.MODIFIER);
	}

	@Override
	public void createMandat(MandatNode mandatNode, CoreSession session) throws ClientException {
		Acteur acteur = updateOrCreateActeur(session, ActionObjetReference.AJOUTER);
		ActeurNode acteurNode = new ActeurNodeImpl();
		acteurNode.remapField(acteur);
		IdentiteNode identiteNode = mandatNode.getIdentite();
		identiteNode.setActeur(acteurNode);
		Identite identite = updateOrCreateIdentite(identiteNode, session, ActionObjetReference.AJOUTER);
		MandatNode mandatNode2 = new MandatNodeImpl();
		identiteNode.remapField(identite);
		mandatNode2.setIdentite(identiteNode);
		updateOrCreateMandat(mandatNode, session, ActionObjetReference.AJOUTER);
	}

	@Override
	public void updateMandat(MandatNode mandatNode, CoreSession session) throws ClientException {
		updateOrCreateMandat(mandatNode, session, ActionObjetReference.MODIFIER);
	}

	@Override
	public void createIdentite(IdentiteNode identiteNode, CoreSession session) throws ClientException {
		updateOrCreateIdentite(identiteNode, session, ActionObjetReference.AJOUTER);
	}

	@Override
	public void createIdentite(MandatNode mandatNode, CoreSession session) throws ClientException {
		IdentiteNode identiteNode = mandatNode.getIdentite();
		Identite identite = updateOrCreateIdentite(identiteNode, session, ActionObjetReference.AJOUTER);
		identiteNode.remapField(identite);
		mandatNode.setIdentite(identiteNode);
		updateOrCreateMandat(mandatNode, session, ActionObjetReference.MODIFIER);
	}

	@Override
	public void updateIdentite(MandatNode mandatNode, CoreSession session) throws ClientException {
		IdentiteNode identiteNode = mandatNode.getIdentite();
		Identite identite = updateOrCreateIdentite(identiteNode, session, ActionObjetReference.MODIFIER);
		identiteNode.remapField(identite);
		mandatNode.setIdentite(identiteNode);
		updateOrCreateMandat(mandatNode, session, ActionObjetReference.MODIFIER);
	}

	@Override
	public void createActeur(MandatNode mandatNode, CoreSession session) throws ClientException {
		ActeurNode acteurNode = mandatNode.getIdentite().getActeur();
		Acteur acteur = updateOrCreateActeur(session, ActionObjetReference.AJOUTER);
		acteurNode.remapField(acteur);
		IdentiteNode identiteNode = mandatNode.getIdentite();
		identiteNode.setActeur(acteurNode);
		Identite identite = updateOrCreateIdentite(identiteNode, session, ActionObjetReference.MODIFIER);
		identiteNode.remapField(identite);
		mandatNode.setIdentite(identiteNode);
		updateOrCreateMandat(mandatNode, session, ActionObjetReference.MODIFIER);
	}

	@Override
	public void updateActeur(MandatNode mandatNode, CoreSession session) throws ClientException {
		ActeurNode acteurNode = mandatNode.getIdentite().getActeur();
		Acteur acteur = updateOrCreateActeur(session, ActionObjetReference.MODIFIER);
		acteurNode.remapField(acteur);
		IdentiteNode identiteNode = mandatNode.getIdentite();
		identiteNode.setActeur(acteurNode);
		Identite identite = updateOrCreateIdentite(identiteNode, session, ActionObjetReference.MODIFIER);
		identiteNode.remapField(identite);
		mandatNode.setIdentite(identiteNode);
		updateOrCreateMandat(mandatNode, session, ActionObjetReference.MODIFIER);
	}

	@Override
	public GouvernementNode getGouvernement(String gouvernementId, CoreSession session) throws ClientException {
		WSEpp wsEpp = getWSEpp(session);
		GouvernementNode result = null;
		ObjetContainer objetContainerGouvernement = getSearchTableReferenceById(wsEpp, session, ObjetType.GOUVERNEMENT,
				gouvernementId);

		List<Gouvernement> list = objetContainerGouvernement.getGouvernement();
		if (list != null && !list.isEmpty()) {
			result = new GouvernementNodeImpl();
			result.remapField(list.get(0));
		}

		return result;
	}

	@Override
	public MinistereNode getMinistere(String ministereId, CoreSession session) throws ClientException {
		WSEpp wsEpp = getWSEpp(session);
		MinistereNode result = null;
		ObjetContainer objetContainerMinistere = getSearchTableReferenceById(wsEpp, session, ObjetType.MINISTERE,
				ministereId);

		List<Ministere> list = objetContainerMinistere.getMinistere();
		if (list != null && !list.isEmpty()) {
			result = new MinistereNodeImpl();
			result.remapField(list.get(0));
		}
		return result;
	}

	@Override
	public MandatNode getMandat(String mandatId, CoreSession session) throws ClientException {
		// returns mandat + identite + acteur
		WSEpp wsEpp = getWSEpp(session);
		IdentiteNode identite = null;
		ActeurNode acteur = null;
		MandatNode mandat = null;
		ObjetContainer objetContainerMandat = getSearchTableReferenceById(wsEpp, session, ObjetType.MANDAT, mandatId);

		List<Mandat> list = objetContainerMandat.getMandat();
		if (list != null && !list.isEmpty()) {
			mandat = getMandatNodeFromMandat(list.get(0));
		}
		if (mandat != null) {
			ObjetContainer objetContainerIdentite = getSearchTableReferenceById(wsEpp, session, ObjetType.IDENTITE,
					mandat.getIdentite().getIdentifiant());

			List<Identite> listIdentite = objetContainerIdentite.getIdentite();
			if (listIdentite != null && !listIdentite.isEmpty()) {
				identite = new IdentiteNodeImpl();
				identite.remapField(listIdentite.get(0));
			}

			if (identite != null) {
				ObjetContainer objetContainerActeur = getSearchTableReferenceById(wsEpp, session, ObjetType.ACTEUR,
						identite.getActeur().getIdentifiant());

				List<Acteur> listActeur = objetContainerActeur.getActeur();
				if (listActeur != null && !listActeur.isEmpty()) {
					acteur = new ActeurNodeImpl();
					acteur.remapField(listActeur.get(0));
				}
				identite.setActeur(acteur);
			}

			mandat.setIdentite(identite);
		}
		return mandat;
	}

	@Override
	public List<Mandat> getMandatsByNor(String nor, CoreSession session) throws ClientException {
		WSEpp wsEpp = getWSEpp(session);
		List<Mandat> listeMandats = new ArrayList<Mandat>();
		final ChercherMandatParNORRequest chercherMandatParNORRequest = new ChercherMandatParNORRequest();
		chercherMandatParNORRequest.setNor(nor);
		ChercherMandatParNORResponse chercherMandatParNORResponse = null;
		try {
			chercherMandatParNORResponse = wsEpp.chercherMandatParNor(chercherMandatParNORRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new EPGException(SolonMgppWsLocator.getConnexionFailureMessage(session), e);
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_MANDAT_TEC, e);
			throw new EPGException(e);
		}
		if (chercherMandatParNORResponse != null) {
			listeMandats.addAll(chercherMandatParNORResponse.getObjetContainer().getMandat());
		}

		return listeMandats;
	}

	private ObjetContainer getSearchTableReferenceResult(WSEpp wsEpp, CoreSession session, ObjetType objetType,
			String parentId) throws ClientException {
		ChercherTableDeReferenceRequest chercherTDRRequest = getRequest(objetType, false, null, parentId);

		ChercherTableDeReferenceResponse chercherTDRResponse = chercherTDR(session, wsEpp, chercherTDRRequest);
		checkChercherTDRResponse(session, chercherTDRResponse);
		return chercherTDRResponse.getObjetContainer();
	}

	private ObjetContainer getSearchTableReferenceById(WSEpp wsEpp, CoreSession session, ObjetType objetType,
			String idObjet) throws ClientException {
		ChercherTableDeReferenceRequest chercherTDRRequest = getRequest(objetType, false,
				Collections.singletonList(idObjet), null);

		ChercherTableDeReferenceResponse chercherTDRResponse = chercherTDR(session, wsEpp, chercherTDRRequest);
		checkChercherTDRResponse(session, chercherTDRResponse);
		return chercherTDRResponse.getObjetContainer();
	}

	private void updateOrCreateGouvernement(GouvernementNode gouvernementNode, CoreSession session,
			ActionObjetReference actionObjetReference) throws ClientException {
		WSEpp wsEpp = getWSEpp(session);

		MajTableRequest majTableRequest = new MajTableRequest();
		majTableRequest.setAction(actionObjetReference);

		ObjetContainer objetContainer = new ObjetContainer();
		objetContainer.setType(ObjetType.GOUVERNEMENT);

		Gouvernement gouvernement = gouvernementNode.toGouvernementXsd();
		objetContainer.getGouvernement().add(gouvernement);
		majTableRequest.setObjetContainer(objetContainer);

		MajTableResponse majTableResponse = majTable(session, wsEpp, majTableRequest);

		checkMajTableResponse(session, majTableResponse);

		// Ajout de l'id récupéré
		if (majTableResponse.getObjetContainer() != null
				&& majTableResponse.getObjetContainer().getGouvernement() != null) {
			if (!majTableResponse.getObjetContainer().getGouvernement().isEmpty()
					&& majTableResponse.getObjetContainer().getGouvernement().get(0) != null) {
				gouvernementNode.setIdentifiant(majTableResponse.getObjetContainer().getGouvernement().get(0).getId());
			}
		}
	}

	private void updateOrCreateMinistere(MinistereNode ministereNode, CoreSession session,
			ActionObjetReference actionObjetReference) throws ClientException {
		WSEpp wsEpp = getWSEpp(session);

		MajTableRequest majTableRequest = new MajTableRequest();
		majTableRequest.setAction(actionObjetReference);

		ObjetContainer objetContainer = new ObjetContainer();
		objetContainer.setType(ObjetType.MINISTERE);

		Ministere ministere = ministereNode.toMinistereXsd();
		objetContainer.getMinistere().add(ministere);
		majTableRequest.setObjetContainer(objetContainer);

		MajTableResponse majTableResponse = majTable(session, wsEpp, majTableRequest);

		checkMajTableResponse(session, majTableResponse);

		// Ajout de l'id récupéré
		if (majTableResponse.getObjetContainer() != null && majTableResponse.getObjetContainer().getMinistere() != null) {
			if (!majTableResponse.getObjetContainer().getMinistere().isEmpty()
					&& majTableResponse.getObjetContainer().getMinistere().get(0) != null) {
				ministereNode.setIdentifiant(majTableResponse.getObjetContainer().getMinistere().get(0).getId());
			}
		}
	}

	private void updateOrCreateMandat(MandatNode mandatNode, CoreSession session,
			ActionObjetReference actionObjetReference) throws ClientException {
		WSEpp wsEpp = getWSEpp(session);

		MajTableRequest majTableRequest = new MajTableRequest();
		majTableRequest.setAction(actionObjetReference);
		ObjetContainer objetContainer = new ObjetContainer();
		objetContainer.setType(ObjetType.MANDAT);

		Mandat mandat = mandatNode.toMandatXsd();
		objetContainer.getMandat().add(mandat);
		majTableRequest.setObjetContainer(objetContainer);
		MajTableResponse majTableResponse = majTable(session, wsEpp, majTableRequest);
		checkMajTableResponse(session, majTableResponse);
	}

	private Identite updateOrCreateIdentite(IdentiteNode identiteNode, CoreSession session,
			ActionObjetReference actionObjetReference) throws ClientException {
		WSEpp wsEpp = getWSEpp(session);

		MajTableRequest majTableRequest = new MajTableRequest();
		majTableRequest.setAction(actionObjetReference);

		ObjetContainer objetContainer = new ObjetContainer();
		objetContainer.setType(ObjetType.IDENTITE);

		Identite identite = identiteNode.toIdentiteXsd();
		objetContainer.getIdentite().add(identite);
		majTableRequest.setObjetContainer(objetContainer);

		MajTableResponse majTableResponse = majTable(session, wsEpp, majTableRequest);
		checkMajTableResponse(session, majTableResponse);

		return majTableResponse.getObjetContainer().getIdentite().get(0);
	}

	private Acteur updateOrCreateActeur(CoreSession session, ActionObjetReference actionObjetReference)
			throws ClientException {
		WSEpp wsEpp = getWSEpp(session);

		MajTableRequest majTableRequest = new MajTableRequest();
		majTableRequest.setAction(actionObjetReference);

		ObjetContainer objetContainer = new ObjetContainer();
		objetContainer.setType(ObjetType.ACTEUR);

		Acteur acteur = new Acteur();
		objetContainer.getActeur().add(acteur);
		majTableRequest.setObjetContainer(objetContainer);

		MajTableResponse majTableResponse = majTable(session, wsEpp, majTableRequest);
		checkMajTableResponse(session, majTableResponse);

		return majTableResponse.getObjetContainer().getActeur().get(0);
	}

	/**
	 * 
	 * @param session
	 * @param identifiant
	 * @return
	 * @throws ClientException
	 */
	private void miseAjour(CoreSession session, String identifiant) throws ClientException {
		synchronized (this) {
			majCacheIdentites(session, identifiant);
		}
	}

	@Override
	public IdentiteNode getIdentiteNode(CoreSession session, String prenomNom) throws ClientException {
		if (prenomNom == null) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_TABLE_REFERENCE_TEC,
					"Paramètre prénom & nom vide pour la recherche d'identité");
			return null;
		}
		WSEpp wsEpp = getWSEpp(session);
		// chargement des identités
		ChercherTableDeReferenceResponse chercherTDRResponse = chercherTDR(session, wsEpp, getRequestIdentitesActives());
		checkChercherTDRResponse(session, chercherTDRResponse);

		List<Identite> listIdentite = chercherTDRResponse.getObjetContainer().getIdentite();
		if (!listIdentite.isEmpty()) {
			for (Identite identite : listIdentite) {
				IdentiteNode node = new IdentiteNodeImpl();
				node.remapField(identite);
				if (prenomNom.equalsIgnoreCase(node.getPrenom() + BLANK + node.getNom())) {
					return node;
				}
			}
		}
		return null;
	}

	@Override
	public MinistereNode getMinistereNode(CoreSession session, String nom, String libelle, String appellation,
			String edition) throws ClientException {
		if (nom == null || libelle == null || edition == null || appellation == null) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_TABLE_REFERENCE_TEC,
					"Paramètre vide pour la recherche de ministères");
			return null;
		}
		WSEpp wsEpp = getWSEpp(session);

		// chargement des ministeres
		ChercherTableDeReferenceResponse chercherTDRResponse = chercherTDR(session, wsEpp, getRequestMinisteresActifs());
		checkChercherTDRResponse(session, chercherTDRResponse);

		List<Ministere> listMinistere = chercherTDRResponse.getObjetContainer().getMinistere();
		if (!listMinistere.isEmpty()) {
			for (Ministere ministere : listMinistere) {
				MinistereNode node = new MinistereNodeImpl();
				node.remapField(ministere);
				if (nom.equalsIgnoreCase(node.getNom()) && libelle.equalsIgnoreCase(node.getLibelle())
						&& edition.equalsIgnoreCase(node.getEdition())
						&& appellation.equalsIgnoreCase(node.getAppellation())) {
					return node;
				}
			}
		}
		return null;
	}

	@Override
	public void updateCaches(CoreSession session) throws ClientException {
		lastUpdateCache.setTime(Calendar.getInstance().getTime());
		majCacheIdentites(session, null);
		majCacheOrganismes(session, null);
	}

	@Override
	public Calendar getLastUpdateCache() {
		return (Calendar) lastUpdateCache.clone();
	}

	/* ******************************************************************************************
	 * METHODES UTILITAIRES *******************************************************************************************
	 */

	private void majCacheIdentites(CoreSession session, String identifiant) throws ClientException {
		WSEpp wsEpp = getWSEpp(session);
		// chargement des mandats
		Map<String, Mandat> mapIdIdentiteMandat = getMapIdIdentiteMandat(session, wsEpp, identifiant);

		// chargement des Identites à partir des mandats
		ChercherTableDeReferenceRequest requestIdentites = getRequestIdentitesActives();
		requestIdentites.getIdObjet().addAll(mapIdIdentiteMandat.keySet());

		ChercherTableDeReferenceResponse chercherTDRResponse = chercherTDR(session, wsEpp, requestIdentites);
		checkChercherTDRResponse(session, chercherTDRResponse);

		// mapping des IDENTITES
		for (Identite identite : chercherTDRResponse.getObjetContainer().getIdentite()) {
			IdentiteDTO identiteDTO = mappingIdentites(identite, mapIdIdentiteMandat);
			mapCacheIdentite.put(identiteDTO.getId(), identiteDTO);
		}
	}

	// Chargement des identités et mandats en prenant en compte les actifs et les inactifs
	private void majCacheIdentitesActifAndInactif(CoreSession session, String identifiant) throws ClientException {
		WSEpp wsEpp = getWSEpp(session);
		// chargement des mandats
		Map<String, Mandat> mapIdIdentiteMandat = getMapIdIdentiteMandatActifAndInactifs(session, wsEpp, identifiant);

		// chargement des Identites à partir des mandats
		ChercherTableDeReferenceRequest requestIdentites = getRequestAllIdentites();
		requestIdentites.getIdObjet().addAll(mapIdIdentiteMandat.keySet());

		ChercherTableDeReferenceResponse chercherTDRResponse = chercherTDR(session, wsEpp, requestIdentites);
		checkChercherTDRResponse(session, chercherTDRResponse);

		// mapping des IDENTITES
		for (Identite identite : chercherTDRResponse.getObjetContainer().getIdentite()) {
			IdentiteDTO identiteDTO = mappingIdentites(identite, mapIdIdentiteMandat);
			mapCacheIdentite.put(identiteDTO.getId(), identiteDTO);
		}
	}

	/**
	 * Met à jour le cache des organismes. Par défaut, on prend tous les organismes actifs, mais il est possible de
	 * spécifier une requête précise.
	 * 
	 * @param session
	 * @param tabRefRequest
	 *            (facultatif) : requête pour particulariser la mise à jour (par exemple, rechargement d'un unique
	 *            organisme)
	 * @throws ClientException
	 */
	private void majCacheOrganismes(CoreSession session, ChercherTableDeReferenceRequest tabRefRequest)
			throws ClientException {
		ChercherTableDeReferenceRequest request = tabRefRequest != null ? tabRefRequest : getRequestOrganismesActifs();
		WSEpp wsEpp = getWSEpp(session);
		ChercherTableDeReferenceResponse chercherTDRResponse = chercherTDR(session, wsEpp, request);
		checkChercherTDRResponse(session, chercherTDRResponse);

		// mapping des ORGANISMES
		for (Organisme organisme : chercherTDRResponse.getObjetContainer().getOrganisme()) {
			OrganismeDTO organismeDTO = new OrganismeDTOImpl();
			organismeDTO.setId(organisme.getId());
			organismeDTO.setNom(organisme.getNom());
			organismeDTO.setDateDebut(DateUtil.xmlGregorianCalendarToDate(organisme.getDateDebut()));
			organismeDTO.setDateFin(DateUtil.xmlGregorianCalendarToDate(organisme.getDateFin()));
			organismeDTO.setType(organisme.getType());
			organismeDTO.setProprietaire(organisme.getProprietaire() == null ? null : organisme.getProprietaire()
					.value());
			mapCacheOrganisme.put(organisme.getId(), organismeDTO);
		}
	}

	/**
	 * récupère l'instance du ws epp
	 * 
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	private WSEpp getWSEpp(final CoreSession session) throws ClientException {
		WSEpp wsEpp = null;
		try {
			wsEpp = SolonMgppWsLocator.getWSEpp(session);
		} catch (WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new EPGException("Impossible de se connecter au Webservice EPP", e);
		}
		return wsEpp;
	}

	/**
	 * Effectue un appel à chercherTableDeReference
	 * 
	 * @param session
	 * @param wsEpp
	 * @param request
	 * @return
	 * @throws ClientException
	 */
	private ChercherTableDeReferenceResponse chercherTDR(final CoreSession session, final WSEpp wsEpp,
			final ChercherTableDeReferenceRequest request) throws ClientException {
		ChercherTableDeReferenceResponse response = null;
		try {
			response = wsEpp.chercherTableDeReference(request);
		} catch (HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new EPGException(SolonMgppWsLocator.getConnexionFailureMessage(session), e);
		} catch (Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_TABLE_REFERENCE_TEC, e);
			throw new EPGException(e);
		}

		return response;
	}

	/**
	 * Effectue un appel à MajTable
	 * 
	 * @param session
	 * @param wsEpp
	 * @param request
	 * @return
	 * @throws ClientException
	 */
	private MajTableResponse majTable(final CoreSession session, final WSEpp wsEpp, final MajTableRequest request)
			throws ClientException {
		MajTableResponse response = null;
		try {
			response = wsEpp.majTable(request);
		} catch (HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new EPGException(SolonMgppWsLocator.getConnexionFailureMessage(session), e);
		} catch (Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_TABLE_REFERENCE_TEC, e);
			throw new EPGException(e);
		}
		return response;
	}

	/**
	 * Forme une request avec :
	 * 
	 * @param objetType
	 *            balise typeObjet obligatoire
	 * @param actifUniquement
	 *            balise actifUniquement obligatoire
	 * @param idsObjet
	 *            balise IdObjet, liste d'id - peut être null
	 * @param parentId
	 *            balise parent - peut être null
	 * @return
	 */
	private ChercherTableDeReferenceRequest getRequest(final ObjetType objetType, final boolean actifUniquement,
			final List<String> idsObjet, final String parentId) {
		ChercherTableDeReferenceRequest request = new ChercherTableDeReferenceRequest();
		request.setActifsUniquement(actifUniquement);
		request.setTypeObjet(objetType);

		if (idsObjet != null && !idsObjet.isEmpty()) {
			request.getIdObjet().addAll(idsObjet);
		}

		if (StringUtil.isNotBlank(parentId)) {
			request.setParentId(parentId);
		}

		return request;
	}

	/**
	 * Forme une request avec : actifsUniquement = true typeObjet = identite
	 * 
	 * @return
	 */
	private ChercherTableDeReferenceRequest getRequestIdentitesActives() {
		return getRequest(ObjetType.IDENTITE, true, null, null);
	}

	/**
	 * Forme une request avec : actifsUniquement = false typeObjet = identite
	 * 
	 * @return
	 */
	private ChercherTableDeReferenceRequest getRequestAllIdentites() {
		return getRequest(ObjetType.IDENTITE, false, null, null);
	}

	/**
	 * Forme une request avec : actifsUniquement = true typeObjet = ministere
	 * 
	 * @return
	 */
	private ChercherTableDeReferenceRequest getRequestMinisteresActifs() {
		return getRequest(ObjetType.MINISTERE, true, null, null);
	}

	/**
	 * Forme une request avec : actifsUniquement = true typeObjet = organisme
	 * 
	 * @return
	 */
	private ChercherTableDeReferenceRequest getRequestOrganismesActifs() {
		return getRequest(ObjetType.ORGANISME, true, null, null);
	}

	/**
	 * Vérifie le statut de la réponse de chercherTableDeReference ClientException si response == null ou si statut !=
	 * OK
	 * 
	 * @param session
	 * @param response
	 * @throws ClientException
	 */
	private void checkChercherTDRResponse(final CoreSession session, final ChercherTableDeReferenceResponse response)
			throws ClientException {
		String erreurRecherche = "Erreur de communication avec SOLON EPP, la récupération des données a échoué.";
		if (response == null) {
			throw new EPGException(erreurRecherche);
		} else if (response.getStatut() == null || !TraitementStatut.OK.equals(response.getStatut())) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_TABLE_REFERENCE_TEC, response.getMessageErreur());
			throw new EPGException(erreurRecherche + response.getMessageErreur());
		}
	}

	/**
	 * Vérifie le statut de la réponse de MajTable ClientException si response == null ou si statut != OK
	 * 
	 * @param session
	 * @param response
	 * @throws ClientException
	 */
	private void checkMajTableResponse(final CoreSession session, final MajTableResponse response)
			throws ClientException {
		String erreurMaj = "Erreur de communication avec SOLON EPP, la mise à jour d'une table de référence a échoué.";
		if (response == null) {
			throw new EPGException(erreurMaj);
		} else if (response.getStatut() == null || !TraitementStatut.OK.equals(response.getStatut())) {
			LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_TABLE_REFERENCE_TEC, response.getMessageErreur());
			throw new EPGException(erreurMaj + WSErrorHelper.buildCleanMessage(response.getMessageErreur()));
		}
	}

	/**
	 * Mapping des données de l'identité
	 * 
	 * @param identite
	 * @param mapIdIdentiteMandat
	 * @return
	 */
	private IdentiteDTO mappingIdentites(final Identite identite, final Map<String, Mandat> mapIdIdentiteMandat) {
		Mandat mandat = mapIdIdentiteMandat.get(identite.getId());
		IdentiteDTO identiteDTO = new IdentiteDTOImpl();
		if (mandat != null) {
			identiteDTO.setId(mandat.getId());
			identiteDTO.setProprietaire(mandat.getProprietaire() == null ? null : mandat.getProprietaire().value());
			identiteDTO.setDateDebutMandat(DateUtil.xmlGregorianCalendarToDate(mandat.getDateDebut()));
			identiteDTO.setDateFinMandat(DateUtil.xmlGregorianCalendarToDate(mandat.getDateFin()));
		}

		identiteDTO.setDateDebut(DateUtil.xmlGregorianCalendarToDate(identite.getDateDebut()));
		identiteDTO.setDateFin(DateUtil.xmlGregorianCalendarToDate(identite.getDateFin()));
		identiteDTO.setNom(identite.getNom());
		identiteDTO.setPrenom(identite.getPrenom());
		Civilite civilite = identite.getCivilite();
		identiteDTO.setCivilite(civilite == null ? Civilite.NON_RENSEIGNE.value() : civilite.value());

		return identiteDTO;
	}

	/**
	 * récupère une map idIdentite/Mandat
	 * 
	 * @param session
	 * @param wsEpp
	 * @param identifiant
	 *            identifiant mandat
	 * @return
	 * @throws ClientException
	 */
	private Map<String, Mandat> getMapIdIdentiteMandat(final CoreSession session, final WSEpp wsEpp,
			final String identifiant) throws ClientException {
		// chargement des mandats
		ChercherTableDeReferenceRequest chercherTDRRequest = getRequest(ObjetType.MANDAT, true,
				Collections.singletonList(identifiant), null);

		ChercherTableDeReferenceResponse chercherTDRResponse = chercherTDR(session, wsEpp, chercherTDRRequest);
		checkChercherTDRResponse(session, chercherTDRResponse);

		Map<String, Mandat> mapIdIdentiteMandat = new HashMap<String, Mandat>();

		for (Mandat mandat : chercherTDRResponse.getObjetContainer().getMandat()) {
			mapIdIdentiteMandat.put(mandat.getIdIdentite(), mandat);
		}
		return mapIdIdentiteMandat;
	}

	/**
	 * récupère une map idIdentite/Mandat en tenant compte des mandats inactifs
	 * 
	 * @param session
	 * @param wsEpp
	 * @param identifiant
	 *            identifiant mandat
	 * @return
	 * @throws ClientException
	 */
	private Map<String, Mandat> getMapIdIdentiteMandatActifAndInactifs(final CoreSession session, final WSEpp wsEpp,
			final String identifiant) throws ClientException {
		// chargement des mandats
		ChercherTableDeReferenceRequest chercherTDRRequest = getRequest(ObjetType.MANDAT, false,
				Collections.singletonList(identifiant), null);

		ChercherTableDeReferenceResponse chercherTDRResponse = chercherTDR(session, wsEpp, chercherTDRRequest);
		checkChercherTDRResponse(session, chercherTDRResponse);

		Map<String, Mandat> mapIdIdentiteMandat = new HashMap<String, Mandat>();

		for (Mandat mandat : chercherTDRResponse.getObjetContainer().getMandat()) {
			mapIdIdentiteMandat.put(mandat.getIdIdentite(), mandat);
		}
		return mapIdIdentiteMandat;
	}

	/**
	 * créé un MandatNode, et remapField avec le mandat en paramètre
	 * 
	 * @param mandat
	 * @return
	 */
	private MandatNode getMandatNodeFromMandat(final Mandat mandat) {
		MandatNodeImpl mandatNode = new MandatNodeImpl();
		mandatNode.remapField(mandat);
		return mandatNode;
	}

	@Override
	// TODO renommer ou bien inclure les autres types d'organismes
	public List<OrganismeDTO> getAllOrganismeByType(String Type, CoreSession session) {
		List<OrganismeDTO> OrganismesList = new ArrayList<OrganismeDTO>();
		for (OrganismeDTO myOrganisme : mapCacheOrganisme.values()) {
			if ("OEP".equals(myOrganisme.getType())) {
				OrganismesList.add(myOrganisme);
			}
		}

		return OrganismesList;
	}
}