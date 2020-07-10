package fr.dila.solonepg.web.activitenormative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

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
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.dto.HabilitationDTO;
import fr.dila.solonepg.api.dto.OrdonnanceHabilitationDTO;
import fr.dila.solonepg.api.exception.ActiviteNormativeException;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.cases.ActiviteNormativeProgrammationImpl;
import fr.dila.solonepg.core.dto.activitenormative.HabilitationDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.OrdonnanceHabilitationDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreLoiDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean Seam de gestion du texte maitre (38C) de l'activite normative
 * 
 * @see Habilitation
 * @see TexteMaitre
 * @see Ordonnance
 * 
 * @author asatre
 */
@Name("texteMaitreHabilitationActions")
@Scope(ScopeType.CONVERSATION)
public class TexteMaitreHabilitationActionsBean implements Serializable {

    private static final long serialVersionUID = -8006779531230543120L;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

	private static final STLogger LOGGER = STLogFactory.getLog(ActiviteNormativeProgrammationImpl.class);

    private Map<String, Boolean> mapToogle;

    private TexteMaitreLoiDTOImpl currentTexteMaitre;

    private HabilitationDTO currentHabilitationDTO;

    private List<HabilitationDTO> listHabilitation;
    private Map<String,OrdonnanceHabilitationDTO> mapOrdonnance;

    private String numeroNor;

    private String scrollTo = "anormativetextemaitre";

    @PostConstruct
    public void initialize() {
        clearPanneaux();
    }

    /**
     * Place tous les panneaux déroulable en mode ouvert.
     */
    public void clearPanneaux() {
        mapToogle = new HashMap<String, Boolean>();
        mapToogle.put("loi_habilitation", Boolean.TRUE);
        mapToogle.put("habilitation", Boolean.TRUE);
        mapToogle.put("ordonnance", Boolean.TRUE);
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

    public String saveTexteMaître() throws ClientException {
    	scrollToTexteMaitre();
        TexteMaitre texteMaitre = navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class);
        DocumentModel doc = null;
        try {
            doc = currentTexteMaitre.remapField(texteMaitre, documentManager);
			// Ajout dans le journal du PAN
			journaliserActionPAN(SolonEpgEventConstant.MODIF_TM_EVENT, SolonEpgEventConstant.MODIF_TM_COMMENT);
        } catch (ActiviteNormativeException e) {
            String message = resourcesAccessor.getMessages().get(e.getMessage());
            facesMessages.add(StatusMessage.Severity.WARN, message);
            return null;
        }

        final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();
        doc = panService.saveTexteMaitre(doc.getAdapter(TexteMaitre.class), documentManager);
        panService.updateHabilitationListePubliee(documentManager, true);
        navigationContext.setCurrentDocument(doc);

        currentTexteMaitre = new TexteMaitreLoiDTOImpl(doc.getAdapter(ActiviteNormative.class));

        Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

        return null;
    }

