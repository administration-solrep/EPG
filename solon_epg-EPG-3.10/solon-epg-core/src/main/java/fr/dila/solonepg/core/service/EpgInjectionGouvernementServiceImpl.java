package fr.dila.solonepg.core.service;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.client.InjectionEpgGvtDTO;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.service.EpgInjectionGouvernementService;
import fr.dila.solonepg.api.service.PdfGeneratorService;
import fr.dila.solonepg.core.client.InjectionEpgGvtDTOImpl;
import fr.dila.solonepg.core.util.ExcelUtil;
import fr.dila.ss.api.client.InjectionGvtDTO;
import fr.dila.ss.core.client.InjectionGvtDTOImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.GouvernementNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.core.organigramme.EntiteNodeImpl;
import fr.dila.st.core.organigramme.ProtocolarOrderComparator;
import fr.dila.st.core.service.STServiceLocator;

/**
 * 
 * @author jbrunet
 * 
 */
public class EpgInjectionGouvernementServiceImpl implements EpgInjectionGouvernementService {

	private static final long		serialVersionUID		= 1L;

	protected static final String	MINISTERE_AJOUT			= "Nouveau";
	protected static final String	MINISTERE_CHANGE		= "Modification";
	protected static final String	MINISTERE_CONSERVE		= "Inchangé";
	protected static final String	MINISTERE_INDETERMINE	= "Indéterminé";

	/**
	 * Récupère la ligne correspondant au gouvernement (1ère ligne)
	 * 
	 * @param listInjection
	 * @return
	 */
	private InjectionGvtDTO getNewGovernment(List<InjectionGvtDTO> listInjection) {
		return listInjection.get(0);
	}

