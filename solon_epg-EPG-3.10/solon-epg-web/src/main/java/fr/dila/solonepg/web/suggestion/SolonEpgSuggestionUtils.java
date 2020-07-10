package fr.dila.solonepg.web.suggestion;

import java.util.Arrays;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

/**
 * Classe Utilité pour les suggestions
 * @author jgomez
 *
 */

public class SolonEpgSuggestionUtils {

    /**
     * Exemple d'appel : getSuggestions("hel",10,"senatTheme","senatRubrique") retourne tous les mots-clés commençant par hel 
     * dans les vocabulaires senatTheme et senatRubrique
     * @param suggestion Les premières lettres du mot
     * @param maxResult La limitation du nombre de résultats
     * @param directoryNameList
     * @return
     */
    public static List<String> getSuggestions(String suggestion,int maxResult,String... directoryNameArrayList) {
        //on récupère le service de vocabularie de solon epg
        SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
        //transformation du tableau en liste
        List<String> directoryNameList = Arrays.asList(directoryNameArrayList);
        //récupération des suggestions à partir de la suggestion et des vocabulaires
        List<String> resultList =  vocService.getSuggestions(suggestion, directoryNameList);
        if (resultList.size() > maxResult) {
            resultList = resultList.subList(0, maxResult);
        }
        return resultList;
    }
    
    /**
     * Exemple d'appel : getSuggestions("hel",10,"type_acte") retourne la liste de documentModel dont tous les mots-clés commençant par hel 
     * 
     * @param suggestion Les premières lettres du mot
     * @param maxResult La limitation du nombre de résultats
     * @param directoryNameList
     * @return
     */
    public static List<DocumentModel> getDocumentModelListSuggestions(String suggestion,int maxResult,String... directoryNameArrayList) {
        //on récupère le service de vocabularie de solon epg
        SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
        //transformation du tableau en liste
        List<String> directoryNameList = Arrays.asList(directoryNameArrayList);
        //récupération des suggestions à partir de la suggestion et des vocabulaires
        List<DocumentModel> resultList =  vocService.getListDocumentModelSuggestions(suggestion, directoryNameList);
        if (resultList.size() > maxResult) {
            resultList = resultList.subList(0, maxResult);
        }
        return resultList;
    }
    
    private SolonEpgSuggestionUtils() {
     //Private default constructor
    }
}
