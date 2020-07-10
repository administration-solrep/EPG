package fr.dila.solonepg.web.selection;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.admin.AdministrationActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STViewConstant;

/**
 * Bean Seam qui permet de gérer les actions de masse des feuilles de route.
 * 
 * @author jtremeaux
 */
@Name("feuilleRouteMassActions")
@Scope(ScopeType.CONVERSATION)
public class FeuilleRouteMassActionsBean implements Serializable {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true)
    protected transient DocumentsListsManager documentsListsManager;

    @In(create = true, required = false)
    protected AdministrationActionsBean administrationActions;
    
    @In(create = true, required = false)
    protected FacesMessages facesMessages;

    @In(create = true)
    protected ResourcesAccessor resourcesAccessor;

    /**
     * Substitution de postes de feuille de route : ancien poste.
     */
    private String substitutionAncienPoste;
    
    /**
     * Substitution de postes de feuille de route : nouveau poste.
     */
    private String substitutionNouveauPoste;
    
    /**
     * Substitution de postes de feuille de route : réinitialise l'outil de sélection.
     */
    private boolean substitutionViderListe;

    /**
     * Effectue une substitution de masse des postes dans les modèles de feuille de route.
     * 
     * @return Vue
     * @throws ClientException
     */
    public String substituerPoste() throws ClientException {
        // Si aucun document n'est sélectionné, ne fait rien
        List<DocumentModel> feuilleRouteDocList = documentsListsManager.getWorkingList(DocumentsListsManager.DEFAULT_WORKING_LIST);
        if (feuilleRouteDocList == null) {
            return null;
        }
        
        // Substitue les postes dans les documents sélectionnés
        final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
        try {
            feuilleRouteModelService.substituerPoste(documentManager, feuilleRouteDocList, substitutionAncienPoste, substitutionNouveauPoste);
        } catch (ClientException e) {
            String errorMessage = resourcesAccessor.getMessages().get("epg.modeleFeuilleRoute.substituerPoste.substitution.error");
            facesMessages.add(StatusMessage.Severity.WARN, errorMessage);

            return STViewConstant.ERROR_VIEW;
        }
        
        // Affiche un message de confirmation
        String errorMessage = resourcesAccessor.getMessages().get("epg.modeleFeuilleRoute.substituerPoste.substitution.success");
        facesMessages.add(StatusMessage.Severity.INFO, errorMessage);
 
        // Réinitialise l'outil de sélection
        if (substitutionViderListe) {
            documentsListsManager.resetWorkingList(DocumentsListsManager.DEFAULT_WORKING_LIST);
        }
        
        return administrationActions.navigateToModeleFeuilleRouteFolder();
    }
    
    /**
     * Controle l'accès à la vue correspondante
     * 
     */
    public boolean isAccessAuthorized() {
    	SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
    	return (ssPrincipal.isAdministrator() || ssPrincipal.isMemberOf(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_UDPATER));
    }
    
    /**
     * Réinitialise le formulaire de substitution des postes.
     */
    public void resetSubstitution() {
        substitutionAncienPoste = null;
        substitutionNouveauPoste = null;
        substitutionViderListe = true;
    }

    /**
     * Getter de substitutionAncienPoste.
     *
     * @return substitutionAncienPoste
     */
    public String getSubstitutionAncienPoste() {
        return substitutionAncienPoste;
    }

    /**
     * Setter de substitutionAncienPoste.
     *
     * @param substitutionAncienPoste substitutionAncienPoste
     */
    public void setSubstitutionAncienPoste(String substitutionAncienPoste) {
        this.substitutionAncienPoste = substitutionAncienPoste;
    }

    /**
     * Getter de substitutionNouveauPoste.
     *
     * @return substitutionNouveauPoste
     */
    public String getSubstitutionNouveauPoste() {
        return substitutionNouveauPoste;
    }

    /**
     * Setter de substitutionNouveauPoste.
     *
     * @param substitutionNouveauPoste substitutionNouveauPoste
     */
    public void setSubstitutionNouveauPoste(String substitutionNouveauPoste) {
        this.substitutionNouveauPoste = substitutionNouveauPoste;
    }

    /**
     * Getter de substitutionViderListe.
     *
     * @return substitutionViderListe
     */
    public boolean isSubstitutionViderListe() {
        return substitutionViderListe;
    }

    /**
     * Setter de substitutionViderListe.
     *
     * @param substitutionViderListe substitutionViderListe
     */
    public void setSubstitutionViderListe(boolean substitutionViderListe) {
        this.substitutionViderListe = substitutionViderListe;
    }
}
