package fr.dila.solonepg.web.suggestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.platform.ui.web.directory.VocabularyEntry;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.recherche.SolonEpgVocSugUI;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.StringUtil;

/**
 * Bean JSF pour la suggestion de type d'actes.
 *
 * @author ARN
 */
@Name("typeActeSuggestionActions")
@Scope(ScopeType.PAGE)
public class TypeActeSuggestionActionsBean implements Serializable {

    /**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 4786093529895329365L;
	
	private static final STLogger LOGGER = STLogFactory.getLog(TypeActeSuggestionActionsBean.class);

	protected Map<String, String> vocabularyMap;
    
    protected List<String> curSelection = new ArrayList<String>();
    
    @In(create = true, required = true)
    protected transient CoreSession documentManager;    

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    public DocumentModel getDocumentModel(String id) throws ClientException {
        return documentManager.getDocument(new IdRef(id));
    }

    /**
     * On récupère la liste de suggestion pour le type d'acte.
     * 
     * @author antoine Rolin
     * 
     * @return Map<String,SolonEpgVocSugUI>
     */
    @Factory(value = "typeActeSuggMap")
    public Map<String,SolonEpgVocSugUI> getVocMap(){
        Map<String,SolonEpgVocSugUI> vocSuggMap = new HashMap<String,SolonEpgVocSugUI>();
        //récupération du nom du schéma vocabulaire du type d'acte
        String vocabularyName = VocabularyConstants.TYPE_ACTE_VOCABULARY;
        //création de la liste de suggestion associée
        vocSuggMap.put(vocabularyName,new SolonEpgVocSugUI(vocabularyName));
        return vocSuggMap;
    }

    /**
     * Retourne une map contenant les propriétés d'un vocabulaire.
     * 
     * @param id id du vocabulaire
     * @return Propriétés
     * @throws ClientException ClientException
     */
    public Map<String, String> getInfo(String id) throws ClientException {
        //on récupère le label 
        String label = getVocabularyMap().get(id);
        //récupère la map
        Map<String, String> res = new HashMap<String, String>();
        res.put(STVocabularyConstants.COLUMN_ID, id);
        res.put(STVocabularyConstants.COLUMN_LABEL, label);
        return res;
    }

    public Map<String, String> getVocabularyMap() throws ClientException {
        if (vocabularyMap == null) {
            //initialisation de la map contenant les valeurs du vocabulaire
            vocabularyMap = new HashMap<String, String>();
            //on récupère les vocabulaires du directory
            SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
            List<VocabularyEntry> voc = vocService.getVocabularyEntryListFromDirectory(VocabularyConstants.TYPE_ACTE_VOCABULARY, VocabularyConstants.VOCABULARY_TYPE_ACTE_SCHEMA);
            for (VocabularyEntry vocabularyEntry : voc) {
                vocabularyMap.put(vocabularyEntry.getId(), vocabularyEntry.getLabel());
            }
        }
        return vocabularyMap;
    }
    
    public void addActions(SolonEpgVocSugUI typeActe) {
    	if (typeActe != null) {
    		String label = typeActe.getMotCleLabel();
    		//On n'ajoute l'élément que si il n'est ni vide ni déjà présent
    		if (!StringUtil.isBlank(label) && !curSelection.contains(label)) {
    			curSelection.add(typeActe.getMotCleLabel());
    		}
    	}
    }
    
    private TableReference getTableReference() throws ClientException {
    	TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
		DocumentModel docTableReference = tableReferenceService.getTableReference(documentManager);
		
		return docTableReference.getAdapter(TableReference.class);
    }
    
    public List<String> getCurSelection() {
    	//Si on n'a rien dans curSelection c'est qu'on n'a probablement pas chargé les données
    	if (curSelection.isEmpty()) {
    		try {
	    		TableReference tableReference = getTableReference();
	    		for (String typeActe : tableReference.getTypesActe()) {
	    			curSelection.add(typeActe);
	    		}
    		} catch (ClientException exc) {
    			facesMessages.add(StatusMessage.Severity.ERROR, "Impossible de récupérer les données de la table de référence");
    			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_TABLE_REFERENCE_TEC, exc);
    		}
    	}
    	return curSelection;
    }
    
    public void removeTypeActeFromSelection(String typeActe) {
    	if (!StringUtil.isBlank(typeActe)) {
    		curSelection.remove(typeActe);
    	}
    }
}
