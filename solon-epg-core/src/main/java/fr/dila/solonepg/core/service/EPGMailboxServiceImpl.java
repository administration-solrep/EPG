package fr.dila.solonepg.core.service;

import static fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_READER;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.mailbox.SolonEpgMailbox;
import fr.dila.solonepg.api.service.EPGMailboxService;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.core.util.SSMailboxUtils;
import fr.dila.st.api.constant.STDossierLinkConstant;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.core.service.MailboxServiceImpl;
import fr.sword.naiad.nuxeo.commons.core.util.StringUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Implémentation du service Mailbox pour SOLON EPG.
 *
 * @author jtremeaux
 */
public class EPGMailboxServiceImpl extends MailboxServiceImpl implements EPGMailboxService {

    public EPGMailboxServiceImpl() {
        super();
    }

    @Override
    public Long getNumberOfDossierLinkIds(CoreSession session, SolonEpgMailbox mailBox) {
        SSPrincipal principal = (SSPrincipal) session.getPrincipal();
        boolean isMesureNominativeReader = principal.isMemberOf(DOSSIER_MESURE_NOMINATIVE_READER);

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

        return QueryUtils.doCountQuery(
            session,
            QueryUtils.ufnxqlToFnxqlQuery(query.toString()),
            new Object[] { mailBox.getDocument().getId(), STDossier.DossierState.running.toString() }
        );
    }

    @Override
    public String getMailboxListQuery(CoreSession session) {
        final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
        Set<String> postesId = SSMailboxUtils.getSelectedPostes((SSPrincipal) session.getPrincipal(), null, null);
        Set<String> mailboxPosteIds = mailboxPosteService.getMailboxPosteIdSetFromPosteIdSet(postesId);
        List<Mailbox> mailBoxes = this.getMailbox(session, mailboxPosteIds);
        Set<String> mailBoxDocId = mailBoxes
            .stream()
            .map(mailbox -> mailbox.getDocument().getId())
            .collect(Collectors.toSet());

        if (mailBoxDocId.isEmpty()) {
            return "('0')";
        }

        return "(" + StringUtil.join(mailBoxDocId, ",", "'") + ")";
    }
}
