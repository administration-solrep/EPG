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
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageProviderDefinition;
import org.nuxeo.ecm.platform.query.api.PageSelection;
import org.nuxeo.ecm.platform.query.api.PageSelections;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgContentViewConstant;
import fr.dila.solonepg.api.constant.SolonEpgProfilUtilisateurConstants;
import fr.dila.solonepg.api.dto.DossierLinkMinimal;
import fr.dila.solonepg.api.dto.IdLabel;
import fr.dila.solonepg.api.service.CorbeilleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.client.DossierLinkMinimalMapper;
import fr.dila.solonepg.web.client.DossierListingDTO;
import fr.dila.solonepg.web.client.DossierListingDTOImpl;
import fr.dila.solonepg.web.suivi.EspaceSuiviTreeBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STLockService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.contentview.CorePageProviderUtil;
import fr.dila.st.web.contentview.HiddenColumnPageProvider;
import fr.dila.st.web.contentview.OrderByDistinctQueryCorrector;

public class DossierPageProvider extends AbstractDTOFiltrableProvider implements HiddenColumnPageProvider {

	public static final String		RESOURCES_ACCESSOR_PROPERTY	= "resourcesAccessor";
	public static final String		IS_CASE_LINK_PROPERTY		= "isCaseLink";
	public static final String		LOAD_CASE_LINK_PROPERTY		= "loadCaseLink";
	private static final String		USER_COLUMN_PROPERTY		= "userColumn";

	private static final long		serialVersionUID			= 2847175638025543949L;

	private static final STLogger	LOGGER						= STLogFactory.getLog(DossierPageProvider.class);

	protected List<Object>			filtrableParameters;
	private int						filtrableCount;

	@Override
	protected void fillCurrentPageMapList(CoreSession coreSession) throws ClientException {
		currentItems = new ArrayList<Map<String, Serializable>>();

		if (query.contains(EspaceSuiviTreeBean.DEFAULT_QUERY)) {
			// requete vide on fait rien
			resultsCount = 0;
			return;
		}

		if (!query.startsWith(FlexibleQueryMaker.KeyCode.UFXNQL.key)) {
			query = QueryUtils.ufnxqlToFnxqlQuery(query);
		}

		List<Object> params = getQueryParams();

		try {
			resultsCount = QueryUtils.doCountQuery(coreSession, query, params.toArray());
		} catch (ClientException ce) {
			resultsCount = 0;
			LOGGER.error(coreSession, STLogEnumImpl.FAIL_EXECUTE_UFNXQL_TEC, query, ce);
		}

		if (resultsCount > 0) {

			List<String> ids = QueryUtils.doQueryForIds(coreSession, query, getPageSize(), offset, params.toArray());

			if (!ids.isEmpty()) {
				SSPrincipal principal = (SSPrincipal) coreSession.getPrincipal();
				Boolean decretArriveReader = principal.getBaseFunctionSet().contains(
						SolonEpgBaseFunctionConstant.DECRET_ARRIVE_READER);

				final STLockService stLockService = STServiceLocator.getSTLockService();

				Boolean isCaseLink = retrieveIsCaseLink();

				if (isCaseLink) {
					Map<String, List<DossierListingDTO>> mapDossierIdDTO = remapDossierLink(coreSession, ids,
							decretArriveReader);

					// extract des lock
					Map<String, String> lockedDossierInfos = stLockService.extractLockedInfo(coreSession,
							mapDossierIdDTO.keySet());

					for (Map.Entry<String, String> lockDossierInfo : lockedDossierInfos.entrySet()) {
						String idDossier = lockDossierInfo.getKey();
						List<DossierListingDTO> dldto = mapDossierIdDTO.get(idDossier);
						for (DossierListingDTO dossierListingDTO : dldto) {
							dossierListingDTO.setLocked(true);
							dossierListingDTO.setLockOwner(lockDossierInfo.getValue());
						}

					}
				} else {

					Map<String, DossierListingDTO> mapDossierIdDTO = new HashMap<String, DossierListingDTO>();
					Map<String, String> lockedDossierInfos;
					if (ids.size() < 1000) {
						remapDossier(coreSession, ids, mapDossierIdDTO, decretArriveReader);
						// on récupère les dossiers lockés ainsi que les logins des utilisateurs ayant locké le dossier
						lockedDossierInfos = stLockService.extractLockedInfo(coreSession, mapDossierIdDTO.keySet());
					} else { // quand > 1000 erreur ORA-01795
						Map<String, DossierListingDTO> mapDossierIdDTOTmp;
						int startIdx = 0, endIdx;
						lockedDossierInfos = new HashMap<String, String>();
						while (startIdx < ids.size()) {
							endIdx = ids.size() - startIdx < 1000 ? ids.size() : startIdx + 999;
							mapDossierIdDTOTmp = new HashMap<String, DossierListingDTO>();
							remapDossier(coreSession, ids.subList(startIdx, endIdx), mapDossierIdDTOTmp,
									decretArriveReader);
							lockedDossierInfos.putAll(stLockService.extractLockedInfo(coreSession,
									mapDossierIdDTOTmp.keySet()));
							startIdx = endIdx;
						}
					}

					for (Map.Entry<String, String> lockDossierInfo : lockedDossierInfos.entrySet()) {
						String idDossier = lockDossierInfo.getKey();
						DossierListingDTO dldto = mapDossierIdDTO.get(idDossier);
						if (dldto != null) {
							// info sur le lock
							dldto.setLocked(true);
							dldto.setLockOwner(lockDossierInfo.getValue());
						}
					}

				}
			}

		}
	}

