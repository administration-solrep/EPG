package fr.dila.solonepg.web.filter;

import static org.jboss.seam.annotations.Install.FRAMEWORK;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.context.NavigationContextBean;

/**
 * WebBean qui permet de filtrer les types d'acte.
 * 
 * @author jtremeaux
 */
@Name("typeActeFilter")
@Scope(ScopeType.EVENT)
@Install(precedence = FRAMEWORK)
public class TypeActeFilterBean implements Filter, Serializable {
    /**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 8873185940289038471L;

	@In(required = true, create = true)
    protected NuxeoPrincipal currentUser;

    @In(required = true, create = true)
    protected NavigationContextBean navigationContext;

    @Override
    public boolean accept(DocumentModel doc) {
        // filtre sur les mesures nominatives
        ActeService acteService = SolonEpgServiceLocator.getActeService();
        if (acteService.hasTypeActeMesureNominative(doc.getId())) {
            final List<String> groups = currentUser.getGroups();
            return groups.contains(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_READER);
        }
        return true;
    }

    public Boolean isRapportAuParlement() {
        DocumentModel currentDoc = navigationContext.getCurrentDocument();
        if (currentDoc != null) {
            if (currentDoc.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
                Dossier dossier = currentDoc.getAdapter(Dossier.class);
                if (dossier != null) {
                    ActeService acteService = SolonEpgServiceLocator.getActeService();
                    return acteService.isRapportAuParlement(dossier.getTypeActe());
                }
            }
        }

        return Boolean.FALSE;
    }
    
    public Boolean isActeTexteNonPublie() {
        DocumentModel currentDoc = navigationContext.getCurrentDocument();
        if (currentDoc != null) {
            if (currentDoc.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
                Dossier dossier = currentDoc.getAdapter(Dossier.class);
                if (dossier != null) {
                    return SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(dossier.getTypeActe());
                }
            }
        }
        return Boolean.FALSE;
    }
}
