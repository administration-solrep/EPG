package fr.dila.solonepg.ui.contentview;

import fr.dila.st.core.requeteur.RequeteurRechercheUtilisateurs;
import java.util.ArrayList;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;

/**
 * Provider for user
 *
 * @author asatre
 *
 */
public class UserRecherchePageDocumentProvider extends CoreQueryDocumentPageProvider {
    private static final long serialVersionUID = 1L;

    @Override
    public List<DocumentModel> getCurrentPage() {
        checkQueryCache();
        if (currentPageDocuments == null) {
            error = null;
            errorMessage = null;

            String queryPart = (String) getParameters()[0];
            RequeteurRechercheUtilisateurs requeteur = new RequeteurRechercheUtilisateurs.Builder()
                .query(queryPart)
                .sortInfos(getSortInfos())
                .build();
            List<DocumentModel> list = requeteur.execute();

            currentPageDocuments = new ArrayList<DocumentModel>();

            int index = 0;
            while (offset + index < list.size() && index < pageSize) {
                currentPageDocuments.add(list.get((int) (offset + index)));
                index++;
            }

            resultsCount = list.size();
        }
        return currentPageDocuments;
    }
}
