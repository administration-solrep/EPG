package fr.dila.solonepg.ui.contentview;

import static fr.dila.st.core.query.QueryHelper.MAX_NB_EXPRESSIONS_IN_LIST;
import static fr.dila.st.core.query.QueryHelper.getDocuments;

import fr.dila.solonepg.api.administration.VecteurPublication;
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
import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.solonepg.api.dto.DossierLinkMinimal;
import fr.dila.solonepg.api.dto.VecteurPublicationDTO;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.core.dto.DossierLinkMinimalMapper;
import fr.dila.solonepg.core.dto.activitenormative.VecteurPublicationDTOImpl;
import fr.dila.solonepg.core.recherche.EpgDossierListingDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.helper.SolonEpgProviderHelper;
import fr.dila.ss.api.recherche.IdLabel;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STContentViewConstant;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.service.STLockService;
import fr.dila.st.core.helper.PaginationHelper;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.FlexibleQueryMaker;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

public class DossierPageProvider extends AbstractDTOFiltrableProvider {
    private static final long serialVersionUID = 3844193838715384559L;

    public static final String RESOURCES_ACCESSOR_PROPERTY = "resourcesAccessor";
    public static final String IS_CASE_LINK_PROPERTY = "isCaseLink";
    public static final String LOAD_CASE_LINK_PROPERTY = "loadCaseLink";
    public static final String USER_COLUMN_PROPERTY = "userColumn";

    public static final String KEEP_ORDER = "keepOrder";

    public DossierPageProvider() {
        super();
    }

    protected void fillCurrentPageFromDossiers(
        CoreSession coreSession,
        List<String> ids,
        boolean decretArriveReader,
        final STLockService stLockService
    ) {
        Map<String, EpgDossierListingDTOImpl> mapDossierIdDTO = new HashMap<>();
        Map<String, String> lockedDossierInfos;

        if (ids.size() < MAX_NB_EXPRESSIONS_IN_LIST) {
            mapDossierIdDTO = remapDossier(coreSession, ids, decretArriveReader);
            // on récupère les dossiers lockés ainsi que les logins des utilisateurs ayant locké le dossier
            lockedDossierInfos = stLockService.extractLockedInfo(coreSession, mapDossierIdDTO.keySet());
        } else {
            int startIdx = 0;
            int endIdx;
            lockedDossierInfos = new HashMap<>();
            while (startIdx < ids.size()) {
                endIdx =
                    ids.size() - startIdx < MAX_NB_EXPRESSIONS_IN_LIST
                        ? ids.size()
                        : startIdx + MAX_NB_EXPRESSIONS_IN_LIST - 1;
                Map<String, EpgDossierListingDTOImpl> mapDossierIdDTOTmp = remapDossier(
                    coreSession,
                    ids.subList(startIdx, endIdx),
                    decretArriveReader
                );
                lockedDossierInfos.putAll(stLockService.extractLockedInfo(coreSession, mapDossierIdDTOTmp.keySet()));
                startIdx = endIdx;
            }
        }

        for (Map.Entry<String, String> lockDossierInfo : lockedDossierInfos.entrySet()) {
            String idDossier = lockDossierInfo.getKey();
            EpgDossierListingDTOImpl dldto = mapDossierIdDTO.get(idDossier);
            if (dldto != null) {
                // info sur le lock
                dldto.setLocked(true);
                dldto.setLockOwner(STServiceLocator.getSTUserService().getUserFullName(lockDossierInfo.getValue()));
            }
        }
    }

    protected void fillCurrentPageFromCaseLinks(
        CoreSession coreSession,
        List<String> ids,
        boolean decretArriveReader,
        final STLockService stLockService
    ) {
        Map<String, List<EpgDossierListingDTOImpl>> mapDossierIdDTO = remapDossierLink(
            coreSession,
            ids,
            decretArriveReader
        );

        // extract des lock
        Map<String, String> lockedDossierInfos = stLockService.extractLockedInfo(coreSession, mapDossierIdDTO.keySet());

        for (Map.Entry<String, String> lockDossierInfo : lockedDossierInfos.entrySet()) {
            String idDossier = lockDossierInfo.getKey();
            List<EpgDossierListingDTOImpl> dldto = mapDossierIdDTO.get(idDossier);
            for (EpgDossierListingDTOImpl dossierListingDTO : dldto) {
                dossierListingDTO.setLocked(true);
                dossierListingDTO.setLockOwner(
                    STServiceLocator.getSTUserService().getUserFullName(lockDossierInfo.getValue())
                );
            }
        }
    }