	protected List<Object> getQueryParams() {
		return filtrableParameters;
	}

	private Map<String, List<DossierListingDTO>> remapDossierLink(CoreSession coreSession, List<String> ids,
			Boolean decretArriveReader) throws ClientException {

		DocumentModelList dml = QueryUtils.retrieveDocuments(coreSession,
				DossierSolonEpgConstants.DOSSIER_LINK_DOCUMENT_TYPE, ids);

		ResourcesAccessor resourcesAccessor = retrievePropertyResourcesAccessor();

		Map<String, List<DossierListingDTO>> mapDossierIdDTO = new HashMap<String, List<DossierListingDTO>>();

		for (String ident : ids) {
			DocumentModel docModel = retrieveDocument(dml, ident);
			if (docModel != null) {
				DossierLink dossierLink = docModel.getAdapter(DossierLink.class);
				DossierListingDTO dldto = initDossierLinkDto(dossierLink, ident);

				List<DossierListingDTO> list = mapDossierIdDTO.get(dossierLink.getDossierId());
				if (list == null) {
					list = new ArrayList<DossierListingDTO>();
				}
				list.add(dldto);

				mapDossierIdDTO.put(dossierLink.getDossierId(), list);

				currentItems.add(dldto);
			}
		}

		dml = QueryUtils.retrieveDocuments(coreSession, DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
				mapDossierIdDTO.keySet());

		for (DocumentModel dm : dml) {
			List<DossierListingDTO> dldto = mapDossierIdDTO.get(dm.getId());
			for (DossierListingDTO dossierListingDTO : dldto) {
				mapDossierField(coreSession, resourcesAccessor, dm, dossierListingDTO, decretArriveReader);
			}

		}

		return mapDossierIdDTO;
	}

	public DossierListingDTO initDossierLinkDto(DossierLink dossierLink, String ident) {
		DossierListingDTO dldto = new DossierListingDTOImpl();
		dldto.setDateCreation(dossierLink.getDateCreation() == null ? null : dossierLink.getDateCreation().getTime());
		if (StringUtils.isEmpty(ident)) {
			ident = dossierLink.getDocument().getId();
		}
		dldto.setCaseLinkId(ident);
		dldto.setFromCaseLink(Boolean.TRUE);
		dldto.setRead(dossierLink.isRead());
		return dldto;
	}

