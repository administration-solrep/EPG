package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.solonepg.core.parapheur.ParapheurImpl;

/**
 * Adapteur de document vers Parapheur.
 * 
 * @author arolin
 */
public class ParapheurAdapterFactory implements DocumentAdapterFactory {

    protected void checkDocument(DocumentModel doc) {
      //TODO add facet or don't use Parapheur Interface
    }

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new ParapheurImpl(doc);
	}

}
