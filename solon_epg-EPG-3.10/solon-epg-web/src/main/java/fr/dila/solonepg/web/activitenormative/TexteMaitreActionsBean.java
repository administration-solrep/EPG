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
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.dto.DecretDTO;
import fr.dila.solonepg.api.dto.MesureApplicativeDTO;
import fr.dila.solonepg.api.exception.ActiviteNormativeException;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.cases.ActiviteNormativeProgrammationImpl;
import fr.dila.solonepg.core.dto.activitenormative.DecretDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.MesureApplicativeDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreLoiDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.dossier.DossierListingActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean Seam de gestion du texte maitre (loi) de l'activite normative
 * 
 * @author asatre
 */
@Name("texteMaitreActions")
@Scope(ScopeType.CONVERSATION)
public class TexteMaitreActionsBean implements Serializable {

    private static final long serialVersionUID = 2776349205588506388L;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true, required = false)
    protected transient DossierListingActionsBean dossierListingActions;

    @In(create = true, required = false)
    protected transient ActiviteNormativeActionsBean activiteNormativeActions;

    @In(create = true, required = false)
    protected transient TexteMaitreHabilitationActionsBean texteMaitreHabilitationActions;

    @In(create = true, required = false)
    protected transient TexteMaitreOrdonnanceActionsBean texteMaitreOrdonnanceActions;

    @In(create = true, required = false)
    protected transient TexteMaitreTraitesActionsBean texteMaitreTraitesActions;

    @In(create = true, required = false)
    protected transient TranspositionDirectiveActionsBean transpositionDirectiveActions;
    
    @In(create = true, required = false)
    protected transient ContentViewActions contentViewActions;
    
    public static final String EVENT_MESURES_ADDED = "mesuresAdded";

	private static final STLogger LOGGER = STLogFactory.getLog(ActiviteNormativeProgrammationImpl.class);

    private TexteMaitreLoiDTOImpl currentTexteMaitre;
    private MesureApplicativeDTO currentMesure;
    private MesureApplicativeDTO currentEditMesure;
    private List<MesureApplicativeDTO> listMesure;
    private Map<String,DecretDTO> mapDecret;
    private String newDecret;

    private Map<String, Boolean> mapToogle;

    private OrganigrammeNode currentMinistere;
    
    private Integer nombreMesuresToAdd = 1;
    
    private String scrollTo = "anormativetextemaitre";

    @PostConstruct
    public void initialize() {
        clearPanneaux();
    }

    /**
     * Place tous les panneaux déroulables en mode ouvert.
     */
    public void clearPanneaux() {
        mapToogle = new HashMap<String, Boolean>();
        mapToogle.put("maitre", Boolean.TRUE);
        mapToogle.put("mesures", Boolean.TRUE);
        mapToogle.put("decret", Boolean.TRUE);
    }

    public String saveTexteMaître() throws ClientException {
        TexteMaitre texteMaitre = navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class);		
        DocumentModel doc = null;
        try {
            doc = currentTexteMaitre.remapField(texteMaitre, documentManager);
        } catch (ActiviteNormativeException e) {
            String message = resourcesAccessor.getMessages().get(e.getMessage());
            facesMessages.add(StatusMessage.Severity.WARN, message);
            return null;
        }

        final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();

        doc = panService.saveTexteMaitre(doc.getAdapter(TexteMaitre.class), documentManager);
        panService.updateLoiListePubliee(documentManager,true);

        navigationContext.setCurrentDocument(null);
        navigationContext.setCurrentDocument(doc);

        currentTexteMaitre = new TexteMaitreLoiDTOImpl(doc.getAdapter(ActiviteNormative.class));

        Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
        
        String message = resourcesAccessor.getMessages().get("activite.normative.texte.maitre.save.done");
        facesMessages.add(StatusMessage.Severity.INFO, message);

		// Ajout dans le journal du PAN
		ActiviteNormative activiteNormative = texteMaitre.getAdapter(ActiviteNormative.class);
		DocumentModel dossierDoc = SolonEpgServiceLocator.getNORService().getDossierFromNOR(documentManager,
				texteMaitre.getNumeroNor());
		if (activiteNormative.getApplicationLoi().equals("1")) {
			STServiceLocator.getJournalService().journaliserActionPAN(documentManager, dossierDoc.getRef().toString(),
					SolonEpgEventConstant.MODIF_TM_EVENT, SolonEpgEventConstant.MODIF_TM_COMMENT,
					SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_LOIS);
		} else if (activiteNormative.getApplicationOrdonnance().equals("1")) {
			STServiceLocator.getJournalService().journaliserActionPAN(documentManager, dossierDoc.getRef().toString(),
					SolonEpgEventConstant.MODIF_TM_EVENT, SolonEpgEventConstant.MODIF_TM_COMMENT,
					SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_ORDO);
		}

        scrollToTexteMaitre();
        return null;
    }

    public String reloadTexteMaître() throws ClientException {
        navigationContext.setCurrentDocument(documentManager.getDocument(navigationContext.getCurrentDocument().getRef()));

        TexteMaitre texteMaitre = navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class);

        Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager, texteMaitre.getNumeroNor());
        DocumentModel ficheLoiDoc = null;
        if (dossier != null) {
            // M154020: fallback sur le champ iddossier pour faire la jointure
            ficheLoiDoc = SolonEpgServiceLocator.getDossierService().findFicheLoiDocumentFromMGPP(documentManager,
                    dossier.getNumeroNor());
            if (ficheLoiDoc == null && dossier.getIdDossier() != null) {
                ficheLoiDoc = SolonEpgServiceLocator.getDossierService().findFicheLoiDocumentFromMGPP(documentManager,
                        dossier.getIdDossier());
            }
        }
        currentTexteMaitre.refresh(dossier, ficheLoiDoc);

        scrollToTexteMaitre();
        reloadMesures(true);
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

    private Map<String,DecretDTO> mapDecret(MesureApplicativeDTO currentMesure, Boolean refreshFromDossier) throws ClientException {
        Map<String,DecretDTO> list = new TreeMap<String, DecretDTO>();
        final ActiviteNormativeService activiteNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();

        List<Decret> listDecret = activiteNormativeService.fetchDecrets(currentMesure.getDecretIds(), documentManager);

        for (Decret decret : listDecret) {
            if (refreshFromDossier && StringUtil.isNotBlank(decret.getNumeroNor())) {
                Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager, decret.getNumeroNor());
                list.put(decret.getNumeroNor(), new DecretDTOImpl(decret, dossier, currentMesure, navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class)));
            } else {
                list.put(decret.getNumeroNor(), new DecretDTOImpl(decret, null, currentMesure, navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class)));
            }
        }
        return list;
    }

    private List<MesureApplicativeDTO> mapMesure(TexteMaitre texteMaitre) throws ClientException {
        List<MesureApplicativeDTO> list = new ArrayList<MesureApplicativeDTO>();

        List<MesureApplicative> listMesureApplicative = SolonEpgServiceLocator.getActiviteNormativeService().fetchMesure(texteMaitre.getMesuresIds(),
                documentManager);

        for (MesureApplicative mesureApplicative : listMesureApplicative) {
            list.add(new MesureApplicativeDTOImpl(mesureApplicative, texteMaitre));
        }

        if (list.isEmpty()) {
            list.add(new MesureApplicativeDTOImpl());
        }

        // tri par defaut sur le numero d'ordre
        Collections.sort(list, new Comparator<MesureApplicativeDTO>() {

            @Override
            public int compare(MesureApplicativeDTO mesure1, MesureApplicativeDTO mesure2) {
                if (mesure1.getNumeroOrdre() != null && mesure2.getNumeroOrdre() != null) {
                    String str1 = String.format("%9s", mesure1.getNumeroOrdre());
                    String str2 = String.format("%9s", mesure2.getNumeroOrdre());
                    return str1.compareTo(str2);
                }
                return -1;

            }

        });
        listMesure = list;
        return list;
    }

    public String reloadMesures() throws ClientException {
        scrollToMesures();
        reloadMesures(true);
        return null;
    }

    private void reloadMesures(Boolean cancelCurrent) throws ClientException {
        TexteMaitre texteMaitre = documentManager.getDocument(navigationContext.getCurrentDocument().getRef()).getAdapter(TexteMaitre.class);
        listMesure = mapMesure(texteMaitre);
        if (cancelCurrent) {
            currentMesure = null;
        }
    }

    public String reloadDecrets() throws ClientException {
        scrollToDecrets();
        return reloadDecrets(Boolean.TRUE);
    }

    public String reloadDecrets(Boolean refreshFromDossier) throws ClientException {
        if (currentMesure != null) {
            mapDecret = mapDecret(currentMesure, refreshFromDossier);
        } else {
            mapDecret = new TreeMap<String,DecretDTO>();
        }
        return null;
    }

    public String setMesure(MesureApplicativeDTO mesureApplicativeDTO) throws ClientException {
        /* Il faut completement recharger la mesure et recréer le dto 
         * car les informations en base concernant ses decrets ont pu être modifiées via le bordereau des dossiers
         */
    	TexteMaitre texteMaitre = documentManager.getDocument(navigationContext.getCurrentDocument().getRef()).getAdapter(TexteMaitre.class);
        DocumentModel mesureDoc = documentManager.getDocument(new IdRef(mesureApplicativeDTO.getId()));
        currentMesure = new MesureApplicativeDTOImpl(mesureDoc.getAdapter(MesureApplicative.class), texteMaitre);
        
        scrollToDecrets();
        reloadDecrets(Boolean.FALSE);
        return null;
    }

    public String removeMesure(MesureApplicativeDTO mesureApplicativeDTO) throws ClientException {
        currentMesure = null;
        listMesure.remove(mesureApplicativeDTO);
        if (listMesure.isEmpty()) {
            listMesure.add(new MesureApplicativeDTOImpl());
        }
		journaliserActionPAN(SolonEpgEventConstant.SUPPR_MESURE_EVENT, SolonEpgEventConstant.SUPPR_MESURE_COMMENT);
        scrollToMesures();
        return reloadDecrets(Boolean.TRUE);
    }

    public Boolean isMesureLoaded() {
        return currentMesure != null;
    }

    public void setListMesure(List<MesureApplicativeDTO> listMesure) {
        this.listMesure = listMesure;
    }

    public List<MesureApplicativeDTO> getListMesure() throws ClientException {
        if (listMesure == null || currentTexteMaitre == null || !currentTexteMaitre.getId().equals(navigationContext.getCurrentDocument().getId())) {
            listMesure = mapMesure(navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class));
        }
        return listMesure;
    }

    public List<DecretDTO> getListDecret() {
    	return new ArrayList<DecretDTO>(mapDecret.values());
    }

    public void setListDecret(List<DecretDTO> listDecret) {
    	mapDecret.clear();
        for(DecretDTO decret :listDecret) {
        	mapDecret.put(decret.getNumeroNor(),decret);
        }
    }

    public String addNewMesure() {
    	for (int i=0; i < nombreMesuresToAdd; i++) {
	    	MesureApplicativeDTO temp = new MesureApplicativeDTOImpl();
	    	temp.setValidate(true);
	        listMesure.add(temp);
    	}
		// Ajout dans le journal du PAN
		String comment = nombreMesuresToAdd == 1 ? SolonEpgEventConstant.AJOUT_MESURE_COMMENT
				: nombreMesuresToAdd > 1 ? "Ajout de " + nombreMesuresToAdd + " mesures" : null;
		if (comment != null) {
			try {
				journaliserActionPAN(SolonEpgEventConstant.AJOUT_MESURE_EVENT, comment);
			} catch (Exception e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_AJOUT_JOURNAL_PAN, e);
			}
		}
        scrollToMesures();
        return null;
    }

    public void setNewDecret(String newDecret) {
        this.newDecret = newDecret;
    }

    public String getNewDecret() {
        return newDecret;
    }

    public String addNewDecret() throws ClientException {
    	scrollToDecrets();
    	final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();
		String message;
        if (StringUtil.isNotEmpty(newDecret)) {
        	if (!mapDecret.containsKey(newDecret)) {
	            try {
	                Dossier dossier = panService.checkIsDecretFromNumeroNOR(newDecret, documentManager);
	                if (dossier != null) {
	
	                    // recherche si le decret existe deja pour pas le recréer
	                    ActiviteNormative activiteNormative = panService.findActiviteNormativeByNor(newDecret, documentManager);
	                    DecretDTO decretToSave;
	                    if (activiteNormative != null) {
	                        Decret decret = activiteNormative.getDocument().getAdapter(Decret.class);
	                        decretToSave = new DecretDTOImpl(decret, dossier, currentMesure, navigationContext.getCurrentDocument().getAdapter(
	                                TexteMaitre.class));
	                     // m154544 : la case doit être cochées par défaut
	                        decretToSave.setValidate(true);
	                        mapDecret.put(decretToSave.getNumeroNor(), decretToSave);
	                    } else {
	                    	decretToSave = new DecretDTOImpl(newDecret, dossier, currentMesure, navigationContext.getCurrentDocument().getAdapter(
	                                TexteMaitre.class));
	                    	// m154544 : la case doit être cochées par défaut
	                    	decretToSave.setValidate(true);
	                    	mapDecret.put(decretToSave.getNumeroNor(), decretToSave);
	                    }
						// Ajout dans le journal du PAN
						journaliserActionPAN(SolonEpgEventConstant.AJOUT_DECRET_APP_EVENT,
								SolonEpgEventConstant.AJOUT_DECRET_APP_COMMENT + " [" + newDecret + "]");
	                }
	            } catch (ActiviteNormativeException e) {
	                message = resourcesAccessor.getMessages().get(e.getMessage());
	                facesMessages.add(StatusMessage.Severity.WARN, message);
	            }
        	} else {
        		message = resourcesAccessor.getMessages().get("activite.normative.decret.existant");
        		facesMessages.add(StatusMessage.Severity.WARN, message);
        	}
        } else {
        	message = resourcesAccessor.getMessages().get("activite.normative.dossier.not.found");
            facesMessages.add(StatusMessage.Severity.WARN, message);
        }
        newDecret = null;
        return null;
    }

    public String saveMesure() throws ClientException {
        final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();
    	DocumentModel doc = documentManager.getDocument(navigationContext.getCurrentDocument().getRef());
        ActiviteNormative activiteNormative = doc.getAdapter(ActiviteNormative.class);

        try {
            panService.saveMesure(activiteNormative, listMesure, documentManager);
            // on rafraichit le tableau maitre et la content view des mesures
            contentViewActions.refreshOnSeamEvent(EVENT_MESURES_ADDED);
        } catch (ActiviteNormativeException e) {
            if (e.getErrors() == null || e.getErrors().isEmpty()) {
                String message = resourcesAccessor.getMessages().get("activite.normative.error.save.after.mesure");
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

        // Save to html file
        panService.generateANRepartitionMinistereHtml(documentManager, activiteNormative,true);

        scrollToMesures();
        reloadMesures(Boolean.TRUE);
        return null;
    }

    public String saveDecret() throws ClientException {
        if (currentMesure != null) {
            String idCurrentMesure = currentMesure.getId();
            if (StringUtil.isEmpty(idCurrentMesure)) {
                String message = resourcesAccessor.getMessages().get("activite.normative.error.save.before.decret");
                facesMessages.add(StatusMessage.Severity.WARN, message);
                return null;
            }

            try {
            	List<DecretDTO> lstDecret = getListDecret();
				SolonEpgServiceLocator.getActiviteNormativeService().saveDecrets(idCurrentMesure, lstDecret,
						currentTexteMaitre.getId(), documentManager);

                for (MesureApplicativeDTO mesureApplicativeDTO : listMesure) {
                    if (idCurrentMesure.equals(mesureApplicativeDTO.getId())) {
                        mesureApplicativeDTO.setDecretIds(documentManager.getDocument(new IdRef(idCurrentMesure)).getAdapter(MesureApplicative.class)
                                .getDecretIds());
                        currentMesure = mesureApplicativeDTO;
                    }
                }
                currentMesure.setDecretIdsInvalidated(null);
                scrollToDecrets();
                reloadDecrets(Boolean.FALSE);

            } catch (ActiviteNormativeException e) {
                String message = resourcesAccessor.getMessages().get("activite.normative.error.save.after.decret");
                if (e.getErrors() == null || e.getErrors().isEmpty()) {
                    facesMessages.add(StatusMessage.Severity.WARN, message);
                } else {
                	message = resourcesAccessor.getMessages().get("activite.normative.error.save.mauvais.decret");
                	StringBuilder nors = new StringBuilder(StringUtil.join(e.getErrors(), ", ", "")); 
                    facesMessages.add(StatusMessage.Severity.WARN, message + nors.toString());
                }
                return null;
            }
        }
        return null;
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

    public String removeDecret(DecretDTO decretDTO) {
        mapDecret.remove(decretDTO.getNumeroNor());
        scrollToDecrets();
		// Ajout dans le journal du PAN
		try {
			journaliserActionPAN(SolonEpgEventConstant.SUPPR_DECRET_APP_EVENT,
					SolonEpgEventConstant.SUPPR_DECRET_APP_COMMENT + " [" + decretDTO.getNumeroNor() + "]");
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

        if (dto instanceof MesureApplicativeDTO) {
            MesureApplicativeDTO mesureApplicativeDTO = (MesureApplicativeDTO) dto;
            if (mesureApplicativeDTO.getId() != null) {
            	final ActiviteNormativeService actNormS = SolonEpgServiceLocator.getActiviteNormativeService();
            	DocumentModel mesureDoc = documentManager.getDocument(new IdRef(mesureApplicativeDTO.getId()));
            	MesureApplicative mesure = mesureDoc.getAdapter(MesureApplicative.class);
            	
            	if (!mesureApplicativeDTO.hasValidation() || !actNormS.checkDecretsValidationForMesure(documentManager, mesure)) {
            		return "dataRowRetourPourModification";
            	}
            }
        }

        if (dto instanceof DecretDTO) {
            DecretDTO decretDTO = (DecretDTO) dto;
            if (decretDTO.getId() != null && (!decretDTO.hasValidation() || !decretDTO.hasValidationLink())) {
                return "dataRowRetourPourModification";
            }
        }

        if (currentMesure != null) {
            if (currentMesure.getId() == null) {
                return null;
            } else if (currentMesure.getId().equals(dto.getDocIdForSelection())) {
                return "dataRowSelected";
            }
        }
        return defaultClass;
    }

    /**
     * navigation vers un dossier avec reset du texteMaître
     * 
     * @param dossierDoc
     * @return
     * @throws ClientException
     */
    public String navigateToActiviteNormative(DocumentModel activiteNormativeDoc) throws ClientException {
    	this.scrollToTexteMaitre();
        transpositionDirectiveActions.scrollToTexteMaitre();
        texteMaitreHabilitationActions.scrollToTexteMaitre();
        texteMaitreOrdonnanceActions.scrollToTexteMaitre();
        reset();
        dossierListingActions.navigateToDossier(activiteNormativeDoc);
        activiteNormativeActions.resetCurrentSubTabAction();
        navigationContext.setCurrentDocument(activiteNormativeDoc);
        return null;
    }

    private void reset() throws ClientException {
        navigationContext.setCurrentDocument(null);
        currentTexteMaitre = null;
        currentMesure = null;
        mapDecret = null;
        listMesure = null;
        currentMinistere = null;
        // reset suivi des habilitations
        texteMaitreHabilitationActions.reset();
        // reset suivi des orodonnaces
        texteMaitreOrdonnanceActions.reset();
        // reset traite et accords
        texteMaitreTraitesActions.reset();
        // reset transposition
        transpositionDirectiveActions.reset();
    }

    /**
     * navigation vers un ministere avec reset du texteMaître
     * 
     * @param dossierDoc
     * @return
     * @throws ClientException
     */
    public String navigateToMinistere(String idMin) throws ClientException {
        reset();
        if (idMin != null) {
            currentMinistere = STServiceLocator.getSTMinisteresService().getEntiteNode(idMin);
            activiteNormativeActions.resetCurrentSubTabAction();
        }
        return null;
    }

    public Boolean isMinistereLoaded() {
        return currentMinistere != null;
    }

    public Boolean isDocumentLoaded() {
        return navigationContext.getCurrentDocument() != null;
    }

    public OrganigrammeNode getCurrentMinistere() {
        return currentMinistere;
    }

    public void setCurrentMinistere(OrganigrammeNode currentMinistere) {
        this.currentMinistere = currentMinistere;
    }

    public void setCurrentEditMesure(MesureApplicativeDTO currentEditMesure) {
        this.currentEditMesure = currentEditMesure;
    }

    public MesureApplicativeDTO getCurrentEditMesure() {
        return currentEditMesure;
    }

    public String computeLegifranceLink(String nor) {
        return SolonEpgServiceLocator.getActiviteNormativeService().createLienJORFLegifrance(nor);
    }

    public String getTitreDivMesure() {
    	return getTitreDivMesure("Loi");
    }

    public String getTitreDivMesureAppOrdo() {
        return getTitreDivMesure("Ordonnance");
    }

    private String getTitreDivMesure(String type) {
    	DocumentModel doc = navigationContext.getCurrentDocument();
        StringBuilder queryBuilder = new StringBuilder(resourcesAccessor.getMessages().get("activite.normative.mesures"));
        if (doc != null) {
            TexteMaitre texteMaitre = doc.getAdapter(TexteMaitre.class);
            queryBuilder.append(" (").append(type).append(" N°");
            queryBuilder.append(texteMaitre.getNumero());
            queryBuilder.append(")");
        }
        return queryBuilder.toString();
    }

    public String getTitreDivDecret() {
    	return getTitreDivDecret("Loi");
    }

    public String getTitreDivDecretAppOrdo() {
        return getTitreDivDecret("Ordonnance");
    }

    private String getTitreDivDecret(String type) {
    	DocumentModel doc = navigationContext.getCurrentDocument();
        StringBuilder queryBuilder = new StringBuilder(resourcesAccessor.getMessages().get("activite.normative.decrets"));
        if (doc != null) {
            TexteMaitre texteMaitre = doc.getAdapter(TexteMaitre.class);
            queryBuilder.append(" (").append(type).append(" N°");
            queryBuilder.append(texteMaitre.getNumero());
            if (currentMesure != null) {
                queryBuilder.append(" - Mesure " + currentMesure.getNumeroOrdre());
            }
            queryBuilder.append(")");

        }
        return queryBuilder.toString();
    }

	public Integer getNombreMesuresToAdd() {
		return nombreMesuresToAdd;
	}

	public void setNombreMesuresToAdd(Integer nombreMesuresToAdd) {
		this.nombreMesuresToAdd = nombreMesuresToAdd;
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
	
	public void scrollToMesures() {
		setScrollTo("anormativemesure");
	}
	
	public void scrollToDecrets() {
		setScrollTo("anormativedecret");
	}

	public void validerMesure() {
		String numeroOrdre = currentEditMesure.getNumeroOrdre();
		setCurrentEditMesure(null);
		String comment = SolonEpgEventConstant.MODIF_MESURE_COMMENT
				.concat(numeroOrdre != null && !numeroOrdre.equals("") ? " [" + numeroOrdre + "]" : " []");
		try {
			journaliserActionPAN(SolonEpgEventConstant.MODIF_MESURE_EVENT, comment);
		} catch (Exception e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_AJOUT_JOURNAL_PAN, e);
		}
	}

	public void journaliserActionPAN(String event, String comment) throws ClientException {
		DocumentModel currentDoc = navigationContext.getCurrentDocument();
		DocumentModel dossierDoc;
		dossierDoc = SolonEpgServiceLocator.getNORService().getDossierFromNOR(documentManager,
				currentDoc.getAdapter(TexteMaitre.class).getNumeroNor());
		String category = null;
		if (currentDoc.getAdapter(ActiviteNormative.class).getApplicationLoi().equals("1"))
			category = SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_LOIS;
		else if (currentDoc.getAdapter(ActiviteNormative.class).getApplicationOrdonnance().equals("1"))
			category = SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_ORDO;
		if (category != null)
			STServiceLocator.getJournalService().journaliserActionPAN(documentManager, dossierDoc.getRef().toString(),
					event, comment, category);
	}
}
