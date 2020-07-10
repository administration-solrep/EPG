package fr.dila.solonmgpp.web.provider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;

import fr.dila.solonmgpp.web.fichepresentation.FichePresentationOEPActionsBean;

public class RepresentantOEPPageProvider extends CoreQueryDocumentPageProvider {

    private static final String TYPE_REPRESENTANT = "typeRepresentant";
    /**
     * 
     */
    private static final long serialVersionUID = 2444676110607732904L;
    private static final String BEAN = "bean";

    @Override
    public List<DocumentModel> getCurrentPage() {
        checkQueryCache();
        if (currentPageDocuments == null) {
            error = null;
            errorMessage = null;

            currentPageDocuments = new ArrayList<DocumentModel>();

            List<DocumentModel> list = getBean().fetchRepresentant(getTypeRepresentant());

            currentPageDocuments = list != null ? list : new ArrayList<DocumentModel>();

            resultsCount = currentPageDocuments.size();

        }

        return currentPageDocuments;
    }

    protected void buildQuery() {
        query = "";
    }

    @Override
    public void setSearchDocumentModel(DocumentModel searchDocumentModel) {
        if (this.searchDocumentModel != searchDocumentModel) {
            this.searchDocumentModel = searchDocumentModel;
            refresh();
        }
    }

    private FichePresentationOEPActionsBean getBean() {
        Map<String, Serializable> props = getProperties();
        return (FichePresentationOEPActionsBean) props.get(BEAN);
    }

    private String getTypeRepresentant() {
        Map<String, Serializable> props = getProperties();
        return (String) props.get(TYPE_REPRESENTANT);
    }
}
