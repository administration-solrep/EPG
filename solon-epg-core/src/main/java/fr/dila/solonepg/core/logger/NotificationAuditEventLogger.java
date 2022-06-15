package fr.dila.solonepg.core.logger;

import fr.dila.cm.event.CaseManagementEventConstants.EventNames;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.constant.SSEventConstant;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.core.logger.STNotificationAuditEventLogger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

/**
 * Listener asynchrone post-commit qui enregistre l'Audit log pour l'application SolonEPG.
 *
 * @author ARN
 */
public class NotificationAuditEventLogger extends STNotificationAuditEventLogger {
    private static final Log LOGGER = LogFactory.getLog(NotificationAuditEventLogger.class);

    /**
     * Liste des évènements qui déclenchent une entrée dans le journal technique du pan. Ces évènements sont
     * attachés à un EventContext
     */
    protected static final Set<String> SET_EVENT_PAN = new HashSet<>();

    static {
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.CREATE_FILE_PARAPHEUR);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.UPDATE_FILE_PARAPHEUR);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.DELETE_FILE_PARAPHEUR);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.MOVE_FILE_PARAPHEUR);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.FAIL_GET_FILE_INFO_PARAPHEUR);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.PARAPHEUR_IMPRESSION_ZIP_EVENT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.CREATE_FILE_FDD);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.UPDATE_FILE_FDD);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.DELETE_FILE_FDD);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.CREATE_FOLDER_FDD);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.UPDATE_FOLDER_FDD);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.DELETE_FOLDER_FDD);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.MOVE_FILE_FDD);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.FDD_IMPRESSION_ZIP_EVENT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.DOSSIER_IMPRESSION_PDF_EVENT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.TRAITEMENT_PAPIER_UPDATE);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.DOSSIER_STATUT_CHANGED);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.AFTER_DOSSIER_TRANSFERT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.AFTER_DOSSIER_REATTRIBUTION);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.WEBSERVICE_ATTRIBUER_NOR_EVENT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.WEBSERVICE_DONNER_AVIS_CE_EVENT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.WEBSERVICE_MODIFIER_DOSSIER_CE_EVENT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.WEBSERVICE_ENVOYER_RETOUR_PE_EVENT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.WEBSERVICE_ENVOYER_PREMIERE_DEMANDE_EVENT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.NOTIF_ABANDON_REJEU_EVENT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.NOTIF_ECHOUEE_EVENT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.NOTIF_REUSSIE_EVENT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.WEBSERVICE_CHERCHER_DOSSIER_EVENT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.AFTER_SAISINE_RECTIFICATIVE);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.AFTER_ENVOI_PIECES_COMPLEMENTAIRES);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.WEBSERVICE_CHERCHER_MODIFICATION_DOSSIER_CE_EVENT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.SUPPRESSION_DOSSIER_EVENT);
        SET_EVENT_DOSSIER.add(SolonEpgEventConstant.EVENT_SUBSTITUTION_FDR);

        SET_EVENT_ADMIN.add(SolonEpgEventConstant.WEBSERVICE_CHERCHER_DOSSIER_EVENT);
        SET_EVENT_ADMIN.add(SolonEpgEventConstant.WEBSERVICE_CHERCHER_DOSSIER_CE_EVENT);
        SET_EVENT_ADMIN.add(SolonEpgEventConstant.PARAM_APPLI_UPDATE_EVENT);
        SET_EVENT_ADMIN.add(SolonEpgEventConstant.PARAM_ADAMANT_UPDATE_EVENT);
        SET_EVENT_ADMIN.add(SolonEpgEventConstant.UPDATE_MODELE_PARAPHEUR_EVENT);
        SET_EVENT_ADMIN.add(SolonEpgEventConstant.CREATE_MODELE_FDD_EVENT);
        SET_EVENT_ADMIN.add(SolonEpgEventConstant.UPDATE_MODELE_FDD_EVENT);
        SET_EVENT_ADMIN.add(SolonEpgEventConstant.DELETE_MODELE_FDD_EVENT);
        SET_EVENT_ADMIN.add(SSEventConstant.CREATE_MODELE_FDR_EVENT);
        SET_EVENT_ADMIN.add(SSEventConstant.UPDATE_MODELE_FDR_EVENT);
        SET_EVENT_ADMIN.add(SSEventConstant.DELETE_MODELE_FDR_EVENT);
        SET_EVENT_ADMIN.add(SSEventConstant.DUPLICATION_MODELE_FDR_EVENT);
        SET_EVENT_ADMIN.add(SolonEpgEventConstant.UPDATE_SQUELETTE_FDR_EVENT);
        SET_EVENT_ADMIN.add(SolonEpgEventConstant.DUPLICATION_SQUELETTE_FDR_EVENT);
        SET_EVENT_ADMIN.add(SolonEpgEventConstant.DOSSIER_SUPPRIME_EVENT);

        SET_EVENT_PAN.add(SolonEpgEventConstant.AJOUT_MESURE_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.AJOUT_DECRET_APP_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.AJOUT_ORDO_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.AJOUT_RATIF_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.AJOUT_TM_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.AJOUT_TXT_TRANSPO_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.AJOUT_HABIL_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.MODIF_DECRET_PUB_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.MODIF_MESURE_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.MODIF_DECRET_APP_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.MODIF_ORDO_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.MODIF_RATIF_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.MODIF_TM_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.MODIF_TXT_TRANSPO_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.MODIF_HABIL_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.SUPPR_MESURE_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.SUPPR_DECRET_APP_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.SUPPR_ORDO_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.SUPPR_RATIF_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.SUPPR_TXT_TRANSPO_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.SUPPR_HABIL_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.PUBLI_STAT_EVENT);
        SET_EVENT_PAN.add(SolonEpgEventConstant.PARAM_PAN_UPDATE_EVENT);
    }

    @Override
    protected void loggerProcess(Event event) {
        String eventName = event.getName();
        if (AUDIT_LOGGER == null) {
            LOGGER.warn("Can not reach AuditLogger");
            return;
        }
        if (event.getContext() instanceof DocumentEventContext) {
            DocumentEventContext docCtx = (DocumentEventContext) event.getContext();
            DocumentModel model = docCtx.getSourceDocument();

            if (model != null) {
                LOGGER.debug("--------------------NotificationAuditEventLogger handleEvent() calling : " + model);
                String docType = model.getType();
                if (
                    DocumentEventTypes.DOCUMENT_CREATED.equals(eventName) ||
                    DocumentEventTypes.DOCUMENT_UPDATED.equals(eventName)
                ) {
                    if (SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE.equals(docType)) {
                        logFeuilleDeRoute(event, docCtx);
                    }
                } else if (
                    EventNames.afterCaseSentEvent.name().equals(eventName) &&
                    DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE.equals(docType)
                ) {
                    // affectation du dossier à un ministere
                    docCtx.setComment(
                        "Transfert du dossier à [" +
                        docCtx.getProperty("eventContextParticipants_type_FOR_ACTION") +
                        "]"
                    );
                    docCtx.setCategory(STEventConstant.CATEGORY_FEUILLE_ROUTE);
                    logDossier(event, docCtx);
                    defaultHandleEvent(event);
                } else if (SET_EVENT_DOSSIER.contains(eventName)) {
                    // log technique
                    if(eventName.equals(SolonEpgEventConstant.DELETE_FOLDER_FDD)) {
                        // log rep fond de dossier
                        logFDD(event, docCtx);
                    } else {
                        logDossier(event, docCtx);
                    }

                    // journal
                    defaultHandleEvent(event);
                }
            }
        } else {
            if (SET_EVENT_ADMIN.contains(eventName)) {
                defaultHandleEvent(event);
            } else if (SET_EVENT_PAN.contains(eventName)) {
                defaultHandleEvent(event);
            }
        }
    }

    /**
     * Log Event notification feuille route
     *
     * @param event
     * @param context
     */
    private void logFeuilleDeRoute(Event event, DocumentEventContext context) {
        String numeroQuestion = null;
        // récupération des dossiers via la feuille de route
        SolonEpgFeuilleRoute feuilleRoute = context.getSourceDocument().getAdapter(SolonEpgFeuilleRoute.class);
        List<String> dossierIds = feuilleRoute.getAttachedDocuments();
        if (dossierIds != null && dossierIds.size() > 0) {
            DocumentModel dossierDoc = context.getCoreSession().getDocument(new IdRef(dossierIds.get(0)));
            if (dossierDoc != null) {
                Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                if (dossier != null) {
                    numeroQuestion = dossier.getNumeroNor();
                }
            }
        }
        Object[] datas = new Object[] { event.getName(), context.getPrincipal().getName(), numeroQuestion, "", "" };
        NotificationMessageLogger.getInstance().logFeuilleDeRoute(event, LOGGER, datas);
    }

    /**
     * Log Event notification dossier
     *
     * @param event
     * @param context
     */
    private void logDossier(Event event, DocumentEventContext context) {
        DocumentModel model = context.getSourceDocument();
        Dossier dossier = model.getAdapter(Dossier.class);
        Object[] datas = new Object[] {
                event.getName(),
                context.getPrincipal().getName(),
                dossier.getMinistereResp(),
                dossier.getNomRespDossier() + " " + dossier.getPrenomRespDossier(),
                dossier.getNumeroNor()
        };
        NotificationMessageLogger.getInstance().logDossier(event, LOGGER, datas);
    }

    /**
     * Log Event notification fdd
     *
     * @param event
     * @param context
     */

    private void logFDD(Event event, DocumentEventContext context) {
        Object[] datas = new Object[] {
                event.getName(), // action
                context.getPrincipal().getName(), // user
                context.getSourceDocument(), // origine
                "", // auteur
                "", // num question
                "", // id auteur
        };
        NotificationMessageLogger.getInstance().logFondDossier(event, LOGGER, datas);
    }

    /**
     * Méthode utilisée pour récupérer les informations liées à un parapheur ou à un fond de Dossier.
     *
     * @param event
     * @param context
     * @return
     */
    protected Object[] getFileData(Event event, DocumentEventContext context) {
        DocumentModel model = context.getSourceDocument();
        // FileSolonEpg fichier = model.getAdapter(FileSolonEpg.class);
        return new Object[] {
            event.getName(),
            context.getPrincipal().getName(),
            context.getComment(),
            model.getParentRef(),
            ""
        };
    }
}
