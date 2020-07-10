package fr.dila.solonepg.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STVocabularyConstants;

/**
 * Converter JSF qui fournit l'identifiant et le label d'un DocumentModel de vocabulaire.
 * 
 * @author arolin
 */
public class DocumentModelVocabularyConverter implements Converter {
    
    /**
     * Nom du vocabulaire.
     */
    private final String directoryName;
    
    /**
     * Constructeur
     * 
     * @param directoryName
     */
    public DocumentModelVocabularyConverter(String directoryName){
        this.directoryName = directoryName;
    }
    
    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String string) {
        if(string!=null){
            SolonEpgVocabularyService solonEpgVocabularyService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
            return solonEpgVocabularyService.getDocumentModelFromId(directoryName, string);
        }
        return string;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent arg1, Object object) {
       if (object != null && object instanceof DocumentModel) {
           DocumentModel documentModelVocabulaire = (DocumentModel) object;
           String valueVocabulary;
        try {
            valueVocabulary = (String) documentModelVocabulaire.getProperty(STVocabularyConstants.VOCABULARY, STVocabularyConstants.COLUMN_ID);
        } catch (ClientException e) {
            return null;
        }
           return valueVocabulary;
        }
        return null;
    }

}