	private void remapDossier(CoreSession coreSession, List<String> ids,
			Map<String, DossierListingDTO> mapDossierIdDTO, Boolean decretArriveReader) throws ClientException {

		DocumentModelList dml = QueryUtils.retrieveDocuments(coreSession,
				DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, ids);

		ResourcesAccessor resourcesAccessor = retrievePropertyResourcesAccessor();

		Map<String, List<DossierLinkMinimal>> dossierIdToDossierLinksMap = null;
		if (retrieveLoadCaseLink()) {
			dossierIdToDossierLinksMap = findDossierLinks(coreSession, resourcesAccessor, ids);
		}
		for (String ident : ids) {
			DocumentModel dm = retrieveDocument(dml, ident);

			if (dm != null) {
				DossierListingDTOImpl dldto = new DossierListingDTOImpl();
				mapDossierField(coreSession, resourcesAccessor, dm, dldto, decretArriveReader);
				mapDossierIdDTO.put(dldto.getDossierId(), dldto);

				currentItems.add(dldto);
				if (dossierIdToDossierLinksMap != null) {
					List<DossierLinkMinimal> dlMinimals = dossierIdToDossierLinksMap.get(ident);
					List<IdLabel> idLabels = new ArrayList<IdLabel>();
					for (DossierLinkMinimal dl : dlMinimals) {
						idLabels.add(dl.getIdLabel());
						if (dl.getRetourPourModification()) {
							dldto.setRetourPourModification(Boolean.TRUE);
						}
					}
					dldto.setCaseLinkIdsLabels(idLabels.toArray(new IdLabel[0]));
				}
			}
		}
	}

