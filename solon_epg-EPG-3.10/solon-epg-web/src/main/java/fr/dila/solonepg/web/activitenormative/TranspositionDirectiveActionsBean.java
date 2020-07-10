package fr.dila.solonepg.web.activitenormative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteTransposition;
import fr.dila.solonepg.api.cases.TranspositionDirective;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.dto.TexteTranspositionDTO;
import fr.dila.solonepg.api.dto.TranspositionDirectiveDTO;
import fr.dila.solonepg.api.exception.ActiviteNormativeException;
import fr.dila.solonepg.core.cases.ActiviteNormativeProgrammationImpl;
import fr.dila.solonepg.core.dto.activitenormative.TexteTranspositionDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.TranspositionDirectiveDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean Seam de gestion des transposition de directive européenne de l'espace d'activite normative
 * 
 * @author asatre
 */
@Name("transpositionDirectiveActions")
@Scope(ScopeType.CONVERSATION)
public class TranspositionDirectiveActionsBean implements Serializable {

    private static final long serialVersionUID = 4443670764370283782L;

    private static final Log log = LogFactory.getLog(ActiviteNormativeActionsBean.class);

    private Map<String, Boolean> mapToogle;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

	@In(create = true, required = false)
	protected transient ResourcesAccessor resourcesAccessor;

	private static final STLogger LOGGER = STLogFactory.getLog(ActiviteNormativeProgrammationImpl.class);

    private TranspositionDirectiveDTO currentTranspositionDTO;

    private List<TexteTranspositionDTO> listTexteTranspositionDTO;

    private String newText;
    
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
        mapToogle.put("texte_maitre", Boolean.TRUE);
        mapToogle.put("texte_transposition", Boolean.TRUE);
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

    public String reloadTransposition() {
    	scrollToTexteMaitre();
        try {
            TranspositionDirective transpositionDirective = SolonEpgServiceLocator.getTranspositionDirectiveService().reloadTransposition(
                    navigationContext.getCurrentDocument().getAdapter(TranspositionDirective.class), documentManager);
            currentTranspositionDTO = null;
            navigationContext.resetCurrentDocument();
            navigationContext.setCurrentDocument(transpositionDirective.getDocument());
            getCurrentTranspositionDTO();
        } catch (ClientException e) {
            log.error("Erreur lors du rechargement de la transposition de directive europeenne", e);
            TransactionHelper.setTransactionRollbackOnly();
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
        }

        return null;
    }

    public String saveTransposition() {
    	scrollToTexteMaitre();
        TranspositionDirective transpositionDirective = currentTranspositionDTO.remapField(navigationContext.getCurrentDocument().getAdapter(
                TranspositionDirective.class));
        try {
            SolonEpgServiceLocator.getTranspositionDirectiveService().updateTranspositionDirectiveWithCheckUnique(documentManager,
                    transpositionDirective);
			// Ajout dans le journal du PAN
			STServiceLocator.getJournalService().journaliserActionPAN(documentManager, null,
					SolonEpgEventConstant.MODIF_TM_EVENT, SolonEpgEventConstant.MODIF_TM_COMMENT + " [" + transpositionDirective.getNumero() + "]",
					SolonEpgEventConstant.CATEGORY_LOG_PAN_TRANSPO_DIRECTIVES);
        } catch (ClientException e) {
            log.error("Erreur lors de la mise a jour dans la transposition de directive europeenne", e);
            TransactionHelper.setTransactionRollbackOnly();
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
        }

        return null;
    }

    public void setCurrentTranspositionDTO(TranspositionDirectiveDTO currentTranspositionDTO) {
        this.currentTranspositionDTO = currentTranspositionDTO;
    }

    public TranspositionDirectiveDTO getCurrentTranspositionDTO() {
        if (currentTranspositionDTO == null || !currentTranspositionDTO.getId().equals(navigationContext.getCurrentDocument().getId())) {
            TranspositionDirective transpositionDirective = navigationContext.getCurrentDocument().getAdapter(TranspositionDirective.class);
            currentTranspositionDTO = new TranspositionDirectiveDTOImpl(transpositionDirective);

            // fetch texteTransposition
            try {
                listTexteTranspositionDTO = new ArrayList<TexteTranspositionDTO>();

                List<TexteTransposition> listTexteTransposition = SolonEpgServiceLocator.getTranspositionDirectiveService().fetchTexteTransposition(
                        transpositionDirective, documentManager);
                for (TexteTransposition texteTransposition : listTexteTransposition) {
                    listTexteTranspositionDTO.add(new TexteTranspositionDTOImpl(texteTransposition));
                }

                if (listTexteTranspositionDTO.isEmpty()) {
                    listTexteTranspositionDTO.add(new TexteTranspositionDTOImpl(""));
                }

            } catch (ClientException e) {
                log.error("Erreur lors de la recuperation des TexteTranspositions de la TranspositionDirective " + transpositionDirective.getNumero());
                TransactionHelper.setTransactionRollbackOnly();
                facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            }
        }

        return currentTranspositionDTO;
    }

