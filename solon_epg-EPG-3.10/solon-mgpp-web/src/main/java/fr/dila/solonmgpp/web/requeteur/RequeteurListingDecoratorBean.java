package fr.dila.solonmgpp.web.requeteur;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.web.fichepresentation.FicheLoiActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentation341ActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationAVIActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationDRActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationDecretActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationIEActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationOEPActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationSDActionsBean;

/**
 * Ce bean Seam permet de décorer les lignes des listing du requeteur MGPP.
 * 
 * @author bgamard
 */
@Name("requeteurListingDecorator")
@Scope(ScopeType.EVENT)
public class RequeteurListingDecoratorBean {

    private static final String DATA_ROW_SELECTED = "dataRowSelected";

    @In(create = true, required = true)
    protected transient NavigationContext navigationContext;

    @In(create = true, required = false)
    protected transient FicheLoiActionsBean ficheLoiActions;
    
    @In(create = true, required = false)
    protected transient FichePresentation341ActionsBean fichePresentation341Actions;
    
    @In(create = true, required = false)
    protected transient FichePresentationDRActionsBean fichePresentationDRActions;
    
    @In(create = true, required = false)
    protected transient FichePresentationDecretActionsBean fichePresentationDecretActions;
    
    @In(create = true, required = false)
    protected transient FichePresentationOEPActionsBean fichePresentationOEPActions;
    
    @In(create = true, required = false)
    protected transient FichePresentationIEActionsBean fichePresentationIEActions;
    
    @In(create = true, required = false)
    protected transient FichePresentationAVIActionsBean fichePresentationAVIActions;
    
    @In(create = true, required = false)
    protected transient FichePresentationSDActionsBean fichePresentationSDActions;

    public String decorate(DocumentModel doc, String defaultClass) {
        
        String result = defaultClass ;
        
        DocumentModel currentDocument = navigationContext.getCurrentDocument();
        if (doc != null && currentDocument != null && currentDocument.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
            if (doc.getType().equals(FicheLoi.DOC_TYPE)) {
                if (ficheLoiActions.getFicheLoi() != null && ficheLoiActions.getFicheLoi().getId().equals(doc.getId())) {
                    result =  DATA_ROW_SELECTED;
                }
            } else if (doc.getType().equals(FichePresentationOEP.DOC_TYPE)) {
                if (fichePresentationOEPActions.getFicheOEP() != null && fichePresentationOEPActions.getFicheOEP().getId().equals(doc.getId())) {
                    result = DATA_ROW_SELECTED;
                }
            } else if (doc.getType().equals(FichePresentation341.DOC_TYPE)) {
                if (fichePresentation341Actions.getFiche341() != null && fichePresentation341Actions.getFiche341().getId().equals(doc.getId())) {
                    result = DATA_ROW_SELECTED;
                }
            } else if (doc.getType().equals(FichePresentationAVI.DOC_TYPE)) {
                if (fichePresentationAVIActions.getFicheAVI() != null && fichePresentationAVIActions.getFicheAVI().getId().equals(doc.getId())) {
                    result = DATA_ROW_SELECTED;
                }
            } else if (doc.getType().equals(FichePresentationDecret.DOC_TYPE)) {
                if (fichePresentationDecretActions.getFicheDecret() != null && fichePresentationDecretActions.getFicheDecret().getId().equals(doc.getId())) {
                    result = DATA_ROW_SELECTED;
                }
            } else if (doc.getType().equals(FichePresentationDR.DOC_TYPE)) {
                if (fichePresentationDRActions.getFicheDR() != null && fichePresentationDRActions.getFicheDR().getId().equals(doc.getId())) {
                    result = DATA_ROW_SELECTED;
                }
            } else if (doc.getType().equals(FichePresentationIE.DOC_TYPE)) {
                if (fichePresentationIEActions.getFicheIE() != null && fichePresentationIEActions.getFicheIE().getId().equals(doc.getId())) {
                    result = DATA_ROW_SELECTED;
                }
            }
            else if (doc.getType().equals(FichePresentationSD.DOC_TYPE)) {
                if (fichePresentationSDActions.getFiche() != null && fichePresentationSDActions.getFiche().getId().equals(doc.getId())) {
                    result = DATA_ROW_SELECTED;
                }
            }
        }

        return result;
    }
}