    private Map<String, List<EpgDossierListingDTOImpl>> remapDossierLink(
        CoreSession coreSession,
        List<String> ids,
        boolean decretArriveReader
    ) {
        DocumentModelList dml = QueryUtils.retrieveDocuments(
            coreSession,
            DossierSolonEpgConstants.DOSSIER_LINK_DOCUMENT_TYPE,
            ids
        );

        Map<String, List<EpgDossierListingDTOImpl>> mapDossierIdDTO = new HashMap<>();

        for (String ident : ids) {
            DocumentModel docModel = retrieveDocument(dml, ident);
            if (docModel != null) {
                DossierLink dossierLink = docModel.getAdapter(DossierLink.class);
                EpgDossierListingDTOImpl dldto = initDossierLinkDto(dossierLink, ident);

                List<EpgDossierListingDTOImpl> list = mapDossierIdDTO.get(dossierLink.getDossierId());
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(dldto);

                mapDossierIdDTO.put(dossierLink.getDossierId(), list);

                currentItems.add(dldto);
            }
        }

        dml =
            QueryUtils.retrieveDocuments(
                coreSession,
                DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
                mapDossierIdDTO.keySet()
            );

        for (DocumentModel dm : dml) {
            List<EpgDossierListingDTOImpl> dldto = mapDossierIdDTO.get(dm.getId());

            dldto.forEach(
                epgDossierListingDTO -> mapDossierField(coreSession, dm, epgDossierListingDTO, decretArriveReader)
            );
        }

        return mapDossierIdDTO;
    }

    public EpgDossierListingDTOImpl initDossierLinkDto(DossierLink dossierLink, String ident) {
        EpgDossierListingDTOImpl dldto = new EpgDossierListingDTOImpl();
        dldto.setDateArrivee(DateUtil.toDate(dossierLink.getDateCreation()));
        if (StringUtils.isEmpty(ident)) {
            ident = dossierLink.getDocument().getId();
        }
        dldto.setCaseLinkId(ident);

        IdLabel[] idLabels = new IdLabel[1];
        idLabels[0] = new IdLabel(ident, dossierLink.getRoutingTaskLabel(), dossierLink.getDossierId());
        dldto.setCaseLinkIdsLabels(idLabels);

        dldto.setFromCaseLink(true);
        dldto.setRead(dossierLink.isRead());
        return dldto;
    }

    private Map<String, EpgDossierListingDTOImpl> remapDossier(
        CoreSession coreSession,
        List<String> ids,
        boolean decretArriveReader
    ) {
        DocumentModelList dml = QueryUtils.retrieveDocuments(
            coreSession,
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
            ids,
            retrieveKeepOrder()
        );

        Map<String, EpgDossierListingDTOImpl> mapDossierIdDTO = new HashMap<>();

        Map<String, List<DossierLinkMinimal>> dossierIdToDossierLinksMap = null;
        if (retrieveLoadCaseLink()) {
            dossierIdToDossierLinksMap = findDossierLinks(coreSession, ids);
        }
        for (String ident : ids) {
            DocumentModel dm = retrieveDocument(dml, ident);

            if (dm != null) {
                EpgDossierListingDTOImpl dldto = new EpgDossierListingDTOImpl();
                mapDossierField(coreSession, dm, dldto, decretArriveReader);
                mapDossierIdDTO.put(dldto.getDossierId(), dldto);

                currentItems.add(dldto);
                if (dossierIdToDossierLinksMap != null) {
                    List<DossierLinkMinimal> dlMinimals = dossierIdToDossierLinksMap.get(ident);
                    List<IdLabel> idLabels = new ArrayList<>();
                    for (DossierLinkMinimal dl : dlMinimals) {
                        idLabels.add(dl.getIdLabel());
                        if (dl.getRetourPourModification()) {
                            dldto.setRetourPourModification(true);
                        }
                    }
                    dldto.setCaseLinkIdsLabels(idLabels.toArray(new IdLabel[0]));
                    dlMinimals
                        .stream()
                        .map(DossierLinkMinimal::getDateCreation)
                        .map(Calendar::getTime)
                        .min(Date::compareTo)
                        .ifPresent(dc -> dldto.setDateArrivee(dc));
                }
            }
        }

        return mapDossierIdDTO;
    }