	private void mapDossierField(CoreSession coreSession, ResourcesAccessor resourcesAccessor, DocumentModel docModel,
			DossierListingDTO dldto, Boolean decretArriveReader) throws ClientException {
		if (docModel != null) {

			Dossier dossier = docModel.getAdapter(Dossier.class);

			ConseilEtat conseilEtat = null;
			RetourDila retourDila = null;

			dldto.setDocIdForSelection(docModel.getId());
			dldto.setDossierId(docModel.getId());
			dldto.setNumeroNor(dossier.getNumeroNor());
			dldto.setTitreActe(dossier.getTitreActe());
			// Dans le cas d'un dossier link, on n'écrase pas la date de création déjà présente.
			if (!retrieveIsCaseLink()) {
				dldto.setDateCreation(dossier.getDateCreation() == null ? null : dossier.getDateCreation().getTime());
			}
			dldto.setLastContributor(dossier.getLastContributor());
			dldto.setAuthor(dossier.getAuthor());
			dldto.setUrgent(dossier.getIsUrgent());

			if (decretArriveReader) {
				TraitementPapier traitementPapier = docModel.getAdapter(TraitementPapier.class);
				if (traitementPapier.getDateArrivePapier() == null) {
					dldto.setDecretArrive(false);
				} else {
					dldto.setDecretArrive(true);
				}
			}

			dldto.setStatut(dossier.getStatut());
			dldto.setTypeActe(dossier.getTypeActe());
			dldto.setMinistereResp(dossier.getMinistereResp());
			dldto.setDirectionResp(dossier.getDirectionResp());
			dldto.setMinistereAttache(dossier.getMinistereAttache());
			dldto.setDirectionAttache(dossier.getDirectionAttache());
			dldto.setNomRespDossier(dossier.getNomRespDossier());
			dldto.setPrenomRespDossier(dossier.getPrenomRespDossier());
			dldto.setQualiteRespDossier(dossier.getQualiteRespDossier());
			dldto.setTelephoneRespDossier(dossier.getTelephoneRespDossier());
			dldto.setMailRespDossier(dossier.getMailRespDossier());
			dldto.setCategorieActe(dossier.getCategorieActe());
			dldto.setBaseLegaleActe(dossier.getBaseLegaleActe());
			dldto.setDatePublication(dossier.getDatePublication() == null ? null : dossier.getDatePublication()
					.getTime());
			dldto.setPublicationRapportPresentation(dossier.getPublicationRapportPresentation());

			if (!isHiddenColumn(ConseilEtatConstants.CONSEIL_ETAT_SECTION_CE_PROPERTY)) {
				// chargement que si nécessaire
				if (conseilEtat == null) {
					conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
				}
				dldto.setSectionCe(conseilEtat.getSectionCe());
			}

			if (!isHiddenColumn(ConseilEtatConstants.CONSEIL_ETAT_PRIORITE_PROPERTY)) {
				// chargement que si nécessaire
				if (conseilEtat == null) {
					conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
				}
				dldto.setPriorite(conseilEtat.getPriorite());
			}

			if (!isHiddenColumn(ConseilEtatConstants.CONSEIL_ETAT_RAPPORTEUR_CE_PROPERTY)) {
				// chargement que si nécessaire
				if (conseilEtat == null) {
					conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
				}
				dldto.setRapporteurCe(conseilEtat.getRapporteurCe());
			}

			if (!isHiddenColumn(ConseilEtatConstants.CONSEIL_ETAT_DATE_TRANSMISSION_SECTION_CE_PROPERTY)) {
				// chargement que si nécessaire
				if (conseilEtat == null) {
					conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
				}
				dldto.setDateTransmissionSectionCe(conseilEtat.getDateTransmissionSectionCe() == null ? null
						: conseilEtat.getDateTransmissionSectionCe().getTime());
			}

			if (!isHiddenColumn(ConseilEtatConstants.CONSEIL_ETAT_DATE_SECTION_CE_PROPERTY)) {
				// chargement que si nécessaire
				if (conseilEtat == null) {
					conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
				}
				dldto.setDateSectionCe(conseilEtat.getDateSectionCe() == null ? null : conseilEtat.getDateSectionCe()
						.getTime());
			}

			if (!isHiddenColumn(ConseilEtatConstants.CONSEIL_ETAT_DATE_AG_CE_PROPERTY)) {
				// chargement que si nécessaire
				if (conseilEtat == null) {
					conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
				}
				dldto.setDateAgCe(conseilEtat.getDateAgCe() == null ? null : conseilEtat.getDateAgCe().getTime());
			}

			if (!isHiddenColumn(ConseilEtatConstants.CONSEIL_ETAT_NUMERO_ISA_PROPERTY)) {
				// chargement que si nécessaire
				if (conseilEtat == null) {
					conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
				}
				dldto.setNumeroISA(conseilEtat.getNumeroISA());
			}

			dldto.setNomCompletChargesMissions(dossier.getNomCompletChargesMissions());
			dldto.setNomCompletConseillersPms(dossier.getNomCompletConseillersPms());
			dldto.setDateSignature(dossier.getDateSignature() == null ? null : dossier.getDateSignature().getTime());
			dldto.setDatePourFournitureEpreuve(dossier.getDatePourFournitureEpreuve() == null ? null : dossier
					.getDatePourFournitureEpreuve().getTime());
			dldto.setVecteurPublication(dossier.getVecteurPublication());
			dldto.setPublicationsConjointes(dossier.getPublicationsConjointes());
			dldto.setPublicationIntegraleOuExtrait(dossier.getPublicationIntegraleOuExtrait());
			dldto.setDecretNumerote(dossier.getDecretNumerote());
			dldto.setDateEffet(dossier.getDateEffet());

			if (!isHiddenColumn(RetourDilaConstants.RETOUR_DILA_MODE_PARUTION_PROPERTY)) {
				// chargement que si nécessaire
				if (retourDila == null) {
					retourDila = dossier.getDocument().getAdapter(RetourDila.class);
				}
				dldto.setModeParution(retourDila.getModeParution());
			}

			if (!isHiddenColumn(RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_JORF_PROPERTY)) {
				// chargement que si nécessaire
				if (retourDila == null) {
					retourDila = dossier.getDocument().getAdapter(RetourDila.class);
				}
				dldto.setDateParutionJorf(retourDila.getDateParutionJorf() == null ? null : retourDila
						.getDateParutionJorf().getTime());
			}

			if (!isHiddenColumn(RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_JORF_PROPERTY)) {
				// chargement que si nécessaire
				if (retourDila == null) {
					retourDila = dossier.getDocument().getAdapter(RetourDila.class);
				}
				dldto.setNumeroTexteParutionJorf(retourDila.getNumeroTexteParutionJorf());
			}

			if (!isHiddenColumn(RetourDilaConstants.RETOUR_DILA_PAGE_PARUTION_JORF_PROPERTY)) {
				// chargement que si nécessaire
				if (retourDila == null) {
					retourDila = dossier.getDocument().getAdapter(RetourDila.class);
				}
				dldto.setPageParutionJorf(retourDila.getPageParutionJorf());
			}

			if (!isHiddenColumn(RetourDilaConstants.RETOUR_DILA_PARUTION_BO_PROPERTY)) {
				// chargement que si nécessaire
				if (retourDila == null) {
					retourDila = dossier.getDocument().getAdapter(RetourDila.class);
				}
				dldto.setParutionBo(retourDila.getParutionBo());
			}

			dldto.setDelaiPublication(dossier.getDelaiPublication());
			dldto.setDatePreciseePublication(dossier.getDatePreciseePublication() == null ? null : dossier
					.getDatePreciseePublication().getTime());
			dldto.setZoneComSggDila(dossier.getZoneComSggDila());
			dldto.setIndexationRubrique(dossier.getIndexationRubrique());
			dldto.setIndexationMotsCles(dossier.getIndexationMotsCles());
			dldto.setIndexationChampLibre(dossier.getIndexationChampLibre());

			if (!isHiddenColumn(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY)) {
				// chargement que si nécessaire
				dldto.setApplicationLoiRef(getListeComplexeTypeRef(dossier.getApplicationLoi()));
			}

			if (!isHiddenColumn(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY)) {
				// chargement que si nécessaire
				dldto.setTranspositionOrdonnanceRef(getListeComplexeTypeRef(dossier.getTranspositionOrdonnance()));
			}

			if (!isHiddenColumn(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY)) {
				// chargement que si nécessaire
				dldto.setTranspositionDirectiveRef(getListeComplexeTypeRef(dossier.getTranspositionDirective()));
			}

			dldto.setHabilitationRefLoi(dossier.getHabilitationRefLoi());
			dldto.setHabilitationNumeroArticles(dossier.getHabilitationNumeroArticles());
			dldto.setHabilitationCommentaire(dossier.getHabilitationCommentaire());

			dldto.setComplet(isBordereauComplet(docModel) && dossier.getIsParapheurComplet());
			dldto.setTexteEntreprise(dossier.getTexteEntreprise());

		}
	}

