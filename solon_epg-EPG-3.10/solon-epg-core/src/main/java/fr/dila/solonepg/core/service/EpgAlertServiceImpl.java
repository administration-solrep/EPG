package fr.dila.solonepg.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;
import javax.security.auth.login.LoginException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.repository.Repository;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

import fr.dila.solonepg.api.alert.SolonEpgAlert;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.st.api.event.batch.BatchLoggerModel;
import fr.dila.solonepg.api.service.EpgAlertService;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepg.core.util.ExcelUtil;
import fr.dila.st.api.alert.Alert;
import fr.dila.st.api.constant.STAlertConstant;
import fr.dila.st.api.constant.STConfigConstants;
import fr.dila.st.api.constant.STLifeCycleConstant;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.requeteur.RequeteExperte;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.api.service.RequeteurService;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.service.SuiviBatchService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STAlertServiceImpl;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;

/**
 * @author arolin
 */
public class EpgAlertServiceImpl extends STAlertServiceImpl implements EpgAlertService {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * délai laissé à l'utilisateur avant la désactivation de l'alerte en jour.
     */
    private final int DELAI_MAX_CONFIRMATION_LIEN = 7;
    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog(EpgAlertServiceImpl.class);
    /**
     * Logger formalisé en surcouche du logger apache/log4j 
     */
    private static final STLogger LOG = STLogFactory.getLog(EpgAlertServiceImpl.class);