    private void mapDossierField(
        CoreSession coreSession,
        DocumentModel docModel,
        EpgDossierListingDTOImpl dldto,
        boolean decretArriveReader
    ) {
        if (docModel != null) {
            Dossier dossier = docModel.getAdapter(Dossier.class);

            ConseilEtat conseilEtat = null;
            RetourDila retourDila = null;

            dldto.setDocIdForSelection(docModel.getId());
            dldto.setDossierId(docModel.getId());
            dldto.setNumeroNor(dossier.getNumeroNor());
            dldto.setTitreActe(dossier.getTitreActe());

            // Dossier => le champ date de création est spécifique au schéma, on écrase celui du dc
            dldto.setDateCreation(DateUtil.toDate(dossier.getDateCreation()));

            dldto.setLastContributor(DublincoreSchemaUtils.getLastContributor(docModel));

            dldto.setAuthor(dossier.getIdCreateurDossier());
            dldto.setUrgent(dossier.getIsUrgent());

            if (decretArriveReader) {
                TraitementPapier traitementPapier = docModel.getAdapter(TraitementPapier.class);
                dldto.setDecretArrive(traitementPapier.getDateArrivePapier() != null);
            }

            dldto.setStatut(dossier.getStatutLabel());
            dldto.setTypeActe(dossier.getTypeActeLabel());
            dldto.setMinistereResp(getOrganigrammeLabel(dossier.getMinistereResp(), OrganigrammeType.MINISTERE));
            dldto.setDirectionResp(getOrganigrammeLabel(dossier.getDirectionResp(), OrganigrammeType.DIRECTION));
            dldto.setMinistereAttache(getOrganigrammeLabel(dossier.getMinistereAttache(), OrganigrammeType.MINISTERE));
            dldto.setDirectionAttache(getOrganigrammeLabel(dossier.getDirectionAttache(), OrganigrammeType.DIRECTION));
            dldto.setNomRespDossier(dossier.getNomRespDossier());
            dldto.setPrenomRespDossier(dossier.getPrenomRespDossier());
            dldto.setQualiteRespDossier(dossier.getQualiteRespDossier());
            dldto.setTelephoneRespDossier(dossier.getTelephoneRespDossier());
            dldto.setMailRespDossier(dossier.getMailRespDossier());
            dldto.setCategorieActe(dossier.getCategorieActeLabel());
            dldto.setBaseLegaleActe(dossier.getBaseLegaleActe());

            dldto.setDatePublication(DateUtil.toDate(dossier.getDatePublication()));

            dldto.setPublicationRapportPresentation(dossier.getPublicationRapportPresentation());

            remapDossierFieldConseilEtat(dldto, dossier, conseilEtat);

            dldto.setNomCompletChargesMissions(dossier.getNomCompletChargesMissions());
            dldto.setNomCompletConseillersPms(dossier.getNomCompletConseillersPms());
            dldto.setDateSignature(DateUtil.toDate(dossier.getDateSignature()));
            dldto.setDatePourFournitureEpreuve(DateUtil.toDate(dossier.getDatePourFournitureEpreuve()));

            remapVecteurPublication(dldto, dossier, coreSession);

            dldto.setPublicationsConjointes(dossier.getPublicationsConjointes());
            dldto.setPublicationIntegraleOuExtrait(dossier.getPublicationIntegraleOuExtraitLabel());
            dldto.setDecretNumerote(dossier.getDecretNumerote());
            dldto.setDateEffet(dossier.getDateEffet());

            remapDossierFieldRetourDila(dldto, dossier, retourDila);

            dldto.setDelaiPublication(dossier.getDelaiPublicationLabel());
            dldto.setDatePreciseePublication(DateUtil.toDate(dossier.getDatePreciseePublication()));
            dldto.setZoneComSggDila(dossier.getZoneComSggDila());
            dldto.setIndexationRubrique(dossier.getIndexationRubrique());
            dldto.setIndexationMotsCles(dossier.getIndexationMotsCles());
            dldto.setIndexationChampLibre(dossier.getIndexationChampLibre());

            remapDossierFieldTransposition(dldto, dossier);

            dldto.setHabilitationRefLoi(dossier.getHabilitationRefLoi());
            dldto.setHabilitationNumeroArticles(dossier.getHabilitationNumeroArticles());
            dldto.setHabilitationCommentaire(dossier.getHabilitationCommentaire());
            dldto.setHabilitationNumeroOrdre(dossier.getHabilitationNumeroOrdre());

            dldto.setTranspositionDirectiveRef(dossier.getTranspositionDirectiveNumero());
            dldto.setApplicationLoiRef(dossier.getApplicationLoiRefs());
            dldto.setTranspositionOrdonnanceRef(dossier.getTranspositionOrdonnanceRefs());

            dldto.setDossierCompletErreur(SolonEpgProviderHelper.getDossierCompletErreur(dossier));
            dldto.setTexteEntreprise(dossier.getTexteEntreprise());
        }
    }

