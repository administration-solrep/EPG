package fr.dila.solonepg.web.activitenormative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.LoiDeRatification;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.dto.DecretApplicationDTO;
import fr.dila.solonepg.api.dto.LoiDeRatificationDTO;
import fr.dila.solonepg.api.dto.OrdonnanceDTO;
import fr.dila.solonepg.api.exception.ActiviteNormativeException;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.cases.ActiviteNormativeProgrammationImpl;
import fr.dila.solonepg.core.dto.activitenormative.DecretApplicationDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.LoiDeRatificationDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.OrdonnanceDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean Seam de gestion du texte maitre (74-1) de l'activite normative
 * 
 * @author asatre
 */
@Name("texteMaitreOrdonnanceActions")
@Scope(ScopeType.CONVERSATION)
public class TexteMaitreOrdonnanceActionsBean implements Serializable {

	private static final long					serialVersionUID				= 2776349205588506388L;

	private static final Log					log								= LogFactory
																						.getLog(TexteMaitreOrdonnanceActionsBean.class);

	private static final String					QUERY_ORDONNANCES				= "SELECT d.ecm:uuid as id FROM "
																						+ ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE
																						+ " as d WHERE d."
																						+ ActiviteNormativeConstants.ACTIVITE_NORMATIVE_PREFIX
																						+ ":"
																						+ ActiviteNormativeConstants.P_ORDONNANCE
																						+ " = '"
																						+ ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE
																						+ "' ";

	private static final String					QUERY_ORDONNANCES_NOT_RATIFIEE	= QUERY_ORDONNANCES
																						+ " AND d."
																						+ TexteMaitreConstants.TEXTE_MAITRE_PREFIX
																						+ ":"
																						+ TexteMaitreConstants.RATIFIE
																						+ " = 0 ";

	@In(create = true, required = false)
	protected transient NavigationContextBean	navigationContext;

	@In(create = true, required = true)
	protected transient CoreSession				documentManager;

	@In(create = true, required = false)
	protected transient ResourcesAccessor		resourcesAccessor;

	@In(create = true, required = false)
	protected transient FacesMessages			facesMessages;

	private static final STLogger LOGGER = STLogFactory.getLog(ActiviteNormativeProgrammationImpl.class);

	private Map<String, Boolean>				mapToogle;

	private OrdonnanceDTO						currentOrdonnance;

	private String								newLoiRatification;

	private LoiDeRatificationDTO				currentLoiDeRatification;

	private List<LoiDeRatificationDTO>			listLoiDeRatification;

	private Map<String, DecretApplicationDTO>	mapDecret;

	private String								newDecret;

	private Boolean								masquerRatifie;

	private String								scrollTo						= "anormativetextemaitre";

	@PostConstruct
	public void initialize() {
		masquerRatifie = Boolean.TRUE;
		clearPanneaux();
	}

	/**
	 * Place tous les panneaux déroulable en mode ouvert.
	 */
	public void clearPanneaux() {
		mapToogle = new HashMap<String, Boolean>();
		mapToogle.put("ordonnance", Boolean.TRUE);
		mapToogle.put("loi_ratification", Boolean.TRUE);
		mapToogle.put("decret_application", Boolean.TRUE);
		mapToogle.put("loi_habilitation", Boolean.TRUE);
	}

	/**
	 * etat courant du panneau dans le xhtml
	 * 
	 * @param toggleBoxName
	 * @return
	 */
	public Boolean getToggleBox(String toggleBoxName) {
		return mapToogle.get(toggleBoxName);
	}

	public Boolean setToggleBox(String toggleBoxName) {
		Boolean result = mapToogle.get(toggleBoxName);
		mapToogle.put(toggleBoxName, !result);
		return result;
	}

	public void setCurrentOrdonnanceDTO(OrdonnanceDTO currentOrdonnanceDTO) {
		currentOrdonnance = currentOrdonnanceDTO;
	}

	public OrdonnanceDTO getCurrentOrdonnanceDTO() throws ClientException {
		findCurrentOrdonnance();
		return currentOrdonnance;
	}

