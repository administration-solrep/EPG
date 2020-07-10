package fr.dila.solonepg.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.schema.PrefetchInfo;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.CorbeilleService;
import fr.dila.st.api.constant.STDossierLinkConstant;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.util.StringUtil;

/**
 * Implémentation du service Corbeille de l'application SOLON EPG.
 * 
 * @author jtremeaux
 */
public class CorbeilleServiceImpl extends fr.dila.st.core.service.CorbeilleServiceImpl implements CorbeilleService {
	/**
	 * UID.
	 */
	private static final long	serialVersionUID	= -2392698015083550568L;
	
	private static final STLogger LOGGER = STLogFactory.getLog(CorbeilleServiceImpl.class);
	
	/**
	 * query : SELECT id FROM DossierLink WHERE caseDocumentId = ? AND deleted = 0 AND caseLifeCycleState = 'running' AND routingTaskType = ?
	 */
	private static final String DOSSIER_LINK_CE_RUNNING_QUERY = "SELECT dl.ecm:uuid as id FROM "
			+ STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE + " as dl WHERE dl.acslk:caseDocumentId = ? AND dl."
			+ DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX + ":" + DossierSolonEpgConstants.DELETED
			+ " = 0 AND dl." + DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX + ":" 
			+ DossierSolonEpgConstants.DOSSIER_LINK_CASE_LIFE_CYCLE_STATE_PROPERTY + " = 'running' AND dl." 
			+ DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX + ":"
			+ STDossierLinkConstant.DOSSIER_LINK_ROUTING_TASK_TYPE_PROPERTY + " = ?";

	@Override
	public List<DossierLink> findDossierLinks(final CoreSession session, final List<String> dossierIds)
			throws ClientException {

		// pas de test sur les droits, les documents non visible par l'utilisaeur ne seront pas chargé
		final StringBuilder query = new StringBuilder("SELECT dl.ecm:uuid as id FROM ");
		query.append(STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE);
		query.append(" as dl WHERE dl.acslk:caseDocumentId IN (");
		query.append(StringUtil.getQuestionMark(dossierIds.size()));
		query.append(") AND dl.");
		query.append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX);
		query.append(":");
		query.append(DossierSolonEpgConstants.DELETED);
		query.append(" = 0 ");

		final List<String> params = new ArrayList<String>();
		params.addAll(dossierIds);