    private String getOrganigrammeLabel(String id, OrganigrammeType type) {
        return Optional
            .ofNullable(STServiceLocator.getOrganigrammeService().getOrganigrammeNodeById(id, type))
            .map(node -> ((OrganigrammeNode) node).getLabel())
            .orElse(id);
    }

    protected void remapDossierFieldTransposition(EpgDossierListingDTOImpl dldto, Dossier dossier) {
        if (!isHiddenColumn(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY)) {
            // chargement que si nécessaire
            dldto.setApplicationLoiRef(getListeComplexeTypeRef(dossier.getApplicationLoi().getTranspositions()));
        }

        if (!isHiddenColumn(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY)) {
            // chargement que si nécessaire
            dldto.setTranspositionOrdonnanceRef(
                getListeComplexeTypeRef(dossier.getTranspositionOrdonnance().getTranspositions())
            );
        }

        if (!isHiddenColumn(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY)) {
            // chargement que si nécessaire
            dldto.setTranspositionDirectiveRef(
                getListeComplexeTypeRef(dossier.getTranspositionDirective().getTranspositions())
            );
        }
    }

    protected void remapDossierFieldRetourDila(EpgDossierListingDTOImpl dldto, Dossier dossier, RetourDila retourDila) {
        if (!isHiddenColumn(RetourDilaConstants.RETOUR_DILA_MODE_PARUTION_PROPERTY)) {
            // chargement que si nécessaire
            if (retourDila == null) {
                retourDila = dossier.getDocument().getAdapter(RetourDila.class);
            }
            dldto.setModeParution(retourDila.getModeParutionLabel());
        }

        if (!isHiddenColumn(RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_JORF_PROPERTY)) {
            // chargement que si nécessaire
            if (retourDila == null) {
                retourDila = dossier.getDocument().getAdapter(RetourDila.class);
            }
            dldto.setDateParutionJorf(DateUtil.toDate(retourDila.getDateParutionJorf()));
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
    }

    protected void remapDossierFieldConseilEtat(
        EpgDossierListingDTOImpl dldto,
        Dossier dossier,
        ConseilEtat conseilEtat
    ) {
        if (!isHiddenColumn(ConseilEtatConstants.CONSEIL_ETAT_SECTION_CE_PROPERTY)) {
            // chargement que si nécessaire
            conseilEtat = loadConseilEtatIfNecessary(dossier, conseilEtat);
            dldto.setSectionCe(conseilEtat.getSectionCe());
        }

        if (!isHiddenColumn(ConseilEtatConstants.CONSEIL_ETAT_PRIORITE_PROPERTY)) {
            conseilEtat = loadConseilEtatIfNecessary(dossier, conseilEtat);
            dldto.setPriorite(SolonEpgServiceLocator.getPrioriteCEService().getTranslation(conseilEtat.getPriorite()));
        }

        if (!isHiddenColumn(ConseilEtatConstants.CONSEIL_ETAT_RAPPORTEUR_CE_PROPERTY)) {
            conseilEtat = loadConseilEtatIfNecessary(dossier, conseilEtat);
            dldto.setRapporteurCe(conseilEtat.getRapporteurCe());
        }

        if (!isHiddenColumn(ConseilEtatConstants.CONSEIL_ETAT_DATE_TRANSMISSION_SECTION_CE_PROPERTY)) {
            conseilEtat = loadConseilEtatIfNecessary(dossier, conseilEtat);
            dldto.setDateTransmissionSectionCe(DateUtil.toDate(conseilEtat.getDateTransmissionSectionCe()));
        }

        if (!isHiddenColumn(ConseilEtatConstants.CONSEIL_ETAT_DATE_SECTION_CE_PROPERTY)) {
            conseilEtat = loadConseilEtatIfNecessary(dossier, conseilEtat);
            dldto.setDateSectionCe(DateUtil.toDate(conseilEtat.getDateSectionCe()));
        }

        if (!isHiddenColumn(ConseilEtatConstants.CONSEIL_ETAT_DATE_AG_CE_PROPERTY)) {
            conseilEtat = loadConseilEtatIfNecessary(dossier, conseilEtat);
            dldto.setDateAgCe(DateUtil.toDate(conseilEtat.getDateAgCe()));
        }

        if (!isHiddenColumn(ConseilEtatConstants.CONSEIL_ETAT_NUMERO_ISA_PROPERTY)) {
            conseilEtat = loadConseilEtatIfNecessary(dossier, conseilEtat);
            dldto.setNumeroISA(conseilEtat.getNumeroISA());
        }
    }

    protected void remapVecteurPublication(EpgDossierListingDTOImpl dldto, Dossier dossier, CoreSession coreSession) {
        List<String> idVecteurs = dossier.getVecteurPublication();

        if (!idVecteurs.isEmpty()) {
            // Retrieve documents from database
            DocumentModelList docs = getDocuments(coreSession, idVecteurs);
            // Verify document type and retrieve specific field 'Intitule'
            List<String> vecteursIntitule = docs
                .stream()
                .filter(doc -> doc.hasSchema(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA))
                .map(doc -> new VecteurPublicationDTOImpl(doc.getAdapter(VecteurPublication.class)))
                .map(VecteurPublicationDTO::getIntitule)
                .collect(Collectors.toList());
            dldto.setVecteurPublication(vecteursIntitule);
        }
    }

    protected ConseilEtat loadConseilEtatIfNecessary(Dossier dossier, ConseilEtat conseilEtat) {
        if (conseilEtat == null) {
            conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
        }
        return conseilEtat;
    }

    /**
     * Récupère la liste des référénces d'un type complexe à partir de la liste dutype complexe.
     *
     * @param complextype
     * @return la liste des référénces d'un type complexe à partir de la liste dutype complexe.
     */
    protected List<String> getListeComplexeTypeRef(List<? extends ComplexeType> complextype) {
        List<String> listeRef = new ArrayList<>();
        for (ComplexeType complexeType : complextype) {
            listeRef.add(
                (String) complexeType
                    .getSerializableMap()
                    .get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY)
            );
        }
        return listeRef;
    }

