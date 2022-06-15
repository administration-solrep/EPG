package fr.dila.solonepg.core.operation.livraison;

import static java.util.function.Predicate.isEqual;

import fr.dila.cm.mailbox.MailboxConstants;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.operation.STVersion;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.service.AbstractPersistenceDefaultComponent;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

/**
 * Operation pour nettoyer les postes qui ont plusieurs mailboxes
 *
 */
@STVersion(version = "4.0.0")
@Operation(id = EpgCleanMailboxesOperation.ID, category = STConstant.ORGANIGRAMME_BASE_FUNCTION_DIR)
public class EpgCleanMailboxesOperation extends AbstractPersistenceDefaultComponent {
    public static final String ID = "SolonEpg.Clean.Mailbox";

    private static final STLogger LOGGER = STLogFactory.getLog(EpgCleanMailboxesOperation.class);

    private static final String MAILBOX_DUPLICATE_QUERY =
        "SELECT TO_CHAR(MAILBOX_ID) FROM MAILBOX GROUP BY MAILBOX_ID HAVING COUNT(MAILBOX_ID) > 1";

    private static final String UFNXQL_ID_MAILBOX_QUERY =
        "SELECT m.ecm:uuid AS id FROM " +
        SolonEpgConstant.SOLON_EPG_MAILBOX_TYPE +
        " AS m WHERE m." +
        MailboxConstants.ID_FIELD +
        " = ?";

    @Context
    protected CoreSession session;

    @Context
    private NuxeoPrincipal principal;

    public EpgCleanMailboxesOperation() {
        this.session = null;
        this.principal = null;
    }

    @OperationMethod
    public void run() {
        if (!principal.isAdministrator()) {
            return;
        }
        getMailboxDuplicateNames().forEach(this::cleanDuplicateMailbox);
    }

    @SuppressWarnings("unchecked")
    private List<String> getMailboxDuplicateNames() {
        return apply(true, entityManager -> entityManager.createNativeQuery(MAILBOX_DUPLICATE_QUERY).getResultList());
    }

    private void cleanDuplicateMailbox(String mailboxId) {
        LOGGER.info(STLogEnumImpl.LOG_INFO_TEC, "Début du traitement de la corbeille en double : " + mailboxId);
        List<String> ids = QueryUtils.doUFNXQLQueryForIdsList(
            session,
            UFNXQL_ID_MAILBOX_QUERY,
            new String[] { mailboxId }
        );
        List<String> notEmptyMailboxes = ids
            .stream()
            .map(id -> ImmutablePair.of(id, QueryHelper.getFolderSize(session, id)))
            .filter(p -> p.getRight() != 0L)
            .sorted((p1, p2) -> p2.getRight().compareTo(p1.getRight()))
            .map(ImmutablePair::getLeft)
            .collect(Collectors.toList());

        final String idToKeep = CollectionUtils.isEmpty(notEmptyMailboxes) ? ids.get(0) : notEmptyMailboxes.get(0);

        // S'il y a plus d'une mailbox avec des enfants, on le déplace dans celle qu'on conserve
        if (notEmptyMailboxes.size() > 1) {
            List<DocumentRef> childrenRefsToCopy = notEmptyMailboxes
                .stream()
                .filter(isEqual(idToKeep).negate())
                .map(id -> QueryUtils.getChildren(session, id))
                .flatMap(DocumentModelList::stream)
                .map(DocumentModel::getRef)
                .collect(Collectors.toList());
            session.move(childrenRefsToCopy, new IdRef(idToKeep));
            LOGGER.info(
                STLogEnumImpl.LOG_INFO_TEC,
                "DossierLink " +
                StringUtils.join(childrenRefsToCopy, ", ") +
                " déplacé(s) dans la corbeille " +
                idToKeep
            );
        }

        DocumentRef[] refsToRemove = ids
            .stream()
            .filter(isEqual(idToKeep).negate())
            .map(IdRef::new)
            .toArray(DocumentRef[]::new);
        session.removeDocuments(refsToRemove);
        LOGGER.info(STLogEnumImpl.LOG_INFO_TEC, "Corbeille(s) supprimée(s) : " + StringUtils.join(refsToRemove, ", "));
        LOGGER.info(STLogEnumImpl.LOG_INFO_TEC, "Fin du traitement de la corbeille en double : " + mailboxId);
    }
}