	private void findCurrentOrdonnance() throws ClientException {
		if (currentOrdonnance == null
				|| !currentOrdonnance.getId().equals(navigationContext.getCurrentDocument().getId())) {
			currentOrdonnance = new OrdonnanceDTOImpl(navigationContext.getCurrentDocument().getAdapter(
					ActiviteNormative.class), documentManager);
		}
	}

	public String reloadOrdonnance() throws ClientException {
		scrollToTexteMaitre();
		TexteMaitre texteMaitre = navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class);
		Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager,
				texteMaitre.getNumeroNor());
		if (dossier == null) {
			log.error("le dossier avec nor " + texteMaitre.getNumeroNor() + " n'existe pas");
			facesMessages.add(StatusMessage.Severity.WARN, "le dossier avec nor " + texteMaitre + " n'existe pas");
		} else {
			try {
				currentOrdonnance.refresh(dossier, documentManager);
			} catch (Exception e) {
				facesMessages.add(StatusMessage.Severity.WARN,
						"L'actualisation n'a pas pu se faire, merci de réessayer");
			}
		}
		return null;
	}

	public String saveOrdonnance() throws ClientException {
		scrollToTexteMaitre();
		TexteMaitre texteMaitre = SolonEpgServiceLocator.getActiviteNormativeService().saveOrdonnance(
				currentOrdonnance, navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class),
				documentManager);
		navigationContext.setCurrentDocument(texteMaitre.getDocument());
		currentOrdonnance = new OrdonnanceDTOImpl(navigationContext.getCurrentDocument().getAdapter(
				ActiviteNormative.class), documentManager);
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		SolonEpgServiceLocator.getActiviteNormativeService().updateOrdonnancesListePubliee(documentManager, true);
		journaliserActionPAN(SolonEpgEventConstant.MODIF_TM_EVENT, SolonEpgEventConstant.MODIF_TM_COMMENT);
		return null;
	}

	public String saveLoiRatification() throws ClientException {
		scrollToRatification();
		DocumentModel doc = documentManager.getDocument(navigationContext.getCurrentDocument().getRef());
		ActiviteNormative activiteNormative = doc.getAdapter(ActiviteNormative.class);

		try {
			activiteNormative = SolonEpgServiceLocator.getActiviteNormativeService().saveProjetsLoiDeRatification(
					listLoiDeRatification, documentManager, activiteNormative);
		} catch (ActiviteNormativeException e) {
			if (e.getErrors() == null || e.getErrors().isEmpty()) {
				String message = resourcesAccessor.getMessages().get(
						"activite.normative.error.save.after.loiRatification");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return null;
			} else {
				for (String error : e.getErrors()) {
					facesMessages.add(StatusMessage.Severity.WARN, error);
				}
				return null;
			}
		}
		navigationContext.setCurrentDocument(null);
		navigationContext.setCurrentDocument(activiteNormative.getDocument());
		mapLoiDeRatification(navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class));
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		return null;
	}

	public void reloadLoiRatification() throws ClientException {
		scrollToRatification();
		listLoiDeRatification = mapLoiDeRatification(
				navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class), true);
	}

	public List<LoiDeRatificationDTO> getListLoiRatification() throws ClientException {
		if (listLoiDeRatification == null || currentOrdonnance == null
				|| !currentOrdonnance.getId().equals(navigationContext.getCurrentDocument().getId())) {
			listLoiDeRatification = mapLoiDeRatification(navigationContext.getCurrentDocument().getAdapter(
					TexteMaitre.class));
		}
		return listLoiDeRatification;
	}

	private List<LoiDeRatificationDTO> mapLoiDeRatification(TexteMaitre texteMaitre) throws ClientException {
		return mapLoiDeRatification(texteMaitre, false);
	}

	private List<LoiDeRatificationDTO> mapLoiDeRatification(TexteMaitre texteMaitre, boolean fromReload)
			throws ClientException {
		List<LoiDeRatificationDTO> list = new ArrayList<LoiDeRatificationDTO>();
		List<LoiDeRatification> listLoiRatification = SolonEpgServiceLocator.getActiviteNormativeService()
				.fetchLoiDeRatification(texteMaitre.getLoiRatificationIds(), documentManager);

		for (LoiDeRatification loiDeRatification : listLoiRatification) {
			if (fromReload) {
				Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager,
						loiDeRatification.getNumeroNor());
				DocumentModel ficheLoiDoc = null;
				if (dossier != null) {
					// M154020: fallback sur le champ iddossier pour faire la jointure
					ficheLoiDoc = SolonEpgServiceLocator.getDossierService().findFicheLoiDocumentFromMGPP(
							documentManager, dossier.getNumeroNor());
					if (ficheLoiDoc == null && dossier.getIdDossier() != null) {
						ficheLoiDoc = SolonEpgServiceLocator.getDossierService().findFicheLoiDocumentFromMGPP(
								documentManager, dossier.getIdDossier());
					}
				}
				list.add(new LoiDeRatificationDTOImpl(loiDeRatification, dossier, ficheLoiDoc));
			} else {
				list.add(new LoiDeRatificationDTOImpl(loiDeRatification));
			}
		}
		if (list.isEmpty()) {
			list.add(new LoiDeRatificationDTOImpl());
		}

		listLoiDeRatification = list;
		return list;
	}

	public void setNewLoiRatification(String newLoiRatification) {
		this.newLoiRatification = newLoiRatification;
	}

	public String getNewLoiRatification() {
		return newLoiRatification;
	}

	public String addLoiRatification() throws ClientException {
		scrollToRatification();
		if (StringUtil.isNotEmpty(newLoiRatification)) {
			final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();

			LoiDeRatificationDTO loiDeRatificationDTO = null;

			ActiviteNormative activiteNormative = panService.findActiviteNormativeByNor(newLoiRatification,
					documentManager);

			if (activiteNormative != null) {
				LoiDeRatification loiDeRatification = activiteNormative.getDocument().getAdapter(
						LoiDeRatification.class);
				loiDeRatificationDTO = new LoiDeRatificationDTOImpl(loiDeRatification);
			} else {
				try {
					Dossier dossier = panService.checkIsLoi(newLoiRatification, documentManager);
					loiDeRatificationDTO = new LoiDeRatificationDTOImpl(dossier);
				} catch (ActiviteNormativeException e) {
					String message = resourcesAccessor.getMessages().get(e.getMessage());
					facesMessages.add(StatusMessage.Severity.WARN, message);
					return null;
				}
			}

			loiDeRatificationDTO.setNumeroNor(newLoiRatification);
			if (!currentOrdonnance.isDispositionHabilitation() && currentOrdonnance.getDatePublication() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentOrdonnance.getDatePublication());
				cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) + 18));
				loiDeRatificationDTO.setDateLimitePublication(cal.getTime());
			}
			listLoiDeRatification.add(loiDeRatificationDTO);
			newLoiRatification = null;
			// Ajout dans le journal du PAN
			journaliserActionPAN(SolonEpgEventConstant.AJOUT_RATIF_EVENT,
					SolonEpgEventConstant.AJOUT_RATIF_COMMENT + " [" + loiDeRatificationDTO.getNumeroNor() + "]");
		}

		return null;
	}

	/**
	 * Decoration de la ligne de la mesure selectionnée dans la table des mesure
	 * 
	 * @param dto
	 * @return
	 * @throws ClientException
	 */
	public String decorate(AbstractMapDTO dto, String defaultClass) throws ClientException {
		if (dto instanceof LoiDeRatificationDTO) {
			LoiDeRatificationDTO loiDeRatificationDTO = (LoiDeRatificationDTO) dto;
			if (loiDeRatificationDTO.getId() != null && !loiDeRatificationDTO.hasValidation()) {
				return "dataRowRetourPourModification";
			}
		} else if (dto instanceof DecretApplicationDTO) {
			DecretApplicationDTO decretAppDTO = (DecretApplicationDTO) dto;
			if (decretAppDTO.getId() != null && (!decretAppDTO.hasValidation() || !decretAppDTO.hasValidationLink())) {
				return "dataRowRetourPourModification";
			}
		}

		if (currentLoiDeRatification != null) {
			if (currentLoiDeRatification.getId() == null) {
				return null;
			} else if (currentLoiDeRatification.getId().equals(dto.getDocIdForSelection())) {
				return "dataRowSelected";
			}
		}
		return defaultClass;
	}

	public LoiDeRatificationDTO getCurrentLoiDeRatification() {
		return currentLoiDeRatification;
	}

	public void setCurrentLoiDeRatification(LoiDeRatificationDTO currentLoiDeRatification) {
		this.currentLoiDeRatification = currentLoiDeRatification;
	}

	public String setLoiDeRatification(LoiDeRatificationDTO loiDeRatificationDTO) {
		currentLoiDeRatification = loiDeRatificationDTO;
		return null;
	}

	private void loadDecret() throws ClientException {
		if (currentOrdonnance == null) {
			// reset decret
			mapDecret = new TreeMap<String, DecretApplicationDTO>();
			setListDecret(new ArrayList<DecretApplicationDTO>());
		} else {
			// load list decret
			mapDecret = mapDecret(currentOrdonnance, Boolean.FALSE);
		}
	}

	private Map<String, DecretApplicationDTO> mapDecret(OrdonnanceDTO ordonnanceDTO, Boolean refreshFromDossier)
			throws ClientException {
		Map<String, DecretApplicationDTO> list = new TreeMap<String, DecretApplicationDTO>();
		ActiviteNormativeService activiteNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();

		List<Decret> listDecret = activiteNormativeService.fetchDecrets(ordonnanceDTO.getDecretIds(), documentManager);

		for (Decret decret : listDecret) {
			if (refreshFromDossier && decret.getIdDossier() != null) {
				DocumentModel dossierDoc = documentManager.getDocument(new IdRef(decret.getIdDossier()));
				list.put(decret.getNumeroNor(),
						new DecretApplicationDTOImpl(decret, dossierDoc.getAdapter(Dossier.class), ordonnanceDTO));
			} else {
				list.put(decret.getNumeroNor(), new DecretApplicationDTOImpl(decret, null, ordonnanceDTO));
			}
		}

		if (list.isEmpty()) {
			list.put("", new DecretApplicationDTOImpl());
		}

		return list;
	}

	public String removeLoiDeRatification(LoiDeRatificationDTO loiDeRatificationDTO) {
		scrollToRatification();
		if (loiDeRatificationDTO != null) {
			listLoiDeRatification.remove(loiDeRatificationDTO);
			if (currentLoiDeRatification != null && currentLoiDeRatification.getId() != null
					&& currentLoiDeRatification.getId().equals(loiDeRatificationDTO.getId())) {
				currentLoiDeRatification = null;
			}
			// Ajout dans le journal du PAN
			try {
				journaliserActionPAN(SolonEpgEventConstant.SUPPR_RATIF_EVENT,
						SolonEpgEventConstant.SUPPR_RATIF_COMMENT + " [" + loiDeRatificationDTO.getNumeroNor() + "]");
			} catch (Exception e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_AJOUT_JOURNAL_PAN, e);
			}
		}
		return null;
	}

	public String reloadDecrets() throws ClientException {
		return reloadDecrets(Boolean.TRUE);
	}

	public Boolean isLoiRatificationLoaded() {
		return currentLoiDeRatification != null;
	}

	public void setListDecret(List<DecretApplicationDTO> listDecret) {
		for (DecretApplicationDTO decret : listDecret) {
			mapDecret.put(decret.getNumeroNor(), decret);
		}
	}

	public List<DecretApplicationDTO> getListDecret() throws ClientException {
		if (mapDecret == null) {
			loadDecret();
		}
		return new ArrayList<DecretApplicationDTO>(mapDecret.values());
	}

	public void setNewDecret(String newDecret) {
		this.newDecret = newDecret;
	}

	public String getNewDecret() {
		return newDecret;
	}

	public String addNewDecret() throws ClientException {
		String message;
		DecretApplicationDTO decretDTO = null;
		if (StringUtil.isNotEmpty(newDecret)) {
			if (!mapDecret.containsKey(newDecret)) {
				try {
					Dossier dossier = SolonEpgServiceLocator.getActiviteNormativeService().checkIsDecretFromNumeroNOR(
							newDecret, documentManager);
					if (dossier != null) {
						decretDTO = new DecretApplicationDTOImpl(dossier);

						decretDTO.setValidation(Boolean.TRUE);
						decretDTO.setValidate(Boolean.TRUE);
						mapDecret.put(decretDTO.getNumeroNor(), decretDTO);
					}
				} catch (ActiviteNormativeException e) {
					message = resourcesAccessor.getMessages().get(e.getMessage());
					facesMessages.add(StatusMessage.Severity.WARN, message);
				}
			} else {
				message = resourcesAccessor.getMessages().get("activite.normative.decret.existant");
				facesMessages.add(StatusMessage.Severity.WARN, message);
			}
		}

		newDecret = null;

		return null;
	}

	public String removeDecret(DecretApplicationDTO decretDTO) {
		mapDecret.remove(decretDTO.getNumeroNor());
		return null;
	}

	public String reloadDecrets(Boolean refreshFromDossier) throws ClientException {
		if (currentOrdonnance != null) {
			mapDecret = mapDecret(currentOrdonnance, refreshFromDossier);
		} else {
			mapDecret = new TreeMap<String, DecretApplicationDTO>();
		}
		return null;
	}

	public String saveDecret() throws ClientException {
		if (currentOrdonnance != null) {
			String idCurrentOrdonnance = currentOrdonnance.getId();
			if (StringUtil.isEmpty(idCurrentOrdonnance)) {
				String message = resourcesAccessor.getMessages().get("activite.normative.error.save.before.decret");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return null;
			}

			try {
				List<DecretApplicationDTO> lstDecret = getListDecret();
				SolonEpgServiceLocator.getActiviteNormativeService().saveDecretsOrdonnance(idCurrentOrdonnance,
						lstDecret, documentManager);
				currentOrdonnance.setDecretIds(documentManager.getDocument(new IdRef(idCurrentOrdonnance))
						.getAdapter(TexteMaitre.class).getDecretIds());
				currentOrdonnance.setDecretIdsInvalidated(null);
				reloadDecrets(Boolean.FALSE);

			} catch (ActiviteNormativeException e) {
				String message = resourcesAccessor.getMessages().get("activite.normative.error.save.after.decret");
				if (e.getErrors() == null || e.getErrors().isEmpty()) {
					facesMessages.add(StatusMessage.Severity.WARN, message);
					return null;
				} else {
					message = resourcesAccessor.getMessages().get("activite.normative.error.save.mauvais.decret");
					StringBuilder nors = new StringBuilder(StringUtil.join(e.getErrors(), ", ", ""));
					facesMessages.add(StatusMessage.Severity.WARN, message + nors.toString());
					return null;
				}
			}
		}
		return null;
	}

	public void reset() {
		currentOrdonnance = null;
		mapDecret = null;
		currentLoiDeRatification = null;
		listLoiDeRatification = null;
	}

	public String getQuery() {
		if (isMasquerRatifie()) {
			return QueryUtils.ufnxqlToFnxqlQuery(QUERY_ORDONNANCES_NOT_RATIFIEE);
		} else {
			return QueryUtils.ufnxqlToFnxqlQuery(QUERY_ORDONNANCES);
		}
	}

	/**
	 * restriction d'affichage sur les ordonnances ratifiées
	 * 
	 * @return
	 */
	public Boolean isMasquerRatifie() {
		return masquerRatifie;
	}

	public void invertMasquerRatifie() throws ClientException {
		if (masquerRatifie != null) {
			masquerRatifie = !masquerRatifie;
		} else {
			masquerRatifie = Boolean.FALSE;
		}

		navigationContext.setCurrentDocument(null);
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
	}

	public void setScrollTo(String scrollTo) {
		this.scrollTo = scrollTo;
	}

	public String getScrollTo() {
		return scrollTo;
	}

	public void scrollToTexteMaitre() {
		setScrollTo("anormativetextemaitre");
	}

	public void scrollToRatification() {
		setScrollTo("loi_ratification");
	}

	public void journaliserActionPAN(String event, String comment) throws ClientException {
		DocumentModel currentDoc = navigationContext.getCurrentDocument();
		DocumentModel dossierDoc;
		dossierDoc = SolonEpgServiceLocator.getNORService().getDossierFromNOR(documentManager,
				currentDoc.getAdapter(TexteMaitre.class).getNumeroNor());
		STServiceLocator.getJournalService().journaliserActionPAN(documentManager, dossierDoc.getRef().toString(),
				event, comment, SolonEpgEventConstant.CATEGORY_LOG_PAN_RATIFICATION_ORDO);
	}
}