    @Override
    public void sendMailsConfirmation(final CoreSession session, BatchLoggerModel batchLoggerModel, Long nbError) throws ClientException {
    	final long startTime = Calendar.getInstance().getTimeInMillis();
    	long nbAlertSent = 0L;
    	final Calendar dateEnvoiConfirmation = Calendar.getInstance();
        final ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
        final Long delaiEnvoiJour = parametreApplicationService.getParametreApplicationDocument(session).getDelaiEnvoiMailMaintienAlerte();
        dateEnvoiConfirmation.add(Calendar.DAY_OF_MONTH, -delaiEnvoiJour.intValue());
        final String literalDate = DateLiteral.dateFormatter.print(dateEnvoiConfirmation.getTimeInMillis());

        final STParametreService paramService = STServiceLocator.getSTParametreService();
        
        String serverUrl;
        try {
            serverUrl = paramService.getParametreValue(session, STParametreConstant.ADRESSE_URL_APPLICATION);
            if (serverUrl == null) {
                throw new ClientException();
            } else {
                // suppression de login.jsp si present
                serverUrl = serverUrl.replace("login.jsp", "");
            }
        } catch (final ClientException e) {
            LOGGER.error("Erreur de récupération de l'url de l'application dans les parametres", e);
            // utilisation de l'url de la config
            final ConfigService configService = STServiceLocator.getConfigService();
            serverUrl = configService.getValue(STConfigConstants.SERVER_URL);
            nbError++;
        }

        // on effectue la requete pour récupérer les documents alert dont l'état est activé, et dont on a pas envoyé de mail depuis 15 jours
        final StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(STAlertConstant.ALERT_DOCUMENT_TYPE);
        query.append(" WHERE ");
        query.append(STAlertConstant.ALERT_PROP_IS_ACTIVATED);
        query.append(" = 1 and ").append(STAlertConstant.ALERT_PROP_DATE_DEMANDE_CONFIRMATION);
        query.append(" < TIMESTAMP '");
        query.append(literalDate);
        query.append(" ' ");
        query.append(" and ecm:currentLifeCycleState != '" + STLifeCycleConstant.DELETED_STATE + "'");
        query.append(" and ecm:isProxy = 0 ");

        try {
            final List<DocumentModel> listAlert = session.query(query.toString());
            for (final DocumentModel alertDoc : listAlert) {
                final StringBuilder sbTexteAlerte = new StringBuilder();
                // pour chaque alerte on envoie un mail à l'utilisateur
                final SolonEpgAlert alert = alertDoc.getAdapter(SolonEpgAlert.class);

                // Détermine l'email au destinataire du mail
                final UserManager userManager = STServiceLocator.getUserManager();
                final DocumentModel destUserModel = userManager.getUserModel(DublincoreSchemaUtils.getCreator(alertDoc));
                if (destUserModel == null) {
                    // l'utilisateur n'existe pas ou plus, on supprime l'alerte
                    LOG.info(session, EpgLogEnumImpl.DEL_ALERT_EPG_TEC, alertDoc);
                    session.removeDocument(alertDoc.getRef());
                } else {
                    final STUser destUser = destUserModel.getAdapter(STUser.class);
                    final List<STUser> users = new ArrayList<STUser>();
                    users.add(destUser);

                    final String mailObjet = paramService.getParametreValue(session, SolonEpgParametreConstant.OBJET_MAIL_CONFIRM_ALERT);
                    final String mailTexte = paramService.getParametreValue(session, SolonEpgParametreConstant.TEXTE_MAIL_CONFIRM_ALERT);
                    final String url = serverUrl + "/alerte/confirmAlert.jsp?alertId=" + alert.getDocument().getId();

                    final String title = alert.getTitle();

                    sbTexteAlerte.append("<a href=\"");
                    sbTexteAlerte.append(url);
                    sbTexteAlerte.append("\" target=\"_blank\">");
                    sbTexteAlerte.append(title);
                    sbTexteAlerte.append("</a>");

                    final Map<String, Object> mailTexteMap = new HashMap<String, Object>();
                    mailTexteMap.put("nom_alerte", alert.getTitle());
                    mailTexteMap.put("lien", sbTexteAlerte);

                    // Envoie l'email au destinataire de la délégation
                    final STMailService mailService = STServiceLocator.getSTMailService();
                    mailService.sendTemplateHtmlMailToUserList(session, users, mailObjet, mailTexte, mailTexteMap);
                    nbAlertSent++;

                    // on met à jour le document alert
                    alert.setDateDemandeConfirmation(Calendar.getInstance());
                    alert.setHasDemandeConfirmation(true);
                    session.saveDocument(alert.getDocument());
                }
            }
            session.save();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage());
            throw new ClientException(e);
        }
        final long endTime = Calendar.getInstance().getTimeInMillis();
        final SuiviBatchService suiviBatchService = STServiceLocator.getSuiviBatchService();
        try {
        	suiviBatchService.createBatchResultFor(batchLoggerModel, "Envoi des méls de confirmation",nbAlertSent, endTime-startTime);
        } catch (ClientException e) {
        	LOG.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC,e);
        }
    }

    @Override
    public void desactivationAlerteNonConfirme(final CoreSession session) throws ClientException {
        final Calendar dateEnvoiConfirmation = Calendar.getInstance();
        dateEnvoiConfirmation.add(Calendar.DAY_OF_MONTH, -DELAI_MAX_CONFIRMATION_LIEN);
        final String literalDate = DateLiteral.dateFormatter.print(dateEnvoiConfirmation.getTimeInMillis());

        // on effectue la requete pour récupérer les documents alert dont l'état est activé, et dont on a envoyé un mail de cofnirmation depuis plus d'une semaine.
        final StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(STAlertConstant.ALERT_DOCUMENT_TYPE);
        query.append(" WHERE ");
        query.append(STAlertConstant.ALERT_PROP_IS_ACTIVATED);
        query.append(" = 1 and ");
        query.append(STAlertConstant.ALERT_PROP_HAS_DEMANDE_CONFIRMATION);
        query.append(" = 1 and ");
        query.append(STAlertConstant.ALERT_PROP_DATE_DEMANDE_CONFIRMATION);
        query.append(" < TIMESTAMP '");
        query.append(literalDate);
        query.append(" '");
        query.append(" and ecm:currentLifeCycleState != '" + STLifeCycleConstant.DELETED_STATE + "'");
        query.append(" and ecm:isProxy = 0 ");

        final List<DocumentModel> listAlert = session.query(query.toString());
        for (final DocumentModel alertDoc : listAlert) {
            // on désactive les documents alert
            final SolonEpgAlert alert = alertDoc.getAdapter(SolonEpgAlert.class);
            alert.setIsActivated(false);
            alert.setHasDemandeConfirmation(false);
            session.saveDocument(alert.getDocument());
        }
        session.save();
    }

    @Override
    public String confirmationAlerte(final String alertId) throws ClientException {
        String message = "";
        if (alertId == null || alertId.isEmpty()) {
            message = "identifiant de l'alerte à confirmer vide";
            LOGGER.info(message);
            return message;
        }
        CoreSession session = null;
        try {
            Framework.login();
            final RepositoryManager mgr = Framework.getService(RepositoryManager.class);
            Repository repository = null;
            if (mgr != null) {
                repository = mgr.getDefaultRepository();
            }
            if (repository != null) {
                
                session = repository.open();
                
                if(session != null){
                    // récupération du document Alert et enregistrement de la modifcation
                    final DocumentModel docModel = session.getDocument(new IdRef(alertId));
                    final SolonEpgAlert alert = docModel.getAdapter(SolonEpgAlert.class);
                    alert.setHasDemandeConfirmation(false);
                    session.saveDocument(alert.getDocument());
                    message = "L'alerte <i>'" + alert.getTitle() + "'</i> est maintenue.";
                    LOGGER.debug("Confirmation de l'alerte");                    
                }
                
            }

        } catch (final LoginException e) {
            LOGGER.error("Problème de login pour la session de confirmation de l'alerte. " + e.getMessage());
        } catch (final Exception e) {
            LOGGER.error("Erreur de session de confirmation de l'alerte. " + e.getMessage());
        } finally {
            if (session != null) {
                Repository.close(session);
            }
        }
        return message;
    }

    @Override
    public Boolean sendMail(CoreSession session, Alert alert) throws ClientException {
        RequeteExperte requeteExperte = alert.getRequeteExperte(session);
        if (requeteExperte == null){
            LOGGER.error("pas de requête trouvée pour l'alerte !");
            return false;
        }
        // Chargement des services
        final STMailService mailService = STServiceLocator.getSTMailService();
        final STUserService sTUserService = STServiceLocator.getSTUserService();
        final RequeteurService requeteurService = STServiceLocator.getRequeteurService();
        final STUserService userService = STServiceLocator.getSTUserService();
        final STParametreService paramService = STServiceLocator.getSTParametreService();

        String patternQueryBeginning = "ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d";
        
        //récupération du nom du créateur de l'alerte
        String login = alert.getNameCreator();
        String nomCreateur = sTUserService.getUserFullName(login);
        String melCreateur = sTUserService.getUserInfo(login, "m");
        String title = alert.getTitle();

        //récupération des adresses mails des destinataires de l'alerte
        List<String> recipients = userService.getEmailAddressFromUserList(alert.getRecipients());
        recipients.addAll(alert.getExternalRecipients());
        
        //récupération de l'objet et du titre du mail
        String object =  paramService.getParametreValue(session, SolonEpgParametreConstant.OBJET_MAIL_ALERT);
        // On change la variable nor_dossier pour afficher le nor du dossier
        final Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("titre_alerte", title);
        object = StringUtil.renderFreemarker(object, paramMap);
        String texte = paramService.getParametreValue(session, SolonEpgParametreConstant.TEXTE_MAIL_ALERT);
        
        StringBuilder sb = new StringBuilder();
        sb.append(texte);
        sb.append("\n\nTitre de l'alerte : ");
        sb.append(title);
        sb.append("\n\nCréateur de l'alerte : ");
        sb.append(nomCreateur);
        sb.append("\n\nAdresse mél du créateur de l'alerte : ");
        sb.append(melCreateur);
        sb.append("\n\nFréquence de l'alerte (en jour) : ");
        sb.append(alert.getPeriodicity());
        sb.append("\n\nDate de fin de validité de l'alerte : ");
        sb.append(DateUtil.formatDDMMYYYYSlash(alert.getDateValidityEnd()));
        String content = sb.toString();
        
        //récupération des dossiers de la requête
        List<DocumentModel> dossiers = new ArrayList<DocumentModel>();
        String whereClause = requeteExperte.getWhereClause();
        if(StringUtils.isEmpty(whereClause)){
            LOGGER.info("la requête contenue par la requête est vide !");
        } else if(StringUtils.contains(whereClause, patternQueryBeginning)){
            //on execute la requete contenu dans la whereclause
            DocumentRef[] refs = QueryUtils.doUFNXQLQueryForIds(session, whereClause, null);
            dossiers = session.getDocuments(refs);
        } else {
            //on construit puis on execute la requete
            dossiers = requeteurService.query(session,requeteExperte);
        }
        
        String nomFichier = "Resultat_dossier_alerte.xls";
        DataSource fichierExcelResultat = ExcelUtil.creationListDossierExcel(dossiers);
        //envoi du mail
        Boolean isSent = true;
        try {
            mailService.sendMailWithAttachement(recipients, object, content, nomFichier, fichierExcelResultat);
        } catch (Exception e) {
            LOGGER.error("erreur lors de l'envoi de mail lors du batch d'alerte", e);
            isSent = false;
        }
        return isSent;
    }
}
