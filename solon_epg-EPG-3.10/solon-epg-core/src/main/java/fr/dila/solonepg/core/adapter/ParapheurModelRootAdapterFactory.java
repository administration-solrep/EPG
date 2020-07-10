package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.solonepg.core.parapheur.ParapheurModelRootImpl;

/**
 * Adapteur de document vers la ParapheurModelRoot (racine des modèles de parapheur) . 
 * 
 * @author arolin
 */
public class ParapheurModelRootAdapterFactory implements DocumentAdapterFactory {

    protected void checkDocument(DocumentModel doc) {
        //TODO pas de schema
    }

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		checkDocument(doc);
		return new ParapheurModelRootImpl(doc);
	}

}