    /**
     * Récupère tous les dossiers link id en 1 seul fois pour une page de dossiers.
     */
    public Map<String, List<DossierLinkMinimal>> findDossierLinks(CoreSession coreSession, List<String> dossierIds) {
        Map<String, List<DossierLinkMinimal>> dossierIdToDossierLinksMap = new HashMap<>();

        final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
        List<DossierLink> dossierLinkResult = corbeilleService.findDossierLinks(coreSession, dossierIds);

        for (DossierLink dossierLink : dossierLinkResult) {
            if (!dossierIdToDossierLinksMap.containsKey(dossierLink.getDossierId())) {
                dossierIdToDossierLinksMap.put(dossierLink.getDossierId(), new ArrayList<>());
            }
            dossierIdToDossierLinksMap
                .get(dossierLink.getDossierId())
                .add(DossierLinkMinimalMapper.doMapping(dossierLink));
        }

        // Initialisation de la liste des dossierLinkId pour les identifiants de dossier non présents dans la requête
        for (String dossierId : dossierIds) {
            if (!dossierIdToDossierLinksMap.containsKey(dossierId)) {
                dossierIdToDossierLinksMap.put(dossierId, new ArrayList<>());
            }
        }

        return dossierIdToDossierLinksMap;
    }

    private boolean isHiddenColumn(String colName) {
        boolean isHiddenColumn = false;
        if (StringUtils.isNotEmpty(colName)) {
            if (SolonEpgProfilUtilisateurConstants.DATE_ARRIVEE_DOSSIER_LINK_PROPERTY.equals(colName)) {
                // colonne spécifique au CaseLink, si c'est pas un caseLink on l'affiche pas
                isHiddenColumn = !retrieveIsCaseLink();
            } else if (SolonEpgProfilUtilisateurConstants.DATE_CREATION_DOSSIER_PROPERTY.equals(colName)) {
                // colonne spécifique au Dossier, si c'est pas un dossier on ne l'affiche pas
                isHiddenColumn = retrieveIsCaseLink();
            } else if (SolonEpgProfilUtilisateurConstants.COMPLET_PROPERTY.equals(colName)) {
                // colonne spécifique a l'espace de creation
                isHiddenColumn = !SolonEpgContentViewConstant.ESPACE_CREATION_DOSSIERS_CONTENT_VIEW.equals(this.name);
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

    /**
     * Chargement a prtir des caseLink ou des dossiers
     */
    protected boolean retrieveIsCaseLink() {
        return getBooleanProperty(IS_CASE_LINK_PROPERTY, false);
    }

    /**
     * chargement ou non des caseLink
     */
    private boolean retrieveLoadCaseLink() {
        return getBooleanProperty(LOAD_CASE_LINK_PROPERTY, false);
    }

    public boolean retrieveKeepOrder() {
        return getBooleanProperty(KEEP_ORDER, false);
    }

    public int getCurrentEntryIndex() {
        return currentEntryIndex;
    }

    @Override
    protected void fillCurrentPageMapList(CoreSession coreSession) {
        currentItems = new ArrayList<>();

        if (query.contains(STContentViewConstant.DEFAULT_EMPTY_QUERY)) {
            // requete vide on fait rien
            resultsCount = 0;
            return;
        }

        if (!query.startsWith(FlexibleQueryMaker.KeyCode.UFXNQL.getKey())) {
            query = QueryUtils.ufnxqlToFnxqlQuery(query);
        }

        List<Object> params = getQueryParams();

        resultsCount = QueryUtils.doCountQuery(coreSession, query, params.toArray());

        if (resultsCount > 0) {
            // récupération page courante
            offset = PaginationHelper.calculeOffSet(offset, getPageSize(), resultsCount);

            List<String> ids = getIdsFromQuery(coreSession, params);

            if (CollectionUtils.isNotEmpty(ids)) {
                SSPrincipal principal = (SSPrincipal) coreSession.getPrincipal();
                boolean decretArriveReader = principal
                    .getBaseFunctionSet()
                    .contains(SolonEpgBaseFunctionConstant.DECRET_ARRIVE_READER);

                fillCurrentPage(coreSession, ids, decretArriveReader);
            }
        }
    }

    public void fillCurrentPage(CoreSession coreSession, List<String> ids, boolean decretArriveReader) {
        final STLockService stLockService = STServiceLocator.getSTLockService();

        boolean isCaseLink = retrieveIsCaseLink();

        if (isCaseLink) {
            fillCurrentPageFromCaseLinks(coreSession, ids, decretArriveReader, stLockService);
        } else {
            fillCurrentPageFromDossiers(coreSession, ids, decretArriveReader, stLockService);
        }
    }

    protected List<String> getIdsFromQuery(CoreSession coreSession, List<Object> params) {
        return QueryUtils.doQueryForIds(coreSession, query, getPageSize(), offset, params.toArray());
    }

    @Override
    protected void addParameter(StringBuilder query, String key, String mapKey, Serializable value, String prefix) {
        super.addParameter(query, key, mapKey, value, prefix);
        query.append(" AND d.dos:deleted=0 ");
    }
}
