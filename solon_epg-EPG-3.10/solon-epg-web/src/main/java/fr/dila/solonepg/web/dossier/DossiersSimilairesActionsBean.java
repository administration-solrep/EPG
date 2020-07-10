package fr.dila.solonepg.web.dossier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.nuxeo.common.utils.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.web.client.DossierListingDTO;
import fr.dila.solonepg.web.espace.CorbeilleActionsBean;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;

@Name("dossiersSimilairesActions")
@Scope(ScopeType.SESSION)
public class DossiersSimilairesActionsBean implements Serializable {

    private static final Log log = LogFactory.getLog(DossiersSimilairesActionsBean.class);

    private static final long serialVersionUID = 1L;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient NavigationContext navigationContext;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true, required = false)
    protected transient BordereauActionsBean bordereauActions;

    @In(create = true, required = false)
    protected transient DocumentRoutingWebActionsBean routingWebActions;

    @In(create = true, required = false)
    protected transient ContentViewActions contentViewActions;

    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true)
    protected transient CorbeilleActionsBean corbeilleActions;

    private DocumentModel baseDossierSimilaire;

    private Map<String, Boolean> fieldsToCopy = new HashMap<String, Boolean>();

    private static final String DOSSIER_SIMILAIRE_CONTENT_VIEW = "bordereau_dossiers_indexation_content";

    public DocumentModel getBaseDossierSimilaire() {
        return baseDossierSimilaire;
    }

    public String navigateToDossiersSimilaires(DocumentModel dossierDoc) {
        baseDossierSimilaire = dossierDoc;
        routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.DOSSIERS_SIMILAIRES_VIEW);
        return SolonEpgViewConstant.DOSSIERS_SIMILAIRES_VIEW;
    }

    @SuppressWarnings("unchecked")
    public String navigateToDossierCopie(String idDossier, DossierListingDTO dossierListingDTO) throws ClientException {

        PageProvider<DossierListingDTO> pageProvider = (PageProvider<DossierListingDTO>) contentViewActions.getContentViewWithProvider(
                DOSSIER_SIMILAIRE_CONTENT_VIEW).getCurrentPageProvider();
        try {
            pageProvider.setCurrentEntry(dossierListingDTO);
        } catch (Exception e) {
            // mantis #36835
            log.warn("Ignoring : ", e);
        }
        navigationContext.setCurrentDocument(documentManager.getDocument(new IdRef(idDossier)));
        fieldsToCopy = new HashMap<String, Boolean>();
        routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.DOSSIER_SIMILAIRE_BORDEREAU_VIEW);
        return SolonEpgViewConstant.DOSSIER_SIMILAIRE_BORDEREAU_VIEW;
    }

    public String previousDossierCopie() throws ClientException {
        return corbeilleActions.navigateToPreviousDossier(DOSSIER_SIMILAIRE_CONTENT_VIEW);
    }

    public String nextDossierCopie() throws ClientException {
        return corbeilleActions.navigateToNextDossier(DOSSIER_SIMILAIRE_CONTENT_VIEW);
    }

    public Map<String, Boolean> getFieldsToCopy() {
        return fieldsToCopy;
    }

    public String copyFields() throws PropertyException, ClientException {
        DocumentModel baseDoc = baseDossierSimilaire;
        DocumentModel copyDoc = navigationContext.getCurrentDocument();
        for (Entry<String, Boolean> entry : fieldsToCopy.entrySet()) {
            if (entry.getValue()) {
                String property = entry.getKey();
                baseDoc.setPropertyValue(property, copyDoc.getProperty(property).getValue());
            }
        }

        bordereauActions.saveBordereau(baseDoc);

        return routingWebActions.getFeuilleRouteView();
    }

    public String annulerCopie() throws ClientException {
        navigationContext.setCurrentDocument(baseDossierSimilaire);
        routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_TRAITEMENT_VIEW);
        return navigationContext.navigateToDocument(baseDossierSimilaire);
    }

    public String getUFNXQLPart() {
        Dossier dossier = baseDossierSimilaire.getAdapter(Dossier.class);

        List<String> rubriques = dossier.getIndexationRubrique();
        List<String> motscles = dossier.getIndexationMotsCles();
        List<String> libre = dossier.getIndexationChampLibre();

        List<String> conditions = new ArrayList<String>();

        conditions.add(" d.ecm:uuid != '" + baseDossierSimilaire.getId() + "'");

        // TODO : escape
        // rubriques
        if (rubriques.size() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(" d.dos:rubriques IN (\"");
            builder.append(StringUtils.join(rubriques, "\", \""));
            builder.append("\")");
            conditions.add(builder.toString());
        }
        // motscles
        if (motscles.size() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(" d.dos:motscles IN (\"");
            builder.append(StringUtils.join(motscles, "\", \""));
            builder.append("\")");
            conditions.add(builder.toString());
        }
        // libre
        if (libre.size() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(" d.dos:libre IN (\"");
            builder.append(StringUtils.join(libre, "\", \""));
            builder.append("\")");
            conditions.add(builder.toString());
        }

        return StringUtils.join(conditions, " AND ");
    }
}
