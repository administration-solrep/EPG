package fr.dila.solonepg.core.event;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.service.STLockService;
import fr.dila.st.core.event.AbstractFilterEventListener;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.constant.FeuilleRouteConstant;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

/**
 * Listener permettant de traiter les changement de document pour verrouiller / déverrouiller automatiquement
 * les documents.
 *
 * @author jtremeaux
 */
public class DocumentLockListener extends AbstractFilterEventListener<InlineEventContext> {
    private static final Log log = LogFactory.getLog(DocumentLockListener.class);

    public DocumentLockListener() {
        super(STEventConstant.CURRENT_DOCUMENT_CHANGED_EVENT, InlineEventContext.class);
    }

    @Override
    protected void doHandleEvent(Event event, final InlineEventContext context) {
        // Traite uniquement les évènements de changement document courant

        // Tente de déverrouiller l'ancien document
        final DocumentModel oldDoc = (DocumentModel) context.getProperty(STEventConstant.OLD_DOCUMENT_EVENT_PARAM);
        final DocumentModel newDoc = (DocumentModel) context.getProperty(STEventConstant.NEW_DOCUMENT_EVENT_PARAM);
        if (oldDoc != null) {
            unlockDocument(context.getCoreSession(), (NuxeoPrincipal) context.getPrincipal(), oldDoc, newDoc);
        }

        // Tente de vérrouiller le nouveau document
        if (newDoc != null) {
            lockDocument(context.getCoreSession(), (NuxeoPrincipal) context.getPrincipal(), oldDoc, newDoc);
        }
    }

    /**
     * Tente de verrouiller un document.
     * Si le document n'est pas verrouillable (l'utilisateur n'a pas les droits, ou il est déja verrouillé
     * par un autre utilisateur) ce n'est pas une erreur, mais le document sera consultable en lecture seule.
     *
     * @param session Session
     * @param principal Principal
     * @param oldDoc Ancien document
     * @param newDoc Document à vérrouiller (remplace l'ancien document)
     */
    private void lockDocument(
        CoreSession session,
        NuxeoPrincipal principal,
        DocumentModel oldDoc,
        DocumentModel newDoc
    ) {
        final STLockService stLockService = STServiceLocator.getSTLockService();

        try {
            String docType = newDoc.getType();
            if (SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE.equals(docType)) {
                // Verrouille les modèles feuilles de route
                SSFeuilleRoute feuilleRoute = newDoc.getAdapter(SSFeuilleRoute.class);
                if (
                    feuilleRoute.isModel() &&
                    principal.isMemberOf(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_UDPATER)
                ) {
                    if (stLockService.isLockActionnableByUser(session, newDoc, principal)) {
                        stLockService.lockDoc(session, newDoc);
                    }
                }
            }
            if (SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE.equals(docType)) {
                // Verrouille les squelettes de feuilles de route
                if (principal.isMemberOf(SolonEpgBaseFunctionConstant.FEUILLE_DE_ROUTE_SQUELETTE_UDPATER)) {
                    if (stLockService.isLockActionnableByUser(session, newDoc, principal)) {
                        stLockService.lockDoc(session, newDoc);
                    }
                }
            }
        } catch (NuxeoException e) {
            log.error("Erreur de verrouillage automatique du document <" + newDoc.getName() + ">", e);
        }
    }

    /**
     * Tente de déverrouiller un document.
     * Si le document n'est pas déverrouillable cela signifie que le document était verrouillé par un autre
     * utilisateur et il n'y a pas d'action à effectuer.
     *
     * @param session Session
     * @param principal Principal
     * @param oldDoc Document à déverrouiller
     * @param newDoc Nouveau document (remplace le document déverrouillé)
     */
    private void unlockDocument(
        CoreSession session,
        NuxeoPrincipal principal,
        DocumentModel oldDoc,
        DocumentModel newDoc
    ) {
        final STLockService stLockService = STServiceLocator.getSTLockService();

        final String oldDocType = oldDoc.getType();
        final Set<String> newDocFacet = new HashSet<String>();
        if (newDoc != null) {
            newDocFacet.addAll(newDoc.getFacets());
        }

        try {
            if (SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE.equals(oldDocType)) {
                // Déverrouille les modèles feuilles de route
                SolonEpgFeuilleRoute feuilleRoute = oldDoc.getAdapter(SolonEpgFeuilleRoute.class);
                if (feuilleRoute.isModel()) {
                    // Ne déverrouille pas encore si on édite une étape de la feuille de route
                    if (!newDocFacet.contains(FeuilleRouteConstant.FACET_FEUILLE_ROUTE_STEP)) {
                        if (stLockService.isLockActionnableByUser(session, oldDoc, principal)) {
                            stLockService.unlockDoc(session, oldDoc);
                        }
                    }
                }
            }
            if (SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE.equals(oldDocType)) {
                // Verrouille les squelettes de feuilles de route
                if (principal.isMemberOf(SolonEpgBaseFunctionConstant.FEUILLE_DE_ROUTE_SQUELETTE_UDPATER)) {
                    // Ne déverrouille pas encore si on édite une étape de la feuille de route
                    if (!newDocFacet.contains(FeuilleRouteConstant.FACET_FEUILLE_ROUTE_STEP)) {
                        if (stLockService.isLockActionnableByUser(session, oldDoc, principal)) {
                            stLockService.unlockDoc(session, oldDoc);
                        }
                    }
                }
            }
        } catch (NuxeoException e) {
            log.error("Erreur de verrouillage automatique du document <" + oldDoc.getName() + ">", e);
        }
    }
}