		final List<DocumentModel> listDocs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE, query.toString(), params.toArray());

		final List<DossierLink> result = new ArrayList<DossierLink>();

		for (final DocumentModel documentModel : listDocs) {
			result.add(documentModel.getAdapter(DossierLink.class));
		}

		return result;
	}

	@Override
	public List<DocumentModel> findDossierLink(final CoreSession session, final String dossierId)
			throws ClientException {

		// pas de test sur les droits, les documents non visible par l'utilisaeur ne seront pas chargé
		final StringBuilder query = new StringBuilder(" SELECT dl.ecm:uuid as id FROM ");
		query.append(STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE);
		query.append(" as dl WHERE dl.acslk:caseDocumentId = ? AND dl.");
		query.append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX);
		query.append(":");
		query.append(DossierSolonEpgConstants.DELETED);
		query.append(" = 0 ");

		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE,
				query.toString(), new Object[] { dossierId });
	}

	@Override
	public boolean existsPourAvisCEStep(final CoreSession session, final String dossierId) throws ClientException {
		DocumentModel dossierLinkDoc = getPourAvisCeDossierLinkDoc(session, dossierId);
		return dossierLinkDoc != null;
	}
	
	@Override
	public DocumentModel getPourAvisCeDossierLinkDoc(final CoreSession session, final String dossierId) throws ClientException {
		Object[] params = new Object[] {dossierId, VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE};
		List<DocumentModel> dossiersLinksDocs = QueryUtils.doUnrestrictedUFNXQLQueryAndFetchForDocuments(session, 
				STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE, DOSSIER_LINK_CE_RUNNING_QUERY, params, 1, 0);
		if (dossiersLinksDocs != null && !dossiersLinksDocs.isEmpty()) {
			return dossiersLinksDocs.get(0);
		}
		return null;
	}
	
	@Override
	public Calendar getStartDateForRunningStep(final CoreSession session, final String dossierId, final String stepType) {
		final Calendar dateDebutEtapePourAvisCE = Calendar.getInstance();
		try {
		new UnrestrictedSessionRunner(session) {
			@Override
			public void run() throws ClientException {
				DocumentModel dossierLinkDoc = null;
				try {
					final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
					dossierLinkDoc = corbeilleService.getPourAvisCeDossierLinkDoc(session, dossierId);
				} catch (final ClientException e) {
					LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC, dossierId, e);
				}
				if (dossierLinkDoc != null) {
					final DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
					dateDebutEtapePourAvisCE.setTime(dossierLink.getDateCreation().getTime());
				}
			}
			
		}.runUnrestricted();
		} catch (ClientException ce) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC, dossierId, ce);
		}
		return dateDebutEtapePourAvisCE;
	}

	@Override
	public List<DocumentModel> findDossierLink(final CoreSession session, final Collection<String> dossierIds, PrefetchInfo prefetch)
			throws ClientException {
		if(dossierIds == null || dossierIds.isEmpty()){
			return Collections.emptyList();
		}
		// pas de test sur les droits, les documents non visible par l'utilisateur ne seront pas chargés
		final StringBuilder query = new StringBuilder(" SELECT dl.ecm:uuid as id FROM ");
		query.append(STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE);
		query.append(" as dl WHERE dl.acslk:caseDocumentId IN (");
		query.append(StringUtil.getQuestionMark(dossierIds.size()));
		query.append(") AND dl.");
		query.append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX);
		query.append(":");
		query.append(DossierSolonEpgConstants.DELETED);
		query.append(" = 0 ");

		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				query.toString(), dossierIds.toArray(), 0, 0, prefetch);
	}

	@Override
	public String getInfocentreQuery(final CoreSession session, final STDossier.DossierState dossierState,
			final Set<String> mailboxDocIds, final Set<String> mailboxIds) throws ClientException {

		final StringBuilder nxQuery = new StringBuilder();

		switch (dossierState) {
			case init:

				nxQuery.append(" SELECT d.ecm:uuid as id FROM Dossier as d ");
				nxQuery.append(" WHERE  d.dos:");
				nxQuery.append(DossierSolonEpgConstants.DOSSIER_POSTE_CREATOR_PROPERTY);
				nxQuery.append(" IN ('");
				nxQuery.append(StringUtils.join(mailboxIds, "','"));
				nxQuery.append("') ");

				nxQuery.append(" AND testAcl(d.ecm:uuid) = 1 ");

				this.lanceOuInitie(nxQuery);
				notArchiveQueryPart(nxQuery);
				notPublieQueryPart(nxQuery);

				break;
			case done:

				nxQuery.append(" SELECT DISTINCT d.ecm:uuid as id FROM Dossier as d, DossierLink as cl ");
				nxQuery.append(" WHERE d.cmdist:all_action_participant_mailboxes IN ('");
				nxQuery.append(StringUtils.join(mailboxIds, "','"));
				nxQuery.append("') ");

				nxQuery.append(" AND (( ");
				nxQuery.append("d.ecm:currentLifeCycleState = '");
				nxQuery.append(STDossier.DossierState.running.name());
				nxQuery.append("'");
				nxQuery.append(" AND (");
				nxQuery.append("(cl.ecm:parentId NOT IN ('");
				nxQuery.append(StringUtils.join(mailboxDocIds, "','"));
				nxQuery.append("') AND cl.");
				nxQuery.append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX);
				nxQuery.append(":");
				nxQuery.append(DossierSolonEpgConstants.DELETED);
				nxQuery.append(" = 0 AND d.ecm:uuid = cl.acslk:caseDocumentId) ");
				nxQuery.append(" ) ");
				nxQuery.append(" ) OR ( ");
				nxQuery.append(" d.ecm:currentLifeCycleState  = '");
				nxQuery.append(STDossier.DossierState.done.name());
				nxQuery.append("' AND (cl.acslk:caseDocumentId IS NULL OR cl.");
				nxQuery.append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX);
				nxQuery.append(":");
				nxQuery.append(DossierSolonEpgConstants.DELETED);
				nxQuery.append(" = 1) ");
				nxQuery.append(")) ");

				nxQuery.append(" AND testAcl(d.ecm:uuid) = 1 ");

				this.lanceOuInitie(nxQuery);
				notArchiveQueryPart(nxQuery);
				notPublieQueryPart(nxQuery);

				break;
			case running:

				nxQuery.append(" SELECT DISTINCT d.ecm:uuid as id FROM DossierLink as cl, Dossier as d ");
				nxQuery.append(" WHERE d.ecm:uuid = cl.acslk:caseDocumentId ");
				nxQuery.append(" AND cl.acslk:caseLifeCycleState = '");
				nxQuery.append(dossierState.name());
				nxQuery.append("' ");
				nxQuery.append(" AND cl.ecm:parentId IN ('");
				nxQuery.append(StringUtils.join(mailboxDocIds, "','"));
				nxQuery.append("') AND cl.");
				nxQuery.append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX);
				nxQuery.append(":");
				nxQuery.append(DossierSolonEpgConstants.DELETED);
				nxQuery.append(" = 0 ");

				nxQuery.append(" AND testAcl(d.ecm:uuid) = 1 ");

				this.lanceOuInitie(nxQuery);
				notArchiveQueryPart(nxQuery);
				notPublieQueryPart(nxQuery);

				break;
			default:
				break;
		}

		return nxQuery.toString();
	}

	private void lanceOuInitie(final StringBuilder nxQuery) {
		// récupérer uniquement les dossier initié et lancé
		nxQuery.append(" AND d." + DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
		nxQuery.append(":");
		nxQuery.append(DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY);
		nxQuery.append(" IN (");
		nxQuery.append(VocabularyConstants.STATUT_INITIE);
		nxQuery.append(",");
		nxQuery.append(VocabularyConstants.STATUT_LANCE);
		nxQuery.append(")");
	}

	private void notArchiveQueryPart(final StringBuilder nxQuery) {
		nxQuery.append(" AND d.dos:");
		nxQuery.append(DossierSolonEpgConstants.ARCHIVE_DOSSIER_PROPERTY);
		nxQuery.append(" = 0 ");
	}

	private void notPublieQueryPart(final StringBuilder nxQuery) {
		nxQuery.append(" AND d.dos:");
		nxQuery.append(DossierSolonEpgConstants.PUBLIE_DOSSIER_PROPERTY);
		nxQuery.append(" = 0 ");
	}

	@Override
	public DocumentModel getCaseLinkPourInitialisation(final CoreSession session, final DocumentModel dossierDoc)
			throws ClientException {
		// FEV 509 - On affiche toujours le dossier qu'on vient de créer, même si l'utilisateur crée un dosiser pour une
		// autre entité
		final StringBuilder queryBuilder = new StringBuilder("SELECT dl.ecm:uuid as id FROM DossierLink as dl WHERE ");
		queryBuilder.append(" dl.acslk:caseDocumentId = ? ");
		queryBuilder.append(" AND dl.acslk:routingTaskType = ? ");
		queryBuilder.append(" AND dl.");
		queryBuilder.append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(DossierSolonEpgConstants.DELETED);
		queryBuilder.append(" = 0 ");

		final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, "DossierLink",
				queryBuilder.toString(), new Object[] { dossierDoc.getId(),
						VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION }, 1, 0);

		if (list.size() != 1) {
			return null;
		}

		return list.get(0);
	}

}
