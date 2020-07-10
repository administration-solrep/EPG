package fr.dila.solonepg.web.decorator;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.st.core.client.AbstractMapDTO;

/**
 * Ce Bean Seam permet de décorer les lignes des listing dto (de type dossier).
 * 
 * @author asatre
 */
@Name("dtoListingDecorator")
@Scope(ScopeType.EVENT)
public class DTOListingDecoratorBean {

    @In(create = true, required = false)
    protected transient NavigationContext navigationContext;

    public String decorate(AbstractMapDTO dto, String defaultClass) {

        DocumentModel currentDocument = navigationContext.getCurrentDocument();
        if (currentDocument != null && currentDocument.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
            String dossierId = dto.getDocIdForSelection();
            if (currentDocument.getId().equals(dossierId)) {
                return "dataRowSelected";
            }
        }

        return defaultClass;
    }
}
