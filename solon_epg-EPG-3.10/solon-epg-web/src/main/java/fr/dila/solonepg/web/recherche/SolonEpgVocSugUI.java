package fr.dila.solonepg.web.recherche;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.web.suggestion.SolonEpgSuggestionUtils;
import fr.dila.st.web.suggestion.VocSugUI;

/**
 * Classe utilitaire pour chercher et récupérer les informations d'un vocabulaire suggéré dans l'UI  dans Solon EPG.
 * 
 * @author ARN
 * 
 */
public class SolonEpgVocSugUI extends VocSugUI {

    private static final Log log = LogFactory.getLog(SolonEpgVocSugUI.class);

    public SolonEpgVocSugUI(String vocId) {
        super(vocId);
    }
    
    /**
     * Nombre maximum de suggestions affichées pour les types d'actes.
     */
    protected static final int TYPE_ACTE_MAX_RESULT = 15;

    /**
     * Récupérer les suggestions sous forme de mots-clés à partir du texte <p>input</p> saisi des vocabulaire <p>vocabularyLabel</p>. 
     */
    @Override
    public List<String> getSuggestions(Object input) {
        log.info("Suggestions : " + vocabularyLabel);
        return SolonEpgSuggestionUtils.getSuggestions((String) input,MAX_RESULT,vocabularyLabel);
    }
    
    /**
     * Récupérer les suggestions sous forme de documentModel à partir du texte <p>input</p> saisi des vocabulaire <p>vocabularyLabel</p>. 
     */
    public List<DocumentModel> getDocumentModelListSuggestions(Object input) {
        log.info("Suggestions doc model : " + vocabularyLabel);
        Integer maxResult = MAX_RESULT;
        if(vocabularyLabel != null && VocabularyConstants.TYPE_ACTE_VOCABULARY.equals(vocabularyLabel)){
            //dans le cas d'un type d'acte, on affiche les 15 premiers résultats afin de pouvoir afficher tous les décrets.
            maxResult = TYPE_ACTE_MAX_RESULT;
        }
        return SolonEpgSuggestionUtils.getDocumentModelListSuggestions((String) input,maxResult,vocabularyLabel);
    }
    
}
