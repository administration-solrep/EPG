package fr.dila.solonepg.web.activitenormative;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreTraiteDTO;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreTraiteDTO.DecretTraite;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreTraiteDTO.RatificationTraite;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.dossier.DossierListingActionsBean;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean Seam de gestion du texte maitre (Traite et Accords) de l'activite normative
 * 
 * @author asatre
 */
@Name("texteMaitreTraitesActions")
@Scope(ScopeType.CONVERSATION)
public class TexteMaitreTraitesActionsBean implements Serializable {


    private static final long serialVersionUID = 2706054441902071834L;

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
    
    private TexteMaitreTraiteDTO currentTexteMaitre;
    
    // currentRatification
    // currentDecret

    private Map<String, Boolean> mapToogle;

    @PostConstruct
    public void initialize() {
        clearPanneaux();
    }

    /**
     * Place tous les panneaux déroulable en mode ouvert.
     */
    public void clearPanneaux() {
        mapToogle = new HashMap<String, Boolean>();
        mapToogle.put("maitre", Boolean.TRUE);
        mapToogle.put("creation", Boolean.FALSE);
        
    }

    public String saveTexteMaître() throws ClientException {

        TexteMaitre texteMaitre = navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class);
        DocumentModel doc = null;
        try {
            doc = currentTexteMaitre.remapField(texteMaitre, documentManager);
            doc = SolonEpgServiceLocator.getActiviteNormativeService().saveTexteMaitre(doc.getAdapter(TexteMaitre.class), documentManager);

            // sauvegarde du decret de publication
            DocumentModel decretDoc = currentTexteMaitre.remapDecret(documentManager);
            if(decretDoc != null) {
                SolonEpgServiceLocator.getActiviteNormativeService().saveTexteMaitre(decretDoc.getAdapter(TexteMaitre.class), documentManager);
				STServiceLocator.getJournalService().journaliserActionPAN(documentManager, null,
						SolonEpgEventConstant.MODIF_DECRET_PUB_EVENT,
						SolonEpgEventConstant.MODIF_DECRET_PUB_COMMENT + " [" + currentTexteMaitre.getTitreActe() + "]",
						SolonEpgEventConstant.CATEGORY_LOG_PAN_TRAITES_ACCORD);
            }
            
            // sauvegarde de la loi de ratification
            DocumentModel ratificationDoc = currentTexteMaitre.remapRatification(documentManager);
            if(ratificationDoc != null) {
                SolonEpgServiceLocator.getActiviteNormativeService().saveTexteMaitre(ratificationDoc.getAdapter(TexteMaitre.class), documentManager);
				STServiceLocator.getJournalService().journaliserActionPAN(documentManager, null,
						SolonEpgEventConstant.MODIF_RATIF_EVENT,
						SolonEpgEventConstant.MODIF_RATIF_COMMENT + " [" + currentTexteMaitre.getTitreActe() + "]",
						SolonEpgEventConstant.CATEGORY_LOG_PAN_TRAITES_ACCORD);
            }
            
            navigationContext.setCurrentDocument(null);
            navigationContext.setCurrentDocument(doc);

        } catch (ClientException e) {
            String message = resourcesAccessor.getMessages().get(e.getMessage());
            facesMessages.add(StatusMessage.Severity.WARN, message);
            return null;
        }

        
        currentTexteMaitre = new TexteMaitreTraiteDTO(doc.getAdapter(ActiviteNormative.class), documentManager);

        Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		STServiceLocator.getJournalService().journaliserActionPAN(documentManager, null,
				SolonEpgEventConstant.MODIF_TM_EVENT,
				SolonEpgEventConstant.MODIF_TM_COMMENT + " [" + currentTexteMaitre.getTitreActe() + "]",
				SolonEpgEventConstant.CATEGORY_LOG_PAN_TRAITES_ACCORD);

        return null;
    }

    public String reloadTexteMaître() throws ClientException {
        navigationContext.setCurrentDocument(documentManager.getDocument(navigationContext.getCurrentDocument().getRef()));

        currentTexteMaitre.refresh(documentManager);

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

    public void reset() {
        this.currentTexteMaitre = null;
    }

    public void setCurrentTexteMaitre(TexteMaitreTraiteDTO currentTexteMaitre) {
        this.currentTexteMaitre = currentTexteMaitre;
    }

    public TexteMaitreTraiteDTO getCurrentTexteMaitre() {
        if (currentTexteMaitre == null || !currentTexteMaitre.getId().equals(navigationContext.getCurrentDocument().getId())) {
            ActiviteNormative activiteNormative = navigationContext.getCurrentDocument().getAdapter(ActiviteNormative.class);
            try {
                currentTexteMaitre = new TexteMaitreTraiteDTO(activiteNormative, documentManager);
            } catch (ClientException e) {
                String message = resourcesAccessor.getMessages().get(e.getMessage());
                facesMessages.add(StatusMessage.Severity.WARN, message);
                return null;
            }
        }
        return currentTexteMaitre;
    }
    
    public RatificationTraite getCurrentRatification(){
        if (currentTexteMaitre == null || !currentTexteMaitre.getId().equals(navigationContext.getCurrentDocument().getId())) {
            getCurrentTexteMaitre();
        }
        return currentTexteMaitre.getRatificationTraite();
    }
    
    public DecretTraite getCurrentDecret(){
        if (currentTexteMaitre == null || !currentTexteMaitre.getId().equals(navigationContext.getCurrentDocument().getId())) {
            getCurrentTexteMaitre();
        }
        return currentTexteMaitre.getDecretTraite();
                
    }
    

}
