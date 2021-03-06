package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ui.web.directory.VocabularyEntry;

import fr.dila.st.api.service.VocabularyService;

/**
 * Interface du Service de vocabulaire de solon EPG
 * 
 * @author ARN
 */
public interface SolonEpgVocabularyService extends VocabularyService, Serializable {
    
    /**
     * Récupère la liste complète des VocabularyEntry d'un vocabulaire à partir du nom du directory.
     * 
     * @param vocabularyDirectoryName nom du directory
     * @param schemaVocabulaire schéma de vocabulaire utilisé (par défaut Vocabulary)
     * @return List<VocabularyEntry>
     */
    public List<VocabularyEntry> getVocabularyEntryListFromDirectory(String vocabularyDirectoryName,String schemaVocabulaire) throws ClientException;

    /**
     * Récupère la liste des VocabularyEntry d'un vocabulaire à partir du nom du directory et des identifiants des éléments.
     * 
     * @param vocabularyDirectoryName nom du directory
     * @param schemaVocabulaire schéma de vocabulaire utilisé (par défaut Vocabulary)
     * @param vocabularyEntryIds liste des identifiants de vocabulaires à afficher
     * @return List<VocabularyEntry>
     */
    public List<VocabularyEntry> getVocabularyEntryListFromDirectory(String vocabularyDirectoryName,String schemaVocabulaire,List<String> vocabularyEntryIds) throws ClientException;

    
    /**
     * Récupère la liste complète des documentModel vocabulaire à partir du nom du directory.
     * 
     * @param vocabularyDirectoryName
     * @param idValueList
     * @return List<DocumentModel>
     */
    public List<DocumentModel> getDocumentModelListFromDirectory(String vocabularyDirectoryName);

    /**
     * Récupère la liste complète des documentModel vocabulaire à partir du nom du directory à l'exception d'une liste d'éntrées mise en paramètre.
     * 
     * @param vocabularyDirectoryName nom du directory
     * @param idValueList identifiant des entrées à enlever de la liste
     * @return List<DocumentModel>
     */
    public List<DocumentModel> getDocumentModelListFromDirectoryExceptFromIdList(String vocabularyDirectoryName,List<String> idValueList);
    
    /**
     * Récupère une liste de document model à partir d'une liste d'identifiant, du nom de la colonne label et du nom du directory.
     * 
     * @param vocabularyDirectoryName
     * @param idValueList
     * @return List<DocumentModel>
     */
    public List<DocumentModel> getDocumentModelListFromIdsList(String vocabularyDirectoryName,List<String> idValueList);

    /**
     * Récupère un document model à partir d'un identifiant, du nom de la colonne label et du nom du directory.
     * 
     * @param vocabularyDirectoryName
     * @param idValue
     * @return DocumentModel
     */
    public DocumentModel getDocumentModelFromId(String vocabularyDirectoryName,String idValueList);

    
    /**
     * Récupère le identifiant du vocabulaire à partir de son label, du nom de la colone label et du nom du directory.
     * 
     * @param vocabularyDirectoryName
     * @param labelValue
     * @param labelColumName
     * @return
     */
    public String getIdFromLabel(String vocabularyDirectoryName,String labelValue,String labelColumName);

    
    /**
     * Ramene la liste des index commencant par keyword ( sur la colonne 'label')
     * 
     * @param keyword
     *            : la chaine de caractere de recherche.
     * @param vocList
     *            : la liste des vocabulaires sur lesquels on veut effectuer la recherche.
     * @return la liste des résultats commencant par keyword, sur le vocabulaire demande
     * 
     **/
    public List<String> getIndexList(String keyword, List<String> vocList);


    /**
     * Ramene la liste des index commencant par keyword ( sur la colonne 'label')
     * 
     * @param keyword
     *            : la chaine de caractere de recherche.
     * @param vocName
     *            : le nom du vocabulaire sur lequel on veut effectuer la recherche.
     * @return la liste des résultats commencant par keyword, sur le vocabulaire demande
     * 
     **/
    public List<String> getIndexList(String keyword, String vocName);
    
    /**
     * récupération de tous les vocabulaire de solonEPG
     */
    public List<String> getVocabularyList();

    /**
     * Récupère la liste complète des labels d'un vocabulaire à partir du nom du directory.
     * 
     * @param vocabularyDirectoryName nom du directory
     * @param schemaVocabulaire schéma de vocabulaire utilisé (par défaut Vocabulary)
     * @return la liste complète des labels d'un vocabulaire à partir du nom du directory.
     */
    public List<String> getLabelsFromDirectory(String vocabularyDirectoryName,String schemaVocabulaire) throws ClientException;

    /**
     * Récupère la liste complète des labels d'un vocabulaire à partir du nom du directory.
     * 
     * @param vocabularyDirectoryName nom du directory
     * @return la liste complète des labels d'un vocabulaire à partir du nom du directory.
     */
    public List<String> getLabelsFromDirectory(String vocabularyDirectoryName) throws ClientException;
    
}
