package fr.dila.solonepg.core.service;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.mailbox.SolonEpgMailbox;
import fr.dila.solonepg.api.service.MailboxService;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STDossierLinkConstant;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.core.query.QueryUtils;

/**
 * Implémentation du service Mailbox pour SOLON EPG.
 * 
 * @author jtremeaux
 */
public class MailboxServiceImpl extends fr.dila.st.core.service.MailboxServiceImpl implements MailboxService {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Long getNumberOfDossierLinkIds(CoreSession session, SolonEpgMailbox mailBox) throws ClientException {

        SSPrincipal principal = (SSPrincipal) session.getPrincipal();
        Boolean isMesureNominativeReader = principal.getGroups().contains(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_READER);

        StringBuilder query = new StringBuilder("SELECT dl.ecm:uuid as id FROM ");

        if (!isMesureNominativeReader) {
            query.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
            query.append(" as d, ");
        }
        query.append(STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE);
        query.append(" as dl WHERE dl.ecm:parentId = ? AND dl.");
        query.append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX);
        query.append(":");
        query.append(DossierSolonEpgConstants.DOSSIER_LINK_CASE_LIFE_CYCLE_STATE_PROPERTY);
        query.append(" = ? AND dl.");
        query.append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX);
        query.append(":");
        query.append(DossierSolonEpgConstants.DELETED);
        query.append(" = 0 AND dl.");
        query.append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX);
        query.append(":");
        query.append(DossierSolonEpgConstants.ARCHIVE_DOSSIER_PROPERTY);
        query.append(" = 0 ");

        // restriction de visibilité des mesures nominatives
        if (!isMesureNominativeReader) {
            query.append(" AND d.ecm:uuid = dl.acslk:caseDocumentId AND d.");
            query.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
            query.append(":");
            query.append(DossierSolonEpgConstants.DOSSIER_MESURE_NOMINATIVE_PROPERTY);
            query.append(" = 0 ");
        }

        return QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(query.toString()), new Object[] { mailBox.getDocument().getId(),
                STDossier.DossierState.running.toString() });

    }
}
