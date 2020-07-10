package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;

import fr.dila.ecm.platform.routing.api.DocumentRouteElement.ElementLifeCycleState;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.client.FeuilleRouteSqueletteDTO;
import fr.dila.solonepg.web.client.FeuilleRouteSqueletteDTOImpl;
import fr.dila.solonepg.web.suivi.EspaceSuiviTreeBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.service.STLockService;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.contentview.CorePageProviderUtil;
import fr.dila.st.web.contentview.OrderByDistinctQueryCorrector;
import fr.dila.st.web.lock.STLockActionsBean;

public class SqueletteFDRPageProvider extends ModeleFDRPageProvider {

	private static final long serialVersionUID = 4380216140738912427L;

	private transient Map<String, FeuilleRouteSqueletteDTO> mapFDRIdDTO;

	@Override
	protected void fillCurrentPageMapList(final CoreSession coreSession) throws ClientException {
		currentItems = new ArrayList<Map<String, Serializable>>();

		if (query.contains(EspaceSuiviTreeBean.DEFAULT_QUERY)) {
			// requete vide on fait rien
			resultsCount = 0;
			return;
		}

		if (!query.startsWith(FlexibleQueryMaker.KeyCode.UFXNQL.key)) {
			query = QueryUtils.ufnxqlToFnxqlQuery(query);
		}

		resultsCount = QueryUtils.doCountQuery(coreSession, query, paramList.toArray());

		final Map<String, Serializable> props = getProperties();
		final SSPrincipal ssPrincipal = (SSPrincipal) props.get(SS_PRINCIPAL);

		final STLockActionsBean stLockActions = (STLockActionsBean) props.get(ST_LOCK_ACTIONS);

		final STLockService stLockService = STServiceLocator.getSTLockService();

		final STUserService sTUserService = STServiceLocator.getSTUserService();
		final ActeService acteService = SolonEpgServiceLocator.getActeService();

		currentItems = new ArrayList<Map<String, Serializable>>();
		mapFDRIdDTO = new HashMap<String, FeuilleRouteSqueletteDTO>();

		if (resultsCount > 0) {

			Boolean deleter = Boolean.FALSE;

			// Les administrateurs fonctionnels ont le droit d'écriture sur tous les modèles
			final List<String> groups = ssPrincipal.getGroups();
			if (groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_VALIDATOR)) {
				deleter = true;
			}

			final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(coreSession, null, query,
					paramList.toArray(), pageSize, offset);

			for (final DocumentModel documentModel : docs) {
				final SolonEpgFeuilleRoute feuilleRoute = documentModel.getAdapter(SolonEpgFeuilleRoute.class);
				final FeuilleRouteSqueletteDTO squeletteFdrDTO = new FeuilleRouteSqueletteDTOImpl();
				squeletteFdrDTO
						.setAuteur(computeFullName(DublincoreSchemaUtils.getCreator(documentModel), sTUserService));
				squeletteFdrDTO.setDateModification(
						computeDateModification(DublincoreSchemaUtils.getModifiedDate(documentModel)));

				if (deleter) {
					squeletteFdrDTO.setDeleter(Boolean.TRUE);
				} else {
					squeletteFdrDTO.setDeleter(isDeleter(feuilleRoute, stLockActions, groups));
				}

				squeletteFdrDTO.setId(documentModel.getId());
				squeletteFdrDTO.setEtat(computeEtat(documentModel.getCurrentLifeCycleState()));
				squeletteFdrDTO.setDocIdForSelection(documentModel.getId());
				squeletteFdrDTO.setTitle(documentModel.getTitle());
				squeletteFdrDTO.setTypeActe(computeTypeActe(feuilleRoute.getTypeActe(), acteService));

				mapFDRIdDTO.put(documentModel.getId(), squeletteFdrDTO);

				currentItems.add(squeletteFdrDTO);
			}

			computeLockInfo(coreSession, stLockService);
		}
	}

	private void computeLockInfo(final CoreSession coreSession, final STLockService stLockService)
			throws ClientException {
		final Map<String, String> lockedFDRInfos = stLockService.extractLockedInfo(coreSession, mapFDRIdDTO.keySet());
		for (final Map.Entry<String, String> lockFDRInfo : lockedFDRInfos.entrySet()) {
			final String idFdr = lockFDRInfo.getKey();
			final FeuilleRouteSqueletteDTO squeletteFdrDTO = mapFDRIdDTO.get(idFdr);
			if (squeletteFdrDTO != null) {
				// info sur le lock
				squeletteFdrDTO.setLockInfo(lockFDRInfo.getValue());
			}
		}
	}

	@Override
	protected void buildQuery() {
		SortInfo[] sortArray = getSortInfosWithAdditionalSort();

		paramList = new ArrayList<Object>();

		final Map<String, Serializable> props = getProperties();

		final StringBuilder queryBuilder = new StringBuilder(
				"SELECT f.ecm:uuid as id FROM FeuilleRouteSquelette as f WHERE f.ecm:parentId = ? ");

		final String currentDocumentId = (String) props.get(CURRENT_DOCUMENT_ID);

		paramList.add(currentDocumentId);

		queryBuilder.append(" AND f.ecm:currentLifeCycleState IN (?, ?) ");
		paramList.add(DRAFT);
		paramList.add(VALIDATED);

		queryBuilder.append(" ");

		final String sortInfo = NXQLQueryBuilder.getSortClause(sortArray);

		queryBuilder.append(sortInfo);

		final String newQuery = OrderByDistinctQueryCorrector.correct(queryBuilder.toString(), sortArray);

		if (newQuery != null && !newQuery.equals(query)) {
			// query has changed => refresh
			if (query != null) {
				refresh();
			}
			query = newQuery;
		}
	}

	private String computeTypeActe(final String typeActe, final ActeService acteService) {
		if (StringUtils.isNotBlank(typeActe)) {
			return acteService.getActe(typeActe).getLabel();
		}
		return null;
	}

	private String computeEtat(final String currentLifeCycleState) {
		if (ElementLifeCycleState.draft.name().equals(currentLifeCycleState)) {
			return DRAFT;
		} else if (ElementLifeCycleState.validated.name().equals(currentLifeCycleState)) {
			return VALIDATED;
		}

		return null;
	}
}
