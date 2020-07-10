package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.solonepg.core.fondedossier.FondDeDossierImpl;

/**
 * Adapteur de document vers FondDeDossier.
 * 
 * @author arolin
 */
public class FondDeDossierAdapterFactory implements DocumentAdapterFactory {

    protected void checkDocument(DocumentModel doc) {
            //TODO add facet or don't use FondDeDossier Interface
    }

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new FondDeDossierImpl(doc);
	}

}
