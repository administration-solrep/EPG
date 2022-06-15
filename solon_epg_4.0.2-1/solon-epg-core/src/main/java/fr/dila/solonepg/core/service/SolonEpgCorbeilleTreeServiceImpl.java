package fr.dila.solonepg.core.service;

import static fr.dila.ss.core.service.SSServiceLocator.getMailboxPosteService;
import static fr.dila.ss.core.util.SSMailboxUtils.getSelectedPostes;
import static fr.dila.st.core.service.STServiceLocator.getMailboxService;
import static java.util.stream.Collectors.toMap;

import fr.dila.solonepg.api.service.SolonEpgCorbeilleTreeService;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.enums.TypeRegroupement;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.core.util.SSMailboxUtils;
import fr.dila.st.api.constant.STDossierLinkConstant;
import fr.dila.st.core.util.StringHelper;
import fr.sword.naiad.nuxeo.commons.core.util.StringUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.IterableQueryResult;

public class SolonEpgCorbeilleTreeServiceImpl implements SolonEpgCorbeilleTreeService {

    @Override
    public Map<String, Integer> getCorbeilleTree(CoreSession session, TypeRegroupement regroupement) {
        switch (regroupement) {
            case PAR_TYPE_ETAPE:
                return getCorbeilleTreeParTypeEtape(session);
            case PAR_TYPE_ACTE:
                return getCorbeilleTreeParActe(session);
            default:
                return getCorbeilleTreeParPoste(session);
        }
    }

    /**
     * Retourne une map avec le nombre de dossierLink par poste de l'utilisateur
     *
     * @param session
     * @return
     */
    private Map<String, Integer> getCorbeilleTreeParPoste(CoreSession session) {
        List<String> posteIds = new ArrayList<>(
            SSMailboxUtils.getSelectedPostes((SSPrincipal) session.getPrincipal(), null, null)
        );
        if (posteIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<String> mailboxIds = getMailboxPosteService().toMailboxIds(posteIds);
        Map<String, String> mapIds = getMailboxService().getMapMailboxDocIdsIds(session, mailboxIds);

        String query =
            "SELECT dl.ecm:parentId as id, count(dl.ecm:uuid) as count FROM " +
            STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE +
            " AS dl, Dossier AS d WHERE dl.acslk:caseDocumentId = d.ecm:uuid" +
            " AND dl.acslk:deleted = 0 AND dl.ecm:parentId IN (" +
            StringHelper.getQuestionMark(mailboxIds.size()) +
            ") AND d.dos:statut = '2' AND testDossierAcl(dl.ecm:uuid) = 1 GROUP BY dl.ecm:parentId";

        return doQueryCorbeille(session, query, mapIds.keySet().toArray())
            .entrySet()
            .stream()
            .collect(
                toMap(
                    e -> StringUtils.removeStart(mapIds.get(e.getKey()), SSConstant.MAILBOX_POSTE_ID_PREFIX),
                    Map.Entry::getValue
                )
            );
    }

    /**
     * Retourne une map avec le nombre de dossierLink par type acte
     *
     * @param session
     * @return
     */
    private Map<String, Integer> getCorbeilleTreeParActe(CoreSession session) {
        Collection<String> mailboxs = getMlbxPosteDocIds(session);
        if (mailboxs.isEmpty()) {
            return Collections.emptyMap();
        }
        String sb =
            "SELECT d.dos:typeActe as id, count(d.ecm:uuid) as count FROM Dossier as d, DossierLink as dl" +
            " WHERE d.ecm:uuid = dl.acslk:caseDocumentId" +
            " AND dl.ecm:parentId IN (" +
            StringUtil.join(mailboxs, ",", "'") +
            ") AND dl.acslk:deleted = 0 AND testDossierAcl(d.ecm:uuid) = 1 AND d.dos:statut = '2' GROUP BY d.dos:typeActe";

        return doQueryCorbeille(session, sb);
    }

    private Map<String, Integer> doQueryCorbeille(CoreSession session, String query, Object[] params) {
        try (IterableQueryResult results = QueryUtils.doUFNXQLQuery(session, query, params)) {
            return StreamSupport
                .stream(results.spliterator(), false)
                .map(this::createPairIdCount)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        }
    }

    private Map<String, Integer> doQueryCorbeille(CoreSession session, String query) {
        return doQueryCorbeille(session, query, null);
    }

    private ImmutablePair<String, Integer> createPairIdCount(Map<String, Serializable> row) {
        return ImmutablePair.of((String) row.get("id"), Integer.valueOf((String) row.get("count")));
    }

    /**
     * Retourne une map avec le nombre de dossierLink par type étape
     *
     * @param session
     * @return
     */
    private Map<String, Integer> getCorbeilleTreeParTypeEtape(CoreSession session) {
        Collection<String> mailboxs = getMlbxPosteDocIds(session);
        if (mailboxs.isEmpty()) {
            return Collections.emptyMap();
        }
        String sb =
            "SELECT dl.acslk:routingTaskType as id, count(dl.acslk:caseDocumentId) as count" +
            " FROM DossierLink as dl, Dossier as d WHERE d.ecm:uuid = dl.acslk:caseDocumentId" +
            " AND dl.ecm:parentId IN (" +
            StringUtil.join(mailboxs, ",", "'") +
            ") AND dl.acslk:deleted = 0 AND d.dos:statut = '2'" +
            " AND testDossierAcl(dl.ecm:uuid) = 1 GROUP BY dl.acslk:routingTaskType";

        return doQueryCorbeille(session, sb);
    }

    private Collection<String> getMlbxPosteDocIds(CoreSession session) {
        List<String> posteIds = new ArrayList<>(getSelectedPostes((SSPrincipal) session.getPrincipal(), null, null));
        return getMailboxPosteService().getMailboxPosteDocIds(session, posteIds);
    }
}
