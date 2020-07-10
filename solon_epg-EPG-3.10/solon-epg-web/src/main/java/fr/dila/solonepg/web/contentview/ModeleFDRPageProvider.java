package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.ClientRuntimeException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageSelection;
import org.nuxeo.ecm.platform.query.api.PageSelections;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;
import org.nuxeo.ecm.platform.usermanager.UserManager;

import fr.dila.ecm.platform.routing.api.DocumentRouteElement.ElementLifeCycleState;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.web.client.FeuilleRouteDTO;
import fr.dila.solonepg.web.client.FeuilleRouteDTOImpl;
import fr.dila.solonepg.web.converter.OrganigrammeMinIdToEditionConverter;
import fr.dila.solonepg.web.suivi.EspaceSuiviTreeBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.FeuilleRouteModelService;
import fr.dila.ss.core.groupcomputer.MinistereGroupeHelper;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.service.STLockService;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.contentview.HiddenColumnPageProvider;
import fr.dila.st.web.lock.STLockActionsBean;

public class ModeleFDRPageProvider extends AbstractDTOFiltrableProvider implements HiddenColumnPageProvider {

    private static final long serialVersionUID = 2847175638025543949L;

    protected static final String CURRENT_DOCUMENT_ID = "currentDocumentId";
    protected static final String VALIDATED = "validated";
    private static final String REQUESTED = "requested";
    protected static final String DRAFT = "draft";
    protected static final String ST_LOCK_ACTIONS = "stLockActions";
    protected static final Object SS_PRINCIPAL = "ssPrincipal";
    protected static final Object CORE_SESSION = "coreSession";

	private static final String VALIDEE_KEY = "currentLifeCycleState";

    private Map<String, FeuilleRouteDTO> mapFDRIdDTO;

	protected List<Object> paramList;

	private int filtrableCount;

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

        final OrganigrammeMinIdToEditionConverter converter = new OrganigrammeMinIdToEditionConverter();

        currentItems = new ArrayList<Map<String, Serializable>>();
        mapFDRIdDTO = new HashMap<String, FeuilleRouteDTO>();