	/**
	 * Récupère les entités dont à modifier = 0 et à créer = 1
	 * 
	 * @param listInjection
	 * @return
	 */
	private List<InjectionGvtDTO> getAllNewEntities(List<InjectionGvtDTO> listInjection) {
		List<InjectionGvtDTO> formattedList = new ArrayList<InjectionGvtDTO>();
		for (InjectionGvtDTO entity : listInjection) {
			if (!entity.isGvt() && entity.isaCreerSolon() && !entity.isaModifierSolon()) {
				formattedList.add(entity);
			}
		}
		return formattedList;
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
	 * Récupère les entités dont à modifier = 1 et à créer = 0
	 * 
	 * @param listInjection
	 * @return
	 */
	private List<InjectionGvtDTO> getAllToModifyEntities(List<InjectionGvtDTO> listInjection) {
		List<InjectionGvtDTO> formattedList = new ArrayList<InjectionGvtDTO>();
		for (InjectionGvtDTO entity : listInjection) {
			if (!entity.isGvt() && !entity.isaCreerSolon() && entity.isaModifierSolon()) {
				formattedList.add(entity);
			}
		}
		return formattedList;
	}

	/**
	 * Modifie le gouvernement pour prendre en compte le changement de libellé/date de début
	 * 
	 * @param session
	 * @param government
	 * @throws ClientException
	 */
	private void modifyGovernment(CoreSession session, InjectionGvtDTO government) throws ClientException {

		GouvernementNode on = STServiceLocator.getSTGouvernementService().getCurrentGouvernement();
		Calendar cal = Calendar.getInstance();
		cal.setTime(government.getDateDeDebut());
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		on.setDateDebut(cal.getTime());
		on.setLabel(government.getLibelleLong());

		STServiceLocator.getSTGouvernementService().updateGouvernement(on);
		// Récupération de l'id du gouvernement
		government.setId(on.getId());

	}

	/**
	 * Modifie les entités
	 * 
	 * @param session
	 * @param newEntities
	 * @param government
	 * @throws ClientException
	 */
	private void modifyEntities(CoreSession session, List<InjectionGvtDTO> listEntities, InjectionGvtDTO government)
			throws ClientException {
		Calendar cal = Calendar.getInstance();
		for (InjectionGvtDTO entity : listEntities) {
			EntiteNodeImpl epgNode = ((EntiteNodeImpl) getFromNor(entity.getNor()));
			epgNode.setOrdre(Long.parseLong(entity.getOrdreProtocolaireSolon()));
			epgNode.setEdition(entity.getLibelleCourt());
			epgNode.setLabel(entity.getLibelleLong());
			epgNode.setFormule(entity.getFormule());
			// Gestion de la date
			cal.setTime(entity.getDateDeDebut());
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			epgNode.setDateDebut(cal.getTime());
			// Supplément
			epgNode.setMembreGouvernementCivilite(entity.getCivilite());
			epgNode.setMembreGouvernementPrenom(entity.getPrenom());
			epgNode.setMembreGouvernementNom(entity.getNom());

			STServiceLocator.getSTMinisteresService().updateEntite(epgNode);
		}
	}

	/**
	 * Au sein du gouvernement, crée l'entité correspondante
	 * 
	 * @param session
	 * @param newEntities
	 * @param government
	 * @throws ClientException
	 */
	private void addNewEntities(CoreSession session, List<InjectionGvtDTO> newEntities, InjectionGvtDTO government)
			throws ClientException {
		EntiteNode epgNode = null;
		Calendar cal = Calendar.getInstance();

		OrganigrammeNode gouvernementNode = STServiceLocator.getSTGouvernementService().getCurrentGouvernement();
		for (InjectionGvtDTO newEntity : newEntities) {
			// Création du ministère
			epgNode = STServiceLocator.getSTMinisteresService().getBareEntiteModel();

			// Ajout du parent
			List<OrganigrammeNode> parentList = new ArrayList<OrganigrammeNode>();
			parentList.add(gouvernementNode);
			epgNode.setParentList(parentList);
			// Création
			cal.setTime(newEntity.getDateDeDebut());
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			epgNode.setDateDebut(cal.getTime());
			epgNode.setNorMinistere(newEntity.getNor());
			epgNode.setLabel(newEntity.getLibelleLong());
			epgNode.setFormule(newEntity.getFormule());
			epgNode.setEdition(newEntity.getLibelleCourt());
			epgNode.setMembreGouvernementCivilite(newEntity.getCivilite());
			epgNode.setMembreGouvernementPrenom(newEntity.getPrenom());
			epgNode.setMembreGouvernementNom(newEntity.getNom());
			epgNode.setOrdre(Long.parseLong(newEntity.getOrdreProtocolaireSolon()));
			STServiceLocator.getSTMinisteresService().createEntite(epgNode);
			newEntity.setId(epgNode.getId());
		}

	}

	@Override
	public List<InjectionGvtDTO> prepareInjection(CoreSession session, File file) throws ClientException {
		List<InjectionGvtDTO> listInjectionResult = ExcelUtil.prepareImportGvt(session, file);
		verifierDTO(listInjectionResult);
		return listInjectionResult;
	}

	@Override
	public void executeInjection(CoreSession session, List<InjectionGvtDTO> listInjection) throws ClientException {
		try {
			// Modification du gouvernement
			modifyGovernment(session, getNewGovernment(listInjection));
			// SOLON à modifier = 1
			modifyEntities(session, getAllToModifyEntities(listInjection), getNewGovernment(listInjection));
			// SOLON à créer = 1
			addNewEntities(session, getAllNewEntities(listInjection), getNewGovernment(listInjection));
		} catch (ClientException e) {
			throw new EPGException("Echec lors de l'injection du gouvernement", e);
		}
	}

	/**
	 * Permet de vérifier la présence des données obligatoires dans le DTO
	 * 
	 * @param listInjection
	 * @throws ClientException
	 */
	private void verifierDTO(List<InjectionGvtDTO> listInjection) throws ClientException {
		// Vérification du Gouvernement
		InjectionGvtDTO injGouv = getNewGovernment(listInjection);
		if (injGouv.getLibelleLong() == null || injGouv.getDateDeDebut() == null) {
			throw new EPGException("Données manquantes pour le gouvernement");
		}
		int ligne = 2; // position du premier ministère dans le fichier
		// Vérification des entités
		for (InjectionGvtDTO injMin : getAllEntities(listInjection)) {
			String entiteName = (injMin.getLibelleLong() == null ? (injMin.getLibelleCourt() == null ? "Ligne " + ligne
					: injMin.getLibelleCourt()) : injMin.getLibelleLong());
			if (injMin.getNor() == null) {
				throw new EPGException("NOR manquant pour l'entité : " + entiteName);
			} else if (injMin.getFormule() == null) {
				throw new EPGException("Formule entête manquante pour l'entité : " + entiteName);
			} else if (injMin.getOrdreProtocolaireSolon() == null) {
				throw new EPGException("Ordre protocolaire Solon manquant pour l'entité : " + entiteName);
			} else if (injMin.getDateDeDebut() == null) {
				throw new EPGException("Date de début manquante pour l'entité : " + entiteName);
			} else if (injMin.getLibelleCourt() == null) {
				throw new EPGException("Libellé Court manquant pour l'entité : " + entiteName);
			} else if (injMin.getLibelleLong() == null) {
				throw new EPGException("Libellé Long manquant pour l'entité : " + entiteName);
			} else if (injMin.getPrenomNom() == null) {
				throw new EPGException("Champ Prenom & Nom manquant pour l'entité : " + entiteName);
			} else if ((injMin.isaCreerReponses() || injMin.isaModifierSolon() || injMin.isaCreerReponses())
					&& injMin.getOrdreProtocolaireReponses() == null) {
				throw new EPGException(
						"Ordre protocolaire Reponses manquant (nécéssaire pour SOLON EPP) pour l'entité : "
								+ entiteName);
			}
			ligne++;
		}
	}

	@Override
	public List<InjectionEpgGvtDTO> createComparedDTO(CoreSession documentManager, List<InjectionGvtDTO> listInjection)
			throws ClientException {
		List<InjectionEpgGvtDTO> resultList = new ArrayList<InjectionEpgGvtDTO>();
		List<String> listDoneEntities = new ArrayList<String>();

		Map<String, InjectionGvtDTO> mapMinObtenus = createFromInjectionDto(documentManager, listInjection);
		OrganigrammeNode currentGvt = STServiceLocator.getSTGouvernementService().getCurrentGouvernement();
		verifierDTO(listInjection);

		for (InjectionGvtDTO newInject : listInjection) {
			// Cas du Gouvernement
			if (newInject.isGvt()) {
				resultList.add(new InjectionEpgGvtDTOImpl(MINISTERE_CHANGE, new InjectionGvtDTOImpl(null, false, null,
						currentGvt.getLabel(), null, null, null, null, null, currentGvt.getDateDebut(), currentGvt
								.getDateFin(), true), newInject));
			} else {
				InjectionGvtDTO minObtenu = mapMinObtenus.get(newInject.getNor());
				// Si modifier = 0 && creer = 0 : inchangé
				if (!newInject.isaCreerSolon() && !newInject.isaModifierSolon()) {
					resultList.add(new InjectionEpgGvtDTOImpl(MINISTERE_CONSERVE, minObtenu, null));
					listDoneEntities.add(minObtenu.getId());
				} else if (!newInject.isaCreerSolon() && newInject.isaModifierSolon()) {
					// modifier = 1 && creer = 0 : modification
					resultList.add(new InjectionEpgGvtDTOImpl(MINISTERE_CHANGE, minObtenu, newInject));
				} else if (newInject.isaCreerSolon() && !newInject.isaModifierSolon()) {
					// modifier = 0 && creer = 1 : ajout
					resultList.add(new InjectionEpgGvtDTOImpl(MINISTERE_AJOUT, null, newInject));
				} else {
					// cas impossible : a créer ET à modifier
					throw new EPGException("L'entité de NOR : " + newInject.getNor() + " est à modifier et à créer");
				}
			}
		}
		return resultList;

	}

	private OrganigrammeNode getFromNor(String nor) throws ClientException {
		if (nor == null) {
			throw new ClientException("Récupération impossible de l'entité : NOR inexistant");
		}
		List<EntiteNode> currentMinisteres = STServiceLocator.getSTMinisteresService().getCurrentMinisteres();
		Collections.sort(currentMinisteres, new ProtocolarOrderComparator());
		for (EntiteNode nodeMin : currentMinisteres) {

			if (nor.equals(nodeMin.getNorMinistere())) {
				return nodeMin;
			}

		}
		return null;
	}

	public InjectionGvtDTO createFromNor(String nor) throws ClientException {
		OrganigrammeNode minByNor = getFromNor(nor);
		if (minByNor != null) {
			EntiteNodeImpl epgNode = ((EntiteNodeImpl) minByNor);

			return new InjectionGvtDTOImpl(epgNode.getNorMinistere(), String.valueOf(epgNode.getOrdre()), false, false,
					epgNode.getEdition(), epgNode.getLabel(), epgNode.getFormule(),
					epgNode.getMembreGouvernementCivilite(), epgNode.getMembreGouvernementPrenom(),
					epgNode.getMembreGouvernementPrenom(), epgNode.getMembreGouvernement(), epgNode.getDateDebut(),
					epgNode.getDateFin(), null, false, false);

		} else {
			throw new EPGException("Récupération impossible de l'entité correspondant au NOR : " + nor);
		}
	}

	@Override
	public void generatePdf(CoreSession documentManager, OutputStream outputStream,
			List<InjectionEpgGvtDTO> listCompared) throws Exception {
		PdfGeneratorService pdfGeneratorService = SolonEpgServiceLocator.getPdfGeneratorService();
		pdfGeneratorService.generateInjection(outputStream, listCompared);
	}

	@Override
	public Map<String, InjectionGvtDTO> createFromInjectionDto(CoreSession documentManager,
			List<InjectionGvtDTO> listInjection) throws ClientException {

		Map<String, InjectionGvtDTO> oldData = new TreeMap<String, InjectionGvtDTO>();

		if (listInjection != null) {
			for (InjectionGvtDTO newInject : listInjection) {

				// Vérification des éléments essentiels à la récupération du ministère
				if (newInject != null && StringUtils.isNotEmpty(newInject.getNor())) {

					String nor = newInject.getNor();
					if (!newInject.isGvt() && !newInject.isaCreerSolon()) {
						InjectionGvtDTO oldDataFromNor = createFromNor(nor);
						oldData.put(nor, oldDataFromNor);
					}
				}
			}
		}
		return oldData;
	}

}
