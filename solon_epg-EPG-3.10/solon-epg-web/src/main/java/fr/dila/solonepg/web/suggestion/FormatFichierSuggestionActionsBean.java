package fr.dila.solonepg.web.suggestion;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.recherche.SolonEpgVocSugUI;

/**
 * Bean JSF pour la suggestion de format de fichier.
 *
 * @author ARN
 */
@Name("formatFichierSuggestionActions")
@Scope(ScopeType.PAGE)
public class FormatFichierSuggestionActionsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    public DocumentModel getDocumentModel(String docId) throws ClientException {
        return documentManager.getDocument(new IdRef(docId));
    }
    
    /**
     * Propriétés associées au format de fichier
     */
    public static final String ID_FORMAT_FICHIER_KEY_NAME = "id";
    
    public static final String LABEL_FORMAT_FICHIER_KEY_NAME = "label";
    
//    /**
//     * Liste de suggestion du format de fichier. 
//     */
//    protected Map<String,SolonEpgVocSugUI> vocMap;

    /**
     * On récupère la liste de suggestion pour le format de fichier.
     * 
     * @author antoine Rolin
     * 
     * @return Map<String,SolonEpgVocSugUI>
     */
    @Factory(value = "formatFichierSuggMap")
    public Map<String,SolonEpgVocSugUI> getVocMap(){
        Map<String,SolonEpgVocSugUI> vocSuggMap = new HashMap<String,SolonEpgVocSugUI>();
        //récupération du nom du schéma vocabulaire du format de fichier
        String vocabularyName = VocabularyConstants.FORMAT_FICHIER;
        //création de la liste de suggestion associée
        vocSuggMap.put(vocabularyName,new SolonEpgVocSugUI(vocabularyName));
        return vocSuggMap;
    }
    
    /**
     * Retourne une map contenant les propriétés d'un format de fichier.
     * 
     * @param docId id du format de fichier
     * @return Propriétés
     * @throws ClientException ClientException
     */
    public Map<String, Object> getFormatFichierInfo(String docId) throws ClientException {
        Map<String, Object> res = new HashMap<String, Object>();
        //récupère l'identifiant du vocabulaire
        res.put(ID_FORMAT_FICHIER_KEY_NAME, docId);
        //on récupère le service de vocabularie de solon epg
        SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
        final String description = vocService.getLabelFromId(VocabularyConstants.FORMAT_FICHIER, docId, LABEL_FORMAT_FICHIER_KEY_NAME);
        //récupère le label  du vocabulaire
        res.put(LABEL_FORMAT_FICHIER_KEY_NAME, description);
        return res;
    }

}
