package fr.dila.solonepg.core.adapter;

import fr.dila.cm.caselink.CaseLinkConstants;
import fr.dila.solonepg.core.caselink.DossierLinkImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de document vers DossierLink.
 *
 * @author arolin
 */
public class DossierLinkAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentSchemas(doc, CaseLinkConstants.CASE_LINK_SCHEMA);
        checkDocumentFacet(doc, CaseLinkConstants.CASE_LINK_FACET);
        return new DossierLinkImpl(doc);
    }
}