	/**
	 * Récupère la liste des référénces d'un type complexe à partir de la liste dutype complexe.
	 * 
	 * @param complextype
	 * @return la liste des référénces d'un type complexe à partir de la liste dutype complexe.
	 */
	protected List<String> getListeComplexeTypeRef(List<ComplexeType> complextype) {
		List<String> listeRef = new ArrayList<String>();
		for (ComplexeType complexeType : complextype) {
			listeRef.add((String) complexeType.getSerializableMap().get(
					DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY));
		}
		return listeRef;
	}

	/**
	 * 
	 * Récupère tous les dossiers link id en 1 seul fois pour une page de dossiers.
	 * 
	 * @throws ClientException
	 * 
	 * 
	 */
	public Map<String, List<DossierLinkMinimal>> findDossierLinks(CoreSession coreSession,
			ResourcesAccessor resourcesAccessor, List<String> dossierIds) throws ClientException {

		Map<String, List<DossierLinkMinimal>> dossierIdToDossierLinksMap = new HashMap<String, List<DossierLinkMinimal>>();

		final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
		List<DossierLink> dossierLinkResult = corbeilleService.findDossierLinks(coreSession, dossierIds);

		DossierLinkMinimalMapper dlMapper = new DossierLinkMinimalMapper(resourcesAccessor);

		for (DossierLink dossierLink : dossierLinkResult) {
			if (!dossierIdToDossierLinksMap.containsKey(dossierLink.getDossierId())) {
				dossierIdToDossierLinksMap.put(dossierLink.getDossierId(), new ArrayList<DossierLinkMinimal>());
			}
			dossierIdToDossierLinksMap.get(dossierLink.getDossierId()).add(dlMapper.doMapping(dossierLink));
		}

		// Initialisation de la liste des dossierLinkId pour les identifiants de dossier non présents dans la requête
		for (String dossierId : dossierIds) {
			if (!dossierIdToDossierLinksMap.containsKey(dossierId)) {
				dossierIdToDossierLinksMap.put(dossierId, new ArrayList<DossierLinkMinimal>());
			}
		}

		return dossierIdToDossierLinksMap;

	}