    public void setNewText(String newText) {
        this.newText = newText;
    }

    public String getNewText() {
        return newText;
    }

    public String addNewText() {
    	scrollToTranspositions();
		try {
			Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager, newText);
			if (dossier == null) {
				throw new ActiviteNormativeException("activite.normative.dossier.not.found");
			}
		} catch (ClientException e) {
			String message = resourcesAccessor.getMessages().get(e.getMessage());
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return null;
		}
    	TexteTranspositionDTO tmp = new TexteTranspositionDTOImpl(newText);
    	tmp.setValidate(Boolean.TRUE);
        listTexteTranspositionDTO.add(tmp);
		// Ajout dans le journal du PAN
		try {
			STServiceLocator.getJournalService().journaliserActionPAN(documentManager, null,
					SolonEpgEventConstant.AJOUT_TXT_TRANSPO_EVENT,
					SolonEpgEventConstant.AJOUT_TXT_TRANSPO_COMMENT + " [" + newText + "] transposant la directive ["
							+ currentTranspositionDTO.getNumero() + "]",
					SolonEpgEventConstant.CATEGORY_LOG_PAN_TRANSPO_DIRECTIVES);
		} catch (Exception e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_AJOUT_JOURNAL_PAN, e);
		}
        return null;
    }

    public void setListTexteTranspositionDTO(List<TexteTranspositionDTO> listTexteTranspositionDTO) {
        this.listTexteTranspositionDTO = listTexteTranspositionDTO;
    }

    public List<TexteTranspositionDTO> getListTexteTranspositionDTO() {
        if (currentTranspositionDTO == null || !currentTranspositionDTO.getId().equals(navigationContext.getCurrentDocument().getId())) {
            getCurrentTranspositionDTO();
        }
        return listTexteTranspositionDTO;
    }

    public String removeTexteTransposition(TexteTranspositionDTO texteTranspositionDTO) {
    	scrollToTranspositions();
        listTexteTranspositionDTO.remove(texteTranspositionDTO);
		// Ajout dans le journal du PAN
		try {
			STServiceLocator.getJournalService().journaliserActionPAN(documentManager, null,
					SolonEpgEventConstant.SUPPR_TXT_TRANSPO_EVENT,
					SolonEpgEventConstant.SUPPR_TXT_TRANSPO_COMMENT + " [" + texteTranspositionDTO.getNumeroNor()
							+ "] transposant la directive [" + currentTranspositionDTO.getNumero() + "]",
					SolonEpgEventConstant.CATEGORY_LOG_PAN_TRANSPO_DIRECTIVES);
		} catch (Exception e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_AJOUT_JOURNAL_PAN, e);
		}
        return null;
    }

    public String saveTexteTransposition() {
    	scrollToTranspositions();
        try {
            TranspositionDirective transpositionDirective = navigationContext.getCurrentDocument().getAdapter(TranspositionDirective.class);
            SolonEpgServiceLocator.getTranspositionDirectiveService().saveTexteTranspositionDTO(transpositionDirective, listTexteTranspositionDTO,
                    documentManager);
            navigationContext.setCurrentDocument(transpositionDirective.getDocument());

            listTexteTranspositionDTO = new ArrayList<TexteTranspositionDTO>();

            List<TexteTransposition> listTexteTransposition = SolonEpgServiceLocator.getTranspositionDirectiveService().fetchTexteTransposition(
                    transpositionDirective, documentManager);
            for (TexteTransposition texteTransposition : listTexteTransposition) {
                listTexteTranspositionDTO.add(new TexteTranspositionDTOImpl(texteTransposition));
            }

        } catch (ClientException e) {
            log.error("Erreur lors de la sauvegarde des TexteTranspositions de la TranspositionDirective "
                    + navigationContext.getCurrentDocument().getAdapter(TranspositionDirective.class).getNumero());
            TransactionHelper.setTransactionRollbackOnly();
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
        }
        return null;
    }

    public String refreshTexteTransposition() {
    	scrollToTranspositions();
        try {
            SolonEpgServiceLocator.getTranspositionDirectiveService().refreshTexteTransposition(listTexteTranspositionDTO, documentManager);
        } catch (ClientException e) {
            log.error("Erreur lors du refresh des TexteTranspositions de la TranspositionDirective "
                    + navigationContext.getCurrentDocument().getAdapter(TranspositionDirective.class).getNumero());
            TransactionHelper.setTransactionRollbackOnly();
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
        }
        return null;
    }

    /**
     * Decoration de la ligne du texte transposant la directive
     * 
     * @param dto
     * @return
     * @throws ClientException
     */
    public String decorate(AbstractMapDTO dto, String defaultClass) throws ClientException {
        return defaultClass;
    }

    public void reset() {
        listTexteTranspositionDTO = null;
        currentTranspositionDTO = null;
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
	
	public void scrollToTranspositions() {
		setScrollTo("anormativedecret");
	}
}
