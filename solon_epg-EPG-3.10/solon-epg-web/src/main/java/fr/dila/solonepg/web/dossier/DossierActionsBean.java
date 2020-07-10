package fr.dila.solonepg.web.dossier;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.DossierBordereauService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.client.InfoDossierDTO;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.web.context.NavigationContextBean;
import fr.dila.st.web.lock.STLockActionsBean;

/**
 * Une classe qui permet de gérer les actions relatives aux dossiers.
 * 
 * @author bgamard
 */
@Name("dossierActions")
@Scope(ScopeType.PAGE)
public class DossierActionsBean implements Serializable {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = -6314042716707063184L;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = false)
    protected transient DossierDistributionActionsBean dossierDistributionActions;

    @In(create = true, required = false)
    protected transient BordereauActionsBean bordereauActions;

    @In(create = true, required = false)
    protected transient STLockActionsBean stLockActions;

    @In(create = true, required = false)
    protected transient DossierListingActionsBean dossierListingActions;

    @In(required = true, create = true)
    protected NuxeoPrincipal currentUser;

    /**
     * Dossier Link courant.
     */
    protected DocumentModel currentDossierLinkDoc;

    /**
     * Dossier courant.
     */
    protected DocumentModel dossierDoc;

    /**
     * Retourne true si le Dossier est complet
     * @param dossierDoc
     * @return
     */
    public boolean isDossierComplet(final DocumentModel dossierDoc) {
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        return bordereauActions.isBordereauComplet(dossierDoc) && dossier.getIsParapheurComplet();
    }

    /**
     * Récupération du Dossier à partir du DossierLink.
     * 
     * @param dossierLinkDoc
     * @return
     */
    public DocumentModel getDossierFromDossierLink(final DocumentModel dossierLinkDoc) {
        if (currentDossierLinkDoc == null || currentDossierLinkDoc.getId() != dossierLinkDoc.getId()) {
            currentDossierLinkDoc = dossierLinkDoc;
            dossierDoc = currentDossierLinkDoc.getAdapter(DossierLink.class).getCase(documentManager).getDocument();
        }
        return dossierDoc;
    }

    /**
     * Retourne vrai si le Dossier est urgent
     * 
     * @param dossierDoc
     * @return boolean
     */
    public boolean isDossierUrgent(final DocumentModel dossierDoc) {
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        return dossier.getIsUrgent();
    }

    /**
     * Marque le dossier comme urgent.
     * 
     * @param dossierDoc
     */
    public void addUrgenceDossier(final DocumentModel dossierDoc) throws ClientException {

        //TODO Vérifie si le document est supprimé

        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (!stLockActions.isDocumentLockedByCurrentUser(dossierDoc)) {
            final String errorMessage = resourcesAccessor.getMessages().get(STLockActionsBean.LOCK_LOST_ERROR_MSG);
            facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
            return;
        }

        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        dossier.setIsUrgent(true);
        dossier.save(documentManager);
        Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
    }

    /**
     * Marque le dossier comme non urgent.
     * 
     * @param dossierDoc
     * @throws ClientException 
     */
    public void removeUrgenceDossier(final DocumentModel dossierDoc) throws ClientException {

        //TODO Vérifie si le document est supprimé

        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (!stLockActions.isDocumentLockedByCurrentUser(dossierDoc)) {
            final String errorMessage = resourcesAccessor.getMessages().get(STLockActionsBean.LOCK_LOST_ERROR_MSG);
            facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
            return;
        }

        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        dossier.setIsUrgent(false);
        dossier.save(documentManager);
        Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
    }

    /**
     * Retire le caractère mesure nominative du dossier courant.
     * 
     * @return Vue
     * @throws ClientException
     */
    public String annulerMesureNominative() throws ClientException {
        final DocumentModel dossierDoc = navigationContext.getCurrentDocument();

        //TODO Vérifie si le document est supprimé

        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (!stLockActions.isDocumentLockedByCurrentUser(dossierDoc)) {
            final String errorMessage = resourcesAccessor.getMessages().get(STLockActionsBean.LOCK_LOST_ERROR_MSG);
            facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
            return null;
        }

        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        // Retire le caractère "mesure nominative" 
        final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        dossierService.annulerMesureNominativeUnrestricted(documentManager, dossierDoc);

        // Affiche un message de confirmation
        final String message = resourcesAccessor.getMessages().get("epg.dossier.command.annulerMesureNominative.success");
        facesMessages.add(StatusMessage.Severity.INFO, MessageFormat.format(message, dossier.getNumeroNor()));

        return null;
    }

    /**
     * Retire le caractère mesure nominative du dossier courant.
     * 
     * @return Vue
     * @throws ClientException
     */
    public String redonnerMesureNominative() throws ClientException {
        final DocumentModel dossierDoc = navigationContext.getCurrentDocument();

        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (!stLockActions.isDocumentLockedByCurrentUser(dossierDoc)) {
            final String errorMessage = resourcesAccessor.getMessages().get(STLockActionsBean.LOCK_LOST_ERROR_MSG);
            facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
            return null;
        }

        // Ajoute le caractère "mesure nominative" 
        final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        dossierService.ajouterMesureNominativeUnrestricted(documentManager, dossierDoc);

        // Affiche un message de confirmation
        final String message = resourcesAccessor.getMessages().get("epg.dossier.command.redonnerMesureNominative.success");
        facesMessages.add(StatusMessage.Severity.INFO, MessageFormat.format(message, dossierDoc.getAdapter(Dossier.class).getNumeroNor()));

        return null;
    }

    public boolean hasTypeActeMesureNominative() {
        final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
        if (dossierDoc != null) {
            final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            final ActeService acteService = SolonEpgServiceLocator.getActeService();
            return acteService.hasTypeActeMesureNominative(dossier.getTypeActe());
        }
        return false;
    }

    public void publierDossierStub(final DocumentModel dossierDoc) throws ClientException {

        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        if (SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(dossier.getTypeActe())) {
            // Affiche un message de non possibilité d'action
            final String message = resourcesAccessor.getMessages().get("feedback.solonepg.action.impossible.publier");
            facesMessages.add(StatusMessage.Severity.WARN, MessageFormat.format(message, dossier.getNumeroNor()));
            return;
        }

        //TODO Vérifie si le document est supprimé

        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (!stLockActions.isDocumentLockedByCurrentUser(dossierDoc)) {
            final String errorMessage = resourcesAccessor.getMessages().get(STLockActionsBean.LOCK_LOST_ERROR_MSG);
            facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
            return;
        }

        final DossierBordereauService dossierBordereauService = SolonEpgServiceLocator.getDossierBordereauService();
        dossierBordereauService.publierDossierStub(dossierDoc, documentManager);
    }

    public InfoDossierDTO getInfo(final DocumentModel doc) throws ClientException {

        final InfoDossierDTO infoDossierDTO = new InfoDossierDTO();
        if (doc != null) {
            if (doc.hasSchema(STSchemaConstant.CASE_LINK_SCHEMA)) {
                final DossierLink dossierLink = doc.getAdapter(DossierLink.class);

                if (SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETOUR_MODIFICATION_VALUE.equals(dossierLink.getRoutingTaskType())) {
                    infoDossierDTO.setRetourPourModification(Boolean.TRUE);
                }

                // Affiche les lignes des dossiers urgents en rose
                final DocumentModel dossierDoc = dossierLink.getCase(documentManager).getDocument();
                final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                if (dossier.getIsUrgent()) {
                    infoDossierDTO.setUrgent(Boolean.TRUE);
                }
                infoDossierDTO.setLockInfo(stLockActions.getLockMessageWithUserInfo(dossierDoc, "c p n"));
                infoDossierDTO.setLock(dossierDoc.isLocked());
            } else if (doc.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
                final Dossier dossier = doc.getAdapter(Dossier.class);
                if (dossier.getIsUrgent()) {
                    infoDossierDTO.setUrgent(Boolean.TRUE);
                }

                infoDossierDTO.setLockInfo(stLockActions.getLockMessageWithUserInfo(doc, "c p n"));
                infoDossierDTO.setLock(doc.isLocked());

                final List<DocumentModel> listDossierLink = dossierListingActions.findDossierLink(doc);
                if (listDossierLink != null) {
                    // on recherche si au moins 1 DossierLing est en retour pour
                    // modification
                    for (final DocumentModel documentModel : listDossierLink) {
                        final DossierLink dossierLink = documentModel.getAdapter(DossierLink.class);

                        if (SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETOUR_MODIFICATION_VALUE.equals(dossierLink.getRoutingTaskType())) {
                            infoDossierDTO.setRetourPourModification(Boolean.TRUE);
                            break;
                        }
                    }
                }
            }
        }

        return infoDossierDTO;
    }

    public String contextMenuId(final String nomCreateur, final Boolean isFichierObligatoire, final String typeNoeud) throws ClientException {

        String contextMenuId = null;
        if (typeNoeud == null) {
            contextMenuId = null;
        } else if ("ParapheurFichier".equals(typeNoeud)) {
            if (isDocParapheurDeletable(nomCreateur, isFichierObligatoire)) {
                contextMenuId = "menuParapheurFichierUpdatableAndDeletable";
            } else {
                contextMenuId = "menuParapheurFichierUpdatable";
            }
        } else if ("FondDeDossierFolderDeletable".equals(typeNoeud)) {
            if (isDocFondDossierDeletable(nomCreateur)) {
                contextMenuId = "menuFondDeDossierFolderUpdatableAndDeletable";
            } else {
                contextMenuId = "menuFondDeDossierFolderUpdatable";
            }
        } else if ("FondDeDossierFichier".equals(typeNoeud)) {
            if (isDocFondDossierDeletable(nomCreateur)) {
                contextMenuId = "menuFondDeDossierFichierUpdatableAndDeletable";
            } else {
                contextMenuId = "menuFondDeDossierFichierUpdatable";
            }
        }
        return contextMenuId;
    }

    /**
     * Détermine si le document du parapheur ou du fond de dossier est supprimable.
     * 
     * @param nomCreateur nom du createur du document
     * @param isFichierObligatoire vrai si le fichier est obligatoire dans le parapheur
     * @return condition
     * @throws ClientException
     */
    public boolean isDocParapheurDeletable(final String nomCreateur, final Boolean isFichierObligatoire) throws ClientException {
        //récupération 

        // Vérifie que l'utilisateur possède la fonction unitaire ou est le createur du fichier
        if (!currentUser.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_PARAPHEUR_OR_FOND_DOSSIER_DELETER)
                && !currentUser.getName().equals(nomCreateur)) {
            return false;
        }

        //La suppression est possible tant que le dossier n'est pas au statut "NOR attribué" ou "Publié". 
        final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        final String statutDossier = dossier.getStatut();
        if (statutDossier == null || statutDossier.equals(VocabularyConstants.STATUT_PUBLIE)
                || statutDossier.equals(VocabularyConstants.STATUT_NOR_ATTRIBUE)) {
            return false;
        }

        //si le dossier est à l'état lancé et est un fichier obligatoire dans le parapheur, on ne peut pas le supprimer
        if (isFichierObligatoire && statutDossier.equals(VocabularyConstants.STATUT_PUBLIE)) {
            return false;
        }

        return true;
    }

    /**
     * Détermine si le document du fond de dossier est supprimable.
     * 
     * @param nomCreateur nom du createur du document
     * @return condition
     * @throws ClientException
     */
    public boolean isDocFondDossierDeletable(final String nomCreateur) throws ClientException {
        return isDocParapheurDeletable(nomCreateur, false);
    }

    public void terminerDossierSansPublication(final DocumentModel dossierDoc) throws ClientException {

        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        if (SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(dossier.getTypeActe())) {
            // Affiche un message de non possibilité d'action
            final String message = resourcesAccessor.getMessages().get("feedback.solonepg.action.impossible.termineSansPub");
            facesMessages.add(StatusMessage.Severity.WARN, MessageFormat.format(message, dossier.getNumeroNor()));
            return;
        }

        //TODO Vérifie si le document est supprimé

        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (!stLockActions.isDocumentLockedByCurrentUser(dossierDoc)) {
            final String errorMessage = resourcesAccessor.getMessages().get(STLockActionsBean.LOCK_LOST_ERROR_MSG);
            facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
            return;
        }

        final DossierBordereauService dossierBordereauService = SolonEpgServiceLocator.getDossierBordereauService();
        dossierBordereauService.terminerDossierSansPublication(dossierDoc, documentManager);
    }

}
