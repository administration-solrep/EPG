package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.core.recherche.ResultatConsulteImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de DocumentModel vers ResultatConsulte.
 *
 * @author asatre
 */
public class ResultatConsulteAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_SCHEMA);
        return new ResultatConsulteImpl(doc);
    }
}
