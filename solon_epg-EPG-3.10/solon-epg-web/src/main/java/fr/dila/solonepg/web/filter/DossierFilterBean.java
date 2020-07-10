package fr.dila.solonepg.web.filter;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.web.context.NavigationContextBean;

/**
 * WebBean qui permet de filtrer les types d'acte.
 * 
 * @author jtremeaux
 */

@Name("dossierFilter")
@Scope(CONVERSATION)
public class DossierFilterBean implements Serializable {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= -635818685865903602L;

	@In(required = true, create = true)
	protected NavigationContextBean	navigationContext;

	public Boolean isNotDossierDeleted() {
		DocumentModel currentDoc = navigationContext.getCurrentDocument();
		if (currentDoc != null) {
			if (currentDoc.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
				Dossier dossier = currentDoc.getAdapter(Dossier.class);
				if (dossier != null) {

					return !dossier.isDossierDeleted();
				}
			}
		}

		return Boolean.FALSE;
	}
}
