package fr.dila.solonepg.web.decorator;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.web.client.DossierListingDTO;
import fr.dila.solonepg.web.espace.CorbeilleActionsBean;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.client.AbstractMapDTO;

/**
 * Ce Bean Seam permet de décorer les lignes des listing dossier.
 * 
 * @author jtremeaux
 */
@Name("dossierListingDecorator")
@Scope(ScopeType.EVENT)
public class DossierListingDecoratorBean {
    private static final String DATA_ROW_SELECTED = "dataRowSelected";

    @In(create = true, required = true)
    protected transient NavigationContext navigationContext;

    @In(create = true, required = true)
    protected transient CorbeilleActionsBean corbeilleActions;

    public String decorate(DocumentModel doc, String defaultClass) throws ClientException {

        DocumentModel currentDocument = navigationContext.getCurrentDocument();
        if (doc != null && currentDocument != null && currentDocument.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
            String dossierId = "";
            if (doc.hasSchema(STSchemaConstant.CASE_LINK_SCHEMA)) {
                dossierId = (String) doc.getProperty(STSchemaConstant.CASE_LINK_SCHEMA, STSchemaConstant.CASE_LINK_CASE_DOCUMENT_ID_PROPERTY);
            } else {
                dossierId = doc.getId();
            }
            if (currentDocument.getId().equals(dossierId)) {
                return DATA_ROW_SELECTED;
            }
        } else {
            if (doc != null && currentDocument != null && currentDocument.getId().equals(doc.getId())) {
                return DATA_ROW_SELECTED;
            }
        }

        return defaultClass;
    }

    public String decorate(AbstractMapDTO dto, String defaultClass) {

        DocumentModel currentDocument = navigationContext.getCurrentDocument();
        if (dto != null && currentDocument != null && currentDocument.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
            if (currentDocument.getId().equals(dto.getDocIdForSelection())) {

                if (dto instanceof DossierListingDTO) {
                    DossierListingDTO dldto = (DossierListingDTO) dto;
                    if (dldto.isFromCaseLink()) {
                        DossierLink dossierLink = corbeilleActions.getCurrentDossierLink();
                        if (dossierLink != null && dldto.getCaseLinkId().equals(dossierLink.getId())) {
                            return DATA_ROW_SELECTED;
                        }
                    } else {
                        return DATA_ROW_SELECTED;
                    }
                } else {
                    return DATA_ROW_SELECTED;
                }

            }
        }

        return defaultClass;
    }
}
