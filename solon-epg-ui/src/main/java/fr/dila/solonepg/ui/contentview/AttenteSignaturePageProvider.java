package fr.dila.solonepg.ui.contentview;

import fr.dila.solonepg.ui.helper.EpgAttenteSignatureHelper;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.ui.contentview.AbstractDTOPageProvider;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class AttenteSignaturePageProvider extends AbstractDTOPageProvider {
    private static final long serialVersionUID = 1L;

    @Override
    protected void fillCurrentPageMapList(CoreSession coreSession) {
        currentItems = new ArrayList<>();

        resultsCount = QueryUtils.doCountQuery(coreSession, query);
        if (resultsCount > 0) {
            List<String> ids = QueryUtils.doQueryForIds(coreSession, query, getPageSize(), offset);
            populateFromIds(coreSession, ids);
        }
    }

    private void populateFromIds(CoreSession session, List<String> ids) {
        if (!ids.isEmpty()) {
            List<DocumentModel> dml = QueryHelper.retrieveDocuments(session, ids, null);
            currentItems.addAll(
                dml
                    .stream()
                    .filter(Objects::nonNull)
                    .map(
                        dm ->
                            EpgAttenteSignatureHelper.dossierDocToAttenteSignatureDTO(
                                session,
                                dm,
                                (String) parameters[0]
                            )
                    )
                    .collect(Collectors.toList())
            );
        }
    }
}