	@Override
	public Boolean isHiddenColumn(String colName) throws ClientException {
		Boolean isHiddenColumn = Boolean.FALSE;
		if (!StringUtils.isEmpty(colName)) {
			if (SolonEpgProfilUtilisateurConstants.DATE_ARRIVEE_DOSSIER_LINK_PROPERTY.equals(colName)) {
				// colonne spécifique au CaseLink, si c'est pas un caseLink on l'affiche pas
				isHiddenColumn = !retrieveIsCaseLink();
			} else if (SolonEpgProfilUtilisateurConstants.DATE_CREATION_DOSSIER_PROPERTY.equals(colName)) {
				// colonne spécifique au Dossier, si c'est pas un dossier on ne l'affiche pas
				isHiddenColumn = retrieveIsCaseLink();
			} else if (SolonEpgProfilUtilisateurConstants.COMPLET_PROPERTY.equals(colName)) {
				// colonne spécifique a l'espace de creation
				isHiddenColumn = !SolonEpgContentViewConstant.ESPACE_CREATION_DOSSIERS_CONTENT_VIEW.equals(this.name);
			} else if (SolonEpgProfilUtilisateurConstants.NUMERO_NOR_PROPERTY.equals(colName)) {
				// NOR et lock tout le temps affiché
				isHiddenColumn = Boolean.FALSE;
			} else {
				Map<String, Serializable> props = getProperties();
				@SuppressWarnings("unchecked")
				Set<String> availableColNames = (Set<String>) props.get(USER_COLUMN_PROPERTY);
				if (availableColNames != null) {
					isHiddenColumn = !availableColNames.contains(colName);
				}

			}
		}
		return isHiddenColumn;
	}

	/**
	 * Retourne le document model associé a un id donné
	 */
	private DocumentModel retrieveDocument(List<DocumentModel> docList, String ident) {
		for (DocumentModel doc : docList) {
			if (ident.equals(doc.getId())) {
				return doc;
			}
		}
		return null;
	}

	private ResourcesAccessor retrievePropertyResourcesAccessor() {
		Map<String, Serializable> props = getProperties();
		return (ResourcesAccessor) props.get(RESOURCES_ACCESSOR_PROPERTY);

	}

	/**
	 * Chargement a prtir des caseLink ou des dossiers
	 * 
	 * @return
	 */
	public Boolean retrieveIsCaseLink() {
		Map<String, Serializable> props = getProperties();
		Boolean bool = (Boolean) props.get(IS_CASE_LINK_PROPERTY);
		if (bool == null || !bool) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	/**
	 * chargement ou non des caseLink
	 * 
	 * @return
	 */
	private Boolean retrieveLoadCaseLink() {
		Map<String, Serializable> props = getProperties();
		Boolean bool = (Boolean) props.get(LOAD_CASE_LINK_PROPERTY);
		if (bool == null || bool) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	private boolean isBordereauComplet(DocumentModel dossierDoc) {
		Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		boolean isComplet = true;

		// Nom resp dossier
		if (StringUtils.isEmpty(dossier.getNomRespDossier())) {
			isComplet = false;
		}
		// Qualité resp dossier
		if (StringUtils.isEmpty(dossier.getQualiteRespDossier())) {
			isComplet = false;
		}
		// Tél resp dossier
		if (StringUtils.isEmpty(dossier.getTelephoneRespDossier())) {
			isComplet = false;
		}
		// Vecteur publication
		if (dossier.getVecteurPublication().size() < 1) {
			isComplet = false;
		}

		// Publication à date précisée
		if ("1".equals(dossier.getDelaiPublication()) && dossier.getDatePreciseePublication() == null) {
			isComplet = false;
		}

		// Indexation : Rubrique
		if (dossier.getIndexationRubrique() == null || dossier.getIndexationRubrique().size() < 1) {
			isComplet = false;
		}

		return isComplet;
	}

	@Override
	protected void buildQuery() {
		try {
			SortInfo[] sortArray = null;

			String newQuery;
			PageProviderDefinition def = getDefinition();

			String stringQuery = getQuery(null, def);

			StringBuilder query = new StringBuilder(stringQuery);

			if (sortInfos != null) {
				sortArray = CorePageProviderUtil.getSortinfoForQuery(sortInfos);
			}

			filtrableParameters = new ArrayList<Object>();
			filtrableCount = 0;

			Map<String, String> prefix = new HashMap<String, String>();
			PageSelections<Map<String, Serializable>> pageSelections = getCurrentFiltrableMap();
			for (PageSelection<Map<String, Serializable>> pageSelection : pageSelections.getEntries()) {
				Set<String> filtrableProps = new HashSet<String>();
				if (getFiltrableProperty() != null) {
					for (String property : getFiltrableProperty()) {
						String[] props = property.split("\\.");
						if (props.length > 1) {
							filtrableProps.add(props[1]);
							prefix.put(props[1], props[0] + ".");
						} else {
							filtrableProps.add(props[0]);
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
									if (Boolean.TRUE.toString().equals(valeur)) {
										addParameter(query, mapInfo.getPrefix(), mapInfo.getProperty(), 1,
												prefix.get(mapInfo.getPrefix() + ":" + mapInfo.getProperty()));
									} else if (Boolean.FALSE.toString().equals(valeur)) {
										addParameter(query, mapInfo.getPrefix(), mapInfo.getProperty(), 0,
												prefix.get(mapInfo.getPrefix() + ":" + mapInfo.getProperty()));
									} else {
										addParameter(query, mapInfo.getPrefix(), mapInfo.getProperty(), valeur,
												prefix.get(mapInfo.getPrefix() + ":" + mapInfo.getProperty()));
									}
								}
							} else if (value instanceof Date) {
								addParameterDate(query, mapInfo.getPrefix(), mapInfo.getProperty(), (Date) value,
										prefix.get(mapInfo.getPrefix() + ":" + mapInfo.getProperty()));
							} else if (value instanceof Boolean) {
								addParameter(query, mapInfo.getPrefix(), mapInfo.getProperty(),
										(Boolean) value ? 1 : 0,
										prefix.get(mapInfo.getPrefix() + ":" + mapInfo.getProperty()));
							}
						}
					}
				}
				// il y a un seul document pour le filtre
				break;
			}

			// on force les quotes est les escape des paramètres
			Boolean forceQuoteAndEscape = Boolean.FALSE;
			newQuery = NXQLQueryBuilder.getQuery(query.toString(), null, forceQuoteAndEscape, forceQuoteAndEscape,
					sortArray);
			newQuery = OrderByDistinctQueryCorrector.correct(newQuery, sortArray);

			if (newQuery != null && !newQuery.equals(this.query)) {
				// query has changed => refresh
				refresh();
				this.query = newQuery;
			}
		} catch (ClientException e) {
			throw new ClientRuntimeException(e);
		}
	}

