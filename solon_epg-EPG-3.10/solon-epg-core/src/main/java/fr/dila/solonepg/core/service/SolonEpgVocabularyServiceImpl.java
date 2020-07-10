/**
 * 
 */
package fr.dila.solonepg.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DataModel;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.directory.Directory;
import org.nuxeo.ecm.directory.DirectoryException;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.ecm.platform.ui.web.directory.VocabularyEntry;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.service.VocabularyServiceImpl;

/**
 * Implémentation du Service de vocabulaire de solon EPG
 * 
 * @author arolin
 * @author jgomez
 * 
 */
public class SolonEpgVocabularyServiceImpl extends VocabularyServiceImpl implements SolonEpgVocabularyService {

    private static final long serialVersionUID = 1L;

    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(VocabularyService.class);

    @Override
    public List<String> getVocabularyList() {
        List<String> vocList = new ArrayList<String>();
        // TODO add solon epg vocabulary
        vocList.add(VocabularyConstants.TYPE_ACTE_VOCABULARY);
        vocList.add(VocabularyConstants.FORMAT_FICHIER);
        return vocList;
    }

    @Override
    public boolean checkData(String vocabularyDirectoryName, String columName, String valueToCheck) {
        final DirectoryService directoryService = STServiceLocator.getDirectoryService();
        log.debug("VocabularyService checkData : " + vocabularyDirectoryName + " / " + columName + " / " + valueToCheck);
        Session vocabularySession = null;
        try {
            Directory directory = directoryService.getDirectory(vocabularyDirectoryName);
            if (directory != null) {
                String schemaName = directory.getSchema();
                vocabularySession = directoryService.open(vocabularyDirectoryName);
                DocumentModelList listEntries = vocabularySession.getEntries();
                Object valueVocabulary = null;
                int nbLine = listEntries.size();
                for (int i = 0; i < nbLine; i++) {
                    DocumentModel documentModel = listEntries.get(i);
                    valueVocabulary = PropertyUtil.getProperty(documentModel,schemaName, columName);
                    if (valueVocabulary != null && valueVocabulary.equals(valueToCheck)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Exception in check data", e);
        } finally {
            if (vocabularySession != null) {
                try {
                    vocabularySession.close();
                } catch (DirectoryException e) {
                    log.warn("erreur lors de la fermeture du directory", e);
                }
            }
        }
        return false;
    }

    @Override
    public List<DocumentModel> getDocumentModelListFromDirectory(String vocabularyDirectoryName) {
        final DirectoryService directoryService = STServiceLocator.getDirectoryService();
        List<DocumentModel> vocabularyDocumentModelList = new ArrayList<DocumentModel>();
        Session vocabularySession = null;
        try {
            vocabularySession = directoryService.open(vocabularyDirectoryName);
            vocabularyDocumentModelList = vocabularySession.getEntries();
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du label du vocbulaire à partir de son identifiant", e);
        } finally {
            if (vocabularySession != null) {
                try {
                    vocabularySession.close();
                } catch (DirectoryException e) {
                    // do nothing
                    log.warn("erreur lors de la fermeture du directory", e);
                }
            }
        }
        return vocabularyDocumentModelList;
    }

    @Override
    public List<VocabularyEntry> getVocabularyEntryListFromDirectory(String vocabularyDirectoryName, String schemaVocabulaire) throws ClientException {
        if(schemaVocabulaire==null || schemaVocabulaire.isEmpty()){
            schemaVocabulaire = STVocabularyConstants.VOCABULARY;
        }
        List<DocumentModel> docList =  getDocumentModelListFromDirectory(vocabularyDirectoryName);
        List<VocabularyEntry> vocEntryList = new ArrayList<VocabularyEntry>();
        for (DocumentModel docModel : docList) {
            vocEntryList.add(new VocabularyEntry(PropertyUtil.getStringProperty(docModel,schemaVocabulaire, STVocabularyConstants.COLUMN_ID), PropertyUtil.getStringProperty(docModel,schemaVocabulaire, STVocabularyConstants.COLUMN_LABEL)));
        }
        return vocEntryList;
    }
    
    @Override
    public List<VocabularyEntry> getVocabularyEntryListFromDirectory(String vocabularyDirectoryName, String schemaVocabulaire, List<String> vocabularyEntryIds) throws ClientException {
        if(schemaVocabulaire==null || schemaVocabulaire.isEmpty()){
            schemaVocabulaire = STVocabularyConstants.VOCABULARY;
        }
        List<DocumentModel> docList =  getDocumentModelListFromIdsList(vocabularyDirectoryName,vocabularyEntryIds);
        List<VocabularyEntry> vocEntryList = new ArrayList<VocabularyEntry>();
        if(docList != null && docList.size()>0){
            for (DocumentModel docModel : docList) {
                vocEntryList.add(new VocabularyEntry(PropertyUtil.getStringProperty(docModel,schemaVocabulaire, STVocabularyConstants.COLUMN_ID), PropertyUtil.getStringProperty(docModel,schemaVocabulaire, STVocabularyConstants.COLUMN_LABEL)));
            }
        }
        return vocEntryList;
    }
    
    @Override
    public List<DocumentModel> getDocumentModelListFromDirectoryExceptFromIdList(String vocabularyDirectoryName, List<String> idValueList) {
        final DirectoryService directoryService = STServiceLocator.getDirectoryService();
        // on récupère tous les entrés du vocabulaires sous forme de liste de document model
        List<DocumentModel> vocabularyDocumentModelList = getDocumentModelListFromDirectory(vocabularyDirectoryName);
        if (idValueList == null || idValueList.size() < 1) {
            return vocabularyDocumentModelList;
        }
        Session vocabularySession = null;
        try {
            vocabularySession = directoryService.open(vocabularyDirectoryName);
            // pour chaque colonnes, on parcourt les documents model
            // TODO optimize
            for (String key : idValueList) {
                List<DocumentModel> tempVocabularyDocumentModelList = new ArrayList<DocumentModel>();
                tempVocabularyDocumentModelList.addAll(vocabularyDocumentModelList);
                for (DocumentModel vocabularyDoc : tempVocabularyDocumentModelList) {
                    // on parcourt
                    if (key.equals(PropertyUtil.getStringProperty(vocabularyDoc,STVocabularyConstants.VOCABULARY, STVocabularyConstants.COLUMN_ID))) {
                        vocabularyDocumentModelList.remove(vocabularyDoc);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du label du vocbulaire à partir de son identifiant", e);
        } finally {
            if (vocabularySession != null) {
                try {
                    vocabularySession.close();
                } catch (DirectoryException e) {
                    // do nothing
                    log.warn("erreur lors de la fermeture du directory", e);
                }
            }
        }
        return vocabularyDocumentModelList;
    }

    @Override
    public List<DocumentModel> getDocumentModelListFromIdsList(String vocabularyDirectoryName, List<String> idValueList) {
        final DirectoryService directoryService = STServiceLocator.getDirectoryService();
        log.debug("VocabularyService getDocumentModelListFromIdsList : " + vocabularyDirectoryName + " / " + idValueList);
        List<DocumentModel> vocabularyDocumentModelList = new ArrayList<DocumentModel>();
        Session vocabularySession = null;
        if (idValueList == null || idValueList.size() < 1) {
            return null;
        }
        try {
            vocabularySession = directoryService.open(vocabularyDirectoryName);
            for (String idValue : idValueList) {
                DocumentModel documentModel = vocabularySession.getEntry(idValue);
                if (documentModel != null) {
                    vocabularyDocumentModelList.add(documentModel);
                }
            }
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du label du vocbulaire à partir de son identifiant", e);
        } finally {
            if (vocabularySession != null) {
                try {
                    vocabularySession.close();
                } catch (DirectoryException e) {
                    // do nothing
                    log.warn("erreur lors de la fermeture du directory", e);
                }
            }
        }
        return vocabularyDocumentModelList;
    }

    @Override
    public DocumentModel getDocumentModelFromId(String vocabularyDirectoryName, String idValue) {
        final DirectoryService directoryService = STServiceLocator.getDirectoryService();
        log.debug("VocabularyService getDocumentModelListFromIdsList : " + vocabularyDirectoryName + " / " + idValue);
        Session vocabularySession = null;
        if (idValue == null || idValue.isEmpty()) {
            return null;
        }
        try {
            vocabularySession = directoryService.open(vocabularyDirectoryName);
            DocumentModel documentModel = vocabularySession.getEntry(idValue);
            return documentModel;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du label du vocbulaire à partir de son identifiant", e);
        } finally {
            if (vocabularySession != null) {
                try {
                    vocabularySession.close();
                } catch (DirectoryException e) {
                    // do nothing
                    log.warn("erreur lors de la fermeture du directory", e);
                }
            }
        }
        return null;
    }

    @Override
    public String getIdFromLabel(String vocabularyDirectoryName, String labelValue, String labelColumName) {
        final DirectoryService directoryService = STServiceLocator.getDirectoryService();
        log.debug("VocabularyService getLabelFromId : " + vocabularyDirectoryName + " / " + labelValue + " / " + labelColumName);
        Session vocabularySession = null;
        String valueVocabulary = null;
        try {
            // récupération du directory contenant le vocabulaire
            Directory directory = directoryService.getDirectory(vocabularyDirectoryName);
            if (directory != null) {
                String schemaName = directory.getSchema();
                // connexion au directory du vocabulaire
                vocabularySession = directoryService.open(vocabularyDirectoryName);
                // récupération des entrées du vocabulaire
                DocumentModelList vocabularyEntriesList = vocabularySession.getEntries();
                String vocabularyLabeIterator;
                for (DocumentModel vocabularyIterator : vocabularyEntriesList) {
                    vocabularyLabeIterator = PropertyUtil.getStringProperty(vocabularyIterator, schemaName, STVocabularyConstants.COLUMN_LABEL);
                    if (vocabularyLabeIterator != null && vocabularyLabeIterator.equals(labelValue)) {
                        valueVocabulary = PropertyUtil.getStringProperty(vocabularyIterator, schemaName, STVocabularyConstants.COLUMN_ID);
                        return valueVocabulary;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de l'id du vocabulaire à partir de son label", e);
        } finally {
            if (vocabularySession != null) {
                try {
                    vocabularySession.close();
                } catch (DirectoryException e) {
                    log.warn("erreur lors de la fermeture du directory", e);
                }
            }
        }
        return valueVocabulary;
    }

    /**
     * Ramene la liste des index commencant par keyword ( sur la colonne 'label')
     * 
     * @param keyword
     *            : la chaine de caractere de recherche.
     * @param dirSession
     *            : la session du directory sur lequel on veut effectuer la recherche.
     * @return la liste des résultats commencant par keyword, sur le vocabulaire demande
     **/
    private List<String> getIndexList(String keyword, Session dirSession, String schemaName) throws DirectoryException, ClientException {
        HashSet<String> keywordSet = new HashSet<String>();
        keywordSet.add(STVocabularyConstants.COLUMN_LABEL);
        Map<String, Serializable> filter = new HashMap<String, Serializable>();
        filter.put(STVocabularyConstants.COLUMN_LABEL, keyword);
        DocumentModelList docListResult = dirSession.query(filter, keywordSet);
        List<String> resultList = new ArrayList<String>();
        String label = "";
        for (DocumentModel doc : docListResult) {
            if (schemaName.equals(STVocabularyConstants.VOCABULARY) || schemaName.equals(STVocabularyConstants.XVOCABULARY)) {
                DataModel mod = doc.getDataModel(schemaName);
                label = (String) mod.getData(STVocabularyConstants.COLUMN_LABEL);
                resultList.add(label);
            }
        }
        return resultList;
    }

    @Override
    public List<String> getIndexList(String keyword, String vocName) {
        final DirectoryService directoryService = STServiceLocator.getDirectoryService();
        Session session = null;
        try {
            session = directoryService.open(vocName);
            String schemaName = directoryService.getDirectorySchema(vocName);
            List<String> resultList = this.getIndexList(keyword, session, schemaName);
            return resultList;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des indexs commençant par le keyword donné", e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (DirectoryException e) {
                    log.warn("erreur lors de la fermeture du directory", e);
                }
            }
        }
        return null;
    }

    @Override
    public List<String> getIndexList(String keyword, List<String> vocList) {
        List<String> resultList = new ArrayList<String>();
        for (String vocName : vocList) {
            resultList.addAll(getIndexList(keyword, vocName));
        }
        List<String> resultListUniq = new ArrayList<String>(new HashSet<String>(resultList));
        return resultListUniq;
    }

    @Override
    public List<String> getLabelsFromDirectory(String vocabularyDirectoryName, String schemaVocabulaire) throws ClientException {
        if(schemaVocabulaire==null || schemaVocabulaire.isEmpty()){
            schemaVocabulaire = STVocabularyConstants.VOCABULARY;
        }
        List<DocumentModel> docList =  getDocumentModelListFromDirectory(vocabularyDirectoryName);
        List<String> labelList = new ArrayList<String>();
        for (DocumentModel docModel : docList) {
            labelList.add(PropertyUtil.getStringProperty(docModel, schemaVocabulaire, STVocabularyConstants.COLUMN_LABEL));
        }
        return labelList;
    }

    @Override
    public List<String> getLabelsFromDirectory(String vocabularyDirectoryName) throws ClientException {
        return getLabelsFromDirectory(vocabularyDirectoryName,STVocabularyConstants.VOCABULARY);
    }

}