        if (resultsCount > 0) {

            final List<String> groups = ssPrincipal.getGroups();

            final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(coreSession, null, query, paramList.toArray(), pageSize,
                    offset);

            for (final DocumentModel documentModel : docs) {
                final SolonEpgFeuilleRoute feuilleRoute = documentModel.getAdapter(SolonEpgFeuilleRoute.class);
                final FeuilleRouteDTO feuilleRouteDTO = new FeuilleRouteDTOImpl();
                feuilleRouteDTO.setAuteur(computeFullName(DublincoreSchemaUtils.getCreator(documentModel), sTUserService));
                feuilleRouteDTO.setDateModification(computeDateModification(DublincoreSchemaUtils.getModifiedDate(documentModel)));

                feuilleRouteDTO.setDeleter(isDeleter(feuilleRoute, stLockActions, groups));

                feuilleRouteDTO.setEtat(computeEtat(documentModel.getCurrentLifeCycleState(), feuilleRoute.isDemandeValidation()));
                feuilleRouteDTO.setId(documentModel.getId());
                feuilleRouteDTO.setDocIdForSelection(documentModel.getId());
                feuilleRouteDTO.setMinistere(computeMinistere(feuilleRoute.getMinistere(), converter));
                feuilleRouteDTO.setNumero(feuilleRoute.getNumero());
                feuilleRouteDTO.setTitle(documentModel.getTitle());

                feuilleRouteDTO.setDefault(feuilleRoute.isFeuilleRouteDefautGlobal() || feuilleRoute.isFeuilleRouteDefaut());

                mapFDRIdDTO.put(documentModel.getId(), feuilleRouteDTO);

                currentItems.add(feuilleRouteDTO);

            }

            computeLockInfo(coreSession, stLockService);

        }
    }
    
	private void computeLockInfo(final CoreSession coreSession, final STLockService stLockService)
			throws ClientException {
		final Map<String, String> lockedFDRInfos = stLockService.extractLockedInfo(coreSession, mapFDRIdDTO.keySet());
		for (final Map.Entry<String, String> lockFDRInfo : lockedFDRInfos.entrySet()) {
			final String idFdr = lockFDRInfo.getKey();
			final FeuilleRouteDTO feuilleRouteDTO = mapFDRIdDTO.get(idFdr);
			if (feuilleRouteDTO != null) {
				// info sur le lock
				feuilleRouteDTO.setLockInfo(lockFDRInfo.getValue());
			}
		}
	}

	@Override
	protected void buildQuery() {
		try {
			SortInfo[] sortArray = getSortInfosWithAdditionalSort();

			paramList = new ArrayList<Object>();

			final Map<String, Serializable> props = getProperties();

			final StringBuilder queryBuilder = new StringBuilder(
					"SELECT DISTINCT f.ecm:uuid as id FROM FeuilleRoute as f, RouteStep as s WHERE f.ecm:parentId = ? AND f.ecm:uuid = s.rtsk:documentRouteId ");

			final String currentDocumentId = (String) props.get(CURRENT_DOCUMENT_ID);
			if (currentDocumentId == null) {
				final FeuilleRouteModelService feuilleRouteModelService = SSServiceLocator.getFeuilleRouteModelService();
				DocumentModel currentDocument = feuilleRouteModelService.getFeuilleRouteModelFolder((CoreSession) props.get(CORE_SESSION));
				paramList.add(currentDocument.getId());
			} else {
				paramList.add(currentDocumentId);
			}

			final SSPrincipal ssPrincipal = (SSPrincipal) props.get(SS_PRINCIPAL);

			// Si l'utilisateur est administrateur fonctionnel, il voit tous les
			// modèles
			final List<String> groups = ssPrincipal.getGroups();
			final boolean isAdminFonctionnel = groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_VALIDATOR);

			if (!isAdminFonctionnel) {
				queryBuilder.append(" AND (f.");
				queryBuilder.append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX);
				queryBuilder.append(":");
				queryBuilder.append(STSchemaConstant.FEUILLE_ROUTE_MINISTERE_PROPERTY);
				queryBuilder.append(" IS NULL ");

				/*
				 * Si l'utilisateur est administrateur ministériel, il voit les
				 * modèles de son ministère et les modèles publics.
				 */
				final Set<String> ministereSet = ssPrincipal.getMinistereIdSet();
				final List<String> orCondition = new ArrayList<String>();
				if (ministereSet != null && !ministereSet.isEmpty()) {
					for (final String ministere : ministereSet) {
						orCondition.add(ministere);
					}
					queryBuilder.append(" OR f.");
					queryBuilder.append(STSchemaConstant.FEUILLE_ROUTE_SCHEMA_PREFIX);
					queryBuilder.append(":");
					queryBuilder.append(STSchemaConstant.FEUILLE_ROUTE_MINISTERE_PROPERTY);
					queryBuilder.append(" IN (");
					queryBuilder.append(StringUtil.getQuestionMark(orCondition.size()));
					queryBuilder.append(")");

					paramList.addAll(orCondition);
				}
				queryBuilder.append(")");

			}

			filtrableCount = 0;

			Map<String, String> prefix = new HashMap<String, String>();
			PageSelections<Map<String, Serializable>> pageSelections = getCurrentFiltrableMap();
			List<String> activeFilter = new ArrayList<String>();
			for (PageSelection<Map<String, Serializable>> pageSelection : pageSelections.getEntries()) {
				Set<String> filtrableProps = new HashSet<String>();
				if (getFiltrableProperty() != null) {
					for (String property : getFiltrableProperty()) {
						String[] properties = property.split("\\.");
						if (properties.length > 1) {
							filtrableProps.add(properties[1]);
							prefix.put(properties[1], properties[0] + ".");
						} else {
							filtrableProps.add(properties[0]);
						}
					}
				}
				if (!filtrableProps.isEmpty()) {
					for (String key : pageSelection.getData().keySet()) {
						Serializable value = pageSelection.getData().get(key);
						if (!(value instanceof Map)) {
							MapInfo mapInfo = retrieveMapInfo(pageSelection.getData(), key);
							if (value instanceof String) {
								String valeur = (String) value;
								if (!StringUtils.isEmpty(valeur)) {
									valeur = valeur.trim().replace('*', '%');
									if (key.equals(VALIDEE_KEY)) {
										valeur = Boolean.TRUE.toString().equals(valeur.toLowerCase())
												? ElementLifeCycleState.validated.name()
												: Boolean.FALSE.toString().equals(valeur.toLowerCase())
														? ElementLifeCycleState.draft.name()
														: valeur;
									} else if (STSchemaConstant.FEUILLE_ROUTE_DEFAUT_PROPERTY.equals(key) && StringUtil.isNotEmpty(valeur)) {
										valeur = Boolean.TRUE.toString().equals(valeur.toLowerCase()) ? "1" : "0";
									} else if (SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_NUMERO_PROPERTY.equals(key)) {
										// Lancer une erreur si la valeur saisie pour numéro n'est pas valide
										String tmp = valeur.replace("%", "");
										try {
											Integer.parseInt(tmp);
										} catch (Exception e) {
											valeur = "";
										}
									}
									addParameter(queryBuilder, mapInfo.getPrefix(), mapInfo.getProperty(), valeur,
											prefix.get(mapInfo.getPrefix() + ":" + mapInfo.getProperty()));
									activeFilter.add(key);
								}
							} else if (value instanceof Boolean) {
								addParameter(queryBuilder, mapInfo.getPrefix(), mapInfo.getProperty(),
										(Boolean) value ? 1 : 0,
										prefix.get(mapInfo.getPrefix() + ":" + mapInfo.getProperty()));
							}
						}
					}
				}
				// il y a un seul document pour le filtre
				break;
			}

			if (!activeFilter.contains(VALIDEE_KEY)) {
				queryBuilder.append(" AND f.ecm:currentLifeCycleState IN (?, ?) ");
				paramList.add(DRAFT);
				paramList.add(VALIDATED);
			}

			queryBuilder.append(" ");

			final String sortInfo = NXQLQueryBuilder.getSortClause(sortArray);

			queryBuilder.append(sortInfo);

			final String newQuery = correctOrderByDistinctQuery(queryBuilder.toString(), sortArray);

			if (newQuery != null && !newQuery.equals(query)) {
				// query has changed => refresh
				if (query != null) {
					refresh();
				}
				query = newQuery;
			}
		} catch (ClientException e) {
			throw new ClientRuntimeException(e);
		}
	}

    protected Boolean isDeleter(final SolonEpgFeuilleRoute feuilleRoute, final STLockActionsBean stLockActions, final List<String> groups)
            throws ClientException {
		// La feuille de route par défaut globale ne peut pas être supprimée
		if (feuilleRoute.isFeuilleRouteDefautGlobal()) {
			return false;
		}
		if (groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_VALIDATOR)) {
			return Boolean.TRUE;
		} else if (isFeuilleDeRouteCreeParAdminFonctionnel(feuilleRoute.getDocument())) {
            return Boolean.FALSE;
        } else if (!stLockActions.canUserLockDoc(feuilleRoute.getDocument())) {
            return Boolean.FALSE;
        } else {
            // Les administrateurs ministériels ont le droit d'écriture sur les modèles de leur ministère sauf sur ceux crées par un Administrateur fonctionnel
            final String ministere = feuilleRoute.getMinistere();
            final String groupMinistere = MinistereGroupeHelper.ministereidToGroup(ministere);
            if (StringUtils.isBlank(ministere) || !groups.contains(groupMinistere)
                    || !groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_UDPATER)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;

    }

    private boolean isFeuilleDeRouteCreeParAdminFonctionnel(final DocumentModel document) throws ClientException {
        final String feuilleDeRouteCreateur = DublincoreSchemaUtils.getCreator(document);

        final UserManager userManager = STServiceLocator.getUserManager();
        final SSPrincipal principal = (SSPrincipal) userManager.getPrincipal(feuilleDeRouteCreateur);

        if (principal == null) {
            return false;
        } else {
            return principal.isMemberOf(STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME);
        }
    }

    private String computeMinistere(final String ministere, final OrganigrammeMinIdToEditionConverter converter) {
        if (StringUtils.isNotBlank(ministere)) {
            return converter.getAsString(null, null, ministere);
        }
        return null;
    }

    private String computeEtat(final String currentLifeCycleState, final Boolean demandeValidation) {
        if (ElementLifeCycleState.draft.name().equals(currentLifeCycleState)) {
            if (!demandeValidation) {
                return DRAFT;
            } else {
                return REQUESTED;
            }
        } else if (ElementLifeCycleState.validated.name().equals(currentLifeCycleState)) {
            return VALIDATED;
        }

        return null;
    }

    protected Date computeDateModification(final Calendar dateModification) {
        if (dateModification != null) {
            return dateModification.getTime();
        } else {
            return null;
        }
    }

    protected String computeFullName(final String userId, final STUserService sTUserService) {
        return sTUserService.getUserFullName(userId);
    }

	@Override
	public Boolean isFiltreActif() {
		return filtrableCount > 0;
	}

	@Override
	public Integer getFiltreActif() {
		return filtrableCount;
	}

	@Override
	public Boolean isHiddenColumn(String isHidden) throws ClientException {
		if (isHidden.equals("true")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@SuppressWarnings("unchecked")
	private MapInfo retrieveMapInfo(Map<String, Serializable> data, String key) throws ClientException {
		for (Entry<String, Serializable> entry : data.entrySet()) {
			Object obj = entry.getValue();
			if (obj instanceof Map) {
				for (String objKey : ((Map<String, Serializable>) obj).keySet()) {
					if (objKey.equals(key)) {
						MapInfo mapInfo = new MapInfo();
						mapInfo.setPrefix(entry.getKey());
						mapInfo.setProperty(objKey);
						return mapInfo;
					}
				}
			}
		}

		throw new ClientException("Impossible de filtrer sur " + key);
	}

	private void addParameter(StringBuilder query, String key, String mapKey, Serializable value, String prefix) {

		if (query.toString().toUpperCase().contains("WHERE")) {
			query.append(" AND ");
		} else {
			query.append(" WHERE ");
		}

		query.append(prefix);
		query.append(key);
		query.append(":");
		query.append(mapKey);
		if (value instanceof String && ((String) value).contains("%")) {
			query.append(" ILIKE ");
		} else {
			query.append(" = ");
		}
		query.append(" ? ");
		paramList.add(value.toString());
		filtrableCount++;

	}

	private String correctOrderByDistinctQuery(String query, SortInfo[] sortArray) {
		if (query == null || query.isEmpty()) {
			return query;
		}

		final String lowerQuery = query.toLowerCase();

		if (!lowerQuery.contains("distinct")) {
			return query;
		}
		int pos = lowerQuery.indexOf("from");
		StringBuilder queryBeforeFrom = new StringBuilder(query.substring(0, pos));
		String queryAfterFrom = query.substring(pos, query.length());

		for (SortInfo sortInfo : sortArray) {
			if (!queryBeforeFrom.toString().contains(sortInfo.getSortColumn())) {
				queryBeforeFrom.append(", ").append(sortInfo.getSortColumn());
			}
		}

		return queryBeforeFrom + " " + queryAfterFrom;
	}
}