	protected String getQuery(SortInfo[] sortArray, PageProviderDefinition def) throws ClientException {
		return NXQLQueryBuilder.getQuery(def.getPattern(), getParameters(), def.getQuotePatternParameters(),
				def.getEscapePatternParameters());
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
		query.append(" AND d.dos:deleted=0");
		filtrableParameters.add(value);
		filtrableCount++;

	}

	private void addParameterDate(StringBuilder query, String key, String mapKey, Date value, String prefix) {

		Calendar calDebut = Calendar.getInstance();
		calDebut.setTime(value);
		calDebut.set(Calendar.HOUR_OF_DAY, 0);
		calDebut.set(Calendar.MINUTE, 0);
		calDebut.set(Calendar.SECOND, 0);
		calDebut.set(Calendar.MILLISECOND, 0);

		Calendar calFin = Calendar.getInstance();
		calFin.setTime(value);
		calFin.set(Calendar.HOUR_OF_DAY, 23);
		calFin.set(Calendar.MINUTE, 59);
		calFin.set(Calendar.SECOND, 59);
		calFin.set(Calendar.MILLISECOND, 999);

		if (query.toString().toUpperCase().contains("WHERE")) {
			query.append(" AND ");
		} else {
			query.append(" WHERE ");
		}

		query.append(prefix);
		query.append(key);
		query.append(":");
		query.append(mapKey);
		query.append(" >= ");
		query.append(" ? ");

		query.append(" AND ");
		query.append(prefix);
		query.append(key);
		query.append(":");
		query.append(mapKey);
		query.append(" <= ");
		query.append(" ? ");

		filtrableParameters.add(calDebut);
		filtrableParameters.add(calFin);
		filtrableCount++;

	}

	@Override
	public Boolean isFiltreActif() {
		return filtrableParameters != null && !filtrableParameters.isEmpty();
	}

	@Override
	public Integer getFiltreActif() {
		if (filtrableParameters != null) {
			return filtrableCount;
		}
		return 0;
	}

}