    public String reloadTexteMaître() throws ClientException {
    	scrollToTexteMaitre();
        navigationContext.setCurrentDocument(documentManager.getDocument(navigationContext.getCurrentDocument().getRef()));

        TexteMaitre texteMaitre = navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class);

        Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager, texteMaitre.getNumeroNor());
        DocumentModel ficheLoiDoc = null;
        if (dossier != null) {
            ficheLoiDoc = SolonEpgServiceLocator.getDossierService().findFicheLoiDocumentFromMGPP(documentManager,
                    dossier.getNumeroNor());
            if (ficheLoiDoc == null && dossier.getIdDossier() != null) {
                // M154020: fallback sur le champ iddossier pour faire la jointure
                ficheLoiDoc = SolonEpgServiceLocator.getDossierService().findFicheLoiDocumentFromMGPP(documentManager,
                        dossier.getIdDossier());
            }
        }
        currentTexteMaitre.refresh(dossier, ficheLoiDoc);

        return null;
    }

    public void setCurrentTexteMaitre(TexteMaitreLoiDTOImpl currentTexteMaitre) {
        this.currentTexteMaitre = currentTexteMaitre;
    }

    public TexteMaitreLoiDTOImpl getCurrentTexteMaitre() {
        if (currentTexteMaitre == null || !currentTexteMaitre.getId().equals(navigationContext.getCurrentDocument().getId())) {
            ActiviteNormative activiteNormative = navigationContext.getCurrentDocument().getAdapter(ActiviteNormative.class);
            currentTexteMaitre = new TexteMaitreLoiDTOImpl(activiteNormative);
        }
        return currentTexteMaitre;
    }

    public void setListHabilitation(List<HabilitationDTO> listHabilitation) {
        this.listHabilitation = listHabilitation;
    }

    public List<HabilitationDTO> getListHabilitation() throws ClientException {
        if (listHabilitation == null || currentTexteMaitre == null
                || !currentTexteMaitre.getId().equals(navigationContext.getCurrentDocument().getId())) {
            TexteMaitre texteMaitre = navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class);
            
            ActiviteNormative activiteNormative = SolonEpgServiceLocator.getActiviteNormativeService().findOrCreateActiviteNormativeByNor(documentManager, texteMaitre.getNumeroNor());
            texteMaitre = activiteNormative.getDocument().getAdapter(TexteMaitre.class);
            
            listHabilitation = mapHabilitation(texteMaitre.getHabilitationIds());
        }
        Collections.sort(listHabilitation, new Comparator<HabilitationDTO>() {
          @Override
          public int compare(HabilitationDTO hab1, HabilitationDTO hab2) {
            if (hab1.getNumeroOrdre() != null && hab2.getNumeroOrdre() != null) {
              try{
                return Integer.valueOf(hab1.getNumeroOrdre()).compareTo(Integer.valueOf(hab2.getNumeroOrdre()));
              } catch (NumberFormatException nfe){
                return hab1.getNumeroOrdre().compareTo(hab2.getNumeroOrdre());
              }
            }
            return -1;
          }
        });
        return listHabilitation;
    }

    private List<HabilitationDTO> mapHabilitation(List<String> habilitationIds) throws ClientException {
        List<HabilitationDTO> list = new ArrayList<HabilitationDTO>();
        List<Habilitation> listHab = SolonEpgServiceLocator.getActiviteNormativeService().fetchListHabilitation(habilitationIds, documentManager);

        for (Habilitation habilitation : listHab) {
            list.add(new HabilitationDTOImpl(habilitation));
        }

        if (list.isEmpty()) {
            list.add(new HabilitationDTOImpl());
        }

        listHabilitation = list;
        return list;
    }

    public void setListOrdonnance(List<OrdonnanceHabilitationDTO> listOrdonnance) {
        for(OrdonnanceHabilitationDTO ordo : listOrdonnance) {
        	mapOrdonnance.put(ordo.getNumeroNor(), ordo);
        }
    }

    public List<OrdonnanceHabilitationDTO> getListOrdonnance() throws ClientException {
        if (mapOrdonnance == null || currentTexteMaitre == null
                || !currentTexteMaitre.getId().equals(navigationContext.getCurrentDocument().getId())) {
            mapOrdonnance = mapOrdonnance(currentHabilitationDTO, Boolean.FALSE);
        }
        return new ArrayList<OrdonnanceHabilitationDTO>(mapOrdonnance.values());
    }

    private Map<String,OrdonnanceHabilitationDTO> mapOrdonnance(HabilitationDTO habilitationDTO, Boolean refreshFromDossier) throws ClientException {
        Map<String,OrdonnanceHabilitationDTO> list = new TreeMap<String,OrdonnanceHabilitationDTO>();
        List<Ordonnance> listOrdo = SolonEpgServiceLocator.getActiviteNormativeService().fetchListOrdonnance(habilitationDTO.getOrdonnanceIds(),
                documentManager);

        for (Ordonnance ordonnance : listOrdo) {
            Dossier dossier = null;
            if (refreshFromDossier) {
                dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager, ordonnance.getNumeroNor());
            }
            list.put(ordonnance.getNumeroNor(),new OrdonnanceHabilitationDTOImpl(ordonnance, habilitationDTO, dossier));
        }

        if (list.isEmpty()) {
            list.put("", new OrdonnanceHabilitationDTOImpl(""));
        }

        mapOrdonnance = list;
        return list;
    }

    public void setCurrentHabilitationDTO(HabilitationDTO currentHabilitationDTO) {
    	scrollToOrdonnances();
        this.currentHabilitationDTO = currentHabilitationDTO;
        mapOrdonnance = null;
    }

    public HabilitationDTO getCurrentHabilitationDTO() {
        return currentHabilitationDTO;
    }

    public String addNewHabilitation() {
    	scrollToHabilitations();
    	HabilitationDTO tmp = new HabilitationDTOImpl();
    	tmp.setValidate(true);
        listHabilitation.add(tmp);
		// Ajout dans le journal du PAN
		try {
			journaliserActionPAN(SolonEpgEventConstant.AJOUT_HABIL_EVENT, SolonEpgEventConstant.AJOUT_HABIL_COMMENT);
		} catch (Exception e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_AJOUT_JOURNAL_PAN, e);
		}
        return null;
    }

    public String removeHabilitation(HabilitationDTO habilitationDTO) {
    	scrollToHabilitations();
        listHabilitation.remove(habilitationDTO);
		// Ajout dans le journal du PAN
		try {
			journaliserActionPAN(SolonEpgEventConstant.SUPPR_HABIL_EVENT, SolonEpgEventConstant.SUPPR_HABIL_COMMENT);
		} catch (Exception e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_AJOUT_JOURNAL_PAN, e);
		}
        return null;
    }

    public void setNumeroNor(String numeroNor) {
        this.numeroNor = numeroNor;
    }

    public String getNumeroNor() {
        return numeroNor;
    }

	public String addNewOrdonnance() throws ClientException {
		scrollToOrdonnances();
		Ordonnance ordonnance = null;
		if (StringUtil.isNotEmpty(numeroNor)) {
			if (!mapOrdonnance.containsKey(numeroNor)) {
				try {
					ordonnance = SolonEpgServiceLocator.getActiviteNormativeService().checkIsOrdonnance38CFromNumeroNOR(numeroNor, documentManager);
				} catch (ActiviteNormativeException e) {
					String message = resourcesAccessor.getMessages().get(e.getMessage());
					facesMessages.add(StatusMessage.Severity.WARN, message);
					return null;
				}
		
				OrdonnanceHabilitationDTO ordonnanceDTO = null;
				if (ordonnance == null) {
					ordonnanceDTO = new OrdonnanceHabilitationDTOImpl(numeroNor, currentHabilitationDTO);
				} else {
					Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager, ordonnance.getNumeroNor());
					ordonnanceDTO = new OrdonnanceHabilitationDTOImpl(ordonnance, currentHabilitationDTO, dossier);
				}
				ordonnanceDTO.setValidation(Boolean.TRUE);
				ordonnanceDTO.setValidate(Boolean.TRUE);
				mapOrdonnance.put(ordonnanceDTO.getNumeroNor(),ordonnanceDTO);
				// Ajout dans le journal du PAN
				journaliserActionPAN(SolonEpgEventConstant.AJOUT_ORDO_EVENT,
						SolonEpgEventConstant.AJOUT_ORDO_COMMENT + " [" + ordonnanceDTO.getNumeroNor()
								+ "] à l'habilitation [" + currentHabilitationDTO.getNumeroOrdre() + "]");
			} else {
        		String message = resourcesAccessor.getMessages().get("activite.normative.ordonnance.existant");
        		facesMessages.add(StatusMessage.Severity.WARN, message);
			}
		}
		return null;
	}

    public String removeOrdonnanceHabilitation(OrdonnanceHabilitationDTO ordonnanceHabilitationDTO) {
    	scrollToOrdonnances();
        mapOrdonnance.remove(ordonnanceHabilitationDTO.getNumeroNor());
		// Ajout dans le journal du PAN
		try {
			journaliserActionPAN(SolonEpgEventConstant.SUPPR_ORDO_EVENT,
					SolonEpgEventConstant.SUPPR_ORDO_COMMENT + " [" + ordonnanceHabilitationDTO.getNumeroNor()
							+ "] de l'habilitation [" + currentHabilitationDTO.getNumeroOrdre() + "]");
		} catch (Exception e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_AJOUT_JOURNAL_PAN, e);
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
        if (dto instanceof HabilitationDTO) {
            HabilitationDTO habilitationDTO = (HabilitationDTO) dto;
            if (habilitationDTO.getId() != null) {
	            final ActiviteNormativeService actNormS = SolonEpgServiceLocator.getActiviteNormativeService();
	        	DocumentModel habilitationDoc = documentManager.getDocument(new IdRef(habilitationDTO.getId()));
	        	Habilitation habilitation = habilitationDoc.getAdapter(Habilitation.class);
	        	
	            if (!habilitationDTO.hasValidation() || !actNormS.checkOrdonnancesValidationForHabilitation(documentManager, habilitation)) {
	                return "dataRowRetourPourModification";
	            }
            }
        }

        if (dto instanceof OrdonnanceHabilitationDTO) {
            OrdonnanceHabilitationDTO ordonnanceHabilitationDTO = (OrdonnanceHabilitationDTO) dto;
            if (ordonnanceHabilitationDTO.getId() != null && (!ordonnanceHabilitationDTO.hasValidation() || !ordonnanceHabilitationDTO.hasValidationLink())) {
                return "dataRowRetourPourModification";
            }
        }

        if (currentHabilitationDTO != null) {
            if (currentHabilitationDTO.getId() == null) {
                return null;
            } else if (currentHabilitationDTO.getId().equals(dto.getDocIdForSelection())) {
                return "dataRowSelected";
            }
        }
        return defaultClass;
    }

    public Boolean isHabilitationLoaded() {
        return currentHabilitationDTO != null;
    }

    public void reset() {
        currentHabilitationDTO = null;
        currentTexteMaitre = null;
        listHabilitation = null;
        mapOrdonnance = null;
    }

    /**
     * Sauvegarde des habilitations
     * 
     * @return
     * @throws ClientException
     */
    public String saveHabilitation() throws ClientException {
    	scrollToHabilitations();
        TexteMaitre texteMaitre = null;

        try {
            texteMaitre = SolonEpgServiceLocator.getActiviteNormativeService().saveHabilitation(listHabilitation, getCurrentTexteMaitre().getId(),
                    documentManager);
        } catch (ClientException e) {
            String message = resourcesAccessor.getMessages().get(e.getMessage());
            facesMessages.add(StatusMessage.Severity.WARN, message);
            TransactionHelper.setTransactionRollbackOnly();
            return null;
        }
        // force reset
        navigationContext.setCurrentDocument(null);

        navigationContext.setCurrentDocument(texteMaitre.getDocument());

        currentHabilitationDTO = null;
        listHabilitation = null;
        mapOrdonnance = null;
        // rechargement des habilitations
        getListHabilitation();
        return null;
    }

    /**
     * reload des habilitations
     * 
     * @return
     * @throws ClientException
     */
    public String reloadHabilitation() throws ClientException {
    	scrollToHabilitations();
        TexteMaitre texteMaitre = navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class);
        listHabilitation = mapHabilitation(texteMaitre.getHabilitationIds());
        mapOrdonnance = null;
        return null;
    }

    /**
     * Sauvegarde des ordonnances habilitations
     * 
     * @return
     * @throws ClientException
     */
    public String saveOrdonnanceHabilitation() throws ClientException {
    	scrollToOrdonnances();
        List<String> listOrdonnancesId = null;
        try {
        	List<OrdonnanceHabilitationDTO> lstOrdos = getListOrdonnance();
            listOrdonnancesId = SolonEpgServiceLocator.getActiviteNormativeService().saveOrdonnanceHabilitation(lstOrdos,
					currentHabilitationDTO.getId(), currentTexteMaitre.getId(), documentManager);
        } catch (ActiviteNormativeException e ){
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
        } catch (ClientException e) {
            String message = resourcesAccessor.getMessages().get(e.getMessage());
            facesMessages.add(StatusMessage.Severity.WARN, message);
            TransactionHelper.setTransactionRollbackOnly();
            return null;
        }

        currentHabilitationDTO.setOrdonnanceIds(listOrdonnancesId);
        currentHabilitationDTO.setOrdonnanceIdsInvalidated(null);

        // rechargement des ordonnances
        mapOrdonnance = null;
        reloadOrdonnanceHabilitation(Boolean.FALSE);
        return null;
    }

    /**
     * reload des ordonnances habilitations
     * 
     * @return
     * @throws ClientException
     */
    public String reloadOrdonnanceHabilitation() throws ClientException {
    	scrollToOrdonnances();
        return reloadOrdonnanceHabilitation(Boolean.TRUE);
    }
    
    private String reloadOrdonnanceHabilitation(Boolean refreshDossier) throws ClientException {
        mapOrdonnance = mapOrdonnance(currentHabilitationDTO, refreshDossier);
        return null;
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

	public void scrollToHabilitations() {
		setScrollTo("an_habilitation");
	}

	public void scrollToOrdonnances() {
		setScrollTo("an_ordonnance");
	}

	public void journaliserActionPAN(String event, String comment) throws ClientException {
		DocumentModel currentDoc = navigationContext.getCurrentDocument();
		DocumentModel dossierDoc;
		dossierDoc = SolonEpgServiceLocator.getNORService().getDossierFromNOR(documentManager,
				currentDoc.getAdapter(TexteMaitre.class).getNumeroNor());
		STServiceLocator.getJournalService().journaliserActionPAN(documentManager, dossierDoc.getRef().toString(),
				event, comment, SolonEpgEventConstant.CATEGORY_LOG_PAN_SUIVI_HABILITATIONS);
	}
}
