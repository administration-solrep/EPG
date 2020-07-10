package fr.dila.solonepg.web.dossier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

@Name("indexationActions")
@Scope(ScopeType.EVENT)
public class IndexationActionsBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(IndexationActionsBean.class);

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient NavigationContext navigationContext;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;

    private String currentIndexation;

    private List<String> listIndexationRubrique;

    private List<String> listIndexationmotCle;

    public void addIndexation(String indexationType, DocumentModel dossierDoc) {
        if (StringUtils.isEmpty(currentIndexation)) {
            return;
        }
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        List<String> indexations = getIndexations(indexationType, dossierDoc);
        if (!indexations.contains(currentIndexation)) {
            indexations.add(currentIndexation);
        }
        setIndexations(indexationType, dossier, indexations);

        currentIndexation = null;
    }

    public void removeIndexation(String indexationType, DocumentModel dossierDoc, String indexation) throws Exception {
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        List<String> indexations = getIndexations(indexationType, dossierDoc);
        indexations.remove(indexation);
        setIndexations(indexationType, dossier, indexations);
    }

    public List<String> getIndexations(String indexationType, DocumentModel dossierDoc) {
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_RUBRIQUE_PROPERTY)) {
            return dossier.getIndexationRubrique();
        }
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_MOTS_CLES_PROPERTY)) {
            return dossier.getIndexationMotsCles();
        }
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_CHAMP_LIBRE_PROPERTY)) {
            return dossier.getIndexationChampLibre();
        }
        return null;
    }

    private void setIndexations(String indexationType, Dossier dossier, List<String> indexations) {
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_RUBRIQUE_PROPERTY)) {
            dossier.setIndexationRubrique(indexations);
        }
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_MOTS_CLES_PROPERTY)) {
            dossier.setIndexationMotsCles(indexations);
        }
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_CHAMP_LIBRE_PROPERTY)) {
            dossier.setIndexationChampLibre(indexations);
        }
    }

    public String getCurrentIndexation() {
        return currentIndexation;
    }

    public void setCurrentIndexation(String currentIndexation) {
        this.currentIndexation = currentIndexation;
    }

    public List<String> findAllIndexation(String indexationType) {
        
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_RUBRIQUE_PROPERTY)) {
            if(listIndexationRubrique == null){
                listIndexationRubrique = new ArrayList<String>();
                try {
                    listIndexationRubrique = SolonEpgServiceLocator.getIndexationEpgService().findAllIndexationRubrique(documentManager, "");
                } catch (ClientException e) {
                    log.error("Impossible de récupérer les indexations Rubriques ");
                }
            }
            
            return listIndexationRubrique;
        }
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_MOTS_CLES_PROPERTY)) {
            if(listIndexationmotCle == null){
                listIndexationmotCle = new ArrayList<String>();
                try {
                    listIndexationmotCle = SolonEpgServiceLocator.getIndexationEpgService().findAllIndexationMotCleForDossier(documentManager, navigationContext.getCurrentDocument(), "");
                } catch (ClientException e) {
                    log.error("Impossible de récupérer les indexations mots cles pour le dossier" + navigationContext.getCurrentDocument().getId());
                }
            }
            return listIndexationmotCle;
        }
        
        return listIndexationRubrique;
    }
}
